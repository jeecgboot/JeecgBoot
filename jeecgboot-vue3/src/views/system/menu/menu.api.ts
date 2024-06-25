import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/sys/permission/list',
  save = '/sys/permission/add',
  edit = '/sys/permission/edit',
  delete = '/sys/permission/delete',
  deleteBatch = '/sys/permission/deleteBatch',
  ruleList = '/sys/permission/queryPermissionRule',
  ruleSave = '/sys/permission/addPermissionRule',
  ruleEdit = '/sys/permission/editPermissionRule',
  ruleDelete = '/sys/permission/deletePermissionRule',
  checkPermDuplication = '/sys/permission/checkPermDuplication',
}

/**
 * 列表接口
 * @param params
 */
export const list = (params) => {
  return defHttp.get({ url: Api.list, params });
}

/**
 * 删除菜单
 */
export const deleteMenu = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.delete, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};
/**
 * 批量删除菜单
 * @param params
 */
export const batchDeleteMenu = (params, handleSuccess) => {
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
 * 保存或者更新菜单
 * @param params
 */
export const saveOrUpdateMenu = (params, isUpdate) => {
  let url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url: url, params });
};
/**
 * 菜单数据权限列表接口
 * @param params
 */
export const dataRuleList = (params) => defHttp.get({ url: Api.ruleList, params });
/**
 * 保存或者更新数据规则
 * @param params
 */
export const saveOrUpdateRule = (params, isUpdate) => {
  let url = isUpdate ? Api.ruleEdit : Api.ruleSave;
  return defHttp.post({ url: url, params });
};

/**
 * 删除数据权限
 */
export const deleteRule = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.ruleDelete, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};
/**
 * 根据code获取字典数值
 * @param params
 */
export const ajaxGetDictItems = (params) => defHttp.get({ url: `/sys/dict/getDictItems/${params.code}` });

/**
 * 唯一校验
 * @param params
 */
export const getCheckPermDuplication = (params) => defHttp.get({ url: Api.checkPermDuplication, params }, { isTransformResponse: false });

/**
 * 校验菜单是否存在
 * @param model
 * @param schema
 * @param required
 */
export const checkPermDuplication=(model, schema, required?)=>{
  return [
    {
      validator: (_, value) => {
        if (!required) {
          return Promise.resolve();
        }
        if (!value && required) {
          return Promise.reject(`请输入${schema.label}`);
        }
        return new Promise<void>((resolve, reject) => {
          getCheckPermDuplication({
            id: model.id,
            url:model.url,
            alwaysShow:model.alwaysShow
          }).then((res) => {
              res.success ? resolve() : reject(res.message || '校验失败');
          }).catch((err) => {
              reject(err.message || '验证失败');
          });
        });
      },
    },
  ];
}
