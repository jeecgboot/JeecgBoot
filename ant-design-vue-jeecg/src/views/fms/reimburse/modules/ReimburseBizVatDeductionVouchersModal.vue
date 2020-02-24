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

        <a-form-item label="报销单编号" :labelCol="labelCol" :wrapperCol="wrapperCol" >
          <a-input v-decorator="[ 'applyNo']" v-bind:read-only="true" class="inputReadOnly"></a-input>
        </a-form-item>
        <a-form-item label="序号" :labelCol="labelCol" :wrapperCol="wrapperCol" v-if="false">
          <a-input v-decorator="[ 'seq']" ></a-input>
        </a-form-item>
        <a-form-item label="增值税扣税凭证类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['voucherType', validatorRules.voucherType]" :trigger-change="true" dictCode="fms_vat_inv_type" placeholder="请输入请输入增值税扣税凭证类型"/>
        </a-form-item>
        <a-form-item label="是否红字发票" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['redInvoiceFlag', validatorRules.redInvoiceFlag]" :trigger-change="true" dictCode="fms_comm_yn" placeholder="请输入是否红字发票" @change="handleRedInv"/>
        </a-form-item>
        <a-form-item label="发票号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'docNum', validatorRules.docNum]" placeholder="请输入发票号码"></a-input>
        </a-form-item>
        <a-form-item label="发票代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'invoiceCode', validatorRules.invoiceCode]" placeholder="请输入发票代码"></a-input>
        </a-form-item>
        <a-form-item label="开票日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择开票日期" v-decorator="[ 'docDate', validatorRules.docDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="是否需抵扣" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['offsetFlag', validatorRules.offsetFlag]" :trigger-change="true" dictCode="fms_comm_yn" placeholder="请输入请输入是否需抵扣"/>
        </a-form-item>
        <div v-if="redInvoiceFlag=='Y'">
        <a-form-item label="原发票号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'docNumOriginal', validatorRules.docNumOriginal]" placeholder="请输入原发票号码"></a-input>
        </a-form-item>
        <a-form-item label="原发票代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'invoiceCodeOriginal', validatorRules.invoiceCodeOriginal]" placeholder="请输入原发票代码"></a-input>
        </a-form-item>
        <a-form-item label="发票价额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'invoiceAmount', validatorRules.invoiceAmount]" placeholder="发票价额" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="税额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'invoiceTaxAmount', validatorRules.invoiceTaxAmount]" placeholder="请输入税额" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="税率" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['invoiceTaxRate', {}]" :trigger-change="true" dictCode="fms_vat_rate" placeholder="请选择税率"/>
        </a-form-item>
        <a-form-item label="价税合计" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'invoiceTotalAmount', validatorRules.invoiceTotalAmount]" placeholder="请输入价税合计" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="红字发票通知单编号" :labelCol="labelCol" :wrapperCol="wrapperCol" >
          <a-input v-decorator="[ 'redInvoiceNum', validatorRules.redInvoiceNum]" placeholder="请输入红字发票通知单编号"></a-input>
        </a-form-item>
        <a-form-item label="红字发票通知单开具日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择红字发票通知单开具日期" v-decorator="[ 'redInvoiceDate', validatorRules.redInvoiceDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="红字发票描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="[ 'redInvoiceDescription', validatorRules.redInvoiceDescription]" placeholder="请输入红字发票描述"></a-textarea>
        </a-form-item>
        </div>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-textarea>
        </a-form-item>


      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDate from '@/components/jeecg/JDate'
  import ATextarea from "ant-design-vue/es/input/TextArea";

  export default {
    name: "ReimburseBizVatDeductionVouchersModal",
    components: {
      ATextarea,
      JDate,
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
        redInvoiceFlag:'N',
        confirmLoading: false,
        validatorRules:{
        applyNo:{rules: [{ required: true, message: '请输入报销单编号!' }]},
        docNum:{rules:[{ required: true, message: '请输入发票号码!' }]},
        invoiceCode:{rules:[{ required: true, message: '请输入发票代码!' }]},
        docDate:{rules: [{ required: true, message: '请输入开票日期!' }]},
        offsetFlag:{rules: [{ required: true, message: '请输入是否需抵扣!' }]},
        docNumOriginal:{},
        invoiceCodeOriginal:{},
        voucherType:{},
        voucherTypeName:{},
        invoiceAmount:{},
        invoiceTaxAmount:{},
        invoiceTaxRate:{},
        invoiceTotalAmount:{},
        buyTaxIdentiNum:{},
        buyTaxIdentiName:{},
        sellerTaxIdentiNum:{},
        sellerTaxIdentiName:{},
        invoiceResult:{},
        invoiceResultName:{},
        claimResult:{},
        authenticationDate:{},
        signDate:{},
        voucherReturnFlag:{},
        processDate:{},
        redInvoiceFlag:{rules: [{ required: true, message: '请输入是否为红字发票!' }]},
        redInvoiceNum:{},
        redInvoiceDate:{},
        isSum:{},
        remarks:{},
        redInvoiceDescription:{},
        vendorNum:{},
        vendorName:{},
        projectAttr:{},
        transferTaxFlag:{},
        inputTaxTurnDate:{},
        isSumBdk:{},
        },
        url: {
          add: "/biz/reimburseBizMainInfo/addReimburseBizVatDeductionVouchers",
          edit: "/biz/reimburseBizMainInfo/editReimburseBizVatDeductionVouchers",
        }

      }
    },
    created () {

    },
    methods: {
      add () {
        this.edit({});
        setTimeout(()=>{
          this.form.setFieldsValue({'applyNo':this.mainId})
        },0);
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'applyNo','seq','docNum','invoiceCode','docDate','offsetFlag','docNumOriginal','invoiceCodeOriginal','voucherType','voucherTypeName','invoiceAmount','invoiceTaxAmount','invoiceTaxRate','invoiceTotalAmount','buyTaxIdentiNum','buyTaxIdentiName','sellerTaxIdentiNum','sellerTaxIdentiName','invoiceResult','invoiceResultName','claimResult','authenticationDate','signDate','voucherReturnFlag','processDate','redInvoiceFlag','redInvoiceNum','redInvoiceDate','isSum','remarks','redInvoiceDescription','vendorNum','vendorName','projectAttr','transferTaxFlag','inputTaxTurnDate','isSumBdk','createTime','createBy','updateTime','updateBy'))
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
            formData['applyNo'] = this.mainId
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
        this.form.setFieldsValue(pick(row,'applyNo','seq','docNum','invoiceCode','docDate','offsetFlag','docNumOriginal','invoiceCodeOriginal','voucherType','voucherTypeName','invoiceAmount','invoiceTaxAmount','invoiceTaxRate','invoiceTotalAmount','buyTaxIdentiNum','buyTaxIdentiName','sellerTaxIdentiNum','sellerTaxIdentiName','invoiceResult','invoiceResultName','claimResult','authenticationDate','signDate','voucherReturnFlag','processDate','redInvoiceFlag','redInvoiceNum','redInvoiceDate','isSum','remarks','redInvoiceDescription','vendorNum','vendorName','projectAttr','transferTaxFlag','inputTaxTurnDate','isSumBdk','createTime','createBy','updateTime','updateBy'))
      },
      handleRedInv(value) {
        this.redInvoiceFlag=value;
        if(value==='Y'){
          this.form.setFieldsValue({"offsetFlag":'N'});
        }else{
          this.form.setFieldsValue({"offsetFlag":'Y'});
        }
      },

    }
  }
</script>
<style lang="less" scoped>
  .inputReadOnly{
    background-color:#fafafa !important;
  }
