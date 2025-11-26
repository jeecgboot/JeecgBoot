import type { Router, RouteRecordRaw } from 'vue-router';

import { usePermissionStoreWithOut } from '/@/store/modules/permission';

import { PageEnum } from '/@/enums/pageEnum';
import { useUserStoreWithOut } from '/@/store/modules/user';

import { PAGE_NOT_FOUND_ROUTE } from '/@/router/routes/basic';

import { RootRoute } from '/@/router/routes';

import {isOAuth2AppEnv, isOAuth2DingAppEnv} from '/@/views/sys/login/useLogin';
import { OAUTH2_THIRD_LOGIN_TENANT_ID } from "/@/enums/cacheEnum";
import { setAuthCache } from "/@/utils/auth";
import { PAGE_NOT_FOUND_NAME_404 } from '/@/router/constant';

const LOGIN_PATH = PageEnum.BASE_LOGIN;
//auth2登录路由
const OAUTH2_LOGIN_PAGE_PATH = PageEnum.OAUTH2_LOGIN_PAGE_PATH;

//分享免登录路由
const SYS_FILES_PATH = PageEnum.SYS_FILES_PATH;

// 邮件中的跳转地址,对应此路由,携带token免登录直接去办理页面
const TOKEN_LOGIN = PageEnum.TOKEN_LOGIN;

const ROOT_PATH = RootRoute.path;

// 代码逻辑说明: [VUEN-2472]分享免登录------------
const whitePathList: PageEnum[] = [LOGIN_PATH, OAUTH2_LOGIN_PAGE_PATH,SYS_FILES_PATH, TOKEN_LOGIN ];

export function createPermissionGuard(router: Router) {
  const userStore = useUserStoreWithOut();
  const permissionStore = usePermissionStoreWithOut();

  // 自定义首页跳转次数
  let homePathJumpCount = 0;

  router.beforeEach(async (to, from, next) => {
    if (
      // 【#6861】跳转到自定义首页的逻辑，只跳转一次即可
      homePathJumpCount < 1 &&
      from.path === ROOT_PATH &&
      to.path === PageEnum.BASE_HOME &&
      userStore.getUserInfo.homePath &&
      userStore.getUserInfo.homePath !== PageEnum.BASE_HOME
    ) {
      homePathJumpCount++;
      next(userStore.getUserInfo.homePath);
      return;
    }

    const token = userStore.getToken;

    // Whitelist can be directly entered
    if (whitePathList.includes(to.path as PageEnum)) {
      if (to.path === LOGIN_PATH && token) {
        const isSessionTimeout = userStore.getSessionTimeout;
        
        //TODO vben默认写法，暂时不知目的，有问题暂时先注释掉
        //await userStore.afterLoginAction();
        
        try {
          if (!isSessionTimeout) {
            next((to.query?.redirect as string) || '/');
            return;
          }
        } catch {}
        // 代码逻辑说明: [issues/I5BG1I]vue3不支持auth2登录------------
      } else if (to.path === LOGIN_PATH && isOAuth2AppEnv() && !token) {
        //退出登录进入此逻辑
        //如果进入的页面是login页面并且当前是OAuth2app环境，并且token为空，就进入OAuth2登录页面
        // 代码逻辑说明: [QQYUN-3440]新建企业微信和钉钉配置表，通过租户模式隔离------------
        if(to.query.tenantId){
          setAuthCache(OAUTH2_THIRD_LOGIN_TENANT_ID,to.query.tenantId)
        }
        next({ path: OAUTH2_LOGIN_PAGE_PATH });
        return;
      }
      next();
      return;
    }

    // token does not exist
    if (!token) {
      // You can access without permission. You need to set the routing meta.ignoreAuth to true
      if (to.meta.ignoreAuth) {
        next();
        return;
      }

      // 代码逻辑说明: [issues/I5BG1I]vue3 Auth2未实现------------
      let path = LOGIN_PATH;
      if (whitePathList.includes(to.path as PageEnum)) {
        // 在免登录白名单，如果进入的页面是login页面并且当前是OAuth2app环境，就进入OAuth2登录页面
        if (to.path === LOGIN_PATH && isOAuth2AppEnv()) {
          next({ path: OAUTH2_LOGIN_PAGE_PATH });
        } else {
          //在免登录白名单，直接进入
          next();
        }
      } else {
        //----------【首次登陆并且是企业微信或者钉钉的情况下才会调用】-----------------------------------------------
        //只有首次登陆并且是企业微信或者钉钉的情况下才会调用
        let href = window.location.href;
        //判断当前是auth2页面，并且是钉钉/企业微信，并且包含tenantId参数
        if(isOAuth2AppEnv() && href.indexOf("/tenantId/")!= -1){
          let params = to.params;
          if(params && params.path && params.path.length>0){
            //直接获取参数最后一位
            setAuthCache(OAUTH2_THIRD_LOGIN_TENANT_ID,params.path[params.path.length-1])
          }
        }
        //---------【首次登陆并且是企业微信或者钉钉的情况下才会调用】------------------------------------------------
        // 如果当前是在OAuth2APP环境，就跳转到OAuth2登录页面，否则跳转到登录页面
        path = isOAuth2AppEnv() ? OAUTH2_LOGIN_PAGE_PATH : LOGIN_PATH;
      }
      // redirect login page
      const redirectData: { path: string; replace: boolean; query?: Recordable<string> } = {
        // 代码逻辑说明: [issues/I5BG1I]vue3 Auth2未实现------------
        path: path,
        replace: true,
      };

      // 代码逻辑说明: 【QQYUN-4713】登录代码调整逻辑有问题，改造待观察--
      if (to.fullPath) {
        console.log("to.fullPath 1",to.fullPath)
        console.log("to.path 2",to.path)
        
        let getFullPath = to.fullPath;
        if(getFullPath=='/' || getFullPath=='/500' || getFullPath=='/400' || getFullPath=='/login?redirect=/' || getFullPath=='/login?redirect=/login?redirect=/'){
          return;
        }
        
        redirectData.query = {
          ...redirectData.query,
          // 代码逻辑说明: 修复登录成功后，没有正确重定向的问题
          redirect: to.fullPath,

        };
      }
      next(redirectData);
      return;
    }

    //==============================【首次登录并且是企业微信或者钉钉的情况下才会调用】==================
    //判断是免登录页面,如果页面包含/tenantId/,那么就直接前往主页
    if(isOAuth2AppEnv() && to.path.indexOf("/tenantId/") != -1){
      // 代码逻辑说明: 【TV360X-2958】钉钉登录后打开了敲敲云，换其他账号登录后，再打开敲敲云显示的是原来账号的应用---
      if (isOAuth2DingAppEnv()) {
        next(OAUTH2_LOGIN_PAGE_PATH);
      } else {
        next(userStore.getUserInfo.homePath || PageEnum.BASE_HOME);
      }
      return;
    }
    //==============================【首次登录并且是企业微信或者钉钉的情况下才会调用】==================
    // Jump to the 404 page after processing the login
    if (from.path === LOGIN_PATH && to.name === PAGE_NOT_FOUND_NAME_404 && to.fullPath !== (userStore.getUserInfo.homePath || PageEnum.BASE_HOME)) {
      next(userStore.getUserInfo.homePath || PageEnum.BASE_HOME);
      return;
    }

    // // get userinfo while last fetch time is empty
    // if (userStore.getLastUpdateTime === 0) {
    //   try {
    //     console.log("--LastUpdateTime---getUserInfoAction-----")
    //     await userStore.getUserInfoAction();
    //   } catch (err) {
    //     console.info(err);
    //     next();
    //   }
    // }
    // 代码逻辑说明: 【QQYUN-8572】表格行选择卡顿问题（customRender中字典引起的）
    if (userStore.getLastUpdateTime === 0) {
      userStore.setAllDictItemsByLocal();
    }
    if (permissionStore.getIsDynamicAddedRoute) {
      next();
      return;
    }

    // 构建后台菜单路由
    const routes = await permissionStore.buildRoutesAction();
    routes.forEach((route) => {
      router.addRoute(route as unknown as RouteRecordRaw);
    });

    router.addRoute(PAGE_NOT_FOUND_ROUTE as unknown as RouteRecordRaw);
    permissionStore.setDynamicAddedRoute(true);
    // 代码逻辑说明: 【issues/7500】vue-router4.5.0版本路由name:PageNotFound同名导致登录进不去
    if (to.name === PAGE_NOT_FOUND_NAME_404) {
      // 动态添加路由后，此处应当重定向到fullPath，否则会加载404页面内容
      next({ path: to.fullPath, replace: true, query: to.query });
    } else {
      const redirectPath = (from.query.redirect || to.path) as string;
      const redirect = decodeURIComponent(redirectPath);
      const nextData = to.path === redirect ? { ...to, replace: true } : { path: redirect };
      next(nextData);
    }
  });
}
