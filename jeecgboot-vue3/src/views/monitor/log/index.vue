<template>
  <BasicTable :ellipsis="true" @register="registerTable" :searchInfo="searchInfo" :columns="logColumns" :expand-column-width="16">
    <template #tableTitle>
      <div class="table-title-bar">
        <a-tabs defaultActiveKey="4" @change="tabChange" size="small">
          <a-tab-pane tab="异常日志" key="4"></a-tab-pane>
          <a-tab-pane tab="登录日志" key="1"></a-tab-pane>
          <a-tab-pane tab="操作日志" key="2"></a-tab-pane>
        </a-tabs>
        <span class="export-btn" v-if="searchInfo.logType == 2">
          <a-tooltip>
            <template #title>导出</template>
            <a-button  type="text" preIcon="ant-design:download-outlined" shape="circle" @click="onExportXls" />
          </a-tooltip>
        </span>
      </div>
    </template>
    <template #expandedRowRender="{ record }">
      <div v-if="searchInfo.logType == 2">
        <div style="margin-bottom: 5px">
          <a-badge status="success" style="vertical-align: middle" />
          <span style="vertical-align: middle">请求方法:{{ record.method }}</span></div
        >
        <div>
          <a-badge status="processing" style="vertical-align: middle" />
          <span style="vertical-align: middle">请求参数:{{ record.requestParam }}</span></div
        >
      </div>
      <div v-if="searchInfo.logType == 4">
        <div style="margin-bottom: 5px">
          <a-badge status="success" style="vertical-align: middle" />
          <span class="error-box" style="vertical-align: middle">异常堆栈:{{ record.requestParam }}</span>
        </div>
      </div>
    </template>
  </BasicTable>
</template>
<script lang="ts" name="monitor-log" setup>
  import { ref } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { getLogList, getExportUrl } from './log.api';
  import {
    columns,
    searchFormSchema,
    operationLogColumn,
    operationSearchFormSchema,
    exceptionColumns
  } from './log.data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useListPage } from '/@/hooks/system/useListPage';
  const { createMessage } = useMessage();
  const checkedKeys = ref<Array<string | number>>([]);

  const logColumns = ref<any>(exceptionColumns);
  const searchSchema = ref<any>(searchFormSchema);
  const searchInfo = { logType: '4' };
  // 列表页面公共参数、方法
  const { prefixCls, tableContext, onExportXls } = useListPage({
    designScope: 'user-list',
    tableProps: {
      title: '日志列表',
      api: getLogList,
      expandRowByClick: true,
      showActionColumn: false,
      rowSelection: {
        columnWidth: 20,
      },
      formConfig: {
        schemas: searchSchema,
        fieldMapToTime: [['fieldTime', ['createTime_begin', 'createTime_end'], 'YYYY-MM-DD']],
      },
    },
    exportConfig: {
      name:"操作日志",
      url: getExportUrl,
      params: searchInfo,
      timeout: 300000, // 设置超时时间为5分钟(300秒)
    },
  });

  const [registerTable, { reload }] = tableContext;

  // 日志类型
  function tabChange(key) {
    searchInfo.logType = key;
    // 代码逻辑说明: [VUEN-943]vue3日志管理列表翻译不对------------
    if (key == '2') {
      logColumns.value = operationLogColumn;
      searchSchema.value = operationSearchFormSchema;
    }else if(key == '4'){
      searchSchema.value = searchFormSchema;
      logColumns.value = exceptionColumns;
    } else {
      searchSchema.value = searchFormSchema;
      logColumns.value = columns;
    }
    reload();
  }

  /**
   * 选择事件
   */
  function onSelectChange(selectedRowKeys: (string | number)[]) {
    checkedKeys.value = selectedRowKeys;
  }
</script>
<style lang="less" scoped>
  .error-box {
    white-space: break-spaces;
  }
  .table-title-bar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
  }
  .export-btn {
    margin-left: auto;
  }
  :deep(.jeecg-basic-table-header__toolbar){
    width:100px !important;
  }
</style>
