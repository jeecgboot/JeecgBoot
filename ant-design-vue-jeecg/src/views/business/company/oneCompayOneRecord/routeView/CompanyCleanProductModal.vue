<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="清洁生产报告名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['reportName', validatorRules.reportName]" placeholder="请输入清洁生产报告名称"></a-input>
        </a-form-item>
        <a-form-item label="报告时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择报告时间" v-decorator="['reportTime']" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="清洁生成报告及专家意见" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['opinionFiles']" :trigger-change="true"></j-upload>
        </a-form-item>
        <a-form-item label="落实情况简要描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['conditionDescribe']" placeholder="请输入落实情况简要描述"></a-textarea>
        </a-form-item>
        <a-form-item label="落实情况附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-image-upload isMultiple v-decorator="['describeFiles']"></j-image-upload>
        </a-form-item>

      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  
  import JUpload from '@/components/jeecg/JUpload'
  import JImageUpload from '@/components/jeecg/JImageUpload'


  export default {
    name: "CompanyCleanProductModal",
    components: { 
      JDate,
      JUpload,
      JImageUpload,
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
          reportName: {
            rules: [
              { required: true, message: '请输入清洁生产报告名称!'},
            ]
          },
        },
        url: {
          add: "/cleanProduct/companyCleanProduct/add",
          edit: "/cleanProduct/companyCleanProduct/edit",
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
          this.form.setFieldsValue(pick(this.model,'reportName','reportTime','opinionFiles','conditionDescribe','describeFiles'))
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
            formData.companyId = this.companyId;
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
        this.form.setFieldsValue(pick(row,'reportName','reportTime','opinionFiles','conditionDescribe','describeFiles'))
      },

      
    },
    props: {
      companyId: ""
    }
  }
</script>