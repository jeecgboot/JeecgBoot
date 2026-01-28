<!-- 应用门户 -->
<template>
  <div ref="portalRef" class="portal-container" :style="portalContainerStyle">
    <div class="leftArea" :class="[expand ? 'expand' : 'shrink']">
      <div class="content">
        <LeftPortalSession ref="leftPortalRef" @app-click="handleAppClick" @task-click="handleTaskClick"></LeftPortalSession>
      </div>
      <div class="toggle-btn" @click="handleToggle">
        <span class="icon">
          <svg viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path
              d="M5.64645 3.14645C5.45118 3.34171 5.45118 3.65829 5.64645 3.85355L9.79289 8L5.64645 12.1464C5.45118 12.3417 5.45118 12.6583 5.64645 12.8536C5.84171 13.0488 6.15829 13.0488 6.35355 12.8536L10.8536 8.35355C11.0488 8.15829 11.0488 7.84171 10.8536 7.64645L6.35355 3.14645C6.15829 2.95118 5.84171 2.95118 5.64645 3.14645Z"
              fill="currentColor"
            ></path>
          </svg>
        </span>
      </div>
    </div>
    <div class="rightArea" :class="[expand ? 'expand' : 'shrink']">
      <chat
        :key="uuid"
        url="/airag/chat/send"
        v-if="uuid"
        :uuid="uuid"
        :historyData="historyData"
        type="view"
        :formState="appData"
        :prologue="appData?.prologue"
        :presetQuestion="appData?.presetQuestion"
        @reload-message-title="reloadMessageTitle"
        :chatTitle="chatTitle"
        :conversationSettings="getCurrentSettings"
        @edit-settings="handleEditSettings"
        sessionType="portal"
        ref="chatRef"
      ></chat>
      <div v-if="showWelcome" class="emptyArea">
        <div class="welcome-text">{{ welcomeText }}</div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { computed, ref, watch } from 'vue';
  import LeftPortalSession from './LeftPortalSession.vue';
  import chat from '/@/views/super/airag/aiapp/chat/chat.vue';
  import { defHttp } from '@/utils/http/axios';
  import { useUserStore } from '@/store/modules/user';

  const portalContainerStyle = ref<any>(null);
  //应用门户的ref
  const portalRef = ref();
  //左侧列表是否展开
  const expand = ref<any>(true);
  //左侧列表展开关闭事件
  const handleToggle = () => {
    expand.value = !expand.value;
  };
  //随机id
  const uuid = ref<string>('');
  //历史记录
  const historyData = ref<any>();
  //应用数据
  const appData = ref<any>();
  //聊天标题
  const chatTitle = ref<any>();
  //当前会话的设置
  const conversationSettings = ref<Record<string, Record<string, any>>>({});
  // 获取当前会话的设置
  const getCurrentSettings = computed(() => {
    return conversationSettings.value[uuid.value] || {};
  });
  //对话设置弹窗ref
  const settingsModalRef = ref();
  //左侧会话的ref
  const leftPortalRef = ref();
  // 欢迎语（取当前登录用户姓名或用户名）
  const userStore = useUserStore();
  const welcomeName = computed(() => userStore.getUserInfo?.realname || userStore.getUserInfo?.username || '');
  const welcomeText = computed(() => (welcomeName.value ? `你好，${welcomeName.value}。准备好开始了吗？` : '你好，准备好开始了吗？'));
  const showWelcome = ref<boolean>(false);

  // 指定长度和基数
  const getUuid = (len = 10, radix = 16) => {
    var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
    var uuid: any = [],
      i;
    radix = radix || chars.length;

    if (len) {
      for (i = 0; i < len; i++) uuid[i] = chars[0 | (Math.random() * radix)];
    } else {
      var r;
      uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
      uuid[14] = '4';
      for (i = 0; i < 36; i++) {
        if (!uuid[i]) {
          r = 0 | (Math.random() * 16);
          uuid[i] = chars[i == 19 ? (r & 0x3) | 0x8 : r];
        }
      }
    }
    return uuid.join('');
  };

  /**
   * 重新加载标题
   * @param title
   */
  function reloadMessageTitle(title) {
    showWelcome.value = false;
    leftPortalRef.value.addSession(title, uuid.value);
  }

  // 编辑对话设置
  function handleEditSettings() {
    if (settingsModalRef.value) {
      settingsModalRef.value.open();
    }
  }

  /**
   * 应用数据点击返回事件
   *
   * @param value
   */
  function handleAppClick(value) {
    //每次点击都是一个新的会话
    uuid.value = getUuid(32);
    appData.value = value;
    chatTitle.value = appData.value.name;
    showWelcome.value = true;
  }

  /**
   * 回话点击事件
   *
   * @param id
   */
  function handleTaskClick(id, title) {
    showWelcome.value = false;
    uuid.value = id;
    chatTitle.value = title;
    //根据选中的id查询聊天内容
    let params = { conversationId: id, sessionType: 'portal' };
    //根据id获取历史记录
    defHttp.get({ url: '/airag/chat/messages', params }, { isTransformResponse: false }).then((res) => {
      if (res.success) {
        // 处理新的返回格式（包含messages和flowInputs）
        if (res.result && res.result.messages) {
          historyData.value = res.result.messages;
          if (res.result?.appData) {
            appData.value = res.result.appData;
          } else {
            appData.value = null;
          }
          // 加载已保存的设置
          if (res.result.flowInputs) {
            conversationSettings.value[id] = res.result.flowInputs;
          }
        } else if (Array.isArray(res.result)) {
          // 兼容旧格式
          historyData.value = res.result;
        } else {
          historyData.value = [];
        }
      } else {
        historyData.value = [];
      }
    });
  }

  // 当 uuid 变化时，清空右侧聊天的会话数据并初始化当前设置，确保开启全新会话
  watch(
    () => uuid.value,
    (newVal) => {
      if (!newVal) return;
      // 清空标题与历史记录、防止沿用上一会话数据
      historyData.value = [];
      // 初始化当前会话设置容器
      conversationSettings.value[newVal] = conversationSettings.value[newVal] || {};
    }
  );

  watch(
    () => portalRef.value,
    () => {
      if (portalRef.value.offsetHeight) {
        portalRef.value = { height: `${portalRef.value.offsetHeight} px` };
      }
    }
  );
</script>

<style lang="less" scoped>
  @width: 260px;
  .portal-container {
    height: 100%;
    width: 100%;
    position: absolute;
    background: white;
    display: flex;
    overflow: hidden;
    z-index: 800;
    border: 1px solid #eeeeee;
  }
  .leftArea {
    width: @width;
    transition: 0.3s left;
    position: absolute;
    left: 0;
    height: 100%;

    .content {
      width: 100%;
      height: 100%;
      overflow: hidden;
    }

    &.shrink {
      left: -@width;

      .toggle-btn {
        .icon {
          transform: rotate(0deg);
        }
      }
    }

    .toggle-btn {
      transition:
        color 0.3s cubic-bezier(0.4, 0, 0.2, 1),
        right 0.3s cubic-bezier(0.4, 0, 0.2, 1),
        left 0.3s cubic-bezier(0.4, 0, 0.2, 1),
        border-color 0.3s cubic-bezier(0.4, 0, 0.2, 1),
        background-color 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      cursor: pointer;
      width: 24px;
      height: 24px;
      position: absolute;
      top: 50%;
      right: 0;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 18px;
      color: rgb(51, 54, 57);
      border: 1px solid rgb(239, 239, 245);
      background-color: #fff;
      box-shadow: 0 2px 4px 0px #e7e9ef;
      transform: translateX(50%) translateY(-50%);
      z-index: 1;
    }

    .icon {
      transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      transform: rotate(180deg);
      font-size: 18px;
      height: 18px;

      svg {
        height: 1em;
        width: 1em;
        vertical-align: top;
      }
    }
  }

  .rightArea {
    margin-left: @width;
    transition: 0.3s margin-left;

    &.shrink {
      margin-left: 0;
    }

    flex: 1;
    min-width: 0;
  }

  .emptyArea {
    position: absolute;
    top: 45%;
    left: 45%;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #d4d4d4;
  }
  .emptyArea .welcome-text {
    font-size: 32px;
    color: #3d4353;
    font-weight: 600;
  }
</style>
