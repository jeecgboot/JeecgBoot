import type { AppRouteModule } from '/@/router/types';

import { getParentLayout, LAYOUT } from '/@/router/constant';
import { ExceptionEnum } from '/@/enums/exceptionEnum';
import { t } from '/@/hooks/web/useI18n';

const ExceptionPage = () => import('/@/views/sys/exception/Exception.vue');

const page: AppRouteModule = {
  path: '/page-demo',
  name: 'PageDemo',
  component: LAYOUT,
  redirect: '/page-demo/form/basic',
  meta: {
    orderNo: 20,
    icon: 'ion:aperture-outline',
    title: t('routes.demo.page.page'),
  },
  children: [
    // =============================form start=============================
    {
      path: 'form',
      name: 'FormPage',
      redirect: '/page-demo/form/basic',
      component: getParentLayout('FormPage'),
      meta: {
        title: t('routes.demo.page.form'),
      },
      children: [
        {
          path: 'basic',
          name: 'FormBasicPage',
          component: () => import('/@/views/demo/page/form/basic/index.vue'),
          meta: {
            title: t('routes.demo.page.formBasic'),
          },
        },
        {
          path: 'step',
          name: 'FormStepPage',
          component: () => import('/@/views/demo/page/form/step/index.vue'),
          meta: {
            title: t('routes.demo.page.formStep'),
          },
        },
        {
          path: 'high',
          name: 'FormHightPage',
          component: () => import('/@/views/demo/page/form/high/index.vue'),
          meta: {
            title: t('routes.demo.page.formHigh'),
          },
        },
      ],
    },
    // =============================form end=============================
    // =============================desc start=============================
    {
      path: 'desc',
      name: 'DescPage',
      component: getParentLayout('DescPage'),
      redirect: '/page-demo/desc/basic',
      meta: {
        title: t('routes.demo.page.desc'),
      },
      children: [
        {
          path: 'basic',
          name: 'DescBasicPage',
          component: () => import('/@/views/demo/page/desc/basic/index.vue'),
          meta: {
            title: t('routes.demo.page.descBasic'),
          },
        },
        {
          path: 'high',
          name: 'DescHighPage',
          component: () => import('/@/views/demo/page/desc/high/index.vue'),
          meta: {
            title: t('routes.demo.page.descHigh'),
          },
        },
      ],
    },
    // =============================desc end=============================

    // =============================result start=============================
    {
      path: 'result',
      name: 'ResultPage',
      redirect: '/page-demo/result/success',
      component: getParentLayout('ResultPage'),

      meta: {
        title: t('routes.demo.page.result'),
      },
      children: [
        {
          path: 'success',
          name: 'ResultSuccessPage',
          component: () => import('/@/views/demo/page/result/success/index.vue'),
          meta: {
            title: t('routes.demo.page.resultSuccess'),
          },
        },
        {
          path: 'fail',
          name: 'ResultFailPage',
          component: () => import('/@/views/demo/page/result/fail/index.vue'),
          meta: {
            title: t('routes.demo.page.resultFail'),
          },
        },
      ],
    },
    // =============================result end=============================

    // =============================account start=============================
    {
      path: 'account',
      name: 'AccountPage',
      component: getParentLayout('AccountPage'),
      redirect: '/page-demo/account/setting',
      meta: {
        title: t('routes.demo.page.account'),
      },
      children: [
        {
          path: 'center',
          name: 'AccountCenterPage',
          component: () => import('/@/views/demo/page/account/center/index.vue'),
          meta: {
            title: t('routes.demo.page.accountCenter'),
          },
        },
        {
          path: 'setting',
          name: 'AccountSettingPage',
          component: () => import('/@/views/demo/page/account/setting/index.vue'),
          meta: {
            title: t('routes.demo.page.accountSetting'),
          },
        },
      ],
    },
    // =============================account end=============================
    // =============================exception start=============================
    {
      path: 'exception',
      name: 'ExceptionPage',
      component: getParentLayout('ExceptionPage'),
      redirect: '/page-demo/exception/404',
      meta: {
        title: t('routes.demo.page.exception'),
      },
      children: [
        {
          path: '403',
          name: 'PageNotAccess',
          component: ExceptionPage,
          props: {
            status: ExceptionEnum.PAGE_NOT_ACCESS,
          },
          meta: {
            title: '403',
          },
        },
        {
          path: '404',
          name: 'PageNotFound',
          component: ExceptionPage,
          props: {
            status: ExceptionEnum.PAGE_NOT_FOUND,
          },
          meta: {
            title: '404',
          },
        },
        {
          path: '500',
          name: 'ServiceError',
          component: ExceptionPage,
          props: {
            status: ExceptionEnum.ERROR,
          },
          meta: {
            title: '500',
          },
        },
        {
          path: 'net-work-error',
          name: 'NetWorkError',
          component: ExceptionPage,
          props: {
            status: ExceptionEnum.NET_WORK_ERROR,
          },
          meta: {
            title: t('routes.demo.page.netWorkError'),
          },
        },
        {
          path: 'not-data',
          name: 'NotData',
          component: ExceptionPage,
          props: {
            status: ExceptionEnum.PAGE_NOT_DATA,
          },
          meta: {
            title: t('routes.demo.page.notData'),
          },
        },
      ],
    },
    // =============================exception end=============================
    // =============================list start=============================
    {
      path: 'list',
      name: 'ListPage',
      component: getParentLayout('ListPage'),
      redirect: '/page-demo/list/card',
      meta: {
        title: t('routes.demo.page.list'),
      },
      children: [
        {
          path: 'basic',
          name: 'ListBasicPage',
          component: () => import('/@/views/demo/page/list/basic/index.vue'),
          meta: {
            title: t('routes.demo.page.listBasic'),
          },
        },
        {
          path: 'card',
          name: 'ListCardPage',
          component: () => import('/@/views/demo/page/list/card/index.vue'),
          meta: {
            title: t('routes.demo.page.listCard'),
          },
        },
        {
          path: 'search',
          name: 'ListSearchPage',
          component: () => import('/@/views/demo/page/list/search/index.vue'),
          meta: {
            title: t('routes.demo.page.listSearch'),
          },
        },
      ],
    },
    // =============================list end=============================
  ],
};

export default page;
