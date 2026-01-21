<!-- 高亮颜色 -->
<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="设置高亮颜色" @ok="handleSubmit" :width="500">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script setup lang="ts">
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, FormSchema, useForm } from '@/components/Form';
  const formSchema: FormSchema[] = [
    {
      label: '高亮颜色',
      field: 'highlightColor',
      component: 'Select',
      required: true,
      componentProps: {
        options: [
          { value: '#ffffff00', label: '无色' },
          { value: '#FFFF00', label: '黄色' },
          { value: '#00FF00', label: '绿色' },
          { value: '#00FFFF', label: '青色' },
          { value: '#FF00FF', label: '粉红色' },
          { value: '#0000FF', label: '蓝色' },
          { value: '#FF0000', label: '红色' },
          { value: '#000080', label: '深蓝色' },
          { value: '#008080', label: '深青色' },
          { value: '#008000', label: '深绿色' },
          { value: '#800080', label: '紫色' },
          { value: '#800000', label: '深红色' },
          { value: '#808000', label: '深黄色' },
          { value: '#808080', label: '深灰色' },
          { value: '#C0C0C0', label: '浅灰色' },
          { value: '#000000', label: '黑色' },
        ],
        getPopupContainer: ()=> document.body
      },
    },
  ];
  // Emits声明
  const emit = defineEmits(['register', 'ok']);
  //表单配置
  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    schemas: formSchema,
    showActionButtonGroup: false,
    wrapperCol: { span: 24 },
    labelCol: { span: 24 },
  });

  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    //重置表单
    await resetFields();
    setModalProps({ confirmLoading: false });
    //表单赋值
    await setFieldsValue({
      ...data,
    });
  });

  //表单提交事件
  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      //关闭弹窗
      closeModal();
      //刷新列表
      emit('ok', values);
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>

<style scoped lang="less"></style>
