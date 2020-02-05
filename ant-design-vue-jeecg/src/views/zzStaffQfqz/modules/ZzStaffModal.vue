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

        <a-form-item label="群防群治组织" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['zzOrg', validatorRules.zzOrg]" :trigger-change="true" dictCode="zz_qfqz,org_name,id" placeholder="请选择群防群治组织"/>
        </a-form-item>
        <a-form-item label="姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入姓名"></a-input>
        </a-form-item>
        <a-form-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="radio" v-decorator="['sex', validatorRules.sex]" :trigger-change="true" dictCode="sex" placeholder="请选择性别"/>
        </a-form-item>
        <a-form-item label="手机号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'phone', validatorRules.phone]" placeholder="请输入手机号"></a-input>
        </a-form-item>
        <!--<a-form-item label="人员类型" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--<j-dict-select-tag type="list" v-decorator="['type', validatorRules.type]" :trigger-change="true" dictCode="" placeholder="请选择人员类型"/>-->
        <!--</a-form-item>-->
        <a-form-item label="民族" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['minZu', validatorRules.minZu]" :trigger-change="true" dictCode="minzu" placeholder="请选择民族"/>
        </a-form-item>
        <a-form-item label="政治面貌" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['zzmm', validatorRules.zzmm]" :trigger-change="true" dictCode="zzmm" placeholder="请选择政治面貌"/>
        </a-form-item>
        <a-form-item label="身份证" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sfz', validatorRules.sfz]" placeholder="请输入身份证"></a-input>
        </a-form-item>
        <a-form-item label="生日" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择生日" v-decorator="[ 'birthday', validatorRules.birthday]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="级别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['lv', validatorRules.lv]" :trigger-change="true" dictCode="staff_lv" placeholder="请选择级别"/>
        </a-form-item>
        <a-form-item label="特长" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['speciality', validatorRules.speciality]" rows="4" placeholder="请输入特长"/>
        </a-form-item>
        <a-form-item label="职务" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['job', validatorRules.job]" :trigger-change="true" dictCode="staff_job" placeholder="请选择职务"/>
        </a-form-item>
        <a-form-item label="学历" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['education', validatorRules.education]" :trigger-change="true" dictCode="education" placeholder="请选择学历"/>
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
    name: "ZzStaffModal",
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
          zzOrg: {rules: [
          ]},
          name: {rules: [
            {required: true, message: '请输入姓名!'},
          ]},
          sex: {rules: [
            {required: true, message: '请输入性别!'},
          ]},
          phone: {rules: [
            {required: true, message: '请输入手机号!'},
            {pattern:/^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},
          ]},
          type: {rules: [
          ]},
          minZu: {rules: [
          ]},
          zzmm: {rules: [
          ]},
          sfz: {rules: [
          ]},
          birthday: {rules: [
          ]},
          lv: {rules: [
          ]},
          speciality: {rules: [
          ]},
          job: {rules: [
          ]},
          education: {rules: [
          ]},
        },
        url: {
          add: "/aaa/zzStaff/add",
          edit: "/aaa/zzStaff/edit",
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
          this.form.setFieldsValue(pick(this.model,'zzOrg','name','sex','phone','type','minZu','zzmm','sfz','birthday','lv','speciality','job','education'))
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
        this.form.setFieldsValue(pick(row,'zzOrg','name','sex','phone','type','minZu','zzmm','sfz','birthday','lv','speciality','job','education'))
      },

      
    }
  }
</script>