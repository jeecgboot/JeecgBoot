<template>
  <div>
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <template #tableTitle>
        <a-button preIcon="ant-design:swap-outlined" type="primary" size="small" @click="handleCompare">数据比较</a-button>
        <span v-if="selectedRowKeys.length === 0" class="compare-tip">请勾选两条相同数据ID的记录</span>
        <a-tag v-else-if="selectedRowKeys.length === 1" color="warning" style="margin-left: 10px">再选一条相同数据ID的记录</a-tag>
        <a-tag v-else-if="selectedRowKeys.length === 2 && !isSameDataId" color="error" style="margin-left: 10px">数据ID不一致，无法比较</a-tag>
        <a-tag v-else-if="selectedRowKeys.length === 2 && isSameDataId" color="success" style="margin-left: 10px">可以比较 V{{ selectedRows[0]?.dataVersion }} vs V{{ selectedRows[1]?.dataVersion }}</a-tag>
        <a-tag v-else color="error" style="margin-left: 10px">只能选择两条记录</a-tag>
      </template>
    </BasicTable>
    <DataLogCompareModal @register="registerModal" @success="reload" />
  </div>
</template>
<script lang="ts" name="monitor-datalog" setup>
  import { computed } from 'vue';
  import { BasicTable } from '/@/components/Table';
  import DataLogCompareModal from './DataLogCompareModal.vue';
  import { getDataLogList } from './datalog.api';
  import { columns, searchFormSchema } from './datalog.data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useModal } from '/@/components/Modal';
  import { useListPage } from '/@/hooks/system/useListPage';
  import { getTableList } from '/@/api/common/api';

  const [registerModal, { openModal }] = useModal();
  const { createMessage } = useMessage();

  const { tableContext } = useListPage({
    designScope: 'datalog-template',
    tableProps: {
      title: '数据日志列表',
      api: getDataLogList,
      columns: columns,
      afterFetch: translateCreateBy,
      formConfig: {
        labelWidth: 80,
        schemas: searchFormSchema,
      },
      actionColumn: false,
    },
  });

  const [registerTable, { reload }, { rowSelection, selectedRowKeys, selectedRows }] = tableContext;

  const isSameDataId = computed(() => {
    const rows = selectedRows.value;
    if (!rows || rows.length !== 2) return false;
    return rows[0].dataId === rows[1].dataId;
  });

  /** 批量将创建人账号翻译为用户姓名 */
  async function translateCreateBy(records) {
    const usernames = [...new Set(records?.map((item) => item.createBy).filter(Boolean))];
    if (!usernames.length) {
      return records;
    }
    try {
      const userInfo = await getTableList({ username: usernames.join(','), isMultiTranslate: true });
      const realnameMap = new Map(userInfo?.records?.map((item) => [item.username, item.realname]));
      records.forEach((record) => {
        record.createBy = realnameMap.get(record.createBy) || record.createBy;
      });
    } catch (_error) {
      // 翻译失败时保留原账号，不影响日志列表展示
    }
    return records;
  }

  function handleCompare() {
    const rows = selectedRows.value;
    if (!rows || rows.length !== 2) {
      createMessage.warning('请选择两条数据进行比较！');
      return;
    }
    if (rows[0].dataId !== rows[1].dataId) {
      createMessage.warning('请选择相同数据ID的记录进行比较！');
      return;
    }
    openModal(true, {
      selectedRows: rows,
      isUpdate: true,
    });
  }
</script>
<style lang="less" scoped>
  .compare-tip {
    margin-left: 10px;
    font-size: 12px;
    color: #999;
  }
</style>
