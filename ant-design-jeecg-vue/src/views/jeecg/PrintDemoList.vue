<template>
  <a-card :bordered="false" :class="{'abcdefg':true}">
    <div class="no-print" style="text-align: right">
      <a-button v-print="'#acceptProof'" ghost type="primary">打印</a-button>
    </div>
    <section ref="print" id="acceptProof" class="abcdefg">
      <div style="text-align: center">
        <p style="font-size: 24px;font-weight: 800">打印测试表单</p>
      </div>
      <!--签字-->
      <div class="sign" style="text-align: left">
        <a-form-item label="打印员：" :labelCol="labelCol" :wrapperCol="wrapperCol" hasFeedback>
          <a-input placeholder="请输入您的名字"/>
        </a-form-item>
        <a-form-item label="打印日期：" :labelCol="labelCol" :wrapperCol="wrapperCol" hasFeedback>
          <a-date-picker></a-date-picker>
        </a-form-item>
        <a-form-item label="打印内容：" :labelCol="labelCol" :wrapperCol="wrapperCol" hasFeedback>
          <a-textarea placeholder="请输入打印内容..."/>
        </a-form-item>
        <a-form-item label="打印目的：" :labelCol="labelCol" :wrapperCol="wrapperCol" hasFeedback>
          <a-textarea placeholder="请输入打印目的..."/>
        </a-form-item>
        <a-form-item label="打印说明：" :labelCol="labelCol" :wrapperCol="wrapperCol" hasFeedback>
          <a-textarea placeholder="请输入打印说明..."/>
        </a-form-item>
        <a-form-item label="打印图片：" :labelCol="labelCol" :wrapperCol="wrapperCol" hasFeedback>
          <a-upload
            action="//jsonplaceholder.typicode.com/posts/"
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
        </a-form-item>
      </div>
    </section>
  </a-card>
  <!--</page-layout>-->
</template>
<script>
  import ACol from "ant-design-vue/es/grid/Col";
  import ARow from "ant-design-vue/es/grid/Row";
  import {getAction} from '@/api/manage';
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
        columns: [
/*          {
            title: '已提交的文件资料',
            dataIndex: 'fileCategoryName',
            align:"center",
          },
          {
            title: '份数',
            dataIndex: 'fileNum',
            align:"center",
          },
          {
            title: '材料介质',
            dataIndex: 'fileType',
            align:"center",
          },*/
        ],
        dataSource:[],
        applicantName:"",
        labelCol: {
          xs: { span: 24 },
          sm: { span: 2 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 8 },
        },
        beginYear:"",
        beginMonth:"",
        beginDay:"",
        endYear:"",
        endMonth:"",
        endDay:"",
        ipagination:{
          hideOnSinglePage:false,
        },
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
      loadData(reBizCode){
        // 获取材料文件
        getAction(this.url.loadRegisterFiles,{reBizCode:reBizCode}).then((res)=>{
          if(res.success){
            console.log(res.result)
            this.dataSource = res.result;
          }
        });
        // 获取申请人信息
        getAction(this.url.loadApplicant,{reBizCode:reBizCode}).then((res)=>{
          if(res.success){
            this.applicant = res.result;
            var name ="";
            for(var i=0;i<res.result.length;i++){
              if(i==res.result.length-1){
                name = name+res.result[i].name;
              }else{
                name = name+res.result[i].name+",";
              }
            }
            if(name=="" || name==null ||name=="null"){
              this.applicantName = "";
            }else{
              this.applicantName = name;
            }

          }
        });

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
  .abcdefg .ant-card-body{
    margin-left: 0%;
    margin-right: 0%;
    margin-bottom: 1%;
    border:0px solid black;
    min-width: 800px;
  }
  .explain{
    text-align: left;
    margin-left: 50px;
  }
  .explain .ant-input,.sign .ant-input{
    font-weight:bolder;
    text-align:center;
    border-left-width:0px!important;
    border-top-width:0px!important;;
    border-right-width:0px!important;;
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