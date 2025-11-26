<template>
  <RouterView>
    <template #default="{ Component, route }">
      <!--      <transition-->
      <!--        :name="-->
      <!--          getTransitionName({-->
      <!--            route,-->
      <!--            openCache,-->
      <!--            enableTransition: getEnableTransition,-->
      <!--            cacheTabs: getCaches,-->
      <!--            def: getBasicTransition,-->
      <!--          })-->
      <!--        "-->
      <!--        mode="out-in"-->
      <!--        appear-->
      <!--      >-->
      <template v-if="Component">
        <keep-alive v-if="openCache" :include="getCaches">
          <component :is="Component" :key="route.fullPath" />
        </keep-alive>
        <component v-else :is="Component" :key="route.fullPath" />
      </template>
      <template v-else>
        <!-- 【QQYUN-13593】空白页美化 -->
        <div class="animationEffect" :style="effectVars">
          <div class="effect-layer">
            <div class="blob blob-a"></div>
            <div class="blob blob-b"></div>
            <div class="blob blob-c"></div>
          </div>
          <div class="effect-grid"></div>
          <div class="effect-tip">
            <p>{{pageTip}}</p>
          </div>
        </div>
      </template>
      <!--      </transition>-->
    </template>
  </RouterView>
  <FrameLayout v-if="getCanEmbedIFramePage" />
</template>

<script lang="ts">
  import { computed, defineComponent, unref } from 'vue';

  import FrameLayout from '/@/layouts/iframe/index.vue';

  import { useRootSetting } from '/@/hooks/setting/useRootSetting';

  import { useTransitionSetting } from '/@/hooks/setting/useTransitionSetting';
  import { useMultipleTabSetting } from '/@/hooks/setting/useMultipleTabSetting';
  import { getTransitionName } from './transition';

  import { useMultipleTabStore } from '/@/store/modules/multipleTab';
  import { useEmpty } from './useEmpty';

  export default defineComponent({
    name: 'PageLayout',
    components: { FrameLayout },
    setup() {
      const { getShowMultipleTab } = useMultipleTabSetting();
      const tabStore = useMultipleTabStore();

      const { getOpenKeepAlive, getCanEmbedIFramePage } = useRootSetting();

      const { getBasicTransition, getEnableTransition } = useTransitionSetting();

      const openCache = computed(() => unref(getOpenKeepAlive) && unref(getShowMultipleTab));

      const getCaches = computed((): string[] => {
        if (!unref(getOpenKeepAlive)) {
          return [];
        }
        return tabStore.getCachedTabList;
      });
      // 代码逻辑说明: 【QQYUN-13593】空白页美化
      const { pageTip, getPageTip, effectVars } = useEmpty();
      return {
        getTransitionName,
        openCache,
        getEnableTransition,
        getBasicTransition,
        getCaches,
        getCanEmbedIFramePage,
        pageTip,
        getPageTip,
        effectVars,
      };
    },
  });
</script>
<style lang="less" scoped>
/** update-begin---author:liaozy ---date:2025-08-26  for：空白页美化样式 */
.pageTip {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  font-size: 18px;
  color: #999;
  margin: 0;
}

.animationEffect {
  position: relative;
  height: 100%;
  min-height: 420px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(180deg, var(--bg-1) 0%, var(--bg-2) 100%);
}

.effect-layer {
  position: absolute;
  top: -20%;
  left: -20%;
  right: -20%;
  bottom: -20%;
  filter: blur(30px);
  pointer-events: none;
  z-index: 0;
}

.blob {
  position: absolute;
  width: 380px;
  height: 380px;
  border-radius: 50%;
  opacity: 0.45;
}

.blob-a {
  background: radial-gradient(circle at 30% 30%, var(--blob-a-1) 0%, var(--blob-a-2) 60%, var(--blob-a-2) 100%);
  left: 5%;
  top: 10%;
  animation: float-a 18s ease-in-out infinite;
}

.blob-b {
  background: radial-gradient(circle at 30% 30%, var(--blob-b-1) 0%, var(--blob-b-2) 60%, var(--blob-b-2) 100%);
  right: 0;
  top: 30%;
  animation: float-b 22s ease-in-out infinite;
}

.blob-c {
  background: radial-gradient(circle at 30% 30%, var(--blob-c-1) 0%, var(--blob-c-2) 60%, var(--blob-c-2) 100%);
  left: 35%;
  bottom: -5%;
  animation: float-c 26s ease-in-out infinite;
}

@keyframes float-a {
  0% { transform: translate(0, 0) scale(1); }
  25% { transform: translate(20%, -10%) scale(1.05); }
  50% { transform: translate(35%, 5%) scale(0.95); }
  75% { transform: translate(10%, 15%) scale(1.02); }
  100% { transform: translate(0, 0) scale(1); }
}

@keyframes float-b {
  0% { transform: translate(0, 0) scale(1); }
  25% { transform: translate(-15%, 10%) scale(1.08); }
  50% { transform: translate(-30%, -5%) scale(0.92); }
  75% { transform: translate(-10%, -15%) scale(1.03); }
  100% { transform: translate(0, 0) scale(1); }
}

@keyframes float-c {
  0% { transform: translate(0, 0) scale(1); }
  25% { transform: translate(-10%, -10%) scale(0.9); }
  50% { transform: translate(10%, -25%) scale(1.05); }
  75% { transform: translate(20%, 0%) scale(0.98); }
  100% { transform: translate(0, 0) scale(1); }
}

.effect-grid {
  position: absolute;
  inset: 0;
  background-image: linear-gradient(0deg, var(--grid-color) 1px, rgba(0, 0, 0, 0) 1px),
    linear-gradient(90deg, var(--grid-color) 1px, rgba(0, 0, 0, 0) 1px);
  background-size: 36px 36px, 36px 36px;
  mask-image: radial-gradient(circle at 50% 50%, rgba(0, 0, 0, 0.8), rgba(0, 0, 0, 0) 70%);
  pointer-events: none;
  z-index: 1;
}

.effect-tip {
  position: relative;
  z-index: 2;
  text-align: center;
  pointer-events: none;
  p {
    margin: 0;
    padding: 8px 14px;
    color: var(--tip-color);
    font-size: 20px;
    border-radius: 8px;
  }
}
/** update-end---author:liaozy ---date:2025-08-26  for：空白页美化样式 */
</style>
