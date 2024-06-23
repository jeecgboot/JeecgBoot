<template>
  <Select @dropdownVisibleChange="handleFetch" v-bind="attrs_" @change="handleChange" :options="getOptions" v-model:value="state">
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

  type OptionsItem = { label: string; value: string; disabled?: boolean };

  export default defineComponent({
    name: 'ApiSelect',
    components: {
      Select,
      LoadingOutlined,
    },
    inheritAttrs: false,
    props: {
      value: [Array, Object, String, Number],
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
      // update-begin--author:liaozhiyang---date:20240509---for：【issues/6191】apiSelect多次请求
      props.immediate && fetch();
      // update-end--author:liaozhiyang---date:20240509---for：【issues/6191】apiSelect多次请求

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
        options.value = [];
        try {
          loading.value = true;
          const res = await api(props.params);
          if (Array.isArray(res)) {
            options.value = res;
            emitChange();
            return;
          }
          if (props.resultField) {
            options.value = get(res, props.resultField) || [];
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
        if (value && typeof value === 'string' && value != 'null' && value != 'undefined') {
          state.value = value.split(',');
        }
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

      return { state, attrs_, attrs, getOptions, loading, t, handleFetch, handleChange };
    },
  });
</script>
