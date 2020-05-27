<template>
  <j-select-biz-component :width="1000" v-bind="configs" v-on="$listeners"/>
</template>

<script>
  import JSelectBizComponent from '@/components/jeecgbiz/JSelectBizComponent'

  export default {
    name: 'JSelectCompany',
    components: { JSelectBizComponent },
    props: ['value'],
    data() {
      return {
        settings: {
          name: '公司',
          displayKey: 'shortName',
          returnKeys: ['companyId', 'shortName'],
          listUrl: '/company/companyBaseinfo/list',
          queryParamCode: 'shortName',
          queryParamText: '公司简称',
          columns: [
            { title: '公司简称', dataIndex: 'shortName', align: 'center', width: '30%', widthRight: '70%' },
            { title: '统一社会信用代码', dataIndex: 'companyId', align: 'center', width: '35%' },
            { title: '企业法人', dataIndex: 'corporate', align: 'center', width: '25%' }
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

<style lang="less" scoped></style>