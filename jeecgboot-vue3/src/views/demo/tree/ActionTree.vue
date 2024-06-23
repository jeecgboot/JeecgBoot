<template>
  <PageWrapper title="Tree函数操作示例" contentBackground contentClass="p-4">
    <div class="mb-4">
      <a-button @click="expandAll(true)" class="mr-2"> 展开全部 </a-button>
      <a-button @click="expandAll(false)" class="mr-2"> 折叠全部 </a-button>
      <a-button @click="checkAll(true)" class="mr-2"> 全选 </a-button>
      <a-button @click="checkAll(false)" class="mr-2"> 全不选 </a-button>
      <a-button @click="handleLevel(2)" class="mr-2"> 显示到第2级 </a-button>
      <a-button @click="handleLevel(1)" class="mr-2"> 显示到第1级 </a-button>
    </div>
    <div class="mb-4">
      <a-button @click="handleSetCheckData" class="mr-2"> 设置勾选数据 </a-button>
      <a-button @click="handleGetCheckData" class="mr-2"> 获取勾选数据 </a-button>
      <a-button @click="handleSetSelectData" class="mr-2"> 设置选中数据 </a-button>
      <a-button @click="handleGetSelectData" class="mr-2"> 获取选中数据 </a-button>

      <a-button @click="handleSetExpandData" class="mr-2"> 设置展开数据 </a-button>
      <a-button @click="handleGetExpandData" class="mr-2"> 获取展开数据 </a-button>
    </div>
    <div class="mb-4">
      <a-button @click="appendNodeByKey(null)" class="mr-2"> 添加根节点 </a-button>
      <a-button @click="appendNodeByKey('2-2')" class="mr-2"> 添加在parent3内添加节点 </a-button>
      <a-button @click="deleteNodeByKey('2-2')" class="mr-2"> 删除parent3节点 </a-button>
      <a-button @click="updateNodeByKey('1-1')" class="mr-2"> 更新parent2节点 </a-button>
    </div>
    <BasicTree :treeData="treeData" title="函数操作" ref="treeRef" :checkable="true" />
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, ref, unref } from 'vue';
  import { BasicTree, TreeActionType } from '/@/components/Tree/index';
  import { treeData } from './data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { PageWrapper } from '/@/components/Page';

  export default defineComponent({
    components: { BasicTree, PageWrapper },
    setup() {
      const treeRef = ref<Nullable<TreeActionType>>(null);
      const { createMessage } = useMessage();

      function getTree() {
        const tree = unref(treeRef);
        if (!tree) {
          throw new Error('tree is null!');
        }
        return tree;
      }

      function handleLevel(level: number) {
        getTree().filterByLevel(level);
      }

      function handleSetCheckData() {
        getTree().setCheckedKeys(['0-0']);
      }

      function handleGetCheckData() {
        const keys = getTree().getCheckedKeys();
        createMessage.success(JSON.stringify(keys));
      }

      function handleSetSelectData() {
        getTree().setSelectedKeys(['0-0']);
      }

      function handleGetSelectData() {
        const keys = getTree().getSelectedKeys();
        createMessage.success(JSON.stringify(keys));
      }

      function handleSetExpandData() {
        getTree().setExpandedKeys(['0-0']);
      }

      function handleGetExpandData() {
        const keys = getTree().getExpandedKeys();
        createMessage.success(JSON.stringify(keys));
      }

      function checkAll(checkAll: boolean) {
        getTree().checkAll(checkAll);
      }

      function expandAll(checkAll: boolean) {
        getTree().expandAll(checkAll);
      }

      function appendNodeByKey(parentKey: string | null = null) {
        getTree().insertNodeByKey({
          parentKey: parentKey,
          node: {
            title: '新增节点',
            key: '2-2-2',
          },
          // 往后插入
          push: 'push',
          // 往前插入
          // push:'unshift'
        });
      }

      function deleteNodeByKey(key: string) {
        getTree().deleteNodeByKey(key);
      }

      function updateNodeByKey(key: string) {
        getTree().updateNodeByKey(key, {
          title: 'parent2-new',
        });
      }

      return {
        treeData,
        treeRef,
        handleLevel,
        handleSetCheckData,
        handleGetCheckData,
        handleSetSelectData,
        handleGetSelectData,
        handleSetExpandData,
        handleGetExpandData,
        appendNodeByKey,
        deleteNodeByKey,
        updateNodeByKey,
        checkAll,
        expandAll,
      };
    },
  });
</script>
