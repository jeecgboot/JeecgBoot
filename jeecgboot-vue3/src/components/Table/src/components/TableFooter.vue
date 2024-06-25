<template>
  <Table
    v-if="summaryFunc || summaryData"
    :showHeader="false"
    :bordered="bordered"
    :pagination="false"
    :dataSource="getDataSource"
    :rowKey="(r) => r[rowKey]"
    :columns="getColumns"
    tableLayout="fixed"
    :scroll="scroll"
  />
</template>
<script lang="ts">
  import type { PropType } from 'vue';
  import { defineComponent, unref, computed, toRaw } from 'vue';
  import { Table } from 'ant-design-vue';
  import { cloneDeep } from 'lodash-es';
  import { isFunction } from '/@/utils/is';
  import type { BasicColumn } from '../types/table';
  import { INDEX_COLUMN_FLAG } from '../const';
  import { propTypes } from '/@/utils/propTypes';
  import { useTableContext } from '../hooks/useTableContext';

  const SUMMARY_ROW_KEY = '_row';
  const SUMMARY_INDEX_KEY = '_index';
  export default defineComponent({
    name: 'BasicTableFooter',
    components: { Table },
    props: {
      bordered: {
        type: Boolean,
        default: false,
      },
      summaryFunc: {
        type: Function as PropType<Fn>,
      },
      summaryData: {
        type: Array as PropType<Recordable[]>,
      },
      scroll: {
        type: Object as PropType<Recordable>,
      },
      rowKey: propTypes.string.def('key'),
      // 是否有展开列
      hasExpandedRow: propTypes.bool,
    },
    setup(props) {
      const table = useTableContext();

      const getDataSource = computed((): Recordable[] => {
        const { summaryFunc, summaryData } = props;
        if (summaryData?.length) {
          summaryData.forEach((item, i) => (item[props.rowKey] = `${i}`));
          return summaryData;
        }
        if (!isFunction(summaryFunc)) {
          return [];
        }
        // update-begin--author:liaozhiyang---date:20230227---for：【QQYUN-8172】可编辑单元格编辑完以后不更新合计值
        let dataSource = cloneDeep(unref(table.getDataSource()));
        // update-end--author:liaozhiyang---date:20230227---for：【QQYUN-8172】可编辑单元格编辑完以后不更新合计值
        dataSource = summaryFunc(dataSource);
        dataSource.forEach((item, i) => {
          item[props.rowKey] = `${i}`;
        });
        return dataSource;
      });

      const getColumns = computed(() => {
        const dataSource = unref(getDataSource);
        let columns: BasicColumn[] = cloneDeep(table.getColumns());
        // update-begin--author:liaozhiyang---date:220230804---for：【issues/638】表格合计，列自定义隐藏或展示时，合计栏会错位
        columns = columns.filter((item) => !item.defaultHidden);
        // update-begin--author:liaozhiyang---date:220230804---for：【issues/638】表格合计，列自定义隐藏或展示时，合计栏会错位
        const index = columns.findIndex((item) => item.flag === INDEX_COLUMN_FLAG);
        const hasRowSummary = dataSource.some((item) => Reflect.has(item, SUMMARY_ROW_KEY));
        const hasIndexSummary = dataSource.some((item) => Reflect.has(item, SUMMARY_INDEX_KEY));

        // 是否有序号列
        let hasIndexCol = false;
        // 是否有选择列
        let hasSelection = table.getRowSelection() && hasRowSummary

        if (index !== -1) {
          if (hasIndexSummary) {
            hasIndexCol = true;
            columns[index].customRender = ({ record }) => record[SUMMARY_INDEX_KEY];
            columns[index].ellipsis = false;
          } else {
            Reflect.deleteProperty(columns[index], 'customRender');
          }
        }

        if (hasSelection) {
          // update-begin--author:liaozhiyang---date:20231009---for：【issues/776】显示100条/页，复选框只能显示3个的问题(fixed也有可能设置true)
          const isFixed = columns.some((col) => col.fixed === 'left' || col.fixed === true);
          // update-begin--author:liaozhiyang---date:20231009---for：【issues/776】显示100条/页，复选框只能显示3个的问题(fixed也有可能设置true)
          columns.unshift({
            width: 50,
            title: 'selection',
            key: 'selectionKey',
            align: 'center',
            ...(isFixed ? { fixed: 'left' } : {}),
            customRender: ({ record }) => hasIndexCol ? '' : record[SUMMARY_ROW_KEY],
          });
        }

        if (props.hasExpandedRow) {
          const isFixed = columns.some((col) => col.fixed === 'left');
          columns.unshift({
            width: 50,
            title: 'expandedRow',
            key: 'expandedRowKey',
            align: 'center',
            ...(isFixed ? { fixed: 'left' } : {}),
            customRender: () => '',
          });
        }
        return columns;
      });
      return { getColumns, getDataSource };
    },
  });
</script>
<style lang="less" scoped>
  // update-begin--author:liaozhiyang---date:20231009---for：【issues/776】显示100条/页，复选框只能显示3个的问题(隐藏合计的滚动条)
  .ant-table-wrapper {
    :deep(.ant-table-body) {
      overflow-x: hidden !important;
    }
  }
  // update-end--author:liaozhiyang---date:20231009---for：【issues/776】显示100条/页，复选框只能显示3个的问题(隐藏合计的滚动条)
</style>
