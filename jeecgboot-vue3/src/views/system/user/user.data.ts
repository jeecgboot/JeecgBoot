import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { getAllRolesListNoByTenant, getDepPostIdByDepId } from './user.api';
import { rules } from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getDepartPathNameByOrgCode, getDepartName, getMultiDepartPathName, getDepartPathName } from '@/utils/common/compUtils';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';
export const columns: BasicColumn[] = [
  {
    title: '用户账号',
    dataIndex: 'username',
    width: 120,
  },
  {
    title: '用户姓名',
    dataIndex: 'realname',
    width: 100,
  },
/*  {
    title: '头像',
    dataIndex: 'avatar',
    width: 120,
    customRender: render.renderAvatar,
  },*/
  {
    title: '性别',
    dataIndex: 'sex',
    width: 80,
    sorter: true,
    customRender: ({ text }) => {
      return render.renderDict(text, 'sex');
    },
  },
/*  {
    title: '生日',
    dataIndex: 'birthday',
    width: 100,
  },*/
  {
    title: '手机号',
    dataIndex: 'phone',
    width: 100,
    customRender:( { record, text })=>{
      if(record.izHideContact && record.izHideContact === '1'){
        return '/';
      }
      return text;
    }
  },
  {
    title: '部门',
    width: 150,
    dataIndex: 'belongDepIds',
    customRender:( { record, text })=>{
      if(!text){
        return '';
      }
      return getDepartName(getMultiDepartPathName(record.orgCodeTxt,text));
    }
  },
  {
    title: '负责部门',
    width: 150,
    dataIndex: 'departIds',
    customRender:( { record, text })=>{
      if(!text){
        return '';
      }
      return getDepartName(getMultiDepartPathName(record.departIds_dictText,text));
    }
  },
  {
    title: '主岗位',
    width: 150,
    dataIndex: 'mainDepPostId',
    customRender: ({ record, text })=>{
      return getDepartName(getDepartPathName(record.mainDepPostId_dictText,text,false));
    }
  },
  {
    title: '兼职岗位',
    width: 150,
    dataIndex: 'otherDepPostId',
    customRender:({ record, text })=>{
      if(!text){
        return '';
      }
      return getDepartName(getMultiDepartPathName(record.otherDepPostId_dictText,text));
    }
  },
  {
    title: '状态',
    dataIndex: 'status_dictText',
    width: 80,
  },
];

export const recycleColumns: BasicColumn[] = [
  {
    title: '用户账号',
    dataIndex: 'username',
    width: 100,
  },
  {
    title: '用户姓名',
    dataIndex: 'realname',
    width: 100,
  },
  {
    title: '头像',
    dataIndex: 'avatar',
    width: 80,
    customRender: render.renderAvatar,
  },
  {
    title: '性别',
    dataIndex: 'sex',
    width: 80,
    sorter: true,
    customRender: ({ text }) => {
      return render.renderDict(text, 'sex');
    },
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    label: '账号',
    field: 'username',
    component: 'JInput',
    //colProps: { span: 6 },
  },
  {
    label: '名字',
    field: 'realname',
    component: 'JInput',
   //colProps: { span: 6 },
  },
  {
    label: '性别',
    field: 'sex',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'sex',
      placeholder: '请选择性别',
      stringToNumber: true,
    },
    //colProps: { span: 6 },
  },
  {
    label: '手机号码',
    field: 'phone',
    component: 'Input',
    //colProps: { span: 6 },
  },
  {
    label: '用户状态',
    field: 'status',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'user_status',
      placeholder: '请选择状态',
      stringToNumber: true,
    },
   //colProps: { span: 6 },
  },
  {
    label: '所属部门',
    field: 'departId',
    component: 'JSelectDept',
    componentProps: {
      placeholder: '请选择所属部门',
      showButton: false,
      checkStrictly: true
    },
  },
];

export const formSchema: FormSchema[] = [
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    label: '用户账号',
    field: 'username',
    component: 'Input',
    required: true,
    dynamicDisabled: ({ values }) => {
      return !!values.id;
    },
    dynamicRules: ({ model, schema }) => rules.duplicateCheckRule('sys_user', 'username', model, schema, true),
  },
  {
    label: '登录密码',
    field: 'password',
    component: 'StrengthMeter',
    componentProps:{
      autocomplete: 'new-password',
    },
    rules: [
      {
        required: true,
        message: '请输入登录密码',
      },
      {
        pattern: /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[~!@#$%^&*()_+`\-={}:";'<>?,./]).{8,}$/,
        message: '密码由 8 位及以上数字、大小写字母和特殊符号组成！',
      },
    ],
  },
  {
    label: '确认密码',
    field: 'confirmPassword',
    component: 'InputPassword',
    dynamicRules: ({ values }) => rules.confirmPassword(values, true),
  },
  {
    label: '用户姓名',
    field: 'realname',
    required: true,
    component: 'Input',
  },
  {
    label: '工号',
    field: 'workNo',
    required: false,
    component: 'Input',
    dynamicRules: ({ model, schema }) => rules.duplicateCheckRule('sys_user', 'work_no', model, schema, false),
  },
/*  {
    label: '职务',
    field: 'post',
    required: false,
    component: 'JSelectPosition',
    componentProps: {
      labelKey: 'name',
    },
  },*/
  {
    label: '职务',
    field: 'positionType',
    required: false,
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: "user_position",
      mode: 'multiple',
    },
  },
  {
    label: '角色',
    field: 'selectedroles',
    component: 'ApiSelect',
    componentProps: {
      mode: 'multiple',
      api: getAllRolesListNoByTenant,
      labelField: 'roleName',
      valueField: 'id',
      immediate: false,
    },
  },
  {
    label: '所属部门',
    field: 'selecteddeparts',
    component: 'JSelectDept',
    componentProps: ({ formActionType, formModel }) => {
      return {
        sync: false,
        checkStrictly: true,
        defaultExpandLevel: 2,

        onSelect: (options, values) => {
          const { updateSchema } = formActionType;
          //所属部门修改后更新负责部门下拉框数据
          updateSchema([
            //修改主岗位和兼职岗位的参数
            {
              field: 'mainDepPostId',
              componentProps: { params: { departIds: values?values.value.join(","): "" } },
            },
            {
              field: 'otherDepPostId',
              componentProps: { params: { departIds: values?values.value.join(","): "" } },
            }
          ]);
          //更新负责部门的option
          updateDepartOption(options, updateSchema);
          if(!values){
            formModel.departIds = [];
            formModel.mainDepPostId = "";
            formModel.otherDepPostId = "";
            return;
          }
          //所属部门修改后更新负责部门数据
          formModel.departIds && (formModel.departIds = formModel.departIds.filter((item) => values.value.indexOf(item) > -1));
        },
        onChange: async (values) => {
          // 当所属部门发生改变时，需要取消主岗位和兼职岗位的选中值
          await removeDepPostByDepId(formModel, values, formActionType);
        }
      };
    },
  },
  {
    label: '主岗位',
    field: 'mainDepPostId',
    component: 'JSelectDepartPost',
    componentProps: {
      rowKey: 'id',
      multiple: false,
      izShowDepPath: true,
    },
    ifShow:  ({ values }) => {
      if(!values.selecteddeparts){
        return false;
      }
      return !(values.selecteddeparts instanceof Array && values.selecteddeparts.length == 0);
    },
  },
  {
    label: '兼职岗位',
    field: 'otherDepPostId',
    component: 'JSelectDepartPost',
    componentProps: {
      rowKey: 'id',
      izShowDepPath: true,
    },
    ifShow:  ({ values }) => {
      if(!values.selecteddeparts){
        return false;
      }
      return !(values.selecteddeparts instanceof Array && values.selecteddeparts.length == 0);
    },
  },
  {
    label: '租户',
    field: 'relTenantIds',
    component: 'JSearchSelect',
    componentProps: {
      dict:"sys_tenant,name,id",
      async: true,
      multiple: true
    },
  },
  {
    label: '身份',
    field: 'userIdentity',
    component: 'RadioGroup',
    defaultValue: 1,
    componentProps: ({ formModel }) => {
      return {
        options: [
          { label: '普通用户', value: 1, key: '1' },
          { label: '上级', value: 2, key: '2' },
        ],
        onChange: () => {
          formModel.userIdentity == 1 && (formModel.departIds = []);
        },
      };
    },
  },
  {
    label: '负责部门',
    field: 'departIds',
    component: 'Select',
    componentProps: {
      mode: 'multiple',
      tagRender: ({ label, value, closable, onClose }) => {
        // 计算显示文本：前面省略号 + 后面字符
        let displayLabel = label;
        if(displayLabel && label.length >= 20) {
          displayLabel = "..." + displayLabel.substring(label.length - 20);
        }
        return h(Tag, {
          style: {
            position: 'relative',
            boxSizing: 'border-box',
            height: '24px',
            marginTop: '2px',
            fontSize: '14px',
            marginBottom: '2px',
            lineHeight: '22px',
            background: 'rgba(51, 51, 51, 0.06)',
            border: '1px solid rgba(5, 5, 5, 0.06)',
            borderRadius: '4px',
            cursor: 'default'
          },
          title: label,
          closable,
          onClose:(e)=>{
            e.stopPropagation();
            onClose();
          }
        }, () => displayLabel);
      }
    },
    ifShow: ({ values }) => values.userIdentity == 2,
  },
  {
    label: '排序',
    field: 'sort',
    component: 'InputNumber',
    defaultValue: 1000,
    componentProps: {
      min: 1,
      max: 999999,
      step: 1,
      precision: 0
    }
  },
  {
    label: '头像',
    field: 'avatar',
    component: 'JImageUpload',
    componentProps: {
      fileMax: 1,
    },
  },
  {
    label: '生日',
    field: 'birthday',
    component: 'DatePicker',
  },
  {
    label: '性别',
    field: 'sex',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'sex',
      placeholder: '请选择性别',
      stringToNumber: true,
    },
  },
  {
    label: '邮箱',
    field: 'email',
    component: 'Input',
    required: false,
    dynamicRules: ({ model, schema }) => {
      return [
        { ...rules.duplicateCheckRule('sys_user', 'email', model, schema, false)[0], trigger: 'blur' },
        { ...rules.rule('email', false)[0], trigger: 'blur' },
      ];
    },
  },
  {
    label: '手机号码',
    field: 'phone',
    component: 'Input',
    required: true,
    dynamicRules: ({ model, schema }) => {
      return [
        { ...rules.duplicateCheckRule('sys_user', 'phone', model, schema, true)[0], trigger: 'blur' },
        { pattern: /^1[3456789]\d{9}$/, message: '手机号码格式有误', trigger: 'blur' },
      ];
    },
  },
  {
    label: '座机',
    field: 'telephone',
    component: 'Input',
    rules: [{ pattern: /^0\d{2,3}-[1-9]\d{6,7}$/, message: '请输入正确的座机号码' }],
  },
  {
    label: '工作流引擎',
    field: 'activitiSync',
    defaultValue: 1,
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'activiti_sync',
      type: 'radio',
      stringToNumber: true,
    },
  },
  {
    label: '隐藏联系方式',
    field: 'izHideContact',
    defaultValue: '0',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'yn',
      type: 'radio',
    },
  },
];

export const formPasswordSchema: FormSchema[] = [
  {
    label: '用户账号',
    field: 'username',
    component: 'Input',
    componentProps: { readOnly: true },
  },
  {
    label: '登录密码',
    field: 'password',
    component: 'StrengthMeter',
    componentProps: {
      placeholder: '请输入登录密码',
    },
    rules: [
      {
        required: true,
        message: '请输入登录密码',
      },
      {
        pattern: /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[~!@#$%^&*()_+`\-={}:";'<>?,./]).{8,}$/,
        message: '密码由 8 位及以上数字、大小写字母和特殊符号组成！',
      },
    ],
  },
  {
    label: '确认密码',
    field: 'confirmPassword',
    component: 'InputPassword',
    dynamicRules: ({ values }) => rules.confirmPassword(values, true),
  },
];


//租户用户列表
export const userTenantColumns: BasicColumn[] = [
  {
    title: '用户账号',
    dataIndex: 'username',
    width: 120,
  },
  {
    title: '用户姓名',
    dataIndex: 'realname',
    width: 100,
  },
  {
    title: '头像',
    dataIndex: 'avatar',
    width: 120,
    customRender: render.renderAvatar,
  },
  {
    title: '手机号',
    dataIndex: 'phone',
    width: 100,
  },
  {
    title: '部门',
    width: 150,
    dataIndex: 'orgCodeTxt',
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 80,
    customRender: ({ text }) => {
      if (text === '1') {
        return '正常';
      } else if (text === '3') {
        return '审批中';
      } else {
        return '已拒绝';
      }
    },
  },
];

//用户租户搜索表单
export const userTenantFormSchema: FormSchema[] = [
  {
    label: '账号',
    field: 'username',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '名字',
    field: 'realname',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '性别',
    field: 'sex',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'sex',
      placeholder: '请选择性别',
      stringToNumber: true,
    },
    colProps: { span: 6 },
  },
];


/**
 * 删除非当前部门下的数据
 * 当所属部门发生改变时，取消主岗位和兼职岗位的选中值
 * 
 * @param formModel 表单模型
 * @param values 选中的部门值
 * @param formActionType 表单操作方法
 */
async function removeDepPostByDepId(formModel, values, formActionType) {
  const { setFieldsValue } = formActionType;
  if (values) {
    let departIds = "";
    if (values instanceof Array) {
      departIds = values.join(",");
    } else {
      departIds = values;
    }
    if (departIds) {
      try {
        // 查询当前选中部门下的岗位ID
        const { result } = await getDepPostIdByDepId({ depIds: departIds });
        const validPostIds = result.split(",") || [];
        
        // 检查主岗位是否在当前部门下
        if (formModel.mainDepPostId) {
          const mainPostId = Array.isArray(formModel.mainDepPostId) 
            ? formModel.mainDepPostId[0] 
            : formModel.mainDepPostId;
          
          if (mainPostId && !validPostIds.includes(mainPostId)) {
            // 主岗位不在当前部门下，清空主岗位
            setFieldsValue({ mainDepPostId: null });
            formModel.mainDepPostId = null;
          }
        }

        // 检查兼职岗位是否在当前部门下
        if(typeof formModel.otherDepPostId === "string"){
          formModel.otherDepPostId = formModel.otherDepPostId.split(",");
        }
        if (formModel.otherDepPostId && Array.isArray(formModel.otherDepPostId)) {
          const validOtherPosts = formModel.otherDepPostId.filter(postId => 
            validPostIds.includes(postId)
          );
          // 有兼职岗位不在当前部门下，更新兼职岗位
          setFieldsValue({ otherDepPostId: validOtherPosts });
          formModel.otherDepPostId = validOtherPosts;
        }
      } catch (error) {
        console.error('查询部门岗位失败:', error);
        // 查询失败时，清空所有岗位选择
        setFieldsValue({
          mainDepPostId: null,
          otherDepPostId: []
        });
        formModel.mainDepPostId = null;
        formModel.otherDepPostId = [];
      }
    } else {
      // 没有选中部门时，清空所有岗位选择
      setFieldsValue({
        mainDepPostId: null,
        otherDepPostId: []
      });
      formModel.mainDepPostId = null;
      formModel.otherDepPostId = [];
    }
  }
}

/**
 * 更新负责部门的options
 *
 * @param options
 * @param updateSchema
 */
async function updateDepartOption(options, updateSchema) {
  if (options && options.length > 0) {
    // 并行处理所有异步操作
    const updatedOptions = await Promise.all(
      options.map(async (item) => {
        const departPathName = await getDepartPathNameByOrgCode('', item.label, item.value);
        return { ...item, label: departPathName };
      })
    );
    updateSchema([
      {
        field: 'departIds',
        componentProps: { options: updatedOptions },
      },
    ]);
  } else {
    updateSchema([
      {
        field: 'departIds',
        componentProps: { options: [] },
      },
    ]);
  }
}
