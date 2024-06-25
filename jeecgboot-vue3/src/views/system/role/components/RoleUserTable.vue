<template>
  <BasicDrawer @register="registerBaseDrawer" title="角色用户" width="800" destroyOnClose>
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <template #tableTitle>
        <a-button type="primary" @click="handleCreate" v-if="!disableUserEdit"> 新增用户</a-button>
        <a-button type="primary" @click="handleSelect"> 已有用户</a-button>

        <a-dropdown v-if="checkedKeys.length > 0">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="batchHandleDelete">
                <Icon icon="bx:bx-unlink"></Icon>
                取消关联
              </a-menu-item>
            </a-menu>
          </template>
          <a-button
            >批量操作
            <Icon icon="ant-design:down-outlined"></Icon>
          </a-button>
        </a-dropdown>
      </template>
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" />
      </template>
    </BasicTable>
    <!--用户操作抽屉-->
    <UserDrawer @register="registerDrawer" @success="handleSuccess" />
    <!--用户选择弹窗-->
    <UseSelectModal @register="registerModal" @select="selectOk" />
  </BasicDrawer>
</template>
<script lang="ts" setup>
  import { ref, watch, unref } from 'vue';
  import { BasicTable, useTable, TableAction } from '/src/components/Table';
  import { BasicDrawer, useDrawer, useDrawerInner } from '/src/components/Drawer';
  import { useModal } from '/src/components/Modal';
  import UserDrawer from '../../user/UserDrawer.vue';
  import UseSelectModal from './UseSelectModal.vue';
  import { userList, deleteUserRole, batchDeleteUserRole, addUserRole } from '../role.api';
  import { userColumns, searchUserFormSchema } from '../role.data';
  import { getUserRoles } from '../../user/user.api';

  const emit = defineEmits(['register', 'hideUserList']);
  const props = defineProps({
    disableUserEdit: {type:Boolean,default:false}
  })
  
  const checkedKeys = ref<Array<string | number>>([]);
  const roleId = ref('');
  const [registerBaseDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    roleId.value = data.id;
    setProps({ searchInfo: { roleId: data.id } });
    reload();
  });
  //注册drawer
  const [registerDrawer, { openDrawer }] = useDrawer();
  //注册drawer
  const [registerModal, { openModal }] = useModal();
  const [registerTable, { reload, updateTableDataRecord, setProps }] = useTable({
    title: '用户列表',
    api: userList,
    columns: userColumns,
    formConfig: {
      //update-begin---author:wangshuai ---date:20230703  for：【QQYUN-5685】3、租户角色下,查询居左显示
      labelWidth: 60,
      //update-end---author:wangshuai ---date:20230703  for：【QQYUN-5685】3、租户角色下,查询居左显示
      schemas: searchUserFormSchema,
      autoSubmitOnEnter: true,
    },
    striped: true,
    useSearchForm: true,
    showTableSetting: true,
    clickToRowSelect: false,
    bordered: true,
    showIndexColumn: false,
    // 【issues/1064】列设置的 cacheKey
    tableSetting: { fullScreen: true, cacheKey: 'role_user_table' },
    canResize: false,
    rowKey: 'id',
    actionColumn: {
      width: 180,
      title: '操作',
      dataIndex: 'action',
      slots: { customRender: 'action' },
      fixed: undefined,
    },
  });

  /**
   * 选择列配置
   */
  const rowSelection = {
    type: 'checkbox',
    columnWidth: 50,
    selectedRowKeys: checkedKeys,
    onChange: onSelectChange,
  };

  /**
   * 选择事件
   */
  function onSelectChange(selectedRowKeys: (string | number)[], selectionRows) {
    checkedKeys.value = selectedRowKeys;
  }

  /**
   * 新增
   */
  function handleCreate() {
    openDrawer(true, {
      isUpdate: false,
      selectedroles: [roleId.value],
      isRole: true,
    });
  }
  /**
   * 编辑
   */
  async function handleEdit(record: Recordable) {
    try {
      const userRoles = await getUserRoles({ userid: record.id });
      if (userRoles && userRoles.length > 0) {
        record.selectedroles = userRoles;
      }
    } catch (e) {
      console.log(e);
    }
    openDrawer(true, {
      record,
      isUpdate: true,
      isRole: true,
    });
  }

  /**
   * 删除事件
   */
  async function handleDelete(record) {
    await deleteUserRole({ userId: record.id, roleId: roleId.value }, reload);
  }

  /**
   * 批量删除事件
   */
  async function batchHandleDelete() {
    await batchDeleteUserRole({ userIds: checkedKeys.value.join(','), roleId: roleId.value }, reload);
  }

  /**
   * 成功回调
   */
  function handleSuccess({ isUpdate, values }) {
    isUpdate ? updateTableDataRecord(values.id, values) : reload();
  }
  /**
   * 选择已有用户
   */
  function handleSelect() {
    openModal(true);
  }
  /**
   * 添加已有用户
   */
  async function selectOk(val) {
    await addUserRole({ roleId: roleId.value, userIdList: val }, reload);
  }
  /**
   * 操作栏
   */
  function getTableAction(record) {
    return [
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
        ifShow: () => !props.disableUserEdit,
      },
      {
        label: '取消关联',
        popConfirm: {
          title: '是否确认取消关联',
          confirm: handleDelete.bind(null, record),
        },
      },
    ];
  }
</script>

<style scoped>
  /*update-begin---author:wangshuai ---date:20230703  for：【QQYUN-5685】3、租户角色下,查询居左显示*/
  :deep(.ant-form-item-control-input-content){
    text-align: left;
  }
  /*update-end---author:wangshuai ---date:20230703  for：【QQYUN-5685】3、租户角色下,查询居左显示*/
</style>
