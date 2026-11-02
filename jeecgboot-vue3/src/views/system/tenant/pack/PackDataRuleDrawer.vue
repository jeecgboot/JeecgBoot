<template>
  <BasicDrawer v-bind="$attrs" @register="registerDrawer" title="数据规则配置" width="450px" destroyOnClose>
    <a-tabs defaultActiveKey="1">
      <a-tab-pane tab="数据规则" key="1">
        <a-checkbox-group v-model:value="dataRuleChecked" v-if="dataRuleList.length > 0">
          <a-row>
            <a-col :span="24" v-for="(item, index) in dataRuleList" :key="'dr' + index">
              <a-checkbox :value="item.id">{{ item.ruleName }}</a-checkbox>
            </a-col>

            <a-col :span="24">
              <div style="width: 100%; margin-top: 15px">
                <a-button @click="saveDataRuleForPack" type="primary" size="small"> <Icon icon="ant-design:save-outlined"></Icon>点击保存</a-button>
              </div>
            </a-col>
          </a-row>
        </a-checkbox-group>
        <div v-else><h3>无配置信息!</h3></div>
      </a-tab-pane>
    </a-tabs>
  </BasicDrawer>
</template>
<script lang="ts" setup>
  // 【issues/9820】租户套餐包菜单支持分配数据规则
  import { ref, unref } from 'vue';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { queryPackDataRule, savePackDataRule } from '../tenant.api';
  // 声明Emits
  const emit = defineEmits(['success', 'register']);
  const { createMessage } = useMessage();
  // 声明数据
  const functionId = ref('');
  const packId = ref('');
  const dataRuleList = ref([]);
  const dataRuleChecked = ref([]);

  /**
   * 数据
   */
  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    await reset();
    setDrawerProps({ confirmLoading: false });
    //权限的id
    functionId.value = data.functionId;
    //套餐包的id
    packId.value = data.packId;
    //查询数据
    const res = await queryPackDataRule({ functionId: unref(functionId), packId: unref(packId) });
    if (res.success) {
      dataRuleList.value = res.result.datarule;
      if (res.result.drChecked) {
        dataRuleChecked.value = res.result.drChecked.split(',');
      }
    }
  });

  /**
   * 重置
   */
  function reset() {
    functionId.value = '';
    packId.value = '';
    dataRuleList.value = [];
    dataRuleChecked.value = [];
  }

  /**
   * 提交
   */
  async function saveDataRuleForPack() {
    if (!unref(dataRuleChecked) || unref(dataRuleChecked).length == 0) {
      createMessage.warning('请注意，现未勾选任何数据权限!');
    }
    let params = {
      permissionId: unref(functionId),
      packId: unref(packId),
      dataRuleIds: unref(dataRuleChecked).join(','),
    };
    await savePackDataRule(params);
    //关闭弹窗
    closeDrawer();
    emit('success');
  }
</script>
