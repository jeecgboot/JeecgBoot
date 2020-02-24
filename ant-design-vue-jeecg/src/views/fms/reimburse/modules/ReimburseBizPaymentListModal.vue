<template>
  <div>
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
        <a-form-item>
              <a-button type="primary" icon="search"  @click="showModal" >选择供应商账户</a-button>
        </a-form-item>
        <a-form-item label="报销单编号" :labelCol="labelCol" :wrapperCol="wrapperCol" >
          <a-input v-decorator="[ 'applyNo']" v-bind:read-only="true" class="inputReadOnly"></a-input>
          <a-input-number v-decorator="[ 'seq']" v-show="false"/>
          <a-input v-decorator="['reimbursementTemplateCode']" v-show="false"></a-input>
          <a-input v-decorator="['reimbursementTemplateName']" v-show="false"></a-input>
          <a-input v-decorator="['vendorCode']" v-show="false"></a-input>
          <a-input v-decorator="['vendorName']" v-show="false"></a-input>
        </a-form-item>
        <a-form-item label="银行类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['bankType']" :trigger-change="true" dictCode="fms_bank_type" placeholder="请选择银企银行类别"/>
        </a-form-item>
        <!--
        <a-form-item label="收方行所在省" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['bankLocProvince']" :trigger-change="true" dictCode="" placeholder="请选择收方行所在省"/>
        </a-form-item>
        <a-form-item label="收方行所在城市" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-search-select-tag v-decorator="['bankLocCity']" dict="" />
        </a-form-item>
        -->
        <a-form-item label="开户行全称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bankBranchName', validatorRules.bankBranchName]" placeholder="请输入收方行分支机构全称（行名）"></a-input>
        </a-form-item>
        <a-form-item label="开户行联行号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bankCnapsCode', validatorRules.bankCnapsCode]" placeholder="请输入收方行分支机构联行号"></a-input>
        </a-form-item>
        <a-form-item label="银行账号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bankAccountNo', validatorRules.bankAccountNo]" placeholder="请输入收方银行账号"></a-input>
        </a-form-item>
        <a-form-item label="银行账号户名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bankAccountName', validatorRules.bankAccountName]" placeholder="请输入收方银行账号户名"></a-input>
        </a-form-item>
        <a-form-item label="账户类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['bankAccountType']" :trigger-change="true" dictCode="fms_bankaccnt_type" placeholder="请选择账户类别"/>
        </a-form-item>
        <a-form-item label="本次付款金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'paymentAmount', validatorRules.paymentAmount]" placeholder="请输入本次支付金额" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="付款清单说明" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="[ 'remark', validatorRules.remark]" placeholder="请输入付款清单说明"></a-textarea>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
  <select-vendor-accnt-modal  ref="vendorBankAccntModal"  @setVendorAccntInfo="setVendorAccntInfo" :vendorCode="vendorCode"></select-vendor-accnt-modal>
  </div>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import SelectVendorAccntModal from "../vendor/modules/SelectVendorAccntModal";
  import AFormItem from "ant-design-vue/es/form/FormItem";
  import {currency} from '@/utils/currency'
  export default {
    name: "ReimburseBizPaymentListModal",
    components: {
      AFormItem,
      SelectVendorAccntModal,
      ATextarea,
      JDictSelectTag,
      JSearchSelectTag,
    },
    filters:{
      currency:currency
    },
    props:{
      mainId:{
        type:String,
        required:false,
        default:''
      },
      rmbsTemplateCode:{
        type:String,
        required:false,
        default:''
      },
      rmbsTemplateName:{
        type:String,
        required:false,
        default:''
      },
      vendorCode:{
        type:String,
        required:false,
        default:''
      },
      vendorName:{
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
        seq:{rules: [{ required: true, message: '请输入付款明细行序号!' }]},
        remark:{},
        bankBranchName:{rules: [{ required: true, message: '请输入收方行分支机构全称（行名）!' }]},
        bankCnapsCode:{},
        bankAccountNo:{rules: [{ required: true, message: '请输入收方银行账号!' }]},
        bankAccountName:{rules: [{ required: true, message: '请输入收方银行账号户名!' }]},
        bankAccountType:{rules: [{ required: true, message: '请输入账户类别!' }]},
        erpAccountComRemark:{rules: [{ required: true, message: '请输入ERP付款账户名称!' }]},
        paymentAmount:{rules: [{ required: true, message: '请输入本次支付金额!' }]},
        bankType:{},
        bankLocProvince:{},
        bankLocCity:{},
        feeItemName:{},
        },
        url: {
          add: "/biz/reimburseBizMainInfo/addReimburseBizPaymentList",
          edit: "/biz/reimburseBizMainInfo/editReimburseBizPaymentList",
        }

      }
    },
    created () {
    },
    methods: {
      showModal() {
        //this.visible = true;
        //this.loadData();
        this.$refs.vendorBankAccntModal.show(true);
      },
      add () {
        this.edit({});
        console.log("this.vendorName"+this.vendorName);
   /*     setTimeout(()=>{
          this.form.setFieldsValue({'applyNo':this.mainId,'reimbursementTemplateCode':this.rmbsTemplateCode,'reimbursementTemplateName':this.rmbsTemplateName,
                                  'vendorCode':this.vendorCode,'vendorName':this.vendorName})
        },0);*/
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'applyNo','reimbursementTemplateCode','reimbursementTemplateName','seq','detailSeq','remark','bankBranchName','bankCnapsCode','bankAccountNo','bankAccountName','bankAccountType','paymentContactorPhone','vendorCode','vendorName','vendorAddress','erpAccountComNo','erpAccountComRemark','paymentAccountNo','paymentAccountName','paymentAmount','bankType','bankLocProvince','bankLocCity','paymentStatus','paymentStatusDesc','auditUserId','biztypeCode','biztypeName','feeItemCode','feeItemName','batchPayTplId','bankReceiptId','createTime','createBy','updateTime','updateBy'))
        });
        setTimeout(()=>{
          this.form.setFieldsValue({'applyNo':this.mainId,'reimbursementTemplateCode':this.rmbsTemplateCode,'reimbursementTemplateName':this.rmbsTemplateName,
            'vendorCode':this.vendorCode,'vendorName':this.vendorName})
        },0);
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
        this.form.setFieldsValue(pick(row,'applyNo','reimbursementTemplateCode','reimbursementTemplateName','seq','detailSeq','remark','bankBranchName','bankCnapsCode','bankAccountNo','bankAccountName','bankAccountType','paymentContactorPhone','vendorCode','vendorName','vendorAddress','erpAccountComNo','erpAccountComRemark','paymentAccountNo','paymentAccountName','paymentAmount','bankType','bankLocProvince','bankLocCity','paymentStatus','paymentStatusDesc','auditUserId','biztypeCode','biztypeName','feeItemCode','feeItemName','batchPayTplId','bankReceiptId','createTime','createBy','updateTime','updateBy'))
      },
      setVendorAccntInfo(vendorAccntInfo){
        try {
          console.log(" setVendorAccntInfo value===="+vendorAccntInfo);
          this.form.setFieldsValue({'bankBranchName':vendorAccntInfo.branchBank,'bankCnapsCode':vendorAccntInfo.cnapNumber,
                                    'bankAccountNo':vendorAccntInfo.accountNumber,'bankAccountName':vendorAccntInfo.accountName,
                                    'bankType':vendorAccntInfo.bankCode});
          //this.form.setFieldsValue(pick(vendorInfo,'vendorCode','vendorName','vendorAddress','vendorVatFlag'));
        }catch (ex) {
          console.log(ex);
        }
      }

    }
  }
</script>
