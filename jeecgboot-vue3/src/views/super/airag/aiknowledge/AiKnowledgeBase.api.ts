import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  //知识库管理
  list = '/airag/knowledge/list',
  save = '/airag/knowledge/add',
  delete = '/airag/knowledge/delete',
  queryById = '/airag/knowledge/queryById',
  edit = '/airag/knowledge/edit',
  rebuild = '/airag/knowledge/rebuild',
  //知识库文档
  knowledgeDocList = '/airag/knowledge/doc/list',
  knowledgeEditDoc = '/airag/knowledge/doc/edit',
  knowledgeDeleteBatchDoc = '/airag/knowledge/doc/deleteBatch',
  knowledgeRebuildDoc = '/airag/knowledge/doc/rebuild',
  knowledgeEmbeddingHitTest = '/airag/knowledge/embedding/hitTest',
}

/**
 * 查询知识库
 * @param params
 */
export const list = (params) => {
  return defHttp.get({ url: Api.list, params }, { isTransformResponse: false });
};

/**
 * 根据id查询知识库
 * @param params
 */
export const queryById = (params) => {
  return defHttp.get({ url: Api.queryById, params }, { isTransformResponse: false });
};

/**
 * 新增知识库
 * @param params
 */
export const saveKnowledge = (params) => {
  return defHttp.post({ url: Api.save, params });
};

/**
 * 编辑知识库
 *
 * @param params
 */
export const editKnowledge = (params) => {
  return defHttp.put({ url: Api.edit, params });
};

/**
 * 删除知识库
 */
export const deleteModel = (params, handleSuccess) => {
  Modal.confirm({
    title: '确认删除',
    content: '是否删除名称为'+params.name+'的知识库吗？',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({ url: Api.delete, params }, { joinParamsToUrl: true }).then(() => {
        handleSuccess();
      });
    },
  });
};

/**
 * 查询知识库详情
 * @param params
 */
export const knowledgeDocList = (params) => {
  return defHttp.get({ url: Api.knowledgeDocList, params }, { isTransformResponse: false });
};

/**
 * 知识库向量化
 *
 * @param params
 */
export const rebuild = (params) => {
  return defHttp.put({ url: Api.rebuild, params,timeout: 2 * 60 * 1000 }, { joinParamsToUrl: true, isTransformResponse: false });
};

/**
 * 新增知识库
 * @param params
 */
export const knowledgeSaveDoc = (params) => {
  return defHttp.post({ url: Api.knowledgeEditDoc, params });
};

/**
 * 文档向量化
 * @param params
 */
export const knowledgeRebuildDoc = (params, handleSuccess) => {
  return defHttp.put({ url: Api.knowledgeRebuildDoc, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};

/**
 * 批量删除文档
 *
 * @param params
 * @param handleSuccess
 */
export const knowledgeDeleteBatchDoc = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.knowledgeDeleteBatchDoc, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};

/**
 * 命中测试
 * @param params
 */
export const knowledgeEmbeddingHitTest = (params) => {
  let url = Api.knowledgeEmbeddingHitTest + '/' + params.knowId;
  return defHttp.get({ url: url, params }, { isTransformResponse: false });
};
