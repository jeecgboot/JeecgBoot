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
      title: '名称',
      dataIndex: 'name',
      customCell: (record, index, column) => ({
        colSpan: index < 4 ? 1 : 5,
      }),
      customRender: ({ text, record, index, column }) => {
        return index < 4 ? text : `${record.name}/${record.age}/${record.address}/${record.phone}`;
      },
    },
    {
      title: '年龄',
      dataIndex: 'age',
      customCell: (record, index, column) => {
        if (index == 4) {
          return { colSpan: 0 };
        }
      },
    },
    {
      title: '家庭住址',
      dataIndex: 'address',
      customCell: (record, index, column) => {
        if (index == 4) {
          return { colSpan: 0 };
        }
      },
    },
    {
      title: '联系电话',
      colSpan: 2,
      dataIndex: 'tel',
      customCell: (record, index, column) => {
        if (index === 2) {
          return { rowSpan: 2 };
        }
        if (index === 3) {
          return { rowSpan: 0 };
        }
        if (index === 4) {
          return { colSpan: 0 };
        }
      },
    },
    {
      title: 'Phone',
      colSpan: 0,
      dataIndex: 'phone',
      customCell: (record, index, column) => {
        if (index === 4) {
          return { colSpan: 0 };
        }
      },
    },
  ];
  // 列表页面公共参数、方法
  const { tableContext } = useListPage({
    designScope: 'basic-table-demo',
    tableProps: {
      title: '合并行列',
      dataSource: [
        {
          key: '1',
          name: '尹嘉乐',
          age: 32,
          tel: '0319-5972018',
          phone: 17600080009,
          address: '北京市昌平区',
        },
        {
          key: '2',
          name: '龙佳钰',
          tel: '0319-5972018',
          phone: 17600060007,
          age: 42,
          address: '北京市海淀区',
        },
        {
          key: '3',
          name: '贺泽惠',
          age: 32,
          tel: '0319-5972018',
          phone: 17600040005,
          address: '北京市门头沟区',
        },
        {
          key: '4',
          name: '沈勇',
          age: 18,
          tel: '0319-5972018',
          phone: 17600010003,
          address: '北京市朝阳区',
        },
        {
          key: '5',
          name: '白佳毅',
          age: 18,
          tel: '0319-5972018',
          phone: 17600010002,
          address: '北京市丰台区',
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
