import { VNodeChild } from 'vue';

export interface ColumnFilterItem {
  text?: string;
  value?: string;
  children?: any;
}

export declare type SortOrder = 'ascend' | 'descend';

export interface RecordProps<T> {
  text: any;
  record: T;
  index: number;
}

export interface FilterDropdownProps {
  prefixCls?: string;
  setSelectedKeys?: (selectedKeys: string[]) => void;
  selectedKeys?: string[];
  confirm?: () => void;
  clearFilters?: () => void;
  filters?: ColumnFilterItem[];
  getPopupContainer?: (triggerNode: HTMLElement) => HTMLElement;
  visible?: boolean;
}

export declare type CustomRenderFunction<T> = (record: RecordProps<T>) => VNodeChild | JSX.Element;

export interface ColumnProps<T> {
  /**
   * specify how content is aligned
   * @default 'left'
   * @type string
   */
  align?: 'left' | 'right' | 'center';

  /**
   * ellipsize cell content, not working with sorter and filters for now.
   * tableLayout would be fixed when ellipsis is true.
   * @default false
   * @type boolean
   */
  ellipsis?: boolean;

  /**
   * Span of this column's title
   * @type number
   */
  colSpan?: number;

  /**
   * Display field of the data record, could be set like a.b.c
   * @type string
   */
  dataIndex?: string;

  /**
   * Default filtered values
   * @type string[]
   */
  defaultFilteredValue?: string[];

  /**
   * Default order of sorted values: 'ascend' 'descend' null
   * @type string
   */
  defaultSortOrder?: SortOrder;

  /**
   * Customized filter overlay
   * @type any (slot)
   */
  filterDropdown?: VNodeChild | JSX.Element | ((props: FilterDropdownProps) => VNodeChild | JSX.Element);

  /**
   * Whether filterDropdown is visible
   * @type boolean
   */
  filterDropdownOpen?: boolean;

  /**
   * Whether the dataSource is filtered
   * @default false
   * @type boolean
   */
  filtered?: boolean;

  /**
   * Controlled filtered value, filter icon will highlight
   * @type string[]
   */
  filteredValue?: string[];

  /**
   * Customized filter icon
   * @default false
   * @type any
   */
  filterIcon?: boolean | VNodeChild | JSX.Element;

  /**
   * Whether multiple filters can be selected
   * @default true
   * @type boolean
   */
  filterMultiple?: boolean;

  /**
   * Filter menu config
   * @type object[]
   */
  filters?: ColumnFilterItem[];

  /**
   * Set column to be fixed: true(same as left) 'left' 'right'
   * @default false
   * @type boolean | string
   */
  fixed?: boolean | 'left' | 'right';

  /**
   * Unique key of this column, you can ignore this prop if you've set a unique dataIndex
   * @type string
   */
  key?: string;

  /**
   * Renderer of the table cell. The return value should be a VNode, or an object for colSpan/rowSpan config
   * @type Function | ScopedSlot
   */
  customRender?: CustomRenderFunction<T> | VNodeChild | JSX.Element;

  /**
   * Sort function for local sort, see Array.sort's compareFunction. If you need sort buttons only, set to true
   * @type boolean | Function
   */
  sorter?: boolean | Function;

  /**
   * Order of sorted values: 'ascend' 'descend' false
   * @type boolean | string
   */
  sortOrder?: boolean | SortOrder;

  /**
   * supported sort way, could be 'ascend', 'descend'
   * @default ['ascend', 'descend']
   * @type string[]
   */
  sortDirections?: SortOrder[];

  /**
   * Title of this column
   * @type any (string | slot)
   */
  title?: VNodeChild | JSX.Element;

  /**
   * Width of this column
   * @type string | number
   */
  width?: string | number;

  /**
   * Set props on per cell
   * @type Function
   */
  customCell?: (record: T, rowIndex: number) => object;

  /**
   * Set props on per header cell
   * @type object
   */
  customHeaderCell?: (column: ColumnProps<T>) => object;
  // update-begin--author:liaozhiyang---date:20240425---for：【pull/1201】添加antd的TableSummary功能兼容老的summary（表尾合计）
  customSummaryRender?: CustomRenderFunction<T> | VNodeChild | JSX.Element;
  // update-end--author:liaozhiyang---date:20240425---for：【pull/1201】添加antd的TableSummary功能兼容老的summary（表尾合计）

  /**
   * Callback executed when the confirm filter button is clicked, Use as a filter event when using template or jsx
   * @type Function
   */
  onFilter?: (value: any, record: T) => boolean;

  /**
   * Callback executed when filterDropdownOpen is changed, Use as a filterDropdownVisible event when using template or jsx
   * @type Function
   */
  onFilterDropdownVisibleChange?: (visible: boolean) => void;

  /**
   * When using columns, you can setting this property to configure the properties that support the slot,
   * such as slots: { filterIcon: 'XXX'}
   * @type object
   */
  slots?: Recordable<string>;
}
