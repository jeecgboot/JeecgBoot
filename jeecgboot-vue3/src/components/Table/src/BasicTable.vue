<template>
  <div ref="wrapRef" :class="getWrapperClass">
    <BasicForm
      :class="{ 'table-search-area-hidden': !getBindValues.formConfig?.schemas?.length }"
      submitOnReset
      v-bind="getFormProps"
      source="table-query"
      v-if="getBindValues.useSearchForm"
      :tableAction="tableAction"
      @register="registerForm"
      @submit="handleSearchInfoChange"
      @advanced-change="redoHeight"
    >
      <template #[replaceFormSlotKey(item)]="data" v-for="item in getFormSlotKeys">
        <slot :name="item" v-bind="data || {}"></slot>
      </template>
    </BasicForm>

    <!-- antd v3 升级兼容，阻止数据的收集，防止控制台报错 -->
    <!-- https://antdv.com/docs/vue/migration-v3-cn -->
    <a-form-item-rest>
      <!-- 【TV360X-377】关联记录必填影响到了table的输入框和页码样式 -->
      <a-form-item>
        <Table ref="tableElRef" v-bind="getBindValues" :rowClassName="getRowClassName" v-show="getEmptyDataIsShowTable" @resizeColumn="handleResizeColumn" @change="handleTableChange">
          <!-- antd的原生插槽直接传递 -->
          <template #[item]="data" v-for="item in slotNamesGroup.native" :key="item">
            <!-- 代码逻辑说明: 【issues/1146】BasicTable使用headerCell全选框出不来 -->
            <template v-if="item === 'headerCell'">
              <CustomSelectHeader v-if="isCustomSelection(data.column)" v-bind="selectHeaderProps" />
              <slot v-else :name="item" v-bind="data || {}"></slot>
            </template>
            <slot v-else :name="item" v-bind="data || {}"></slot>
          </template>
          <template #headerCell="{ column }">
            <!-- 代码逻辑说明: 【QQYUN-5571】自封装选择列，解决数据行选择卡顿问题 -->
            <CustomSelectHeader v-if="isCustomSelection(column)" v-bind="selectHeaderProps"/>
            <HeaderCell v-else :column="column" />
          </template>
          <!-- 增加对antdv3.x兼容 -->
          <template #bodyCell="data">
            <template v-if="data.column?.slotsBak?.customRender">
              <slot :name="data.column.slotsBak.customRender" v-bind="data || {}"></slot>
            </template>
            <template v-else>
              <slot name="bodyCell" v-bind="data || {}"></slot>
            </template>
          </template>
          <template v-if="showSummaryRef && !getBindValues.showSummary" #summary="data">
            <slot name="summary" v-bind="data || {}">
              <TableSummary :data="data || {}" v-bind="getSummaryProps" />
            </slot>
          </template>
        </Table>
      </a-form-item>
    </a-form-item-rest>
  </div>
</template>
<script lang="ts">
  import type { BasicTableProps, TableActionType, SizeType, ColumnChangeParam, BasicColumn } from './types/table';

  import { defineComponent, ref, computed, unref, toRaw, inject, watchEffect, watch, onUnmounted, onMounted, nextTick } from 'vue';
  import { Table } from 'ant-design-vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { PageWrapperFixedHeightKey } from '/@/components/Page/injectionKey';
  import CustomSelectHeader from './components/CustomSelectHeader.vue'
  import expandIcon from './components/ExpandIcon';
  import HeaderCell from './components/HeaderCell.vue';
  import TableSummary from './components/TableSummary';
  import { InnerHandlers } from './types/table';
  import { usePagination } from './hooks/usePagination';
  import { useColumns } from './hooks/useColumns';
  import { useDataSource } from './hooks/useDataSource';
  import { useLoading } from './hooks/useLoading';
  import { useRowSelection } from './hooks/useRowSelection';
  import { useTableScroll } from './hooks/useTableScroll';
  import { useCustomRow } from './hooks/useCustomRow';
  import { useTableStyle } from './hooks/useTableStyle';
  import { useTableHeader } from './hooks/useTableHeader';
  import { useTableExpand } from './hooks/useTableExpand';
  import { createTableContext } from './hooks/useTableContext';
  import { useTableFooter } from './hooks/useTableFooter';
  import { useTableForm } from './hooks/useTableForm';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useCustomSelection } from "./hooks/useCustomSelection";

  import { omit, pick } from 'lodash-es';
  import { basicProps } from './props';
  import { isFunction } from '/@/utils/is';
  import { warn } from '/@/utils/log';

  export default defineComponent({
    components: {
      Table,
      BasicForm,
      HeaderCell,
      TableSummary,
      CustomSelectHeader,
    },
    props: basicProps,
    emits: [
      'fetch-success',
      'fetch-error',
      'selection-change',
      'register',
      'row-click',
      'row-dbClick',
      'row-contextmenu',
      'row-mouseenter',
      'row-mouseleave',
      'edit-end',
      'edit-cancel',
      'edit-row-end',
      'edit-change',
      'expanded-rows-change',
      'change',
      'columns-change',
      'table-redo',
    ],
    setup(props, { attrs, emit, slots, expose }) {
      const tableElRef = ref(null);
      const tableData = ref<Recordable[]>([]);

      const wrapRef = ref(null);
      const innerPropsRef = ref<Partial<BasicTableProps>>();

      const { prefixCls } = useDesign('basic-table');
      const [registerForm, formActions] = useForm();

      const getProps = computed(() => {
        return { ...props, ...unref(innerPropsRef) } as BasicTableProps;
      });

      const isFixedHeightPage = inject(PageWrapperFixedHeightKey, false);
      watchEffect(() => {
        unref(isFixedHeightPage) &&
          props.canResize &&
          warn("'canResize' of BasicTable may not work in PageWrapper with 'fixedHeight' (especially in hot updates)");
      });

      const { getLoading, setLoading } = useLoading(getProps);
      const { getPaginationInfo, getPagination, setPagination, setShowPagination, getShowPagination } = usePagination(getProps);


      // const { getRowSelection, getRowSelectionRef, getSelectRows, clearSelectedRowKeys, getSelectRowKeys, deleteSelectRowByKey, setSelectedRowKeys } =
      //   useRowSelection(getProps, tableData, emit);

      // 子级列名
      const childrenColumnName = computed(() => getProps.value.childrenColumnName || 'children');

      // 自定义选择列
      const {
        getRowSelection,
        getSelectRows,
        getSelectRowKeys,
        setSelectedRowKeys,
        getRowSelectionRef,
        selectHeaderProps,
        isCustomSelection,
        handleCustomSelectColumn,
        clearSelectedRowKeys,
        deleteSelectRowByKey,
        getExpandIconColumnIndex,
      } = useCustomSelection(
        getProps,
        emit,
        wrapRef,
        getPaginationInfo,
        tableData,
        childrenColumnName
      )

      const {
        handleTableChange: onTableChange,
        getDataSourceRef,
        getDataSource,
        getRawDataSource,
        setTableData,
        updateTableDataRecord,
        deleteTableDataRecord,
        insertTableDataRecord,
        findTableDataRecord,
        fetch,
        getRowKey,
        reload,
        getAutoCreateKey,
        updateTableData,
      } = useDataSource(
        getProps,
        {
          tableData,
          getPaginationInfo,
          setLoading,
          setPagination,
          validate: formActions.validate,
          clearSelectedRowKeys,
        },
        emit
      );

      function handleTableChange(...args) {
        onTableChange.call(undefined, ...args);
        emit('change', ...args);
        // 解决通过useTable注册onChange时不起作用的问题
        const { onChange } = unref(getProps);
        onChange && isFunction(onChange) && onChange.call(undefined, ...args);
      }

      const { getViewColumns, getColumns, getRefColumns, setCacheColumnsByField, setColumns, getColumnsRef, getCacheColumns } = useColumns(
        getProps,
        getPaginationInfo,
        // 代码逻辑说明: 【QQYUN-5571】自封装选择列，解决数据行选择卡顿问题
        handleCustomSelectColumn,
      );

      const { getScrollRef, redoHeight } = useTableScroll(getProps, tableElRef, getColumnsRef, getRowSelectionRef, getDataSourceRef, slots, getPaginationInfo);

      const { customRow } = useCustomRow(getProps, {
        setSelectedRowKeys,
        getSelectRowKeys,
        clearSelectedRowKeys,
        getAutoCreateKey,
        emit,
      });

      const { getRowClassName } = useTableStyle(getProps, prefixCls);

      const { getExpandOption, expandAll, collapseAll } = useTableExpand(getProps, tableData, emit);

      const handlers: InnerHandlers = {
        onColumnsChange: (data: ColumnChangeParam[]) => {
          emit('columns-change', data);
          // support useTable
          unref(getProps).onColumnsChange?.(data);
        },
      };

      const { getHeaderProps } = useTableHeader(getProps, slots, handlers);
      // 代码逻辑说明: 【pull/1201】添加antd的TableSummary功能兼容老的summary（表尾合计）
      const getSummaryProps = computed(() => {
        // 代码逻辑说明: 【issues/7956】修复showSummary: false时且有内嵌子表时合计栏错位
        const result = pick(unref(getProps), ['summaryFunc', 'summaryData', 'hasExpandedRow', 'rowKey']);
        result['hasExpandedRow'] = Object.keys(slots).includes('expandedRowRender');
        return result;
      });
      const getIsEmptyData = computed(() => {
        return (unref(getDataSourceRef) || []).length === 0;
      });
      const showSummaryRef = computed(() => {
        const summaryProps = unref(getSummaryProps);
        return (summaryProps.summaryFunc || summaryProps.summaryData) && !unref(getIsEmptyData);
      });

      const { getFooterProps } = useTableFooter(getProps, slots, getScrollRef, tableElRef, getDataSourceRef);

      const { getFormProps, replaceFormSlotKey, getFormSlotKeys, handleSearchInfoChange } = useTableForm(getProps, slots, fetch, getLoading);

      const getBindValues = computed(() => {
        const dataSource = unref(getDataSourceRef);
        let propsData: Recordable = {
          // date-begin--author:liaozhiyang---date:20250716---for：【issues/8564】basicTale的TableLayout换成auto不生效
          tableLayout: 'fixed',
          // date-begin--author:liaozhiyang---date:20250716---for：【issues/8564】basicTale的TableLayout换成auto不生效
          // ...(dataSource.length === 0 ? { getPopupContainer: () => document.body } : {}),
          ...attrs,
          customRow,
          //树列表展开使用AntDesignVue默认的加减图标 author:scott date:20210914
          //expandIcon: slots.expandIcon ? null : expandIcon(),
          ...unref(getProps),
          ...unref(getHeaderProps),
          scroll: unref(getScrollRef),
          loading: unref(getLoading),
          rowSelection: unref(getRowSelectionRef),
          rowKey: unref(getRowKey),
          columns: toRaw(unref(getViewColumns)),
          pagination: toRaw(unref(getPaginationInfo)),
          dataSource,
          footer: unref(getFooterProps),
          ...unref(getExpandOption),
          // 【QQYUN-5837】动态计算 expandIconColumnIndex
          expandIconColumnIndex: getExpandIconColumnIndex.value,
        };

        //额外的展开行存在插槽时会将滚动移除掉,注释掉
        /*if (slots.expandedRowRender) {
          propsData = omit(propsData, 'scroll');
        }*/

        // 自定义选择列，需要去掉原生的
        delete propsData.rowSelection

        // 代码逻辑说明: 【QQYUN-6387】展开写法（去掉报错）
        !propsData.isTreeTable && delete propsData.expandIconColumnIndex;
        propsData.expandedRowKeys === null && delete propsData.expandedRowKeys;
        propsData = omit(propsData, ['class', 'onChange']);
        return propsData;
      });

      // 统一设置表格列宽度
      const getMaxColumnWidth = computed(() => {
        const values = unref(getBindValues);
        return values.maxColumnWidth > 0 ? values.maxColumnWidth + 'px' : null;
      });

      const getWrapperClass = computed(() => {
        const values = unref(getBindValues);
        return [
          prefixCls,
          attrs.class,
          {
            [`${prefixCls}-form-container`]: values.useSearchForm,
            [`${prefixCls}--inset`]: values.inset,
            [`${prefixCls}-col-max-width`]: getMaxColumnWidth.value != null,
            // 是否显示表尾合计
            [`${prefixCls}--show-summary`]: values.showSummary,
          },
        ];
      });

      const getEmptyDataIsShowTable = computed(() => {
        const { emptyDataIsShowTable, useSearchForm } = unref(getProps);
        if (emptyDataIsShowTable || !useSearchForm) {
          return true;
        }
        return !!unref(getDataSourceRef).length;
      });

      function setProps(props: Partial<BasicTableProps>) {
        innerPropsRef.value = { ...unref(innerPropsRef), ...props };
      }

      const tableAction: TableActionType = {
        reload,
        getSelectRows,
        clearSelectedRowKeys,
        getSelectRowKeys,
        deleteSelectRowByKey,
        setPagination,
        setTableData,
        updateTableDataRecord,
        deleteTableDataRecord,
        insertTableDataRecord,
        findTableDataRecord,
        redoHeight,
        setSelectedRowKeys,
        setColumns,
        setLoading,
        getDataSource,
        getRawDataSource,
        setProps,
        getRowSelection,
        getPaginationRef: getPagination,
        getColumns,
        // 代码逻辑说明: 【issues/8529】setColumns后列配置没联动更新
        getColumnsRef: () => getColumnsRef,
        getCacheColumns,
        emit,
        updateTableData,
        setShowPagination,
        getShowPagination,
        setCacheColumnsByField,
        expandAll,
        collapseAll,
        getSize: () => {
          return unref(getBindValues).size as SizeType;
        },
        // 代码逻辑说明: 【QQYUN-13558】erp风格主表在5条数据时也有滚动条
        getBindValuesRef: () => getBindValues,
      };
      createTableContext({ ...tableAction, wrapRef, getBindValues });

      // 获取分组之后的slot名称
      const slotNamesGroup = computed<{
        // AntTable原生插槽
        native: string[];
        // 列自定义插槽
        custom: string[];
      }>(() => {
        const native: string[] = [];
        const custom: string[] = [];
        const columns = unref<Recordable[]>(getViewColumns) as BasicColumn[];
        const allCustomRender = columns.map<string>((column) => column.slotsBak?.customRender);
        for (const name of Object.keys(slots)) {
          // 过滤特殊的插槽
          if (['bodyCell'].includes(name)) {
            continue;
          }
          if (allCustomRender.includes(name)) {
            custom.push(name);
          } else {
            native.push(name);
          }
        }
        return { native, custom };
      });
      // 代码逻辑说明: 【issues/945】BasicTable组件设置默认展开不生效
      nextTick(() => {
        getProps.value.defaultExpandAllRows && expandAll();
      })
      // 代码逻辑说明: 【issues/7588】选择后自动刷新表格
      expose({ ...tableAction, handleSearchInfoChange });

      emit('register', tableAction, formActions);


      return {
        tableElRef,
        getBindValues,
        getLoading,
        registerForm,
        handleSearchInfoChange,
        getEmptyDataIsShowTable,
        handleTableChange,
        getRowClassName,
        wrapRef,
        tableAction,
        redoHeight,
        handleResizeColumn: (w, col) => {
          // 代码逻辑说明: 【issues/7101】列配置resizable: true时，表尾合计的列宽没有同步改变
          const columns = getColumns();
          const findItem = columns.find((item) => {
            if (item['dataIndex'] != null) {
              return item['dataIndex'] === col['dataIndex'];
            } else if (item['flag'] != null) {
              return item['flag'] === col['flag'];
            }
            return false;
          });
          if (findItem) {
            findItem.width = w;
            setColumns(columns);
          }
          console.log('col',col);
          col.width = w;
        },
        getFormProps: getFormProps as any,
        replaceFormSlotKey,
        getFormSlotKeys,
        getWrapperClass,
        getMaxColumnWidth,
        columns: getViewColumns,

        selectHeaderProps,
        isCustomSelection,
        slotNamesGroup,
        getSummaryProps,
        showSummaryRef,
      };
    },
  });
</script>
<style lang="less">
  @border-color: #cecece4d;

  @prefix-cls: ~'@{namespace}-basic-table';

  [data-theme='dark'] {
    .ant-table-tbody > tr:hover.ant-table-row-selected > td,
    .ant-table-tbody > tr.ant-table-row-selected td {
      background-color: #262626;
    }

    .@{prefix-cls} {
      //表格选择工具栏样式
      .alert {
        // background-color: #323232;
        // border-color: #424242;
      }
    }
  }

  .@{prefix-cls} {
    max-width: 100%;

    &-row__striped {
      td {
        background-color: @app-content-background;
      }
    }
    // 代码逻辑说明: 【TV360X-1232】查询区域隐藏后点击刷新不走请求了(采用css隐藏)
    > .table-search-area-hidden {
      display: none;
    }
    &-form-container {
      padding: 10px;

      .ant-form {
        padding: 12px 10px 6px 10px;
        margin-bottom: 8px;
        background-color: @component-background;
        border-radius: 2px;
      }
    }

    .ant-tag {
      margin-right: 0;
    }

    // 代码逻辑说明: [issues/526]RangePicker 设置预设范围按钮样式问题---
    .ant-picker-preset {
      .ant-tag {
        margin-right: 8px !important;
      }
    }

    .ant-table-wrapper {
      padding: 6px;
      background-color: @component-background;
      border-radius: 2px;

      .ant-table-title {
        min-height: 40px;
        padding: 0 0 8px 0 !important;
      }

      .ant-table.ant-table-bordered .ant-table-title {
        border: none !important;
      }
    }

    .ant-table {
      width: 100%;
      overflow-x: hidden;

      &-title {
        display: flex;
        padding: 8px 6px;
        border-bottom: none;
        justify-content: space-between;
        align-items: center;
      }
      //定义行颜色
      .trcolor {
        background-color: rgba(255, 192, 203, 0.31);
        color: red;
      }

      //.ant-table-tbody > tr.ant-table-row-selected td {
      //background-color: fade(@primary-color, 8%) !important;
      //}
    }

    .ant-pagination {
      margin: 10px 0 0 0;
    }

    .ant-table-footer {
      padding: 0;

      .ant-table-wrapper {
        padding: 0;
      }

      table {
        border: none !important;
      }

      .ant-table-content {
        overflow-x: hidden !important;
        //  overflow-y: scroll !important;
      }

      td {
        padding: 12px 8px;
      }
    }
    //表格选择工具栏样式
    .alert {
      height: 38px;
      // background-color: #e6f7ff;
      // border-color: #91d5ff;
    }
    &--inset {
      .ant-table-wrapper {
        padding: 0;
      }
    }

    // ------ 统一设置表格列最大宽度 ------
    &-col-max-width {
      .ant-table-thead tr th,
      .ant-table-tbody tr td {
        max-width: v-bind(getMaxColumnWidth);
      }
    }
    // ------ 统一设置表格列最大宽度 ------

    // 代码逻辑说明: 【issues/622】修复表尾合计错位的问题
    &--show-summary {
      .ant-table > .ant-table-footer {
        padding: 12px 0 0;
      }
      .ant-table > .ant-table-footer {
        // 代码逻辑说明: 【issues/7413】合计行有点对不齐
        padding-left: 0 !important;
        padding-right: 0 !important;
      }
      .ant-table.ant-table-bordered > .ant-table-footer {
        border: 0;
      }
    }
    // 代码逻辑说明: 【TV360X-377】关联记录必填影响到了table的输入框和页码样式
    > .ant-form-item {
      margin-bottom: 0;
    }
  }
</style>
