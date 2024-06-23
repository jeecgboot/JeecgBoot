<!-- 基本用法 -->
<template>
  <!-- 自定表单 -->
  <BasicForm @register="registerForm" @submit="handleSubmit" style="margin-top: 20px" />
</template>

<script lang="ts" setup>
  //引入依赖
  import { useForm, BasicForm, FormSchema } from '/@/components/Form';

  //自定义表单字段
  const formSchemas: FormSchema[] = [
    {
      //标题名称
      label: '用户名(后面根据labelLength定义的长度隐藏)',
      //字段
      field: 'username',
      //组件
      component: 'Input',
      //标题宽度,支持数字和字符串
      labelWidth: 150,
      //标题长度，超过位数隐藏
      labelLength: 3,
    },
    {
      label: '密码',
      field: 'password',
      //子标题名称（在主标题后面）
      subLabel: '(数字和字母组成)',
      component: 'InputPassword',
      labelWidth: '150px',
    },
  ];

  /**
   * BasicForm绑定注册;
   * useForm 是整个框架的核心用于表单渲染，里边封装了很多公共方法;
   * 支持（schemas: 渲染表单列，autoSubmitOnEnter：回车提交,submitButtonOptions：自定义按钮文本和图标等方法）；
   * 平台通过此封装，简化了代码，支持自定义扩展;
   */
  const [registerForm] = useForm({
    //注册表单列
    schemas: formSchemas,
    //回车提交
    autoSubmitOnEnter: true,
    //不显示重置按钮
    showResetButton: false,
    //自定义提交按钮文本和图标
    submitButtonOptions: { text: '提交', preIcon: '' },
    //查询列占比 24代表一行 取值范围 0-24
    actionColOptions: { span: 17 },
  });

  /**
   * 点击提交按钮的value值
   * @param values
   */
  function handleSubmit(values: any) {
    console.log('提交按钮数据::::', values);
  }
</script>

<style scoped></style>
