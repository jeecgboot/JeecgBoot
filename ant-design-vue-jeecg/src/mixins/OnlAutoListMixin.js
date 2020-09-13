import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'

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
          cancelButtonProps: { style: { display: 'none' } }
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
    // 处理接收href参数
    handleAcceptHrefParams(){
      this.acceptHrefParams={}
      let hrefparam = this.$route.query;
      if(hrefparam){
        this.acceptHrefParams = {...hrefparam}
      }
    },
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
      this.hrefComponent.model.title = '操作'
      this.hrefComponent.is = () => import('@/views/' + (path.startsWith('/') ? path.slice(1) : path))
    },
    /** 处理列中的 href 跳转和 dict 字典，使两者可以兼容存在 */
    handleColumnHrefAndDict(column = {}, fieldHrefSlotKeysMap = {}) {
      let { customRender, hrefSlotName } = column
      if (!hrefSlotName && (column.scopedSlots && column.scopedSlots.customRender)) {
        //hrefSlotName = column.scopedSlots.customRender
      }
      // 如果 customRender 有值则代表使用了字典
      // 如果 hrefSlotName 有值则代表使用了href跳转
      // 两者可以兼容。兼容的具体思路为：先获取到字典替换的值，再添加href链接跳转
      if (customRender || hrefSlotName) {
        let dictCode = customRender
        let replaceFlag = '_replace_text_'
        column.customRender = (text, record) => {
          let value = text
          // 如果 dictCode 有值，就进行字典转换
          if (dictCode) {
            if (dictCode.startsWith(replaceFlag)) {
              let textFieldName = dictCode.replace(replaceFlag, '')
              value = record[textFieldName]
            } else {
              value = filterMultiDictText(this.dictOptions[dictCode], text)
            }
          }
          // 如果 hrefSlotName 有值，就生成一个 a 标签，包裹住字典替换后（或原生）的值
          if (hrefSlotName) {
            let field = fieldHrefSlotKeysMap[hrefSlotName]
            if (field) {
              // 此处为 JSX 语法
              return (<a onClick={() => this.handleClickFieldHref(field, record)}>{value}</a>)
            }
          }
          return value
        }
      }
    },

  }
}