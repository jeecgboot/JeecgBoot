<template>
<BasicModal v-bind="$attrs" @register="registerModal" width="500px"  :title="title" :showCancelBtn="false" :showOkBtn="false">
  <a-form v-if="type==='updatePhone'" class="antd-modal-form" ref="updateFormRef" :model="updateFormState"
          :rules="updateValidatorRules">
    <div v-if="current === 0">
      <a-form-item name="phoneText">
        <span class="black font-size-13">原手机号</span>
        <div class="phone-padding">
          <span>{{ updateFormState.phoneText }}</span>
        </div>
      </a-form-item>
      <a-form-item name="smscode">
        <span class="black font-size-13">验证码</span>
        <CountdownInput class="phone-padding" size="large" v-model:value="updateFormState.smscode" placeholder="输入6位验证码"
                        :sendCodeApi="()=>updateSendCodeApi('verifyOriginalPhone')"/>
      </a-form-item>
      <a-form-item>
        <a-button size="large" type="primary" block @click="nextStepClick">
          下一步
        </a-button>
      </a-form-item>
    </div>
    <div v-else-if="current === 1">
      <a-form-item name="newPhone">
        <span class="black font-size-13">新手机号</span>
        <div class="phone-padding">
          <a-input v-model:value="updateFormState.newPhone" placeholder="请输入新手机号"/>
        </div>
      </a-form-item>
      <a-form-item name="smscode">
        <span class="black font-size-13">验证码</span>
        <CountdownInput class="phone-padding" size="large" v-model:value="updateFormState.smscode" placeholder="输入6位验证码"
                        :sendCodeApi="()=>updateSendCodeApi('updatePhone')"/>
      </a-form-item>
      <a-form-item>
        <a-button size="large" type="primary" block @click="finishedClick">
          完成
        </a-button>
      </a-form-item>
    </div>
  </a-form>

  <a-form v-else-if="type==='bindPhone'" class="antd-modal-form" ref="formRef" :model="formState" :rules="validatorRules">
    <a-form-item  name="phone">
      <a-input size="large" v-model:value="formState.phone" placeholder="请输入手机号" />
    </a-form-item>
    <a-form-item name="smscode">
      <CountdownInput size="large" v-model:value="formState.smscode" placeholder="输入6位验证码" :sendCodeApi="sendCodeApi" />
    </a-form-item>
    <a-form-item>
      <a-button size="large" type="primary" block @click="updatePhone">
        确认
      </a-button>
    </a-form-item>
  </a-form>
</BasicModal>
</template>

<script lang="ts" setup name="user-replace-phone-modal">
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
import { updateMobile, changePhone } from "../UserSetting.api";
import { duplicateCheck } from "/@/views/system/user/user.api";
import {defHttp} from "@/utils/http/axios";
import { ExceptionEnum } from "@/enums/exceptionEnum";

const userStore = useUserStore();
const { createMessage } = useMessage();
const formState = reactive<Record<string, any>>({
  phone:'',
  smscode:''
});

//修改手机号
const updateFormState = reactive<Record<string, any>>({
  phone:'',
  smscode:'',
  newPhone:'',
  phoneText:'',
  newSmsCode:''
});

const formRef = ref();
const userData = ref<any>({})

const validatorRules: Record<string, Rule[]> = {
  phone: [{...rules.duplicateCheckRule("sys_user",'phone',formState,{ label:'手机号' })[0]},{ pattern: /^1[3456789]\d{9}$/, message: '手机号码格式有误' }],
  smscode: [{ required: true,message:'请输入验证码' }],
};

//修改手机号验证规则
const updateValidatorRules: Record<string, Rule[]> = {
  newPhone: [{...rules.duplicateCheckRule("sys_user",'phone',formState,{ label:'手机号' })[0]},{ pattern: /^1[3456789]\d{9}$/, message: '手机号码格式有误' }],
  smscode: [{ required: true,message:'请输入验证码' }],
  newSmsCode: [{ required: true,message:'请输入验证码' }],
};
const useForm = Form.useForm;
const title = ref<string>('');
const emit = defineEmits(['register','success']);
//修改手机号还是绑定手机号
const type = ref<string>('updatePhone');
const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
  setModalProps({ confirmLoading: false });
  if(data.record.phone){
    updateFormState.phone = "";
    updateFormState.smscode = "";
    current.value = 0;
    title.value = '修改手机号';
    type.value = "updatePhone";
    Object.assign(updateFormState, data.record);
  }else{
    title.value = '绑定手机号';
    type.value = "bindPhone"
    //赋值
    data.record.smscode = '';
    Object.assign(formState, data.record);
    setTimeout(()=>{
      formRef.value.resetFields();
      formRef.value.clearValidate();
    },300)
  }
  userData.value = data.record;
});

/**
 * 倒计时执行前的函数
 */
function sendCodeApi() {
  return getCaptcha({ mobile: formState.phone, smsmode: SmsEnum.REGISTER });
}

/**
 * 倒计时执行前的函数
 * 
 * @param type 类型 verifyOriginalPhone 验证员手机号 updatePhone 修改手机号
 */
function updateSendCodeApi(type) {
  let phone = ""
  if(current.value === 0){
    phone = updateFormState.phone;
  }else{
    phone = updateFormState.newPhone;
  }
  let params = { phone: phone,  type: type };
  return new Promise((resolve, reject) => {
    defHttp.post({ url: "/sys/user/sendChangePhoneSms", params }, { isTransformResponse: false }).then((res) => {
      console.log(res);
      if (res.success) {
        resolve(true);
      } else {
        //update-begin---author:wangshuai---date:2024-04-18---for:【QQYUN-9005】同一个IP，1分钟超过5次短信，则提示需要验证码---
        if(res.code != ExceptionEnum.PHONE_SMS_FAIL_CODE){
          createMessage.error(res.message || '未知问题');
          reject();
        }
        reject(res);
        //update-end---author:wangshuai---date:2024-04-18---for:【QQYUN-9005】同一个IP，1分钟超过5次短信，则提示需要验证码---
      }
    }).catch((res)=>{
      createMessage.error(res.message || '未知问题');
      reject();
    });
  });
}

/**
 * 更新手机号
 */
async function updatePhone() {
  await formRef.value.validateFields();
  updateMobile(formState).then((res) =>{
    if(res.success){
      createMessage.success(type.value === "updatePhone"?"修改手机号成功":"绑定手机号成功")
      emit("success")
      closeModal();
    }else{
      createMessage.warning(res.message)
    }
  })
}
  //走到第几步
  const current = ref<number>(0);
  const updateFormRef = ref();

  /**
   * 下一步点击事件
   */
  async function nextStepClick() {
    let params = { phone: updateFormState.phone, smscode: updateFormState.smscode, type: 'verifyOriginalPhone' };
    changeAndVerifyPhone(params,1)
  }

  /**
   * 完成
   */
  function finishedClick() {
    changeAndVerifyPhone({ phone: updateFormState.phone, newPhone: updateFormState.newPhone, smscode: updateFormState.smscode, type: 'updatePhone' },0);
  }

  /**
   * 修改并验证手机号
   * @param params
   * @param index
   */
  async function changeAndVerifyPhone(params, index) {
    await updateFormRef.value.validateFields();
    changePhone(params).then((res)=>{
      if(res.success){
        current.value = index;
        if(index == 0){
          createMessage.success(res.message);
          emit("success");
          closeModal();
        }
        updateFormState.smscode = "";
      }else{
        createMessage.warn(res.message);
      }
    }).catch((res) =>{
      createMessage.warn(res.message);
    })
  }
</script>
<style lang="less" scoped>
  .antd-modal-form {
    padding: 10px 24px 10px 24px;
  }
 .black {
   color: #000000;
 }
 .font-size-13 {
   font-size: 13px;
   line-height: 15px;
 }
  .phone-padding {
    padding-top: 10px;
    padding-bottom: 10px;
  }
  :deep(.ant-form-item){
    margin-bottom: 10px;
  }
</style>