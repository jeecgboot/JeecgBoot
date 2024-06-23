<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="title" @ok="handleSubmit" :width="800" destroyOnClose>
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script lang="ts" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formSchema } from './fill.rule.data';
  import { saveFillRule, updateFillRule } from './fill.rule.api';

  //设置标题
  const title = computed(() => (!unref(isUpdate) ? '新增' : '编辑'));

  // 声明Emits
  const emit = defineEmits(['register', 'success']);
  const isUpdate = ref(true);

  //表单配置
  const [registerForm, { resetFields, setFieldsValue, validate, getFieldsValue }] = useForm({
    schemas: formSchema,
    showActionButtonGroup: false,
    baseColProps: { span: 12 },
  });

  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    //重置表单
    await resetFields();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;
    if (unref(isUpdate)) {
      //表单赋值
      await setFieldsValue({
        ...data.record,
      });
    }
  });

  //表单提交事件
  async function handleSubmit() {
    try {
      let formValue = await validate();
      setModalProps({ confirmLoading: true });
      if (isUpdate.value) {
        let allFieldsValue = getFieldsValue();
        // 编辑页面 如果表单没有父级下拉框 则提交时候 validate方法不返该值 需要手动设置
        if (!formValue.parentId && allFieldsValue.parentId) {
          formValue.parentId = allFieldsValue.parentId;
        }
        await updateFillRule(formValue);
      } else {
        await saveFillRule(formValue);
      }
      //关闭弹窗
      closeModal();
      //刷新列表
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
