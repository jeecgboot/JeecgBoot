import { BasicColumn, FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '日志内容',
    dataIndex: 'logContent',
    width: 200,
    align: 'left',
    ellipsis: true,
  },
  {
    title: '操作人ID',
    dataIndex: 'userid',
    width: 80,
  },
  {
    title: '操作人',
    dataIndex: 'username',
    width: 80,
  },
  {
    title: 'IP',
    dataIndex: 'ip',
    width: 60,
  },
  {
    title: '耗时(毫秒)',
    dataIndex: 'costTime',
    width: 50,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    sorter: true,
    width: 80,
  },
  {
    title: '客户端类型',
    dataIndex: 'clientType_dictText',
    width: 50,
  },
];

export const loginLogColumns: BasicColumn[] = columns.filter((column) => column.dataIndex !== 'costTime');

/**
 * 操作日志需要操作类型
 */
export const operationLogColumn: BasicColumn[] = [
  ...columns,
  {
    title: '操作类型',
    dataIndex: 'operateType_dictText',
    width: 40,
  },
];

export const exceptionColumns: BasicColumn[] = [
  {
    title: '异常标题',
    dataIndex: 'logContent',
    width: 200,
    align: 'left',
    ellipsis: true,
  },
  {
    title: '请求地址',
    dataIndex: 'requestUrl',
    width: 140,
    align: 'left',
    ellipsis: true,
  },
  {
    title: '请求方法',
    dataIndex: 'method',
    width: 120,
    align: 'left',
    ellipsis: true,
  },
  {
    title: '操作人',
    dataIndex: 'username',
    width: 80,
    customRender: ({ record }) => {
      const pname = record.username;
      const pid = record.userid;
      if (!pname && !pid) {
        return '';
      }
      return pname + ' (' + pid + ')';
    },
  },
  {
    title: 'IP',
    dataIndex: 'ip',
    width: 60,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    sorter: true,
    width: 80,
  },
  {
    title: '客户端类型',
    dataIndex: 'clientType_dictText',
    width: 50,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'keyWord',
    label: '搜索日志',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'fieldTime',
    component: 'RangePicker',
    label: '创建时间',
    componentProps: {
      valueType: 'Date',
    },
    colProps: {
      span: 6,
    },
  },
];

export const operationSearchFormSchema: FormSchema[] = [
  ...searchFormSchema,
  {
    field: 'operateType',
    label: '操作类型',
    component: 'JDictSelectTag',
    colProps: { span: 4 },
    componentProps: {
      dictCode: 'operate_type',
    },
  },
];
