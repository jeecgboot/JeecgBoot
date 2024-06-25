import { BasicColumn, FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '表名',
    dataIndex: 'dataTable',
    width: 150,
    align: 'left',
  },
  {
    title: '数据ID',
    dataIndex: 'dataId',
    width: 350,
  },
  {
    title: '版本号',
    dataIndex: 'dataVersion',
    width: 100,
  },
  {
    title: '数据内容',
    dataIndex: 'dataContent',
  },
  {
    title: '创建人',
    dataIndex: 'createBy',
    sorter: true,
    width: 200,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'dataTable',
    label: '表名',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'dataId',
    label: '数据ID',
    component: 'Input',
    colProps: { span: 8 },
  },
];
