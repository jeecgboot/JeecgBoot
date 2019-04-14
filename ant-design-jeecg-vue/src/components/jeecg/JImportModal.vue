<template>
  <a-modal
    title="导入EXCEL"
    :width="600"
    :visible="visible"
    :confirmLoading="uploading"
    @cancel="handleClose">

    <a-upload
      name="file"
      :multiple="true"
      accept=".xls,.xlsx"
      :fileList="fileList"
      :remove="handleRemove"
      :beforeUpload="beforeUpload">
      <a-button>
        <a-icon type="upload" />
        选择导入文件
      </a-button>
    </a-upload>

    <template slot="footer">
      <a-button @click="handleClose">关闭</a-button>
      <a-button
        type="primary"
        @click="handleImport"
        :disabled="fileList.length === 0"
        :loading="uploading">
        {{ uploading ? '上传中...' : '开始上传' }}
      </a-button>
    </template>

  </a-modal>
</template>

<script>
  import { postAction } from '@/api/manage'
  export default {
    name: 'JImportModal',
    props:{
      url:{
        type: String,
        default: '',
        required: false
      }
    },
    data(){
      return {
        visible:false,
        uploading:false,
        fileList:[],
        uploadAction:''
      }
    },
    watch: {
      url (val) {
        if(val){
         this.uploadAction = window._CONFIG['domianURL']+val
        }
      }
    },
    created () {
      this.uploadAction = window._CONFIG['domianURL']+this.url
    },

    methods:{
      handleClose(){
        this.visible=false
      },
      show(){
        this.fileList = []
        this.uploading = false
        this.visible = true
      },
      handleRemove(file) {
        const index = this.fileList.indexOf(file);
        const newFileList = this.fileList.slice();
        newFileList.splice(index, 1);
        this.fileList = newFileList
      },
      beforeUpload(file) {
        this.fileList = [...this.fileList, file]
        return false;
      },
      handleImport() {
        const { fileList } = this;
        const formData = new FormData();
        fileList.forEach((file) => {
          formData.append('files[]', file);
        });
        this.uploading = true
        postAction(this.uploadAction, formData).then((res) => {
          this.uploading = false
          if(res.success){
            this.$message.success(res.message)
            this.visible=false
            this.$emit('ok')
          }else{
            this.$message.warning(res.message)
          }
        })
      }

    }
  }
</script>

<style scoped>

</style>