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

        <a-form-item label="供应商 ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'vendorId', validatorRules.vendorId]" placeholder="请输入供应商 ID" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="供应商名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'vendorName', validatorRules.vendorName]" placeholder="请输入供应商名称"></a-input>
        </a-form-item>
        <a-form-item label="供应商编号（主数据编号）" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'mdCode', validatorRules.mdCode]" placeholder="请输入供应商编号（主数据编号）"></a-input>
        </a-form-item>
        <a-form-item label="供应商类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'vendorType', validatorRules.vendorType]" placeholder="请输入供应商类型"></a-input>
        </a-form-item>
        <a-form-item label="是否三证合一：Y - 是，N - 否" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'certificateFlag', validatorRules.certificateFlag]" placeholder="请输入是否三证合一：Y - 是，N - 否"></a-input>
        </a-form-item>
        <a-form-item label="组织机构代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'orgcertNumber', validatorRules.orgcertNumber]" placeholder="请输入组织机构代码"></a-input>
        </a-form-item>
        <a-form-item label="身份证号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'identifyCode', validatorRules.identifyCode]" placeholder="请输入身份证号码"></a-input>
        </a-form-item>
        <a-form-item label="统一社会信用代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'socIden', validatorRules.socIden]" placeholder="请输入统一社会信用代码"></a-input>
        </a-form-item>
        <a-form-item label="纳税标识：YBNSR 一般纳税人，XGM 小规模纳税人，FZZS 非增值税" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'vatFlag', validatorRules.vatFlag]" placeholder="请输入纳税标识：YBNSR 一般纳税人，XGM 小规模纳税人，FZZS 非增值税"></a-input>
        </a-form-item>
        <a-form-item label="纳税登记编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'taxRegistrationNumber', validatorRules.taxRegistrationNumber]" placeholder="请输入纳税登记编号"></a-input>
        </a-form-item>
        <a-form-item label="是否内部关联方：ZB-总部,QN-其他内部关联方,WZ-外部重要关联方,QW-其他外部供应商" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'innerFlag', validatorRules.innerFlag]" placeholder="请输入是否内部关联方：ZB-总部,QN-其他内部关联方,WZ-外部重要关联方,QW-其他外部供应商"></a-input>
        </a-form-item>
        <a-form-item label="内部关联方标识（往来段）" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'innerCode', validatorRules.innerCode]" placeholder="请输入内部关联方标识（往来段）"></a-input>
        </a-form-item>
        <a-form-item label="是否交易方：Y - 是，N - 否" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'tradingParty', validatorRules.tradingParty]" placeholder="请输入是否交易方：Y - 是，N - 否"></a-input>
        </a-form-item>
        <a-form-item label="客户编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'cuetomerNumber', validatorRules.cuetomerNumber]" placeholder="请输入客户编码"></a-input>
        </a-form-item>
        <a-form-item label="组织机构类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'corporationType', validatorRules.corporationType]" placeholder="请输入组织机构类型"></a-input>
        </a-form-item>
        <a-form-item label="供应商最后更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择供应商最后更新时间" v-decorator="[ 'lastUpdateDate', validatorRules.lastUpdateDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="供应商曾用名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'vendorOldName', validatorRules.vendorOldName]" placeholder="请输入供应商曾用名"></a-input>
        </a-form-item>
        <a-form-item label="法定代表人/责任人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'legalRepresentative', validatorRules.legalRepresentative]" placeholder="请输入法定代表人/责任人"></a-input>
        </a-form-item>
        <a-form-item label="税务登记地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'taxRegAddress', validatorRules.taxRegAddress]" placeholder="请输入税务登记地址"></a-input>
        </a-form-item>
        <a-form-item label="办公地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'officeAddress', validatorRules.officeAddress]" placeholder="请输入办公地址"></a-input>
        </a-form-item>
        <a-form-item label="注册地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'registryAddress', validatorRules.registryAddress]" placeholder="请输入注册地址"></a-input>
        </a-form-item>
        <a-form-item label="经营范围" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'businessRange', validatorRules.businessRange]" placeholder="请输入经营范围"></a-input>
        </a-form-item>
        <a-form-item label="公司类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'companyType', validatorRules.companyType]" placeholder="请输入公司类型"></a-input>
        </a-form-item>
        <a-form-item label="营业期限" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'businessTimelimit', validatorRules.businessTimelimit]" placeholder="请输入营业期限"></a-input>
        </a-form-item>
        <a-form-item label="注册资金" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'registryFund', validatorRules.registryFund]" placeholder="请输入注册资金"></a-input>
        </a-form-item>
        <a-form-item label="注册币种" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'registryCurrency', validatorRules.registryCurrency]" placeholder="请输入注册币种"></a-input>
        </a-form-item>
        <a-form-item label="供应商所在国家" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'country', validatorRules.country]" placeholder="请输入供应商所在国家"></a-input>
        </a-form-item>
        <a-form-item label="供应商所在省份" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'province', validatorRules.province]" placeholder="请输入供应商所在省份"></a-input>
        </a-form-item>
        <a-form-item label="供应商所在城市" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'city', validatorRules.city]" placeholder="请输入供应商所在城市"></a-input>
        </a-form-item>
        <a-form-item label="供应商邮政编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zip', validatorRules.zip]" placeholder="请输入供应商邮政编码"></a-input>
        </a-form-item>
        <a-form-item label="来源系统" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sourceCode', validatorRules.sourceCode]" placeholder="请输入来源系统"></a-input>
        </a-form-item>
        <a-form-item label="是否虚拟供应商：Y - 是，N - 否" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'virtualVendor', validatorRules.virtualVendor]" placeholder="请输入是否虚拟供应商：Y - 是，N - 否"></a-input>
        </a-form-item>
        <a-form-item label="虚拟供应商类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'virtualType', validatorRules.virtualType]" placeholder="请输入虚拟供应商类型"></a-input>
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
    name: "MdmVendorInfoModal",
    components: { 
      JDate,
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
        vendorId:{rules: [{ required: true, message: '请输入供应商 ID!' }]},
        vendorName:{rules: [{ required: true, message: '请输入供应商名称!' }]},
        mdCode:{rules: [{ required: true, message: '请输入供应商编号（主数据编号）!' }]},
        vendorType:{},
        certificateFlag:{},
        orgcertNumber:{},
        identifyCode:{},
        socIden:{},
        vatFlag:{},
        taxRegistrationNumber:{},
        innerFlag:{},
        innerCode:{},
        tradingParty:{},
        cuetomerNumber:{},
        corporationType:{},
        lastUpdateDate:{},
        vendorOldName:{},
        legalRepresentative:{},
        taxRegAddress:{},
        officeAddress:{},
        registryAddress:{},
        businessRange:{},
        companyType:{},
        businessTimelimit:{},
        registryFund:{},
        registryCurrency:{},
        country:{},
        province:{},
        city:{},
        zip:{},
        sourceCode:{},
        virtualVendor:{},
        virtualType:{},
        },
        url: {
          add: "/base/mdmVendorInfo/add",
          edit: "/base/mdmVendorInfo/edit",
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
          this.form.setFieldsValue(pick(this.model,'vendorId','vendorName','mdCode','vendorType','certificateFlag','orgcertNumber','identifyCode','socIden','vatFlag','taxRegistrationNumber','stockFlag','innerFlag','innerCode','tradingParty','cuetomerNumber','corporationType','lastUpdateDate','vendorStatus','vendorOldName','legalRepresentative','taxRegAddress','officeAddress','registryAddress','businessRange','companyType','businessTimelimit','registryFund','registryCurrency','country','province','city','zip','sourceCode','virtualVendor','virtualType','updateTime'))
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
        this.form.setFieldsValue(pick(row,'vendorId','vendorName','mdCode','vendorType','certificateFlag','orgcertNumber','identifyCode','socIden','vatFlag','taxRegistrationNumber','stockFlag','innerFlag','innerCode','tradingParty','cuetomerNumber','corporationType','lastUpdateDate','vendorStatus','vendorOldName','legalRepresentative','taxRegAddress','officeAddress','registryAddress','businessRange','companyType','businessTimelimit','registryFund','registryCurrency','country','province','city','zip','sourceCode','virtualVendor','virtualType','updateTime'))
      },

      
    }
  }
</script>