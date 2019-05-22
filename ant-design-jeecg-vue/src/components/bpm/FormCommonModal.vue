<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    destroyOnClose
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
        <dynamic-component ref="dynamiclink" :path="path" :formData="formData" @ok="callBackOk" @close="callBackClose"></dynamic-component>
    </a-spin>
  </a-modal>
</template>

<script>

  import DynamicComponent from "./DynamicComponent";

  export default {
    name: "FormCommonModal",
    props: ['path'],
    components: {
      DynamicComponent
    },
    data () {
      return {
        title:"操作",
        width:"80%",
        visible: false,
        confirmLoading: false,
        formData:{},
      }
    },
    created () {
    },
    methods: {
      add () {
        this.formData =[];
        this.title = "新增";
        this.visible = true;
        this.$refs.dynamiclink.add();
      },
      edit (record) {
        var  data = {
          dataId:record.id,
        }
        this.formData = data;
        this.visible = true;
      },
      callBackClose () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        this.$refs.dynamiclink.handleOk();
      },
      callBackOk(){
        this.$emit('ok');
        this.callBackClose();
      },
      handleCancel () {
        this.callBackClose()
      },
    }
  }
</script>

<style scoped>

</style>