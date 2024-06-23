import type { RouteRecordRaw } from 'vue-router';

import { useAppStore } from '/@/store/modules/app';
import { usePermissionStore } from '/@/store/modules/permission';
import { useUserStore } from '/@/store/modules/user';

import { useTabs } from './useTabs';

import { router, resetRouter } from '/@/router';
// import { RootRoute } from '/@/router/routes';

import projectSetting from '/@/settings/projectSetting';
import { PermissionModeEnum } from '/@/enums/appEnum';
import { RoleEnum } from '/@/enums/roleEnum';

import { intersection } from 'lodash-es';
import { isArray } from '/@/utils/is';
import { useMultipleTabStore } from '/@/store/modules/multipleTab';

// User permissions related operations
export function usePermission() {
  const userStore = useUserStore();
  const appStore = useAppStore();
  const permissionStore = usePermissionStore();
  //动态加载流程节点表单权限
  let formData: any = {};
  function initBpmFormData(_bpmFormData) {
    formData = _bpmFormData;
  }
  const { closeAll } = useTabs(router);

  //==================================工作流权限判断-begin=========================================
  function hasBpmPermission(code, type) {
    // 禁用-type=2
    // 显示-type=1
    let codeList: string[] = [];
    let permissionList = formData.permissionList;
    if (permissionList && permissionList.length > 0) {
      for (let item of permissionList) {
        if (item.type == type) {
          codeList.push(item.action);
        }
      }
    }
    return codeList.indexOf(code) >= 0;
  }
  //==================================工作流权限判断-end=========================================

  /**
   * Change permission mode
   */
  async function togglePermissionMode() {
    appStore.setProjectConfig({
      permissionMode: projectSetting.permissionMode === PermissionModeEnum.BACK ? PermissionModeEnum.ROUTE_MAPPING : PermissionModeEnum.BACK,
    });
    location.reload();
  }

  /**
   * Reset and regain authority resource information
   * @param id
   */
  async function resume() {
    const tabStore = useMultipleTabStore();
    tabStore.clearCacheTabs();
    resetRouter();
    const routes = await permissionStore.buildRoutesAction();
    routes.forEach((route) => {
      router.addRoute(route as unknown as RouteRecordRaw);
    });
    permissionStore.setLastBuildMenuTime();
    closeAll();
  }

  /**
   * 确定是否存在权限
   */
  function hasPermission(value?: RoleEnum | RoleEnum[] | string | string[], def = true): boolean {
    // Visible by default
    if (!value) {
      return def;
    }

    const permMode = projectSetting.permissionMode;

    if ([PermissionModeEnum.ROUTE_MAPPING, PermissionModeEnum.ROLE].includes(permMode)) {
      if (!isArray(value)) {
        return userStore.getRoleList?.includes(value as RoleEnum);
      }
      return (intersection(value, userStore.getRoleList) as RoleEnum[]).length > 0;
    }

    if (PermissionModeEnum.BACK === permMode) {
      const allCodeList = permissionStore.getPermCodeList as string[];
      if (!isArray(value) && allCodeList && allCodeList.length > 0) {
        //=============================工作流权限判断-显示-begin==============================================
        if (formData) {
          let code = value as string;
          if (hasBpmPermission(code, '1') === true) {
            return true;
          }
        }
        //=============================工作流权限判断-显示-end==============================================
        return allCodeList.includes(value);
      }
      return (intersection(value, allCodeList) as string[]).length > 0;
    }
    return true;
  }
  /**
   * 是否禁用组件
   */
  function isDisabledAuth(value?: RoleEnum | RoleEnum[] | string | string[], def = true): boolean {
    //=============================工作流权限判断-禁用-begin==============================================
    if (formData) {
      let code = value as string;
      if (hasBpmPermission(code, '2') === true) {
        return true;
      }
      //update-begin-author:taoyan date:2022-6-17 for: VUEN-1342【流程】编码方式 节点权限配置好后，未生效
      if (isCodingButNoConfig(code) == true) {
        return false;
      }
      //update-end-author:taoyan date:2022-6-17 for: VUEN-1342【流程】编码方式 节点权限配置好后，未生效
    }
    //=============================工作流权限判断-禁用-end==============================================
    return !hasPermission(value);
  }

  /**
   * Change roles
   * @param roles
   */
  async function changeRole(roles: RoleEnum | RoleEnum[]): Promise<void> {
    if (projectSetting.permissionMode !== PermissionModeEnum.ROUTE_MAPPING) {
      throw new Error('Please switch PermissionModeEnum to ROUTE_MAPPING mode in the configuration to operate!');
    }

    if (!isArray(roles)) {
      roles = [roles];
    }
    userStore.setRoleList(roles);
    await resume();
  }

  /**
   * refresh menu data
   */
  async function refreshMenu() {
    resume();
  }

  //update-begin-author:taoyan date:2022-6-17 for: VUEN-1342【流程】编码方式 节点权限配置好后，未生效
  /**
   * 判断是不是 代码里写了逻辑但是没有配置权限这种情况
   */
  function isCodingButNoConfig(code) {
    let all = permissionStore.allAuthList;
    if (all && all instanceof Array) {
      let temp = all.filter((item) => item.action == code);
      if (temp && temp.length > 0) {
        if (temp[0].status == '0') {
          return true;
        }
      } else {
        return true;
      }
    }
    return false;
  }
  //update-end-author:taoyan date:2022-6-17 for: VUEN-1342【流程】编码方式 节点权限配置好后，未生效

  return { changeRole, hasPermission, togglePermissionMode, refreshMenu, isDisabledAuth, initBpmFormData };
}
