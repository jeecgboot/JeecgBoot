<template>
  <BasicDrawer class="online-auth-manager-drawer" @register="registerDrawer" title="权限管理" :width="800" @close="onClose">
    <a-tabs v-model:activeKey="activeKey">
      <a-tab-pane tab="字段权限" key="field" forceRender>
        <AuthFieldConfig :headId="headId" v-model:authFields="authFields" />
      </a-tab-pane>
      <template v-if="hasDataAuth">
        <a-tab-pane tab="按钮权限" key="button" forceRender>
          <AuthButtonConfig :headId="headId" :tableType="curTableType" :isTree="curIsTree" />
        </a-tab-pane>
        <a-tab-pane tab="数据权限" key="data" forceRender>
          <!-- 数据权限不需要实时刷新 故而此处传原表单ID -->
          <AuthDataConfig :cgformId="cgformId" :authFields="authFields" :tableType="curTableType" />
        </a-tab-pane>
      </template>
    </a-tabs>
  </BasicDrawer>
</template>

<script lang="ts">
  import { computed, defineComponent, ref } from 'vue';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import AuthFieldConfig from './manager/AuthFieldConfig.vue';
  import AuthButtonConfig from './manager/AuthButtonConfig.vue';
  import AuthDataConfig from './manager/AuthDataConfig.vue';

  export default defineComponent({
    name: 'AuthManagerDrawer',
    components: {
      BasicDrawer,
      AuthFieldConfig,
      AuthButtonConfig,
      AuthDataConfig,
    },
    props: {
      // 1单表 2主表 3附表
      tableType: {
        type: Number,
        default: 1,
      },
    },
    emits: ['register'],
    setup(props) {
      const cgformId = ref('');
      const headId = ref('');
      const authFields = ref([]);
      const activeKey = ref('field');
      const curTableType = ref(1);
      const curIsTree = ref('N');
      const hasDataAuth = computed(() => props.tableType == 1 || props.tableType == 2);

      const [registerDrawer, { closeDrawer }] = useDrawerInner((data) => {
        cgformId.value = data.cgformId;
        headId.value = cgformId.value + '?' + new Date().getTime();
        activeKey.value = 'field';
        // update-begin--author:liaozhiyang---date:20240520---for：【TV360X-187】去掉子表权限管理中按钮权限的高级查询
        curTableType.value = data.tableType;
        curIsTree.value = data.isTree || 'N';
        // update-end--author:liaozhiyang---date:20240520---for：【TV360X-187】去掉子表权限管理中按钮权限的高级查询
      });

      function onClose() {
        closeDrawer();
      }

      return {
        activeKey,
        cgformId,
        headId,
        authFields,
        hasDataAuth,
        onClose,
        registerDrawer,
        curTableType,
        curIsTree,
      };
    },
  });
</script>

<style lang="less">
// update-begin--author:liaozhiyang---date:20260811---for：【LHZP-311】权限管理调整到50条/页时会出现两个滚动条
.online-auth-manager-drawer {
  .is-vertical {
    display: none;
  }
}
// update-end--author:liaozhiyang---date:20260811---for：【LHZP-311】权限管理调整到50条/页时会出现两个滚动条
</style>
