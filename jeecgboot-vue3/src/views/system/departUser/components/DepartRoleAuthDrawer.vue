<template>
  <BasicDrawer
    title="部门角色权限配置"
    :width="650"
    :loading="loading"
    showFooter
    okText="保存并关闭"
    @ok="onSubmit(true)"
    @close="onClose"
    @register="registerDrawer"
  >
    <div>
      <a-spin :spinning="loading">
        <template v-if="treeData.length > 0">
          <BasicTree
            title="所拥有的部门权限"
            toolbar
            checkable
            :treeData="treeData"
            :checkedKeys="checkedKeys"
            :selectedKeys="selectedKeys"
            :expandedKeys="expandedKeys"
            :checkStrictly="checkStrictly"
            :clickRowToExpand="false"
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
      </a-spin>
    </div>

    <template #centerFooter>
      <a-button type="primary" :loading="loading" ghost @click="onSubmit(false)">仅保存</a-button>
    </template>
  </BasicDrawer>
  <DepartRoleDataRuleDrawer @register="registerDataRuleDrawer" />
</template>

<script lang="ts" setup>
  import { ref } from 'vue';

  import { BasicTree } from '/@/components/Tree/index';
  import { BasicDrawer, useDrawer, useDrawerInner } from '/@/components/Drawer';
  import { useMessage } from '/@/hooks/web/useMessage';

  import DepartRoleDataRuleDrawer from './DepartRoleDataRuleDrawer.vue';
  import { queryTreeListForDeptRole, queryDeptRolePermission, saveDeptRolePermission } from '../depart.user.api';
  import { translateTitle } from "@/utils/common/compUtils";

  defineEmits(['register']);
  const { createMessage } = useMessage();
  const loading = ref(false);
  const departId = ref('');
  const roleId = ref('');
  const treeData = ref<Array<any>>([]);
  const checkedKeys = ref<Array<any>>([]);
  const lastCheckedKeys = ref<Array<any>>([]);
  const expandedKeys = ref<Array<any>>([]);
  const selectedKeys = ref<Array<any>>([]);
  const allTreeKeys = ref<Array<any>>([]);
  const checkStrictly = ref(true);

  // 注册抽屉组件
  const [registerDrawer, { closeDrawer }] = useDrawerInner((data) => {
    roleId.value = data.record.id;
    departId.value = data.record.departId;
    loadData();
  });
  // 注册数据规则授权弹窗抽屉
  const [registerDataRuleDrawer, dataRuleDrawer] = useDrawer();

  async function loadData() {
    try {
      loading.value = true;
      // 用户角色授权功能，查询菜单权限树
      const { ids, treeList } = await queryTreeListForDeptRole({ departId: departId.value });
      if (ids.length > 0) {
        allTreeKeys.value = ids;
        expandedKeys.value = ids;
        //update-begin---author:wangshuai---date:2024-04-08---for:【issues/1169】我的部门功能中的【部门权限】中未翻译 t('') 多语言---
        treeData.value = translateTitle(treeList);
        //update-end---author:wangshuai---date:2024-04-08---for:【issues/1169】我的部门功能中的【部门权限】中未翻译 t('') 多语言---
        // 查询角色授权
        checkedKeys.value = await queryDeptRolePermission({ roleId: roleId.value });
        lastCheckedKeys.value = [checkedKeys.value];
      } else {
        reset();
      }
    } finally {
      loading.value = false;
    }
  }

  // 重置页面
  function reset() {
    treeData.value = [];
    expandedKeys.value = [];
    checkedKeys.value = [];
    lastCheckedKeys.value = [];
    loading.value = false;
  }

  // tree勾选复选框事件
  function onCheck(event) {
    if (checkStrictly.value) {
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
      dataRuleDrawer.openDrawer(true, { roleId, departId, functionId });
    }
    selectedKeys.value = [];
  }

  function doClose() {
    reset();
    closeDrawer();
  }

  function onClose() {
    reset();
  }

  async function onSubmit(exit) {
    try {
      loading.value = true;
      let params = {
        roleId: roleId.value,
        permissionIds: checkedKeys.value.join(','),
        lastpermissionIds: lastCheckedKeys.value.join(','),
      };
      await saveDeptRolePermission(params);
      if (exit) {
        doClose();
      }
    } finally {
      loading.value = false;
      if (!exit) {
        loadData();
      }
    }
  }
</script>
