<template>
  <j-select-biz-component :width="1000" v-bind="configs" v-on="$listeners"/>
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
          queryParamCode: 'name',
          queryParamText: '职务名称',
          columns: [
            { title: '职务名称', dataIndex: 'name', align: 'center', width: '30%', widthRight: '70%' },
            { title: '职务编码', dataIndex: 'code', align: 'center', width: '35%' },
            { title: '职级', dataIndex: 'rank_dictText', align: 'center', width: '25%' }
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