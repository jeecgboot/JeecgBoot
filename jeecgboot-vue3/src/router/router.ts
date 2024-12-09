/*
 * 路由实例存储文件，请勿轻易添加其他代码，防止出现 HMR 或其他问题
 */
import type {Router, RouterHistory} from 'vue-router';
import {createRouter as createVueRouter, createWebHistory, RouterOptions} from 'vue-router';

export let router: Router = null as unknown as Router;

export function setRouter(r: Router) {
  router = r
}

let webHistory: Nullable<RouterHistory> = null;

/**
 * 创建路由
 * @param options 参数
 */
export function createRouter(options: Partial<RouterOptions>) {
  webHistory = createWebHistory(import.meta.env.VITE_PUBLIC_PATH);
  // app router
  let router = createVueRouter({
    history: webHistory,
    routes: [],
    ...options,
  });

  setRouter(router)

  return router
}

// 销毁路由
export function destroyRouter() {
  setRouter(null as unknown as Router);
  if (webHistory) {
    webHistory.destroy();
  }
  webHistory = null
}
