import { isObject, isString } from '/@/utils/is';
import dayjs from "dayjs";
// update-begin--author:liaozhiyang---date:20240426---for：【QQYUN-9138】系统用户保存的时间没有秒
const DATE_TIME_FORMAT = 'YYYY-MM-DD HH:mm:ss';
// update-end--author:liaozhiyang---date:20240426---for：【QQYUN-9138】系统用户保存的时间没有秒

export function joinTimestamp<T extends boolean>(join: boolean, restful: T): T extends true ? string : object;

export function joinTimestamp(join: boolean, restful = false): string | object {
  if (!join) {
    return restful ? '' : {};
  }
  const now = new Date().getTime();
  if (restful) {
    return `?_t=${now}`;
  }
  return { _t: now };
}

/**
 * @description: Format request parameter time
 */
export function formatRequestDate(params: Recordable) {
  if (Object.prototype.toString.call(params) !== '[object Object]') {
    return;
  }

  for (const key in params) {
    // 判断是否是dayjs实例
    if (dayjs.isDayjs(params[key])) {
      params[key] = params[key].format(DATE_TIME_FORMAT);
    }
    if (isString(key)) {
      const value = params[key];
      if (value) {
        try {
          params[key] = isString(value) ? value.trim() : value;
        } catch (error) {
          throw new Error(error);
        }
      }
    }
    if (isObject(params[key])) {
      formatRequestDate(params[key]);
    }
  }
}
