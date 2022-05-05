<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" slot="detail" :rules="validatorRules">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="租户名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="model.name" placeholder="请输入租户名称"></a-input>
            </a-form-model-item>
          </a-col>
          
          <a-col :span="24">
            <a-form-model-item label="租户编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="id">
              <a-input-number style="width: 100%" :min="1" v-model="model.id" placeholder="请输入租户编号" :disabled="disabledId"></a-input-number>
            </a-form-model-item>
          </a-col>
          
          <a-col :span="24">
            <a-form-model-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择开始时间" v-model="model.beginDate" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择结束时间" v-model="model.endDate" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-radio-group name="tenantStatus" v-model="model.status">
                <a-radio :value="1">正常</a-radio>
                <a-radio :value="0">冻结</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>
  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  
  export default {
    name: "TenantForm",
    components: {
      JFormContainer,
      JDate,
      JDictSelectTag,
    },
    props: {
      formData: {
        type: Object,
        default: ()=>{},
        required: false
      },
      normal: {
        type: Boolean,
        default: false,
        required: false
      },
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model: {status:1},
        id:'',
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
          id:[ { required: true, message: '请输入租户编号!' },]
        },
        url: {
          add: "/sys/tenant/add",
          edit: "/sys/tenant/edit",
          queryById: "/sys/tenant/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        if(this.normal===false){
          if(this.formData.disabled===false){
            return false
          }else{
            return true
          }
        }
        return this.disabled
      },
      disabledId(){
        return this.id?true : false;
      },
      showFlowSubmitButton(){
        if(this.normal===false){
          if(this.formData.disabled===false){
            return true
          }else{
            return false
          }
        }else{
          return false
        }
      }
    },
    created () {
      this.showFlowData();
    },
    methods: {
      show (record) {
        this.model = record?Object.assign({}, record):this.model;
        this.id = record?record.id:'';
        this.visible = true;
      },
      showFlowData(){
        if(this.normal === false){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          });
        }
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        that.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
              method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                if("该编号已存在!" == res.message){
                  this.model.id=""
                }
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }else{
            return false;
          }

        })
      },
      popupCallback(row){
        this.model = Object.assign(this.model, row);
      },
    }
  }
</script>
