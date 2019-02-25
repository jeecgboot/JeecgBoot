<script>
  import Tooltip from 'ant-design-vue/es/tooltip'
  import { cutStrByFullLength, getStrFullLength } from '@/components/_util/StringUtil'
/*
  const isSupportLineClamp = document.body.style.webkitLineClamp !== undefined;

  const TooltipOverlayStyle = {
    overflowWrap: 'break-word',
    wordWrap: 'break-word',
  };
*/

  export default {
    name: 'Ellipsis',
    components: {
      Tooltip
    },
    props: {
      prefixCls: {
        type: String,
        default: 'ant-pro-ellipsis'
      },
      tooltip: {
        type: Boolean
      },
      length: {
        type: Number,
        required: true
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
    methods: {
      getStrDom (str) {
        return (
          <span>{ cutStrByFullLength(str, this.length) + '...' }</span>
        )
      },
      getTooltip ( fullStr) {
        return (
          <Tooltip>
            <template slot="title">{ fullStr }</template>
            { this.getStrDom(fullStr) }
          </Tooltip>
        )
      }
    },
    render () {
      const { tooltip, length } = this.$props
      let str = this.$slots.default.map(vNode => vNode.text).join("")
      const strDom = tooltip && getStrFullLength(str) > length ? this.getTooltip(str) : this.getStrDom(str);
      return (
        strDom
      )
    }
  }
</script>