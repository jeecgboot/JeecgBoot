import type { BasicColumn, BasicTableProps, CellFormat, GetColumnsParams } from '../types/table';
import type { PaginationProps } from '../types/pagination';
import type { ComputedRef } from 'vue';
import { Table } from 'ant-design-vue';
import { computed, Ref, ref, toRaw, unref, watch, reactive } from 'vue';
import { renderEditCell } from '../components/editable';
import { usePermission } from '/@/hooks/web/usePermission';
import { useI18n } from '/@/hooks/web/useI18n';
import { isArray, isBoolean, isFunction, isMap, isString } from '/@/utils/is';
import { cloneDeep, isEqual } from 'lodash-es';
import { formatToDate } from '/@/utils/dateUtil';
import { ACTION_COLUMN_FLAG, DEFAULT_ALIGN, INDEX_COLUMN_FLAG, PAGE_SIZE } from '../const';
import { CUS_SEL_COLUMN_KEY } from './useCustomSelection';

function handleItem(item: BasicColumn, ellipsis: boolean) {
  const { key, dataIndex, children } = item;
  item.align = item.align || DEFAULT_ALIGN;
  if (ellipsis) {
    if (!key) {
      item.key = dataIndex;
    }
    if (!isBoolean(item.ellipsis)) {
      Object.assign(item, {
        ellipsis,
      });
    }
  }
  if (children && children.length) {
    handleChildren(children, !!ellipsis);
  }
}

function handleChildren(children: BasicColumn[] | undefined, ellipsis: boolean) {
  if (!children) return;
  children.forEach((item) => {
    const { children } = item;
    handleItem(item, ellipsis);
    handleChildren(children, ellipsis);
  });
}

function handleIndexColumn(propsRef: ComputedRef<BasicTableProps>, getPaginationRef: ComputedRef<boolean | PaginationProps>, columns: BasicColumn[]) {
  const { t } = useI18n();

  const { showIndexColumn, indexColumnProps, isTreeTable } = unref(propsRef);

  let pushIndexColumns = false;
  if (unref(isTreeTable)) {
    return;
  }
  columns.forEach(() => {
    const indIndex = columns.findIndex((column) => column.flag === INDEX_COLUMN_FLAG);
    if (showIndexColumn) {
      pushIndexColumns = indIndex === -1;
    } else if (!showIndexColumn && indIndex !== -1) {
      columns.splice(indIndex, 1);
    }
  });
  // 代码逻辑说明: 【TV360X-105】列展示设置问题[列展示复选框不应该判断序号列复选框的状态]
  if (columns.length === 0 && showIndexColumn) {
    const indIndex = columns.findIndex((column) => column.flag === INDEX_COLUMN_FLAG);
    if (indIndex === -1) {
      pushIndexColumns = true;
    }
  }
  if (!pushIndexColumns) return;

  const isFixedLeft = columns.some((item) => item.fixed === 'left');

  columns.unshift({
    flag: INDEX_COLUMN_FLAG,
    // 代码逻辑说明: 【TV360X-1634】密度是宽松模式时，序号列表头换行了
    width: propsRef.value.size === 'large' ? 65 : 50,
    title: t('component.table.index'),
    align: 'center',
    customRender: ({ index }) => {
      const getPagination = unref(getPaginationRef);
      if (isBoolean(getPagination)) {
        return `${index + 1}`;
      }
      const { current = 1, pageSize = PAGE_SIZE } = getPagination;
      return ((current < 1 ? 1 : current) - 1) * pageSize + index + 1;
    },
    ...(isFixedLeft
      ? {
          fixed: 'left',
        }
      : {}),
    ...indexColumnProps,
  });
}

function handleActionColumn(propsRef: ComputedRef<BasicTableProps>, columns: BasicColumn[]) {
  const { actionColumn, showActionColumn } = unref(propsRef);
  if (!actionColumn || !showActionColumn) return;

  const hasIndex = columns.findIndex((column) => column.flag === ACTION_COLUMN_FLAG);
  if (hasIndex === -1) {
    columns.push({
      ...columns[hasIndex],
      ...actionColumn,
      flag: ACTION_COLUMN_FLAG,
    });
  }
}

export function useColumns(
  propsRef: ComputedRef<BasicTableProps>,
  getPaginationRef: ComputedRef<boolean | PaginationProps>,
  handleCustomSelectColumn: Fn
) {
  const columnsRef = ref(unref(propsRef).columns) as unknown as Ref<BasicColumn[]>;
  let cacheColumns = unref(propsRef).columns;

  const getColumnsRef = computed(() => {
    const columns = cloneDeep(unref(columnsRef));
    // 代码逻辑说明: 【issues/6908】多语言无刷新切换时，BasicColumn和FormSchema里面的值不能正常切换
    if (isArray(columns)) {
      columns.forEach((item) => {
        item.title = isFunction(item.title) ? item.title() : item.title;
      });
    }
    handleIndexColumn(propsRef, getPaginationRef, columns);
    handleActionColumn(propsRef, columns);
    // 代码逻辑说明: 【QQYUN-5571】自封装选择列，解决数据行选择卡顿问题
    handleCustomSelectColumn(columns);

    if (!columns) {
      return [];
    }
    const { ellipsis } = unref(propsRef);

    columns.forEach((item) => {
      const { customRender, slots } = item;

      handleItem(item, Reflect.has(item, 'ellipsis') ? !!item.ellipsis : !!ellipsis && !customRender && !slots);
    });
    return columns;
  });

  function isIfShow(column: BasicColumn): boolean {
    const ifShow = column.ifShow;

    let isIfShow = true;

    if (isBoolean(ifShow)) {
      isIfShow = ifShow;
    }
    if (isFunction(ifShow)) {
      isIfShow = ifShow(column);
    }
    return isIfShow;
  }
  const { hasPermission } = usePermission();

  const getViewColumns = computed(() => {
    const viewColumns = sortFixedColumn(unref(getColumnsRef));

    const columns = cloneDeep(viewColumns);
    const formatEditColumn = (columns) => {
      return columns.map((column) => {
        // 代码逻辑说明: 【issues-179】antd3 一些警告以及报错(针对表格)
        if(column.slots?.customRender) {
          // slots的备份，兼容老的写法，转成新写法避免控制台警告
          column.slotsBak = column.slots;
          delete column.slots;
        }

        const { slots, customRender, format, edit, editRow, flag, title: metaTitle } = column;

        if (!slots || !slots?.title) {
          // column.slots = { title: `header-${dataIndex}`, ...(slots || {}) };
          column.customTitle = column.title as string;
          Reflect.deleteProperty(column, 'title');
        }
        // 代码逻辑说明: 【online报表】分组标题显示错误，都显示成了联系信息 LOWCOD-2343
        if (column.children) {
          column.title = metaTitle;
        }

        const isDefaultAction = [INDEX_COLUMN_FLAG, ACTION_COLUMN_FLAG].includes(flag!);
        if (!customRender && format && !edit && !isDefaultAction) {
          column.customRender = ({ text, record, index }) => {
            return formatCell(text, format, record, index);
          };
        }

        // edit table
        if ((edit || editRow) && !isDefaultAction) {
          column.customRender = renderEditCell(column);
        }
        // 代码逻辑说明: 【pull/7333】修复分组表头可编辑表格失效问题
        if (column.children?.length) {
          formatEditColumn(column.children.filter((item) => hasPermission(column.auth) && isIfShow(column)));
        }
        return reactive(column);
      });
    };
    // 代码逻辑说明: 【pull/7333】修复分组表头可编辑表格失效问题
    const result = formatEditColumn(columns.filter((item) => hasPermission(item.auth) && isIfShow(item)));
    // 代码逻辑说明: 【QQYUN-6387】展开写法（去掉报错）
    if (propsRef.value.expandedRowKeys && !propsRef.value.isTreeTable) {
      let index = 0;
      const findIndex = result.findIndex((item) => item.key === CUS_SEL_COLUMN_KEY);
      if (findIndex != -1) {
        index = findIndex + 1;
      }
      const next: any = result[index + 1];
      let expand = Table.EXPAND_COLUMN;
      if (next && (next['fixed'] == true || next['fixed'] == 'left')) {
        expand = Object.assign(expand, { fixed: 'left' });
      }
      result.splice(index, 0, expand);
    }
    return result;
  });

  watch(
    () => unref(propsRef).columns,
    (columns) => {
      columnsRef.value = columns;
      cacheColumns = columns?.filter((item) => !item.flag) ?? [];
    }
  );

  function setCacheColumnsByField(dataIndex: string | undefined, value: Partial<BasicColumn>) {
    if (!dataIndex || !value) {
      return;
    }
    cacheColumns.forEach((item) => {
      if (item.dataIndex === dataIndex) {
        Object.assign(item, value);
        return;
      }
    });
  }

  /**
   * set columns
   * @param columnList key｜column
   */
  function setColumns(columnList: Partial<BasicColumn>[] | (string | string[])[]) {
    const columns = cloneDeep(columnList);
    if (!isArray(columns)) return;

    if (columns.length <= 0) {
      columnsRef.value = [];
      return;
    }

    const firstColumn = columns[0];

    const cacheKeys = cacheColumns.map((item) => item.dataIndex);

    if (!isString(firstColumn) && !isArray(firstColumn)) {
      columnsRef.value = columns as BasicColumn[];
    } else {
      const columnKeys = (columns as (string | string[])[]).map((m) => m.toString());
      const newColumns: BasicColumn[] = [];
      cacheColumns.forEach((item) => {
        newColumns.push({
          ...item,
          defaultHidden: !columnKeys.includes(item.dataIndex?.toString() || (item.key as string)),
        });
      });
      // Sort according to another array
      if (!isEqual(cacheKeys, columns)) {
        newColumns.sort((prev, next) => {
          return columnKeys.indexOf(prev.dataIndex?.toString() as string) - columnKeys.indexOf(next.dataIndex?.toString() as string);
        });
      }
      columnsRef.value = newColumns;
    }
  }

  function getColumns(opt?: GetColumnsParams) {
    const { ignoreIndex, ignoreAction, ignoreAuth, ignoreIfShow, sort } = opt || {};
    let columns = toRaw(unref(getColumnsRef));
    if (ignoreIndex) {
      columns = columns.filter((item) => item.flag !== INDEX_COLUMN_FLAG);
    }
    if (ignoreAction) {
      columns = columns.filter((item) => item.flag !== ACTION_COLUMN_FLAG);
    }
    // 过滤自定义选择列
    columns = columns.filter((item) => item.key !== CUS_SEL_COLUMN_KEY);
    // 代码逻辑说明: 【issues/8502】解决权限列在列表中不显示，列配置中还显示
    if (ignoreAuth) {
      columns = columns.filter((item) => {
        if (item.auth) {
          return hasPermission(item.auth);
        }
        return true;
      });
    }
    if (ignoreIfShow) {
      columns = columns.filter((item) => {
        if (isBoolean(item.ifShow)) {
          return item.ifShow;
        }
        if (isFunction(item.ifShow)) {
          return item.ifShow(item);
        }
        return true;
      });
    }
    if (sort) {
      columns = sortFixedColumn(columns);
    }

    return columns;
  }
  function getCacheColumns() {
    return cacheColumns;
  }

  return {
    getColumnsRef,
    getCacheColumns,
    getColumns,
    setColumns,
    getViewColumns,
    setCacheColumnsByField,
  };
}

function sortFixedColumn(columns: BasicColumn[]) {
  const fixedLeftColumns: BasicColumn[] = [];
  const fixedRightColumns: BasicColumn[] = [];
  const defColumns: BasicColumn[] = [];
  for (const column of columns) {
    if (column.fixed === 'left') {
      fixedLeftColumns.push(column);
      continue;
    }
    if (column.fixed === 'right') {
      fixedRightColumns.push(column);
      continue;
    }
    defColumns.push(column);
  }
  return [...fixedLeftColumns, ...defColumns, ...fixedRightColumns].filter((item) => !item.defaultHidden);
}

// format cell
export function formatCell(text: string, format: CellFormat, record: Recordable, index: number) {
  if (!format) {
    return text;
  }

  // custom function
  if (isFunction(format)) {
    return format(text, record, index);
  }

  try {
    // date type
    const DATE_FORMAT_PREFIX = 'date|';
    if (isString(format) && format.startsWith(DATE_FORMAT_PREFIX)) {
      const dateFormat = format.replace(DATE_FORMAT_PREFIX, '');

      if (!dateFormat) {
        return text;
      }
      return formatToDate(text, dateFormat);
    }

    // Map
    if (isMap(format)) {
      return format.get(text);
    }
  } catch (error) {
    return text;
  }
}

