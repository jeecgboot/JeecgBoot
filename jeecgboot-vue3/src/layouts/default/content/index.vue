<template>
  <div :class="[prefixCls, getLayoutContentMode]" v-loading="getOpenPageLoading && getPageLoading">
    <PageLayout />
    <!-- update-begin-author:zyf date:20211129 for:qiankun 挂载子应用盒子 -->
    <div id="content" class="app-view-box" v-if="openQianKun == 'true'"></div>
    <!-- update-end-author:zyf date:20211129 for: qiankun 挂载子应用盒子-->
  </div>
</template>
<script lang="ts">
  import { defineComponent, onMounted } from 'vue';
  import PageLayout from '/@/layouts/page/index.vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useRootSetting } from '/@/hooks/setting/useRootSetting';
  import { useTransitionSetting } from '/@/hooks/setting/useTransitionSetting';
  import { useContentViewHeight } from './useContentViewHeight';
  // import registerApps from '/@/qiankun';
  import { useGlobSetting } from '/@/hooks/setting';
  export default defineComponent({
    name: 'LayoutContent',
    components: { PageLayout },
    setup() {
      const { prefixCls } = useDesign('layout-content');
      const { getOpenPageLoading } = useTransitionSetting();
      const { getLayoutContentMode, getPageLoading } = useRootSetting();
      const globSetting = useGlobSetting();
      const openQianKun = globSetting.openQianKun;
      useContentViewHeight();
      onMounted(() => {
        // //注册openQianKun
        // if (openQianKun == 'true') {
        //   if (!window.qiankunStarted) {
        //     window.qiankunStarted = true;
        //     registerApps();
        //   }
        // }
      });
      return {
        prefixCls,
        openQianKun,
        getOpenPageLoading,
        getLayoutContentMode,
        getPageLoading,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-layout-content';

  .@{prefix-cls} {
    position: relative;
    flex: 1 1 auto;
    min-height: 0;

    &.fixed {
      width: 1200px;
      margin: 0 auto;
    }

    &-loading {
      position: absolute;
      top: 200px;
      z-index: @page-loading-z-index;
    }
  }
</style>
