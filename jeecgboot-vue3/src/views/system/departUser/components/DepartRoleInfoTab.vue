<template>
  <!--引用表格-->
  <BasicTable @register="registerTable" :rowSelection="rowSelection">
    <!--插槽:table标题-->
    <template #tableTitle>
      <a-button type="primary" preIcon="ant-design:plus-outlined" @click="addDepartRole">添加部门角色</a-button>
      <template v-if="selectedRowKeys.length > 0">
        <a-divider type="vertical" />
        <a-dropdown>
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="onDeleteDepartRoleBatch">
                <icon icon="ant-design:delete-outlined" />
                <span>删除</span>
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
  <!-- 添加部门弹窗 -->
  <DepartRoleModal :departId="departId" @register="registerFormModal" @success="onFormModalSuccess" />
  <DepartRoleAuthDrawer @register="registerAuthDrawer" />
</template>

<script lang="ts" setup>
  import { inject, ref, unref, watch, computed, onMounted } from 'vue';

  import { ActionItem, BasicTable, TableAction } from '/@/components/Table';
  import { useModal } from '/@/components/Modal';
  import { useDrawer } from '/@/components/Drawer';
  import { useListPage } from '/@/hooks/system/useListPage';

  import DepartRoleModal from './DepartRoleModal.vue';
  import DepartRoleAuthDrawer from './DepartRoleAuthDrawer.vue';
  import { deleteBatchDepartRole, departRoleList } from '../depart.user.api';
  import { departRoleColumns, departRoleSearchFormSchema } from '../depart.user.data';
  import { ColEx } from '/@/components/Form/src/types';

  const prefixCls = inject('prefixCls');
  const props = defineProps({
    data: { require: true, type: Object },
  });
  defineEmits(['register']);
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
      api: departRoleList,
      columns: departRoleColumns,
      canResize: false,
      formConfig: {
        labelWidth: 100,
        schemas: departRoleSearchFormSchema,
        baseColProps: adaptiveColProps,
        labelAlign: 'left',
        labelCol: {
          xs: 24,
          sm: 24,
          md: 24,
          lg: 9,
          xl: 7,
          xxl: 6,
        },
        wrapperCol: {},
        // 操作按钮配置
        actionColOptions: {
          ...adaptiveColProps,
          style: { textAlign: 'left' },
        },
      },
      // 【issues/1064】列设置的 cacheKey
      tableSetting: { cacheKey: 'depart_user_departInfo' },
      // 请求之前对参数做处理
      beforeFetch(params) {
        params.deptId = departId.value;
      },
      // update-begin--author:liaozhiyang---date:20240517---for：【TV360X-53】未选择部门的情况下，部门角色全查出来了
      immediate: !!departId.value,
      // update-end--author:liaozhiyang---date:20240517---for：【TV360X-53】未选择部门的情况下，部门角色全查出来了
    },
  });

  // 注册 ListTable
  const [registerTable, { reload, setProps, setLoading, updateTableDataRecord }, { rowSelection, selectedRowKeys }] = tableContext;

  // 注册Form弹窗
  const [registerFormModal, formModal] = useModal();
  // 注册授权弹窗抽屉
  const [registerAuthDrawer, authDrawer] = useDrawer();

  // 监听 data 更改，重新加载数据
  watch(
    () => props.data,
    () => reload()
  );
  onMounted(() => {
    // update-begin--author:liaozhiyang---date:20240517---for：【TV360X-53】未选择部门的情况下，部门角色全查出来了
    // reload();
    // update-end--author:liaozhiyang---date:20240517---for：【TV360X-53】未选择部门的情况下，部门角色全查出来了
  });

  // 清空选择的行
  function clearSelection() {
    selectedRowKeys.value = [];
  }

  // 添加部门角色
  function addDepartRole() {
    formModal.openModal(true, {
      isUpdate: false,
      record: {},
    });
  }

  // 编辑部门角色
  function editDepartRole(record) {
    formModal.openModal(true, {
      isUpdate: true,
      record: record,
    });
  }

  // 授权部门角色
  function permissionDepartRole(record) {
    authDrawer.openDrawer(true, { record });
  }

  // 批量删除部门角色
  async function deleteDepartRole(idList, confirm) {
    if (!departId.value) {
      createMessage.warning('请先选择一个部门');
    } else {
      setLoading(true);
      let ids = unref(idList).join(',');
      try {
        await deleteBatchDepartRole({ ids }, confirm);
        return reload();
      } finally {
        setLoading(false);
      }
    }
    return Promise.reject();
  }

  // 批量删除部门角色事件
  async function onDeleteDepartRoleBatch() {
    try {
      await deleteDepartRole(selectedRowKeys, true);
      // 批量删除成功后清空选择
      clearSelection();
    } catch (e) {}
  }

  // 表单弹窗成功后的回调
  function onFormModalSuccess({ isUpdate, values }) {
    isUpdate ? updateTableDataRecord(values.id, values) : reload();
  }

  /**
   * 操作栏
   */
  function getTableAction(record): ActionItem[] {
    return [{ label: '编辑', onClick: editDepartRole.bind(null, record) }];
  }

  /**
   * 下拉操作栏
   */
  function getDropDownAction(record): ActionItem[] {
    return [
      { label: '授权', onClick: permissionDepartRole.bind(null, record) },
      {
        label: '删除',
        color: 'error',
        popConfirm: {
          title: '确认要删除吗？',
          confirm: deleteDepartRole.bind(null, [record.id], false),
        },
      },
    ];
  }
</script>
