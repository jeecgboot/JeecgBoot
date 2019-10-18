<template>
  <j-select-biz-component v-bind="configs" v-on="$listeners"/>
</template>

<script>
  import JSelectBizComponent from './JSelectBizComponent'

  export default {
    name: 'JSelectPosition',
    components: { JSelectBizComponent },
    props: ['value'],
    data() {
      return {
        settings: {
          name: '职务',
          displayKey: 'name',
          returnKeys: ['id', 'code'],
          listUrl: '/sys/position/list',
          queryParamText: '职务编码',
          columns: [
            { title: '职务名称', dataIndex: 'name', align: 'center', width: 100 },
            { title: '职务编码', dataIndex: 'code', align: 'center', width: 100 },
            { title: '职级', dataIndex: 'rank_dictText', align: 'center', width: 100 }
          ]
        }
      }
    },
    computed: {
      configs() {
        return Object.assign({ value: this.value }, this.settings, this.$attrs)
      }
    }
  }
</script>

<style lang="scss" scoped></style>