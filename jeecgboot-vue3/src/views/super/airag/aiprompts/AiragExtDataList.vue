<template>
  <div>
    <!--引用表格-->
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <!--插槽:table标题-->
      <template #tableTitle>
        <a-button type="primary" @click="handleAdd" preIcon="ant-design:plus-outlined"> 新增</a-button>
      </template>
      <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" :dropDownActions="getDropDownAction(record)" />
      </template>
    </BasicTable>
    <!-- 表单区域 -->
    <AiragExtDataModal @register="registerModal" @success="handleSuccess"></AiragExtDataModal>
    <!-- 评测集 -->
    <AiragDataSetModal @register="registerDataSetModal" @success="handleSuccess"></AiragDataSetModal>
    <!-- 评估器配置 -->
    <AiEvaluatorSettingModal @register="registerEvaluatorModal" @success="handleSuccess"></AiEvaluatorSettingModal>
    <!-- 调试配置 -->
    <AiEvaluatorDebugModal @register="registerDebugModal" @success="handleSuccess"></AiEvaluatorDebugModal>
    <!-- 调用记录 -->
    <AiragInvokeRecordsDrawer @register="registerRecordsModal" @success="handleSuccess"></AiragInvokeRecordsDrawer>
  </div>
</template>

<script lang="ts" name="prompts-airagExtData" setup>
  import { BasicTable, TableAction } from '/@/components/Table';
  import { useModal } from '/@/components/Modal';
  import { useDrawer } from '/@/components/Drawer';
  import { useListPage } from '/@/hooks/system/useListPage';
  import AiragExtDataModal from './components/AiragExtDataModal.vue';
  import AiragDataSetModal from './components/AiragDataSetModal.vue';
  import AiEvaluatorSettingModal from './components/AiEvaluatorSettingModal.vue';
  import AiEvaluatorDebugModal from './components/AiEvaluatorDebugModal.vue';
  import AiragInvokeRecordsDrawer from './components/AiragInvokeRecordsDrawer.vue';
  import { columns, searchFormSchema } from './AiragExtData.data';
  import { list, deleteOne } from './AiragExtData.api';
  //注册model
  const [registerModal, { openModal }] = useModal();
  //注册评测集model
  const [registerDataSetModal, { openModal:openDataSetModal }] = useModal();
  //注册评估器model
  const [registerEvaluatorModal, { openModal:openEvaluatorModal }] = useModal();
  //注册评估器model
  const [registerDebugModal, { openModal:openDebugModal }] = useModal();
  //注册调用记录model
  const [registerRecordsModal, { openDrawer:openRecordsDrawer }] = useDrawer();
  const { tableContext } = useListPage({
    tableProps: {
      title: 'airag_ext_data',
      api: list,
      columns,
      canResize: true,
      formConfig: {
        schemas: searchFormSchema,
        autoSubmitOnEnter: true,
        showAdvancedButton: true,
        fieldMapToNumber: [],
        fieldMapToTime: [],
      },
      actionColumn: {
        width: 120,
        fixed: 'right',
      },
    },
  });

  const [registerTable, { reload }, { rowSelection, selectedRowKeys }] = tableContext;

  /**
   * 新增事件
   */
  function handleAdd() {
    openModal(true, {
      isUpdate: false,
      showFooter: true,
    });
  }
  /**
   * 编辑事件
   */
  function handleEdit(record: Recordable) {
    openEvaluatorModal(true, {
      record,
      isUpdate: true,
      showFooter: true,
    });
  }
  /**
   * 详情
   */
  function handleDateSet(record: Recordable) {
    openDataSetModal(true, {
      record,
    });
  }
  /**
   * 删除事件
   */
  async function handleDelete(record) {
    await deleteOne({ id: record.id }, handleSuccess);
  }
  /**
   * 调试事件
   */
  async function handleDebug(record) {
    openDebugModal(true, {
      record,
    });
  }
  /**
   * 调试事件
   */
  async function handleRecords(record) {
    openRecordsDrawer(true, {
      record,
    });
  }
  /**
   * 成功回调
   */
  function handleSuccess() {
    (selectedRowKeys.value = []) && reload();
  }
  /**
   * 操作栏
   */
  function getTableAction(record) {
    return [
      {
        label: '配置',
        onClick: handleEdit.bind(null, record),
      }
    ];
  }
  /**
   * 下拉操作栏
   */
  function getDropDownAction(record) {
    return [
      {
        label: '评测集',
        onClick: handleDateSet.bind(null, record),
      },
      {
        label: '调试',
        onClick: handleDebug.bind(null, record),
      },
      {
        label: '调用记录',
        onClick: handleRecords.bind(null, record),
      },
      {
        label: '删除',
        popConfirm: {
          title: '是否确认删除',
          confirm: handleDelete.bind(null, record),
          placement: 'topLeft',
        },
      },
    ];
  }
</script>

<style lang="less" scoped>
  :deep(.ant-picker),
  :deep(.ant-input-number) {
    width: 100%;
  }
</style>
