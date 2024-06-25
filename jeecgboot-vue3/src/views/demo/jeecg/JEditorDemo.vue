<template>
  <div class="p-4">
    <div class="p-4 bg-white">
      <a-button-group class="j-table-operator">
        <a-button type="primary" @click="setDis(0)">启用</a-button>
        <a-button type="primary" @click="setDis(1)">禁用</a-button>
        <a-button type="primary" @click="getValues()">校验表单并获取值</a-button>
        <a-button type="primary" @click="setValues()">设置值</a-button>
      </a-button-group>

      <BasicForm @register="register" @submit="handleSubmit" />
    </div>
  </div>
</template>

<script lang="ts">
  export default {
    title: '富文本 | Markdown',
    name: 'MarkdownDemo',
  };
</script>

<script lang="ts" setup>
  import { FormSchema, useForm, BasicForm } from '/@/components/Form';
  import { useMessage } from '/@/hooks/web/useMessage';

  const { createMessage, createSuccessModal } = useMessage();

  const schemas: FormSchema[] = [
    {
      field: 'name',
      component: 'Input',
      label: '姓名',
      required: true,
      defaultValue: 'zhangsan',
    },
    {
      field: 'tinymce',
      component: 'JEditor',
      label: '富文本',
      defaultValue: 'defaultValue',
      required: true,
    },
    {
      field: 'markdown',
      component: 'JMarkdownEditor',
      label: 'Markdown',
      defaultValue: '# 张三',
      required: true,
      componentProps: {
        height: 300,
      },
    },
  ];

  const [register, { setProps, validate, setFieldsValue }] = useForm({
    labelWidth: 120,
    schemas: schemas,
    actionColOptions: {
      span: 24,
    },
    compact: true,
    showResetButton: false,
    showSubmitButton: false,
    showAdvancedButton: false,
    disabled: false,
  });

  function handleSubmit(values) {
    console.log(values);
  }

  function setDis(flag) {
    setProps({ disabled: !!flag });
  }

  async function getValues() {
    try {
      const values = await validate();
      console.log(values);
      createSuccessModal({
        title: '校验通过',
        content: `${JSON.stringify(values)}`,
      });
    } catch (error) {
      createMessage.warning('检验不通过');
    }
  }

  function setValues() {
    setFieldsValue({
      name: 'LiSi',
      markdown: '# 李四',
      tinymce: '<p><strong><span style="font-size: 18pt;">张<span style="color: #e03e2d;">三</span>丰</span></strong></p>',
    });
  }
</script>

<style scoped></style>
