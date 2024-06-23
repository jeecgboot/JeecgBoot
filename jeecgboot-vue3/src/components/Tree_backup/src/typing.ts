import type { TreeDataItem, CheckEvent as CheckEventOrigin } from 'ant-design-vue/es/tree/Tree';
import { ContextMenuItem } from '/@/hooks/web/useContextMenu';

export interface ActionItem {
  render: (record: Recordable) => any;
  show?: boolean | ((record: Recordable) => boolean);
}

export interface TreeItem extends TreeDataItem {
  icon?: any;
}

export interface ReplaceFields {
  children?: string;
  title?: string;
  key?: string;
}

export type Keys = (string | number)[];
export type CheckKeys = (string | number)[] | { checked: (string | number)[]; halfChecked: (string | number)[] };

export interface TreeActionType {
  checkAll: (checkAll: boolean) => void;
  expandAll: (expandAll: boolean) => void;
  setExpandedKeys: (keys: Keys) => void;
  getExpandedKeys: () => Keys;
  setSelectedKeys: (keys: Keys) => void;
  getSelectedKeys: () => Keys;
  setCheckedKeys: (keys: CheckKeys) => void;
  getCheckedKeys: () => CheckKeys;
  filterByLevel: (level: number) => void;
  insertNodeByKey: (opt: InsertNodeParams) => void;
  insertNodesByKey: (opt: InsertNodeParams) => void;
  deleteNodeByKey: (key: string) => void;
  updateNodeByKey: (key: string, node: Omit<TreeDataItem, 'key'>) => void;
  setSearchValue: (value: string) => void;
  getSearchValue: () => string;
}

export interface InsertNodeParams {
  parentKey: string | null;
  node: TreeDataItem;
  list?: TreeDataItem[];
  push?: 'push' | 'unshift';
}

export interface ContextMenuOptions {
  icon?: string;
  styles?: any;
  items?: ContextMenuItem[];
}

export type CheckEvent = CheckEventOrigin;
