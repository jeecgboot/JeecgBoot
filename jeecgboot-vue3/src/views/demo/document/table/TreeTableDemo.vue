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
  import { BasicColumn, BasicTable } from '/@/components/Table';
  import { useListPage } from '/@/hooks/system/useListPage';
  const columns: BasicColumn[] = [
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
  export default defineComponent({
    components: { BasicTable },
    setup() {
      const { tableContext } = useListPage({
        tableProps: {
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
          columns: columns,
          dataSource: getTreeTableData(),
          rowKey: 'id',
          useSearchForm: false,
        },
      });
      //注册table数据
      const [register, { expandAll, collapseAll }] = tableContext;
      function getTreeTableData() {
        const data: any = (() => {
          const arr: any = [];
          for (let index = 0; index < 40; index++) {
            arr.push({
              id: `${index}`,
              name: 'John Brown',
              age: `1${index}`,
              no: `${index + 10}`,
              address: 'New York No. 1 Lake ParkNew York No. 1 Lake Park',
              beginTime: new Date().toLocaleString(),
              endTime: new Date().toLocaleString(),
              children: [
                {
                  id: `l2-${index}`,
                  name: 'John Brown',
                  age: `1${index}`,
                  no: `${index + 10}`,
                  address: 'New York No. 1 Lake ParkNew York No. 1 Lake Park',
                  beginTime: new Date().toLocaleString(),
                  endTime: new Date().toLocaleString(),
                },
                {
                  id: `l3-${index}`,
                  name: 'John Mary',
                  age: `1${index}`,
                  no: `${index + 10}`,
                  address: 'New York No. 1 Lake ParkNew York No. 1 Lake Park',
                  beginTime: new Date().toLocaleString(),
                  endTime: new Date().toLocaleString(),
                },
              ],
            });
          }
          return arr;
        })();

        return data;
      }
      return { register, expandAll, collapseAll };
    },
  });
</script>
