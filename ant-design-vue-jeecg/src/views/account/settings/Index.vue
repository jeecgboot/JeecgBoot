<template>
  <div class="page-header-index-wide">
    <a-card :bordered="false" :bodyStyle="{ padding: '16px 0', height: '100%' }" :style="{ height: '100%' }">
      <div class="account-settings-info-main" :class="device" :style=" 'min-height:'+ mainInfoHeight ">
        <div class="account-settings-info-left">
          <a-menu
            :mode="device == 'mobile' ? 'horizontal' : 'inline'"
            :style="{ border: '0', width: device == 'mobile' ? '560px' : 'auto'}"
            :defaultSelectedKeys="defaultSelectedKeys"
            type="inner"
            @openChange="onOpenChange"
          >
            <a-menu-item key="/account/settings/base">
              <router-link :to="{ name: 'account-settings-base' }">
                基本设置
              </router-link>
            </a-menu-item>
            <a-menu-item key="/account/settings/security">
              <router-link :to="{ name: 'account-settings-security' }">
                安全设置
              </router-link>
            </a-menu-item>
            <a-menu-item key="/account/settings/custom">
              <router-link :to="{ name: 'account-settings-custom' }">
                个性化
              </router-link>
            </a-menu-item>
            <a-menu-item key="/account/settings/binding">
              <router-link :to="{ name: 'account-settings-binding' }">
                账户绑定
              </router-link>
            </a-menu-item>
            <a-menu-item key="/account/settings/notification">
              <router-link :to="{ name: 'account-settings-notification' }">
                新消息通知
              </router-link>
            </a-menu-item>
          </a-menu>
        </div>
        <div class="account-settings-info-right">
          <div class="account-settings-info-title">
            <span>{{ $route.meta.title }}</span>
          </div>
          <route-view></route-view>
        </div>
      </div>
    </a-card>
  </div>
</template>

<script>
  import PageLayout from '@/components/page/PageLayout'
  import RouteView from "@/components/layouts/RouteView"
  import { mixinDevice } from '@/utils/mixin.js'

  export default {
    components: {
      RouteView,
      PageLayout
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

        pageTitle: ''
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
      }
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