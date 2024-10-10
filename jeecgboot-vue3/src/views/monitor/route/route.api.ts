import { defHttp } from '/@/utils/http/axios';

enum Api {
  list = '/sys/gatewayRoute/list',
  deleteList = '/sys/gatewayRoute/deleteList',
  save = '/sys/gatewayRoute/add',
  edit = '/sys/gatewayRoute/updateAll',
  delete = '/sys/gatewayRoute/delete',

  copyRoute = '/sys/gatewayRoute/copyRoute',
  batchPutRecycleBin = '/sys/gatewayRoute/putRecycleBin',
  batchDeleteRecycleBin = '/sys/gatewayRoute/deleteRecycleBin',
}

/**
 * 查询路由列表
 * @param params
 */
export const getRouteList = (params) => {
  return defHttp.get({ url: Api.list, params });
};
/**
 * 查询逻辑删除的路由列表
 * @param params
 */
export const deleteRouteList = (params) => {
  return defHttp.get({ url: Api.deleteList, params });
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

/**
 * 回收站还原
 * @param params
 */
export const putRecycleBin = (params, handleSuccess) => {
  return defHttp.put({ url: Api.batchPutRecycleBin, params }).then(() => {
    handleSuccess();
  });
};
/**
 * 回收站删除
 * @param params
 */
export const deleteRecycleBin = (params, handleSuccess) => {
  return defHttp.delete({ url: `${Api.batchDeleteRecycleBin}?ids=${params.ids}` }).then(() => {
    handleSuccess();
  });
};
/**
 * 复制
 */
export const copyRoute = (params, handleSuccess) => {
  return defHttp.get({ url: Api.copyRoute, params }).then(() => {
    handleSuccess();
  });
};
