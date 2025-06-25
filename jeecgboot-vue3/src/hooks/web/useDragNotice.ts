import { ref, nextTick, getCurrentInstance, watch } from 'vue';
import { getToken } from '/@/utils/auth';
import md5 from 'crypto-js/md5';
import { connectWebSocket, onWebSocket } from '/@/hooks/web/useWebSocket';
import { useGlobSetting } from '/@/hooks/setting';
import { useModal } from '/@/components/Modal';
import { useUserStore } from '/@/store/modules/user';
import { isUrl } from '@/utils/is';
import { getQueryVariable, getUrlParams } from '@/utils';
import { useRouter } from 'vue-router';
import { useMessage } from '@/hooks/web/useMessage';
const { createMessage } = useMessage();
export function useDragNotice() {
  //*********************************websocket配置begin******************************************
  const glob = useGlobSetting();
  const { push, currentRoute } = useRouter();
  const userStore = useUserStore();
  const instance: any = getCurrentInstance();
  // 初始化 WebSocket
  function initWebSocket() {
    const token = getToken();
    //将登录token生成一个短的标识
    const wsClientId = md5(token);
    // WebSocket与普通的请求所用协议有所不同，ws等同于http，wss等同于https
    const url = glob.domainUrl?.replace('https://', 'wss://').replace('http://', 'ws://') + '/dragChannelSocket/' + wsClientId;
    connectWebSocket(url);
    onWebSocket(onWebSocketMessage);
  }

  async function onWebSocketMessage(data) {
    console.log('仪表盘监听按钮点击事件websocket', data);
    if (data?.CMD === 'drag') {
      //触发动作： url：路径 modal：弹窗
      const action = data.result.action;
      //弹窗类型： 点击按钮打开什么弹窗，根据type打开不同的弹窗
      const type = data.result.type;
      //url地址，可以是路由，也可以是外部链接
      let url = data.result.url;
      //弹窗参数或者url参数
      const record = data.result.records || {};
      console.log('仪表盘监听点击事件类型type', type);
      console.log('仪表盘监听点击事件动作action', action);
      console.log('仪表盘监听点击事件路径url', url);
      console.log('仪表盘监听点击事件参数', record);
      //1.路径的话，判断外部链接还是内部路由跳转
      if (action == 'url') {
        //常用下載特殊处理
        if (url == 'fileUrl') {
          url = record[url];
        }
        const urlParamsObj = getUrlParams(url);
        if (url.startsWith('http')) {
          window.open(url, '_blank');
        } else {
          push({ path: urlParamsObj.url, query: { ...urlParamsObj.params, ...record } });
        }
      } else {
        //2.弹窗方式打开项目组件
        switch (type) {
          case 'email':
            //邮箱查看弹窗
            handleOpenType('email', { record });
            break;
          default:
            break;
        }
      }
    }
  }
  //*********************************websocket配置end******************************************

  //*********************************打开弹窗修改，动态设置弹窗begin*******************************
  //当前表单弹窗
  const currentModal = ref<string | null>(null);
  //当前表单参数
  const modalParams = ref<Recordable>({});
  //表单注册缓存
  const modalRegCache = ref<Recordable>({});
  //组件绑定参数
  const bindParams = ref<Recordable>({});
  /**
   * 根据类型打开不同弹窗
   * @param type
   * @param params
   */
  async function handleOpenType(type, params) {
    currentModal.value = null;
    modalParams.value = { ...params };
    switch (type) {
      case 'email':
        //邮件查看
        currentModal.value = 'EoaMailBoxInModal';
        break;
      default:
        currentModal.value = null;
        break;
    }
    //注册表单弹窗
    initModalRegister();
    await nextTick(() => {
      if (modalRegCache.value[currentModal.value!]?.isRegister) {
        console.log('已注冊，走缓存');
        modalRegCache.value[currentModal.value!].modalMethods.openModal(true, modalParams.value);
      }
    });
  }
  /**
   * 初始化弹窗注册
   */
  function initModalRegister() {
    //如果当前选择表单为null，就不处理
    if (!currentModal.value) {
      return;
    }
    //判断缓存中是否存在，不存在就走缓存逻辑
    if (!modalRegCache.value[currentModal.value]) {
      const [registerModal, modalMethods] = useModal();
      modalRegCache.value[currentModal.value] = {
        isRegister: false,
        register: bindRegisterModal(registerModal, modalMethods),
        modalMethods,
      };
    }
  }

  /**
   * 绑定注册弹窗
   * @param regFn
   * @param modalMethod
   */
  function bindRegisterModal(regFn, modalMethod) {
    return async (...args) => {
      console.log('开始注册：', currentModal.value);
      await regFn(...args);
      console.log('注册完成：', currentModal.value);
      //打开弹窗
      modalMethod.openModal(true, modalParams.value);
      //设置缓存标识
      modalRegCache.value[currentModal.value!].isRegister = true;
    };
  }
  //*********************************打开弹窗修改，动态设置弹窗end******************************************
  //刷新页面
  function reloadPage() {
    const iframes: any = document.getElementsByClassName('jeecg-iframe-page__main');
    // 将 HTMLCollection 转换为数组
    const iframeArray = Array.from(iframes);
    if (currentRoute.value?.meta?.frameSrc && currentRoute.value?.meta?.frameSrc.indexOf('/drag/view?pageId=') >= 0) {
      const targetIframe: any = iframeArray.find((iframe: any) => iframe.src == currentRoute.value?.meta?.frameSrc);
      console.log('targetIframe', targetIframe);
      if (targetIframe) {
        targetIframe.contentWindow.postMessage({ reload: true }, '*');
      }
    }
  }
  return {
    initDragWebSocket: initWebSocket,
    handleOpenType,
    currentModal,
    modalParams,
    modalRegCache,
    bindParams,
    reloadPage,
  };
}
