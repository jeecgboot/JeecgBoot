export default {
  name: 'JSelectBizQueryItem',
  props: {
    queryParam: Object,
    queryConfig: Array,
  },
  data() {
    return {}
  },
  methods: {
    renderQueryItem() {
      return this.queryConfig.map(queryItem => {
        const {key, label, placeholder, dictCode, props, customRender} = queryItem
        const options = {
          props: {},
          on: {
            pressEnter: () => this.$emit('pressEnter'),
          }
        }
        if (props != null) {
          Object.assign(options.props, props)
        }
        if (placeholder === undefined) {
          if (dictCode) {
            options.props['placeholder'] = `请选择${label}`
          } else {
            options.props['placeholder'] = `请输入${label}`
          }
        } else {
          options.props['placeholder'] = placeholder
        }

        let input
        if (typeof customRender === 'function') {
          input = customRender.call(this, {key, options, queryParam: this.queryParam})
        } else if (dictCode) {
          input = <j-dict-select-tag {...options} vModel={this.queryParam[key]} dictCode={dictCode} style="width:180px;"/>
        } else {
          input = <a-input {...options} vModel={this.queryParam[key]}/>
        }
        return <a-form-item key={key} label={label}>{input}</a-form-item>
      })
    },
  },
  render() {
    return <span>{this.renderQueryItem()}</span>
  },
}