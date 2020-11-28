<template>
  <a-card :bordered="false">
    <!-- 左侧文件树 -->
    <a-col :span="4" class="clName">
      <a-tree
        :treeData="treeData"
        :defaultExpandAll="defaultExpandAll"
        @select="this.onSelect"
        style="height: 500px;overflow-y: auto;"
      >
      </a-tree>
    </a-col>
    <!-- 中间面板 -->
    <a-col :span="2"/>
    <!--右侧缩略图-->
    <a-col :span="18">
      <a-spin tip="Loading..." :spinning="spinning">
        <div v-for="(file, key) in dataSource" :key="key">
          <a-row>
            <a-col :span="24"><p><a-divider orientation="left">{{ file.fileName }}</a-divider></p></a-col>
            <!-- 预览区域 -->
            <a-col :span="24">
              <template v-if="file.filePdfPath">
                <div style="float: left;width:104px;height:104px;margin-right: 10px;margin: 0 8px 8px 0;">
                  <div style="width: 100%;height: 100%;position: relative;padding: 8px;" @click="pdfPreview(file.title)">
                    <img style="width: 100%;" src="~@/assets/pdf4.jpg">
                  </div>
                </div>
              </template>
              <template v-else>
                (暂无材料，点击"选择文件"或"扫描上传"上传文件)
              </template>
            </a-col>
          </a-row>
        </div>
      </a-spin>
    </a-col>
    <pdf-preview-modal ref="pdfmodal"></pdf-preview-modal >
  </a-card>
</template>

<script>

  import { getAction } from '@/api/manage'
  import { ACCESS_TOKEN } from "@/store/mutation-types"
  import Vue from 'vue'
  import PdfPreviewModal from './modules/PdfPreviewModal'
  const mockdata=[{
    "id": "1",
    "key": "1",
    "title": "实例.pdf",
    "fileCode": "shili",
    "fileName": "实例",
    "filePdfPath": "实例"
  }]

  export default {
    name: "JeecgPdfView",
    components:{
      PdfPreviewModal
    },
    data () {
      return {
        description: 'PDF预览页面',
        // 文件类型集
        treeData:[{
          title: '所有PDF电子档',
          key: '0-0',
          children: mockdata }],
        // 文件数据集
        dataSource: mockdata,
        allData:mockdata,
        // 上传文件集
        defaultExpandAll: true,
        // 加载中
        spinning:false,
        url: {
          pdfList: "/mock/api/pdfList",
        },
      }
    },
    created() {
      //this.loadData();
    },
    methods: {
      loadData (){
        this.spinning = false;
        getAction(this.url.pdfList).then((res)=>{
          if(res.length>0){
            this.allData = res;
            this.dataSource = res;
            this.treeData[0].children = res;
          }
          this.spinning = false;
        })
      },
      pdfPreview:function(title){
        const token = Vue.ls.get(ACCESS_TOKEN);
        this.headers = {"X-Access-Token":token}
        this.$refs.pdfmodal.previewFiles(title,token);
      },
      // 选择文件类型
      onSelect (selectedKeys, info) {
        this.dataSource = [];
        if(selectedKeys[0] === undefined || selectedKeys[0] === '0-0'){
          this.dataSource = this.allData;
        }else{
          this.dataSource.push(info.node._props.dataRef);
        }
        console.log("SELECT-->dataSource",this.dataSource );
      },
      // model回调
      modalFormOk () {
        this.loadData();
      },
    },
  }
</script>

<style scoped>
  .clName .ant-tree li span.ant-tree-switcher, .ant-tree li span.ant-tree-iconEle{width:10px}
</style>