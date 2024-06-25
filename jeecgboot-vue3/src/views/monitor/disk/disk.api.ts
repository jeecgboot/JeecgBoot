import { defHttp } from '/@/utils/http/axios';

enum Api {
  queryDiskInfo = '/sys/actuator/redis/queryDiskInfo',
}

/**
 * 详细信息
 */
export const queryDiskInfo = () => {
  return defHttp.get({ url: Api.queryDiskInfo }, { successMessageMode: 'none' });
};
