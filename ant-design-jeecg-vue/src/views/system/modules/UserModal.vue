<template>
  <a-modal
    :width="modalWidth"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :okButtonProps="{ props: {disabled: disableSubmit} }"
    @ok="handleSubmit"
    @cancel="handleCancel"
    cancelText="关闭"
    wrapClassName="ant-modal-cust-warp"
    style="top:5%;height: auto;overflow-y: hidden">

    <template slot="title">
      <div style="width: 100%;">
        <span>{{ title }}</span>
        <span style="display:inline-block;width:calc(100% - 56px);padding-top: 2px;padding-right:10px;text-align: right">
          <a-button @click="togglescreen" style="height:18px;width:18px;padding: 0;border-width: 2px">&nbsp;&nbsp;</a-button>
        </span>
      </div>

    </template>

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="用户账号" :labelCol="labelCol" :wrapperCol="wrapperCol" hasFeedback>
          <a-input placeholder="请输入用户账号" v-decorator="[ 'username', validatorRules.username]" :readOnly="!!model.id"/>
        </a-form-item>

        <template v-if="!model.id">
          <a-form-item label="登陆密码" :labelCol="labelCol" :wrapperCol="wrapperCol" hasFeedback >
            <a-input type="password" placeholder="请输入登陆密码" v-decorator="[ 'password', validatorRules.password]" />
          </a-form-item>

          <a-form-item label="确认密码" :labelCol="labelCol" :wrapperCol="wrapperCol" hasFeedback >
            <a-input type="password" @blur="handleConfirmBlur" placeholder="请重新输入登陆密码" v-decorator="[ 'confirmpassword', validatorRules.confirmpassword]"/>
          </a-form-item>
        </template>

        <a-form-item label="用户名称" :labelCol="labelCol" :wrapperCol="wrapperCol" hasFeedback >
          <a-input placeholder="请输入用户名称" v-decorator="[ 'realname', validatorRules.realname]" />
        </a-form-item>

        <a-form-item label="角色分配" :labelCol="labelCol" :wrapperCol="wrapperCol" hasFeedback >
          <a-select
            mode="multiple"
            style="width: 100%"
            placeholder="请选择用户角色"
            v-model="selectedRole">
            <a-select-option v-for="(role,roleindex) in roleList" :key="roleindex.toString()" :value="role.id">
              {{ role.roleName }}
            </a-select-option>


          </a-select>

        </a-form-item>




        <a-form-item label="头像" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-upload
            listType="picture-card"
            class="avatar-uploader"
            :showUploadList="false"
            :action="uploadAction"
            :data="{'isup':1}"
            :headers="headers"
            :beforeUpload="beforeUpload"
            @change="handleChange"
          >
            <img v-if="model.avatar" :src="getAvatarView()" alt="头像" style="height:104px;max-width:300px"/>
            <div v-else>
              <a-icon :type="uploadLoading ? 'loading' : 'plus'" />
              <div class="ant-upload-text">上传</div>
            </div>
          </a-upload>
        </a-form-item>

        <a-form-item label="生日" :labelCol="labelCol" :wrapperCol="wrapperCol" hasFeedback >
          <a-date-picker
            style="width: 100%"
            placeholder="请选择生日"
            v-decorator="['birthday', {initialValue:!model.birthday?null:moment(model.birthday,dateFormat)}]"/>
        </a-form-item>

        <a-form-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol" hasFeedback >
          <a-select
            v-decorator="[ 'sex', {initialValue:model.sex && model.sex.toString()}]"
            placeholder="请选择性别">
            <a-select-option value="1">男</a-select-option>
            <a-select-option value="2">女</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="邮箱" :labelCol="labelCol" :wrapperCol="wrapperCol" hasFeedback >
          <a-input placeholder="请输入邮箱" v-decorator="[ 'email', validatorRules.email]" />
        </a-form-item>

        <a-form-item label="手机号码" :labelCol="labelCol" :wrapperCol="wrapperCol" hasFeedback >
          <a-input placeholder="请输入手机号码" v-decorator="[ 'phone', validatorRules.phone]" />
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import pick from 'lodash.pick'
  import moment from 'moment'
  import Vue from 'vue'
  import { ACCESS_TOKEN } from "@/store/mutation-types"
  import {doMian,addUser,editUser,queryUserRole,queryall,checkUsername} from '@/api/api'

  export default {
    name: "RoleModal",
    data () {
      return {
        modalWidth:800,
        modaltoggleFlag:true,
        confirmDirty: false,
        disableSubmit:false,
        dateFormat:"YYYY-MM-DD",
        validatorRules:{
          username:{
            rules: [{
              required: true, message: '请输入用户账号!'
            },{
              validator: this.validateUsername,
            }]
          },
          password:{
            rules: [{
              required: true, message: '请输入密码!',
            }, {
              validator: this.validateToNextPassword,
            }],
          },
          confirmpassword:{
            rules: [{
              required: true, message: '请重新输入登陆密码!',
            }, {
              validator: this.compareToFirstPassword,
            }],
          },
          realname:{rules: [{ required: true, message: '请输入用户名称!' }]},
          phone:{rules: [{validator: this.validatePhone}]},
          email:{rules: [{type: 'email', message: '请输入正确格式的电子邮箱!',}]},
          roles:{}
        //  sex:{initialValue:((!this.model.sex)?"": (this.model.sex+""))}
        },
        title:"操作",
        visible: false,
        model: {},
        roleList:[],
        selectedRole:[],
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        uploadLoading:false,
        confirmLoading: false,
        headers:{},
        form:this.$form.createForm(this),
        url: {
          fileUpload:doMian+"sys/common/upload",
          imgerver:doMian+"sys/common/view",
        },
      }
    },
    created () {
      const token = Vue.ls.get(ACCESS_TOKEN);
      this.headers = {"X-Access-Token":token}

    },
    computed:{
      uploadAction:function () {
        return this.url.fileUpload;
      }
    },
    methods: {

      togglescreen(){
        if(this.modaltoggleFlag){
          this.modalWidth = window.innerWidth;
        }else{
          this.modalWidth = 800;
        }
        this.modaltoggleFlag = !this.modaltoggleFlag;
      },
      initialRoleList(){
        queryall().then((res)=>{
          if(res.success){
            this.roleList = res.result;
          }else{
            console.log(res.message);
          }
        });
      },
      loadUserRoles(userid){
        queryUserRole({userid:userid}).then((res)=>{
          if(res.success){
            this.selectedRole = res.result;
          }else{
            console.log(res.message);
          }
        });
      },
      add () {
        this.edit({});
      },
      edit (record) {
        this.initialRoleList();
        this.form.resetFields();
        if(record.hasOwnProperty("id")){
          this.loadUserRoles(record.id);
        }
        this.visible = true;
        this.model = Object.assign({}, record);
        let filedsVal = pick(this.model,'username','password','realname','email','phone');
        filedsVal.confirmpassword = this.model.password;
        this.$nextTick(() => {
          this.form.setFieldsValue(filedsVal);
        });

      },
      close () {
        this.$emit('close');
        this.visible = false;
        this.disableSubmit = false;
        this.selectedRole = [];
      },
      moment,
      handleSubmit () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let avatar = that.model.avatar;
            if(!values.birthday){
              values.birthday = '';
            }else{
              values.birthday = values.birthday.format(this.dateFormat);
            }
            let formData = Object.assign(this.model, values);
            formData.avatar = avatar;
            formData.selectedroles = this.selectedRole.length>0?this.selectedRole.join(","):'';
            let obj;
            if(!this.model.id){
              obj=addUser(formData);
            }else{
              obj=editUser(formData);
            }
            obj.then((res)=>{
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
      validateToNextPassword  (rule, value, callback) {
        const form = this.form;
        if (value && this.confirmDirty) {
          form.validateFields(['confirm'], { force: true })
        }
        callback();
      },
      compareToFirstPassword  (rule, value, callback) {
        const form = this.form;
        if (value && value !== form.getFieldValue('password')) {
          callback('两次输入的密码不一样！');
        } else {
          callback()
        }
      },
      validatePhone(rule, value, callback){
        if(!value || new RegExp(/^1[3|4|5|7|8][0-9]\d{8}$/).test(value)){
          callback();
        }else{
          callback("请输入正确格式的手机号码!");
        }
      },
      validateUsername(rule, value, callback){
        var params = {
          id:this.model.id,
          username:value
        };
        checkUsername(params).then((res)=>{
          if(res.success){
            callback();
          }else{
            callback("用户账号已存在！");
          }
        });
      },
      handleConfirmBlur  (e) {
        const value = e.target.value
        this.confirmDirty = this.confirmDirty || !!value
      },

      normFile  (e) {
        console.log('Upload event:', e)
        if (Array.isArray(e)) {
          return e
        }
        return e && e.fileList
      },
      beforeUpload: function(file){
        var fileType = file.type;
        if(fileType.indexOf('image')<0){
          this.$message.warning('请上传图片');
          return false;
        }
        //TODO 验证文件大小
      },
      handleChange (info) {
        if (info.file.status === 'uploading') {
          this.uploadLoading = true
          return
        }
        if (info.file.status === 'done') {
          var response = info.file.response;
          this.uploadLoading = false;
          console.log(response);
          if(response.success){
            this.model.avatar = response.message;
          }else{
            this.$message.warning(response.message);
          }
        }
      },
      getAvatarView(){
        return this.url.imgerver +"/"+ this.model.avatar;
      },

    }
  }
</script>

<style scoped>
  .avatar-uploader > .ant-upload {
    width:104px;
    height:104px;
  }
  .ant-upload-select-picture-card i {
    font-size: 49px;
    color: #999;
  }

  .ant-upload-select-picture-card .ant-upload-text {
    margin-top: 8px;
    color: #666;
  }

  .ant-table-tbody .ant-table-row td{
    padding-top:10px;
    padding-bottom:10px;
  }

</style>