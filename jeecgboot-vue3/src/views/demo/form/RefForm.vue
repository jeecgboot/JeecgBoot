<template>
  <PageWrapper title="Ref操作示例">
    <div class="mb-4">
      <a-button @click="setProps({ labelWidth: 150 })" class="mr-2"> 更改labelWidth </a-button>
      <a-button @click="setProps({ labelWidth: 120 })" class="mr-2"> 还原labelWidth </a-button>
      <a-button @click="setProps({ size: 'large' })" class="mr-2"> 更改Size </a-button>
      <a-button @click="setProps({ size: 'default' })" class="mr-2"> 还原Size </a-button>
      <a-button @click="setProps({ disabled: true })" class="mr-2"> 禁用表单 </a-button>
      <a-button @click="setProps({ disabled: false })" class="mr-2"> 解除禁用 </a-button>
      <a-button @click="setProps({ compact: true })" class="mr-2"> 紧凑表单 </a-button>
      <a-button @click="setProps({ compact: false })" class="mr-2"> 还原正常间距 </a-button>
      <a-button @click="setProps({ actionColOptions: { span: 8 } })" class="mr-2"> 操作按钮位置 </a-button>
    </div>
    <div class="mb-4">
      <a-button @click="setProps({ showActionButtonGroup: false })" class="mr-2"> 隐藏操作按钮 </a-button>
      <a-button @click="setProps({ showActionButtonGroup: true })" class="mr-2"> 显示操作按钮 </a-button>
      <a-button @click="setProps({ showResetButton: false })" class="mr-2"> 隐藏重置按钮 </a-button>
      <a-button @click="setProps({ showResetButton: true })" class="mr-2"> 显示重置按钮 </a-button>
      <a-button @click="setProps({ showSubmitButton: false })" class="mr-2"> 隐藏查询按钮 </a-button>
      <a-button @click="setProps({ showSubmitButton: true })" class="mr-2"> 显示查询按钮 </a-button>
      <a-button
        @click="
          setProps({
            resetButtonOptions: {
              disabled: true,
              text: '重置New',
            },
          })
        "
        class="mr-2"
      >
        修改重置按钮
      </a-button>
      <a-button
        @click="
          setProps({
            submitButtonOptions: {
              disabled: true,
              loading: true,
            },
          })
        "
        class="mr-2"
      >
        修改查询按钮
      </a-button>
    </div>
    <CollapseContainer title="使用ref调用表单内部函数示例">
      <BasicForm :schemas="schemas" ref="formElRef" :labelWidth="100" @submit="handleSubmit" :actionColOptions="{ span: 24 }" />
    </CollapseContainer>
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { BasicForm, FormSchema, FormActionType, FormProps } from '/@/components/Form/index';
  import { CollapseContainer } from '/@/components/Container/index';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { PageWrapper } from '/@/components/Page';

  const schemas: FormSchema[] = [
    {
      field: 'field1',
      component: 'Input',
      label: '字段1',
      colProps: {
        span: 8,
      },
      componentProps: {
        placeholder: '自定义placeholder',
        onChange: (e: any) => {
          console.log(e);
        },
      },
    },
    {
      field: 'field2',
      component: 'Input',
      label: '字段2',
      colProps: {
        span: 8,
      },
    },
    {
      field: 'field3',
      component: 'DatePicker',
      label: '字段3',
      colProps: {
        span: 8,
      },
    },
    {
      field: 'field4',
      component: 'Select',
      label: '字段4',
      colProps: {
        span: 8,
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
    },
  ];

  export default defineComponent({
    components: { BasicForm, CollapseContainer, PageWrapper },
    setup() {
      const formElRef = ref<Nullable<FormActionType>>(null);
      const { createMessage } = useMessage();
      return {
        formElRef,
        schemas,
        handleSubmit: (values: any) => {
          createMessage.success('click search,values:' + JSON.stringify(values));
        },
        setProps(props: FormProps) {
          const formEl = formElRef.value;
          if (!formEl) return;
          formEl.setProps(props);
        },
      };
    },
  });
</script>
