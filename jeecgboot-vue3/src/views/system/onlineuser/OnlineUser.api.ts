import { defHttp } from '/@/utils/http/axios';

enum Api {
  list = '/sys/online/list',
  forceLogout = '/sys/online/forceLogout'
}

/**
 * 列表
 * @param params
 */
export const list = (params) => defHttp.get({ url: Api.list, params });

/**
 * 批量删除角色
 * @param params
 */
export const forceLogout = (params) => {
  return defHttp.post({url:Api.forceLogout,params},{isTransformResponse:false})
};
