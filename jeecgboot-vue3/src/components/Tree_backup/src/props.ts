import type { PropType } from 'vue';
import type { ReplaceFields, ActionItem, Keys, CheckKeys, ContextMenuOptions, TreeItem } from './typing';
import type { ContextMenuItem } from '/@/hooks/web/useContextMenu';
import type { TreeDataItem } from 'ant-design-vue/es/tree/Tree';
import { propTypes } from '/@/utils/propTypes';

export const basicProps = {
  value: {
    type: [Object, Array] as PropType<Keys | CheckKeys>,
  },
  renderIcon: {
    type: Function as PropType<(params: Recordable) => string>,
  },

  helpMessage: {
    type: [String, Array] as PropType<string | string[]>,
    default: '',
  },

  title: propTypes.string,
  toolbar: propTypes.bool,
  search: propTypes.bool,
  searchValue: propTypes.string,
  checkStrictly: propTypes.bool,
  clickRowToExpand: propTypes.bool.def(true),
  checkable: propTypes.bool.def(false),
  defaultExpandLevel: {
    type: [String, Number] as PropType<string | number>,
    default: '',
  },
  // 高亮搜索值，仅高亮具体匹配值（通过title）值为true时使用默认色值，值为#xxx时使用此值替代且高亮开启
  highlight: {
    type: [Boolean, String] as PropType<Boolean | String>,
    default: false,
  },
  defaultExpandAll: propTypes.bool.def(false),

  replaceFields: {
    type: Object as PropType<ReplaceFields>,
  },

  treeData: {
    type: Array as PropType<TreeDataItem[]>,
  },

  actionList: {
    type: Array as PropType<ActionItem[]>,
    default: () => [],
  },

  expandedKeys: {
    type: Array as PropType<Keys>,
    default: () => [],
  },

  selectedKeys: {
    type: Array as PropType<Keys>,
    default: () => [],
  },

  checkedKeys: {
    type: Array as PropType<CheckKeys>,
    default: () => [],
  },

  beforeRightClick: {
    type: Function as PropType<(...arg: any) => ContextMenuItem[] | ContextMenuOptions>,
    default: null,
  },

  rightMenuList: {
    type: Array as PropType<ContextMenuItem[]>,
  },
  // 自定义数据过滤判断方法(注: 不是整个过滤方法，而是内置过滤的判断方法，用于增强原本仅能通过title进行过滤的方式)
  filterFn: {
    type: Function as PropType<(searchValue: any, node: TreeItem, replaceFields: ReplaceFields) => boolean>,
    default: null,
  },
  // 搜索完成时自动展开结果
  expandOnSearch: propTypes.bool.def(false),
  // 搜索完成自动选中所有结果,当且仅当 checkable===true 时生效
  checkOnSearch: propTypes.bool.def(false),
  // 搜索完成自动select所有结果
  selectedOnSearch: propTypes.bool.def(false),
};

export const treeNodeProps = {
  actionList: {
    type: Array as PropType<ActionItem[]>,
    default: () => [],
  },
  replaceFields: {
    type: Object as PropType<ReplaceFields>,
  },
  treeData: {
    type: Array as PropType<TreeDataItem[]>,
    default: () => [],
  },
};
