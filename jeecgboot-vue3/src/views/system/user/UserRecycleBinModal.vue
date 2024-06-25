<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="用户回收站" :showOkBtn="false" width="1000px" destroyOnClose>
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <!--插槽:table标题-->
      <template #tableTitle>
        <a-dropdown v-if="checkedKeys.length > 0">
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
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction :actions="getTableAction(record)" />
        </template>
      </template>
    </BasicTable>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, toRaw, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { recycleColumns } from './user.data';
  import { getRecycleBinList, putRecycleBin, deleteRecycleBin } from './user.api';
  import { useMessage } from '/@/hooks/web/useMessage';

  const { createConfirm } = useMessage();
  // 声明Emits
  const emit = defineEmits(['success', 'register']);
  const checkedKeys = ref<Array<string | number>>([]);
  const [registerModal] = useModalInner(() => {
    checkedKeys.value = [];
  });
  //注册table数据
  const [registerTable, { reload }] = useTable({
    api: getRecycleBinList,
    columns: recycleColumns,
    rowKey: 'id',
    striped: true,
    useSearchForm: false,
    showTableSetting: false,
    clickToRowSelect: false,
    bordered: true,
    showIndexColumn: false,
    pagination: true,
    tableSetting: { fullScreen: true },
    canResize: false,
    actionColumn: {
      width: 150,
      title: '操作',
      dataIndex: 'action',
      // slots: { customRender: 'action' },
      fixed: undefined,
    },
  });
  /**
   * 选择列配置
   */
  const rowSelection = {
    type: 'checkbox',
    columnWidth: 50,
    selectedRowKeys: checkedKeys,
    onChange: onSelectChange,
  };
  /**
   * 选择事件
   */
  function onSelectChange(selectedRowKeys: (string | number)[]) {
    checkedKeys.value = selectedRowKeys;
  }
  /**
   * 还原事件
   */
  async function handleRevert(record) {
    await putRecycleBin({ userIds: record.id }, reload);
    emit('success');
  }
  /**
   * 批量还原事件
   */
  function batchHandleRevert() {
    handleRevert({ id: toRaw(unref(checkedKeys)).join(',') });
  }
  /**
   * 删除事件
   */
  async function handleDelete(record) {
    await deleteRecycleBin({ userIds: record.id }, reload);
  }
  /**
   * 批量删除事件
   */
  function batchHandleDelete() {
    createConfirm({
      iconType: 'warning',
      title: '删除',
      content: '确定要永久删除吗？删除后将不可恢复！',
      onOk: () => handleDelete({ id: toRaw(unref(checkedKeys)).join(',') }),
      onCancel() {},
    });
  }
  //获取操作栏事件
  function getTableAction(record) {
    return [
      {
        label: '取回',
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
          title: '是否确认删除',
          confirm: handleDelete.bind(null, record),
        },
      },
    ];
  }
</script>
