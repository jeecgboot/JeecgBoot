<!-- 插槽 -->
<template>
  <!-- 自定义表单 -->
  <BasicForm @register="registerForm" style="margin-top: 20px">
    <!--  #phone对应的是formSchemas对应slot(phone)插槽    -->
    <template #phone="{ model, field }">
      <!-- 如果是组件需要进行双向绑定，model当前表单对象，field当前字段名称  -->
      <a-input v-model:value="model[field]" placeholder="请输入手机号" />
      <span class="font-color">请输入您的手机号，方便我们联系您</span>
    </template>
    <template #feedback="{ model, field }">
      <JEditor v-model:value="model[field]" placeholder="请输入问题反馈" />
      <span class="font-color">请您图文并茂，方便我们了解问题并及时反馈</span>
    </template>
  </BasicForm>
</template>

<script lang="ts" setup>
  //引入依赖
  import { useForm, BasicForm, FormSchema } from '/@/components/Form';
  import JEditor from '/@/components/Form/src/jeecg/components/JEditor.vue';

  //自定义表单字段
  const formSchemas: FormSchema[] = [
    {
      field: 'name',
      label: '姓名',
      component: 'Input',
    },
    {
      field: 'phone',
      label: '联系方式',
      component: 'Input',
      slot: 'phone',
    },
    {
      field: 'feedback',
      label: '问题反馈',
      component: 'InputTextArea',
      slot: 'feedback',
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

<style scoped>
  .font-color {
    font-size: 13px;
    color: #a1a1a1;
    margin-bottom: 5px;
  }
</style>
