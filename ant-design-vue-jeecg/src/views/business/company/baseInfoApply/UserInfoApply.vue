<template>
  <div>


      <company-apply-list :company-id="companyId" :hoverable="latestArchived"
                        :from-table="fromTable" @applyDetail = "applyDetail"  @toDetail="latestDetail" @toApply="apply"
                         v-if="!showDetail"></company-apply-list>

    <userinfo-list :company-id="companyId" ref="userinfoList" v-if="showDetail" ></userinfo-list>
    <companyApply-modal ref="applyInfoForm" ></companyApply-modal>
  </div>
</template>

<script>

  import CompanyApplyList from "./modules/CompanyApplyList"
  import {queryLatestArchivedData} from "../../requestAction/request"
  import CompanyApplyModal from "./modules/childModules/CompanyApplyModal";
  import UserinfoList from "../oneCompayOneRecord/routeView/UserinfoList";

  export default {
        name: "UserInfoApply",
      components:{
        CompanyApplyList,
        queryLatestArchivedData,
        CompanyApplyModal,
        UserinfoList
      },
      data(){
        return {
          fromTable:"company_userinfo",
          //最新归档信息数据
          latestArchived:false,
          companyId:this.$store.getters.userInfo.companyIds[0],
          showDetail:false,

        }
      },//计算属性

      methods:{
        //详情
        latestDetail(){
          //查询详情数据
          this.showDetail = true;
        },
        //新增申请
        apply(){

          //查询详情数据
          this.showDetail = true;


        },
        applyDetail(record){
          console.log(record);
          //查询详情数据
          this.$refs.applyInfoForm.detail(record);

        },


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


      }
    }
</script>

<style scoped>

</style>