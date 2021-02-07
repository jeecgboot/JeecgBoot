import JDictSelectTag from './JDictSelectTag.vue'
import JMultiSelectTag from './JMultiSelectTag.vue'
import JSearchSelectTag from './JSearchSelectTag.vue'
import { filterMultiDictText,filterDictText,initDictOptions,filterDictTextByCache } from './JDictSelectUtil'

export default {
  install: function (Vue) {
    Vue.component('JDictSelectTag',JDictSelectTag);
    Vue.component('JMultiSelectTag',JMultiSelectTag);
    Vue.component('JSearchSelectTag',JSearchSelectTag);
    Vue.prototype.$initDictOptions = (dictCode) => initDictOptions(dictCode)
    Vue.prototype.$filterMultiDictText = (dictOptions, text) => filterMultiDictText(dictOptions, text)
    Vue.prototype.$filterDictText = (dictOptions, text) => filterDictText(dictOptions, text)
    Vue.prototype.$filterDictTextByCache = (...param) => filterDictTextByCache(...param)
  }
}