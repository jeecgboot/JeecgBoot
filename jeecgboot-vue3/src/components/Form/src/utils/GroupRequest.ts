import { getAuthCache, setAuthCache } from '/@/utils/auth';
/**
 * 将一个请求分组
 *
 * @param getPromise 传入一个可以获取到Promise对象的方法
 * @param groupId 分组ID，如果不传或者为空则不分组
 * @param expire 过期时间，默认 半分钟
 */
export function httpGroupRequest(getPromise, groupId, expire = 1000 * 30) {
  if (groupId == null || groupId === '') {
    console.log('--------popup----------getFrom  DB-------with---no--groupId ');
    return getPromise();
  }

  if (getAuthCache(groupId)) {
    console.log('---------popup--------getFrom  Cache--------groupId = ' + groupId);
    return Promise.resolve(getAuthCache(groupId));
  } else {
    console.log('--------popup----------getFrom  DB---------groupId = ' + groupId);
  }

  // 还没有发出请求，就发出第一次的请求
  return getPromise().then((res) => {
    setAuthCache(groupId, res);
    return Promise.resolve(res);
  });
}
