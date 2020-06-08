<template>
  <company-apply-list :company-id="companyId" :hoverable="latestArchived"
                      :from-table="fromTable" @applyDetail = "applyDetail"  @toDetail="latestDetail" @toApply="apply"
                      v-if="!showDetail"></company-apply-list>
</template>

<script>
  import CompanyApplyList from "./modules/CompanyApplyList"
    export default {
        name: "ProductMaterialApply",
      components:{
        CompanyApplyList
      },
      data(){
        return {
          fromTable:"company_product_material",
          companyId:this.$store.getters.userInfo.companyIds[0],
          showDetail:false
        }
      },

    }
</script>

<style scoped>

</style>