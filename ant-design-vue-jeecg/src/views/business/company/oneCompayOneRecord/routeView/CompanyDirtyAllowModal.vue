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

        <a-form-item label="许可证编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['licenceCode', validatorRules.licenceCode]" placeholder="请输入许可证编号" disabled="true"></a-input>
        </a-form-item>
        <a-form-item label="发证日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择发证日期" v-decorator="['certificateTime']" :trigger-change="true" style="width: 100%" disabled="true"/>
        </a-form-item>
        <a-form-item label="有效开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择有效开始时间" v-decorator="['validStarttime']" :trigger-change="true" style="width: 100%" disabled="true"/>
        </a-form-item>
        <a-form-item label="有效结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择有效结束时间" v-decorator="['validEndtime']" :trigger-change="true" style="width: 100%" disabled="true"/>
        </a-form-item>
        <a-form-item label="发证机关" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['certificateOffice']" placeholder="请输入发证机关" disabled="true"></a-input>
        </a-form-item>
        <a-form-item label="排污类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['dirtyType']" placeholder="请输入排污类别" disabled="true"></a-input>
        </a-form-item>
        <a-form-item label="附件表id集合" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['files']" :trigger-change="true" disabled="true"></j-upload>
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


  export default {
    name: "CompanyDirtyAllowModal",
    components: { 
      JDate,
      JUpload,
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
          licenceCode: {
            rules: [
              { required: true, message: '请输入许可证编号!'},
            ]
          },
        },
        url: {
          add: "/dirty/companyDirtyAllow/add",
          edit: "/dirty/companyDirtyAllow/edit",
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
          this.form.setFieldsValue(pick(this.model,'licenceCode','certificateTime','validStarttime','validEndtime','certificateOffice','dirtyType','files'))
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
            formData.companyId=this.companyId;
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
        this.form.setFieldsValue(pick(row,'licenceCode','certificateTime','validStarttime','validEndtime','certificateOffice','dirtyType','files'))
      },
    },
    props:{
      companyId:""
    }
  }
</script>