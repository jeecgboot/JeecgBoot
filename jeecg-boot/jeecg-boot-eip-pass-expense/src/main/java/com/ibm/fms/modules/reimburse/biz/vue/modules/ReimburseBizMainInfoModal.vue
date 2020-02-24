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

        <a-form-item label="报账单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'applyNo', validatorRules.applyNo]" placeholder="请输入报账单编号"></a-input>
        </a-form-item>
        <a-form-item label="员工姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'userName', validatorRules.userName]" placeholder="请输入员工姓名"></a-input>
        </a-form-item>
        <a-form-item label="员工所属公司名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'userCompanyName', validatorRules.userCompanyName]" placeholder="请输入员工所属公司名称"></a-input>
        </a-form-item>
        <a-form-item label="员工所属部门名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'userDepartName', validatorRules.userDepartName]" placeholder="请输入员工所属部门名称"></a-input>
        </a-form-item>
        <a-form-item label="员工职务" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'userPosition', validatorRules.userPosition]" placeholder="请输入员工职务"></a-input>
        </a-form-item>
        <a-form-item label="员工编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'userNo', validatorRules.userNo]" placeholder="请输入员工编号"></a-input>
        </a-form-item>
        <a-form-item label="员工邮箱" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'userEmail', validatorRules.userEmail]" placeholder="请输入员工邮箱"></a-input>
        </a-form-item>
        <a-form-item label="员工手机号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'userMobile', validatorRules.userMobile]" placeholder="请输入员工手机号"></a-input>
        </a-form-item>
        <a-form-item label="报销事由" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'reimbursementItem', validatorRules.reimbursementItem]" placeholder="请输入报销事由"></a-input>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'remark', validatorRules.remark]" placeholder="请输入备注"></a-input>
        </a-form-item>
        <a-form-item label="支出成本类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'costType', validatorRules.costType]" placeholder="请输入支出成本类型"></a-input>
        </a-form-item>
        <a-form-item label="币种" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'currency', validatorRules.currency]" placeholder="请输入币种"></a-input>
        </a-form-item>
        <a-form-item label="发票张数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'attachmentNum', validatorRules.attachmentNum]" placeholder="请输入发票张数" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="支付方式：转账（银企直联），结清（银行托收），网银支付" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'paymentType', validatorRules.paymentType]" placeholder="请输入支付方式：转账（银企直联），结清（银行托收），网银支付"></a-input>
        </a-form-item>
        <a-form-item label="供应商编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'vendorCode', validatorRules.vendorCode]" placeholder="请输入供应商编号"></a-input>
        </a-form-item>
        <a-form-item label="供应商名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'vendorName', validatorRules.vendorName]" placeholder="请输入供应商名称"></a-input>
        </a-form-item>
        <a-form-item label="供应商地点" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'vendorAddress', validatorRules.vendorAddress]" placeholder="请输入供应商地点"></a-input>
        </a-form-item>
        <a-form-item label="供应商纳税人类型: 一般纳税人/ 小规模纳税人/非增值税纳税人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'vendorVatFlag', validatorRules.vendorVatFlag]" placeholder="请输入供应商纳税人类型: 一般纳税人/ 小规模纳税人/非增值税纳税人"></a-input>
        </a-form-item>
        <a-form-item label="是否借款：N否，Y是" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'isLoan', validatorRules.isLoan]" placeholder="请输入是否借款：N否，Y是"></a-input>
        </a-form-item>
        <a-form-item label="是否冲减：N否，Y是" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'isStrike', validatorRules.isStrike]" placeholder="请输入是否冲减：N否，Y是"></a-input>
        </a-form-item>
        <a-form-item label="是否预付：N否，Y是" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'isPrepay', validatorRules.isPrepay]" placeholder="请输入是否预付：N否，Y是"></a-input>
        </a-form-item>
        <a-form-item label="是否有合同：N否，Y是" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'existContract', validatorRules.existContract]" placeholder="请输入是否有合同：N否，Y是"></a-input>
        </a-form-item>
        <a-form-item label="是否关联交易:N否，Y是" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'isConntrans', validatorRules.isConntrans]" placeholder="请输入是否关联交易:N否，Y是"></a-input>
        </a-form-item>
        <a-form-item label="关联交易类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'conntransType', validatorRules.conntransType]" placeholder="请输入关联交易类型"></a-input>
        </a-form-item>
        <a-form-item label="是否包含订单：N-否/Y-是" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'existOrder', validatorRules.existOrder]" placeholder="请输入是否包含订单：N-否/Y-是"></a-input>
        </a-form-item>
        <a-form-item label="是否不动产：Y-是/N-否" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'isImmovable', validatorRules.isImmovable]" placeholder="请输入是否不动产：Y-是/N-否"></a-input>
        </a-form-item>
        <a-form-item label="是否电子发票：Y-是/N-否" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'isEleInvoice', validatorRules.isEleInvoice]" placeholder="请输入是否电子发票：Y-是/N-否"></a-input>
        </a-form-item>
        <a-form-item label="是否包含进项抵扣凭证：Y-是/N-否" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'existOffsetFlag', validatorRules.existOffsetFlag]" placeholder="请输入是否包含进项抵扣凭证：Y-是/N-否"></a-input>
        </a-form-item>
        <a-form-item label="是否成本下分：Y-是/N-否" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'isPayDivide', validatorRules.isPayDivide]" placeholder="请输入是否成本下分：Y-是/N-否"></a-input>
        </a-form-item>
        <a-form-item label="是否包含视同销售：N-否/Y-是" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'isSales', validatorRules.isSales]" placeholder="请输入是否包含视同销售：N-否/Y-是"></a-input>
        </a-form-item>
        <a-form-item label="是否需要起草进项发票报账单：N-否，Y-是" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'isInputVatInvoice', validatorRules.isInputVatInvoice]" placeholder="请输入是否需要起草进项发票报账单：N-否，Y-是"></a-input>
        </a-form-item>
        <a-form-item label="是否已经预匹配订单：Y-是，N-否" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'isPreMatchPo', validatorRules.isPreMatchPo]" placeholder="请输入是否已经预匹配订单：Y-是，N-否"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'

  export default {
    name: "ReimburseBizMainInfoModal",
    components: { 
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
        applyNo:{rules: [{ required: true, message: '请输入报账单编号!' }]},
        userName:{rules: [{ required: true, message: '请输入员工姓名!' }]},
        userCompanyName:{rules: [{ required: true, message: '请输入员工所属公司名称!' }]},
        userDepartName:{rules: [{ required: true, message: '请输入员工所属部门名称!' }]},
        userPosition:{},
        userNo:{rules: [{ required: true, message: '请输入员工编号!' }]},
        userEmail:{rules: [{ required: true, message: '请输入员工邮箱!' }]},
        userMobile:{},
        reimbursementItem:{rules: [{ required: true, message: '请输入报销事由!' }]},
        remark:{},
        costType:{},
        currency:{},
        attachmentNum:{rules: [{ required: true, message: '请输入发票张数!' }]},
        paymentType:{},
        vendorCode:{},
        vendorName:{},
        vendorAddress:{},
        vendorVatFlag:{},
        isLoan:{rules: [{ required: true, message: '请输入是否借款：N否，Y是!' }]},
        isStrike:{rules: [{ required: true, message: '请输入是否冲减：N否，Y是!' }]},
        isPrepay:{rules: [{ required: true, message: '请输入是否预付：N否，Y是!' }]},
        existContract:{rules: [{ required: true, message: '请输入是否有合同：N否，Y是!' }]},
        isConntrans:{},
        conntransType:{},
        existOrder:{},
        isImmovable:{},
        isEleInvoice:{},
        existOffsetFlag:{},
        isPayDivide:{},
        isSales:{},
        isInputVatInvoice:{},
        isPreMatchPo:{},
        },
        url: {
          add: "/biz/reimburseBizMainInfo/add",
          edit: "/biz/reimburseBizMainInfo/edit",
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
          this.form.setFieldsValue(pick(this.model,'applyNo','reimbursementTemplateName','userName','userCompanyName','userDepartName','userPosition','userNo','userEmail','userMobile','reimbursementItem','remark','costType','currency','attachmentNum','paymentType','invoiceAmount','paymentAmount','invoicePriceAmount','invoiceTaxAmount','paymentPriceAmount','paymentTaxAmount','strikeAmount','auditConfirmAmount','auditDiscrepancyAmount','auditConfirmRemark','vendorCode','vendorName','vendorAddress','vendorVatFlag','balSeqPayType','sourceSystemName','monthBdgtPeriod','auditUserName','docProcessState','vatAuditUserName','vatDocProcessState','procTemplateName','submitTime','endTime','procState','isLoan','isStrike','isPrepay','existContract','isConntrans','conntransType','existOrder','isImmovable','isEleInvoice','existOffsetFlag','certified','isPayDivide','isSales','isInputVatInvoice','isPreMatchPo','archivingTime','archivingMan','createTime','createBy','updateTime','updateBy'))
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
        this.form.setFieldsValue(pick(row,'applyNo','reimbursementTemplateName','userName','userCompanyName','userDepartName','userPosition','userNo','userEmail','userMobile','reimbursementItem','remark','costType','currency','attachmentNum','paymentType','invoiceAmount','paymentAmount','invoicePriceAmount','invoiceTaxAmount','paymentPriceAmount','paymentTaxAmount','strikeAmount','auditConfirmAmount','auditDiscrepancyAmount','auditConfirmRemark','vendorCode','vendorName','vendorAddress','vendorVatFlag','balSeqPayType','sourceSystemName','monthBdgtPeriod','auditUserName','docProcessState','vatAuditUserName','vatDocProcessState','procTemplateName','submitTime','endTime','procState','isLoan','isStrike','isPrepay','existContract','isConntrans','conntransType','existOrder','isImmovable','isEleInvoice','existOffsetFlag','certified','isPayDivide','isSales','isInputVatInvoice','isPreMatchPo','archivingTime','archivingMan','createTime','createBy','updateTime','updateBy'))
      },

      
    }
  }
</script>