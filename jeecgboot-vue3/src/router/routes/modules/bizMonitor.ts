import type { AppRouteModule } from '/@/router/types';

import { LAYOUT } from '/@/router/constant';
import { t } from '/@/hooks/web/useI18n';

const bizMonitor: AppRouteModule = {
  path: '/monitor',
  name: 'Monitor',
  component: LAYOUT,
  redirect: '/monitor/biz/realTimeBoard',
  meta: {
    orderNo: 1000,
    icon: 'ant-design:dashboard-filled',
    title: t('routes.monitor.bizMonitor'),
  },
  children: [
    {
      path: 'biz',
      name: 'BizMonitor',
      component: LAYOUT,
      meta: {
        title: t('routes.monitor.bizMonitor'),
      },
      children: [
        {
          path: 'realTimeBoard',
          name: 'RealTimeBoard',
          component: () => import('/@/views/monitor/biz/RealTimeBoard.vue'),
          meta: {
            title: t('routes.monitor.realTimeBoard'),
          },
        },
        {
          path: 'alertRuleConfig',
          name: 'AlertRuleConfig',
          component: () => import('/@/views/monitor/biz/AlertRuleConfig.vue'),
          meta: {
            title: t('routes.monitor.alertRuleConfig'),
          },
        },
      ],
    },
  ],
};

export default bizMonitor;