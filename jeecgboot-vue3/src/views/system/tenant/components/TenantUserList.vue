<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="title" @ok="handleSubmit" width="800px">
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <template #tableTitle>
        <a-button v-if="selectedRowKeys.length>0" preIcon="ant-design:delete-outlined" type="primary" @click="handleLeaveBatch" style="margin-right: 5px">批量请离</a-button>
      </template>
      <template #action="{ record }">
        <TableAction :actions="getActions(record)" />
      </template>
    </BasicTable>
  </BasicModal>
</template>
<script lang="ts" setup>
import { ref, computed, unref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form/index';
import { userColumns, userSearchFormSchema } from "../tenant.data";
import { getTenantUserList, leaveTenant } from "../tenant.api";
import { useListPage } from "/@/hooks/system/useListPage";
import { BasicTable, TableAction } from '/@/components/Table';

const tenantId = ref<number>(0);
// 列表页面公共参数、方法
const { prefixCls, tableContext } = useListPage({
  designScope: 'tenant-template',
  tableProps: {
    api: getTenantUserList,
    columns: userColumns,
    immediate:false,
    formConfig: {
      schemas: userSearchFormSchema,
      //update-begin---author:wangshuai ---date:20230704  for：【QQYUN-5698】样式问题------------
      labelWidth: 40,
      //update-end---author:wangshuai ---date:20230704  for：【QQYUN-5698】样式问题------------
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
      return Object.assign(params, { userTenantId: unref(tenantId) });
    },
  },
});
const [registerTable, { reload }, { rowSelection, selectedRowKeys }] = tableContext;

// Emits声明
const emit = defineEmits(['register', 'success']);
//表单赋值
const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
  tenantId.value = data.id;
  success();
});
//设置标题
const title = '成员';
//表单提交事件
async function handleSubmit(v) {
  closeModal();
}

function getActions(record) {
  return [
    {
      label: '移除',
      onClick: handleLeave.bind(null, record.id),
    },
  ]
}

/**
 * 成功
 */
function success() {
  (selectedRowKeys.value = []) && reload();
}

/**
 * 请离
 * @param id
 */
async function handleLeave(id) {
  await leaveTenant({userIds:id,tenantId:unref(tenantId)},success)
}

/**
 * 批量请离
 */
async function  handleLeaveBatch(){
  await leaveTenant({userIds:selectedRowKeys.value.join(","),tenantId:unref(tenantId)},success)
}
</script>

<style scoped>
  /*update-begin---author:wangshuai ---date:20220705  for：查询组件input有后缀，隐藏掉------------*/
  :deep(.ant-input-suffix){
    display: none;
  }
  /*update-begin---author:wangshuai ---date:20220705  for：查询组件input有后缀，隐藏掉------------*/
</style>
