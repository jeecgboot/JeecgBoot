<template>
  <a-card :bordered="false">
    <a-tabs>
      <a-tab-pane tab="ERP布局模板" key="erp">
        <erp-template/>
      </a-tab-pane>
      <a-tab-pane tab="布局模板1" key="1">
        <template1/>
      </a-tab-pane>
      <a-tab-pane tab="布局模板2" key="2">
        <template2/>
      </a-tab-pane>
      <a-tab-pane tab="布局模板3" key="3">
        <template3/>
      </a-tab-pane>
      <a-tab-pane tab="布局模板4" key="4">
        <template4/>
      </a-tab-pane>
      <a-tab-pane tab="布局模板5" key="5">
        <template5/>
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>

<script>
  import Template1 from './Template1'
  import Template2 from './Template2'
  import Template3 from './Template3'
  import Template4 from './Template4'
  import Template5 from './Template5'
  import ErpTemplate from './ErpTemplate'

  export default {
    name: 'LayoutDemo',
    components: {Template5, Template4, Template3, Template2, Template1, ErpTemplate}
  }
</script>

<style scoped>

</style>