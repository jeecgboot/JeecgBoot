<template>
  <!--引用表格-->
  <div class="p-4">
    <BasicTable @register="registerTable">
      <template #bodyCell="{ column, text }">
        <template v-if="column.dataIndex === 'name'">
          <a>{{ text }}</a>
        </template>
      </template>
      <template #footer>页脚</template>
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
      width: 300,
    },
    {
      title: '年龄',
      dataIndex: 'age',
      key: 'age',
      width: 300,
    },
    {
      title: '住址',
      dataIndex: 'address',
      key: 'address',
      ellipsis: true,
    },
    {
      title: '长内容列',
      dataIndex: 'address',
      key: 'address 2',
      ellipsis: true,
    },
    {
      title: '长内容列',
      dataIndex: 'address',
      key: 'address 3',
      ellipsis: true,
    },
  ];
  // 列表页面公共参数、方法
  const { tableContext } = useListPage({
    designScope: 'basic-table-demo',
    tableProps: {
      title: '边框表格',
      dataSource: [
        {
          key: '1',
          name: '张三',
          age: 32,
          address: '中国北京北京市朝阳区大屯路科学院南里1号楼3单元401',
        },
        {
          key: '2',
          name: '刘思',
          age: 32,
          address: '中国北京北京市昌平区顺沙路尚湖世家2号楼7单元503',
        },
      ],
      columns: columns,
      showActionColumn: false,
      useSearchForm: false,
    },
  });
  //注册table数据
  const [registerTable] = tableContext;
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
