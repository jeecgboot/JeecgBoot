<template>
  <div id="userLayout" :class="['user-layout-wrapper', device]">
    <div class="container">

      <ul class="slide-box">
        <li></li>
        <li></li>
        <li></li>
        <li></li>
      </ul>

      <vue-particles
        color="#6495ED"
        :particleOpacity="0.8"
        :particlesNumber="70"
        shapeType="circle"
        :particleSize="4"
        linesColor="#6495ED"
        :linesWidth="1.3"
        :lineLinked="true"
        :lineOpacity="0.6"
        :linesDistance="150"
        :moveSpeed="3"
        :hoverEffect="true"
        hoverMode="grab"
        :clickEffect="true"
        clickMode="push"
      >
      </vue-particles>

      <div class="top">
        <div class="header">
          <a href="/">
            <img src="~@/assets/dd1.png" class="logo" alt="logo">
            <span class="title" style="color: rgba(255, 255, 255, 1)">DADA-Pro数据中台</span>
          </a>
        </div>
        <div class="desc">
          账号密码登录
        </div>
      </div>
      <route-view></route-view>


    </div>
  </div>
</template>

<script>
  import RouteView from "@/components/layouts/RouteView"
  import { mixinDevice } from '@/utils/mixin.js'

  export default {
    name: "UserLayout",
    components: { RouteView },
    mixins: [mixinDevice],
    data () {
      return {}
    },
    mounted () {
      document.body.classList.add('userLayout')
    },
    beforeDestroy () {
      document.body.classList.remove('userLayout')
    },
  }
</script>

<style lang="scss" scoped>
  #userLayout.user-layout-wrapper {
    height: 100%;

    &.mobile {
      .container {
        .main {
          max-width: 368px;
          width: 98%;
        }
      }
    }

    .container {
      width: 100%;
      min-height: 100%;
      //background: #f0f2f5 url(~@/assets/background.svg) no-repeat 50%;
      background-size: 100%;
      padding: 10% 10% 0% 40%;
      position: relative;

      a {
        text-decoration: none;
      }

      .top {
        text-align: center;

        .header {
          height: 44px;
          line-height: 44px;

          .badge {
            position: absolute;
            display: inline-block;
            line-height: 1;
            vertical-align: middle;
            margin-left: -12px;
            margin-top: -10px;
            opacity: 0.8;
          }

          .logo {
            height: 104px;
            //vertical-align: top;
            margin-right: 16px;
            border-style: none;
          }

          .title {
            font-size: 33px;
            color: rgba(0, 0, 0, 1);
            font-family: "Chinese Quote", -apple-system, BlinkMacSystemFont, "Segoe UI", "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "Helvetica Neue", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
            font-weight: 600;
            position: relative;
            top: 2px;
          }
        }
        .desc {
          font-size: 18px;
          color: rgba(255, 255, 255, 1);
          margin-top: 22px;
        }
      }

      .main {
        min-width: 260px;
        width: 368px;
        margin: 0 auto;
      }

      .footer {
        position: absolute;
        width: 100%;
        bottom: 0;
        padding: 0 16px;
        margin: 48px 0 24px;
        text-align: center;

        .links {
          margin-bottom: 8px;
          font-size: 14px;
          a {
            color: rgba(0, 0, 0, 0.45);
            transition: all 0.3s;
            &:not(:last-child) {
              margin-right: 40px;
            }
          }
        }
        .copyright {
          color: rgba(0, 0, 0, 0.45);
          font-size: 14px;
        }

      }
    }

    #particles-js {
      position: fixed;
      top: 0;
      left: 0;
      z-index: -100;
      width: 100%;
      height: 100%;
    }

    .slide-box {
      position: fixed;
      width: 100%;
      height: 100%;
      background: #004961;
      top: 0px;
      left: 0px;
      z-index: -200;
      li {
        width: 100%;
        height: 100%;
        position: absolute;
        top: 0px;
        left: 0px;
        color: transparent;
        background-size: cover;
        background-position: 50% 50%;
        background-repeat: none;
        opacity: 0;
        z-index: 0;
        -webkit-backface-visibility: hidden;
        -webkit-animation: imageAnimation 48s linear infinite 0s;
        -moz-animation: imageAnimation 48s linear infinite 0s;
        -o-animation: imageAnimation 48s linear infinite 0s;
        -ms-animation: imageAnimation 48s linear infinite 0s;
        animation: imageAnimation 48s linear infinite 0s;

        &:nth-child(1) {
          background-image: url(../../assets/bg-1.jpg);
        }
        &:nth-child(2) {
          background-image: url(../../assets/bg-2.jpg);
          -webkit-animation-delay: 12s;
          -moz-animation-delay: 12s;
          -o-animation-delay: 12s;
          -ms-animation-delay: 12s;
          animation-delay: 12s;
        }
        &:nth-child(3) {
          background-image: url(../../assets/bg-3.jpg);
          -webkit-animation-delay: 24s;
          -moz-animation-delay: 24s;
          -o-animation-delay: 24s;
          -ms-animation-delay: 24s;
          animation-delay: 24s;
        }
        &:nth-child(4) {
          background-image: url(../../assets/bg-1.jpg);
          animation-delay: 36s;
        }
      }
    }
    @keyframes imageAnimation {
      0% {
        opacity: 0;
        animation-timing-function: ease-in;
      }
      8% {
        opacity: 1;
        transform: scale(1.15);
        animation-timing-function: ease-out;
      }
      17% {
        opacity: 1;
        transform: scale(1.2);
      }
      25% {
        opacity: 0;
        transform: scale(1.4);
      }
      100% {
        opacity: 0;
      }
    }
  }
</style>