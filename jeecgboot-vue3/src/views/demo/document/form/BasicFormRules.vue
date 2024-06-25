<!-- 表单验证 -->
<template>
  <!-- 自定义表单 -->
  <BasicForm @register="registerForm" style="margin-top: 20px" @submit="handleSubmit" />
</template>

<script lang="ts" setup>
  //引入依赖
  import { useForm, BasicForm, FormSchema } from '/@/components/Form';

  //自定义表单字段
  const formSchemas: FormSchema[] = [
    {
      field: 'visitor',
      label: '来访人员',
      component: 'Input',
      //自动触发检验，布尔类型
      required: true,
      //检验的时候不加上标题
      rulesMessageJoinLabel: false,
    },
    {
      field: 'accessed',
      label: '来访日期',
      component: 'DatePicker',
      //支持获取当前值判断触发 values代表当前表单的值
      required: ({ values }) => {
        return !values.accessed;
      },
    },
    {
      field: 'phone',
      label: '来访人手机号',
      component: 'Input',
      //支持正则表达式pattern 和 自定义提示信息 message
      rules: [{ required: false, message: '请输入正确的手机号', pattern: /^1[3456789]\d{9}$/ }],
    },
  ];

  /**
   * BasicForm绑定注册;
   */
  const [registerForm] = useForm({
    //注册表单列
    schemas: formSchemas,
    showResetButton: false,
    labelWidth: '150px',
    submitButtonOptions: { text: '提交', preIcon: '' },
    actionColOptions: { span: 17 },
  });

  /**
   * 提交事件
   */
  function handleSubmit(values: any) {}
</script>

<style scoped></style>
