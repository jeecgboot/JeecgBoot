<template>
  <a-popover trigger="click" placement="bottomRight" :overlayStyle="{ width: '300px' }">
    <template slot="content">
      <a-spin :spinning="loadding">
        <a-tabs>
          <a-tab-pane v-for="(tab, k) in tabs" :tab="tab.title" :key="k">

          </a-tab-pane>
        </a-tabs>
      </a-spin>
    </template>
    <span @click="fetchNotice" class="header-notice">
      <a-badge count="12">
        <a-icon style="font-size: 16px; padding: 4px" type="bell" />
      </a-badge>
    </span>
  </a-popover>
</template>

<script>
  export default {
    name: "HeaderNotice",
    props: {
      tabs: {
        type: Array,
        default: null,
        required: true
      }
    },
    data () {
      return {
        loadding: false
      }
    },
    methods: {
      fetchNotice () {
        if (this.loadding) {
          this.loadding = false
          return
        }
        this.loadding = true
        setTimeout(() => {
          this.loadding = false
        }, 2000)
      }
    }
  }
</script>

<style lang="scss" scoped>
  .header-notice{
    display: inline-block;
    transition: all 0.3s;
    span {
      vertical-align: initial;
    }
  }
</style>