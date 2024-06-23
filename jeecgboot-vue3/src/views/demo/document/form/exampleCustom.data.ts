import { FormSchema } from '/@/components/Form';
import { defHttp } from '/@/utils/http/axios';

export const schemas: FormSchema[] = [
  {
    label: '验证码',
    field: 'code',
    component: 'InputCountDown',
    componentProps: {
      //'default': 默认, 'large': 最大, 'small': 最小
      size:'default',
      //倒计时
      count: 120,
    },
  },
  {
    label: 'Api下拉选择',
    field: 'apiSelect',
    component: 'ApiSelect',
    componentProps: {
      //multiple: 多选；不填写为单选
      mode: 'multiple',
      //请求api,返回结果{ result: { records:[{'id':'1',name:'scott'},{'id':'2',name:'小张'}] }}
      api: () => defHttp.get({ url: '/test/jeecgDemo/list' }),
      //数值转成String
      numberToString: false,
      //标题字段
      labelField: 'name',
      //值字段
      valueField: 'id',
      //请求参数
      params: {},
      //返回结果字段
      resultField: 'records',
    },
  },
  {
    label: 'Api树选择',
    field: 'apiSelect',
    component: 'ApiTreeSelect',
    componentProps: {
      /* 请求api,返回结果
         { result: { list: [{ title:'选项0',value:'0',key:'0',
           children: [ {"title": "选项0-0","value": "0-0","key": "0-0"},...]
           }, ...]
         }} */
      api: () => defHttp.get({ url: '/mock/tree/getDemoOptions' }),
      //请求参数
      params: {},
      //返回结果字段
      resultField: 'list',
    },
  },
  {
    label: '校验密码强度',
    field: 'pwd',
    component: 'StrengthMeter',
    componentProps: {
      //是否显示密码文本框
      showInput: true,
      //是否禁用
      disabled: false,
    },
  },
  {
    label: '省市县联动',
    field: 'province',
    component: 'JAreaLinkage',
    componentProps: {
      //是否显示区县，默认true,否则只显示省
      showArea: true,
      //是否是全部文本，默认false
      showAll: true,
    },
  },
  {
    label: '岗位选择',
    field: 'post',
    component: 'JSelectPosition',
    componentProps: {
      //是否右侧显示选中列表
      showSelected: true,
      //最大选择数量
      maxSelectCount: 1,
      //岗位标题
      modalTitle: '岗位',
    },
  },
  {
    label: '角色选择',
    field: 'role',
    component: 'JSelectRole',
    componentProps: {
      //请求参数 如params:{"code":"001"}
      params: {},
      //是否单选,默认false
      isRadioSelection: true,
      //角色标题
      modalTitle: '角色',
    },
  },
  {
    label: '用户选择',
    field: 'user',
    component: 'JSelectUser',
    componentProps: {
      //取值字段配置,一般为主键字段
      rowKey: 'username',
      //显示字段配置
      labelKey: 'realname',
      //是否显示选择按钮
      showButton: false,
      //用户标题
      modalTitle: '用户',
    },
  },
  {
    label: '图片上传',
    field: 'uploadImage',
    component: 'JImageUpload',
    componentProps: {
      //按钮显示文字
      text:'图片上传',
      //支持两种基本样式picture和picture-card
      listType:'picture-card',
      //用于控制文件上传的业务路径,默认temp
      bizPath:'temp',
      //是否禁用
      disabled:false,
      //最大上传数量
      fileMax:1,
    },
  },
  {
    label: '字典标签',
    field: 'dictTags',
    component: 'JDictSelectTag',
    componentProps: {
      //字典code配置，比如通过性别字典编码：sex，也可以使用demo,name,id 表名,名称,值的方式
      dictCode:'sex',
      //支持radio(单选按钮)、radioButton(单选按钮 btn风格)、select(下拉框)
      type:'radioButton'
    },
  },
  {
    label: '部门选择',
    field: 'dept',
    component: 'JSelectDept',
    componentProps: {
      //是否开启异步加载
      sync: false,
      //是否显示复选框
      checkable: true,
      //是否显示选择按钮
      showButton: false,
      //父子节点选中状态不再关联
      checkStrictly: true,
      //选择框标题
      modalTitle: '部门选择',
    },
  },
  {
    label: '省市县级联动',
    field: 'provinceArea',
    component: 'JAreaSelect',
    componentProps: {
      //级别 1 只显示省 2 省市 3 省市区
      level:3
    },
  },
  {
    label: '富文本',
    field: 'editor',
    component: 'JEditor',
    componentProps: {
      //是否禁用
      disabled: false
    },
  },
  {
    label: 'markdown',
    field: 'markdown',
    component: 'JMarkdownEditor',
    componentProps: {
      //是否禁用
      disabled: false
    },
  },
  {
    label: '可输入下拉框',
    field: 'inputSelect',
    component: 'JSelectInput',
    componentProps: {
      options: [
        { label: 'Default', value: 'default' },
        { label: 'IFrame', value: 'iframe' },
      ],
      //是否为搜索模式
      showSearch: true,
      //是否禁用
      disabled: false
    },
  },
  {
    label: '代码编辑器组件',
    field: 'jCode',
    component: 'JCodeEditor',
    componentProps: {
      //高度，默认auto
      height:'150px',
      //是否禁用
      disabled:false,
      //是否全屏
      fullScreen:false,
      //全屏之后的坐标
      zIndex: 999,
      //代码主题，目前只支持idea,可在组件自行扩展
      theme:'idea',
      //代码提示
      keywords:['console'],
      //语言如（javascript,vue,markdown）可在组件自行扩展
      language:'javascript'
    },
  },
  {
    label: '分类字典树',
    field: 'dictTree',
    component: 'JCategorySelect',
    componentProps: {
      //占位内容
      placeholder:'请选择分类字典树',
      //查询条件，如“{'name':'笔记本'}”
      condition:"",
      //是否多选
      multiple: false,
      //起始选择code，见配置的分类字典的类型编码
      pcode: 'A04',
      //父级id
      pid:'',
      //返回key
      back:'id',
    },
  },
  {
    label: '下拉多选',
    field: 'selectMultiple',
    component: 'JSelectMultiple',
    componentProps: {
      //字典code配置，比如通过性别字典编码：sex，也可以使用demo,name,id 表名,名称,值的方式
      dictCode:'company_rank',
      //是否只读
      readOnly:false,
    },
  },
  {
    label: 'popup',
    field: 'popup',
    component: 'JPopup',
    componentProps: ({ formActionType }) => {
      const {setFieldsValue} = formActionType;
      return{
        setFieldsValue:setFieldsValue,
        //online报表编码
        code:"demo",
        //是否为多选
        multi:false,
        //字段配置
        fieldConfig: [
          { source: 'name', target: 'popup' },
        ],
      }
    },
  },
  {
    label: '开关自定义',
    field: 'switch',
    component: 'JSwitch',
    componentProps:{
      //取值 options
      options:['Y','N'],
      //文本option
      labelOptions:['是', '否'],
      //是否启用下拉
      query: false,
      //是否禁用
      disabled: false,
    },
  },
  {
    label: '定时表达式选择',
    field: 'timing',
    component: 'JEasyCron',
    componentProps:{
      //是否隐藏参数秒和年设置，如果隐藏，那么参数秒和年将会全部忽略掉。
      hideSecond: false,
      //是否隐藏参数年设置，如果隐藏，那么参数年将会全部忽略掉
      hideYear: false,
      //是否禁用
      disabled: false,
      //获取预览执行时间列表的函数，格式为：remote (cron值, time时间戳, cb回调函数)
      remote:(cron,time,cb)=>{}
    },
  },
  {
    label: '分类字典树',
    field: 'treeDict',
    component: 'JTreeDict',
    componentProps:{
      //指定当前组件需要存储的字段 可选: id(主键)和code(编码)
      field:'id',
      //是否为异步
      async: true,
      //是否禁用
      disabled: false,
      //指定一个节点的编码,加载该节点下的所有字典数据,若不指定，默认加载所有数据
      parentCode:'A04'
    },
  },
  {
    label: '多行输入窗口',
    field: 'inputPop',
    component: 'JInputPop',
    componentProps:{
      //标题
      title:'多行输入窗口',
      //弹窗显示位置
      position:'bottom',
    },
  },
  {
    label: '多选',
    field: 'multipleChoice',
    component: 'JCheckbox',
    componentProps:{
      //字典code配置，比如通过职位字典编码：company_rank，也可以使用demo,name,id 表名,名称,值的方式
      dictCode:'company_rank',
      //是否禁用
      disabled: false,
      //没有字典code可以使用option来定义
      // options:[
      //   {label:'CE0',value:'1'}
      // ]
    },
  },
  {
    label: '下拉树选择',
    field: 'treeCusSelect',
    component: 'JTreeSelect',
    componentProps: {
      //字典code配置，比如通过性别字典编码：sex，也可以使用sys_permission,name,id 表名,名称,值的方式
      dict: 'sys_permission,name,id',
      //父级id字段
      pidField: 'parent_id',
    },
  },
  {
    label: '根据部门选择用户组件',
    field: 'userByDept',
    component: 'JSelectUserByDept',
    componentProps: {
      //是否显示选择按钮
      showButton: true,
      //选择框标题
      modalTitle: '部门用户选择'
    },
  },
  {
    label: '文件上传',
    field: 'uploadFile',
    component: 'JUpload',
    componentProps: {
      //是否显示选择按钮
      text: '文件上传',
      //最大上传数
      maxCount: 2,
      //是否显示下载按钮
      download: true,
    },
  },
  {
    label: '字典表搜索',
    field: 'dictSearchSelect',
    component: 'JSearchSelect',
    componentProps: {
      //字典code配置，通过 demo,name,id 表名,名称,值的方式
      dict: 'demo,name,id',
      //是否异步加载
      async: true,
      //当async设置为true时有效，表示异步查询时，每次获取数据的数量，默认10
      pageSize:3
    },
  },
  {
    label: '动态创建input框',
    field: 'jAddInput',
    component: 'JAddInput',
    componentProps: {
      //自定义超过多少行才会显示删除按钮，默认为1
      min:1
    },
  },
  {
    label: '用户选择组件',
    field: 'userCusSelect',
    component: 'UserSelect',
    componentProps: {
      //是否多选
      multi: true,
      //从用户表中选择一列，其值作为该控件的存储值，默认id列
      store: 'id',
      //是否排除我自己(当前登录用户)
      izExcludeMy: false,
      //是否禁用
      disabled: false,
    },
  },  
  {
    label: '选择角色组件',
    field: 'roleSelect',
    component: 'RoleSelect',
    componentProps: {
      //最大选择数量  
      maxSelectCount: 3,
      //是否单选
      isRadioSelection: false
    },
  },  
  {
    label: '数值范围输入框',
    field: 'rangeNumber',
    component: 'JRangeNumber',
  }, 
  {
    label: '远程Api单选框组',
    field: 'apiRadioGroup',
    component: 'ApiRadioGroup',
    componentProps:{
      //请求接口返回结果{ result:{ list: [ name: '选项0',id: '1' ] }}
      api:()=> defHttp.get({ url: '/mock/select/getDemoOptions' }),
      //请求参数
      params:{},
      //是否为按钮风格类型，默认false
      isBtn: false,
      //返回集合名称
      resultField: 'list',
      //标题字段名称
      labelField: 'name',
      //值字段名称
      valueField: 'id',
    }
  },
];
