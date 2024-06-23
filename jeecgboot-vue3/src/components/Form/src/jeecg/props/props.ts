//下拉选择框组件公共props
import { propTypes } from '/@/utils/propTypes';

export const selectProps = {
  //是否多选
  isRadioSelection: {
    type: Boolean,
    //update-begin---author:wangshuai ---date:20220527  for：部门用户组件默认应该单选，否则其他地方有问题------------
    default: false,
    //update-end---author:wangshuai ---date:20220527  for：部门用户组件默认应该单选，否则其他地方有问题--------------
  },
  //回传value字段名
  rowKey: {
    type: String,
    default: 'id',
  },
  //回传文本字段名
  labelKey: {
    type: String,
    default: 'name',
  },
  //查询参数
  params: {
    type: Object,
    default: () => {},
  },
  //是否显示选择按钮
  showButton: propTypes.bool.def(true),
  //是否显示右侧选中列表
  showSelected: propTypes.bool.def(false),
  //最大选择数量
  maxSelectCount: {
    type: Number,
    default: 0,
  },
};

//树形选择组件公共props
export const treeProps = {
  //回传value字段名
  rowKey: {
    type: String,
    default: 'key',
  },
  //回传文本字段名
  labelKey: {
    type: String,
    default: 'title',
  },
  //初始展开的层级
  defaultExpandLevel: {
    type: [Number],
    default: 0,
  },
  //根pid值
  startPid: {
    type: [Number, String],
    default: '',
  },
  //主键字段
  primaryKey: {
    type: [String],
    default: 'id',
  },
  //父ID字段
  parentKey: {
    type: [String],
    default: 'parentId',
  },
  //title字段
  titleKey: {
    type: [String],
    default: 'title',
  },
  //是否开启服务端转换tree数据结构
  serverTreeData: propTypes.bool.def(true),
  //是否开启异步加载数据
  sync: propTypes.bool.def(true),
  //是否显示选择按钮
  showButton: propTypes.bool.def(true),
  //是否显示复选框
  checkable: propTypes.bool.def(true),
  //checkable 状态下节点选择完全受控（父子节点选中状态不再关联）
  checkStrictly: propTypes.bool.def(false),
  // 是否允许多选，默认 true
  multiple: propTypes.bool.def(true),
};
