<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="title" @ok="handleSubmit" width="800px" destroyOnClose>
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup name="tenant-pack-menu-modal">
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';
  import { packMenuFormSchema } from '../tenant.data';
  import { addPackPermission, editPackPermission } from '../tenant.api';

  const isUpdate = ref<boolean>(false);
  // Emits声明
  const emit = defineEmits(['register', 'success']);
  //表单配置
  const [registerForm, { resetFields, setFieldsValue, validate, setProps }] = useForm({
    schemas: packMenuFormSchema,
    showActionButtonGroup: false,
  });
  //租户
  const tenantId = ref<number>();
  //套餐包类型
  const packType = ref<number>();
  //权限
  const permissionIds = ref<string>();
  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    //重置表单
    await resetFields();
    isUpdate.value = !!data?.isUpdate;
    if(data.tenantId){
      tenantId.value = data.tenantId;
    }
    packType.value = data.packType;
    if (unref(isUpdate)) {
      //表单赋值
      console.log(data.record)
      await setFieldsValue({ ...data.record });
      permissionIds.value = data.record.permissionIds;
    }
    // 代码逻辑说明: 【QQYUN-5685】2 套餐包增加一个查看：添加底部有没有按钮及表单禁用------------
    setModalProps({ confirmLoading: false, showCancelBtn:!!data?.showFooter, showOkBtn:!!data?.showFooter });
    // 隐藏底部时禁用整个表单
    setProps({ disabled: !data?.showFooter })
  });
  //设置标题
  const title = computed(() => (unref(isUpdate) ? '编辑 租户套餐' : '新增 租户套餐'));
  //表单提交事件
  async function handleSubmit(v) {
    const values = await validate();
    
    setModalProps({ confirmLoading: true });
    values.packType = unref(packType);
    if(values.packType === 'custom'){
      values.tenantId = unref(tenantId);
    }else{
      values.tenantId = 0;
    }
    values.permissionIds = permissionIds.value;
    if (!unref(isUpdate)) {
      await addPackPermission(values);
    } else {
      await editPackPermission(values);
    }
    emit('success');
    setModalProps({ confirmLoading: false });
    closeModal();
  }
</script>
