<template>
  <j-multi-line-select-biz-component :width="1000" v-bind="configs"  v-on="$listeners"/>
</template>

<script>
  import JMultiLineSelectBizComponent from './JMultiLineSelectBizComponent'

  export default {
    name: 'JSelectVendor',
    components: { JMultiLineSelectBizComponent },
    props: ['value'],
    data() {
      return {
        settings: {
          name: '供应商',
          displayKey: 'vendorName',
          returnKeys: ['id,','mdCode','vendorName','vendorType'],
          listUrl: '/base/mdmVendorInfo/listVendorForBiz',
          queryParamCode: 'mdCode',
          queryParamText: '供应商编号',
          multiple:false,
          columns: [
            { title: '供应商编号', dataIndex: 'mdCode', align: 'center', width: '30%', widthRight: '70%' },
            { title: '供应商名称', dataIndex: 'vendorName', align: 'center', width: '35%' },
            { title: '供应商类型', dataIndex: 'vendorType', align: 'center', width: '25%' },
           ]
        }
      }
    },
    computed: {
      configs() {
        return Object.assign({ value: this.value }, this.settings, this.$attrs)
      },
      setVendor(){
        try {
          this.$emit('setVendorName','名称是什么=='+this.value);
        }catch (ex) {
          console.log(ex);
        }
      }
    }

  }
</script>

<style lang="scss" scoped></style>