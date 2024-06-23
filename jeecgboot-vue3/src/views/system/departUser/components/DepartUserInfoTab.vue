<template>
  <!--引用表格-->
  <BasicTable @register="registerTable" :rowSelection="rowSelection">
    <!--插槽:table标题-->
    <template #tableTitle>
      <a-button type="primary" preIcon="ant-design:plus-outlined" @click="selectAddUser">添加已有用户</a-button>
      <a-button type="primary" preIcon="ant-design:plus-outlined" @click="createUser">新建用户</a-button>
      <template v-if="selectedRowKeys.length > 0">
        <a-dropdown>
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="onUnlinkDepartUserBatch">
                <icon icon="bx:bx-unlink" />
                <span>取消关联</span>
              </a-menu-item>
            </a-menu>
          </template>
          <a-button>
            <span>批量操作 </span>
            <icon icon="akar-icons:chevron-down" />
          </a-button>
        </a-dropdown>
      </template>
    </template>
    <!-- 插槽：行内操作按钮 -->
    <template #action="{ record }">
      <TableAction :actions="getTableAction(record)" :dropDownActions="getDropDownAction(record)" />
    </template>
  </BasicTable>
  <UserDrawer @register="registerDrawer" @success="onUserDrawerSuccess" />
  <DepartRoleUserAuthDrawer @register="registerUserAuthDrawer" />
  <UserSelectModal rowKey="id" @register="registerSelUserModal" @getSelectResult="onSelectUserOk" />
</template>

<script lang="ts" setup>
  import { computed, inject, ref, unref, watch } from 'vue';
  import { ActionItem, BasicTable, TableAction } from '/@/components/Table';
  import { useModal } from '/@/components/Modal';
  import { useDrawer } from '/@/components/Drawer';
  import { useListPage } from '/@/hooks/system/useListPage';

  import UserDrawer from '/@/views/system/user/UserDrawer.vue';
  import UserSelectModal from '/@/components/Form/src/jeecg/components/modal/UserSelectModal.vue';
  import DepartRoleUserAuthDrawer from './DepartRoleUserAuthDrawer.vue';
  import { departUserList, linkDepartUserBatch, unlinkDepartUserBatch } from '../depart.user.api';
  import { userInfoColumns, userInfoSearchFormSchema } from '../depart.user.data';
  import { ColEx } from '/@/components/Form/src/types';

  const prefixCls = inject('prefixCls');
  const props = defineProps({
    data: { require: true, type: Object },
  });
  // 当前选中的部门ID，可能会为空，代表未选择部门
  const departId = computed(() => props.data?.id);

  // 自适应列配置
  const adaptiveColProps: Partial<ColEx> = {
    xs: 24, // <576px
    sm: 24, // ≥576px
    md: 24, // ≥768px
    lg: 12, // ≥992px
    xl: 12, // ≥1200px
    xxl: 8, // ≥1600px
  };
  // 列表页面公共参数、方法
  const { tableContext, createMessage } = useListPage({
    tableProps: {
      api: departUserList,
      columns: userInfoColumns,
      canResize: false,
      formConfig: {
        schemas: userInfoSearchFormSchema,
        baseColProps: adaptiveColProps,
        labelAlign: 'left',
        labelCol: {
          xs: 24,
          sm: 24,
          md: 24,
          lg: 9,
          xl: 7,
          xxl: 5,
        },
        wrapperCol: {},
        // 操作按钮配置
        actionColOptions: {
          ...adaptiveColProps,
          style: { textAlign: 'left' },
        },
      },
      // 【issues/1064】列设置的 cacheKey
      tableSetting: { cacheKey: 'depart_user_userInfo' },
      // 请求之前对参数做处理
      beforeFetch(params) {
        params.depId = departId.value;
      },
    },
  });

  // 注册 ListTable
  const [registerTable, { reload, setProps, setLoading, updateTableDataRecord }, { rowSelection, selectedRowKeys }] = tableContext;

  watch(
    () => props.data,
    () => reload()
  );
  //注册drawer
  const [registerDrawer, { openDrawer, setDrawerProps }] = useDrawer();
  const [registerUserAuthDrawer, userAuthDrawer] = useDrawer();
  // 注册用户选择 modal
  const [registerSelUserModal, selUserModal] = useModal();

  // 清空选择的行
  function clearSelection() {
    selectedRowKeys.value = [];
  }

  // 查看部门角色
  function showDepartRole(record) {
    userAuthDrawer.openDrawer(true, { userId: record.id, departId });
  }

  // 创建用户
  function createUser() {
    if (!departId.value) {
      createMessage.warning('请先选择一个部门');
    } else {
      openDrawer(true, {
        isUpdate: false,
        departDisabled: true,
        // 初始化负责部门
        nextDepartOptions: { value: props.data?.key, label: props.data?.title },
        record: {
          activitiSync: 1,
          userIdentity: 1,
          selecteddeparts: departId.value,
        },
      });
    }
  }

  // 查看用户详情
  function showUserDetail(record) {
    openDrawer(true, {
      record,
      isUpdate: true,
      departDisabled: true,
      showFooter: false,
    });
  }

  // 编辑用户信息
  function editUserInfo(record) {
    openDrawer(true, { isUpdate: true, record, departDisabled: true });
  }

  // 选择添加已有用户
  function selectAddUser() {
    selUserModal.openModal();
  }

  // 批量取消关联部门和用户之间的关系
  async function unlinkDepartUser(idList, confirm) {
    if (!departId.value) {
      createMessage.warning('请先选择一个部门');
    } else {
      setLoading(true);
      let userIds = unref(idList).join(',');
      try {
        await unlinkDepartUserBatch({ depId: departId.value, userIds }, confirm);
        return reload();
      } finally {
        setLoading(false);
      }
    }
    return Promise.reject();
  }

  // 批量取消关联事件
  async function onUnlinkDepartUserBatch() {
    try {
      await unlinkDepartUser(selectedRowKeys, true);
      // 批量删除成功后清空选择
      clearSelection();
    } catch (e) {}
  }

  // 选择用户成功
  async function onSelectUserOk(options, userIdList) {
    if (userIdList.length > 0) {
      try {
        setLoading(true);
        await linkDepartUserBatch(departId.value, userIdList);
        reload();
      } finally {
        setLoading(false);
      }
    }
  }

  /**
   * 用户抽屉表单成功回调
   */
  function onUserDrawerSuccess({ isUpdate, values }) {
    isUpdate ? updateTableDataRecord(values.id, values) : reload();
  }

  /**
   * 操作栏
   */
  function getTableAction(record): ActionItem[] {
    return [{ label: '编辑', onClick: editUserInfo.bind(null, record) }];
  }

  /**
   * 下拉操作栏
   */
  function getDropDownAction(record): ActionItem[] {
    return [
      { label: '部门角色', onClick: showDepartRole.bind(null, record) },
      { label: '用户详情', onClick: showUserDetail.bind(null, record) },
      {
        label: '取消关联',
        color: 'error',
        popConfirm: {
          title: '确认取消关联吗？',
          confirm: unlinkDepartUser.bind(null, [record.id], false),
        },
      },
    ];
  }
</script>
