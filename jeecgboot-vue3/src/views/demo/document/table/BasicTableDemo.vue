<template>
  <div class="p-4">
    <!--引用表格-->
    <BasicTable @register="registerTable">
      <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" />
      </template>
    </BasicTable>
  </div>
</template>

<script lang="ts" name="basic-table-demo" setup>
  import { ActionItem, BasicColumn, BasicTable, TableAction } from '/@/components/Table';
  import { useListPage } from '/@/hooks/system/useListPage';
  //定义表格列
  const columns: BasicColumn[] = [
    {
      title: '姓名',
      dataIndex: 'name',
      key: 'name',
      resizable: false,
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
      title: '用户列表',
      dataSource: [
        {
          key: '1',
          name: '胡歌',
          age: 32,
          address: '朝阳区林萃路1号',
        },
        {
          key: '2',
          name: '刘诗诗',
          age: 32,
          address: '昌平区白沙路1号',
        },
      ],
      columns: columns,
      size: 'large', //紧凑型表格 large
      striped: false, //斑马纹设置 false
      showActionColumn: true,
      useSearchForm: false,
    },
  });
  //注册table数据
  const [registerTable, methods] = tableContext;
  console.log('methods', methods);
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
  }
</script>

<style scoped></style>
