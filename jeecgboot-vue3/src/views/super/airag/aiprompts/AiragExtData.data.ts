import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '名称',
    align: 'center',
    dataIndex: 'name',
  },
  {
    title: '描述信息',
    align: 'center',
    dataIndex: 'descr',
  },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
  {
    label: '名称',
    field: 'name',
    component: 'Input',
    colProps: {
      span: 6,
    },
  },
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '业务类型标识',
    field: 'bizType',
    component: 'Input',
    defaultValue: 'evaluator',
    show: false,
  },
  {
    label: '名称',
    field: 'name',
    component: 'Input',
  },
  {
    label: '描述信息',
    field: 'descr',
    component: 'InputTextArea',
  },
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
];

/**
 * 流程表单调用这个方法获取formSchema
 * @param param
 */
export function getBpmFormSchema(_formData): FormSchema[] {
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}
