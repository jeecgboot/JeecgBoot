import { defHttp } from '/@/utils/http/axios';

enum Api {
  list = '/sys/annountCement/list',
  save = '/sys/annountCement/add',
  edit = '/sys/annountCement/edit',
  delete = '/sys/annountCement/delete',
  queryById = '/sys/annountCement/queryById',
  deleteBatch = '/sys/annountCement/deleteBatch',
  exportXls = '/sys/annountCement/exportXls',
  importExcel = '/sys/annountCement/importExcel',
  releaseData = '/sys/annountCement/doReleaseData',
  reovkeData = '/sys/annountCement/doReovkeData',
  editIzTop = '/sys/annountCement/editIzTop',

  addVisitsNum = '/sys/annountCement/addVisitsNumber',

  tempList = '/sys/message/sysMessageTemplate/list',
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
 * 查询消息列表
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
  const url = isUpdate ? Api.edit : Api.save;
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
 * 置顶编辑
 * @param params
 */
export const editIzTop = (params, handleSuccess) => {
  return defHttp.post({ url: Api.editIzTop, data: params }).then(() => {
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
/**
 * 新增访问量
 * @param id
 */
export const addVisitsNum = (params) => defHttp.get({ url: Api.addVisitsNum, params }, { successMessageMode: 'none' });
/**
 * 根据ID查询数据
 * @param id
 */
export const queryById = (params) => defHttp.get({ url: Api.queryById, params }, { isTransformResponse: false });
/**
 * 发起流程
 *  import { startProcess } from '/@/api/common/api';
 * @param params
 */
export const startProcess = (params) => defHttp.post({ url: Api.startProcess, params }, { isTransformResponse: false });
/**
 * 查询模板列表
 * @param params
 */
export const getTempList = (params) => {
  return defHttp.get({ url: Api.tempList, params });
};
