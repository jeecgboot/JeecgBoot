<template>
  <company-apply-list :company-id="companyId" :hoverable="latestArchived"
                      :from-table="fromTable" @applyDetail = "applyDetail"  @toDetail="latestDetail" @toApply="apply"
                      v-if="!showDetail"></company-apply-list>
  <qualification :company-id="companyId" v-else></qualification>
</template>

<script>
  import CompanyApplyList from "./modules/CompanyApplyList"
  import {queryLatestArchivedData} from "../../requestAction/request";
  import Qualification from "../oneCompayOneRecord/routeView/Qualification";
    export default {
        name: "QualificationApply",
      components:{
        CompanyApplyList,Qualification
      },
      data(){
          return {
            fromTable:"company_qualification",
            companyId:this.$store.getters.userInfo.companyIds[0],
            showDetail:false
          }
      },
      created() {

        let that = this;
        //查询最新归档信息
        queryLatestArchivedData({companyId:this.companyId,fromTable:this.fromTable}).then((res)=>{
          console.log(res)
          if(res.success){
            that.latestArchived = res.result;
            console.log(res.result);
          }else{
            that.$message.warning(res.message);
          }

        })


      },
      methods:{
        latestDetail(){
          this.showDetail = true;
        }
      }
    }
</script>

<style scoped>

</style>