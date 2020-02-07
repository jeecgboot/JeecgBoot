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

        <a-form-item label="实施地区" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'ssdq', validatorRules.ssdq]" placeholder="请输入实施地区"></a-input>
        </a-form-item>
        <a-form-item label="被实施地区层级" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['bssdqLevel', validatorRules.bssdqLevel]" :trigger-change="true" dictCode="org_level" placeholder="请选择被实施地区层级"/>
        </a-form-item>
        <a-form-item label="被实施主体名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bssztName', validatorRules.bssztName]" placeholder="请输入被实施主体名称"></a-input>
        </a-form-item>
        <a-form-item label="被实施主体层级" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['bssztLevel', validatorRules.bssztLevel]" :trigger-change="true" dictCode="org_level" placeholder="请选择被实施主体层级"/>
        </a-form-item>
        <a-form-item label="政策种类" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['zczl', validatorRules.zczl]" :trigger-change="true" dictCode="policy_class" placeholder="请选择政策种类"/>
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
    name: "ZzLdzrzModal",
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
          ssdq: {rules: [
            {required: true, message: '请输入实施地区!'},
          ]},
          bssdqLevel: {rules: [
            {required: true, message: '请输入被实施地区层级!'},
          ]},
          bssztName: {rules: [
            {required: true, message: '请输入被实施主体名称!'},
          ]},
          bssztLevel: {rules: [
            {required: true, message: '请输入被实施主体层级!'},
          ]},
          zczl: {rules: [
            {required: true, message: '请输入政策种类!'},
          ]},
        },
        url: {
          add: "/ldzrz/zzLdzrz/add",
          edit: "/ldzrz/zzLdzrz/edit",
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
          this.form.setFieldsValue(pick(this.model,'ssdq','bssdqLevel','bssztName','bssztLevel','zczl'))
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
        this.form.setFieldsValue(pick(row,'ssdq','bssdqLevel','bssztName','bssztLevel','zczl'))
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