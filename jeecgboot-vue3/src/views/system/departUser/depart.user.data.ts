import { Ref } from 'vue';
import { duplicateCheckDelay } from '/@/views/system/user/user.api';
import { BasicColumn, FormSchema } from '/@/components/Table';
import { DescItem } from '/@/components/Description';
import { findTree } from '/@/utils/common/compUtils';

// 用户信息 columns
export const userInfoColumns: BasicColumn[] = [
  {
    title: '用户账号',
    dataIndex: 'username',
    width: 150,
  },
  {
    title: '用户名称',
    dataIndex: 'realname',
    width: 180,
  },
  {
    title: '部门',
    dataIndex: 'orgCode',
    width: 200,
  },
  {
    title: '性别',
    dataIndex: 'sex_dictText',
    width: 80,
  },
  {
    title: '电话',
    dataIndex: 'phone',
    width: 120,
  },
];

// 用户信息查询条件表单
export const userInfoSearchFormSchema: FormSchema[] = [
  {
    field: 'username',
    label: '用户账号',
    component: 'Input',
  },
];

// 部门角色 columns
export const departRoleColumns: BasicColumn[] = [
  {
    title: '部门角色名称',
    dataIndex: 'roleName',
    width: 100,
  },
  {
    title: '部门角色编码',
    dataIndex: 'roleCode',
    width: 100,
  },
  {
    title: '部门',
    dataIndex: 'departId_dictText',
    width: 100,
  },
  {
    title: '备注',
    dataIndex: 'description',
    width: 100,
  },
];

// 部门角色查询条件表单
export const departRoleSearchFormSchema: FormSchema[] = [
  {
    field: 'roleName',
    label: '部门角色名称',
    component: 'Input',
  },
];

// 部门角色弹窗form表单
export const departRoleModalFormSchema: FormSchema[] = [
  {
    label: 'id',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    field: 'roleName',
    label: '部门角色名称',
    component: 'Input',
    rules: [
      { required: true, message: '部门角色名称不能为空！' },
      { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' },
    ],
  },
  {
    field: 'roleCode',
    label: '部门角色编码',
    component: 'Input',
    dynamicDisabled: ({ values }) => {
      return !!values.id;
    },
    dynamicRules: ({ model }) => {
      return [
        { required: true, message: '部门角色编码不能为空！' },
        { min: 0, max: 64, message: '长度不能超过 64 个字符', trigger: 'blur' },
        {
          validator: (_, value) => {
            if (/[\u4E00-\u9FA5]/g.test(value)) {
              return Promise.reject('部门角色编码不可输入汉字！');
            }
            return new Promise((resolve, reject) => {
              let params = {
                tableName: 'sys_depart_role',
                fieldName: 'role_code',
                fieldVal: value,
                dataId: model.id,
              };
              duplicateCheckDelay(params)
                .then((res) => {
                  res.success ? resolve() : reject(res.message || '校验失败');
                })
                .catch((err) => {
                  reject(err.message || '验证失败');
                });
            });
          },
        },
      ];
    },
  },
  {
    field: 'description',
    label: '描述',
    component: 'Input',
    rules: [{ min: 0, max: 126, message: '长度不能超过 126 个字符', trigger: 'blur' }],
  },
];

// 基本信息form
export function useBaseInfoForm(treeData: Ref<any[]>) {
  const descItems: DescItem[] = [
    {
      field: 'departName',
      label: '机构名称',
    },
    {
      field: 'parentId',
      label: '上级部门',
      render(val) {
        if (val) {
          let data = findTree(treeData.value, (item) => item.key == val);
          return data?.title ?? val;
        }
        return val;
      },
    },
    {
      field: 'orgCode',
      label: '机构编码',
    },
    {
      field: 'orgCategory',
      label: '机构类型',
      render(val) {
        if (val === '1') {
          return '公司';
        } else if (val === '2') {
          return '部门';
        } else if (val === '3') {
          return '岗位';
        }
        return val;
      },
    },
    {
      field: 'departOrder',
      label: '排序',
    },

    {
      field: 'mobile',
      label: '手机号',
    },
    {
      field: 'address',
      label: '地址',
    },
    {
      field: 'memo',
      label: '备注',
    },
  ];

  return { descItems };
}
