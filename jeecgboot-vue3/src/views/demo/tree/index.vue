<template>
  <PageWrapper title="Tree基础示例">
    <Row :gutter="[16, 16]">
      <Col :span="8">
        <BasicTree title="基础示例，默认展开第一层" :treeData="treeData" defaultExpandLevel="1" />
      </Col>
      <Col :span="8">
        <BasicTree title="可勾选，默认全部展开" :treeData="treeData" :checkable="true" defaultExpandAll @check="handleCheck" />
      </Col>
      <Col :span="8">
        <BasicTree title="指定默认展开/勾选示例" :treeData="treeData" :checkable="true" :expandedKeys="['0-0']" :checkedKeys="['0-0']" />
      </Col>
      <Col :span="8">
        <BasicTree title="懒加载异步树" ref="asyncTreeRef" :treeData="tree" :load-data="onLoadData" />
      </Col>
      <Col :span="8">
        <Card title="异步数据，默认展开">
          <template #extra>
            <a-button @click="loadTreeData" :loading="treeLoading">加载数据</a-button>
          </template>
          <Spin :spinning="treeLoading">
            <BasicTree ref="asyncExpandTreeRef" :treeData="tree2" />
          </Spin>
        </Card>
      </Col>
      <Col :span="8">
        <Card title="BasicTree内置加载">
          <template #extra>
            <a-button @click="loadTreeData2" :loading="treeLoading">请求数据</a-button>
          </template>
          <BasicTree ref="loadTreeRef" :treeData="tree2" :loading="treeLoading" />
        </Card>
      </Col>
    </Row>
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, nextTick, ref, unref } from 'vue';
  import { BasicTree, TreeActionType, TreeItem } from '/@/components/Tree/index';
  import { treeData } from './data';
  import { PageWrapper } from '/@/components/Page';
  import { Card, Row, Col, Spin } from 'ant-design-vue';
  import { cloneDeep, uniq } from 'lodash-es';
  import { isArray } from '/@/utils/is';

  export default defineComponent({
    name: 'system-testtree',
    components: { BasicTree, PageWrapper, Card, Row, Col, Spin },
    setup() {
      const asyncTreeRef = ref<Nullable<TreeActionType>>(null);
      const asyncExpandTreeRef = ref<Nullable<TreeActionType>>(null);
      const loadTreeRef = ref<Nullable<TreeActionType>>(null);
      const tree2 = ref<TreeItem[]>([]);
      const treeLoading = ref(false);

      function handleCheck(checkedKeys, e) {
        console.log('onChecked', checkedKeys, e);
      }

      function loadTreeData() {
        treeLoading.value = true;
        // 以下是模拟异步获取数据
        setTimeout(() => {
          // 设置数据源
          tree2.value = cloneDeep(treeData);
          treeLoading.value = false;
          // 展开全部
          nextTick(() => {
            console.log(unref(asyncExpandTreeRef));
            unref(asyncExpandTreeRef)?.expandAll(true);
          });
        }, 2000);
      }
      function loadTreeData2() {
        treeLoading.value = true;
        // 以下是模拟异步获取数据
        setTimeout(() => {
          // 设置数据源
          tree2.value = cloneDeep(treeData);
          treeLoading.value = false;
        }, 2000);
      }

      const tree = ref([
        {
          title: 'parent ',
          key: '0-0',
        },
      ]);

      function onLoadData(treeNode) {
        return new Promise((resolve: (value?: unknown) => void) => {
          if (isArray(treeNode.children) && treeNode.children.length > 0) {
            resolve();
            return;
          }
          setTimeout(() => {
            const asyncTreeAction: TreeActionType | null = unref(asyncTreeRef);
            if (asyncTreeAction) {
              const nodeChildren = [
                { title: `Child Node ${treeNode.eventKey}-0`, key: `${treeNode.eventKey}-0` },
                { title: `Child Node ${treeNode.eventKey}-1`, key: `${treeNode.eventKey}-1` },
              ];
              asyncTreeAction.updateNodeByKey(treeNode.eventKey, { children: nodeChildren });
              asyncTreeAction.setExpandedKeys(uniq([treeNode.eventKey, ...asyncTreeAction.getExpandedKeys()]));
            }

            resolve();
            return;
          }, 300);
        });
      }
      return {
        treeData,
        handleCheck,
        tree,
        onLoadData,
        asyncTreeRef,
        asyncExpandTreeRef,
        loadTreeRef,
        tree2,
        loadTreeData,
        treeLoading,
        loadTreeData2,
      };
    },
  });
</script>
