import type { App } from 'vue';
import type { Pinia } from 'pinia';
import { createPinia } from 'pinia';

let store: Nullable<Pinia> = null;

export function setupStore(app: App<Element>) {
  if (store == null) {
    store = createPinia();
  }
  app.use(store);
}

// 销毁store
export function destroyStore() {
  store = null;
}

export { store };
