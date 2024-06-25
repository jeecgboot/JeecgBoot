import { defHttp } from '/@/utils/http/axios';

enum Api {
  list = '/sys/gatewayRoute/list',
  save = '/sys/gatewayRoute/add',
  edit = '/sys/gatewayRoute/updateAll',
  delete = '/sys/gatewayRoute/delete',
}

/**
 * 查询路由列表
 * @param params
 */
export const getRouteList = (params) => {
  return defHttp.get({ url: Api.list, params });
};

/**
 * 保存或者更新路由
 * @param params
 */
export const saveOrUpdateRoute = (params) => {
  return defHttp.post({ url: Api.edit, params });
};

/**
 * 删除路由
 * @param params
 */
export const deleteRoute = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.delete, data: params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};
