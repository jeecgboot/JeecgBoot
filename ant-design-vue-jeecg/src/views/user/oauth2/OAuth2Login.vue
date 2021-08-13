<template>
  <div>
    <div id="loader-wrapper">
      <div id="loader"></div>
      <div class="loader-section section-left"></div>
      <div class="loader-section section-right"></div>
      <div class="load_title">正在登录 JeecgBoot 低代码平台，请耐心等待</div>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import { isOAuth2AppEnv, timeFix } from '@/utils/util'
import { INDEX_MAIN_PAGE_PATH } from '@/store/mutation-types'

export default {
  name: 'OAuth2Login',
  data() {
    return {
      env: {
        thirdApp: false,
        wxWork: false,
        dingtalk: false,
      },
    }
  },
  beforeCreate() {
    // 如果当前 不是 OAuth2APP环境，就重定向到 /user/login 页面
    if (!isOAuth2AppEnv()) {
      this.$router.replace({path: '/user/login'})
    }
  },
  created() {
    this.checkEnv()
    this.doOAuth2Login()
  },
  methods: {
    ...mapActions(['ThirdLogin']),

    /** 检测当前的环境 */
    checkEnv() {
      // 判断当时是否是企业微信环境
      if (/wxwork/i.test(navigator.userAgent)) {
        this.env.thirdApp = true
        this.env.wxWork = true
      }
      // 判断当时是否是钉钉环境
      if (/dingtalk/i.test(navigator.userAgent)) {
        this.env.thirdApp = true
        this.env.dingtalk = true
      }
    },

    /** 进行OAuth2登录操作 */
    doOAuth2Login() {
      if (this.env.thirdApp) {
        // 判断是否携带了Token，是就说明登录成功
        if (this.$route.query.oauth2LoginToken) {
          this.thirdType = this.$route.query.thirdType
          let token = this.$route.query.oauth2LoginToken
          this.doThirdLogin(token)
        } else if (this.env.wxWork) {
          this.doWechatEnterpriseOAuth2Login()
        } else if (this.env.dingtalk) {
          this.doDingTalkOAuth2Login()
        }
      }
    },

    // 根据token执行登录
    doThirdLogin(token) {
      let param = {}
      param.thirdType = this.thirdType
      param.token = token
      this.ThirdLogin(param).then(res => {
        if (res.success) {
          this.loginSuccess()
        } else {
          this.requestFailed(res)
        }
      })
    },
    loginSuccess() {
      // 登陆成功，重定向到主页
      this.$router.replace({path: INDEX_MAIN_PAGE_PATH})
      // TODO 这个提示是否还需要？
      this.$notification.success({
        message: '欢迎',
        description: `${timeFix()}，欢迎回来`,
      })
    },
    requestFailed(err) {
      this.$error({
        title: '登录失败',
        content: ((err.response || {}).data || {}).message || err.message || '请求出现错误，请稍后再试',
        okText: '重新登陆',
        onOk() {
          window.location.reload()
        },
        onCancel() {
          window.location.reload()
        },
      })
    },

    /** 企业微信OAuth2登录 */
    doWechatEnterpriseOAuth2Login() {
      this.sysOAuth2Login('wechat_enterprise')
    },

    /** 钉钉OAuth2登录 */
    doDingTalkOAuth2Login() {
      this.sysOAuth2Login('dingtalk')
    },

    /** 后台构造oauth2登录地址 */
    sysOAuth2Login(source) {
      let url = `${window._CONFIG['domianURL']}/sys/thirdLogin/oauth2/${source}/login`
      url += `?state=${encodeURIComponent(window.location.origin)}`
      window.location.href = url
    },

  },
}
</script>

<style scoped>

</style>