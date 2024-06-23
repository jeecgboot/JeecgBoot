import { BasicColumn } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '参数',
    dataIndex: 'param',
    width: 80,
    align: 'left',
    slots: { customRender: 'param' },
  },
  {
    title: '描述',
    dataIndex: 'text',
    slots: { customRender: 'text' },
    width: 80,
  },
  {
    title: '当前值',
    dataIndex: 'value',
    slots: { customRender: 'value' },
    width: 80,
  },
];
