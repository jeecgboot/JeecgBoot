<template>
  <div class="p-4">
    <BasicTable @register="registerTable" />
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicTable, useTable } from '/@/components/Table';
  import { BasicColumn } from '/@/components/Table/src/types/table';
  import { demoListApi } from '/@/api/demo/table';

  export default defineComponent({
    components: { BasicTable },
    setup() {
      function handleSummary(tableData: Recordable[]) {
        const totalAge = tableData.reduce((prev, next) => {
          prev += next.age;
          return prev;
        }, 0);
        const totalScore = tableData.reduce((prev, next) => {
          prev += next.score;
          return prev;
        }, 0);
        return [
          {
            _row: '合计',
            _index: '平均值',
            age: Math.round(totalAge / tableData.length),
            score: Math.round(totalScore / tableData.length),
          },
          {
            _row: '合计',
            _index: '平均值',
            age: totalAge,
            score: totalScore,
          },
        ];
      }
      const getBasicColumns = (): BasicColumn[] => {
        return [
          {
            title: 'ID',
            dataIndex: 'id',
            fixed: 'left',
            width: 200,
          },
          {
            title: '姓名',
            dataIndex: 'name',
            width: 150,
            filters: [
              { text: 'Male', value: 'male' },
              { text: 'Female', value: 'female' },
            ],
          },
          {
            title: '年龄',
            dataIndex: 'age',
            width: 100,
          },
          {
            title: '得分',
            dataIndex: 'score',
            width: 100,
            resizable: true,
          },
          {
            title: '地址',
            dataIndex: 'address',
            width: 300,
          },
          {
            title: '编号',
            dataIndex: 'no',
            width: 150,
            sorter: true,
            defaultHidden: true,
          },
          {
            title: '开始时间',
            width: 150,
            sorter: true,
            dataIndex: 'beginTime',
          },
          {
            title: '结束时间',
            width: 150,
            sorter: true,
            dataIndex: 'endTime',
          },
        ];
      };
      const [registerTable] = useTable({
        title: '表尾行合计示例',
        api: demoListApi,
        rowSelection: { type: 'checkbox' },
        columns: getBasicColumns(),
        // showSummary: true使用的是自定义的表尾行合计方式，如果不设置或者为false使用的antd
        showSummary: true,
        summaryFunc: handleSummary,
        scroll: { x: 1000 },
        canResize: false,
      });

      return {
        registerTable,
      };
    },
  });
</script>
