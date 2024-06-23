import { defHttp } from '/@/utils/http/axios';
import { cloneObject } from '/@/utils/index';

export const backEndUrl = {
  // 获取启用的第三方App
  getEnabledType: '/sys/thirdApp/getEnabledType',
  // 企业微信
  wechatEnterprise: {
    user: '/sys/thirdApp/sync/wechatEnterprise/user',
    depart: '/sys/thirdApp/sync/wechatEnterprise/depart',
  },
  // 钉钉
  dingtalk: {
    user: '/sys/thirdApp/sync/dingtalk/user',
    depart: '/sys/thirdApp/sync/dingtalk/depart',
  },
};
// 启用了哪些第三方App（在此缓存）
let enabledTypes = null;

// 获取启用的第三方App
export const getEnabledTypes = async () => {
  // 获取缓存
  if (enabledTypes != null) {
    return cloneObject(enabledTypes);
  } else {
    let { success, result } = await defHttp.get({ url: backEndUrl.getEnabledType }, { isTransformResponse: false });
    if (success) {
      // 在此缓存
      enabledTypes = cloneObject(result);
      return result;
    } else {
      console.warn('getEnabledType查询失败：');
    }
  }
  return {};
};
