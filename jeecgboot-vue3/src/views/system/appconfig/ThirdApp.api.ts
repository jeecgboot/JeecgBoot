import { defHttp } from '/@/utils/http/axios';

enum Api {
  //第三方登录配置
  addThirdAppConfig = '/sys/thirdApp/addThirdAppConfig',
  editThirdAppConfig = '/sys/thirdApp/editThirdAppConfig',
  getThirdConfigByTenantId = '/sys/thirdApp/getThirdConfigByTenantId',
  syncDingTalkDepartUserToLocal = '/sys/thirdApp/sync/dingtalk/departAndUser/toLocal',
  getThirdUserByWechat = '/sys/thirdApp/getThirdUserByWechat',
  wechatEnterpriseToLocal = '/sys/thirdApp/sync/wechatEnterprise/departAndUser/toLocal',
  getThirdUserBindByWechat = '/sys/thirdApp/getThirdUserBindByWechat',
  deleteThirdAccount = '/sys/thirdApp/deleteThirdAccount',
}

/**
 * 第三方配置保存或者更新
 */
export const saveOrUpdateThirdConfig = (params, isUpdate) => {
  let url = isUpdate ? Api.editThirdAppConfig : Api.addThirdAppConfig;
  return defHttp.post({ url: url, params }, { joinParamsToUrl: true });
};

/**
 * 获取第三方配置
 * @param params
 */
export const getThirdConfigByTenantId = (params) => {
  return defHttp.get({ url: Api.getThirdConfigByTenantId, params });
};

/**
 * 同步钉钉部门用户到本地
 * @param params
 */
export const syncDingTalkDepartUserToLocal = () => {
  return defHttp.get({ url: Api.syncDingTalkDepartUserToLocal, timeout: 60000 }, { isTransformResponse: false });
};

/**
 * 获取企业微信绑定的用户信息
 * @param params
 */
export const getThirdUserByWechat = () => {
  return defHttp.get({ url: Api.getThirdUserByWechat }, { isTransformResponse: false });
};

/**
 * 同步企业微信用户部门到本地
 * @param params
 */
export const wechatEnterpriseToLocal = (params) => {
  return defHttp.get({ url: Api.wechatEnterpriseToLocal, params }, { isTransformResponse: false });
};

/**
 * 获取绑定企业微信的用户
 * @param params
 */
export const getThirdUserBindByWechat = () => {
  return defHttp.get({ url: Api.getThirdUserBindByWechat }, { isTransformResponse: false });
};

/**
 * 根据第三方账号表的id解绑账号
 * @param params
 */
export const deleteThirdAccount = (params) => {
  return defHttp.delete({ url: Api.deleteThirdAccount, params }, { isTransformResponse:false, joinParamsToUrl: true });
};