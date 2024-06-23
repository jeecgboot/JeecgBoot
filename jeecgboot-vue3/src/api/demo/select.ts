import { defHttp } from '/@/utils/http/axios';
import { DemoOptionsItem, selectParams } from './model/optionsModel';
enum Api {
  OPTIONS_LIST = '/mock/select/getDemoOptions',
}

/**
 * @description: Get sample options value
 */
export const optionsListApi = (params?: selectParams) => defHttp.get<DemoOptionsItem[]>({ url: Api.OPTIONS_LIST, params });
