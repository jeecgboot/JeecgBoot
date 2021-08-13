<template>
  <div class="config-list">
    <a-radio-group v-model="type">
      <div class="item">
        <a-radio value="TYPE_EVERY" class="choice" :disabled="disabled">每年</a-radio>
      </div>
      <div class="item">
        <a-radio value="TYPE_RANGE" class="choice" :disabled="disabled">区间</a-radio>
        从
        <a-input-number :disabled="type!==TYPE_RANGE || disabled" :min="0" :precision="0" class="w60" v-model="valueRange.start"/>
        年
        至
        <a-input-number :disabled="type!==TYPE_RANGE || disabled" :min="1" :precision="0" class="w60" v-model="valueRange.end"/>
        年
      </div>
      <div class="item">
        <a-radio value="TYPE_LOOP" class="choice" :disabled="disabled">循环</a-radio>
        从
        <a-input-number :disabled="type!==TYPE_LOOP || disabled" :min="0" :precision="0" class="w60" v-model="valueLoop.start"/>
        年开始，间隔
        <a-input-number :disabled="type!==TYPE_LOOP || disabled" :min="1" :precision="0" class="w60" v-model="valueLoop.interval"/>
        年
      </div>
    </a-radio-group>
  </div>
</template>

<script>
import mixin from './mixin'

export default {
  name: 'year',
  mixins: [mixin],
  data() {
    return {}
  },
  watch: {
    value_c(newVal, oldVal) {
      // console.info('change:' + newVal)
      this.$emit('change', newVal)
    }
  },
  created() {
    const nowYear = (new Date()).getFullYear()
    this.DEFAULT_VALUE = '*'
    this.minValue = 0
    this.maxValue = 0
    this.valueRange.start = nowYear
    this.valueRange.end = nowYear + 100
    this.valueLoop.start = nowYear
    this.valueLoop.interval = 1
    // console.info('created')
    this.parseProp(this.prop)
  }
}
</script>

<style lang="less" scoped>
  @import "mixin.less";
</style>
