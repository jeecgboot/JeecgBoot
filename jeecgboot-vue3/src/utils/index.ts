import type { RouteLocationNormalized, RouteRecordNormalized } from 'vue-router';
import type { App, Plugin } from 'vue';
import type { FormSchema, FormActionType } from "@/components/Form";

import { unref } from 'vue';
import { isObject, isFunction, isString } from '/@/utils/is';
import { dynamicPages } from './dynamicPages';
import Big from 'big.js';
import dayjs from "dayjs";
// 代码逻辑说明: 【VUEN-656】配置外部网址打不开，原因是带了#号，需要替换一下
export const URL_HASH_TAB = `__AGWE4H__HASH__TAG__PWHRG__`;

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
    // 代码逻辑说明: 【QQYUN-7872】online表单label较长优化
    if (isObject(src[key]) && isObject(target[key])) {
      src[key] = deepMerge(src[key], target[key]);
    } else {
      src[key] = target[key];
    }
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
  let formSchema = unref(unref(props)?.schemas)
  let valueType = 'string';
  if (formSchema) {
    let schema = formSchema.filter((item) => item.field === field)[0];
    // 代码逻辑说明: 【issues/8976】useListPage 查询中componentProps是函数时获取不到valueType
    if (schema && schema.componentProps) {
      if (isFunction(schema.componentProps)) {
        try {
          const result = schema.componentProps({ schema, tableAction: {}, formModel: {}, formActionType: {} });
          valueType = result?.valueType ?? valueType;
        } catch (err) {}
      } else {
        valueType = schema.componentProps.valueType ? schema.componentProps.valueType : valueType;
      }
    }
  }
  return valueType;
}

/**
 * 获取表单字段值数据类型
 * @param schema
 * @param formAction
 */
export function getValueTypeBySchema(schema: FormSchema, formAction: FormActionType) {
  let valueType = 'string';
  if (schema) {
    const componentProps = formAction.getSchemaComponentProps(schema);
    // 代码逻辑说明: 【issues/8738】componentProps是函数时获取不到valueType
    if (isFunction(componentProps)) {
      try {
        const result = componentProps({ schema, tableAction: {}, formModel: {}, formActionType: {} });
        valueType = result?.valueType ?? valueType;
      } catch (err) {}
    } else {
      valueType = componentProps?.valueType ? componentProps?.valueType : valueType;
    }
  }
  return valueType;
}

/**
 * 通过picker属性获取日期数据
 * @param data
 * @param picker
 */
export function getDateByPicker(data, picker) {
  if (!data || !picker) {
    return data;
  }
  /**
   * 需要把年、年月、设置成这段时间内的第一天（[年季度]不需要处理antd回传的就是该季度的第一天，[年周]也不处理）
   * 例如日期格式是年，传给数据库的时间必须是20240101
   * 例如日期格式是年月（选择了202502），传给数据库的时间必须是20250201
   */
  if (picker === 'year') {
    return dayjs(data).set('month', 0).set('date', 1).format('YYYY-MM-DD');
  } else if (picker === 'month') {
    return dayjs(data).set('date', 1).format('YYYY-MM-DD');
  } else if (picker === 'week') {
    return dayjs(data).startOf('week').format('YYYY-MM-DD');
  }
  return data;
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
    // 代码逻辑说明: 【issues/7493】numToUpper方法返回解决错误
    value = new Big(value).times(100);
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

// 代码逻辑说明: 解决老的vue2动态导入文件语法 vite不支持的问题
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
    for (const path in dynamicPages) {
      if (path == page) {
        flag = false;
        dynamicPages[path]().then((mod) => {
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


/**
 * 跳转至积木报表的 预览页面
 * @param url
 * @param id
 * @param token
 */
export function goJmReportViewPage(url, id, token) {
  // URL支持{{ window.xxx }}占位符变量
  url = url.replace(/{{([^}]+)?}}/g, (_s1, s2) => _eval(s2))
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
/**
  * 处理word文档中的o:p标签
  * @param html
 */
export const removeSpecialTags = (html: string): string => {
  if (!html) return '';
  try {
    const BORDER = '1px solid #8c8c8c';

    // ===================================================================
    // 第一步：移除 Office 垃圾标签（纯字符串）
    // ===================================================================
    // o:p 标签（转义和普通形式）
    html = html.replace(/&lt;o:p[^&]*?&gt;.*?&lt;\/o:p&gt;/gis, '');
    html = html.replace(/&lt;o:p[^&]*?\/?&gt;/gis, '');
    html = html.replace(/&lt;\/o:p&gt;/gis, '');
    html = html.replace(/<o:p[^>]*>.*?<\/o:p>/gis, '');
    html = html.replace(/<o:p[^>]*\/?>/gis, '');
    html = html.replace(/<\/o:p>/gis, '');

    // style 标签（转义和普通形式）
    html = html.replace(/&lt;style[^&]*?&gt;.*?&lt;\/style&gt;/gis, '');
    html = html.replace(/&lt;style[^&]*?\/?&gt;/gis, '');
    html = html.replace(/&lt;\/style&gt;/gis, '');
    html = html.replace(/<style[^>]*>.*?<\/style>/gis, '');

    // 条件注释和 Office 命名空间标签
    html = html.replace(/<!--\[if[^>]*>.*?<!\[endif\]-->/gis, '');
    html = html.replace(/<\/?w:[^>]*>/gis, '');
    html = html.replace(/<\/?xml[^>]*>/gis, '');
    html = html.replace(/<\/?v:[^>]*>/gis, '');

    // ===================================================================
    // 第二步：DOM 清理（仅清理空段落，完全不动表格行结构）
    //   ★ 关键：放在边框处理之前，防止 DOM 序列化丢失 !important
    // ===================================================================
    try {
      const DOMParserCtor: any =
          typeof DOMParser !== 'undefined' ? DOMParser : (globalThis as any).DOMParser;
      if (DOMParserCtor) {
        const parser = new DOMParserCtor() as DOMParser;
        const doc = parser.parseFromString(html, 'text/html') as Document;

        // 仅清理单元格内的空 <p> 标签
        const cells = doc.querySelectorAll('td,th');
        for (let ci = 0; ci < cells.length; ci++) {
          const cell = cells[ci] as HTMLElement;
          if (!cell) continue;
          const ps = cell.querySelectorAll('p');
          for (let pi = ps.length - 1; pi >= 0; pi--) {
            const p = ps[pi] as HTMLParagraphElement;
            if (!p) continue;
            const content = (p.innerHTML || '').replace(/&nbsp;|\u00A0/g, '').trim();
            if (content === '') {
              p.remove();
            } else {
              const frag = doc.createDocumentFragment();
              while (p.firstChild) frag.appendChild(p.firstChild);
              if (p.parentNode) p.parentNode.replaceChild(frag, p);
            }
          }
        }

        html = doc.body.innerHTML;
      }
    } catch (_) {
      // DOM 不可用，跳过段落清理（不影响边框修复）
    }

    // ===================================================================
    // 第三步：注入全局表格样式 <style>（兜底保障）
    //   ★ 放在 DOM 序列化之后，确保 <style> 标签不会被 DOM 改写
    // ===================================================================
    const globalCSS = `<style>
table{border-collapse:collapse!important;border-spacing:0!important;width:100%!important;max-width:100%!important;}
table td,table th{border-top:${BORDER}!important;border-right:${BORDER}!important;border-bottom:${BORDER}!important;border-left:${BORDER}!important;padding:6px 8px!important;vertical-align:middle!important;box-sizing:border-box!important;empty-cells:show!important;}
table td p,table th p{margin:0!important;padding:0!important;line-height:1.4!important;}
img{max-width:100%!important;height:auto!important;display:block!important;}
</style>`;

    if (/<head[^>]*>/i.test(html)) {
      html = html.replace(/(<head[^>]*>)/i, `$1${globalCSS}`);
    } else if (/<html[^>]*>/i.test(html)) {
      html = html.replace(/(<html[^>]*>)/i, `$1<head>${globalCSS}</head>`);
    } else {
      html = globalCSS + html;
    }

    // ===================================================================
    // 第四步：纯字符串处理内联样式（核心边框修复）
    //   ★ 最后执行！确保输出 HTML 中的 !important 不会被任何后续处理丢失
    //
    //   策略：按分号拆分 style 值，逐条判断属性名，
    //         过滤掉所有 border* 和 mso-* 声明，
    //         然后追加统一的边框声明（含 !important）
    // ===================================================================

    /**
     * 从 CSS style 字符串中移除所有 border* 和 mso-* 声明，
     * 保留其余布局相关属性（width, height, padding, text-align 等）
     */
    function stripBorderAndMso(styleStr: string): string {
      return styleStr
          .split(';')
          .filter(function (decl) {
            const trimmed = decl.trim();
            if (!trimmed) return false;
            const colonIdx = trimmed.indexOf(':');
            if (colonIdx === -1) return true; // 保留无效声明（安全起见）
            const prop = trimmed.substring(0, colonIdx).trim().toLowerCase();
            // 移除所有 border 开头和 mso- 开头的属性
            if (prop.startsWith('border') || prop.startsWith('mso-')) return false;
            return true;
          })
          .join(';');
    }

    // 4a. 移除 <table> 上的 border="0" HTML 属性
    html = html.replace(/(<table\b[^>]*?)\sborder\s*=\s*['"]?0['"]?/gi, '$1');

    // 4b. 处理 <table> 标签的内联样式
    html = html.replace(/<table(\s[^>]*)>/gi, function (fullMatch, attrs) {
      try {
        const sm = attrs.match(/\sstyle\s*=\s*(["'])([\s\S]*?)\1/i);
        if (sm) {
          const q = sm[1];
          const cleaned = stripBorderAndMso(sm[2]);
          const newStyle = cleaned
              + ';border-collapse:collapse!important'
              + ';border-spacing:0!important'
              + ';width:100%!important';
          return '<table' + attrs.replace(sm[0], ` style=${q}${newStyle}${q}`) + '>';
        }
        return '<table' + attrs
            + ' style="border-collapse:collapse!important;border-spacing:0!important;width:100%!important">';
      } catch (_) { return fullMatch; }
    });

    // 4c. 处理 <td>/<th> 标签的内联样式
    const cellBorderCSS =
        `border-top:${BORDER}!important;` +
        `border-right:${BORDER}!important;` +
        `border-bottom:${BORDER}!important;` +
        `border-left:${BORDER}!important;` +
        'padding:6px 8px!important;' +
        'vertical-align:middle!important;' +
        'box-sizing:border-box!important';

    html = html.replace(/<(td|th)(\s[^>]*)>/gi, function (fullMatch, tag, attrs) {
      try {
        const sm = attrs.match(/\sstyle\s*=\s*(["'])([\s\S]*?)\1/i);
        if (sm) {
          const q = sm[1];
          const cleaned = stripBorderAndMso(sm[2]);
          const newStyle = (cleaned ? cleaned + ';' : '') + cellBorderCSS;
          return '<' + tag + attrs.replace(sm[0], ` style=${q}${newStyle}${q}`) + '>';
        }
        return '<' + tag + attrs + ` style="${cellBorderCSS}">`;
      } catch (_) { return fullMatch; }
    });

    return html;
  } catch (_) {
    return html;
  }
};


