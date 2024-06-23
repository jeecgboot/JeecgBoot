import { BasicColumn, FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '日志内容',
    dataIndex: 'logContent',
    width: 100,
    align: 'left',
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
    width: 80,
  },
  {
    title: '耗时(毫秒)',
    dataIndex: 'costTime',
    width: 80,
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
    width: 60,
  },
];

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
    width: 100,
    align: 'left',
  },
  {
    title: '请求地址',
    dataIndex: 'requestUrl',
    width: 100,
  },
  {
    title: '请求参数',
    dataIndex: 'method',
    width: 60,
  },
  {
    title: '操作人',
    dataIndex: 'username',
    width: 60,
    customRender: ({ record }) => {
      let pname = record.username;
      let pid = record.userid;
      if(!pname && !pid){
        return "";
      }
      return pname + " (账号: "+ pid + " )";
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
    width: 60,
  },
  {
    title: '客户端类型',
    dataIndex: 'clientType_dictText',
    width: 60,
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
