<template>
  <j-modal
    title="详细信息"
    :width="1200"
    :visible="visible"
    @ok="handleOk"
    @cancel="close"
    switch-fullscreen
    :fullscreen.sync="fullscreen"
  >

    <transition name="fade">
      <div v-if="visible">
        <slot name="mainForm" :row="row" :column="column"/>
        <slot name="subForm" :row="row" :column="column"/>
      </div>
    </transition>

  </j-modal>
</template>
<script>

  import { cloneObject } from '@/utils/util'

  export default {
    name: 'JVxeDetailsModal',
    inject: ['superTrigger'],
    data() {
      return {
        visible: false,
        fullscreen: false,
        row: null,
        column: null,
      }
    },
    created() {
    },
    methods: {

      open(event) {
        let {row, column} = event
        this.row = cloneObject(row)
        this.column = column
        this.visible = true
      },

      close() {
        this.visible = false
      },

      handleOk() {
        this.superTrigger('detailsConfirm', {
          row: this.row,
          column: this.column,
          callback: (success) => {
            this.visible = !success
          },
        })
      },

    },
  }
</script>
<style lang="less">
  .fade-enter-active,
  .fade-leave-active {
    opacity: 1;
    transition: opacity 0.5s;
  }

  .fade-enter,
  .fade-leave-to {
    opacity: 0;
  }
</style>