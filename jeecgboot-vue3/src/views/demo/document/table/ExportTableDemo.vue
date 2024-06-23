<template>
  <div class="p-4">
    <!--引用表格-->
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <template #tableTitle>
        <a-button type="primary" preIcon="ant-design:export-outlined" @click="onExportXls"> 导出</a-button>
        <j-upload-button type="primary" preIcon="ant-design:import-outlined" @click="onImportXls">导入</j-upload-button>
      </template>
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
  import { defHttp } from '/@/utils/http/axios';
  //定义表格列
  const columns: BasicColumn[] = [
    {
      title: '姓名',
      dataIndex: 'name',
      width: 170,
      align: 'left',
      resizable: true,
      sorter: {
        multiple: 1,
      },
    },
    {
      title: '关键词',
      dataIndex: 'keyWord',
      width: 130,
      resizable: true,
    },
    {
      title: '打卡时间',
      dataIndex: 'punchTime',
      width: 140,
      resizable: true,
    },
    {
      title: '工资',
      dataIndex: 'salaryMoney',
      width: 140,
      resizable: true,
      sorter: {
        multiple: 2,
      },
    },
    {
      title: '奖金',
      dataIndex: 'bonusMoney',
      width: 140,
      resizable: true,
    },
    {
      title: '性别',
      dataIndex: 'sex',
      sorter: {
        multiple: 3,
      },
      filters: [
        { text: '男', value: '1' },
        { text: '女', value: '2' },
      ],
      customRender: ({ record }) => {
        return record.sex ? (record.sex == '1' ? '男' : '女') : '';
      },
      width: 120,
      resizable: true,
    },
    {
      title: '生日',
      dataIndex: 'birthday',
      width: 120,
      resizable: true,
    },
    {
      title: '邮箱',
      dataIndex: 'email',
      width: 120,
      resizable: true,
    },
  ];

  //ajax请求api接口
  const demoListApi = (params) => {
    return defHttp.get({ url: '/test/jeecgDemo/list', params });
  };
  // 列表页面公共参数、方法
  const { tableContext, onExportXls, onImportXls } = useListPage({
    designScope: 'basic-table-demo-filter',
    tableProps: {
      title: '表单搜索',
      api: demoListApi,
      columns: columns,
      showActionColumn: false,
      useSearchForm: false,
    },
    exportConfig: {
      name: '示例列表',
      url: '/test/jeecgDemo/exportXls',
    },
    importConfig: {
      url: '/test/jeecgDemo/importExcel',
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
  }
</script>

<style scoped></style>
