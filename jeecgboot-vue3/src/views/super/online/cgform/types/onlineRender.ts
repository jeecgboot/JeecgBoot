/**
 * 因为online列表渲染 公用一个list 所以一些独有的属性配置 需要单独放到map中存放 避免切换路由冲突
 */
interface SpecialConfig {
  // 排序字段
  sortField: string;
  // 排序方式
  sortType: 'asc' | 'desc';
  // 当前页数
  currentPage: number;
  // 当前每页数目
  pageSize: number;
  // 总页数
  total: number;
  // 选中的行key
  selectedRowKeys: string[];
  // 查询条件
  queryParam: object;
  // href跳转至online列表页 会携带参数
  acceptHrefParams: object;
  // 路由是否被缓存
  cache: boolean;
  // 表描述
  description: string;
  // 表名
  currentTableName: string;
  // 是否启用表单设计器表单
  isDesForm: boolean;
  // 表单设计器表单编码
  desFormCode: string;
  isTree: boolean;
  hasChildrenField?: string;
}

interface Page {
  current?: number;
  pageSize?: number;
  pageSizeOptions?: string[];
  showTotal?: Function;
  showQuickJumper?: boolean;
  showSizeChanger?: boolean;
  total?: number;
}

interface CgFormButton {
  buttonCode?: string;
  buttonName?: string;
  buttonStyle?: string;
  optType?: 'js' | 'bus';
  exp?: string;
  buttonIcon?: string;
}

/***
 * 表单页面的扩展配置
 */
interface ExtendConfig {
  modalMinWidth: number;
}

/***
 * 表单字段的扩展配置解析结果
 */
interface FieldExtends {
  //上传数量
  uploadnum?: number | string;

  //限制大文本在列表页面的展示长度
  showLength?: number | string;

  //popup是否支持多选
  popupMulti?: boolean;

  //下拉搜索是否支持多选
  multiple?: boolean;

  //部门、用户组件 用于存储的字段名
  store?: string;

  //部门、用户组件 用于展示的字段名
  text?: string;

  //部门、用户组件 是否多选
  multiSelect?: boolean;

  //查询排序规则
  orderRule?: 'asc' | 'desc';
  
  // 关联记录展示风格 card/select
  showType?: string;
  // 关联记录封面图
  imageField?: string;
  // label长度
  labelLength?: number;
}

// online提交表单和流程标识 默认只提交表单
const SUBMIT_FLOW_KEY = 'jeecg_submit_form_and_flow';
// 表单提交成功后回传表单数据的id key用于提交流程取id
const SUBMIT_FLOW_ID = 'flow_submit_id';
// 表单提交成功后 在formData中添加设置表名的属性
const ONL_FORM_TABLE_NAME = 'online_form_table_name';
// 校验失败
const VALIDATE_FAILED = 'validate-failed';

/*
 * 查询表单的样式-label
 * */
const ONL_QUERY_LABEL_COL = { xs: { span: 24 }, sm: { span: 6 } };

/*
 * 查询表单的样式-wrapper
 * */
const ONL_QUERY_WRAPPER_COL = { xs: { span: 24 }, sm: { span: 18 } };

/**setup*/
const SETUP = 'setup';

/**EnhanceJS*/
const ENHANCEJS = 'EnhanceJS';

/**
 * 表单类型转换成查询类型
 * 普通查询和高级查询组件区别 ：高级查询不支持联动组件
 */
const FORM_VIEW_TO_QUERY_VIEW = {
  password: 'text',
  file: 'text',
  image: 'text',
  textarea: 'text',
  umeditor: 'text',
  markdown: 'text',
  checkbox: 'list_multi',
  radio: 'list',
};

/**下拉组件PopupContainer类选择器*/
const POP_CONTAINER = '.jeecg-online-modal .ant-modal-content';

/**online权限前缀-现主要用于子表button*/
const ONL_AUTH_PRE = 'online_';

export {
  SpecialConfig,
  Page,
  CgFormButton,
  ExtendConfig,
  FieldExtends,
  SUBMIT_FLOW_KEY,
  SUBMIT_FLOW_ID,
  VALIDATE_FAILED,
  ONL_QUERY_LABEL_COL,
  ONL_QUERY_WRAPPER_COL,
  SETUP,
  ENHANCEJS,
  FORM_VIEW_TO_QUERY_VIEW,
  POP_CONTAINER,
  ONL_AUTH_PRE,
  ONL_FORM_TABLE_NAME
};
