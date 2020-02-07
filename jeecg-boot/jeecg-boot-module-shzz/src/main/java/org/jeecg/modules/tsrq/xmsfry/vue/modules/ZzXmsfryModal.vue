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
        <a-form-item label="是否累犯" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sflf', validatorRules.sflf]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否累犯"/>
        </a-form-item>
        <a-form-item label="原罪名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['yzm', validatorRules.yzm]" :trigger-change="true" dictCode="yzm" placeholder="请选择原罪名"/>
        </a-form-item>
        <a-form-item label="原判刑期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'ypxq', validatorRules.ypxq]" placeholder="请输入原判刑期"></a-input>
        </a-form-item>
        <a-form-item label="服刑场所" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fxcs', validatorRules.fxcs]" placeholder="请输入服刑场所"></a-input>
        </a-form-item>
        <a-form-item label="未安置原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'wazyy', validatorRules.wazyy]" placeholder="请输入未安置原因"></a-input>
        </a-form-item>
        <a-form-item label="帮教情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bjqk', validatorRules.bjqk]" placeholder="请输入帮教情况"></a-input>
        </a-form-item>
        <a-form-item label="是否重新犯罪" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfcxfz', validatorRules.sfcxfz]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否重新犯罪"/>
        </a-form-item>
        <a-form-item label="重新犯罪罪名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'cxfzzm', validatorRules.cxfzzm]" placeholder="请输入重新犯罪罪名"></a-input>
        </a-form-item>
        <a-form-item label="释放日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择释放日期" v-decorator="[ 'sfDate', validatorRules.sfDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="危险评估类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['wxpglx', validatorRules.wxpglx]" :trigger-change="true" dictCode="wxpglx" placeholder="请选择危险评估类型"/>
        </a-form-item>
        <a-form-item label="衔接日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择衔接日期" v-decorator="[ 'xjDate', validatorRules.xjDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="衔接情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['xjqk', validatorRules.xjqk]" :trigger-change="true" dictCode="xjqk" placeholder="请选择衔接情况"/>
        </a-form-item>
        <a-form-item label="安置日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择安置日期" v-decorator="[ 'azDate', validatorRules.azDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="安置情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['azqk', validatorRules.azqk]" :trigger-change="true" dictCode="azqk" placeholder="请选择安置情况"/>
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
    name: "ZzXmsfryModal",
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
          ]},
          sflf: {rules: [
          ]},
          yzm: {rules: [
          ]},
          ypxq: {rules: [
          ]},
          fxcs: {rules: [
          ]},
          wazyy: {rules: [
          ]},
          bjqk: {rules: [
          ]},
          sfcxfz: {rules: [
          ]},
          cxfzzm: {rules: [
          ]},
          sfDate: {rules: [
          ]},
          wxpglx: {rules: [
          ]},
          xjDate: {rules: [
          ]},
          xjqk: {rules: [
          ]},
          azDate: {rules: [
          ]},
          azqk: {rules: [
          ]},
        },
        url: {
          add: "/xmsfry/zzXmsfry/add",
          edit: "/xmsfry/zzXmsfry/edit",
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
          this.form.setFieldsValue(pick(this.model,'name','sfz','sex','phone','sflf','yzm','ypxq','fxcs','wazyy','bjqk','sfcxfz','cxfzzm','sfDate','wxpglx','xjDate','xjqk','azDate','azqk'))
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
        this.form.setFieldsValue(pick(row,'name','sfz','sex','phone','sflf','yzm','ypxq','fxcs','wazyy','bjqk','sfcxfz','cxfzzm','sfDate','wxpglx','xjDate','xjqk','azDate','azqk'))
      },

      
    }
  }
</script>