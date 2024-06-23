<template>
  <BasicTable @register="registerTable">
    <template #tableTitle>
      <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleCreate"> 新增</a-button>
      <a-button type="primary" preIcon="ant-design:export-outlined" @click="onExportXls"> 导出</a-button>
      <j-upload-button type="primary" preIcon="ant-design:import-outlined" @click="onImportXls">导入</j-upload-button>
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
    <template #action="{ record }">
      <TableAction :actions="getTableAction(record)" :dropDownActions="getDropDownAction(record)" />
    </template>
  </BasicTable>
  <!--角色用户表格-->
  <RoleUserTable @register="roleUserDrawer" />
  <!--角色编辑抽屉-->
  <RoleDrawer @register="registerDrawer" @success="reload" :showFooter="showFooter" />
  <!--角色详情-->
  <RoleDesc @register="registerDesc"></RoleDesc>
  <!--角色菜单授权抽屉-->
  <RolePermissionDrawer @register="rolePermissionDrawer" />
  <!--角色首页配置-->
  <RoleIndexModal @register="registerIndexModal" />
</template>
<script lang="ts" name="system-role" setup>
  import { ref } from 'vue';
  import { BasicTable, TableAction } from '/@/components/Table';
  import { useDrawer } from '/@/components/Drawer';
  import { useModal } from '/@/components/Modal';
  import RoleDrawer from './components/RoleDrawer.vue';
  import RoleDesc from './components/RoleDesc.vue';
  import RolePermissionDrawer from './components/RolePermissionDrawer.vue';
  import RoleIndexModal from './components/RoleIndexModal.vue';
  import RoleUserTable from './components/RoleUserTable.vue';
  import { columns, searchFormSchema } from './role.data';
  import { list, deleteRole, batchDeleteRole, getExportUrl, getImportUrl } from './role.api';
  import { useListPage } from '/@/hooks/system/useListPage';
  const showFooter = ref(true);
  const [roleUserDrawer, { openDrawer: openRoleUserDrawer }] = useDrawer();
  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerIndexModal, { openModal: openIndexModal }] = useModal();
  const [rolePermissionDrawer, { openDrawer: openRolePermissionDrawer }] = useDrawer();
  const [registerDesc, { openDrawer: openRoleDesc }] = useDrawer();

  // 列表页面公共参数、方法
  const { prefixCls, tableContext, onImportXls, onExportXls } = useListPage({
    designScope: 'role-template',
    tableProps: {
      title: '系统角色列表',
      api: list,
      columns: columns,
      formConfig: {
        // update-begin--author:liaozhiyang---date:20230803---for：【QQYUN-5873】查询区域lablel默认居左
        labelWidth:65,
        rowProps: { gutter: 24 },
        // update-end--author:liaozhiyang---date:20230803---for：【QQYUN-5873】查询区域lablel默认居左
        schemas: searchFormSchema,
      },
      actionColumn: {
        width: 120,
      },
      rowSelection: null,
      //自定义默认排序
      defSort: {
        column: 'id',
        order: 'desc',
      },
    },
    exportConfig: {
      name: '角色列表',
      url: getExportUrl,
    },
    importConfig: {
      url: getImportUrl,
    },
  });
  const [registerTable, { reload }, { rowSelection, selectedRowKeys }] = tableContext;

  /**
   * 新增
   */
  function handleCreate() {
    showFooter.value = true;
    openDrawer(true, {
      isUpdate: false,
    });
  }
  /**
   * 编辑
   */
  function handleEdit(record: Recordable) {
    showFooter.value = true;
    openDrawer(true, {
      record,
      isUpdate: true,
    });
  }
  /**
   * 详情
   */
  function handleDetail(record) {
    showFooter.value = false;
    openRoleDesc(true, {
      record,
      isUpdate: true,
    });
  }
  /**
   * 删除事件
   */
  async function handleDelete(record) {
    await deleteRole({ id: record.id }, reload);
  }
  /**
   * 批量删除事件
   */
  async function batchHandleDelete() {
    await batchDeleteRole({ ids: selectedRowKeys.value }, reload);
  }
  /**
   * 角色授权弹窗
   */
  function handlePerssion(record) {
    openRolePermissionDrawer(true, { roleId: record.id });
  }
  /**
   * 首页配置弹窗
   */
  function handleIndexConfig(roleCode) {
    openIndexModal(true, { roleCode });
  }
  /**
   * 角色用户
   */
  function handleUser(record) {
    //onSelectChange(selectedRowKeys)
    openRoleUserDrawer(true, record);
  }
  /**
   * 操作栏
   */
  function getTableAction(record) {
    return [
      {
        label: '用户',
        onClick: handleUser.bind(null, record),
      },
      {
        label: '授权',
        onClick: handlePerssion.bind(null, record),
      },
    ];
  }

  /**
   * 下拉操作栏
   */
  function getDropDownAction(record) {
    return [
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
      },
      {
        label: '详情',
        onClick: handleDetail.bind(null, record),
      },
      {
        label: '删除',
        popConfirm: {
          title: '是否确认删除',
          confirm: handleDelete.bind(null, record),
        },
      },
      {
        label: '首页配置',
        onClick: handleIndexConfig.bind(null, record.roleCode),
      },
    ];
  }
</script>
