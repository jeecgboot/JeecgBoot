<template>
  <div class="p-2">
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <!--插槽:table标题-->
      <template #tableTitle>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleCreate">新增</a-button>
      </template>
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" />
      </template>
    </BasicTable>
    <AiOcrModal @register="registerModal" @success="reload()"></AiOcrModal>
    <AiOcrAnalysisModal @register="registerAnalysisModal" @success="reload()"></AiOcrAnalysisModal>
  </div>
</template>
<script lang="ts" name="site-list" setup>
  import { ref } from 'vue';
  import { BasicTable, TableAction } from '/@/components/Table';
  import { useModal } from '/@/components/Modal';
  import { useListPage } from '/@/hooks/system/useListPage';
  import { list, deleteOcrById } from './AiOcr.api';
  import { columns } from './AiOcr.data';
  import AiOcrModal from './components/AiOcrModal.vue';
  import AiOcrAnalysisModal from './components/AiOcrAnalysisModal.vue';

  const [registerModal, { openModal }] = useModal();
  const [registerAnalysisModal, { openModal: openAnalysisModal }] = useModal();
  
  // 列表页面公共参数、方法
  const { prefixCls, tableContext } = useListPage({
    tableProps: {
      api: list,
      columns,
      useSearchForm: false,
      pagination: false,
      actionColumn: {
        width: 120,
      },
      canResize: false,
    },
  });
  const [registerTable, { reload }, { rowSelection, selectedRowKeys }] = tableContext;

  /**
   * 新增
   */
  function handleCreate() {
    openModal(true, {});
  }

  /**
   * 编辑
   */
  function handleEdit(record: Recordable) {
    openModal(true, {
      record,
      isUpdate: true,
      showFooter: true,
    });
  }

  /**
   * 删除
   */
  async function handleDelete(id) {
    await deleteOcrById({ id: id });
    reload();
  }

  /**
   * 解析
   * @param record
   */
  function handleAnalysis(record){
    openAnalysisModal(true,{
      isUpdate: true,
      record
    })
  }
  
  /**
   * 操作栏
   */
  function getTableAction(record) {
    return [
      {
        label: '解析',
        onClick: handleAnalysis.bind(null, record),
      },
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
      },
      {
        label: '删除',
        popConfirm: {
          title: '是否确认删除',
          placement: 'left',
          confirm: handleDelete.bind(null, record.id),
        },
      },
    ];
  }
</script>
