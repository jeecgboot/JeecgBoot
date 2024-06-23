import type { RouteLocationRaw, Router } from 'vue-router';

import { PageEnum } from '/@/enums/pageEnum';
import { isString } from '/@/utils/is';
import { unref } from 'vue';

import { useRouter } from 'vue-router';
import { REDIRECT_NAME } from '/@/router/constant';
import { useUserStore } from '/@/store/modules/user';
import { useMultipleTabStore } from '/@/store/modules/multipleTab';

export type RouteLocationRawEx = Omit<RouteLocationRaw, 'path'> & { path: PageEnum };

function handleError(e: Error) {
  console.error(e);
}

// page switch
export function useGo(_router?: Router) {
  // update-begin--author:liaozhiyang---date:20230908---for：【issues/694】404返回首页问题
  const userStore = useUserStore();
  const homePath = userStore.getUserInfo.homePath || PageEnum.BASE_HOME;
  // update-end--author:liaozhiyang---date:20230908---for：【issues/694】404返回首页问题
  let router;
  if (!_router) {
    router = useRouter();
  }
  const { push, replace } = _router || router;
  function go(opt: PageEnum | RouteLocationRawEx | string = homePath, isReplace = false) {
    if (!opt) {
      return;
    }
    if (isString(opt)) {
      isReplace ? replace(opt).catch(handleError) : push(opt).catch(handleError);
    } else {
      const o = opt as RouteLocationRaw;
      isReplace ? replace(o).catch(handleError) : push(o).catch(handleError);
    }
  }
  return go;
}

/**
 * @description: redo current page
 */
export const useRedo = (_router?: Router) => {
  const { push, currentRoute } = _router || useRouter();
  const { query, params = {}, name, fullPath } = unref(currentRoute.value);
  function redo(): Promise<boolean> {
    return new Promise((resolve) => {
      if (name === REDIRECT_NAME) {
        resolve(false);
        return;
      }
      // update-begin--author:liaozhiyang---date:20231123---for：【QQYUN-7099】动态路由匹配右键重新加载404
      const tabStore = useMultipleTabStore();
      if (name && Object.keys(params).length > 0) {
        tabStore.setRedirectPageParam({
          redirect_type: 'name',
          name: String(name),
          params,
          query,
        });
        params['path'] = String(name);
      } else {
        tabStore.setRedirectPageParam({
          redirect_type: 'path',
          path: fullPath,
          query,
        });
        params['path'] = fullPath;
      }
      // update-end--author:liaozhiyang---date:20231123---for：【QQYUN-7099】动态路由匹配右键重新加载404
      push({ name: REDIRECT_NAME, params, query }).then(() => resolve(true));
    });
  }
  return redo;
};
