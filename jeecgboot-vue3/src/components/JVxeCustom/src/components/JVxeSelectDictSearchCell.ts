import { computed, ref, watch, defineComponent, h } from 'vue';
import { cloneDeep, debounce } from 'lodash-es';
import { defHttp } from '/@/utils/http/axios';
import { filterDictText } from '/@/utils/dict/JDictSelectUtil';
import { ajaxGetDictItems, getDictItemsByCode } from '/@/utils/dict';
import { JVxeComponent } from '/@/components/jeecg/JVxeTable/types';
import { dispatchEvent } from '/@/components/jeecg/JVxeTable/utils';
import { useResolveComponent as rc } from '/@/components/jeecg/JVxeTable/hooks';
import { useJVxeComponent, useJVxeCompProps } from '/@/components/jeecg/JVxeTable/hooks';
import { useMessage } from '/@/hooks/web/useMessage';

/** value - label map，防止重复查询（刷新清空缓存） */
const LabelMap = new Map<string, any>();
// 请求id
let requestId = 0;

/** 显示组件，自带翻译 */
export const DictSearchSpanCell = defineComponent({
  name: 'JVxeSelectSearchSpanCell',
  props: useJVxeCompProps(),
  setup(props: JVxeComponent.Props) {
    const { innerOptions, innerSelectValue, innerValue } = useSelectDictSearch(props);
    return () => {
      return h('span', {}, [filterDictText(innerOptions.value, innerSelectValue.value || innerValue.value)]);
    };
  },
});

// 输入选择组件
export const DictSearchInputCell = defineComponent({
  name: 'JVxeSelectSearchInputCell',
  props: useJVxeCompProps(),
  setup(props: JVxeComponent.Props) {
    const { createMessage } = useMessage();
    const { dict, loading, isAsync, options, innerOptions, originColumn, cellProps, innerSelectValue, handleChangeCommon } =
      useSelectDictSearch(props);
    const hasRequest = ref(false);
    // 提示信息
    const tipsContent = computed(() => {
      return originColumn.value.tipsContent || '请输入搜索内容';
    });
    // 筛选函数
    const filterOption = computed(() => {
      if (isAsync.value) {
        //【jeecgboot-vue3/issues/I5QRT8】JVxeTypes.selectDictSearch sync问题
        return ()=>true;
      }
      return (input, option) => option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    });

    /** 加载数据 */
    const loadData = debounce((value) => {
      const currentRequestId = ++requestId;
      loading.value = true;
      innerOptions.value = [];
      if (value == null || value.trim() === '') {
        loading.value = false;
        hasRequest.value = false;
        return;
      }
      // 字典code格式：table,text,code
      hasRequest.value = true;
      loadDictByKeyword(dict.value, value)
        .then((res) => {
          if (currentRequestId !== requestId) {
            return;
          }
          let { success, result, message } = res;
          if (success) {
            innerOptions.value = result;
            result.forEach((item) => {
              LabelMap.set(item.value, [item]);
            });
          } else {
            createMessage.warning(message || '查询失败');
          }
        })
        .finally(() => {
          loading.value = false;
        });
    }, 300);

    function handleChange(selectedValue) {
      innerSelectValue.value = selectedValue;
      handleChangeCommon(innerSelectValue.value);
    }

    function handleSearch(value) {
      if (isAsync.value) {
        // 在输入时也应该开启加载，因为loadData加了消抖，所以会有800ms的用户主观上认为的卡顿时间
        loading.value = true;
        if (innerOptions.value.length > 0) {
          innerOptions.value = [];
        }
        loadData(value);
      }
    }

    function renderOptionItem() {
      let optionItems: any[] = [];
      options.value.forEach(({ value, text, label, title, disabled }) => {
        optionItems.push(
          h(
            rc('a-select-option'),
            {
              key: value,
              value: value,
              disabled: disabled,
            },
            {
              default: () => text || label || title,
            }
          )
        );
      });
      return optionItems;
    }

    return () => {
      return h(
        rc('a-select'),
        {
          ...cellProps.value,
          value: innerSelectValue.value,
          filterOption: filterOption.value,
          showSearch: true,
          allowClear: true,
          autofocus: true,
          defaultOpen: true,
          style: 'width: 100%',
          onSearch: handleSearch,
          onChange: handleChange,
        },
        {
          default: () => renderOptionItem(),
          notFoundContent: () => {
            if (loading.value) {
              return h(rc('a-spin'), { size: 'small' });
            } else if (hasRequest.value) {
              return h('div', '没有查询到任何数据');
            } else {
              return h('div', [tipsContent.value]);
            }
          },
        }
      );
    };
  },
  // 【组件增强】注释详见：JVxeComponent.Enhanced
  enhanced: {
    aopEvents: {
      editActived({ $event }) {
        dispatchEvent({
          $event,
          props: this.props,
          className: '.ant-select .ant-select-selection-search-input',
          isClick: false,
          handler: (el) => el.focus(),
        });
      },
    },
  } as JVxeComponent.EnhancedPartial,
});

function useSelectDictSearch(props) {
  const setup = useJVxeComponent(props);
  const { innerValue, originColumn } = setup;

  // 加载状态
  const loading = ref(false);
  // 内部选择值
  const innerSelectValue = ref(null);
  // 内部 options
  const innerOptions = ref<any[]>([]);

  const dict = computed(() => originColumn.value.dict);
  // 是否是异步模式
  const isAsync = computed(() => {
    let isAsync = originColumn.value.async;
    return isAsync != null && isAsync !== '' ? !!isAsync : true;
  });
  const options = computed(() => {
    if (isAsync.value) {
      return innerOptions.value;
    } else {
      return originColumn.value.options || [];
    }
  });

  /** 公共属性监听 */
  watch(
    innerValue,
    (value: string) => {
      if (value == null || value === '') {
        innerSelectValue.value = null;
      } else {
        loadDataByValue(value);
      }
    },
    { immediate: true }
  );
  watch(dict, () => loadDataByDict());

  // 根据 value 查询数据，用于回显
  async function loadDataByValue(value) {
    if (isAsync.value) {
      if (innerSelectValue.value !== value) {
        if (LabelMap.has(value)) {
          innerOptions.value = cloneDeep(LabelMap.get(value));
        } else {
          let result = await loadDictItem(dict.value, value);
          if (result && result.length > 0) {
            innerOptions.value = [{ value: value, text: result[0] }];
            LabelMap.set(value, cloneDeep(innerOptions.value));
          }
        }
      }
    }
    innerSelectValue.value = (value || '').toString();
  }

  // 初始化字典
  async function loadDataByDict() {
    if (!isAsync.value) {
      // 如果字典项集合有数据
      if (!originColumn.value.options || originColumn.value.options.length === 0) {
        // 根据字典Code, 初始化字典数组
        let dictStr = '';
        if (dict.value) {
          let arr = dict.value.split(',');
          if (arr[0].indexOf('where') > 0) {
            let tbInfo = arr[0].split('where');
            dictStr = tbInfo[0].trim() + ',' + arr[1] + ',' + arr[2] + ',' + encodeURIComponent(tbInfo[1]);
          } else {
            dictStr = dict.value;
          }
          if (dict.value.indexOf(',') === -1) {
            //优先从缓存中读取字典配置
            let cache = getDictItemsByCode(dict.value);
            if (cache) {
              innerOptions.value = cache;
              return;
            }
          }
          let { success, result } = await ajaxGetDictItems(dictStr, null);
          if (success) {
            innerOptions.value = result;
          }
        }
      }
    }
  }

  return {
    ...setup,
    loading,
    innerOptions,
    innerSelectValue,
    dict,
    isAsync,
    options,
  };
}

/** 获取字典项 */
function loadDictItem(dict: string, key: string) {
  return defHttp.get({
    url: `/sys/dict/loadDictItem/${dict}`,
    params: {
      key: key,
    },
  });
}

/** 根据关键字获取字典项（搜索） */
function loadDictByKeyword(dict: string, keyword: string) {
  return defHttp.get(
    {
      url: `/sys/dict/loadDict/${dict}`,
      params: {
        keyword: keyword,
      },
    },
    {
      isTransformResponse: false,
    }
  );
}
