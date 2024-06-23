import { defHttp } from '/@/utils/http/axios';

enum Api {
  actuatorList = '/actuator/httptrace-new/',
}

/**
 * 追踪信息
 */
export const getActuatorList = (query: String, order: String) => {
  return defHttp.get({ url: Api.actuatorList + query + '/' + order }, { isTransformResponse: false });
};
