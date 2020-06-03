<template>
  <a-layout id="components-layout-demo-top-side">
    <a-layout-header class="header">
      <business-menu :item-list="headMenus" :menu-style="headStyle"  mode="horizontal"  @clickHandle = "headHandle"></business-menu>

    </a-layout-header>
    <a-layout-content style="padding: 0 0">

      <a-layout style="padding: 24px 0;  background: #fff">
        <a-layout-sider width="200" style="background: #fff">

          <business-menu :item-list="leftMenus" :menu-style="leftStyle"  mode="inline"  @clickHandle = "leftHandle"></business-menu>
        </a-layout-sider>
        <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
          <base-info v-if="leftActive==1 && topActive==1"  :companyId="companyId"/>
          <qualification v-if="leftActive==2 && topActive==1"  :companyId="companyId"/>
          <userinfo-list v-if="leftActive==3 && topActive==1" :companyId="companyId"/>
          <product-material-list v-if="leftActive==4 && topActive==1" :companyId="companyId"/>
          <base-info v-if="leftActive==5 && topActive==1"/>
          <company-acceptance-list v-if="leftActive==6 && topActive==1" :companyId="companyId"/>
          <prevention v-if="leftActive==7 && topActive==1" :companyId="companyId"/>
          <company-dirty-allow-list v-if="leftActive==8 && topActive==1" :companyId="companyId"/>
          <base-info v-if="leftActive==9 && topActive==1"/>
          <base-info v-if="leftActive==10 && topActive==1"/>
          <base-info v-if="leftActive==11 && topActive==1"/>
          <company-env-tax-list v-if="leftActive==12 && topActive==1" :companyId="companyId"/>
          <base-info v-if="leftActive==13 && topActive==1"/>
          <base-info v-if="leftActive==14 && topActive==1"/>

          <company-dynamic-supervision-list v-if="leftActive==1 && topActive==2" :companyId="companyId"/>
          <company-admin-penalties-list v-if="leftActive==2 && topActive==2" :companyId="companyId" />
          <company-supervisory-monitor-list v-if="leftActive==3 && topActive==2" :companyId="companyId" />
          <company-complaint-letter-list v-if="leftActive==4 && topActive==2" :companyId="companyId"/>

        </a-layout-content>
      </a-layout>
    </a-layout-content>
  </a-layout>
</template>



<script>
    import BusinessMenu from "../../component/BusinessMenu";

    import {getDetailMenus} from "../../requestAction/request"
    import Qualification from "./routeView/Qualification";
    import CompanyAcceptanceList from "./routeView/CompanyAcceptanceList";
    import BaseInfo from "./routeView/BaseInfo";
    import BasicInfo from "./routeView/BasicInfo";
    import CompanyDynamicSupervisionList from './routeView/CompanyDynamicSupervisionList'
    import UserinfoList from "./routeView/UserinfoList"
    import ProductMaterialList from "./routeView/ProductMaterialList";
    import Prevention from "./routeView/Prevention";
    import CompanyDirtyAllowList from "./routeView/CompanyDirtyAllowList";
    import CompanyAdminPenaltiesList from "./CompanyAdminPenaltiesList";
    import CompanySupervisoryMonitorList from "./CompanySupervisoryMonitorList";
    import CompanyEnvTaxList from "./routeView/CompanyEnvTaxList";
    import CompanyComplaintLetterList from "./routeView/CompanyComplaintLetterList";
    export default {
      name: "CompanyDetail",
      components: {
        CompanyEnvTaxList,
        CompanyAcceptanceList,
        BusinessMenu,BaseInfo,BasicInfo,Qualification,Prevention,CompanyDirtyAllowList,CompanyDynamicSupervisionList, CompanyAdminPenaltiesList,
        CompanySupervisoryMonitorList,CompanyComplaintLetterList,
        UserinfoList,
        ProductMaterialList,
        CompanyEnvTaxList},
      props:{
        companyId:''
      },
      data(){
        return {
          topActive:1,
          leftActive:1,
          leftMenus:[],
          basicInfoMenus : [],
          superviseMenus:[],
          leftStyle :{
            top: '0px',
            left:'0px'
          },
          headMenus : [
            {"key":"1","text":"基础信息"},
            {"key":"2","text":"监督检查"},
          ],
          headStyle :{
            lineHeight: '64px'
          }
        }
      },
      methods:{
        headHandle(key){
          this.topActive = key;
          //key1为 基础信息  2为监督检查
          if(key==="1"){
            this.leftMenus = this.basicInfoMenus;
          }else{
            this.leftMenus = this.superviseMenus;
          }
        },
        leftHandle(key){

          this.leftActive = key;
          console.log("topActive",this.topActive,"leftActive",this.leftActive);
        }
      },
      created() {

        console.log("res",getDetailMenus);
        //发送请求，查找
        getDetailMenus({companyId:this.companyId}).then((res)=>{
          console.log("res",res);
          if(res.success){
            this.basicInfoMenus = res.result.basicInfoMenus;
            this.superviseMenus = res.result.superviseMenus;
            this.leftMenus = this.basicInfoMenus;
          }else{
            console.log(res.message);
          }
        });

      }

    }
</script>

<style scoped>

</style>