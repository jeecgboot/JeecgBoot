<template>
  <div class="wrap">
    <div class="content">
      <AiChat></AiChat>
    </div>
  </div>
</template>

<script setup>
  import AiChat from '/@/views/super/airag/aiapp/chat/AiChat.vue';
  
</script>

<style lang="less" scoped>
  .wrap {
    height: 100%;
    width: 100%;
    .content {
      background: #fff;
      width: 100%;
      height: 100%;
    }
  }
</style>
