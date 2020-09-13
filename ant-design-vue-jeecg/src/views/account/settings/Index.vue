<template>
  <div class="page-header-index-wide">
    <a-card :bordered="false" :bodyStyle="{ padding: '16px 0', height: '100%' }" :style="{ height: '100%' }">
      <div class="account-settings-info-main" :class="device" :style=" 'min-height:'+ mainInfoHeight ">
        <div class="account-settings-info-left">
          <a-menu
            :mode="device == 'mobile' ? 'horizontal' : 'inline'"
            :default-selected-keys="['settings']"
            :style="{ border: '0', width: device == 'mobile' ? '560px' : 'auto'}"
            type="inner"
            @openChange="onOpenChange"
          >
            <a-menu-item key="settings">
              <a  @click="settingsClick()">
                基本设置
              </a>
            </a-menu-item>
            <a-menu-item key="security">
              <a @click="securityClick()">安全设置</a>
            </a-menu-item>
            <a-menu-item key="custom">
              <a @click="customClick()"> 个性化</a>
            </a-menu-item>
            <a-menu-item key="binding">
              <a @click="bindingClick()">账户绑定</a>
            </a-menu-item>
            <a-menu-item key="notification">
              <a @click="notificationClick()">新消息通知</a>
            </a-menu-item>
          </a-menu>
        </div>
        <div class="account-settings-info-right">
          <div class="account-settings-info-title">
            <span>{{ title }}</span>
          </div>
          <security ref="security" v-if="security"></security>
          <base-setting ref="baseSetting" v-if="baseSetting"></base-setting>
          <custom ref="custom" v-if="custom"></custom>
          <notification ref="notification" v-if="notification"></notification>
          <binding ref="binding" v-if="binding"></binding>
        </div>
      </div>
    </a-card>
  </div>
</template>

<script>
  import PageLayout from '@/components/page/PageLayout'
  import RouteView from "@/components/layouts/RouteView"
  import { mixinDevice } from '@/utils/mixin.js'
  import security from './Security'
  import baseSetting from './BaseSetting'
  import  custom from  './Custom'
  import notification from './Notification'
  import binding from './Binding'
  export default {
    components: {
      RouteView,
      PageLayout,
      security,
      baseSetting,
      custom,
      notification,
      binding
    },
    mixins: [mixinDevice],
    data () {
      return {
        // horizontal  inline
        mode: 'inline',
        mainInfoHeight:"100%",
        openKeys: [],
        defaultSelectedKeys: [],

        // cropper
        preview: {},
        option: {
          img: '/avatar2.jpg',
          info: true,
          size: 1,
          outputType: 'jpeg',
          canScale: false,
          autoCrop: true,
          // 只有自动截图开启 宽度高度才生效
          autoCropWidth: 180,
          autoCropHeight: 180,
          fixedBox: true,
          // 开启宽度和高度比例
          fixed: true,
          fixedNumber: [1, 1]
        },

        pageTitle: '',
        title:"基本设置",
        security:false,
        baseSetting:true,
        custom:false,
        notification:false,
        binding:false
      }
    },
    created () {
      this.updateMenu()
    },
    mounted(){
      this.mainInfoHeight = (window.innerHeight-285)+"px";
    },
    methods: {
      onOpenChange (openKeys) {
        this.openKeys = openKeys
      },
      updateMenu () {
        let routes = this.$route.matched.concat()
        this.defaultSelectedKeys = [ routes.pop().path ]
      },
      //update-begin--Author:wangshuai  Date:20200729 for：聚合路由错误 issues#1441--------------------
      settingsClick(){
        this.security=false
        this.custom=false
        this.notification=false
        this.binding=false
        this.baseSetting=true
        this.title="基本设置"
      },
      securityClick(){
        this.baseSetting=false
        this.custom=false;
        this.notification=false
        this.binding=false
        this.security=true
        this.title="安全设置"
      },
      notificationClick(){
        this.security=false
        this.custom=false
        this.baseSetting=false
        this.binding=false
        this.notification=true
        this.title="新消息通知"
      },
      bindingClick(){
        this.security=false
        this.baseSetting=false
        this.notification=false;
        this.custom=false;
        this.binding=true
        this.title="账号绑定"
      },
      customClick(){
        this.security=false
        this.baseSetting=false
        this.notification=false;
        this.binding=false
        this.custom=true;
        this.title="个性化"
      }
      //update-end--Author:wangshuai  Date:20200729 for：聚合路由错误 issues#1441--------------------
    },
  }
</script>

<style lang="less" scoped>
  .account-settings-info-main {
    width: 100%;
    display: flex;
    height: 100%;
    overflow: auto;

    &.mobile {
      display: block;

      .account-settings-info-left {
        border-right: unset;
        border-bottom: 1px solid #e8e8e8;
        width: 100%;
        height: 50px;
        overflow-x: auto;
        overflow-y: scroll;
      }
      .account-settings-info-right {
        padding: 20px 40px;
      }
    }

    .account-settings-info-left {
      border-right: 1px solid #e8e8e8;
      width: 224px;
    }

    .account-settings-info-right {
      flex: 1 1;
      padding: 8px 40px;

      .account-settings-info-title {
        color: rgba(0,0,0,.85);
        font-size: 20px;
        font-weight: 500;
        line-height: 28px;
        margin-bottom: 12px;
      }
      .account-settings-info-view {
        padding-top: 12px;
      }
    }
  }

</style>