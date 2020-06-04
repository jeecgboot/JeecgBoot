<template>
   <div>
    <a-row >
      <a-col span="4">
        <left-card :title="cardTitle" :hoverable="hoverable" @toDetail = "latestDetail"></left-card>
      </a-col>
      <a-col span="20">
        <company-apply-list :company-id="companyId" from-table="company_baseinfo" @toDetail = "applyDetail"></company-apply-list>
      </a-col>
    </a-row>
    <jmodal-base-info ref="baseInfoForm"></jmodal-base-info>
   </div>
</template>

<script>

  import CompanyApplyList from "./modules/CompanyApplyList"
  import {queryLatestArchivedData} from "../../requestAction/request"
  import LeftCard from "../../component/LeftCard";
  import JmodalBaseInfo from "./modules/childModules/JmodalBaseInfo";
    export default {
        name: "BasicInfoApply",
      components:{
        JmodalBaseInfo,
        LeftCard,
        CompanyApplyList,
        queryLatestArchivedData
      },
        data(){
          return {

            hoverable:true,
            //最新归档信息数据
            latestArchived:{},
            companyId:this.$store.getters.userInfo.companyIds[0],

          }
        },
      //计算属性
      computed:{
          cardTitle(){
            if(this.latestArchived.companyId){
              this.hoverable = true;
              return "最新归档信息";
            }
            this.hoverable = false;
            return "暂无基础信息申报";

          }
      },
        methods:{
          //详情
          latestDetail(){
            let that = this;
            //查询详情数据
            that.$refs.baseInfoForm.title="详情";
            that.$refs.baseInfoForm.disableSubmit = false;
            that.$refs.baseInfoForm.visible = true;
          },
          //新增申请
          apply(){

          },
          applyDetail(){

          }


        },
      created() {

          let that = this;
          //查询最新归档信息

        queryLatestArchivedData({companyId:this.companyId}).then((res)=>{
          if(res.success){
            that.latestArchived = res.result;
            console.log(res.result);
          }else{
            that.$message.warning(res.message);
          }

        })


      }
    }
</script>

<style scoped>

</style>