<template>
  <reload-effect
    :vNode="innerValue"
    :effect="reloadEffect"
    @effect-end="handleEffectEnd"
  />
</template>

<script>
  import ReloadEffect from './ReloadEffect'
  import JVxeCellMixins from '@/components/jeecg/JVxeTable/mixins/JVxeCellMixins'

  export default {
    name: 'JVxeNormalCell',
    mixins: [JVxeCellMixins],
    components: {ReloadEffect},
    computed: {
      reloadEffectRowKeysMap() {
        return this.renderOptions.reloadEffectRowKeysMap
      },
      reloadEffect() {
        return (this.renderOptions.reloadEffect && this.reloadEffectRowKeysMap[this.row.id]) === true
      },
    },
    methods: {
      // 特效结束
      handleEffectEnd() {
        this.$delete(this.reloadEffectRowKeysMap, this.row.id)
      },
    },
    // 【组件增强】注释详见：JVxeCellMixins.js
    enhanced: {
      switches: {
        editRender: false,
      },
    }
  }
</script>

<style scoped>

</style>