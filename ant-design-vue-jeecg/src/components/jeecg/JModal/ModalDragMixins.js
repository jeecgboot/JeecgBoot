import {getRefPromise} from '@/utils/util'

/** JModal 的拖拽混入 */
export default {
  data() {
    return {
      // 拖动配置
      dragSettings: {
        // 上次拖动top记录
        top: null,
        // 上次拖动left记录
        left: null,
        wrapEl: null,
        dragEl: null,
        headerEl: null,
      },
    }
  },
  watch: {
    visible() {
      if (!this.visible || !this.draggable) {
        return
      }
      this.handleDrag()
    },
    draggable() {
      if (!this.visible || !this.draggable) {
        return
      }
      this.handleDrag()
    },
  },
  methods: {
    async handleDrag() {
      let modalRef = await getRefPromise(this, 'modal')
      const dragWraps = modalRef.$el.querySelectorAll('.ant-modal-wrap')
      let wrapEl = dragWraps[0]
      if (!wrapEl) return
      this.dragSettings.wrapEl = wrapEl
      this.dragSettings.dragEl = wrapEl.querySelector('.ant-modal')
      this.dragSettings.headerEl = wrapEl.querySelector('.ant-modal-header')
      const display = getStyle(wrapEl, 'display')
      const draggable = wrapEl.getAttribute('data-drag')
      if (display !== 'none') {
        // 拖拽位置
        if (draggable === null || this.destroyOnClose) {
          this.enableDrag(wrapEl)
        }
      }
    },
    /** 启用拖拽 */
    enableDrag() {
      let {wrapEl, dragEl, headerEl} = this.dragSettings
      if (!wrapEl) return
      wrapEl.setAttribute('data-drag', this.draggable)
      if (!headerEl || !dragEl || !this.draggable) return

      // 还原上一次移动的位置
      this.resetModalPosition()

      headerEl.style.cursor = 'move'
      headerEl.onmousedown = (e) => {
        if (!e) return
        // 鼠标按下，计算当前元素距离可视区的距离
        const disX = e.clientX
        const disY = e.clientY
        const screenWidth = document.body.clientWidth // body当前宽度
        const screenHeight = document.documentElement.clientHeight // 可见区域高度(应为body高度，可某些环境下无法获取)

        const dragElWidth = dragEl.offsetWidth // 对话框宽度
        const dragElHeight = dragEl.offsetHeight // 对话框高度

        const minDragElLeft = dragEl.offsetLeft

        const maxDragElLeft = screenWidth - dragEl.offsetLeft - dragElWidth
        const minDragElTop = dragEl.offsetTop
        const maxDragElTop = screenHeight - dragEl.offsetTop - dragElHeight
        // 获取到的值带px 正则匹配替换
        const domLeft = getStyle(dragEl, 'left')
        const domTop = getStyle(dragEl, 'top')
        let styL = +domLeft
        let styT = +domTop

        // 注意在ie中 第一次获取到的值为组件自带50% 移动之后赋值为px
        if (domLeft.includes('%')) {
          styL = +document.body.clientWidth * (+domLeft.replace(/%/g, '') / 100)
          styT = +document.body.clientHeight * (+domTop.replace(/%/g, '') / 100)
        } else {
          styL = +domLeft.replace(/px/g, '')
          styT = +domTop.replace(/px/g, '')
        }

        document.onmousemove = (e) => {
          // 全屏时不触发移动方法
          if (this.innerFullscreen) {
            return
          }
          // 通过事件委托，计算移动的距离
          let left = e.clientX - disX
          let top = e.clientY - disY

          // 边界处理
          if (-left > minDragElLeft) {
            left = -minDragElLeft
          } else if (left > maxDragElLeft) {
            left = maxDragElLeft
          }

          if (-top > minDragElTop) {
            top = -minDragElTop
          } else if (top > maxDragElTop) {
            top = maxDragElTop
          }

          this.setModalPosition(top + styT, left + styL)
        }

        document.onmouseup = () => {
          document.onmousemove = null
          document.onmouseup = null
        }
      }
    },

    /**
     * 移动弹窗位置
     * @param top 顶部位置
     * @param left 左侧位置
     * @param remember 是否记录位置，默认 true
     */
    setModalPosition(top, left, remember = true) {
      // 记录移动位置
      if (remember) {
        this.dragSettings.top = top
        this.dragSettings.left = left
      }
      // 移动当前元素
      this.dragSettings.dragEl.style.cssText += `;left:${left}px;top:${top}px;`
    },
    /**
     * 将弹窗移动到上次记录的位置
     */
    resetModalPosition() {
      this.setModalPosition(this.dragSettings.top, this.dragSettings.left, false)
    },

  },
}

function getStyle(dom, attr) {
  return getComputedStyle(dom)[attr]
}
