import { defHttp } from '/@/utils/http/axios';

enum Api {
  //查询app版本
  queryAppVersion = '/sys/version/app3version',
  //保存app版本
  saveAppVersion = '/sys/version/saveVersion',
}
/**
 * 查询APP版本
 * @param params
 */
export const queryAppVersion = (params) => defHttp.get({ url: Api.queryAppVersion, params });
/**
 * 保存APP版本
 * @param params
 */
export const saveAppVersion = (params) => {
  return defHttp.post({ url: Api.saveAppVersion, params });
};
