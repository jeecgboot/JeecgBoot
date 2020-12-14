<template>
  <div>
    <a-form :form="form" style="max-width: 500px; margin: 40px auto 0;">
      <a-form-item
        label="账号名"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
      >
        <a-input
          type="text"
          autocomplete="false" :value="accountName" disabled>
        </a-input>
      </a-form-item>
      <a-form-item
        label="新密码"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
        class="stepFormText">
        <a-input
          v-decorator="['password',validatorRules.password]"
          type="password"
          autocomplete="false">
        </a-input>
      </a-form-item>
      <a-form-item
        label="确认密码"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
        class="stepFormText">
        <a-input
          v-decorator="['confirmPassword',validatorRules.confirmPassword]"
          type="password"
          autocomplete="false">
        </a-input>
      </a-form-item>
      <a-form-item :wrapperCol="{span: 19, offset: 5}">
        <a-button style="margin-left: 8px" @click="prevStep">上一步</a-button>
        <a-button :loading="loading" type="primary" @click="nextStep" style="margin-left:20px">提交</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
  import { putAction,getAction } from '@/api/manage'
  export default {
    name: "Step3",
//    components: {
//      Result
//    },
    props: ['userList'],
    data () {
      return {
        loading: false,
        form: this.$form.createForm(this),
        accountName: this.userList.username,
        validatorRules: {
          username: {rules: [{required: true, message: '用户名不能为空!'}]},
          password: {
            rules: [{
              required: true,
              pattern: /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[~!@#$%^&*()_+`\-={}:";'<>?,.\/]).{8,}$/,
              message: '密码由8位数字、大小写字母和特殊符号组成!!'
            }, {validator: this.handlePasswordLevel}]
          },
          confirmPassword: {rules: [{required: true, message: '密码不能为空!'}, {validator: this.handlePasswordCheck}]},
        },
      }
    },
    methods: {
      nextStep () {
        let that = this
        that.loading = true
        this.form.validateFields((err, values) => {
          if ( !err ){
          var params={}
          params.username=this.userList.username;
          params.password=values.password;
          params.smscode=this.userList.smscode;
          params.phone= this.userList.phone;
          getAction("/sys/user/passwordChange", params).then((res) => {
            if(res.success){
            var userList = {
              username: this.userList.username
            }
            console.log(userList);
            setTimeout(function () {
              that.$emit('nextStep', userList)
            }, 1500)
          }else{
            this.passwordFailed(res.message);
            that.loading = false
          }
        })
        } else{
          that.loading = false
        }
      })

      },
      prevStep () {
        this.$emit('prevStep', this.userList)
      },

      handlePasswordCheck (rule, value, callback) {
        let password = this.form.getFieldValue('password')
        if (value && password && value.trim() !== password.trim()) {
          callback(new Error('两次密码不一致'))
        }
        callback()
      },
      passwordFailed(err){
        this.$notification[ 'error' ]({
          message: "更改密码失败",
          description:err,
          duration: 4,
        });
      },
    }
  }
</script>
<style lang="less" scoped>
  .stepFormText {
    margin-bottom: 24px;
  }

  .ant-form-item-label,
  .ant-form-item-control {
    line-height: 22px;
  }

</style>