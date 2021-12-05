<template>
  <div class="j-easy-cron">
    <div class="content">
      <div>
        <a-tabs size="small" v-model="curtab">
          <a-tab-pane tab="秒" key="second" v-if="!hideSecond">
            <second-ui v-model="second" :disabled="disabled"></second-ui>
          </a-tab-pane>
          <a-tab-pane tab="分" key="minute">
            <minute-ui v-model="minute" :disabled="disabled"></minute-ui>
          </a-tab-pane>
          <a-tab-pane tab="时" key="hour">
            <hour-ui v-model="hour" :disabled="disabled"></hour-ui>
          </a-tab-pane>
          <a-tab-pane tab="日" key="day">
            <day-ui v-model="day" :week="week" :disabled="disabled"></day-ui>
          </a-tab-pane>
          <a-tab-pane tab="月" key="month">
            <month-ui v-model="month" :disabled="disabled"></month-ui>
          </a-tab-pane>
          <a-tab-pane tab="周" key="week">
            <week-ui v-model="week" :day="day" :disabled="disabled"></week-ui>
          </a-tab-pane>
          <a-tab-pane tab="年" key="year" v-if="!hideYear && !hideSecond">
            <year-ui v-model="year" :disabled="disabled"></year-ui>
          </a-tab-pane>
        </a-tabs>
      </div>
      <a-divider/>
      <!-- 执行时间预览 -->
      <a-row :gutter="8">
        <a-col :span="18" style="margin-top: 22px;">
          <a-row :gutter="8">
            <a-col :span="8" style="margin-bottom: 8px;">
              <a-input addon-before="秒" v-model="inputValues.second" @blur="onInputBlur"/>
            </a-col>
            <a-col :span="8" style="margin-bottom: 8px;">
              <a-input addon-before="分" v-model="inputValues.minute" @blur="onInputBlur"/>
            </a-col>
            <a-col :span="8" style="margin-bottom: 8px;">
              <a-input addon-before="时" v-model="inputValues.hour" @blur="onInputBlur"/>
            </a-col>
            <a-col :span="8" style="margin-bottom: 8px;">
              <a-input addon-before="日" v-model="inputValues.day" @blur="onInputBlur"/>
            </a-col>
            <a-col :span="8" style="margin-bottom: 8px;">
              <a-input addon-before="月" v-model="inputValues.month" @blur="onInputBlur"/>
            </a-col>
            <a-col :span="8" style="margin-bottom: 8px;">
              <a-input addon-before="周" v-model="inputValues.week" @blur="onInputBlur"/>
            </a-col>
            <a-col :span="8" style="margin-bottom: 8px;">
              <a-input addon-before="年" v-model="inputValues.year" @blur="onInputBlur"/>
            </a-col>
            <a-col :span="16" style="margin-bottom: 8px;">
              <a-input addon-before="Cron" v-model="inputValues.cron" @blur="onInputCronBlur"/>
            </a-col>
          </a-row>
        </a-col>
        <a-col :span="6">

          <div>近十次执行时间（不含年）</div>
          <a-textarea type="textarea" :value="preTimeList" :rows="5"/>
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script>
import SecondUi from './tabs/second'
import MinuteUi from './tabs/minute'
import HourUi from './tabs/hour'
import DayUi from './tabs/day'
import WeekUi from './tabs/week'
import MonthUi from './tabs/month'
import YearUi from './tabs/year'
import CronParser from 'cron-parser'
import dateFormat from './format-date'
import { simpleDebounce } from '@/utils/util'
import ACol from 'ant-design-vue/es/grid/Col'

export default {
  name: 'easy-cron',
  components: {
    ACol,
    SecondUi,
    MinuteUi,
    HourUi,
    DayUi,
    WeekUi,
    MonthUi,
    YearUi
  },
  props: {
    cronValue: {
      type: String,
      default: ''
    },
    disabled: {
      type: Boolean,
      default: false
    },
    hideSecond: {
      type: Boolean,
      default: false
    },
    hideYear: {
      type: Boolean,
      default: false
    },
    remote: {
      type: Function,
      default: null
    }
  },
  data() {
    return {
      curtab: this.hideSecond ? 'minute' : 'second',
      second: '*',
      minute: '*',
      hour: '*',
      day: '*',
      month: '*',
      week: '?',
      year: '*',
      inputValues: {second: '', minute: '', hour: '', day: '', month: '', week: '', year: '', cron: ''},
      preTimeList: '执行预览，会忽略年份参数',
    }
  },
  computed: {
    cronValue_c() {
      let result = []
      if (!this.hideSecond) result.push(this.second ? this.second : '*')
      result.push(this.minute ? this.minute : '*')
      result.push(this.hour ? this.hour : '*')
      result.push(this.day ? this.day : '*')
      result.push(this.month ? this.month : '*')
      result.push(this.week ? this.week : '?')
      if (!this.hideYear && !this.hideSecond) result.push(this.year ? this.year : '*')
      return result.join(' ')
    },
    cronValue_c2() {
      const v = this.cronValue_c
      if (this.hideYear || this.hideSecond) return v
      const vs = v.split(' ')
      if (vs.length >= 6) {
        // 转成 Quartz 的规则
        vs[5] = this.convertWeekToQuartz(vs[5])
      }
      return vs.slice(0, vs.length - 1).join(' ')
    },
  },
  watch: {
    cronValue(newVal, oldVal) {
      if (newVal === this.cronValue_c) {
        // console.info('same cron value: ' + newVal)
        return
      }
      this.formatValue()
    },
    cronValue_c(newVal, oldVal) {
      this.calTriggerList()
      this.$emit('change', newVal)
      this.assignInput()
    },
    minute() {
      if (this.second === '*') {
        this.second = '0'
      }
    },
    hour() {
      if (this.minute === '*') {
        this.minute = '0'
      }
    },
    day(day) {
      if (day !== '?' && this.hour === '*') {
        this.hour = '0'
      }
    },
    week(week) {
      if (week !== '?' && this.hour === '*') {
        this.hour = '0'
      }
    },
    month() {
      if (this.day === '?' && this.week === '*') {
        this.week = '1'
      } else if (this.week === '?' && this.day === '*') {
        this.day = '1'
      }
    },
    year() {
      if (this.month === '*') {
        this.month = '1'
      }
    },
  },
  created() {
    this.formatValue()
    this.$nextTick(() => {
      this.calTriggerListInner()
    })
  },
  methods: {
    assignInput() {
      Object.assign(this.inputValues, {
        second: this.second,
        minute: this.minute,
        hour: this.hour,
        day: this.day,
        month: this.month,
        week: this.week,
        year: this.year,
        cron: this.cronValue_c,
      })
    },
    formatValue() {
      if (!this.cronValue) return
      const values = this.cronValue.split(' ').filter(item => !!item)
      if (!values || values.length <= 0) return
      let i = 0
      if (!this.hideSecond) this.second = values[i++]
      if (values.length > i) this.minute = values[i++]
      if (values.length > i) this.hour = values[i++]
      if (values.length > i) this.day = values[i++]
      if (values.length > i) this.month = values[i++]
      if (values.length > i) this.week = values[i++]
      if (values.length > i) this.year = values[i]
      this.assignInput()
    },
    // Quartz 的规则：
    // 1 = 周日，2 = 周一，3 = 周二，4 = 周三，5 = 周四，6 = 周五，7 = 周六
    convertWeekToQuartz(week) {
      let convert = (v) => {
        if (v === '0') {
          return '1'
        }
        if (v === '1') {
          return '0'
        }
        return (Number.parseInt(v) - 1).toString()
      }
      // 匹配示例 1-7 or 1/7
      let patten1 = /^([0-7])([-/])([0-7])$/
      // 匹配示例 1,4,7
      let patten2 = /^([0-7])(,[0-7])+$/
      if (/^[0-7]$/.test(week)) {
        return convert(week)
      } else if (patten1.test(week)) {
        return week.replace(patten1, ($0, before, separator, after) => {
          if (separator === '/') {
            return convert(before) + separator + after
          } else {
            return convert(before) + separator + convert(after)
          }
        })
      } else if (patten2.test(week)) {
        return week.split(',').map(v => convert(v)).join(',')
      }
      return week
    },
    calTriggerList: simpleDebounce(function () {
      this.calTriggerListInner()
    }, 500),
    calTriggerListInner() {
      // 设置了回调函数
      if (this.remote) {
        this.remote(this.cronValue_c, +new Date(), v => {
          this.preTimeList = v
        })
        return
      }
      const format = 'yyyy-MM-dd hh:mm:ss'
      const options = {
        currentDate: dateFormat(new Date(), format)
      }
      const iter = CronParser.parseExpression(this.cronValue_c2, options)
      const result = []
      for (let i = 1; i <= 10; i++) {
        result.push(dateFormat(new Date(iter.next()), format))
      }
      this.preTimeList = result.length > 0 ? result.join('\n') : '无执行时间'
    },
    onInputBlur(){
      this.second = this.inputValues.second
      this.minute = this.inputValues.minute
      this.hour = this.inputValues.hour
      this.day = this.inputValues.day
      this.month = this.inputValues.month
      this.week = this.inputValues.week
      this.year = this.inputValues.year
    },
    onInputCronBlur(event){
      this.$emit('change', event.target.value)
    },
  },
  model: {
    prop: 'cronValue',
    event: 'change'
  },
}
</script>

<style scoped lang="less">
  .j-easy-cron {

    /deep/ .content {
      .ant-checkbox-wrapper + .ant-checkbox-wrapper {
        margin-left: 0;
      }
    }

  }
</style>
