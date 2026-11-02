<template>
  <BasicModal
    title="选择用户"
    @register="registerModal"
    width="100%"
    wrapClassName="tabs-select-user-modal"
    style="top: 10px"
    @ok="handleSelectSuccess"
    :canFullscreen="true"
    keyboard
    defaultFullscreen
  >
    <a-row>
      <!-- 左侧多tabs切换 -->
      <a-col :xs="24" :sm="6">
        <a-card class="left-select-card" :bordered="true" :bodyStyle="{ overflow: 'hidden', padding: 0 }">
          <a-tabs class="left-select-tabs" v-model:activeKey="activeTab" size="small" centered :tabBarStyle="{ margin: 10 }" @change="onTabChange">
            <!-- 部门标签页 -->
            <a-tab-pane key="depart" tab="部门">
              <div class="left-tab-content">
                <a-alert type="info" :showIcon="true" class="alert-info">
                  <template #message>
                    <div class="alert-message-content">
                      <span class="alert-label">当前选择：</span>
                      <span v-if="departInfo.currentSelectRow.title" class="selected-title">{{ departInfo.currentSelectRow.title }}</span>
                      <a v-if="departInfo.currentSelectRow.title" class="clear-link" @click="onClearSelectedDepart">取消选择</a>
                    </div>
                  </template>
                </a-alert>
                <a-input-search placeholder="按部门名称搜索…" :allowClear="true" style="margin-bottom: 10px" @search="onSearchDepart" v-model:value="departSearchText" />
                <!--组织机构-->
                <div class="tree-container" v-if="departInfo.treeData && departInfo.treeData.length > 0">
                  <a-directory-tree
                    selectable
                    :selectedKeys="departInfo.selectedKeys"
                    :checkStrictly="true"
                    @select="onSelectDepart"
                    :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                    :load-data="onLoadTreeData"
                    :treeData="departInfo.treeData"
                    :showIcon="false"
                    :expandedKeys="expandedDepartKeys"
                    @expand="onDepartExpand"
                  >
                    <template #title="{ orgCategory, title }">
                      <TreeIcon :orgCategory="orgCategory" :title="title"></TreeIcon>
                    </template>
                  </a-directory-tree>
                </div>
                <a-empty v-else description="无部门信息" />
              </div>
            </a-tab-pane>

            <!-- 岗位标签页 -->
            <a-tab-pane key="position" tab="岗位">
              <div class="left-tab-content">
                <a-alert type="info" :showIcon="true" class="alert-info">
                  <template #message>
                    <div class="alert-message-content">
                      <span class="alert-label">当前选择：</span>
                      <span v-if="positionInfo.currentSelectRow.title" class="selected-title">{{ positionInfo.currentSelectRow.title }}</span>
                      <a v-if="positionInfo.currentSelectRow.title" class="clear-link" @click="onClearSelectedPosition">取消选择</a>
                    </div>
                  </template>
                </a-alert>
                <a-input-search
                  placeholder="按岗位名称搜索…"
                  style="margin-bottom: 10px"
                  allowClear
                  @search="onSearchPosition"
                  v-model:value="positionSearchText"
                />
                <!--岗位列表-->
                <div class="position-list">
                  <PostRankRelation :treeData="positionInfo.treeData" @select="onSelectPosition" />
                </div>
              </div>
            </a-tab-pane>

            <!-- 用户组标签页 -->
            <a-tab-pane key="userGroup" tab="用户组">
              <div class="left-tab-content">
                <a-alert type="info" :showIcon="true" class="alert-info">
                  <template #message>
                    <div class="alert-message-content">
                      <span class="alert-label">当前选择：</span>
                      <span v-if="userGroupInfo.currentSelectRow.title" class="selected-title">{{ userGroupInfo.currentSelectRow.title }}</span>
                      <a v-if="userGroupInfo.currentSelectRow.title" class="clear-link" @click="onClearSelectedUserGroup">取消选择</a>
                    </div>
                  </template>
                </a-alert>
                <a-input-search
                  placeholder="按用户组名称搜索…"
                  style="margin-bottom: 10px"
                  allowClear
                  @search="onSearchUserGroup"
                  v-model:value="userGroupSearchText"
                />
                <!--用户组列表-->
                <div class="user-group-list" v-if="userGroupInfo.data && userGroupInfo.data.length > 0">
                  <div
                    v-for="item in userGroupInfo.data"
                    :key="item.id"
                    class="user-group-item"
                    :class="{ 'user-group-item-selected': userGroupInfo.selectedKeys.includes(item.id) }"
                    @click="onSelectUserGroup(item)"
                  >
                    <span class="user-group-title">{{ item.title }}</span>
                  </div>
                </div>
                <a-empty v-else description="无用户组信息" />
              </div>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </a-col>

      <!-- 中间列表-展示用户信息 -->
      <a-col :xs="24" :sm="showSelected ? 12 : 18">
        <a-card title="选择人员" :bordered="true" :bodyStyle="{ paddingTop: '1px' }">
          <BasicTable @register="registerTable" :rowSelection="rowSelection" />
        </a-card>
      </a-col>

      <!-- 右侧显示已经选中用户，支持调整顺序 -->
      <a-col :xs="24" :sm="6" v-if="showSelected">
        <a-card title="已选用户" :bordered="true">
          <BasicTable @register="registerSelectedUserTable">
            <!--操作栏-->
            <template #action="{ record }">
              <a-button type="primary" size="small" @click="handleDelete(record)" preIcon="ant-design:delete">删除</a-button>
            </template>
          </BasicTable>
        </a-card>
      </a-col>
    </a-row>
  </BasicModal>
</template>

<script lang="ts" setup>
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { ref, unref, reactive, toRaw, watch } from 'vue';
  import {
    getDepartTreeData,
    getDepartUserList,
    getUserList,
    queryALLRankRelation,
    searchByKeywords,
    columns,
    selectedUserColumns,
    searchFormSchema,
  } from './useSelectUser';
  import { BasicTable } from '/@/components/Table';
  import { useListPage } from '/@/hooks/system/useListPage';
  import TreeIcon from '@/components/Form/src/jeecg/components/TreeIcon/TreeIcon.vue';
  import { defHttp } from '/@/utils/http/axios';
  import PostRankRelation from './component/PostRankRelation.vue';

  const props = defineProps({
    multi: {
      type: Boolean,
      default: true,
    },
    showSelected: {
      type: Boolean,
      default: true,
    },
    //回传value字段名
    rowKey: {
      type: String,
      default: 'username',
    },
  });
  const emit = defineEmits(['selected', 'register']);
  const [registerModal, { closeModal }] = useModalInner((data) => {
    showSelectedValue(data);
  });

  // 当前激活的标签页
  const activeTab = ref('depart');
  // 加载状态
  const loading = ref(false);
  // 搜索文本
  const departSearchText = ref('');
  const positionSearchText = ref('');
  const userGroupSearchText = ref('');

  // 原始数据缓存
  const originalDepartData = ref<any[]>([]);
  const originalPositionData = ref<any[]>([]);
  const originalUserGroupData = ref<any[]>([]);
  /*-----------------部门---begin----------------*/
  const departInfo = reactive({
    treeData: [] as any[],
    selectedKeys: [] as any[],
    currentSelectRow: {
      title: '',
    } as any,
  });
  function onSelectDepart(data, { node }) {
    departInfo.selectedKeys[0] = data[0];
    departInfo.currentSelectRow = toRaw(node.dataRef);
    console.log(departInfo);
    reload();
  }
  function onClearSelectedDepart() {
    departInfo.selectedKeys = [];
    departInfo.currentSelectRow = { title: '' };
    reload();
  }
  // 部门树展开的keys
  const expandedDepartKeys = ref<any[]>([]);

  // 部门树展开事件
  function onDepartExpand(expandedKeys) {
    expandedDepartKeys.value = expandedKeys;
    // 延迟触发滚动区域重新计算
    setTimeout(() => {
      const treeContainer = document.querySelector('.tree-container') as HTMLElement | null;
      if (treeContainer) {
        // 强制触发重排
        treeContainer.style.overflow = 'hidden';
        setTimeout(() => {
          treeContainer.style.overflow = 'auto';
        }, 10);
      }
    }, 100);
  }
  async function loadRootDepart() {
    const result = await getDepartTreeData();
    if (Array.isArray(result)) {
      //update-begin-author:liusq---date:2025-12-05--for: JHHB-1175 【流程审批】指定下一步操作人，人员需要按用户的排序进行排序 部门加滚动条 显示部门简称
      result.forEach((item) => {
        item.title = item.departNameAbbr || item.title;
      });
      //update-end-author:liusq---date:2025-12-05--for: JHHB-1175 【流程审批】指定下一步操作人，人员需要按用户的排序进行排序 部门加滚动条 显示部门简称
      departInfo.treeData = result;
      originalDepartData.value = result;
    }
  }

  // 部门搜索
  async function onSearchDepart(value) {
    departSearchText.value = value;
    if (!value) {
      departInfo.treeData = originalDepartData.value;
      return;
    }
    try {
      departInfo.treeData = [];
      let result = await searchByKeywords({ keyWord: value, orgCategory: '1,2' });
      if (Array.isArray(result)) {
        departInfo.treeData = result;
      }
    } finally {
      loading.value = false;
    }
  }
  async function onLoadTreeData(treeNode) {
    try {
      const result = await getDepartTreeData({
        pid: treeNode.dataRef.id,
      });
      if (result.length == 0) {
        treeNode.dataRef.isLeaf = true;
      } else {
        //update-begin-author:liusq---date:2025-12-05--for: JHHB-1175 【流程审批】指定下一步操作人，人员需要按用户的排序进行排序 部门加滚动条 显示部门简称
        result.forEach((item) => {
          item.title = item.departNameAbbr || item.title;
        });
        //update-end-author:liusq---date:2025-12-05--for: JHHB-1175 【流程审批】指定下一步操作人，人员需要按用户的排序进行排序 部门加滚动条 显示部门简称
        treeNode.dataRef.children = result;
      }
      // departInfo.treeData = [...departInfo.treeData]
      // 数据加载完成后，延迟触发滚动区域重新计算
      // setTimeout(() => {
      //   const treeContainer = document.querySelector('.tree-container');
      //   if (treeContainer) {
      //     // 强制触发重排，保持当前滚动位置
      //     const scrollTop = treeContainer.scrollTop;
      //     treeContainer.style.overflow = 'hidden';
      //     setTimeout(() => {
      //       treeContainer.style.overflow = 'auto';
      //       treeContainer.scrollTop = scrollTop;
      //     }, 10);
      //   }
      // }, 50);
    } catch (e) {
      console.error('部门树子节点加载失败', e);
    }
    return Promise.resolve();
  }
  /*-----------------部门---end----------------*/

  /*-----------------岗位---begin----------------*/
  const positionInfo = reactive({
    treeData: [] as any[],
    selectedKeys: [] as any[],
    currentSelectRow: { title: '' } as any,
  });
  // 递归搜索函数，支持搜索所有层级的节点
  const findNodeInTree = (treeData: any[], targetId: any): any => {
    for (const node of treeData) {
      if (node.id == targetId) {
        return node;
      }
      if (node.children && Array.isArray(node.children)) {
        const found = findNodeInTree(node.children, targetId);
        if (found) {
          return found;
        }
      }
    }
    return null;
  };

  function onSelectPosition(data) {
    // 避免循环引用错误，只使用安全的数据
    positionInfo.selectedKeys[0] = data[0];
    let obj = findNodeInTree(positionInfo.treeData, data[0]);
    positionInfo.currentSelectRow = obj ? { ...obj } : { title: '' };
    reload();
  }

  function onClearSelectedPosition() {
    positionInfo.selectedKeys = [];
    positionInfo.currentSelectRow = { title: '' };
    reload();
  }

  async function loadPositionList() {
    try {
      const result = await queryALLRankRelation();
      if (result && Array.isArray(result) && result.length > 0) {
        positionInfo.treeData = result;
        originalPositionData.value = result;
      }
    } catch (error) {
      console.error('获取岗位列表失败', error);
    }
  }
  // 岗位搜索
  function onSearchPosition(value) {
    positionSearchText.value = value;
    if (!value) {
      positionInfo.treeData = originalPositionData.value;
      return;
    }

    const searchText = value.toLowerCase();

    // 递归过滤树：如果节点自身匹配或任一子孙匹配，则保留该节点（并只保留匹配的子节点）
    function filterTree(nodes: any[]): any[] {
      if (!Array.isArray(nodes)) return [];
      const result: any[] = [];
      for (const node of nodes) {
        const title = (node.title || '').toLowerCase();
        let matched = title.includes(searchText);
        let filteredChildren = [];
        if (node.children && Array.isArray(node.children)) {
          filteredChildren = filterTree(node.children);
          if (filteredChildren.length > 0) matched = true;
        }
        if (matched) {
          // 浅拷贝节点，避免修改原始数据
          const newNode: any = { ...node };
          if (filteredChildren.length > 0) {
            newNode.children = filteredChildren;
          } else {
            // 确保在没有匹配子节点时不携带 children 或将其清空
            delete newNode.children;
          }
          result.push(newNode as any);
        }
      }
      return result;
    }

    positionInfo.treeData = filterTree(originalPositionData.value || []);
  }
  /*-----------------岗位---end----------------*/

  /*-----------------用户组---begin----------------*/
  const userGroupInfo = reactive({
    data: [] as any[],
    selectedKeys: [] as string[],
    currentSelectRow: {
      title: '',
    } as any,
  });

  function onSelectUserGroup(data) {
    userGroupInfo.selectedKeys[0] = data.id;
    userGroupInfo.currentSelectRow = data;
    reload();
  }

  function onClearSelectedUserGroup() {
    userGroupInfo.selectedKeys = [];
    userGroupInfo.currentSelectRow = { title: '' };
    reload();
  }

  async function loadUserGroupList() {
    try {
      const result = await defHttp.get({ url: '/sys/ugroup/list' });
      if (result.records && Array.isArray(result.records) && result.records.length > 0) {
        const userGroupData = result.records.map((item) => ({
          key: item.id,
          title: item.groupName,
          id: item.id,
        }));
        userGroupInfo.data = userGroupData;
        originalUserGroupData.value = userGroupData;
      }
    } catch (error) {
      console.error('获取用户组列表失败', error);
    }
  }
  // 用户组搜索
  function onSearchUserGroup(value) {
    userGroupSearchText.value = value;
    if (!value) {
      userGroupInfo.data = originalUserGroupData.value;
      return;
    }

    const searchText = value.toLowerCase();
    const filteredData = originalUserGroupData.value.filter((item) => item.title.toLowerCase().includes(searchText));
    userGroupInfo.data = filteredData;
  }
  /*-----------------用户组---end----------------*/

  /*-----------------用户列表---begin----------------*/
  async function queryUserList(params) {
    params['column'] = 'sort';
    params['order'] = 'ASC';
    params['status'] = '1';
    if(departInfo.selectedKeys.length == 0 && positionInfo.selectedKeys.length == 0 && userGroupInfo.selectedKeys.length == 0 && params.realname){
      params['realname'] = `*${params.realname.trim()}*`;
    }
    // 根据当前激活的标签页进行查询
    if (activeTab.value === 'depart') {
      let arr = departInfo.selectedKeys;
      if (arr.length > 0) {
        //根据部门查询
        params['id'] = arr[0];
        let result = await getDepartUserList(params);
        if (params.username) {
          result.records = result.records.filter((item) => {
            return item.username.indexOf(params.username) != -1;
          });
        }
        return Promise.resolve(result);
      } else {
        return getUserList(params);
      }
    } else if (activeTab.value === 'position') {
      let arr = positionInfo.selectedKeys;
      if (arr.length > 0) {
        //根据岗位查询
        params['orgCode'] = positionInfo.currentSelectRow?.orgCode;
        return defHttp.get({ url: '/sys/user/queryDepartPostByOrgCode', params });
      } else {
        return getUserList(params);
      }
    } else if (activeTab.value === 'userGroup') {
      let arr = userGroupInfo.selectedKeys;
      if (arr.length > 0) {
        //根据用户组查询
        params['groupId'] = arr[0];
        return defHttp.get({ url: '/sys/user/groupUserList', params });
      } else {
        return getUserList(params);
      }
    } else {
      return getUserList(params);
    }
  }
  const { tableContext } = useListPage({
    designScope: 'tabs-select-user',
    pagination: true,
    tableProps: {
      title: '',
      api: queryUserList,
      columns: columns,
      showActionColumn: false,
      showTableSetting: false,
      canResize: false,
      clickToRowSelect: true,
      formConfig: {
        labelWidth: '90px',
        schemas: searchFormSchema,
        autoAdvancedCol: 4,
        //update-begin-author:liusq---date:2024-06-11--for: 指定会签人员的弹框 查询遮挡了
        baseColProps: { xs: 24, sm: 24, md: 24, lg: 12, xl: 8, xxl: 8 },
        actionColOptions: { xs: 24, sm: 24, md: 24, lg: 12, xl: 8, xxl: 8 },
        //update-end-author:liusq---date:2024-06-11--for:指定会签人员的弹框 查询遮挡了
      },
    },
  });
  const [registerTable, { reload, deleteSelectRowByKey }, { rowSelection, selectedRows, selectedRowKeys }] = tableContext;

  watch(
    () => props.multi,
    (val) => {
      if (val === false) {
        rowSelection.type = 'radio';
      } else {
        rowSelection.type = 'checkbox';
      }
    }
  );

  /*-----------------用户列表--end-----------------*/
  const selectedUserList = ref<any[]>([]);
  //update-begin-author:liusq---date:2024-06-11--for: TV360X-1047 指定下一步操作人/抄送给，选人组件无法多选。
  watch(
    selectedRows,
    () => {
      let arr = [];
      for (let row of unref(selectedRows)) {
        arr.push({
          realname: row.realname,
          username: row.username,
          id: row.id,
        });
      }
      selectedUserList.value = arr;
    },
    { deep: true }
  );
  //update-end-author:liusq---date:2024-06-11--for: TV360X-1047 指定下一步操作人/抄送给，选人组件无法多选。

  const { tableContext: selectedTableContext } = useListPage({
    designScope: 'bpm-select-user',
    pagination: false,
    tableProps: {
      title: '',
      columns: selectedUserColumns,
      pagination: false,
      dataSource: selectedUserList,
      showActionColumn: true,
      showTableSetting: false,
      canResize: false,
      useSearchForm: false,
    },
  });
  const [registerSelectedUserTable] = selectedTableContext;
  function handleDelete(record) {
    let id = record.id;
    let arr = selectedUserList.value;
    arr = arr.filter((item) => item.id != id);
    selectedUserList.value = arr;
    deleteSelectRowByKey(record.id);
  }

  function handleSelectSuccess() {
    let arr = toRaw(selectedUserList.value);
    emit('selected', arr);
    closeModal();
  }

  /**
   * 弹框打开 回显下拉框选中的数据
   * @param data
   */
  function showSelectedValue(data) {
    let selectedValue = data.selected;
    if (!selectedValue || selectedValue.length == 0) {
      selectedUserList.value = [];
      selectedRows.value = [];
      selectedRowKeys.value = [];
    } else {
      let arr1 = [],
        arr2 = [],
        arr3 = [];
      for (let item of selectedValue) {
        arr1.push(item.id);
        arr2.push({ ...item });
        arr3.push({ ...item });
      }
      selectedRowKeys.value = arr1;
      selectedUserList.value = arr2;
      selectedRows.value = arr3;
    }
  }

  /**
   * tab切换，取消选择
   * @param key
   */
  function onTabChange() {
    onClearSelectedDepart();
    onClearSelectedPosition();
    onClearSelectedUserGroup();
  }
  // 初始化加载所有数据
  loadRootDepart();
  loadPositionList();
  loadUserGroupList();
</script>

<style scoped>
  :global(.tabs-select-user-modal .jeecg-modal-content > .scroll-container > .scrollbar__wrap) {
    margin-bottom: 0 !important;
    overflow-y: hidden !important;
  }

  :global(.tabs-select-user-modal .jeecg-modal-content > .scroll-container > .scrollbar__bar.is-vertical) {
    display: none;
  }

  .left-select-card :deep(.ant-card-body) {
    height: calc(100vh - 140px);
  }

  .left-select-tabs {
    height: 100%;
  }

  .left-select-tabs :deep(.ant-tabs-content-holder),
  .left-select-tabs :deep(.ant-tabs-content),
  .left-select-tabs :deep(.ant-tabs-tabpane) {
    min-height: 0;
    height: 100%;
  }

  .left-tab-content {
    display: flex;
    flex-direction: column;
    height: 100%;
    min-height: 0;
    padding: 14px;
  }

  .position-list {
    flex: 1;
    min-height: 0;
    overflow: hidden;
  }

  .position-list :deep(.ant-spin-nested-loading),
  .position-list :deep(.ant-spin-container),
  .position-list :deep(.post-rank-tree) {
    height: 100%;
    min-height: 0;
  }

  .user-group-list {
    flex: 1;
    min-height: 0;
    overflow-y: auto;
    margin-top: 8px;
  }

  .user-group-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 8px 12px;
    margin-bottom: 4px;
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    cursor: pointer;
    transition: all 0.3s ease;
    background-color: #fff;
  }

  .user-group-item:hover {
    border-color: #40a9ff;
    box-shadow: 0 2px 8px rgba(24, 144, 255, 0.1);
  }

  .user-group-item-selected {
    border-color: #1890ff;
    background-color: #f0f8ff;
    color: #1890ff;
    font-weight: 500;
  }

  .user-group-item-selected:hover {
    border-color: #096dd9;
    background-color: #e6f7ff;
  }

  .user-group-title {
    flex: 1;
    font-size: 14px;
    line-height: 1.5;
  }

  .selected-icon {
    color: #52c41a;
    font-size: 14px;
    margin-left: 8px;
  }
  .alert-info {
    margin-bottom: 5px;
  }
  /**部门树组件滚动条样式*/
  .tree-container {
    flex: 1;
    min-height: 0;
    overflow-y: auto;                /* 垂直滚动 */
    overflow-x: hidden;              /* 隐藏水平滚动 */
    position: relative;              /* 相对定位 */
  }
  /* 深度选择器优化树组件内部样式 */
  .tree-container :deep(.ant-tree) {
    width: 100%;
    padding: 8px 4px;
  }

  .tree-container :deep(.ant-tree-list-holder) {
    overflow: visible !important;
  }
  .tree-container::-webkit-scrollbar {
    width: 6px;
  }

  .tree-container::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 3px;
  }

  .tree-container::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 3px;
  }

  .tree-container::-webkit-scrollbar-thumb:hover {
    background: #a8a8a8;
  }
  .selected-title {
    display: inline-block; /* 设置为块级元素才能应用宽度限制 */
    flex: 1;
    min-width: 0;
    max-width: 180px; /* 最大宽度限制 */
    overflow: hidden; /* 超出部分隐藏 */
    text-overflow: ellipsis; /* 显示省略号 */
    white-space: nowrap; /* 禁止换行 */
    vertical-align: middle; /* 垂直居中 */
  }
  /* Alert消息内容水平对齐 */
  .alert-message-content {
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
    gap: 8px; /* 元素间距 */
    flex-wrap: nowrap; /* 允许换行 */
  }

  .alert-label {
    color: #000000d9; /* 标签颜色 */
    font-weight: 500; /* 字体加粗 */
    white-space: nowrap; /* 禁止换行 */
  }

  .clear-link {
    flex-shrink: 0;
    color: #1890ff; /* 链接颜色 */
    text-decoration: none; /* 去除下划线 */
    white-space: nowrap; /* 禁止换行 */
    cursor: pointer; /* 手型光标 */
  }

  .clear-link:hover {
    color: #40a9ff; /* 悬停颜色 */
    text-decoration: underline; /* 悬停下划线 */
  }
</style>
