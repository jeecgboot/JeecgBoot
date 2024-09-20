<template>
  <a-spin :spinning="loading">
    <template v-if="treeData.length > 0">
      <BasicTree
        ref="basicTree"
        class="depart-rule-tree"
        checkable
        :treeData="treeData"
        :checkedKeys="checkedKeys"
        :selectedKeys="selectedKeys"
        :expandedKeys="expandedKeys"
        :checkStrictly="true"
        style="height: 500px; overflow: auto"
        @check="onCheck"
        @expand="onExpand"
        @select="onSelect"
      >
        <template #title="{ slotTitle, ruleFlag }">
          <span>{{ slotTitle }}</span>
          <Icon v-if="ruleFlag" icon="ant-design:align-left-outlined" style="margin-left: 5px; color: red" />
        </template>
      </BasicTree>
    </template>
    <a-empty v-else description="无可配置部门权限" />

    <div class="j-box-bottom-button offset-20" style="margin-top: 30px">
      <div class="j-box-bottom-button-float" :class="[`${prefixCls}`]">
        <a-dropdown :trigger="['click']" placement="top">
          <template #overlay>
            <a-menu>
              <a-menu-item key="3" @click="toggleCheckALL(true)">{{ t('component.tree.selectAll') }}</a-menu-item>
              <a-menu-item key="4" @click="toggleCheckALL(false)">{{ t('component.tree.unSelectAll') }}</a-menu-item>
              <a-menu-item key="5" @click="toggleExpandAll(true)">{{ t('component.tree.expandAll') }}</a-menu-item>
              <a-menu-item key="6" @click="toggleExpandAll(false)">{{ t('component.tree.unExpandAll') }}</a-menu-item>
              <a-menu-item key="7" @click="toggleRelationAll(false)">{{ t('component.tree.checkStrictly') }}</a-menu-item>
              <a-menu-item key="8" @click="toggleRelationAll(true)">{{ t('component.tree.checkUnStrictly') }}</a-menu-item>
            </a-menu>
          </template>
          <a-button style="float: left">
            树操作
            <Icon icon="ant-design:up-outlined" />
          </a-button>
        </a-dropdown>
        <a-button type="primary" preIcon="ant-design:save-filled" @click="onSubmit">保存</a-button>
      </div>
    </div>
  </a-spin>
  <DepartDataRuleDrawer @register="registerDataRuleDrawer" />
</template>

<script lang="ts" setup>
  import { watch, computed, inject, ref, nextTick } from 'vue';
  import { useDrawer } from '/@/components/Drawer';
  import { BasicTree } from '/@/components/Tree/index';
  import DepartDataRuleDrawer from './DepartDataRuleDrawer.vue';
  import { queryRoleTreeList, queryDepartPermission, saveDepartPermission } from '../depart.api';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { translateTitle } from '/@/utils/common/compUtils';
  import { DEPART_MANGE_AUTH_CONFIG_KEY } from '/@/enums/cacheEnum';
  import { useI18n } from '/@/hooks/web/useI18n';

  const { prefixCls } = useDesign('j-depart-form-content');
  const props = defineProps({
    data: { type: Object, default: () => ({}) },
  });
  // 当前选中的部门ID，可能会为空，代表未选择部门
  const departId = computed(() => props.data?.id);

  const basicTree = ref();
  const loading = ref<boolean>(false);
  //树的全部节点信息
  const allTreeKeys = ref([]);
  const treeData = ref<any[]>([]);
  const expandedKeys = ref<Array<any>>([]);
  const selectedKeys = ref<Array<any>>([]);
  const checkedKeys = ref<Array<any>>([]);
  const lastCheckedKeys = ref<Array<any>>([]);
  const checkStrictly = ref(false);
  const { t } = useI18n();

  // 注册数据规则授权弹窗抽屉
  const [registerDataRuleDrawer, dataRuleDrawer] = useDrawer();

  // onCreated
  loadData({
    success: (ids) => {
      // update-begin--author:liaozhiyang---date:20240704---for：【TV360X-1689】同步系统角色改法加上缓存层级关联等功能
      const localData = localStorage.getItem(DEPART_MANGE_AUTH_CONFIG_KEY);
      if (localData) {
        const obj = JSON.parse(localData);
        obj.level && toggleRelationAll(obj.level == 'relation' ? false : true);
        obj.expand && toggleExpandAll(obj.expand == 'openAll' ? true :false);
      } else {
        // expandedKeys.value = ids;
      }
      // update-end--author:liaozhiyang---date:20240704---for：【TV360X-1689】同步系统角色改法加上缓存层级关联等功能
    }
  });
  watch(departId, () => loadDepartPermission(), { immediate: true });

  async function loadData(options: any = {}) {
    try {
      loading.value = true;
      let { treeList, ids } = await queryRoleTreeList();
      //update-begin---author:wangshuai---date:2024-04-08---for:【issues/1169】部门管理功能中的【部门权限】中未翻译 t('') 多语言---
      treeData.value = translateTitle(treeList);
      //update-end---author:wangshuai---date:2024-04-08---for:【issues/1169】部门管理功能中的【部门权限】中未翻译 t('') 多语言---
      // update-begin--author:liaozhiyang---date:20240704---for：【TV360X-1689】同步系统角色改法加上缓存层级关联等功能
      allTreeKeys.value = ids;
      options.success?.(ids);
      // update-end--author:liaozhiyang---date:20240704---for：【TV360X-1689】同步系统角色改法加上缓存层级关联等功能
    } finally {
      loading.value = false;
    }
  }

  async function loadDepartPermission() {
    if (departId.value) {
      try {
        loading.value = true;
        let keys = await queryDepartPermission({ departId: departId.value });
        checkedKeys.value = keys;
        lastCheckedKeys.value = [...keys];
      } finally {
        loading.value = false;
      }
    }
  }

  async function onSubmit() {
    try {
      loading.value = true;
      await saveDepartPermission({
        departId: departId.value,
        permissionIds: checkedKeys.value.join(','),
        lastpermissionIds: lastCheckedKeys.value.join(','),
      });
      await loadData();
      await loadDepartPermission();
    } finally {
      loading.value = false;
    }
  }

  /**
   * 点击选中
   * 2024-07-04
   * liaozhiyang
   */
  function onCheck(o, e) {
    // checkStrictly: true=>层级独立，false=>层级关联.
    if (checkStrictly.value) {
      checkedKeys.value = o.checked ? o.checked : o;
    } else {
      const keys = getNodeAllKey(e.node, 'children', 'key');
      if (e.checked) {
        // 反复操作下可能会有重复的keys，得用new Set去重下
        checkedKeys.value = [...new Set([...checkedKeys.value, ...keys])];
      } else {
        const result = removeMatchingItems(checkedKeys.value, keys);
        checkedKeys.value = result;
      }
    }
  }
  /**
   * 2024-07-04
   * liaozhiyang
   * 删除相匹配数组的项
   */
  function removeMatchingItems(arr1, arr2) {
    // 使用哈希表记录 arr2 中的元素
    const hashTable = {};
    for (const item of arr2) {
      hashTable[item] = true;
    }
    // 使用 filter 方法遍历第一个数组，过滤出不在哈希表中存在的项
    return arr1.filter((item) => !hashTable[item]);
  }
  /**
   * 2024-07-04
   * liaozhiyang
   * 获取当前节点及以下所有子孙级的key
   */
  function getNodeAllKey(node: any, children: any, key: string) {
    const result: any = [];
    result.push(node[key]);
    const recursion = (data) => {
      data.forEach((item: any) => {
        result.push(item[key]);
        if (item[children]?.length) {
          recursion(item[children]);
        }
      });
    };
    node[children]?.length && recursion(node[children]);
    return result;
  }

  // tree展开事件
  function onExpand($expandedKeys) {
    expandedKeys.value = $expandedKeys;
  }

  // tree选中事件
  function onSelect($selectedKeys, { selectedNodes }) {
    if (selectedNodes[0]?.ruleFlag) {
      let functionId = $selectedKeys[0];
      dataRuleDrawer.openDrawer(true, { departId, functionId });
    }
    selectedKeys.value = [];
  }

  // 切换父子关联
  async function toggleCheckStrictly(flag) {
    checkStrictly.value = flag;
    await nextTick();
    checkedKeys.value = basicTree.value.getCheckedKeys();
  }

  // 切换展开收起
  async function toggleExpandAll(flag) {
    // update-begin--author:liaozhiyang---date:20240704---for：【TV360X-1689】同步系统角色改法加上缓存层级关联等功能
    if (flag) {
      expandedKeys.value = allTreeKeys.value;
      saveLocalOperation('expand', 'openAll');
    } else {
      expandedKeys.value = [];
      saveLocalOperation('expand', 'closeAll');
    }
    // update-end--author:liaozhiyang---date:20240704---for：【TV360X-1689】同步系统角色改法加上缓存层级关联等功能
  }

  // 切换全选
  async function toggleCheckALL(flag) {
    // update-begin--author:liaozhiyang---date:20240704---for：【TV360X-1689】同步系统角色改法加上缓存层级关联等功能
    if (flag) {
      checkedKeys.value = allTreeKeys.value;
    } else {
      checkedKeys.value = [];
    }
    // update-end--author:liaozhiyang---date:20240704---for：【TV360X-1689】同步系统角色改法加上缓存层级关联等功能
  }

  // 切换层级关联(独立)
  const toggleRelationAll = (flag) => {
    // update-begin--author:liaozhiyang---date:20240704---for：【TV360X-1689】同步系统角色改法加上缓存层级关联等功能
    checkStrictly.value = flag;
    if (flag) {
      saveLocalOperation('level', 'standAlone');
    } else {
      saveLocalOperation('level', 'relation');
    }
    // update-end--author:liaozhiyang---date:20240704---for：【TV360X-1689】同步系统角色改法加上缓存层级关联等功能
  };
  /**
   * 2024-07-04
   * liaozhiyang
   * 缓存
   * */
  const saveLocalOperation = (key, value) => {
    const localData = localStorage.getItem(DEPART_MANGE_AUTH_CONFIG_KEY);
    const obj = localData ? JSON.parse(localData) : {};
    obj[key] = value;
    localStorage.setItem(DEPART_MANGE_AUTH_CONFIG_KEY, JSON.stringify(obj))
  };
</script>

<style lang="less" scoped>
  // 【VUEN-188】解决滚动条不灵敏的问题
  .depart-rule-tree :deep(.scrollbar__bar) {
    pointer-events: none;
  }
</style>
