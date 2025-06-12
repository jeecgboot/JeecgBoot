import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '课程名称',
    align:"center",
    dataIndex: 'coursename'
   },
   {
    title: '课程代码',
    align:"center",
    dataIndex: 'coursecode'
   },
   {
    title: '任课老师',
    align:"center",
    dataIndex: 'instructor'
   },
   {
    title: '上课地点',
    align:"center",
    dataIndex: 'location'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '课程名称',
    field: 'coursename',
    component: 'Input',
  },
  {
    label: '课程代码',
    field: 'coursecode',
    component: 'Input',
  },
  {
    label: '任课老师',
    field: 'instructor',
    component: 'Input',
  },
  {
    label: '上课地点',
    field: 'location',
    component: 'Input',
  },
	// TODO 主键隐藏字段，目前写死为ID
	{
	  label: '',
	  field: 'id',
	  component: 'Input',
	  show: false
	},
];

// 高级查询数据
export const superQuerySchema = {
  coursename: {title: '课程名称',order: 0,view: 'text', type: 'string',},
  coursecode: {title: '课程代码',order: 1,view: 'text', type: 'string',},
  instructor: {title: '任课老师',order: 2,view: 'text', type: 'string',},
  location: {title: '上课地点',order: 3,view: 'text', type: 'string',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}