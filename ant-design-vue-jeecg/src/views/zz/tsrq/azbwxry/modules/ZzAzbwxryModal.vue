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
        <a-form-item label="感染途径" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['grtj', validatorRules.grtj]" :trigger-change="true" dictCode="grtj" placeholder="请选择感染途径"/>
        </a-form-item>
        <a-form-item label="是否有犯罪史" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfyfzs', validatorRules.sfyfzs]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否有犯罪史"/>
        </a-form-item>
        <a-form-item label="违法犯罪情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'wffzqk', validatorRules.wffzqk]" placeholder="请输入违法犯罪情况"></a-input>
        </a-form-item>
        <a-form-item label="案件类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'ajlb', validatorRules.ajlb]" placeholder="请输入案件类别"></a-input>
        </a-form-item>
        <a-form-item label="关注类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['gzlx', validatorRules.gzlx]" :trigger-change="true" dictCode="gzlx" placeholder="请选择关注类型"/>
        </a-form-item>
        <a-form-item label="帮扶情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bfqk', validatorRules.bfqk]" placeholder="请输入帮扶情况"></a-input>
        </a-form-item>
        <a-form-item label="帮扶人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bfrName', validatorRules.bfrName]" placeholder="请输入帮扶人姓名"></a-input>
        </a-form-item>
        <a-form-item label="帮扶人电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bfrPhone', validatorRules.bfrPhone]" placeholder="请输入帮扶人电话"></a-input>
        </a-form-item>
        <a-form-item label="收治情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['szqk', validatorRules.szqk]" :trigger-change="true" dictCode="szqk" placeholder="请选择收治情况"/>
        </a-form-item>
        <a-form-item label="收治机构名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'szjgName', validatorRules.szjgName]" placeholder="请输入收治机构名称"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: "ZzAzbwxryModal",
    components: { 
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
            {required: true, message: '请输入证件号码!'},
          ]},
          sex: {rules: [
            {required: true, message: '请输入性别!'},
          ]},
          phone: {rules: [
            {pattern:/^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},
          ]},
          grtj: {rules: [
          ]},
          sfyfzs: {rules: [
          ]},
          wffzqk: {rules: [
          ]},
          ajlb: {rules: [
          ]},
          gzlx: {rules: [
          ]},
          bfqk: {rules: [
          ]},
          bfrName: {rules: [
          ]},
          bfrPhone: {rules: [
            {pattern:/^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},
          ]},
          szqk: {rules: [
          ]},
          szjgName: {rules: [
          ]},
        },
        url: {
          add: "/azbwxry/zzAzbwxry/add",
          edit: "/azbwxry/zzAzbwxry/edit",
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
          this.form.setFieldsValue(pick(this.model,'name','sfz','sex','phone','grtj','sfyfzs','wffzqk','ajlb','gzlx','bfqk','bfrName','bfrPhone','szqk','szjgName'))
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
        this.form.setFieldsValue(pick(row,'name','sfz','sex','phone','grtj','sfyfzs','wffzqk','ajlb','gzlx','bfqk','bfrName','bfrPhone','szqk','szjgName'))
      },

      
    }
  }
</script>