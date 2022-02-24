<template>
  <a-modal
    title="分屏"
    :width="modalWidth"
    :visible="visible"
    :bodyStyle="bodyStyle"
    style="top: 0px;"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">

    <split-pane :min-percent='20' :default-percent='50' split="vertical">
      <template slot="paneL">
        <split-panel-a></split-panel-a>
      </template>
      <template slot="paneR">
        <split-panel-b></split-panel-b>
      </template>
    </split-pane>

  </a-modal>
</template>

<script>
  import splitPane from 'vue-splitpane'
  import SplitPanelA from './SplitPanelA'
  import SplitPanelB from './SplitPanelB'
  export default {
    name: "SplitPanelModal",
    components:{
      splitPane,
      SplitPanelA,
      SplitPanelB
    },
    data () {
      return {
        visible: false,
        bodyStyle:{
          padding: "0",
          //update-begin---author:wangshuai ---date:20220104  for：[JTC-411]火狐 分屏 图片大时，与按钮重叠，样式不好------------
          height:(window.innerHeight-140)+"px"
          //update-begin---author:wangshuai ---date:20220104  for：[JTC-411]火狐 分屏 图片大时，与按钮重叠，样式不好------------
        },
        modalWidth:800,
      }
    },
    created () {
      this.modalWidth = window.innerWidth-0;
    },
    methods: {
      show () {
        this.visible = true;
      },
      handleOk(){

      },
      handleCancel () {
        this.visible = false;
      },
    }
  }
</script>

<style scoped>
</style>