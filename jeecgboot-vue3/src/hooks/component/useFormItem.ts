import type { UnwrapRef, Ref, WritableComputedRef, DeepReadonly } from 'vue';
import { reactive, readonly, computed, getCurrentInstance, watchEffect, unref, nextTick, toRaw } from 'vue';
import { Form } from 'ant-design-vue';
import { FormItemContext } from 'ant-design-vue/es/form/FormItemContext';

import { isEqual } from 'lodash-es';
export function useRuleFormItem<T extends Recordable, K extends keyof T, V = UnwrapRef<T[K]>>(
  props: T,
  key?: K,
  changeEvent?,
  emitData?: Ref<any[] | undefined>
): [WritableComputedRef<V>, (val: V) => void, DeepReadonly<V>, FormItemContext];
export function useRuleFormItem<T extends Recordable>(props: T, key: keyof T = 'value', changeEvent = 'change', emitData?: Ref<any[]>) {
  const instance = getCurrentInstance();
  const emit = instance?.emit;
  const formItemContext = Form.useInjectFormItemContext();

  const innerState = reactive({
    value: props[key],
  });

  const defaultState = readonly(innerState);

  const setState = (val: UnwrapRef<T[keyof T]>): void => {
    innerState.value = val as T[keyof T];
  };

  watchEffect(() => {
    innerState.value = props[key];
  });

  const state: any = computed({
    get() {
      //修复多选时空值显示问题（兼容值为0的情况）
      return innerState.value == null || innerState.value === '' ? [] : innerState.value;
    },
    set(value) {
      if (isEqual(value, defaultState.value)) return;

      innerState.value = value as T[keyof T];
      nextTick(() => {
        emit?.(changeEvent, value, ...(toRaw(unref(emitData)) || []));
        // https://antdv.com/docs/vue/migration-v3-cn
        // antDv3升级后需要调用这个方法更新校验的值
        nextTick(() => formItemContext.onFieldChange());
      });
    },
  });

  return [state, setState, defaultState, formItemContext];
}
