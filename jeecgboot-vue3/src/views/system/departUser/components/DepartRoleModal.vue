<template>
  <BasicModal :title="title" :width="800" v-bind="$attrs" @ok="handleOk" @register="registerModal">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script lang="ts" setup>
  import { computed, inject, ref, unref } from 'vue';

  import { BasicForm, useForm } from '/@/components/Form/index';
  // noinspection ES6UnusedImports
  import { BasicModal, useModalInner } from '/@/components/Modal';

  import { saveOrUpdateDepartRole } from '../depart.user.api';
  import { departRoleModalFormSchema } from '../depart.user.data';

  const emit = defineEmits(['success', 'register']);
  const props = defineProps({
    // 当前部门ID
    departId: { require: true, type: String },
  });
  const prefixCls = inject('prefixCls');
  // 当前是否是更新模式
  const isUpdate = ref<boolean>(true);
  // 当前的弹窗数据
  const model = ref<object>({});
  const title = computed(() => (isUpdate.value ? '编辑' : '新增'));

  //注册表单
  const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({
    schemas: departRoleModalFormSchema,
    showActionButtonGroup: false,
  });

  // 注册弹窗
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    await resetFields();
    isUpdate.value = unref(data?.isUpdate);
    // 无论新增还是编辑，都可以设置表单值
    let record = unref(data?.record);
    if (typeof record === 'object') {
      model.value = record;
      await setFieldsValue({ ...record });
    }
  });

  //提交事件
  async function handleOk() {
    try {
      setModalProps({ confirmLoading: true });
      let values = await validate();
      values.departId = unref(props.departId);
      //提交表单
      await saveOrUpdateDepartRole(values, isUpdate.value);
      //关闭弹窗
      closeModal();
      //刷新列表
      emit('success', { isUpdate: unref(isUpdate), values });
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
