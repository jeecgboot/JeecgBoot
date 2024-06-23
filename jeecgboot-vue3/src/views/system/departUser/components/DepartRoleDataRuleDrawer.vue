<template>
  <BasicDrawer title="数据规则/按钮权限配置" :width="365" @close="onClose" @register="registerDrawer">
    <a-spin :spinning="loading">
      <a-tabs defaultActiveKey="1">
        <a-tab-pane tab="数据规则" key="1">
          <a-checkbox-group v-model:value="dataRuleChecked" v-if="dataRuleList.length > 0">
            <a-row>
              <a-col :span="24" v-for="(item, index) in dataRuleList" :key="'dr' + index">
                <a-checkbox :value="item.id">{{ item.ruleName }}</a-checkbox>
              </a-col>
              <a-col :span="24">
                <div style="width: 100%; margin-top: 15px">
                  <a-button type="primary" :loading="loading" :size="'small'" preIcon="ant-design:save-filled" @click="saveDataRuleForRole">
                    <span>点击保存</span>
                  </a-button>
                </div>
              </a-col>
            </a-row>
          </a-checkbox-group>
          <a-empty v-else description="无配置信息" />
        </a-tab-pane>
        <!--<a-tab-pane tab="按钮权限" key="2">敬请期待!!!</a-tab-pane>-->
      </a-tabs>
    </a-spin>
  </BasicDrawer>
</template>

<script lang="ts" setup>
  import { ref, unref } from 'vue';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';

  import { queryDepartRoleDataRule, saveDepartRoleDataRule } from '../depart.user.api';

  defineEmits(['register']);
  const loading = ref<boolean>(false);
  const departId = ref('');
  const functionId = ref('');
  const roleId = ref('');
  const dataRuleList = ref<Array<any>>([]);
  const dataRuleChecked = ref<Array<any>>([]);

  // 注册抽屉组件
  const [registerDrawer, { closeDrawer }] = useDrawerInner((data) => {
    roleId.value = unref(data.roleId);
    departId.value = unref(data.departId);
    functionId.value = unref(data.functionId);
    loadData();
  });

  async function loadData() {
    try {
      loading.value = true;
      const { datarule, drChecked } = await queryDepartRoleDataRule(functionId, departId, roleId);
      dataRuleList.value = datarule;
      if (drChecked) {
        dataRuleChecked.value = drChecked.split(',');
      }
    } finally {
      loading.value = false;
    }
  }

  function saveDataRuleForRole() {
    let params = {
      permissionId: functionId.value,
      roleId: roleId.value,
      dataRuleIds: dataRuleChecked.value.join(','),
    };
    saveDepartRoleDataRule(params);
  }

  function onClose() {
    doReset();
  }

  function doReset() {
    functionId.value = '';
    roleId.value = '';
    dataRuleList.value = [];
    dataRuleChecked.value = [];
  }
</script>
