<!--边距页面-->
<template>
  <BasicDrawer @register="registerBaseDrawer" title="设置边距" width="600" showFooter @ok="handleOk">
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>

<script setup lang="ts">
  import { BasicDrawer, useDrawerInner } from '@/components/Drawer';
  import { BasicForm, FormSchema, useForm } from '@/components/Form';

  let emit = defineEmits(['register','ok']);
  
  const schemas: FormSchema[] = [
    {
      label:'上边距',
      field: 'marginTop',
      component: 'InputNumber',
      componentProps: {
        min: 0,
      },
      rules: [{ required: true, message: '请填写上边距' }],
    },
    {
      label:'下边距',
      field: 'marginBottom',
      component: 'InputNumber',
      componentProps: {
        min: 0,
      },
      rules: [{ required: true, message: '请填写下边距' }],
    },
    {
      label:'左边距',
      field: 'marginLeft',
      component: 'InputNumber',
      componentProps: {
        min: 0,
      },
      rules: [{ required: true, message: '请填写左边距' }],
    },
    {
      label:'右边距',
      field: 'marginRight',
      component: 'InputNumber',
      componentProps: {
        min: 0,
      },
      rules: [{ required: true, message: '请填写右边距' }],
    },
  ];

  const [registerForm, { setFieldsValue, resetFields, validate }] = useForm({
    schemas,
    showActionButtonGroup: false,
    wrapperCol: { span: 24 },
    labelCol: { span: 24 },
  });

  //注册Drawer
  const [registerBaseDrawer, { closeDrawer }] = useDrawerInner((data) => {
    resetFields();
    setFieldsValue({
      ...data,
    });
  });

  /**
   * 确定事件
   */
  async function handleOk() {
    let values = await validate();
    emit('ok', values);
    closeDrawer();
  }
</script>

<style scoped lang="less">
  :deep(.ant-input-number){
    width: 100%;
  }
</style>
