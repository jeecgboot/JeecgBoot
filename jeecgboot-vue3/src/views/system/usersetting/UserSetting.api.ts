import { defHttp } from "/@/utils/http/axios";

enum Api {
  userEdit='/sys/user/login/setting/userEdit',
  getUserData='/sys/user/login/setting/getUserData',
  queryNameByCodes='/sys/position/queryByCodes',
  updateMobile='/sys/user/updateMobile',
  updateUserPassword='/sys/user/passwordChange',
  getTenantListByUserId = '/sys/tenant/getTenantListByUserId',
  cancelApplyTenant = '/sys/tenant/cancelApplyTenant',
  exitUserTenant = '/sys/tenant/exitUserTenant',
  changeOwenUserTenant = '/sys/tenant/changeOwenUserTenant',
  getThirdAccountByUserId = '/sys/thirdApp/getThirdAccountByUserId',
  bindThirdAppAccount = '/sys/thirdApp/bindThirdAppAccount',
  deleteThirdAccount = '/sys/thirdApp/deleteThirdAccount',
  agreeOrRefuseJoinTenant = '/sys/tenant/agreeOrRefuseJoinTenant',
  //更改手机号
  changePhone = '/sys/user/changePhone',
}

/**
 * 用户编辑
 * @param params
 */
export const userEdit = (params) => {
  return defHttp.post({ url: Api.userEdit, params },{ isTransformResponse:false });
}

/**
 * 获取用户信息
 * @param params
 */
export const getUserData = () => {
  return defHttp.get({ url: Api.getUserData },{ isTransformResponse:false });
}

/**
 * 获取多个职务信息
 * @param params
 */
export const queryNameByCodes = (params) => {
  return defHttp.get({ url: Api.queryNameByCodes, params },{isTransformResponse:false});
}

/**
 * 修改手机号
 * @param params
 */
export const updateMobile = (params) => {
  return defHttp.put({ url: Api.updateMobile, params },{isTransformResponse:false});
}

/**
 * 修改密码
 * @param params
 */
export const updateUserPassword = (params) => {
  return defHttp.get({ url: Api.updateUserPassword, params },{isTransformResponse:false});
}

/**
 * 通过用户id获取租户列表
 * @param params
 */
export const getTenantListByUserId = (params) => {
  return defHttp.get({ url: Api.getTenantListByUserId, params }, { isTransformResponse: false });
};

/**
 * 取消申请
 * @param params
 */
export const cancelApplyTenant = (params) => {
  return defHttp.put({ url: Api.cancelApplyTenant, data: params }, { joinParamsToUrl: true, isTransformResponse: false });
};

/**
 * 用户退出租户
 * @param params
 */
export const exitUserTenant = (params)=>{
  return defHttp.delete({ url: Api.exitUserTenant, params },{ isTransformResponse: false, joinParamsToUrl: true });
}

/**
 * 变更租户拥有者
 * @param params
 */
export const changeOwenUserTenant = (params)=>{
  return defHttp.post({ url: Api.changeOwenUserTenant, params },{ isTransformResponse: false, joinParamsToUrl: true });
}

/**
 * 获取账号第三方信息通过第三方类型
 * @param params
 */
export const getThirdAccountByUserId = (params) => {
  return defHttp.get({ url: Api.getThirdAccountByUserId, params }, { isTransformResponse: false });
};

/**
 * 根据第三方uuid绑定账号
 * @param params
 */
export const bindThirdAppAccount = (params) => {
  return defHttp.post({ url: Api.bindThirdAppAccount, params }, { isTransformResponse: false, joinParamsToUrl: true });
};

/**
 * 根据第三方uuid绑定账号
 * @param params
 */
export const deleteThirdAccount = (params) => {
  return defHttp.delete({ url: Api.deleteThirdAccount, params }, { isTransformResponse:false, joinParamsToUrl: true });
};

/**
 * 同意和拒绝加入租户
 * @param params
 */
export const agreeOrRefuseJoinTenant = (params) => {
  return defHttp.put({ url: Api.agreeOrRefuseJoinTenant, params },{ joinParamsToUrl: true });
};

/**
 * 更改手机号
 * @param params
 */
export const changePhone = (params) => {
  return defHttp.put({ url: Api.changePhone, params },{ joinParamsToUrl: true, isTransformResponse: false });
};
