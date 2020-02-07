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

        <a-form-item label="小区名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'villageName', validatorRules.villageName]" placeholder="请输入小区名称"></a-input>
        </a-form-item>
        <a-form-item label="楼栋名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'buildingName', validatorRules.buildingName]" placeholder="请输入楼栋名称"></a-input>
        </a-form-item>
        <a-form-item label="建筑面积" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'buildArea', validatorRules.buildArea]" placeholder="请输入建筑面积" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="层数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'floorNum', validatorRules.floorNum]" placeholder="请输入层数" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="单元楼" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'dyl', validatorRules.dyl]" placeholder="请输入单元楼"></a-input>
        </a-form-item>
        <a-form-item label="楼栋户数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'ldhs', validatorRules.ldhs]" placeholder="请输入楼栋户数" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="楼栋人数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'ldrs', validatorRules.ldrs]" placeholder="请输入楼栋人数" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="楼栋长姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'ldzName', validatorRules.ldzName]" placeholder="请输入楼栋长姓名"></a-input>
        </a-form-item>
        <a-form-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="radio" v-decorator="['sex', validatorRules.sex]" :trigger-change="true" dictCode="sex" placeholder="请选择性别"/>
        </a-form-item>
        <a-form-item label="民族" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['folk', validatorRules.folk]" :trigger-change="true" dictCode="minzu" placeholder="请选择民族"/>
        </a-form-item>
        <a-form-item label="政治面貌" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['politicalStatus', validatorRules.politicalStatus]" :trigger-change="true" dictCode="zzmm" placeholder="请选择政治面貌"/>
        </a-form-item>
        <a-form-item label="出生日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择出生日期" v-decorator="[ 'birthday', validatorRules.birthday]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="学历" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['education', validatorRules.education]" :trigger-change="true" dictCode="education" placeholder="请选择学历"/>
        </a-form-item>
        <a-form-item label="手机号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'phone', validatorRules.phone]" placeholder="请输入手机号码"></a-input>
        </a-form-item>
        <a-form-item label="固定电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'tel', validatorRules.tel]" placeholder="请输入固定电话"></a-input>
        </a-form-item>
        <a-form-item label="所在地" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'szd', validatorRules.szd]" placeholder="请输入所在地"></a-input>
        </a-form-item>
        <a-form-item label="所在地详址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['szdxz', validatorRules.szdxz]" rows="4" placeholder="请输入所在地详址"/>
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
    name: "ZzLdzModal",
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
          villageName: {rules: [
            {required: true, message: '请输入小区名称!'},
          ]},
          buildingName: {rules: [
            {required: true, message: '请输入楼栋名称!'},
          ]},
          buildArea: {rules: [
          ]},
          floorNum: {rules: [
            {pattern:/^-?\d+$/, message: '请输入整数!'},
          ]},
          dyl: {rules: [
          ]},
          ldhs: {rules: [
            {pattern:/^-?\d+$/, message: '请输入整数!'},
          ]},
          ldrs: {rules: [
            {pattern:/^-?\d+$/, message: '请输入整数!'},
          ]},
          ldzName: {rules: [
            {required: true, message: '请输入楼栋长姓名!'},
          ]},
          sex: {rules: [
          ]},
          folk: {rules: [
          ]},
          politicalStatus: {rules: [
          ]},
          birthday: {rules: [
          ]},
          education: {rules: [
          ]},
          phone: {rules: [
          ]},
          tel: {rules: [
          ]},
          szd: {rules: [
          ]},
          szdxz: {rules: [
          ]},
          longitude: {rules: [
          ]},
          latitude: {rules: [
          ]},
        },
        url: {
          add: "/ldz/zzLdz/add",
          edit: "/ldz/zzLdz/edit",
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
          this.form.setFieldsValue(pick(this.model,'villageName','buildingName','buildArea','floorNum','dyl','ldhs','ldrs','ldzName','sex','folk','politicalStatus','birthday','education','phone','tel','szd','szdxz','longitude','latitude'))
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
        this.form.setFieldsValue(pick(row,'villageName','buildingName','buildArea','floorNum','dyl','ldhs','ldrs','ldzName','sex','folk','politicalStatus','birthday','education','phone','tel','szd','szdxz','longitude','latitude'))
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