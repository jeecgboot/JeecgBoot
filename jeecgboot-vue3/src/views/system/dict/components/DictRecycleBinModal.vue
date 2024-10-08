<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="字典回收站" :showOkBtn="false" width="1000px" destroyOnClose>
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
              <a-menu-item key="2" @click="batchHandleRevert">
                <Icon icon="ant-design:redo-outlined"></Icon>
                批量取回
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
<script lang="ts" setup>
  import { ref, toRaw } from 'vue';
  import { BasicModal, useModalInner } from '/src/components/Modal';
  import { BasicTable, useTable, TableAction } from '/src/components/Table';
  import { recycleBincolumns } from '../dict.data';
  import { getRecycleBinList, putRecycleBin, deleteRecycleBin, batchPutRecycleBin, batchDeleteRecycleBin } from '../dict.api';
  // 声明Emits
  const emit = defineEmits(['success', 'register']);
  const checkedKeys = ref<Array<string | number>>([]);
  const [registerModal, { setModalProps, closeModal }] = useModalInner(() => {
    checkedKeys.value = [];
  });
  //注册table数据
  const [registerTable, { reload }] = useTable({
    rowKey: 'id',
    api: getRecycleBinList,
    columns: recycleBincolumns,
    striped: true,
    useSearchForm: false,
    showTableSetting: false,
    clickToRowSelect: false,
    bordered: true,
    showIndexColumn: false,
    pagination: false,
    tableSetting: { fullScreen: true },
    canResize: false,
    actionColumn: {
      width: 100,
      title: '操作',
      dataIndex: 'action',
      slots: { customRender: 'action' },
      fixed: undefined,
    },
  });
  // update-begin--author:liaozhiyang---date:20240709---for：【TV360X-1663】数据字典回收增加批量功能
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
  // update-end--author:liaozhiyang---date:20240709---for：【TV360X-1663】数据字典回收增加批量功能
  /**
   * 还原事件
   */
  async function handleRevert(record) {
    await putRecycleBin(record.id, reload);
    emit('success');
  }
  /**
   * 删除事件
   */
  async function handleDelete(record) {
    await deleteRecycleBin(record.id, reload);
  }
  /**
   * 批量还原事件
   */
  function batchHandleRevert() {
    batchPutRecycleBin({ ids: toRaw(checkedKeys.value).join(',') }, () => {
      // update-begin--author:liaozhiyang---date:20240709---for：【TV360X-1663】数据字典回收增加批量功能
      reload();
      checkedKeys.value = [];
      emit('success');
      // update-end--author:liaozhiyang---date:20240709---for：【TV360X-1663】数据字典回收增加批量功能
    });
  }
  /**
   * 批量删除事件
   */
  function batchHandleDelete() {
    batchDeleteRecycleBin({ ids: toRaw(checkedKeys.value).join(',') }, () => {
      // update-begin--author:liaozhiyang---date:20240709---for：【TV360X-1663】数据字典回收增加批量功能
      checkedKeys.value = [];
      reload();
      // update-end--author:liaozhiyang---date:20240709---for：【TV360X-1663】数据字典回收增加批量功能
    });
  }
  //获取操作栏事件
  function getTableAction(record) {
    return [
      {
        label: '取回',
        icon: 'ant-design:redo-outlined',
        popConfirm: {
          title: '是否确认取回',
          confirm: handleRevert.bind(null, record),
        },
      },
      {
        label: '彻底删除',
        icon: 'ant-design:scissor-outlined',
        color: 'error',
        popConfirm: {
          title: '是否确认删除',
          confirm: handleDelete.bind(null, record),
        },
      },
    ];
  }
</script>
