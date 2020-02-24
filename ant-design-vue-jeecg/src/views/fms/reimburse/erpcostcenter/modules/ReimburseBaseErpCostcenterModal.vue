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

        <a-form-item label="ERP成本中心代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'erpOrgCode', validatorRules.erpOrgCode]" placeholder="请输入ERP成本中心代码"></a-input>
        </a-form-item>
        <a-form-item label="ERP成本中心名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'erpOrgName', validatorRules.erpOrgName]" placeholder="请输入ERP成本中心名称"></a-input>
        </a-form-item>
        <a-form-item label="ERP成本中心类型代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'costcenterTypeCode', validatorRules.costcenterTypeCode]" placeholder="请输入ERP成本中心类型代码" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="ERP成本类型名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'costcenterTypeName', validatorRules.costcenterTypeName]" placeholder="请输入ERP成本类型名称"></a-input>
        </a-form-item>
        <a-form-item label="成本中心所属公司代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'erpCompanyCode', validatorRules.erpCompanyCode]" placeholder="请输入成本中心所属公司代码"></a-input>
        </a-form-item>
        <a-form-item label="成本中心所属公司名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'erpCompanyName', validatorRules.erpCompanyName]" placeholder="请输入成本中心所属公司名称"></a-input>
        </a-form-item>
        <a-form-item label="成本中心所属OU代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'erpOuCode', validatorRules.erpOuCode]" placeholder="请输入成本中心所属OU代码"></a-input>
        </a-form-item>
        <a-form-item label="成本中心所属OU名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'misOuName', validatorRules.misOuName]" placeholder="请输入成本中心所属OU名称"></a-input>
        </a-form-item>
        <a-form-item label="ERP成本中心是否有效" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'validFlag', validatorRules.validFlag]" placeholder="请输入ERP成本中心是否有效"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: "ReimburseBaseErpCostcenterModal",
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
        validatorRules: {
          erpOrgCode: {rules: [
            {required: true, message: '请输入ERP成本中心代码!'},
          ]},
          erpOrgName: {rules: [
            {required: true, message: '请输入ERP成本中心名称!'},
          ]},
          costcenterTypeCode: {rules: [
            {required: true, message: '请输入ERP成本中心类型代码!'},
          ]},
          costcenterTypeName: {rules: [
            {required: true, message: '请输入ERP成本类型名称!'},
          ]},
          erpCompanyCode: {rules: [
            {required: true, message: '请输入成本中心所属公司代码!'},
          ]},
          erpCompanyName: {rules: [
            {required: true, message: '请输入成本中心所属公司名称!'},
          ]},
          erpOuCode: {rules: [
            {required: true, message: '请输入成本中心所属OU代码!'},
          ]},
          misOuName: {rules: [
            {required: true, message: '请输入成本中心所属OU名称!'},
          ]},
          validFlag: {rules: [
          ]},
        },
        url: {
          add: "/base/reimburseBaseErpCostcenter/add",
          edit: "/base/reimburseBaseErpCostcenter/edit",
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
          this.form.setFieldsValue(pick(this.model,'erpOrgCode','erpOrgName','costcenterTypeCode','costcenterTypeName','erpCompanyCode','erpCompanyName','erpOuCode','misOuName','validFlag','createTime','createBy','updateTime','updateBy'))
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
        this.form.setFieldsValue(pick(row,'erpOrgCode','erpOrgName','costcenterTypeCode','costcenterTypeName','erpCompanyCode','erpCompanyName','erpOuCode','misOuName','validFlag','createTime','createBy','updateTime','updateBy'))
      },

      
    }
  }
</script>