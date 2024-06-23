<template>
  <div>
    <!--表格-->
    <BasicTable @register="registerTable" :rowSelection="rowSelection" :searchInfo="searchInfo">
      <!--插槽:table标题-->
      <template #tableTitle>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleCreate"> 新增</a-button>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="batchHandleDelete">
                <Icon icon="ant-design:delete-outlined"></Icon>
                删除
              </a-menu-item>
            </a-menu>
          </template>
          <a-button
            >批量操作
            <Icon icon="mdi:chevron-down"></Icon>
          </a-button>
        </a-dropdown>
      </template>
      <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" />
      </template>
    </BasicTable>
    <!-- 表单区域 -->
    <JeecgOrderTicketModal @register="registerModal" @success="handleSuccess"></JeecgOrderTicketModal>
  </div>
</template>

<script lang="ts" setup>
  //ts语法
  import type { ComputedRef } from 'vue';
  import { ref, computed, unref, watch, inject } from 'vue';
  import { BasicTable, TableAction } from '/@/components/Table';
  import JeecgOrderTicketModal from './components/JeecgOrderTicketModal.vue';
  import { useListPage } from '/@/hooks/system/useListPage';
  import { useModal } from '/@/components/Modal';
  import { ticketColumns } from './erplist.data';
  import { ticketList, deleteTicket, deleteBatchTicket } from './erplist.api';
  import { isEmpty } from '/@/utils/is';
  import { useMessage } from '/@/hooks/web/useMessage';
  //接收主表id
  const orderId = inject<ComputedRef<string>>(
    'orderId',
    computed(() => '')
  );
  //提示弹窗
  const $message = useMessage();
  //弹窗model
  const [registerModal, { openModal }] = useModal();
  const searchInfo = {};
  // 列表页面公共参数、方法
  const { prefixCls, tableContext } = useListPage({
    tableProps: {
      api: getTicketList,
      tableSetting:{
        cacheKey:'ticket'
      },
      columns: ticketColumns,
      canResize: false,
      useSearchForm: false,
      actionColumn: {
        width: 180,
      },
      pagination: {
        current: 1,
        pageSize: 5,
        pageSizeOptions: ['5', '10', '20'],
      },
    },
  });

  //注册table数据
  const [registerTable, { reload, setSelectedRowKeys }, { rowSelection, selectedRowKeys }] = tableContext;

  watch(orderId, () => {
    searchInfo['orderId'] = unref(orderId);
    reload();
    // 主表id变化时，清空子表的选中状态
    setSelectedRowKeys([]);
  });

  async function getTicketList(params) {
    let { orderId } = params;
    // 主表Id为空时，不查询子表数据，直接返回空数组
    if (orderId == null || isEmpty(orderId)) {
      return [];
    }
    return await ticketList(params);
  }

  /**
   * 新增事件
   */
  function handleCreate() {
    if (isEmpty(unref(orderId))) {
      $message.createMessage.warning('请选择一个订单信息');
      return;
    }
    openModal(true, {
      isUpdate: false,
      showFooter: true,
    });
  }

  /**
   * 编辑事件
   */
  async function handleEdit(record: Recordable) {
    openModal(true, {
      record,
      isUpdate: true,
      showFooter: true,
    });
  }

  /**
   * 删除事件
   */
  async function handleDelete(record) {
    await deleteTicket({ id: record.id }, reload);
  }

  /**
   * 批量删除事件
   */
  async function batchHandleDelete() {
    await deleteBatchTicket({ ids: selectedRowKeys.value }, () => {
      selectedRowKeys.value = [];
      reload();
    });
  }

  /**
   * 成功回调
   */
  function handleSuccess() {
    reload();
  }

  /**
   * 操作栏
   */
  function getTableAction(record) {
    return [
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
      },
      {
        label: '删除',
        popConfirm: {
          title: '是否确认删除',
          confirm: handleDelete.bind(null, record),
        },
      },
    ];
  }
</script>

<style scoped></style>
