<template>
  <div class="p-4">
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <template #tableTitle>
        <a-dropdown>
          <template #overlay>
            <a-menu @click="handleCreate">
              <a-menu-item :key="1">一对一示例</a-menu-item>
              <a-menu-item :key="2">一对多示例</a-menu-item>
              <a-menu-item :key="3">一对多(JVexTable)</a-menu-item>
            </a-menu>
          </template>
          <a-button type="primary">新增 <DownOutlined /></a-button>
        </a-dropdown>
      </template>
      <template #ctype="{ text }">
        {{ text === '1' ? '国内订单' : text === '2' ? '国际订单' : text }}
      </template>
      <template #action="{ record }">
        <TableAction :actions="getAction(record)" :dropDownActions="getDropDownActions(record)" />
      </template>
    </BasicTable>
    <!--        <TableDrawer @register="registerDrawer" @success="handleSuccess" />-->
    <TableModal @register="registerModal" @success="handleSuccess" />
    <JVxeTableModal @register="registerVexTableModal" @success="handleSuccess"></JVxeTableModal>
    <OneToOneModal @register="registerOneToOneModal" @success="handleSuccess"></OneToOneModal>
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import TableDrawer from './drawer.vue';
  import TableModal from './modal.vue';
  import VexTableModal from './VexTableModal.vue';
  import JVxeTableModal from './jvxetable/JVxeTableModal.vue';
  import OneToOneModal from './OneToOneModal.vue';
  import { DownOutlined } from '@ant-design/icons-vue';
  import { useListPage } from '/@/hooks/system/useListPage';

  import { useModal } from '/@/components/Modal';
  import { columns } from './data';
  import { list, deleteOne } from './api';
  import { defHttp } from '/@/utils/http/axios';

  const [registerModal, { openModal }] = useModal();
  const [registerOneToOneModal, { openModal: openOneToOneModal }] = useModal();
  const [registerVexTableModal, { openModal: openVexTableModal }] = useModal();

  //定义表格行操作
  const getAction = (record) => {
    return [
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
      },
    ];
  };

  const getDropDownActions = (record) => {
    let arr = [
      {
        label: '删除',
        popConfirm: {
          title: '是否删除？',
          confirm: handleDelete.bind(null, record),
        },
      },
    ];
    return arr;
  };

  // 列表页面公共参数、方法
  const { tableContext } = useListPage({
    tableProps: {
      api: list,
      columns: columns,
      useSearchForm: false,
      actionColumn: {
        width: 160,
        title: '操作',
        dataIndex: 'action',
        slots: { customRender: 'action' },
      },
    },
  });

  //注册table数据
  const [registerTable, { reload }, { rowSelection }] = tableContext;
  //新增类型
  //update-begin---author:wangshuai ---date:20220720  for：[VUEN-1661]一对多示例，编辑的时候，有时候是一对一，有时候是一对多，默认一对多------------
  const addType = ref(3);
  //update-end---author:wangshuai ---date:20220720  for：[VUEN-1661]一对多示例，编辑的时候，有时候是一对一，有时候是一对多，默认一对多--------------
  //添加事件
  function handleCreate(e) {
    addType.value = e.key;
    let type = addType.value;
    if (type == 1) {
      openOneToOneModal(true, {
        isUpdate: false,
      });
    }
    if (type == 2) {
      openModal(true, {
        isUpdate: false,
      });
    }
    if (type == 3) {
      openVexTableModal(true, {
        isUpdate: false,
      });
    }
  }

  //编辑事件
  function handleEdit(record: Recordable) {
    let type = addType.value;
    if (type == 1) {
      openOneToOneModal(true, {
        record,
        isUpdate: true,
      });
    }
    if (type == 2) {
      openModal(true, {
        record,
        isUpdate: true,
      });
    }
    if (type == 3) {
      openVexTableModal(true, {
        record,
        isUpdate: true,
      });
    }
  }

  async function handleDelete(record: Recordable) {
    await deleteOne({ id: record.id }, reload);
  }

  function handleSuccess() {
    reload();
  }
</script>
