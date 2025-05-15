import type { RouteLocationNormalized, RouteRecordNormalized } from 'vue-router';
import type { App, Plugin } from 'vue';
import type { FormSchema } from "@/components/Form";

import { unref } from 'vue';
import { isObject, isFunction, isString } from '/@/utils/is';
import Big from 'big.js';
// update-begin--author:sunjianlei---date:20220408---for: 【VUEN-656】配置外部网址打不开，原因是带了#号，需要替换一下
export const URL_HASH_TAB = `__AGWE4H__HASH__TAG__PWHRG__`;
// update-end--author:sunjianlei---date:20220408---for: 【VUEN-656】配置外部网址打不开，原因是带了#号，需要替换一下

export const noop = () => {};

/**
 * @description:  Set ui mount node
 */
export function getPopupContainer(node?: HTMLElement): HTMLElement {
  return (node?.parentNode as HTMLElement) ?? document.body;
}

/**
 * Add the object as a parameter to the URL
 * @param baseUrl url
 * @param obj
 * @returns {string}
 * eg:
 *  let obj = {a: '3', b: '4'}
 *  setObjToUrlParams('www.baidu.com', obj)
 *  ==>www.baidu.com?a=3&b=4
 */
export function setObjToUrlParams(baseUrl: string, obj: any): string {
  let parameters = '';
  for (const key in obj) {
    parameters += key + '=' + encodeURIComponent(obj[key]) + '&';
  }
  parameters = parameters.replace(/&$/, '');
  return /\?$/.test(baseUrl) ? baseUrl + parameters : baseUrl.replace(/\/?$/, '?') + parameters;
}

export function deepMerge<T = any>(src: any = {}, target: any = {}): T {
  let key: string;
  for (key in target) {
    // update-begin--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
    if (isObject(src[key]) && isObject(target[key])) {
      src[key] = deepMerge(src[key], target[key]);
    } else {
      // update-begin--author:liaozhiyang---date:20250318---for：【issues/7940】componentProps写成函数形式时，updateSchema写成对象时，参数没合并
      try {
        if (isFunction(src[key]) && isObject(src[key]()) && isObject(target[key])) {
          // src[key]是函数且返回对象，且target[key]是对象
          src[key] = deepMerge(src[key](), target[key]);
        } else if (isObject(src[key]) && isFunction(target[key]) && isObject(target[key]())) {
          // target[key]是函数且返回对象，且src[key]是对象
          src[key] = deepMerge(src[key], target[key]());
        } else if (isFunction(src[key]) && isFunction(target[key]) && isObject(src[key]()) && isObject(target[key]())) {
          // src[key]是函数且返回对象，target[key]是函数且返回对象
          src[key] = deepMerge(src[key](), target[key]());
        } else {
          src[key] = target[key];
        }
      } catch (error) {
        src[key] = target[key];
      }
      // update-end--author:liaozhiyang---date:20250318---for：【issues/7940】componentProps写成函数形式时，updateSchema写成对象时，参数没合并
    }
    // update-end--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
  }
  return src;
}

export function openWindow(url: string, opt?: { target?: TargetContext | string; noopener?: boolean; noreferrer?: boolean }) {
  const { target = '__blank', noopener = true, noreferrer = true } = opt || {};
  const feature: string[] = [];

  noopener && feature.push('noopener=yes');
  noreferrer && feature.push('noreferrer=yes');

  window.open(url, target, feature.join(','));
}

// dynamic use hook props
export function getDynamicProps<T, U>(props: T): Partial<U> {
  const ret: Recordable = {};

  // @ts-ignore
  Object.keys(props).map((key) => {
    ret[key] = unref((props as Recordable)[key]);
  });

  return ret as Partial<U>;
}

/**
 * 获取表单字段值数据类型
 * @param props
 * @param field
 * @updateBy:zyf
 */
export function getValueType(props, field) {
  let formSchema = unref(unref(props)?.schemas);
  let valueType = 'string';
  if (formSchema) {
    let schema = formSchema.filter((item) => item.field === field)[0];
    valueType = schema && schema.componentProps && schema.componentProps.valueType ? schema.componentProps.valueType : valueType;
  }
  return valueType;
}

/**
 * 获取表单字段值数据类型
 * @param schema
 */
export function getValueTypeBySchema(schema: FormSchema) {
  let valueType = 'string';
  if (schema) {
    const componentProps = schema.componentProps as Recordable;
    valueType = componentProps?.valueType ? componentProps?.valueType : valueType;
  }
  return valueType;
}

export function getRawRoute(route: RouteLocationNormalized): RouteLocationNormalized {
  if (!route) return route;
  const { matched, ...opt } = route;
  return {
    ...opt,
    matched: (matched
      ? matched.map((item) => ({
          meta: item.meta,
          name: item.name,
          path: item.path,
        }))
      : undefined) as RouteRecordNormalized[],
  };
}
/**
 * 深度克隆对象、数组
 * @param obj 被克隆的对象
 * @return 克隆后的对象
 */
export function cloneObject(obj) {
  return JSON.parse(JSON.stringify(obj));
}

export const withInstall = <T>(component: T, alias?: string) => {
  //console.log("---初始化---", component)

  const comp = component as any;
  comp.install = (app: App) => {
    // @ts-ignore
    app.component(comp.name || comp.displayName, component);
    if (alias) {
      app.config.globalProperties[alias] = component;
    }
  };
  return component as T & Plugin;
};

/**
 * 获取url地址参数
 * @param paraName
 */
export function getUrlParam(paraName) {
  let url = document.location.toString();
  let arrObj = url.split('?');

  if (arrObj.length > 1) {
    let arrPara = arrObj[1].split('&');
    let arr;

    for (let i = 0; i < arrPara.length; i++) {
      arr = arrPara[i].split('=');

      if (arr != null && arr[0] == paraName) {
        return arr[1];
      }
    }
    return '';
  } else {
    return '';
  }
}

/**
 * 休眠（setTimeout的promise版）
 * @param ms 要休眠的时间，单位：毫秒
 * @param fn callback，可空
 * @return Promise
 */
export function sleep(ms: number, fn?: Fn) {
  return new Promise<void>((resolve) =>
    setTimeout(() => {
      fn && fn();
      resolve();
    }, ms)
  );
}

/**
 * 不用正则的方式替换所有值
 * @param text 被替换的字符串
 * @param checker  替换前的内容
 * @param replacer 替换后的内容
 * @returns {String} 替换后的字符串
 */
export function replaceAll(text, checker, replacer) {
  let lastText = text;
  text = text.replace(checker, replacer);
  if (lastText !== text) {
    return replaceAll(text, checker, replacer);
  }
  return text;
}

/**
 * 获取URL上参数
 * @param url
 */
export function getQueryVariable(url) {
  if (!url) return;

  var t,
    n,
    r,
    i = url.split('?')[1],
    s = {};
  (t = i.split('&')), (r = null), (n = null);
  for (var o in t) {
    var u = t[o].indexOf('=');
    u !== -1 && ((r = t[o].substr(0, u)), (n = t[o].substr(u + 1)), (s[r] = n));
  }
  return s;
}
/**
 * 判断是否显示办理按钮
 * @param bpmStatus
 * @returns {*}
 */
export function showDealBtn(bpmStatus) {
  if (bpmStatus != '1' && bpmStatus != '3' && bpmStatus != '4') {
    return true;
  }
  return false;
}
/**
 * 数字转大写
 * @param value
 * @returns {*}
 */
export function numToUpper(value) {
  if (value != '') {
    let unit = new Array('仟', '佰', '拾', '', '仟', '佰', '拾', '', '角', '分');
    const toDx = (n) => {
      switch (n) {
        case '0':
          return '零';
        case '1':
          return '壹';
        case '2':
          return '贰';
        case '3':
          return '叁';
        case '4':
          return '肆';
        case '5':
          return '伍';
        case '6':
          return '陆';
        case '7':
          return '柒';
        case '8':
          return '捌';
        case '9':
          return '玖';
      }
    };
    let lth = value.toString().length;
    // update-begin--author:liaozhiyang---date:20241202---for：【issues/7493】numToUpper方法返回解决错误
    value = new Big(value).times(100);
    // update-end--author:liaozhiyang---date:20241202---for：【issues/7493】numToUpper方法返回解决错误
    value += '';
    let length = value.length;
    if (lth <= 8) {
      let result = '';
      for (let i = 0; i < length; i++) {
        if (i == 2) {
          result = '元' + result;
        } else if (i == 6) {
          result = '万' + result;
        }
        if (value.charAt(length - i - 1) == 0) {
          if (i != 0 && i != 1) {
            if (result.charAt(0) != '零' && result.charAt(0) != '元' && result.charAt(0) != '万') {
              result = '零' + result;
            }
          }
          continue;
        }
        result = toDx(value.charAt(length - i - 1)) + unit[unit.length - i - 1] + result;
      }
      result += result.charAt(result.length - 1) == '元' ? '整' : '';
      return result;
    } else {
      return null;
    }
  }
  return null;
}

//update-begin-author:taoyan date:2022-6-8 for:解决老的vue2动态导入文件语法 vite不支持的问题
const allModules = import.meta.glob('../views/**/*.vue');
export function importViewsFile(path): Promise<any> {
  if (path.startsWith('/')) {
    path = path.substring(1);
  }
  let page = '';
  if (path.endsWith('.vue')) {
    page = `../views/${path}`;
  } else {
    page = `../views/${path}.vue`;
  }
  return new Promise((resolve, reject) => {
    let flag = true;
    for (const path in allModules) {
      if (path == page) {
        flag = false;
        allModules[path]().then((mod) => {
          console.log(path, mod);
          resolve(mod);
        });
      }
    }
    if (flag) {
      reject('该文件不存在:' + page);
    }
  });
}
//update-end-author:taoyan date:2022-6-8 for:解决老的vue2动态导入文件语法 vite不支持的问题


/**
 * 跳转至积木报表的 预览页面
 * @param url
 * @param id
 * @param token
 */
export function goJmReportViewPage(url, id, token) {
  // update-begin--author:liaozhiyang---date:20230904---for：【QQYUN-6390】eval替换成new Function，解决build警告
  // URL支持{{ window.xxx }}占位符变量
  url = url.replace(/{{([^}]+)?}}/g, (_s1, s2) => _eval(s2))
  // update-end--author:liaozhiyang---date:20230904---for：【QQYUN-6390】eval替换成new Function，解决build警告
  if (url.includes('?')) {
    url += '&'
  } else {
    url += '?'
  }
  url += `id=${id}`
  url += `&token=${token}`
  window.open(url)
}

/**
 * 获取随机颜色
 */
export function getRandomColor(index?) {

  const colors = [
    'rgb(100, 181, 246)',
    'rgb(77, 182, 172)',
    'rgb(255, 183, 77)',
    'rgb(229, 115, 115)',
    'rgb(149, 117, 205)',
    'rgb(161, 136, 127)',
    'rgb(144, 164, 174)',
    'rgb(77, 208, 225)',
    'rgb(129, 199, 132)',
    'rgb(255, 138, 101)',
    'rgb(133, 202, 205)',
    'rgb(167, 214, 118)',
    'rgb(254, 225, 89)',
    'rgb(251, 199, 142)',
    'rgb(239, 145, 139)',
    'rgb(169, 181, 255)',
    'rgb(231, 218, 202)',
    'rgb(252, 128, 58)',
    'rgb(254, 161, 172)',
    'rgb(194, 163, 205)',
  ];
  return index && index < 19 ? colors[index] : colors[Math.floor((Math.random()*(colors.length-1)))];
}

export function getRefPromise(componentRef) {
  return new Promise((resolve) => {
    (function next() {
      const ref = componentRef.value;
      if (ref) {
        resolve(ref);
      } else {
        setTimeout(() => {
          next();
        }, 100);
      }
    })();
  });
}

/**
 * 2023-09-04
 * liaozhiyang
 * 用new Function替换eval
 */
export function _eval(str: string) {
 return new Function(`return ${str}`)();
}

/**
 * 2024-04-30
 * liaozhiyang
 * 通过时间或者时间戳获取对应antd的年、月、周、季度。
 */
export function getWeekMonthQuarterYear(date) {
  // 获取 ISO 周数的函数
  const getISOWeek = (date) => {
    const jan4 = new Date(date.getFullYear(), 0, 4);
    const oneDay = 86400000; // 一天的毫秒数
    return Math.ceil(((date - jan4.getTime()) / oneDay + jan4.getDay() + 1) / 7);
  };
  // 将时间戳转换为日期对象
  const dateObj = new Date(date);
  // 计算周
  const week = getISOWeek(dateObj);
  // 计算月
  const month = dateObj.getMonth() + 1; // 月份是从0开始的，所以要加1
  // 计算季度
  const quarter = Math.floor(dateObj.getMonth() / 3) + 1;
  // 计算年
  const year = dateObj.getFullYear();
  return {
    year: `${year}`,
    month: `${year}-${month.toString().padStart(2, '0')}`,
    week: `${year}-${week}周`,
    quarter: `${year}-Q${quarter}`,
  };
}

/**
 * 2024-05-17
 * liaozhiyang
 * 设置挂载的modal元素有可能会有多个，需要找到对应的。
 */
export const setPopContainer = (node, selector) => {
  if (typeof selector === 'string') {
    const targetEles = Array.from(document.querySelectorAll(selector));
    if (targetEles.length > 1) {
      const retrospect = (node, elems) => {
        let ele = node.parentNode;
        while (ele) {
          const findParentNode = elems.find(item => item === ele);
          if (findParentNode) {
            ele = null;
            return findParentNode;
          } else {
            ele = ele.parentNode;
          }
        }
        return null;
      };
      const elem = retrospect(node, targetEles);
      if (elem) {
        return elem;
      } else {
        return document.querySelector(selector);
      }
    } else {
      return document.querySelector(selector);
    }
  } else {
    return selector;
  }
};

/**
 * 2024-06-14
 * liaozhiyang
 * 根据控件显示条件
 * label、value通用，title、val给权限管理用的
 */
export function useConditionFilter() {

  // 通用条件
  const commonConditionOptions = [
    {label: '为空', value: 'empty', val: 'EMPTY'},
    {label: '不为空', value: 'not_empty', val: 'NOT_EMPTY'},
  ]

  // 数值、日期
  const numberConditionOptions = [
    { label: '等于', value: 'eq', val: '=' },
    { label: '在...中', value: 'in', val: 'IN', title: '包含' },
    { label: '不等于', value: 'ne', val: '!=' },
    { label: '大于', value: 'gt', val: '>' },
    { label: '大于等于', value: 'ge', val: '>=' },
    { label: '小于', value: 'lt', val: '<' },
    { label: '小于等于', value: 'le', val: '<=' },
    ...commonConditionOptions,
  ];

  // 文本、密码、多行文本、富文本、markdown
  const inputConditionOptions = [
    { label: '等于', value: 'eq', val: '=' },
    { label: '模糊', value: 'like', val: 'LIKE' },
    { label: '以..开始', value: 'right_like', title: '右模糊', val: 'RIGHT_LIKE' },
    { label: '以..结尾', value: 'left_like', title: '左模糊', val: 'LEFT_LIKE' },
    { label: '在...中', value: 'in', val: 'IN', title: '包含' },
    { label: '不等于', value: 'ne', val: '!=' },
    ...commonConditionOptions,
  ];

  // 下拉、单选、多选、开关、用户、部门、关联记录、省市区、popup、popupDict、下拉多选、下拉搜索、分类字典、自定义树
  const selectConditionOptions = [
    { label: '等于', value: 'eq', val: '=' },
    { label: '在...中', value: 'in', val: 'IN', title: '包含' },
    { label: '不等于', value: 'ne', val: '!=' },
    ...commonConditionOptions,
  ];

  const def = [
    { label: '等于', value: 'eq', val: '=' },
    { label: '模糊', value: 'like', val: 'LIKE' },
    { label: '以..开始', value: 'right_like', title: '右模糊', val: 'RIGHT_LIKE' },
    { label: '以..结尾', value: 'left_like', title: '左模糊', val: 'LEFT_LIKE' },
    { label: '在...中', value: 'in', val: 'IN', title: '包含' },
    { label: '不等于', value: 'ne', val: '!=' },
    { label: '大于', value: 'gt', val: '>' },
    { label: '大于等于', value: 'ge', val: '>=' },
    { label: '小于', value: 'lt', val: '<' },
    { label: '小于等于', value: 'le', val: '<=' },
    ...commonConditionOptions,
  ];

  const filterCondition = (data) => {
    if (data.view == 'text' && data.fieldType == 'number') {
      data.view = 'number';
    }
    switch (data.view) {
      case 'file':
      case 'image':
      case 'password':
        return commonConditionOptions;
      case 'text':
      case 'textarea':
      case 'umeditor':
      case 'markdown':
      case 'pca':
      case 'popup':
        return inputConditionOptions;
      case 'list':
      case 'radio':
      case 'checkbox':
      case 'switch':
      case 'sel_user':
      case 'sel_depart':
      case 'link_table':
      case 'popup_dict':
      case 'list_multi':
      case 'sel_search':
      case 'cat_tree':
      case 'sel_tree':
        return selectConditionOptions;
      case 'date':
      // number是虚拟的
      case 'number':
        return numberConditionOptions;
      default:
        return def;
    }
  };
  return { filterCondition };
}
// 获取url中的参数
export const getUrlParams = (url) => {
  const result = {
    url: '',
    params: {},
  };
  const list = url.split('?');
  result.url = list[0];
  const params = list[1];
  if (params) {
    const list = params.split('&');
    list.forEach((ele) => {
      const dic = ele.split('=');
      const label = dic[0];
      result.params[label] = dic[1];
    });
  }
  return result;
};

/* 20250325
 * liaozhiyang
 * 分割url字符成数组
 * 【issues/7990】图片参数中包含逗号会错误的识别成多张图
 * */
export const split = (str) => {
  if (isString(str)) {
    const text = str.trim();
    if (text.startsWith('http')) {
      const parts = str.split(',');
      const urls: any = [];
      let currentUrl = '';
      for (const part of parts) {
        if (part.startsWith('http://') || part.startsWith('https://')) {
          // 如果遇到新的URL开头，保存当前URL并开始新的URL
          if (currentUrl) {
            urls.push(currentUrl);
          }
          currentUrl = part;
        } else {
          // 否则，是当前URL的一部分（如参数）
          currentUrl += ',' + part;
        }
      }
      // 添加最后一个URL
      if (currentUrl) {
        urls.push(currentUrl);
      }
      return urls;
    } else {
      return str.split(',');
    }
  }
  return str;
};
