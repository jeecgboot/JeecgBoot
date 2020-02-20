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

        <a-form-item label="房主" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fz', validatorRules.fz]" placeholder="请输入房主"></a-input>
        </a-form-item>
        <a-form-item label="身份证号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sfzh', validatorRules.sfzh]" placeholder="请输入身份证号"></a-input>
        </a-form-item>
        <a-form-item label="房屋编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fwbh', validatorRules.fwbh]" placeholder="请输入房屋编号"></a-input>
        </a-form-item>
        <a-form-item label="房屋类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fwlb', validatorRules.fwlb]" placeholder="请输入房屋类别"></a-input>
        </a-form-item>
        <a-form-item label="房屋性质" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fwxz', validatorRules.fwxz]" placeholder="请输入房屋性质"></a-input>
        </a-form-item>
        <a-form-item label="房屋用途" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fwyt', validatorRules.fwyt]" placeholder="请输入房屋用途"></a-input>
        </a-form-item>
        <a-form-item label="使用形式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'syxs', validatorRules.syxs]" placeholder="请输入使用形式"></a-input>
        </a-form-item>
        <a-form-item label="室内面积(m2)" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'snmj', validatorRules.snmj]" placeholder="请输入室内面积(m2)" style="width: 100%"/>
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
  
  export default {
    name: "ZzFwxxModal",
    components: { 
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
          fz: {rules: [
            {required: true, message: '请输入房主!'},
          ]},
          sfzh: {rules: [
            {required: true, message: '请输入身份证号!'},
          ]},
          fwbh: {rules: [
            {required: true, message: '请输入房屋编号!'},
          ]},
          fwlb: {rules: [
          ]},
          fwxz: {rules: [
          ]},
          fwyt: {rules: [
          ]},
          syxs: {rules: [
          ]},
          snmj: {rules: [
          ]},
        },
        url: {
          add: "/fwxx/zzFwxx/add",
          edit: "/fwxx/zzFwxx/edit",
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
          this.form.setFieldsValue(pick(this.model,'fz','sfzh','fwbh','fwlb','fwxz','fwyt','syxs','snmj'))
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
        this.form.setFieldsValue(pick(row,'fz','sfzh','fwbh','fwlb','fwxz','fwyt','syxs','snmj'))
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