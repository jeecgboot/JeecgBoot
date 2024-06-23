<!-- 控件属性 -->
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
      label: '员工姓名',
      field: 'name',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      defaultValue: '张三',
    },
    {
      label: '性别',
      field: 'sex',
      component: 'Select',
      //填写组件的属性
      componentProps: {
        options: [
          { label: '男', value: 1 },
          { label: '女', value: 2 },
          { label: '未知', value: 3 },
        ],
      },
      //默认值
      defaultValue: 3,
    },
    {
      label: '年龄',
      field: 'age',
      component: 'Input',
    },
    {
      label: '入职时间',
      subLabel: '( 选填 )',
      field: 'entryTime',
      component: 'TimePicker',
    },
  ];

  /**
   * BasicForm绑定注册;
   */
  const [registerForm] = useForm({
    //注册表单列
    schemas: formSchemas,
    labelWidth: '150px',
    showResetButton: false,
    submitButtonOptions: { text: '提交', preIcon: '' },
    actionColOptions: { span: 17 },
  });
</script>

<style scoped></style>
