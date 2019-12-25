<template>
  <a-card :bordered="false" :class="{'abcdefg':true}">
    <div class="no-print" style="text-align: right">
      <a-button v-print="'#printContent'" ghost type="primary">打印</a-button>
    </div>
    <section ref="print" id="printContent" class="abcdefg">
      <div style="text-align: center">
        <p style="font-size: 24px;font-weight: 800">打印测试表单</p>
      </div>
      <!--签字-->
      <a-col :md="24" :sm="24">
      <div class="sign" style="text-align: left;height: inherit">
        <a-col :span="24">
          <span>
            打印人员:
          </span>
          <a-input style="width: 30%" v-model="printer"/>
          <span style="margin-left: 12.5%">打印日期:</span>
          <a-input style="width: 30%" v-model="printTime"/>
        </a-col>
        <a-col :span="24">
        </a-col>
        <a-col :span="24" style="margin-top: 20px">
          <span>打印内容:</span>
          <a-input style="width: 80%" v-model="printContent"/>
        </a-col>
        <a-col :span="24" style="margin-top: 20px">
          <span>打印目的:</span>
          <a-input style="width: 80%" v-model="printReason"/>
        </a-col>
        <a-col style="margin-top: 20px" :span="24">
          <span>打印图片:</span>
          <br/>
          <a-upload
            action="/jsonplaceholder.typicode.com/posts/"
            listType="picture-card"
            :fileList="fileList"
            @preview="handlePreview"
            @change="handleChange">
            <div v-if="fileList.length < 3">
              <a-icon type="plus" />
              <div class="ant-upload-text">Upload</div>
            </div>
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-col>
      </div>
      </a-col>
    </section>
  </a-card>
  <!--</page-layout>-->
</template>
<script>
  import ACol from "ant-design-vue/es/grid/Col";
  import ARow from "ant-design-vue/es/grid/Row";
  import ATextarea from 'ant-design-vue/es/input/TextArea'

  export default {
    components: {
      ATextarea,
      ARow,
      ACol,
    },
    name: 'Printgzsld',
    props:{
      reBizCode:{
        type: String,
        default: ''
      }
    },
    data(){
      return {
        columns: [{
        }
        ],
        labelCol: {
          xs: { span: 24 },
          sm: { span: 2 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 8 },
        },
        printer:'张三',
        printTime:'2019-02-01 12:00:00',
        printContent:'打印内容就是,做一个打印测试',
        printReason:'做一个打印测试',
        previewVisible: false,
        previewImage: '',
        fileList: [{
          uid: '-1',
          name: 'xxx.png',
          status: 'done',
          url: 'https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png',
        },
          {
            uid:'-2',
            name:'pic1.png',
            status:'done',
            url:'https://www.gizbot.com/img/2016/11/whatsapp-error-lead-image-08-1478607387.jpg',
          }
        ],
        url:{
          loadApplicant:"/sps/register/loadApplicants",
          loadRegisterFiles:"/sps/register/getRegisterFilesConfig",
        }
      }
    },
    created() {
      this.getDate();
    },
    methods: {
      loadData(){

      },
      getDate(){
        // 当前时间
      },
      handleCancel () {
        this.previewVisible = false
      },
      handlePreview (file) {
        this.previewImage = file.url || file.thumbUrl
        this.previewVisible = true
      },
      handleChange ({ fileList }) {
        this.fileList = fileList
      }
    }
  }
</script>
<style scoped>
  /*update_begin author:scott date:20191203 for:打印机打印的字体模糊问题 */
  * {
    color: #000000!important;
    -webkit-tap-highlight-color: #000000!important;
  }
  /*update_end author:scott date:20191203 for:打印机打印的字体模糊问题 */

  .abcdefg .ant-card-body{
    margin-left: 0%;
    margin-right: 0%;
    margin-bottom: 1%;
    border:0px solid black;
    min-width: 800px;
    color:#000000!important;
  }
  .explain{
    text-align: left;
    margin-left: 50px;
    color:#000000!important;
  }
  .explain .ant-input,.sign .ant-input{
    font-weight:bolder;
    text-align:center;
    border-left-width:0px!important;
    border-top-width:0px!important;
    border-right-width:0px!important;
  }
  .explain div{
    margin-bottom: 10px;
  }
  /* you can make up upload button and sample style by using stylesheets */
  .ant-upload-select-picture-card i {
    font-size: 32px;
    color: #999;
  }

  .ant-upload-select-picture-card .ant-upload-text {
    margin-top: 8px;
    color: #666;
  }
</style>