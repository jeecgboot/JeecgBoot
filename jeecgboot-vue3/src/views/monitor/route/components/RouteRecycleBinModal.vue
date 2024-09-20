<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="路由回收站" :showOkBtn="false" width="1000px" destroyOnClose>
    <BasicTable @register="registerTable">
      <template #status="{ record, text }">
        <a-tag color="pink" v-if="text == 0">禁用</a-tag>
        <a-tag color="#87d068" v-if="text == 1">正常</a-tag>
      </template>
      <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" />
      </template>
    </BasicTable>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { columns } from '../route.data';
  import { deleteRouteList, putRecycleBin, deleteRecycleBin } from '../route.api';
  // 声明Emits
  const emit = defineEmits(['success', 'register']);
  const checkedKeys = ref<Array<string | number>>([]);
  const [registerModal] = useModalInner(() => {
    checkedKeys.value = [];
  });
  //注册table数据
  const [registerTable, { reload }] = useTable({
    rowKey: 'id',
    api: deleteRouteList,
    columns: columns,
    striped: true,
    useSearchForm: false,
    bordered: true,
    showIndexColumn: false,
    pagination: false,
    tableSetting: { fullScreen: true },
    canResize: false,
    actionColumn: {
      width: 150,
      title: '操作',
      dataIndex: 'action',
      slots: { customRender: 'action' },
      fixed: 'right',
    },
  });

  /**
   * 还原事件
   */
  async function handleRevert(record) {
    await putRecycleBin({ ids: record.id }, reload);
    emit('success');
  }
  /**
   * 删除事件
   */
  async function handleDelete(record) {
    await deleteRecycleBin({ ids: record.id }, reload);
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
