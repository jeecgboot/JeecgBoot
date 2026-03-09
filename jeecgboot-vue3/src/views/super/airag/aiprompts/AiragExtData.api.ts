import {defHttp} from '/@/utils/http/axios';
import { useMessage } from "/@/hooks/web/useMessage";

const { createConfirm } = useMessage();

enum Api {
  list = '/airag/extData/list',
  queryById = '/airag/extData/queryById',
  save='/airag/extData/add',
  edit='/airag/extData/edit',
  deleteOne = '/airag/extData/delete',
  deleteBatch = '/airag/extData/deleteBatch',
  importExcel = '/airag/extData/importExcel',
  exportXls = '/airag/extData/exportXls',
  debugEvaluator = '/airag/extData/evaluator/debug',

  queryTrackById = '/airag/extData/queryTrackById',
  getTrackList = '/airag/extData/getTrackList',
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
export const list = (params) =>
  defHttp.get({url: Api.list, params});
/**
 * 调用轨迹接口
 * @param params
 */
export const getTrackList = (params) =>
  defHttp.get({url: Api.getTrackList, params});
/**
 * 根据数据集id查询数据
 * @param params
 */
export const queryById = (params) =>
  defHttp.get({url: Api.queryById, params},{isTransformResponse: false});
/**
 * 根据数据集id查询数据
 * @param params
 */
export const queryTrackById = (params) =>
  defHttp.get({url: Api.queryTrackById, params},{isTransformResponse: false});

/**
 * 删除单个
 */
export const deleteOne = (params,handleSuccess) => {
  return defHttp.delete({url: Api.deleteOne, params}, {joinParamsToUrl: true}).then(() => {
    handleSuccess();
  });
}
/**
 * 批量删除
 * @param params
 */
export const batchDelete = (params, handleSuccess) => {
  createConfirm({
    iconType: 'warning',
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({url: Api.deleteBatch, data: params}, {joinParamsToUrl: true}).then(() => {
        handleSuccess();
      });
    }
  });
}
/**
 * 保存或者更新
 * @param params
 */
export const saveOrUpdate = (params, isUpdate,showSuccessMsg=true) => {
  const url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({url: url, params},{successMessageMode:showSuccessMsg?'success':'none'});
}
/**
 * 調試
 * @param params
 */
export const debugEvaluator = (params) => {
  return defHttp.post({url: Api.debugEvaluator, params},{ isTransformResponse: false });
}
