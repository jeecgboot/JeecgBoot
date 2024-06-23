<template>
  <!--引用表格-->
  <BasicTable @register="registerTable" :rowSelection="rowSelection">
    <!--插槽:table标题-->
    <template #tableTitle>
      <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleCreate"> 新增</a-button>
      <a-button type="primary" preIcon="ant-design:export-outlined" @click="onExportXls"> 导出</a-button>
      <j-upload-button type="primary" preIcon="ant-design:import-outlined" @click="onImportXls">导入</j-upload-button>
      <a-button type="primary" @click="handlerRefreshCache" preIcon="ant-design:sync-outlined"> 刷新缓存</a-button>
      <a-button type="primary" @click="openRecycleModal(true)" preIcon="ant-design:hdd-outlined"> 回收站</a-button>

      <a-dropdown v-if="selectedRowKeys.length > 0">
        <template #overlay>
          <a-menu>
            <a-menu-item key="1" @click="batchHandleDelete">
              <Icon icon="ant-design:delete-outlined"></Icon>
              删除
            </a-menu-item>
          </a-menu>
        </template>
        <a-button
          >批量操作
          <Icon icon="ant-design:down-outlined"></Icon>
        </a-button>
      </a-dropdown>
    </template>
    <!--操作栏-->
    <template #action="{ record }">
      <TableAction :actions="getTableAction(record)" />
    </template>
  </BasicTable>
  <!--字典弹窗-->
  <DictModal @register="registerModal" @success="handleSuccess" />
  <!--字典配置抽屉-->
  <DictItemList @register="registerDrawer" />
  <!--回收站弹窗-->
  <DictRecycleBinModal @register="registerModal1" @success="reload" />
</template>

<script lang="ts" name="system-dict" setup>
  //ts语法
  import { ref, computed, unref } from 'vue';
  import { BasicTable, TableAction } from '/src/components/Table';
  import { useDrawer } from '/src/components/Drawer';
  import { useModal } from '/src/components/Modal';
  import DictItemList from './components/DictItemList.vue';
  import DictModal from './components/DictModal.vue';
  import DictRecycleBinModal from './components/DictRecycleBinModal.vue';
  import { useMessage } from '/src/hooks/web/useMessage';
  import { removeAuthCache, setAuthCache } from '/src/utils/auth';
  import { columns, searchFormSchema } from './dict.data';
  import { list, deleteDict, batchDeleteDict, getExportUrl, getImportUrl, refreshCache, queryAllDictItems } from './dict.api';
  import { DB_DICT_DATA_KEY } from '/src/enums/cacheEnum';
  import { useUserStore } from '/@/store/modules/user';

  const { createMessage } = useMessage();
  //字典model
  const [registerModal, { openModal }] = useModal();
  //字典配置drawer
  const [registerDrawer, { openDrawer }] = useDrawer();
  import { useListPage } from '/@/hooks/system/useListPage';

  //回收站model
  const [registerModal1, { openModal: openRecycleModal }] = useModal();

  // 列表页面公共参数、方法
  const { prefixCls, tableContext, onExportXls, onImportXls } = useListPage({
    designScope: 'dict-template',
    tableProps: {
      title: '数据字典',
      api: list,
      columns: columns,
      formConfig: {
        schemas: searchFormSchema,
      },
      actionColumn: {
        width: 240,
      },
    },
    //update-begin---author:wangshuai ---date:20220616  for：[issues/I5AMDD]导入/导出功能，操作后提示没有传递 export.url/import.url 参数------------
    exportConfig: {
      name: '数据字典列表',
      url: getExportUrl,
    },
    importConfig: {
      url: getImportUrl,
    },
    //update-end---author:wangshuai ---date:20220616  for：[issues/I5AMDD]导入/导出功能，操作后提示没有传递 export.url/import.url 参数--------------
  });

  //注册table数据
  const [registerTable, { reload, updateTableDataRecord }, { rowSelection, selectedRowKeys }] = tableContext;

  /**
   * 新增事件
   */
  function handleCreate() {
    openModal(true, {
      isUpdate: false,
    });
  }
  /**
   * 编辑事件
   */
  async function handleEdit(record: Recordable) {
    openModal(true, {
      record,
      isUpdate: true,
    });
  }
  /**
   * 详情
   */
  async function handleDetail(record) {
    openModal(true, {
      record,
      isUpdate: true,
    });
  }
  /**
   * 删除事件
   */
  async function handleDelete(record) {
    await deleteDict({ id: record.id }, reload);
  }
  /**
   * 批量删除事件
   */
  async function batchHandleDelete() {
    await batchDeleteDict({ ids: selectedRowKeys.value }, reload);
  }
  /**
   * 成功回调
   */
  function handleSuccess({ isUpdate, values }) {
    if (isUpdate) {
      updateTableDataRecord(values.id, values);
    } else {
      reload();
    }
  }
  /**
   * 刷新缓存
   */
  async function handlerRefreshCache() {
    const result = await refreshCache();
    if (result.success) {
      const res = await queryAllDictItems();
      removeAuthCache(DB_DICT_DATA_KEY);
      // update-begin--author:liaozhiyang---date:20230908---for：【QQYUN-6417】生产环境字典慢的问题
      const userStore = useUserStore();
      userStore.setAllDictItems(res.result);
      // update-end--author:liaozhiyang---date:20230908---for：【QQYUN-6417】生产环境字典慢的问题
      createMessage.success('刷新缓存完成！');
    } else {
      createMessage.error('刷新缓存失败！');
    }
  }
  /**
   * 字典配置
   */
  function handleItem(record) {
    openDrawer(true, {
      id: record.id,
    });
  }
  /**
   * 操作栏
   */
  function getTableAction(record) {
    return [
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
      },
      {
        label: '字典配置',
        onClick: handleItem.bind(null, record),
      },
      {
        label: '删除',
        popConfirm: {
          title: '确定删除吗?',
          confirm: handleDelete.bind(null, record),
        },
      },
    ];
  }
</script>

<style scoped></style>
