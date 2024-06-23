import {store} from '/@/store';
import {defineStore} from 'pinia';
import {defHttp} from "@/utils/http/axios";

interface DefIndexState {
  // 首页url
  url: string,
  // 首页组件
  component: string
}

export const useDefIndexStore = defineStore({
  id: 'defIndex',
  state: (): DefIndexState => ({
    url: '',
    component: '',
  }),
  getters: {},
  actions: {
    /**
     * 查询默认主页配置
     */
    async query() {
      const config = await defIndexApi.query();
      this.url = config.url;
      this.component = config.component;
    },
    /**
     * 更新默认主页配置
     * @param url 首页url
     * @param component 首页组件
     * @param isRoute 是否是路由
     */
    async update(url: string, component: string, isRoute: boolean) {
      await defIndexApi.update(url, component, isRoute);
      await this.query()
    },

    check(url: string) {
      return url === this.url;
    }
  }
});

// Need to be used outside the setup
export function useDefIndexStoreWithOut() {
  return useDefIndexStore(store);
}

/**
 * 默认首页配置API
 */
export const defIndexApi = {
  /**
   * 查询默认首页配置
   */
  async query() {
    const url = '/sys/sysRoleIndex/queryDefIndex'
    return await defHttp.get({url});
  },
  /**
   * 更新默认首页配置
   * @param url 首页url
   * @param component 首页组件
   * @param isRoute 是否是路由
   */
  async update(url: string, component: string, isRoute: boolean) {
    let apiUrl = '/sys/sysRoleIndex/updateDefIndex'
    apiUrl += '?url=' + url
    apiUrl += '&component=' + component
    apiUrl += '&isRoute=' + isRoute
    return await defHttp.put({url: apiUrl});
  },

}
