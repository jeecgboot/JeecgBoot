import {defHttp} from '/@/utils/http/axios';
import { useMessage } from "/@/hooks/web/useMessage";

const { createConfirm } = useMessage();

enum Api {
  list = '/openapi/list',
  save='/openapi/add',
  edit='/openapi/edit',
  deleteOne = '/openapi/delete',
  deleteBatch = '/openapi/deleteBatch',
  genPath = '/openapi/genPath',
  importExcel = '/openapi/importExcel',
  exportXls = '/openapi/exportXls',
  openApiHeaderList = '/openapi/list',
  openApiParamList = '/openapi/list',
  openApiJson = '/openapi/json',
}

/**
 * 子表单查询接口
 * @param params
 */
export const genPath = Api.genPath
/**
 * swagger文档json
 * @param params
 */
export const openApiJson = Api.openApiJson
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
 * 子表单查询接口
 * @param params
 */
export const queryOpenApiHeader = Api.openApiHeaderList
/**
 * 子表单查询接口
 * @param params
 */
export const queryOpenApiParam = Api.openApiParamList

/**
 * 列表接口
 * @param params
 */
export const list = (params) =>
  defHttp.get({url: Api.list, params});

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
export const saveOrUpdate = (params, isUpdate) => {
  if (isUpdate) {
    return defHttp.put({url: Api.edit, params});
  } else {
    return defHttp.post({url: Api.save, params});
  }
}
/**
 * 获取接口地址
 * @param params
 */
export const getGenPath = (params) =>
  defHttp.get({url: Api.genPath, params},{isTransformResponse:false});
/**
 * 子表列表接口
 * @param params
 */
export const openApiHeaderList = (params) =>
  defHttp.get({url: Api.openApiHeaderList, params},{isTransformResponse:false});
/**
 * 子表列表接口
 * @param params
 */
export const openApiParamList = (params) =>
  defHttp.get({url: Api.openApiParamList, params},{isTransformResponse:false});
/**
 * swagger文档json
 * @param params
 */
export const getOpenApiJson = (params) =>
  defHttp.get({url: Api.openApiJson, params},{isTransformResponse:false});
