<template>
  <Form v-bind="getBindValue" :class="getFormClass" ref="formElRef" :model="formModel" @keypress.enter="handleEnterPress">
    <Row v-bind="getRow">
      <slot name="formHeader"></slot>
      <template v-for="schema in getSchema" :key="schema.field">
        <FormItem
          :tableAction="tableAction"
          :formActionType="formActionType"
          :schema="schema"
          :formProps="getProps"
          :allDefaultValues="defaultValueRef"
          :formModel="formModel"
          :setFormModel="setFormModel"
          :validateFields="validateFields"
          :clearValidate="clearValidate"
          v-auth="schema.auth"
        >
          <template #[item]="data" v-for="item in Object.keys($slots)">
            <slot :name="item" v-bind="data || {}"></slot>
          </template>
        </FormItem>
      </template>

      <FormAction v-bind="getFormActionBindProps" @toggle-advanced="handleToggleAdvanced">
        <template #[item]="data" v-for="item in ['resetBefore', 'submitBefore', 'advanceBefore', 'advanceAfter']">
          <slot :name="item" v-bind="data || {}"></slot>
        </template>
      </FormAction>
      <slot name="formFooter"></slot>
    </Row>
  </Form>
</template>
<script lang="ts">
  import type { FormActionType, FormProps, FormSchema } from './types/form';
  import type { AdvanceState } from './types/hooks';
  import type { Ref } from 'vue';

  import { defineComponent, reactive, ref, computed, unref, onMounted, watch, nextTick } from 'vue';
  import { Form, Row } from 'ant-design-vue';
  import FormItem from './components/FormItem.vue';
  import FormAction from './components/FormAction.vue';

  import { dateItemType } from './helper';
  import { dateUtil } from '/@/utils/dateUtil';

  // import { cloneDeep } from 'lodash-es';
  import { deepMerge } from '/@/utils';

  import { useFormValues } from './hooks/useFormValues';
  import useAdvanced from './hooks/useAdvanced';
  import { useFormEvents } from './hooks/useFormEvents';
  import { createFormContext } from './hooks/useFormContext';
  import { useAutoFocus } from './hooks/useAutoFocus';
  import { useModalContext } from '/@/components/Modal';

  import { basicProps } from './props';
  import componentSetting from '/@/settings/componentSetting';

  import { useDesign } from '/@/hooks/web/useDesign';
  import dayjs from 'dayjs';
  import { useDebounceFn } from '@vueuse/core';

  export default defineComponent({
    name: 'BasicForm',
    components: { FormItem, Form, Row, FormAction },
    props: basicProps,
    emits: ['advanced-change', 'reset', 'submit', 'register'],
    setup(props, { emit, attrs }) {
      const formModel = reactive<Recordable>({});
      const modalFn = useModalContext();

      const advanceState = reactive<AdvanceState>({
        // 默认是收起状态
        isAdvanced: false,
        hideAdvanceBtn: true,
        isLoad: false,
        actionSpan: 6,
      });

      const defaultValueRef = ref<Recordable>({});
      const isInitedDefaultRef = ref(false);
      const propsRef = ref<Partial<FormProps>>({});
      const schemaRef = ref<Nullable<FormSchema[]>>(null);
      const formElRef = ref<Nullable<FormActionType>>(null);

      const { prefixCls } = useDesign('basic-form');

      // Get the basic configuration of the form
      const getProps = computed((): FormProps => {
        let mergeProps = { ...props, ...unref(propsRef) } as FormProps;
        //update-begin-author:sunjianlei date:20220923 for: 如果用户设置了labelWidth，则使labelCol失效，解决labelWidth设置无效的问题
        if (mergeProps.labelWidth) {
          mergeProps.labelCol = undefined;
        }
        //update-end-author:sunjianlei date:20220923 for: 如果用户设置了labelWidth，则使labelCol失效，解决labelWidth设置无效的问题
        // update-begin--author:liaozhiyang---date:20231017---for：【QQYUN-6566】BasicForm支持一行显示(inline)
        if (mergeProps.layout === 'inline') {
          if (mergeProps.labelCol === componentSetting.form.labelCol) {
            mergeProps.labelCol = undefined;
          }
          if (mergeProps.wrapperCol === componentSetting.form.wrapperCol) {
            mergeProps.wrapperCol = undefined;
          }
        }
        // update-end--author:liaozhiyang---date:20231017---for：【QQYUN-6566】BasicForm支持一行显示(inline)
        return mergeProps;
      });

      const getFormClass = computed(() => {
        return [
          prefixCls,
          {
            [`${prefixCls}--compact`]: unref(getProps).compact,
            'jeecg-form-detail-effect': unref(getProps).disabled
          },
        ];
      });

      // Get uniform row style and Row configuration for the entire form
      const getRow = computed((): Recordable => {
        const { baseRowStyle = {}, rowProps } = unref(getProps);
        return {
          style: baseRowStyle,
          ...rowProps,
        };
      });

      const getBindValue = computed(() => ({ ...attrs, ...props, ...unref(getProps) } as Recordable));

      const getSchema = computed((): FormSchema[] => {
        const schemas: FormSchema[] = unref(schemaRef) || (unref(getProps).schemas as any);
        for (const schema of schemas) {
          const { defaultValue, component, componentProps } = schema;
          // handle date type
          if (defaultValue && dateItemType.includes(component)) {
            //update-begin---author:wangshuai ---date:20230410  for：【issues/435】代码生成的日期控件赋默认值报错------------
            let valueFormat:string = "";
            if(componentProps){
              valueFormat = componentProps?.valueFormat;
            }
            if(!valueFormat){
              console.warn("未配置valueFormat,可能导致格式化错误！");
            }
            //update-end---author:wangshuai ---date:20230410  for：【issues/435】代码生成的日期控件赋默认值报错------------
            if (!Array.isArray(defaultValue)) {
              //update-begin---author:wangshuai ---date:20221124  for：[issues/215]列表页查询框（日期选择框）设置初始时间，一进入页面时，后台报日期转换类型错误的------------
              if(valueFormat){
                // schema.defaultValue = dateUtil(defaultValue).format(valueFormat);
                // update-begin--author:liaozhiyang---date:20240529---for：【TV360X-346 】时间组件填写默认值有问题
                schema.defaultValue = dateUtil(defaultValue, valueFormat).format(valueFormat);
                // update-end--author:liaozhiyang---date:20240529---for：【TV360X-346 】时间组件填写默认值有问题
              }else{
                schema.defaultValue = dateUtil(defaultValue);
              }
              //update-end---author:wangshuai ---date:20221124  for：[issues/215]列表页查询框（日期选择框）设置初始时间，一进入页面时，后台报日期转换类型错误的------------
            } else {
              const def: dayjs.Dayjs[] = [];
              defaultValue.forEach((item) => {
                //update-begin---author:wangshuai ---date:20221124  for：[issues/215]列表页查询框（日期选择框）设置初始时间，一进入页面时，后台报日期转换类型错误的------------
                if(valueFormat){
                  // update-begin--author:liaozhiyang---date:20240529---for：【TV360X-346 】时间组件填写默认值有问题
                  def.push(dateUtil(item, valueFormat).format(valueFormat));
                  // update-end--author:liaozhiyang---date:20240529---for：【TV360X-346 】时间组件填写默认值有问题
                }else{
                  def.push(dateUtil(item));
                }
                //update-end---author:wangshuai ---date:20221124  for：[issues/215]列表页查询框（日期选择框）设置初始时间，一进入页面时，后台报日期转换类型错误的------------
              });
              // update-begin--author:liaozhiyang---date:20240328---for：【issues/1114】rangepicker等时间控件报错（vue3.4以上版本有问题）
              def.forEach((item, index) => {
                defaultValue[index] = item;
              });
              // update-end--author:liaozhiyang---date:20240328---for：【issues/1114】rangepicker等时间控件报错（vue3.4以上版本有问题）
            }
          }
        }
        if (unref(getProps).showAdvancedButton) {
          return schemas.filter((schema) => schema.component !== 'Divider') as FormSchema[];
        } else {
          return schemas as FormSchema[];
        }
      });

      const { handleToggleAdvanced } = useAdvanced({
        advanceState,
        emit,
        getProps,
        getSchema,
        formModel,
        defaultValueRef,
      });

      const { handleFormValues, initDefault } = useFormValues({
        getProps,
        defaultValueRef,
        getSchema,
        formModel,
      });

      useAutoFocus({
        getSchema,
        getProps,
        isInitedDefault: isInitedDefaultRef,
        formElRef: formElRef as Ref<FormActionType>,
      });

      const {
        handleSubmit,
        setFieldsValue,
        clearValidate,
        validate,
        validateFields,
        getFieldsValue,
        updateSchema,
        resetSchema,
        appendSchemaByField,
        removeSchemaByFiled,
        resetFields,
        scrollToField,
      } = useFormEvents({
        emit,
        getProps,
        formModel,
        getSchema,
        defaultValueRef,
        formElRef: formElRef as Ref<FormActionType>,
        schemaRef: schemaRef as Ref<FormSchema[]>,
        handleFormValues,
      });

      createFormContext({
        resetAction: resetFields,
        submitAction: handleSubmit,
      });

      watch(
        () => unref(getProps).model,
        () => {
          const { model } = unref(getProps);
          if (!model) return;
          setFieldsValue(model);
        },
        {
          immediate: true,
        }
      );

      watch(
        () => unref(getProps).schemas,
        (schemas) => {
          resetSchema(schemas ?? []);
        }
      );

      watch(
        () => getSchema.value,
        (schema) => {
          nextTick(() => {
            //  Solve the problem of modal adaptive height calculation when the form is placed in the modal
            modalFn?.redoModalHeight?.();
          });
          if (unref(isInitedDefaultRef)) {
            return;
          }
          if (schema?.length) {
            initDefault();
            isInitedDefaultRef.value = true;
          }
        }
      );

      async function setProps(formProps: Partial<FormProps>): Promise<void> {
        propsRef.value = deepMerge(unref(propsRef) || {}, formProps);
      }

      //update-begin-author:taoyan date:2022-11-28 for: QQYUN-3121 【优化】表单视图问题#scott测试 8、此功能未实现
      const onFormSubmitWhenChange = useDebounceFn(handleSubmit, 300);
      function setFormModel(key: string, value: any) {
        formModel[key] = value;
        // update-begin--author:liaozhiyang---date:20230922---for：【issues/752】表单校验dynamicRules 无法 使用失去焦点后校验 trigger: 'blur'
        // const { validateTrigger } = unref(getBindValue);
        // if (!validateTrigger || validateTrigger === 'change') {
        //   validateFields([key]).catch((_) => {});
        // }
        // update-end--author:liaozhiyang---date:20230922---for：【issues/752】表单校验dynamicRules 无法 使用失去焦点后校验 trigger: 'blur'
        if(props.autoSearch === true){
          onFormSubmitWhenChange();
        }
      }
      //update-end-author:taoyan date:2022-11-28 for: QQYUN-3121 【优化】表单视图问题#scott测试 8、此功能未实现

      function handleEnterPress(e: KeyboardEvent) {
        const { autoSubmitOnEnter } = unref(getProps);
        if (!autoSubmitOnEnter) return;
        if (e.key === 'Enter' && e.target && e.target instanceof HTMLElement) {
          const target: HTMLElement = e.target as HTMLElement;
          if (target && target.tagName && target.tagName.toUpperCase() == 'INPUT') {
            handleSubmit();
          }
        }
      }

      const formActionType: Partial<FormActionType> = {
        getFieldsValue,
        setFieldsValue,
        resetFields,
        updateSchema,
        resetSchema,
        setProps,
        getProps,
        removeSchemaByFiled,
        appendSchemaByField,
        clearValidate,
        validateFields,
        validate,
        submit: handleSubmit,
        scrollToField: scrollToField,
      };

      onMounted(() => {
        initDefault();
        emit('register', formActionType);
      });

      return {
        getBindValue,
        handleToggleAdvanced,
        handleEnterPress,
        formModel,
        defaultValueRef,
        advanceState,
        getRow,
        getProps,
        formElRef,
        getSchema,
        formActionType: formActionType as any,
        setFormModel,
        getFormClass,
        getFormActionBindProps: computed((): Recordable => ({ ...getProps.value, ...advanceState })),
        ...formActionType,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-basic-form';

  .@{prefix-cls} {
    .ant-form-item {
      &-label label::after {
        margin: 0 6px 0 2px;
      }

      &-with-help {
        margin-bottom: 0;
      }
      // update-begin--author:liaozhiyang---date:20240514---for：【QQYUN-9241】form表单上下间距大点
      //&:not(.ant-form-item-with-help) {
      //  margin-bottom: 24px;
      //}
      // update-begin--author:liaozhiyang---date:20240514---for：【QQYUN-9241】form表单上下间距大点
      // update-begin--author:liaozhiyang---date:20240620---for：【TV360X-1420】校验时闪动
      &-has-error {
        margin-bottom: 24px;
      }
      // update-end--author:liaozhiyang---date:20240620---for：【TV360X-1420】校验时闪动
      &.suffix-item {
        .ant-form-item-children {
          display: flex;
        }

        .ant-form-item-control {
          margin-top: 4px;
        }

        .suffix {
          display: inline-flex;
          padding-left: 6px;
          margin-top: 1px;
          line-height: 1;
          align-items: center;
        }
      }
    }
    /*【美化表单】form的字体改小一号*/
/*    .ant-form-item-label > label{
      font-size: 13px;
    }
    .ant-form-item .ant-select {
      font-size: 13px;
    }
    .ant-select-item-option-selected {
      font-size: 13px;
    }
    .ant-select-item-option-content {
      font-size: 13px;
    }
    .ant-input {
      font-size: 13px;
    }*/
    /*【美化表单】form的字体改小一号*/
    
    .ant-form-explain {
      font-size: 14px;
    }

    &--compact {
      .ant-form-item {
        margin-bottom: 8px !important;
      }
    }
    // update-begin--author:liaozhiyang---date:20231017---for：【QQYUN-6566】BasicForm支持一行显示(inline)
    &.ant-form-inline {
      & > .ant-row {
        .ant-col { width:auto !important; }
      }
    }
    // update-end--author:liaozhiyang---date:20231017---for：【QQYUN-6566】BasicForm支持一行显示(inline)
  }
</style>
