import type { Ref } from 'vue';
import { inject, reactive, ref, computed, unref, watch, nextTick } from 'vue';
import { TreeActionType } from '/@/components/Tree';
import { listToTree } from '/@/utils/common/compUtils';
import { isEqual } from 'lodash-es';
import { defHttp } from "@/utils/http/axios";
import { queryAllParentId } from "/@/api/common/api";

export function useTreeBiz(treeRef, getList, props, realProps, emit) {
  //接收下拉框选项
  const selectOptions = inject('selectOptions', ref<Array<object>>([]));
  //接收已选择的值
  const selectValues = <object>inject('selectValues', reactive({}));
  // 是否正在加载回显
  const loadingEcho = inject<Ref<boolean>>('loadingEcho', ref(false));
  //数据集
  const treeData = ref<Array<object>>([]);
  //已选择的值
  const checkedKeys = ref<Array<string | number>>([]);
  //选则的行记录
  const selectRows = ref<Array<object>>([]);
  //是否是打开弹框模式
  const openModal = ref(false);
  //是否层级关联
  const checkStrictly = ref<boolean>(realProps.multiple ? props.checkStrictly : true);
  // 是否开启父子关联，如果不可以多选，就始终取消父子关联
  const getCheckStrictly = computed(() => checkStrictly.value);
  // 是否是首次加载回显，只有首次加载，才会显示 loading
  let isFirstLoadEcho = true;
  let prevSelectValues = [];
  // 需要展开的父节点ID列表
  const expandedKeys = ref<Array<string | number>>([]);
  // 是否启用自动展开功能（可以通过props控制）
  const enableAutoExpand = props.enableAutoExpand !== false;
  /**
   * 监听selectValues变化
   */
  watch(
    selectValues,
    ({ value: values }: Recordable) => {
      if(!values){
        return;
      }
      // 代码逻辑说明: 【issues/8232】代码设置JSelectDept组件值没翻译
      if (values.length > 0) {
        // 防止多次请求
        if (isEqual(values, prevSelectValues)) return;
        prevSelectValues = values;
        loadingEcho.value = isFirstLoadEcho;
        isFirstLoadEcho = false;
        onLoadData(null, values.join(',')).finally(() => {
          loadingEcho.value = false;
        });
      }
    },
    { immediate: true }
  );

  /**
   * 获取树实例
   */
  function getTree() {
    const tree = unref(treeRef);
    if (!tree) {
      //throw new Error('tree is null!');
      return null;
    }
    return tree;
  }

  /**
   * 获取需要展开的父节点ID
   */
  async function getParentIdsToExpand(selectedIds) {
    if (!selectedIds || selectedIds.length === 0) return [];
    
    try {
      const result = await queryAllParentId({ 
        departId: selectedIds.join(','),
        orgCode: props.params?.orgCode 
      });
      
      if (result) {
        const allParentIds = [];
        // 处理 Map 或 Object 结构
        const valuesToProcess = result instanceof Map 
          ? Array.from(result.values()) 
          : Object.values(result);
        
        // 遍历所有选中节点的父节点
        valuesToProcess.forEach((nodeData: any) => {
          if (nodeData && nodeData.parentIds && Array.isArray(nodeData.parentIds)) {
            // 添加父节点ID（不包含选中节点本身）
            const parentIds = nodeData.parentIds.filter(id => !selectedIds.includes(id));
            allParentIds.push(...parentIds);
          }
        });
        
        return [...new Set(allParentIds)]; // 去重
      }
      return [];
    } catch (error) {
      console.warn('获取父节点ID失败:', error);
      return [];
    }
  }

  /**
   * 设置树展开级别
   */
  function expandTree() {
    nextTick(() => {
      if (props.defaultExpandLevel && props.defaultExpandLevel > 0) {
        getTree().filterByLevel(props.defaultExpandLevel);
      }
      //设置列表默认选中
      checkedKeys.value = selectValues['value'];
      
      // 如果有需要展开的父节点，则展开它们
      if (expandedKeys.value.length > 0) {
        getTree().setExpandedKeys(expandedKeys.value);
      }
    }).then(() => {
      // 再次确保展开，因为树可能还没有完全渲染
      if (expandedKeys.value.length > 0) {
        setTimeout(() => {
          getTree().setExpandedKeys(expandedKeys.value);
        }, 100);
      }
    });
  }

  /**
   * 树节点选择
   */
  function onSelect(keys, info) {
    if (props.checkable == false) {
      checkedKeys.value = props.checkStrictly ? keys.checked : keys;
      const { selectedNodes } = info;
      let rows = <any[]>[];
      selectedNodes.forEach((item) => {
        rows.push(item);
      });
      selectRows.value = rows;
    }
  }

  /**
   * 树节点选择
   */
  function onCheck(keys, info) {
    if(!info){
      return;
    }
    if (props.checkable == true) {
      // 如果不能多选，就只保留最后一个选中的
      if (!realProps.multiple) {
        if (info.checked) {
          // 代码逻辑说明: 单选模式下，设定rowKey，无法选中数据-
          checkedKeys.value = [info.node.eventKey];
          let rowKey = props.rowKey;
          let temp = info.checkedNodes.find((n) => n[rowKey] === info.node.eventKey);
          selectRows.value = [temp];
        } else {
          checkedKeys.value = [];
          selectRows.value = [];
        }
        return;
      }
      // 代码逻辑说明: 【JHHB-250】选择部门加一个层级关联/独立的配置，现在是点击就全勾选了---
      checkedKeys.value = checkStrictly.value ? keys.checked : keys;
      const { checkedNodes } = info;
      let rows = <any[]>[];
      checkedNodes.forEach((item) => {
        rows.push(item);
      });
      selectRows.value = rows;
    }
  }

  /**
   * 勾选全部
   */
  async function checkALL(checkAll) {
    getTree().checkAll(checkAll);
    // 代码逻辑说明: 【issues/394】所属部门树操作全部勾选不生效/【issues/4646】部门全部勾选后，点击确认按钮，部门信息丢失------------
    await nextTick();
    checkedKeys.value = getTree().getCheckedKeys();
    if(checkAll){
      getTreeRow();
    }else{
      selectRows.value = [];
    }
  }

  /**
   * 获取数列表
   * @param res
   */
  function getTreeRow() {
    let ids = "";
    if(unref(checkedKeys).length>0){
      ids = checkedKeys.value.join(",");
    }
    getList({ids:ids}).then((res) =>{
      selectRows.value = res;
    })
  }

  /**
   * 展开全部
   */
  function expandAll(expandAll) {
    getTree().expandAll(expandAll);
  }

  /**
   * 加载树数据
   */
  async function onLoadData(treeNode, ids) {
    let params = {};
    let startPid = '';
    if (treeNode) {
      startPid = treeNode.eventKey;
      // 代码逻辑说明: rowkey不设置成id，sync开启异步的时候，点击上级下级不显示------------
      params['pid'] = treeNode.value;
    }
    if (ids) {
      startPid = '';
      params['ids'] = ids;
    }

    if(props.params?.departIds){
      params['departIds'] = props.params.departIds;
    }
    let record = await getList(params);
    let optionData = record;
    //只展示公司信息（公司+子公司）
    if(props.onlyShowCompany){
      record = getCompanyData(record)
    }
    //是否只选择部门岗位
    if (props.izOnlySelectDepartPost) {
      setCompanyDepartCheckable(record);
    }
    if (!props.serverTreeData) {
      //前端处理数据为tree结构
      record = listToTree(record, props, startPid);
      if (record.length == 0 && treeNode) {
        checkHasChild(startPid, treeData.value);
      }
    }

    if (openModal.value == true) {
      //弹框模式下加载全部数据
      if (!treeNode) {
        treeData.value = record;
      } else {
        return new Promise((resolve: (value?: unknown) => void) => {
          if (!treeNode.children) {
            resolve();
            return;
          }
          const asyncTreeAction: TreeActionType | null = unref(treeRef);
          if (asyncTreeAction) {
            asyncTreeAction.updateNodeByKey(treeNode.eventKey, { children: record });
            asyncTreeAction.setExpandedKeys([treeNode.eventKey, ...asyncTreeAction.getExpandedKeys()]);
          }
          resolve();
          return;
        });
      }
      expandTree();
    } else {
      const options = <any[]>[];
      optionData.forEach((item) => {
        // 代码逻辑说明: issues/I5F3P4 online配置部门选择后编辑，查看数据应该显示部门名称，不是部门代码
        options.push({ label: item[props.labelKey], value: item[props.rowKey] });
      });
      selectOptions.value = options;
    }
  }

  /**
   * 获取到公司/子公司数据
   * @param record
   */
  function getCompanyData(record){
    const companyData = record.filter(item=>item.orgCategory && ['1','4'].includes(item.orgCategory));
    return companyData
  }
  /**
   * 异步加载时检测是否含有下级节点
   * @param pid 父节点
   * @param treeArray  tree数据
   */
  function checkHasChild(pid, treeArray) {
    if (treeArray && treeArray.length > 0) {
      for (let item of treeArray) {
        if (item.key == pid) {
          if (!item.child) {
            item.isLeaf = true;
          }
          break;
        } else {
          checkHasChild(pid, item.children);
        }
      }
    }
  }

  /**
   * 获取已选择数据
   */
  function getSelectTreeData(success) {
    const options = <any[]>[];
    const values = <any[]>[];
    selectRows.value.forEach((item) => {
      options.push({ label: item[props.labelKey], value: item[props.rowKey] });
    });
    checkedKeys.value.forEach((item) => {
      values.push(item);
    });
    selectOptions.value = options;
    success && success(options, values);
  }

  /**
   * 弹出框显示隐藏触发事件
   */
  async function visibleChange(visible) {
    if (visible) {
      //弹出框打开时加载全部数据
      openModal.value = true;
      await onLoadData(null, null);
      
      // 在数据加载完成后，如果有选中的值且启用了自动展开功能，则展开父节点
      if (enableAutoExpand && selectValues.value && selectValues.value.length > 0) {
        try {
          const selectedIds = selectValues.value;
          const parentIds = await getParentIdsToExpand(selectedIds);
          
          if (parentIds.length > 0) {
            expandedKeys.value = parentIds;
            
            // 延迟展开，确保树已经渲染完成
            nextTick(() => {
              try {
                const tree = getTree();
                if (tree) {
                  tree.setExpandedKeys(parentIds);

                  // 再次确保展开
                  setTimeout(() => {
                    try {
                      const tree = getTree();
                      if (tree) {
                        tree.setExpandedKeys(parentIds);
                        console.log('父节点已展开:', parentIds);
                        // 第三次确保展开，使用更长的延迟
                        setTimeout(() => {
                          try {
                            const tree = getTree();
                            if (tree) {
                              tree.setExpandedKeys(parentIds);
                            }
                          } catch (error) {
                            console.warn('展开父节点失败:', error);
                          }
                        }, 500);
                      }
                    } catch (error) {
                      console.warn('展开父节点失败:', error);
                    }
                  }, 200);
                }
              } catch (error) {
                console.warn('展开父节点失败:', error);
              }
            });
            
          }
        } catch (error) {
          console.warn('获取父节点ID失败:', error);
        }
      }
    } else {
      openModal.value = false;
      emit?.('close');
    }
  }

  /**
   * 设置公司部门复选框显示
   * @param record
   */
  function setCompanyDepartCheckable(record) {
    if (record && record.length > 0) {
      for (const item of record) {
        if (item.orgCategory !== '3') {
          item.checkable = false;
          item.selectable = false;
        } else {
          item.checkable = true;
          item.selectable = true;
        }
        if (item.isLeaf) {
          setCompanyDepartCheckable(item.children);
        }
      }
    }
  }

  /**
   * 岗位搜索
   *
   * @param value
   */
  async function onSearch(value) {
    if(value){
      let result = await defHttp.get({ url: "/sys/sysDepart/searchBy", params: { keyWord: value, orgCategory: "3",...props.params } });
      if (Array.isArray(result)) {
        treeData.value = result;
      } else {
        treeData.value = [];
      }
    } else {
      treeData.value = [];
      await onLoadData(null, null)
    }
  }

  return [
    {
      visibleChange,
      selectOptions,
      selectValues,
      onLoadData,
      onCheck,
      onSelect,
      checkALL,
      expandAll,
      checkedKeys,
      selectRows,
      treeData,
      getCheckStrictly,
      getSelectTreeData,
      onSearch,
      expandedKeys,
      checkStrictly,
    },
  ];
}
