import type { ValidationRule } from 'ant-design-vue/lib/form/Form';
import type { ComponentType } from './types/index';
import { useI18n } from '/@/hooks/web/useI18n';
import { dateUtil } from '/@/utils/dateUtil';
import { isNumber, isObject } from '/@/utils/is';

const { t } = useI18n();

/**
 * @description: 生成placeholder
 */
export function createPlaceholderMessage(component: ComponentType) {
  if (component.includes('Input') || component.includes('Complete')) {
    return t('common.inputText');
  }
  if (component.includes('Picker')) {
    return t('common.chooseText');
  }
  if (
    component.includes('Select') ||
    component.includes('Cascader') ||
    component.includes('Checkbox') ||
    component.includes('Radio') ||
    component.includes('Switch')
  ) {
    // return `请选择${label}`;
    return t('common.chooseText');
  }
  return '';
}

const DATE_TYPE = ['DatePicker', 'MonthPicker', 'WeekPicker', 'TimePicker'];

function genType() {
  return [...DATE_TYPE, 'RangePicker'];
}

export function setComponentRuleType(rule: ValidationRule, component: ComponentType, valueFormat: string) {
  //update-begin---author:wangshuai---date:2024-02-01---for:【QQYUN-8176】编辑表单中,校验必填时,如果组件是ApiSelect,打开编辑页面时,即使该字段有值,也会提示请选择---
  //https://github.com/vbenjs/vue-vben-admin/pull/3082 github修复原文
  if (Reflect.has(rule, 'type')) {
    return;
  }
  //update-end---author:wangshuai---date:2024-02-01---for:【QQYUN-8176】编辑表单中,校验必填时,如果组件是ApiSelect,打开编辑页面时,即使该字段有值,也会提示请选择---
  if (['DatePicker', 'MonthPicker', 'WeekPicker', 'TimePicker'].includes(component)) {
    rule.type = valueFormat ? 'string' : 'object';
  } else if (['RangePicker', 'Upload', 'CheckboxGroup', 'TimePicker'].includes(component)) {
    rule.type = 'array';
  } else if (['InputNumber'].includes(component)) {
    rule.type = 'number';
  }
}

export function processDateValue(attr: Recordable, component: string) {
  const { valueFormat, value } = attr;
  if (valueFormat) {
    attr.value = isObject(value) ? dateUtil(value).format(valueFormat) : value;
  } else if (DATE_TYPE.includes(component) && value) {
    attr.value = dateUtil(attr.value);
  }
}

export function handleInputNumberValue(component?: ComponentType, val?: any) {
  if (!component) return val;
  if (['Input', 'InputPassword', 'InputSearch', 'InputTextArea'].includes(component)) {
    return val && isNumber(val) ? `${val}` : val;
  }
  return val;
}
/** 
*liaozhiyang
*2023-12-26
*某些组件的传值需要把字符串类型转成数值类型
*/ 
export function handleInputStringValue(component?: ComponentType, val?: any) {
  if (!component) return val;
  // update-begin--author:liaozhiyang---date:20240517---for：【TV360X-13】InputNumber设置精确3位小数传入''变成了0.00
  if (['InputNumber'].includes(component) && typeof val === 'string' && val != '') {
    return Number(val);
  }
  // update-end--author:liaozhiyang---date:20240517---for：【TV360X-13】InputNumber设置精确3位小数传入''变成了0.00
  return val;
}

/**
 * 时间字段
 */
export const dateItemType = genType();
