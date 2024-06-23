<template>
  <div class="p-4">
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <template #tableTitle>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleCreate"> 新增菜单</a-button>
        <a-button type="primary" preIcon="ic:round-expand" @click="expandAll">展开全部</a-button>
        <a-button type="primary" preIcon="ic:round-compress" @click="collapseAll">折叠全部</a-button>

        <a-dropdown v-if="checkedKeys.length > 0">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="batchHandleDelete">
                <Icon icon="ant-design:delete-outlined" />
                删除
              </a-menu-item>
            </a-menu>
          </template>
          <a-button
            >批量操作
            <Icon icon="ant-design:down-outlined" />
          </a-button>
        </a-dropdown>
      </template>
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" :dropDownActions="getDropDownAction(record)" />
      </template>
    </BasicTable>
    <MenuDrawer @register="registerDrawer" @success="handleSuccess" :showFooter="showFooter" />
    <DataRuleList @register="registerDrawer1" />
  </div>
</template>
<script lang="ts" name="system-menu" setup>
  import { nextTick, ref } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { useListPage } from '/@/hooks/system/useListPage';
  import { useDrawer } from '/@/components/Drawer';
  import MenuDrawer from './MenuDrawer.vue';
  import DataRuleList from './DataRuleList.vue';
  import { columns,searchFormSchema } from './menu.data';
  import { list, deleteMenu, batchDeleteMenu } from './menu.api';
  import { useDefIndexStore } from "@/store/modules/defIndex";
  import { useI18n } from "/@/hooks/web/useI18n";

  const checkedKeys = ref<Array<string | number>>([]);
  const showFooter = ref(true);
  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerDrawer1, { openDrawer: openDataRule }] = useDrawer();
  const { t } = useI18n();

  // 自定义菜单名称列渲染
  columns[0].customRender = function ({text, record}) {
    const isDefIndex = checkDefIndex(record)
    if (isDefIndex) {
      text += '（默认首页）'
    }
    // update-begin--author:liaozhiyang---date:20240306---for：【QQYUN-8379】菜单管理页菜单国际化
    if (text.includes("t('") && t) {
      return new Function('t', `return ${text}`)(t);
    }
    // update-end--author:liaozhiyang---date:20240306---for：【QQYUN-8379】菜单管理页菜单国际化
    return text
  }

  // 列表页面公共参数、方法
  const { prefixCls, tableContext } = useListPage({
    tableProps: {
      title: '菜单列表',
      api: list,
      columns: columns,
      size: 'small',
      pagination: false,
      isTreeTable: true,
      striped: true,
      useSearchForm: true,
      showTableSetting: true,
      bordered: true,
      showIndexColumn: false,
      tableSetting: { fullScreen: true },
      formConfig: {
        // update-begin--author:liaozhiyang---date:20230803---for：【QQYUN-5873】查询区域lablel默认居左
        labelWidth:60,
        owProps: { gutter: 24 },
        // update-end--author:liaozhiyang---date:20230803---for：【QQYUN-5873】查询区域lablel默认居左
        schemas: searchFormSchema,
        autoAdvancedCol: 4,
        baseColProps: { xs: 24, sm: 12, md: 6, lg: 6, xl: 6, xxl: 6 },
        actionColOptions: { xs: 24, sm: 12, md: 6, lg: 6, xl: 6, xxl: 6 },
      },
      actionColumn: {
        width: 120,
      },
    },
  });
  //注册table数据
  const [registerTable, { reload, expandAll, collapseAll }] = tableContext;

  /**
   * 选择列配置
   */
  const rowSelection = {
    type: 'checkbox',
    columnWidth: 30,
    selectedRowKeys: checkedKeys,
    onChange: onSelectChange,
  };

  /**
   * 选择事件
   */
  function onSelectChange(selectedRowKeys: (string | number)[]) {
    checkedKeys.value = selectedRowKeys;
  }

  /**
   * 新增
   */
  function handleCreate() {
    showFooter.value = true;
    openDrawer(true, {
      isUpdate: false,
    });
  }

  /**
   * 编辑
   */
  function handleEdit(record) {
    showFooter.value = true;
    openDrawer(true, {
      record,
      isUpdate: true,
    });
  }
  /**
   * 详情
   */
  function handleDetail(record) {
    showFooter.value = false;
    openDrawer(true, {
      record,
      isUpdate: true,
    });
  }
  /**
   * 添加下级
   */
  function handleAddSub(record) {
    openDrawer(true, {
      record: { parentId: record.id, menuType: 1 },
      isUpdate: false,
    });
  }
  /**
   * 数据权限弹窗
   */
  function handleDataRule(record) {
    openDataRule(true, { id: record.id });
  }

  /**
   * 删除
   */
  async function handleDelete(record) {
    await deleteMenu({ id: record.id }, reload);
  }
  /**
   * 批量删除事件
   */
  async function batchHandleDelete() {
    await batchDeleteMenu({ ids: checkedKeys.value }, reload);
  }
  /**
   * 成功回调
   */
  function handleSuccess() {
    reload();
    reloadDefIndex();
  }

  function onFetchSuccess() {
    // 演示默认展开所有表项
    nextTick(expandAll);
  }

  // --------------- begin 默认首页配置 ------------

  const defIndexStore = useDefIndexStore()

  // 设置默认主页
  async function handleSetDefIndex(record: Recordable) {
    defIndexStore.update(record.url, record.component, record.route)
  }

  /**
   * 检查是否为默认主页
   * @param record
   */
  function checkDefIndex(record: Recordable) {
    return defIndexStore.check(record.url)
  }

  // 重新加载默认首页配置
  function reloadDefIndex() {
    try {
      defIndexStore.query();
    } catch (e) {
      console.error(e)
    }
  }

  reloadDefIndex()

  // --------------- end 默认首页配置 ------------

  /**
   * 操作栏
   */
  function getTableAction(record) {
    return [
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
      },
    ];
  }

  /**
   * 下拉操作栏
   */
  function getDropDownAction(record) {
    return [
      // {
      //   label: '详情',
      //   onClick: handleDetail.bind(null, record),
      // },
      {
        label: '添加下级',
        onClick: handleAddSub.bind(null, record),
      },
      {
        label: '数据规则',
        onClick: handleDataRule.bind(null, record),
      },
      {
        label: '设为默认首页',
        onClick: handleSetDefIndex.bind(null, record),
        ifShow: () => !record.internalOrExternal && record.component && !checkDefIndex(record),
      },
      {
        label: '删除',
        color: 'error',
        popConfirm: {
          title: '是否确认删除',
          confirm: handleDelete.bind(null, record),
        },
      },
    ];
  }
</script>
