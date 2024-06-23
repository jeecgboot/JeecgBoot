<template>
  <div class="p-4">
    <BasicTable @register="registerTable" />
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicColumn, BasicTable, useTable } from '/@/components/Table';

  import { demoListApi } from '/@/api/demo/table';
  //计算合并表头

  export default defineComponent({
    components: { BasicTable },
    setup() {
      const [registerTable] = useTable({
        title: '分组表头示例',
        api: demoListApi,
        columns: getMergeHeaderColumns(),
        bordered: true,
        useSearchForm: false,
      });

      function getMergeHeaderColumns(): BasicColumn[] {
        return [
          {
            title: 'ID',
            dataIndex: 'id',
            width: 300,
          },
          {
            title: '姓名',
            dataIndex: 'name',
            width: 300,
          },
          {
            title: '地址',
            width: 120,
            children: [
              {
                title: '地址',
                dataIndex: 'address',
                key: 'address',
                width: 200,
              },
              {
                title: '编号',
                dataIndex: 'no',
                key: 'no',
              },
            ],
          },
          {
            title: '开始时间',
            dataIndex: 'beginTime',
            width: 200,
          },
          {
            title: '结束时间',
            dataIndex: 'endTime',
            width: 200,
          },
        ];
      }
      return {
        registerTable,
      };
    },
  });
</script>
