import JVxeCellMixins from '@/components/jeecg/JVxeTable/mixins/JVxeCellMixins'

// tags 组件的显示组件
export const TagsSpanCell = {
  name: 'JVxeTagsCell',
  mixins: [JVxeCellMixins],
  data() {
    return {
      innerTags: [],
    }
  },
  watch: {
    innerValue: {
      immediate: true,
      handler(value) {
        if (value !== this.innerTags.join(';')) {
          let rv = replaceValue(value)
          this.innerTags = rv.split(';')
          this.handleChangeCommon(rv)
        }
      },
    },
  },
  methods: {
    renderTags(h) {
      let tags = []
      for (let tag of this.innerTags) {
        if (tag) {
          let tagProps = {}
          let tagStyle = {}
          let setTagColor = this.originColumn.setTagColor
          if (typeof setTagColor === 'function') {
            /**
             * 设置 tag 颜色
             *
             * @param event 包含的字段：
             * event.tagValue 当前tag的值
             * event.value 当前原始值
             * event.row 当前行的所有值
             * event.column 当前列的配置
             * event.column.own 当前列的原始配置
             * @return Array | String 可以返回一个数组，数据第一项是tag背景颜色，第二项是字体颜色。也可以返回一个字符串，即tag背景颜色
             */
            let color = setTagColor({
              tagValue: tag,
              value: this.innerValue,
              row: this.row,
              column: this.column,
            })
            if (Array.isArray(color)) {
              tagProps.color = color[0]
              tagStyle.color = color[1]
            } else if (color && typeof color === 'string') {
              tagProps.color = color
            }
          }
          tags.push(h('a-tag', {
            props: tagProps,
            style: tagStyle,
          }, [tag]))
        }
      }
      return tags
    },
  },
  render(h) {
    return h('div', {}, [
      this.renderTags(h)
    ])
  },
}

// tags 组件的输入框
export const TagsInputCell = {
  name: 'JVxeTagsInputCell',
  mixins: [JVxeCellMixins],
  data() {
    return {
      innerTagValue: '',
    }
  },
  watch: {
    innerValue: {
      immediate: true,
      handler(value) {
        if (value !== this.innerTagValue) {
          this.handleInputChange(value)
        }
      },
    },
  },
  methods: {

    handleInputChange(value, event) {
      this.innerTagValue = replaceValue(value, event)
      this.handleChangeCommon(this.innerTagValue)
      return this.innerTagValue
    },

  },
  render(h) {
    return h('a-input', {
      props: {
        value: this.innerValue,
        ...this.cellProps
      },
      on: {
        change: (event) => {
          let {target, target: {value}} = event
          let newValue = this.handleInputChange(value, event)
          if (newValue !== value) {
            target.value = newValue
          }
        }
      },
    })
  },
}

// 将值每隔两位加上一个分号
function replaceValue(value, event) {
  if (value) {
    // 首先去掉现有的分号
    value = value.replace(/;/g, '')
    // 然后再遍历添加分号
    let rv = ''
    let splitArr = value.split('')
    let count = 0
    splitArr.forEach((val, index) => {
      rv += val
      let position = index + 1
      if (position % 2 === 0 && position < splitArr.length) {
        count++
        rv += ';'
      }
    })
    if (event && count > 0) {
      let {target, target: {selectionStart}} = event
      target.selectionStart = selectionStart + count
      target.selectionEnd = selectionStart + count
    }
    return rv
  }
  return ''
}
