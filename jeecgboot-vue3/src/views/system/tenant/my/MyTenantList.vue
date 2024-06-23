<template>
  <div>
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <template #tableTitle>
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
      </template>
      <template #action="{ record }">
        <TableAction :actions="getActions(record)" />
      </template>
    </BasicTable>
    <TenantInviteUserModal @register="registerSelUserModal" @inviteOk="handleInviteUserOk" />
    <TenantUserModal @register="registerTenUserModal" />
    <!--  产品包  -->
    <TenantPackList @register="registerPackModal" />
  </div>
</template>
<script lang="ts" name="tenant-my-tenant-list" setup>
  import { onMounted, ref, unref } from 'vue';
  import { BasicTable, TableAction } from '/@/components/Table';
  import { useModal } from '/@/components/Modal';
  import { invitationUserJoin, getTenantPageListByUserId } from '../tenant.api';
  import { columns, searchFormSchema } from '../tenant.data';
  import TenantModal from '../components/TenantModal.vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useListPage } from '/@/hooks/system/useListPage';
  import TenantInviteUserModal from '../components/TenantInviteUserModal.vue';
  import TenantUserModal from '../components/TenantUserList.vue';
  import TenantPackList from '../pack/TenantPackList.vue';
  import { getTenantId } from '/@/utils/auth';
  import { useUserStore } from '/@/store/modules/user';
  import { tenantSaasMessage } from "@/utils/common/compUtils";

  const { createMessage } = useMessage();
  const [registerModal, { openModal }] = useModal();
  const [registerSelUserModal, { openModal: userOpenModal }] = useModal();
  const [registerTenUserModal, { openModal: tenUserOpenModal }] = useModal();
  const [registerPackModal, { openModal: packModal }] = useModal();
  const userStore = useUserStore();

  // 列表页面公共参数、方法
  const { prefixCls, tableContext } = useListPage({
    designScope: 'tenant-template',
    tableProps: {
      title: '租户列表',
      api: getTenantPageListByUserId,
      columns: columns,
      formConfig: {
        schemas: searchFormSchema,
      },
      actionColumn: {
        width: 150,
        fixed: 'right',
      },
      rowSelection:{
        type: "radio"
      },
      beforeFetch: (params) => {
        return Object.assign(params, { userTenantStatus: '1,3,4' });
      },
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
        label: '用户',
        onClick: handleSeeUser.bind(null, record.id),
      },
    ];
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
    if (value) {
      await invitationUserJoin({ ids: selectedRowKeys.value.join(','), phone: value });
    }
  }

  /**
   * 查看用户
   * @param id
   */
  async function handleSeeUser(id) {
    tenUserOpenModal(true, {
      id: id,
    });
  }

  /**
   * 新增产品包
   */
  async function handlePack() {
    if (unref(selectedRowKeys).length > 1) {
      createMessage.warn('请选择一个');
      return;
    }
    packModal(true, {
      tenantId: unref(selectedRowKeys.value.join(',')),
      //我的租户不显示新增和编辑产品包
      showPackAddAndEdit: false
    });
  }

  /**
   * 删除成功之后回调事件
   */
  function handleSuccess() {
    (selectedRowKeys.value = []) && reload();
  }

  onMounted(()=>{
    //提示信息
    tenantSaasMessage('我的租户')
  })
</script>
