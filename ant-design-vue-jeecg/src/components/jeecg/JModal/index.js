import JModal from './JModal'
import JPrompt from './JPrompt'

export default {
  install(Vue) {
    Vue.component(JModal.name, JModal)

    const JPromptExtend = Vue.extend(JPrompt)
    Vue.prototype.$JPrompt = function (options = {}) {
      // 创建prompt实例
      const vm = new JPromptExtend().$mount()
      vm.show(options)
      // 关闭后销毁
      vm.$on('after-close', () => vm.$destroy())
      return vm
    }
  },
}