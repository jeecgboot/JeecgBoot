<template>
  <a-drawer
    :title="title"
    :width="width"
    placement="right"
    :closable="false"
    @close="close"
    :visible="visible">
  
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
        <a-form-item label="手机号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'phone', validatorRules.phone]" placeholder="请输入手机号码"></a-input>
        </a-form-item>
        <a-form-item label="人员类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['rylx', validatorRules.rylx]" :trigger-change="true" dictCode="rylx" placeholder="请选择人员类型"/>
        </a-form-item>
        <a-form-item label="家庭情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['jtqk', validatorRules.jtqk]" :trigger-change="true" dictCode="jtqk" placeholder="请选择家庭情况"/>
        </a-form-item>
        <a-form-item label="监护人身份证" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'jhrSfz', validatorRules.jhrSfz]" placeholder="请输入监护人身份证"></a-input>
        </a-form-item>
        <a-form-item label="监护人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'jhrName', validatorRules.jhrName]" placeholder="请输入监护人姓名"></a-input>
        </a-form-item>
        <a-form-item label="与监护人关系" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'jhrGx', validatorRules.jhrGx]" placeholder="请输入与监护人关系"></a-input>
        </a-form-item>
        <a-form-item label="监护人电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'jhrPhone', validatorRules.jhrPhone]" placeholder="请输入监护人电话"></a-input>
        </a-form-item>
        <a-form-item label="监护人住址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'jhrZz', validatorRules.jhrZz]" placeholder="请输入监护人住址"></a-input>
        </a-form-item>
        <a-form-item label="是否违法犯罪" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfwffz', validatorRules.sfwffz]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否违法犯罪"/>
        </a-form-item>
        <a-form-item label="违法犯罪情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'wffzqk', validatorRules.wffzqk]" placeholder="请输入违法犯罪情况"></a-input>
        </a-form-item>
        <a-form-item label="帮扶人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bfrName', validatorRules.bfrName]" placeholder="请输入帮扶人姓名"></a-input>
        </a-form-item>
        <a-form-item label="帮扶人电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bfrPhone', validatorRules.bfrPhone]" placeholder="请输入帮扶人电话"></a-input>
        </a-form-item>
        <a-form-item label="帮扶手段" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['bfsd', validatorRules.bfsd]" :trigger-change="true" dictCode="bfsd" placeholder="请选择帮扶手段"/>
        </a-form-item>
        <a-form-item label="帮扶情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bfqk', validatorRules.bfqk]" placeholder="请输入帮扶情况"></a-input>
        </a-form-item>
        
      </a-form>
    </a-spin>
    <a-button type="primary" @click="handleOk">确定</a-button>
    <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  
  export default {
    name: "ZzZdqsnModal",
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
            {required: true, message: '请输入身份证!'},
          ]},
          sex: {rules: [
            {required: true, message: '请输入性别!'},
          ]},
          phone: {rules: [
            {pattern:/^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},
          ]},
          rylx: {rules: [
          ]},
          jtqk: {rules: [
          ]},
          jhrSfz: {rules: [
          ]},
          jhrName: {rules: [
          ]},
          jhrGx: {rules: [
          ]},
          jhrPhone: {rules: [
            {pattern:/^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},
          ]},
          jhrZz: {rules: [
          ]},
          sfwffz: {rules: [
          ]},
          wffzqk: {rules: [
          ]},
          bfrName: {rules: [
          ]},
          bfrPhone: {rules: [
            {pattern:/^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},
          ]},
          bfsd: {rules: [
          ]},
          bfqk: {rules: [
          ]},
        },
        url: {
          add: "/zdqsn/zzZdqsn/add",
          edit: "/zdqsn/zzZdqsn/edit",
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
          this.form.setFieldsValue(pick(this.model,'name','sfz','sex','phone','rylx','jtqk','jhrSfz','jhrName','jhrGx','jhrPhone','jhrZz','sfwffz','wffzqk','bfrName','bfrPhone','bfsd','bfqk'))
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
        this.form.setFieldsValue(pick(row,'name','sfz','sex','phone','rylx','jtqk','jhrSfz','jhrName','jhrGx','jhrPhone','jhrZz','sfwffz','wffzqk','bfrName','bfrPhone','bfsd','bfqk'))
      }
      
    }
  }
</script>

<style lang="less" scoped>
/** Button按钮间距 */
  .ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
  }
</style>