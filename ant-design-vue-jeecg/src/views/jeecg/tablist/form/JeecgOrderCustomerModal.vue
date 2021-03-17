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
    <a-spin :spinning="confirmLoading" v-if="editStatus">
      <a-form-model ref="form" :model="model" :rules="validatorRules">
        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="客户姓名"
          prop="name"
          required
          hasFeedback>
          <a-input placeholder="请输入客户姓名" v-model="model.name" :readOnly="disableSubmit"/>
        </a-form-model-item>
        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="性别"
          hasFeedback>
          <a-select v-model="model.sex" placeholder="请选择性别">
            <a-select-option value="1">男性</a-select-option>
            <a-select-option value="2">女性</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="身份证号码"
          prop="idcard"
          hasFeedback>
          <a-input placeholder="请输入身份证号码" v-model="model.idcard" :readOnly="disableSubmit"/>
        </a-form-model-item>
        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="身份证扫描件"
          hasFeedback>
          <j-image-upload text="上传" v-model="fileList" :isMultiple="true"></j-image-upload>
        </a-form-model-item>
        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="联系方式"
          prop="telphone"
          hasFeedback>
          <a-input v-model="model.telphone" :readOnly="disableSubmit"/>
        </a-form-model-item>
        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="订单号码"
          :hidden="hiding"
          hasFeedback>
          <a-input v-model="model.orderId" disabled="disabled"/>
        </a-form-model-item>
      </a-form-model>
    </a-spin>
  </a-modal>
</template>

<script>
  import {httpAction} from '@/api/manage'
  import Vue from 'vue'
  import {ACCESS_TOKEN} from "@/store/mutation-types"
  import JImageUpload from '../../../../components/jeecg/JImageUpload'

  export default {
    name: "JeecgOrderCustomerModal",
    components: { JImageUpload },
    data() {
      return {
        title: "操作",
        visible: false,
        model: {},
        labelCol: {
          xs: {span: 24},
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        fileList: [],
        disableSubmit: false,
        selectedRowKeys: [],
        orderId: "",
        hiding: false,
        headers: {},
        picUrl: "",
        picArray:[],
        previewVisible: false,
        previewImage: '',
        addStatus: false,
        editStatus: false,
        confirmLoading: false,
        url: {
          add: "/test/order/addCustomer",
          edit: "/test/order/editCustomer",
          fileUpload: window._CONFIG['domianURL'] + "/sys/common/upload",
          getOrderCustomerList: "/test/order/listOrderCustomerByMainId",
        },
        validatorRules: {
          name :[{required: true, message: '请输入客户姓名!'}],
          telphone: [{validator: this.validateMobile}],
          idcard: [{validator: this.validateIdCard}]
        },
      }
    },
    computed: {
      uploadAction: function () {
        return this.url.fileUpload;
      }
    },
    created() {
      const token = Vue.ls.get(ACCESS_TOKEN);
      this.headers = {"X-Access-Token": token}
    },
    methods: {
      add(orderId) {
        this.hiding = true;
        if (orderId) {
          this.edit({orderId}, '');
        } else {
          this.$message.warning("请选择一个客户信息");
        }
      },
      detail(record) {
        this.edit(record, 'd');
      },
      edit(record, v) {
        if (v == 'e') {
          this.hiding = false;
          this.disableSubmit = false;
        } else if (v == 'd') {
          this.hiding = false;
          this.disableSubmit = true;
        } else {
          this.hiding = true;
          this.disableSubmit = false;
        }
        
        this.model = Object.assign({}, record);
        if (record.id) {
          this.hiding = false;
          this.addStatus = false;
          this.editStatus = true;
          setTimeout(() => {
            this.fileList = record.idcardPic
          }, 5)
        } else {
          this.addStatus = false;
          this.editStatus = true;
        }
        this.visible = true;
      },
      close() {
        this.$emit('close');
        this.visible = false;
        this.picUrl = "";
        this.fileList=[];
        this.$refs.form.resetFields();
      },
      handleOk() {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if (!this.model.id) {
              httpurl += this.url.add;
              method = 'post';
            } else {
              httpurl += this.url.edit;
              method = 'put';
            }
            let formData = Object.assign({}, this.model);
            if(this.fileList != '') {
              formData.idcardPic = this.fileList;
            }else{
              formData.idcardPic = '';
            }
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit('ok');
              } else {
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }else{
            return false;
          }
        })
      },
      handleCancel() {
        this.close();
      },
      validateMobile(rule, value, callback) {
        if (!value || new RegExp(/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/).test(value)) {
          callback();
        } else {
          callback("您的手机号码格式不正确!");
        }
      },
      validateIdCard(rule, value, callback) {
        if (!value || new RegExp(/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/).test(value)) {
          callback();
        } else {
          callback("您的身份证号码格式不正确!");
        }
      },
    }
  }
</script>

<style scoped>
  /* tile uploaded pictures */
  .upload-list-inline > > > .ant-upload-list-item {
    float: left;
    width: 200px;
    margin-right: 8px;
  }

  .upload-list-inline > > > .ant-upload-animate-enter {
    animation-name: uploadAnimateInlineIn;
  }

  .upload-list-inline > > > .ant-upload-animate-leave {
    animation-name: uploadAnimateInlineOut;
  }
</style>