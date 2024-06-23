import { defHttp } from '/@/utils/http/axios';

enum Api {
  list = '/sys/dataLog/list',
  queryDataVerList = '/sys/dataLog/queryDataVerList',
  queryCompareList = '/sys/dataLog/queryCompareList',
}

/**
 * 查询数据日志列表
 * @param params
 */
export const getDataLogList = (params) => {
  return defHttp.get({ url: Api.list, params });
};

/**
 * 查询数据日志列表
 * @param params
 */
export const queryDataVerList = (params) => {
  return defHttp.get({ url: Api.queryDataVerList, params });
};

/**
 * 查询对比数据
 * @param params
 */
export const queryCompareList = (params) => {
  return defHttp.get({ url: Api.queryCompareList, params });
};
