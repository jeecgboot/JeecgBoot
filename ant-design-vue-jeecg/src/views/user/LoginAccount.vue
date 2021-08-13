<template>
    <div>
      <a-form-model ref="form" :model="model" :rules="validatorRules">
        <a-form-model-item required prop="username">
          <a-input v-model="model.username" size="large" placeholder="请输入帐户名 / admin">
            <a-icon slot="prefix" type="user" :style="{ color: 'rgba(0,0,0,.25)' }"/>
          </a-input>
        </a-form-model-item>
        <a-form-model-item required prop="password">
          <a-input v-model="model.password" size="large" type="password" autocomplete="false" placeholder="请输入密码 / 123456">
            <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
          </a-input>
        </a-form-model-item>

        <a-row :gutter="0">
          <a-col :span="16">
            <a-form-model-item required prop="inputCode">
              <a-input v-model="model.inputCode" size="large" type="text" placeholder="请输入验证码">
                <a-icon slot="prefix" type="smile" :style="{ color: 'rgba(0,0,0,.25)' }"/>
              </a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8" style="text-align: right">
            <img v-if="requestCodeSuccess" style="margin-top: 2px;" :src="randCodeImage" @click="handleChangeCheckCode"/>
            <img v-else style="margin-top: 2px;" src="../../assets/checkcode.png" @click="handleChangeCheckCode"/>
          </a-col>
        </a-row>
      </a-form-model>
    </div>
</template>

<script>
  import { getAction } from '@/api/manage'
  import Vue from 'vue'
  import { mapActions } from 'vuex'

  export default {
    name: 'LoginAccount',
    data(){
      return {
        requestCodeSuccess: false,
        randCodeImage: '',
        currdatetime: '',
        loginType: 0,
        model:{
          username: '',
          password: '',
          inputCode: ''
        },
        validatorRules:{
          username: [
            { required: true, message: '请输入用户名!' },
            { validator: this.handleUsernameOrEmail }
          ],
          password: [{
            required: true, message: '请输入密码!', validator: 'click'
          }],
          inputCode: [{
            required: true, message: '请输入验证码!'
          }]
        }

      }
    },
    created() {
      this.handleChangeCheckCode();
    },
    methods:{
      ...mapActions(['Login']),
      /**刷新验证码*/
      handleChangeCheckCode(){
        this.currdatetime = new Date().getTime();
        this.model.inputCode = ''
        getAction(`/sys/randomImage/${this.currdatetime}`).then(res=>{
          if(res.success){
            this.randCodeImage = res.result
            this.requestCodeSuccess=true
          }else{
            this.$message.error(res.message)
            this.requestCodeSuccess=false
          }
        }).catch(()=>{
          this.requestCodeSuccess=false
        })
      },
      // 判断登录类型
      handleUsernameOrEmail (rule, value, callback) {
        const regex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
        if (regex.test(value)) {
          this.loginType = 0
        } else {
          this.loginType = 1
        }
        callback()
      },
      /**
       * 验证字段
       * @param arr
       * @param callback
       */
      validateFields(arr, callback){
        let promiseArray = []
        for(let item of arr){
          let p = new Promise((resolve, reject) => {
            this.$refs['form'].validateField(item, (err)=>{
              if(!err){
                resolve();
              }else{
                reject(err);
              }
            })
          });
          promiseArray.push(p)
        }
        Promise.all(promiseArray).then(()=>{
          callback()
        }).catch(err=>{
          callback(err)
        })
      },
      acceptUsername(username){
        this.model['username'] = username
      },
      //账号密码登录
      handleLogin(rememberMe){
        this.validateFields([ 'username', 'password', 'inputCode' ], (err)=>{
          if(!err){
            let loginParams = {
              username: this.model.username,
              password: this.model.password,
              captcha: this.model.inputCode,
              checkKey: this.currdatetime,
              remember_me: rememberMe,
            }
            console.log("登录参数", loginParams)
            this.Login(loginParams).then((res) => {
              this.$emit('success', res.result)
            }).catch((err) => {
              this.$emit('fail', err)
            });
          }else{
            this.$emit('validateFail')
          }
        })
      }


    }

  }
</script>

<style scoped>

</style>