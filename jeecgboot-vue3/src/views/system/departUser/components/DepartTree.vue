<template>
  <div class="bg-white m-4 mr-0 overflow-hidden">
    <a-spin :spinning="loading">
      <template v-if="userIdentity === '2'">
        <!--组织机构树-->
        <BasicTree
          v-if="!treeReloading"
          title="部门列表"
          toolbar
          search
          showLine
          :checkStrictly="true"
          :clickRowToExpand="false"
          :treeData="treeData"
          :selectedKeys="selectedKeys"
          :expandedKeys="expandedKeys"
          :autoExpandParent="autoExpandParent"
          @select="onSelect"
          @expand="onExpand"
          @search="onSearch"
        />
      </template>
      <a-empty v-else description="普通员工无此权限" />
    </a-spin>
  </div>
</template>

<script lang="ts" setup>
  import { inject, nextTick, ref } from 'vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { BasicTree } from '/@/components/Tree';
  import { queryMyDepartTreeList, searchByKeywords } from '../depart.user.api';

  const prefixCls = inject('prefixCls');
  const emit = defineEmits(['select']);
  const { createMessage } = useMessage();

  let loading = ref<boolean>(false);
  // 部门树列表数据
  let treeData = ref<any[]>([]);
  // 当前展开的项
  let expandedKeys = ref<any[]>([]);
  // 当前选中的项
  let selectedKeys = ref<any[]>([]);
  // 是否自动展开父级
  let autoExpandParent = ref<boolean>(true);
  // 用户身份
  let userIdentity = ref<string>('2');
  // 树组件重新加载
  let treeReloading = ref<boolean>(false);

  // 加载部门信息
  function loadDepartTreeData() {
    loading.value = true;
    treeData.value = [];
    queryMyDepartTreeList()
      .then((res) => {
        if (res.success) {
          if (Array.isArray(res.result)) {
            treeData.value = res.result;
            userIdentity.value = res.message;
            autoExpandParentNode();
          }
        } else {
          createMessage.warning(res.message);
        }
      })
      .finally(() => (loading.value = false));
  }

  loadDepartTreeData();

  // 自动展开父节点，只展开一级
  function autoExpandParentNode() {
    let keys: Array<any> = [];
    treeData.value.forEach((item, index) => {
      if (item.children && item.children.length > 0) {
        keys.push(item.key);
      }
      if (index === 0) {
        // 默认选中第一个
        setSelectedKey(item.id, item);
      }
    });
    if (keys.length > 0) {
      reloadTree();
      expandedKeys.value = keys;
    }
  }

  // 重新加载树组件，防止无法默认展开数据
  async function reloadTree() {
    await nextTick();
    treeReloading.value = true;
    await nextTick();
    treeReloading.value = false;
  }

  /**
   * 设置当前选中的行
   */
  function setSelectedKey(key: string, data?: object) {
    selectedKeys.value = [key];
    if (data) {
      emit('select', data);
    }
  }

  // 搜索事件
  function onSearch(value: string) {
    if (value) {
      loading.value = true;
      searchByKeywords({ keyWord: value, myDeptSearch: '1' })
        .then((result) => {
          if (Array.isArray(result)) {
            treeData.value = result;
          } else {
            createMessage.warning('未查询到部门信息');
            treeData.value = [];
          }
        })
        .finally(() => (loading.value = false));
    } else {
      loadDepartTreeData();
    }
  }

  // 树选择事件
  function onSelect(selKeys, event) {
    if (selKeys.length > 0 && selectedKeys.value[0] !== selKeys[0]) {
      setSelectedKey(selKeys[0], event.selectedNodes[0]);
    } else {
      // 这样可以防止用户取消选择
      setSelectedKey(selectedKeys.value[0]);
    }
  }

  // 树展开事件
  function onExpand(keys) {
    expandedKeys.value = keys;
    autoExpandParent.value = false;
  }
</script>
<style lang="less" scoped>
  /*升级antd3后，查询框与树贴的太近，样式优化*/
  :deep(.jeecg-tree-header) {
    margin-bottom: 6px;
  }
</style>
