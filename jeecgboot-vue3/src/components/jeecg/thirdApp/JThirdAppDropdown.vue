<template>
  <a-dropdown v-if="syncToApp && syncToLocal">
    <a-button type="primary" preIcon="ant-design:sync-outlined">同步{{ name }}</a-button>
    <template #overlay>
      <a-menu @click="handleMenuClick">
        <a-menu-item v-if="syncToApp" key="to-app">同步到{{ name }}</a-menu-item>
        <a-menu-item v-if="getSyncToLocal" key="to-local">同步到本地</a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
  <a-button v-else-if="syncToApp" type="primary" preIcon="ant-design:sync-outlined" @click="handleMenuClick({ key: 'to-app' })"
    >同步{{ name }}</a-button
  >
  <a-button v-else type="primary" preIcon="ant-design:sync-outlined" @click="handleMenuClick({ key: 'to-local' })">同步{{ name }}到本地</a-button>
</template>

<script lang="ts" setup>
  /* JThirdAppButton 的子组件，不可单独使用 */
  import { computed } from 'vue';

  const props = defineProps({
    type: String,
    name: String,
    syncToApp: Boolean,
    syncToLocal: Boolean,
  });
  // 声明Emits
  const emit = defineEmits(['to-app', 'to-local']);

  const getSyncToLocal = computed(() => {
    // 由于企业微信接口变更，将不再支持同步到本地
    if (props.type === 'wechatEnterprise') {
      return false;
    }
    return props.syncToLocal;
  });

  function handleMenuClick(event) {
    emit(event.key, { type: props.type });
  }
</script>

<style scoped></style>
