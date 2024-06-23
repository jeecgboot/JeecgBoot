import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/test/order/orderList',
  save = '/test/order/add',
  edit = '/test/order/edit',
  deleteOne = '/test/order/delete',
  deleteBatch = '/test/order/deleteBatch',
  customList = '/test/order/listOrderCustomerByMainId',
  saveCustomer = '/test/order/addCustomer',
  editCustomer = '/test/order/editCustomer',
  deleteCustomer = '/test/order/deleteCustomer',
  deleteBatchCustomer = '/test/order/deleteBatchCustomer',
  ticketList = '/test/order/listOrderTicketByMainId',
  saveTicket = '/test/order/addTicket',
  editTicket = '/test/order/editTicket',
  deleteTicket = '/test/order/deleteTicket',
  deleteBatchTicket = '/test/order/deleteBatchTicket',
}

/**
 * 列表接口
 * @param params
 */
export const list = (params) => defHttp.get({ url: Api.list, params });

/**
 * 删除
 */
export const deleteOne = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteOne, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};
/**
 * 批量删除
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
 * 保存或者更新
 * @param params
 */
export const saveOrUpdate = (params, isUpdate) => {
  let url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url: url, params });
};

/**
 * 列表接口
 * @param params
 */
export const customList = (params) => defHttp.get({ url: Api.customList, params });

/**
 * 删除
 */
export const deleteCustomer = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteCustomer, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};
/**
 * 批量删除
 * @param params
 */
export const deleteBatchCustomer = (params, handleSuccess) => {
  Modal.confirm({
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({ url: Api.deleteBatchCustomer, data: params }, { joinParamsToUrl: true }).then(() => {
        handleSuccess();
      });
    },
  });
};
/**
 * 保存或者更新
 * @param params
 */
export const saveOrUpdateCustomer = (params, isUpdate) => {
  let url = isUpdate ? Api.editCustomer : Api.saveCustomer;
  return defHttp.post({ url: url, params });
};
/**
 * 列表接口
 * @param params
 */
export const ticketList = (params) => defHttp.get({ url: Api.ticketList, params });

/**
 * 删除
 */
export const deleteTicket = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteTicket, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};
/**
 * 批量删除
 * @param params
 */
export const deleteBatchTicket = (params, handleSuccess) => {
  Modal.confirm({
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({ url: Api.deleteBatchTicket, data: params }, { joinParamsToUrl: true }).then(() => {
        handleSuccess();
      });
    },
  });
};
/**
 * 保存或者更新
 * @param params
 */
export const saveOrUpdateTicket = (params, isUpdate) => {
  let url = isUpdate ? Api.editTicket : Api.saveTicket;
  return defHttp.post({ url: url, params });
};
