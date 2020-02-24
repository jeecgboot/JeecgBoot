<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="供应商公司 ID（地市公司组织 ID）" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'vendorCompanyId', validatorRules.vendorCompanyId]" placeholder="请输入供应商公司 ID（地市公司组织 ID）" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="供应商银行账户 ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'vendorAccountId', validatorRules.vendorAccountId]" placeholder="请输入供应商银行账户 ID" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="供应商 ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'vendorId', validatorRules.vendorId]" placeholder="请输入供应商 ID" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="供应商编号（主数据编号）" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'mdCode', validatorRules.mdCode]" placeholder="请输入供应商编号（主数据编号）"></a-input>
        </a-form-item>
        <a-form-item label="是否主账号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'mainAccountFlag', validatorRules.mainAccountFlag]" placeholder="请输入是否主账号"></a-input>
        </a-form-item>
        <a-form-item label="银行名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bankName', validatorRules.bankName]" placeholder="请输入银行名称"></a-input>
        </a-form-item>
        <a-form-item label="银行编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bankCode', validatorRules.bankCode]" placeholder="请输入银行编码"></a-input>
        </a-form-item>
        <a-form-item label="支行名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'branchBank', validatorRules.branchBank]" placeholder="请输入支行名称"></a-input>
        </a-form-item>
        <a-form-item label="支行编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'branchCode', validatorRules.branchCode]" placeholder="请输入支行编码"></a-input>
        </a-form-item>
        <a-form-item label="联行号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'cnapNumber', validatorRules.cnapNumber]" placeholder="请输入联行号"></a-input>
        </a-form-item>
        <a-form-item label="银行户名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'accountName', validatorRules.accountName]" placeholder="请输入银行户名"></a-input>
        </a-form-item>
        <a-form-item label="银行账号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'accountNumber', validatorRules.accountNumber]" placeholder="请输入银行账号"></a-input>
        </a-form-item>
        <a-form-item label="账户币种" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'accountCurrency', validatorRules.accountCurrency]" placeholder="请输入账户币种"></a-input>
        </a-form-item>
        <a-form-item label="银行账户是否有效" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'validFlag', validatorRules.validFlag]" placeholder="请输入银行账户是否有效"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'

  export default {
    name: "MdmVendorAccountInfoModal",
    components: {
    },
    props:{
      mainId:{
        type:String,
        required:false,
        default:''
      }
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
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

        confirmLoading: false,
        validatorRules:{
        vendorCompanyId:{rules: [{ required: true, message: '请输入供应商公司 ID（地市公司组织 ID）!' }]},
        vendorAccountId:{},
        vendorId:{rules: [{ required: true, message: '请输入供应商 ID!' }]},
        mdCode:{},
        mainAccountFlag:{},
        bankName:{},
        bankCode:{},
        branchBank:{},
        branchCode:{},
        cnapNumber:{rules: [{ required: true, message: '请输入联行号!' }]},
        accountName:{rules: [{ required: true, message: '请输入银行户名!' }]},
        accountNumber:{rules: [{ required: true, message: '请输入银行账号!' }]},
        accountCurrency:{},
        validFlag:{},
        },
        url: {
          add: "/base/mdmVendorInfo/addMdmVendorAccountInfo",
          edit: "/base/mdmVendorInfo/editMdmVendorAccountInfo",
        }

      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'vendorCompanyId','vendorAccountId','vendorId','mdCode','mainAccountFlag','bankName','bankCode','branchBank','branchCode','cnapNumber','accountName','accountNumber','accountCurrency','validFlag','createTime','createBy','updateTime','updateBy'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            formData['vendorId'] = this.mainId
            console.log("表单提交数据",formData)
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

        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'vendorCompanyId','vendorAccountId','vendorId','mdCode','mainAccountFlag','bankName','bankCode','branchBank','branchCode','cnapNumber','accountName','accountNumber','accountCurrency','validFlag','createTime','createBy','updateTime','updateBy'))
      },


    }
  }
</script>
