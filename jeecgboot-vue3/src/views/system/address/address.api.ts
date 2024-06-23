import { defHttp } from '/@/utils/http/axios';

export enum Api {
  list = '/sys/user/queryByOrgCodeForAddressList',
  positionList = '/sys/position/list',
  queryDepartTreeSync = '/sys/sysDepart/queryDepartTreeSync',
}
/**
 * 获取部门树列表
 */
export const queryDepartTreeSync = (params?) => defHttp.get({ url: Api.queryDepartTreeSync, params });
/**
 * 部门用户信息
 */
export const list = (params?) => defHttp.get({ url: Api.list, params });
/**
 * 职务list
 */
export const positionList = (params?) => defHttp.get({ url: Api.positionList, params });
