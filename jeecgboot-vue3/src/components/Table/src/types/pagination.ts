import Pagination from 'ant-design-vue/lib/pagination';
import { VNodeChild } from 'vue';

interface PaginationRenderProps {
  page: number;
  type: 'page' | 'prev' | 'next';
  originalElement: any;
}

export declare class PaginationConfig extends Pagination {
  position?: 'top' | 'bottom' | 'both';
}
export interface PaginationProps {
  /**
   * total number of data items
   * @default 0
   * @type number
   */
  total?: number;

  /**
   * default initial page number
   * @default 1
   * @type number
   */
  defaultCurrent?: number;

  /**
   * current page number
   * @type number
   */
  current?: number;

  /**
   * default number of data items per page
   * @default 10
   * @type number
   */
  defaultPageSize?: number;

  /**
   * number of data items per page
   * @type number
   */
  pageSize?: number;

  /**
   * Whether to hide pager on single page
   * @default false
   * @type boolean
   */
  hideOnSinglePage?: boolean;

  /**
   * determine whether pageSize can be changed
   * @default false
   * @type boolean
   */
  showSizeChanger?: boolean;

  /**
   * specify the sizeChanger options
   * @default ['10', '20', '30', '40']
   * @type string[]
   */
  pageSizeOptions?: string[];

  /**
   * determine whether you can jump to pages directly
   * @default false
   * @type boolean
   */
  showQuickJumper?: boolean | object;

  /**
   * to display the total number and range
   * @type Function
   */
  showTotal?: (total: number, range: [number, number]) => any;

  /**
   * specify the size of Pagination, can be set to small
   * @default ''
   * @type string
   */
  size?: string;

  /**
   * whether to setting simple mode
   * @type boolean
   */
  simple?: boolean;

  /**
   * to customize item innerHTML
   * @type Function
   */
  itemRender?: (props: PaginationRenderProps) => VNodeChild | JSX.Element;
}
