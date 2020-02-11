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

        <a-form-item label="工商执照注册号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'gszzCode', validatorRules.gszzCode]" placeholder="请输入工商执照注册号"></a-input>
        </a-form-item>
        <a-form-item label="企业名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'businessName', validatorRules.businessName]" placeholder="请输入企业名称"></a-input>
        </a-form-item>
        <a-form-item label="所在地" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'adress', validatorRules.adress]" placeholder="请输入所在地"></a-input>
        </a-form-item>
        <a-form-item label="企业详址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'qyAdress', validatorRules.qyAdress]" placeholder="请输入企业详址"></a-input>
        </a-form-item>
        <a-form-item label="企业联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'qyPhone', validatorRules.qyPhone]" placeholder="请输入企业联系方式"></a-input>
        </a-form-item>
        <a-form-item label="企业负责人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'qyfzrName', validatorRules.qyfzrName]" placeholder="请输入企业负责人姓名"></a-input>
        </a-form-item>
        <a-form-item label="负责人联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fzrPhone', validatorRules.fzrPhone]" placeholder="请输入负责人联系方式"></a-input>
        </a-form-item>
        <a-form-item label="登记注册类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['djzcType', validatorRules.djzcType]" :trigger-change="true" dictCode="djzclx" placeholder="请选择登记注册类型"/>
        </a-form-item>
        <a-form-item label="控股情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['kgqk', validatorRules.kgqk]" :trigger-change="true" dictCode="kgqk" placeholder="请选择控股情况"/>
        </a-form-item>
        <a-form-item label="经营范围" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-multi-select-tag type="checkbox" v-decorator="['jyfw', validatorRules.jyfw]" :trigger-change="true" dictCode="jyfw" placeholder="请选择经营范围"/>
        </a-form-item>
        <a-form-item label="企业类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['businessType', validatorRules.businessType]" :trigger-change="true" dictCode="qylx" placeholder="请选择企业类型"/>
        </a-form-item>
        <a-form-item label="服务品牌" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fwpp', validatorRules.fwpp]" placeholder="请输入服务品牌"></a-input>
        </a-form-item>
        <a-form-item label="从业人员数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'cyryNum', validatorRules.cyryNum]" placeholder="请输入从业人员数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="监控摄像机数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'sxjNum', validatorRules.sxjNum]" placeholder="请输入监控摄像机数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="X光机数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'xgj', validatorRules.xgj]" placeholder="请输入X光机数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="是否落实100%先验视后封箱" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfys', validatorRules.sfys]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否落实100%先验视后封箱"/>
        </a-form-item>
        <a-form-item label="是否落实100%寄递实名制" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfsmz', validatorRules.sfsmz]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否落实100%寄递实名制"/>
        </a-form-item>
        <a-form-item label="是否落实100%X光机安检" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfxgj', validatorRules.sfxgj]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否落实100%X光机安检"/>
        </a-form-item>
        <a-form-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'longitude', validatorRules.longitude]" placeholder="请输入经度"></a-input>
        </a-form-item>
        <a-form-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'latitude', validatorRules.latitude]" placeholder="请输入纬度"></a-input>
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
  import JMultiSelectTag from "@/components/dict/JMultiSelectTag"

  export default {
    name: "ZzWlaqModal",
    components: { 
      JDictSelectTag,
      JMultiSelectTag,
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
          gszzCode: {rules: [
            {required: true, message: '请输入工商执照注册号!'},
          ]},
          businessName: {rules: [
            {required: true, message: '请输入企业名称!'},
          ]},
          adress: {rules: [
            {required: true, message: '请输入所在地!'},
          ]},
          qyAdress: {rules: [
          ]},
          qyPhone: {rules: [
            {required: true, message: '请输入企业联系方式!'},
          ]},
          qyfzrName: {rules: [
          ]},
          fzrPhone: {rules: [
          ]},
          djzcType: {rules: [
            {required: true, message: '请输入登记注册类型!'},
          ]},
          kgqk: {rules: [
          ]},
          jyfw: {rules: [
          ]},
          businessType: {rules: [
          ]},
          fwpp: {rules: [
          ]},
          cyryNum: {rules: [
          ]},
          sxjNum: {rules: [
          ]},
          xgj: {rules: [
          ]},
          sfys: {rules: [
          ]},
          sfsmz: {rules: [
          ]},
          sfxgj: {rules: [
          ]},
          longitude: {rules: [
          ]},
          latitude: {rules: [
          ]},
        },
        url: {
          add: "/wlaq/zzWlaq/add",
          edit: "/wlaq/zzWlaq/edit",
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
          this.form.setFieldsValue(pick(this.model,'gszzCode','businessName','adress','qyAdress','qyPhone','qyfzrName','fzrPhone','djzcType','kgqk','jyfw','businessType','fwpp','cyryNum','sxjNum','xgj','sfys','sfsmz','sfxgj','longitude','latitude'))
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
        this.form.setFieldsValue(pick(row,'gszzCode','businessName','adress','qyAdress','qyPhone','qyfzrName','fzrPhone','djzcType','kgqk','jyfw','businessType','fwpp','cyryNum','sxjNum','xgj','sfys','sfsmz','sfxgj','longitude','latitude'))
      },

      
    }
  }
</script>