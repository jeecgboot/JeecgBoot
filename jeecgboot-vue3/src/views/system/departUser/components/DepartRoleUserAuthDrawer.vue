<template>
  <BasicDrawer title="部门角色分配" :width="365" @close="onClose" @register="registerDrawer">
    <a-spin :spinning="loading">
      <template v-if="desformList.length > 0">
        <a-checkbox-group v-model:value="designNameValue">
          <a-row>
            <a-col :span="24" v-for="item of desformList">
              <a-checkbox :value="item.id">{{ item.roleName }}</a-checkbox>
            </a-col>
          </a-row>
        </a-checkbox-group>
        <div style="width: 100%; margin-top: 15px">
          <a-button type="primary" :loading="loading" :size="'small'" preIcon="ant-design:save-filled" @click="onSubmit">
            <span>点击保存</span>
          </a-button>
        </div>
      </template>
      <a-empty v-else description="无配置信息" />
    </a-spin>
  </BasicDrawer>
</template>

<script lang="ts" setup>
  import { ref, unref } from 'vue';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';

  import { queryDepartRoleByUserId, queryDepartRoleUserList, saveDepartRoleUser } from '../depart.user.api';

  defineEmits(['register']);
  const loading = ref<boolean>(false);
  const userId = ref('');
  const departId = ref('');
  const oldRoleId = ref('');
  const desformList = ref<Array<any>>([]);
  const designNameValue = ref<Array<any>>([]);

  // 注册抽屉组件
  const [registerDrawer, { closeDrawer }] = useDrawerInner((data) => {
    userId.value = unref(data.userId);
    departId.value = unref(data.departId);
    loadData();
  });

  async function loadData() {
    try {
      loading.value = true;
      const params = {
        departId: departId.value,
        userId: userId.value,
      };
      // 查询 DepartRole
      const [$desformList, $departRoleList] = await Promise.all([queryDepartRoleUserList(params), queryDepartRoleByUserId(params)]);
      desformList.value = $desformList;
      designNameValue.value = $departRoleList.map((item) => item.droleId);
      oldRoleId.value = designNameValue.value.join(',');
    } finally {
      loading.value = false;
    }
  }

  async function onSubmit() {
    try {
      loading.value = true;
      await saveDepartRoleUser({
        userId: userId.value,
        newRoleId: designNameValue.value.join(','),
        oldRoleId: oldRoleId.value,
      });
      doClose();
    } finally {
      loading.value = false;
    }
  }

  function onClose() {
    doReset();
  }

  function doClose() {
    doReset();
    closeDrawer();
  }

  function doReset() {
    userId.value = '';
    departId.value = '';
    oldRoleId.value = '';
    desformList.value = [];
    designNameValue.value = [];
  }
</script>
