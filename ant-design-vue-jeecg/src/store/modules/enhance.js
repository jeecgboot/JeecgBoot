import Vue from 'vue'
const enhance = {
  state: {
    enhanceJs:{

    }
  },
  mutations: {
    ADD_TABLE_ENHANCE: (state, record) => {
      if(!state.enhanceJs){
        let obj = {}
        let arr = []
        arr.push({...record})
        obj[record.code] = arr
        state.enhanceJs = obj
      }else{
        if(!state.enhanceJs[record.code]){
          let arr = []
          arr.push({...record})
          state.enhanceJs[record.code] = arr
        }
        state.enhanceJs[record.code].push({...record})
      }
      let arr = state.enhanceJs[record.code]
      while(arr.length>16){
        arr.shift()
      }
      Vue.ls.set('enhance_'+record['code'], arr)
    }
  },
  actions: {
    addEhanceRecord({ commit }, record) {
      commit('ADD_TABLE_ENHANCE', record)
    }
  }
}
export default enhance