import { defHttp } from '/@/utils/http/axios';

export enum Api {
  list = '/online/cgform/head/list',
  delete = '/online/cgform/head/delete',
  deleteBatch = '/online/cgform/head/deleteBatch',
  databaseSync = '/online/cgform/api/doDbSynch',
  removeRecord = '/online/cgform/head/removeRecord',
  copyOnline = '/online/cgform/head/copyOnline',
  copyTable = '/online/cgform/head/copyOnlineTable',
  queryById = '/online/cgform/head/queryById',

  // CgformModal页面API
  addAll = '/online/cgform/api/addAll',
  editAll = '/online/cgform/api/editAll',
  queryField = '/online/cgform/field/listByHeadId',
  queryIndex = '/online/cgform/index/listByHeadId',
  checkOnlyTable = '/online/cgform/api/checkOnlyTable',
  // 只修改表配置，不改字段
  editHead = '/online/cgform/head/edit'
}

/**
 * 列表接口
 * @param params
 */
export const list = (params) => defHttp.get({ url: Api.list, params });

// update-begin---author:liusq---date:20261013---for:【LHZP-1967】打印设计生成后表单扩展配置未带出报表地址
/**
 * 根据ID查询表单配置（获取最新的 extConfigJson 等）
 * @param id
 */
export const queryHeadById = (id: string) => defHttp.get({ url: Api.queryById, params: { id } });
// update-end---author:liusq---date:20261013---for:【LHZP-1967】打印设计生成后表单扩展配置未带出报表地址

// 批量移除（移除只会删除表单配置）
export const doBatchRemove = (idList: string[]) => doRemove(idList, 0);
export const doSingleRemove = (pid) => defHttp.delete({ url: Api.removeRecord, params: { id: pid } },
  { joinParamsToUrl: true });
// 批量删除（删除会删除对应的数据库表以及子表）
export const doBatchDelete = (idList: string[]) => doRemove(idList, 1);
export const doSingleDelete = (pid) => defHttp.delete({ url: Api.delete, params: { id: pid } },
  { joinParamsToUrl: true });

// 执行删除操作
function doRemove(idList: string[], flag: number) {
  return defHttp.delete(
    {
      url: Api.deleteBatch,
      params: {
        ids: idList.join(','),
        flag: flag,
      },
    },
    { joinParamsToUrl: true }
  );
}

// 同步数据库
export const doDatabaseSync = (id, method) =>
  defHttp.post({ url: `${Api.databaseSync}/${id}/${method}`, timeout: 12000, timeoutErrorMessage: '同步数据库超时，已自动刷新' });

export const doCopyOnlineView = (id) => defHttp.post({ url: `${Api.copyOnline}?code=${id}` });

/**
 * 复制表
 * @param id 要复制的表的id
 * @param tableName 新的表名
 * @param params 其他参数
 */
export const doCopyTable = (id, tableName, params?) => defHttp.get({ url: `${Api.copyTable}/${id}`, params: { tableName, ...params } });

// 弹窗formApi
export const formApi = {
  // 查询表字段
  doQueryField: (headId: string, params?) => defHttp.get({ url: Api.queryField, params: { headId, ...params } }),
  // 查询表index配置
  doQueryIndexes: (headId: string, params?) => defHttp.get({ url: Api.queryIndex, params: { headId, ...params } }),
  // 新增或修改
  doSaveOrUpdate: (params, isUpdate) => {
    if (isUpdate) {
      return defHttp.put({ url: Api.editAll, params });
    } else {
      return defHttp.post({ url: Api.addAll, params });
    }
  },
  //只是修改表配置不改字段
  editHead: (params)=>{
    return defHttp.put({ url: Api.editHead, params });
  }
};
