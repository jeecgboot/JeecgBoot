<!--
 * @Description:It is troublesome to implement radio button group in the form. So it is extracted independently as a separate component
-->
<template>
  <RadioGroup v-bind="attrs" v-model:value="state" button-style="solid" @change="handleChange">
    <template v-for="item in getOptions" :key="`${item.value}`">
      <RadioButton v-if="props.isBtn" :value="item.value" :disabled="item.disabled">
        {{ item.label }}
      </RadioButton>
      <Radio v-else :value="item.value" :disabled="item.disabled">
        {{ item.label }}
      </Radio>
    </template>
  </RadioGroup>
</template>
<script lang="ts">
  import { defineComponent, PropType, ref, watchEffect, computed, unref, watch } from 'vue';
  import { Radio } from 'ant-design-vue';
  import { isFunction } from '/@/utils/is';
  import { useRuleFormItem } from '/@/hooks/component/useFormItem';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { propTypes } from '/@/utils/propTypes';
  import { get, omit } from 'lodash-es';
  import { useI18n } from '/@/hooks/web/useI18n';
  type OptionsItem = { label: string; value: string | number | boolean; disabled?: boolean };

  export default defineComponent({
    name: 'ApiRadioGroup',
    components: {
      RadioGroup: Radio.Group,
      RadioButton: Radio.Button,
      Radio,
    },
    props: {
      api: {
        type: Function as PropType<(arg?: Recordable | string) => Promise<OptionsItem[]>>,
        default: null,
      },
      params: {
        type: [Object, String] as PropType<Recordable | string>,
        default: () => ({}),
      },
      value: {
        type: [String, Number, Boolean] as PropType<string | number | boolean>,
      },
      isBtn: {
        type: [Boolean] as PropType<boolean>,
        default: false,
      },
      numberToString: propTypes.bool,
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
      const [state] = useRuleFormItem(props);

      // Processing options value
      const getOptions = computed(() => {
        const { labelField, valueField, numberToString } = props;

        return unref(options).reduce((prev, next: Recordable) => {
          if (next) {
            const value = next[valueField];
            prev.push({
              label: next[labelField],
              value: numberToString ? `${value}` : value,
              ...omit(next, [labelField, valueField]),
            });
          }
          return prev;
        }, [] as OptionsItem[]);
      });

      watchEffect(() => {
        props.immediate && fetch();
      });

      watch(
        () => props.params,
        () => {
          !unref(isFirstLoad) && fetch();
        },
        { deep: true }
      );

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
        }
      }

      function emitChange() {
        emit('options-change', unref(getOptions));
      }

      function handleChange(_, ...args) {
        emitData.value = args;
      }

      return { state, getOptions, attrs, loading, t, handleChange, props };
    },
  });
</script>
