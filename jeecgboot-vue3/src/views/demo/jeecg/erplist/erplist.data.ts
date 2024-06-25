import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { render } from '/@/utils/common/renderUtils';

export const columns: BasicColumn[] = [
  {
    title: '订单号',
    dataIndex: 'orderCode',
    width: 260,
  },
  {
    title: '订单类型',
    dataIndex: 'ctype',
    width: 160,
    customRender: ({ text }) => {
      return text == '1' ? '国内订单' : text == '2' ? '国际订单' : '';
    },
  },
  {
    title: '订单日期',
    dataIndex: 'orderDate',
    width: 300,
  },
  {
    title: '订单金额',
    width: 200,
    dataIndex: 'orderMoney',
  },
  {
    title: '订单备注',
    width: 200,
    dataIndex: 'content',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    label: '订单号',
    field: 'orderCode',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '订单类型',
    field: 'ctype',
    component: 'Select',
    componentProps: {
      options: [
        {
          label: '国内订单',
          value: '1',
          key: '1',
        },
        {
          label: '国际订单',
          value: '2',
          key: '2',
        },
      ],
    },
    colProps: { span: 6 },
  },
];

export const formSchema: FormSchema[] = [
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    label: '订单号',
    field: 'orderCode',
    component: 'Input',
    required: true,
  },
  {
    label: '订单类型',
    field: 'ctype',
    component: 'Select',
    componentProps: {
      options: [
        {
          label: '国内订单',
          value: '1',
          key: '1',
        },
        {
          label: '国际订单',
          value: '2',
          key: '2',
        },
      ],
    },
  },
  {
    label: '订单日期',
    field: 'orderDate',
    component: 'DatePicker',
    componentProps: {
      valueFormat: 'YYYY-MM-DD hh:mm:ss',
    },
  },
  {
    label: '订单金额',
    field: 'orderMoney',
    component: 'InputNumber',
  },
  {
    label: '订单备注',
    field: 'content',
    component: 'Input',
  },
];

export const customColumns: BasicColumn[] = [
  {
    title: '客户名',
    dataIndex: 'name',
    width: 260,
  },
  {
    title: '性别',
    dataIndex: 'sex',
    width: 100,
    customRender: ({ text }) => {
      return render.renderDict(text, 'sex');
    },
  },
  {
    title: '身份证号',
    dataIndex: 'idcard',
    width: 300,
  },
  {
    title: '电话',
    width: 200,
    dataIndex: 'telphone',
  },
];

export const customerFormSchema: FormSchema[] = [
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    label: '客户姓名',
    field: 'name',
    component: 'Input',
    required: true,
  },
  {
    label: '性别',
    field: 'sex',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'sex',
      placeholder: '请选择性别',
    },
  },
  {
    label: '身份证号码',
    field: 'idcard',
    component: 'Input',
  },
  {
    label: '身份证扫描件',
    field: 'idcardPic',
    component: 'JImageUpload',
    componentProps: {
      fileMax: 2,
    },
  },
  {
    label: '联系方式',
    field: 'telphone',
    component: 'Input',
    rules: [{ required: false, pattern: /^1[3456789]\d{9}$/, message: '手机号码格式有误' }],
  },
  {
    label: 'orderId',
    field: 'orderId',
    component: 'Input',
    show: false,
  },
];

export const ticketColumns: BasicColumn[] = [
  {
    title: '航班号',
    dataIndex: 'ticketCode',
  },
  {
    title: '航班时间',
    dataIndex: 'tickectDate',
  },
  {
    title: '创建人',
    dataIndex: 'createBy',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
];

export const ticketFormSchema: FormSchema[] = [
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    label: '航班号',
    field: 'ticketCode',
    component: 'Input',
    required: true,
  },
  {
    label: '航班时间',
    field: 'tickectDate',
    component: 'DatePicker',
    componentProps: {
      valueFormat: 'YYYY-MM-DD',
    },
  },
  {
    label: 'orderId',
    field: 'orderId',
    component: 'Input',
    show: false,
  },
];
