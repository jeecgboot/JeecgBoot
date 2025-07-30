import { FormSchema } from '/@/components/Table';

//列配置
export const columns = [
  {
    title: '关联类型(用户/角色)',
    dataIndex: 'relationType_dictText',
    width: 80,
    slots: { customRender: 'relationType' },
  },
  {
    title: '用户/角色编码',
    dataIndex: 'roleCode',
    width: 80,
    slots: { customRender: 'roleCode' },
  },
  {
    title: '首页路由',
    dataIndex: 'url',
    width: 100,
  },
  {
    title: '组件地址',
    dataIndex: 'component',
    width: 100,
  },
  {
    title: '是否开启',
    dataIndex: 'status',
    slots: { customRender: 'status' },
    width: 60,
  },
];
//查询配置
export const searchFormSchema: FormSchema[] = [
  {
    field: 'relationType',
    label: '关联类型',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'relation_type',
    },
  },
  {
    field: 'route',
    label: '是否路由菜单',
    helpMessage: '非路由菜单设置成首页，需开启',
    component: 'Switch',
    show: false,
  },
];

export const formSchema: FormSchema[] = [
  {
    field: 'id',
    label: '',
    component: 'Input',
    show: false,
  },
  {
    field: 'relationType',
    label: '关联类型',
    component: 'JDictSelectTag',
    required: true,
    defaultValue: 'ROLE',
    componentProps: {
      dictCode: 'relation_type',
      type: 'radioButton',
    },
  },
  {
    label: '角色编码',
    field: 'roleCode',
    component: 'JSelectRole',
    required: true,
    componentProps: {
      rowKey: 'roleCode',
      isRadioSelection: true,
    },
    ifShow: ({ values }) => values.relationType == 'ROLE',
  },
  {
    label: '用户编码',
    field: 'userCode',
    component: 'JSelectUser',
    required: true,
    componentProps: {
      isRadioSelection: true,
    },
    ifShow: ({ values }) => values.relationType == 'USER',
  },
  {
    label: '首页路由',
    field: 'url',
    component: 'Input',
    required: true,
  },
  {
    label: '组件地址',
    field: 'component',
    component: 'Input',
    componentProps: {
      placeholder: '请输入前端组件',
    },
    required: true,
  },
  {
    label: '优先级',
    field: 'priority',
    component: 'InputNumber',
  },
  {
    field: 'route',
    label: '是否路由菜单',
    helpMessage: '非路由菜单设置成首页，需开启',
    component: 'Switch',
    defaultValue: true,
    show: false,
  },
  {
    label: '是否开启',
    field: 'status',
    component: 'JSwitch',
    defaultValue: '1',
    componentProps: {
      options: ['1', '0'],
    },
  },
];
