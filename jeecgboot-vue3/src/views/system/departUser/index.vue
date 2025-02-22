<template>
  <a-row :class="['p-4', `${prefixCls}--box`]" :gutter="10">
    <a-col :xl="6" :lg="8" :md="10" :sm="24" style="flex: 1">
      <a-card :bordered="false" style="height: 100%">
        <DepartTree @select="onTreeSelect" />
      </a-card>
    </a-col>
    <a-col :xl="18" :lg="16" :md="14" :sm="24" style="flex: 1">
      <a-card :bordered="false" style="height: 100%">
        <a-tabs defaultActiveKey="user-info">
          <a-tab-pane tab="基本信息" key="base-info" forceRender>
            <DepartBaseInfoTab :data="departData" />
          </a-tab-pane>
          <a-tab-pane tab="用户信息" key="user-info">
            <DepartUserInfoTab :key="reRender" :data="departData" />
          </a-tab-pane>
          <a-tab-pane tab="部门角色" key="role-info">
            <DepartRoleInfoTab :key="reRender" :data="departData" />
          </a-tab-pane>
        </a-tabs>
      </a-card>
    </a-col>
  </a-row>
</template>

<script lang="ts" setup name="system-depart-user">
  import { provide, ref } from 'vue';
  import { useDesign } from '/@/hooks/web/useDesign';

  import DepartTree from './components/DepartTree.vue';
  import DepartBaseInfoTab from './components/DepartBaseInfoTab.vue';
  import DepartUserInfoTab from './components/DepartUserInfoTab.vue';
  import DepartRoleInfoTab from './components/DepartRoleInfoTab.vue';

  const { prefixCls } = useDesign('depart-user');
  provide('prefixCls', prefixCls);

  // 当前选中的部门信息
  let departData = ref({});

  const reRender = ref(-1);

  // 左侧树选择后触发
  function onTreeSelect(data) {
    // update-begin--author:liaozhiyang---date:20250106---for：【issues/7658】我的部门无部门列表数据时，点击查询或者重置能查出数据
    if (reRender.value == -1) {
      // 重新渲染组件
      reRender.value = Math.random();
    }
    // update-end--author:liaozhiyang---date:20250106---for：【issues/7658】我的部门无部门列表数据时，点击查询或者重置能查出数据
    departData.value = data;
  }
</script>

<style lang="less">
  @import './index.less';
</style>
