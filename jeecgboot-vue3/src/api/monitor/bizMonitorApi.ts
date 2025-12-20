import { defHttp } from '/@/utils/http/axios';

// 业务指标API
export const getBizMetricList = (params) =>
  defHttp.get({
    url: '/monitor/bizMetric/list',
    params,
  });

// 预警规则API
export const getBizAlertRuleList = (params) =>
  defHttp.get({
    url: '/monitor/alertRule/list',
    params,
  });

export const addBizAlertRule = (params) =>
  defHttp.post({
    url: '/monitor/alertRule/add',
    params,
  });

export const editBizAlertRule = (params) =>
  defHttp.put({
    url: '/monitor/alertRule/edit',
    params,
  });

export const deleteBizAlertRule = (params) =>
  defHttp.delete({
    url: '/monitor/alertRule/delete',
    params,
  });
