<template>
  <div class="config-list">
    <a-radio-group v-model="type">
      <div class="item">
        <a-radio value="TYPE_NOT_SET" class="choice" :disabled="disableChoice">不设置</a-radio>
        <span class="tip-info">日和周只能设置其中之一</span>
      </div>
      <div class="item">
        <a-radio value="TYPE_EVERY" class="choice" :disabled="disableChoice">每日</a-radio>
      </div>
      <div class="item">
        <a-radio value="TYPE_RANGE" class="choice" :disabled="disableChoice">区间</a-radio>
        从
        <a-input-number :disabled="type!==TYPE_RANGE || disableChoice" :max="maxValue" :min="minValue" :precision="0" class="w60" v-model="valueRange.start"/>
        日
        至
        <a-input-number :disabled="type!==TYPE_RANGE || disableChoice" :max="maxValue" :min="minValue" :precision="0" class="w60" v-model="valueRange.end"/>
        日
      </div>
      <div class="item">
        <a-radio value="TYPE_LOOP" class="choice" :disabled="disableChoice">循环</a-radio>
        从
        <a-input-number :disabled="type!==TYPE_LOOP || disableChoice" :max="maxValue" :min="minValue" :precision="0" class="w60" v-model="valueLoop.start"/>
        日开始，间隔
        <a-input-number :disabled="type!==TYPE_LOOP || disableChoice" :max="maxValue" :min="minValue" :precision="0" class="w60" v-model="valueLoop.interval"/>
        日
      </div>
      <div class="item">
        <a-radio value="TYPE_WORK" class="choice" :disabled="disableChoice">工作日</a-radio>
        本月
        <a-input-number :disabled="type!==TYPE_WORK || disableChoice" :max="maxValue" :min="minValue" :precision="0" class="w60" v-model="valueWork"/>
        日，最近的工作日
      </div>
      <div class="item">
        <a-radio value="TYPE_LAST" class="choice" :disabled="disableChoice">最后一日</a-radio>
      </div>
      <div class="item">
        <a-radio value="TYPE_SPECIFY" class="choice" :disabled="disableChoice">指定</a-radio>
        <div class="list">
          <a-checkbox-group v-model="valueList">
            <template v-for="i of specifyRange">
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
  name: 'day',
  mixins: [mixin],
  props: {
    week: {
      type: String,
      default: '?'
    }
  },
  data() {
    return {}
  },
  computed: {
    disableChoice() {
      return (this.week && this.week !== '?') || this.disabled
    }
  },
  watch: {
    value_c(newVal, oldVal) {
      // 数值变化
      this.updateValue()
    },
    week(newVal, oldVal) {
      // console.info('new week: ' + newVal)
      this.updateValue()
    }
  },
  methods: {
    updateValue() {
      this.$emit('change', this.disableChoice ? '?' : this.value_c)
    }
  },
  created() {
    this.DEFAULT_VALUE = '*'
    this.minValue = 1
    this.maxValue = 31
    this.valueRange.start = 1
    this.valueRange.end = 31
    this.valueLoop.start = 1
    this.valueLoop.interval = 1
    this.parseProp(this.prop)
  }
}
</script>

<style lang="less" scoped>
  @import "mixin.less";
</style>
