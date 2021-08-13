<template>
  <div class="config-list">
    <a-radio-group v-model="type">
      <div class="item">
        <a-radio value="TYPE_EVERY" class="choice" :disabled="disabled">每分</a-radio>
      </div>
      <div class="item">
        <a-radio value="TYPE_RANGE" class="choice" :disabled="disabled">区间</a-radio>
        从
        <a-input-number :disabled="type!==TYPE_RANGE || disabled" :max="maxValue" :min="minValue" :precision="0" class="w60" v-model="valueRange.start"/>
        分
        至
        <a-input-number :disabled="type!==TYPE_RANGE || disabled" :max="maxValue" :min="minValue" :precision="0" class="w60" v-model="valueRange.end"/>
        分
      </div>
      <div class="item">
        <a-radio value="TYPE_LOOP" class="choice" :disabled="disabled">循环</a-radio>
        从
        <a-input-number :disabled="type!==TYPE_LOOP || disabled" :max="maxValue" :min="minValue" :precision="0" class="w60" v-model="valueLoop.start"/>
        分开始，间隔
        <a-input-number :disabled="type!==TYPE_LOOP || disabled" :max="maxValue" :min="minValue" :precision="0" class="w60" v-model="valueLoop.interval"/>
        分
      </div>
      <div class="item">
        <a-radio value="TYPE_SPECIFY" class="choice" :disabled="disabled">指定</a-radio>
        <div class="list">
          <a-checkbox-group v-model="valueList">
            <template v-for="i in specifyRange">
              <a-checkbox class="list-check-item" :key="`key-${i}`" :value="i" :disabled="type!==TYPE_SPECIFY || disabled">{{i}}</a-checkbox>
            </template>
          </a-checkbox-group>
        </div>
      </div>
    </a-radio-group>
  </div>
</template>

<script>
import mixin from './mixin'

export default {
  name: 'minute',
  mixins: [mixin],
  data() {
    return {}
  },
  watch: {
    value_c(newVal, oldVal) {
      this.$emit('change', newVal)
    }
  },
  created() {
    this.DEFAULT_VALUE = '*'
    this.minValue = 0
    this.maxValue = 59
    this.valueRange.start = 0
    this.valueRange.end = 59
    this.valueLoop.start = 0
    this.valueLoop.interval = 1
    this.parseProp(this.prop)
  }
}
</script>

<style lang="less" scoped>
  @import "mixin.less";
</style>
