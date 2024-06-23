<template>
  <BasicTable @register="registerTable" :rowSelection="rowSelection">
    <template #action="{ record }">
      <TableAction :actions="getTableAction(record)" />
    </template>
  </BasicTable>
</template>

<script lang="ts" name="online-user" setup>
import { BasicTable, TableAction } from '/@/components/Table';
import { columns, searchFormSchema } from './OnlineUser.data';
import { list, forceLogout } from './OnlineUser.api';
import { useListPage } from '/@/hooks/system/useListPage';
import { useMessage } from "/@/hooks/web/useMessage";
// 列表页面公共参数、方法
const { prefixCls, tableContext, onImportXls, onExportXls } = useListPage({
  designScope: 'online-user',
  tableProps: {
    //在线用户rowKey默认id会造成key重复，导致页面出现重复数据
    rowKey:'token',
    title: '在线用户',
    api: list,
    columns: columns,
    formConfig: {
      schemas: searchFormSchema,
    },
    actionColumn: {
      width: 120,
    },
    rowSelection: null,
  },
});
const [registerTable, { reload }, { rowSelection, selectedRowKeys }] = tableContext;
const $message = useMessage();

//操作栏
function getTableAction(record) {
  return [
    {
      label: '强退',
      popConfirm: {
        title: '强制退出用户？',
        confirm: handleForce.bind(null, record),
      },
    },
  ];
}

/**
 * 强退
 * @param record
 */
function handleForce(record) {
   forceLogout({ token: record.token }).then((res)=>{
     if(res.success){
       reload();
       $message.createMessage.success('强制退出用户”'+record.realname+'“成功！');
     }else{
       $message.createMessage.warn(res.message);
     }
   })
}
</script>

<style scoped>

</style>