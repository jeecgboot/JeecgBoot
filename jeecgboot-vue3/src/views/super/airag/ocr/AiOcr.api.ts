import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

export enum Api {
  list = '/airag/ocr/list',
  add = '/airag/ocr/add',
  edit = '/airag/ocr/edit',
  deleteById = '/airag/ocr/deleteById',
  flowRun = '/airag/flow/run',
}

/**
 * 查询ocr列表
 *
 * @param params
 */
export const list = (params) => {
  return defHttp.get({ url: Api.list, params });
};

/**
 * 添加Orc
 * @param params
 * @param handleSuccess
 */
export const addOcr = (params) => {
  return defHttp.post({ url: Api.add, params });
};

/**
 * 编辑Orc
 * @param params
 * @param handleSuccess
 */
export const editOcr = (params) => {
  return defHttp.put({ url: Api.edit, params });
};

/**
 * 根据id删除 Orc
 * @param params
 * @param handleSuccess
 */
export const deleteOcrById = (params) => {
  return defHttp.delete({ url: Api.deleteById, params });
};
