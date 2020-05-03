<script>
  import { cutStrByFullLength, getStrFullLength } from '@/components/_util/StringUtil'

  export default {
    name: 'Ellipsis',
    props: {
      prefixCls: {
        type: String,
        default: 'ant-pro-ellipsis'
      },
      tooltip: {
        type: Boolean,
        default: true,
      },
      length: {
        type: Number,
        default: 25,
      },
      lines: {
        type: Number,
        default: 1
      },
      fullWidthRecognition: {
        type: Boolean,
        default: false
      }
    },
    methods: {},
    render() {
      const { tooltip, length } = this.$props
      let text = ''
      // 处理没有default插槽时的特殊情况
      if (this.$slots.default) {
        text = this.$slots.default.map(vNode => vNode.text).join('')
      }
      // 判断是否显示 tooltip
      if (tooltip && getStrFullLength(text) > length) {
        return (
          <a-tooltip>
            <template slot="title">{text}</template>
            <span>{cutStrByFullLength(text, this.length) + '…'}</span>
          </a-tooltip>
        )
      } else {
        return (<span>{text}</span>)
      }
    }
  }
</script>