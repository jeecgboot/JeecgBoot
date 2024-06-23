<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="title" @ok="handleSubmit" width="800px" :showCancelBtn="false" :showOkBtn="false">
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <template #tableTitle>
        <a-button preIcon="ant-design:plus-outlined" type="primary" @click="handleAdd" style="margin-right: 5px" v-if="showPackAddAndEdit">新增 </a-button>
        <a-button
          v-if="selectedRowKeys.length > 0"
          preIcon="ant-design:delete-outlined"
          type="primary"
          @click="handlePackBatch"
          style="margin-right: 5px"
          >批量删除
        </a-button>
      </template>
      <template #action="{ record }">
        <TableAction :actions="getActions(record)" :dropDownActions="getDropDownAction(record)" />
      </template>
    </BasicTable>
  </BasicModal>
  <TenantPackMenuModal @register="registerPackMenu" @success="success" />
  <TenantPackUserModal @register="registerPackUser" @success="success" />
</template>
<script lang="ts" setup name="tenant-pack-modal">
  import { reactive, ref, unref } from 'vue';
  import { BasicModal, useModal, useModalInner } from '/@/components/Modal';
  import { packColumns, userColumns, packFormSchema } from '../tenant.data';
  import { getTenantUserList, leaveTenant, packList, deleteTenantPack } from '../tenant.api';
  import { useListPage } from '/@/hooks/system/useListPage';
  import { BasicTable, TableAction } from '/@/components/Table';
  import TenantPackMenuModal from './TenantPackMenuModal.vue';
  import {Modal} from "ant-design-vue";
  import TenantPackUserModal from './TenantPackUserModal.vue';
  import {useMessage} from "/@/hooks/web/useMessage";

  const [registerPackMenu, { openModal }] = useModal();
  const [registerPackUser, { openModal: packUserOpenModal }] = useModal();
  const tenantId = ref<number>(0);
  // 列表页面公共参数、方法
  const { prefixCls, tableContext } = useListPage({
    designScope: 'tenant-template',
    tableProps: {
      api: packList,
      columns: packColumns,
      immediate: false,
      formConfig: {
        schemas: packFormSchema,
        labelCol: {
          xxl: 8,
        },
        actionColOptions: {
          xs: 24,
          sm: 8,
          md: 8,
          lg: 8,
          xl: 8,
          xxl: 8,
        },
      },
      beforeFetch: (params) => {
        return Object.assign(params, { tenantId: unref(tenantId), packType:'custom' });
      },
    },
  });
  const [registerTable, { reload }, { rowSelection, selectedRowKeys, selectedRows }] = tableContext;
  // Emits声明
  const emit = defineEmits(['register', 'success']);
  //是否显示新增和编辑产品包
  const showPackAddAndEdit = ref<boolean>(false);
  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    tenantId.value = data.tenantId;
    showPackAddAndEdit.value = data.showPackAddAndEdit;
    success();
  });
  //设置标题
  const title = '租户产品包列表';

  //表单提交事件
  async function handleSubmit(v) {
    closeModal();
  }

  function getActions(record) {
    return [
      {
        label: '用户',
        onClick: seeTenantPackUser.bind(null, record),
      },
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
        ifShow: ()=>{ return showPackAddAndEdit.value }
      },
    ];
  }

  /**
   * 成功
   */
  function success() {
    (selectedRowKeys.value = []) && reload();
  }

  /**
   * 编辑
   * @param record
   */
  async function handleEdit(record) {
    openModal(true, {
      isUpdate: true,
      record: record,
      tenantId: unref(tenantId),
      packType:'custom',
      showFooter: true
    });
  }

  //默认系统产品包不允许删除,包含(超级管理员、组织账户管理员、组织应用管理员)
  const packCode = reactive<any>(['superAdmin','accountAdmin','appAdmin']);
  const { createMessage } = useMessage();
  
  /**
   * 删除产品包
   * @param 删除
   */
  async function handleDelete(record) {
    //update-begin---author:wangshuai ---date:20230222  for：系统默认产品包不允许删除------------
    if(packCode.indexOf(record.packCode) != -1){
        createMessage.warning("默认系统产品包不允许删除");
       return;
    }
    //update-end---author:wangshuai ---date:20230222  for：系统默认产品包不允许删除------------
    await deleteTenantPack({ ids: record.id }, success);
  }

  /**
   * 批量删除产品包
   */
  async function handlePackBatch() {
    let value = selectedRows.value;
    if(value && value.length>0){
      for (let i = 0; i < value.length; i++) {
        if(packCode.indexOf(value[i].packCode) != -1){
          createMessage.warning("默认系统产品包不允许删除");
          return;
        }
      }
    }
    Modal.confirm({
      title: '删除租户产品包',
      content: '是否删除租户产品包',
      okText: '确认',
      cancelText: '取消',
      onOk: async () => {
        await deleteTenantPack({ ids: selectedRowKeys.value.join(',')}, success);
      }
    })
  }

  /**
   *
   * 新增表单
   */
  function handleAdd() {
    openModal(true, {
      isUpdate: false,
      tenantId: unref(tenantId),
      packType:'custom',
      showFooter: true
    });
  }

  /**
   * 产品包下面的用户
   * @param record
   */
  function seeTenantPackUser(record) {
    packUserOpenModal(true,{
      record:record
    })
  }

  /**
   * 更多
   * @param record
   */
  function getDropDownAction(record) {
    return [
      {
        label: '详情',
        onClick: handleDetail.bind(null, record),
      },
      {
        label: '删除',
        popConfirm: {
          title: '是否确认删除租户产品包',
          confirm: handleDelete.bind(null, record),
        },
      },
    ]
  }

  /**
   * 详情
   * @param record
   */
  function handleDetail(record) {
    openModal(true, {
      isUpdate: true,
      record: record,
      tenantId: unref(tenantId),
      packType:'custom',
      showFooter: false
    });
  }
</script>
