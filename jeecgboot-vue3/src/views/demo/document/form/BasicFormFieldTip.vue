<!-- 字段标题提示及前缀 -->
<template>
  <!-- 自定义表单 -->
  <BasicForm @register="registerForm" style="margin-top: 20px" />
</template>

<script lang="ts" setup>
  //引入依赖
  import { useForm, BasicForm, FormSchema } from '/@/components/Form';

  //自定义表单字段
  const formSchemas: FormSchema[] = [
    {
      field: 'month',
      label: '当前月份',
      component: 'Input',
      suffix: '月',
    },
    {
      field: 'lateNumber',
      label: '迟到次数',
      component: 'InputNumber',
      //帮助信息：可以直接返回String(helpMessage:"迟到次数")，也可以获取表单值，动态填写
      helpMessage: ({ values }) => {
        return '当前迟到次数' + values.lateNumber + ', 扣款' + values.lateNumber * 50 + '元';
      },
      defaultValue: 0,
    },
    {
      field: 'lateReason',
      label: '迟到原因',
      component: 'Input',
      helpMessage: '什么原因耽误上班迟到',
      //自定义提示属性，需要结合helpMessage一起使用
      helpComponentProps: {
        maxWidth: '200px',
        color: '#66CCFF',
      },
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
</script>

<style scoped></style>
