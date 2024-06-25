<!-- 自定义组件 -->
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
      field: 'custom',
      label: '自定义组件',
      //引入自定义组件
      component: 'JInput',
    },
  ];

  /**
   * BasicForm绑定注册;
   */
  const [registerForm] = useForm({
    //注册表单列
    schemas: formSchemas,
    labelWidth: '150px',
    showActionButtonGroup: false,
  });
</script>

<style scoped></style>
