import { BasicColumn, FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '路由ID',
    dataIndex: 'routerId',
    width: 200,
    align: 'left',
  },
  {
    title: '路由名称',
    dataIndex: 'name',
    width: 200,
  },
  {
    title: '路由URI',
    dataIndex: 'uri',
    width: 200,
  },
  {
    title: '状态',
    dataIndex: 'status',
    slots: { customRender: 'status' },
    width: 200,
  },
];

export const formSchema: FormSchema[] = [
  {
    field: 'name',
    label: '路由ID',
    component: 'Input',
    required: true,
  },
  {
    field: 'name',
    label: '路由名称',
    component: 'InputNumber',
    required: true,
  },
  {
    field: 'uri',
    label: '路由URI',
    component: 'Input',
  },
  {
    field: 'predicates',
    label: '路由条件',
    slot: 'predicates',
    component: 'Input',
  },
];
