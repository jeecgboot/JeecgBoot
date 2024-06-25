<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="字典回收站" :showOkBtn="false" width="1000px" destroyOnClose>
    <BasicTable @register="registerTable">
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
  import { getRecycleBinList, putRecycleBin, deleteRecycleBin } from '../dict.api';
  // 声明Emits
  const emit = defineEmits(['success', 'register']);
  const checkedKeys = ref<Array<string | number>>([]);
  const [registerModal, { setModalProps, closeModal }] = useModalInner();
  //注册table数据
  const [registerTable, { reload }] = useTable({
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
    handleRevert({ id: toRaw(checkedKeys.value).join(',') });
  }
  /**
   * 批量删除事件
   */
  function batchHandleDelete() {
    handleDelete({ id: toRaw(checkedKeys.value).join(',') });
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
