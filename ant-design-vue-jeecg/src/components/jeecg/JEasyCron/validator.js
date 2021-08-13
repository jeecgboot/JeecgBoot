import CronParser from 'cron-parser'
import { replaceWeekName } from './tabs/const'

export default (rule, value, callback) => {
  // 没填写就不校验
  if (!value) {
    callback()
    return true
  }
  const values = value.split(' ').filter(item => !!item)
  if (values.length > 7) {
    callback(new Error('Cron表达式最多7项！'))
    return false
  }
  // 检查第7项
  let e = value
  if (values.length === 7) {
    const year = replaceWeekName(values[6])
    if (year !== '*' && year !== '?') {
      let yearValues = []
      if (year.indexOf('-') >= 0) {
        yearValues = year.split('-')
      } else if (year.indexOf('/')) {
        yearValues = year.split('/')
      } else {
        yearValues = [year]
      }
      // console.info(yearValues)
      // 判断是否都是数字
      const checkYear = yearValues.some(item => isNaN(item))
      if (checkYear) {
        callback(new Error('Cron表达式参数[年]错误：' + year))
        return false
      }
    }
    // 取其中的前六项
    e = values.slice(0, 6).join(' ')
  }
  // 6位 没有年
  // 5位没有秒、年
  let result = true
  try {
    const iter = CronParser.parseExpression(e)
    iter.next()
    callback()
  } catch (e) {
    callback(new Error('Cron表达式错误：' + e))
    result = false
  }
  return result
}
