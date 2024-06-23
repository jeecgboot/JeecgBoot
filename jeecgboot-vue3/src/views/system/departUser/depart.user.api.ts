import { unref } from 'vue';
import { defHttp } from '/@/utils/http/axios';
import { useMessage } from '/@/hooks/web/useMessage';

const { createConfirm } = useMessage();

enum Api {
  treeList = '/sys/sysDepart/queryMyDeptTreeList',
  queryIdTree = '/sys/sysDepart/queryIdTree',
  searchBy = '/sys/sysDepart/searchBy',
}

// 部门用户API
enum DepartUserApi {
  list = '/sys/user/departUserList',
  link = '/sys/user/editSysDepartWithUser',
  unlink = '/sys/user/deleteUserInDepartBatch',
}

// 部门角色API
enum DepartRoleApi {
  list = '/sys/sysDepartRole/list',
  deleteBatch = '/sys/sysDepartRole/deleteBatch',
  save = '/sys/sysDepartRole/add',
  edit = '/sys/sysDepartRole/edit',
  queryTreeListForDeptRole = '/sys/sysDepartPermission/queryTreeListForDeptRole',
  queryDeptRolePermission = '/sys/sysDepartPermission/queryDeptRolePermission',
  saveDeptRolePermission = '/sys/sysDepartPermission/saveDeptRolePermission',
  dataRule = '/sys/sysDepartRole/datarule',
  getDeptRoleList = '/sys/sysDepartRole/getDeptRoleList',
  getDeptRoleByUserId = '/sys/sysDepartRole/getDeptRoleByUserId',
  saveDeptRoleUser = '/sys/sysDepartRole/deptRoleUserAdd',
}

/**
 * 获取部门树列表
 */
export const queryMyDepartTreeList = (params?) => defHttp.get({ url: Api.treeList, params }, { isTransformResponse: false });

/**
 * 查询数据，以树结构形式加载所有部门的名称
 */
export const queryIdTree = (params?) => defHttp.get({ url: Api.queryIdTree, params });

/**
 * 根据关键字搜索部门
 */
export const searchByKeywords = (params) => defHttp.get({ url: Api.searchBy, params });

/**
 * 查询部门下的用户信息
 */
export const departUserList = (params) => defHttp.get({ url: DepartUserApi.list, params });

/**
 * 批量添加部门和用户的关联关系
 *
 * @param departId 部门ID
 * @param userIdList 用户ID列表
 */
export const linkDepartUserBatch = (departId: string, userIdList: string[]) =>
  defHttp.post({ url: DepartUserApi.link, params: { depId: departId, userIdList } });

/**
 * 批量取消部门和用户的关联关系
 */
export const unlinkDepartUserBatch = (params, confirm = false) => {
  return new Promise((resolve, reject) => {
    const doDelete = () => {
      resolve(defHttp.delete({ url: DepartUserApi.unlink, params }, { joinParamsToUrl: true }));
    };
    if (confirm) {
      createConfirm({
        iconType: 'warning',
        title: '取消关联',
        content: '确定要取消关联吗？',
        onOk: () => doDelete(),
        onCancel: () => reject(),
      });
    } else {
      doDelete();
    }
  });
};

/**
 * 查询部门角色信息
 */
export const departRoleList = (params) => defHttp.get({ url: DepartRoleApi.list, params });

/**
 * 保存或者更新部门角色
 */
export const saveOrUpdateDepartRole = (params, isUpdate) => {
  if (isUpdate) {
    return defHttp.put({ url: DepartRoleApi.edit, params });
  } else {
    return defHttp.post({ url: DepartRoleApi.save, params });
  }
};

/**
 * 批量删除部门角色
 */
export const deleteBatchDepartRole = (params, confirm = false) => {
  return new Promise((resolve, reject) => {
    const doDelete = () => {
      resolve(defHttp.delete({ url: DepartRoleApi.deleteBatch, params }, { joinParamsToUrl: true }));
    };
    if (confirm) {
      createConfirm({
        iconType: 'warning',
        title: '删除',
        content: '确定要删除吗？',
        onOk: () => doDelete(),
        onCancel: () => reject(),
      });
    } else {
      doDelete();
    }
  });
};

/**
 * 用户角色授权功能，查询菜单权限树
 */
export const queryTreeListForDeptRole = (params) => defHttp.get({ url: DepartRoleApi.queryTreeListForDeptRole, params });
/**
 * 查询角色授权
 */
export const queryDeptRolePermission = (params) => defHttp.get({ url: DepartRoleApi.queryDeptRolePermission, params });
/**
 * 保存角色授权
 */
export const saveDeptRolePermission = (params) => defHttp.post({ url: DepartRoleApi.saveDeptRolePermission, params });

/**
 *  查询部门角色数据权限列表
 */
export const queryDepartRoleDataRule = (functionId, departId, roleId, params?) => {
  let url = `${DepartRoleApi.dataRule}/${unref(functionId)}/${unref(departId)}/${unref(roleId)}`;
  return defHttp.get({ url, params });
};
/**
 * 保存部门角色数据权限
 */
export const saveDepartRoleDataRule = (params) => defHttp.post({ url: DepartRoleApi.dataRule, params });
/**
 * 查询部门角色用户授权
 */
export const queryDepartRoleUserList = (params) => defHttp.get({ url: DepartRoleApi.getDeptRoleList, params });
/**
 * 根据 userId 查询部门角色用户授权
 */
export const queryDepartRoleByUserId = (params) => defHttp.get({ url: DepartRoleApi.getDeptRoleByUserId, params });
/**
 * 保存部门角色用户授权
 */
export const saveDepartRoleUser = (params) => defHttp.post({ url: DepartRoleApi.saveDeptRoleUser, params });
