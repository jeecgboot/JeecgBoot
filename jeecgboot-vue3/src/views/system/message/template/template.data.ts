import { BasicColumn, FormSchema } from '/@/components/Table';
import { rules } from '/@/utils/helper/validator';
import { filterDictTextByCache } from '/@/utils/dict/JDictSelectUtil';

export const columns: BasicColumn[] = [
  {
    title: '模板标题',
    dataIndex: 'templateName',
    width: 80,
  },
  {
    title: '模板编码',
    dataIndex: 'templateCode',
    width: 100,
  },
  {
    title: '通知模板',
    dataIndex: 'templateContent',
    width: 150,
  },
  {
    title: '模板类型',
    dataIndex: 'templateType',
    width: 100,
    customRender: ({ text }) => filterDictTextByCache('msgType', text),
  },
  {
    title: '是否应用',
    dataIndex: 'useStatus',
    width: 90,
    customRender: function ({ text }) {
      if (text == '1') {
        return '是';
      } else {
        return '否';
      }
    },
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    label: '模板标题',
    field: 'templateName',
    component: 'Input',
  },
  {
    label: '模板编码',
    field: 'templateCode',
    component: 'Input',
  },
  {
    label: '模板类型',
    field: 'templateType',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'msgType',
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
    label: '模板标题',
    field: 'templateName',
    component: 'Input',
    required: true,
  },
  {
    label: '模板编码',
    field: 'templateCode',
    component: 'Input',
    dynamicRules: ({ model, schema }) => {
      return [ ...rules.duplicateCheckRule('sys_sms_template', 'template_code', model, schema, true)];
    },
    // 编辑模式下不可修改编码
    dynamicDisabled: (params) => !!params.values.id,
  },
  {
    label: '模板类型',
    field: 'templateType',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'msgType',
      placeholder: '请选择模板类型',
    },
    required: true,
  },
  {
    label: '是否应用',
    field: 'useStatus',
    component: 'JSwitch',
    componentProps: {
      options: ['1', '0'],
    },
  },
  {
    label: '模板内容',
    field: 'templateContent',
    component: 'InputTextArea',
    componentProps: {
      autoSize: {
        minRows: 8,
        maxRows: 8,
      },
    },
    ifShow: ({ values }) => {
      return !['2', '4', '5'].includes(values.templateType);
    },
  },

  {
    label: '模板内容',
    field: 'templateContent',
    component: 'JEditor',
    ifShow: ({ values }) => {
      return ['2', '4'].includes(values.templateType);
    },
  },
  {
    label: '模板内容',
    field: 'templateContent',
    component: 'JMarkdownEditor',
    ifShow: ({ values }) => {
      return ['5'].includes(values.templateType);
    },
  },
];

export const sendTestFormSchemas: FormSchema[] = [
  {
    label: '模板编码',
    field: 'templateCode',
    component: 'Input',
    show: false,
  },
  {
    label: '模板标题',
    field: 'templateName',
    component: 'Input',
    componentProps: { disabled: true },
  },
  {
    label: '模板内容',
    field: 'templateContent',
    component: 'InputTextArea',
    componentProps: { disabled: true, rows: 5 },
  },
  {
    label: '测试数据',
    field: 'testData',
    component: 'InputTextArea',
    required: true,
    helpMessage: 'JSON数据',
    defaultValue: '{}',
    componentProps: {
      placeholder: '请输入JSON格式测试数据',
      rows: 5,
    },
  },
  {
    label: '消息类型',
    field: 'msgType',
    component: 'JDictSelectTag',
    required: true,
    defaultValue:'system',
    componentProps: { dictCode: 'messageType',type:'radio' },
  },
  {
    label: '消息接收方',
    field: 'receiver',
    required: true,
    component: 'JSelectUser',
    componentProps: {
      labelKey: 'username',
      rowKey: 'username',
    },
  },
];
