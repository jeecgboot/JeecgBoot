import { FormSchema } from '/@/components/Form';
import { BasicColumn } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '姓名',
    dataIndex: 'realname',
    width: 150,
  },
  {
    title: '工号',
    dataIndex: 'workNo',
    width: 100,
  },
  {
    title: '部门',
    dataIndex: 'departName',
    width: 200,
  },
  {
    title: '职务',
    dataIndex: 'post',
    width: 150,
    slots: { customRender: 'post' },
  },
  {
    title: '手机',
    width: 150,
    dataIndex: 'telephone',
  },
  {
    title: '邮箱',
    width: 150,
    dataIndex: 'email',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    label: '姓名',
    field: 'realname',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '工号',
    field: 'workNo',
    component: 'Input',
    colProps: { span: 6 },
  },
];
