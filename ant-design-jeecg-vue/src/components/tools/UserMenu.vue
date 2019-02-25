<template>
  <div class="user-wrapper">
    <span class="action">
      <a-icon type="question-circle-o"></a-icon>
    </span>
    <header-notice class="action"/>
    <a-dropdown>
      <span class="action ant-dropdown-link user-dropdown-menu">
        <a-avatar class="avatar" size="small" :src="getAvatar()"/>
        <span>欢迎您，{{ nickname() }}</span>
      </span>
      <a-menu slot="overlay" class="user-dropdown-menu-wrapper">
        <a-menu-item key="0">
          <router-link :to="{ name: 'account-center' }">
            <a-icon type="user"/>
            <span>个人中心</span>
          </router-link>
        </a-menu-item>
        <a-menu-item key="1">
          <router-link :to="{ name: 'account-settings' }">
            <a-icon type="setting"/>
            <span>账户设置</span>
          </router-link>
        </a-menu-item>
       <!-- <a-menu-item key="2" disabled>
          <a-icon type="setting"/>
          <span>测试</span>
        </a-menu-item>
        <a-menu-divider/>
        <a-menu-item key="3">
          <a href="javascript:;" @click="handleLogout">
            <a-icon type="logout"/>
            <span>退出登录</span>
          </a>
        </a-menu-item>-->
      </a-menu>
    </a-dropdown>
    <span class="action">
      <a class="logout_title" href="javascript:;" @click="handleLogout">
        <a-icon type="logout"/>
        <span> 退出登录</span>
      </a>
    </span>
  </div>
</template>

<script>
  import HeaderNotice from './HeaderNotice'
  import { mapActions, mapGetters } from 'vuex'
  import {imgView} from '@/api/api'

  export default {
    name: "UserMenu",
    components: {
      HeaderNotice
    },
    methods: {
      ...mapActions(["Logout"]),
      ...mapGetters(["nickname", "avatar"]),
      getAvatar(){
        console.log('url = '+ imgView+this.avatar())
        return imgView+this.avatar()
      },
      handleLogout() {
        const that = this

        this.$confirm({
          title: '提示',
          content: '真的要注销登录吗 ?',
          onOk() {
            return that.Logout({}).then(() => {
              window.location.reload()
            }).catch(err => {
              that.$message.error({
                title: '错误',
                description: err.message
              })
            })
          },
          onCancel() {
          },
        });
      },
    }
  }
</script>

<style scoped>
  .logout_title{
    color: rgba(0, 0, 0, 0.65);
    text-decoration:none;
  }
</style>