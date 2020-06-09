<template>
  <div>
    <a-tabs default-active-key="1"  >
      <a-tab-pane key="1" tab="企业形象">
          <pic-list :images="qualificttionImgs.companyImage" qualificttion-type="companyImage" :isApply="isApply"/>
      </a-tab-pane>
      <a-tab-pane key="2" tab="营业执照照片" force-render>
        <pic-list :images="qualificttionImgs.businessLicense" qualificttion-type="businessLicense" :isApply="isApply" />
      </a-tab-pane>
      <a-tab-pane key="3" tab="企业平面图">
        <pic-list :images="qualificttionImgs.floorPlan" qualificttion-type="floorPlan" :isApply="isApply" />
      </a-tab-pane>

      <a-tab-pane key="4" tab="生产工艺图">
        <pic-list :images="qualificttionImgs.produceCrafts" qualificttion-type="produceCrafts" :isApply="isApply" />
      </a-tab-pane>
      <a-tab-pane key="5" tab="治理工艺图">
        <pic-list :images="qualificttionImgs.controlCrafts" qualificttion-type="controlCrafts" :isApply="isApply" />
      </a-tab-pane>

    </a-tabs>

  </div>
</template>

<script>
    //一企一档 企业资质
    //企业形象照片
    import {loadQualifications} from "../../../requestAction/request"
    import PicList from "../../../component/PicList";
    import {getFileAccessHttpUrl} from '@/api/manage';


    export default {
      name: "Qualification",
      components:{
        PicList
      },
      props:{
        companyId:''
      },
      data() {
        return {
          qualificttionImgs:{},
          isApply:false
        };
      },
      methods:{
        loadQualificationImgs(){
          let that = this;
          loadQualifications({companyId:that.companyId}).then((res)=>{
            if(res.success){
              console.log(res.success);
              that.qualificttionImgs=res.result;
            }else{
              console.log(res.message);
            }
          });
        }
      },
      created() {

        //查询
        this.loadQualificationImgs();

      }
    }
</script>

<style scoped>

</style>