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
    title: '访问模式',
    align:"center",
    dataIndex: 'listMode',
    customRender: ({ text }) => {
      return text === 'WHITELIST' ? '白名单' : '黑名单';
    }
   },
   {
    title: '清单项数量',
    align:"center",
    dataIndex: 'allowedList',
    customRender: ({ text }) => {
      if (!text) return 0;
      return text.split(/[,\n]/).filter(item => item.trim()).length;
    }
   },
   {
    title: '最后修改人',
    align:"center",
    dataIndex: 'updateBy'
   },
   {
    title: '最后修改时间',
    align:"center",
    dataIndex: 'updateTime'
   },
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
    label: "访问模式",
    field: "listMode",
    component: 'Select',
    componentProps: {
      options: [
        { label: '白名单', value: 'WHITELIST' },
        { label: '黑名单', value: 'BLACKLIST' },
      ],
    },
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
    label: '访问模式',
    field: 'listMode',
    component: 'RadioGroup',
    defaultValue: 'WHITELIST',
    componentProps: {
      options: [
        { label: '白名单', value: 'WHITELIST' },
        { label: '黑名单', value: 'BLACKLIST' },
      ],
    },
  },
  {
    label: '访问清单',
    field: 'allowedList',
    component: 'InputTextArea',
    slot: 'allowedListSlot',
    componentProps: {
      rows: 6,
      placeholder: '支持IP、CIDR、域名；支持10.2.3.*与10.2.3.[1-234]，每行一个或逗号分隔',
    },
    dynamicRules: ({ model, schema }) => {
      return [
        {
          validator: (rule, value) => {
            if (!value) return Promise.resolve();
            const items = value.split(/[,\n]/).filter((item) => item.trim());
            const ipv4Seg = '(?:25[0-5]|2[0-4][0-9]|[01]?\\d\\d?)';
            const ipRegex = new RegExp(`^(?:${ipv4Seg}\\.){3}${ipv4Seg}$`);
            const cidrRegex = new RegExp(`^(?:${ipv4Seg}\\.){3}${ipv4Seg}\\/(?:[0-9]|[1-2][0-9]|3[0-2])$`);
            const domainRegex = /^([a-zA-Z0-9]+(-[a-zA-Z0-9]+)*\.)+[a-zA-Z]{2,}$/;
            // 10.2.3.* 支持最后一段通配符
            const wildcardLastOctetRegex = new RegExp(`^(?:${ipv4Seg}\\.){3}\\*$`);
            // 10.2.3.[1-234] 支持最后一段范围
            const rangeLastOctetRegex = new RegExp(`^(?:${ipv4Seg}\\.){3}\\[(\\d{1,3})-(\\d{1,3})\\]$`);

            for (const raw of items) {
              const item = raw.trim();
              // 基础合法：IP / CIDR / 域名
              if (ipRegex.test(item) || cidrRegex.test(item) || domainRegex.test(item)) {
                continue;
              }
              // 最后一段通配符：10.2.3.*
              if (wildcardLastOctetRegex.test(item)) {
                continue;
              }
              // 最后一段范围：10.2.3.[1-234]
              const m = item.match(rangeLastOctetRegex);
              if (m) {
                const start = Number(m[1]);
                const end = Number(m[2]);
                if (Number.isInteger(start) && Number.isInteger(end) && start >= 0 && end >= start && end <= 255) {
                  continue;
                }
              }
              return Promise.reject(new Error(`"${item}" 不是有效的IP/CIDR/域名，或不支持的模式（仅支持10.2.3.*与10.2.3.[start-end]）`));
            }
            return Promise.resolve();
          },
          message: '请输入有效的IP、CIDR、域名或通配/范围模式',
        },
      ];
    },
  },
  {
    label: '备注',
    field: 'comment',
    component: 'InputTextArea',
    componentProps: {
      rows: 3,
    },
  },
  {
    label: '高级设置',
    field: 'advancedSettings',
    component: 'Divider',
  },
  {
    label: '严格模式',
    field: 'enableStrict',
    component: 'Switch',
    defaultValue: false,
  },
  {
    label: 'DNS缓存TTL(秒)',
    field: 'dnsCacheTtlSeconds',
    component: 'InputNumber',
    defaultValue: 300,
    componentProps: {
      min: 0,
      max: 86400,
    },
  },
  {
    label: 'IP版本',
    field: 'ipVersion',
    component: 'Select',
    defaultValue: 'Dual',
    componentProps: {
      options: [
        { label: 'IPv4', value: 'IPv4' },
        { label: 'IPv6', value: 'IPv6' },
        { label: '双栈', value: 'Dual' },
      ],
    },
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
