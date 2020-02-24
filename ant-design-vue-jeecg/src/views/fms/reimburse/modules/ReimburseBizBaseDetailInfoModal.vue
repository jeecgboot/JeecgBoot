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
        <a-form-item label="成本中心" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'costcenterCode']"  v-show="false"></a-input>
          <a-input v-decorator="[ 'costcenterName', validatorRules.costcenterName]" placeholder="请输入成本中心名称" v-bind:read-only="true" class="inputReadOnly"></a-input>
        </a-form-item>
        <a-row :gutter="18">
          <a-col>
          <a-form-item label="业务大类名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'biztypeName']"  v-show="false"></a-input>
            <a-input v-decorator="[ 'applyNo']"  v-show="false"></a-input>
            <a-input v-decorator="[ 'seq']"  v-show="false"></a-input>
            <a-input v-decorator="['reimbursementTemplateCode']" v-show="false"></a-input>
            <a-input v-decorator="['reimbursementTemplateName']" v-show="false"></a-input>
           <a-select  @change="handleInput" v-decorator="[ 'biztypeCode',validatorRules.biztypeCode]"  placeholder="请选择业务大类" >
             <a-select-option v-for="(item, key) in bizTypeOptions" :key="key" :value="item.value">
                <span style="display: inline-block;width: 100%" :title=" item.text || item.label " id="selectedBiztypeName">
                  {{ item.text || item.label }}
                </span>
             </a-select-option>
           </a-select>
          </a-form-item>
          </a-col>
          <a-col>

          <a-form-item label="业务小类名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
             <a-input v-decorator="[ 'feeItemName']"  v-show="false"></a-input>
             <a-select @change="handleBizTypeInput" v-decorator="[ 'feeItemCode',validatorRules.feeItemCode]" placeholder="请选择业务小类">
              <a-select-option v-for="(item, key) in feeItemOptions" :key="key" :value="item.value" >
                <span style="display: inline-block;width: 100%" :title=" item.text || item.label " id="selectedFeeItem">
                  {{ item.text || item.label }}
                </span>
              </a-select-option>
            </a-select>
          </a-form-item>
          </a-col>
        </a-row>
        <a-form-item label="会计科目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'erpAccountNo']"  v-show="false"></a-input>
          <a-input v-decorator="[ 'erpAccountName', validatorRules.erpAccountName]" placeholder="请输入会计科目名称" v-bind:read-only="true" class="inputReadOnly"></a-input>
        </a-form-item>

        <a-form-item label="税率" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['taxRate', validatorRules.taxRate]" :trigger-change="true" dictCode="fms_vat_rate" placeholder="请选择税率" ref="vatRate"/>
        </a-form-item>
        <a-form-item label="发票金额（价税总额）" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'invoiceAmt', validatorRules.invoiceAmt]" placeholder="请输入发票金额（价税总额）" @change="calculateVat" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="发票价额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'invoicePriceAmt', validatorRules.invoicePriceAmt]" placeholder="请输入发票价额" v-bind:read-only="true" class="inputReadOnly" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="发票税额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'invoiceTaxAmt', validatorRules.invoiceTaxAmt]" placeholder="请输入发票税额" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="付款（支付）金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'paymentAmt', validatorRules.paymentAmt]" placeholder="请输入付款（支付）金额" @change="calculatePayment" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="付款（支付）价额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'paymentPriceAmt', validatorRules.paymentPriceAmt]" placeholder="请输入付款（支付）价额"  v-bind:read-only="true" class="inputReadOnly" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="付款（支付）税额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'paymentTaxAmt', validatorRules.paymentTaxAmt]" placeholder="请输入付款（支付）税额" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="报账单明细说明" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'paymentListId']"  v-show="false"></a-input>
          <a-textarea v-decorator="[ 'dtlDesc', validatorRules.dtlDesc]" placeholder="请输入报账单明细说明"></a-textarea>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction ,getAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import AForm from "ant-design-vue/es/form/Form";
  import AFormItem from "ant-design-vue/es/form/FormItem";
  import ARow from "ant-design-vue/es/grid/Row";
  import ACol from "ant-design-vue/es/grid/Col"
  import ATextarea from "ant-design-vue/es/input/TextArea";

  export default {
    name: "ReimburseBizBaseDetailInfoModal",
    components: {
      ATextarea,
      JDictSelectTag,
      AForm,
      AFormItem,
      ARow,
      ACol
    },
    props:{
      mainId:{
        type:String,
        required:false,
        default:''
      },
      costType:{
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
      tagType:""
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
        bizTypeOptions:[],
        feeItemOptions:[],
        confirmLoading: false,
        ccTypeCode:'',
        bizTName:'',
        validatorRules:{
        biztypeCode:{rules: [{ required: true, message: '请选择业务大类!' }]},
        feeItemCode:{rules: [{ required: true, message: '请选项业务小类!' }]},
        invoiceAmt:{rules: [{ required: true, message: '请输入发票金额（价税总额）!' }]},
        invoicePriceAmt:{rules: [{ required: true, message: '请输入发票价额!' }]},
        invoiceTaxAmt:{rules: [{ required: true, message: '请输入发票税额!' }]},
        paymentAmt:{rules: [{ required: true, message: '请输入付款（支付）金额!' }]},
        paymentPriceAmt:{rules: [{ required: true, message: '请输入付款（支付）价额!' }]},
        paymentTaxAmt:{rules: [{ required: true, message: '请输入付款（支付）税额!' }]},
        taxRate:{rules: [{ required: true, message: '请输入税率!' }]},
        dtlDesc:{rules: [{ required: true, message: '请输入报账单明细说明!' }]},
        erpAccountName:{rules: [{ required: true, message: '请输入会计科目名称!' }]},
        costcenterName:{rules: [{ required: true, message: '请输入成本中心名称!' }]},
        paymentListId:{},
        },
        url: {
          add: "/biz/reimburseBizMainInfo/addReimburseBizBaseDetailInfo",
          edit: "/biz/reimburseBizMainInfo/editReimburseBizBaseDetailInfo",
          initCostCenterInfo: "/biz/reimburseBizMainInfo/initCostCenter"
        }
      }
    },
    watch:{
      bizetypeCode:{
        immediate:true,
        handler() {
          this.initDtl()
        }
      }
    },
    created () {
    },
    methods: {
      add () {
        this.model = Object.assign({},{});
        this.initDtl();
        this.visible = true;
        //this.form.resetFields();
        //this.initDtl();
        //this.form.setFieldsValue({applyNo:this.mainId});
      },
      edit (record) {
        this.form.resetFields();
        //初始化业务大小类及会计科目
        this.initEdit();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'applyNo','reimbursementTemplateCode','reimbursementTemplateName','seq','withdrawCause','biztypeCode','biztypeName','feeItemCode','feeItemName','invoiceAmt','invoicePriceAmt','invoiceTaxAmt','paymentAmt','paymentPriceAmt','paymentTaxAmt','taxCode','taxRate','dtlDesc','erpAccountNo','erpAccountName','costcenterCode','costcenterName','advanceDetailId','budgetItemId','paymentListId'))
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
        handleInput(value){
        //alert(e);
        //alert('biztypeName==='+e);
        //this.form.get
          //alert(document.getElementById("selectedBiztypeName").title);
        let costParams={costTypeCenter:this.costType+','+this.ccTypeCode,bizFeeInfo:value+','};
        getAction('/base/rmbsErpAccntItemRela/getErpAccntRela',costParams).then((res) => {
          if (res.success) {
            this.feeItemOptions = res.result;
          }
        })
      },
      handleBizTypeInput(e){
        let selBizTypeCode = this.form.getFieldValue('biztypeCode');

        let costParams={costTypeCenter:this.costType+','+this.ccTypeCode,bizFeeInfo:selBizTypeCode+','+e};
        getAction('/base/rmbsErpAccntItemRela/getErpAccntRela',costParams).then((res) => {
          if (res.success) {
            //console.log("rst=="+res.result[0].text);
            //console.log("rst templateCode=="+this.rmbsTemplateCode);
            //alert("rst --- rmbsTemplateName=="+this.rmbsTemplateName);
            this.form.resetFields({'biztypeName':'','feeItemName':''});
            setTimeout(()=>{
              this.form.setFieldsValue({'biztypeName':document.getElementById("selectedBiztypeName").title});
              this.form.setFieldsValue({'feeItemName':document.getElementById('selectedFeeItem').title});
              //console.log("value selBizTypeCode ==="+document.getElementById('selectedFeeItem').title);
              this.form.setFieldsValue({'erpAccountNo':res.result[0].value,'erpAccountName':res.result[0].text,'reimbursementTemplateCode':this.rmbsTemplateCode,'reimbursementTemplateName':this.rmbsTemplateName});
            },0);
          }
        })
      },
      async initDtl(){
          //this.rmbsTemplateCode;
         let geturl = '/base/rmbsErpCostcenter/initCostCenterInfo';
         let params = '';
        let res =  await getAction(geturl,params);
          if (res.success) {
            setTimeout(()=>{
              this.form.setFieldsValue({'costcenterCode':res.result.costcenterCode,'costcenterName':res.result.costcenterName})
            },0);
            this.ccTypeCode = res.result.costcenterTypeCode;
            let costParams={costTypeCenter:this.costType+','+this.ccTypeCode,bizFeeInfo:''};
            //console.log("costParams===="+costParams);
            getAction('/base/rmbsErpAccntItemRela/getErpAccntRela',costParams).then((res) => {
              if (res.success) {
                this.bizTypeOptions = res.result;
                this.form.setFieldsValue({applyNo:this.mainId});
              }
            })
          }
         },
      async initEdit(){
          let costParams={costTypeCenter:this.costType+','+this.ccTypeCode,bizFeeInfo:''};
          getAction('/base/rmbsErpAccntItemRela/getErpAccntRela',costParams).then((res) => {
          if (res.success) {
             this.bizTypeOptions = res.result;
            let costParams={costTypeCenter:this.costType+','+this.ccTypeCode,bizFeeInfo:this.form.getFieldValue('biztypeCode')+','};
            getAction('/base/rmbsErpAccntItemRela/getErpAccntRela',costParams).then((res) => {
              if (res.success) {
                this.feeItemOptions = res.result;
              }
            })
          }
        })
      },
      calculateVat(val){
        //console.log("val===="+ val.toFixed(2));
        let vatRate = parseFloat(this.$refs["vatRate"].value);
        let price = parseFloat(val)/parseFloat(1+vatRate);
        //console.log("price======"+price.toFixed(2));
        let vat = val.toFixed(2) - price.toFixed(2);
        //console.log("vat======"+vat.toFixed(2));
        this.form.setFieldsValue({'invoicePriceAmt':price.toFixed(2),'invoiceTaxAmt':vat.toFixed(2)});
      },
      calculatePayment(val){
        //console.log("val===="+ val.toFixed(2));
        let vatRate = parseFloat(this.$refs["vatRate"].value);
        let price = parseFloat(val)/parseFloat(1+vatRate);
        //console.log("price======"+price.toFixed(2));
        let vat = val.toFixed(2) - price.toFixed(2);
       // console.log("vat======"+vat.toFixed(2));
        this.form.setFieldsValue({'paymentPriceAmt':price.toFixed(2),'paymentTaxAmt':vat.toFixed(2)});
      }
    }
  }
</script>
<style lang="less" scoped>
  .inputReadOnly{
    background-color:#fafafa !important;
  }
</style>
