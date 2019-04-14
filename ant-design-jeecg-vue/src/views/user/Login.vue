<template>
  <div class="main">
    <a-form class="user-layout-login" ref="formLogin" :autoFormCreate="(form)=>{this.form = form}" id="formLogin">
      <a-tabs
        :activeKey="customActiveKey"
        :tabBarStyle="{ textAlign: 'center', borderBottom: 'unset' }"
        @change="handleTabClick">
        <a-tab-pane key="tab1" tab="账号密码登陆">

          <a-form-item
            fieldDecoratorId="username"
            :fieldDecoratorOptions="{rules: [{ required: true, message: '请输入帐户名或邮箱' }, { validator: this.handleUsernameOrEmail }], validateTrigger: 'change'}">
            <a-input size="large" type="text" placeholder="请输入帐户名 / jeecg">
              <a-icon slot="prefix" type="user" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input>
          </a-form-item>

          <a-form-item
            fieldDecoratorId="password"
            :fieldDecoratorOptions="{rules: [{ required: true, message: '请输入密码' }], validateTrigger: 'blur'}">
            <a-input size="large" type="password" autocomplete="false" placeholder="密码 / 123456">
              <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input>
          </a-form-item>
        </a-tab-pane>
        <a-tab-pane key="tab2" tab="手机号登陆">
          <a-form-item
            fieldDecoratorId="mobile"
            :fieldDecoratorOptions="{rules: [{ required: true, pattern: /^1[34578]\d{9}$/, message: '请输入正确的手机号' }], validateTrigger: 'change'}">
            <a-input size="large" type="text" placeholder="手机号">
              <a-icon slot="prefix" type="mobile" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input>
          </a-form-item>

          <a-row :gutter="16">
            <a-col class="gutter-row" :span="16">
              <a-form-item
                fieldDecoratorId="captcha"
                :fieldDecoratorOptions="{rules: [{ required: true, message: '请输入验证码' }], validateTrigger: 'blur'}">
                <a-input size="large" type="text" placeholder="验证码">
                  <a-icon slot="prefix" type="mail" :style="{ color: 'rgba(0,0,0,.25)' }"/>
                </a-input>
              </a-form-item>
            </a-col>
            <a-col class="gutter-row" :span="8">
              <a-button
                class="getCaptcha"
                tabindex="-1"
                :disabled="state.smsSendBtn"
                @click.stop.prevent="getCaptcha"
                v-text="!state.smsSendBtn && '获取验证码' || (state.time+' s')"></a-button>
            </a-col>
          </a-row>
        </a-tab-pane>
      </a-tabs>

      <a-form-item>
        <a-checkbox v-model="formLogin.rememberMe">自动登陆</a-checkbox>
        <router-link :to="{ name: 'recover', params: { user: 'aaa'} }" class="forge-password" style="float: right;">
          忘记密码
        </router-link>
      </a-form-item>

      <a-form-item style="margin-top:24px">
        <a-button
          size="large"
          type="primary"
          htmlType="submit"
          class="login-button"
          :loading="loginBtn"
          @click.stop.prevent="handleSubmit"
          :disabled="loginBtn">确定
        </a-button>
      </a-form-item>

     <!-- <div class="user-login-other">
        <span>其他登陆方式</span>
        <a><a-icon class="item-icon" type="alipay-circle"></a-icon></a>
        <a><a-icon class="item-icon" type="taobao-circle"></a-icon></a>
        <a><a-icon class="item-icon" type="weibo-circle"></a-icon></a>
        <router-link class="register" :to="{ name: 'register' }">
          注册账户
        </router-link>
      </div>-->
    </a-form>

    <two-step-captcha
      v-if="requiredTwoStepCaptcha"
      :visible="stepCaptchaVisible"
      @success="stepCaptchaSuccess"
      @cancel="stepCaptchaCancel"></two-step-captcha>
  </div>
</template>

<script>
  //import md5 from "md5"
  import api from '@/api'
  import TwoStepCaptcha from '@/components/tools/TwoStepCaptcha'
  import { mapActions } from "vuex"
  import { timeFix } from "@/utils/util"
  import Vue from 'vue'
  import { ACCESS_TOKEN } from "@/store/mutation-types"

  export default {
    components: {
      TwoStepCaptcha
    },
    data () {
      return {
        customActiveKey: "tab1",
        loginBtn: false,
        // login type: 0 email, 1 username, 2 telephone
        loginType: 0,
        requiredTwoStepCaptcha: false,
        stepCaptchaVisible: false,
        form: null,
        state: {
          time: 60,
          smsSendBtn: false,
        },
        formLogin: {
          username: "",
          password: "",
          captcha: "",
          mobile: "",
          rememberMe: true
        },
      }
    },
    created () {
      Vue.ls.remove(ACCESS_TOKEN)
      // update-begin- --- author:scott ------ date:20190225 ---- for:暂时注释，未实现登录验证码功能
//      this.$http.get('/auth/2step-code')
//        .then(res => {
//          this.requiredTwoStepCaptcha = res.result.stepCode
//        }).catch(err => {
//          console.log('2step-code:', err)
//        })
      // update-end- --- author:scott ------ date:20190225 ---- for:暂时注释，未实现登录验证码功能
     // this.requiredTwoStepCaptcha = true
      
    },
    methods: {
      ...mapActions([ "Login", "Logout" ]),
      // handler
      handleUsernameOrEmail (rule, value, callback) {
        const regex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
        if (regex.test(value)) {
          this.loginType = 0
        } else {
          this.loginType = 1
        }
        callback()
      },
      handleTabClick (key) {
        this.customActiveKey = key
        // this.form.resetFields()
      },
      handleSubmit () {
        let that = this
        let flag = false

        let loginParams = {
          remember_me: that.formLogin.rememberMe
        };

        // 使用账户密码登陆
        if (that.customActiveKey === 'tab1') {
          that.form.validateFields([ 'username', 'password' ], { force: true }, (err, values) => {
            if (!err) {
              flag = true
              loginParams[!that.loginType ? 'email' : 'username'] = values.username
              //loginParams.password = md5(values.password)
              loginParams.password = values.password
            }
          })
        // 使用手机号登陆
        } else {
          that.form.validateFields([ 'mobile', 'captcha' ], { force: true }, (err, values) => {
            if (!err) {
              flag = true
              loginParams = Object.assign(loginParams, values)
            }
          })
        }

        if (!flag) return

        that.loginBtn = true

        that.Login(loginParams).then(() => {
          if (that.requiredTwoStepCaptcha) {
            that.stepCaptchaVisible = true
          } else {
            that.loginSuccess()
          }
        }).catch((err) => {
          that.requestFailed(err);
        })

      },
      getCaptcha (e) {
        e.preventDefault()
        let that = this

        this.form.validateFields([ 'mobile' ], { force: true },
          (err) => {
            if (!err) {
              this.state.smsSendBtn = true;

              let interval = window.setInterval(() => {
                if (that.state.time-- <= 0) {
                  that.state.time = 60;
                  that.state.smsSendBtn = false;
                  window.clearInterval(interval);
                }
              }, 1000);

              const hide = this.$message.loading('验证码发送中..', 0);
              this.$http.post(api.SendSms, { mobile: that.formLogin.mobile })
                .then(res => {
                  setTimeout(hide, 2500);
                  this.$notification[ 'success' ]({
                    message: '提示',
                    description: '验证码获取成功，您的验证码为：' + res.result.captcha,
                    duration: 8
                  })
                })
                .catch(err => {
                  setTimeout(hide, 1);
                  clearInterval(interval);
                  that.state.time = 60;
                  that.state.smsSendBtn = false;
                  this.requestFailed(err);
                });
            }
          }
        );
      },
      stepCaptchaSuccess () {
        this.loginSuccess()
      },
      stepCaptchaCancel () {
        this.Logout().then(() => {
          this.loginBtn = false
          this.stepCaptchaVisible = false
        })
      },
      loginSuccess () {
        this.loginBtn = false
        this.$router.push({ name: "dashboard" })
        this.$notification.success({
          message: '欢迎',
          description: `${timeFix()}，欢迎回来`,
        });
      },
      requestFailed (err) {
        this.$notification[ 'error' ]({
          message: '登录失败',
          description: ((err.response || {}).data || {}).message || err.message || "请求出现错误，请稍后再试",
          duration: 4,
        });
        this.loginBtn = false;
      },
    }
  }
</script>

<style lang="scss" scoped>

  .user-layout-login {
    label {
      font-size: 14px;
    }

    .getCaptcha {
      display: block;
      width: 100%;
      height: 40px;
    }

    .forge-password {
      font-size: 14px;
    }

    button.login-button {
      padding: 0 15px;
      font-size: 16px;
      height: 40px;
      width: 100%;
    }

    .user-login-other {
      text-align: left;
      margin-top: 24px;
      line-height: 22px;

      .item-icon {
        font-size: 24px;
        color: rgba(0,0,0,.2);
        margin-left: 16px;
        vertical-align: middle;
        cursor: pointer;
        transition: color .3s;

        &:hover {
          color: #1890ff;
        }
      }

      .register {
        float: right;
      }
    }
  }

</style>