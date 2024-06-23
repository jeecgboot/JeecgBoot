<template>
  <div class="wrap">
    <div class="content">
      <AiChat></AiChat>
    </div>
  </div>
</template>

<script setup>
  import AiChat from '/@/components/jeecg/AiChat/index.vue';
  
</script>

<style lang="less" scoped>
  .wrap {
    height: 100%;
    width: 100%;
    padding: 20px;
    .content {
      background: #fff;
      width: 100%;
      height: 100%;
      padding: 20px;
    }
  }
</style>
