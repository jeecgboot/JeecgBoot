import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import {JVxeTypes,JVxeColumn} from '/@/components/jeecg/JVxeTable/types'
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '接口名称',
    align:"center",
    dataIndex: 'name'
   },
   {
    title: '请求方法',
    align:"center",
    dataIndex: 'requestMethod'
   },
   {
    title: '接口地址',
    align:"center",
    dataIndex: 'requestUrl'
   },
   {
    title: 'IP 黑名单',
    align:"center",
    dataIndex: 'blackList'
   },
   // {
   //  title: '状态',
   //  align:"center",
   //  dataIndex: 'status'
   // },
   {
    title: '创建人',
    align:"center",
    dataIndex: 'createBy'
   },
   {
    title: '创建时间',
    align:"center",
    dataIndex: 'createTime'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
  {
    label: "接口名称",
    field: "name",
    component: 'JInput',
  },
  {
    label: "创建人",
    field: "createBy",
    component: 'JInput',
  },
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '接口名称',
    field: 'name',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入接口名称!'},
          ];
     },
  },
  {
    label: '原始地址',
    field: 'originUrl',
    component: 'Input',
  },
  {
    label: '请求方法',
    field: 'requestMethod',
    component: 'JSearchSelect',
    componentProps:{
      dictOptions: [
        {
          text: 'POST',
          value: 'POST',
        },
        {
          text: 'GET',
          value: 'GET',
        },
        {
          text: 'HEAD',
          value: 'HEAD',
        },
        {
          text: 'PUT',
          value: 'PUT',
        },
        {
          text: 'PATCH',
          value: 'PATCH',
        },
        {
          text: 'DELETE',
          value: 'DELETE',
        },{
          text: 'OPTIONS',
          value: 'OPTIONS',
        },{
          text: 'TRACE',
          value: 'TRACE',
        },
      ]
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入请求方法!'},
          ];
     },
  },
  {
    label: '接口地址',
    field: 'requestUrl',
    component: 'Input',
    dynamicDisabled:true
  },
  {
    label: 'IP 黑名单',
    field: 'blackList',
    component: 'Input',
  },
  {
    label: '请求体内容',
    component:"Input",
    field: 'body'
  },
  {
    label: '删除标识',
    field: 'delFlag',
    component: 'Input',
    defaultValue:0,
    show:false
  },
  {
    label: '状态',
    field: 'status',
    component: 'Input',
    defaultValue:"1",
    show:false
  },
	// TODO 主键隐藏字段，目前写死为ID
	{
	  label: '',
	  field: 'id',
	  component: 'Input',
	  show: false
	},
];
//子表单数据
//子表列表数据
export const openApiHeaderColumns: BasicColumn[] = [
   // {
   //  title: 'apiId',
   //  align:"center",
   //  dataIndex: 'apiId'
   // },
   {
    title: '请求头Key',
    align:"center",
    dataIndex: 'headerKey'
   },
   {
    title: '是否必填',
    align:"center",
    dataIndex: 'required_dictText'
   },
   {
    title: '默认值',
    align:"center",
    dataIndex: 'defaultValue'
   },
   {
    title: '备注',
    align:"center",
    dataIndex: 'note'
   },
];
//子表列表数据
export const openApiParamColumns: BasicColumn[] = [
   // {
   //  title: 'apiId',
   //  align:"center",
   //  dataIndex: 'apiId'
   // },
   {
    title: '参数Key',
    align:"center",
    dataIndex: 'paramKey'
   },
   {
    title: '是否必填',
    align:"center",
    dataIndex: 'required_dictText'
   },
   {
    title: '默认值',
    align:"center",
    dataIndex: 'defaultValue'
   },
   {
    title: '备注',
    align:"center",
    dataIndex: 'note'
   },
];
//子表表格配置
export const openApiHeaderJVxeColumns: JVxeColumn[] = [
    // {
    //   title: 'apiId',
    //   key: 'apiId',
    //   type: JVxeTypes.input,
    //   width:"200px",
    //   placeholder: '请输入${title}',
    //   defaultValue:'',
    // },
    {
      title: '请求头Key',
      key: 'headerKey',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '是否必填',
      key: 'required',
      type: JVxeTypes.checkbox,
      options:[],
      // dictCode:"yn",
      width:"100px",
      placeholder: '请输入${title}',
      defaultValue:'',
      customValue: ['1','0']
    },
    {
      title: '默认值',
      key: 'defaultValue',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '备注',
      key: 'note',
      type: JVxeTypes.input,
      placeholder: '请输入${title}',
      defaultValue:'',
    },
  ]
export const openApiParamJVxeColumns: JVxeColumn[] = [
    // {
    //   title: 'apiId',
    //   key: 'apiId',
    //   type: JVxeTypes.input,
    //   width:"200px",
    //   placeholder: '请输入${title}',
    //   defaultValue:'',
    // },
    {
      title: '参数Key',
      key: 'paramKey',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '是否必填',
      key: 'required',
      type: JVxeTypes.checkbox,
      options:[],
      // dictCode:"yn",
      width:"100px",
      placeholder: '请输入${title}',
      defaultValue:'',
      customValue: ['1','0']
    },
    {
      title: '默认值',
      key: 'defaultValue',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '备注',
      key: 'note',
      type: JVxeTypes.input,
      placeholder: '请输入${title}',
      defaultValue:'',
    },
  ]

// 高级查询数据
export const superQuerySchema = {
  name: {title: '接口名称',order: 0,view: 'text', type: 'string',},
  requestMethod: {title: '请求方法',order: 1,view: 'list', type: 'string',dictCode: '',},
  requestUrl: {title: '接口地址',order: 2,view: 'text', type: 'string',},
  blackList: {title: 'IP 黑名单',order: 3,view: 'text', type: 'string',},
  status: {title: '状态',order: 5,view: 'number', type: 'number',},
  createBy: {title: '创建人',order: 6,view: 'text', type: 'string',},
  createTime: {title: '创建时间',order: 7,view: 'datetime', type: 'string',},
  //子表高级查询
  openApiHeader: {
    title: '请求头表',
    view: 'table',
    fields: {
        // apiId: {title: 'apiId',order: 0,view: 'text', type: 'string',},
        headerKey: {title: '请求头Key',order: 1,view: 'text', type: 'string',},
        required: {title: '是否必填',order: 2,view: 'number', type: 'number',dictCode: 'yn',},
        defaultValue: {title: '默认值',order: 3,view: 'text', type: 'string',},
        note: {title: '备注',order: 4,view: 'text', type: 'string',},
    }
  },
  openApiParam: {
    title: '请求参数部分',
    view: 'table',
    fields: {
        // apiId: {title: 'apiId',order: 0,view: 'text', type: 'string',},
        paramKey: {title: '参数Key',order: 1,view: 'text', type: 'string',},
        required: {title: '是否必填',order: 2,view: 'number', type: 'number',dictCode: 'yn',},
        defaultValue: {title: '默认值',order: 3,view: 'text', type: 'string',},
        note: {title: '备注',order: 4,view: 'text', type: 'string',},
    }
  },
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}
