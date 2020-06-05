<template>
   <div>
    <a-row >
      <a-col span="4">
        <left-card :title="cardTitle" :hoverable="hoverable" @toDetail = "latestDetail" @toApply="apply"></left-card>
      </a-col>
      <a-col span="20">
        <company-apply-list :company-id="companyId" :from-table="fromTable" @toDetail = "applyDetail"></company-apply-list>
      </a-col>
    </a-row>
    <jmodal-base-info ref="baseInfoForm" ></jmodal-base-info>
     <companyApply-modal ref="applyInfoForm" ></companyApply-modal>
   </div>
</template>

<script>

  import CompanyApplyList from "./modules/CompanyApplyList"
  import {queryLatestArchivedData} from "../../requestAction/request"
  import LeftCard from "../../component/LeftCard";
  import JmodalBaseInfo from "./modules/childModules/JmodalBaseInfo";
  import CompanyApplyModal from "./modules/childModules/CompanyApplyModal";
    export default {
        name: "BasicInfoApply",
      components:{
        JmodalBaseInfo,
        LeftCard,
        CompanyApplyList,
        queryLatestArchivedData,
        CompanyApplyModal
      },
        data(){
          return {
            fromTable:"company_baseinfo",
            hoverable:true,
            //最新归档信息数据
            latestArchived:{},
            companyId:this.$store.getters.userInfo.companyIds[0],

          }
        },
      //计算属性
      computed:{
          cardTitle(){
            if(JSON.stringify(this.latestArchived)==='{}'){
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
            //查询详情数据
            this.$refs.baseInfoForm.title="详情";
            this.$refs.baseInfoForm.disableSubmit = false;
            this.$refs.baseInfoForm.visible = true;
            this.$refs.baseInfoForm.confirmLoading = false;

          },
          //新增申请
          apply(){

            this.$refs.baseInfoForm.title="申请";
            this.$refs.baseInfoForm.disableSubmit = true;
            this.$refs.baseInfoForm.visible = true;
            this.$refs.baseInfoForm.confirmLoading = false;


          },
          applyDetail(record){

            console.log(record)
            //查询详情数据
            this.$refs.applyInfoForm.detail(record);



          }


        },
      created() {

          let that = this;
          //查询最新归档信息
        queryLatestArchivedData({companyId:this.companyId,fromTable:this.fromTable}).then((res)=>{
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