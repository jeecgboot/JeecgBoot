<!--水印页面-->
<template>
  <BasicDrawer @register="registerBaseDrawer" title="设置水印" width="600" showFooter @ok="handleOk">
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>

<script setup lang="ts">
  import { BasicDrawer, useDrawerInner } from '@/components/Drawer';
  import { BasicForm, FormSchema, useForm } from '@/components/Form';

  let emit = defineEmits(['register', 'ok']);

  const schemas: FormSchema[] = [
    {
      label: '内容',
      field: 'data',
      component: 'Input',
      rules: [{ required: true, message: '请填写内容' }],
    },
    {
      label: '颜色',
      field: 'color',
      component: 'Input',
      componentProps: {
        type: 'color',
      },
      rules: [{ required: true, message: '请选择颜色' }],
      defaultValue: '#AEB5C0',
    },
    {
      label: '字体大小',
      field: 'size',
      component: 'InputNumber',
      componentProps: {
        min: 10,
      },
      defaultValue: 50,
      rules: [{ required: true, message: '请填写字体大小' }],
    },
    {
      label: '透明度',
      field: 'opacity',
      component: 'Slider',
      componentProps: {
        min: 0.1,
        max: 1,
        step: 0.1,
      },
      defaultValue: 0.3,
      rules: [{ required: true, message: '请填写透明度' }],
    },
    {
      label: '重复',
      field: 'repeat',
      component: 'Select',
      componentProps: {
        options: [
          { label: '不重复', value: '0' },
          { label: '重复', value: '1' },
        ],
      },
      defaultValue: '0',
    },
    {
      label: '水平间隔',
      field: 'horizontalGap',
      component: 'InputNumber',
      defaultValue: 10,
    },
    {
      label: '垂直间隔',
      field: 'verticalGap',
      component: 'InputNumber',
      defaultValue: 10,
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
  :deep(.ant-input-number) {
    width: 100%;
  }
</style>
