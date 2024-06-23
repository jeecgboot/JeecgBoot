<!-- 固定label标题的宽度 -->
<template>
  <!-- 自定义表单 -->
  <BasicForm @register="registerForm" @submit="handleSubmit" style="margin-top: 20px" />
</template>

<script lang="ts" setup>
  //引入依赖
  import { useForm, BasicForm, FormSchema } from '/@/components/Form';

  //自定义表单字段
  const formSchemas: FormSchema[] = [
    {
      label: '姓名',
      field: 'name',
      component: 'Input',
    },
    {
      label: '年龄',
      field: 'password',
      component: 'InputNumber',
    },
    {
      label: '生日',
      field: 'birthday',
      component: 'DatePicker',
    },
    {
      label: '头像',
      field: 'avatar',
      component: 'JImageUpload',
    },
  ];

  /**
   * BasicForm绑定注册;
   */
  const [registerForm] = useForm({
    //注册表单列
    schemas: formSchemas,
    showResetButton: false,
    submitButtonOptions: { text: '提交', preIcon: '' },
    actionColOptions: { span: 17 },
    //使用labelWidth控制标题宽度
    labelWidth: '150px',
    //使用labelCol的样式属性来控制标题宽度
    labelCol: { style: { width: '150px' } },
    //标题对齐方式（left:左对齐，right：右对齐），默认右对齐
    labelAlign: 'right',
  });

  /**
   * 点击提交按钮的value值
   * @param values
   */
  function handleSubmit(values: any) {
    console.log('提交按钮数据::::', values);
  }
</script>

<style scoped>
  /** 时间和数字输入框样式 */
  :deep(.ant-input-number) {
    width: 100%;
  }

  :deep(.ant-picker) {
    width: 100%;
  }
</style>
