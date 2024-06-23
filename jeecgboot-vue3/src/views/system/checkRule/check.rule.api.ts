import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/sys/checkRule/list',
  delete = '/sys/checkRule/delete',
  deleteBatch = '/sys/checkRule/deleteBatch',
  exportXls = 'sys/checkRule/exportXls',
  importXls = 'sys/checkRule/importExcel',
  checkByCode = '/sys/checkRule/checkByCode',
  save = '/sys/checkRule/add',
  edit = '/sys/checkRule/edit',
}

/**
 * 导出地址
 */
export const exportUrl = Api.exportXls;
/**
 * 导入地址
 */
export const importUrl = Api.importXls;

/**
 * 列表查询
 * @param params
 */
export const getCheckRuleList = (params) => {
  return defHttp.get({ url: Api.list, params });
};

/**
 * 删除
 * @param params
 * @param handleSuccess
 */
export const deleteCheckRule = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.delete, data: params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};

/**
 * 批量删除
 * @param params
 */
export const batchDeleteCheckRule = (params, handleSuccess) => {
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
 * 根据编码校验规则code，校验传入的值是否合法
 * @param ruleCode
 * @param value
 */
export const validateCheckRule = (ruleCode, value) => {
  value = encodeURIComponent(value);
  let params = { ruleCode, value };
  return defHttp.get({ url: Api.checkByCode, params }, { isTransformResponse: false });
};

/**
 * 保存
 * @param params
 */
export const saveCheckRule = (params) => {
  return defHttp.post({ url: Api.save, params });
};

/**
 * 更新
 * @param params
 */
export const updateCheckRule = (params) => {
  return defHttp.put({ url: Api.edit, params });
};
