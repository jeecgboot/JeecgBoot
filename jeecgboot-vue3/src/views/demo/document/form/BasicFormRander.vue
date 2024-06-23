<!-- 自定义渲染 -->
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
  import { h } from 'vue';
  import { Input } from 'ant-design-vue';

  //自定义表单字段
  const formSchemas: FormSchema[] = [
    {
      field: 'productName',
      label: '商品名称',
      component: 'Input',
    },
    {
      field: 'price',
      label: '价格',
      component: 'InputNumber',
    },
    {
      field: 'buyNums',
      label: '购买个数',
      component: 'InputNumber',
      //model 单签表单对象，field 当前字段
      render: ({ model, field }) => {
        //渲染自定义组件，以Input为例
        return h(Input, {
          placeholder: '请输入购买个数',
          value: model[field],
          style: { width: '100%' },
          type: 'number',
          onChange: (e: ChangeEvent) => {
            model[field] = e.target.value;
          },
        });
      },
    },
    {
      field: 'describe',
      label: '描述',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
      //渲染 values当前表单所有值
      render: ({ values }) => {
        let productName = values.productName?values.productName:'';
        let price = values.price ? values.price : 0;
        let buyNums = values.buyNums ? values.buyNums : 0;
        return '购买商品名称：' + productName + ', 总价格: ' + price * buyNums + '元';
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

<style scoped>
  /** 数字输入框样式 */
  :deep(.ant-input-number) {
    width: 100%;
  }
</style>
