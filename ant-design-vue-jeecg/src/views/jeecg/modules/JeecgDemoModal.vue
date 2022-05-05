<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form"  :label-col="labelCol" :wrapper-col="wrapperCol"  :model="model" :rules="validatorRules">
        <a-form-model-item label="姓名" required prop="name" hasFeedback>
          <a-input v-model="model.name"    placeholder="请输入姓名"/>
        </a-form-model-item>

        <a-form-model-item label="关键词"  prop="keyWord" hasFeedback>
          <a-input v-model="model.keyWord"    placeholder="请输入关键词"/>
        </a-form-model-item>

        <a-form-model-item label="打卡时间"  prop="punchTime" hasFeedback>
          <a-date-picker showTime valueFormat="YYYY-MM-DD HH:mm:ss" v-model="model.punchTime" />
        </a-form-model-item>

        <a-form-model-item label="性别"  prop="sex" hasFeedback>
          <j-dict-select-tag type="radio" v-model="model.sex"  :trigger-change="true" dictCode="sex"/>
        </a-form-model-item>

        <a-form-model-item label="年龄"  prop="age" hasFeedback>
          <a-input placeholder="请输入年龄" v-model="model.age"/>
        </a-form-model-item>

        <a-form-model-item label="生日"  prop="age" hasFeedback>
          <a-date-picker valueFormat="YYYY-MM-DD"  v-model="model.birthday"/>
        </a-form-model-item>

        <a-form-model-item label="邮箱"  prop="email" hasFeedback >
          <a-input  placeholder="请输入邮箱"  v-model="model.email"/>
        </a-form-model-item>

        <a-form-model-item label="个人简介"  prop="content" hasFeedback>
          <a-input  type="textarea" placeholder="请输入个人简介"  v-model="model.content"/>
        </a-form-model-item>

      </a-form-model>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'

  export default {
    name: "JeecgDemoModal",
    data () {
      return {
        title:"操作",
        visible: false,
        model: {},
        layout: {
          labelCol: { span: 3 },
          wrapperCol: { span: 14 },
        },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules:{
          name: [
            { required: true, message: '请输入姓名!' },
            { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
          ],
          email: [
            { required: false, type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
          ]
        },
        url: {
          add: "/test/jeecgDemo/add",
          edit: "/test/jeecgDemo/edit",
        },
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      close () {
        this.$refs.form.resetFields();
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
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
            httpAction(httpurl,this.model,method).then((res)=>{
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
      }
    }
  }
</script>

<style scoped>

</style>