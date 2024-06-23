/* JVxeTable 行编辑 权限 */
import { usePermissionStoreWithOut } from '/@/store/modules/permission';

const permissionStore = usePermissionStoreWithOut();

/**
 * JVxe 专用，获取权限
 * @param prefix
 */
export function getJVxeAuths(prefix) {
  prefix = getPrefix(prefix);
  let { authList, allAuthList } = permissionStore;
  let authsMap = new Map<string, typeof allAuthList[0]>();
  if (!prefix || prefix.length == 0) {
    return authsMap;
  }
  // 将所有vxe用到的权限取出来
  for (let auth of allAuthList) {
    if (auth.status == '1' && (auth.action || '').startsWith(prefix)) {
      authsMap.set(auth.action, { ...auth, isAuth: false });
    }
  }
  // 设置是否已授权
  for (let auth of authList) {
    let getAuth = authsMap.get(auth.action);
    if (getAuth != null) {
      getAuth.isAuth = true;
    }
  }
  //update-begin-author:taoyan date:2022-6-1 for:  VUEN-1162 子表按钮没控制
  let onlineButtonAuths = permissionStore.getOnlineSubTableAuth(prefix);
  if (onlineButtonAuths && onlineButtonAuths.length > 0) {
    for (let auth of onlineButtonAuths) {
      authsMap.set(prefix + 'btn:' + auth, { action: auth, type: 1, status: 1, isAuth: false });
    }
  }
  //update-end-author:taoyan date:2022-6-1 for:  VUEN-1162 子表按钮没控制
  return authsMap;
}

/**
 * 获取前缀
 * @param prefix
 */
export function getPrefix(prefix: string) {
  if (prefix && !prefix.endsWith(':')) {
    return prefix + ':';
  }
  return prefix;
}
