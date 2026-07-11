import { store } from '/@/store';
import { defineStore } from 'pinia';
import { createLocalStorage } from '/@/utils/cache';

const ls = createLocalStorage();
const ENHANCE_PRE = 'enhance_';

interface EnhanceStore {
  enhanceJs: Recordable;
}

export const useEnhanceStore = defineStore('online-cgform-enhance', {
  state: (): EnhanceStore => ({
    enhanceJs: {},
  }),
  getters: {},
  actions: {
    getEnhanceJs(code: string): Recordable[] {
      this.enhanceJs[code] = ls.get(ENHANCE_PRE + code);
      return this.enhanceJs[code];
    },
    addEnhanceJs(record: Recordable) {
      if (!this.enhanceJs[record.code]) {
        this.enhanceJs[record.code] = [{ ...record }];
      } else {
        this.enhanceJs[record.code].push({ ...record });
      }
      let enhanceJsArray = this.enhanceJs[record.code];
      while (enhanceJsArray.length > 16) {
        enhanceJsArray.shift();
      }
      ls.set(ENHANCE_PRE + record.code, enhanceJsArray);
    },
  },
});

// 在setup函数之外使用
export function useEnhanceStoreWithOut() {
  return useEnhanceStore(store);
}
