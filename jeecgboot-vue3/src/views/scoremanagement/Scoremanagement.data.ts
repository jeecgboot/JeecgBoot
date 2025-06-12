import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '学生',
    align:"center",
    dataIndex: 'astudent'
   },
   {
    title: '学号',
    align:"center",
    dataIndex: 'studentid'
   },
   {
    title: '课程名字',
    align:"center",
    dataIndex: 'coursename'
   },
   {
    title: '课程代码',
    align:"center",
    dataIndex: 'coursecode'
   },
   {
    title: '考试名字',
    align:"center",
    dataIndex: 'examname'
   },
   {
    title: '考试时间',
    align:"center",
    dataIndex: 'examtime'
   },
   {
    title: '学生成绩',
    align:"center",
    dataIndex: 'studentscore'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '学生',
    field: 'astudent',
    component: 'Input',
  },
  {
    label: '学号',
    field: 'studentid',
    component: 'Input',
  },
  {
    label: '课程名字',
    field: 'coursename',
    component: 'Input',
  },
  {
    label: '课程代码',
    field: 'coursecode',
    component: 'Input',
  },
  {
    label: '考试名字',
    field: 'examname',
    component: 'Input',
  },
  {
    label: '考试时间',
    field: 'examtime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: '学生成绩',
    field: 'studentscore',
    component: 'InputNumber',
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
  astudent: {title: '学生',order: 0,view: 'text', type: 'string',},
  studentid: {title: '学号',order: 1,view: 'text', type: 'string',},
  coursename: {title: '课程名字',order: 2,view: 'text', type: 'string',},
  coursecode: {title: '课程代码',order: 3,view: 'text', type: 'string',},
  examname: {title: '考试名字',order: 4,view: 'text', type: 'string',},
  examtime: {title: '考试时间',order: 5,view: 'datetime', type: 'string',},
  studentscore: {title: '学生成绩',order: 6,view: 'number', type: 'number',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}