<template>
  <div :class="prefixCls">
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <!--插槽:table标题-->
      <template #tableTitle>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="onAdd">新增</a-button>
        <a-button type="primary" preIcon="ant-design:export-outlined" @click="onExportXls"> 导出</a-button>
        <j-upload-button type="primary" preIcon="ant-design:import-outlined" @click="onImportXls">导入</j-upload-button>
        <a-dropdown v-if="showBatchBtn">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="onDeleteBatch">
                <Icon icon="ant-design:delete-outlined"></Icon>
                <span>删除</span>
              </a-menu-item>
            </a-menu>
          </template>
          <a-button>
            <span>批量操作</span>
            <Icon icon="mdi:chevron-down"></Icon>
          </a-button>
        </a-dropdown>
      </template>

      <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" :dropDownActions="getDropDownAction(record)" />
      </template>
    </BasicTable>
    <TemplateModal @register="registerModal" @success="reload" />
    <TemplateTestModal @register="registerTestModal" />
  </div>
</template>

<script lang="ts" setup name="message-template">
  import { unref, computed, toRaw } from 'vue';
  import { ActionItem, BasicTable, TableAction } from '/@/components/Table';
  import { useModal } from '/@/components/Modal';
  import { useListPage } from '/@/hooks/system/useListPage';
  import TemplateModal from './TemplateModal.vue';
  import TemplateTestModal from './TemplateTestModal.vue';
  import { Api, saveOrUpdate, list, deleteBatch } from './template.api';
  import { columns, searchFormSchema } from './template.data';
  import { useMessage } from '/@/hooks/web/useMessage';
  const { createMessage } = useMessage();

  // 列表页面公共参数、方法
  const { prefixCls, onExportXls, onImportXls, tableContext } = useListPage({
    designScope: 'message-template',
    tableProps: {
      title: '消息中心模板列表数据',
      api: list,
      columns: columns,
      formConfig: {
        schemas: searchFormSchema,
      },
    },
    exportConfig: {
      url: Api.exportXls,
      name: '消息中心模板列表',
    },
    importConfig: {
      url: Api.importXls,
      success: () => reload(),
    },
  });

  // 注册 ListTable
  const [registerTable, { reload, setLoading }, { rowSelection, selectedRowKeys, selectedRows }] = tableContext;
  const [registerModal, { openModal }] = useModal();
  const [registerTestModal, testModal] = useModal();
  const showBatchBtn = computed(() => selectedRowKeys.value.length > 0);

  function onAdd() {
    openModal(true, {
      title: '新增消息模板',
      isUpdate: false,
      showFooter: true,
      record: {},
    });
  }

  function onEdit(record) {
    if (record.useStatus === '1') {
      createMessage.warning('此模板已被应用，禁止编辑!');
      return;
    }
    openModal(true, {
      title: '修改消息模板',
      isUpdate: true,
      record: record,
      showFooter: true,
    });
  }

  function onDelete(record) {
    if (record) {
      //update-begin-author:taoyan date:2022-7-14 for: VUEN-1652【bug】应用状态下不允许删除
      if(record.useStatus == '1'){
        createMessage.warning('该模板已被应用禁止删除!');
        return;
      }
      //update-end-author:taoyan date:2022-7-14 for: VUEN-1652【bug】应用状态下不允许删除
      doDeleteDepart([record.id], false);
    }
  }

  /**
   * 根据 ids 批量删除
   * @param idListRef array
   * @param confirm 是否显示确认提示框
   */
  async function doDeleteDepart(idListRef, confirm = true) {
    const idList = unref(idListRef);
    if (idList.length > 0) {
      try {
        setLoading(true);
        await deleteBatch({ ids: idList.join(',') }, confirm);
        await reload();
      } finally {
        setLoading(false);
      }
    }
  }

  async function onDeleteBatch() {
    try {
      //update-begin-author:taoyan date:2022-7-14 for: VUEN-1652【bug】应用状态下不允许删除
      let arr = toRaw(selectedRows.value);
      let temp = arr.filter(item=>item.useStatus=='1')
      if(temp.length>0){
        createMessage.warning('选中的模板已被应用禁止删除!');
        return;
      }
      //update-end-author:taoyan date:2022-7-14 for: VUEN-1652【bug】应用状态下不允许删除
      await doDeleteDepart(selectedRowKeys);
      selectedRowKeys.value = [];
    } finally {
    }
  }

  // 发送消息测试
  function onSendTest(record) {
    testModal.openModal(true, { record });
  }

  /**
   * 操作栏
   */
  function getTableAction(record): ActionItem[] {
    //update-begin---author:wangshuai ---date:20221123  for：[VUEN-2807]消息模板加一个查看功能------------
    return [{ label: '查看', onClick: handleDetail.bind(null, record)}, { label: '编辑', onClick: onEdit.bind(null, record) }];
    //update-end---author:wangshuai ---date:20221123  for：[VUEN-2807]消息模板加一个查看功能------------
  }

  /**
   * 下拉操作栏
   */
  function getDropDownAction(record): ActionItem[] {
    return [
      { label: '应用', onClick: handleUse.bind(null, record) },
      { label: '停用', onClick: handleNotUse.bind(null, record) },
      { label: '发送测试', onClick: onSendTest.bind(null, record) },
      {
        label: '删除',
        color: 'error',
        popConfirm: {
          title: '确认要删除吗？',
          confirm: onDelete.bind(null, record),
        },
      },
    ];
  }

  /**
   * 应用
   */
  async function handleUse(record) {
    let param = { id: record.id, useStatus: '1' };
    await saveOrUpdate(param, true);
    await reload();
  }

  /**
   * 停用
   */
  async function handleNotUse(record) {
    let param = { id: record.id, useStatus: '0' };
    await saveOrUpdate(param, true);
    await reload();
  }

  /**
   * 查看
   * @param record
   */
  function handleDetail(record) {
    openModal(true,{
      title: "消息模板详情",
      isUpdate: true,
      showFooter: false,
      record:record
    })
  }
</script>

<style lang="less">
  @import 'index';
</style>
