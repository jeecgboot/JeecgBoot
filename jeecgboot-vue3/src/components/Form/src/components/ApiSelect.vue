<template>
  <Select
    v-bind="attrs_"
    v-model:value="state"
    :options="getOptions"
    @change="handleChange"
    @dropdownVisibleChange="handleFetch"
    @popupScroll="handlePopupScroll"
  >
    <template #[item]="data" v-for="item in Object.keys($slots)">
      <slot :name="item" v-bind="data || {}"></slot>
    </template>
    <template #suffixIcon v-if="loading">
      <LoadingOutlined spin />
    </template>
    <template #notFoundContent v-if="loading">
      <span>
        <LoadingOutlined spin class="mr-1" />
        {{ t('component.form.apiSelectNotFound') }}
      </span>
    </template>
  </Select>
</template>
<script lang="ts">
  import { defineComponent, PropType, ref, watchEffect, computed, unref, watch } from 'vue';
  import { Select } from 'ant-design-vue';
  import { isFunction } from '/@/utils/is';
  import { useRuleFormItem } from '/@/hooks/component/useFormItem';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { get, omit } from 'lodash-es';
  import { LoadingOutlined } from '@ant-design/icons-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { propTypes } from '/@/utils/propTypes';
  import { isNumber } from '/@/utils/is';

  type OptionsItem = { label: string; value: string; disabled?: boolean };
  //文档 https://help.jeecg.com/ui/apiSelect#pageconfig%E5%8F%82%E6%95%B0%E9%85%8D%E7%BD%AE
  export default defineComponent({
    name: 'ApiSelect',
    components: {
      Select,
      LoadingOutlined,
    },
    inheritAttrs: false,
    props: {
      value: [Array, String, Number],
      numberToString: propTypes.bool,
      api: {
        type: Function as PropType<(arg?: Recordable) => Promise<OptionsItem[]>>,
        default: null,
      },
      // api params
      params: {
        type: Object as PropType<Recordable>,
        default: () => ({}),
      },
      //分页配置
      pageConfig: {
        type: Object as PropType<Recordable>,
        default: () => ({ isPage: false }),
      },
      // support xxx.xxx.xx
      resultField: propTypes.string.def(''),
      labelField: propTypes.string.def('label'),
      valueField: propTypes.string.def('value'),
      immediate: propTypes.bool.def(true),
    },
    emits: ['options-change', 'change'],
    setup(props, { emit }) {
      const options = ref<OptionsItem[]>([]);
      const loading = ref(false);
      const isFirstLoad = ref(true);
      const emitData = ref<any[]>([]);
      const attrs = useAttrs();
      const { t } = useI18n();
      // update-begin--author:liusq---date:20250407---for：【QQYUN-11831】ApiSelect 分页下拉方案 #7883
      const hasMore = ref(true);
      const pagination = ref({
        pageNo: 1,
        pageSize: 10,
        total: 0,
      });
      const defPageConfig = { isPage: false, pageField: 'pageNo', pageSizeField: 'pageSize', totalField: 'total', listField: 'records' };
      // update-end--author:liusq---date:20250407---for：【QQYUN-11831】ApiSelect 分页下拉方案 #7883
      // Embedded in the form, just use the hook binding to perform form verification
      const [state, setState] = useRuleFormItem(props, 'value', 'change', emitData);
      // update-begin--author:liaozhiyang---date:20230830---for：【QQYUN-6308】解决警告
      let vModalValue: any;
      const attrs_ = computed(() => {
        let obj: any = unref(attrs) || {};
        if (obj && obj['onUpdate:value']) {
          vModalValue = obj['onUpdate:value'];
          delete obj['onUpdate:value'];
        }
        // update-begin--author:liaozhiyang---date:20231017---for：【issues/5467】ApiSelect修复覆盖了用户传递的方法
        if (obj['filterOption'] === undefined) {
          // update-begin--author:liaozhiyang---date:20230904---for：【issues/5305】无法按照预期进行搜索
          obj['filterOption'] = (inputValue, option) => {
            if (typeof option['label'] === 'string') {
              return option['label'].toLowerCase().indexOf(inputValue.toLowerCase()) != -1;
            } else {
              return true;
            }
          };
          // update-end--author:liaozhiyang---date:20230904---for：【issues/5305】无法按照预期进行搜索
        }
        // update-end--author:liaozhiyang---date:20231017---for：【issues/5467】ApiSelect修复覆盖了用户传递的方法
        return obj;
      });
      // update-begin--author:liaozhiyang---date:20230830---for：【QQYUN-6308】解决警告
      const getOptions = computed(() => {
        const { labelField, valueField, numberToString } = props;
        return unref(options).reduce((prev, next: Recordable) => {
          if (next) {
            const value = next[valueField];
            prev.push({
              ...omit(next, [labelField, valueField]),
              label: next[labelField],
              value: numberToString ? `${value}` : value,
            });
          }
          return prev;
        }, [] as OptionsItem[]);
      });
      // update-begin--author:liaozhiyang---date:20240823---for：【issues/6999】ApiSelect联动更新字段不生效（代码还原）
      watchEffect(() => {
        props.immediate && fetch();
      });
      // update-end--author:liaozhiyang---date:20240823---for：【issues/6999】ApiSelect联动更新字段不生效（代码还原）

      watch(
        () => props.params,
        () => {
          !unref(isFirstLoad) && fetch();
        },
        { deep: true }
      );
      //监听数值修改，查询数据
      watchEffect(() => {
        props.value && handleFetch();
      });

      async function fetch() {
        const api = props.api;
        if (!api || !isFunction(api)) return;
        // update-begin--author:liusq---date:20250407---for：【QQYUN-11831】ApiSelect 分页下拉方案 #7883
        if (!props.pageConfig.isPage || pagination.value.pageNo == 1) {
          options.value = [];
        }
        try {
          loading.value = true;
          let { isPage, pageField, pageSizeField, totalField, listField } = { ...defPageConfig, ...props.pageConfig };
          let params = isPage
            ? { ...props.params, [pageField]: pagination.value.pageNo, [pageSizeField]: pagination.value.pageSize }
            : { ...props.params };
          // update-end--author:liusq---date:20250407---for：【QQYUN-11831】ApiSelect 分页下拉方案 #7883
          const res = await api(params);
          if (isPage) {
            // update-begin--author:liusq---date:20250407---for：【QQYUN-11831】ApiSelect 分页下拉方案 #7883
            options.value = [...options.value, ...res[listField]];
            pagination.value.total = res[totalField] || 0;
            hasMore.value = res[totalField] ? options.value.length < res[totalField] : res[listField] < pagination.value.pageSize;
            // update-end--author:liusq---date:20250407---for：【QQYUN-11831】ApiSelect 分页下拉方案 #7883
          } else {
            if (Array.isArray(res)) {
              options.value = res;
              emitChange();
              return;
            }
            if (props.resultField) {
              options.value = get(res, props.resultField) || [];
            }
          }
          emitChange();
        } catch (error) {
          console.warn(error);
        } finally {
          loading.value = false;
          //--@updateBy-begin----author:liusq---date:20210914------for:判断选择模式，multiple多选情况下的value值空的情况下需要设置为数组------
          unref(attrs).mode == 'multiple' && !Array.isArray(unref(state)) && setState([]);
          //--@updateBy-end----author:liusq---date:20210914------for:判断选择模式，multiple多选情况下的value值空的情况下需要设置为数组------

          //update-begin---author:wangshuai ---date:20230505  for：初始化value值，如果是多选字符串的情况下显示不出来------------
          initValue();
          //update-end---author:wangshuai ---date:20230505  for：初始化value值，如果是多选字符串的情况下显示不出来------------
        }
      }

      function initValue() {
        let value = props.value;
        // update-begin--author:liaozhiyang---date:20250407---for：【issues/8037】初始化值单选的值被错误地写入数组值
        if (unref(attrs).mode == 'multiple') {
          if (value && typeof value === 'string' && value != 'null' && value != 'undefined') {
            state.value = value.split(',');
          } else if (isNumber(value)) {
            state.value = [value];
          }
        } else {
          state.value = value;
        }
        // update-end--author:liaozhiyang---date:20250407---for：【issues/8037】初始化值单选的值被错误地写入数组值
      }

      async function handleFetch() {
        if (!props.immediate && unref(isFirstLoad)) {
          await fetch();
          isFirstLoad.value = false;
        }
      }

      function emitChange() {
        emit('options-change', unref(getOptions));
      }

      function handleChange(_, ...args) {
        vModalValue && vModalValue(_);
        emitData.value = args;
      }
      // update-begin--author:liusq---date:20250407---for：【QQYUN-11831】ApiSelect 分页下拉方案 #7883
      // 滚动加载更多
      function handlePopupScroll(e) {
        const { scrollTop, scrollHeight, clientHeight } = e.target;
        const isNearBottom = scrollHeight - scrollTop <= clientHeight + 20;
        if (props.pageConfig.isPage && isNearBottom && hasMore.value && !loading.value) {
          pagination.value.pageNo += 1;
          fetch();
        }
      }
      // update-end--author:liusq---date:20250407---for：【QQYUN-11831】ApiSelect 分页下拉方案 #7883
      return { state, attrs_, attrs, getOptions, loading, t, handleFetch, handleChange, handlePopupScroll };
    },
  });
</script>
