<template>
  <div>
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <template #tableTitle>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleCreate">新增</a-button>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="batchHandleDelete">
                <Icon icon="ant-design:delete-outlined" /> 删除
              </a-menu-item>
            </a-menu>
          </template>
          <a-button>批量操作<Icon icon="mdi:chevron-down" /></a-button>
        </a-dropdown>
      </template>
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" />
      </template>
      <template #status="{ text }">
        <a-tag color="pink" v-if="text == 0">禁用</a-tag>
        <a-tag color="#87d068" v-if="text == 1">启用</a-tag>
      </template>
      <template #relationType="{ text, record }">
        <span>{{ record.roleCode == 'DEF_INDEX_ALL' ? '全局默认' : text }}</span>
      </template>
      <template #roleCode="{ text, record }">
        <span>{{ record.roleCode == 'DEF_INDEX_ALL' ? '菜单默认首页' : text }}</span>
      </template>
    </BasicTable>
    <!--角色首页配置-->
    <HomeConfigModal @register="register" @success="reload" />
  </div>
</template>
<script lang="ts" name="home-config" setup>
  import { BasicTable, TableAction } from '/@/components/Table';
  import { useModal } from '/@/components/Modal';
  import HomeConfigModal from './components/HomeConfigModal.vue';
  import { columns, searchFormSchema } from './home.data';
  import { useListPage } from '/@/hooks/system/useListPage';
  import { list, deleteIndex, batchDelete } from './home.api';

  //弹窗配置
  const [register, { openModal }] = useModal();

  // 列表页面公共参数、方法
  const { tableContext } = useListPage({
    designScope: 'home-config',
    tableProps: {
      title: '首页配置',
      api: list,
      columns: columns,
      formConfig: {
        labelAlign: 'left',
        labelWidth: 80,
        schemas: searchFormSchema,
        baseRowStyle: {
          marginLeft: '2px',
        },
      },
      actionColumn: {
        width: 80,
      },
      //自定义默认排序
      defSort: {
        column: 'id',
        order: 'desc',
      },
    },
  });
  const [registerTable, { reload, clearSelectedRowKeys }, { rowSelection, selectedRowKeys }] = tableContext;

  /**
   * 新增事件
   */
  async function handleCreate() {
    openModal(true, {
      isUpdate: false,
    });
  }
  /**
   * 编辑事件
   */
  async function handleEdit(record) {
    openModal(true, {
      isUpdate: true,
      values: record,
    });
  }
  /**
   * 删除事件
   */
  async function handleDelete(record) {
    await deleteIndex({ id: record.id }, () => {
      reload();
    });
  }
  /**
   * 批量删除事件
   */
  async function batchHandleDelete() {
    await batchDelete({ ids: selectedRowKeys.value }, () => {
      clearSelectedRowKeys();
      reload();
    });
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
