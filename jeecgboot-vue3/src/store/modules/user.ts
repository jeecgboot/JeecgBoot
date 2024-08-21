import type { UserInfo, LoginInfo } from '/#/store';
import type { ErrorMessageMode } from '/#/axios';
import { defineStore } from 'pinia';
import { store } from '/@/store';
import { RoleEnum } from '/@/enums/roleEnum';
import { PageEnum } from '/@/enums/pageEnum';
import { ROLES_KEY, TOKEN_KEY, USER_INFO_KEY, LOGIN_INFO_KEY, DB_DICT_DATA_KEY, TENANT_ID, OAUTH2_THIRD_LOGIN_TENANT_ID } from '/@/enums/cacheEnum';
import { getAuthCache, setAuthCache, removeAuthCache } from '/@/utils/auth';
import { GetUserInfoModel, LoginParams, ThirdLoginParams } from '/@/api/sys/model/userModel';
import { doLogout, getUserInfo, loginApi, phoneLoginApi, thirdLogin } from '/@/api/sys/user';
import { useI18n } from '/@/hooks/web/useI18n';
import { useMessage } from '/@/hooks/web/useMessage';
import { router } from '/@/router';
import { usePermissionStore } from '/@/store/modules/permission';
import { RouteRecordRaw } from 'vue-router';
import { PAGE_NOT_FOUND_ROUTE } from '/@/router/routes/basic';
import { isArray } from '/@/utils/is';
import { useGlobSetting } from '/@/hooks/setting';
import { JDragConfigEnum } from '/@/enums/jeecgEnum';
import { useSso } from '/@/hooks/web/useSso';
import { isOAuth2AppEnv } from "/@/views/sys/login/useLogin";
import { getUrlParam } from "@/utils";
interface dictType {
  [key: string]: any;
}
interface UserState {
  userInfo: Nullable<UserInfo>;
  token?: string;
  roleList: RoleEnum[];
  dictItems?: dictType | null;
  sessionTimeout?: boolean;
  lastUpdateTime: number;
  tenantid?: string | number;
  shareTenantId?: Nullable<string | number>;
  loginInfo?: Nullable<LoginInfo>;
}

export const useUserStore = defineStore({
  id: 'app-user',
  state: (): UserState => ({
    // 用户信息
    userInfo: null,
    // token
    token: undefined,
    // 角色列表
    roleList: [],
    // 字典
    dictItems: null,
    // session过期时间
    sessionTimeout: false,
    // Last fetch time
    lastUpdateTime: 0,
    //租户id
    tenantid: '',
    // 分享租户ID
    // 用于分享页面所属租户与当前用户登录租户不一致的情况
    shareTenantId: null,
    //登录返回信息
    loginInfo: null,
  }),
  getters: {
    getUserInfo(): UserInfo {
      if(this.userInfo == null){
        this.userInfo = getAuthCache<UserInfo>(USER_INFO_KEY)!=null ? getAuthCache<UserInfo>(USER_INFO_KEY) : null;
      }
      return this.userInfo || getAuthCache<UserInfo>(USER_INFO_KEY) || {};
    },
    getLoginInfo(): LoginInfo {
      return this.loginInfo || getAuthCache<LoginInfo>(LOGIN_INFO_KEY) || {};
    },
    getToken(): string {
      return this.token || getAuthCache<string>(TOKEN_KEY);
    },
    getAllDictItems(): [] {
      return this.dictItems || getAuthCache(DB_DICT_DATA_KEY);
    },
    getRoleList(): RoleEnum[] {
      return this.roleList.length > 0 ? this.roleList : getAuthCache<RoleEnum[]>(ROLES_KEY);
    },
    getSessionTimeout(): boolean {
      return !!this.sessionTimeout;
    },
    getLastUpdateTime(): number {
      return this.lastUpdateTime;
    },
    getTenant(): string | number {
      return this.tenantid || getAuthCache<string | number>(TENANT_ID);
    },
    // 是否有分享租户id
    hasShareTenantId(): boolean {
      return this.shareTenantId != null && this.shareTenantId !== '';
    },
  },
  actions: {
    setToken(info: string | undefined) {
      this.token = info ? info : ''; // for null or undefined value
      setAuthCache(TOKEN_KEY, info);
    },
    setRoleList(roleList: RoleEnum[]) {
      this.roleList = roleList;
      setAuthCache(ROLES_KEY, roleList);
    },
    setUserInfo(info: UserInfo | null) {
      this.userInfo = info;
      this.lastUpdateTime = new Date().getTime();
      setAuthCache(USER_INFO_KEY, info);
    },
    setLoginInfo(info: LoginInfo | null) {
      this.loginInfo = info;
      setAuthCache(LOGIN_INFO_KEY, info);
    },
    setAllDictItems(dictItems) {
      this.dictItems = dictItems;
      setAuthCache(DB_DICT_DATA_KEY, dictItems);
    },
    setAllDictItemsByLocal() {
      // update-begin--author:liaozhiyang---date:20240321---for：【QQYUN-8572】表格行选择卡顿问题（customRender中字典引起的）
      if (!this.dictItems) {
        const allDictItems = getAuthCache(DB_DICT_DATA_KEY);
        if (allDictItems) {
          this.dictItems = allDictItems;
        }
      }
      // update-end--author:liaozhiyang---date:20240321---for：【QQYUN-8572】表格行选择卡顿问题（customRender中字典引起的）
    },
    setTenant(id) {
      this.tenantid = id;
      setAuthCache(TENANT_ID, id);
    },
    setShareTenantId(id: NonNullable<typeof this.shareTenantId>) {
      this.shareTenantId = id;
    },
    setSessionTimeout(flag: boolean) {
      this.sessionTimeout = flag;
    },
    resetState() {
      this.userInfo = null;
      this.dictItems = null;
      this.token = '';
      this.roleList = [];
      this.sessionTimeout = false;
    },
    /**
     * 登录事件
     */
    async login(
      params: LoginParams & {
        goHome?: boolean;
        mode?: ErrorMessageMode;
      }
    ): Promise<GetUserInfoModel | null> {
      try {
        const { goHome = true, mode, ...loginParams } = params;
        const data = await loginApi(loginParams, mode);
        const { token, userInfo } = data;
        // save token
        this.setToken(token);
        this.setTenant(userInfo.loginTenantId);
        return this.afterLoginAction(goHome, data);
      } catch (error) {
        return Promise.reject(error);
      }
    },
    /**
     * 扫码登录事件
     */
    async qrCodeLogin(token): Promise<GetUserInfoModel | null> {
      try {
        // save token
        this.setToken(token);
        return this.afterLoginAction(true, {});
      } catch (error) {
        return Promise.reject(error);
      }
    },
    /**
     * 登录完成处理
     * @param goHome
     */
    async afterLoginAction(goHome?: boolean, data?: any): Promise<any | null> {
      if (!this.getToken) return null;
      //获取用户信息
      const userInfo = await this.getUserInfoAction();
      const sessionTimeout = this.sessionTimeout;
      if (sessionTimeout) {
        this.setSessionTimeout(false);
      } else {
        //update-begin---author:scott ---date::2024-02-21  for：【QQYUN-8326】登录不需要构建路由，进入首页有构建---
        // // 构建后台菜单路由
        // const permissionStore = usePermissionStore();
        // if (!permissionStore.isDynamicAddedRoute) {
        //   const routes = await permissionStore.buildRoutesAction();
        //   routes.forEach((route) => {
        //     router.addRoute(route as unknown as RouteRecordRaw);
        //   });
        //   router.addRoute(PAGE_NOT_FOUND_ROUTE as unknown as RouteRecordRaw);
        //   permissionStore.setDynamicAddedRoute(true);
        // }
        //update-end---author:scott ---date::2024-02-21  for：【QQYUN-8326】登录不需要构建路由，进入首页有构建---
        
        await this.setLoginInfo({ ...data, isLogin: true });
        //update-begin-author:liusq date:2022-5-5 for:登录成功后缓存拖拽模块的接口前缀
        localStorage.setItem(JDragConfigEnum.DRAG_BASE_URL, useGlobSetting().domainUrl);
        //update-end-author:liusq date:2022-5-5 for: 登录成功后缓存拖拽模块的接口前缀

        // update-begin-author:sunjianlei date:20230306 for: 修复登录成功后，没有正确重定向的问题
        let redirect = router.currentRoute.value?.query?.redirect as string;
        // 判断是否有 redirect 重定向地址
        //update-begin---author:wangshuai ---date:20230424  for：【QQYUN-5195】登录之后直接刷新页面导致没有进入创建组织页面------------
        if (redirect && goHome) {
        //update-end---author:wangshuai ---date:20230424  for：【QQYUN-5195】登录之后直接刷新页面导致没有进入创建组织页面------------
          // update-begin--author:liaozhiyang---date:20240104---for：【QQYUN-7804】部署生产环境，登录跳转404问题
          let publicPath = import.meta.env.VITE_PUBLIC_PATH;
          if (publicPath && publicPath != '/') {
            // update-begin--author:liaozhiyang---date:20240509---for：【issues/1147】登录跳转时去掉发布路径的最后一个/以解决404问题
            if (publicPath.endsWith('/')) {
              publicPath = publicPath.slice(0, -1);
            }
            redirect = publicPath + redirect;
          }
          // update-end--author:liaozhiyang---date:20240509---for：【issues/1147】登录跳转时去掉发布路径的最后一个/以解决404问题
          // 当前页面打开
          window.open(redirect, '_self')
          return data;
        }
        // update-end-author:sunjianlei date:20230306 for: 修复登录成功后，没有正确重定向的问题

        //update-begin---author:wangshuai---date:2024-04-03---for:【issues/1102】设置单点登录后页面，进入首页提示404，也没有绘制侧边栏 #1102---
        let ticket = getUrlParam('ticket');
        if(ticket){
          goHome && (window.location.replace((userInfo && userInfo.homePath) || PageEnum.BASE_HOME));
        }else{
          goHome && (await router.replace((userInfo && userInfo.homePath) || PageEnum.BASE_HOME));
        }
        //update-end---author:wangshuai---date:2024-04-03---for:【issues/1102】设置单点登录后页面，进入首页提示404，也没有绘制侧边栏 #1102---
      }
      return data;
    },
    /**
     * 手机号登录
     * @param params
     */
    async phoneLogin(
      params: LoginParams & {
        goHome?: boolean;
        mode?: ErrorMessageMode;
      }
    ): Promise<GetUserInfoModel | null> {
      try {
        const { goHome = true, mode, ...loginParams } = params;
        const data = await phoneLoginApi(loginParams, mode);
        const { token } = data;
        // save token
        this.setToken(token);
        return this.afterLoginAction(goHome, data);
      } catch (error) {
        return Promise.reject(error);
      }
    },
    /**
     * 获取用户信息
     */
    async getUserInfoAction(): Promise<UserInfo | null> {
      if (!this.getToken) {
        return null;
      }
      const { userInfo, sysAllDictItems } = await getUserInfo();
      if (userInfo) {
        const { roles = [] } = userInfo;
        if (isArray(roles)) {
          const roleList = roles.map((item) => item.value) as RoleEnum[];
          this.setRoleList(roleList);
        } else {
          userInfo.roles = [];
          this.setRoleList([]);
        }
        this.setUserInfo(userInfo);
      }
      /**
       * 添加字典信息到缓存
       * @updateBy:lsq
       * @updateDate:2021-09-08
       */
      if (sysAllDictItems) {
        this.setAllDictItems(sysAllDictItems);
      }
      return userInfo;
    },
    /**
     * 退出登录
     */
    async logout(goLogin = false) {
      if (this.getToken) {
        try {
          await doLogout();
        } catch {
          console.log('注销Token失败');
        }
      }

      // //update-begin-author:taoyan date:2022-5-5 for: src/layouts/default/header/index.vue showLoginSelect方法 获取tenantId 退出登录后再次登录依然能获取到值，没有清空
      // let username:any = this.userInfo && this.userInfo.username;
      // if(username){
      //   removeAuthCache(username)
      // }
      // //update-end-author:taoyan date:2022-5-5 for: src/layouts/default/header/index.vue showLoginSelect方法 获取tenantId 退出登录后再次登录依然能获取到值，没有清空

      this.setToken('');
      setAuthCache(TOKEN_KEY, null);
      this.setSessionTimeout(false);
      this.setUserInfo(null);
      this.setLoginInfo(null);
      this.setTenant(null);
      // update-begin--author:liaozhiyang---date:20240517---for：【TV360X-23】退出登录后会提示「Token时效，请重新登录」
      setTimeout(() => {
        this.setAllDictItems(null);
      }, 1e3);
      // update-end--author:liaozhiyang---date:20240517---for：【TV360X-23】退出登录后会提示「Token时效，请重新登录」
      //update-begin-author:liusq date:2022-5-5 for:退出登录后清除拖拽模块的接口前缀
      localStorage.removeItem(JDragConfigEnum.DRAG_BASE_URL);
      //update-end-author:liusq date:2022-5-5 for: 退出登录后清除拖拽模块的接口前缀

      //如果开启单点登录,则跳转到单点统一登录中心
      const openSso = useGlobSetting().openSso;
      if (openSso == 'true') {
        await useSso().ssoLoginOut();
      }
      //update-begin---author:wangshuai ---date:20230224  for：[QQYUN-3440]新建企业微信和钉钉配置表，通过租户模式隔离------------
      //退出登录的时候需要用的应用id
      if(isOAuth2AppEnv()){
        let tenantId = getAuthCache(OAUTH2_THIRD_LOGIN_TENANT_ID);
        removeAuthCache(OAUTH2_THIRD_LOGIN_TENANT_ID);
        goLogin && await router.push({ name:"Login",query:{ tenantId:tenantId }})
      }else{
        // update-begin-author:sunjianlei date:20230306 for: 修复登录成功后，没有正确重定向的问题
        goLogin && (await router.push({
          path: PageEnum.BASE_LOGIN,
          query: {
            // 传入当前的路由，登录成功后跳转到当前路由
            redirect: router.currentRoute.value.fullPath,
          }
        }));
        // update-end-author:sunjianlei date:20230306 for: 修复登录成功后，没有正确重定向的问题

      }
      //update-end---author:wangshuai ---date:20230224  for：[QQYUN-3440]新建企业微信和钉钉配置表，通过租户模式隔离------------
    },
    /**
     * 登录事件
     */
    async ThirdLogin(
      params: ThirdLoginParams & {
        goHome?: boolean;
        mode?: ErrorMessageMode;
      }
    ): Promise<any | null> {
      try {
        const { goHome = true, mode, ...ThirdLoginParams } = params;
        const data = await thirdLogin(ThirdLoginParams, mode);
        const { token } = data;
        // save token
        this.setToken(token);
        return this.afterLoginAction(goHome, data);
      } catch (error) {
        return Promise.reject(error);
      }
    },
    /**
     * 退出询问
     */
    confirmLoginOut() {
      const { createConfirm } = useMessage();
      const { t } = useI18n();
      createConfirm({
        iconType: 'warning',
        title: t('sys.app.logoutTip'),
        content: t('sys.app.logoutMessage'),
        onOk: async () => {
          await this.logout(true);
        },
      });
    },
  },
});

// Need to be used outside the setup
export function useUserStoreWithOut() {
  return useUserStore(store);
}
