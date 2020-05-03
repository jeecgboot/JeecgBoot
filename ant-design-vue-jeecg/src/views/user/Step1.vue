<template>
  <div class="main">

    <a-form style="max-width: 500px; margin: 40px auto 0;" :form="form" @keyup.enter.native="nextStep">
      <a-form-item>
        <a-input
          v-decorator="['username',validatorRules.username]"
          size="large"
          type="text"
          autocomplete="false"
          placeholder="请输入用户账号或手机号">
          <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
        </a-input>
      </a-form-item>
      <a-row :gutter="0">
        <a-col :span="14">
          <a-form-item>
            <a-input
              v-decorator="['inputCode',validatorRules.inputCode]"
              size="large"
              type="text"
              @change="inputCodeChange"
              placeholder="请输入验证码">
              <a-icon slot="prefix" v-if=" inputCodeContent==verifiedCode " type="smile"
                      :style="{ color: 'rgba(0,0,0,.25)' }"/>
              <a-icon slot="prefix" v-else type="frown" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input>
          </a-form-item>
        </a-col>
        <a-col :span="10" style="text-align: right">
          <img v-if="requestCodeSuccess" style="margin-top: 2px;" :src="randCodeImage" @click="handleChangeCheckCode"/>
          <img v-else style="margin-top: 2px;" src="../../assets/checkcode.png" @click="handleChangeCheckCode"/>
        </a-col>
      </a-row>
      <a-form-item :wrapperCol="{span: 19, offset: 5}">
        <router-link style="float: left;line-height: 40px;" :to="{ name: 'login' }">使用已有账户登录</router-link>
        <a-button type="primary" @click="nextStep">下一步</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
  import { getAction,postAction } from '@/api/manage'
  import { checkOnlyUser } from '@/api/api'

  export default {
    name: "Step1",
    data() {
      return {
        form: this.$form.createForm(this),
        inputCodeContent: "",
        inputCodeNull: true,
        verifiedCode: "",
        validatorRules: {
          username: {rules: [{required: false}, {validator: this.validateInputUsername}]},
          inputCode: {rules: [{required: true, message: '请输入验证码!'}]},
        },
        randCodeImage:'',
        requestCodeSuccess:true,
        currdatetime:''

      }
    },
    created(){
      this.handleChangeCheckCode();
    },
    methods: {
      handleChangeCheckCode(){
        this.currdatetime = new Date().getTime();
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
      nextStep() {
        let that = this
        this.form.validateFields((err, values) => {
          if (!err) {
            let isPhone = false;
            var params = {}
            var reg = /^[1-9]\d*$|^0$/;
            var username = values.username;
            if (reg.test(username) == true) {
              params.phone = username;
              isPhone = true
            } else {
              params.username = username;
            }
            that.validateInputCode().then(()=>{
              getAction("/sys/user/querySysUser", params).then((res) => {
                if (res.success) {
                  var userList = {
                    username: res.result.username,
                    phone: res.result.phone,
                    isPhone: isPhone
                  };
                  setTimeout(function () {
                    that.$emit('nextStep', userList)
                  })
                }
              });
            })
          }
        })

      },
      validateInputCode() {
        return new Promise((resolve,reject)=>{
          postAction("/sys/checkCaptcha",{
            captcha:this.inputCodeContent,
            checkKey:this.currdatetime
          }).then(res=>{
            if(res.success){
              resolve();
            }else{
              this.$message.error(res.message)
              reject();
            }
          });
        })
      },
      inputCodeChange(e) {
        this.inputCodeContent = e.target.value;
        console.log(this.inputCodeContent)
        if (!e.target.value || 0 == e.target.value) {
          this.inputCodeNull = true
        } else {
          this.inputCodeContent = this.inputCodeContent.toLowerCase()
          this.inputCodeNull = false
        }
      },
      generateCode(value) {
        this.verifiedCode = value.toLowerCase();
        console.log(this.verifiedCode);
      },
      validateInputUsername(rule, value, callback) {
        console.log(value);
        var reg = /^[0-9]+.?[0-9]*/;
        if (!value) {
          callback("请输入用户名和手机号！");
        }

        //判断用户输入账号还是手机号码
        if (reg.test(value)) {
          var params = {
            phone: value,
          };
          checkOnlyUser(params).then((res) => {
            if (res.success) {
              callback("用户名不存在!")
            } else {
              callback()
            }
          })
        } else {
          var params = {
            username: value,
          };
          checkOnlyUser(params).then((res) => {
            if (res.success) {
              callback("用户名不存在!")
            } else {
              callback()
            }
          })
        }
      },

    }
  }
</script>

<style scoped>

</style>