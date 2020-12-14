import JDictSelectTag from './JDictSelectTag.vue'
import JMultiSelectTag from './JMultiSelectTag.vue'
import JSearchSelectTag from './JSearchSelectTag.vue'

export default {
  install: function (Vue) {
    Vue.component('JDictSelectTag',JDictSelectTag);
    Vue.component('JMultiSelectTag',JMultiSelectTag);
    Vue.component('JSearchSelectTag',JSearchSelectTag);
  }
}