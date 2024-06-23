import type { ComputedRef, Ref } from 'vue';
import type { FormSchema, FormActionType, FormProps } from '../types/form';

import { unref, nextTick, watchEffect } from 'vue';

interface UseAutoFocusContext {
  getSchema: ComputedRef<FormSchema[]>;
  getProps: ComputedRef<FormProps>;
  isInitedDefault: Ref<boolean>;
  formElRef: Ref<FormActionType>;
}
export async function useAutoFocus({ getSchema, getProps, formElRef, isInitedDefault }: UseAutoFocusContext) {
  watchEffect(async () => {
    if (unref(isInitedDefault) || !unref(getProps).autoFocusFirstItem) {
      return;
    }
    await nextTick();
    const schemas = unref(getSchema);
    const formEl = unref(formElRef);
    const el = (formEl as any)?.$el as HTMLElement;
    if (!formEl || !el || !schemas || schemas.length === 0) {
      return;
    }

    const firstItem = schemas[0];
    // Only open when the first form item is input type
    if (!firstItem.component.includes('Input')) {
      return;
    }

    const inputEl = el.querySelector('.ant-row:first-child input') as Nullable<HTMLInputElement>;
    if (!inputEl) return;
    inputEl?.focus();
  });
}
