import { defHttp } from '/@/utils/http/axios';
import { useMessage } from '/@/hooks/web/useMessage';
const { createConfirm } = useMessage();

enum Api {
  list = '/online/cgreport/head/list',
  save = '/online/cgreport/head/add',
  edit = '/online/cgreport/head/editAll',
  deleteOne = '/online/cgreport/head/delete',
  deleteBatch = '/online/cgreport/head/deleteBatch',
  copy = '/online/cgreport/head/copy',
  onlCgreportParamList = '/online/cgreport/param/listByHeadId',
  onlCgreportItemList = '/online/cgreport/item/listByHeadId',
  getDataSourceList = '/sys/dataSource/options',
  getParamsInfo = '/online/cgreport/api/getParamsInfo/',
  analyzeSql = '/online/cgreport/head/parseSql',
}

/**
 * 查询子表数据
 * @param params
 */
export const onlCgreportParamList = Api.onlCgreportParamList;
/**
 * 查询子表数据
 * @param params
 */
export const onlCgreportItemList = Api.onlCgreportItemList;
/**
 * 列表接口
 * @param params
 */
export const list = (params) => defHttp.get({ url: Api.list, params });

/**
 * 删除单个
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
  createConfirm({
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
    iconType: 'warning',
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
  if (isUpdate) {
    return defHttp.put({ url: Api.edit, params });
  } else {
    return defHttp.post({ url: Api.save, params });
  }
};

/**
 * 获取参数地址
 * @param params
 */
export const getReportParam = (id) => {
  return defHttp.get({ url: Api.getParamsInfo + id });
};

/**
 * 获取数据源列表
 */
export const getDataSourceList = () => {
  return defHttp.get({ url: Api.getDataSourceList });
};

/**
 * 复制报表
 * @param id 报表id
 */
export const doCopy = (id) => {
  // 注意：defHttp 的非 GET 请求会把 params 当作请求体 data，后端 @RequestParam 从 query 取参，
  // 因此这里把 id 直接拼到 URL 上（与 cgform 的 doCopyOnlineView 保持一致）
  return defHttp.post({ url: `${Api.copy}?id=${id}` });
};

/**
 * 解析sql
 * @param params
 */
export const analyzeSql = (params) => {
  return defHttp.get({
    url: Api.analyzeSql + '?' + params,
  });
};
