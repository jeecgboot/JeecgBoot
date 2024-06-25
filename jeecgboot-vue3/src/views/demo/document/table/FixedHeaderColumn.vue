<template>
  <div class="p-4">
    <BasicTable @register="registerTable">
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              label: '删除',
              icon: 'ic:outline-delete-outline',
              onClick: handleDelete.bind(null, record),
            },
          ]"
          :dropDownActions="[
            {
              label: '启用',
              popConfirm: {
                title: '是否启用？',
                confirm: handleOpen.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicTable, useTable, BasicColumn, TableAction } from '/@/components/Table';

  import { demoListApi } from '/@/api/demo/table';
  import { useListPage } from '/@/hooks/system/useListPage';
  const columns: BasicColumn[] = [
    {
      title: 'ID',
      dataIndex: 'id',
      fixed: 'left',
      width: 280,
    },
    {
      title: '姓名',
      dataIndex: 'name',
      width: 260,
    },
    {
      title: '地址',
      dataIndex: 'address',
    },
    {
      title: '编号',
      dataIndex: 'no',
      width: 300,
    },
    {
      title: '开始时间',
      width: 200,
      dataIndex: 'beginTime',
    },
    {
      title: '结束时间',
      dataIndex: 'endTime',
      width: 200,
    },
  ];
  export default defineComponent({
    components: { BasicTable, TableAction },
    setup() {
      const { tableContext } = useListPage({
        tableProps: {
          title: '固定头和列示例',
          api: demoListApi,
          columns: columns,
          canResize: false,
          scroll: { y: 200 },
          actionColumn: {
            width: 160,
            title: 'Action',
            dataIndex: 'action',
          },
          useSearchForm: false,
        },
      });

      const [registerTable] = tableContext;
      function handleDelete(record: Recordable) {
        console.log('点击了删除', record);
      }
      function handleOpen(record: Recordable) {
        console.log('点击了启用', record);
      }
      return {
        registerTable,
        handleDelete,
        handleOpen,
      };
    },
  });
</script>
