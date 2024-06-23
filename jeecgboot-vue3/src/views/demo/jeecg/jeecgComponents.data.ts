import { FormSchema, JCronValidator } from '/@/components/Form';
import { usePermission } from '/@/hooks/web/usePermission';

const { isDisabledAuth } = usePermission();
export const schemas: FormSchema[] = [
  {
    field: 'jdst',
    component: 'JDictSelectTag',
    label: '性别下拉',
    helpMessage: ['component模式'],
    componentProps: {
      dictCode: 'sex',
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'jdst',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },
  {
    field: 'jdst1',
    component: 'JDictSelectTag',
    label: '性别选择',
    helpMessage: ['component模式'],
    componentProps: {
      dictCode: 'sex',
      type: 'radioButton',
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'jdst1',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },
  {
    field: 'jdst2',
    component: 'JDictSelectTag',
    label: '字典表下拉',
    helpMessage: ['component模式'],
    componentProps: {
      dictCode: 'sys_user,realname,id',
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'jdst2',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },
  {
    field: 'jdst3',
    component: 'JDictSelectTag',
    label: '字典表下拉(带条件)',
    helpMessage: ['component模式'],
    componentProps: {
      dictCode: "sys_user,realname,id,username!='admin' order by create_time",
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'jdst3',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },
  {
    field: 'jsst',
    component: 'JSearchSelect',
    label: '字典搜索(同步)',
    colProps: { span: 12 },
    componentProps: {
      //dict: "sys_depart,depart_name,id",
      dictOptions: [
        {
          text: '选项一',
          value: '1',
        },
        {
          text: '选项二',
          value: '2',
        },
        {
          text: '选项三',
          value: '3',
        },
      ],
    },
  },
  {
    field: 'jsst',
    component: 'JEllipsis',
    label: '选择值',
    colProps: { span: 12 },
  },
  {
    field: 'jsst2',
    component: 'JSearchSelect',
    label: '字典搜索(异步)',
    colProps: { span: 12 },
    componentProps: {
      dict: 'sys_depart,depart_name,id',
      pageSize: 6,
      async: true,
    },
  },
  {
    field: 'jsst2',
    component: 'JEllipsis',
    label: '选择值',
    colProps: { span: 12 },
  },
  {
    field: 'xldx',
    component: 'JDictSelectTag',
    label: '字典下拉多选',
    colProps: { span: 12 },
    componentProps: {
      dictCode: 'sex',
      mode: 'multiple',
    },
  },
  {
    field: 'xldx',
    component: 'JEllipsis',
    label: '选择值',
    colProps: { span: 12 },
  },
  {
    field: 'xldx2',
    component: 'JSelectMultiple',
    label: '字典下拉多选2',
    colProps: { span: 12 },
    componentProps: {
      dictCode: 'sex',
    },
  },
  {
    field: 'xldx2',
    component: 'JEllipsis',
    label: '选择值',
    colProps: { span: 12 },
  },
  {
    field: 'dxxlk',
    component: 'JDictSelectTag',
    label: '字典下拉单选',
    colProps: { span: 12 },
    componentProps: {
      dictCode: 'sex',
    },
  },
  {
    field: 'dxxlk',
    component: 'JEllipsis',
    label: '选择值',
    colProps: { span: 12 },
  },
  {
    label: '可输入下拉',
    field: 'selectInput',
    component: 'JSelectInput',
    componentProps: {
      options: [
        { label: '选项一', value: '1' },
        { label: '选项二', value: '2' },
        { label: '选项三', value: '3' },
      ],
    },
    colProps: { span: 12 },
  },
  {
    field: 'selectInput',
    component: 'JEllipsis',
    label: '选择值',
    colProps: { span: 12 },
  },
  {
    field: 'depart3',
    component: 'JSelectDept',
    label: '选择部门—自定义值',
    helpMessage: ['component模式'],
    componentProps: { showButton: false, rowKey: 'orgCode', primaryKey: 'orgCode' },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'depart3',
    component: 'JEllipsis',
    label: '选中部门',
    colProps: { span: 12 },
  },
  {
    field: 'depart2',
    component: 'JSelectDept',
    label: '选择部门',
    helpMessage: ['component模式'],
    componentProps: { showButton: false },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'depart2',
    component: 'JEllipsis',
    label: '选中部门',
    colProps: { span: 12 },
  },
  {
    field: 'user2',
    component: 'JSelectUser',
    label: '用户选择组件',
    helpMessage: ['component模式'],
    componentProps: {
      labelKey: 'realname',
      rowKey: 'id',
      showSelected: true,
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'user2',
    component: 'JEllipsis',
    label: '选中用户',
    colProps: { span: 12 },
  },
  {
    field: 'user3',
    component: 'JSelectUserByDept',
    label: '部门选择用户',
    helpMessage: ['component模式'],
    componentProps: {
      labelKey: 'realname',
      rowKey: 'username',
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'user3',
    component: 'JEllipsis',
    label: '选中用户',
    colProps: { span: 12 },
  },
  {
    field: 'role2',
    component: 'JSelectRole',
    label: '角色选择组件',
    helpMessage: ['component模式'],
    colProps: {
      span: 12,
    },
  },
  {
    field: 'role2',
    component: 'JEllipsis',
    label: '选中角色',
    colProps: { span: 12 },
  },
  {
    field: 'position2',
    component: 'JSelectPosition',
    label: '职务选择组件',
    helpMessage: ['component模式'],
    colProps: { span: 12 },
    componentProps: { async: true, showSelectTable: true },
  },
  {
    field: 'position2',
    component: 'JEllipsis',
    label: '选中职务',
    colProps: { span: 12 },
  },
  {
    field: 'checkbox1',
    component: 'JCheckbox',
    label: 'JCheckbox组件1',
    helpMessage: ['component模式'],
    defaultValue: '1,2',
    componentProps: {
      options: [
        { label: '男', value: '1' },
        { label: '女', value: '2' },
      ],
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'checkbox1',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },
  {
    field: 'checkbox2',
    component: 'Input',
    label: 'JCheckbox组件2',
    defaultValue: '1',
    helpMessage: ['插槽模式'],
    slot: 'JCheckbox',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'checkbox2',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },
  {
    field: 'data1',
    label: '日期选择',
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'data1',
    component: 'JEllipsis',
    label: '选中值',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'data2',
    label: '年份范围选择',
    component: 'RangePicker',
    componentProps: {
      picker: 'year',
      valueFormat: 'YYYY',
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'data2',
    component: 'JEllipsis',
    label: '选中值',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'hk',
    component: 'Input',
    label: '滑块验证码',
    helpMessage: ['插槽模式'],
    slot: 'dargVerify',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'hk',
    component: 'JEllipsis',
    label: '选中值',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'JTreeDict',
    component: 'JTreeDict',
    label: '树字典',
    helpMessage: ['component模式'],
    colProps: { span: 12 },
  },
  {
    field: 'JTreeDict',
    component: 'JEllipsis',
    label: '选中值',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'ts',
    component: 'JTreeSelect',
    label: '下拉树选择',
    helpMessage: ['component模式'],
    componentProps: {
      dict: 'sys_permission,name,id',
      pidField: 'parent_id',
      hasChildField: 'is_leaf',
      converIsLeafVal: 0,
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'ts',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },
  {
    field: 'ts1',
    component: 'JTreeSelect',
    label: '下拉树多选',
    helpMessage: ['component模式'],
    componentProps: {
      dict: 'sys_permission,name,id',
      pidField: 'parent_id',
      hasChildField: 'is_leaf',
      converIsLeafVal: 0,
      multiple: true,
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'ts1',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },
  {
    field: 'category',
    component: 'JCategorySelect',
    label: '分类字典树',
    helpMessage: ['component模式'],
    defaultValue: '',
    componentProps: {
      pcode: 'B01',
      multiple: true,
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'category',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },
  {
    field: 'JEasyCron',
    component: 'JEasyCron',
    label: 'JEasyCron',
    helpMessage: ['component模式'],
    colProps: { span: 12 },
    defaultValue: '* * * * * ? *',
    rules: [{ validator: JCronValidator }],
  },
  {
    field: 'JEasyCron',
    component: 'JEllipsis',
    label: '选择值',
    colProps: { span: 12 },
  },
  {
    field: 'JInput',
    component: 'JInput',
    label: '特殊查询组件',
    helpMessage: ['插槽模式'],
    slot: 'JInput',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'jinputtype',
    component: 'Select',
    label: '查询类型',
    componentProps: {
      options: [
        { value: 'like', label: '模糊（like）' },
        { value: 'ne', label: '不等于（ne）' },
        { value: 'ge', label: '大于等于（ge）' },
        { value: 'le', label: '小于等于（le)' },
      ],
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'JInput',
    component: 'JEllipsis',
    label: '输入值',
    colProps: { span: 6 },
  },
  {
    field: 'field1',
    component: 'Select',
    label: '省市区选择',
    helpMessage: ['插槽模式'],
    slot: 'jAreaLinkage',
    colProps: {
      span: 12,
    },
    defaultValue: ['130000', '130200'],
  },
  {
    field: 'field1',
    component: 'JEllipsis',
    label: '选中值',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'field0',
    component: 'Select',
    label: '禁用组件(方式一)',
    helpMessage: ['插槽模式'],
    slot: 'jAreaLinkage1',
    colProps: {
      span: 12,
    },
    defaultValue: ['130000', '130200'],
  },

  {
    field: 'field0',
    component: 'JEllipsis',
    label: '选中值',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'field2',
    component: 'JAreaLinkage',
    label: '禁用组件(方式二)',
    helpMessage: ['component模式'],
    colProps: {
      span: 12,
    },
    dynamicDisabled: ({ values }) => {
      console.log(values);
      return isDisabledAuth(['demo.dbarray']);
    },
    defaultValue: ['140000', '140300', '140302'],
  },
  {
    field: 'field2',
    component: 'JEllipsis',
    label: '选中值',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'pca1',
    component: 'JAreaSelect',
    label: '省市区级联',
    helpMessage: ['component模式'],
    defaultValue: '140302',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'pca1',
    component: 'JEllipsis',
    label: '选中值',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'pop1',
    component: 'Input',
    label: 'JPopup示例',
    helpMessage: ['插槽模式'],
    slot: 'JPopup',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'pop1',
    component: 'JEllipsis',
    label: '选中值',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'JInputPop',
    component: 'JInputPop',
    label: 'JInputPop',
    helpMessage: ['component模式'],
    colProps: { span: 12 },
  },
  {
    field: 'JInputPop',
    component: 'JEllipsis',
    label: '输入值',
    colProps: { span: 12 },
  },
  {
    field: 'JTreeDictAsync',
    component: 'JTreeDict',
    label: '异步JTreeDict',
    helpMessage: ['component模式'],
    colProps: { span: 12 },
    componentProps: { async: true },
  },
  {
    field: 'JTreeDictAsync',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },
  {
    field: 'JSwitch',
    component: 'JSwitch',
    label: 'JSwitch',
    helpMessage: ['component模式'],
    colProps: { span: 12 },
  },
  {
    field: 'JSwitch',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },
  {
    field: 'JSwitchSelect',
    component: 'JSwitch',
    label: 'JSwitchSelect',
    helpMessage: ['component模式'],
    colProps: { span: 12 },
    componentProps: { query: true },
  },
  {
    field: 'JSwitchSelect',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },
  
  {
    field: 'userSelect2',
    component: 'UserSelect',
    label: '高级用户选择',
    helpMessage: ['component模式'],
    colProps: { span: 12 },
  },
  {
    field: 'userSelect2',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },
  
  {
    field: 'superQuery',
    component: 'Input',
    label: '高级查询',
    helpMessage: ['插槽模式'],
    slot: 'superQuery',
    colProps: { span: 12 },
  },
  {
    field: 'superQuery',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },
  {
    field: 'superQuery1',
    component: 'Input',
    label: '高级查询',
    helpMessage: ['插槽模式-自己保存查询条件'],
    slot: 'superQuery1',
    colProps: { span: 12 },
  },
  {
    field: 'superQuery1',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },
  {
    field: 'pop2',
    component: 'JPopupDict',
    label: 'JPopupDict示例',
    colProps: {
      span: 12,
    },
    componentProps:{
      placeholder: '请选择',
      dictCode: 'report_user,username,id',
      multi: true,
    },
  },
  {
    field: 'pop2',
    component: 'JEllipsis',
    label: '选中值',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'sex',
    component: 'JDictSelectTag',
    label: '性别(控制下方课程options)',
    helpMessage: ['component模式','性别不同，下方课程展示选项不同'],
    componentProps: {
      dictCode: 'sex',
      type: 'radioButton',
      onChange: (value) => {
        console.log(value);
      },
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'sex',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },
  {
    field: 'course',
    component: 'Select',
    label: '课程',
    dynamicPropskey: 'options',
    dynamicPropsVal: ({ model }) => {
      let options;
      if (model.sex == 1) {
        return [
          { value: '0', label: 'java - 男' },
          { value: '1', label: 'vue - 男' },
        ];
      } else if (model.sex == 2) {
        return [
          { value: '2', label: '瑜伽 - 女' },
          { value: '3', label: '美甲 - 女' },
        ];
      } else {
        return [];
      }
    },
    componentProps: {
      disabled: false,
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'course',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },
  {
    field: 'field100',
    component: 'JInputSelect',
    label: 'JInputSelect',
    helpMessage: ['component模式'],
    componentProps: {
      selectPlaceholder: '可选择系统变量',
      inputPlaceholder: '请输入',
      options: [
        {
          label: '登录用户账号',
          value: '${sys_user_code}',
        },
        {
          label: '登录用户名称',
          value: '${sys_user_name}',
        },
        {
          label: '当前日期',
          value: '${sys_date}',
        },
        {
          label: '当前时间',
          value: '${sys_date}',
        },
        {
          label: '登录用户部门',
          value: '${sys_org_code}',
        },
        {
          label: '用户拥有部门',
          value: '${sys_multi_org_code}',
        },
        {
          label: '登录用户租户',
          value: '${tenant_id}',
        },
      ],
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'field100',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },
  {
    field: 'JAreaLinkage',
    component: 'JAreaLinkage',
    label: '省市区选择',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'JAreaLinkage',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },

  {
    field: 'orderAuth',
    component: 'Input',
    label: '指令权限',
    helpMessage: ['有权限右侧的"选中值"可见，否则不可见'],
    colProps: {
      span: 12,
    },
  },
  {
    field: 'orderAuth',
    auth: 'demo:order:auth',
    component: 'JEllipsis',
    label: '选中值',
    colProps: { span: 12 },
  },
  
];
