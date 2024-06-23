<template>
  <div> </div>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { isOAuth2AppEnv, sysOAuth2Login } from '/@/views/sys/login/useLogin';
  import { useRouter } from 'vue-router';
  import { PageEnum } from '/@/enums/pageEnum';
  import { router } from '/@/router';
  import { useUserStore } from '/@/store/modules/user';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useI18n } from '/@/hooks/web/useI18n';
  import {getTenantId} from "/@/utils/auth";

  const isOAuth = ref<boolean>(isOAuth2AppEnv());
  const env = ref<any>({ thirdApp: false, wxWork: false, dingtalk: false });
  const { currentRoute } = useRouter();
  const route = currentRoute.value;
  if (!isOAuth2AppEnv()) {
    router.replace({ path: PageEnum.BASE_LOGIN, query: route.query });
  }

  if (isOAuth.value) {
    checkEnv();
  }

  /**
   * 检测当前的环境
   */
  function checkEnv() {
    // 判断当时是否是企业微信环境
    if (/wxwork/i.test(navigator.userAgent)) {
      env.value.thirdApp = true;
      env.value.wxWork = true;
    }
    // 判断当时是否是钉钉环境
    if (/dingtalk/i.test(navigator.userAgent)) {
      env.value.thirdApp = true;
      env.value.dingtalk = true;
    }
    doOAuth2Login();
  }

  /**
   * 进行OAuth2登录操作
   */
  function doOAuth2Login() {
    if (env.value.thirdApp) {
      // 判断是否携带了Token，是就说明登录成功
      if (route.query.oauth2LoginToken) {
        let token = route.query.oauth2LoginToken;
        //执行登录操作
        thirdLogin({ token, thirdType: route.query.thirdType,tenantId: getTenantId });
      } else if (env.value.wxWork) {
        sysOAuth2Login('wechat_enterprise');
      } else if (env.value.dingtalk) {
        sysOAuth2Login('dingtalk');
      }
    }
  }

  /**
   * 第三方登录
   * @param params
   */
  function thirdLogin(params) {
    const userStore = useUserStore();
    const { notification } = useMessage();
    const { t } = useI18n();
    userStore.ThirdLogin(params).then((res) => {
      if (res && res.userInfo) {
        notification.success({
          message: t('sys.login.loginSuccessTitle'),
          description: `${t('sys.login.loginSuccessDesc')}: ${res.userInfo.realname}`,
          duration: 3,
        });
      } else {
        notification.error({
          message: t('sys.login.errorTip'),
          description: ((res.response || {}).data || {}).message || res.message || t('sys.login.networkExceptionMsg'),
          duration: 4,
        });
      }
    });
  }
</script>
