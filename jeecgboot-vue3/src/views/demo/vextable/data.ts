import { BasicColumn, FormSchema } from '/@/components/Table';
import { usePermission } from '/@/hooks/web/usePermission';
import { JVxeColumn, JVxeTypes } from '/@/components/jeecg/JVxeTable/types';
const { isDisabledAuth, hasPermission, initBpmFormData} = usePermission();

export const columns: BasicColumn[] = [
  {
    title: '订单号',
    dataIndex: 'orderCode',
    width: 260,
  },
  {
    title: '订单类型',
    dataIndex: 'ctype',
    slots: { customRender: 'ctype' },
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
  {
    title: '流程状态',
    width: 200,
    dataIndex: 'bpmStatus',
    customRender: ({ text }) => {
      if (!text || text == '1') {
        return '待提交';
      } else if (text == '2') {
        return '处理中';
      } else if (text == '2') {
        return '已完成';
      } else {
        return text;
      }
    },
  },
];

export function getBpmFormSchema(formData) {
  //注入流程节点表单权限
  initBpmFormData(formData);
  
  const formSchema2: FormSchema[] = [
    {
      label: '订单号',
      field: 'orderCode',
      component: 'Input',
      show: ({ values }) => {
        return hasPermission('order:orderCode');
      },
    },
    {
      label: '订单类型',
      field: 'ctype',
      component: 'Select',
      componentProps: {
        options: [
          { label: '国内订单', value: '1', key: '1' },
          { label: '国际订单', value: '2', key: '2' },
        ],
      },
    },
    {
      label: '订单日期',
      field: 'orderDate',
      component: 'DatePicker',
      componentProps: {
        valueFormat: 'YYYY-MM-DD HH:mm:ss',
        style: {
          width: '100%',
        },
      },
    },
    {
      label: '订单金额',
      field: 'orderMoney',
      component: 'Input',
    },
    {
      label: '订单备注',
      field: 'content',
      component: 'Input',
    },
  ];
  return formSchema2;
}

export function getOrderCustomerFormSchema(formData) {
  //注入流程节点表单权限
  initBpmFormData(formData);
  
  const formSchema2: FormSchema[] = [
    {
      label: '客户名',
      field: 'name',
      component: 'Input',
      dynamicDisabled: ({ values }) => {
        return isDisabledAuth('order:name');
      },
    },
    {
      label: '性别',
      field: 'sex',
      component: 'Select',
      componentProps: {
        options: [
          { label: '男', value: '1', key: '1' },
          { label: '女', value: '2', key: '2' },
        ],
      },
    },
    {
      label: '身份证号',
      field: 'idcard',
      component: 'Input',
    },
    {
      label: '手机号',
      field: 'telphone',
      component: 'Input',
    },
  ];
  return formSchema2;
}

export const jeecgOrderTicketColumns: JVxeColumn[] = [
  {
    title: '航班号',
    key: 'ticketCode',
    width: 180,
    type: JVxeTypes.input,
    placeholder: '请输入${title}',
    defaultValue: '',
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
