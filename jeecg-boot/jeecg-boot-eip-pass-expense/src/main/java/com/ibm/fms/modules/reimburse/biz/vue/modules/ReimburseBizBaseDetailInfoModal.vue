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
        <a-form-item label="费用项（业务大类）编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'biztypeCode', validatorRules.biztypeCode]" placeholder="请输入费用项（业务大类）编码"></a-input>
        </a-form-item>
        <a-form-item label="费用项（业务大类）名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'biztypeName', validatorRules.biztypeName]" placeholder="请输入费用项（业务大类）名称"></a-input>
        </a-form-item>
        <a-form-item label="费用项名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['feeItemName']" :trigger-change="true" dictCode="" placeholder="请选择费用项名称"/>
        </a-form-item>
        <a-form-item label="发票金额（价税总额）" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'invoiceAmt', validatorRules.invoiceAmt]" placeholder="请输入发票金额（价税总额）" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="发票价额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'invoicePriceAmt', validatorRules.invoicePriceAmt]" placeholder="请输入发票价额" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="发票税额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'invoiceTaxAmt', validatorRules.invoiceTaxAmt]" placeholder="请输入发票税额" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="付款（支付）金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'paymentAmt', validatorRules.paymentAmt]" placeholder="请输入付款（支付）金额" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="付款（支付）价额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'paymentPriceAmt', validatorRules.paymentPriceAmt]" placeholder="请输入付款（支付）价额" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="付款（支付）税额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'paymentTaxAmt', validatorRules.paymentTaxAmt]" placeholder="请输入付款（支付）税额" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="税率" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['taxRate']" :trigger-change="true" dictCode="" placeholder="请选择税率"/>
        </a-form-item>
        <a-form-item label="报账单明细说明" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'dtlDesc', validatorRules.dtlDesc]" placeholder="请输入报账单明细说明"></a-input>
        </a-form-item>
        <a-form-item label="会计科目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'erpAccountName', validatorRules.erpAccountName]" placeholder="请输入会计科目名称"></a-input>
        </a-form-item>
        <a-form-item label="成本中心名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'costcenterName', validatorRules.costcenterName]" placeholder="请输入成本中心名称"></a-input>
        </a-form-item>
        <a-form-item label="付款清单ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'paymentListId', validatorRules.paymentListId]" placeholder="请输入付款清单ID"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: "ReimburseBizBaseDetailInfoModal",
    components: {
      JDictSelectTag,
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
        applyNo:{rules: [{ required: true, message: '请输入报账单编号!' }]},
        biztypeCode:{rules: [{ required: true, message: '请输入费用项（业务大类）编码!' }]},
        biztypeName:{rules: [{ required: true, message: '请输入费用项（业务大类）名称!' }]},
        feeItemName:{rules: [{ required: true, message: '请输入费用项名称!' }]},
        invoiceAmt:{rules: [{ required: true, message: '请输入发票金额（价税总额）!' }]},
        invoicePriceAmt:{rules: [{ required: true, message: '请输入发票价额!' }]},
        invoiceTaxAmt:{rules: [{ required: true, message: '请输入发票税额!' }]},
        paymentAmt:{rules: [{ required: true, message: '请输入付款（支付）金额!' }]},
        paymentPriceAmt:{rules: [{ required: true, message: '请输入付款（支付）价额!' }]},
        paymentTaxAmt:{rules: [{ required: true, message: '请输入付款（支付）税额!' }]},
        taxRate:{},
        dtlDesc:{rules: [{ required: true, message: '请输入报账单明细说明!' }]},
        erpAccountName:{rules: [{ required: true, message: '请输入会计科目名称!' }]},
        costcenterName:{rules: [{ required: true, message: '请输入成本中心名称!' }]},
        paymentListId:{},
        },
        url: {
          add: "/biz/reimburseBizMainInfo/addReimburseBizBaseDetailInfo",
          edit: "/biz/reimburseBizMainInfo/editReimburseBizBaseDetailInfo",
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
          this.form.setFieldsValue(pick(this.model,'applyNo','reimbursementTemplateCode','reimbursementTemplateName','seq','withdrawCause','biztypeCode','biztypeName','feeItemCode','feeItemName','invoiceAmt','invoicePriceAmt','invoiceTaxAmt','paymentAmt','paymentPriceAmt','paymentTaxAmt','taxCode','taxRate','dtlDesc','erpAccountNo','erpAccountName','costcenterCode','costcenterName','createTime','createBy','updateTime','updateBy','advanceDetailId','budgetItemId','paymentListId'))
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
        this.form.setFieldsValue(pick(row,'applyNo','reimbursementTemplateCode','reimbursementTemplateName','seq','withdrawCause','biztypeCode','biztypeName','feeItemCode','feeItemName','invoiceAmt','invoicePriceAmt','invoiceTaxAmt','paymentAmt','paymentPriceAmt','paymentTaxAmt','taxCode','taxRate','dtlDesc','erpAccountNo','erpAccountName','costcenterCode','costcenterName','createTime','createBy','updateTime','updateBy','advanceDetailId','budgetItemId','paymentListId'))
      },


    }
  }
</script>
