import { isArray, isFunction, isObject, isString, isNullOrUnDef } from '/@/utils/is';
import { unref } from 'vue';
import type { Ref, ComputedRef } from 'vue';
import type { FormProps, FormSchema } from '../types/form';
import dayjs from "dayjs";
import { set } from 'lodash-es';
import { handleRangeValue } from '/@/components/Form/src/utils/formUtils';

interface UseFormValuesContext {
  defaultValueRef: Ref<any>;
  getSchema: ComputedRef<FormSchema[]>;
  getProps: ComputedRef<FormProps>;
  formModel: Recordable;
}
export function useFormValues({ defaultValueRef, getSchema, formModel, getProps }: UseFormValuesContext) {
  // Processing form values
  function handleFormValues(values: Recordable) {
    if (!isObject(values)) {
      return {};
    }
    const res: Recordable = {};
    for (const item of Object.entries(values)) {
      let [, value] = item;
      const [key] = item;
      if (!key || (isArray(value) && value.length === 0) || isFunction(value)) {
        continue;
      }
      const transformDateFunc = unref(getProps).transformDateFunc;
      if (isObject(value)) {
        value = transformDateFunc?.(value);
      }
      // 判断是否是dayjs实例
      if (isArray(value) && dayjs.isDayjs(value[0]) && dayjs.isDayjs(value[1])) {
        value = value.map((item) => transformDateFunc?.(item));
      }
      // Remove spaces
      if (isString(value)) {
        value = value.trim();
      }
      set(res, key, value);
    }
    return handleRangeValue(getProps, res);
  }

  function initDefault() {
    const schemas = unref(getSchema);
    const obj: Recordable = {};
    schemas.forEach((item) => {
      const { defaultValue } = item;
      if (!isNullOrUnDef(defaultValue)) {
        obj[item.field] = defaultValue;
        formModel[item.field] = defaultValue;
      }
    });
    defaultValueRef.value = obj;
  }

  return { handleFormValues, initDefault };
}
