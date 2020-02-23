import JModal from './JModal'
import JFormContainer from './JFormContainer.vue'

export default {
  install(Vue) {
    Vue.component('JFormContainer', JFormContainer)
    Vue.component(JModal.name, JModal)
  }
}