<template>
  <div class="p-4">
    <BasicTable @register="registerTable">
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              label: '编辑',
              onClick: handleEdit.bind(null, record),
              auth: 'demo:btn:show', // 根据权限控制是否显示: 无权限，不显示
            },
            {
              label: '删除',
              icon: 'ic:outline-delete-outline',
              onClick: handleDelete.bind(null, record),
              auth: 'super', // 根据权限控制是否显示: 有权限，会显示
            },
          ]"
          :dropDownActions="[
            {
              label: '启用',
              popConfirm: {
                title: '是否启用？',
                confirm: handleOpen.bind(null, record),
              },
              ifShow: (_action) => {
                return record.status !== 'enable'; // 根据业务控制是否显示: 非enable状态的不显示启用按钮
              },
            },
            {
              label: '禁用',
              popConfirm: {
                title: '是否禁用？',
                confirm: handleOpen.bind(null, record),
              },
              ifShow: () => {
                return record.status === 'enable'; // 根据业务控制是否显示: enable状态的显示禁用按钮
              },
            },
            {
              label: '同时控制',
              popConfirm: {
                title: '是否动态显示？',
                confirm: handleOpen.bind(null, record),
              },
              auth: 'super', // 同时根据权限和业务控制是否显示
              ifShow: () => {
                return true;
              },
            },
          ]"
        />
      </template>
    </BasicTable>
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicTable, useTable, BasicColumn, TableAction } from '/@/components/Table';

  import { demoListApi } from '/@/api/demo/table';
  import { useListPage } from '/@/hooks/system/useListPage';
  const columns: BasicColumn[] = [
    {
      title: '编号',
      dataIndex: 'no',
      width: 100,
    },
    {
      title: '姓名',
      dataIndex: 'name',
      auth: 'demo:field:show', // 根据权限控制是否显示: 无权限，不显示
    },
    {
      title: '状态',
      dataIndex: 'status',
    },
    {
      title: '地址',
      dataIndex: 'address',
      auth: 'super', // 同时根据权限和业务控制是否显示
      ifShow: (_column) => {
        return true;
      },
    },
    {
      title: '开始时间',
      dataIndex: 'beginTime',
    },
    {
      title: '结束时间',
      dataIndex: 'endTime',
      width: 200,
    },
  ];
  export default defineComponent({
    components: { BasicTable, TableAction },
    setup() {
      const { tableContext } = useListPage({
        tableProps: {
          title: '权限列',
          api: demoListApi,
          columns: columns,
          bordered: true,
          useSearchForm: false,
          actionColumn: {
            width: 250,
            title: 'Action',
            dataIndex: 'action',
            slots: { customRender: 'action' },
          },
        },
      });

      //注册table数据
      const [registerTable] = tableContext;

      function handleEdit(record: Recordable) {
        console.log('点击了编辑', record);
      }
      function handleDelete(record: Recordable) {
        console.log('点击了删除', record);
      }
      function handleOpen(record: Recordable) {
        console.log('点击了启用', record);
      }
      return {
        registerTable,
        handleEdit,
        handleDelete,
        handleOpen,
      };
    },
  });
</script>
