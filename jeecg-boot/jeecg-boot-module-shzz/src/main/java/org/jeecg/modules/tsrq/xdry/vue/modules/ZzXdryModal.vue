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

        <a-form-item label="姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入姓名"></a-input>
        </a-form-item>
        <a-form-item label="身份证" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sfz', validatorRules.sfz]" placeholder="请输入身份证"></a-input>
        </a-form-item>
        <a-form-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="radio" v-decorator="['sex', validatorRules.sex]" :trigger-change="true" dictCode="sex" placeholder="请选择性别"/>
        </a-form-item>
        <a-form-item label="手机号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'phone', validatorRules.phone]" placeholder="请输入手机号"></a-input>
        </a-form-item>
        <a-form-item label="初次发现日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择初次发现日期" v-decorator="[ 'ccfxDate', validatorRules.ccfxDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="管控情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['gkqk', validatorRules.gkqk]" :trigger-change="true" dictCode="gkqk" placeholder="请选择管控情况"/>
        </a-form-item>
        <a-form-item label="管控人员姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'gkryName', validatorRules.gkryName]" placeholder="请输入管控人员姓名"></a-input>
        </a-form-item>
        <a-form-item label="管控人员手机号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'gkryPhone', validatorRules.gkryPhone]" placeholder="请输入管控人员手机号"></a-input>
        </a-form-item>
        <a-form-item label="帮扶情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bfqk', validatorRules.bfqk]" placeholder="请输入帮扶情况"></a-input>
        </a-form-item>
        <a-form-item label="帮扶人员姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bfryName', validatorRules.bfryName]" placeholder="请输入帮扶人员姓名"></a-input>
        </a-form-item>
        <a-form-item label="帮扶人员手机号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bfryPhone', validatorRules.bfryPhone]" placeholder="请输入帮扶人员手机号"></a-input>
        </a-form-item>
        <a-form-item label="有无犯罪史" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['ywfzs', validatorRules.ywfzs]" :trigger-change="true" dictCode="have_no" placeholder="请选择有无犯罪史"/>
        </a-form-item>
        <a-form-item label="犯罪情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fzqk', validatorRules.fzqk]" placeholder="请输入犯罪情况"></a-input>
        </a-form-item>
        <a-form-item label="吸毒原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['xdyy', validatorRules.xdyy]" :trigger-change="true" dictCode="xdyy" placeholder="请选择吸毒原因"/>
        </a-form-item>
        <a-form-item label="吸毒后果" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['xdhg', validatorRules.xdhg]" :trigger-change="true" dictCode="xdhg" placeholder="请选择吸毒后果"/>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: "ZzXdryModal",
    components: { 
      JDate,
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
          name: {rules: [
            {required: true, message: '请输入姓名!'},
          ]},
          sfz: {rules: [
            {required: true, message: '请输入身份证!'},
          ]},
          sex: {rules: [
            {required: true, message: '请输入性别!'},
          ]},
          phone: {rules: [
            {required: true, message: '请输入手机号!'},
            {pattern:/^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},
          ]},
          ccfxDate: {rules: [
          ]},
          gkqk: {rules: [
          ]},
          gkryName: {rules: [
          ]},
          gkryPhone: {rules: [
            {pattern:/^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},
          ]},
          bfqk: {rules: [
          ]},
          bfryName: {rules: [
          ]},
          bfryPhone: {rules: [
            {pattern:/^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},
          ]},
          ywfzs: {rules: [
          ]},
          fzqk: {rules: [
          ]},
          xdyy: {rules: [
          ]},
          xdhg: {rules: [
          ]},
        },
        url: {
          add: "/xdry/zzXdry/add",
          edit: "/xdry/zzXdry/edit",
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
          this.form.setFieldsValue(pick(this.model,'name','sfz','sex','phone','ccfxDate','gkqk','gkryName','gkryPhone','bfqk','bfryName','bfryPhone','ywfzs','fzqk','xdyy','xdhg'))
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
        this.form.setFieldsValue(pick(row,'name','sfz','sex','phone','ccfxDate','gkqk','gkryName','gkryPhone','bfqk','bfryName','bfryPhone','ywfzs','fzqk','xdyy','xdhg'))
      },

      
    }
  }
</script>