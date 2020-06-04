<template>
  <j-modal
    ref="modal"
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
    v-if="visible">
    <base-info  :companyId="companyId"></base-info>
  </j-modal>
</template>

<script>
  import BaseInfo from "../../../oneCompayOneRecord/routeView/BaseInfo";


  export default {
        name: "JmodalBaseInfo",
    components:{
      BaseInfo
    },
      data(){
          return{
            companyId:this.$store.getters.userInfo.companyIds[0],
            confirmLoading:true,
            visible: false,
            width:1200,
            title:"申请"
          }

      },
      methods:{
        handleCancel () {
          this.visible = false;
          // this.close()
        },
        handleOk(){

        }

      }
    }
</script>

<style scoped>

</style>