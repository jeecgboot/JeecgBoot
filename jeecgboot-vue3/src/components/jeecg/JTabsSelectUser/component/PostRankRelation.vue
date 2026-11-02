<template>
  <a-spin :spinning="loading">
    <template v-if="treeData && treeData.length > 0">
      <BasicTree
        :expandedKeys="expandedKeys"
        :fieldNames="{ children: 'children', title: 'title', key: 'value' }"
        ref="basicTree"
        :treeData="treeData"
        :checkStrictly="true"
        @select="onSelectDepart"
        class="post-rank-tree"
      >
        <template #title="{ title, value }">
          <a-tooltip
            :title="title"
            placement="top"
            :open="activeTooltipKey !== null && activeTooltipKey === value"
            overlayClassName="post-rank-tooltip"
          >
            <span
              class="post-rank-title"
              @mouseenter="handleTitleMouseEnter($event, value)"
              @mouseleave="handleTitleMouseLeave(value)"
            >
              {{ title }}
            </span>
          </a-tooltip>
        </template>
      </BasicTree>
    </template>
    <a-empty v-else description="无岗位信息" />
  </a-spin>
</template>

<script lang="ts" setup>
  import { onBeforeUnmount, ref, watchEffect } from 'vue';
  import { BasicTree } from '/@/components/Tree/index';

  const props = defineProps({
    treeData: { type: Array, default: () => ([]) },
  });
  const emit = defineEmits(['select']);
  const basicTree = ref();
  const loading = ref<boolean>(false);
  const activeTooltipKey = ref<any>(null);
  let tooltipTimer: ReturnType<typeof setTimeout> | null = null;
  //选中的key
  const expandedKeys = ref<any[]>([]);
  //所有的部门id
  const departIds = ref<any[]>([]);
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

  watchEffect(() => {
    props.treeData && (departIds.value = getParentDepartmentIds(props.treeData));
  });

  /**
   * 
   * @param data
   * @param node
   */
  function onSelectDepart(data, { node }) {
    emit('select', data);
  }

  /**
   * 岗位名称显示不全时展示完整内容
   */
  function handleTitleMouseEnter(event: MouseEvent, value: any) {
    const titleElement = event.currentTarget as HTMLElement;
    clearTooltipTimer();
    if (titleElement.scrollWidth > titleElement.clientWidth) {
      tooltipTimer = setTimeout(() => {
        activeTooltipKey.value = value;
      }, 500);
    }
  }

  function handleTitleMouseLeave(value: any) {
    clearTooltipTimer();
    if (activeTooltipKey.value === value) {
      activeTooltipKey.value = null;
    }
  }

  function clearTooltipTimer() {
    if (tooltipTimer) {
      clearTimeout(tooltipTimer);
      tooltipTimer = null;
    }
  }

  onBeforeUnmount(clearTooltipTimer);
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
  // update-begin--author:liaozhiyang---date:20260811---for：【LHZP-1694】选中用户组件切换到岗位时滚动条拖动不了
  .post-rank-tree {
    width: 100%;
    min-width: 0;
    height: calc(100vh - 390px);
    min-height: 150px;
    overflow-y: auto;
    overflow-x: hidden;
    scrollbar-gutter: stable;
  }

  .post-rank-tree :deep(.scrollbar__bar) {
    pointer-events: auto;
  }

  .post-rank-tree :deep(.scrollbar__view),
  .post-rank-tree :deep(.ant-tree),
  .post-rank-tree :deep(.ant-tree-list-holder-inner) {
    width: 100%;
    min-width: 0;
  }

  .post-rank-tree :deep(.ant-tree-treenode) {
    box-sizing: border-box;
    width: 100%;
    min-width: 0;
    overflow: hidden;
  }

  .post-rank-tree :deep(.ant-tree-node-content-wrapper) {
    min-width: 0;
    overflow: hidden;
  }

  .post-rank-title {
    display: block;
    width: 100%;
    overflow: hidden;
    color: inherit;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  :global(.post-rank-tooltip) {
    max-width: 420px;
  }

  :global(.post-rank-tooltip .ant-tooltip-inner) {
    min-height: auto;
    padding: 10px 14px;
    color: rgba(255, 255, 255, 0.96);
    font-size: 14px;
    line-height: 1.7;
    word-break: break-all;
    background: rgba(31, 41, 55, 0.96);
    border: 1px solid rgba(255, 255, 255, 0.14);
    border-radius: 8px;
    box-shadow: 0 8px 24px rgba(15, 23, 42, 0.22);
    backdrop-filter: blur(6px);
  }
  // update-end--author:liaozhiyang---date:20260811---for：【LHZP-1694】选中用户组件切换到岗位时滚动条拖动不了
</style>
