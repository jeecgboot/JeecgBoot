//第三方app配置表单
import { FormSchema } from '/@/components/Form';

//第三方app表单
export const thirdAppFormSchema: FormSchema[] = [
  {
    label: 'id',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    label: 'thirdType',
    field: 'thirdType',
    component: 'Input',
    show: false,
  },
  {
    label: 'CorpId',
    field: 'corpId',
    component: 'Input',
    ifShow: ({ values }) => {
      return values.thirdType === 'dingtalk';
    },
    required: true,
  },
  {
    label: 'Agentld',
    field: 'agentId',
    component: 'Input',
    required: true,
  },
  {
    label: 'AppKey',
    field: 'clientId',
    component: 'Input',
    required: true,
  },
  {
    label: 'AppSecret',
    field: 'clientSecret',
    component: 'Input',
    required: true,
  },{
    label: '启用',
    field: 'status',
    component: 'Switch',
    componentProps:{
      checkedChildren:'关闭',
      checkedValue:1,
      unCheckedChildren:'开启',
      unCheckedValue: 0
    },
    defaultValue: 1
  },{
    label: '租户id',
    field: 'tenantId',
    component: 'Input',
    show: false,
  },
];
