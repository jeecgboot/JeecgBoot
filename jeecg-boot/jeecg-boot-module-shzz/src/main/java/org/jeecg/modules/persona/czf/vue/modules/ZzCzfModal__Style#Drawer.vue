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

        <a-form-item label="房屋编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fwCode', validatorRules.fwCode]" placeholder="请输入房屋编号"></a-input>
        </a-form-item>
        <a-form-item label="房屋地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fwAddr', validatorRules.fwAddr]" placeholder="请输入房屋地址"></a-input>
        </a-form-item>
        <a-form-item label="建筑用途" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['jzyt', validatorRules.jzyt]" :trigger-change="true" dictCode="" placeholder="请选择建筑用途"/>
        </a-form-item>
        <a-form-item label="建筑面积" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'jzmj', validatorRules.jzmj]" placeholder="请输入建筑面积"></a-input>
        </a-form-item>
        <a-form-item label="房主姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fzName', validatorRules.fzName]" placeholder="请输入房主姓名"></a-input>
        </a-form-item>
        <a-form-item label="证件类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['zjlx', validatorRules.zjlx]" :trigger-change="true" dictCode="zjlx" placeholder="请选择证件类型"/>
        </a-form-item>
        <a-form-item label="证件号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zjh', validatorRules.zjh]" placeholder="请输入证件号"></a-input>
        </a-form-item>
        <a-form-item label="房主联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fzLxfs', validatorRules.fzLxfs]" placeholder="请输入房主联系方式"></a-input>
        </a-form-item>
        <a-form-item label="房主现住详址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fzAddr', validatorRules.fzAddr]" placeholder="请输入房主现住详址"></a-input>
        </a-form-item>
        <a-form-item label="出租用途" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['czyt', validatorRules.czyt]" :trigger-change="true" dictCode="czyt" placeholder="请选择出租用途"/>
        </a-form-item>
        <a-form-item label="隐患类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['yhlx', validatorRules.yhlx]" :trigger-change="true" dictCode="yhlx" placeholder="请选择隐患类型"/>
        </a-form-item>
        <a-form-item label="承租人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'czrName', validatorRules.czrName]" placeholder="请输入承租人姓名"></a-input>
        </a-form-item>
        <a-form-item label="承租人身份证" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'czrSfz', validatorRules.czrSfz]" placeholder="请输入承租人身份证"></a-input>
        </a-form-item>
        <a-form-item label="承租人联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'czrLxfs', validatorRules.czrLxfs]" placeholder="请输入承租人联系方式"></a-input>
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
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  
  export default {
    name: "ZzCzfModal",
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
          fwCode: {rules: [
            {required: true, message: '请输入房屋编号!'},
          ]},
          fwAddr: {rules: [
            {required: true, message: '请输入房屋地址!'},
          ]},
          jzyt: {rules: [
          ]},
          jzmj: {rules: [
          ]},
          fzName: {rules: [
            {required: true, message: '请输入房主姓名!'},
          ]},
          zjlx: {rules: [
          ]},
          zjh: {rules: [
          ]},
          fzLxfs: {rules: [
          ]},
          fzAddr: {rules: [
          ]},
          czyt: {rules: [
          ]},
          yhlx: {rules: [
          ]},
          czrName: {rules: [
          ]},
          czrSfz: {rules: [
          ]},
          czrLxfs: {rules: [
          ]},
          longitude: {rules: [
          ]},
          latitude: {rules: [
          ]},
        },
        url: {
          add: "/czf/zzCzf/add",
          edit: "/czf/zzCzf/edit",
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
          this.form.setFieldsValue(pick(this.model,'fwCode','fwAddr','jzyt','jzmj','fzName','zjlx','zjh','fzLxfs','fzAddr','czyt','yhlx','czrName','czrSfz','czrLxfs','longitude','latitude'))
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
        this.form.setFieldsValue(pick(row,'fwCode','fwAddr','jzyt','jzmj','fzName','zjlx','zjh','fzLxfs','fzAddr','czyt','yhlx','czrName','czrSfz','czrLxfs','longitude','latitude'))
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