import { defHttp } from '/@/utils/http/axios';
enum Api {
  save = '/test/jeecgOrderMain/add',
  edit = '/test/jeecgOrderMain/edit',
  orderCustomerList = '/test/jeecgOrderMain/queryOrderCustomerListByMainId',
  orderTicketList = '/test/jeecgOrderMain/queryOrderTicketListByMainId',
}
export const orderCustomerList = Api.orderCustomerList;
export const orderTicketList = Api.orderTicketList;
/**
 * 保存或者更新
 * @param params
 */
export const saveOrUpdate = (params, isUpdate) => {
  let url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url: url, params });
};
