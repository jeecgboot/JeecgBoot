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
        <a-form-item label="员工姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'userName', validatorRules.userName]" placeholder="请输入员工姓名"  v-bind:read-only="true" class="inputReadOnly"></a-input>
        </a-form-item>
        <a-form-item label="员工所属公司" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'userCompanyName', validatorRules.userCompanyName]" placeholder="请输入员工所属公司名称" v-bind:read-only="true" class="inputReadOnly"></a-input>
        </a-form-item>
        <a-form-item label="员工所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'userDepartName', validatorRules.userDepartName]" placeholder="请输入员工所属部门名称" v-bind:read-only="true" class="inputReadOnly"></a-input>
        </a-form-item>
        <a-form-item label="员工职务" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'userPosition', validatorRules.userPosition]" placeholder="请输入员工职务" v-bind:read-only="true" class="inputReadOnly"></a-input>
        </a-form-item>
        <a-form-item label="员工编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'userNo', validatorRules.userNo]" placeholder="请输入员工编号" v-bind:read-only="true" class="inputReadOnly"></a-input>
        </a-form-item>
        <a-form-item label="邮箱" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'userEmail', validatorRules.userEmail]" placeholder="请输入员工邮箱"></a-input>
        </a-form-item>
        <a-form-item label="手机号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'userMobile', validatorRules.userMobile]" placeholder="请输入员工手机号"></a-input>
        </a-form-item>
        <a-form-item label="报销事由" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="[ 'reimbursementItem', validatorRules.reimbursementItem]" placeholder="请输入报销事由"></a-textarea>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="[ 'remark', validatorRules.remark]" placeholder="请输入备注"></a-textarea>
        </a-form-item>
        <a-form-item label="支出成本类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['costType',validatorRules.costType]" :trigger-change="true" dictCode="fms_cost_type" placeholder="请选择"/>
        </a-form-item>
        <a-form-item label="币种" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['currency', {}]" :trigger-change="true" dictCode="fms_currency" placeholder="请选择币种"/>
        </a-form-item>
        <a-form-item label="发票张数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'attachmentNum', validatorRules.attachmentNum]" placeholder="请输入发票张数" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="支付方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['paymentType', {}]" :trigger-change="true" dictCode="fms_payment_type" placeholder="请选择支付方式"/>
        </a-form-item>
        <a-form-item label="供应商编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-row class="j-select-biz-component-box" type="flex" :gutter="8">
            <a-col class="left"  style="width: 75%;">
              <a-input v-decorator="[ 'vendorCode', validatorRules.vendorName]" placeholder="请选择供应商编号"></a-input>
            </a-col>
            <a-col class="right"style="width: 10%;">
              <a-button type="primary" icon="search"  @click="showModal" >选择供应商</a-button>
            </a-col>
          </a-row>
        </a-form-item>
        <a-form-item label="供应商名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'vendorName', validatorRules.vendorName]" placeholder="请选择供应商名称"></a-input>
        </a-form-item>
        <a-form-item label="供应商纳税人类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'vendorVatFlag', validatorRules.vendorVatFlag]" placeholder="请输入供应商纳税人类型: 一般纳税人/ 小规模纳税人/非增值税纳税人"></a-input>
        </a-form-item>
        <a-form-item label="是否冲减" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-dict-select-tag type="list" v-decorator="['isStrike', {}]" :trigger-change="true" @change="setPrepay" dictCode="fms_comm_yn" placeholder="请选择是否冲减"/>
        </a-form-item>
        <a-form-item label="是否预付" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['isPrepay', {}]" :trigger-change="true" @change="setStrike" dictCode="fms_comm_yn" placeholder="请选择是否预付"/>
        </a-form-item>
        <a-form-item label="是否有合同" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['existContract', {}]"  :trigger-change="true" dictCode="fms_comm_yn" placeholder="请选择是否有合同"/>
        </a-form-item>
        <a-form-item label="是否关联交易" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['isConntrans', {}]" :trigger-change="true" dictCode="fms_comm_yn" placeholder="请选择是否关联交易" @change="connTypesVis"/>
        </a-form-item>
        <a-form-item label="关联交易类型" :labelCol="labelCol" :wrapperCol="wrapperCol" v-if="showConnType" key="111" >
          <a-input v-decorator="[ 'conntransType', validatorRules.conntransType]" placeholder="请输入关联交易类型" v-if="true"></a-input>
        </a-form-item>
        <a-form-item label="是否包含订单" :labelCol="labelCol" :wrapperCol="wrapperCol">
             <j-dict-select-tag type="list" v-decorator="['existOrder', {}]" :trigger-change="true" dictCode="fms_comm_yn" placeholder="请选择是否包含订单"/>
        </a-form-item>
        <a-form-item label="是否不动产" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['isImmovable', {}]" :trigger-change="true" dictCode="fms_comm_yn" placeholder="请选择是否不动产"/>
        </a-form-item>
        <a-form-item label="是否电子发票" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['isEleInvoice', {}]" :trigger-change="true" dictCode="fms_comm_yn" placeholder="请选择是否电子发票"/>
        </a-form-item>
        <a-form-item label="是否包含进项抵扣凭证" :labelCol="labelCol" :wrapperCol="wrapperCol">
             <j-dict-select-tag type="list" v-decorator="['existOffsetFlag', {}]" :trigger-change="true" dictCode="fms_comm_yn" placeholder="是否包含进项抵扣凭证"/>
        </a-form-item>
        <a-form-item label="是否成本下分" :labelCol="labelCol" :wrapperCol="wrapperCol">
             <j-dict-select-tag type="list" v-decorator="['isPayDivide', {}]" :trigger-change="true" dictCode="fms_comm_yn" placeholder="请选择是否成本下分"/>
        </a-form-item>
        <a-form-item label="是否包含视同销售" :labelCol="labelCol" :wrapperCol="wrapperCol">
               <j-dict-select-tag type="list" v-decorator="['isSales', {}]" :trigger-change="true" dictCode="fms_comm_yn" placeholder="请选择是否视同销售"/>
        </a-form-item>
        <a-form-item label="是否需要起草进项发票报账单" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['isInputVatInvoice', {}]" :trigger-change="true" dictCode="fms_comm_yn" placeholder="请选择是否需要起草进项发票报账单"/>
        </a-form-item>
        <a-form-item label="是否已经预匹配订单" :labelCol="labelCol" :wrapperCol="wrapperCol">
           <j-dict-select-tag type="list" v-decorator="['isPreMatchPo', {}]" :trigger-change="true" dictCode="fms_comm_yn" placeholder="请选择是否已经预匹配订单"/>
        </a-form-item>
        <a-form-item label="员工所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol" v-if="false" key="userDepartCode">
          <a-input v-decorator="[ 'userDepartCode', {}]" ></a-input>
        </a-form-item>
        <a-form-item label="用户所在公司代码" :labelCol="labelCol" :wrapperCol="wrapperCol" v-if="false" key="UserCompanyCode" >
          <a-input v-decorator="[ 'userCompanyCode', {}]"></a-input>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
    <select-vendor-modal  ref="vendorModal"  @setVendorInfo="setVendorInfo"></select-vendor-modal>
  </div>
</template>

<script>

  import { httpAction,getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import Options from "ant-design-vue/es/vc-pagination/Options";
  import JSelectVendor from "../../components/JSelectVendor";
  import ARow from "ant-design-vue/es/grid/Row";
  import ACol from "ant-design-vue/es/grid/Col";
  import AForm from "ant-design-vue/es/form/Form";
  import AFormItem from "ant-design-vue/es/form/FormItem";
  import SelectVendorModal from "../vendor/modules/SelectVendorModal";

  export default {
    name: "ReimburseBizMainInfoModal",
    components: {
      SelectVendorModal,
      AForm,
      AFormItem,
      ARow,
      ACol,
      JSelectVendor,
      Options,
      ATextarea
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        selectVendor:false,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        defaultSelected:'N',
        showConnType:false,
        confirmLoading: false,
        validatorRules:{
        userName:{rules: [{ required: true, message: '请输入员工姓名!' }]},
        userCompanyName:{rules: [{ required: true, message: '请输入员工所属公司名称!' }]},
        userDepartName:{rules: [{ required: true, message: '请输入员工所属部门名称!' }]},
        userPosition:{},
        userNo:{rules: [{ required: true, message: '请输入员工编号!' }]},
        userEmail:{rules: [{ required: true, message: '请输入员工邮箱!' }]},
        userMobile:{},
        reimbursementItem:{rules: [{ required: true, message: '请输入报销事由!' }]},
        remark:{},
        costType:{rules: [{ required: true, message: '请选择支出成本类型' }]},
        currency:{},
        attachmentNum:{rules: [{ required: true, message: '请输入发票张数!' }]},
        paymentType:{},
        vendorCode:{},
        vendorName:{},
        vendorAddress:{},
        vendorVatFlag:{},
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
          getRbsNo: "/biz/reimburseBizMainInfo/genReimburseNum",
          initRbsBizMainInfo: "/biz/reimburseBizMainInfo/initRbsBizMainInfo"
        }
     
      }
    },
    created () {
    },
    methods: {
      showModal() {
        //this.visible = true;
        //this.loadData();
        this.$refs.vendorModal.show(true);
      },
      add () {
        this.edit({});
        //初始化报销单编号，人员信息等
        let geturl = this.url.initRbsBizMainInfo;
        //let geturl = this.url.getRbsNo;
        let dbApplyNo = '';
        console.log("获取报销单数据链接",geturl);
        let params = '';
        getAction(geturl,params).then((res)=>{
          if(res.success){
            this.model = Object.assign({}, res.result);
            setTimeout(()=>{
              this.form.setFieldsValue(pick(this.model,'reimbursementTemplateName','userName','userCompanyName','userDepartName','userCompanyCode','userDepartCode','userPosition','userNo','userEmail','userMobile','reimbursementItem','remark','costType','currency','attachmentNum','paymentType','vendorCode','vendorName','vendorAddress','vendorVatFlag','balSeqPayType','monthBdgtPeriod','procState','isStrike','isPrepay','existContract','isConntrans','conntransType','existOrder','isImmovable','isEleInvoice','existOffsetFlag','isPayDivide','isSales','isInputVatInvoice','isPreMatchPo'))
              },0);
            }
        }).finally(() => {

        })
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
      connTypesVis(value){
            try {
              //console.log("value===="+value);
              if('N'==value){
                this.showConnType=false;
              }else{
                this.showConnType=true;
              }
            }catch (ex) {
              console.log(ex);
            }
           //this.form.setFieldsValue({'conntransType':'我不知道'});
      },
      setPrepay(value){
        try {
          if('Y'==value){
            this.form.setFieldsValue({'isPrepay':'N'});
          }
        }catch (ex) {
          console.log(ex);
        }
      },
      setStrike(value){
        try {
          if('Y'==value){
            this.form.setFieldsValue({'isStrike':'N'});
          }
        }catch (ex) {
          console.log(ex);
        }
      },
      setVendorInfo(vendorInfo){
        try {
          //console.log(" setVendorInfo value===="+vendorInfo);
          setTimeout(()=>{
            this.form.setFieldsValue({'vendorCode':vendorInfo.mdCode,'vendorName':vendorInfo.vendorName,'vendorVatFlag':vendorInfo.vatFlag});
          },0);
          //this.form.setFieldsValue(pick(vendorInfo,'vendorCode','vendorName','vendorAddress','vendorVatFlag'));
        }catch (ex) {
          console.log(ex);
        }
        //this.form.setFieldsValue({'conntransType':'我不知道'});
      }
    }
  }
</script>
<style lang="less" scoped>
  .inputReadOnly{
    background-color:#fafafa !important;
  }
</style>