<template>
   <div>
    <a-row >
      <a-col span="24">
        <company-apply-list :company-id="companyId" :hoverable="latestArchived"
                            :from-table="fromTable" @applyDetail = "applyDetail"  @toDetail="latestDetail" @toApply="apply"
                            ></company-apply-list>
      </a-col>
    </a-row>
    <jmodal-base-info ref="baseInfoForm" ></jmodal-base-info>
     <companyApply-modal ref="applyInfoForm" ></companyApply-modal>
   </div>
</template>

<script>

  import CompanyApplyList from "./modules/CompanyApplyList"
  import {queryComparisonData, queryLatestArchivedData} from "../../requestAction/request"
  import JmodalBaseInfo from "./modules/childModules/JmodalBaseInfo";
  import CompanyApplyModal from "./modules/childModules/CompanyApplyModal";
    export default {
        name: "BasicInfoApply",
      components:{
        JmodalBaseInfo,
        CompanyApplyList,
        CompanyApplyModal
      },
        data(){
          return {
            fromTable:"company_baseinfo",
            //最新归档信息数据
            latestArchived:false,
            companyId:this.$store.getters.userInfo.companyIds[0],

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
            let that = this;
            debugger
            //查询前后明细
            queryComparisonData({beforeId:record.id,afterId:record.newId}).then((res)=>{
              if(res.success) {
                console.log(res.result);
                that.$refs.applyInfoForm.data = res.result;
              }else{
                this.$message.error(res.message);
              }
            })
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