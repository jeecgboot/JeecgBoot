import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '模版名称',
    align: 'center',
    dataIndex: 'name',
  },
  {
    title: '模版编码',
    align: 'center',
    dataIndex: 'code',
  },
  // {
  //   title: '页眉',
  //   align: 'center',
  //   dataIndex: 'header',
  //   ifShow: false,
  // },
  // {
  //   title: '页脚',
  //   align: 'center',
  //   dataIndex: 'footer',
  //   ifShow: false,
  // },
  // {
  //   title: '主体内容',
  //   align: 'center',
  //   dataIndex: 'main',
  //   ifShow: false,
  // },
  // {
  //   title: '页边距',
  //   align: 'center',
  //   dataIndex: 'margins',
  //   ifShow: false,
  // },
  // {
  //   title: '宽度',
  //   align: 'center',
  //   dataIndex: 'width',
  //   ifShow: false,
  // },
  // {
  //   title: '高度',
  //   align: 'center',
  //   dataIndex: 'height',
  //   ifShow: false,
  // },
  // {
  //   title: '纸张方向',
  //   align: 'center',
  //   dataIndex: 'paperDirection',
  //   ifShow: false,
  // },
  // {
  //   title: '水印',
  //   align: 'center',
  //   dataIndex: 'watermark',
  //   ifShow: false,
  // },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
  {
    label: '模版名称',
    field: 'name',
    component: 'Input',
  },
  {
    label: '模版编码',
    field: 'code',
    component: 'Input',
  },
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '模版名称',
    field: 'name',
    component: 'Input',
    dynamicRules: ({ model, schema }) => {
      return [{ required: true, message: '请输入模版名称!' }];
    },
  },
  {
    label: '模版编码',
    field: 'code',
    component: 'Input',
    dynamicRules: ({ model, schema }) => {
      return [
        { required: true, message: '请输入模版编码!' },
        { pattern: /^[A-Za-z_]+$/, message: '只能输入英文和下划线!' },
      ];
    },
  },
  {
    label: '页眉',
    field: 'header',
    component: 'Input',
    show: false,
  },
  {
    label: '页脚',
    field: 'footer',
    component: 'Input',
    show: false,
  },
  {
    label: '主体内容',
    field: 'main',
    component: 'Input',
    show: false,
  },
  {
    label: '页边距',
    field: 'margins',
    component: 'Input',
    show: false,
  },
  {
    label: '宽度',
    field: 'width',
    component: 'InputNumber',
    show: false,
  },
  {
    label: '高度',
    field: 'height',
    component: 'InputNumber',
    show: false,
  },
  {
    label: '纸张方向',
    field: 'paperDirection',
    component: 'Input',
    show: false,
  },
  {
    label: '水印',
    field: 'watermark',
    component: 'Input',
    show: false,
  },
  // TODO 主键隐藏字段，目前写死为ID
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
];

// 高级查询数据
export const superQuerySchema = {
  name: { title: '模版名称', order: 0, view: 'text', type: 'string' },
  code: { title: '模版编码', order: 1, view: 'text', type: 'string' },
  header: { title: '页眉', order: 2, view: 'text', type: 'string' },
  footer: { title: '页脚', order: 3, view: 'text', type: 'string' },
  main: { title: '主体内容', order: 4, view: 'text', type: 'string' },
  margins: { title: '页边距', order: 5, view: 'text', type: 'string' },
  width: { title: '宽度', order: 6, view: 'number', type: 'number' },
  height: { title: '高度', order: 7, view: 'number', type: 'number' },
  paperDirection: { title: '纸张方向', order: 8, view: 'text', type: 'string' },
  watermark: { title: '水印', order: 9, view: 'text', type: 'string' },
};

/**
 * 流程表单调用这个方法获取formSchema
 * @param param
 */
export function getBpmFormSchema(_formData): FormSchema[] {
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}
