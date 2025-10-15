<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="title" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" name="AiOcrModal" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { schemas } from '../AiOcr.data';
  import {addOcr, editOcr} from "../AiOcr.api";
  const title = ref<string>('新增');
  const isUpdate = ref<boolean>();
  // 声明Emits
  const emit = defineEmits(['success', 'register']);
  //表单配置
  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    schemas: schemas,
    showActionButtonGroup: false,
    layout: 'vertical',
    wrapperCol: { span: 24 },
  });

  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: true, bodyStyle:{ padding:'24px'} });
    isUpdate.value = !!data?.isUpdate;
    title.value = !unref(isUpdate) ? '新增' : '编辑'
    //重置表单
    await resetFields();
    setModalProps({ confirmLoading: false });
    if(unref(isUpdate)){
      //表单赋值
      await setFieldsValue({ ...data.record });
    }
  });

  //表单提交事件
  async function handleSubmit() {
    try {
      const values = await validate();
      if(unref(isUpdate)){
        await editOcr(values);
      } else{
        await addOcr(values);
      }
      setModalProps({ confirmLoading: true });
      //关闭弹窗
      closeModal();
      //刷新列表
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>

<style lang="less" scoped>
:deep(.ant-modal-body){
  padding: 24px !important;
}
</style>
