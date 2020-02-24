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
        <a-form-item label="供应商 ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'vendorId', validatorRules.vendorId]" placeholder="请输入供应商 ID" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="供应商编号（主数据编号）" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'mdCode', validatorRules.mdCode]" placeholder="请输入供应商编号（主数据编号）"></a-input>
        </a-form-item>
        <a-form-item label="联系人 ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'vendorContactsId', validatorRules.vendorContactsId]" placeholder="请输入联系人 ID" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="联系人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'contactName', validatorRules.contactName]" placeholder="请输入联系人姓名"></a-input>
        </a-form-item>
        <a-form-item label="手机号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'contactCellphone', validatorRules.contactCellphone]" placeholder="请输入手机号码"></a-input>
        </a-form-item>
        <a-form-item label="电话号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'contactPhone', validatorRules.contactPhone]" placeholder="请输入电话号码"></a-input>
        </a-form-item>
        <a-form-item label="传真" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'contactFax', validatorRules.contactFax]" placeholder="请输入传真"></a-input>
        </a-form-item>
        <a-form-item label="电子邮箱" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'contactMail', validatorRules.contactMail]" placeholder="请输入电子邮箱"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'

  export default {
    name: "MdmVendorContactsInfoModal",
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
        vendorCompanyId:{},
        vendorId:{rules: [{ required: true, message: '请输入供应商 ID!' }]},
        mdCode:{},
        vendorContactsId:{},
        contactName:{},
        contactCellphone:{},
        contactPhone:{},
        contactFax:{},
        contactMail:{},
        },
        url: {
          add: "/base/mdmVendorInfo/addMdmVendorContactsInfo",
          edit: "/base/mdmVendorInfo/editMdmVendorContactsInfo",
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
          this.form.setFieldsValue(pick(this.model,'vendorCompanyId','vendorId','mdCode','vendorContactsId','contactName','contactCellphone','contactPhone','contactFax','contactMail','createTime','createBy','updateTime','updateBy'))
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
        this.form.setFieldsValue(pick(row,'vendorCompanyId','vendorId','mdCode','vendorContactsId','contactName','contactCellphone','contactPhone','contactFax','contactMail','createTime','createBy','updateTime','updateBy'))
      },


    }
  }
</script>
