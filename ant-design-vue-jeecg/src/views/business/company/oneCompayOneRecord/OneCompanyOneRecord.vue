<template>
  <div>
    <company-baseinfo-list v-if="list_visible"  @toDetail="companyDetail"></company-baseinfo-list>
    <company-detail v-if="!list_visible" :companyId = "companyId"></company-detail>
  </div>
</template>

<script>


  import store from '@/store/'
  import CompanyBaseinfoList from './CompanyBaseinfoList'
  import CompanyDetail from './CompanyDetail'
  export default {
      name: "OneCompanyOneRecord",
      components: { CompanyBaseinfoList ,CompanyDetail},

      data() {
        return {
          // 控制子组件显示与隐藏的标识，类型为Boolean
          list_visible: false,
          companyId : ''
        }
      },
      created() {

        console.log("userInfo",store.getters.userInfo)
        const companyIds = store.getters.userInfo.companyIds;
        if(companyIds.length>1){
          this.list_visible = true;

        }else{
          this.list_visible = false;
        }

      },
      methods:{
        changeVisible(value){

          this.list_visible = value;
        },
        companyDetail(companyId){
          console.log("companyId",companyId);
          this.changeVisible(false);
          this.companyId = companyId;
        }
      }


  }
</script>

<style scoped>

</style>