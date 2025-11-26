import { defHttp } from '/@/utils/http/axios';
// import { useMessage } from "/@/hooks/web/useMessage"; // 需要确认弹窗再启用

enum Api {
  list = '/airag/airagMcp/list',
  save='/airag/airagMcp/save',
  deleteOne = '/airag/airagMcp/delete',
  importExcel = '/airag/airagMcp/importExcel',
  exportXls = '/airag/airagMcp/exportXls',
  sync = '/airag/airagMcp/sync',
  toggleStatus = '/airag/airagMcp/status',
  saveAndSync = '/airag/airagMcp/saveAndSync',
  queryById = '/airag/airagMcp/queryById',
  saveTools = '/airag/airagMcp/saveTools',
}

/**
 * 导出api
 * @param params
 */
export const getExportUrl = Api.exportXls;

/**
 * 导入api
 */
export const getImportUrl = Api.importExcel;

/**
 * 列表接口
 * @param params
 */
export const list = (params) => defHttp.get({ url: Api.list, params });

/**
 * 删除单个
 * @param params
 * @param handleSuccess
 */
export const deleteOne = (params,handleSuccess) => {
  return defHttp.delete({url: Api.deleteOne, params}, {joinParamsToUrl: true}).then(() => {
    handleSuccess();
  });
}


/**
 * 保存或者更新
 * @param params
 * @param isUpdate
 */
export const saveOrUpdate = (params) => {
  return defHttp.post({url: Api.save, data: params}, { isTransformResponse: false });
}

/**
 * 保存并同步
 * @param params
 * @param isUpdate
 */
export const saveAndSync = (params) => {
  return defHttp.post({url: Api.saveAndSync, data: params}, { isTransformResponse: false });
}

/**
 * 同步接口
 * @param id
 */
export const syncMcp = (id) => defHttp.post({ url: Api.sync+"/"+id });

/**
 * 修改状态
 * @param id
 */
export const toggleStatus = (id,status) => defHttp.post({ url: Api.toggleStatus+"/"+id + "/"+ status });

/**
 * 详情查询
 * @param id
 */
export const queryById = (id) => defHttp.get({ url: Api.queryById ,params: { id:id }}, { isTransformResponse: false });

/**
 * 保存插件工具（仅更新tools字段）
 * @param id 插件ID
 * @param tools 工具列表JSON字符串
 */
export const saveTools = (id, tools) => defHttp.post({ url: Api.saveTools, data: { id, tools } }, { isTransformResponse: false });
