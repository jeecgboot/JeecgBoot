<script lang="tsx">
  import type { CSSProperties } from 'vue';
  import type {
    FieldNames,
    TreeState,
    TreeItem,
    KeyType,
    CheckKeys,
    TreeActionType,
  } from './types/tree';

  import {
    defineComponent,
    reactive,
    computed,
    unref,
    ref,
    watchEffect,
    toRaw,
    watch,
    onMounted,
    nextTick,
  } from 'vue';
  import TreeHeader from './components/TreeHeader.vue';
  import { Tree, Spin, Empty } from 'ant-design-vue';
  import { TreeIcon } from './TreeIcon';
  import { ScrollContainer } from '/@/components/Container';
  import { omit, get, difference, cloneDeep } from 'lodash-es';
  import { isArray, isBoolean, isEmpty, isFunction } from '/@/utils/is';
  import { extendSlots, getSlot } from '/@/utils/helper/tsxHelper';
  import { filter, treeToList, eachTree } from '/@/utils/helper/treeHelper';
  import { useTree } from './hooks/useTree';
  import { useContextMenu } from '/@/hooks/web/useContextMenu';
  import { CreateContextOptions } from '/@/components/ContextMenu';
  import { treeEmits, treeProps } from './types/tree';
  import { createBEM } from '/@/utils/bem';

  export default defineComponent({
    name: 'BasicTree',
    inheritAttrs: false,
    props: treeProps,
    emits: treeEmits,
    setup(props, { attrs, slots, emit, expose }) {
      const [bem] = createBEM('tree');

      const state = reactive<TreeState>({
        checkStrictly: props.checkStrictly,
        expandedKeys: props.expandedKeys || [],
        selectedKeys: props.selectedKeys || [],
        checkedKeys: props.checkedKeys || [],
      });

      const searchState = reactive({
        startSearch: false,
        searchText: '',
        searchData: [] as TreeItem[],
      });

      const treeDataRef = ref<TreeItem[]>([]);

      const [createContextMenu] = useContextMenu();

      const getFieldNames = computed((): Required<FieldNames> => {
        const { fieldNames } = props;
        return {
          children: 'children',
          title: 'title',
          key: 'key',
          ...fieldNames,
        };
      });
      const treeRef = ref<any>(null);

      const getBindValues = computed(() => {
        let propsData = {
          blockNode: true,
          ...attrs,
          ...props,
          expandedKeys: state.expandedKeys,
          selectedKeys: state.selectedKeys,
          checkedKeys: state.checkedKeys,
          checkStrictly: state.checkStrictly,
          fieldNames: unref(getFieldNames),
          'onUpdate:expandedKeys': (v: KeyType[]) => {
            state.expandedKeys = v;
            emit('update:expandedKeys', v);
          },
          'onUpdate:selectedKeys': (v: KeyType[]) => {
            state.selectedKeys = v;
            emit('update:selectedKeys', v);
          },
          onCheck: (v: CheckKeys, e) => {
            handleCheck(v, e);
          },
          onRightClick: handleRightClick,
        };
        return omit(propsData, 'treeData', 'class');
      });
      /**
       * 2024-04-26
       * liaozhiyang
       * 【issues/1151】层级独立时勾选了父级，然后点击层级关联子级视觉上勾选了，但是保存子级没存上(把函数独立出来复用)
       * */
      const handleCheck = (v: CheckKeys, e?) => {
        let currentValue = toRaw(state.checkedKeys) as KeyType[];
        if (isArray(currentValue) && searchState.startSearch && e) {
          // update-begin-author:liusq---date:20230404--for: [issue/429]树搜索点击事件失效---
          const value = e.node.eventKey;
          currentValue = difference(currentValue, getChildrenKeys(value));
          if (e.checked) {
            currentValue.push(value);
          }
          // update-begin-author:liusq---date:20230404--for: [issue/429]树搜索点击事件失效---
          state.checkedKeys = currentValue;
        } else {
          state.checkedKeys = v;
        }
        const rawVal = toRaw(state.checkedKeys);
        emit('update:value', rawVal);
        emit('check', rawVal, e);
      };

      const getTreeData = computed((): TreeItem[] =>
        searchState.startSearch ? searchState.searchData : unref(treeDataRef),
      );

      const getNotFound = computed((): boolean => {
        return !getTreeData.value || getTreeData.value.length === 0;
      });

      const {
        deleteNodeByKey,
        insertNodeByKey,
        insertNodesByKey,
        filterByLevel,
        updateNodeByKey,
        getAllKeys,
        getChildrenKeys,
        getEnabledKeys,
        getSelectedNode,
      } = useTree(treeDataRef, getFieldNames);

      function getIcon(params: Recordable, icon?: string) {
        if (!icon) {
          if (props.renderIcon && isFunction(props.renderIcon)) {
            return props.renderIcon(params);
          }
        }
        return icon;
      }

      async function handleRightClick({ event, node }: Recordable) {
        const { rightMenuList: menuList = [], beforeRightClick } = props;
        let contextMenuOptions: CreateContextOptions = { event, items: [] };

        if (beforeRightClick && isFunction(beforeRightClick)) {
          let result = await beforeRightClick(node, event);
          if (Array.isArray(result)) {
            contextMenuOptions.items = result;
          } else {
            Object.assign(contextMenuOptions, result);
          }
        } else {
          contextMenuOptions.items = menuList;
        }
        if (!contextMenuOptions.items?.length) return;
        contextMenuOptions.items = contextMenuOptions.items.filter((item) => !item.hidden);
        createContextMenu(contextMenuOptions);
      }

      function setExpandedKeys(keys: KeyType[]) {
        state.expandedKeys = keys;
      }

      function getExpandedKeys() {
        return state.expandedKeys;
      }
      function setSelectedKeys(keys: KeyType[]) {
        state.selectedKeys = keys;
      }

      function getSelectedKeys() {
        return state.selectedKeys;
      }

      function setCheckedKeys(keys: CheckKeys) {
        state.checkedKeys = keys;
      }

      function getCheckedKeys() {
        return state.checkedKeys;
      }

      function checkAll(checkAll: boolean) {
        state.checkedKeys = checkAll ? getEnabledKeys() : ([] as KeyType[]);
      }

      function expandAll(expandAll: boolean) {
        state.expandedKeys = expandAll ? getAllKeys() : ([] as KeyType[]);
      }

      function onStrictlyChange(strictly: boolean) {
        state.checkStrictly = strictly;
      }

      watch(
        () => props.searchValue,
        (val) => {
          if (val !== searchState.searchText) {
            searchState.searchText = val;
          }
        },
        {
          immediate: true,
        },
      );

      watch(
        () => props.treeData,
        (val) => {
          if (val) {
            handleSearch(searchState.searchText);
          }
        },
      );

      function handleSearch(searchValue: string) {
        if (searchValue !== searchState.searchText) searchState.searchText = searchValue;
        emit('update:searchValue', searchValue);
        if (!searchValue) {
          searchState.startSearch = false;
          return;
        }
        const { filterFn, checkable, expandOnSearch, checkOnSearch, selectedOnSearch } =
          unref(props);
        searchState.startSearch = true;
        const { title: titleField, key: keyField } = unref(getFieldNames);

        const matchedKeys: string[] = [];
        searchState.searchData = filter(
          unref(treeDataRef),
          (node) => {
            const result = filterFn
              ? filterFn(searchValue, node, unref(getFieldNames))
              : node[titleField]?.includes(searchValue) ?? false;
            if (result) {
              matchedKeys.push(node[keyField]);
            }
            return result;
          },
          unref(getFieldNames),
        );

        if (expandOnSearch) {
          const expandKeys = treeToList(searchState.searchData).map((val) => {
            return val[keyField];
          });
          if (expandKeys && expandKeys.length) {
            setExpandedKeys(expandKeys);
          }
        }

        if (checkOnSearch && checkable && matchedKeys.length) {
          setCheckedKeys(matchedKeys);
        }

        if (selectedOnSearch && matchedKeys.length) {
          setSelectedKeys(matchedKeys);
        }
      }

      function handleClickNode(key: string, children: TreeItem[]) {
        if (!props.clickRowToExpand || !children || children.length === 0) return;
        if (!state.expandedKeys.includes(key)) {
          setExpandedKeys([...state.expandedKeys, key]);
        } else {
          const keys = [...state.expandedKeys];
          const index = keys.findIndex((item) => item === key);
          if (index !== -1) {
            keys.splice(index, 1);
          }
          setExpandedKeys(keys);
        }
      }

      watchEffect(() => {
        treeDataRef.value = props.treeData as TreeItem[];
      });

      onMounted(() => {
        const level = parseInt(props.defaultExpandLevel);
        if (level > 0) {
          state.expandedKeys = filterByLevel(level);
        } else if (props.defaultExpandAll) {
          expandAll(true);
        }
      });

      watchEffect(() => {
        state.expandedKeys = props.expandedKeys;
      });

      watchEffect(() => {
        state.selectedKeys = props.selectedKeys;
      });

      watchEffect(() => {
        state.checkedKeys = props.checkedKeys;
      });

      watch(
        () => props.value,
        () => {
          // update-end--author:liaozhiyang---date:20231122---for：【issues/863】关闭选择部门弹窗，再打开之前勾选的消失了
          state.checkedKeys = toRaw(props.value || props.checkedKeys || []);
          // update-end--author:liaozhiyang---date:20231122---for：【issues/863】关闭选择部门弹窗，再打开之前勾选的消失了
        },
        { immediate: true },
      );

      watch(
        () => state.checkedKeys,
        () => {
          const v = toRaw(state.checkedKeys);
          emit('update:value', v);
          emit('change', v);
        },
      );
      // update-begin--author:liaozhiyang---date:20240426---for：【issues/1151】层级独立时勾选了父级，然后点击层级关联子级视觉上勾选了，但是保存子级没存上
      watch(
        () => props.checkStrictly,
        () => {
          state.checkStrictly = props.checkStrictly;
          nextTick(() => {
            const value = treeRef.value?.checkedKeys;
            handleCheck([...value]);
          });
        }
      );
      // update-end--author:liaozhiyang---date:20240426---for：【issues/1151】层级独立时勾选了父级，然后点击层级关联子级视觉上勾选了，但是保存子级没存上

      const instance: TreeActionType = {
        setExpandedKeys,
        getExpandedKeys,
        setSelectedKeys,
        getSelectedKeys,
        setCheckedKeys,
        getCheckedKeys,
        insertNodeByKey,
        insertNodesByKey,
        deleteNodeByKey,
        updateNodeByKey,
        getSelectedNode,
        checkAll,
        expandAll,
        filterByLevel: (level: number) => {
          state.expandedKeys = filterByLevel(level);
        },
        setSearchValue: (value: string) => {
          handleSearch(value);
        },
        getSearchValue: () => {
          return searchState.searchText;
        },
      };

      function renderAction(node: TreeItem) {
        const { actionList } = props;
        if (!actionList || actionList.length === 0) return;
        return actionList.map((item, index) => {
          let nodeShow = true;
          if (isFunction(item.show)) {
            nodeShow = item.show?.(node);
          } else if (isBoolean(item.show)) {
            nodeShow = item.show;
          }

          if (!nodeShow) return null;

          return (
            <span key={index} class={bem('action')}>
              {item.render(node)}
            </span>
          );
        });
      }

      const treeData = computed(() => {
        const data = cloneDeep(getTreeData.value);
        eachTree(data, (item, _parent) => {
          const searchText = searchState.searchText;
          const { highlight } = unref(props);
          const {
            title: titleField,
            key: keyField,
            children: childrenField,
          } = unref(getFieldNames);

          const icon = getIcon(item, item.icon);
          const title = get(item, titleField);

          const searchIdx = searchText ? title.indexOf(searchText) : -1;
          const isHighlight =
            searchState.startSearch && !isEmpty(searchText) && highlight && searchIdx !== -1;
          const highlightStyle = `color: ${isBoolean(highlight) ? '#f50' : highlight}`;

          const titleDom = isHighlight ? (
            <span class={unref(getBindValues)?.blockNode ? `${bem('content')}` : ''}>
              <span>{title.substr(0, searchIdx)}</span>
              <span style={highlightStyle}>{searchText}</span>
              <span>{title.substr(searchIdx + (searchText as string).length)}</span>
            </span>
          ) : (
            title
          );
          item[titleField] = (
            <span
              class={`${bem('title')} pl-2`}
              onClick={handleClickNode.bind(null, item[keyField], item[childrenField])}
            >
              {slots?.title ? (
                getSlot(slots, 'title', item)
              ) : (
                <>
                  {icon && <TreeIcon icon={icon} />}
                  {titleDom}
                  <span class={bem('actions')}>{renderAction(item)}</span>
                </>
              )}
            </span>
          );
          return item;
        });
        return data;
      });

      expose(instance);

      return () => {
        const { title, helpMessage, toolbar, search, checkable } = props;
        const showTitle = title || toolbar || search || slots.headerTitle;
        const scrollStyle: CSSProperties = { height: 'calc(100% - 38px)' };
        return (
          <div class={[bem(), 'h-full', attrs.class]}>
            {showTitle && (
              <TreeHeader
                checkable={checkable}
                checkAll={checkAll}
                expandAll={expandAll}
                title={title}
                search={search}
                toolbar={toolbar}
                helpMessage={helpMessage}
                onStrictlyChange={onStrictlyChange}
                onSearch={handleSearch}
                onClickSearch={($event) => emit('search', $event)}
                searchText={searchState.searchText}
              >
                {extendSlots(slots)}
              </TreeHeader>
            )}
            <Spin spinning={unref(props.loading)} tip="加载中...">
              <ScrollContainer style={scrollStyle} v-show={!unref(getNotFound)}>
                <Tree ref={treeRef} {...unref(getBindValues)} showIcon={false} treeData={treeData.value} />
              </ScrollContainer>
              <Empty
                v-show={unref(getNotFound)}
                image={Empty.PRESENTED_IMAGE_SIMPLE}
                class="!mt-4"
              />
            </Spin>
          </div>
        );
      };
    },
  });
</script>
