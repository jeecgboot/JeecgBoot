import type { Component, Ref, ComputedRef, ExtractPropTypes } from 'vue';
import type { VxeColumnProps } from 'vxe-table/types/column';
import type { JVxeComponent } from './JVxeComponent';
import type { VxeGridInstance, VxeTablePropTypes } from 'vxe-table';
import { JVxeTypes } from './JVxeTypes';
import { vxeProps } from '../vxe.data';
import { useMethods } from '../hooks/useMethods';
import { getJVxeAuths } from '../utils/authUtils';

export type JVxeTableProps = Partial<ExtractPropTypes<ReturnType<typeof vxeProps>>>;
export type JVxeTableMethods = ReturnType<typeof useMethods>['methods'];

export type JVxeVueComponent = {
  enhanced?: JVxeComponent.EnhancedPartial;
} & Component;

type statisticsTypes = 'sum' | 'average';

export type JVxeColumn = IJVxeColumn & Recordable;

/**
 * JVxe 列配置项
 */
export interface IJVxeColumn extends VxeColumnProps {
  type?: any;
  // 行唯一标识
  key: string;
  // 表单预期值的提示信息，可以使用${...}变量替换文本
  placeholder?: string;
  // 默认值
  defaultValue?: any;
  // 是否禁用当前列，默认false
  disabled?: boolean;
  // 校验规则 TODO 类型待定义
  validateRules?: any;
  // 联动下一级的字段key
  linkageKey?: string;
  // 自定义传入组件的其他属性
  props?: Recordable;
  allowClear?: boolean; // 允许清除
  // 【inputNumber】是否是统计列，只有 inputNumber 才能设置统计列。统计列：sum 求和；average 平均值
  statistics?: boolean | [statisticsTypes, statisticsTypes?];
  // 【select】
  dictCode?: string; // 字典 code
  options?: { title?: string; label?: string; text?: string; value: any; disabled?: boolean }[]; // 下拉选项列表
  allowInput?: boolean; // 允许输入
  allowSearch?: boolean; // 允许搜索
  // 【slot】
  slotName?: string; // 插槽名
  // 【checkbox】
  customValue?: [any, any]; // 自定义值
  defaultChecked?: boolean; // 默认选中
  // 【upload】 upload
  btnText?: string; // 上传按钮文字
  token?: boolean; // 是否传递 token
  responseName?: string; // 返回取值名称
  action?: string; // 上传地址
  allowRemove?: boolean; // 是否允许删除
  allowDownload?: boolean; // 是否允许下载
  // 【下拉字典搜索】
  dict?: string; // 字典表配置信息：数据库表名,显示字段名,存储字段名
  async?: boolean; // 是否同步模式
  tipsContent?: string;
  // 【popup】
  popupCode?: string;
  field?: string;
  orgFields?: string;
  destFields?: string;
}

export interface JVxeRefs {
  gridRef: Ref<VxeGridInstance | undefined>;
  subPopoverRef: Ref<any>;
  detailsModalRef: Ref<any>;
}

export interface JVxeDataProps {
  prefixCls: string;
  // vxe 实例ID
  caseId: string;
  // vxe 最终 columns
  vxeColumns?: ComputedRef;
  // vxe 最终 dataSource
  vxeDataSource: Ref<Recordable[]>;
  // 记录滚动条位置
  scroll: { top: number; left: number };
  // 当前是否正在滚动
  scrolling: Ref<boolean>;
  // vxe 默认配置
  defaultVxeProps: object;
  // 绑定左侧选择框
  selectedRows: Ref<any[]>;
  // 绑定左侧选择框已选择的id
  selectedRowIds: Ref<string[]>;
  disabledRowIds: string[];
  // 统计列配置
  statistics: {
    has: boolean;
    sum: string[];
    average: string[];
  };
  // 所有和当前表格相关的授权信息
  authsMap: Ref<Nullable<ReturnType<typeof getJVxeAuths>>>;
  // 内置 EditRules
  innerEditRules: Recordable<VxeTablePropTypes.EditRules[]>;
  // 联动下拉选项（用于隔离不同的下拉选项）
  // 内部联动配置，map
  innerLinkageConfig: Map<string, any>;
  // 开启了数据刷新效果的行
  reloadEffectRowKeysMap: Recordable;
}

export interface JVxeLinkageConfig {
  // 联动第一级的 key
  key: string;
  // 获取数据的方法
  requestData: (parent: string) => Promise<any>;
}

export { JVxeTypes };
