import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/sys/fillRule/list',
  test = '/sys/fillRule/testFillRule',
  save = '/sys/fillRule/add',
  edit = '/sys/fillRule/edit',
  delete = '/sys/fillRule/delete',
  deleteBatch = '/sys/fillRule/deleteBatch',
  exportXls = '/sys/fillRule/exportXls',
  importExcel = '/sys/fillRule/importExcel',
}

/**
 * 导出地址
 */
export const exportUrl = Api.exportXls;
/**
 * 导入地址
 */
export const importUrl = Api.importExcel;

/**
 * 列表查询
 * @param params
 */
export const getFillRuleList = (params) => {
  return defHttp.get({ url: Api.list, params });
};

/**
 * 删除
 * @param params
 * @param handleSuccess
 */
export const deleteFillRule = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.delete, data: params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};

/**
 * 批量删除
 * @param params
 */
export const batchDeleteFillRule = (params, handleSuccess) => {
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
 * 规则功能测试
 * @param params
 */
export const handleTest = (params) => {
  return defHttp.get({ url: Api.test, params }, { isTransformResponse: false });
};

/**
 * 保存
 * @param params
 */
export const saveFillRule = (params) => {
  return defHttp.post({ url: Api.save, params });
};

/**
 * 更新
 * @param params
 */
export const updateFillRule = (params) => {
  return defHttp.put({ url: Api.edit, params });
};
