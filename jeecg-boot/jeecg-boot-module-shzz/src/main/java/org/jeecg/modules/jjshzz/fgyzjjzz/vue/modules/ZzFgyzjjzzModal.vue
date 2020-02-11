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
        <a-form-item label="企业类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['businessType', validatorRules.businessType]" :trigger-change="true" dictCode="qylb" placeholder="请选择企业类别"/>
        </a-form-item>
        <a-form-item label="企业地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'businessAdress', validatorRules.businessAdress]" placeholder="请输入企业地址"></a-input>
        </a-form-item>
        <a-form-item label="企业联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'businessPhone', validatorRules.businessPhone]" placeholder="请输入企业联系方式"></a-input>
        </a-form-item>
        <a-form-item label="企业员工数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'businessYgs', validatorRules.businessYgs]" placeholder="请输入企业员工数" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="法定代表人证件代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fddbrZjdm', validatorRules.fddbrZjdm]" placeholder="请输入法定代表人证件代码"></a-input>
        </a-form-item>
        <a-form-item label="法定代表人证件号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fddbrZjh', validatorRules.fddbrZjh]" placeholder="请输入法定代表人证件号码"></a-input>
        </a-form-item>
        <a-form-item label="法定代表人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fddbrName', validatorRules.fddbrName]" placeholder="请输入法定代表人姓名"></a-input>
        </a-form-item>
        <a-form-item label="法定代表人联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fddbrPhone', validatorRules.fddbrPhone]" placeholder="请输入法定代表人联系方式"></a-input>
        </a-form-item>
        <a-form-item label="治保负责人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zbfzrName', validatorRules.zbfzrName]" placeholder="请输入治保负责人姓名"></a-input>
        </a-form-item>
        <a-form-item label="治保负责人联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zbfzrPhone', validatorRules.zbfzrPhone]" placeholder="请输入治保负责人联系方式"></a-input>
        </a-form-item>
        <a-form-item label="是否危化企业" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfwhqy', validatorRules.sfwhqy]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否危化企业"/>
        </a-form-item>
        <a-form-item label="安全隐患类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['aqyhType', validatorRules.aqyhType]" :trigger-change="true" dictCode="aqyhlx" placeholder="请选择安全隐患类型"/>
        </a-form-item>
        <a-form-item label="关注程度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['gzcd', validatorRules.gzcd]" :trigger-change="true" dictCode="gzcd" placeholder="请选择关注程度"/>
        </a-form-item>
        <a-form-item label="是否具备建立中共党组织条件" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfjbjltj', validatorRules.sfjbjltj]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否具备建立中共党组织条件"/>
        </a-form-item>
        <a-form-item label="是否有中共党组织" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfzgdzz', validatorRules.sfzgdzz]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否有中共党组织"/>
        </a-form-item>
        <a-form-item label="中共党员数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'zgdyNum', validatorRules.zgdyNum]" placeholder="请输入中共党员数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="是否有工会" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfgh', validatorRules.sfgh]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否有工会"/>
        </a-form-item>
        <a-form-item label="工会会员数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'ghhyNum', validatorRules.ghhyNum]" placeholder="请输入工会会员数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="是否有共青团组织" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfgqtzz', validatorRules.sfgqtzz]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否有共青团组织"/>
        </a-form-item>
        <a-form-item label="共青团团员数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'gqttyNum', validatorRules.gqttyNum]" placeholder="请输入共青团团员数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="是否有妇联组织" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfflzz', validatorRules.sfflzz]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否有妇联组织"/>
        </a-form-item>
        <a-form-item label="妇女数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'womanNum', validatorRules.womanNum]" placeholder="请输入妇女数量" style="width: 100%"/>
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

  export default {
    name: "ZzFgyzjjzzModal",
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
          gszzCode: {rules: [
            {required: true, message: '请输入工商执照注册号!'},
          ]},
          businessName: {rules: [
            {required: true, message: '请输入企业名称!'},
          ]},
          businessType: {rules: [
            {required: true, message: '请输入企业类别!'},
          ]},
          businessAdress: {rules: [
          ]},
          businessPhone: {rules: [
            {required: true, message: '请输入企业联系方式!'},
          ]},
          businessYgs: {rules: [
          ]},
          fddbrZjdm: {rules: [
          ]},
          fddbrZjh: {rules: [
          ]},
          fddbrName: {rules: [
          ]},
          fddbrPhone: {rules: [
          ]},
          zbfzrName: {rules: [
          ]},
          zbfzrPhone: {rules: [
          ]},
          sfwhqy: {rules: [
          ]},
          aqyhType: {rules: [
          ]},
          gzcd: {rules: [
            {required: true, message: '请输入关注程度!'},
          ]},
          sfjbjltj: {rules: [
          ]},
          sfzgdzz: {rules: [
          ]},
          zgdyNum: {rules: [
          ]},
          sfgh: {rules: [
          ]},
          ghhyNum: {rules: [
          ]},
          sfgqtzz: {rules: [
          ]},
          gqttyNum: {rules: [
          ]},
          sfflzz: {rules: [
          ]},
          womanNum: {rules: [
          ]},
          longitude: {rules: [
          ]},
          latitude: {rules: [
          ]},
        },
        url: {
          add: "/fgyzjjzz/zzFgyzjjzz/add",
          edit: "/fgyzjjzz/zzFgyzjjzz/edit",
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
          this.form.setFieldsValue(pick(this.model,'gszzCode','businessName','businessType','businessAdress','businessPhone','businessYgs','fddbrZjdm','fddbrZjh','fddbrName','fddbrPhone','zbfzrName','zbfzrPhone','sfwhqy','aqyhType','gzcd','sfjbjltj','sfzgdzz','zgdyNum','sfgh','ghhyNum','sfgqtzz','gqttyNum','sfflzz','womanNum','longitude','latitude'))
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
        this.form.setFieldsValue(pick(row,'gszzCode','businessName','businessType','businessAdress','businessPhone','businessYgs','fddbrZjdm','fddbrZjh','fddbrName','fddbrPhone','zbfzrName','zbfzrPhone','sfwhqy','aqyhType','gzcd','sfjbjltj','sfzgdzz','zgdyNum','sfgh','ghhyNum','sfgqtzz','gqttyNum','sfflzz','womanNum','longitude','latitude'))
      },

      
    }
  }
</script>