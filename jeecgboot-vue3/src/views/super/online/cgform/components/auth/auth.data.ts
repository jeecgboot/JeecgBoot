import { computed } from 'vue';
import dayjs from 'dayjs';
import { BasicColumn, FormSchema } from '/@/components/Table';
import { getAreaTextByCodeAnyLevel } from '/@/components/Form/src/utils/Area';
import { provinceOptions, provinceAndCityData, regionData } from '/@/components/Form/src/utils/areaDataUtil';
import { useConditionFilter } from '/@/utils/index';

// 字段权限列配置
export const authFieldColumns: BasicColumn[] = [
  {
    title: '启用',
    dataIndex: 'switch',
    width: 100,
    align: 'center',
    slots: { customRender: 'switch' },
  },
  {
    title: '字段名称',
    width: 200,
    dataIndex: 'code',
  },
  {
    title: '字段描述',
    // width: 200,
    dataIndex: 'title',
  },
  {
    title: '列表控制',
    dataIndex: 'list',
    width: 120,
    slots: { customRender: 'list' },
  },
  {
    title: '表单控制',
    dataIndex: 'form',
    width: 180,
    slots: { customRender: 'form' },
  },
];

// 按钮权限列配置
export const authButtonColumns: BasicColumn[] = [
  {
    title: '启用',
    dataIndex: 'switch',
    width: 80,
    slots: { customRender: 'switch' },
  },
  {
    title: '名称',
    dataIndex: 'title',
  },
  {
    title: '编码',
    dataIndex: 'code',
  },
  {
    title: '权限控制',
    dataIndex: 'control',
    width: 180,
    slots: { customRender: 'control' },
  },
];

export const authButtonFixedList = [
  { code: 'add', title: '新增', status: 0 },
  { code: 'addSub', title: '添加下级', status: 0 },
  { code: 'edit', title: '编辑', status: 0 },
  { code: 'detail', title: '详情', status: 0 },
  { code: 'delete', title: '删除', status: 0 },
  { code: 'batch_delete', title: '批量删除', status: 0 },
  { code: 'export', title: '导出', status: 0 },
  { code: 'import', title: '导入', status: 0 },
  { code: 'print', title: '打印', status: 0 },
  { code: 'query', title: '查询', status: 0 },
  { code: 'reset', title: '重置', status: 0 },
  { code: 'aigc_mock_data', title: '生成测试数据', status: 0 },
  { code: 'bpm', title: '提交流程', status: 0 },
  { code: 'super_query', title: '高级查询', status: 0 },
  { code: 'form_confirm', title: '确定', status: 0 },
  // update-begin--author:liusq---date:20250721---for：【LHZP-540】一对多子表按钮权限控制
  { code: 'popup_add', title: '弹窗新增', status: 0 },
  { code: 'popup_update', title: '弹窗编辑', status: 0 },
  // update-end--author:liusq---date:20250721---for：【LHZP-540】一对多子表按钮权限控制
];

export const USE_SQL_RULES = 'USE_SQL_RULES';

const FUZZY_RULE_OPERATORS = ['LIKE', 'RIGHT_LIKE', 'LEFT_LIKE'];
const FUZZY_RULE_VIEWS = [
  'list',
  'list_multi',
  'radio',
  'checkbox',
  'sel_search',
  'sel_user',
  'sel_depart',
  'link_table',
  'popup_dict',
  'cat_tree',
  'sel_tree',
  'pca',
];
const FUZZY_RULE_OPTIONS = [
  { label: '模糊', value: 'LIKE' },
  { label: '以..开始', value: 'RIGHT_LIKE' },
  { label: '以..结尾', value: 'LEFT_LIKE' },
];

export function isFuzzyRuleOperator(operator) {
  return FUZZY_RULE_OPERATORS.includes(operator);
}

/** 为保存编码/键值的选择类字段补充模糊匹配条件，不影响全局条件过滤规则。 */
export function supplementAuthFuzzyConditions(view, options) {
  if (!FUZZY_RULE_VIEWS.includes(view)) return options;
  const missingOptions = FUZZY_RULE_OPTIONS.filter((option) => !options.some((item) => item.value === option.value));
  if (!missingOptions.length) return options;
  const equalIndex = options.findIndex((item) => item.value === '=');
  const insertIndex = equalIndex < 0 ? 0 : equalIndex + 1;
  return [...options.slice(0, insertIndex), ...missingOptions, ...options.slice(insertIndex)];
}

/** 数据权限规则字段仅支持已同步数据库的非 popup 字段。 */
export function isAuthDataField(field) {
  return Number(field?.dbIsPersist) === 1 && String(field?.fieldShowType || '').toLowerCase() !== 'popup';
}

// update-begin--author:scott---date:20260722---for：【数据权限】规则值/条件规则根据规则字段控件类型自动适配，参考FormSchemaFactory的控件映射方式
// 系统字典驱动的控件（下拉、单选、多选、下拉多选）
const DICT_CODE_VIEWS = ['list', 'list_multi', 'radio', 'checkbox'];
// 表字典驱动的控件，其 dictTable/dictText/dictField 语义与系统表字典一致，可直接复用 table,text,code 格式
// （关联记录和 Popup 字典使用各自的专用组件；cat_tree 的字典配置格式不同，暂不纳入自动转换）
const DICT_TABLE_VIEWS = ['sel_search'];
// 日期时间类控件
const DATE_TIME_VIEWS = ['date', 'datetime', 'time', 'date_year', 'date_month', 'date_week', 'date_quarter'];

function buildDictCode(dictField, dictTable, dictText) {
  if (dictTable) {
    // 表字典，格式与 SelectWidget/LinkTableWidget 保持一致：table,text,code
    return encodeURI(`${dictTable},${dictText || dictField},${dictField}`);
  }
  return dictField;
}

/** 获取规则字段对应的字典编码，列表展示与规则值组件共用同一字典配置。 */
export function getRuleValueDictCode(field) {
  if (!field) return '';
  const { view, dictField, dictTable, dictText } = field;
  if (DICT_CODE_VIEWS.includes(view) && dictField) {
    return buildDictCode(dictField, dictTable, dictText);
  }
  if (DICT_TABLE_VIEWS.includes(view) && dictTable && dictField) {
    return buildDictCode(dictField, dictTable, dictText);
  }
  if (view === 'link_table' && dictTable && dictField) {
    // 关联记录可配置多个显示字段，表字典翻译只取第一个，确保生成 table,text,code 三段式编码
    const firstTextField = (dictText || dictField).split(',')[0];
    return buildDictCode(dictField, dictTable, firstTextField);
  }
  return '';
}

function dictSelectWidget(dictCode, multiple = false) {
  return {
    component: 'JDictSelectTag',
    componentProps: {
      dictCode,
      allowClear: true,
      getPopupContainer: () => document.body,
      // IN 条件时多选，多值以逗号分隔存储
      ...(multiple ? { mode: 'multiple' } : {}),
    },
  };
}

// 关联记录，参考 JLinkTableCard 的 table 模式
function linkTableWidget(field, multiple = false) {
  const { dictTable, dictField, dictText, fieldExtendJson } = field;
  if (!dictTable || !dictField || !dictText) return null;
  let imageField = '';
  try {
    imageField = fieldExtendJson ? JSON.parse(fieldExtendJson).imageField || '' : '';
  } catch (_error) {}
  return {
    component: 'JLinkTableCard',
    componentProps: {
      queryMode: 'table',
      tableName: dictTable,
      valueField: dictField,
      textField: dictText,
      imageField,
      multi: multiple,
    },
  };
}

/** 获取 Popup 字典配置，格式与 PopupDictWidget 保持一致：报表编码,显示字段,存值字段。 */
export function getPopupDictConfig(field) {
  const { dictTable, dictField, dictText } = field;
  if (!dictTable || !dictField || !dictText) return null;
  return {
    code: dictTable,
    labelField: dictText,
    valueField: dictField,
    dictCode: `${dictTable},${dictText},${dictField}`,
  };
}

// Popup 字典
function popupDictWidget(field, multiple = false) {
  const config = getPopupDictConfig(field);
  if (!config) return null;
  return {
    component: 'JPopupDict',
    componentProps: {
      dictCode: config.dictCode,
      multi: multiple,
      allowClear: true,
    },
  };
}

const AREA_DISPLAY_LEVELS = ['province', 'city', 'region', 'all'];

/** 从字段扩展配置中读取有效的省市区展示层级，非法 JSON 按未配置处理。 */
function getAreaDisplayLevel(fieldExtendJson) {
  try {
    const displayLevel = fieldExtendJson ? JSON.parse(fieldExtendJson).displayLevel : undefined;
    return AREA_DISPLAY_LEVELS.includes(displayLevel) ? displayLevel : undefined;
  } catch (_error) {
    return undefined;
  }
}

/** 将级联地区树展开为多选下拉项，并保留完整层级名称避免同名地区混淆。 */
function flattenAreaOptions(options, parentLabels: string[] = []) {
  return options.flatMap((option) => {
    const labels = [...parentLabels, option.label];
    if (option.children?.length) {
      return flattenAreaOptions(option.children, labels);
    }
    return [{ value: option.value, label: labels.join('/') }];
  });
}

/** 根据字段展示层级选择省、市或区县数据源。 */
function getAreaOptions(displayLevel) {
  if (displayLevel === 'province') return provinceOptions;
  if (displayLevel === 'city') return flattenAreaOptions(provinceAndCityData);
  return flattenAreaOptions(regionData);
}

// 省市区，参考 auto/comp/factory/impl/AreaLinkage.ts；IN 条件使用扁平多选，值仍保存地区编码
function areaWidget(fieldExtendJson, multiple = false) {
  const displayLevel = getAreaDisplayLevel(fieldExtendJson);
  if (multiple) {
    return {
      component: 'JDictSelectTag',
      componentProps: {
        options: getAreaOptions(displayLevel),
        mode: 'multiple',
        allowClear: true,
        showSearch: true,
        onlySearchByLabel: true,
        getPopupContainer: () => document.body,
      },
    };
  }
  return {
    component: 'JAreaLinkage',
    componentProps: {
      saveCode: displayLevel && displayLevel !== 'all' ? displayLevel : 'region',
      ...(displayLevel ? { displayLevel } : {}),
      getPopupContainer: () => document.body,
    },
  };
}

// 日期、时间（含年/月/周/季度选择器），参考 auto/comp/factory/impl/DateWidget.ts
function dateTimeWidget(view, fieldExtendJson) {
  const viewPicker = view.startsWith('date_') ? view.substring(5) : undefined;
  const normalizedView = viewPicker ? 'date' : view;
  if (normalizedView === 'time') {
    return {
      component: 'TimePicker',
      componentProps: {
        valueFormat: 'HH:mm:ss',
        style: { width: '100%' },
        getPopupContainer: () => document.body,
      },
    };
  }
  let picker = viewPicker;
  if (normalizedView === 'date' && !picker && fieldExtendJson) {
    try {
      const json = JSON.parse(fieldExtendJson);
      if (json.picker && json.picker != 'default') {
        picker = json.picker; // year、month、week、quarter
      }
    } catch (_error) {}
  }
  const displayFormatMap = { year: 'YYYY', month: 'YYYY-MM', week: 'YYYY-wo', quarter: 'YYYY-[Q]Q' };
  // picker 特殊格式只用于显示；规则值统一保存为标准日期，避免 antd 内部解析 Q/wo 格式时抛错
  const valueFormat = normalizedView === 'datetime' ? 'YYYY-MM-DD HH:mm:ss' : 'YYYY-MM-DD';
  return {
    component: 'DatePicker',
    componentProps: {
      picker,
      format: picker ? displayFormatMap[picker] : undefined,
      showTime: normalizedView === 'datetime',
      valueFormat,
      style: { width: '100%' },
      getPopupContainer: () => document.body,
    },
  };
}

//update-begin--author:wangshuai---date:20260723---for：【LHZP-1267】年/年月规则值保存时归一化为当年/当月第一天
/**
 * 年/年月日期控件的规则值归一化：年保存为当年1月1日，年月保存为当月1日。
 * 年/年月选择器选中后 valueFormat 输出的是面板当天日期，直接保存会导致后端比较范围偏差。
 */
export function normalizeDateRuleValue(field, value) {
  if (!field || !value || typeof value !== 'string' || value.startsWith('#{')) return value;
  const { view, fieldExtendJson } = field;
  let picker = view.startsWith('date_') ? view.substring(5) : '';
  if (!picker && view === 'date' && fieldExtendJson) {
    try {
      const json = JSON.parse(fieldExtendJson);
      if (json.picker && json.picker !== 'default') picker = json.picker;
    } catch (_error) {}
  }
  if (picker === 'year') return `${value.substring(0, 4)}-01-01`;
  if (picker === 'month') return `${value.substring(0, 7)}-01`;
  if (picker === 'week') {
    // 周选择器保存为当周第一天（周一），按星期偏移计算
    const date = dayjs(value);
    if (!date.isValid()) return value;
    return date.subtract((date.day() + 6) % 7, 'day').format('YYYY-MM-DD');
  }
  return value;
}
//update-end--author:wangshuai---date:20260723---for：【LHZP-1267】年/年月规则值保存时归一化为当年/当月第一天

// 解析用户/部门选择控件在字段扩展配置中指定的存储字段与展示字段
export function getUserDepartExtend(field) {
  if (!field?.fieldExtendJson) return {};
  try {
    return typeof field.fieldExtendJson === 'string' ? JSON.parse(field.fieldExtendJson) : field.fieldExtendJson;
  } catch (_error) {
    return {};
  }
}

// 用户选择，参考 auto/comp/factory/impl/SelectUserWidget.ts（规则值为逗号分隔存储字段；IN 条件时允许多选，否则单选）
function userWidget(field, multiple = false) {
  const extend = getUserDepartExtend(field);
  const componentProps: any = {
    isRadioSelection: !multiple,
    showSelected: false,
    allowClear: true,
    showButton: true,
  };
  // 使用字段扩展配置中的存储字段/展示字段，与 Online 表单保存值保持一致
  if (extend.store) {
    componentProps.rowKey = extend.store;
  }
  if (extend.text) {
    componentProps.labelKey = extend.text;
  }
  return {
    component: 'JSelectUser',
    componentProps,
  };
}

// 部门选择，参考 auto/comp/factory/impl/SelectDepartWidget.ts（规则值为逗号分隔存储字段；IN 条件时允许多选，否则单选）
function departWidget(field, multiple = false) {
  const extend = getUserDepartExtend(field);
  const componentProps: any = {
    checkStrictly: true,
    multiple,
    showButton: true,
  };
  // 使用字段扩展配置中的存储字段/展示字段，与 Online 表单保存值保持一致
  if (extend.store) {
    componentProps.rowKey = extend.store;
  }
  if (extend.text) {
    componentProps.labelKey = extend.text;
  }
  return {
    component: 'JSelectDept',
    componentProps,
  };
}

/**
 * 根据"规则字段"对应的控件类型，计算出"规则值"应使用的组件及componentProps
 * 非字典类控件（文本、popup等）返回 null，沿用原来的 JInputSelect 输入框
 */
export function getRuleValueWidget(field, ruleOperator?) {
  if (!field) return null;
  if (isFuzzyRuleOperator(ruleOperator)) return null;
  // 条件规则为"在...中"(IN)时，字典/用户/部门类控件切换为多选（多值以逗号分隔存储，后端按逗号拆分拼 in 条件）
  const isIn = ruleOperator === 'IN';
  const { view, fieldExtendJson } = field;
  if (view === 'popup_dict') {
    return popupDictWidget(field, isIn);
  }
  if (view === 'link_table') {
    return linkTableWidget(field, isIn);
  }
  const dictCode = getRuleValueDictCode(field);
  if (dictCode) {
    return dictSelectWidget(dictCode, isIn);
  }
  if (view === 'pca') {
    return areaWidget(fieldExtendJson, isIn);
  }
  if (DATE_TIME_VIEWS.includes(view)) {
    return dateTimeWidget(view, fieldExtendJson);
  }
  if (view === 'sel_user') {
    return userWidget(field, isIn);
  }
  if (view === 'sel_depart') {
    return departWidget(field, isIn);
  }
  return null;
}

// 默认的规则值输入组件（文本输入 + 可选系统变量）
const DEFAULT_SYS_VAR_OPTIONS = [
  { label: '当前用户账号', value: '#{sys_user_code}' },
  { label: '当前用户名称', value: '#{sys_user_name}' },
  { label: '当前日期', value: '#{sys_date}' },
  { label: '当前时间', value: '#{sys_time}' },
  { label: '当前用户部门', value: '#{sys_org_code}' },
  { label: '登录用户部门ID', value: '#{sys_org_id}' },
  { label: '当前用户所有部门', value: '#{sys_multi_org_code}' },
  { label: '当前租户', value: '#{tenant_id}' },
];

/** 获取系统变量的中文名称，仅用于界面展示。 */
export function getSysVarLabel(value) {
  return DEFAULT_SYS_VAR_OPTIONS.find((item) => item.value === value)?.label;
}

/** 根据字段控件类型转换列表中的规则值文本。 */
export function getRuleValueDisplayText(field, value, ruleOperator?) {
  const rawValue = Array.isArray(value) ? value.join(',') : String(value ?? '');
  if (isFuzzyRuleOperator(ruleOperator)) return rawValue;
  if (field?.view === 'pca') {
    const displayLevel = getAreaDisplayLevel(field.fieldExtendJson);
    const levelMap = { province: 1, city: 2, region: 3 } as const;
    const level = levelMap[displayLevel];
    const values =
      Array.isArray(value) || ruleOperator === 'IN'
        ? rawValue
            .split(',')
            .map((item) => item.trim())
            .filter(Boolean)
        : [rawValue];
    return values.map((item) => getAreaTextByCodeAnyLevel(item, !level, level) || item).join('、');
  }
  return rawValue;
}

export const DEFAULT_RULE_VALUE_WIDGET = {
  component: 'JAuthRuleValue' as const,
  componentProps: {
    selectPlaceholder: '系统变量',
    inputPlaceholder: '请输入',
    getPopupContainer: () => document.body,
    options: DEFAULT_SYS_VAR_OPTIONS,
  },
};

/**
 * 将具体规则值组件包装到 JAuthRuleValue 中，使其仍然可以切换系统变量
 * @param widget {component, componentProps}
 */
export function wrapRuleValueWidget(widget) {
  return {
    component: 'JAuthRuleValue' as const,
    componentProps: {
      selectPlaceholder: '系统变量',
      inputPlaceholder: undefined,
      getPopupContainer: () => document.body,
      options: DEFAULT_SYS_VAR_OPTIONS,
      customComponent: widget,
    },
  };
}
// update-end--author:scott---date:20260722---for：【数据权限】规则值/条件规则根据规则字段控件类型自动适配，参考FormSchemaFactory的控件映射方式
// 数据权限列配置
export const authDataColumns: BasicColumn[] = [
  {
    title: '启用',
    dataIndex: 'switch',
    width: 80,
    slots: { customRender: 'switch' },
  },
  {
    title: '规则名称',
    dataIndex: 'ruleName',
    width: 130,
  },
  {
    title: '规则描述',
    dataIndex: 'description',
    align: 'left',
    slots: { customRender: 'description' },
  },
];

export function useAuthDataFormSchemas(props, methods) {
  const formSchemas = computed<FormSchema[]>(() => [
    {
      label: '规则名称',
      field: 'ruleName',
      required: true,
      component: 'Input',
      componentProps: {
        onChange: methods.onRuleNameChange,
      },
    },
    {
      label: '规则字段',
      field: 'ruleColumn',
      component: 'JSearchSelect',
      componentProps: {
        dictOptions: props.authFields,
        getPopupContainer: () => document.body,
        onChange: methods.onRuleColumnChange,
      },
      dynamicRules({ model }) {
        return [{ required: model.ruleOperator != USE_SQL_RULES, message: '请输入规则字段' }];
      },
      show: ({ model }) => model.ruleOperator != USE_SQL_RULES,
    },
    // -update-begin--author:liaozhiyang---date:20240617---for：【TV360X-201】权限管理条件根据控件过滤
    {
      label: '条件规则',
      field: 'ruleOperator',
      required: true,
      component: 'JDictSelectTag',
      componentProps: {
        options: [],
        onChange: methods.onRuleOperatorChange,
        getPopupContainer: () => document.body,
      },
      dynamicPropskey: 'options',
      dynamicPropsVal: ({ model }) => {
        const getFieldType = (type) => {
          if (['BigDecimal', 'double', 'int', 'long'].includes(type)) {
            return 'number';
          } else {
            return;
          }
        };
        const { filterCondition } = useConditionFilter();
        if (model.ruleColumn) {
          const findItem = props.authFields.find((item) => item.value === model.ruleColumn) ?? {};
          const filteredOptions = filterCondition({ view: findItem.view, fieldType: getFieldType(findItem.dbType) }).map((item) => ({
            label: item.label,
            value: item.val ?? item.value,
          }));
          const result = supplementAuthFuzzyConditions(findItem.view, filteredOptions);
          result.push({ value: 'USE_SQL_RULES', label: '自定义SQL' });
          return result;
        } else {
          return [{ value: 'USE_SQL_RULES', label: '自定义SQL' }];
        }
      },
    },
    // {
    //   label: '条件规则',
    //   field: 'ruleOperator',
    //   required: true,
    //   component: 'JDictSelectTag',
    //   componentProps: {
    //     dictCode: 'rule_conditions',
    //     onChange: methods.onRuleOperatorChange,
    //     getPopupContainer: () => document.body,
    //   },
    // },
    // -update-end--author:liaozhiyang---date:20240617---for：【TV360X-201】权限管理条件根据控件过滤
    {
      label: '规则值',
      field: 'ruleValue',
      // -update-begin--author:liaozhiyang---date:20260713---for：【LHZP-600】条件规则为空和不为空时，规则值则隐藏
      dynamicRules({ model }) {
        if (['EMPTY', 'NOT_EMPTY'].includes(model.ruleOperator)) {
          return [];
        }
        return [
          {
            required: true,
            validator: (_rule, value) => {
              // IN 条件下多选组件的值为数组，需兼容数组判空
              const isEmpty = value == null || value === '' || (Array.isArray(value) && value.length === 0);
              return isEmpty ? Promise.reject(new Error('请输入规则值')) : Promise.resolve();
            },
          },
        ];
      },
      show: ({ model }) => !['EMPTY', 'NOT_EMPTY'].includes(model.ruleOperator),
      // -update-end--author:liaozhiyang---date:20260713---for：【LHZP-600】条件规则为空和不为空时，规则值则隐藏
      // -update-begin--author:liaozhiyang---date:20240607---for：【TV360X-536】数据权限配置配置优化及新增JInputSelect组件
      ...DEFAULT_RULE_VALUE_WIDGET,
      // -update-end--author:liaozhiyang---date:20240607---for：【TV360X-536】数据权限配置配置优化及新增JInputSelect组件
    },
    {
      label: '状态',
      field: 'status',
      required: true,
      component: 'RadioButtonGroup',
      componentProps: {
        options: [
          { label: '启用', value: 1 },
          { label: '不启用', value: 0 },
        ],
      },
      defaultValue: 1,
    },
  ]);
  return { formSchemas };
}
