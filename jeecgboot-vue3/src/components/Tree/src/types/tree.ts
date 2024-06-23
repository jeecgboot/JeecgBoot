import type { ExtractPropTypes } from 'vue';
import type { TreeDataItem } from 'ant-design-vue/es/tree/Tree';

import { buildProps } from '/@/utils/props';

export enum ToolbarEnum {
  SELECT_ALL,
  UN_SELECT_ALL,
  EXPAND_ALL,
  UN_EXPAND_ALL,
  CHECK_STRICTLY,
  CHECK_UN_STRICTLY,
}

export const treeEmits = [
  'update:expandedKeys',
  'update:selectedKeys',
  'update:value',
  'change',
  'check',
  'search',
  'update:searchValue',
];

export interface TreeState {
  expandedKeys: KeyType[];
  selectedKeys: KeyType[];
  checkedKeys: CheckKeys;
  checkStrictly: boolean;
}

export interface FieldNames {
  children?: string;
  title?: string;
  key?: string;
}

export type KeyType = string | number;

export type CheckKeys =
  | KeyType[]
  | { checked: string[] | number[]; halfChecked: string[] | number[] };

export const treeProps = buildProps({
  value: {
    type: [Object, Array] as PropType<KeyType[] | CheckKeys>,
  },

  renderIcon: {
    type: Function as PropType<(params: Recordable) => string>,
  },

  helpMessage: {
    type: [String, Array] as PropType<string | string[]>,
    default: '',
  },

  title: {
    type: String,
    default: '',
  },
  toolbar: Boolean,
  search: Boolean,
  searchValue: {
    type: String,
    default: '',
  },
  checkStrictly: Boolean,
  clickRowToExpand: {
    type: Boolean,
    default: false,
  },
  checkable: Boolean,
  defaultExpandLevel: {
    type: [String, Number] as PropType<string | number>,
    default: '',
  },
  defaultExpandAll: Boolean,

  fieldNames: {
    type: Object as PropType<FieldNames>,
  },

  treeData: {
    type: Array as PropType<TreeDataItem[]>,
  },

  actionList: {
    type: Array as PropType<TreeActionItem[]>,
    default: () => [],
  },

  expandedKeys: {
    type: Array as PropType<KeyType[]>,
    default: () => [],
  },

  selectedKeys: {
    type: Array as PropType<KeyType[]>,
    default: () => [],
  },

  checkedKeys: {
    type: Array as PropType<CheckKeys>,
    default: () => [],
  },

  beforeRightClick: {
    type: Function as PropType<(...arg: any) => ContextMenuItem[] | ContextMenuOptions>,
    default: undefined,
  },

  rightMenuList: {
    type: Array as PropType<ContextMenuItem[]>,
  },
  // 自定义数据过滤判断方法(注: 不是整个过滤方法，而是内置过滤的判断方法，用于增强原本仅能通过title进行过滤的方式)
  filterFn: {
    type: Function as PropType<
      (searchValue: any, node: TreeItem, fieldNames: FieldNames) => boolean
    >,
    default: undefined,
  },
  // 高亮搜索值，仅高亮具体匹配值（通过title）值为true时使用默认色值，值为#xxx时使用此值替代且高亮开启
  highlight: {
    type: [Boolean, String] as PropType<Boolean | String>,
    default: false,
  },
  // 搜索完成时自动展开结果
  expandOnSearch: Boolean,
  // 搜索完成自动选中所有结果,当且仅当 checkable===true 时生效
  checkOnSearch: Boolean,
  // 搜索完成自动select所有结果
  selectedOnSearch: Boolean,
  loading: {
    type: Boolean,
    default: false,
  },
});

export type TreeProps = ExtractPropTypes<typeof treeProps>;

export interface ContextMenuItem {
  label: string;
  icon?: string;
  hidden?: boolean;
  disabled?: boolean;
  handler?: Fn;
  divider?: boolean;
  children?: ContextMenuItem[];
}

export interface ContextMenuOptions {
  icon?: string;
  styles?: any;
  items?: ContextMenuItem[];
}

export interface TreeItem extends TreeDataItem {
  icon?: any;
}

export interface TreeActionItem {
  render: (record: Recordable) => any;
  show?: boolean | ((record: Recordable) => boolean);
}

export interface InsertNodeParams {
  parentKey: string | null;
  node: TreeDataItem;
  list?: TreeDataItem[];
  push?: 'push' | 'unshift';
}

export interface TreeActionType {
  checkAll: (checkAll: boolean) => void;
  expandAll: (expandAll: boolean) => void;
  setExpandedKeys: (keys: KeyType[]) => void;
  getExpandedKeys: () => KeyType[];
  setSelectedKeys: (keys: KeyType[]) => void;
  getSelectedKeys: () => KeyType[];
  setCheckedKeys: (keys: CheckKeys) => void;
  getCheckedKeys: () => CheckKeys;
  filterByLevel: (level: number) => void;
  insertNodeByKey: (opt: InsertNodeParams) => void;
  insertNodesByKey: (opt: InsertNodeParams) => void;
  deleteNodeByKey: (key: string) => void;
  updateNodeByKey: (key: string, node: Omit<TreeDataItem, 'key'>) => void;
  setSearchValue: (value: string) => void;
  getSearchValue: () => string;
  getSelectedNode: (
    key: KeyType,
    treeList?: TreeItem[],
    selectNode?: TreeItem | null,
  ) => TreeItem | null;
}
