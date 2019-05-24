import Bus from 'vue';
let install = function (Vue) {
  Vue.prototype.$bus = new Bus()
}
export default { install };