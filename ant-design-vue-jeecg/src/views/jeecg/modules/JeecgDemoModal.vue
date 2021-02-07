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
      <a-form :form="form">
      
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="姓名"
          hasFeedback >
          <a-input placeholder="请输入姓名" v-decorator="['name', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="关键词"
          hasFeedback >
          <a-input placeholder="请输入关键词" v-decorator="['keyWord', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="打卡时间"
          hasFeedback >
          <a-date-picker showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="[ 'punchTime', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="性别">
         <!-- <a-select v-decorator="['sex', {}]" placeholder="请选择性别">
            <a-select-option value="">请选择</a-select-option>
            <a-select-option value="1">男</a-select-option>
            <a-select-option value="2">女</a-select-option>
          </a-select>-->
          <j-dict-select-tag type="radio" v-decorator="['sex', {}]" :trigger-change="true" dictCode="sex"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="年龄"
          hasFeedback >
          <a-input placeholder="请输入年龄" v-decorator="['age', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="生日"
          hasFeedback >
          <a-date-picker v-decorator="[ 'birthday', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="邮箱"
          hasFeedback >
          <a-input placeholder="请输入邮箱" v-decorator="['email', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="个人简介"
          hasFeedback >
          <a-input placeholder="请输入个人简介" v-decorator="['content', {}]" />
        </a-form-item>
		
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"

  export default {
    name: "JeecgDemoModal",
    data () {
      return {
        title:"操作",
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
        form: this.$form.createForm(this),
        validatorRules:{
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
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name','keyWord','sex','age','email','content'))
		      //时间格式化
          this.form.setFieldsValue({punchTime:this.model.punchTime?moment(this.model.punchTime,'YYYY-MM-DD HH:mm:ss'):null})
          this.form.setFieldsValue({birthday:this.model.birthday?moment(this.model.birthday):null})
        });

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
            //时间格式化
            formData.punchTime = formData.punchTime?formData.punchTime.format('YYYY-MM-DD HH:mm:ss'):null;
            formData.birthday = formData.birthday?formData.birthday.format():null;
            
            console.log(formData)
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


    }
  }
</script>

<style scoped>

</style>