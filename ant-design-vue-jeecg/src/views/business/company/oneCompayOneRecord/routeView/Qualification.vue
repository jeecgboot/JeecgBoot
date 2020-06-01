<template>
  <div>
    <a-tabs default-active-key="1"  >
      <a-tab-pane key="1" tab="企业形象">
        <img style="width: 100%;" :src="companyImage"  :preview="controlCrafts">
      </a-tab-pane>
      <a-tab-pane key="2" tab="营业执照照片" force-render>
        <img style="width: 100%;" :src="businessLicense"  :preview="businessLicense">

      </a-tab-pane>
      <a-tab-pane key="3" tab="企业平面图">
        <img style="width: 100%;" :src="floorPlan"  :preview="businessLicense">

      </a-tab-pane>

      <a-tab-pane key="4" tab="生产工艺图">
        <img style="width: 100%;" :src="produceCrafts"  :preview="businessLicense">

      </a-tab-pane>
      <a-tab-pane key="5" tab="治理工艺图">
        <img style="width: 100%;" :src="controlCrafts"  :preview="businessLicense">

      </a-tab-pane>

    </a-tabs>

  </div>
</template>

<script>
    //一企一档 企业资质
    //企业形象照片
    import {loadQualifications} from "../../../requestAction/request"
    import {getFileAccessHttpUrl} from '@/api/manage';

    export default {
      name: "Qualification",
      props:{
        companyId:''
      },
      data() {
        return {
          companyImage:'',
          controlCrafts:'',
          businessLicense:'',
          floorPlan:'',
          produceCrafts:'',




        };
      },
      methods:{

      },
      created() {
        let that = this;
        //查询
        loadQualifications({companyId:that.companyId}).then((res)=>{
          if(res.success){
            that.companyImage=getFileAccessHttpUrl(res.result.companyImage);
            that.controlCrafts=getFileAccessHttpUrl(res.result.controlCrafts);
            that.businessLicense=getFileAccessHttpUrl(res.result.businessLicense);
            that.floorPlan=getFileAccessHttpUrl(res.result.floorPlan);
            that.produceCrafts=getFileAccessHttpUrl(res.result.produceCrafts);
          }else{
            console.log(res.message);
          }


        });

      }
    }
</script>

<style scoped>

</style>