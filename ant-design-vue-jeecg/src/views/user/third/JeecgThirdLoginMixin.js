/**
 *第三方登录
 */
import { mapActions } from 'vuex'
import { postAction } from '@api/manage'
import { timeFix } from '@/utils/util'

export const JeecgThirdLoginMixin = {
  data() {
    return {
      //第三方登录相关信息
      thirdLoginInfo: '',
      thirdPasswordShow: false,
      thirdLoginPassword: '',
      thirdLoginUser: '',
      thirdConfirmShow: false,
      thirdCreateUserLoding: false,
      thirdLoginState: false,
      //绑定手机号弹窗
      bindingPhoneModal: false,
      thirdPhone: '',
      thirdCaptcha: '',
      //获取验证码按钮30s之内是否可点击
      thirdState: {
        time: 30,
        smsSendBtn: false
      },
      //第三方用户UUID
      thirdUserUuid: '',
      thirdType: '',
      url: {
        bindingThirdPhone: '/sys/thirdLogin/bindingThirdPhone'
      }
    }
  },
  created() {
  },
  methods: {
    ...mapActions(['ThirdLogin']),
    //第三方登录
    onThirdLogin(source) {
      let url = window._CONFIG['domianURL'] + `/sys/thirdLogin/render/${source}`
      window.open(url, `login ${source}`, 'height=500, width=500, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=n o, status=no')
      let that = this
      that.thirdType = source
      that.thirdLoginInfo = ''
      that.thirdLoginState = false
      let receiveMessage = function(event) {
        let token = event.data
        if (typeof token === 'string') {
          //如果是字符串类型 说明是token信息
          if (token === '登录失败') {
            that.$message.warning(token)
          } else if (token.includes('绑定手机号')) {
            that.bindingPhoneModal = true
            let strings = token.split(',')
            that.thirdUserUuid = strings[1]
          } else {
            that.doThirdLogin(token)
          }
        } else if (typeof token === 'object') {
          //对象类型 说明需要提示是否绑定现有账号
          if (token['isObj'] === true) {
            that.thirdConfirmShow = true
            that.thirdLoginInfo = { ...token }
          }
        } else {
          that.$message.warning('不识别的信息传递')
        }
      }
      window.addEventListener('message', receiveMessage, false)
    },
    // 根据token执行登录
    doThirdLogin(token) {
      if (this.thirdLoginState === false) {
        this.thirdLoginState = true
        let param={};
        param.thirdType=this.thirdType
        param.token=token
        this.ThirdLogin(param).then(res => {
          if (res.success) {
            this.loginSuccess()
          } else {
            this.requestFailed(res)
          }
        })
      }
    },
    // 绑定已有账号 需要输入密码
    thirdLoginUserBind() {
      this.thirdLoginPassword = ''
      this.thirdLoginUser = this.thirdLoginInfo.uuid
      this.thirdConfirmShow = false
      this.thirdPasswordShow = true
    },
    //创建新账号
    thirdLoginUserCreate() {
      this.thirdCreateUserLoding = true
      // 账号名后面添加两位随机数
      this.thirdLoginInfo['suffix'] = parseInt(Math.random() * 98 + 1)
      //this.thirdLoginInfo.operateCode = 123 //测试校验失败
      postAction('/sys/third/user/create', this.thirdLoginInfo).then(res => {
        if (res.success) {
          let token = res.result
          console.log('thirdCreateNewAccount', token)
          this.doThirdLogin(token)
          this.thirdConfirmShow = false
        } else {
          this.$message.warning(res.message)
        }
      }).finally(() => {
        this.thirdCreateUserLoding = false
      })
    },
    // 核实密码
    thirdLoginCheckPassword() {
      //this.thirdLoginInfo.operateCode = 123 //测试校验失败
      let param = Object.assign({}, this.thirdLoginInfo, { password: this.thirdLoginPassword })
      postAction('/sys/third/user/checkPassword', param).then(res => {
        if (res.success) {
          this.thirdLoginNoPassword()
          this.doThirdLogin(res.result)
        } else {
          this.$message.warning(res.message)
        }
      })
    },
    // 没有密码 取消操作
    thirdLoginNoPassword() {
      this.thirdPasswordShow = false
      this.thirdLoginPassword = ''
      this.thirdLoginUser = ''
    },
    //获取第三方验证码
    getThirdCaptcha() {
      let that = this
      if (!this.thirdPhone) {
        that.cmsFailed('请输入手机号')
      } else {
        this.thirdState.smsSendBtn = true
        let interval = window.setInterval(() => {
          if (that.thirdState.time-- <= 0) {
            that.thirdState.time = 30
            that.thirdState.smsSendBtn = false
            window.clearInterval(interval)
          }
        }, 1000)
        const hide = this.$message.loading('验证码发送中..', 0)
        let smsParams = {}
        smsParams.mobile = this.thirdPhone
        smsParams.smsmode = '0'
        postAction('/sys/sms', smsParams).then(res => {
          if (!res.success) {
            setTimeout(hide, 0)
            this.cmsFailed(res.message)
          }
          setTimeout(hide, 500)
        }).catch(err => {
          setTimeout(hide, 1)
          clearInterval(interval)
          that.thirdState.time = 30
          that.thirdState.smsSendBtn = false
          this.requestFailed(err)
        })
      }
    },
    //绑定手机号点击确定按钮
    thirdHandleOk() {
      let bingingParams = {}
      bingingParams.mobile = this.thirdPhone
      bingingParams.captcha = this.thirdCaptcha
      bingingParams.thirdUserUuid = this.thirdUserUuid
      postAction(this.url.bindingThirdPhone, bingingParams).then(res => {
        if (res.success) {
          this.bindingPhoneModal = false
          this.doThirdLogin(res.result)
        } else {
          this.$message.warning(res.message)
        }
      })
    },
    loginSuccess () {
      // update-begin- author:sunjianlei --- date:20190812 --- for: 登录成功后不解除禁用按钮，防止多次点击
      // this.loginBtn = false
      // update-end- author:sunjianlei --- date:20190812 --- for: 登录成功后不解除禁用按钮，防止多次点击
      this.$router.push({ path: "/dashboard/analysis" }).catch(()=>{
        console.log('登录跳转首页出错,这个错误从哪里来的')
      })
      this.$notification.success({
        message: '欢迎',
        description: `${timeFix()}，欢迎回来`,
      });
    },
    cmsFailed(err){
      this.$notification[ 'error' ]({
        message: "登录失败",
        description:err,
        duration: 4,
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