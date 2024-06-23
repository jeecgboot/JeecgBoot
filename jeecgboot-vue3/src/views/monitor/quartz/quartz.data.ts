import { BasicColumn, FormSchema } from '/@/components/Table';
import { render } from '/@/utils/common/renderUtils';
import { JCronValidator } from '/@/components/Form';

export const columns: BasicColumn[] = [
  {
    title: '任务类名',
    dataIndex: 'jobClassName',
    width: 200,
    align: 'left',
  },
  {
    title: 'Cron表达式',
    dataIndex: 'cronExpression',
    width: 200,
  },
  {
    title: '参数',
    dataIndex: 'parameter',
    width: 200,
  },
  {
    title: '描述',
    dataIndex: 'description',
    width: 200,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
    customRender: ({ text }) => {
      const color = text == '0' ? 'green' : text == '-1' ? 'red' : 'gray';
      return render.renderTag(render.renderDict(text, 'quartz_status'), color);
    },
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'jobClassName',
    label: '任务类名',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'status',
    label: '任务状态',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'quartz_status',
      stringToNumber: true,
    },
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
    field: 'jobClassName',
    label: '任务类名',
    component: 'Input',
    required: true,
  },
  {
    field: 'cronExpression',
    label: 'Cron表达式',
    component: 'JEasyCron',
    defaultValue: '* * * * * ? *',
    rules: [{ required: true, message: '请输入Cron表达式' }, { validator: JCronValidator }],
  },
  {
    field: 'paramterType',
    label: '参数类型',
    component: 'Select',
    defaultValue: 'string',
    componentProps: {
      options: [
        { label: '字符串', value: 'string' },
        { label: 'JSON对象', value: 'json' },
      ],
    },
  },
  {
    field: 'parameter',
    label: '参数',
    component: 'InputTextArea',
    ifShow: ({ values }) => {
      return values.paramterType == 'string';
    },
  },
  {
    field: 'parameter',
    label: '参数',
    component: 'JAddInput',
    helpMessage: '键值对形式填写',
    ifShow: ({ values }) => {
      return values.paramterType == 'json';
    },
  },
  {
    field: 'status',
    label: '状态',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'quartz_status',
      type: 'radioButton',
      stringToNumber: true,
      dropdownStyle: {
        maxHeight: '6vh',
      },
    },
  },
  {
    field: 'description',
    label: '描述',
    component: 'InputTextArea',
  },
];
