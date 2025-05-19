<template>
  <div class="footer">
    <div v-if="!showChat" class="footer-icon" @click="chatClick">
      <Icon icon="ant-design:comment-outlined" size="22"></Icon>
    </div>
    <div v-if="showChat" class="footer-close-icon" @click="chatClick">
      <Icon icon="ant-design:close-outlined" size="20"></Icon>
    </div>
    <div v-if="showChat" class="ai-chat">
      <AiChat ref="aiChatRef"></AiChat>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { onMounted, ref } from 'vue';
  import AiChat from './AiChat.vue';
  import { useRouter } from 'vue-router';

  //aiChat的ref
  const aiChatRef = ref();
  //应用id
  const appId = ref<string>('');

  //是否显示聊天
  const showChat = ref<any>(false);
  const router = useRouter();
  //判断是否为初始化
  const isInit = ref<boolean>(false);
  
  /**
   * chat图标点击事件
   */
  function chatClick() {
    showChat.value = !showChat.value;
    if(showChat.value && !isInit.value){
      setTimeout(()=>{
        isInit.value = true;
        aiChatRef.value.initChat(appId.value);
      },100)
    }
  }

  onMounted(() => {
    let params: any = router.currentRoute.value.params;
    appId.value = params?.appId;
    isInit.value = false;
  });
</script>

<style scoped lang="less">
  .footer {
    position: fixed;
    bottom: 16px;
    right: 16px;
    left: unset;
    top: unset;

    .footer-icon {
      cursor: pointer;
      background-color: #155eef;
      color: white;
      border-radius: 100%;
      padding: 20px;
      width: 48px;
      height: 48px;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: rgba(0, 0, 0, 0.2) 0 4px 8px 0;
    }
    .footer-close-icon {
      color: #0a3069;
      height: 48px;
      position: absolute;
      right: 10px;
      top: 20px;
      cursor: pointer;
      z-index: 9999;
    }
    .ai-chat {
      border: 1px solid #eeeeee;
      width: calc(100vh - 20px);
      height: calc(100vh - 200px);
    }
  }
</style>
