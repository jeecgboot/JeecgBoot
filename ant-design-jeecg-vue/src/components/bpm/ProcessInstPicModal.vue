<template>
  <a-modal
    :title="title"
    :width="900"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
    :bodyStyle="bodyStyle"
    style="top: 50px;"
    destroyOnClose
    :footer="null"
    cancelText="关闭">
    
    <a-spin :spinning="confirmLoading">
      <img :src="picUrl" alt="流程图" usemap="#planetmap"/>
      <map name="planetmap">
        <template v-for="(item, key, index) in nodePositionInfo.positionList">
          <area shape="rect" :coords="item.coords" title="Venus" @mouseover="showNodeInfo(nodePositionInfo.hisTasks,item.id)">
        </template>
      </map>
    </a-spin>
    <proc-node-info-model ref="nodeInfoModel"></proc-node-info-model>
  </a-modal>
</template>

<script>
  import { getAction } from '@/api/manage'
  import qs from 'qs';
  import ProcNodeInfoModel from "./ProcNodeInfoModel.vue";

  export default {
    components: {ProcNodeInfoModel},
    name: "ProcessInstPicModal",
    data () {
      return {
        title:"操作",
        visible: false,
        nodePositionInfo:{},
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        bodyStyle:{
          "overflow-y":"auto",
          "overflow-x":"auto",
          height:(window.innerHeight-280)+"px",
        },
        confirmLoading: false,
        picUrl:"",
        url: {
          getProcessInfo: "/process/extActFlowData/getProcessInfo",
          getNodePositionInfo:"/act/task/getNodePositionInfo",
        },
      }
    },
    created () {
    },
    methods: {
      preview(flowCode,dataId){
        this.visible = true;
        var params = {
          flowCode:flowCode,
          dataId:dataId
        };//查询条件
        this.confirmLoading = true;
        getAction(this.url.getProcessInfo,params).then((res)=>{
          if(res.success){
            var processInstanceId = res.result.processInstanceId;
            this.picUrl =  this.getResourceURL(processInstanceId);
            this.getNodePositionInfoData(processInstanceId);
            console.log("---流程图----",this.picUrl)
          }else{
            this.$message.warning(res.message);
          }
        }).catch(e => {
          console.error(e)
        }).then(() => {
          this.confirmLoading = false;
        })


      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleCancel () {
        this.close()
      },
      // 获取静态资源访问地址
      getResourceURL(processInstanceId) {
        var params = qs.stringify({
          //'token': Cookies.get('token'),
          '_t': Date.parse(new Date())/1000,
          'processInstanceId': processInstanceId
        })
        return `${window._CONFIG['domianURL']}/act/process/processPic?${params}`
      },
      // 获取静态资源访问地址
      getResourceURL(processInstanceId) {
        var params = qs.stringify({
          //'token': Cookies.get('token'),
          '_t': Date.parse(new Date())/1000,
          'processInstanceId': processInstanceId
        })
        return `${window._CONFIG['domianURL']}/act/process/processPic?${params}`
      },

      // 查询坐标信息数据
      getNodePositionInfoData(processInstanceId) {
        var params = {processInstanceId:processInstanceId};//查询条件
        getAction(this.url.getNodePositionInfo,params).then(res => {
          if (res.success) {
            this.nodePositionInfo = res.result
          }
        }).catch(e => {
          console.error(e)
        }).then(() => {
        })
      },
      showNodeInfo(data,taskId){
        this.$refs.nodeInfoModel.close();
        this.$refs.nodeInfoModel.showInfo(data,taskId);
      },
    }
  }
</script>

<style scoped>

</style>