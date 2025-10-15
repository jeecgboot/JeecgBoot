import { BasicColumn, FormSchema } from '/@/components/Table';
import { rules } from '/@/utils/helper/validator';

export const columns: BasicColumn[] = [
  // {
  //   title: '职务编码',
  //   dataIndex: 'code',
  //   width: 200,
  //   align: 'left',
  // },
  {
    title: '职务级别名称',
    dataIndex: 'name',
    align: 'left'
    // width: 200,
  },
  {
    title: '职务级别(越小级别越高)',
    dataIndex: 'postLevel',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'name',
    label: '职务级别名称',
    component: 'Input',
    colProps: { span: 8 },
  },
];

export const formSchema: FormSchema[] = [
  {
    label: '主键',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    field: 'name',
    label: '职务级别名称',
    component: 'Input',
    required: true,
  },
  {
    label: '职务级别',
    field: 'postLevel',
    component: 'InputNumber',
    required: true,
    componentProps: {
      min: 1,
      max: 99
    },
    dynamicRules: ({ model, schema }) => {
      return [{ required: true, message: '请输入职务级别!' }];
    },
  },
  // {
  //   field: 'code',
  //   label: '职务编码',
  //   component: 'Input',
  //   required: true,
  //   dynamicDisabled: ({ values }) => {
  //     return !!values.id;
  //   },
  //   dynamicRules: ({ model, schema }) => {
  //     return rules.duplicateCheckRule('sys_position', 'code', model, schema, true);
  //   },
  // },
];
