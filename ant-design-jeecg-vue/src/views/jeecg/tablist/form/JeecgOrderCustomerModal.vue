<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :okButtonProps="{ props: {disabled: disableSubmit} }"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">

    <!-- 编辑 -->
    <a-spin :spinning="confirmLoading"  v-if="editStatus">
      <a-form :form="form">

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="客户姓名"
          hasFeedback >
          <a-input placeholder="请输入客户姓名" v-decorator="['name', {rules: [{ required: true, message: '请输入客户姓名!' }]}]" :readOnly="disableSubmit" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="性别"
          hasFeedback >
          <a-select v-decorator="['sex', {}]" placeholder="请选择性别">
            <a-select-option value="1">男性</a-select-option>
            <a-select-option value="2">女性</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="身份证号码"
          hasFeedback >
          <a-input placeholder="请输入身份证号码" v-decorator="['idcard', validatorRules.idcard]" :readOnly="disableSubmit" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="身份证扫描件"
          hasFeedback >
          <a-upload
            :action="uploadAction"
            listType="picture-card"
            :headers="headers"
            :fileList="fileList"
            @change="handleChange"
            @preview="handlePreview"
          >
            <a-button>
              <a-icon type="upload" /> upload
            </a-button>
          </a-upload>
          <img v-if="model.idcardPic" :src="getIdCardView()" alt="头像" style="height:104px;max-width:300px"/>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handlePicCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
          <br />
      </a-form-item>
      <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="联系方式"
          hasFeedback >
          <a-input v-decorator="[ 'telphone', validatorRules.telphone]" :readOnly="disableSubmit" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="订单号码"
          :hidden="hiding"
          hasFeedback >
          <a-input v-decorator="[ 'orderId', {}]" disabled="disabled" />
        </a-form-item>
      </a-form>
    </a-spin>

    <!-- 新增 -->
    <a-table
      v-if="addStatus"
      ref="table"
      bordered
      rowKey="id"
      :columns="columns"
      :dataSource="dataSource"
      :pagination="ipagination"
      :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
      @change="handleTableChange"
    >
    </a-table>

  </a-modal>
</template>

<script>
  import { httpAction,getAction,postAction} from '@/api/manage'
  import { doMian } from '@/api/api'
  import pick from 'lodash.pick'
  import Vue from 'vue'
  import { ACCESS_TOKEN } from "@/store/mutation-types"

  export default {
    name: "JeecgOrderCustomerModal",
    // 查询条件
    data () {
      return {
        title:"操作",
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        // 表头
        columns: [
          {
            title: '客户名',
            align:"center",
            dataIndex: 'name',
          },
          {
            title: '性别',
            align:"center",
            dataIndex: 'sex',
          },
          {
            title: '身份证号码',
            align:"center",
            dataIndex: 'idcard',
          },
          {
            title: '身份证扫描件',
            align:"center",
            dataIndex: 'idcardPic',
          },
          {
            title: '电话',
            dataIndex: 'telphone',
            align:"center",
          },
          {
            title: '订单号码',
            dataIndex: 'orderId',
            align:"center",
          },
          {
            title: '创建人',
            dataIndex: 'createBy',
            align:"center",
          },
          {
            title: '创建时间',
            dataIndex: 'createTime',
            align:"center",
          },
          {
            title: '更新时间',
            dataIndex: 'updateBy',
            align:"center",
          },
          {
            title: '更新人',
            dataIndex: 'updateTime',
            align:"center",
          },
        ],
        dataSource:[],
        // 分页参数
        ipagination:{
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        fileList:[],
        disableSubmit:false,
        selectedRowKeys: [],
        orderId:"",
        hiding:false,
        headers:{},
        picUrl:"",
        previewVisible: false,
        previewImage: '',
        addStatus:false,
        editStatus:false,
        confirmLoading: false,
        form: this.$form.createForm(this),
        url: {
          fileUpload:doMian+"sys/common/upload",
          add: "/test/order/addCustomer",
          edit: "/test/order/editCustomer",
          imgerver:doMian+"sys/common/view",
          getOrderCustomerList: "/test/order/queryOrderCustomerListByMainId",
        },
        validatorRules:{
          telphone:{rules: [{ validator:this.validateMobile}]},
          idcard:{rules:[{ validator:this.validateIdCard}]}
        },
      }
    },
    computed:{
      uploadAction:function () {
        return this.url.fileUpload;
      }
    },
    created () {
      const token = Vue.ls.get(ACCESS_TOKEN);
      this.headers = {"X-Access-Token":token}
    },
    methods: {
      add (orderId) {
        this.hiding = true;
        if(orderId){
          this.orderId = orderId;
          this.edit({},'');
        }else{
          this.$message.warning("请选择一个客户信息");
        }
      },
      detail(record){
        this.edit(record,'d');
      },
      edit (record,v) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        if(record.id){
          this.hiding = false;
          if(v == 'e'){
            this.disableSubmit = false;
          }else {
            this.disableSubmit = true;
          }
          this.addStatus = false;
          this.editStatus = true;
          this.$nextTick(() => {
            this.form.setFieldsValue(pick(this.model,'id','name','sex','idcard','idcardPic','telphone','orderId','createBy','createTime','updateBy','updateTime'))
          });
        }else{
          this.addStatus = false;
          this.editStatus = true;
        }
        // 加载客户信息
        let httpurl = this.url.getOrderCustomerList;
        getAction(httpurl,{id:this.orderId}).then((res)=>{
          if(res.success){
            this.dataSource = res.result;
          }else{
            this.$message.warning(res.message);
          }
        });
        this.visible = true;
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      onSelectChange (selectedRowKeys,selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        console.log(selectionRows);
      },
      handleOk () {
        const that = this;
        this.form.validateFields((err, values) => {
          if (!err) {
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              let formData = Object.assign(this.model, values);
              formData.orderId = this.orderId;
              formData.idcardPic = this.picUrl;
              postAction(httpurl,formData).then((res)=>{
                if(res.success){
                  that.$message.success(res.message);
                  that.$emit('ok');
                  this.visible = false;
                }else{
                  this.$message.warning(res.message);
                }
              });
            }else{
              httpurl+=this.url.edit;
              method = 'put';
              let formData = Object.assign(this.model, values);
              //时间格式化
              httpAction(httpurl,formData,method).then((res)=>{
                if(res.success){
                  that.$message.success(res.message);
                  that.$emit('ok');
                }else{
                  that.$message.warning(res.message);
                }
              }).finally(() => {
                that.confirmLoading = false;
                that.close();
              })
            }
          }
        })
      },

      handleTableChange(pagination, filters, sorter){
        //TODO 筛选
        if (Object.keys(sorter).length>0){
          this.isorter.column = sorter.field;
          this.isorter.order = "ascend"==sorter.order?"asc":"desc"
        }
        this.ipagination = pagination;
        this.loadData();
      },
      handleCancel () {
        this.close()
      },
      validateMobile(rule,value,callback){
        if (!value || new RegExp(/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/).test(value)){
          callback();
        }else{
          callback("您的手机号码格式不正确!");
        }
      },
      validateIdCard(rule,value,callback){
        if (!value || new RegExp(/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/).test(value)){
          callback();
        }else{
          callback("您的身份证号码格式不正确!");
        }
      },
      handleChange (info) {
        this.fileList = info.fileList;
        if (info.file.status === 'uploading') {
          return
        }
        if (info.file.status === 'done') {
          var response = info.file.response;
          console.log(response);
          if(response.success){
            this.picUrl += response.message + ",";
          }else{
            this.$message.warning(response.message);
          }
        }
      },
      handlePicCancel () {
        this.previewVisible = false
      },
      handlePreview (file) {
        this.previewImage = file.url || file.thumbUrl
        this.previewVisible = true
      },
      getIdCardView(){
        let pics = this.model.idcardPic.split(",");
          return this.url.imgerver +"/"+ pics[0];
      }
  }
  }
</script>

<style scoped>
  /* tile uploaded pictures */
  .upload-list-inline >>> .ant-upload-list-item {
    float: left;
    width: 200px;
    margin-right: 8px;
  }
  .upload-list-inline >>> .ant-upload-animate-enter {
    animation-name: uploadAnimateInlineIn;
  }
  .upload-list-inline >>> .ant-upload-animate-leave {
    animation-name: uploadAnimateInlineOut;
  }
</style>