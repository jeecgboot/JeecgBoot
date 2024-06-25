import type { AppRouteModule } from '/@/router/types';

import { getParentLayout, LAYOUT } from '/@/router/constant';
import { t } from '/@/hooks/web/useI18n';

const charts: AppRouteModule = {
  path: '/charts',
  name: 'Charts',
  component: LAYOUT,
  redirect: '/charts/echarts/map',
  meta: {
    orderNo: 500,
    icon: 'ion:bar-chart-outline',
    title: t('routes.demo.charts.charts'),
  },
  children: [
    {
      path: 'baiduMap',
      name: 'BaiduMap',
      meta: {
        title: t('routes.demo.charts.baiduMap'),
      },
      component: () => import('/@/views/demo/charts/map/Baidu.vue'),
    },
    {
      path: 'aMap',
      name: 'AMap',
      meta: {
        title: t('routes.demo.charts.aMap'),
      },
      component: () => import('/@/views/demo/charts/map/Gaode.vue'),
    },
    {
      path: 'googleMap',
      name: 'GoogleMap',
      meta: {
        title: t('routes.demo.charts.googleMap'),
      },
      component: () => import('/@/views/demo/charts/map/Google.vue'),
    },
    {
      path: 'echarts',
      name: 'Echarts',
      component: getParentLayout('Echarts'),
      meta: {
        title: 'Echarts',
      },
      redirect: '/charts/echarts/map',
      children: [
        {
          path: 'map',
          name: 'Map',
          component: () => import('/@/views/demo/charts/Map.vue'),
          meta: {
            title: t('routes.demo.charts.map'),
          },
        },
        {
          path: 'line',
          name: 'Line',
          component: () => import('/@/views/demo/charts/Line.vue'),
          meta: {
            title: t('routes.demo.charts.line'),
          },
        },
        {
          path: 'pie',
          name: 'Pie',
          component: () => import('/@/views/demo/charts/Pie.vue'),
          meta: {
            title: t('routes.demo.charts.pie'),
          },
        },
      ],
    },
  ],
};

export default charts;
