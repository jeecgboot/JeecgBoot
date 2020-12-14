import '../../less/reload-effect.less'
import { randomString } from '@/utils/util'

// 修改数据特效
export default {
  props: {
    vNode: null,
    // 是否启用特效
    effect: Boolean,
  },
  data() {
    return {
      // vNode: null,
      innerEffect: false,
      // 应付同时多个特效
      effectIdx: 0,
      effectList: [],
    }
  },
  watch: {
    vNode: {
      deep: true,
      immediate: true,
      handler(vNode, old) {
        this.innerEffect = this.effect
        if (this.innerEffect && old != null) {
          let topLayer = this.renderSpan(old, 'top')
          this.effectList.push(topLayer)
        }
      },
    },
  },
  methods: {

    // 条件渲染内容 span
    renderVNode() {
      if (this.vNode == null) {
        return null
      }
      let bottom = this.renderSpan(this.vNode, 'bottom')
      // 启用了特效，并且有旧数据，就渲染特效顶层
      if (this.innerEffect && this.effectList.length > 0) {
        this.$emit('effect-begin')
        // 1.4s 以后关闭特效
        window.setTimeout(() => {
          let item = this.effectList[this.effectIdx]
          if (item && item.elm) {
            // 特效结束后，展示先把 display 设为 none，而不是直接删掉该元素，
            // 目的是为了防止页面重新渲染，导致动画重置
            item.elm.style.display = 'none'
          }
          // 当所有的层级动画都结束时，再删掉所有元素
          if (++this.effectIdx === this.effectList.length) {
            this.innerEffect = false
            this.effectIdx = 0
            this.effectList = []
            this.$emit('effect-end')
          }
        }, 1400)
        return [this.effectList, bottom]
      } else {
        return bottom
      }
    },
    // 渲染内容 span
    renderSpan(vNode, layer) {
      let options = {
        key: layer + this.effectIdx + randomString(6),
        class: ['j-vxe-reload-effect-span', `layer-${layer}`],
        style: {},
      }
      if (layer === 'top') {
        // 最新渲染的在下面
        options.style['z-index'] = (9999 - this.effectIdx)
      }
      return this.$createElement('span', options, [vNode])
    },
  },
  render(h) {
    return h('div', {
      class: ['j-vxe-reload-effect-box'],
    }, [this.renderVNode()])
  },
}