<template>
  <div>
    <!--主表表格-->
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
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
    <!--子表表格tab-->
    <a-tabs defaultActiveKey="1" style="margin: 10px">
      <a-tab-pane tab="客户信息" key="1">
        <JeecgOrderCustomerList />
      </a-tab-pane>
      <a-tab-pane tab="机票信息" key="2" forceRender>
        <JeecgOrderTicketList />
      </a-tab-pane>
    </a-tabs>
  </div>
  <!-- 表单区域 -->
  <JeecgOrderModal @register="registerModal" @success="handleSuccess"></JeecgOrderModal>
</template>

<script lang="ts" name="tab-list" setup>
  //ts语法
  import { ref, computed, unref, watch, provide } from 'vue';
  import { BasicTable, TableAction } from '/@/components/Table';
  import { useListPage } from '/@/hooks/system/useListPage';
  import { useModal } from '/@/components/Modal';
  import JeecgOrderModal from './components/JeecgOrderModal.vue';
  import JeecgOrderCustomerList from './JeecgOrderCustomerList.vue';
  import JeecgOrderTicketList from './JeecgOrderTicketList.vue';
  import { columns, searchFormSchema } from './erplist.data';
  import { list, deleteOne, batchDelete } from './erplist.api';

  //弹窗model
  const [registerModal, { openModal }] = useModal();

  // 列表页面公共参数、方法
  const { tableContext } = useListPage({
    tableProps: {
      api: list,
      tableSetting:{
        cacheKey:'erp_main'
      },
      columns: columns,
      canResize: false,
      rowSelection: { type: 'radio' },
      formConfig: {
        schemas: searchFormSchema,
      },
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
  const [registerTable, { reload, updateTableDataRecord }, { rowSelection, selectedRowKeys }] = tableContext;

  const orderId = computed(() => (unref(selectedRowKeys).length > 0 ? unref(selectedRowKeys)[0] : ''));
  //下发 orderId,子组件接收
  provide('orderId', orderId);

  /**
   * 新增事件
   */
  function handleCreate() {
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
   * 详情
   */
  async function handleDetail(record: Recordable) {
    openModal(true, {
      record,
      isUpdate: true,
      showFooter: false,
    });
  }

  /**
   * 删除事件
   */
  async function handleDelete(record) {
    await deleteOne({ id: record.id }, reload);
  }

  /**
   * 批量删除事件
   */
  async function batchHandleDelete() {
    await batchDelete({ ids: selectedRowKeys.value }, () => {
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
