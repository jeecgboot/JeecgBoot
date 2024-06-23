<!--回收站-->
<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="用户回收站" :showOkBtn="false" width="1000px" destroyOnClose>
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <!--插槽:table标题-->
      <template #tableTitle>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="batchHandleDelete">
                <Icon icon="ant-design:delete-outlined"></Icon>
                批量删除
              </a-menu-item>
              <a-menu-item key="1" @click="batchHandleRevert">
                <Icon icon="ant-design:redo-outlined"></Icon>
                批量还原
              </a-menu-item>
            </a-menu>
          </template>
          <a-button
            >批量操作
            <Icon icon="ant-design:down-outlined"></Icon>
          </a-button>
        </a-dropdown>
      </template>
      <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" />
      </template>
    </BasicTable>
  </BasicModal>
</template>

<script lang="ts" setup name="tenant-recycle-bin-modal">
  import { BasicTable, TableAction } from '/@/components/Table';
  import { recycleBinPageList, deleteLogicDeleted, revertTenantLogic } from '../tenant.api';
  import { recycleColumns, searchRecycleFormSchema } from '../tenant.data';
  import { useListPage } from '/@/hooks/system/useListPage';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { Modal } from 'ant-design-vue';
  import { toRaw, unref } from 'vue';

  const { createMessage: $message } = useMessage();
  const [registerModal] = useModalInner(() => {});
  // 列表页面公共参数、方法
  const { prefixCls, tableContext, onExportXls, onImportXls } = useListPage({
    tableProps: {
      api: recycleBinPageList,
      columns: recycleColumns,
      size: 'small',
      formConfig: {
        schemas: searchRecycleFormSchema,
      },
      actionColumn: {
        width: 120,
      },
      ellipsis: true,
    },
  });
  const emit = defineEmits(['success', 'register']);
  //注册table数据
  const [registerTable, { reload, updateTableDataRecord }, { rowSelection, selectedRows, selectedRowKeys }] = tableContext;

  //获取操作栏事件
  function getTableAction(record) {
    return [
      {
        label: '还原',
        icon: 'ant-design:redo-outlined',
        popConfirm: {
          title: '是否确认还原',
          confirm: handleRevert.bind(null, record),
        },
      },
      {
        label: '彻底删除',
        icon: 'ant-design:scissor-outlined',
        popConfirm: {
          title: '是否确认彻底删除',
          confirm: handleDelete.bind(null, record),
        },
      },
    ];
  }

  /**
   * 还原
   * @param record
   */
  function handleRevert(record) {
    revertTenantLogic({ ids: record.id }, handleSuccess);
    emit('success');
  }

  /**
   * 成功刷新表格
   */
  function handleSuccess() {
    (selectedRowKeys.value = []) && reload();
  }

  /**
   * 彻底删除
   * @param record
   */
  async function handleDelete(record) {
    await deleteLogicDeleted({ ids: record.id }, handleSuccess);
  }

  /**
   * 批量彻底删除
   */
  function batchHandleDelete() {
    Modal.confirm({
      title: '彻底删除',
      content: '是否确认彻底删除',
      okText: '确认',
      cancelText: '取消',
      onOk: () => {
        deleteLogicDeleted({ ids: toRaw(unref(selectedRowKeys)).join(',') }, handleSuccess);
      },
    });
  }

  /**
   * 批量还原
   */
  function batchHandleRevert() {
    Modal.confirm({
      title: '还原',
      content: '是否确认还原',
      okText: '确认',
      cancelText: '取消',
      onOk: () => {
        revertTenantLogic({ ids: toRaw(unref(selectedRowKeys)).join(',') }, handleSuccess);
        emit('success');
      },
    });
  }
</script>

<style lang="less" scoped>
  :deep(.ant-popover-inner-content) {
    width: 185px !important;
  }
</style>
