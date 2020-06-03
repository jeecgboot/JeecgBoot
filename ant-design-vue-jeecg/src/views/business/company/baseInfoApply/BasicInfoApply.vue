<template>
    <a-row >
      <a-col span="4">
        <template>

            <a-card :title="title"  :hoverable="hoverable"  :headStyle = "{'font-size':'16px','text-align':'center'}"
                    style="width: 90%;">
              <p>简略信息</p>
              <p>简略信息</p>
              <p>简略信息</p>
              <p>简略信息</p>
              <p>简略信息</p>
              <p>一些简略信息</p>
              <template slot="actions" class="ant-card-actions">
                <span @click="detail" style="font-size: 16px">详情</span>
                <span @click="apply" style="font-size: 16px">申请</span>
              </template>
            </a-card>
        </template>
      </a-col>
      <a-col span="20">
        <company-apply-list></company-apply-list>
      </a-col>
    </a-row>
</template>

<script>

  import CompanyApplyList from "./modules/CompanyApplyList";
  import {queryLatestArchivedData} from "../../requestAction/request"
    export default {
        name: "BasicInfoApply",
      components:{
        CompanyApplyList,
        queryLatestArchivedData
      },
        data(){
          return {

            hoverable:true,
            //最新归档信息数据
            latestArchived:{},
            companyId:this.$store.getters.userInfo.companyIds[0]
          }
        },
      //计算属性
      computed:{
          title(){

            if(this.latestArchived){
              return "暂无基础信息申报";
            }
            return "最新归档信息";

          }
      },
        methods:{
          //详情
          detail(){

          },
          //新增申请
          apply(){

          }



        },
      created() {

          let that = this;
          //查询最新归档信息
        debugger
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