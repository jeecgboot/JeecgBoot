<template>
  <div>
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <template #tableTitle>
        <a-button preIcon="ant-design:plus-outlined" type="primary" @click="handleAdd" style="margin-right: 5px">新增</a-button>
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
        <a-button
          preIcon="ant-design:user-add-outlined"
          type="primary"
          @click="handleInvitation"
          style="margin-right: 5px"
          :disabled="selectedRowKeys.length === 0"
          >邀请用户加入</a-button
        >
        <a-button
          preIcon="ant-design:plus-outlined"
          type="primary"
          @click="handlePack"
          style="margin-right: 5px"
          :disabled="selectedRowKeys.length === 0"
          >套餐</a-button
        >
        <a-button type="primary" @click="recycleBinClick" preIcon="ant-design:hdd-outlined">回收站</a-button>
      </template>
      <template #action="{ record }">
        <TableAction :actions="getActions(record)" />
      </template>
    </BasicTable>
    <TenantModal @register="registerModal" @success="reload" />
    <TenantInviteUserModal @register="registerSelUserModal" @inviteOk="handleInviteUserOk"/>
    <TenantUserModal @register="registerTenUserModal" />
    <!--  产品包  -->
    <TenantPackList @register="registerPackModal" />
    <!--  租户回收站  -->
    <TenantRecycleBinModal @register="registerRecycleBinModal" @success="reload" />
  </div>
</template>
<script lang="ts" name="system-tenant" setup>
  import { ref, unref } from 'vue';
  import { BasicTable, TableAction } from '/@/components/Table';
  import { useModal } from '/@/components/Modal';
  import { getTenantList, deleteTenant, batchDeleteTenant, invitationUserJoin } from './tenant.api';
  import { columns, searchFormSchema } from './tenant.data';
  import TenantModal from './components/TenantModal.vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useListPage } from '/@/hooks/system/useListPage';
  import TenantInviteUserModal from './components/TenantInviteUserModal.vue';
  import TenantUserModal from './components/TenantUserList.vue';
  import TenantPackList from './pack/TenantPackList.vue';
  import TenantRecycleBinModal from './components/TenantRecycleBinModal.vue';

  const { createMessage } = useMessage();
  const [registerModal, { openModal }] = useModal();
  const [registerSelUserModal, { openModal: userOpenModal }] = useModal();
  const [registerTenUserModal, { openModal: tenUserOpenModal }] = useModal();
  const [registerPackModal, { openModal: packModal }] = useModal();
  const [registerRecycleBinModal, { openModal: recycleBinModal }] = useModal();

  // 列表页面公共参数、方法
  const { prefixCls, tableContext } = useListPage({
    designScope: 'tenant-template',
    tableProps: {
      title: '租户列表',
      api: getTenantList,
      columns: columns,
      formConfig: {
        schemas: searchFormSchema,
        fieldMapToTime: [['fieldTime', ['beginDate', 'endDate'], 'YYYY-MM-DD HH:mm:ss']],
      },
      actionColumn:{
        width: 150,
        fixed:'right'
      }
    },
  });
  const [registerTable, { reload }, { rowSelection, selectedRowKeys, selectedRows }] = tableContext;

  /**
   * 操作列定义
   * @param record
   */
  function getActions(record) {
    return [
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
      },
      {
        label: '删除',
        popConfirm: {
          title: '是否确认删除',
          placement: 'left',
          confirm: handleDelete.bind(null, record),
        },
      },
      {
        label: '用户',
        onClick: handleSeeUser.bind(null, record.id),
      },
    ];
  }

  /**
   * 新增事件
   */
  function handleAdd() {
    openModal(true, {
      isUpdate: false,
    });
  }

  /**
   * 编辑事件
   */
  function handleEdit(record) {
    openModal(true, {
      record,
      isUpdate: true,
    });
  }

  /**
   * 删除事件
   */
  async function handleDelete(record) {
    await deleteTenant({ id: record.id }, handleSuccess);
  }

  /**
   * 批量删除事件
   */
  async function batchHandleDelete() {
    await batchDeleteTenant({ ids: selectedRowKeys.value }, handleSuccess);
  }

  /**
   * 邀请用户加入租户
   */
  function handleInvitation() {
    userOpenModal(true, {});
  }

  /**
   * 用户选择回调事件
   * @param options
   * @param value
   */
  async function handleInviteUserOk(value) {
    //update-begin---author:wangshuai ---date:20230314  for：【QQYUN-4605】后台的邀请谁加入租户，没办法选不是租户下的用户------------
    if (value) {
      await invitationUserJoin({ ids: selectedRowKeys.value.join(','), phone: value });
    }
    //update-end---author:wangshuai ---date:20230314  for：【QQYUN-4605】后台的邀请谁加入租户，没办法选不是租户下的用户------------
  }

  /**
   * 查看用户
   * @param id
   */
  function handleSeeUser(id) {
    tenUserOpenModal(true, {
      id: id,
    });
  }

  /**
   * 新增产品包
   */
  function handlePack() {
    if (unref(selectedRowKeys).length > 1) {
      createMessage.warn('请选择一个');
      return;
    }
    packModal(true, {
      tenantId: unref(selectedRowKeys.value.join(',')),
      //我的租户显示新增和编辑产品包
      showPackAddAndEdit: true
    });
  }

  /**
   * 回收站
   */
  function recycleBinClick() {
    recycleBinModal(true, {});
  }

  /**
   * 删除成功之后回调事件
   */
  function handleSuccess() {
    (selectedRowKeys.value = []) && reload();
  }
</script>
