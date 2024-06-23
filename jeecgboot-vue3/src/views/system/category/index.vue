<template>
  <div>
    <!--引用表格-->
    <BasicTable
      @register="registerTable"
      :rowSelection="rowSelection"
      :expandedRowKeys="expandedRowKeys"
      @expand="handleExpand"
      @fetch-success="onFetchSuccess"
    >
      <!--插槽:table标题-->
      <template #tableTitle>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleCreate"> 新增</a-button>
        <a-button type="primary" preIcon="ant-design:export-outlined" @click="onExportXls"> 导出</a-button>
        <j-upload-button type="primary" preIcon="ant-design:import-outlined" @click="onImportXls">导入</j-upload-button>
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
    <CategoryModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts" name="system-category" setup>
  //ts语法
  import { ref, computed, unref, toRaw, nextTick } from 'vue';
  import { BasicTable, useTable, TableAction } from '/src/components/Table';
  import { useDrawer } from '/src/components/Drawer';
  import CategoryModal from './components/CategoryModal.vue';
  import { useModal } from '/src/components/Modal';
  import { useMethods } from '/src/hooks/system/useMethods';
  import { columns, searchFormSchema } from './category.data';
  import { list, deleteCategory, batchDeleteCategory, getExportUrl, getImportUrl, getChildList, getChildListBatch } from './category.api';
  import { useListPage } from '/@/hooks/system/useListPage';

  const expandedRowKeys = ref([]);
  const { handleExportXls, handleImportXls } = useMethods();
  //字典model
  const [registerModal, { openModal }] = useModal();
  // 列表页面公共参数、方法
  const { prefixCls, onExportXls, onImportXls, tableContext } = useListPage({
    designScope: 'category-template',
    tableProps: {
      title: '分类字典',
      api: list,
      columns: columns,
      actionColumn: {
        width: 180,
      },
      formConfig: {
        schemas: searchFormSchema,
      },
      isTreeTable: true,
    },
    exportConfig: {
      name: '分类字典列表',
      url: getExportUrl,
    },
    importConfig: {
      url: getImportUrl,
    },
  });

  //注册table数据
  const [registerTable, { reload, collapseAll, updateTableDataRecord, findTableDataRecord, getDataSource }, { rowSelection, selectedRowKeys }] =
    tableContext;

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
  async function handleEdit(record) {
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
      hideFooter: true,
    });
  }

  /**
   * 删除事件
   */
  async function handleDelete(record) {
    await deleteCategory({ id: record.id }, importSuccess);
  }

  /**
   * 批量删除事件
   */
  async function batchHandleDelete() {
    const ids = selectedRowKeys.value.filter((item) => !item.includes('loading'));
    await batchDeleteCategory({ ids: ids }, importSuccess);
  }
  /**
   * 导入
   */
  function importSuccess() {
    //update-begin---author:wangshuai ---date:20220530  for：[issues/54]树字典，勾选，然后批量删除，系统错误------------
    (selectedRowKeys.value = []) && reload();
    //update-end---author:wangshuai ---date:20220530  for：[issues/54]树字典，勾选，然后批量删除，系统错误--------------
  }
  /**
   * 添加下级
   */
  function handleAddSub(record) {
    openModal(true, {
      record,
      isUpdate: false,
    });
  }
  /**
   * 成功回调
   */
  async function handleSuccess({ isUpdate,isSubAdd, values, expandedArr }) {
    if (isUpdate) {
      //编辑回调
      updateTableDataRecord(values.id, values);
    } else {
      if (!values['pid']) {
        //新增根节点
        reload();
      } else {
        //新增子集
        //update-begin-author:liusq---date:20230411--for: [issue/4550]分类字典数据量过多会造成数据查询时间过长--- 
        if(isSubAdd){
          await expandTreeNode(values.pid);
        //update-end-author:liusq---date:20230411--for: [issue/4550]分类字典数据量过多会造成数据查询时间过长--- 
        }else{
          //update-begin-author:wangshuai---date:20240319--for: 字典树删除之后其他节点出现loading---
          //expandedRowKeys.value = [];
          //update-end-author:wangshuai---date:20240319--for: 字典树删除之后其他节点出现loading--- 
          for (let key of unref(expandedArr)) {
            await expandTreeNode(key);
          }
        }
      }
    }
  }

  /**
   * 接口请求成功后回调
   */
  function onFetchSuccess(result) {
    getDataByResult(result.items) && loadDataByExpandedRows();
  }
  /**
   * 根据已展开的行查询数据（用于保存后刷新时异步加载子级的数据）
   */
  async function loadDataByExpandedRows() {
    if (unref(expandedRowKeys).length > 0) {
      const res = await getChildListBatch({ parentIds: unref(expandedRowKeys).join(',') });
      if (res.success && res.result.records.length > 0) {
        //已展开的数据批量子节点
        let records = res.result.records;
        const listMap = new Map();
        for (let item of records) {
          let pid = item['pid'];
          if (unref(expandedRowKeys).includes(pid)) {
            let mapList = listMap.get(pid);
            if (mapList == null) {
              mapList = [];
            }
            mapList.push(item);
            listMap.set(pid, mapList);
          }
        }
        let childrenMap = listMap;
        let fn = (list) => {
          if (list) {
            list.forEach((data) => {
              if (unref(expandedRowKeys).includes(data.id)) {
                data.children = getDataByResult(childrenMap.get(data.id));
                fn(data.children);
              }
            });
          }
        };
        fn(getDataSource());
      }
    }
  }
  /**
   * 处理数据集
   */
  function getDataByResult(result) {
    if (result && result.length > 0) {
      return result.map((item) => {
        //判断是否标记了带有子节点
        if (item['hasChild'] == '1') {
          let loadChild = { id: item.id + '_loadChild', name: 'loading...', isLoading: true };
          item.children = [loadChild];
        }
        return item;
      });
    }
  }
  /**
   *树节点展开合并
   * */
  async function handleExpand(expanded, record) {
    // 判断是否是展开状态，展开状态(expanded)并且存在子集(children)并且未加载过(isLoading)的就去查询子节点数据
    if (expanded) {
      expandedRowKeys.value.push(record.id);
      if (record.children.length > 0 && !!record.children[0].isLoading) {
        let result = await getChildList({ pid: record.id });
        if (result && result.length > 0) {
          record.children = getDataByResult(result);
        } else {
          record.children = null;
          record.hasChild = '0';
        }
      }
    } else {
      let keyIndex = expandedRowKeys.value.indexOf(record.id);
      if (keyIndex >= 0) {
        expandedRowKeys.value.splice(keyIndex, 1);
      }
    }
  }
  /**
   *操作表格后处理树节点展开合并
   * */
  async function expandTreeNode(key) {
    let record:any = findTableDataRecord(key);
    //update-begin-author:liusq---date:20230411--for: [issue/4550]分类字典数据量过多会造成数据查询时间过长，显示“接口请求超时,请刷新页面重试!”--- 
    if(!expandedRowKeys.value.includes(key)){
      expandedRowKeys.value.push(key);
    }
    //update-end-author:liusq---date:20230411--for: [issue/4550]分类字典数据量过多会造成数据查询时间过长，显示“接口请求超时,请刷新页面重试!”--- 
    let result = await getChildList({ pid: key });
    if (result && result.length > 0) {
      record.children = getDataByResult(result);
    } else {
      record.children = null;
      record.hasChild = '0';
    }
    updateTableDataRecord(key, record);
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
        label: '删除',
        popConfirm: {
          title: '确定删除吗?',
          confirm: handleDelete.bind(null, record),
        },
      },
      {
        label: '添加下级',
        onClick: handleAddSub.bind(null, { pid: record.id }),
      },
    ];
  }
</script>

<style scoped></style>
