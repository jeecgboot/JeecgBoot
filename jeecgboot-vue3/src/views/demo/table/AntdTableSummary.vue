<template>
  <div class="p-4">
    <BasicTable @register="registerTable" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, h } from 'vue';
  import { BasicTable, useTable } from '/@/components/Table';
  import { demoListApi } from '/@/api/demo/table';
  import { BasicColumn } from '/@/components/Table/src/types/table';
  /** 
    相比原先的优势：
    1、Table 列头拖动时合计行不会错位;
    2、合计行使用TableSummary方式渲染；
    3、支持slot自定义合计行展示；
    4、列添加customSummaryRender自定义渲染函数
    5、能解决在非配置api情况下，列进行过滤时，合计行统计值未更新的问题
  */
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
            _index: '总计',
            age: totalAge,
            score: totalScore,
          },
        ];
      }
      const getBasicColumns = () :BasicColumn[] => {
        return [
          {
            title: 'ID',
            dataIndex: 'id',
            fixed: 'left',
            width: 200,
            resizable: true,
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
            resizable: true,
            customSummaryRender: ({ text }) => {
              // return <span style="color: red;">{text}</span>;
              return h('span', { style: { color: 'red' } }, [text]);
            },
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
        title: '原生总结栏（表尾合计）示例',
        api: demoListApi,
        rowSelection: { type: 'checkbox' },
        columns: getBasicColumns(),
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
