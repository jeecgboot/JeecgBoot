import { defHttp } from '/@/utils/http/axios';

enum Api {
  list = '/test/jeecgOrderMain/list',
  delete = '/test/jeecgOrderMain/delete',
  orderCustomerList = '/test/jeecgOrderMain/queryOrderCustomerListByMainId',
  orderTicketList = '/test/jeecgOrderMain/queryOrderTicketListByMainId',
}

/**
 * 列表接口
 * @param params
 */
export const list = (params) => defHttp.get({ url: Api.list, params });
/**
 * 子表单信息
 * @param params
 */
export const orderTicketList = (params) => defHttp.get({ url: Api.orderTicketList, params });
/**
 * 子表单信息
 * @param params
 */
export const orderCustomerList = (params) => defHttp.get({ url: Api.orderCustomerList, params });
/**
 * 删除用户
 */
export const deleteOne = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.delete, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};
