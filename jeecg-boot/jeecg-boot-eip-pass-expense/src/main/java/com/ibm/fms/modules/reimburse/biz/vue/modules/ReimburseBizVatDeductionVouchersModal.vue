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

        <a-form-item label="报销单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'applyNo', validatorRules.applyNo]" placeholder="请输入报销单编号"></a-input>
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
        <a-form-item label="是否需抵扣：Y-是/N-否，指发票本身是否需要进行抵扣" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'offsetFlag', validatorRules.offsetFlag]" placeholder="请输入是否需抵扣：Y-是/N-否，指发票本身是否需要进行抵扣"></a-input>
        </a-form-item>
        <a-form-item label="原发票号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'docNumOriginal', validatorRules.docNumOriginal]" placeholder="请输入原发票号码"></a-input>
        </a-form-item>
        <a-form-item label="原发票代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'invoiceCodeOriginal', validatorRules.invoiceCodeOriginal]" placeholder="请输入原发票代码"></a-input>
        </a-form-item>
        <a-form-item label="增值税扣税凭证类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'voucherType', validatorRules.voucherType]" placeholder="请输入增值税扣税凭证类型"></a-input>
        </a-form-item>
        <a-form-item label="增值税扣税凭证类型名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'voucherTypeName', validatorRules.voucherTypeName]" placeholder="请输入增值税扣税凭证类型名称"></a-input>
        </a-form-item>
        <a-form-item label="金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'invoiceAmount', validatorRules.invoiceAmount]" placeholder="请输入金额" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="税额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'invoiceTaxAmount', validatorRules.invoiceTaxAmount]" placeholder="请输入税额" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="税率" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'invoiceTaxRate', validatorRules.invoiceTaxRate]" placeholder="请输入税率"></a-input>
        </a-form-item>
        <a-form-item label="价税合计" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'invoiceTotalAmount', validatorRules.invoiceTotalAmount]" placeholder="请输入价税合计" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="购方纳税人识别号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'buyTaxIdentiNum', validatorRules.buyTaxIdentiNum]" placeholder="请输入购方纳税人识别号"></a-input>
        </a-form-item>
        <a-form-item label="购方纳税人名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'buyTaxIdentiName', validatorRules.buyTaxIdentiName]" placeholder="请输入购方纳税人名称"></a-input>
        </a-form-item>
        <a-form-item label="销方纳税人识别号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sellerTaxIdentiNum', validatorRules.sellerTaxIdentiNum]" placeholder="请输入销方纳税人识别号"></a-input>
        </a-form-item>
        <a-form-item label="销方纳税人名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sellerTaxIdentiName', validatorRules.sellerTaxIdentiName]" placeholder="请输入销方纳税人名称"></a-input>
        </a-form-item>
        <a-form-item label="发票认证结果:1：认证通过；2：认证不通过；3：未认证; 4：无需认证" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'invoiceResult', validatorRules.invoiceResult]" placeholder="请输入发票认证结果:1：认证通过；2：认证不通过；3：未认证; 4：无需认证"></a-input>
        </a-form-item>
        <a-form-item label="发票认证结果名称：1：认证通过；2：认证不通过；3：未认证; 4：无需认证" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'invoiceResultName', validatorRules.invoiceResultName]" placeholder="请输入发票认证结果名称：1：认证通过；2：认证不通过；3：未认证; 4：无需认证"></a-input>
        </a-form-item>
        <a-form-item label="报账单认证结果:1.都通过；2：部分通过；3：都未通过；4：认证异常" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'claimResult', validatorRules.claimResult]" placeholder="请输入报账单认证结果:1.都通过；2：部分通过；3：都未通过；4：认证异常"></a-input>
        </a-form-item>
        <a-form-item label="认证日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择认证日期" v-decorator="[ 'authenticationDate', validatorRules.authenticationDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="发票签收时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择发票签收时间" v-decorator="[ 'signDate', validatorRules.signDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="凭证是否退回" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'voucherReturnFlag', validatorRules.voucherReturnFlag]" placeholder="请输入凭证是否退回"></a-input>
        </a-form-item>
        <a-form-item label="报账单处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择报账单处理时间" v-decorator="[ 'processDate', validatorRules.processDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="是否红字发票：N- 否，Y- 是" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'redInvoiceFlag', validatorRules.redInvoiceFlag]" placeholder="请输入是否红字发票：N- 否，Y- 是"></a-input>
        </a-form-item>
        <a-form-item label="红字发票通知单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'redInvoiceNum', validatorRules.redInvoiceNum]" placeholder="请输入红字发票通知单编号"></a-input>
        </a-form-item>
        <a-form-item label="红字发票通知单开具日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择红字发票通知单开具日期" v-decorator="[ 'redInvoiceDate', validatorRules.redInvoiceDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="是否汇总" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'isSum', validatorRules.isSum]" placeholder="请输入是否汇总"></a-input>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-input>
        </a-form-item>
        <a-form-item label="红字发票描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'redInvoiceDescription', validatorRules.redInvoiceDescription]" placeholder="请输入红字发票描述"></a-input>
        </a-form-item>
        <a-form-item label="供应商编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'vendorNum', validatorRules.vendorNum]" placeholder="请输入供应商编号"></a-input>
        </a-form-item>
        <a-form-item label="供应商名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'vendorName', validatorRules.vendorName]" placeholder="请输入供应商名称"></a-input>
        </a-form-item>
        <a-form-item label="项目属性：Y-动产/N-不动产" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'projectAttr', validatorRules.projectAttr]" placeholder="请输入项目属性：Y-动产/N-不动产"></a-input>
        </a-form-item>
        <a-form-item label="结转税额标识（0、动产项目或成本费用；1、不动产项目并且第一次结转；2、不动产项目并且第二次结转；3、动产转为不动产项目；4、不动产转为动产项目）" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'transferTaxFlag', validatorRules.transferTaxFlag]" placeholder="请输入结转税额标识（0、动产项目或成本费用；1、不动产项目并且第一次结转；2、不动产项目并且第二次结转；3、动产转为不动产项目；4、不动产转为动产项目）" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="进项税额转出会计期间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择进项税额转出会计期间" v-decorator="[ 'inputTaxTurnDate', validatorRules.inputTaxTurnDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="不抵扣进项税凭证是否汇总：Y-是，N-否" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'isSumBdk', validatorRules.isSumBdk]" placeholder="请输入不抵扣进项税凭证是否汇总：Y-是，N-否"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDate from '@/components/jeecg/JDate'

  export default {
    name: "ReimburseBizVatDeductionVouchersModal",
    components: {
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

        confirmLoading: false,
        validatorRules:{
        applyNo:{rules: [{ required: true, message: '请输入报销单编号!' }]},
        docNum:{},
        invoiceCode:{},
        docDate:{rules: [{ required: true, message: '请输入开票日期!' }]},
        offsetFlag:{},
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
        redInvoiceFlag:{},
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


    }
  }
</script>
