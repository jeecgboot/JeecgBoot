<template>
   <div>
    <a-row >
      <a-col span="4">
        <left-card  v-if="!listshow" :title="cardTitle" :hoverable="hoverable" @toDetail = "latestDetail" @toApply="apply"></left-card>
      </a-col>
      <a-col span="20">
        <company-apply-list v-if="!listshow" :company-id="companyId" :from-table="fromTable" @toDetail = "applyDetail"></company-apply-list>
      </a-col>
    </a-row>
    <company-acceptance-list  v-if="listshow" :company-id="companyId" :operationshow="operationshow" :status="status"></company-acceptance-list>
   </div>
</template>

<script>

  import CompanyApplyList from "./modules/CompanyApplyList"
  import CompanyAcceptanceList from "../oneCompayOneRecord/routeView/CompanyAcceptanceList";
  import {queryLatestArchivedData} from "../../requestAction/request"
  import LeftCard from "../../component/LeftCard";
    export default {
        name: "AcceptanceApply",
      components:{
        LeftCard,
        CompanyApplyList,
        queryLatestArchivedData,
        CompanyAcceptanceList
      },
        data(){
          return {
            listshow:false,
            fromTable:"company_acceptance",
            hoverable:true,
            //最新归档信息数据
            latestArchived:{},
            companyId:this.$store.getters.userInfo.companyIds[0],
            operationshow:false,
            status:""
          }
        },
      //计算属性
      computed:{
        cardTitle(){
          debugger
          if(JSON.stringify(this.latestArchived)=='{}'
          ||this.latestArchived==null){
            this.hoverable = false;
            return "暂无基础信息申报";
          }
          this.hoverable = true;
          return "最新归档信息";

        }
      },
        methods:{
          //详情
          latestDetail(){
            this.title="详情";
            this.listshow = true;
            this.operationshow=false;
            this.status="2";
          },
          //新增申请
          apply(){
            this.title="详情";
            this.listshow = true;
            this.operationshow=true;
          },
          applyDetail(){

          }


        },
      created() {

          let that = this;
          //查询最新归档信息
        queryLatestArchivedData({companyId:this.companyId,fromTable:this.fromTable}).then((res)=>{
          if(res.success){
            that.latestArchived = res.result;
            console.log("res"+res.result);
          }else{
            that.$message.warning(res.message);
          }

        })


      }
    }
</script>

<style scoped>

</style>