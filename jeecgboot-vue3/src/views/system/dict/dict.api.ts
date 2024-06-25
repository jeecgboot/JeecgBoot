import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';
enum Api {
  list = '/sys/dict/list',
  save = '/sys/dict/add',
  edit = '/sys/dict/edit',
  duplicateCheck = '/sys/duplicate/check',
  deleteDict = '/sys/dict/delete',
  deleteBatch = '/sys/dict/deleteBatch',
  importExcel = '/sys/dict/importExcel',
  exportXls = '/sys/dict/exportXls',
  recycleBinList = '/sys/dict/deleteList',
  putRecycleBin = '/sys/dict/back',
  deleteRecycleBin = '/sys/dict/deletePhysic',
  itemList = '/sys/dictItem/list',
  deleteItem = '/sys/dictItem/delete',
  itemSave = '/sys/dictItem/add',
  itemEdit = '/sys/dictItem/edit',
  dictItemCheck = '/sys/dictItem/dictItemCheck',
  refreshCache = '/sys/dict/refleshCache',
  queryAllDictItems = '/sys/dict/queryAllDictItems',
}
/**
 * 导出api
 * @param params
 */
export const getExportUrl = Api.exportXls;
/**
 * 导入api
 * @param params
 */
export const getImportUrl = Api.importExcel;
/**
 * 字典列表接口
 * @param params
 */
export const list = (params) => defHttp.get({ url: Api.list, params });
/**
 * 删除字典
 */
export const deleteDict = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteDict, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};
/**
 * 批量删除字典
 * @param params
 */
export const batchDeleteDict = (params, handleSuccess) => {
  Modal.confirm({
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({ url: Api.deleteBatch, data: params }, { joinParamsToUrl: true }).then(() => {
        handleSuccess();
      });
    },
  });
};
/**
 * 保存或者更新字典
 * @param params
 */
export const saveOrUpdateDict = (params, isUpdate) => {
  let url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url: url, params });
};
/**
 * 唯一校验
 * @param params
 */
export const duplicateCheck = (params) => defHttp.get({ url: Api.duplicateCheck, params }, { isTransformResponse: false });
/**
 * 字典回收站列表
 * @param params
 */
export const getRecycleBinList = (params) => defHttp.get({ url: Api.recycleBinList, params });
/**
 * 回收站还原
 * @param params
 */
export const putRecycleBin = (id, handleSuccess) => {
  return defHttp.put({ url: Api.putRecycleBin + `/${id}` }).then(() => {
    handleSuccess();
  });
};
/**
 * 回收站删除
 * @param params
 */
export const deleteRecycleBin = (id, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteRecycleBin + `/${id}` }).then(() => {
    handleSuccess();
  });
};
/**
 * 字典配置列表
 * @param params
 */
export const itemList = (params) => defHttp.get({ url: Api.itemList, params });
/**
 * 字典配置删除
 * @param params
 */
export const deleteItem = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteItem, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};
/**
 * 保存或者更新字典配置
 * @param params
 */
export const saveOrUpdateDictItem = (params, isUpdate) => {
  let url = isUpdate ? Api.itemEdit : Api.itemSave;
  return defHttp.post({ url: url, params });
};
/**
 * 校验字典数据值
 * @param params
 */
export const dictItemCheck = (params) => defHttp.get({ url: Api.dictItemCheck, params }, { isTransformResponse: false });
/**
 * 刷新字典
 * @param params
 */
export const refreshCache = () => defHttp.get({ url: Api.refreshCache }, { isTransformResponse: false });
/**
 * 获取所有字典项
 * @param params
 */
export const queryAllDictItems = () => defHttp.get({ url: Api.queryAllDictItems }, { isTransformResponse: false });
