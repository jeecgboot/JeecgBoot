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

        <a-form-item label="事件名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'eventName', validatorRules.eventName]" placeholder="请输入事件名称"></a-input>
        </a-form-item>
        <a-form-item label="发生日期	" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择发生日期	" v-decorator="[ 'fsDate', validatorRules.fsDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="发生地点" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fsdd', validatorRules.fsdd]" placeholder="请输入发生地点"></a-input>
        </a-form-item>
        <a-form-item label="事件规模" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sjgm', validatorRules.sjgm]" :trigger-change="true" dictCode="sjgm" placeholder="请选择事件规模"/>
        </a-form-item>
        <a-form-item label="事件类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sjlb', validatorRules.sjlb]" :trigger-change="true" dictCode="sjlb" placeholder="请选择事件类别"/>
        </a-form-item>
        <a-form-item label="涉及人数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'sjrs', validatorRules.sjrs]" placeholder="请输入涉及人数" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="事件简述	" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sjjs', validatorRules.sjjs]" placeholder="请输入事件简述	"></a-input>
        </a-form-item>
        <a-form-item label="涉及单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sjdw', validatorRules.sjdw]" placeholder="请输入涉及单位"></a-input>
        </a-form-item>
        <a-form-item label="主要当事人证件代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zydsrZjdm', validatorRules.zydsrZjdm]" placeholder="请输入主要当事人证件代码"></a-input>
        </a-form-item>
        <a-form-item label="主要当事人证件号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zydsrZjhm', validatorRules.zydsrZjhm]" placeholder="请输入主要当事人证件号码"></a-input>
        </a-form-item>
        <a-form-item label="主要当事人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zydsrName', validatorRules.zydsrName]" placeholder="请输入主要当事人姓名"></a-input>
        </a-form-item>
        <a-form-item label="主要当事人性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="radio" v-decorator="['zydsrSex', validatorRules.zydsrSex]" :trigger-change="true" dictCode="sex" placeholder="请选择主要当事人性别"/>
        </a-form-item>
        <a-form-item label="主要当事人民族" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['zydsrMz', validatorRules.zydsrMz]" :trigger-change="true" dictCode="minzu" placeholder="请选择主要当事人民族"/>
        </a-form-item>
        <a-form-item label="主要当事人学历" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['zydsrXl', validatorRules.zydsrXl]" :trigger-change="true" dictCode="education" placeholder="请选择主要当事人学历"/>
        </a-form-item>
        <a-form-item label="主要当事人人员类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['zydsrRylb', validatorRules.zydsrRylb]" :trigger-change="true" dictCode="dsrrylb" placeholder="请选择主要当事人人员类别"/>
        </a-form-item>
        <a-form-item label="主要当事人居住详址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zydsrAdress', validatorRules.zydsrAdress]" placeholder="请输入主要当事人居住详址"></a-input>
        </a-form-item>
        <a-form-item label="化解时限" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择化解时限" v-decorator="[ 'hjsx', validatorRules.hjsx]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="化解方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['hjfs', validatorRules.hjfs]" :trigger-change="true" dictCode="hjfs" placeholder="请选择化解方式"/>
        </a-form-item>
        <a-form-item label="化解组织" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'hjzz', validatorRules.hjzz]" placeholder="请输入化解组织"></a-input>
        </a-form-item>
        <a-form-item label="化解责任人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'hjzrrName', validatorRules.hjzrrName]" placeholder="请输入化解责任人姓名"></a-input>
        </a-form-item>
        <a-form-item label="化解责任人联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'hjzrrPhone', validatorRules.hjzrrPhone]" placeholder="请输入化解责任人联系方式"></a-input>
        </a-form-item>
        <a-form-item label="化解是否成功" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfHjcg', validatorRules.sfHjcg]" :trigger-change="true" dictCode="yes_no" placeholder="请选择化解是否成功"/>
        </a-form-item>
        <a-form-item label="化解情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'hjqk', validatorRules.hjqk]" placeholder="请输入化解情况"></a-input>
        </a-form-item>
        <a-form-item label="考评日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择考评日期" v-decorator="[ 'kpDate', validatorRules.kpDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="考评意见" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'kpyj', validatorRules.kpyj]" placeholder="请输入考评意见"></a-input>
        </a-form-item>
        <a-form-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'longitude', validatorRules.longitude]" placeholder="请输入经度"></a-input>
        </a-form-item>
        <a-form-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'latitude', validatorRules.latitude]" placeholder="请输入纬度"></a-input>
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
  import JDate from '@/components/jeecg/JDate'  
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  
  export default {
    name: "ZzMdjfpcModal",
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
            {required: true, message: '请输入事件名称!'},
          ]},
          fsDate: {rules: [
            {required: true, message: '请输入发生日期	!'},
          ]},
          fsdd: {rules: [
            {required: true, message: '请输入发生地点!'},
          ]},
          sjgm: {rules: [
            {required: true, message: '请输入事件规模!'},
          ]},
          sjlb: {rules: [
            {required: true, message: '请输入事件类别!'},
          ]},
          sjrs: {rules: [
          ]},
          sjjs: {rules: [
          ]},
          sjdw: {rules: [
          ]},
          zydsrZjdm: {rules: [
          ]},
          zydsrZjhm: {rules: [
          ]},
          zydsrName: {rules: [
          ]},
          zydsrSex: {rules: [
          ]},
          zydsrMz: {rules: [
          ]},
          zydsrXl: {rules: [
          ]},
          zydsrRylb: {rules: [
          ]},
          zydsrAdress: {rules: [
          ]},
          hjsx: {rules: [
          ]},
          hjfs: {rules: [
          ]},
          hjzz: {rules: [
          ]},
          hjzrrName: {rules: [
          ]},
          hjzrrPhone: {rules: [
          ]},
          sfHjcg: {rules: [
          ]},
          hjqk: {rules: [
          ]},
          kpDate: {rules: [
          ]},
          kpyj: {rules: [
          ]},
          longitude: {rules: [
          ]},
          latitude: {rules: [
          ]},
        },
        url: {
          add: "/mdjfpc/zzMdjfpc/add",
          edit: "/mdjfpc/zzMdjfpc/edit",
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
          this.form.setFieldsValue(pick(this.model,'eventName','fsDate','fsdd','sjgm','sjlb','sjrs','sjjs','sjdw','zydsrZjdm','zydsrZjhm','zydsrName','zydsrSex','zydsrMz','zydsrXl','zydsrRylb','zydsrAdress','hjsx','hjfs','hjzz','hjzrrName','hjzrrPhone','sfHjcg','hjqk','kpDate','kpyj','longitude','latitude'))
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
        this.form.setFieldsValue(pick(row,'eventName','fsDate','fsdd','sjgm','sjlb','sjrs','sjjs','sjdw','zydsrZjdm','zydsrZjhm','zydsrName','zydsrSex','zydsrMz','zydsrXl','zydsrRylb','zydsrAdress','hjsx','hjfs','hjzz','hjzrrName','hjzrrPhone','sfHjcg','hjqk','kpDate','kpyj','longitude','latitude'))
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