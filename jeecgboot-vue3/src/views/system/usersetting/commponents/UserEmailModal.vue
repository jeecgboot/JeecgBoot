<template>
<BasicModal v-bind="$attrs" @register="registerModal" width="500px"  :title="title" :showCancelBtn="false" :showOkBtn="false">
  <a-form class="antd-modal-form" ref="formRef" :model="formState" :rules="validatorRules">
    <a-form-item name="email">
      <a-input size="large" v-model:value="formState.email" placeholder="请输入邮箱" />
    </a-form-item>
    <a-form-item>
      <a-button size="large" type="primary" block @click="updateEmail">
        确认
      </a-button>
    </a-form-item>
  </a-form>
</BasicModal>
</template>

<script lang="ts" setup name="user-replace-email-modal">
import BasicModal from "/@/components/Modal/src/BasicModal.vue";
import { CountdownInput } from '/@/components/CountDown';
import { useUserStore } from "/@/store/modules/user";
import { useMessage } from "/@/hooks/web/useMessage";
import { defineEmits, ref, reactive, toRaw } from "vue";
import { useModalInner } from "/@/components/Modal";
import { getCaptcha } from "/@/api/sys/user";
import { SmsEnum } from "/@/views/sys/login/useLogin";
import { Rule } from "/@/components/Form";
import { rules } from "/@/utils/helper/validator";
import { Form } from "ant-design-vue";
import { updateMobile, userEdit } from "../UserSetting.api";
import { duplicateCheck } from "/@/views/system/user/user.api";

const userStore = useUserStore();
const { createMessage } = useMessage();
const formState = reactive<Record<string, any>>({
  email:'',
});
const formRef = ref();

const validatorRules: Record<string, Rule[]> = {
  email: [{...rules.duplicateCheckRule("sys_user",'email',formState,{ label:'邮箱' })[0]},{ required: true, type: 'email', message: '邮箱格式不正确' }],
};
const useForm = Form.useForm;
const title = ref<string>('');
const emit = defineEmits(['register','success']);
const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
  formRef.value.resetFields();
  formRef.value.clearValidate();
  setModalProps({ confirmLoading: false });
  title.value = '修改邮箱';
  //赋值
  data.record.smscode = '';
  Object.assign(formState, data.record);
});

/**
 * 更新邮箱
 */
async function updateEmail() {
  await formRef.value.validateFields();
  userEdit(formState).then((res) =>{
    if(res.success){
      createMessage.success("修改邮箱成功")
      emit("success")
      closeModal();
    }else{
      createMessage.warning(res.message)
    }
  })
}
</script>

<style scoped>

</style>