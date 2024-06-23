<template>
  <LoginFormTitle v-show="getShow" class="enter-x" />
  <Form class="p-4 enter-x" :model="formData" :rules="getFormRules" ref="formRef" v-show="getShow" @keypress.enter="handleLogin">
    <FormItem name="account" class="enter-x">
      <Input size="large" v-model:value="formData.account" :placeholder="t('sys.login.userName')" class="fix-auto-fill" />
    </FormItem>
    <FormItem name="password" class="enter-x">
      <InputPassword size="large" visibilityToggle v-model:value="formData.password" :placeholder="t('sys.login.password')" />
    </FormItem>

    <!--验证码-->
    <ARow class="enter-x">
      <ACol :span="12">
        <FormItem name="inputCode" class="enter-x">
          <Input size="large" v-model:value="formData.inputCode" :placeholder="t('sys.login.inputCode')" style="min-width: 100px" />
        </FormItem>
      </ACol>
      <ACol :span="8">
        <FormItem :style="{ 'text-align': 'right', 'margin-left': '20px' }" class="enter-x">
          <img
            v-if="randCodeData.requestCodeSuccess"
            style="margin-top: 2px; max-width: initial"
            :src="randCodeData.randCodeImage"
            @click="handleChangeCheckCode"
          />
          <img v-else style="margin-top: 2px; max-width: initial" src="../../../assets/images/checkcode.png" @click="handleChangeCheckCode" />
        </FormItem>
      </ACol>
    </ARow>

    <ARow class="enter-x">
      <ACol :span="12">
        <FormItem>
          <!-- No logic, you need to deal with it yourself -->
          <Checkbox v-model:checked="rememberMe" size="small">
            {{ t('sys.login.rememberMe') }}
          </Checkbox>
        </FormItem>
      </ACol>
      <ACol :span="12">
        <FormItem :style="{ 'text-align': 'right' }">
          <!-- No logic, you need to deal with it yourself -->
          <Button type="link" size="small" @click="setLoginState(LoginStateEnum.RESET_PASSWORD)">
            {{ t('sys.login.forgetPassword') }}
          </Button>
        </FormItem>
      </ACol>
    </ARow>

    <FormItem class="enter-x">
      <Button type="primary" size="large" block @click="handleLogin" :loading="loading">
        {{ t('sys.login.loginButton') }}
      </Button>
      <!-- <Button size="large" class="mt-4 enter-x" block @click="handleRegister">
              {{ t('sys.login.registerButton') }}
            </Button> -->
    </FormItem>
    <ARow class="enter-x">
      <ACol :md="8" :xs="24">
        <Button block @click="setLoginState(LoginStateEnum.MOBILE)">
          {{ t('sys.login.mobileSignInFormTitle') }}
        </Button>
      </ACol>
      <ACol :md="8" :xs="24" class="!my-2 !md:my-0 xs:mx-0 md:mx-2">
        <Button block @click="setLoginState(LoginStateEnum.QR_CODE)">
          {{ t('sys.login.qrSignInFormTitle') }}
        </Button>
      </ACol>
      <ACol :md="7" :xs="24">
        <Button block @click="setLoginState(LoginStateEnum.REGISTER)">
          {{ t('sys.login.registerButton') }}
        </Button>
      </ACol>
    </ARow>

    <Divider class="enter-x">{{ t('sys.login.otherSignIn') }}</Divider>

    <div class="flex justify-evenly enter-x" :class="`${prefixCls}-sign-in-way`">
      <a @click="onThirdLogin('github')" title="github"><GithubFilled /></a>
      <a @click="onThirdLogin('wechat_enterprise')" title="企业微信"> <icon-font class="item-icon" type="icon-qiyeweixin3" /></a>
      <a @click="onThirdLogin('dingtalk')" title="钉钉"><DingtalkCircleFilled /></a>
      <a @click="onThirdLogin('wechat_open')" title="微信"><WechatFilled /></a>
    </div>
  </Form>
  <!-- 第三方登录相关弹框 -->
  <ThirdModal ref="thirdModalRef"></ThirdModal>
</template>
<script lang="ts" setup>
  import { reactive, ref, toRaw, unref, computed, onMounted } from 'vue';

  import { Checkbox, Form, Input, Row, Col, Button, Divider } from 'ant-design-vue';
  import { GithubFilled, WechatFilled, DingtalkCircleFilled, createFromIconfontCN } from '@ant-design/icons-vue';
  import LoginFormTitle from './LoginFormTitle.vue';
  import ThirdModal from './ThirdModal.vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useMessage } from '/@/hooks/web/useMessage';

  import { useUserStore } from '/@/store/modules/user';
  import { LoginStateEnum, useLoginState, useFormRules, useFormValid } from './useLogin';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { getCodeInfo } from '/@/api/sys/user';
  //import { onKeyStroke } from '@vueuse/core';

  const ACol = Col;
  const ARow = Row;
  const FormItem = Form.Item;
  const InputPassword = Input.Password;
  const IconFont = createFromIconfontCN({
    scriptUrl: '//at.alicdn.com/t/font_2316098_umqusozousr.js',
  });
  const { t } = useI18n();
  const { notification, createErrorModal } = useMessage();
  const { prefixCls } = useDesign('login');
  const userStore = useUserStore();

  const { setLoginState, getLoginState } = useLoginState();
  const { getFormRules } = useFormRules();

  const formRef = ref();
  const thirdModalRef = ref();
  const loading = ref(false);
  const rememberMe = ref(false);

  const formData = reactive({
    account: 'admin',
    password: '123456',
    inputCode: '',
  });
  const randCodeData = reactive({
    randCodeImage: '',
    requestCodeSuccess: false,
    checkKey: null,
  });

  const { validForm } = useFormValid(formRef);

  //onKeyStroke('Enter', handleLogin);

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.LOGIN);

  async function handleLogin() {
    const data = await validForm();
    if (!data) return;
    try {
      loading.value = true;
      const { userInfo } = await userStore.login(
        toRaw({
          password: data.password,
          username: data.account,
          captcha: data.inputCode,
          checkKey: randCodeData.checkKey,
          mode: 'none', //不要默认的错误提示
        })
      );
      if (userInfo) {
        notification.success({
          message: t('sys.login.loginSuccessTitle'),
          description: `${t('sys.login.loginSuccessDesc')}: ${userInfo.realname}`,
          duration: 3,
        });
      }
    } catch (error) {
      notification.error({
        message: t('sys.api.errorTip'),
        description: error.message || t('sys.api.networkExceptionMsg'),
        duration: 3,
      });
      loading.value = false;

      //update-begin-author:taoyan date:2022-5-3 for: issues/41 登录页面，当输入验证码错误时，验证码图片要刷新一下，而不是保持旧的验证码图片不变
      handleChangeCheckCode();
      //update-end-author:taoyan date:2022-5-3 for: issues/41 登录页面，当输入验证码错误时，验证码图片要刷新一下，而不是保持旧的验证码图片不变
    }
  }
  function handleChangeCheckCode() {
    formData.inputCode = '';
    //TODO 兼容mock和接口，暂时这样处理
    randCodeData.checkKey = 1629428467008; //new Date().getTime();
    getCodeInfo(randCodeData.checkKey).then((res) => {
      randCodeData.randCodeImage = res;
      randCodeData.requestCodeSuccess = true;
    });
  }

  /**
   * 第三方登录
   * @param type
   */
  function onThirdLogin(type) {
    thirdModalRef.value.onThirdLogin(type);
  }
  //初始化验证码
  onMounted(() => {
    handleChangeCheckCode();
  });
</script>
