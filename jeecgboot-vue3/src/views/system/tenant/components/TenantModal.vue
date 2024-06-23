<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="title" @ok="handleSubmit" width="700px">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formSchema } from '../tenant.data';
  import { saveOrUpdateTenant, getTenantById } from '../tenant.api';
  // Emits声明
  const emit = defineEmits(['register', 'success']);
  const isUpdate = ref(true);
  //表单配置
  const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({
    // labelWidth: 150,
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
      // 编辑模式下禁用id字段
      updateSchema({ field: 'id', dynamicDisabled: true });
      //获取详情
      data.record = await getTenantById({ id: data.record.id });
      //表单赋值
      await setFieldsValue({
        ...data.record,
      });
    } else {
      updateSchema({ field: 'id', dynamicDisabled: false });
    }
  });
  //设置标题
  const title = computed(() => (!unref(isUpdate) ? '新增租户' : '编辑租户'));
  //表单提交事件
  async function handleSubmit(v) {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      //提交表单
      await saveOrUpdateTenant(values, isUpdate.value);
      //关闭弹窗
      closeModal();
      //刷新列表
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
