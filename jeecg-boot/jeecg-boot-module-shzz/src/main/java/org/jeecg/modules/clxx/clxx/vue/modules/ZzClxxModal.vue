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

        <a-form-item label="车牌号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'carCode', validatorRules.carCode]" placeholder="请输入车牌号"></a-input>
        </a-form-item>
        <a-form-item label="所有人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'syr', validatorRules.syr]" placeholder="请输入所有人"></a-input>
        </a-form-item>
        <a-form-item label="品牌型号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'ppxh', validatorRules.ppxh]" placeholder="请输入品牌型号"></a-input>
        </a-form-item>
        <a-form-item label="车龄" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'carAge', validatorRules.carAge]" placeholder="请输入车龄" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'adress', validatorRules.adress]" placeholder="请输入地址"></a-input>
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
    name: "ZzClxxModal",
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
          carCode: {rules: [
            {required: true, message: '请输入车牌号!'},
          ]},
          syr: {rules: [
            {required: true, message: '请输入所有人!'},
          ]},
          ppxh: {rules: [
          ]},
          carAge: {rules: [
          ]},
          adress: {rules: [
          ]},
        },
        url: {
          add: "/clxx/zzClxx/add",
          edit: "/clxx/zzClxx/edit",
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
          this.form.setFieldsValue(pick(this.model,'carCode','syr','ppxh','carAge','adress'))
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
        this.form.setFieldsValue(pick(row,'carCode','syr','ppxh','carAge','adress'))
      },

      
    }
  }
</script>