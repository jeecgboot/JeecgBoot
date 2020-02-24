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

        <a-form-item label="供应商公司ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'vendorCompanyId', validatorRules.vendorCompanyId]" placeholder="请输入供应商公司ID" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="供应商ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'vendorId', validatorRules.vendorId]" placeholder="请输入供应商ID" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="供应商编号（主数据编码）" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'mdCode', validatorRules.mdCode]" placeholder="请输入供应商编号（主数据编码）"></a-input>
        </a-form-item>
        <a-form-item label="OU代码（公司段ID）" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'ouCode', validatorRules.ouCode]" placeholder="请输入OU代码（公司段ID）"></a-input>
        </a-form-item>
        <a-form-item label="OU名称（公司段名称）" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'orgName', validatorRules.orgName]" placeholder="请输入OU名称（公司段名称）"></a-input>
        </a-form-item>
        <a-form-item label="父级节点" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'pid', validatorRules.pid]" placeholder="请输入父级节点"></a-input>
        </a-form-item>
        <a-form-item label="公司层是否有效" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'validFlag', validatorRules.validFlag]" placeholder="请输入公司层是否有效"></a-input>
        </a-form-item>
        <a-form-item label="公司层创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择公司层创建时间" v-decorator="[ 'creationDate', validatorRules.creationDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="失效日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择失效日期" v-decorator="[ 'expiryDate', validatorRules.expiryDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择创建时间" v-decorator="[ 'createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'createBy', validatorRules.createBy]" placeholder="请输入创建人"></a-input>
        </a-form-item>
        <a-form-item label="更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择更新时间" v-decorator="[ 'updateTime', validatorRules.updateTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="更新人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'updateBy', validatorRules.updateBy]" placeholder="请输入更新人"></a-input>
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
    name: "MdmVendorCompanyInfoModal",
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
        vendorCompanyId:{rules: [{ required: true, message: '请输入供应商公司ID!' }]},
        vendorId:{rules: [{ required: true, message: '请输入供应商ID!' }]},
        mdCode:{rules: [{ required: true, message: '请输入供应商编号（主数据编码）!' }]},
        ouCode:{},
        orgName:{},
        pid:{},
        validFlag:{rules: [{ required: true, message: '请输入公司层是否有效!' }]},
        creationDate:{rules: [{ required: true, message: '请输入公司层创建时间!' }]},
        expiryDate:{},
        createTime:{},
        createBy:{},
        updateTime:{},
        updateBy:{},
        },
        url: {
          add: "/base/mdmVendorInfo/addMdmVendorCompanyInfo",
          edit: "/base/mdmVendorInfo/editMdmVendorCompanyInfo",
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
          this.form.setFieldsValue(pick(this.model,'vendorCompanyId','vendorId','mdCode','ouCode','orgName','pid','validFlag','hasChild','creationDate','expiryDate','createTime','createBy','updateTime','updateBy'))
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
        this.form.setFieldsValue(pick(row,'vendorCompanyId','vendorId','mdCode','ouCode','orgName','pid','validFlag','hasChild','creationDate','expiryDate','createTime','createBy','updateTime','updateBy'))
      },


    }
  }
</script>
