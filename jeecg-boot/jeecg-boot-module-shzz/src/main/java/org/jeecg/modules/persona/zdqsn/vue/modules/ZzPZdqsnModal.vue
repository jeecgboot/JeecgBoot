<template>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-row>

      <a-col :span="12">
        <a-form-item label="姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入姓名"></a-input>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="身份证" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sfz', validatorRules.sfz]" placeholder="请输入身份证"></a-input>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="曾用名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'oldName', validatorRules.oldName]" placeholder="请输入曾用名"></a-input>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="radio" v-decorator="['sex', validatorRules.sex]" :trigger-change="true" dictCode="sex" placeholder="请选择性别"/>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="照片" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['photo', validatorRules.photo]" :trigger-change="true"></j-upload>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="出生日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择出生日期" v-decorator="[ 'birthday', validatorRules.birthday]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="民族" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['zm', validatorRules.zm]" :trigger-change="true" dictCode="minzu" placeholder="请选择民族"/>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="籍贯" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'jg', validatorRules.jg]" placeholder="请输入籍贯"></a-input>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="婚姻状况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['hyzk', validatorRules.hyzk]" :trigger-change="true" dictCode="hyzk" placeholder="请选择婚姻状况"/>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="政治面貌" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['zzmm', validatorRules.zzmm]" :trigger-change="true" dictCode="zzmm" placeholder="请选择政治面貌"/>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="学历" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['xl', validatorRules.xl]" :trigger-change="true" dictCode="education" placeholder="请选择学历"/>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="宗教信仰" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zjxy', validatorRules.zjxy]" placeholder="请输入宗教信仰"></a-input>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="职业类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zylb', validatorRules.zylb]" placeholder="请输入职业类别"></a-input>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="职业" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zy', validatorRules.zy]" placeholder="请输入职业"></a-input>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="户籍门（楼）祥" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'hjxq', validatorRules.hjxq]" placeholder="请输入户籍门（楼）祥"></a-input>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="现住门（楼）祥" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'xzdxq', validatorRules.xzdxq]" placeholder="请输入现住门（楼）祥"></a-input>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'lxfs', validatorRules.lxfs]" placeholder="请输入联系方式"></a-input>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="人员类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['rylx', validatorRules.rylx]" :trigger-change="true" dictCode="rylx" placeholder="请选择人员类型"/>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="家庭情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['jtqk', validatorRules.jtqk]" :trigger-change="true" dictCode="jtqk" placeholder="请选择家庭情况"/>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="监护人身份证" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'jhrSfz', validatorRules.jhrSfz]" placeholder="请输入监护人身份证"></a-input>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="监护人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'jhrName', validatorRules.jhrName]" placeholder="请输入监护人姓名"></a-input>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="与监护人关系" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'jhrGx', validatorRules.jhrGx]" placeholder="请输入与监护人关系"></a-input>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="监护人电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'jhrPhone', validatorRules.jhrPhone]" placeholder="请输入监护人电话"></a-input>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="监护人住址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'jhrZz', validatorRules.jhrZz]" placeholder="请输入监护人住址"></a-input>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="是否违法犯罪" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfwffz', validatorRules.sfwffz]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否违法犯罪"/>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="违法犯罪情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'wffzqk', validatorRules.wffzqk]" placeholder="请输入违法犯罪情况"></a-input>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="帮扶人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bfrName', validatorRules.bfrName]" placeholder="请输入帮扶人姓名"></a-input>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="帮扶人电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bfrPhone', validatorRules.bfrPhone]" placeholder="请输入帮扶人电话"></a-input>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="帮扶手段" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['bfsd', validatorRules.bfsd]" :trigger-change="true" dictCode="bfsd" placeholder="请选择帮扶手段"/>
        </a-form-item>
      <a-col :span="12">
        <a-form-item label="帮扶情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bfqk', validatorRules.bfqk]" placeholder="请输入帮扶情况"></a-input>
        </a-form-item>
        </a-row>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  
  import JUpload from '@/components/jeecg/JUpload'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: "ZzPZdqsnModal",
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
          name: {rules: [
            {required: true, message: '请输入姓名!'},
          ]},
          sfz: {rules: [
          ]},
          oldName: {rules: [
          ]},
          sex: {rules: [
          ]},
          photo: {rules: [
          ]},
          birthday: {rules: [
          ]},
          zm: {rules: [
          ]},
          jg: {rules: [
          ]},
          hyzk: {rules: [
          ]},
          zzmm: {rules: [
          ]},
          xl: {rules: [
          ]},
          zjxy: {rules: [
          ]},
          zylb: {rules: [
          ]},
          zy: {rules: [
          ]},
          hjxq: {rules: [
          ]},
          xzdxq: {rules: [
          ]},
          lxfs: {rules: [
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
          add: "/zdqsn/zzPZdqsn/add",
          edit: "/zdqsn/zzPZdqsn/edit",
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
          this.form.setFieldsValue(pick(this.model,'name','sfz','oldName','sex','photo','birthday','zm','jg','hyzk','zzmm','xl','zjxy','zylb','zy','hjxq','xzdxq','lxfs','rylx','jtqk','jhrSfz','jhrName','jhrGx','jhrPhone','jhrZz','sfwffz','wffzqk','bfrName','bfrPhone','bfsd','bfqk'))
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
        this.form.setFieldsValue(pick(row,'name','sfz','oldName','sex','photo','birthday','zm','jg','hyzk','zzmm','xl','zjxy','zylb','zy','hjxq','xzdxq','lxfs','rylx','jtqk','jhrSfz','jhrName','jhrGx','jhrPhone','jhrZz','sfwffz','wffzqk','bfrName','bfrPhone','bfsd','bfqk'))
      },

      
    }
  }
</script>