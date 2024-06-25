import { Persistent, BasicKeys } from '/@/utils/cache/persistent';
import { CacheTypeEnum } from '/@/enums/cacheEnum';
import projectSetting from '/@/settings/projectSetting';
import { TOKEN_KEY, LOGIN_INFO_KEY, TENANT_ID } from '/@/enums/cacheEnum';

const { permissionCacheType } = projectSetting;
const isLocal = permissionCacheType === CacheTypeEnum.LOCAL;

/**
 * 获取token
 */
export function getToken() {
  return getAuthCache<string>(TOKEN_KEY);
}
/**
 * 获取登录信息
 */
export function getLoginBackInfo() {
  return getAuthCache(LOGIN_INFO_KEY);
}
/**
 * 获取租户id
 */
export function getTenantId() {
  return getAuthCache<string>(TENANT_ID);
}

export function getAuthCache<T>(key: BasicKeys) {
  const fn = isLocal ? Persistent.getLocal : Persistent.getSession;
  return fn(key) as T;
}

export function setAuthCache(key: BasicKeys, value) {
  const fn = isLocal ? Persistent.setLocal : Persistent.setSession;
  return fn(key, value, true);
}

/**
 * 设置动态key
 * @param key
 * @param value
 */
export function setCacheByDynKey(key, value) {
  const fn = isLocal ? Persistent.setLocal : Persistent.setSession;
  return fn(key, value, true);
}

/**
 * 获取动态key
 * @param key
 */
export function getCacheByDynKey<T>(key) {
  const fn = isLocal ? Persistent.getLocal : Persistent.getSession;
  return fn(key) as T;
}

/**
 * 移除动态key
 * @param key
 */
export function removeCacheByDynKey<T>(key) {
  const fn = isLocal ? Persistent.removeLocal : Persistent.removeSession;
  return fn(key) as T;
}
/**
 * 移除缓存中的某个属性
 * @param key
 * @update:移除缓存中的某个属性
 * @updateBy:lsq
 * @updateDate:2021-09-07
 */
export function removeAuthCache<T>(key: BasicKeys) {
  const fn = isLocal ? Persistent.removeLocal : Persistent.removeSession;
  return fn(key) as T;
}

export function clearAuthCache(immediate = true) {
  const fn = isLocal ? Persistent.clearLocal : Persistent.clearSession;
  return fn(immediate);
}
