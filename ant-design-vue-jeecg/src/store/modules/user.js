import Vue from 'vue'
import { login, logout, phoneLogin } from "@/api/login"
import { ACCESS_TOKEN, USER_NAME,USER_INFO,USER_AUTH,SYS_BUTTON_AUTH } from "@/store/mutation-types"
import { welcome } from "@/utils/util"
import { queryPermissionsByUser } from '@/api/api'

const user = {
  state: {
    token: '',
    username: '',
    realname: '',
    welcome: '',
    avatar: '',
    permissionList: [],
    info: {}
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, { username, realname, welcome }) => {
      state.username = username
      state.realname = realname
      state.welcome = welcome
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_PERMISSIONLIST: (state, permissionList) => {
      state.permissionList = permissionList
    },
    SET_INFO: (state, info) => {
      state.info = info
    },
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo).then(response => {
          if(response.code =='200'){
            const result = response.result
            const userInfo = result.userInfo
            Vue.ls.set(ACCESS_TOKEN, result.token, 7 * 24 * 60 * 60 * 1000)
            Vue.ls.set(USER_NAME, userInfo.username, 7 * 24 * 60 * 60 * 1000)
            Vue.ls.set(USER_INFO, userInfo, 7 * 24 * 60 * 60 * 1000)
            commit('SET_TOKEN', result.token)
            commit('SET_INFO', userInfo)
            commit('SET_NAME', { username: userInfo.username,realname: userInfo.realname, welcome: welcome() })
            commit('SET_AVATAR', userInfo.avatar)
            resolve(response)
          }else{
            reject(response)
          }
        }).catch(error => {
          reject(error)
        })
      })
    },
    //手机号登录
    PhoneLogin({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
          phoneLogin(userInfo).then(response => {
          if(response.code =='200'){
        const result = response.result
        const userInfo = result.userInfo
        Vue.ls.set(ACCESS_TOKEN, result.token, 7 * 24 * 60 * 60 * 1000)
        Vue.ls.set(USER_NAME, userInfo.username, 7 * 24 * 60 * 60 * 1000)
        Vue.ls.set(USER_INFO, userInfo, 7 * 24 * 60 * 60 * 1000)
        commit('SET_TOKEN', result.token)
        commit('SET_INFO', userInfo)
        commit('SET_NAME', { username: userInfo.username,realname: userInfo.realname, welcome: welcome() })
        commit('SET_AVATAR', userInfo.avatar)
        resolve(response)
      }else{
        reject(response)
      }
    }).catch(error => {
        reject(error)
      })
    })
    },
    // 获取用户信息
    GetPermissionList({ commit }) {
      return new Promise((resolve, reject) => {
        let v_token = Vue.ls.get(ACCESS_TOKEN);
        let params = {token:v_token};
        queryPermissionsByUser(params).then(response => {
          const menuData = response.result.menu;
          const authData = response.result.auth;
          const allAuthData = response.result.allAuth;
          //Vue.ls.set(USER_AUTH,authData);
          sessionStorage.setItem(USER_AUTH,JSON.stringify(authData));
          sessionStorage.setItem(SYS_BUTTON_AUTH,JSON.stringify(allAuthData));
          if (menuData && menuData.length > 0) {
            commit('SET_PERMISSIONLIST', menuData)
          } else {
            reject('getPermissionList: permissions must be a non-null array !')
          }
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    Logout({ commit, state }) {
      return new Promise((resolve) => {
        let logoutToken = state.token;
        commit('SET_TOKEN', '')
        commit('SET_PERMISSIONLIST', [])
        Vue.ls.remove(ACCESS_TOKEN)
        //console.log('logoutToken: '+ logoutToken)
        logout(logoutToken).then(() => {
          resolve()
        }).catch(() => {
          resolve()
        })
      })
    },

  }
}

export default user