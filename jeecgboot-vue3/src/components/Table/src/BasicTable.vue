<template>
  <div ref="wrapRef" :class="getWrapperClass">
    <BasicForm
      :class="{ 'table-search-area-hidden': !getBindValues.formConfig?.schemas?.length }"
      submitOnReset
      v-bind="getFormProps"
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
            <!-- update-begin--author:liaozhiyang---date:20240424---for：【issues/1146】BasicTable使用headerCell全选框出不来 -->
            <template v-if="item === 'headerCell'">
              <CustomSelectHeader v-if="isCustomSelection(data.column)" v-bind="selectHeaderProps" />
              <slot v-else :name="item" v-bind="data || {}"></slot>
            </template>
            <slot v-else :name="item" v-bind="data || {}"></slot>
            <!-- update-begin--author:liaozhiyang---date:20240424---for：【issues/1146】BasicTable使用headerCell全选框出不来 -->
          </template>
          <template #headerCell="{ column }">
            <!-- update-begin--author:sunjianlei---date:220230630---for：【QQYUN-5571】自封装选择列，解决数据行选择卡顿问题 -->
            <CustomSelectHeader v-if="isCustomSelection(column)" v-bind="selectHeaderProps"/>
            <HeaderCell v-else :column="column" />
            <!-- update-end--author:sunjianlei---date:220230630---for：【QQYUN-5571】自封装选择列，解决数据行选择卡顿问题 -->
          </template>
          <!-- 增加对antdv3.x兼容 -->
          <template #bodyCell="data">
            <!-- update-begin--author:liaozhiyang---date:220230717---for：【issues-179】antd3 一些警告以及报错(针对表格) -->
            <!-- update-begin--author:liusq---date:20230921---for：【issues/770】slotsBak异常报错的问题,增加判断column是否存在 -->
            <template v-if="data.column?.slotsBak?.customRender">
            <!-- update-end--author:liusq---date:20230921---for：【issues/770】slotsBak异常报错的问题,增加判断column是否存在 -->
              <slot :name="data.column.slotsBak.customRender" v-bind="data || {}"></slot>
            </template>
            <template v-else>
              <slot name="bodyCell" v-bind="data || {}"></slot>
            </template>
            <!-- update-begin--author:liaozhiyang---date:22030717---for：【issues-179】antd3 一些警告以及报错(针对表格) -->
          </template>
          <!-- update-begin--author:liaozhiyang---date:20240425---for：【pull/1201】添加antd的TableSummary功能兼容老的summary（表尾合计） -->
          <template v-if="showSummaryRef && !getBindValues.showSummary" #summary="data">
            <slot name="summary" v-bind="data || {}">
              <TableSummary :data="data || {}" v-bind="getSummaryProps" />
            </slot>
          </template>
          <!-- update-end--author:liaozhiyang---date:20240425---for：【pull/1201】添加antd的TableSummary功能兼容老的summary（表尾合计） -->
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

      // update-begin--author:sunjianlei---date:220230630---for：【QQYUN-5571】自封装选择列，解决数据行选择卡顿问题

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
      // update-end--author:sunjianlei---date:220230630---for：【QQYUN-5571】自封装选择列，解决数据行选择卡顿问题

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

      const { getViewColumns, getColumns, setCacheColumnsByField, setColumns, getColumnsRef, getCacheColumns } = useColumns(
        getProps,
        getPaginationInfo,
        // update-begin--author:sunjianlei---date:220230630---for：【QQYUN-5571】自封装选择列，解决数据行选择卡顿问题
        handleCustomSelectColumn,
        // update-end--author:sunjianlei---date:220230630---for：【QQYUN-5571】自封装选择列，解决数据行选择卡顿问题
      );

      const { getScrollRef, redoHeight } = useTableScroll(getProps, tableElRef, getColumnsRef, getRowSelectionRef, getDataSourceRef);

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
      // update-begin--author:liaozhiyang---date:20240425---for：【pull/1201】添加antd的TableSummary功能兼容老的summary（表尾合计）
      const getSummaryProps = computed(() => {
        return pick(unref(getProps), ['summaryFunc', 'summaryData', 'hasExpandedRow', 'rowKey']);
      });
      const getIsEmptyData = computed(() => {
        return (unref(getDataSourceRef) || []).length === 0;
      });
      const showSummaryRef = computed(() => {
        const summaryProps = unref(getSummaryProps);
        return (summaryProps.summaryFunc || summaryProps.summaryData) && !unref(getIsEmptyData);
      });
      // update-end--author:liaozhiyang---date:20240425---for：【pull/1201】添加antd的TableSummary功能兼容老的summary（表尾合计）

      const { getFooterProps } = useTableFooter(getProps, slots, getScrollRef, tableElRef, getDataSourceRef);

      const { getFormProps, replaceFormSlotKey, getFormSlotKeys, handleSearchInfoChange } = useTableForm(getProps, slots, fetch, getLoading);

      const getBindValues = computed(() => {
        const dataSource = unref(getDataSourceRef);
        let propsData: Recordable = {
          // ...(dataSource.length === 0 ? { getPopupContainer: () => document.body } : {}),
          ...attrs,
          customRow,
          //树列表展开使用AntDesignVue默认的加减图标 author:scott date:20210914
          //expandIcon: slots.expandIcon ? null : expandIcon(),
          ...unref(getProps),
          ...unref(getHeaderProps),
          scroll: unref(getScrollRef),
          loading: unref(getLoading),
          tableLayout: 'fixed',
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

        //update-begin---author:wangshuai ---date:20230214  for：[QQYUN-4237]代码生成 内嵌子表模式 没有滚动条------------
        //额外的展开行存在插槽时会将滚动移除掉,注释掉
        /*if (slots.expandedRowRender) {
          propsData = omit(propsData, 'scroll');
        }*/
        //update-end---author:wangshuai ---date:20230214  for：[QQYUN-4237]代码生成 内嵌子表模式 没有滚动条------------ 

        // update-begin--author:sunjianlei---date:220230630---for：【QQYUN-5571】自封装选择列，解决数据行选择卡顿问题
        // 自定义选择列，需要去掉原生的
        delete propsData.rowSelection
        // update-end--author:sunjianlei---date:220230630---for：【QQYUN-5571】自封装选择列，解决数据行选择卡顿问题

        // update-begin--author:liaozhiyang---date:20230919---for：【QQYUN-6387】展开写法（去掉报错）
        !propsData.isTreeTable && delete propsData.expandIconColumnIndex;
        propsData.expandedRowKeys === null && delete propsData.expandedRowKeys;
        // update-end--author:liaozhiyang---date:20230919---for：【QQYUN-6387】展开写法（去掉报错）
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
      };
      createTableContext({ ...tableAction, wrapRef, getBindValues });

      // update-begin--author:sunjianlei---date:220230718---for：【issues/179】兼容新老slots写法，移除控制台警告
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
      // update-end--author:sunjianlei---date:220230718---for：【issues/179】兼容新老slots写法，移除控制台警告
      // update-begin--author:liaozhiyang---date:20231226---for：【issues/945】BasicTable组件设置默认展开不生效
      nextTick(() => {
        getProps.value.defaultExpandAllRows && expandAll();
      })
      // update-end--author:sunjianlei---date:20231226---for：【issues/945】BasicTable组件设置默认展开不生效
      expose(tableAction);

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
        console.log('col',col);
          col.width = w;
        },
        getFormProps: getFormProps as any,
        replaceFormSlotKey,
        getFormSlotKeys,
        getWrapperClass,
        getMaxColumnWidth,
        columns: getViewColumns,

        // update-begin--author:sunjianlei---date:220230630---for：【QQYUN-5571】自封装选择列，解决数据行选择卡顿问题
        selectHeaderProps,
        isCustomSelection,
        // update-end--author:sunjianlei---date:220230630---for：【QQYUN-5571】自封装选择列，解决数据行选择卡顿问题
        slotNamesGroup,
        // update-begin--author:liaozhiyang---date:20240425---for：【pull/1201】添加antd的TableSummary功能兼容老的summary（表尾合计）
        getSummaryProps,
        showSummaryRef,
        // update-end--author:liaozhiyang---date:20240425---for：【pull/1201】添加antd的TableSummary功能兼容老的summary（表尾合计）
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
    // update-begin--author:liaozhiyang---date:20240613---for：【TV360X-1232】查询区域隐藏后点击刷新不走请求了(采用css隐藏)
    > .table-search-area-hidden {
      display: none;
    }
    // update-end--author:liaozhiyang---date:20240613---for：【TV360X-1232】查询区域隐藏后点击刷新不走请求了(采用css隐藏)
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

    //update-begin-author:liusq---date:20230517--for: [issues/526]RangePicker 设置预设范围按钮样式问题---
    .ant-picker-preset {
      .ant-tag {
        margin-right: 8px !important;
      }
    }
    //update-end-author:liusq---date:20230517--for: [issues/526]RangePicker 设置预设范围按钮样式问题---

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

    // update-begin--author:sunjianlei---date:220230718---for：【issues/622】修复表尾合计错位的问题
    &--show-summary {
      .ant-table > .ant-table-footer {
        padding: 12px 0 0;
      }

      .ant-table.ant-table-bordered > .ant-table-footer {
        border: 0;
      }
    }
    // update-end--author:sunjianlei---date:220230718---for：【issues/622】修复表尾合计错位的问题
    // update-begin--author:liaozhiyang---date:20240604---for：【TV360X-377】关联记录必填影响到了table的输入框和页码样式
    > .ant-form-item {
      margin-bottom: 0;
    }
    // update-end--author:liaozhiyang---date:20240604---for：【TV360X-377】关联记录必填影响到了table的输入框和页码样式
  }
</style>
