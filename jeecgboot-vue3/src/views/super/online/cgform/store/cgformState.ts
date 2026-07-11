import {defineStore} from 'pinia';
import {store} from '/@/store';

interface CgformState {
  // 近期被修改过的表，存储表id
  changedTables: string[];
}

export const useCgformStore = defineStore('cgform-state', {
  state: (): CgformState => ({
    changedTables: [],
  }),
  getters: {},
  actions: {
    /**
     * 检查是否同步过数据库
     * @param tableId 表id
     */
    checkIsChanged(tableId: string) {
      return this.changedTables.includes(tableId);
    },
    addChangedTable(tableId: string) {
      this.changedTables.push(tableId);
    },
    removeChangedTable(tableId: string) {
      const index = this.changedTables.findIndex((item) => item === tableId);
      if (index !== -1) {
        this.changedTables.splice(index, 1);
      }
    },
  },
});

// 在 setup 之外使用
export function useCgformStoreWithOut() {
  return useCgformStore(store);
}
