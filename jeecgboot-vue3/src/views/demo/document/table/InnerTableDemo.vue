<template>
  <div class="p-4">
    <BasicTable @register="registerTable" class="components-table-demo-nested">
      <template #bodyCell="{ column }">
        <template v-if="column.key === 'operation'">
          <a>Publish</a>
        </template>
      </template>
      <template #expandedRowRender>
        <a-table :columns="innerColumns" :data-source="innerData" :pagination="false">
          <template #bodyCell="{ column }">
            <template v-if="column.dataIndex === 'state'">
              <span>
                <a-badge status="success" />
                Finished
              </span>
            </template>
            <template v-if="column.dataIndex === 'operation'">
              <span class="table-operation">
                <a>Pause</a>
                <a>Stop</a>
                <a-dropdown>
                  <template #overlay>
                    <a-menu>
                      <a-menu-item>Action 1</a-menu-item>
                      <a-menu-item>Action 2</a-menu-item>
                    </a-menu>
                  </template>
                  <a> More </a>
                </a-dropdown>
              </span>
            </template>
          </template>
        </a-table>
      </template>
    </BasicTable>
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicTable } from '/@/components/Table';
  import { useListPage } from '/@/hooks/system/useListPage';

  const columns = [
    { title: 'Name', dataIndex: 'name', key: 'name' },
    { title: 'Platform', dataIndex: 'platform', key: 'platform' },
    { title: 'Version', dataIndex: 'version', key: 'version' },
    { title: 'Upgraded', dataIndex: 'upgradeNum', key: 'upgradeNum' },
    { title: 'Creator', dataIndex: 'creator', key: 'creator' },
    { title: 'Date', dataIndex: 'createdAt', key: 'createdAt' },
    { title: 'Action', key: 'operation' },
  ];

  interface DataItem {
    key: number;
    name: string;
    platform: string;
    version: string;
    upgradeNum: number;
    creator: string;
    createdAt: string;
  }

  const data: DataItem[] = [];
  for (let i = 0; i < 3; ++i) {
    data.push({
      key: i,
      name: 'Screem',
      platform: 'iOS',
      version: '10.3.4.5654',
      upgradeNum: 500,
      creator: 'Jack',
      createdAt: '2014-12-24 23:12:00',
    });
  }

  const innerColumns = [
    { title: 'Date', dataIndex: 'date', key: 'date' },
    { title: 'Name', dataIndex: 'name', key: 'name' },
    { title: 'Status', dataIndex: 'state', key: 'state' },
    { title: 'Upgrade Status', dataIndex: 'upgradeNum', key: 'upgradeNum' },
    {
      title: 'Action',
      dataIndex: 'operation',
      key: 'operation',
    },
  ];

  interface innerDataItem {
    key: number;
    date: string;
    name: string;
    upgradeNum: string;
  }

  const innerData: innerDataItem[] = [];
  for (let i = 0; i < 3; ++i) {
    innerData.push({
      key: i,
      date: '2014-12-24 23:12:00',
      name: 'This is production name',
      upgradeNum: 'Upgraded: 56',
    });
  }

  export default defineComponent({
    components: { BasicTable },
    setup() {
      // 列表页面公共参数、方法
      const { tableContext } = useListPage({
        tableProps: {
          title: '内嵌表格',
          dataSource: data,
          columns: columns,
          showActionColumn: false,
          rowKey: 'key',
          useSearchForm: false,
        },
      });
      //注册table数据
      const [registerTable] = tableContext;
      return {
        data,
        columns,
        innerColumns,
        innerData,
        registerTable,
      };
    },
  });
</script>
