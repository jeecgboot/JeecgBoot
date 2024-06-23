<template>
  <div class="p-4">
    <!--引用表格-->
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" />
      </template>
    </BasicTable>
  </div>
</template>

<script lang="ts" name="basic-table-demo" setup>
  import { BasicColumn, BasicTable, TableAction } from '/@/components/Table';
  import { useListPage } from '/@/hooks/system/useListPage';
  //定义表格列
  const columns: BasicColumn[] = [
    {
      title: '姓名',
      dataIndex: 'name',
      key: 'name',
      resizable: true,
    },
    {
      title: '年龄',
      dataIndex: 'age',
      key: 'age',
    },
    {
      title: '住址',
      dataIndex: 'address',
      key: 'address',
    },
  ];
  // 列表页面公共参数、方法
  const { tableContext } = useListPage({
    designScope: 'basic-table-demo',
    tableProps: {
      title: '可选择表格',
      dataSource: [
        {
          id: '1',
          name: '胡歌',
          age: 32,
          address: '朝阳区林萃路1号',
        },
        {
          id: '2',
          name: '刘诗诗',
          age: 32,
          address: '昌平区白沙路1号',
        },
      ],
      columns: columns,
      rowSelection: { type: 'checkbox' }, //默认是 checkbox 多选，可以设置成 radio 单选
      useSearchForm: false,
    },
  });
  //注册table数据
  const [registerTable, { reload }, { rowSelection, selectedRows, selectedRowKeys }] = tableContext;
  /**
   * 操作栏
   */
  function getTableAction(record): ActionItem[] {
    return [
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
      },
    ];
  }

  function handleEdit(record) {
    console.log(record);
    console.log(selectedRows.value);
    console.log(selectedRowKeys.value);
  }
</script>

<style scoped></style>
