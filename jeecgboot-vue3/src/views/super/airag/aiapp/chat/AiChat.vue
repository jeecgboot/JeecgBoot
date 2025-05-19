<template>
  <div ref="chatContainerRef" class="chat-container" :style="chatContainerStyle">
    <template v-if="dataSource">
      <div class="leftArea" :class="[expand ? 'expand' : 'shrink']">
        <div class="content">
          <slide v-if="uuid" :dataSource="dataSource" @save="handleSave" :prologue="prologue" :appData="appData" @click="handleChatClick"></slide>
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
          url="/airag/chat/send"
          v-if="uuid && chatVisible"
          :uuid="uuid"
          :historyData="chatData"
          type="view"
          @save="handleSave"
          :formState="appData"
          :prologue="prologue"
          :presetQuestion="presetQuestion"
          @reload-message-title="reloadMessageTitle"
          :chatTitle="chatTitle"
          :quickCommandData="quickCommandData"
        ></chat>
      </div>
    </template>
    <Spin v-else :spinning="true"></Spin>
  </div>
</template>

<script setup lang="ts">
  import slide from './slide.vue';
  import chat from './chat.vue';
  import { Spin } from 'ant-design-vue';
  import { ref, watch, nextTick, onUnmounted, onMounted } from 'vue';
  import { useUserStore } from '/@/store/modules/user';
  import { JEECG_CHAT_KEY } from '/@/enums/cacheEnum';
  import { defHttp } from '/@/utils/http/axios';
  import { useRouter } from 'vue-router';

  const router = useRouter();
  const userId = useUserStore().getUserInfo?.id;
  const localKey = JEECG_CHAT_KEY + userId;
  let timer: any = null;
  let unwatch01: any = null;
  const dataSource = ref<any>({});
  const uuid = ref<string>('');
  const chatData = ref<any>([]);
  const expand = ref<any>(true);
  const chatVisible = ref(true);
  const chatContainerRef = ref<any>(null);
  const chatContainerStyle = ref({});
  //左侧聊天信息
  const chatTitle = ref<string>('');
  //左侧聊天点击的坐标
  const chatActiveKey = ref<number>(0);
  //预置开场白
  const presetQuestion = ref<string>('');

  const handleToggle = () => {
    expand.value = !expand.value;
  };
  //应用id
  const appId = ref<string>('');
  //应用数据
  const appData = ref<any>({});
  //开场白
  const prologue = ref<string>('');
  //快捷指令
  const quickCommandData = ref<any>([]);

  const priming = () => {
    dataSource.value = {
      active: '1002',
      usingContext: true,
      history: [{ id: '1002', title: '新建聊天', isEdit: false, disabled: true }],
    };
    chatTitle.value = '新建聊天';
    chatActiveKey.value = 0;
  };

  const handleSave = () => {
    // 删除标签或清空内容之后的保存
    //save(dataSource.value);
    setTimeout(() => {
      // 删除标签或清空内容也会触发watch保存，此时不需watch保存需清除
      //clearTimeout(timer);
    }, 50);
  };

  // 监听dataSource变化执行操作
  const execute = () => {
    unwatch01 = watch(
      () => dataSource.value.active,
      (value) => {
        if (value) {
          if (value == '1002') {
            uuid.value = '1002';
            chatData.value = [];
            chatTitle.value = "新建聊天";
            chatVisible.value = false;
            nextTick(() => {
              chatVisible.value = true;
            });
            return;
          }
          //update-begin---author:wangshuai---date:2025-03-14---for:【QQYUN-11421】聊天，删除会话后，聊天切换到新的会话，但是聊天标题没有变---
          let values = dataSource.value.history.filter((item) => item.id === value);
          if(values && values.length>0){
            chatTitle.value = values[0]?.title
          }
          //update-end---author:wangshuai---date:2025-03-14---for:【QQYUN-11421】聊天，删除会话后，聊天切换到新的会话，但是聊天标题没有变---
          //根据选中的id查询聊天内容
          let params = { conversationId: value };
          uuid.value = value;
          defHttp.get({ url: '/airag/chat/messages', params }, { isTransformResponse: false }).then((res) => {
            if (res.success) {
              chatData.value = res.result;
            } else {
              chatData.value = [];
            }
            chatVisible.value = false;
            nextTick(() => {
              chatVisible.value = true;
            });
          });
        }else{
          chatData.value = [];
          chatTitle.value = "";
        }
      },
      { immediate: true }
    );
  };

  /**
   * 初始化聊天信息
   * @param appId
   */
  function initChartData(appId = '') {
    defHttp
      .get(
        {
          url: '/airag/chat/conversations',
          params: { appId: appId },
        },
        { isTransformResponse: false }
      )
      .then((res) => {
        if (res.success && res.result && res.result.length > 0) {
          dataSource.value.history = res.result;
          dataSource.value.active = res.result[0].id;
          chatTitle.value = res.result[0].title;
          chatActiveKey.value = 0;
        } else {
          priming();
        }
        !unwatch01 && execute();
      })
      .catch(() => {
        priming();
      });
  }

  onMounted(() => {
    let params: any = router.currentRoute.value.params;
    if (params.appId) {
      appId.value = params.appId;
      getApplicationData(params.appId);
      initChartData(params.appId);
    } else {
      initChartData();
      quickCommandData.value = [
          { name: '请介绍一下JeecgBoot', descr: "请介绍一下JeecgBoot" },
          { name: 'JEECG有哪些优势？', descr: "JEECG有哪些优势？" },
          { name: 'JEECG可以做哪些事情？', descr: "JEECG可以做哪些事情？" },];
    }
  });

  onUnmounted(() => {
    chatData.value = [];
    chatTitle.value = "";
    prologue.value = ""
    presetQuestion.value = "";
    quickCommandData.value = [];
  })
  
  /**
   * 获取应用id
   *
   * @param appId
   */
  async function getApplicationData(appId) {
    await defHttp
      .get(
        {
          url: '/airag/app/queryById',
          params: { id: appId },
        },
        { isTransformResponse: false }
      )
      .then((res) => {
        if (res.success) {
          appData.value = res.result;
          if (res.result && res.result.prologue) {
            prologue.value = res.result.prologue;
          }  
          if (res.result && res.result.quickCommand) {
            quickCommandData.value = JSON.parse(res.result.quickCommand);
          } 
          if (res.result && res.result.presetQuestion) {
            presetQuestion.value = res.result.presetQuestion;
          }
        } else {
          appData.value = {};
        }
      });
  }

  /**
   * 左侧消息列表点击事件
   * @param title
   * @param index
   */
  function handleChatClick(title, index) {
    chatTitle.value = title;
    chatActiveKey.value = index;
  }

  /**
   * 重新加载标题消息
   * @param text
   */
  function reloadMessageTitle(text) {
    let title = dataSource.value.history[chatActiveKey.value].title;
    if(title === '新建聊天'){
      dataSource.value.history[chatActiveKey.value].title = text;
      dataSource.value.history[chatActiveKey.value]['disabled'] = false;
    }

  }
  
  /**
   * 初始化聊天：用于icon点击
   */
  function initChat(value) {
    appId.value = value;
    getApplicationData(value);
    initChartData(value);
  }
  
  defineExpose({
    initChat
  })

  onUnmounted(() => {
    unwatch01 && unwatch01();
  });

  watch(
    () => chatContainerRef.value,
    () => {
      if(chatContainerRef.value.offsetHeight){
        chatContainerStyle.value = { height: `${chatContainerRef.value.offsetHeight} px` };
      }
    }
  );
</script>

<style scoped lang="less">
  @width: 260px;
  .chat-container {
    height: 100%;
    width: 100%;
    position: relative;
    background: white;
    display: flex;
    overflow: hidden;
    z-index: 999;
    border: 1px solid #eeeeee;
    :deep(.ant-spin) {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
    }
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
      box-shadow: 0 2px 4px 0px rgba(0, 0, 0, 0.06);
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
</style>
