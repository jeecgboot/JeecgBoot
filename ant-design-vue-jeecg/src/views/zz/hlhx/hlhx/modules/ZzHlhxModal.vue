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

        <a-form-item label="线路名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'roadName', validatorRules.roadName]" placeholder="请输入线路名称"></a-input>
        </a-form-item>
        <a-form-item label="线路类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['roadType', validatorRules.roadType]" :trigger-change="true" dictCode="xllx" placeholder="请选择线路类型"/>
        </a-form-item>
        <a-form-item label="隶属单位名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'lsdwName', validatorRules.lsdwName]" placeholder="请输入隶属单位名称"></a-input>
        </a-form-item>
        <a-form-item label="隶属单位地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'lsdwAdress', validatorRules.lsdwAdress]" placeholder="请输入隶属单位地址"></a-input>
        </a-form-item>
        <a-form-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'lsdwPhone', validatorRules.lsdwPhone]" placeholder="请输入联系方式"></a-input>
        </a-form-item>
        <a-form-item label="隶属单位负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'lsdwFzr', validatorRules.lsdwFzr]" placeholder="请输入隶属单位负责人"></a-input>
        </a-form-item>
        <a-form-item label="负责人联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fzrPhone', validatorRules.fzrPhone]" placeholder="请输入负责人联系方式"></a-input>
        </a-form-item>
        <a-form-item label="管辖单位名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'gxdwName', validatorRules.gxdwName]" placeholder="请输入管辖单位名称"></a-input>
        </a-form-item>
        <a-form-item label="管辖单位地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'gxdwAdress', validatorRules.gxdwAdress]" placeholder="请输入管辖单位地址"></a-input>
        </a-form-item>
        <a-form-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'gxdwPhone', validatorRules.gxdwPhone]" placeholder="请输入联系方式"></a-input>
        </a-form-item>
        <a-form-item label="分管治保负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fgzbFzr', validatorRules.fgzbFzr]" placeholder="请输入分管治保负责人"></a-input>
        </a-form-item>
        <a-form-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fgzbPhone', validatorRules.fgzbPhone]" placeholder="请输入联系方式"></a-input>
        </a-form-item>
        <a-form-item label="治安隐患等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['zayhdj', validatorRules.zayhdj]" :trigger-change="true" dictCode="zayhdj" placeholder="请选择治安隐患等级"/>
        </a-form-item>
        <a-form-item label="治安隐患情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zayhqk', validatorRules.zayhqk]" placeholder="请输入治安隐患情况"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: "ZzHlhxModal",
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
          roadName: {rules: [
            {required: true, message: '请输入线路名称!'},
          ]},
          roadType: {rules: [
            {required: true, message: '请输入线路类型!'},
          ]},
          lsdwName: {rules: [
            {required: true, message: '请输入隶属单位名称!'},
          ]},
          lsdwAdress: {rules: [
          ]},
          lsdwPhone: {rules: [
          ]},
          lsdwFzr: {rules: [
          ]},
          fzrPhone: {rules: [
          ]},
          gxdwName: {rules: [
          ]},
          gxdwAdress: {rules: [
          ]},
          gxdwPhone: {rules: [
          ]},
          fgzbFzr: {rules: [
          ]},
          fgzbPhone: {rules: [
          ]},
          zayhdj: {rules: [
          ]},
          zayhqk: {rules: [
          ]},
        },
        url: {
          add: "/hlhx/zzHlhx/add",
          edit: "/hlhx/zzHlhx/edit",
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
          this.form.setFieldsValue(pick(this.model,'roadName','roadType','lsdwName','lsdwAdress','lsdwPhone','lsdwFzr','fzrPhone','gxdwName','gxdwAdress','gxdwPhone','fgzbFzr','fgzbPhone','zayhdj','zayhqk'))
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
        this.form.setFieldsValue(pick(row,'roadName','roadType','lsdwName','lsdwAdress','lsdwPhone','lsdwFzr','fzrPhone','gxdwName','gxdwAdress','gxdwPhone','fgzbFzr','fgzbPhone','zayhdj','zayhqk'))
      },

      
    }
  }
</script>