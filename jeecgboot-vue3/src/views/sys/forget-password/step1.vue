<template>
  <Form class="p-4 enter-x" :model="formData" :rules="getFormRules" ref="formRef">
    <FormItem name="mobile" class="enter-x">
      <Input size="large" v-model:value="formData.mobile" :placeholder="t('sys.login.mobile')" />
    </FormItem>
    <FormItem name="sms" class="enter-x">
      <CountdownInput size="large" v-model:value="formData.sms" :placeholder="t('sys.login.smsCode')" :sendCodeApi="sendCodeApi" />
    </FormItem>
    <FormItem class="enter-x">
      <Button type="primary" size="large" block @click="handleNext" :loading="loading"> 下一步 </Button>
      <Button size="large" block class="mt-4" @click="handleBackLogin">
        {{ t('sys.login.backSignIn') }}
      </Button>
    </FormItem>
  </Form>
</template>
<script lang="ts">
  import { defineComponent, reactive, ref, computed, unref, toRaw } from 'vue';

  import { Form, Input, Button, steps } from 'ant-design-vue';
  import { CountdownInput } from '/@/components/CountDown';

  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useLoginState, useFormRules, useFormValid, LoginStateEnum, SmsEnum } from '../login/useLogin';
  import { phoneVerify, getCaptcha } from '/@/api/sys/user';

  export default defineComponent({
    name: 'step1',
    components: {
      Button,
      Form,
      FormItem: Form.Item,
      Input,
      CountdownInput,
    },
    emits: ['nextStep'],
    setup(_, { emit }) {
      const { t } = useI18n();
      const { handleBackLogin } = useLoginState();
      const { notification } = useMessage();

      const formRef = ref();
      const { validForm } = useFormValid(formRef);
      const { getFormRules } = useFormRules();

      const loading = ref(false);
      const formData = reactive({
        mobile: '',
        sms: '',
      });

      /**
       * 下一步
       */
      async function handleNext() {
        const data = await validForm();
        if (!data) return;
        const resultInfo = await phoneVerify(
          toRaw({
            phone: data.mobile,
            smscode: data.sms,
          })
        );
        if (resultInfo.success) {
          let accountInfo = {
            username: resultInfo.result.username,
            phone: data.mobile,
            smscode: resultInfo.result.smscode,
          };
          emit('nextStep', accountInfo);
        } else {
          notification.error({
            message: t('sys.api.errorTip'),
            description: resultInfo.message || t('sys.api.networkExceptionMsg'),
            duration: 3,
          });
        }
      }
      //倒计时执行前的函数
      function sendCodeApi() {
        return getCaptcha({ mobile: formData.mobile, smsmode: SmsEnum.FORGET_PASSWORD });
      }
      return {
        t,
        formRef,
        formData,
        getFormRules,
        handleNext,
        loading,
        handleBackLogin,
        sendCodeApi,
      };
    },
  });
</script>
