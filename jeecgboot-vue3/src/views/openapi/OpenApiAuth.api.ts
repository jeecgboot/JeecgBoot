import { defHttp } from '/@/utils/http/axios';
import { useMessage } from "/@/hooks/web/useMessage";

const { createConfirm } = useMessage();

enum Api {
  list = '/openapi/auth/list',
  save='/openapi/auth/add',
  edit='/openapi/auth/edit',
  apiList= '/openapi/list',
  genAKSK = '/openapi/auth/genAKSK',
  permissionList='/openapi/permission/getOpenApi',
  permissionAdd='/openapi/permission/add',
  deleteOne = '/openapi/auth/delete',
  deleteBatch = '/openapi/auth/deleteBatch',
  importExcel = '/openapi/auth/importExcel',
  exportXls = '/openapi/auth/exportXls',
}

/**
 * 获取API
 * @param params
 */
export const apiList = Api.apiList;
/**
 * 权限添加
 * @param params
 */
export const permissionAdd = Api.permissionAdd;
/**
 * 生成AKSK
 * @param params
 */
export const genAKSK = Api.genAKSK;

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
 * 批量删除
 * @param params
 * @param handleSuccess
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
 * @param isUpdate
 */
export const saveOrUpdate = (params, isUpdate) => {
  let url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url: url, params }, { isTransformResponse: false });
}

/**
 * 全部权限列表接口
 * @param params
 */
export const getApiList = (params) => defHttp.get({ url: Api.apiList, params });

/**
 * 获取已授权项目的接口
 * @param params
 */
export const getPermissionList = (params) => defHttp.get({ url: Api.permissionList, params });
/**
 * 授权保存方法
 * @param params
 * @param isUpdate
 */
export const permissionAddFunction = (params) => {
  return defHttp.post({ url: Api.permissionAdd, params }, { isTransformResponse: false });
}
/**
 * 授权保存方法
 * @param params
 * @param isUpdate
 */
export const getGenAKSK = (params) => {
  return defHttp.get({ url: Api.genAKSK, params });
}
