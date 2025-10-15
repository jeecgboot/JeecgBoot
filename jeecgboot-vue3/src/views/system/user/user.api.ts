import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';
import { isObject } from '/@/utils/is';
enum Api {
  listNoCareTenant = '/sys/user/listAll',
  list = '/sys/user/list',
  save = '/sys/user/add',
  edit = '/sys/user/edit',
  getUserRole = '/sys/user/queryUserRole',
  duplicateCheck = '/sys/duplicate/check',
  deleteUser = '/sys/user/delete',
  deleteBatch = '/sys/user/deleteBatch',
  importExcel = '/sys/user/importExcel',
  exportXls = '/sys/user/exportXls',
  recycleBinList = '/sys/user/recycleBin',
  putRecycleBin = '/sys/user/putRecycleBin',
  deleteRecycleBin = '/sys/user/deleteRecycleBin',
  allRolesList = '/sys/role/queryall',
  allRolesListNoByTenant = '/sys/role/queryallNoByTenant',
  allTenantList = '/sys/tenant/queryList',
  allPostList = '/sys/position/list',
  userDepartList = '/sys/user/userDepartList',
  changePassword = '/sys/user/changePassword',
  frozenBatch = '/sys/user/frozenBatch',
  getUserAgent = '/sys/sysUserAgent/queryByUserName',
  userQuitAgent = '/sys/user/userQuitAgent',
  getQuitList = '/sys/user/getQuitList',
  putCancelQuit = '/sys/user/putCancelQuit',
  updateUserTenantStatus='/sys/tenant/updateUserTenantStatus',
  getUserTenantPageList='/sys/tenant/getUserTenantPageList',
}
/**
 * 导出api
 * @param params
 */
export const getExportUrl = Api.exportXls;
/**
 * 导入api
 */
export const getImportUrl = Api.importExcel;
/**
 * 列表接口(查询用户，通过租户隔离)
 * @param params
 */
export const list = (params) => defHttp.get({ url: Api.list, params });

/**
 * 列表接口(查询全部用户，不通过租户隔离)
 * @param params
 */
export const listNoCareTenant = (params) => defHttp.get({ url: Api.listNoCareTenant, params });

/**
 * 用户角色接口
 * @param params
 */
export const getUserRoles = (params) => defHttp.get({ url: Api.getUserRole, params }, { errorMessageMode: 'none' });

/**
 * 删除用户
 */
export const deleteUser = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteUser, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};
/**
 * 批量删除用户
 * @param params
 */
export const batchDeleteUser = (params, handleSuccess) => {
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
 * 保存或者更新用户
 * @param params
 */
export const saveOrUpdateUser = (params, isUpdate) => {
  let url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url: url, params });
};
/**
 * 唯一校验
 * @param params
 */
export const duplicateCheck = (params) => defHttp.get({ url: Api.duplicateCheck, params }, { isTransformResponse: false });

/**
 * 20231215
 * liaozhiyang
 * 唯一校验（ 延迟【防抖】）
 * @param params
 */
const timer = {};
export const duplicateCheckDelay = (params) => {
  return new Promise((resove, rejected) => {
    // -update-begin--author:liaozhiyang---date:20240619---for：【TV360X-1380】表单中使用多个duplicateCheckDelay，validate方法调用时会导致promise被挂起保存不了
    let key;
    if (isObject(params)) {
      key = `${params.tableName}_${params.fieldName}`;
    } else {
      key = params;
    }
    clearTimeout(timer[key]);
    // -update-end--author:liaozhiyang---date:20240619---for：【TV360X-1380】表单中使用多个duplicateCheckDelay，validate方法调用时会导致promise被挂起保存不了
    timer[key] = setTimeout(() => {
      defHttp
        .get({ url: Api.duplicateCheck, params }, { isTransformResponse: false })
        .then((res: any) => {
          resove(res as any);
        })
        .catch((error) => {
          rejected(error);
        });
      delete timer[key];
    }, 500);
  });
};
/**
 * 获取全部角色（租户隔离）
 * @param params
 */
export const getAllRolesList = (params) => defHttp.get({ url: Api.allRolesList, params });
/**
 * 获取全部角色（不租户隔离）
 * @param params
 */
export const getAllRolesListNoByTenant = (params) => defHttp.get({ url: Api.allRolesListNoByTenant, params });
/**
 * 获取全部租户
 */
export const getAllTenantList = (params) => defHttp.get({ url: Api.allTenantList, params });
/**
 * 获取指定用户负责部门
 */
export const getUserDepartList = (params) => defHttp.get({ url: Api.userDepartList, params }, { successMessageMode: 'none' });
/**
 * 获取全部职务
 */
export const getAllPostList = (params) => {
  return new Promise((resolve) => {
    defHttp.get({ url: Api.allPostList, params }).then((res) => {
      resolve(res.records);
    });
  });
};
/**
 * 回收站列表
 * @param params
 */
export const getRecycleBinList = (params) => defHttp.get({ url: Api.recycleBinList, params });
/**
 * 回收站还原
 * @param params
 */
export const putRecycleBin = (params, handleSuccess) => {
  return defHttp.put({ url: Api.putRecycleBin, params }).then(() => {
    handleSuccess();
  });
};
/**
 * 回收站删除
 * @param params
 */
export const deleteRecycleBin = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteRecycleBin, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};
/**
 * 修改密码
 * @param params
 */
export const changePassword = (params) => {
  return defHttp.put({ url: Api.changePassword, params });
};
/**
 * 冻结解冻
 * @param params
 */
export const frozenBatch = (params, handleSuccess) => {
  return defHttp.put({ url: Api.frozenBatch, params }).then(() => {
    handleSuccess();
  });
};


/**
 * 用户离职列表
 * @param params
 */
export const getQuitList = (params) => {
  return defHttp.get({ url: Api.getQuitList, params });
};

/**
 * 取消离职
 * @param params
 */
export const putCancelQuit = (params, handleSuccess) => {
  return defHttp.put({ url: Api.putCancelQuit, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};

/**
 * 待审批获取列表数据
 */
export const getUserTenantPageList = (params) => {
  return defHttp.get({ url: Api.getUserTenantPageList, params });
};

/**
 * 更新租户状态
 * @param params
 */
export const updateUserTenantStatus = (params) => {
  return defHttp.put({ url: Api.updateUserTenantStatus, params }, { joinParamsToUrl: true, isTransformResponse: false });
};
