<template>
  <div :class="getWrapClass">
    <Tabs
      type="editable-card"
      size="small"
      :animated="false"
      :hideAdd="true"
      :tabBarGutter="3"
      :activeKey="activeKeyRef"
      @change="handleChange"
      @edit="handleEdit"
    >
      <template v-for="item in getTabsState" :key="item.query ? item.fullPath : item.path">
        <TabPane :closable="!(item && item.meta && item.meta.affix)">
          <template #tab>
            <TabContent :tabItem="item" />
          </template>
        </TabPane>
      </template>

      <template #rightExtra v-if="getShowRedo || getShowQuick">
        <TabRedo v-if="getShowRedo" />
        <!-- <TabContent isExtra :tabItem="$route" v-if="getShowQuick" /> -->
        <!-- 列表页全屏 
        <FoldButton v-if="getShowFold" />-->
        <!-- <FullscreenOutlined /> -->
        <router-link to="/ai" class="ai-icon">
          <a-tooltip title="AI助手" placement="left">
            <svg t="1706259688149" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2056" width="17" height="17">
              <path d="M826.368 325.632c0-7.168 2.048-10.24 10.24-10.24h123.904c7.168 0 10.24 2.048 10.24 10.24v621.568c0 7.168-2.048 10.24-10.24 10.24h-122.88c-8.192 0-10.24-4.096-10.24-10.24l-1.024-621.568z m-8.192-178.176c0-50.176 35.84-79.872 79.872-79.872 48.128 0 79.872 32.768 79.872 79.872 0 52.224-33.792 79.872-81.92 79.872-46.08 1.024-77.824-27.648-77.824-79.872zM462.848 584.704C441.344 497.664 389.12 307.2 368.64 215.04h-2.048c-16.384 92.16-58.368 247.808-92.16 369.664h188.416zM243.712 712.704l-62.464 236.544c-2.048 7.168-4.096 8.192-12.288 8.192H54.272c-8.192 0-10.24-2.048-8.192-12.288l224.256-783.36c4.096-13.312 7.168-26.624 8.192-65.536 0-6.144 2.048-8.192 7.168-8.192H450.56c6.144 0 8.192 2.048 10.24 8.192l250.88 849.92c2.048 7.168 0 10.24-7.168 10.24H573.44c-7.168 0-10.24-2.048-12.288-7.168l-65.536-236.544c1.024 1.024-251.904 0-251.904 0z" fill="#333333" p-id="19816"></path>
            </svg>
          </a-tooltip>
        </router-link>
      </template>
    </Tabs>
  </div>
</template>
<script lang="ts">
  import type { RouteLocationNormalized, RouteMeta } from 'vue-router';

  import { defineComponent, computed, unref, ref } from 'vue';

  import { Tabs } from 'ant-design-vue';
  import TabContent from './components/TabContent.vue';
  import FoldButton from './components/FoldButton.vue';
  import TabRedo from './components/TabRedo.vue';

  import { useGo } from '/@/hooks/web/usePage';

  import { useMultipleTabStore } from '/@/store/modules/multipleTab';
  import { useUserStore } from '/@/store/modules/user';

  import { initAffixTabs, useTabsDrag } from './useMultipleTabs';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useMultipleTabSetting } from '/@/hooks/setting/useMultipleTabSetting';

  import { REDIRECT_NAME } from '/@/router/constant';
  import { listenerRouteChange } from '/@/logics/mitt/routeChange';

  import { useRouter } from 'vue-router';
  import Aide from "/@/views/dashboard/ai/components/aide/index.vue"

  export default defineComponent({
    name: 'MultipleTabs',
    components: {
      TabRedo,
      FoldButton,
      Tabs,
      TabPane: Tabs.TabPane,
      TabContent,
      Aide,
    },
    setup() {
      const affixTextList = initAffixTabs();
      const activeKeyRef = ref('');

      useTabsDrag(affixTextList);
      const tabStore = useMultipleTabStore();
      const userStore = useUserStore();
      const router = useRouter();

      const { prefixCls } = useDesign('multiple-tabs');
      const go = useGo();
      const { getShowQuick, getShowRedo, getShowFold, getTabsTheme } = useMultipleTabSetting();

      const getTabsState = computed(() => {
        return tabStore.getTabList.filter((item) => !item.meta?.hideTab);
      });

      const unClose = computed(() => unref(getTabsState).length === 1);

      const getWrapClass = computed(() => {
        return [
          prefixCls,
          {
            [`${prefixCls}--hide-close`]: unref(unClose),
          },
          `${prefixCls}--theme-${unref(getTabsTheme)}`,
        ];
      });

      listenerRouteChange((route) => {
        const { name } = route;
        if (name === REDIRECT_NAME || !route || !userStore.getToken) {
          return;
        }

        const { path, fullPath, meta = {} } = route;
        const { currentActiveMenu, hideTab } = meta as RouteMeta;
        const isHide = !hideTab ? null : currentActiveMenu;
        const p = isHide || fullPath || path;
        if (activeKeyRef.value !== p) {
          activeKeyRef.value = p as string;
        }

        if (isHide) {
          const findParentRoute = router.getRoutes().find((item) => item.path === currentActiveMenu);

          findParentRoute && tabStore.addTab(findParentRoute as unknown as RouteLocationNormalized);
        } else {
          tabStore.addTab(unref(route));
        }
      });

      function handleChange(activeKey: any) {
        activeKeyRef.value = activeKey;
        go(activeKey, false);
      }

      // Close the current tab
      function handleEdit(targetKey: string) {
        // Added operation to hide, currently only use delete operation
        if (unref(unClose)) {
          return;
        }

        tabStore.closeTabByKey(targetKey, router);
      }
      return {
        prefixCls,
        unClose,
        getWrapClass,
        handleEdit,
        handleChange,
        activeKeyRef,
        getTabsState,
        getShowQuick,
        getShowRedo,
        getShowFold,
      };
    },
  });
</script>
<style lang="less">
  @import './index.less';
  @import './tabs.theme.card.less';
  @import './tabs.theme.smooth.less';
</style>
<style lang="less" scoped>
@prefix-cls: ~'@{namespace}-multiple-tabs';
.@{prefix-cls} {
  :deep(.anticon) {
    display: inline-block;
  }
  .ai-icon{
    float: right;
    margin-top: 3px;
    cursor: pointer;
    width: 36px;
    height: 50px;
    line-height: 50px;
    color: @text-color;
    text-align: center;
    border-left: 1px solid @border-color-base;
  }
}
</style>
