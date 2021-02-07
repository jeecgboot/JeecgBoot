<template>
  <a-input
    ref="input"
    :value="innerValue"
    v-bind="cellProps"
    @blur="handleBlur"
    @change="handleChange"
  />
</template>

<script>
  import { JVXETypes } from '@/components/jeecg/JVxeTable'
  import JVxeCellMixins from '@/components/jeecg/JVxeTable/mixins/JVxeCellMixins'

  const NumberRegExp = /^-?\d+\.?\d*$/
  export default {
    name: 'JVxeInputCell',
    mixins: [JVxeCellMixins],
    methods: {

      /** 处理change事件 */
      handleChange(event) {
        let {$type} = this
        let {target} = event
        let {value, selectionStart} = target
        let change = true
        if ($type === JVXETypes.inputNumber) {
          // 判断输入的值是否匹配数字正则表达式，不匹配就还原
          if (!NumberRegExp.test(value) && (value !== '' && value !== '-')) {
            change = false
            value = this.innerValue
            target.value = value || ''
            if (typeof selectionStart === 'number') {
              target.selectionStart = selectionStart - 1
              target.selectionEnd = selectionStart - 1
            }
          }
        }
        // 触发事件，存储输入的值
        if (change) {
          this.handleChangeCommon(value)
        }

        if ($type === JVXETypes.inputNumber) {
          // this.recalcOneStatisticsColumn(col.key)
        }
      },

      /** 处理blur失去焦点事件 */
      handleBlur(event) {
        let {$type} = this
        let {target} = event
        // 判断输入的值是否匹配数字正则表达式，不匹配就置空
        if ($type === JVXETypes.inputNumber) {
          if (!NumberRegExp.test(target.value)) {
            target.value = ''
          } else {
            target.value = Number.parseFloat(target.value)
          }
          this.handleChangeCommon(target.value)
        }

        this.handleBlurCommon(target.value)
      },

    },
    // 【组件增强】注释详见：JVxeCellMixins.js
    enhanced: {
      installOptions: {
        // 自动聚焦的 class 类名
        autofocus: '.ant-input',
      },
      getValue(value) {
        if (this.$type === JVXETypes.inputNumber && typeof value === 'string') {
          if (NumberRegExp.test(value)) {
            return Number.parseFloat(value)
          }
        }
        return value
      },
    }
  }
</script>

<style scoped>

</style>