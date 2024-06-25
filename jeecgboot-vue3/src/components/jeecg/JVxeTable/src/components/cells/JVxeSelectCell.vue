<template>
  <a-select :value="innerValue" v-bind="selectProps">
    <template v-if="loading" #notFoundContent>
      <LoadingOutlined />
      <span>&nbsp;加载中…</span>
    </template>
    <template v-for="option of selectOptions" :key="option.value">
      <a-select-option :value="option.value" :title="option.text || option.label || option.title" :disabled="option.disabled">
        <span>{{ option.text || option.label || option.title || option.value }}</span>
      </a-select-option>
    </template>
  </a-select>
</template>

<script lang="ts">
  import { ref, computed, defineComponent } from 'vue';
  import { LoadingOutlined } from '@ant-design/icons-vue';
  import { filterDictText } from '/@/utils/dict/JDictSelectUtil';
  import { JVxeComponent, JVxeTypes } from '/@/components/jeecg/JVxeTable/types';
  import { useJVxeComponent, useJVxeCompProps } from '/@/components/jeecg/JVxeTable/hooks';
  import { dispatchEvent } from '/@/components/jeecg/JVxeTable/utils';
  import { isPromise } from '/@/utils/is';

  export default defineComponent({
    name: 'JVxeSelectCell',
    components: { LoadingOutlined },
    props: useJVxeCompProps(),
    setup(props: JVxeComponent.Props) {
      const { innerValue, cellProps, row, originColumn, scrolling, handleChangeCommon, handleBlurCommon } = useJVxeComponent(props);
      const loading = ref(false);
      // 异步加载的options（用于多级联动）
      const asyncOptions = ref<any[] | null>(null);
      // 下拉框 props
      const selectProps = computed(() => {
        let selProps = {
          ...cellProps.value,
          allowClear: true,
          autofocus: true,
          defaultOpen: !scrolling.value,
          style: { width: '100%' },
          filterOption: handleSelectFilterOption,
          onBlur: handleBlur,
          onChange: handleChange,
        };
        // 判断select是否允许输入
        let { allowSearch, allowInput } = originColumn.value;
        if (allowInput === true || allowSearch === true) {
          selProps['showSearch'] = true;
          selProps['onSearch'] = handleSearchSelect;
        }
        return selProps;
      });
      // 下拉选项
      const selectOptions = computed(() => {
        if (asyncOptions.value) {
          return asyncOptions.value;
        }
        let { linkage } = props.renderOptions;
        if (linkage) {
          let { getLinkageOptionsSibling, config } = linkage;
          let res = getLinkageOptionsSibling(row.value, originColumn.value, config, true);
          // 当返回Promise时，说明是多级联动
          if (res instanceof Promise) {
            loading.value = true;
            res
              .then((opt) => {
                asyncOptions.value = opt;
                loading.value = false;
              })
              .catch((e) => {
                console.error(e);
                loading.value = false;
              });
          } else {
            asyncOptions.value = null;
            return res;
          }
        }
        return originColumn.value.options;
      });

      // --------- created ---------

      // 多选、搜索type
      let multipleTypes = [JVxeTypes.selectMultiple, 'list_multi'];
      let searchTypes = [JVxeTypes.selectSearch, 'sel_search'];
      if (multipleTypes.includes(props.type)) {
        // 处理多选
        let props = originColumn.value.props || {};
        props['mode'] = 'multiple';
        props['maxTagCount'] = 1;
        //update-begin-author:taoyan date:2022-12-5 for: issues/271 Online表单主子表单下拉多选无法搜索
        originColumn.value.allowSearch = true;
        //update-end-author:taoyan date:2022-12-5 for: issues/271 Online表单主子表单下拉多选无法搜索
        originColumn.value.props = props;
      } else if (searchTypes.includes(props.type)) {
        // 处理搜索
        originColumn.value.allowSearch = true;
      }

      /** 处理 change 事件 */
      function handleChange(value) {
        // 处理下级联动
        let linkage = props.renderOptions.linkage;
        if (linkage) {
          linkage.handleLinkageSelectChange(row.value, originColumn.value, linkage.config, value);
        }
        handleChangeCommon(value);
      }

      /** 处理blur失去焦点事件 */
      function handleBlur(value) {
        let { allowInput, options } = originColumn.value;
        if (allowInput === true) {
          // 删除无用的因搜索（用户输入）而创建的项
          if (typeof value === 'string') {
            let indexes: number[] = [];
            options.forEach((option, index) => {
              if (option.value.toLocaleString() === value.toLocaleString()) {
                delete option.searchAdd;
              } else if (option.searchAdd === true) {
                indexes.push(index);
              }
            });
            // 翻转删除数组中的项
            for (let index of indexes.reverse()) {
              options.splice(index, 1);
            }
          }
        }
        handleBlurCommon(value);
      }

      /** 用于搜索下拉框中的内容 */
      function handleSelectFilterOption(input, option) {
      
        let { allowSearch, allowInput } = originColumn.value;
        if (allowSearch === true || allowInput === true) {
          // update-begin--author:liaozhiyang---date:20240321---for：【QQYUN-5806】js增强改变下拉搜索options (防止option.title为null报错)
          if (option.title == null) return false;
          // update-begin--author:liaozhiyang---date:20240321---for：【QQYUN-5806】js增强改变下拉搜索options (防止option.title为null报错)
          // update-begin--author:liaozhiyang---date:20230904---for：【issues/5305】JVxeTypes.select 无法按照预期进行搜索
          return option.title.toLowerCase().indexOf(input.toLowerCase()) >= 0;
          // update-begin--author:liaozhiyang---date:20230904---for：【issues/5305】JVxeTypes.select 无法按照预期进行搜索
        }
        return true;
      }

      /** select 搜索时的事件，用于动态添加options */
      function handleSearchSelect(value) {
        let { allowSearch, allowInput, options } = originColumn.value;

        if (allowSearch !== true && allowInput === true) {
          // 是否找到了对应的项，找不到则添加这一项
          let flag = false;
          for (let option of options) {
            if (option.value.toLocaleString() === value.toLocaleString()) {
              flag = true;
              break;
            }
          }
          // !!value ：不添加空值
          if (!flag && !!value) {
            // searchAdd 是否是通过搜索添加的
            options.push({ title: value, value: value, searchAdd: true });
          }
        }
      }

      return {
        loading,
        innerValue,
        selectProps,
        selectOptions,
        handleChange,
        handleBlur,
      };
    },
    // 【组件增强】注释详见：JVxeComponent.Enhanced
    enhanced: {
      aopEvents: {
        editActived({ $event, row, column }) {
          dispatchEvent({
            $event,
            row,
            column,
            props: this.props,
            instance: this,
            className: '.ant-select .ant-select-selection-search-input',
            isClick: false,
            handler: (el) => el.focus(),
          });
        },
      },
      translate: {
        enabled: true,
        async handler(value, ctx) {
          let { props, context } = ctx!;
          let { row, originColumn } = context;
          let options;
          let linkage = props?.renderOptions.linkage;
          // 判断是否是多级联动，如果是就通过接口异步翻译
          if (linkage) {
            let { getLinkageOptionsSibling, config } = linkage;
            let linkageOptions = getLinkageOptionsSibling(row.value, originColumn.value, config, true);
            options = isPromise(linkageOptions) ? await linkageOptions : linkageOptions;
          } else if (isPromise(originColumn.value.optionsPromise)) {
            options = await originColumn.value.optionsPromise;
          } else {
            options = originColumn.value.options;
          }
          return filterDictText(options, value);
        },
      },
      getValue(value) {
        if (Array.isArray(value)) {
          return value.join(',');
        } else {
          return value;
        }
      },
      setValue(value, ctx) {
        let { context } = ctx!;
        let { originColumn } = context;
        // 判断是否是多选
        if ((originColumn.value.props || {})['mode'] === 'multiple') {
          originColumn.value.props['maxTagCount'] = 1;
        }
        if (value != null && value !== '') {
          if (typeof value === 'string') {
            return value === '' ? [] : value.split(',');
          }
          return value;
        } else {
          return undefined;
        }
      },
    } as JVxeComponent.EnhancedPartial,
  });
</script>
