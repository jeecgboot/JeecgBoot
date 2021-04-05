<template>
  <div>
    <a-form-model ref="form" :model="model" :rules="validatorRules" class="password-retrieval-form">

      <a-form-model-item label="账号名" v-bind="layout">
        <a-input type="text" :value="accountName" disabled/>
      </a-form-model-item>

      <a-form-model-item prop="password" label="新密码" v-bind="layout" class="stepFormText">
        <a-input v-model="model.password" type="password" autocomplete="false"/>
      </a-form-model-item>

      <a-form-model-item prop="confirmPassword" label="确认密码" v-bind="layout" class="stepFormText">
        <a-input v-model="model.confirmPassword" type="password" autocomplete="false"/>
      </a-form-model-item>

      <a-form-model-item :wrapperCol="{span: 19, offset: 5}">
        <a-button style="margin-left: 8px" @click="prevStep">上一步</a-button>
        <a-button :loading="loading" type="primary" @click="nextStep" style="margin-left:20px">提交</a-button>
      </a-form-model-item>
    </a-form-model>
  </div>
</template>

<script>
  import { getAction } from '@/api/manage'
  export default {
    name: "Step3",
//    components: {
//      Result
//    },
    props: ['userList'],
    data () {
      return {
        model:{},
        layout: {
          labelCol: { span: 5 },
          wrapperCol: { span: 19 },
        },
        loading: false,
        accountName: this.userList.username,
        validatorRules: {
          password: [{
            required: true, pattern: /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[~!@#$%^&*()_+`\-={}:";'<>?,.\/]).{8,}$/, message: '密码由8位数字、大小写字母和特殊符号组成!!'
          }],
          confirmPassword: [
            { required: true, message: '密码不能为空!'},
            { validator: this.handlePasswordCheck}
          ]
        }
      }
    },
    methods: {
      nextStep () {
        let that = this
        that.loading = true
        that.$refs['form'].validate(success => {
          if (success === true) {
            let params = {
              username: that.userList.username,
              password: that.model.password,
              smscode: that.userList.smscode,
              phone: that.userList.phone,
            }
            getAction("/sys/user/passwordChange", params).then((res) => {
              if (res.success) {
                let userList = {
                  username: that.userList.username
                }
                console.log(userList);
                setTimeout(function() {
                  that.$emit('nextStep', userList)
                }, 1500)
              } else {
                that.passwordFailed(res.message);
                that.loading = false
              }
            })
          } else {
            that.loading = false
          }
        })
      },
      prevStep () {
        this.$emit('prevStep', this.userList)
      },

      handlePasswordCheck (rule, value, callback) {
        let password = this.model['password']
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