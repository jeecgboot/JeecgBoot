import type { FormProps, FormActionType, UseFormReturnType, FormSchema } from '../types/form';
import type { NamePath, ValidateOptions } from 'ant-design-vue/lib/form/interface';
import type { DynamicProps } from '/#/utils';
import { handleRangeValue } from '../utils/formUtils';
import { ref, onUnmounted, unref, nextTick, watch } from 'vue';
import { isProdMode } from '/@/utils/env';
import { error } from '/@/utils/log';
import { getDynamicProps, getValueType } from '/@/utils';
import { add } from "/@/components/Form/src/componentMap";
//集成online专用控件
import { OnlineSelectCascade, LinkTableCard, LinkTableSelect } from  '@jeecg/online';

export declare type ValidateFields = (nameList?: NamePath[], options?: ValidateOptions) => Promise<Recordable>;

type Props = Partial<DynamicProps<FormProps>>;

export function useForm(props?: Props): UseFormReturnType {
  const formRef = ref<Nullable<FormActionType>>(null);
  const loadedRef = ref<Nullable<boolean>>(false);

  //集成online专用控件
  add("OnlineSelectCascade", OnlineSelectCascade)
  add("LinkTableCard", LinkTableCard)
  add("LinkTableSelect", LinkTableSelect)
  
  async function getForm() {
    const form = unref(formRef);
    if (!form) {
      error('The form instance has not been obtained, please make sure that the form has been rendered when performing the form operation!');
    }
    await nextTick();
    return form as FormActionType;
  }

  function register(instance: FormActionType) {
    isProdMode() &&
      onUnmounted(() => {
        formRef.value = null;
        loadedRef.value = null;
      });
    if (unref(loadedRef) && isProdMode() && instance === unref(formRef)) return;

    formRef.value = instance;
    loadedRef.value = true;

    watch(
      () => props,
      () => {
        props && instance.setProps(getDynamicProps(props));
      },
      {
        immediate: true,
        deep: true,
      }
    );
  }

  const methods: FormActionType = {
    scrollToField: async (name: NamePath, options?: ScrollOptions | undefined) => {
      const form = await getForm();
      form.scrollToField(name, options);
    },
    setProps: async (formProps: Partial<FormProps>) => {
      const form = await getForm();
      form.setProps(formProps);
    },

    updateSchema: async (data: Partial<FormSchema> | Partial<FormSchema>[]) => {
      const form = await getForm();
      form.updateSchema(data);
    },

    resetSchema: async (data: Partial<FormSchema> | Partial<FormSchema>[]) => {
      const form = await getForm();
      form.resetSchema(data);
    },

    clearValidate: async (name?: string | string[]) => {
      const form = await getForm();
      form.clearValidate(name);
    },

    resetFields: async () => {
      getForm().then(async (form) => {
        await form.resetFields();
      });
    },

    removeSchemaByFiled: async (field: string | string[]) => {
      unref(formRef)?.removeSchemaByFiled(field);
    },

    // TODO promisify
    getFieldsValue: <T>() => {
      //update-begin-author:taoyan date:2022-7-5 for: VUEN-1341【流程】编码方式 流程节点编辑表单时，填写数据报错 包括用户组件、部门组件、省市区
      let values = unref(formRef)?.getFieldsValue() as T;
      if(values){
        Object.keys(values).map(key=>{
          if (values[key] instanceof Array) {
            // update-begin-author:sunjianlei date:20221205 for: 【issues/4330】判断如果是对象数组，则不拼接
            let isObject = typeof (values[key][0] || '') === 'object';
            if (!isObject) {
              values[key] = values[key].join(',');
            }
            // update-end-author:sunjianlei date:20221205 for: 【issues/4330】判断如果是对象数组，则不拼接
          }
        });
      }
      return values;
      //update-end-author:taoyan date:2022-7-5 for: VUEN-1341【流程】编码方式 流程节点编辑表单时，填写数据报错 包括用户组件、部门组件、省市区
    },

    setFieldsValue: async <T>(values: T) => {
      const form = await getForm();
      form.setFieldsValue<T>(values);
    },

    appendSchemaByField: async (schema: FormSchema, prefixField: string | undefined, first: boolean) => {
      const form = await getForm();
      form.appendSchemaByField(schema, prefixField, first);
    },

    submit: async (): Promise<any> => {
      const form = await getForm();
      return form.submit();
    },

    /**
     * 表单验证并返回表单值
     * @update:添加表单值转换逻辑
     * @updateBy:zyf
     * @updateDate:2021-09-02
     */
    validate: async (nameList?: NamePath[]): Promise<Recordable> => {
      const form = await getForm();
      let getProps = props || form.getProps;
      let values = form.validate(nameList).then((values) => {
        for (let key in values) {
          if (values[key] instanceof Array) {
            let valueType = getValueType(getProps, key);
            if (valueType === 'string') {
              values[key] = values[key].join(',');
            }
          }
        }
        //--@updateBy-begin----author:liusq---date:20210916------for:处理区域事件字典信息------
        return handleRangeValue(getProps, values);
        //--@updateBy-end----author:liusq---date:20210916------for:处理区域事件字典信息------
      });
      return values;
    },
    validateFields: async (nameList?: NamePath[], options?: ValidateOptions): Promise<Recordable> => {
      const form = await getForm();
      return form.validateFields(nameList, options);
    },
  };

  return [register, methods];
}
