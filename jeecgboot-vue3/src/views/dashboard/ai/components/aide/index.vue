<template>
  <div v-if="visible" ref="aideWrapRef" class="aide-wrap" @click="handleGo">
    <div class="icon">
      <svg t="1706259688149" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2056" width="17" height="17">
        <path
          fill="currentColor"
          d="M427.904 492.608a16.896 16.896 0 0 0 0 24.96l438.528 426.368a54.272 54.272 0 0 0 77.056 0 50.752 50.752 0 0 0 0-74.88L504.96 442.688a18.112 18.112 0 0 0-25.664 0l-51.392 49.92z m-12.16-137.728l-70.272-58.624a36.48 36.48 0 0 0-46.208 0l-46.144 38.464c-13.248 11.008-13.248 27.52 0 38.464l70.336 58.624a24.32 24.32 0 0 0 30.784 0l61.568-51.264c8.768-7.36 8.768-18.304 0-25.664z m-160.64 448c23.68-78.72 81.152-140.8 158.4-165.696a13.12 13.12 0 0 0 0-24.832C338.24 587.52 278.848 527.424 255.104 446.656c-3.968-12.416-19.84-12.416-23.808 0-23.68 78.72-81.152 140.8-158.4 165.696a13.12 13.12 0 0 0 0 24.832c75.264 24.896 134.656 84.928 158.4 165.76 3.968 10.304 19.84 10.304 23.808 0zM621.184 71.04a203.584 203.584 0 0 1-132.096 132.096 11.008 11.008 0 0 0 0 20.48 203.584 203.584 0 0 1 132.16 132.16 11.008 11.008 0 0 0 20.48 0 203.584 203.584 0 0 1 132.096-132.16 11.008 11.008 0 0 0 0-20.48 203.584 203.584 0 0 1-132.096-132.096c-3.776-9.28-18.624-9.28-20.48 0zM191.488 282.368c15.936-48.512 53.76-83.968 105.536-98.88 7.936-1.92 7.936-13.056 0-14.976-51.776-14.912-89.6-50.368-105.536-98.88-1.984-7.488-13.952-7.488-15.936 0-15.936 48.512-53.76 83.968-105.536 98.88-7.936 1.92-7.936 13.056 0 14.976 51.84 14.912 89.6 50.368 105.6 98.88 1.92 7.488 13.888 7.488 15.872 0z"
          p-id="2057"
        ></path>
      </svg>
    </div>
    <a-popconfirm
      :open="popconfirmVisible"
      title="确定AI助手退出吗？"
      ok-text="确定"
      cancel-text="取消"
      @cancel="handleCancel"
      @confirm="handleConfirm"
    >
      <span class="text">AI助手</span>
    </a-popconfirm>
  </div>
</template>

<script setup>
  import { onMounted, ref } from 'vue';
  import { router } from '/@/router';
  import { AIDE_FLAG } from '/@/enums/cacheEnum';
  import { getToken } from '/@/utils/auth';
  import { getAuthCache, setAuthCache, removeAuthCache } from '/@/utils/auth';
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
    right: 0;
    transform: translate(0, -50%);
    background-color: @primary-color;
    height: 46px;
    width: 46px;
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
