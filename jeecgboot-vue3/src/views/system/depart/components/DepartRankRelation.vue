<template>
  <a-spin :spinning="loading">
    <template v-if="treeData && treeData.length > 0">
      <div style="margin-top: 10px;margin-bottom: 10px;display: flex">
        <a-button preIcon="ant-design:down-outlined" @click="expandAll(true)" type="primary">展开全部</a-button>
        <a-button preIcon="ant-design:up-outlined" @click="expandAll(false)" type="primary" style="margin-left: 10px">折叠全部</a-button>
      </div>
      <BasicTree
        :expandedKeys="expandedKeys"
        :fieldNames="{ children: 'children', title: 'title', key: 'value' }"
        ref="basicTree"
        :treeData="treeData"
        :checkStrictly="true"
        style="height: 500px; overflow: auto"
      ></BasicTree>
    </template>
    <a-empty v-else description="无岗位消息" />
  </a-spin>
</template>

<script lang="ts" setup>
  import { computed, ref, watch } from 'vue';
  import { BasicTree } from '/@/components/Tree/index';
  import { getRankRelation } from '../depart.api';

  const props = defineProps({
    data: { type: Object, default: () => ({}) },
  });
  // 当前选中的部门ID，可能会为空，代表未选择部门
  const departId = computed(() => props.data?.id);

  const basicTree = ref();
  const loading = ref<boolean>(false);
  //树的全部节点信息
  const treeData = ref<any[]>([]);
  //选中的key
  const expandedKeys = ref<any[]>([]);
  //所有的部门id
  const departIds = ref<any[]>([]);

  watch(departId, (val) => loadData(val), { immediate: true });

  async function loadData(val) {
    try {
      loading.value = true;
      await getRankRelation({ departId: val }).then((res) => {
        if (res.success) {
          treeData.value = res.result;
          departIds.value = getParentDepartmentIds(res.result);
        }
      });
    } finally {
      loading.value = false;
    }
  }

  /**
   * 折叠全部
   *
   * @param expandAll
   */
  async function expandAll(expandAll) {
    if (!expandAll) {
      expandedKeys.value = [];
    } else {
      expandedKeys.value = departIds.value;
    }
  }

  /**
   * 获取存在子级的部门id
   * @param departments
   */
  function getParentDepartmentIds(departments) {
    const ids: any = [];
    departments.forEach((dept) => {
      // 检查是否有 children 数组且不为空
      if (dept.children && Array.isArray(dept.children) && dept.children.length > 0) {
        ids.push(dept.id);
        // 递归检查子部门是否也有子级
        ids.push(...getParentDepartmentIds(dept.children));
      }
    });
    return ids;
  }
</script>

<style lang="less" scoped>
  .depart-rule-tree :deep(.scrollbar__bar) {
    pointer-events: none;
  }
</style>
