<!-- 标题与字段布局 -->
<template>
  <!-- 自定义表单 -->
  <BasicForm @register="registerForm" @submit="handleSubmit" style="margin: 20px auto"/>
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
    showActionButtonGroup: false,
    actionColOptions: { span: 12 },
    //控制标题宽度占比
    labelCol: {
      xs: 2,
      sm: 2,
      md: 2,
      lg: 9,
      xl: 3,
      xxl: 2,
    },
    //控制组件宽度占比
    wrapperCol: {
      xs: 15,
      sm: 14,
      md: 16,
      lg: 17,
      xl: 19,
      xxl: 20,
    },
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
