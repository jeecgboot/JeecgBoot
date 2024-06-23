import type { AppRouteModule } from '/@/router/types';

import { getParentLayout, LAYOUT } from '/@/router/constant';
import { t } from '/@/hooks/web/useI18n';

const feat: AppRouteModule = {
  path: '/feat',
  name: 'FeatDemo',
  component: LAYOUT,
  redirect: '/feat/icon',
  meta: {
    orderNo: 19,
    icon: 'ion:git-compare-outline',
    title: t('routes.demo.feat.feat'),
  },

  children: [
    {
      path: 'ws',
      name: 'WebSocket',
      component: () => import('/@/views/demo/feat/ws/index.vue'),
      meta: {
        title: t('routes.demo.feat.ws'),
      },
    },
    {
      path: 'session-timeout',
      name: 'SessionTimeout',
      component: () => import('/@/views/demo/feat/session-timeout/index.vue'),
      meta: {
        title: t('routes.demo.feat.sessionTimeout'),
      },
    },

    {
      path: 'breadcrumb',
      name: 'BreadcrumbDemo',
      redirect: '/feat/breadcrumb/flat',
      component: getParentLayout('BreadcrumbDemo'),
      meta: {
        title: t('routes.demo.feat.breadcrumb'),
      },

      children: [
        {
          path: 'flat',
          name: 'BreadcrumbFlatDemo',
          component: () => import('/@/views/demo/feat/breadcrumb/FlatList.vue'),
          meta: {
            title: t('routes.demo.feat.breadcrumbFlat'),
          },
        },
        {
          path: 'flatDetail',
          name: 'BreadcrumbFlatDetailDemo',
          component: () => import('/@/views/demo/feat/breadcrumb/FlatListDetail.vue'),
          meta: {
            title: t('routes.demo.feat.breadcrumbFlatDetail'),
            hideMenu: true,
            hideTab: true,
            currentActiveMenu: '/feat/breadcrumb/flat',
          },
        },
        {
          path: 'children',
          name: 'BreadcrumbChildrenDemo',
          component: () => import('/@/views/demo/feat/breadcrumb/ChildrenList.vue'),
          meta: {
            title: t('routes.demo.feat.breadcrumbChildren'),
          },
          children: [
            {
              path: 'childrenDetail',
              name: 'BreadcrumbChildrenDetailDemo',
              component: () => import('/@/views/demo/feat/breadcrumb/ChildrenListDetail.vue'),
              meta: {
                currentActiveMenu: '/feat/breadcrumb/children',
                title: t('routes.demo.feat.breadcrumbChildrenDetail'),
                //hideTab: true,
                // hideMenu: true,
              },
            },
          ],
        },
      ],
    },

    {
      path: 'context-menu',
      name: 'ContextMenuDemo',
      component: () => import('/@/views/demo/feat/context-menu/index.vue'),
      meta: {
        title: t('routes.demo.feat.contextMenu'),
      },
    },

    {
      path: 'copy',
      name: 'CopyDemo',
      component: () => import('/@/views/demo/feat/copy/index.vue'),
      meta: {
        title: t('routes.demo.feat.copy'),
      },
    },

    {
      path: 'watermark',
      name: 'WatermarkDemo',
      component: () => import('/@/views/demo/feat/watermark/index.vue'),
      meta: {
        title: t('routes.demo.feat.watermark'),
      },
    },

    {
      path: 'full-screen',
      name: 'FullScreenDemo',
      component: () => import('/@/views/demo/feat/full-screen/index.vue'),
      meta: {
        title: t('routes.demo.feat.fullScreen'),
      },
    },

    {
      path: '/error-log',
      name: 'ErrorLog',
      component: () => import('/@/views/sys/error-log/index.vue'),
      meta: {
        title: t('routes.demo.feat.errorLog'),
      },
    },
    {
      path: 'testTab/:id',
      name: 'TestTab',
      component: () => import('/@/views/demo/feat/tab-params/index.vue'),
      meta: {
        title: t('routes.demo.feat.tab'),
        carryParam: true,
        hidePathForChildren: true,
      },
      children: [
        {
          path: 'testTab/id1',
          name: 'TestTab1',
          component: () => import('/@/views/demo/feat/tab-params/index.vue'),
          meta: {
            title: t('routes.demo.feat.tab1'),
            carryParam: true,
            ignoreRoute: true,
          },
        },
        {
          path: 'testTab/id2',
          name: 'TestTab2',
          component: () => import('/@/views/demo/feat/tab-params/index.vue'),
          meta: {
            title: t('routes.demo.feat.tab2'),
            carryParam: true,
            ignoreRoute: true,
          },
        },
      ],
    },
    {
      path: 'testParam/:id',
      name: 'TestParam',
      component: getParentLayout('TestParam'),
      meta: {
        title: t('routes.demo.feat.menu'),
        ignoreKeepAlive: true,
      },
      children: [
        {
          path: 'sub1',
          name: 'TestParam_1',
          component: () => import('/@/views/demo/feat/menu-params/index.vue'),
          meta: {
            title: t('routes.demo.feat.menu1'),
            ignoreKeepAlive: true,
          },
        },
        {
          path: 'sub2',
          name: 'TestParam_2',
          component: () => import('/@/views/demo/feat/menu-params/index.vue'),
          meta: {
            title: t('routes.demo.feat.menu2'),
            ignoreKeepAlive: true,
          },
        },
      ],
    },
  ],
};

export default feat;
