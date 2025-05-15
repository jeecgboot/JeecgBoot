import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/airag/airagModel/list',
  save = '/airag/airagModel/add',
  delete = '/airag/airagModel/delete',
  queryById = '/airag/airagModel/queryById',
  edit = '/airag/airagModel/edit',
}

/**
 * 查询AI模型
 * @param params
 */
export const list = (params) => {
  return defHttp.get({ url: Api.list, params }, { isTransformResponse: false });
};

/**
 * 根据id查询AI模型
 * @param params
 */
export const queryById = (params) => {
  return defHttp.get({ url: Api.queryById, params }, { isTransformResponse: false });
};

/**
 * 新增AI模型
 *
 * @param params
 */
export const saveModel = (params) => {
  return defHttp.post({ url: Api.save, params });
};

/**
 * 编辑AI模型
 *
 * @param params
 */
export const editModel = (params) => {
  return defHttp.put({ url: Api.edit, params });
};

/**
 * 删除数据权限
 */
export const deleteModel = (params, handleSuccess) => {
  Modal.confirm({
    title: '确认删除',
    content: '是否删除名称为'+params.name+'的模型吗？',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({ url: Api.delete, params }, { joinParamsToUrl: true }).then(() => {
        handleSuccess();
      });
    },
  });
};
