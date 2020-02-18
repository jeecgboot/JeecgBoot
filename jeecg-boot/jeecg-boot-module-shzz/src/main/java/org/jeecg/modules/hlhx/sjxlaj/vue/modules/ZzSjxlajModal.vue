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

        <a-form-item label="案(事)件名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'eventName', validatorRules.eventName]" placeholder="请输入案(事)件名称"></a-input>
        </a-form-item>
        <a-form-item label="案(事)件编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'eventCode', validatorRules.eventCode]" placeholder="请输入案(事)件编号"></a-input>
        </a-form-item>
        <a-form-item label="发生日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择发生日期" v-decorator="[ 'fsDate', validatorRules.fsDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="发生地点" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fsAdress', validatorRules.fsAdress]" placeholder="请输入发生地点"></a-input>
        </a-form-item>
        <a-form-item label="案(事)件性质" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['eventXz', validatorRules.eventXz]" :trigger-change="true" dictCode="ajxz" placeholder="请选择案(事)件性质"/>
        </a-form-item>
        <a-form-item label="案(事)件情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'eventQk', validatorRules.eventQk]" placeholder="请输入案(事)件情况"></a-input>
        </a-form-item>
        <a-form-item label="主犯(嫌疑人)证件类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['zfZjdm', validatorRules.zfZjdm]" :trigger-change="true" dictCode="zjlx" placeholder="请选择主犯(嫌疑人)证件类型"/>
        </a-form-item>
        <a-form-item label="主犯(嫌疑人)证件号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zfZjhm', validatorRules.zfZjhm]" placeholder="请输入主犯(嫌疑人)证件号码"></a-input>
        </a-form-item>
        <a-form-item label="主犯(嫌疑人)姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zfName', validatorRules.zfName]" placeholder="请输入主犯(嫌疑人)姓名"></a-input>
        </a-form-item>
        <a-form-item label="是否破案" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfPa', validatorRules.sfPa]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否破案"/>
        </a-form-item>
        <a-form-item label="作案人数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'zaRs', validatorRules.zaRs]" placeholder="请输入作案人数" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="在逃人数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'ztRs', validatorRules.ztRs]" placeholder="请输入在逃人数" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="抓捕人数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'zbRs', validatorRules.zbRs]" placeholder="请输入抓捕人数" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="案件侦破情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'ajzpqk', validatorRules.ajzpqk]" placeholder="请输入案件侦破情况"></a-input>
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
    name: "ZzSjxlajModal",
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
          eventName: {rules: [
            {required: true, message: '请输入案(事)件名称!'},
          ]},
          eventCode: {rules: [
            {required: true, message: '请输入案(事)件编号!'},
          ]},
          fsDate: {rules: [
            {required: true, message: '请输入发生日期!'},
          ]},
          fsAdress: {rules: [
            {required: true, message: '请输入发生地点!'},
          ]},
          eventXz: {rules: [
            {required: true, message: '请输入案(事)件性质!'},
          ]},
          eventQk: {rules: [
          ]},
          zfZjdm: {rules: [
          ]},
          zfZjhm: {rules: [
          ]},
          zfName: {rules: [
          ]},
          sfPa: {rules: [
          ]},
          zaRs: {rules: [
          ]},
          ztRs: {rules: [
          ]},
          zbRs: {rules: [
          ]},
          ajzpqk: {rules: [
          ]},
        },
        url: {
          add: "/sjxlaj/zzSjxlaj/add",
          edit: "/sjxlaj/zzSjxlaj/edit",
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
          this.form.setFieldsValue(pick(this.model,'eventName','eventCode','fsDate','fsAdress','eventXz','eventQk','zfZjdm','zfZjhm','zfName','sfPa','zaRs','ztRs','zbRs','ajzpqk'))
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
        this.form.setFieldsValue(pick(row,'eventName','eventCode','fsDate','fsAdress','eventXz','eventQk','zfZjdm','zfZjhm','zfName','sfPa','zaRs','ztRs','zbRs','ajzpqk'))
      },

      
    }
  }
</script>