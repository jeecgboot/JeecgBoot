<template>
  <div :class="prefixCls">
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <!--插槽:table标题-->
      <template #tableTitle>
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
    <ManageDrawer @register="registerDrawer" />
  </div>
</template>

<script lang="ts" setup name="message-manage">
  import { unref, computed } from 'vue';
  import { ActionItem, BasicTable, TableAction } from '/@/components/Table';
  import { useDrawer } from '/@/components/Drawer';
  import { useListPage } from '/@/hooks/system/useListPage';
  import ManageDrawer from './ManageDrawer.vue';
  import { Api, list, deleteBatch } from './manage.api';
  import { columns, searchFormSchema } from './manage.data';

  // 列表页面公共参数、方法
  const { prefixCls, tableContext } = useListPage({
    designScope: 'message-manage',
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
  const [registerTable, { reload, setLoading }, { rowSelection, selectedRowKeys }] = tableContext;
  const showBatchBtn = computed(() => selectedRowKeys.value.length > 0);

  const [registerDrawer, { openDrawer }] = useDrawer();

  function onDetail(record) {
    openDrawer(true, { record: record });
  }

  function onDelete(record) {
    if (record) {
      doDeleteDepart([record.id], false);
    }
  }

  async function onDeleteBatch() {
    try {
      await doDeleteDepart(selectedRowKeys);
      selectedRowKeys.value = [];
    } finally {
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

  /**
   * 操作栏
   */
  function getTableAction(record): ActionItem[] {
    return [{ label: '详情', onClick: onDetail.bind(null, record) }];
  }

  /**
   * 下拉操作栏
   */
  function getDropDownAction(record): ActionItem[] {
    return [
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
</script>

<style lang="less">
  @import 'index';
</style>
