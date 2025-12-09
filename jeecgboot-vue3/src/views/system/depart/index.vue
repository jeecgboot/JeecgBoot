<template>
  <a-row :class="['p-4', `${prefixCls}--box`]" type="flex" :gutter="10">
    <a-col :xl="10" :lg="24" :md="24" style="margin-bottom: 10px">
      <DepartLeftTree ref="leftTree" @select="onTreeSelect" @rootTreeData="onRootTreeData" />
    </a-col>
    <a-col :xl="14" :lg="24" :md="24" style="margin-bottom: 10px">
      <div style="height: 100%;" :class="[`${prefixCls}`]">
        <a-tabs v-show="departData != null" defaultActiveKey="base-info">
          <a-tab-pane tab="基本信息" key="base-info" forceRender style="position: relative">
            <div style="padding: 20px">
              <DepartFormTab :data="departData" :rootTreeData="rootTreeData" @success="onSuccess" />
            </div>
          </a-tab-pane>
          <a-tab-pane tab="部门权限" key="role-info">
            <div style="padding: 0 20px 20px">
              <DepartRuleTab :data="departData" />
            </div>
          </a-tab-pane>
          <a-tab-pane tab="职级汇报关系" key="rank">
            <div style="padding: 0 20px 20px">
              <DepartRankRelation :data="departData" />
            </div>
          </a-tab-pane>
          <a-tab-pane tab="用户列表" key="user">
            <div style="padding: 0 20px 20px">
              <DepartUserList :data="departData" :key="reRender"></DepartUserList>
            </div>
          </a-tab-pane>
          <a-tab-pane tab="部门负责人" key="departmentHead">
            <DepartmentHeadList :data="departData"></DepartmentHeadList>
          </a-tab-pane>
        </a-tabs>
        <div v-show="departData == null" style="padding-top: 40px">
          <a-empty description="尚未选择部门" />
        </div>
      </div>
    </a-col>
  </a-row>
</template>

<script lang="ts" setup name="system-depart">
  import { provide, ref } from 'vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import DepartLeftTree from './components/DepartLeftTree.vue';
  import DepartFormTab from './components/DepartFormTab.vue';
  import DepartRuleTab from './components/DepartRuleTab.vue';
  import DepartRankRelation from './components/DepartRankRelation.vue';
  import DepartUserList from './components/DepartUserList.vue';
  import DepartmentHeadList from './components/DepartmentHeadList.vue';

  const { prefixCls } = useDesign('depart-manage');
  provide('prefixCls', prefixCls);

  // 给子组件定义一个ref变量
  const leftTree = ref();

  // 当前选中的部门信息
  const departData = ref({});
  const rootTreeData = ref<any[]>([]);
  const reRender = ref(-1);

  // 左侧树选择后触发
  function onTreeSelect(data) {
    console.log('onTreeSelect: ', data);
    if (reRender.value == -1) {
      // 重新渲染组件
      reRender.value = Math.random();
    }
    departData.value = data;
  }

  // 左侧树rootTreeData触发
  function onRootTreeData(data) {
    rootTreeData.value = data;
  }

  function onSuccess() {
    leftTree.value.loadRootTreeData();
  }
</script>

<style lang="less">
  @import './index.less';
</style>
