<template>
  <company-apply-list :company-id="companyId" :hoverable="latestArchived"
                      :from-table="fromTable" @applyDetail = "applyDetail"  @toDetail="latestDetail" @toApply="apply"
                      v-if="!showDetail"></company-apply-list>
  <product-material-list v-else :company-id="companyId"> </product-material-list>
</template>

<script>
  import CompanyApplyList from "./modules/CompanyApplyList"
  import {queryLatestArchivedData} from "../../requestAction/request";
  import ProductMaterialList from "../oneCompayOneRecord/routeView/ProductMaterialList";
    export default {
      name: "ProductMaterialApply",
      components: {
        CompanyApplyList,
        ProductMaterialList
      },
      data() {
        return {
          fromTable: "company_product_material",
          companyId: this.$store.getters.userInfo.companyIds[0],
          showDetail: false,
          latestArchived: false
        }
      },
      methods: {
        //详情
        latestDetail() {
          //查询详情数据
          this.showDetail = true;
        },
        //新增申请
        apply() {

          this.$refs.baseInfoForm.title = "申请";
          this.$refs.baseInfoForm.disableSubmit = true;
          this.$refs.baseInfoForm.visible = true;
          this.$refs.baseInfoForm.confirmLoading = false;


        },
        applyDetail(record) {
          console.log(record);
          //查询详情数据
          this.$refs.applyInfoForm.detail(record);

        }


      }, created() {

        let that = this;
        //查询最新归档信息
        queryLatestArchivedData({companyId: this.companyId, fromTable: this.fromTable}).then((res) => {
          console.log(res)
          if (res.success) {
            that.latestArchived = res.result;
          } else {
            that.$message.warning(res.message);
          }

        })

      }
    }
</script>

<style scoped>

</style>