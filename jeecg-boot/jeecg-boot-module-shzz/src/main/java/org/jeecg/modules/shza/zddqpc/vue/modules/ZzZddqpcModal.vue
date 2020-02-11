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

        <a-form-item label="重点地区名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zddqName', validatorRules.zddqName]" placeholder="请输入重点地区名称"></a-input>
        </a-form-item>
        <a-form-item label="治安突出问题" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['zatcwt', validatorRules.zatcwt]" :trigger-change="true" dictCode="zatcwt" placeholder="请选择治安突出问题"/>
        </a-form-item>
        <a-form-item label="涉及区域类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sjqyType', validatorRules.sjqyType]" :trigger-change="true" dictCode="sjqylx" placeholder="请选择涉及区域类型"/>
        </a-form-item>
        <a-form-item label="涉及区域范围" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sjqyfw', validatorRules.sjqyfw]" placeholder="请输入涉及区域范围"></a-input>
        </a-form-item>
        <a-form-item label="整治牵头单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zzqtdw', validatorRules.zzqtdw]" placeholder="请输入整治牵头单位"></a-input>
        </a-form-item>
        <a-form-item label="单位负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'dwfzr', validatorRules.dwfzr]" placeholder="请输入单位负责人"></a-input>
        </a-form-item>
        <a-form-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'phone', validatorRules.phone]" placeholder="请输入联系方式"></a-input>
        </a-form-item>
        <a-form-item label="整治参与单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zzcydw', validatorRules.zzcydw]" placeholder="请输入整治参与单位"></a-input>
        </a-form-item>
        <a-form-item label="整改时限" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择整改时限" v-decorator="[ 'zgsx', validatorRules.zgsx]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="期间破获刑事案件数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'phxs', validatorRules.phxs]" placeholder="请输入期间破获刑事案件数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="期间破获治安案件数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'phza', validatorRules.phza]" placeholder="请输入期间破获治安案件数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="效果评估" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['xgpg', validatorRules.xgpg]" :trigger-change="true" dictCode="pgxg" placeholder="请选择效果评估"/>
        </a-form-item>
        <a-form-item label="整治情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zzqk', validatorRules.zzqk]" placeholder="请输入整治情况"></a-input>
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
    name: "ZzZddqpcModal",
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
          zddqName: {rules: [
            {required: true, message: '请输入重点地区名称!'},
          ]},
          zatcwt: {rules: [
            {required: true, message: '请输入治安突出问题!'},
          ]},
          sjqyType: {rules: [
            {required: true, message: '请输入涉及区域类型!'},
          ]},
          sjqyfw: {rules: [
          ]},
          zzqtdw: {rules: [
            {required: true, message: '请输入整治牵头单位!'},
          ]},
          dwfzr: {rules: [
          ]},
          phone: {rules: [
          ]},
          zzcydw: {rules: [
            {required: true, message: '请输入整治参与单位!'},
          ]},
          zgsx: {rules: [
          ]},
          phxs: {rules: [
          ]},
          phza: {rules: [
          ]},
          xgpg: {rules: [
          ]},
          zzqk: {rules: [
          ]},
        },
        url: {
          add: "/zddqpc/zzZddqpc/add",
          edit: "/zddqpc/zzZddqpc/edit",
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
          this.form.setFieldsValue(pick(this.model,'zddqName','zatcwt','sjqyType','sjqyfw','zzqtdw','dwfzr','phone','zzcydw','zgsx','phxs','phza','xgpg','zzqk'))
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
        this.form.setFieldsValue(pick(row,'zddqName','zatcwt','sjqyType','sjqyfw','zzqtdw','dwfzr','phone','zzcydw','zgsx','phxs','phza','xgpg','zzqk'))
      },

      
    }
  }
</script>