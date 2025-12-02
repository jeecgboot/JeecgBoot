import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '分类名称',
    dataIndex: 'name',
    width: 350,
    align: 'left',
  },
  {
    title: '分类编码',
    dataIndex: 'code',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    label: '名称',
    field: 'name',
    component: 'JInput',
    colProps: { span: 6 },
  },
  {
    label: '编码',
    field: 'code',
    component: 'JInput',
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
    label: '父级节点',
    field: 'pid',
    component: 'TreeSelect',
    componentProps: {
      // 代码逻辑说明: replaceFields已过期，使用fieldNames代替------------
      fieldNames: {
        value: 'key',
      },
      dropdownStyle: {
        maxHeight: '50vh',
      },
      getPopupContainer: () => document.body,
    },
    show: ({ values }) => {
      return values.pid !== '0';
    },
    dynamicDisabled: ({ values }) => {
      return !!values.id;
    },
  },
  {
    label: '分类名称',
    field: 'name',
    required: true,
    component: 'Input',
  },
];
