<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入姓名"></a-input>
        </a-form-item>
        <a-form-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['sex', validatorRules.sex]" placeholder="请输入性别"></a-input>
        </a-form-item>
        <a-form-item label="学历" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['education', validatorRules.education]" placeholder="请输入学历"></a-input>
        </a-form-item>
        <a-form-item label="专业" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['profession', validatorRules.profession]" placeholder="请输入专业"></a-input>
        </a-form-item>
        <a-form-item label="职称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['jobTitle', validatorRules.jobTitle]" placeholder="请输入职称"></a-input>
        </a-form-item>
        <a-form-item label="部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['department', validatorRules.department]" placeholder="请输入部门"></a-input>
        </a-form-item>
        <a-form-item label="岗位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['post', validatorRules.post]" placeholder="请输入岗位"></a-input>
        </a-form-item>
        <a-form-item label="身份证号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['idCard', validatorRules.idCard]" placeholder="请输入身份证号码"></a-input>
        </a-form-item>
        <a-form-item label="手机号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['pbone', validatorRules.pbone]" placeholder="请输入手机号码"></a-input>
        </a-form-item>
        <a-form-item label="出生日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请输入出生日期" v-decorator="['birthDate', validatorRules.birthDate]" :trigger-change="true" style="width: 100%" />
        </a-form-item>
      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel">取消</a-button>
      <a-button type="primary" @click="handleOk">暂存</a-button>
      <a-button type="primary" @click="handDeclare">申报</a-button>
    </template>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  


  export default {
    name: "CompanyUserinfoModal",
    components: { 
      JDate,
    },
    props:{
      companyId:''
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
          name: {
            rules: [
              { required: true, message: '请输入姓名!'},
            ]
          },
          sex: {
            rules: [
              { required: true, message: '请输入性别!'},
            ]
          },
          education: {
            rules: [
              { required: true, message: '请输入学历!'},
            ]
          },
          profession: {
            rules: [
              { required: true, message: '请输入专业!'},
            ]
          },
          jobTitle: {
            rules: [
              { required: true, message: '请输入职称!'},
            ]
          },
          department: {
            rules: [
              { required: true, message: '请输入部门!'},
            ]
          },
          post: {
            rules: [
              { required: true, message: '请输入岗位!'},
            ]
          },
          idCard: {
            rules: [
              { required: true, message: '请输入身份证号码!'},
            ]
          },
          pbone: {
            rules: [
              { required: true, message: '请输入手机号码!'},
            ]
          },
          birthDate: {
            rules: [
              { required: true, message: '请输入出生日期!'},
            ]
          },
        },
        url: {
          add: "/companyUserinfo/add",
          edit: "/companyUserinfo/edit",
          apply:"/companyUserinfo/editAndApply"
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
          this.form.setFieldsValue(pick(this.model,'name','sex','education','profession','jobTitle','department','post','idCard','pbone','birthDate'))
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
          //获取表单数据
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
              values.companyId = that.companyId;
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
      //申报
      handDeclare(){
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          //获取表单数据
          if (!err) {
            that.confirmLoading = true;

            let httpurl =this.url.apply;
            let method = 'post';
            values.companyId = that.companyId;
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
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'createBy','createTime','updateBy','updateTime','companyId','name','sex','education','profession','jobTitle','department','post','idCard','pbone','birthDate'))
      },

      
    }
  }
</script>