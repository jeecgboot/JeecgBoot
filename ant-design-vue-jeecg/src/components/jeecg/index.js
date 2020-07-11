import JModal from './JModal'
import JFormContainer from './JFormContainer.vue'
import JPopup from './JPopup.vue'

export default {
  install(Vue) {
    Vue.component('JFormContainer', JFormContainer)
    Vue.component('JPopup', JPopup)
    Vue.component(JModal.name, JModal)
  }
}