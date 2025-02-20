<template>
  <div v-if="visible" ref="aideWrapRef" class="aide-wrap" @click="handleGo">
    <a-popconfirm
      :open="popconfirmVisible"
      placement="topRight"
      title="确定AI助手退出吗？"
      ok-text="确定"
      cancel-text="取消"
      @cancel="handleCancel"
      @confirm="handleConfirm"
    >
      <img :src="aiImage" alt="ai助手" />
    </a-popconfirm>
  </div>
</template>

<script setup>
  import { onMounted, ref } from 'vue';
  import { router } from '/@/router';
  import { AIDE_FLAG } from '/@/enums/cacheEnum';
  import { getToken } from '/@/utils/auth';
  import { getAuthCache, setAuthCache, removeAuthCache } from '/@/utils/auth';
  import aiImage from './images/ai.png';
  const visible = ref(1);
  const aideWrapRef = ref(null);
  const popconfirmVisible = ref(false);

  onMounted(() => {
    const aideFlag = getAuthCache(AIDE_FLAG);
    if (aideFlag && aideFlag == getToken()) {
      visible.value = false;
    } else {
      visible.value = true;
    }
    if (visible.value) {
      aideWrapRef.value.addEventListener('contextmenu', (e) => {
        popconfirmVisible.value = true;
        e.preventDefault();
      });
    }
  });
  const handleCancel = () => {
    popconfirmVisible.value = false;
  };
  const handleConfirm = () => {
    popconfirmVisible.value = false;
    visible.value = false;
    setAuthCache(AIDE_FLAG, getToken());
  };
  const handleGo = (params) => {
    router.push({ path: '/ai' });
  };
</script>

<style lang="less" scoped>
  .aide-wrap {
    position: fixed;
    top: 50%;
    right: 5px;
    transform: translate(0, -50%);
    height: 33px;
    width: 33px;
    border-radius: 50%;
    text-align: center;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    line-height: 1;
    color: #fff;
    cursor: pointer;
    .text {
      font-size: 12px;
      transform: scale(0.88);
    }
  }
</style>
