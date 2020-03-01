export const HrefJump = {
  data() {
    return {
      fieldHrefSlots: [],
      hrefComponent: {
        model: {
          title: '',
          width: '100%',
          visible: false,
          destroyOnClose: true,
          style: {
            top: 0,
            left: 0,
            height: '100%',
            margin: 0,
            padding: 0
          },
          bodyStyle: { padding: '8px', height: 'calc(100vh - 108px)', overflow: 'auto', overflowX: 'hidden' },
          // 隐藏掉取消按钮
          cancelButtonProps: { style: { display: 'none' } },
          afterClose: () => {
            // 恢复body的滚动
            document.body.style.overflow = null
          }
        },
        on: {
          ok: () => this.hrefComponent.model.visible = false,
          cancel: () => this.hrefComponent.model.visible = false
        },
        is: null,
        params: {},
      }
    }
  },
  methods: {
    //支持链接href跳转
    handleClickFieldHref(field, record) {
      let href = field.href
      let urlPattern = /(ht|f)tp(s?)\:\/\/[0-9a-zA-Z]([-.\w]*[0-9a-zA-Z])*(:(0-9)*)*(\/?)([a-zA-Z0-9\-\.\?\,\'\/\\\+&amp;%\$#_]*)?/
      let compPattern = /\.vue(\?.*)?$/
      if (typeof href === 'string') {
        href = href.trim().replace(/\${([^}]+)?}/g, (s1, s2) => record[s2])
        if (urlPattern.test(href)) {
          window.open(href, '_blank')
        } else if (compPattern.test(href)) {
          this.openHrefCompModal(href)
        } else {
          this.$router.push(href)
        }
      }
    },
    openHrefCompModal(href) {
      // 解析 href 参数
      let index = href.indexOf('?')
      let path = href
      if (index !== -1) {
        path = href.substring(0, index)
        let paramString = href.substring(index + 1, href.length)
        let paramArray = paramString.split('&')
        let params = {}
        paramArray.forEach(paramObject => {
          let paramItem = paramObject.split('=')
          params[paramItem[0]] = paramItem[1]
        })
        this.hrefComponent.params = params
      } else {
        this.hrefComponent.params = {}
      }
      this.hrefComponent.model.visible = true
      this.hrefComponent.model.title = '@/views/' + path
      this.hrefComponent.is = () => import('@/views/' + (path.startsWith('/')?path.slice(1):path))
      // 禁止body滚动，防止滚动穿透
      setTimeout(() => {
        document.body.style.overflow = 'hidden'
      }, 300)
    },
  }
}