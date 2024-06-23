<template>
  <PageWrapper title="表单校验示例">
    <div class="mb-4">
      <a-button @click="validateForm" class="mr-2"> 手动校验表单</a-button>
      <a-button @click="resetValidate" class="mr-2"> 清空校验信息</a-button>
      <a-button @click="getFormValues" class="mr-2"> 获取表单值</a-button>
      <a-button @click="setFormValues" class="mr-2"> 设置表单值</a-button>
      <a-button @click="resetFields" class="mr-2"> 重置</a-button>
    </div>
    <CollapseContainer title="表单校验">
      <BasicForm @register="register" @submit="handleSubmit" />
    </CollapseContainer>
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicForm, FormSchema, useForm } from '/@/components/Form/index';
  import { CollapseContainer } from '/@/components/Container';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { PageWrapper } from '/@/components/Page';
  import { isAccountExist } from '/@/api/demo/system';

  const schemas: FormSchema[] = [
    {
      field: 'field1',
      component: 'Input',
      label: '字段1',
      colProps: {
        span: 8,
      },
      required: true,
    },
    {
      field: 'field2',
      component: 'Input',
      label: '字段2',
      colProps: {
        span: 8,
      },
      required: true,
    },
    {
      field: 'id',
      label: 'id',
      required: true,
      defaultValue: 0,
      component: 'InputNumber',
      show: false,
    },
    {
      field: 'field3',
      component: 'DatePicker',
      label: '字段3',
      colProps: {
        span: 8,
      },
      required: true,
    },
    {
      field: 'field33',
      component: 'DatePicker',
      label: '字段33',
      colProps: {
        span: 8,
      },
      componentProps: {
        valueFormat: 'YYYY-MM-DD',
      },
      rules: [{ required: true, type: 'string' }],
    },
    {
      field: 'field44',
      component: 'InputCountDown',
      label: '验证码',
      colProps: {
        span: 8,
      },
      required: true,
    },
    {
      field: 'field4',
      component: 'Select',
      label: '字段4',
      colProps: {
        span: 8,
      },
      componentProps: {
        mode: 'multiple',
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
      rules: [
        {
          required: true,
          message: '请输入规则',
          type: 'array',
        },
      ],
    },
    {
      field: 'field441',
      component: 'Input',
      label: '自定义校验',
      colProps: {
        span: 8,
      },
      rules: [
        {
          required: true,
          // @ts-ignore
          validator: async (rule, value) => {
            if (!value) {
              /* eslint-disable-next-line */
              return Promise.reject('值不能为空');
            }
            if (value === '1') {
              /* eslint-disable-next-line */
              return Promise.reject('值不能为1');
            }
            return Promise.resolve();
          },
          trigger: 'change',
        },
      ],
    },
    {
      field: 'field5',
      component: 'CheckboxGroup',
      label: '字段5',
      colProps: {
        span: 8,
      },
      componentProps: {
        options: [
          {
            label: '选项1',
            value: '1',
          },
          {
            label: '选项2',
            value: '2',
          },
        ],
      },
      rules: [{ required: true }],
    },
    {
      field: 'field7',
      component: 'RadioGroup',
      label: '字段7',
      colProps: {
        span: 8,
      },
      componentProps: {
        options: [
          {
            label: '选项1',
            value: '1',
          },
          {
            label: '选项2',
            value: '2',
          },
        ],
      },
      rules: [{ required: true, message: '覆盖默认生成的校验信息' }],
    },
    {
      field: 'field8',
      component: 'Input',
      label: '后端异步验证',
      colProps: {
        span: 8,
      },
      helpMessage: ['本字段演示异步验证', '本地规则：必须填写', '后端规则：不能包含admin'],
      rules: [
        {
          required: true,
          message: '请输入数据',
        },
        {
          validator(_, value) {
            return new Promise((resolve, reject) => {
              isAccountExist(value)
                .then(() => resolve())
                .catch((err) => {
                  reject(err.message || '验证失败');
                });
            });
          },
        },
      ],
    },
  ];

  export default defineComponent({
    components: { BasicForm, CollapseContainer, PageWrapper },
    setup() {
      const { createMessage } = useMessage();
      const [register, { validateFields, clearValidate, getFieldsValue, resetFields, setFieldsValue }] = useForm({
        labelWidth: 120,
        schemas,
        actionColOptions: {
          span: 24,
        },
      });

      async function validateForm() {
        try {
          const res = await validateFields();
          console.log('passing', res);
        } catch (error) {
          console.log('not passing', error);
        }
      }

      async function resetValidate() {
        clearValidate();
      }

      function getFormValues() {
        const values = getFieldsValue();
        createMessage.success('values:' + JSON.stringify(values));
      }

      function setFormValues() {
        setFieldsValue({
          field1: 1111,
          field5: ['1'],
          field7: '1',
          field33: '2020-12-12',
          field3: '2020-12-12',
        });
      }

      return {
        register,
        schemas,
        handleSubmit: (values: any) => {
          createMessage.success('click search,values:' + JSON.stringify(values));
        },
        getFormValues,
        setFormValues,
        validateForm,
        resetValidate,
        resetFields,
      };
    },
  });
</script>
