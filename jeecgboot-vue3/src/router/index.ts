import type { RouteRecordRaw } from 'vue-router';
import type { App } from 'vue';

import { basicRoutes } from './routes';
import {createRouter as createVueRouter, destroyRouter, router} from './router'

// 白名单应该包含基本静态路由
const WHITE_NAME_LIST: string[] = [];
const getRouteNames = (array: any[]) =>
  array.forEach((item) => {
    WHITE_NAME_LIST.push(item.name);
    getRouteNames(item.children || []);
  });
getRouteNames(basicRoutes);

/**
 * 创建路由实例
 */
export function createRouter() {
  let router = createVueRouter({
    routes: basicRoutes as unknown as RouteRecordRaw[],
    strict: true,
    scrollBehavior: () => ({left: 0, top: 0}),
  })

  // TODO 【QQYUN-4517】【表单设计器】记录分享路由守卫测试
  // @ts-ignore
  router.beforeEach(async (to, from, next) => {
    //console.group('【QQYUN-4517】beforeEach');
    //console.warn('from', from);
    //console.warn('to', to);
    //console.groupEnd();
    next();
  });
}

// reset router
export function resetRouter() {
  router.getRoutes().forEach((route) => {
    const { name } = route;
    if (name && !WHITE_NAME_LIST.includes(name as string)) {
      router.hasRoute(name) && router.removeRoute(name);
    }
  });
}

// config router
export function setupRouter(app: App<Element>) {
  app.use(router);
}

export {
  router,
  destroyRouter,
}
