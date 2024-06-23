import { BasicColumn } from '/@/components/Table/src/types/table';

import { Badge } from 'ant-design-vue';

export const refundTimeTableSchema: BasicColumn[] = [
  {
    title: '时间',
    width: 150,
    dataIndex: 't1',
  },
  {
    title: '当前进度',
    width: 150,
    dataIndex: 't2',
  },
  {
    title: '状态',
    width: 150,
    dataIndex: 't3',
    customRender: ({ record }) => {
      return <Badge status="success" text={record.t3} />;
    },
  },
  {
    title: '操作员ID	',
    width: 150,
    dataIndex: 't4',
  },
  {
    title: '耗时',
    width: 150,
    dataIndex: 't5',
  },
];

export const refundTimeTableData: any[] = [
  {
    t1: '2017-10-01 14:10',
    t2: '联系客户',
    t3: '进行中',
    t4: '取货员 ID1234',
    t5: '5mins',
  },
  {
    t1: '2017-10-01 14:10',
    t2: '取货员出发',
    t3: '成功',
    t4: '取货员 ID1234',
    t5: '5mins',
  },
  {
    t1: '2017-10-01 14:10',
    t2: '取货员接单',
    t3: '成功',
    t4: '系统',
    t5: '5mins',
  },
  {
    t1: '2017-10-01 14:10',
    t2: '申请审批通过',
    t3: '成功',
    t4: '用户',
    t5: '1h',
  },
];
