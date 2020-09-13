import Vue from 'vue'
import { ONL_AUTH_FIELDS } from "@/store/mutation-types"
import { getAction } from '@/api/manage'


const online = {
  state: {
    //存储对象属性 value,text
    authFields: [],
  },
  mutations: {
    SET_AUTHFIELDS: (state, fields) => {
      console.log('fields',fields)
      Vue.set(state, 'authFields', fields)
    }
  },
  actions: {
    // TODO 如果没找到可以尝试请求一下
    xxxxxx({ commit }, userInfo) {
    }

  }
}

export default online