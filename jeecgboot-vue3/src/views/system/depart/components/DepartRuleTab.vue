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
        :checkStrictly="checkStrictly"
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
              <a-menu-item key="3" @click="toggleCheckALL(true)">全部勾选</a-menu-item>
              <a-menu-item key="4" @click="toggleCheckALL(false)">取消全选</a-menu-item>
              <a-menu-item key="5" @click="toggleExpandAll(true)">展开所有</a-menu-item>
              <a-menu-item key="6" @click="toggleExpandAll(false)">收起所有</a-menu-item>
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

  const { prefixCls } = useDesign('j-depart-form-content');
  const props = defineProps({
    data: { type: Object, default: () => ({}) },
  });
  // 当前选中的部门ID，可能会为空，代表未选择部门
  const departId = computed(() => props.data?.id);

  const basicTree = ref();
  const loading = ref<boolean>(false);
  const treeData = ref<any[]>([]);
  const expandedKeys = ref<Array<any>>([]);
  const selectedKeys = ref<Array<any>>([]);
  const checkedKeys = ref<Array<any>>([]);
  const lastCheckedKeys = ref<Array<any>>([]);
  const checkStrictly = ref(true);

  // 注册数据规则授权弹窗抽屉
  const [registerDataRuleDrawer, dataRuleDrawer] = useDrawer();

  // onCreated
  loadData();
  watch(departId, () => loadDepartPermission(), { immediate: true });

  async function loadData() {
    try {
      loading.value = true;
      let { treeList } = await queryRoleTreeList();
      //update-begin---author:wangshuai---date:2024-04-08---for:【issues/1169】部门管理功能中的【部门权限】中未翻译 t('') 多语言---
      treeData.value = translateTitle(treeList);
      //update-end---author:wangshuai---date:2024-04-08---for:【issues/1169】部门管理功能中的【部门权限】中未翻译 t('') 多语言---
      await nextTick();
      toggleExpandAll(true);
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

  // tree勾选复选框事件
  function onCheck(event) {
    if (!Array.isArray(event)) {
      checkedKeys.value = event.checked;
    } else {
      checkedKeys.value = event;
    }
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
    basicTree.value.expandAll(flag);
    await nextTick();
    expandedKeys.value = basicTree.value.getExpandedKeys();
  }

  // 切换全选
  async function toggleCheckALL(flag) {
    basicTree.value.checkAll(flag);
    await nextTick();
    checkedKeys.value = basicTree.value.getCheckedKeys();
  }
</script>

<style lang="less" scoped>
  // 【VUEN-188】解决滚动条不灵敏的问题
  .depart-rule-tree :deep(.scrollbar__bar) {
    pointer-events: none;
  }
</style>
