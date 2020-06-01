<template>
  <div>
    <a-tabs default-active-key="1"  >
      <a-tab-pane key="1" tab="企业形象">
        <img style="width: 100%;" :src="controlCrafts"  :preview="controlCrafts">
      </a-tab-pane>
      <a-tab-pane key="2" tab="营业执照照片" force-render>
        <img style="width: 100%;" :src="businessLicense"  :preview="businessLicense">

      </a-tab-pane>
      <a-tab-pane key="3" tab="企业平面图">
        Content of Tab Pane 3
      </a-tab-pane>

      <a-tab-pane key="4" tab="生产工艺图">
        Content of Tab Pane 3
      </a-tab-pane>
      <a-tab-pane key="5" tab="治理工艺图">
      Content of Tab Pane 3
    </a-tab-pane>

    </a-tabs>

  </div>
</template>

<script>
    //一企一档 企业资质
    //企业形象照片
    import {loadQualifications} from "../../../requestAction/request"
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
          debugger
          if(res.success){
            that.companyImage=res.result.companyImage;
            that.controlCrafts=res.result.controlCrafts;
            that.businessLicense=res.result.businessLicense;
            that.floorPlan=res.result.floorPlan;
            that.produceCrafts=res.result.produceCrafts;
          }else{
            console.log(res.message);
          }


        });

      }
    }
</script>

<style scoped>

</style>