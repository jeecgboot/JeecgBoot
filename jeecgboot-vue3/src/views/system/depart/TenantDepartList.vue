<template>
  <a-row :class="['p-4', `${prefixCls}--box`]" type="flex" :gutter="10">
    <a-col :xl="12" :lg="24" :md="24" style="margin-bottom: 10px">
      <DepartLeftTree
        v-if="showDepart"
        ref="leftTree"
        @select="onTreeSelect"
        @rootTreeData="onRootTreeData"
        :isTenantDepart="true"
        :loginTenantName="loginTenantName"
      />
    </a-col>
    <a-col :xl="12" :lg="24" :md="24" style="margin-bottom: 10px">
      <div style="height: 100%" :class="[`${prefixCls}`]">
        <a-tabs v-show="departData != null" defaultActiveKey="base-info">
          <a-tab-pane tab="基本信息" key="base-info" forceRender style="position: relative">
            <div style="padding: 20px">
              <DepartFormTab v-if="showDepart" :data="departData" :rootTreeData="rootTreeData" @success="onSuccess" :isTenantDepart="true" />
            </div>
          </a-tab-pane>
        </a-tabs>
        <div v-show="departData == null" style="padding-top: 40px">
          <a-empty description="尚未选择部门" />
        </div>
      </div>
    </a-col>
  </a-row>
</template>

<script lang="ts" setup name="TenantDepartList">
  import { onMounted, provide, ref } from 'vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import DepartLeftTree from '/@/views/system/depart/components/DepartLeftTree.vue';
  import DepartFormTab from '/@/views/system/depart/components/DepartFormTab.vue';
  import { getLoginTenantName } from '@/views/system/tenant/tenant.api';
  import { tenantSaasMessage } from '@/utils/common/compUtils';

  const { prefixCls } = useDesign('tenant-depart-manage');
  provide('prefixCls', prefixCls);

  // 给子组件定义一个ref变量
  const leftTree = ref();
  //是否显示部门
  const showDepart = ref(false);

  // 当前选中的部门信息
  const departData = ref({});
  const rootTreeData = ref<any[]>([]);
  const loginTenantName = ref<string>('');

  /**
   * 获取租户名称
   */
  getTenantName();

  async function getTenantName() {
    loginTenantName.value = await getLoginTenantName();
    if (loginTenantName.value) {
      showDepart.value = true;
    } else {
      showDepart.value = false;
    }
  }

  // 左侧树选择后触发
  function onTreeSelect(data) {
    departData.value = data;
  }

  // 左侧树rootTreeData触发
  function onRootTreeData(data) {
    rootTreeData.value = data;
  }

  function onSuccess() {
    leftTree.value.loadRootTreeData();
  }

  onMounted(() => {
    //提示信息
    tenantSaasMessage('租户部门');
  });
</script>

<style lang="less">
  @prefix-cls: ~'@{namespace}-tenant-depart-manage';

  .@{prefix-cls} {
    background: @component-background;

    &--box {
      .ant-tabs-nav {
        padding: 0 20px;
      }
    }
  }
</style>
