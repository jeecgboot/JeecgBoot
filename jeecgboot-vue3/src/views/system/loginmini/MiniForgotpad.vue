<template>
  <div class="aui-content">
    <div class="aui-container">
      <div class="aui-form">
        <div class="aui-image">
          <div class="aui-image-text">
            <img :src="adTextImg" alt="" />
          </div>
        </div>
        <div class="aui-formBox">
          <div class="aui-formWell">
            <div class="aui-step-box">
              <div class="aui-step-item" :class="activeKey === 1 ? 'activeStep' : ''">
                <div class="aui-step-tags">
                  <em>1</em>
                  <p>{{t('sys.login.authentication')}}</p>
                </div>
              </div>
              <div class="aui-step-item" :class="activeKey === 2 ? 'activeStep' : ''">
                <div class="aui-step-tags">
                  <em>2</em>
                  <p>{{t('sys.login.resetLoginPassword')}}</p>
                </div>
              </div>
              <div class="aui-step-item" :class="activeKey === 3 ? 'activeStep' : ''">
                <div class="aui-step-tags">
                  <em>3</em>
                  <p>{{t('sys.login.resetSuccess')}}</p>
                </div>
              </div>
            </div>
            <div class="" style="height: 230px; position: relative">
              <a-form ref="formRef" :model="formData" v-if="activeKey === 1">
                <!-- 身份验证 begin -->
                <div class="aui-account aui-account-line aui-forgot">
                  <a-form-item>
                    <div class="aui-input-line">
                      <a-input type="text" :placeholder="t('sys.login.mobile')" v-model:value="formData.mobile" />
                    </div>
                  </a-form-item>
                  <div class="aui-input-line">
                    <a-form-item>
                      <a-input type="text" :placeholder="t('sys.login.smsCode')" v-model:value="formData.smscode" />
                    </a-form-item>
                    <div v-if="showInterval" class="aui-code-line" @click="getLoginCode">{{t('component.countdown.normalText')}}</div>
                    <div v-else class="aui-code-line">{{t('component.countdown.sendText',[unref(timeRuning)])}}</div>
                  </div>
                </div>
                <!-- 身份验证 end -->
              </a-form>
              <a-form ref="pwdFormRef" :model="pwdFormData" v-else-if="activeKey === 2">
                <!-- 重置密码 begin -->
                <div class="aui-account aui-account-line aui-forgot">
                  <a-form-item>
                    <div class="aui-input-line">
                      <a-input type="password" :placeholder="t('sys.login.passwordPlaceholder')" v-model:value="pwdFormData.password" />
                    </div>
                  </a-form-item>
                  <a-form-item>
                    <div class="aui-input-line">
                      <a-input type="password" :placeholder="t('sys.login.confirmPassword')" v-model:value="pwdFormData.confirmPassword" />
                    </div>
                  </a-form-item>
                </div>
                <!-- 重置密码 end -->
              </a-form>
                <!-- 重置成功 begin -->
                <div class="aui-success" v-else>
                  <div class="aui-success-icon">
                    <img :src="successImg"/>
                  </div>
                  <h3>恭喜您，重置密码成功！</h3>
                </div>
                <!-- 重置成功 end -->
            </div>
            <div class="aui-formButton" style="padding-bottom: 40px">
              <div class="aui-flex" v-if="activeKey === 1 || activeKey === 2">
                <a class="aui-link-login aui-flex-box" @click="nextStepClick">{{t('sys.login.nextStep')}}</a>
              </div>
              <div class="aui-flex" v-else>
                <a class="aui-linek-code aui-flex-box" @click="toLogin">{{t('sys.login.goToLogin')}}</a>
              </div>
              <div class="aui-flex">
                <a class="aui-linek-code aui-flex-box" @click="goBack"> {{ t('sys.login.backSignIn') }}</a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- 图片验证码弹窗 -->
  <CaptchaModal @register="captchaRegisterModal" @ok="getLoginCode" />
</template>
<script lang="ts" name="mini-forgotpad" setup>
  import { reactive, ref, toRaw, unref } from 'vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { SmsEnum, useFormRules, useFormValid, useLoginState } from '/@/views/sys/login/useLogin';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { getCaptcha, passwordChange, phoneVerify } from '/@/api/sys/user';
  import logoImg from '/@/assets/loginmini/icon/jeecg_logo.png'
  import adTextImg from '/@/assets/loginmini/icon/jeecg_ad_text.png'
  import successImg from '/@/assets/loginmini/icon/icon-success.png'
  import CaptchaModal from '@/components/jeecg/captcha/CaptchaModal.vue';
  import { useModal } from "@/components/Modal";
  import { ExceptionEnum } from "@/enums/exceptionEnum";
  const [captchaRegisterModal, { openModal: openCaptchaModal }] = useModal();

  //下一步控制
  const activeKey = ref<number>(1);
  const { t } = useI18n();
  const { handleBackLogin } = useLoginState();
  const { notification, createMessage, createErrorModal } = useMessage();
  //是否显示获取验证码
  const showInterval = ref<boolean>(true);
  //60s
  const timeRuning = ref<number>(60);
  //定时器
  const timer = ref<any>(null);
  const formRef = ref();
  const pwdFormRef = ref();
  //账号数据
  const accountInfo = reactive<any>({});
  //手机号表单
  const formData = reactive({
    mobile: '',
    smscode: '',
  });
  //密码表单
  const pwdFormData = reactive<any>({
    password: '',
    confirmPassword: '',
  });
  const emit = defineEmits(['go-back', 'success', 'register']);

  /**
   * 下一步
   */
  async function handleNext() {
    if (!formData.mobile) {
      createMessage.warn(t('sys.login.mobilePlaceholder'));
      return;
    }
    if (!formData.smscode) {
      createMessage.warn(t('sys.login.smsPlaceholder'));
      return;
    }
    const resultInfo = await phoneVerify(
      toRaw({
        phone: formData.mobile,
        smscode: formData.smscode,
      })
    );
    if (resultInfo.success) {
      Object.assign(accountInfo, {
        username: resultInfo.result.username,
        phone: formData.mobile,
        smscode: formData.smscode,
      });
      activeKey.value = 2;
      setTimeout(()=>{
        pwdFormRef.value.resetFields();
      },300)
    } else {
      notification.error({
        message: '错误提示',
        description: resultInfo.message || t('sys.api.networkExceptionMsg'),
        duration: 3,
      });
    }
  }

  /**
   * 完成修改密码
   */
  async function finishedPwd() {
    if (!pwdFormData.password) {
      createMessage.warn(t('sys.login.passwordPlaceholder'));
      return;
    }
    if (!pwdFormData.confirmPassword) {
      createMessage.warn(t('sys.login.confirmPassword'));
      return;
    }
    if (pwdFormData.password !== pwdFormData.confirmPassword) {
      createMessage.warn(t('sys.login.diffPwd'));
      return;
    }
    const resultInfo = await passwordChange(
      toRaw({
        username: accountInfo.username,
        password: pwdFormData.password,
        smscode: accountInfo.smscode,
        phone: accountInfo.phone,
      })
    );
    if (resultInfo.success) {
      accountInfo.password = pwdFormData.password;
      //修改密码
      activeKey.value = 3;
    } else {
      //错误提示
      createErrorModal({
        title: t('sys.api.errorTip'),
        content: resultInfo.message || t('sys.api.networkExceptionMsg'),
      });
    }
  }
  /**
   * 下一步
   */
  function nextStepClick() {
    if (unref(activeKey) == 1) {
      handleNext();
    } else if (unref(activeKey) == 2) {
      finishedPwd();
    }
  }

  /**
   * 去登录
   */
  function toLogin() {
    emit('success', { username: accountInfo.username, password: accountInfo.password });
    initForm();
  }

  /**
   * 返回
   */
  function goBack() {
    emit('go-back');
    initForm();
  }

  /**
   * 获取手机验证码
   */
  async function getLoginCode() {
    if (!formData.mobile) {
      createMessage.warn(t('sys.login.mobilePlaceholder'));
      return;
    }
    //update-begin---author:wangshuai---date:2024-04-18---for:【QQYUN-9005】同一个IP，1分钟超过5次短信，则提示需要验证码---
    const result = await getCaptcha({ mobile: formData.mobile, smsmode: SmsEnum.FORGET_PASSWORD }).catch((res) =>{
      if(res.code === ExceptionEnum.PHONE_SMS_FAIL_CODE){
        openCaptchaModal(true, {});
      }
    });
    //update-end---author:wangshuai---date:2024-04-18---for:【QQYUN-9005】同一个IP，1分钟超过5次短信，则提示需要验证码---
    if (result) {
      const TIME_COUNT = 60;
      if (!unref(timer)) {
        timeRuning.value = TIME_COUNT;
        showInterval.value = false;
        timer.value = setInterval(() => {
          if (unref(timeRuning) > 0 && unref(timeRuning) <= TIME_COUNT) {
            timeRuning.value = timeRuning.value - 1;
          } else {
            showInterval.value = true;
            clearInterval(unref(timer));
            timer.value = null;
          }
        }, 1000);
      }
    }
  }

  /**
   * 初始化表单
   */
  function initForm() {
    activeKey.value = 1;
    Object.assign(formData, { phone: '', smscode: '' });
    Object.assign(pwdFormData, { password: '', confirmPassword: '' });
    Object.assign(accountInfo, {});
    if(unref(timer)){
      clearInterval(unref(timer));
      timer.value = null;
      showInterval.value = true;
    }
    setTimeout(()=>{
      formRef.value.resetFields();
    },300)
  }

  defineExpose({
    initForm,
  });
</script>
<style lang="less" scoped>
@import '/@/assets/loginmini/style/home.less';
@import '/@/assets/loginmini/style/base.less';
</style>
