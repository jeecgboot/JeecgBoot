import { BasicColumn, FormSchema } from '/@/components/Table';
import { rules } from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';

export const columns: BasicColumn[] = [
  {
    title: '标题',
    width: 150,
    dataIndex: 'titile',
  },
  {
    title: '消息类型',
    dataIndex: 'msgCategory',
    width: 100,
    customRender: ({ text }) => {
      return render.renderDict(text, 'msg_category');
    },
  },
  {
    title: '发布人',
    width: 100,
    dataIndex: 'sender',
  },
  {
    title: '优先级',
    dataIndex: 'priority',
    width: 70,
    customRender: ({ text }) => {
      const color = text == 'L' ? 'blue' : text == 'M' ? 'yellow' : 'red';
      return render.renderTag(render.renderDict(text, 'priority'), color);
    },
  },
  {
    title: '通告对象',
    dataIndex: 'msgType',
    width: 100,
    customRender: ({ text }) => {
      return render.renderDict(text, 'msg_type');
    },
  },
  {
    title: '发布状态',
    dataIndex: 'sendStatus',
    width: 70,
    customRender: ({ text }) => {
      const color = text == '0' ? 'red' : text == '1' ? 'green' : 'gray';
      return render.renderTag(render.renderDict(text, 'send_status'), color);
    },
  },
  {
    title: '发布时间',
    width: 100,
    dataIndex: 'sendTime',
  },
  {
    title: '撤销时间',
    width: 100,
    dataIndex: 'cancelTime',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'titile',
    label: '标题',
    component: 'JInput',
    colProps: { span: 8 },
  },
];

export const formSchema: FormSchema[] = [
  {
    field: 'id',
    label: 'id',
    component: 'Input',
    show: false,
  },
  {
    field: 'msgCategory',
    label: '消息类型',
    required: true,
    component: 'JDictSelectTag',
    defaultValue: '1',
    componentProps: {
      type: 'radio',
      dictCode: 'msg_category',
      placeholder: '请选择类型',
    },
  },
  {
    field: 'titile',
    label: '标题',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入标题',
    },
  },
  {
    field: 'msgAbstract',
    label: '摘要',
    component: 'InputTextArea',
    required: true,
  },
  // {
  //   field: 'endTime',
  //   label: '截至日期',
  //   component: 'DatePicker',
  //   componentProps: {
  //     showTime: true,
  //     valueFormat: 'YYYY-MM-DD HH:mm:ss',
  //     placeholder: '请选择截至日期',
  //   },
  //   dynamicRules: ({ model }) => rules.endTime(model.startTime, true),
  // },
  {
    field: 'msgType',
    label: '接收用户',
    defaultValue: 'ALL',
    component: 'JDictSelectTag',
    required: true,
    componentProps: {
      type: 'radio',
      dictCode: 'msg_type',
      placeholder: '请选择发布范围',
    },
  },
  {
    field: 'userIds',
    label: '指定用户',
    component: 'JSelectUser',
    required: true,
    componentProps: {
      rowKey: 'id',
      labelKey: 'username',
    },
    ifShow: ({ values }) => values.msgType == 'USER',
  },
  {
    field: 'priority',
    label: '优先级',
    defaultValue: 'H',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'priority',
      type: 'radio',
      placeholder: '请选择优先级',
    },
  },
  {
    field: 'msgContent',
    label: '内容',
    component: 'Input',
    render: render.renderTinymce,
  },
];
