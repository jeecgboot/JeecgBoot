import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';
import { getTenantId } from "/@/utils/auth";

enum Api {
  list = '/sys/tenant/list',
  save = '/sys/tenant/add',
  edit = '/sys/tenant/edit',
  get = '/sys/tenant/queryById',
  delete = '/sys/tenant/delete',
  deleteBatch = '/sys/tenant/deleteBatch',
  getCurrentUserTenants = '/sys/tenant/getCurrentUserTenant',
  invitationUserJoin = '/sys/tenant/invitationUserJoin',
  getTenantUserList = '/sys/tenant/getTenantUserList',
  leaveTenant = '/sys/tenant/leaveTenant',
  packList = '/sys/tenant/packList',
  addPackPermission = '/sys/tenant/addPackPermission',
  editPackPermission = '/sys/tenant/editPackPermission',
  deleteTenantPack = '/sys/tenant/deleteTenantPack',
  recycleBinPageList = '/sys/tenant/recycleBinPageList',
  deleteLogicDeleted = '/sys/tenant/deleteLogicDeleted',
  revertTenantLogic = '/sys/tenant/revertTenantLogic',
  //用户产品包关系api
  queryTenantPackUserList = '/sys/tenant/queryTenantPackUserList',
  deleteTenantPackUser = '/sys/tenant/deleteTenantPackUser',
  addTenantPackUser = '/sys/tenant/addTenantPackUser',
  //获取用户租户列表
  getTenantPageListByUserId = '/sys/tenant/getTenantPageListByUserId',
  
  //新增、编辑用户租户
  saveUser = '/sys/user/add',
  editUser = '/sys/user/editTenantUser',
}

/**
 * 查询租户列表
 * @param params
 */
export const getTenantList = (params) => {
  return defHttp.get({ url: Api.list, params });
};

/**
 * 保存或者更新租户
 * @param params
 */
export const saveOrUpdateTenant = (params, isUpdate) => {
  let url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url: url, params });
};

/**
 * 查询租户详情
 * @param params
 */
export const getTenantById = (params) => {
  return defHttp.get({ url: Api.get, params });
};

/**
 * 删除租户
 * @param params
 */
export const deleteTenant = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.delete, data: params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};

/**
 * 批量删除租户
 * @param params
 */
export const batchDeleteTenant = (params, handleSuccess) => {
  Modal.confirm({
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
 * 获取登录用户部门信息
 */
export const getUserTenants = (params?) => defHttp.get({ url: Api.getCurrentUserTenants, params });

/**
 * 邀请用户加入租户
 * @param params
 */
export const invitationUserJoin = (params) => defHttp.put({ url: Api.invitationUserJoin, params }, { joinParamsToUrl: true });

/**
 * 通过租户id获取数据
 * @param params
 */
export const getTenantUserList = (params) => {
  return defHttp.get({ url: Api.getTenantUserList, params });
};

/**
 * 用户离开租户
 * @param params
 */
export const leaveTenant = (params, handleSuccess) => {
  Modal.confirm({
    title: '请离',
    content: '是否请离该用户',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.put({ url: Api.leaveTenant, data: params }, { joinParamsToUrl: true }).then(() => {
        handleSuccess();
      });
    },
  });
};

/**
 * 获取产品包列表
 * @param params
 */
export const packList = (params) => {
  return defHttp.get({ url: Api.packList, params });
};

/**
 * 添加菜单
 * @param params
 */
export const addPackPermission = (params) => {
  return defHttp.post({ url: Api.addPackPermission, params });
};

/**
 * 添加菜单
 * @param params
 */
export const editPackPermission = (params) => {
  return defHttp.put({ url: Api.editPackPermission, params });
};

/**
 * 删除菜单
 * @param params
 */
export const deleteTenantPack = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteTenantPack, data: params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};

/**
 * 获取租户回收站的列表
 * @param params
 */
export const recycleBinPageList = (params) => {
  return defHttp.get({ url: Api.recycleBinPageList, params });
};

/**
 * 租户彻底删除
 * @param params
 */
export const deleteLogicDeleted = (params,handleSuccess) => {
  return defHttp.delete({ url: Api.deleteLogicDeleted, params },{ joinParamsToUrl: true }).then(() => {
    handleSuccess();
  }).catch(()=>{
    handleSuccess();
  });
};

/**
 * 租户还原
 * @param params
 */
export const revertTenantLogic = (params,handleSuccess) => {
  return defHttp.put({ url: Api.revertTenantLogic, params },{ joinParamsToUrl: true }).then(() => {
    handleSuccess();
  })
};

/**
 * 获取租户产品包下面的用户
 * @param params
 */
export const queryTenantPackUserList = (params) => {
  return defHttp.get({ url: Api.queryTenantPackUserList, params });
};

/**
 * 移除用户和产品包的关系数据
 * @param params
 */
export const deleteTenantPackUser = (params)=>{
  return defHttp.put({ url: Api.deleteTenantPackUser, params });
}

/**
 * 添加用户和产品包的关系数据
 * @param params
 */
export const addTenantPackUser = (params)=>{
  return defHttp.post({ url: Api.addTenantPackUser, params });
}

/**
 * 查询用户租户列表
 * @param params
 */
export const getTenantPageListByUserId = (params) => {
  return defHttp.get({ url: Api.getTenantPageListByUserId, params });
};


/**
 * 获取当前登录租户名称
 */
export async function getLoginTenantName() {
  let tenantId = getTenantId();
  if(tenantId){
    let result = await getTenantById({ id:tenantId });
    if(result){
      return result.name;
    }
  }
  return "空";
}

/**
 * 保存或者更新用户
 * @param params
 */
export const saveOrUpdateTenantUser = (params, isUpdate) => {
  let url = isUpdate ? Api.editUser : Api.saveUser;
  return defHttp.post({ url: url, params },{ joinParamsToUrl: true });
};
