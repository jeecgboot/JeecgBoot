<template>
  <div>
    <!--引用表格-->
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <!--插槽:table标题-->
      <template #tableTitle>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleCreate">新增</a-button>
        <a-button
            preIcon="ant-design:user-add-outlined"
            type="primary"
            @click="handleInvitation"
            style="margin-right: 5px">
            邀请用户加入
        </a-button>
        <JThirdAppButton biz-type="user" :selected-row-keys="selectedRowKeys" syncToApp syncToLocal @sync-finally="onSyncFinally" />
        <a-button type="primary" @click="openQuitModal(true, {})" preIcon="ant-design:user-delete-outlined">离职人员</a-button>
        <div style="margin-left: 10px;margin-top: 5px"> 当前登录租户: <span class="tenant-name">{{loginTenantName}}</span> </div>
        <a-tooltip title="租户用户更多操作说明">
          <a-icon type="question-circle" style="margin-left: 8px; cursor: pointer " @click="tipShow = true"/>
        </a-tooltip>
      </template>
      <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" :dropDownActions="getDropDownAction(record)" />
      </template>
    </BasicTable>
    <!--用户抽屉-->
    <TenantUserDrawer @register="registerDrawer" @success="handleSuccess" />
    <!-- 离职人员列弹窗 -->
    <UserQuitModal @register="registerQuitModal" @success="reload" />
    <!--  变更拥有者弹窗  -->
    <UserSelectModal @register="registerUserModal" :excludeUserIdList="excludeUserIdList" :maxSelectCount="1" @getSelectResult="selectResult" />
    <!--  套餐分配弹窗  -->
    <TenantPackAllotModal @register="registerPackAllotModal"></TenantPackAllotModal>
    <!--  邀请人弹窗  -->
    <TenantInviteUserModal @register="registerSelUserModal" @inviteOk="handleInviteUserOk" />
    <a-modal v-model:open="tipShow" :footer="null" title="租户用户更多操作说明" :width="800">
      <ul class="user-tenant-tip">
        <li>移除：将用户从当前租户中移除</li>
        <li>删除：仅可删除当天创建的用户，删除后可在系统用户回收站恢复</li>
        <li>离职：非租户创建者可进行离职操作，离职员工可在离职人员列表查看</li>
        <li>交接：租户创建者可进行租户交接，交接后员工信息可在离职人员列表查看</li>
      </ul>
      <div style="height: 10px"></div>
    </a-modal>
  </div>
</template>

<script lang="ts" name="tenant-system-user" setup>
  //ts语法
  import { onMounted, ref, unref } from 'vue';
  import { BasicTable, TableAction, ActionItem } from '/@/components/Table';
  import UserDrawer from '../user/UserDrawer.vue';
  import JThirdAppButton from '/@/components/jeecg/thirdApp/JThirdAppButton.vue';
  import UserQuitModal from '../user/UserQuitModal.vue';
  import { useDrawer } from '/@/components/Drawer';
  import { useListPage } from '/@/hooks/system/useListPage';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { columns, searchFormSchema } from '../user/user.data';
  import { list , deleteUser, batchDeleteUser, getImportUrl, getExportUrl, frozenBatch, getUserTenantPageList, updateUserTenantStatus } from '../user/user.api';
  // import { usePermission } from '/@/hooks/web/usePermission'
  // const { hasPermission } = usePermission();
  import { userTenantColumns, userTenantFormSchema } from '../user/user.data';
  import { useUserStore } from '/@/store/modules/user';
  import UserSelectModal from '/@/components/Form/src/jeecg/components/modal/UserSelectModal.vue';
  import { getTenantId } from "/@/utils/auth";
  import { changeOwenUserTenant } from "/@/views/system/usersetting/UserSetting.api";
  import {getLoginTenantName, invitationUserJoin, leaveTenant} from "/@/views/system/tenant/tenant.api";
  import TenantUserDrawer from './components/TenantUserDrawer.vue';
  import { sameDay, tenantSaasMessage } from "@/utils/common/compUtils";
  import TenantPackAllotModal from './components/TenantPackAllotModal.vue'
  import TenantInviteUserModal from "@/views/system/tenant/components/TenantInviteUserModal.vue";

  const { createMessage, createConfirm } = useMessage();

  //注册drawer
  const [registerDrawer, { openDrawer }] = useDrawer();
  //离职代理人model
  const [registerQuitAgentModal, { openModal: openQuitAgentModal }] = useModal();
  //离职用户列表model
  const [registerQuitModal, { openModal: openQuitModal }] = useModal();
  //分配套餐弹窗
  const [registerPackAllotModal, { openModal: openPackAllotModal }] = useModal();
  const userStore = useUserStore();
  const createBy = userStore.getUserInfo.username;
  //弹窗提示显示
  const tipShow = ref<boolean>(false);

  // 列表页面公共参数、方法
  const { prefixCls, tableContext, onExportXls, onImportXls } = useListPage({
    designScope: 'user-list',
    tableProps: {
      title: '租户用户列表',
      api: getUserTenantPageList,
      columns: userTenantColumns,
      size: 'small',
      formConfig: {
        schemas: userTenantFormSchema,
      },
      actionColumn: {
        width: 120,
      },
      beforeFetch: (params) => {
        params['userTenantStatus'] = '1,3,4';
        return Object.assign({ column: 'createTime', order: 'desc' }, params);
      },
    },
  });

  //注册table数据
  const [registerTable, { reload, updateTableDataRecord }, { rowSelection, selectedRows, selectedRowKeys }] = tableContext;

  /**
   * 新增事件
   */
  function handleCreate() {
    openDrawer(true, {
      isUpdate: false,
      showFooter: true,
      tenantSaas: true,
    });
  }
  /**
   * 编辑事件
   */
  async function handleEdit(record: Recordable) {
    openDrawer(true, {
      record,
      isUpdate: true,
      showFooter: true,
      tenantSaas: true,
    });
  }
  /**
   * 详情
   */
  async function handleDetail(record: Recordable) {
    openDrawer(true, {
      record,
      isUpdate: true,
      showFooter: false,
      tenantSaas: true,
    });
  }

  /**
   * 成功回调
   */
  function handleSuccess() {
    reload();
  }

  /**
   *同步钉钉和微信回调
   */
  function onSyncFinally({ isToLocal }) {
    // 同步到本地时刷新下数据
    if (isToLocal) {
      reload();
    }
  }

  /**
   * 操作栏
   */
  function getTableAction(record): ActionItem[] {
    return [
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
        // ifShow: () => hasPermission('system:user:edit'),
      },
    ];
  }
  /**
   * 下拉操作栏
   */
  function getDropDownAction(record): ActionItem[] {
    return [
      {
        label: '查看详情',
        onClick: handleDetail.bind(null, record),
      },
      {
        label: '移除用户',
        onClick: handleLeave.bind(null, record.id),
      },
      {
        label: '删除用户',
        popConfirm: {
          title: '是否确认删除该用户',
          confirm: handleDeleteUser.bind(null, record),
        },
        ifShow: () => record.username!== userStore.getUserInfo?.username && sameDay(record.createTime),
      },
      {
        label: '变更拥有者',
        onClick: handleHandover.bind(null, record),
        ifShow: () =>{
          return record.username === record.createBy;
        }
      },
      {
        label: '同意',
        onClick: updateStatus.bind(null, record.id, '1'),
        ifShow: () => {
          return (record.status === '3' || record.status === '4') && record.createBy === createBy;
        },
      },
      {
        label: '拒绝',
        popConfirm: {
          title: '是否确认拒绝',
          confirm: updateStatus.bind(null, record.id, '4'),
        },
        ifShow: () => {
          return record.status === '3' && record.createBy === createBy;
        },
      },
      {
        label: '用户套餐',
        onClick: handleAllotPack.bind(null, record),
      }
    ];
  }

  /**
   * 更新用户租户状态
   * @param id
   * @param status
   */
  function updateStatus(id, status) {
    updateUserTenantStatus({ userId: id, status: status })
      .then((res) => {
        if (res.success) {
          handleSuccess();
        }
      })
      .catch((e) => {
        createMessage.warning(e.message);
      });
  }

  //============================================ 租户离职交接  ============================================
  //租户id
  const tenantId = ref<string>('');
  //排除自己的编号集合
  const excludeUserIdList = ref<any>([]);
  //离职代理人model
  const [registerUserModal, { openModal: openUserModal }] = useModal();
  //邀请用户加入弹窗
  const [registerSelUserModal, { openModal: userOpenModal }] = useModal();
  const handOverUserName = ref<string>('');
  
  /**
   * 人员交接
   */
  function handleHandover(record) {
    tenantId.value = getTenantId();
    excludeUserIdList.value = [record.id];
    //记录一下当前需要交接的用户名
    handOverUserName.value = record.createBy;
    openUserModal(true)
  }

  /**
   * 用户选择回调
   * @param options
   * @param values
   */
  function selectResult(options,values) {
    console.log(values)
    if(values && values.length>0){
      let userId = values[0];
      changeOwenUserTenant({ userId:userId, tenantId:unref(tenantId) }).then((res) =>{
        if(res.success){
          createMessage.success("交接成功");
          let username = userStore.getUserInfo?.username;
          if(username == handOverUserName.value){
            userStore.logout(true);
          }else{
            reload();
          }
        } else {
          createMessage.warning(res.message);
        }
      })
    }
  }
  //============================================  租户离职交接  ============================================


  //update-begin---author:wangshuai ---date:20230710  for：【QQYUN-5723】4、显示当前登录租户------------
  const loginTenantName = ref<string>('');

  getTenantName();

  async function getTenantName(){
    loginTenantName.value = await getLoginTenantName();
  }
  //update-end---author:wangshuai ---date:20230710  for：【QQYUN-5723】4、显示当前登录租户------------
  

  /**
   * 分配套餐
   * 
   * @param record
   */
  function handleAllotPack(record) {
    openPackAllotModal(true,{
      record
    })
  }

  /**
   * 删除用户
   */
  function handleDeleteUser(record) {
    deleteUser({ id: record.id }, reload);
  }

  /**
   * 邀请用户加入租户
   */
  function handleInvitation() {
    userOpenModal(true, {});
  }
  
  /**
   * 用户选择回调事件
   * @param username
   * @param phone
   * @param userSelectId
   */
  async function handleInviteUserOk(phone, username) {
    let tId = getTenantId();
    if (phone) {
      await invitationUserJoin({ ids: tId, phone: phone });
      reload();
    }
    if (username) {
      await invitationUserJoin({ ids: tId, username: username });
      reload();
    }
  }

  /**
   * 请离
   * @param id
   */
  async function handleLeave(id) {
    await leaveTenant({ userIds: id, tenantId: getTenantId() }, reload)
  }
  
  
  onMounted(()=>{
    tenantSaasMessage('租户用户')
  })
</script>

<style scoped>
  .tenant-name{
    text-decoration:underline;
    margin: 5px;
    font-size: 15px;
  }
  .user-tenant-tip{
    margin: 20px;
    background-color: #f8f9fb;
    color: #99a1a9;
    border-radius: 4px;
    padding: 12px;
  }
</style>
