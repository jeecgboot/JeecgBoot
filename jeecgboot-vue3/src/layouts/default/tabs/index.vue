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
        <div class="rightExtra">
          <TabRedo v-if="getShowRedo" />
          <!-- <TabContent isExtra :tabItem="$route" v-if="getShowQuick" /> -->
          <!-- 列表页全屏 
          <FoldButton v-if="getShowFold" />-->
          <!-- <FullscreenOutlined /> -->
          <router-link to="/ai" class="ai-icon">
            <a-tooltip title="AI助手" placement="left">
<!--              <svg t="1706259688149" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2056" width="17" height="17">-->
<!--                <path d="M826.368 325.632c0-7.168 2.048-10.24 10.24-10.24h123.904c7.168 0 10.24 2.048 10.24 10.24v621.568c0 7.168-2.048 10.24-10.24 10.24h-122.88c-8.192 0-10.24-4.096-10.24-10.24l-1.024-621.568z m-8.192-178.176c0-50.176 35.84-79.872 79.872-79.872 48.128 0 79.872 32.768 79.872 79.872 0 52.224-33.792 79.872-81.92 79.872-46.08 1.024-77.824-27.648-77.824-79.872zM462.848 584.704C441.344 497.664 389.12 307.2 368.64 215.04h-2.048c-16.384 92.16-58.368 247.808-92.16 369.664h188.416zM243.712 712.704l-62.464 236.544c-2.048 7.168-4.096 8.192-12.288 8.192H54.272c-8.192 0-10.24-2.048-8.192-12.288l224.256-783.36c4.096-13.312 7.168-26.624 8.192-65.536 0-6.144 2.048-8.192 7.168-8.192H450.56c6.144 0 8.192 2.048 10.24 8.192l250.88 849.92c2.048 7.168 0 10.24-7.168 10.24H573.44c-7.168 0-10.24-2.048-12.288-7.168l-65.536-236.544c1.024 1.024-251.904 0-251.904 0z" fill="#333333" p-id="19816"></path>-->
<!--              </svg>-->
              <svg t="1737024931936" class="icon" viewBox="0 0 3011 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4248" width="256" height="256"><path d="M2028.242824 558.561882h415.021176V445.620706h-346.352941V321.415529h346.352941V210.823529l-328.463059 10.842353a2367.969882 2367.969882 0 0 0-31.021176-127.09647c341.955765-4.397176 602.352941-13.492706 781.131294-27.286588l41.441882 124.265411-320.933647 14.095059v115.772235h307.802353v124.205177h-307.802353v112.941176h375.506824v127.096471h-375.506824v113.844706c0 30.117647-5.782588 56.982588-17.468235 80.474353a114.447059 114.447059 0 0 1-50.778353 53.187764c-22.287059 11.926588-41.261176 19.154824-56.922353 21.684706-15.36 2.770824-82.522353 5.300706-201.426824 7.529412a5065.065412 5065.065412 0 0 0-31.984941-142.155294c64.632471 4.999529 114.447059 7.529412 149.624471 7.529412 44.574118 0 66.861176-23.190588 66.861176-69.632v-72.463059h-415.081411v-127.096471zM1685.624471 956.717176a1296.865882 1296.865882 0 0 0-27.286589-133.662117 2187.745882 2187.745882 0 0 0 96.978824 3.794823c18.191059 0 32.587294-4.758588 43.248941-14.155294 10.661647-9.697882 17.468235-23.491765 20.239059-41.381647 3.132235-17.889882 6.023529-66.379294 8.432941-145.408 2.590118-79.390118 3.614118-179.621647 3.373177-300.754823h-109.206589c-3.734588 212.389647-24.455529 364.604235-62.102588 456.523294-37.345882 91.919059-77.161412 156.491294-119.567059 193.837176-28.551529-27.888941-59.632941-54.392471-93.123764-79.510588-142.757647 12.830118-265.456941 25.298824-368.037648 37.165176l-15.962352-126.132705c14.095059-0.602353 28.190118-1.385412 42.345411-2.349177V88.003765h377.374118v687.043764c12.227765-1.204706 24.154353-2.349176 35.779765-3.312941 50.838588-95.653647 77.040941-244.555294 78.607058-446.58447h-85.172705V200.944941h87.04c0.301176-47.706353 0.481882-100.412235 0.481882-158.117647h125.168941c0 58.368-0.301176 111.073882-0.963765 158.117647H1957.647059l-9.878588 525.613177c-1.566118 62.464-5.481412 104.688941-11.745883 126.614588-6.324706 22.287059-16.805647 41.441882-31.563294 57.404235-14.757647 16.022588-32.768 27.105882-54.091294 33.430588-21.082353 6.264471-75.896471 10.480941-164.743529 12.649412z m-321.837177-759.567058h-140.227765V327.077647h140.227765V197.150118z m-140.227765 359.604706h140.227765V426.767059h-140.227765v129.867294z m140.227765 229.616941v-129.92753h-140.227765v140.288l140.227765-10.36047zM963.764706 114.326588V843.294118h-155.286588V114.326588H963.764706zM716.679529 843.294118H547.297882l-54.573176-167.032471H227.689412L174.140235 843.294118H5.662118l266.842353-728.96753h182.512941L716.739765 843.294118zM455.981176 556.212706L373.157647 303.585882c-5.300706-16.022588-9.035294-37.165176-11.264-63.548235h-4.216471a269.010824 269.010824 0 0 1-12.709647 61.680941L261.662118 556.212706H455.981176z" fill="#1F1F1F" p-id="4249"></path></svg>
            </a-tooltip>
          </router-link>
        </div>
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
  // update-begin--author:liaozhiyang---date:20241016---for：【issues/7345】标签样式切换到极简模式样式错乱
  .rightExtra {
    display: flex;
    :deep(svg) {
      &:not(.icon) {
        vertical-align: -0.3em;
      }
    }
    .ai-icon {
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      width: 50px;
      padding: 0 4px;
      height: 50px;
      color: @text-color;
      text-align: center;
      border-left: 1px solid @border-color-base;
      overflow: hidden;
    }
  }
  // update-end--author:liaozhiyang---date:20241016---for：【issues/7345】标签样式切换到极简模式样式错乱
}
</style>
