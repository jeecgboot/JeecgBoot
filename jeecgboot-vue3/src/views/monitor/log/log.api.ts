import { defHttp } from '/@/utils/http/axios';

enum Api {
  list = '/sys/log/list',
  exportXls = '/sys/log/exportXls',
}

/**
 * 查询日志列表
 * @param params
 */
export const getLogList = (params) => {
  return defHttp.get({ url: Api.list, params });
};


/**
 * 导出api
 * @param params
 */
export const getExportUrl = Api.exportXls;
