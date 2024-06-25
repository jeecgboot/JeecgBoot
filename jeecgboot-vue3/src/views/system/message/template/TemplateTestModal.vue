<template>
  <BasicModal @register="registerModal" title="发送测试" :width="800" v-bind="$attrs" @ok="onSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script lang="ts" setup>
  import { ref, unref } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { sendTestFormSchemas } from './template.data';
  import { sendMessageTest } from './template.api';

  // 声明 emits
  const emit = defineEmits(['register']);
  // 注册 form
  const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({
    schemas: sendTestFormSchemas,
    showActionButtonGroup: false,
  });
  // 注册 modal
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    await resetFields();
    await setFieldsValue({ ...unref(data.record) });
  });

  //表单提交事件
  async function onSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      // 提交表单
      await sendMessageTest(values);
      //关闭弹窗
      closeModal();
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
