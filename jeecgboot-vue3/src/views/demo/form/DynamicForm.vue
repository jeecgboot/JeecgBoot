<template>
  <PageWrapper title="动态表单示例">
    <div class="mb-4">
      <a-button @click="changeLabel3" class="mr-2"> 更改字段3label </a-button>
      <a-button @click="changeLabel34" class="mr-2"> 同时更改字段3,4label </a-button>
      <a-button @click="appendField" class="mr-2"> 往字段3后面插入字段10 </a-button>
      <a-button @click="deleteField" class="mr-2"> 删除字段11 </a-button>
    </div>
    <CollapseContainer title="动态表单示例,动态根据表单内其他值改变">
      <BasicForm @register="register" />
    </CollapseContainer>

    <CollapseContainer class="mt-5" title="componentProps动态改变">
      <BasicForm @register="register1" />
    </CollapseContainer>
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form/index';
  import { CollapseContainer } from '/@/components/Container/index';
  import { PageWrapper } from '/@/components/Page';
  import { usePermission } from '/@/hooks/web/usePermission';
  const { hasPermission } = usePermission();
  const schemas: FormSchema[] = [
    {
      field: 'field5',
      component: 'Switch',
      label: '是否显示字段1(css控制)',
      defaultValue: true,
      colProps: {
        span: 12,
      },
      labelWidth: 200,
    },
    {
      field: 'field1',
      component: 'Input',
      label: '字段1',
      colProps: {
        span: 12,
      },
      show: ({ values }) => {
        return hasPermission('test001');
      },
    },
    {
      field: 'field6',
      component: 'Switch',
      label: '是否显示字段2(dom控制)',
      defaultValue: true,
      colProps: {
        span: 12,
      },
      labelWidth: 200,
    },
    {
      field: 'field2',
      component: 'Input',
      label: '字段2',
      colProps: {
        span: 12,
      },
      ifShow: ({ values }) => {
        return !!values.field6;
      },
    },
    {
      field: 'field7',
      component: 'Switch',
      label: '是否禁用字段3',
      colProps: {
        span: 12,
      },
      labelWidth: 200,
    },
    {
      field: 'field3',
      component: 'DatePicker',
      label: '字段3',
      colProps: {
        span: 12,
      },
      dynamicDisabled: ({ values }) => {
        return !!values.field7;
      },
    },
    {
      field: 'field8',
      component: 'Switch',
      label: '字段4是否必填',
      colProps: {
        span: 12,
      },
      labelWidth: 200,
    },
    {
      field: 'field4',
      component: 'Select',
      label: '字段4',
      colProps: {
        span: 12,
      },
      dynamicRules: ({ values }) => {
        return values.field8 ? [{ required: true, message: '字段必填' }] : [];
      },
      componentProps: {
        options: [
          {
            label: '选项1',
            value: '1',
            key: '1',
          },
          {
            label: '选项2',
            value: '2',
            key: '2',
          },
        ],
      },
    },
    {
      field: 'field11',
      component: 'DatePicker',
      label: '字段11',
      colProps: {
        span: 8,
      },
    },
  ];

  const schemas1: FormSchema[] = [
    {
      field: 'f1',
      component: 'Input',
      label: 'F1',
      colProps: {
        span: 12,
      },
      labelWidth: 200,
      componentProps: ({ formModel }) => {
        return {
          placeholder: '同步f2的值为f1',
          onChange: (e: ChangeEvent) => {
            formModel.f2 = e.target.value;
          },
        };
      },
    },
    {
      field: 'f2',
      component: 'Input',
      label: 'F2',
      colProps: {
        span: 12,
      },
      labelWidth: 200,
      componentProps: { disabled: true },
    },
    {
      field: 'f3',
      component: 'Input',
      label: 'F3',
      colProps: {
        span: 12,
      },
      labelWidth: 200,
      // @ts-ignore
      componentProps: ({ formActionType }) => {
        return {
          placeholder: '值改变时执行查询,查看控制台',
          onChange: async () => {
            const { validate } = formActionType;
            // tableAction只适用于在表格内开启表单的例子
            // const { reload } = tableAction;
            const res = await validate();
            console.log(res);
          },
        };
      },
    },
  ];

  export default defineComponent({
    components: { BasicForm, CollapseContainer, PageWrapper },
    setup() {
      const [register, { setProps, updateSchema, appendSchemaByField, removeSchemaByFiled }] = useForm({
        labelWidth: 120,
        schemas,
        //禁用表单所有组件
        disabled: true,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 18 },
        },
        actionColOptions: {
          span: 24,
        },
      });
      const [register1] = useForm({
        labelWidth: 120,
        schemas: schemas1,
        actionColOptions: {
          span: 24,
        },
      });
      function changeLabel3() {
        updateSchema({
          field: 'field3',
          label: '字段3 New',
        });
      }
      function changeLabel34() {
        updateSchema([
          {
            field: 'field3',
            label: '字段3 New++',
          },
          {
            field: 'field4',
            label: '字段4 New++',
          },
        ]);
      }

      function appendField() {
        appendSchemaByField(
          {
            field: 'field10',
            label: '字段10',
            component: 'Input',
            colProps: {
              span: 8,
            },
          },
          'field3'
        );
      }
      function deleteField() {
        removeSchemaByFiled('field11');
      }
      return {
        register,
        register1,
        schemas,
        setProps,
        changeLabel3,
        changeLabel34,
        appendField,
        deleteField,
      };
    },
  });
</script>
