import { defHttp } from '/@/utils/http/axios';

enum Api {
  TREE_OPTIONS_LIST = '/mock/tree/getDemoOptions',
}

/**
 * @description: Get sample options value
 */
export const treeOptionsListApi = (params?: Recordable) => defHttp.get<Recordable[]>({ url: Api.TREE_OPTIONS_LIST, params });
