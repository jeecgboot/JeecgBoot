<template>
  <div ref="chatContainerRef" class="chat-container" :style="chatContainerStyle">
    <template v-if="dataSource">
      <div class="leftArea" :class="[expand ? 'expand' : 'shrink']">
        <div class="content">
          <slide v-if="uuid" :dataSource="dataSource"></slide>
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
        <chat v-if="uuid && chatVisible" :uuid="uuid" :chatData="chatData" :dataSource="dataSource"></chat>
      </div>
    </template>
    <Spin v-else :spinning="true"></Spin>
  </div>
</template>

<script setup lang="ts">
  import slide from './components/slide.vue';
  import chat from './components/chat.vue';
  import { Spin } from 'ant-design-vue';
  import { ref, watch, nextTick, onUnmounted } from 'vue';
  import { useUserStore } from '/@/store/modules/user';
  import { JEECG_CHAT_KEY } from '/@/enums/cacheEnum';
  import { defHttp } from '/@/utils/http/axios';
  const configUrl = {
    get: '/test/ai/chat/history/get',
    save: '/test/ai/chat/history/save',
  };
  const userId = useUserStore().getUserInfo?.id;
  const localKey = JEECG_CHAT_KEY + userId;
  let timer: any = null;
  let unwatch01: any = null;
  let unwatch02: any = null;
  const dataSource = ref<any>(null);
  const uuid = ref(null);
  const chatData = ref([]);
  const expand = ref<any>(true);
  const chatVisible = ref(true);
  const chatContainerRef = ref<any>(null);
  const chatContainerStyle = ref({});
  const handleToggle = () => {
    expand.value = !expand.value;
  };
  // 初始查询历史
  const init = () => {
    const priming = () => {
      dataSource.value = {
        active: 1002,
        usingContext: true,
        history: [{ uuid: 1002, title: '新建聊天', isEdit: false }],
        chat: [{ uuid: 1002, data: [] }],
      };
    };
    defHttp
      .get({ url: configUrl.get })
      .then((res) => {
        const { content } = res;
        if (content) {
          const json = JSON.parse(content);
          if (json.history?.length) {
            dataSource.value = json;
          } else {
            priming();
          }
        } else {
          priming();
        }
        !unwatch01 && execute();
      })
      .catch(() => {
        priming();
      });
  };
  const save = (content) => {
    defHttp.post({ url: configUrl.save, params: { content: JSON.stringify(content) } }, { isTransformResponse: false });
  };
  // 监听dataSource变化执行操作
  const execute = () => {
    unwatch01 = watch(
      () => dataSource.value.active,
      (value) => {
        if (value) {
          const findItem = dataSource.value.chat.find((item) => item.uuid === value);
          if (findItem) {
            uuid.value = findItem.uuid;
            chatData.value = findItem.data;
          }
          chatVisible.value = false;
          nextTick(() => {
            chatVisible.value = true;
          });
        }
      },
      { immediate: true }
    );
    unwatch02 = watch(dataSource.value, () => {
      clearInterval(timer);
      timer = setTimeout(() => {
        save(dataSource.value);
      }, 2e3);
    });
  };
  onUnmounted(() => {
    unwatch01 && unwatch01();
    unwatch02 && unwatch02();
  });
  watch(
    () => chatContainerRef.value,
    () => {
      chatContainerStyle.value = { height: `${chatContainerRef.value.offsetHeight}px` };
    }
  );
  init();
</script>

<style scoped lang="less">
  @width: 260px;
  .chat-container {
    position: relative;
    height: 100%;
    box-shadow:
      0 0 #0000,
      0 0 #0000,
      0 0 #0000,
      0 0 #0000,
      0 4px 6px -1px rgb(0 0 0 / 0.1),
      0 2px 4px -2px rgb(0 0 0 / 0.1);
    border-width: 1px;
    border-radius: 0.375rem;
    display: flex;
    overflow: hidden;
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
