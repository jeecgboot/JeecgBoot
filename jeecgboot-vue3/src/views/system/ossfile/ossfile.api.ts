import { defHttp } from '/@/utils/http/axios';

enum Api {
  list = '/sys/oss/file/list',
  deleteFile = '/sys/oss/file/delete',
  ossUpload = '/sys/oss/file/upload',
  minioUpload = '/sys/upload/uploadMinio',
}

/**
 * oss上传
 * @param params
 */
export const getOssUrl = Api.ossUpload;
/**
 * minio上传
 * @param params
 */
export const getMinioUrl = Api.minioUpload;
/**
 * 列表接口
 * @param params
 */
export const list = (params) => defHttp.get({ url: Api.list, params });

/**
 * 删除用户
 */
export const deleteFile = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteFile, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};
