<!-- 插槽 -->
<template>
  <!-- 自定义表单 -->
  <BasicForm @register="registerForm" style="margin-top: 20px" @submit="handleSubmit">
      <!--  #name对应的是formSchemas对应slot(name)插槽    -->
      <template #name="{ model, field }">
        <JInput v-model:value="model[field]" />
      </template>
  </BasicForm>
</template>

<script lang="ts" setup>
  //引入依赖
  import { useForm, BasicForm, FormSchema } from '/@/components/Form';
  //引入CustomDemo自定义组件
  import JInput from "/@/components/Form/src/jeecg/components/JInput.vue";

  //自定义表单字段
  const formSchemas: FormSchema[] = [
    {
      field: 'name',
      label: '姓名',
      component: 'Input',
      slot:'name'
    },
    {
      field: 'phone',
      label: '联系方式',
      component: 'Input',
    },
    {
      field: 'feedback',
      label: '问题反馈',
      component: 'InputTextArea',
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
   * 提交信息
   */
  function handleSubmit(values) {
    console.log("values::",values);
  }
</script>

<style scoped>
  .font-color {
    font-size: 13px;
    color: #a1a1a1;
    margin-bottom: 5px;
  }
</style>
