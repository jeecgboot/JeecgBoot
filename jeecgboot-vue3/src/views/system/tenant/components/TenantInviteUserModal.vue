<!--邀请用户加入租户弹窗-->
<template>
  <BasicModal @register="registerModal" :width="500" :title="title" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script lang="ts">
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form/index';
import { defineComponent, ref } from 'vue';
export default defineComponent({
  name: 'TenantInviteUserModal',
  components: {
    BasicModal,
    BasicForm,
  },
  setup(props, { emit }) {
    const title = ref<string>('邀请成员');
    const [registerForm, { resetFields, validate }] = useForm({
      schemas: [
        {
          label: '手机号',
          field: 'phone',
          component: 'Input',
          dynamicRules: () => {
            return [
              { required: true, message: '请填写手机号' },
              { pattern: /^1[3456789]\d{9}$/, message: '手机号码格式有误' },
            ];
          },
        },
      ],
      showActionButtonGroup: false,
      labelCol: { span: 24 },
      wrapperCol: { span: 24 },
    });
    //表单赋值
    const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
      //重置表单
      await resetFields();
      setModalProps({ minHeight: 100 });
    });

    /**
     * 提交，返回给租户list页面
     */
    async function handleSubmit() {
      let values = await validate();
      emit('inviteOk',values.phone);
      closeModal();
    }

    return {
      title,
      registerModal,
      registerForm,
      handleSubmit,
    };
  },
});
</script>

<style scoped></style>
