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
          <router-view/>
        </a-layout-content>
      </a-layout>
    </a-layout-content>
  </a-layout>
</template>



<script>
    import BusinessMenu from "../../component/BusinessMenu";

    export default {
      name: "CompanyDetail",
      components: {BusinessMenu},
      data(){
        return {
          leftMenus :
          [
            {"key":"1","text":"基本信息"},
            {"key":"2","text":"企业资质"},
            {"key":"3","text":"员工信息"}
          ],
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

          if(key==="1"){
            this.leftMenus = [
              {"key":"1","text":"基本信息"},
              {"key":"2","text":"企业资质"},
              {"key":"3","text":"员工信息"}
            ];
          }else{
            this.leftMenus = [
              {"key":"1","text":"年度动态监管"},
              {"key":"2","text":"行政处罚信息"},
              {"key":"3","text":"信访投诉信息"},
              {"key":"4","text":"信访投诉信息"}
            ];
          }
        }
      }

    }
</script>

<style scoped>

</style>