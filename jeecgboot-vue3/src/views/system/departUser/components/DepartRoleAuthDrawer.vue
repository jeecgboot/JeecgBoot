<template>
  <BasicDrawer
    :width="650"
    :loading="loading"
    showFooter
    okText="保存并关闭"
    @ok="onSubmit(true)"
    @close="onClose"
    @register="registerDrawer"
  >
    <template #title>
      部门角色权限配置
      <a-dropdown>
        <Icon icon="ant-design:more-outlined" class="more-icon" />
        <template #overlay>
          <a-menu @click="treeMenuClick">
            <a-menu-item key="checkAll">{{ t('component.tree.selectAll') }}</a-menu-item>
            <a-menu-item key="cancelCheck">{{ t('component.tree.unSelectAll') }}</a-menu-item>
            <div class="line"></div>
            <a-menu-item key="openAll">{{ t('component.tree.expandAll') }}</a-menu-item>
            <a-menu-item key="closeAll">{{ t('component.tree.unExpandAll') }}</a-menu-item>
            <div class="line"></div>
            <a-menu-item key="relation">{{ t('component.tree.checkStrictly') }}</a-menu-item>
            <a-menu-item key="standAlone">{{ t('component.tree.checkUnStrictly') }}</a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
    </template>
    <div>
      <a-spin :spinning="loading">
        <template v-if="treeData.length > 0">
          <BasicTree
            title="所拥有的部门权限"
            checkable
            :treeData="treeData"
            :checkedKeys="checkedKeys"
            :selectedKeys="selectedKeys"
            :expandedKeys="expandedKeys"
            :checkStrictly="true"
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
  import { useI18n } from '/@/hooks/web/useI18n';
  import DepartRoleDataRuleDrawer from './DepartRoleDataRuleDrawer.vue';
  import { queryTreeListForDeptRole, queryDeptRolePermission, saveDeptRolePermission } from '../depart.user.api';
  import { translateTitle } from "@/utils/common/compUtils";
  import { DEPART_ROLE_AUTH_CONFIG_KEY } from '/@/enums/cacheEnum';

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
  //父子节点选中状态是否关联 true不关联，false关联
  const checkStrictly = ref(false);
  const { t } = useI18n();

  // 注册抽屉组件
  const [registerDrawer, { closeDrawer }] = useDrawerInner((data) => {
    roleId.value = data.record.id;
    departId.value = data.record.departId;
    loadData({
      success: (ids) => {
        // update-begin--author:liaozhiyang---date:20240704---for：【TV360X-1619】同步系统角色改法加上缓存，默认层级关联修正原生层级关联bug
        const localData = localStorage.getItem(DEPART_ROLE_AUTH_CONFIG_KEY);
        if (localData) {
          const obj = JSON.parse(localData);
          obj.level && treeMenuClick({ key: obj.level });
          obj.expand && treeMenuClick({ key: obj.expand });
        } else {
          // expandedKeys.value = ids;
        }
        // update-end--author:liaozhiyang---date:20240704---for：【TV360X-1619】同步系统角色改法加上缓存，默认层级关联修正原生层级关联bug
      },
    });
  });
  // 注册数据规则授权弹窗抽屉
  const [registerDataRuleDrawer, dataRuleDrawer] = useDrawer();

  async function loadData(options: any = {}) {
    try {
      loading.value = true;
      // 用户角色授权功能，查询菜单权限树
      const { ids, treeList } = await queryTreeListForDeptRole({ departId: departId.value });
      if (ids.length > 0) {
        allTreeKeys.value = ids;
        // update-begin--author:liaozhiyang---date:20240704---for：【TV360X-1619】同步系统角色改法加上缓存，默认层级关联修正原生层级关联bug
        options.success?.(ids);
        // update-end--author:liaozhiyang---date:20240704---for：【TV360X-1619】同步系统角色改法加上缓存，默认层级关联修正原生层级关联bug
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

  /**
   * 树菜单选择
   * @param key
   */
  function treeMenuClick({ key }) {
    if (key === 'checkAll') {
      checkedKeys.value = allTreeKeys.value;
    } else if (key === 'cancelCheck') {
      checkedKeys.value = [];
    } else if (key === 'openAll') {
      expandedKeys.value = allTreeKeys.value;
      saveLocalOperation('expand', 'openAll');
    } else if (key === 'closeAll') {
      expandedKeys.value = [];
      saveLocalOperation('expand', 'closeAll');
    } else if (key === 'relation') {
      checkStrictly.value = false;
      saveLocalOperation('level', 'relation');
    } else {
      checkStrictly.value = true;
      saveLocalOperation('level', 'standAlone');
    }
  }
  /**
   * 2024-07-04
   * liaozhiyang
   * */
  const saveLocalOperation = (key, value) => {
    const localData = localStorage.getItem(DEPART_ROLE_AUTH_CONFIG_KEY);
    const obj = localData ? JSON.parse(localData) : {};
    obj[key] = value;
    localStorage.setItem(DEPART_ROLE_AUTH_CONFIG_KEY, JSON.stringify(obj));
  };
</script>
<style lang="less" scoped>
  /** 固定操作按钮 */
  .jeecg-basic-tree {
    position: absolute;
    width: 618px;
  }
  .line {
    height: 1px;
    width: 100%;
    border-bottom: 1px solid #f0f0f0;
  }
  .more-icon {
    font-size: 20px !important;
    color: black;
    display: inline-flex;
    float: right;
    margin-right: 2px;
    cursor: pointer;
  }
  :deep(.jeecg-tree-header) {
    border-bottom: none;
  }
</style>
