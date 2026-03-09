import { defHttp } from '/@/utils/http/axios';
import { useMessage } from '/@/hooks/web/useMessage';
import { downloadFile } from '/@/api/common/api';
import { message } from 'ant-design-vue';

const { createConfirm } = useMessage();

enum Api {
  list = '/airag/word/list',
  save = '/airag/word/add',
  edit = '/airag/word/edit',
  deleteOne = '/airag/word/delete',
  deleteBatch = '/airag/word/deleteBatch',
  downloadTpl = '/airag/word/download',
  parseFile = '/airag/word/parse/file',
  generateWord = '/airag/word/generate/word',
  generateResume = '/airag/flow/run',
}
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
    iconType: 'warning',
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
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
  const url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url: url, params });
};

/**
 * 下载模版
 * @param params
 */
export const downloadTpl = (params) => {
  return downloadFile(Api.downloadTpl, params.name + '.docx', { id: params.id });
};

/**
 * 生成word
 * @param params
 */
export const generateWord = (fileName, params) => {
  return defHttp
    .post(
      {
        url: Api.generateWord,
        params: params,
        responseType: 'blob',
      },
      { isTransformResponse: false }
    )
    .then((data) => {
      if (!data || data.size === 0) {
        message.warning('文件下载失败');
        return;
      }
      if (typeof window.navigator.msSaveBlob !== 'undefined') {
        window.navigator.msSaveBlob(new Blob([data]), fileName);
      } else {
        const url = window.URL.createObjectURL(new Blob([data]));
        const link = document.createElement('a');
        link.style.display = 'none';
        link.href = url;
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link); //下载完成移除元素
        window.URL.revokeObjectURL(url); //释放掉blob对象
      }
    });
};

/**
 * 生成简历
 * @param params
 */
export const generateResume = (params, handleSuccess) => {
  return defHttp
    .post(
      {
        url: Api.generateResume,
        params: params,
        timeout: 120000,
        timeoutErrorMessage: '同步数据库超时，已自动刷新',
      },
      { isTransformResponse: false }
    )
    .then((value) => {
      handleSuccess(value);
    });
};

/**
 * 解析word文档地址
 * @param params
 */
export const parseFileUrl = Api.parseFile;
