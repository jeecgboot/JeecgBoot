import {BasicColumn, FormSchema} from '/@/components/Table';

const statusOptions = [
  {label: '禁用', value: '0'},
  {label: '启用', value: '1'},
]

export const columns: BasicColumn[] = [
  {
    title: '允许的表名',
    dataIndex: 'tableName',
  },
  {
    title: '允许的字段名',
    dataIndex: 'fieldName',
  },
  {
    title: '状态',
    dataIndex: 'status',
    customRender({text}) {
      const find = statusOptions.find(opt => opt.value === text);
      return find?.label || '未知';
    }
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  }
];

export const searchFormSchema: FormSchema[] = [
  {
    label: '允许的表名',
    field: 'tableName',
    component: 'Input',
  },
  {
    label: '允许的字段名',
    field: 'fieldName',
    component: 'Input',
  },
  {
    label: '状态',
    field: 'status',
    component: 'Select',
    componentProps: {
      options: statusOptions,
    },
  },
];

export const formSchema: FormSchema[] = [
  {label: '', field: 'id', component: 'Input', show: false},
  {
    label: '允许的表名',
    field: 'tableName',
    component: 'Input',
    required: true,
  },
  {
    label: '允许的字段名',
    field: 'fieldName',
    component: 'Input',
    required: true,
    helpMessage: '多个用逗号分割',
  },
  {
    label: '状态',
    field: 'status',
    component: 'Select',
    defaultValue: '1',
    componentProps: {
      options: statusOptions,
    },
  },
];
