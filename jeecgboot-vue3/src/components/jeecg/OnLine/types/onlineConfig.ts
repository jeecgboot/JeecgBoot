interface ScopedSlots {
  customRender: string;
}

interface HrefSlots {
  // 链接地址
  href: string;
  // fieldHref_字段名
  slotName: string;
}

interface OnlineColumn {
  dataIndex?: string;
  title?: string;
  key?: string;
  fieldType?: string;
  width?: number | string;
  align?: string;
  sorter?: string | boolean;
  isTotal?: string | number | boolean;
  groupTitle?: string;
  // 超链的时候 和HrefSlots中的slotName匹配
  scopedSlots?: ScopedSlots;
  // 一般用于字典 字典传过来的是字典编码字符串 后转函数
  customRender?: string | Function;
  // 这个类型不知道有什么用
  hrefSlotName?: string;
  showLength?: number | string;
  children?: OnlineColumn[];
  sortOrder?: string;
  // 插槽对应控件类型(列表)
  slots?: ScopedSlots;
  //超过宽度将自动省略，暂不支持和排序筛选一起使用。
  ellipsis?: boolean;
  // 是否固定列
  fixed?: boolean | 'left' | 'right';
  //字段类型 int/string 
  dbType?:string;
  //他表字段用
  linkField?:string;
}

export { OnlineColumn, HrefSlots };
