<template>
  <div> </div>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { isOAuth2AppEnv, sysOAuth2Callback, sysOAuth2Login } from '/@/views/sys/login/useLogin';
  import { useRouter } from 'vue-router';
  import { PageEnum } from '/@/enums/pageEnum';
  import { router } from '/@/router';
  import { useUserStore } from '/@/store/modules/user';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { getAuthCache, getTenantId, getToken } from "/@/utils/auth";
  import { requestAuthCode } from 'dingtalk-jsapi';
  import { defHttp } from '/@/utils/http/axios';
  import { OAUTH2_THIRD_LOGIN_TENANT_ID } from "/@/enums/cacheEnum";

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
        //新版钉钉登录
        dingdingLogin();
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

  /**
   * 钉钉登录
   */
  function dingdingLogin() {
    //先获取钉钉的企业id，如果没有配置 还是走原来的逻辑，走原来的逻辑 需要判断存不存在token，存在token直接去首页
    let tenantId = getAuthCache(OAUTH2_THIRD_LOGIN_TENANT_ID) || 0;
    let url = `/sys/thirdLogin/get/corpId/clientId?tenantId=${tenantId}`;
    //update-begin---author:wangshuai---date:2024-12-09---for:不要使用getAction online里面的，要用defHttp---
    defHttp.get({ url:url },{ isTransformResponse: false }).then((res) => {
    //update-end---author:wangshuai---date:2024-12-09---for:不要使用getAction online里面的，要用defHttp---
        if (res.success) {
          if(res.result && res.result.corpId && res.result.clientId){
            requestAuthCode({ corpId: res.result.corpId, clientId: res.result.clientId }).then((res) => {
              let { code } = res;
              sysOAuth2Callback(code);
            });
          }else{
            toOldAuthLogin();
          }
        } else {
          toOldAuthLogin();
        }
      }).catch((err) => {
        toOldAuthLogin();
      });
  }
  
  /**
   * 旧版钉钉登录
   */
  function toOldAuthLogin() {
    let token = getToken();
    if (token) {
      router.replace({ path: PageEnum.BASE_HOME });
    } else {
      sysOAuth2Login('dingtalk');
    }
  }
</script>
