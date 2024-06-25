<template>
  <div class="p-4">
    <BasicTable @register="register">
      <template #toolbar>
        <a-button type="primary" @click="expandAll">展开全部</a-button>
        <a-button type="primary" @click="collapseAll">折叠全部</a-button>
      </template>
    </BasicTable>
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicTable, useTable } from '/@/components/Table';
  import { getBasicColumns, getTreeTableData } from './tableData';

  export default defineComponent({
    components: { BasicTable },
    setup() {
      const [register, { expandAll, collapseAll }] = useTable({
        title: '树形表格',
        isTreeTable: true,
        rowSelection: {
          type: 'checkbox',
          getCheckboxProps(record: Recordable) {
            // Demo: 第一行（id为0）的选择框禁用
            if (record.id === '0') {
              return { disabled: true };
            } else {
              return { disabled: false };
            }
          },
        },
        titleHelpMessage: '树形组件不能和序列号列同时存在',
        columns: getBasicColumns(),
        dataSource: getTreeTableData(),
        rowKey: 'id',
      });
      return { register, expandAll, collapseAll };
    },
  });
</script>
