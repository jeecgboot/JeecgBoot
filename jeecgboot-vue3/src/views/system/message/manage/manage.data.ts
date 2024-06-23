import { BasicColumn, FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '消息标题',
    dataIndex: 'esTitle',
    width: 140,
  },
  {
    title: '发送内容',
    dataIndex: 'esContent',
    width: 200,
    // slots: { customRender: 'esContent' },
  },
  {
    title: '接收人',
    dataIndex: 'esReceiver',
    width: 140,
  },
  {
    title: '发送次数',
    dataIndex: 'esSendNum',
    width: 120,
  },
  {
    title: '发送状态',
    dataIndex: 'esSendStatus_dictText',
    width: 120,
  },
  {
    title: '发送时间',
    dataIndex: 'esSendTime',
    width: 140,
  },
  {
    title: '发送方式',
    dataIndex: 'esType_dictText',
    width: 120,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    label: '消息标题',
    field: 'esTitle',
    component: 'Input',
  },
  {
    label: '发送状态',
    field: 'esSendStatus',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'msgSendStatus',
    },
  },
  {
    label: '发送方式',
    field: 'esType',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'messageType',
    },
  },
];

export const formSchemas: FormSchema[] = [
  {
    label: 'ID',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    label: '消息标题',
    field: 'esTitle',
    component: 'Input',
    componentProps: { readOnly: true },
  },
  {
    label: '发送内容',
    field: 'esContent',
    component: 'InputTextArea',
    componentProps: { readOnly: true },
  },
  {
    label: '发送参数',
    field: 'esParam',
    component: 'Input',
    componentProps: { readOnly: true },
  },

  {
    label: '接收人',
    field: 'esReceiver',
    component: 'Input',
    componentProps: { readOnly: true },
  },
  {
    label: '发送方式',
    field: 'esType',
    component: 'JDictSelectTag',
    componentProps: { disabled: true, dictCode: 'messageType' },
  },
  {
    label: '发送时间',
    field: 'esSendTime',
    component: 'Input',
    componentProps: { readOnly: true },
  },
  {
    label: '发送状态',
    field: 'esSendStatus',
    component: 'JDictSelectTag',
    componentProps: { disabled: true, dictCode: 'msgSendStatus' },
  },
  {
    label: '发送次数',
    field: 'esSendNum',
    component: 'Input',
    componentProps: { readOnly: true },
  },
  {
    label: '发送失败原因',
    field: 'esResult',
    component: 'Input',
    componentProps: { readOnly: true },
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
    componentProps: { readOnly: true },
  },
];
