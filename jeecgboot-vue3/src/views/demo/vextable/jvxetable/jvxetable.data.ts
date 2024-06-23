import { JVxeTypes, JVxeColumn } from '/@/components/jeecg/JVxeTable/types';

export const columns: JVxeColumn[] = [
  {
    title: '客户名',
    key: 'name',
    width: 180,
    type: JVxeTypes.input,
    defaultValue: '',
    placeholder: '请输入${title}',
    validateRules: [{ required: true, message: '${title}不能为空' }],
  },
  {
    title: '性别',
    key: 'sex',
    width: 180,
    type: JVxeTypes.select,
    options: [
      // 下拉选项
      { title: '男', value: '1' },
      { title: '女', value: '2' },
    ],
    defaultValue: '',
    placeholder: '请选择${title}',
  },
  {
    title: '身份证号',
    key: 'idcard',
    width: 180,
    type: JVxeTypes.input,
    defaultValue: '',
    placeholder: '请输入${title}',
    validateRules: [
      {
        pattern: '^\\d{6}(18|19|20)?\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|[xX])$',
        message: '${title}格式不正确',
      },
    ],
  },
  {
    title: '手机号',
    key: 'telphone',
    width: 180,
    type: JVxeTypes.input,
    defaultValue: '',
    placeholder: '请输入${title}',
    validateRules: [
      {
        pattern: '^1[3456789]\\d{9}$',
        message: '${title}格式不正确',
      },
    ],
  },
];
export const columns1: JVxeColumn[] = [
  {
    title: '航班号',
    key: 'ticketCode',
    width: 180,
    type: JVxeTypes.input,
    defaultValue: '',
    placeholder: '请输入${title}',
    validateRules: [{ required: true, message: '${title}不能为空' }],
  },
  {
    title: '航班时间',
    key: 'tickectDate',
    width: 180,
    type: JVxeTypes.date,
    placeholder: '请选择${title}',
    defaultValue: '',
  },
];
