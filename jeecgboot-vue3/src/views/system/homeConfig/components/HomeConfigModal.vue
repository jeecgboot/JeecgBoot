<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="首页配置" @ok="handleSubmit" :width="600">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script lang="ts" setup>
  import { ref, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formSchema } from '../home.data';
  import { saveOrUpdate } from '../home.api';
  // Emits声明
  const emit = defineEmits(['register', 'success']);
  const isUpdate = ref(false);
  //表单配置
  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    baseRowStyle: { marginTop: '10px' },
    schemas: formSchema,
    showActionButtonGroup: false,
  });
  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    //重置表单
    await resetFields();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;
    if (unref(isUpdate)) {
      //表单赋值
      if (data.values.relationType == 'USER') {
        data.values.userCode = data.values.roleCode;
      }
      await setFieldsValue({
        ...data.values,
      });
    }
  });

  //表单提交事件
  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      //提交表单
      if(values.relationType == 'USER'){
        values.roleCode = values.userCode;
      }
      await saveOrUpdate(values, isUpdate.value);
      //关闭弹窗
      closeModal();
      //刷新列表
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>

<style lang="less" scoped></style>
