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
        <a-form-item label="家庭经济状况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'jtjjzk', validatorRules.jtjjzk]" placeholder="请输入家庭经济状况"></a-input>
        </a-form-item>
        <a-form-item label="是否纳入低保" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sfnrdb', validatorRules.sfnrdb]" placeholder="请输入是否纳入低保"></a-input>
        </a-form-item>
        <a-form-item label="监护人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'jhrName', validatorRules.jhrName]" placeholder="请输入监护人姓名"></a-input>
        </a-form-item>
        <a-form-item label="监护人身份证" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'jhrShz', validatorRules.jhrShz]" placeholder="请输入监护人身份证"></a-input>
        </a-form-item>
        <a-form-item label="监护人电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'jhrPhone', validatorRules.jhrPhone]" placeholder="请输入监护人电话"></a-input>
        </a-form-item>
        <a-form-item label="初次发病日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择初次发病日期" v-decorator="[ 'ccfbDate', validatorRules.ccfbDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="目前诊断类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'mqzdlx', validatorRules.mqzdlx]" placeholder="请输入目前诊断类型"></a-input>
        </a-form-item>
        <a-form-item label="有无肇事肇祸" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'ywzszh', validatorRules.ywzszh]" placeholder="请输入有无肇事肇祸"></a-input>
        </a-form-item>
        <a-form-item label="肇事肇祸次数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zszhcs', validatorRules.zszhcs]" placeholder="请输入肇事肇祸次数"></a-input>
        </a-form-item>
        <a-form-item label="上次肇事肇祸日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择上次肇事肇祸日期" v-decorator="[ 'sczhrq', validatorRules.sczhrq]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="危险评估登记" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'wxpgdj', validatorRules.wxpgdj]" placeholder="请输入危险评估登记"></a-input>
        </a-form-item>
        <a-form-item label="治疗情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zlqk', validatorRules.zlqk]" placeholder="请输入治疗情况"></a-input>
        </a-form-item>
        <a-form-item label="治疗医院名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zlyymc', validatorRules.zlyymc]" placeholder="请输入治疗医院名称"></a-input>
        </a-form-item>
        <a-form-item label="住院治疗原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zyzlyy', validatorRules.zyzlyy]" placeholder="请输入住院治疗原因"></a-input>
        </a-form-item>
        <a-form-item label="康复训练机构" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'kfxljg', validatorRules.kfxljg]" placeholder="请输入康复训练机构"></a-input>
        </a-form-item>
        <a-form-item label="参与管理人员" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'cyglry', validatorRules.cyglry]" placeholder="请输入参与管理人员"></a-input>
        </a-form-item>
        <a-form-item label="帮扶情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bfqk', validatorRules.bfqk]" placeholder="请输入帮扶情况"></a-input>
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
    name: "ZzJsbryModal",
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
          jtjjzk: {rules: [
          ]},
          sfnrdb: {rules: [
          ]},
          jhrName: {rules: [
          ]},
          jhrShz: {rules: [
          ]},
          jhrPhone: {rules: [
            {pattern:/^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},
          ]},
          ccfbDate: {rules: [
          ]},
          mqzdlx: {rules: [
          ]},
          ywzszh: {rules: [
          ]},
          zszhcs: {rules: [
          ]},
          sczhrq: {rules: [
          ]},
          wxpgdj: {rules: [
          ]},
          zlqk: {rules: [
          ]},
          zlyymc: {rules: [
          ]},
          zyzlyy: {rules: [
          ]},
          kfxljg: {rules: [
          ]},
          cyglry: {rules: [
          ]},
          bfqk: {rules: [
          ]},
        },
        url: {
          add: "/jsbry/zzJsbry/add",
          edit: "/jsbry/zzJsbry/edit",
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
          this.form.setFieldsValue(pick(this.model,'name','sfz','sex','phone','jtjjzk','sfnrdb','jhrName','jhrShz','jhrPhone','ccfbDate','mqzdlx','ywzszh','zszhcs','sczhrq','wxpgdj','zlqk','zlyymc','zyzlyy','kfxljg','cyglry','bfqk'))
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
        this.form.setFieldsValue(pick(row,'name','sfz','sex','phone','jtjjzk','sfnrdb','jhrName','jhrShz','jhrPhone','ccfbDate','mqzdlx','ywzszh','zszhcs','sczhrq','wxpgdj','zlqk','zlyymc','zyzlyy','kfxljg','cyglry','bfqk'))
      },

      
    }
  }
</script>