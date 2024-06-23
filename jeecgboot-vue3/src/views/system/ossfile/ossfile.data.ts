import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '文件名称',
    dataIndex: 'fileName',
    width: 120,
  },
  {
    title: '文件地址',
    dataIndex: 'url',
    width: 100,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    label: '文件名称',
    field: 'fileName',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '文件地址',
    field: 'url',
    component: 'Input',
    colProps: { span: 6 },
  },
];
