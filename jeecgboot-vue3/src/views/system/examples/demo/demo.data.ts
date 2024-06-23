import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { render } from '/@/utils/common/renderUtils';

export const columns: BasicColumn[] = [
  {
    title: '姓名',
    dataIndex: 'name',
    width: 170,
    align: 'left',
    resizable: true,
    sorter: {
      multiple:1
    }
  },
  {
    title: '关键词',
    dataIndex: 'keyWord',
    width: 130,
    resizable: true,
  },
  {
    title: '打卡时间',
    dataIndex: 'punchTime',
    width: 140,
    resizable: true,
  },
  {
    title: '工资',
    dataIndex: 'salaryMoney',
    width: 140,
    resizable: true,
    sorter: {
      multiple: 2
    }
  },
  {
    title: '奖金',
    dataIndex: 'bonusMoney',
    width: 140,
    resizable: true,
  },
  {
    title: '性别',
    dataIndex: 'sex',
    sorter: {
      multiple: 3
    },
    customRender: ({ record }) => {
      return render.renderDict(record.sex, 'sex');
      // let v = record.sex ? (record.sex == '1' ? '男' : '女') : '';
      // return h('span', v);
    },
    width: 120,
    resizable: true,
  },
  {
    title: '生日',
    dataIndex: 'birthday',
    width: 120,
    resizable: true,
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    width: 120,
    resizable: true,
  },
  {
    title: '个人简介',
    dataIndex: 'content',
    width: 120,
    resizable: true,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'name',
    label: '姓名',
    component: 'Input',
    componentProps: {
      trim: true,
    },
    colProps: { span: 8 },
  },
  {
    field: 'birthday',
    label: '生日',
    component: 'RangePicker',
    componentProps: {
      valueType: 'Date'
    },
    colProps: { span: 8 },
  },
  {
    field: 'age',
    label: '年龄',
    component: 'Input',
    slot: 'age',
    colProps: { span: 8 },
  },
  {
    field: 'sex',
    label: '性别',
    colProps: { span: 8 },
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'sex',
      placeholder: '请选择性别',
    },
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
    field: 'createBy',
    label: 'createBy',
    component: 'Input',
    show: false,
  },
  {
    field: 'createTime',
    label: 'createTime',
    component: 'Input',
    show: false,
  },
  {
    field: 'name',
    label: '名字',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入名字',
    },
  },
  {
    field: 'keyWord',
    label: '关键词',
    component: 'Input',
    componentProps: {
      placeholder: '请输入关键词',
    },
  },
  {
    field: 'punchTime',
    label: '打卡时间',
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
      placeholder: '请选择打卡时间',
    },
  },
  {
    field: 'salaryMoney',
    label: '工资',
    component: 'Input',
    componentProps: {
      placeholder: '请输入工资',
    },
  },
  {
    field: 'sex',
    label: '性别',
    component: 'JDictSelectTag',
    defaultValue: '1',
    componentProps: {
      type: 'radio',
      dictCode: 'sex',
      placeholder: '请选择性别',
    },
  },
  {
    field: 'age',
    label: '年龄',
    component: 'InputNumber',
    defaultValue: 1,
    componentProps: {
      placeholder: '请输入年龄',
    },
  },
  {
    field: 'birthday',
    label: '生日',
    component: 'DatePicker',
    defaultValue: '',
    componentProps: {
      valueFormat: 'YYYY-MM-DD',
      placeholder: '请选择生日',
    },
  },
  {
    field: 'email',
    label: '邮箱',
    component: 'Input',
    rules: [{ required: false, type: 'email', message: '邮箱格式不正确', trigger: 'blur' }],
    componentProps: {
      placeholder: '请输入邮箱',
    },
  },
  {
    field: 'content',
    label: '个人简介 - To introduce myself',
    component: 'InputTextArea',
    labelLength: 4,
    componentProps: {
      placeholder: '请输入个人简介',
    },
  },
  {
    field: 'updateCount',
    label: '乐观锁',
    show: false,
    component: 'Input',
  },
];
