import type { AppRouteRecordRaw, Menu } from '/@/router/types';

import { defineStore } from 'pinia';
import { store } from '/@/store';
import { useI18n } from '/@/hooks/web/useI18n';
import { useUserStore } from './user';
import { useAppStoreWithOut } from './app';
import { toRaw } from 'vue';
import { transformObjToRoute, flatMultiLevelRoutes, addSlashToRouteComponent } from '/@/router/helper/routeHelper';
import { transformRouteToMenu } from '/@/router/helper/menuHelper';

import projectSetting from '/@/settings/projectSetting';

import { PermissionModeEnum } from '/@/enums/appEnum';

import { asyncRoutes } from '/@/router/routes';
import { ERROR_LOG_ROUTE, PAGE_NOT_FOUND_ROUTE } from '/@/router/routes/basic';
import { staticRoutesList } from '../../router/routes/staticRouter';

import { filter } from '/@/utils/helper/treeHelper';

import { getBackMenuAndPerms } from '/@/api/sys/menu';

import { useMessage } from '/@/hooks/web/useMessage';
import { PageEnum } from '/@/enums/pageEnum';

// 系统权限
interface AuthItem {
  // 菜单权限编码，例如：“sys:schedule:list,sys:schedule:info”,多个逗号隔开
  action: string;
  // 权限策略1显示2禁用
  type: string | number;
  // 权限状态(0无效1有效)
  status: string | number;
  // 权限名称
  describe?: string;
  isAuth?: boolean;
}

interface PermissionState {
  // Permission code list
  permCodeList: string[] | number[];
  // Whether the route has been dynamically added
  isDynamicAddedRoute: boolean;
  // To trigger a menu update
  lastBuildMenuTime: number;
  // Backstage menu list
  backMenuList: Menu[];
  frontMenuList: Menu[];
  // 用户所拥有的权限
  authList: AuthItem[];
  // 全部权限配置
  allAuthList: AuthItem[];
  // 系统安全模式
  sysSafeMode: boolean;
  // online子表按钮权限
  onlineSubTableAuthMap: object;
}
export const usePermissionStore = defineStore({
  id: 'app-permission',
  state: (): PermissionState => ({
    permCodeList: [],
    // Whether the route has been dynamically added
    isDynamicAddedRoute: false,
    // To trigger a menu update
    lastBuildMenuTime: 0,
    // Backstage menu list
    backMenuList: [],
    // menu List
    frontMenuList: [],
    authList: [],
    allAuthList: [],
    sysSafeMode: false,
    onlineSubTableAuthMap: {},
  }),
  getters: {
    getPermCodeList(): string[] | number[] {
      return this.permCodeList;
    },
    getBackMenuList(): Menu[] {
      return this.backMenuList;
    },
    getFrontMenuList(): Menu[] {
      return this.frontMenuList;
    },
    getLastBuildMenuTime(): number {
      return this.lastBuildMenuTime;
    },
    getIsDynamicAddedRoute(): boolean {
      return this.isDynamicAddedRoute;
    },

    //update-begin-author:taoyan date:2022-6-1 for: VUEN-1162 子表按钮没控制
    getOnlineSubTableAuth: (state) => {
      return (code) => state.onlineSubTableAuthMap[code];
    },
    //update-end-author:taoyan date:2022-6-1 for: VUEN-1162 子表按钮没控制
  },
  actions: {
    setPermCodeList(codeList: string[]) {
      this.permCodeList = codeList;
    },

    setBackMenuList(list: Menu[]) {
      this.backMenuList = list;
      list?.length > 0 && this.setLastBuildMenuTime();
    },

    setFrontMenuList(list: Menu[]) {
      this.frontMenuList = list;
    },

    setLastBuildMenuTime() {
      this.lastBuildMenuTime = new Date().getTime();
    },

    setDynamicAddedRoute(added: boolean) {
      this.isDynamicAddedRoute = added;
    },
    resetState(): void {
      this.isDynamicAddedRoute = false;
      this.permCodeList = [];
      this.backMenuList = [];
      this.lastBuildMenuTime = 0;
    },
    async changePermissionCode() {
      const systemPermission = await getBackMenuAndPerms();
      const codeList = systemPermission.codeList;
      this.setPermCodeList(codeList);
      this.setAuthData(systemPermission);
      
      //菜单路由
      const routeList = systemPermission.menu;
      return routeList;
    },
    async buildRoutesAction(): Promise<AppRouteRecordRaw[]> {
      const { t } = useI18n();
      const userStore = useUserStore();
      const appStore = useAppStoreWithOut();

      let routes: AppRouteRecordRaw[] = [];
      const roleList = toRaw(userStore.getRoleList) || [];
      const { permissionMode = projectSetting.permissionMode } = appStore.getProjectConfig;

      const routeFilter = (route: AppRouteRecordRaw) => {
        const { meta } = route;
        const { roles } = meta || {};
        if (!roles) return true;
        return roleList.some((role) => roles.includes(role));
      };

      const routeRemoveIgnoreFilter = (route: AppRouteRecordRaw) => {
        const { meta } = route;
        const { ignoreRoute } = meta || {};
        return !ignoreRoute;
      };

      /**
       * @description 根据设置的首页path，修正routes中的affix标记（固定首页）
       * */
      const patchHomeAffix = (routes: AppRouteRecordRaw[]) => {
        if (!routes || routes.length === 0) return;
        let homePath: string = userStore.getUserInfo.homePath || PageEnum.BASE_HOME;
        function patcher(routes: AppRouteRecordRaw[], parentPath = '') {
          if (parentPath) parentPath = parentPath + '/';
          routes.forEach((route: AppRouteRecordRaw) => {
            const { path, children, redirect } = route;
            const currentPath = path.startsWith('/') ? path : parentPath + path;
            if (currentPath === homePath) {
              if (redirect) {
                homePath = route.redirect! as string;
              } else {
                route.meta = Object.assign({}, route.meta, { affix: true });
                throw new Error('end');
              }
            }
            children && children.length > 0 && patcher(children, currentPath);
          });
        }
        try {
          patcher(routes);
        } catch (e) {
          // 已处理完毕跳出循环
        }
        return;
      };

      switch (permissionMode) {
        case PermissionModeEnum.ROLE:
          routes = filter(asyncRoutes, routeFilter);
          routes = routes.filter(routeFilter);
          //  将多级路由转换为二级
          routes = flatMultiLevelRoutes(routes);
          break;

        case PermissionModeEnum.ROUTE_MAPPING:
          routes = filter(asyncRoutes, routeFilter);
          routes = routes.filter(routeFilter);
          const menuList = transformRouteToMenu(routes, true);
          routes = filter(routes, routeRemoveIgnoreFilter);
          routes = routes.filter(routeRemoveIgnoreFilter);
          menuList.sort((a, b) => {
            return (a.meta?.orderNo || 0) - (b.meta?.orderNo || 0);
          });

          this.setFrontMenuList(menuList);
          // 将多级路由转换为二级
          routes = flatMultiLevelRoutes(routes);
          break;

        // 后台菜单构建
        case PermissionModeEnum.BACK:
          const { createMessage, createWarningModal } = useMessage();
          console.log(" --- 构建后台路由菜单 --- ")
          // 菜单加载提示
          // createMessage.loading({
          //   content: t('sys.app.menuLoading'),
          //   duration: 1,
          // });

          // 从后台获取权限码，
          // 这个函数可能只需要执行一次，并且实际的项目可以在正确的时间被放置
          let routeList: AppRouteRecordRaw[] = [];
          try {
            routeList = await this.changePermissionCode();
            //routeList = (await getMenuList()) as AppRouteRecordRaw[];
            // update-begin--author:liaozhiyang---date:20240313---for：【QQYUN-8487】注释掉判断菜单是否vue2版本逻辑代码
            // update-begin----author:sunjianlei---date:20220315------for: 判断是否是 vue3 版本的菜单 ---
            // let hasIndex: boolean = false;
            // let hasIcon: boolean = false;
            // for (let menuItem of routeList) {
            //   // 条件1：判断组件是否是 layouts/default/index
            //   if (!hasIndex) {
            //     hasIndex = menuItem.component === 'layouts/default/index';
            //   }
            //   // 条件2：判断图标是否带有 冒号
            //   if (!hasIcon) {
            //     hasIcon = !!menuItem.meta?.icon?.includes(':');
            //   }
            //   // 满足任何一个条件都直接跳出循环
            //   if (hasIcon || hasIndex) {
            //     break;
            //   }
            // }
            // // 两个条件都不满足，就弹出提示框
            // if (!hasIcon && !hasIndex) {
            //   // 延迟1.5秒之后再出现提示，否则提示框出不来
            //   setTimeout(
            //     () =>
            //       createWarningModal({
            //         title: '检测提示',
            //         content:
            //           '当前菜单表是 <b>Vue2版本</b>，导致菜单加载异常!<br>点击确认，切换到Vue3版菜单！',
            //         onOk:function () {
            //           switchVue3Menu();
            //           location.reload();
            //         }
            //       }),
            //     100
            //   );
            // }
            // update-end----author:sunjianlei---date:20220315------for: 判断是否是 vue3 版本的菜单 ---
            // update-end--author:liaozhiyang---date:20240313---for：【QQYUN-8487】注释掉判断菜单是否vue2版本逻辑代码
          } catch (error) {
            console.error(error);
          }
          // 组件地址前加斜杠处理  author: lsq date:2021-09-08
          routeList = addSlashToRouteComponent(routeList);
          // 动态引入组件
          routeList = transformObjToRoute(routeList);

          // 构建后台路由菜单
          const backMenuList = transformRouteToMenu(routeList);
          this.setBackMenuList(backMenuList);

          // 删除meta.ignoreRoute项
          routeList = filter(routeList, routeRemoveIgnoreFilter);
          routeList = routeList.filter(routeRemoveIgnoreFilter);

          routeList = flatMultiLevelRoutes(routeList);
          // update-begin--author:liaozhiyang---date:20240529---for：【TV360X-522】ai助手路由写死在前端
          routes = [PAGE_NOT_FOUND_ROUTE, ...routeList, ...staticRoutesList];
          // update-end--author:liaozhiyang---date:20240529---for：【TV360X-522】ai助手路由写死在前端
          break;
      }

      routes.push(ERROR_LOG_ROUTE);
      patchHomeAffix(routes);
      return routes;
    },
    setAuthData(systemPermission) {
      this.authList = systemPermission.auth;
      this.allAuthList = systemPermission.allAuth;
      this.sysSafeMode = systemPermission.sysSafeMode;
    },
    setAuthList(authList: AuthItem[]) {
      this.authList = authList;
    },
    setAllAuthList(authList: AuthItem[]) {
      this.allAuthList = authList;
    },

    //update-begin-author:taoyan date:2022-6-1 for: VUEN-1162 子表按钮没控制
    setOnlineSubTableAuth(code, hideBtnList) {
      this.onlineSubTableAuthMap[code] = hideBtnList;
    },
    //update-end-author:taoyan date:2022-6-1 for: VUEN-1162 子表按钮没控制
  },
});

// 需要在设置之外使用
export function usePermissionStoreWithOut() {
  return usePermissionStore(store);
}
