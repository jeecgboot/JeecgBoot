<template>
  <a-dropdown v-if="syncToApp && syncToLocal">
    <a-button type="primary" icon="sync">同步{{name}}</a-button>
    <a-menu slot="overlay" @click="handleMenuClick">
      <a-menu-item v-if="syncToApp" key="to-app">同步到{{name}}</a-menu-item>
      <a-menu-item v-if="syncToLocal" key="to-local">同步到本地</a-menu-item>
    </a-menu>
  </a-dropdown>
  <a-button v-else-if="syncToApp" type="primary" icon="sync" @click="handleMenuClick({key:'to-app'})">同步{{name}}</a-button>
  <a-button v-else type="primary" icon="sync" @click="handleMenuClick({key:'to-local'})">同步{{name}}到本地</a-button>
</template>

<script>

/* JThirdAppButton 的子组件，不可单独使用 */
export default {
  name: 'JThirdAppDropdown',
  props: {
    type: String,
    name: String,
    syncToApp: Boolean,
    syncToLocal: Boolean,
  },
  methods: {
    handleMenuClick(event) {
      this.$emit(event.key, {type: this.type})
    },
  },
}
</script>

<style scoped>

</style>