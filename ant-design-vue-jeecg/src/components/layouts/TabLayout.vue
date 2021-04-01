<template>
  <global-layout @dynamicRouterShow="dynamicRouterShow">
    <!-- update-begin- author:sunjianlei --- date:20191009 --- for: 提升右键菜单的层级 -->
    <contextmenu :itemList="menuItemList" :visible.sync="menuVisible" style="z-index: 9999;" @select="onMenuSelect"/>
    <!-- update-end- author:sunjianlei --- date:20191009 --- for: 提升右键菜单的层级 -->
    <a-tabs
      @contextmenu.native="e => onContextmenu(e)"
      v-if="multipage"
      :active-key="activePage"
      class="tab-layout-tabs"
      style="height:52px"
      :hide-add="true"
      type="editable-card"
      @change="changePage"
      @tabClick="tabCallBack"
      @edit="editPage">
      <a-tab-pane :id="page.fullPath" :key="page.fullPath" v-for="page in pageList" :closable="!(page.meta.title=='首页')">
        <span slot="tab" :pagekey="page.fullPath">{{ page.meta.title }}</span>
      </a-tab-pane>
    </a-tabs>
    <div style="margin: 12px 12px 0;">
      <!-- update-begin-author:taoyan date:20201221 for:此处删掉transition标签 不知道为什么加上后 页面路由切换的时候即1及菜单切到2及菜单的时候 两个菜单页面会同时出现300-500秒左右 -->
      <keep-alive v-if="multipage">
        <router-view v-if="reloadFlag"/>
      </keep-alive>
      <template v-else>
        <router-view v-if="reloadFlag"/>
      </template>
      <!-- update-end-author:taoyan date:20201221 for:此处删掉transition标签 不知道为什么加上后 页面路由切换的时候即1及菜单切到2及菜单的时候 两个菜单页面会同时出现300-500秒左右 -->
    </div>
  </global-layout>
</template>

<script>
  import GlobalLayout from '@/components/page/GlobalLayout'
  import Contextmenu from '@/components/menu/Contextmenu'
  import { mixin, mixinDevice } from '@/utils/mixin.js'
  import { triggerWindowResizeEvent } from '@/utils/util'
  const indexKey = '/dashboard/analysis'
  import Vue from 'vue'
  import { CACHE_INCLUDED_ROUTES } from "@/store/mutation-types"

  export default {
    name: 'TabLayout',
    components: {
      GlobalLayout,
      Contextmenu
    },
    mixins: [mixin, mixinDevice],
    data() {
      return {
        pageList: [],
        linkList: [],
        activePage: '',
        menuVisible: false,
        menuItemList: [
          { key: '4', icon: 'reload', text: '刷 新' },
          { key: '1', icon: 'arrow-left', text: '关闭左侧' },
          { key: '2', icon: 'arrow-right', text: '关闭右侧' },
          { key: '3', icon: 'close', text: '关闭其它' }
        ],
        reloadFlag:true
      }
    },
    /* update_begin author:wuxianquan date:20190828 for: 关闭当前tab页，供子页面调用 ->望菜单能配置外链，直接弹出新页面而不是嵌入iframe #428 */
    provide(){
      return{
        closeCurrent:this.closeCurrent
      }
    },
    /* update_end author:wuxianquan date:20190828 for: 关闭当前tab页，供子页面调用->望菜单能配置外链，直接弹出新页面而不是嵌入iframe #428 */
    computed: {
      multipage() {
        //判断如果是手机模式，自动切换为单页面模式
        if (this.isMobile()) {
          return false
        } else {
          return this.$store.state.app.multipage
        }
      }
    },
    created() {
      if (this.$route.path != indexKey) {
        this.addIndexToFirst()
      }
      // 复制一个route对象出来，不能影响原route
      let currentRoute = Object.assign({}, this.$route)
      currentRoute.meta = Object.assign({}, currentRoute.meta)
      // update-begin-author:sunjianlei date:20191223 for: 修复刷新后菜单Tab名字显示异常
      let storeKey = 'route:title:' + currentRoute.fullPath
      let routeTitle = this.$ls.get(storeKey)
      if (routeTitle) {
        currentRoute.meta.title = routeTitle
      }
      // update-end-author:sunjianlei date:20191223 for: 修复刷新后菜单Tab名字显示异常
      this.pageList.push(currentRoute)
      this.linkList.push(currentRoute.fullPath)
      this.activePage = currentRoute.fullPath
    },
    mounted() {
    },
    watch: {
      '$route': function(newRoute) {
        //console.log("新的路由",newRoute)
        this.activePage = newRoute.fullPath
        if (!this.multipage) {
          this.linkList = [newRoute.fullPath]
          this.pageList = [Object.assign({},newRoute)]
        // update-begin-author:taoyan date:20200211 for: TASK #3368 【路由缓存】首页的缓存设置有问题，需要根据后台的路由配置来实现是否缓存
        } else if(indexKey==newRoute.fullPath) {
          //首页时 判断是否缓存 没有缓存 刷新之
          if (newRoute.meta.keepAlive === false) {
            this.routeReload()
          }
        // update-end-author:taoyan date:20200211 for: TASK #3368 【路由缓存】首页的缓存设置有问题，需要根据后台的路由配置来实现是否缓存
        }else if (this.linkList.indexOf(newRoute.fullPath) < 0) {
          this.linkList.push(newRoute.fullPath)
          this.pageList.push(Object.assign({},newRoute))
          //// update-begin-author:sunjianlei date:20200103 for: 如果新增的页面配置了缓存路由，那么就强制刷新一遍 #842
          // if (newRoute.meta.keepAlive) {
          //   this.routeReload()
          // }
          //// update-end-author:sunjianlei date:20200103 for: 如果新增的页面配置了缓存路由，那么就强制刷新一遍 #842
        } else if (this.linkList.indexOf(newRoute.fullPath) >= 0) {
          let oldIndex = this.linkList.indexOf(newRoute.fullPath)
          let oldPositionRoute = this.pageList[oldIndex]
          this.pageList.splice(oldIndex, 1, Object.assign({},newRoute,{meta:oldPositionRoute.meta}))
        }
      },
      'activePage': function(key) {
        let index = this.linkList.lastIndexOf(key)
        let waitRouter = this.pageList[index]
        // 【TESTA-523】修复：不允许重复跳转路由异常
        if (waitRouter.fullPath !== this.$route.fullPath) {
          this.$router.push(Object.assign({}, waitRouter))
        }
        this.changeTitle(waitRouter.meta.title)
      },
      'multipage': function(newVal) {
        if(this.reloadFlag){
          if (!newVal) {
            this.linkList = [this.$route.fullPath]
            this.pageList = [this.$route]
          }
        }
      },
      // update-begin-author:sunjianlei date:20191223 for: 修复从单页模式切换回多页模式后首页不居第一位的 BUG
      device() {
        if (this.multipage && this.linkList.indexOf(indexKey) === -1) {
          this.addIndexToFirst()
        }
      },
      // update-end-author:sunjianlei date:20191223 for: 修复从单页模式切换回多页模式后首页不居第一位的 BUG
    },
    methods: {
      // update-begin-author:sunjianlei date:20191223 for: 修复从单页模式切换回多页模式后首页不居第一位的 BUG
      // 将首页添加到第一位
      addIndexToFirst() {
        this.pageList.splice(0, 0, {
          name: 'dashboard-analysis',
          path: indexKey,
          fullPath: indexKey,
          meta: {
            icon: 'dashboard',
            title: '首页'
          }
        })
        this.linkList.splice(0, 0, indexKey)
      },
      // update-end-author:sunjianlei date:20191223 for: 修复从单页模式切换回多页模式后首页不居第一位的 BUG

      // update-begin-author:sunjianlei date:20200120 for: 动态更改页面标题
      changeTitle(title) {
        let projectTitle = "Jeecg-Boot 企业级低代码平台"
        // 首页特殊处理
        if (this.$route.path === indexKey) {
          document.title = projectTitle
        } else {
          document.title = title + ' · ' + projectTitle
        }
      },
      // update-end-author:sunjianlei date:20200120 for: 动态更改页面标题

      changePage(key) {
        this.activePage = key
      },
      tabCallBack() {
        this.$nextTick(() => {
          //update-begin-author:taoyan date: 20201211 for:【新版】online报错 JT-100
         setTimeout(()=>{
           //省市区组件里面给window绑定了个resize事件 导致切换页面的时候触发了他的resize，但是切换页面，省市区组件还没被销毁前就触发了该事件，导致控制台报错，加个延迟
           triggerWindowResizeEvent()
         },20)
          //update-end-author:taoyan date: 20201211 for:【新版】online报错 JT-100
        })
      },
      editPage(key, action) {
        this[action](key)
      },
      remove(key) {
        if (key == indexKey) {
          this.$message.warning('首页不能关闭!')
          return
        }
        if (this.pageList.length === 1) {
          this.$message.warning('这是最后一页，不能再关闭了啦')
          return
        }
        console.log("this.pageList ",this.pageList );
        let removeRoute = this.pageList.filter(item => item.fullPath == key)
        this.pageList = this.pageList.filter(item => item.fullPath !== key)
        let index = this.linkList.indexOf(key)
        this.linkList = this.linkList.filter(item => item !== key)
        index = index >= this.linkList.length ? this.linkList.length - 1 : index
        this.activePage = this.linkList[index]

        //update-begin--Author:scott  Date:20201015 for：路由缓存问题，关闭了tab页时再打开就不刷新 #842
        //关闭页面则从缓存cache_included_routes中删除路由，下次点击菜单会重新加载页面
        let cacheRouterArray = Vue.ls.get(CACHE_INCLUDED_ROUTES) || []
        if (removeRoute && removeRoute[0]) {
          let componentName = removeRoute[0].meta.componentName
          console.log("key: ", key);
          console.log("componentName: ", componentName);
          if(cacheRouterArray.includes(componentName)){
            cacheRouterArray.splice(cacheRouterArray.findIndex(item => item === componentName), 1)
            Vue.ls.set(CACHE_INCLUDED_ROUTES, cacheRouterArray)
          }
        }
        //update-end--Author:scott  Date:20201015 for：路由缓存问题，关闭了tab页时再打开就不刷新 #842

      },
      onContextmenu(e) {
        const pagekey = this.getPageKey(e.target)
        if (pagekey !== null) {
          e.preventDefault()
          this.menuVisible = true
        }
      },
      getPageKey(target, depth) {
        depth = depth || 0
        if (depth > 2) {
          return null
        }
        let pageKey = target.getAttribute('pagekey')
        pageKey = pageKey || (target.previousElementSibling ? target.previousElementSibling.getAttribute('pagekey') : null)
        return pageKey || (target.firstElementChild ? this.getPageKey(target.firstElementChild, ++depth) : null)
      },
      onMenuSelect(key, target) {
        let pageKey = this.getPageKey(target)
        switch (key) {
          case '1':
            this.closeLeft(pageKey)
            break
          case '2':
            this.closeRight(pageKey)
            break
          case '3':
            this.closeOthers(pageKey)
            break
          case '4':
            this.routeReload()
            break
          default:
            break
        }
      },
      /* update_begin author:wuxianquan date:20190828 for: 关闭当前tab页，供子页面调用->望菜单能配置外链，直接弹出新页面而不是嵌入iframe #428 */
      closeCurrent(){
        this.remove(this.activePage);
      },
      /* update_end author:wuxianquan date:20190828 for: 关闭当前tab页，供子页面调用->望菜单能配置外链，直接弹出新页面而不是嵌入iframe #428 */
      closeOthers(pageKey) {
        let index = this.linkList.indexOf(pageKey)
        if (pageKey == indexKey || pageKey.indexOf('?ticke=')>=0) {
          this.linkList = this.linkList.slice(index, index + 1)
          this.pageList = this.pageList.slice(index, index + 1)
          this.activePage = this.linkList[0]
        } else {
          let indexContent = this.pageList.slice(0, 1)[0]
          this.linkList = this.linkList.slice(index, index + 1)
          this.pageList = this.pageList.slice(index, index + 1)
          this.linkList.unshift(indexContent.fullPath)
          this.pageList.unshift(indexContent)
          this.activePage = this.linkList[1]
        }
      },
      closeLeft(pageKey) {
        if (pageKey == indexKey) {
          return
        }
        let tempList = [...this.pageList]
        let indexContent = tempList.slice(0, 1)[0]
        let index = this.linkList.indexOf(pageKey)
        this.linkList = this.linkList.slice(index)
        this.pageList = this.pageList.slice(index)
        this.linkList.unshift(indexContent.fullPath)
        this.pageList.unshift(indexContent)
        if (this.linkList.indexOf(this.activePage) < 0) {
          this.activePage = this.linkList[0]
        }
      },
      closeRight(pageKey) {
        let index = this.linkList.indexOf(pageKey)
        this.linkList = this.linkList.slice(0, index + 1)
        this.pageList = this.pageList.slice(0, index + 1)
        if (this.linkList.indexOf(this.activePage < 0)) {
          this.activePage = this.linkList[this.linkList.length - 1]
        }
      },
      //update-begin-author:taoyan date:20190430 for:动态路由title显示配置的菜单title而不是其对应路由的title
      dynamicRouterShow(key,title){
        let keyIndex = this.linkList.indexOf(key)
        if(keyIndex>=0){
          let currRouter = this.pageList[keyIndex]
          let meta = Object.assign({},currRouter.meta,{title:title})
          this.pageList.splice(keyIndex, 1, Object.assign({},currRouter,{meta:meta}))
          if (key === this.activePage) {
            this.changeTitle(title)
          }
        }
      },
      //update-end-author:taoyan date:20190430 for:动态路由title显示配置的菜单title而不是其对应路由的title

      //update-begin-author:taoyan date:20191008 for:路由刷新
      routeReload(){
        this.reloadFlag = false
        let ToggleMultipage = "ToggleMultipage"
        this.$store.dispatch(ToggleMultipage,false)
        this.$nextTick(()=>{
          this.$store.dispatch(ToggleMultipage,true)
          this.reloadFlag = true
        })
      },
      //update-end-author:taoyan date:20191008 for:路由刷新
      //新增一个返回方法
      excuteCallback(callback){
        callback()
      },
    }
  }
</script>

<style lang="less">

  /*
 * The following styles are auto-applied to elements with
 * transition="page-transition" when their visibility is toggled
 * by Vue.js.
 *
 * You can easily play with the page transition by editing
 * these styles.
 */

  .page-transition-enter {
    opacity: 0;
  }

  .page-transition-leave-active {
    opacity: 0;
  }

  .page-transition-enter .page-transition-container,
  .page-transition-leave-active .page-transition-container {
    -webkit-transform: scale(1.1);
    transform: scale(1.1);
  }

  /*美化弹出Tab样式*/
  .ant-tabs-nav-container {
    margin-top: 4px;
  }

  /* 修改 ant-tabs 样式 */
  .tab-layout-tabs.ant-tabs {
    border-bottom: 1px solid #ccc;
    border-left: 1px solid #ccc;
    background-color: white;
    padding: 0 20px;

    .ant-tabs-bar {
      margin: 4px 0 0;
      border: none;
    }

  }

  .tab-layout-tabs.ant-tabs {

    &.ant-tabs-card .ant-tabs-tab {

      padding: 0 24px !important;
      background-color: white !important;
      margin-right: 10px !important;

      .ant-tabs-close-x {
        width: 12px !important;
        height: 12px !important;
        opacity: 0 !important;
        cursor: pointer !important;
        font-size: 12px !important;
        margin: 0 !important;
        position: absolute;
        top: 36%;
        right: 6px;
      }

      &:hover .ant-tabs-close-x {
        opacity: 1 !important;
      }

    }

  }

  .tab-layout-tabs.ant-tabs.ant-tabs-card > .ant-tabs-bar {
    .ant-tabs-tab {
      border: none !important;
      border-bottom: 1px solid transparent !important;
    }
    .ant-tabs-tab-active {
      border-color: @primary-color!important;
    }
  }


</style>
