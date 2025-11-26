import { defHttp } from '/@/utils/http/axios';
import { getMenuListResultModel } from './model/menuModel';
import { useUserStoreWithOut } from '@/store/modules/user';
import { setAuthCache } from '@/utils/auth';
import { TOKEN_KEY } from '@/enums/cacheEnum';
import { router } from '@/router';
import { PageEnum } from '@/enums/pageEnum';

enum Api {
  GetMenuList = '/sys/permission/getUserPermissionByToken',
  // 【QQYUN-8487】
  // SwitchVue3Menu = '/sys/switchVue3Menu',
}

/**
 * @description: Get user menu based on id
 */

export const getMenuList = () => {
  return new Promise((resolve) => {
    //为了兼容mock和接口数据
    defHttp.get<getMenuListResultModel>({ url: Api.GetMenuList }).then((res) => {
      if (Array.isArray(res)) {
        resolve(res);
      } else {
        resolve(res['menu']);
      }
    });
  });
};

/**
 * @description: 获取后台菜单权限和按钮权限
 */
export function getBackMenuAndPerms() {
  return defHttp.get({ url: Api.GetMenuList }).catch((e) => {
    console.log('接口 getBackMenuAndPerms 异常错误信息：', e);
    // Token过期失效，直接跳转登录页面 2025-09-08 scott
    if (e && (e.message.includes('timeout') || e.message.includes('401') || e.message.includes('500'))) {
      const userStore = useUserStoreWithOut();
      userStore.setToken('');
      setAuthCache(TOKEN_KEY, null);
      router.push({
        path: PageEnum.BASE_LOGIN,
        query: {
          // 传入当前的路由，登录成功后跳转到当前路由
          redirect: router.currentRoute.value.fullPath,
        }
      });
    }
  });
}

/**
 * 切换成vue3菜单
 */
// export const switchVue3Menu = () => {
//   return new Promise((resolve) => {
//     defHttp.get({ url: Api.SwitchVue3Menu });
//   });
// };
