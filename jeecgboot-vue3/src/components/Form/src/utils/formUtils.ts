import { unref } from 'vue';
import { dateUtil } from '/@/utils/dateUtil';

/**
 * 表单区间时间数值字段转换
 * @param props
 * @param values
 */
export function handleRangeValue(props, values) {
  //判断是否配置并处理fieldMapToTime
  const fieldMapToTime = unref(props)?.fieldMapToTime;
  fieldMapToTime && (values = handleRangeTimeValue(props, values));
  //判断是否配置并处理fieldMapToNumber
  const fieldMapToNumber = unref(props)?.fieldMapToNumber;
  fieldMapToNumber && (values = handleRangeNumberValue(props, values));
  return values;
}
/**
 * 处理时间转换成2个字段
 * @param props
 * @param values
 */
export function handleRangeTimeValue(props, values) {
  const fieldMapToTime = unref(props).fieldMapToTime;
  if (!fieldMapToTime || !Array.isArray(fieldMapToTime)) {
    return values;
  }
  for (const [field, [startTimeKey, endTimeKey], format = 'YYYY-MM-DD'] of fieldMapToTime) {
    if (!field || !startTimeKey || !endTimeKey || !values[field]) {
      continue;
    }

    // 【issues/I53G9Y】 日期区间组件有可能是字符串
    let timeValue = values[field];
    if (!Array.isArray(timeValue)) {
      timeValue = timeValue.split(',');
    }
    const [startTime, endTime]: string[] = timeValue;
    values[startTimeKey] = dateUtil(startTime).format(format);
    values[endTimeKey] = dateUtil(endTime).format(format);
    Reflect.deleteProperty(values, field);
  }
  return values;
}
/**
 * 处理数字转换成2个字段
 * @param props
 * @param values
 * @updateby liusq
 * @updateDate:2021-09-16
 */
export function handleRangeNumberValue(props, values) {
  const fieldMapToNumber = unref(props).fieldMapToNumber;
  if (!fieldMapToNumber || !Array.isArray(fieldMapToNumber)) {
    return values;
  }
  for (const [field, [startNumberKey, endNumberKey]] of fieldMapToNumber) {
    if (!field || !startNumberKey || !endNumberKey || !values[field]) {
      continue;
    }
    //update-begin-author:taoyan date:2022-5-10 for: 用于数值的范围查询 数组格式的中间转换不知道哪里出了问题，这里会变成字符串，需要再强制转成数组
    let temp = values[field];
    if (typeof temp === 'string') {
      temp = temp.split(',');
    }
    const [startNumber, endNumber]: number[] = temp;
    //update-end-author:taoyan date:2022-5-10 for: 用于数值的范围查询 数组格式的中间转换不知道哪里出了问题，这里会变成字符串，需要再强制转成数组
    values[startNumberKey] = startNumber;
    values[endNumberKey] = endNumber;
    Reflect.deleteProperty(values, field);
  }
  return values;
}
