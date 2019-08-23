<template>
  <a-card :bordered="false">

    <a-tabs>

      <a-tab-pane tab="普通列表" key="1">
        <default-table/>
      </a-tab-pane>

      <a-tab-pane tab="只读列表" key="2">
        <read-only-table/>
      </a-tab-pane>

      <a-tab-pane tab="三级联动" key="3">
        <three-linkage/>
      </a-tab-pane>

    </a-tabs>

  </a-card>
</template>

<script>
  import DefaultTable from './modules/JEditableTable/DefaultTable'
  import ReadOnlyTable from './modules/JEditableTable/ReadOnlyTable'
  import ThreeLinkage from './modules/JEditableTable/ThreeLinkage'

  export default {
    name: 'JeecgEditableTableExample',
    components: { DefaultTable, ReadOnlyTable, ThreeLinkage },
    data() {
      return {}
    },
    methods: {}
  }
</script>

<style scoped>

</style>