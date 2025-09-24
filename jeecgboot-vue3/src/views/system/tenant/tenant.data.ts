import { BasicColumn, FormSchema } from '/@/components/Table';
import { getAutoScrollContainer } from '/@/utils/common/compUtils';
import { render } from "/@/utils/common/renderUtils";
import { rules } from "/@/utils/helper/validator";

export const columns: BasicColumn[] = [
  {
    title: '租户名称',
    dataIndex: 'name',
    width: 200,
    align: 'left',
  },
  {
    title: '租户编号(ID)',
    dataIndex: 'id',
    width: 180,
  },{
    title: '组织LOGO',
    dataIndex: 'companyLogo',
    width: 100,
    customRender: ({ text }) => {
      if(!text){
        return text;
      }
      return render.renderImage({text});
    },
  },
  {
    dataIndex: 'trade_dictText',
    title: '所属行业',
    width: 150
  },
  {
    dataIndex: 'companySize_dictText',
    title: '公司规模',
    width: 100
  },
  {
    dataIndex: 'houseNumber',
    title: '门牌号',
    width: 100,
  },
  // {
  //   dataIndex: 'position_dictText',
  //   title: '职级',
  //   width: 150
  // },
  // {
  //   dataIndex: 'department_dictText',
  //   title: '部门',
  //   width: 150
  // },
  {
    dataIndex: 'createBy_dictText',
    title: '创建者(拥有)',
    width: 150
  },
/*  {
    title: '开始时间',
    dataIndex: 'beginDate',
    sorter: true,
    width: 180,
  },
  {
    title: '结束时间',
    dataIndex: 'endDate',
    sorter: true,
    width: 180,
  },*/
  {
    title: '状态',
    dataIndex: 'status_dictText',
    width: 100,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'name',
    label: '租户名称',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'status',
    label: '状态',
    component: 'Select',
    componentProps: {
      options: [
        { label: '正常', value: 1 },
        { label: '冻结', value: 0 },
      ],
    },
    colProps: { span: 8 },
  },
  // {
  //   field: 'fieldTime',
  //   component: 'RangePicker',
  //   label: '时间字段',
  //   componentProps: {
  //     valueType: 'Date',
  //   },
  //   colProps: {
  //     span: 8,
  //   },
  // },
];

export const formSchema: FormSchema[] = [
  {
    field: 'name',
    label: '租户名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'id',
    label: '租户编号(ID)',
    component: 'InputNumber',
    required: true,
    ifShow: ({ values }) => {
      return values.id!=null;
    },
  },
  {
    field: 'companyLogo',
    label: '组织LOGO',
    component: 'JImageUpload',
    componentProps:{
      text:'logo'
    }
  },
  {
    field: 'trade',
    label: '所属行业',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode:'trade',
    }
  }, {
    field: 'companySize',
    label: '公司规模',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode:'company_size',
    }
  }, {
    field: 'companyAddress',
    label: '公司地址',
    component: 'JAreaSelect',
    componentProps: {
      placeholder: '请输入公司地址',
      rows: 4,
    }
  },
  {
    field: 'workPlace',
    label: '工作地点',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入工作地点',
      rows: 4,
    }
  },
/*  {
    field: 'beginDate',
    label: '开始时间',
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
      getPopupContainer: getAutoScrollContainer,
    },
  },
  {
    field: 'endDate',
    label: '结束时间',
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
      getPopupContainer: getAutoScrollContainer,
    },
  },*/
  {
    field: 'houseNumber',
    label: '门牌号',
    component: 'Input',
    dynamicDisabled: true,
    ifShow: ({ values }) => {
      return values.id!=null;
    },
  },
  {
    field: 'position',
    label: '职级',
    component: 'JDictSelectTag',
    componentProps:{
      dictCode: 'company_rank'
    }
  },
  {
    field: 'department',
    label: '部门',
    component: 'JDictSelectTag',
    componentProps:{
      dictCode:'company_department'
    }
  },
  {
    field: 'status',
    label: '状态',
    component: 'RadioButtonGroup',
    defaultValue: 1,
    componentProps: {
      options: [
        { label: '正常', value: 1 },
        { label: '冻结', value: 0 },
      ],
    },
  },
];

//定义用户表格列
export const userColumns: BasicColumn[] =[
  {
    title: '用户账号',
    dataIndex: 'username',
    width: 100,
    align: 'left',
  },
  {
    title: '用户姓名',
    dataIndex: 'realname',
    width: 100,
  },
  {
    title: '性别',
    dataIndex: 'sex_dictText',
    width: 100,
  },
  {
    title: '手机号码',
    dataIndex: 'phone',
    width: 100,
  },
];

//邀请用户搜索表单
export const userSearchFormSchema: FormSchema[] = [
  {
    field: 'username',
    label: '账号',
    component: 'Input',
  },
  {
    field: 'realname',
    label: '姓名',
    component: 'Input',
  },
];

//套餐包列表
export const packColumns: BasicColumn[] = [
  {
    title: '套餐包名称',
    dataIndex: 'packName',
    width: 100,
    customRender: ( { record, text }) => {
      if(record.packCode && record.packCode.indexOf('default') != -1) {
        return text + '(默认产品包)';
      } else {
        return text;
      }
    }
  },
  {
    title: '是否自动分配用户',
    dataIndex: 'izSysn',
    width: 100,
    customRender: ( { text }) => {
      if(text === '1') {
        return '是';
      } else {
        return '否';
      }
    }
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
    customRender: ({ text }) => {
      if (text === '1') {
        return '开启';
      } else {
        return '关闭';
      }
    },
  },
  {
    title: '备注说明',
    dataIndex: 'remarks',
    width: 150,
  },
];

//套餐包列表
export const tenantPackColumns: BasicColumn[] = [
  {
    title: '套餐包名称',
    dataIndex: 'packName',
    width: 100,
    customRender: ( { record, text }) => {
      if(record.packCode && record.packCode.indexOf('default') != -1) {
        return text + '(默认产品包)';
      } else {
        return text;
      }
    }
  },
  {
    title: '是否自动分配用户',
    dataIndex: 'izSysn',
    width: 100,
    customRender: ( { text }) => {
      if(text === '1') {
        return '是';
      } else {
        return '否';
      }
    }
  },
  {
    title: '备注说明',
    dataIndex: 'remarks',
    width: 150,
  },
];

//套餐包列表
export const defalutPackColumns: BasicColumn[] = [
  {
    title: '默认套餐名称',
    dataIndex: 'packName',
    width: 100,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
    customRender: ({ text }) => {
      if (text === '1') {
        return '开启';
      } else {
        return '关闭';
      }
    },
  },
  {
    title: '备注说明',
    dataIndex: 'remarks',
    width: 150,
  },
];

//套餐包搜索表单
export const packFormSchema: FormSchema[] = [
  {
    field: 'packName',
    label: '套餐包名',
    component: 'JInput',
    colProps: { xxl: 8 },
  },
];

//套餐包搜索表单
export const defaultPackFormSchema: FormSchema[] = [
  {
    field: 'packName',
    label: '默认套餐名',
    component: 'JInput',
    colProps: { xxl: 8 },
  },
];

//套餐包表单
export const packMenuFormSchema: FormSchema[] = [
  {
    field: 'packName',
    label: '套餐包名称',
    component: 'Input',
  },
  {
    field: 'permissionIds',
    label: '授权菜单',
    component: 'JTreeSelect',
    componentProps: {
      dict: 'sys_permission,name,id',
      pidField: 'parent_id',
      hasChildField:'is_leaf',
      multiple: true,
      treeCheckAble:true,
      treeCheckStrictly: true,
      converIsLeafVal: 0,
      getPopupContainer: () => document.body,
    },
  },
  {
    field: 'remarks',
    label: '备注说明',
    component: 'InputTextArea',
  },
  {
    field: 'izSysn',
    label: '自动分配用户',
    component: 'Switch',
    componentProps: {
      checkedValue: "1",
      checkedChildren: '是',
      unCheckedValue: "0",
      unCheckedChildren: '否',
    },
    defaultValue: "1",
    helpMessage: "默认会自动分配给用户，个性高级套餐，需要租户管理员手工分配人员(拥有更灵活性权限控制)"
  },  
  {
    field: 'status',
    label: '开启状态',
    component: 'Switch',
    componentProps: {
      checkedValue: '1',
      checkedChildren: '开启',
      unCheckedValue: '0',
      unCheckedChildren: '关闭',
    },
    defaultValue: '1',
  },
  {
    field: 'id',
    label: '开启状态',
    component: 'Input',
    show: false
  },
  {
    field: 'packCode',
    label: '产品包编码',
    component: 'Input',
    show: false
  },  
  {
    field: 'packType',
    label: '产品包类型',
    component: 'Input',
    show: false
  },
];

//回收站列表
export const recycleColumns : BasicColumn[] = [
  {
    title: '租户名称',
    dataIndex: 'name',
    width: 100,
    align: 'left',
  },
  {
    title: '租户编号(ID)',
    dataIndex: 'id',
    width: 100,
  },
  {
    title: '组织LOGO',
    dataIndex: 'companyLogo',
    width: 100,
    customRender: ({ text }) => {
      if(!text){
        return text;
      }
      return render.renderImage({text});
    },
  },
  {
    dataIndex: 'houseNumber',
    title: '门牌号',
    width: 100,
  }
]

//租户回收站搜索表单
export const searchRecycleFormSchema : FormSchema[] = [
  {
    field: 'name',
    label: '租户名称',
    component: 'Input',
  },
  {
    field: 'houseNumber',
    label: '门牌号',
    component: 'Input',
  },
]

//套餐包用户列表
export const tenantPackUserColumns: BasicColumn[] = [
  {
    title: '用户',
    dataIndex: 'realname',
    width: 200,
  },
  {
    title: '部门',
    dataIndex: 'departNames',
    width: 200,
    ellipsis: true,
    slots: { customRender: 'departNames' }
  },
  {
    title: '职位',
    dataIndex: 'positionNames',
    ellipsis: true,
    width: 200,
    slots: { customRender: 'positionNames' }
  }
]

/**
 * 用户租户新增编辑表单
 */
export const tenantUserSchema: FormSchema[] = [
  { field: 'id', label: 'id', component: 'Input', show: false },
  { field: 'username', label: 'username', component: 'Input', show: false },
  {
    field: 'realname',
    label: '姓名',
    component: 'Input',
    dynamicDisabled: ({ values }) => {
      return !!values.id;
    },
  },
  {
    field: 'phone',
    label: '手机',
    component: 'Input',
    dynamicRules: ({ model, schema }) => {
      if (model.id) {
        return [];
      }
      return [{ ...rules.phone(true)[0] }, { ...rules.duplicateCheckRule('sys_user', 'phone', model, schema, false)[0] }];
    },
    dynamicDisabled: ({ values }) => {
      return !!values.id;
    },
  },
  {
    field: 'email',
    label: '邮箱',
    component: 'Input',
    dynamicRules: ({ model, schema }) => {
      if (model.id) {
        return [];
      }
      return [{ ...rules.email(true)[0] }, { ...rules.duplicateCheckRule('sys_user', 'email', model, schema, false)[0] }];
    },
    dynamicDisabled: ({ values }) => {
      return !!values.id;
    },
  },
  { field: 'selecteddeparts', label: '部门', component: 'JSelectDept', componentProps: { checkStrictly: true } },
 /* {
    field: 'post',
    label: '职位',
    component: 'JSelectPosition',
  },
  {
    field: 'workNo',
    label: '工号',
    component: 'Input',
    dynamicRules: ({ model, schema }) => {
      return [{ required: false, message: '请输入工号' }, { ...rules.duplicateCheckRule('sys_user', 'work_no', model, schema, false)[0] }];
    },
  },*/
  { field: 'relTenantIds', label: '租户', component: 'Input',show:false },
  { field: 'selectedroles', label: '角色', component: 'Input',show:false },
];

// 分配用户套餐
export const packUserAllotSchemas: FormSchema[] = [
  { 
    field: 'userId', 
    label: '用户id', 
    component: 'Input',
    show: false
  },
  {
    field: 'realname',
    label: '用户姓名',
    component: 'Input',
    componentProps:{
      readonly : true
    },
  },
  {
    field: 'packId',
    label: '套餐',
    component: 'Select',
    slot: 'packId'
  }
];