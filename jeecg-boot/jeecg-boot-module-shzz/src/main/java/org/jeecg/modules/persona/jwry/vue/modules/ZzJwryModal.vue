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

        <a-form-item label="外文姓" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'wyx', validatorRules.wyx]" placeholder="请输入外文姓"></a-input>
        </a-form-item>
        <a-form-item label="外文名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'wym', validatorRules.wym]" placeholder="请输入外文名"></a-input>
        </a-form-item>
        <a-form-item label="中文姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zwxm', validatorRules.zwxm]" placeholder="请输入中文姓名"></a-input>
        </a-form-item>
        <a-form-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="radio" v-decorator="['sex', validatorRules.sex]" :trigger-change="true" dictCode="sex" placeholder="请选择性别"/>
        </a-form-item>
        <a-form-item label="生日" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择生日" v-decorator="[ 'birthday', validatorRules.birthday]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="国籍" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'gj', validatorRules.gj]" placeholder="请输入国籍"></a-input>
        </a-form-item>
        <a-form-item label="宗教信仰" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zjxy', validatorRules.zjxy]" placeholder="请输入宗教信仰"></a-input>
        </a-form-item>
        <a-form-item label="证件类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['zjlx', validatorRules.zjlx]" :trigger-change="true" dictCode="zjlx" placeholder="请选择证件类型"/>
        </a-form-item>
        <a-form-item label="证件号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zjh', validatorRules.zjh]" placeholder="请输入证件号"></a-input>
        </a-form-item>
        <a-form-item label="证件有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择证件有效期" v-decorator="[ 'zjyxq', validatorRules.zjyxq]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'lxfs', validatorRules.lxfs]" placeholder="请输入联系方式"></a-input>
        </a-form-item>
        <a-form-item label="来华目的" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'lhmd', validatorRules.lhmd]" placeholder="请输入来华目的"></a-input>
        </a-form-item>
        <a-form-item label="职业类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zylb', validatorRules.zylb]" placeholder="请输入职业类别"></a-input>
        </a-form-item>
        <a-form-item label="职业" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zy', validatorRules.zy]" placeholder="请输入职业"></a-input>
        </a-form-item>
        <a-form-item label="服务处所" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fwcs', validatorRules.fwcs]" placeholder="请输入服务处所"></a-input>
        </a-form-item>
        <a-form-item label="现住地" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'xzd', validatorRules.xzd]" placeholder="请输入现住地"></a-input>
        </a-form-item>
        <a-form-item label="现住门（楼）详" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'xzm', validatorRules.xzm]" placeholder="请输入现住门（楼）详"></a-input>
        </a-form-item>
        <a-form-item label="抵达日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择抵达日期" v-decorator="[ 'ddDate', validatorRules.ddDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="预计离开日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择预计离开日期" v-decorator="[ 'yjlkDate', validatorRules.yjlkDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="是否重点关注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sfZdgz', validatorRules.sfZdgz]" placeholder="请输入是否重点关注"></a-input>
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
    name: "ZzJwryModal",
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
          wyx: {rules: [
            {required: true, message: '请输入外文姓!'},
          ]},
          wym: {rules: [
            {required: true, message: '请输入外文名!'},
          ]},
          zwxm: {rules: [
            {required: true, message: '请输入中文姓名!'},
          ]},
          sex: {rules: [
          ]},
          birthday: {rules: [
          ]},
          gj: {rules: [
          ]},
          zjxy: {rules: [
          ]},
          zjlx: {rules: [
          ]},
          zjh: {rules: [
          ]},
          zjyxq: {rules: [
          ]},
          lxfs: {rules: [
          ]},
          lhmd: {rules: [
          ]},
          zylb: {rules: [
          ]},
          zy: {rules: [
          ]},
          fwcs: {rules: [
          ]},
          xzd: {rules: [
          ]},
          xzm: {rules: [
          ]},
          ddDate: {rules: [
          ]},
          yjlkDate: {rules: [
          ]},
          sfZdgz: {rules: [
          ]},
        },
        url: {
          add: "/jwry/zzJwry/add",
          edit: "/jwry/zzJwry/edit",
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
          this.form.setFieldsValue(pick(this.model,'wyx','wym','zwxm','sex','birthday','gj','zjxy','zjlx','zjh','zjyxq','lxfs','lhmd','zylb','zy','fwcs','xzd','xzm','ddDate','yjlkDate','sfZdgz'))
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
        this.form.setFieldsValue(pick(row,'wyx','wym','zwxm','sex','birthday','gj','zjxy','zjlx','zjh','zjyxq','lxfs','lhmd','zylb','zy','fwcs','xzd','xzm','ddDate','yjlkDate','sfZdgz'))
      },

      
    }
  }
</script>