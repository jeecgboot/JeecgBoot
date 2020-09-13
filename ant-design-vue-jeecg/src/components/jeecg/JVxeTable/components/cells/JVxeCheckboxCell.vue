<template>
  <div :class="clazz" :style="boxStyle">
    <a-checkbox
      ref="checkbox"
      :checked="innerValue"
      v-bind="cellProps"
      @change="handleChange"
    />
  </div>
</template>

<script>
  import { neverNull } from '@/utils/util'
  import JVxeCellMixins from '@/components/jeecg/JVxeTable/mixins/JVxeCellMixins'

  export default {
    name: 'JVxeCheckboxCell',
    mixins: [JVxeCellMixins],
    props: {},
    computed: {
      bordered() {
        return !!this.renderOptions.bordered
      },
      scrolling() {
        return !!this.renderOptions.scrolling
      },
      clazz() {
        return {
          'j-vxe-checkbox': true,
          'no-animation': this.scrolling
        }
      },
      boxStyle() {
        const style = {}
        // 如果有边框且未设置align属性，就强制居中
        if (this.bordered && !this.originColumn.align) {
          style['text-align'] = 'center'
        }
        return style
      },
    },
    methods: {
      handleChange(event) {
        this.handleChangeCommon(event.target.checked)
      },
    },
    // 【组件增强】注释详见：JVxeCellMixins.js
    enhanced: {
      switches: {
        visible: true,
      },
      getValue(value) {
        let {own: col} = this.column
        // 处理 customValue
        if (Array.isArray(col.customValue)) {
          let customValue = getCustomValue(col)
          if (typeof value === 'boolean') {
            return value ? customValue[0] : customValue[1]
          } else {
            return value
          }
        } else {
          return value
        }
      },
      setValue(value) {
        let {own: col} = this.column
        // 判断是否设定了customValue（自定义值）
        if (Array.isArray(col.customValue)) {
          let customValue = getCustomValue(col)
          return neverNull(value).toString() === customValue[0].toString()
        } else {
          return !!value
        }
      },
      createValue({column}) {
        let {own: col} = column
        if (Array.isArray(col.customValue)) {
          let customValue = getCustomValue(col)
          return col.defaultChecked ? customValue[0] : customValue[1]
        } else {
          return !!col.defaultChecked
        }
      },
    }
  }

  function getCustomValue(col) {
    let customTrue = neverNull(col.customValue[0], true)
    let customFalse = neverNull(col.customValue[1], false)
    return [customTrue, customFalse]
  }
</script>

<style lang="less">
  // 关闭动画，防止滚动时动态赋值出现问题
  .j-vxe-checkbox.no-animation {
    .ant-checkbox-inner,
    .ant-checkbox-inner::after {
      transition: none !important;
    }
  }
</style>