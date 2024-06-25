import { defHttp } from '/@/utils/http/axios';

enum Api {
  list = '/sys/annountCement/list',
  save = '/sys/annountCement/add',
  edit = '/sys/annountCement/edit',
  delete = '/sys/annountCement/delete',
  deleteBatch = '/sys/annountCement/deleteBatch',
  exportXls = '/sys/annountCement/exportXls',
  importExcel = '/sys/annountCement/importExcel',
  releaseData = '/sys/annountCement/doReleaseData',
  reovkeData = '/sys/annountCement/doReovkeData',
}

/**
 * 导出url
 */
export const getExportUrl = Api.exportXls;
/**
 * 导入url
 */
export const getImportUrl = Api.importExcel;
/**
 * 查询租户列表
 * @param params
 */
export const getList = (params) => {
  return defHttp.get({ url: Api.list, params });
};

/**
 * 保存或者更新通告
 * @param params
 */
export const saveOrUpdate = (params, isUpdate) => {
  let url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url: url, params });
};

/**
 * 删除通告
 * @param params
 */
export const deleteNotice = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.delete, data: params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};

/**
 * 批量消息公告
 * @param params
 */
export const batchDeleteNotice = (params) => defHttp.delete({ url: Api.deleteBatch, data: params }, { joinParamsToUrl: true });

/**
 * 发布
 * @param id
 */
export const doReleaseData = (params) => defHttp.get({ url: Api.releaseData, params });
/**
 * 撤销
 * @param id
 */
export const doReovkeData = (params) => defHttp.get({ url: Api.reovkeData, params });
