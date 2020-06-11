<template >
  <a-list :grid="{ gutter: 16, column: 4 }" :data-source="imgsrc" >
    <a-list-item slot="renderItem" slot-scope="item, index" >
      <div class="img_div">
        <img  :id="'img'+index" class="item" :src="item.url" preview/>

          <div class="mask" v-if="isApply">
            <div class="icon1">
              <a-tooltip title="预览文件"  class="icon1" placement="bottomLeft" >
                <a-icon  type="eye"  @click="previewImg(index)" style="fontSize:25px" />
              </a-tooltip>
            </div>
            <div class="icon2" >
              <a-popconfirm title="是否确定删除？"  @confirm="deleteFile(index)" >
                <a-tooltip title="删除文件" placement="bottomLeft" >
                  <a-icon  class="icon2"  type="delete" style="fontSize:25px" />
                </a-tooltip>
              </a-popconfirm>
            </div>

          </div>

      </div>
    </a-list-item>
    <a-list-item   v-if="isApply">
    <a-upload
      action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
      list-type="picture-card"
      :file-list="[]"
      :before-upload="beforeUpload"
      :disabled="disable"
    >
        <a-icon type="plus" />
        <div class="ant-upload-text">
          Upload
        </div>

    </a-upload>
      <a-button
        type="primary"
        :disabled="(deleteImgs.length === 0&&uploadImgs.length === 0)||disable"
        :loading="uploading"
        style="margin-top: 16px"
        @click="handleUpload"
      >
        {{ disable?  "正在申报中，耐心等待审核" :uploading ? '正在申报' : '点击申报' }}
      </a-button>
      <span>

      </span>
    </a-list-item>
  </a-list>

</template>
<script>
  import {getFileAccessHttpUrl,uploadAction} from '@/api/manage';
  import Icons from "../../system/modules/icon/Icons";
  import {qualificationApply,queryQualification} from "../requestAction/request"

  export default {
    name:"PicList",
    components:{Icons},
    props:{
      images:{
        type:Array,
        default(){
          return [];
        }
      },
      isApply:true,
      qualificttionType:'',
      companyId:''

    },
    data() {
      return {
        //列表展示 图片
        imgsrc:[],
        //删除图片
        deleteImgs:[],
        //上传图片
        uploadImgs:[],
        uploading:false,
        disable:false

      }
    },
    methods: {
      getPopupContainer(trigger) {
        return trigger.parentElement;
      },
      previewImg(index){
        document.getElementById("img"+index).click();
      },
      deleteFile(index){

        //是不是提交列表中的
        let a = this.uploadImgs.findIndex(e=>
          e.url===this.imgsrc[index].url
        );
        if(a>-1){
          //删除提交列表中的对象
          this.uploadImgs.splice(a,1);
        }else{
          //添加到删除对象中
          this.deleteImgs.push(this.imgsrc[index].id);
        }
        //删除列表中的元素
        this.imgsrc.splice(index,1);
        console.log(this.imgsrc);
        console.log(this.uploadImgs);
        console.log(this.deleteImgs);
      },
      beforeUpload(file) {
        console.log(file);
        //在本地预览
        const windowURL = window.URL || window.webkitURL;
        const dataURl = windowURL.createObjectURL(file);
        console.log(dataURl)
        this.imgsrc.push({id:0,url:dataURl});
        //添加到提交数组中
        this.uploadImgs.push({url:dataURl,value:file});
        //不提交文件
        return false;
      },
      //提交按钮
      async handleUpload(){
        this.uploading = true;
        let addImgs = [];
        //需要提交的文件大于0
        if(this.uploadImgs.length>0){
          let formData = new FormData();
          this.uploadImgs.forEach(e=>{
            formData.append('file',e.value);
          });

          formData.append('biz', "jeditor");
          // formData.append("jeditor","1");

          await uploadAction(window._CONFIG['domianURL']+"/sys/common/uploadImages", formData).then((res) => {
            this.uploading = false;
            if (res.success) {
              this.$message.success("文件上传成功");
              //返回存储文件路径
              addImgs = res.result;
            }
            else{
              this.$message.error(res.message);
            }
          });
        }
        //发送申报请求
        await qualificationApply(
          {companyId:this.companyId,
                  qualificttionType:this.qualificttionType,
                  addImgs:addImgs,
                  deleteImgs:this.deleteImgs})
          .then((res)=>{
            this.uploading = false;
            if (res.success) {
              this.$message.success("申请完成");
            }
            else{
              this.$message.error(res.message);
            }
        });
        //提交完成处理
        this.disable = true;
      }
    },created(){
      let that =this;
      this.images.forEach(
        element => {

          that.imgsrc.push({id:element.id,url:getFileAccessHttpUrl(element.url)});
        }
      );

          //查询申报
          queryQualification({
            companyId: this.companyId,
            qualificttionType: this.qualificttionType
          }).then((res) => {

            if (res.success) {
              that.disable = res.result;
            }
          });

    }



  }
</script>
<style>

  /* item 用来放置全部的子元素 */
  .item {
    margin-top: 30px;
    width: 25vh;
    height: 20vh;
    background: green;
    object-fit:cover;
  }
  .img_div {
    position: relative;

  }
  .mask {
    position: absolute;
    margin-top: 30px;
    width: 25vh;
    height: 20vh;
    background: white;
    top: 0;
    left: 0;
    background: rgba(101, 101, 101, 0.6);
    color: #ffffff;
    opacity: 0;
  }
  .icon2 {
    position: absolute;
    top: 40%;
    left: 50%;
    height: 50%;
    width: 50%;
    margin: -5% 0 0 -15%;
  }
  .icon1 {
    position: absolute;
    top: 40%;
    left: 25%;
    height: 50%;
    width: 50%;
    margin: -5% 0 0 -15%;
  }
  .img_div:hover .mask {
    opacity: 1;
  }
</style>