import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/sys/sysRoleIndex/list',
  save = '/sys/sysRoleIndex/add',
  edit = '/sys/sysRoleIndex/edit',
  deleteIndex = '/sys/sysRoleIndex/delete',
  deleteBatch = '/sys/sysRoleIndex/deleteBatch',
  queryIndexByCode = '/sys/sysRoleIndex/queryByCode',
}
/**
 * 系统角色列表
 * @param params
 */
export const list = (params) => defHttp.get({ url: Api.list, params });

/**
 * 删除角色
 */
export const deleteIndex = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteIndex, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};
/**
 * 批量删除角色
 * @param params
 */
export const batchDelete = (params, handleSuccess) => {
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
 * 保存或者更新首页配置
 * @param params
 */
export const saveOrUpdate = (params, isUpdate) => {
  const url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url: url, params });
};
/**
 * 查询首页配置
 * @param params
 */
export const queryIndexByCode = (params) => defHttp.get({ url: Api.queryIndexByCode, params }, { isTransformResponse: false });
