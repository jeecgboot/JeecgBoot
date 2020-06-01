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

        <a-form-item label="数据状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['status', validatorRules.status]" :trigger-change="true" dictCode="statue" placeholder="请选择数据状态"/>
        </a-form-item>
        <a-form-item label="企业id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['companyId', validatorRules.companyId]" placeholder="请输入企业id"></a-input>
        </a-form-item>
        <a-form-item label="申报年份" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['reportYear', validatorRules.reportYear]" placeholder="请输入申报年份" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="材料类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['documentType', validatorRules.documentType]" :trigger-change="true" dictCode="supervision_document_type" placeholder="请选择材料类型"/>
        </a-form-item>
        <a-form-item label="材料名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['documentName', validatorRules.documentName]" placeholder="请输入材料名称"></a-input>
        </a-form-item>
        <a-form-item label="内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['content']" :trigger-change="true"></j-upload>
        </a-form-item>
        <a-form-item label="申报人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['createBy']" placeholder="请输入申报人"></a-input>
        </a-form-item>
        <a-form-item label="申报时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择申报时间" v-decorator="['createTime']" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="审核人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['updateBy']" placeholder="请输入审核人"></a-input>
        </a-form-item>
        <a-form-item label="审核时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择审核时间" v-decorator="['updateTime']" :trigger-change="true" style="width: 100%"/>
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
  import JDictSelectTag from "@/components/dict/JDictSelectTag"


  export default {
    name: "CompanyDynamicSupervisionModal",
    components: { 
      JDate,
      JUpload,
      JDictSelectTag,
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
          status: {
            rules: [
              { required: true, message: '请输入数据状态!'},
            ]
          },
          companyId: {
            rules: [
              { required: true, message: '请输入企业id!'},
            ]
          },
          reportYear: {
            rules: [
              { required: true, message: '请输入申报年份!'},
            ]
          },
          documentType: {
            rules: [
              { required: true, message: '请输入材料类型!'},
            ]
          },
          documentName: {
            rules: [
              { required: true, message: '请输入材料名称!'},
            ]
          },
        },
        url: {
          add: "/cds/companyDynamicSupervision/add",
          edit: "/cds/companyDynamicSupervision/edit",
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
          this.form.setFieldsValue(pick(this.model,'status','companyId','reportYear','documentType','documentName','content','createBy','createTime','updateBy','updateTime'))
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
        this.form.setFieldsValue(pick(row,'status','companyId','reportYear','documentType','documentName','content','createBy','createTime','updateBy','updateTime'))
      },

      
    }
  }
</script>