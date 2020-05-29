<template>
  <a-layout id="components-layout-demo-top-side">
    <a-layout-header class="header">
      <business-menu :item-list="headMenus" :menu-style="headStyle"  mode="horizontal"  @headHandle = 'headHandle'></business-menu>

    </a-layout-header>
    <a-layout-content style="padding: 0 0">

      <a-layout style="padding: 24px 0;  background: #fff">
        <a-layout-sider width="160" style="background: #fff">

          <business-menu :item-list="leftMenus" :menu-style="leftStyle"  mode="inline"></business-menu>
        </a-layout-sider>
        <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
           <base-info v-if="leftActive==1 && topActive==1"/>
           <basic-info v-if="leftActive==1 && topActive==2"/>
        </a-layout-content>
      </a-layout>
    </a-layout-content>
  </a-layout>
</template>



<script>
    import BusinessMenu from "../../component/BusinessMenu";
    import BaseInfo from "./routeView/BaseInfo";
    import BasicInfo from "./routeView/BasicInfo";
    import getDetailMenus from "../../requestAction/request"

    export default {
      name: "CompanyDetail",
      components: {BusinessMenu,BaseInfo,BasicInfo},
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
            left:'0px',
            width:'150px'
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
          console.log("key",key);
          this.topActive = key;
          //key1为 基础信息  2为监督检查
          if(key==="1"){
            this.leftMenus = this.basicInfoMenus;
          }else{
            this.leftMenus = this.superviseMenus;
          }
        }
      },
      created() {
        //发送请求，查找
        getDetailMenus({companyId:this.companyId}).then((res)=>{
          if(res.success){
            this.basicInfoMenus = res.result.basicInfoMenus;
            this.superviseMenus = res.result.superviseMenus;
            this.leftMenus = this.basicInfoMenus;
          }else{
            console.log(res.message);
          }
        })

      }

    }
</script>

<style scoped>

</style>