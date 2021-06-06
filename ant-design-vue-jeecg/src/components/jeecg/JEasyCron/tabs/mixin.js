// 主要用于日和星期的互斥使用
const TYPE_NOT_SET = 'TYPE_NOT_SET'
const TYPE_EVERY = 'TYPE_EVERY'
const TYPE_RANGE = 'TYPE_RANGE'
const TYPE_LOOP = 'TYPE_LOOP'
const TYPE_WORK = 'TYPE_WORK'
const TYPE_LAST = 'TYPE_LAST'
const TYPE_SPECIFY = 'TYPE_SPECIFY'

const DEFAULT_VALUE = '?'

export default {
  model: {
    prop: 'prop',
    event: 'change'
  },
  props: {
    prop: {
      type: String,
      default: DEFAULT_VALUE
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  data () {
    const type = TYPE_EVERY
    return {
      DEFAULT_VALUE,
      // 类型
      type,
      // 启用日或者星期互斥用
      TYPE_NOT_SET,
      TYPE_EVERY,
      TYPE_RANGE,
      TYPE_LOOP,
      TYPE_WORK,
      TYPE_LAST,
      TYPE_SPECIFY,
      // 对于不同的类型，所定义的值也有所不同
      valueRange: {
        start: 0,
        end: 0
      },
      valueLoop: {
        start: 0,
        interval: 1
      },
      valueWeek: {
        start: 0,
        end: 0
      },
      valueList: [],
      valueWork: 1,
      maxValue: 0,
      minValue: 0
    }
  },
  watch: {
    prop (newVal, oldVal) {
      if (newVal === this.value_c) {
        // console.info('skip ' + newVal)
        return
      }
      this.parseProp(newVal)
    }
  },
  computed: {
    value_c () {
      let result = []
      switch (this.type) {
        case TYPE_NOT_SET:
          result.push('?')
          break
        case TYPE_EVERY:
          result.push('*')
          break
        case TYPE_RANGE:
          result.push(`${this.valueRange.start}-${this.valueRange.end}`)
          break
        case TYPE_LOOP:
          result.push(`${this.valueLoop.start}/${this.valueLoop.interval}`)
          break
        case TYPE_WORK:
          result.push(`${this.valueWork}W`)
          break
        case TYPE_LAST:
          result.push('L')
          break
        case TYPE_SPECIFY:
          result.push(this.valueList.join(','))
          break
        default:
          result.push(this.DEFAULT_VALUE)
          break
      }
      return result.length > 0 ? result.join('') : this.DEFAULT_VALUE
    }
  },
  methods: {
    parseProp (value) {
      if (value === this.value_c) {
        // console.info('same ' + value)
        return
      }
      if (typeof (this.preProcessProp) === 'function') {
        value = this.preProcessProp(value)
      }
      try {
        if (!value || value === this.DEFAULT_VALUE) {
          this.type = TYPE_EVERY
        } else if (value.indexOf('?') >= 0) {
          this.type = TYPE_NOT_SET
        } else if (value.indexOf('-') >= 0) {
          this.type = TYPE_RANGE
          const values = value.split('-')
          if (values.length >= 2) {
            this.valueRange.start = parseInt(values[0])
            this.valueRange.end = parseInt(values[1])
          }
        } else if (value.indexOf('/') >= 0) {
          this.type = TYPE_LOOP
          const values = value.split('/')
          if (values.length >= 2) {
            this.valueLoop.start = value[0] === '*' ? 0 : parseInt(values[0])
            this.valueLoop.interval = parseInt(values[1])
          }
        } else if (value.indexOf('W') >= 0) {
          this.type = TYPE_WORK
          const values = value.split('W')
          if (!values[0] && !isNaN(values[0])) {
            this.valueWork = parseInt(values[0])
          }
        } else if (value.indexOf('L') >= 0) {
          this.type = TYPE_LAST
          const values = value.split('L')
          this.valueLast = parseInt(values[0])
        } else if (value.indexOf(',') >= 0 || !isNaN(value)) {
          this.type = TYPE_SPECIFY
          this.valueList = value.split(',').map(item => parseInt(item))
        } else {
          this.type = TYPE_EVERY
        }
      } catch (e) {
        // console.info(e)
        this.type = TYPE_EVERY
      }
    }
  }
}
