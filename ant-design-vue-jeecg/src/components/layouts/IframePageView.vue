<template>

    <iframe  :id="id" :src="url" frameborder="0" width="100%" height="800px" scrolling="auto"></iframe>

</template>

<script>
  import Vue from 'vue'
  import { ACCESS_TOKEN } from "@/store/mutation-types"
  import { TENANT_ID } from "@/store/mutation-types"
  import PageLayout from '../page/PageLayout'
  import RouteView from './RouteView'
  import {mixinDevice} from '@/utils/mixin'

  export default {
    name: "IframePageContent",
    inject:['closeCurrent'],
    mixins: [mixinDevice],
    data () {
      return {
        url: "",
        id:""
      }
    },
    created () {
      this.goUrl()
    },
    updated () {
      this.goUrl()
    },
    watch: {
      $route(to, from) {
        this.goUrl();
      }
    },
    methods: {
      goUrl () {
        let url = this.$route.meta.url
        let id = this.$route.path
        this.id = id
        //url = "http://www.baidu.com"
        console.log("------url------"+url)
        if (url !== null && url !== undefined) {
          //-----------------------------------------------------------------------------------------
          //url支持通过 ${token}方式传递当前登录TOKEN
          let tokenStr = "${token}";
          if(url.indexOf(tokenStr)!=-1) {
            let token = Vue.ls.get(ACCESS_TOKEN);
            this.url = url.replace(tokenStr, token);
          } else {
            this.url = url
          }

          //update-begin---author:wangshuai ---date:20220711  for：[VUEN-1638]菜单tenantId需要动态生成------------
          let tenantIdStr = '${tenantId}'
          let tenantUrl = this.url
          if (tenantUrl.indexOf(tenantIdStr) != -1) {
            let tenantId = Vue.ls.get(TENANT_ID)
            this.url = tenantUrl.replace(tenantIdStr, tenantId)
          }
          //update-end---author:wangshuai ---date:20220711  for：[VUEN-1638]菜单tenantId需要动态生成--------------
          //-----------------------------------------------------------------------------------------

          // 是否允许打开外部页面，需要非Mobile模式且打开多页签模式
          let allowOpen = !this.isMobile() && this.$store.state.app.multipage
          /*update_begin author:wuxianquan date:20190908 for:判断打开方式，新窗口打开时this.$route.meta.internalOrExternal==true */
          if(allowOpen && this.$route.meta.internalOrExternal === true){
            this.closeCurrent();
            window.open(this.url);
          }
          /*update_end author:wuxianquan date:20190908 for:判断打开方式，新窗口打开时this.$route.meta.internalOrExternal==true */

        }
      }
    }
  }
</script>

<style>
</style>