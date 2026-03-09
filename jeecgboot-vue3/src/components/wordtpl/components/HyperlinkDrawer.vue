<!--超链接-->
<template>
  <BasicDrawer @register="registerBaseDrawer" title="超链接" width="600" showFooter @ok="handleOk">
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>

<script setup lang="ts">
  import { BasicDrawer, useDrawerInner } from '@/components/Drawer';
  import { BasicForm, FormSchema, useForm } from '@/components/Form';

  let emit = defineEmits(['register', 'ok']);

  const schemas: FormSchema[] = [
    {
      label: '链接名称',
      field: 'name',
      component: 'Input',
      rules: [{ required: true, message: '请填写链接名称' }],
    },
    {
      label: '链接地址',
      field: 'url',
      component: 'Input',
      componentProps: {
        min: 0,
      },
      rules: [{ required: true, message: '请填写链接地址' }],
    },
  ];

  const [registerForm, { setFieldsValue, resetFields, validate, clearValidate }] = useForm({
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
    clearValidate();
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
  :deep(.ant-input-number) {
    width: 100%;
  }
</style>
