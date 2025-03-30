import type { App } from 'vue';
import type { Pinia } from 'pinia';
import { createPinia } from 'pinia';

let app: Nullable<App<Element>> = null;
let store: Nullable<Pinia> = null;

export function setupStore($app: App<Element>) {
  if (store == null) {
    store = createPinia();
  }
  app = $app;
  app.use(store);
}

// 销毁store
export function destroyStore() {
  store = null;
}

// 获取app实例
export const getAppContext = () => app?._context;

export {app, store};
