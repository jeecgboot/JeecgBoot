import { ref, unref } from 'vue';
import { defHttp } from '/@/utils/http/axios';
import { useGlobSetting } from '/@/hooks/setting';
import { useMessage } from '/@/hooks/web/useMessage';
import { useUserStore } from '/@/store/modules/user';
import { setThirdCaptcha, getCaptcha } from '/@/api/sys/user';
import { useI18n } from '/@/hooks/web/useI18n';

export function useThirdLogin() {
  const { createMessage, notification } = useMessage();
  const { t } = useI18n();
  const glob = useGlobSetting();
  const userStore = useUserStore();
  //第三方类型
  const thirdType = ref('');
  //第三方登录相关信息
  const thirdLoginInfo = ref<any>({});
  //状态
  const thirdLoginState = ref(false);
  //绑定手机号弹窗
  const bindingPhoneModal = ref(false);
  //第三方用户UUID
  const thirdUserUuid = ref('');
  //提示窗
  const thirdConfirmShow = ref(false);
  //绑定密码弹窗
  const thirdPasswordShow = ref(false);
  //绑定密码
  const thirdLoginPassword = ref('');
  //绑定用户
  const thirdLoginUser = ref('');
  //加载中
  const thirdCreateUserLoding = ref(false);
  //绑定手机号
  const thirdPhone = ref('');
  //验证码
  const thirdCaptcha = ref('');
  //第三方登录
  function onThirdLogin(source) {
    let url = `${glob.uploadUrl}/sys/thirdLogin/render/${source}`;
    const openWin = window.open(
      url,
      `login ${source}`,
      'height=500, width=500, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=n o, status=no'
    );
    thirdType.value = source;
    thirdLoginInfo.value = {};
    thirdLoginState.value = false;
    let receiveMessage = function (event) {
      let token = event.data;
      if (typeof token === 'string') {
        //如果是字符串类型 说明是token信息
        if (token === '登录失败') {
          createMessage.warning(token);
        } else if (token.includes('绑定手机号')) {
          bindingPhoneModal.value = true;
          let strings = token.split(',');
          thirdUserUuid.value = strings[1];
        } else {
          doThirdLogin(token);
        }
      } else if (typeof token === 'object') {
        //对象类型 说明需要提示是否绑定现有账号
        if (token['isObj'] === true) {
          thirdConfirmShow.value = true;
          thirdLoginInfo.value = { ...token };
        }
      } else {
        createMessage.warning('不识别的信息传递');
      }
      // update-begin--author:liaozhiyang---date:20240717---for：【TV360X-1827】mac系统谷歌浏览器企业微信第三方登录成功后没有弹出绑定手机弹窗
      if (openWin?.closed) {
        window.removeEventListener('message', receiveMessage, false);
      }
      // update-end--author:liaozhiyang---date:20240717---for：【TV360X-1827】mac系统谷歌浏览器企业微信第三方登录成功后没有弹出绑定手机弹窗
    };
    // update-begin--author:liaozhiyang---date:20240717---for：【TV360X-1827】mac系统谷歌浏览器企业微信第三方登录成功后没有弹出绑定手机弹窗
    window.removeEventListener('message', receiveMessage, false);
    // update-end--author:liaozhiyang---date:20240717---for：【TV360X-1827】mac系统谷歌浏览器企业微信第三方登录成功后没有弹出绑定手机弹窗
    window.addEventListener('message', receiveMessage, false);
  }
  // 根据token执行登录
  function doThirdLogin(token) {
    if (unref(thirdLoginState) === false) {
      thirdLoginState.value = true;
      userStore.ThirdLogin({ token, thirdType: unref(thirdType) }).then((res) => {
        console.log('res====>doThirdLogin', res);
        if (res && res.userInfo) {
          notification.success({
            message: t('sys.login.loginSuccessTitle'),
            description: `${t('sys.login.loginSuccessDesc')}: ${res.userInfo.realname}`,
            duration: 3,
          });
        } else {
          requestFailed(res);
        }
      });
    }
  }

  function requestFailed(err) {
    notification.error({
      message: '登录失败',
      description: ((err.response || {}).data || {}).message || err.message || '请求出现错误，请稍后再试',
      duration: 4,
    });
  }
  // 绑定已有账号 需要输入密码
  function thirdLoginUserBind() {
    thirdLoginPassword.value = '';
    thirdLoginUser.value = thirdLoginInfo.value.uuid;
    thirdConfirmShow.value = false;
    thirdPasswordShow.value = true;
  }
  //创建新账号
  function thirdLoginUserCreate() {
    thirdCreateUserLoding.value = true;
    // 账号名后面添加两位随机数
    thirdLoginInfo.value.suffix = parseInt(Math.random() * 98 + 1);
    defHttp
      .post({ url: '/sys/third/user/create', params: { thirdLoginInfo: unref(thirdLoginInfo) } }, { isTransformResponse: false })
      .then((res) => {
        if (res.success) {
          let token = res.result;
          doThirdLogin(token);
          thirdConfirmShow.value = false;
        } else {
          createMessage.warning(res.message);
        }
      })
      .finally(() => {
        thirdCreateUserLoding.value = false;
      });
  }
  // 核实密码
  function thirdLoginCheckPassword() {
    let params = Object.assign({}, unref(thirdLoginInfo), { password: unref(thirdLoginPassword) });
    defHttp.post({ url: '/sys/third/user/checkPassword', params }, { isTransformResponse: false }).then((res) => {
      if (res.success) {
        thirdLoginNoPassword();
        doThirdLogin(res.result);
      } else {
        createMessage.warning(res.message);
      }
    });
  }
  // 没有密码 取消操作
  function thirdLoginNoPassword() {
    thirdPasswordShow.value = false;
    thirdLoginPassword.value = '';
    thirdLoginUser.value = '';
  }

  //倒计时执行前的函数
  function sendCodeApi() {
    //return setThirdCaptcha({mobile:unref(thirdPhone)});
    return getCaptcha({ mobile: unref(thirdPhone), smsmode: '0' });
  }
  //绑定手机号点击确定按钮
  function thirdHandleOk() {
    if (!unref(thirdPhone)) {
      cmsFailed('请输入手机号');
    }
    if (!unref(thirdCaptcha)) {
      cmsFailed('请输入验证码');
    }
    let params = {
      mobile: unref(thirdPhone),
      captcha: unref(thirdCaptcha),
      thirdUserUuid: unref(thirdUserUuid),
    };
    defHttp.post({ url: '/sys/thirdLogin/bindingThirdPhone', params }, { isTransformResponse: false }).then((res) => {
      if (res.success) {
        bindingPhoneModal.value = false;
        doThirdLogin(res.result);
      } else {
        createMessage.warning(res.message);
      }
    });
  }
  function cmsFailed(err) {
    notification.error({
      message: '登录失败',
      description: err,
      duration: 4,
    });
    return;
  }
  //返回数据和方法
  return {
    thirdPasswordShow,
    thirdLoginCheckPassword,
    thirdLoginNoPassword,
    thirdLoginPassword,
    thirdConfirmShow,
    thirdCreateUserLoding,
    thirdLoginUserCreate,
    thirdLoginUserBind,
    bindingPhoneModal,
    thirdHandleOk,
    thirdPhone,
    thirdCaptcha,
    onThirdLogin,
    sendCodeApi,
  };
}
