<template>
  <a-card>
    <a-button @click="splitPane" type="primary" icon="desktop">点我分屏</a-button>

    <split-panel-modal ref="splitPanelModal"></split-panel-modal>
  </a-card>
</template>

<script>
  import SplitPanelModal from './modules/SplitPanelModal'

  export default {
    name: 'SplitPanel',
    components:{
      SplitPanelModal,
    },
    data() {
      return {
        description: '分屏',
      }
    },
    methods:{
      splitPane(){
        this.$refs.splitPanelModal.show();
      }
    }
  }
</script>

<style scoped>

</style>