import { computed, nextTick, ref, unref, watch } from 'vue';
import { propTypes } from '/@/utils/propTypes';
import { useDesign } from '/@/hooks/web/useDesign';
import { getEnhanced, replaceProps } from '../utils/enhancedUtils';
import { vModel } from '/@/components/jeecg/JVxeTable/utils';
import { JVxeRenderType } from '../types/JVxeTypes';
import { isBoolean, isFunction, isObject, isPromise } from '/@/utils/is';
import { JVxeComponent } from '../types/JVxeComponent';
import { filterDictText } from '/@/utils/dict/JDictSelectUtil';

export function useJVxeCompProps() {
  return {
    // 组件类型
    type: propTypes.string,
    // 渲染类型
    renderType: propTypes.string.def('default'),
    // 渲染参数
    params: propTypes.object,
    // 渲染自定义选项
    renderOptions: propTypes.object,
  };
}

export function useJVxeComponent(props: JVxeComponent.Props) {
  const value = computed(() => {
    // update-begin--author:liaozhiyang---date:20240430---for：【QQYUN-9125】oracle数据库日期类型字段会默认带上时分秒
    const val = props.params.row[props.params.column.property];
    if (props.type === 'date' && typeof val === 'string') {
      return val.split(' ').shift();
    } else {
      return val;
    }
    // update-end--author:liaozhiyang---date:20240430---for：【QQYUN-9125】oracle数据库日期类型字段会默认带上时分秒
  });
  const innerValue = ref(value.value);
  const row = computed(() => props.params.row);
  const rows = computed(() => props.params.data);
  const column = computed(() => props.params.column);
  // 用户配置的原始 column
  const originColumn = computed(() => column.value.params);
  const rowIndex = computed(() => props.params._rowIndex);
  const columnIndex = computed(() => props.params._columnIndex);
  // 表格数据长度
  const fullDataLength = computed(() => props.params.$table.internalData.tableFullData.length);
  // 是否正在滚动中
  const scrolling = computed(() => !!props.renderOptions.scrolling);
  const cellProps = computed(() => {
    let renderOptions = props.renderOptions;
    let col = originColumn.value;

    let cellProps = {};

    // 输入占位符
    cellProps['placeholder'] = replaceProps(col, col.placeholder);

    // 解析props
    if (isObject(col.props)) {
      Object.keys(col.props).forEach((key) => {
        cellProps[key] = replaceProps(col, col.props[key]);
      });
    }

    // 判断是否是禁用的列
    cellProps['disabled'] = isBoolean(col['disabled']) ? col['disabled'] : cellProps['disabled'];
    // 判断是否禁用行
    if (renderOptions.isDisabledRow(row.value, rowIndex.value)) {
      cellProps['disabled'] = true;
    }
    // update-begin--author:liaozhiyang---date:20240528---for：【TV360X-291】没勾选同步数据库禁用排序功能
    if (col.props && col.props.isDisabledCell) {
      if (col.props.isDisabledCell({ row: row.value, rowIndex: rowIndex.value, column: col, columnIndex: columnIndex.value })) {
        cellProps['disabled'] = true;
      }
    }
    // update-end--author:liaozhiyang---date:20240528---for：【TV360X-291】没勾选同步数据库禁用排序功能
    // 判断是否禁用所有组件
    if (renderOptions.disabled === true) {
      cellProps['disabled'] = true;
      // update-begin--author:liaozhiyang---date:20240607---for：【TV360X-1068】行编辑整体禁用时上传按钮不显示
      cellProps['disabledTable'] = true;
      // update-end--author:liaozhiyang---date:20240607---for：【TV360X-1068】行编辑整体禁用时上传按钮不显示
    }
    //update-begin-author:taoyan date:2022-5-25 for: VUEN-1111 一对多子表 部门选择 不应该级联
    if (col.checkStrictly === true) {
      cellProps['checkStrictly'] = true;
    }
    //update-end-author:taoyan date:2022-5-25 for: VUEN-1111 一对多子表 部门选择 不应该级联

    //update-begin-author:taoyan date:2022-5-27 for: 用户组件 控制单选多选新的参数配置
    if (col.isRadioSelection === true) {
      cellProps['isRadioSelection'] = true;
    } else if (col.isRadioSelection === false) {
      cellProps['isRadioSelection'] = false;
    }
    //update-end-author:taoyan date:2022-5-27 for: 用户组件 控制单选多选新的参数配置

    return cellProps;
  });

  const listeners = computed(() => {
    let listeners = Object.assign({}, props.renderOptions.listeners || {});
    // 默认change事件
    if (!listeners.change) {
      listeners.change = async (event) => {
        vModel(event.value, row, column);
        await nextTick();
        // 处理 change 事件相关逻辑（例如校验）
        props.params.$table.updateStatus(props.params);
      };
    }
    return listeners;
  });
  const context = {
    innerValue,
    row,
    rows,
    rowIndex,
    column,
    columnIndex,
    originColumn,
    fullDataLength,
    cellProps,
    scrolling,
    handleChangeCommon,
    handleBlurCommon,
  };
  const ctx = { props, context };

  // 获取组件增强
  const enhanced = getEnhanced(props.type);

  watch(
    value,
    (newValue) => {
      // 验证值格式
      let getValue = enhanced.getValue(newValue, ctx);
      if (newValue !== getValue) {
        // 值格式不正确，重新赋值
        newValue = getValue;
        vModel(newValue, row, column);
      }
      innerValue.value = enhanced.setValue(newValue, ctx);
      // update-begin--author:liaozhiyang---date:20240509---for：【QQYUN-9205】一对多(jVxetable组件date)支持年，年月，年度度，年周
      if (props.type === 'date' && props.renderType === JVxeRenderType.spaner && enhanced.translate.enabled === true) {
        if (isFunction(enhanced.translate.handler)) {
          innerValue.value = enhanced.translate.handler(newValue, ctx);
        }
        return;
      }
      // update-end--author:liaozhiyang---date:20240509---for：【QQYUN-9205】一对多(jVxetable组件date)支持年，年月，年度度，年周
      // 判断是否启用翻译
      if (props.renderType === JVxeRenderType.spaner && enhanced.translate.enabled === true) {
        if (isFunction(enhanced.translate.handler)) {
          let res = enhanced.translate.handler(newValue, ctx);
          // 异步翻译，可解决字典查询慢的问题
          if (isPromise(res)) {
            res.then((v) => (innerValue.value = v));
          } else {
            innerValue.value = res;
          }
        }
      }
    },
    { immediate: true }
  );

  /** 通用处理 change 事件 */
  function handleChangeCommon($value, force = false) {
    const newValue = enhanced.getValue($value, ctx);
    const oldValue = value.value;
    // update-begin--author:liaozhiyang---date:20230718---for：【issues-5025】JVueTable的事件 @valueChange重复触发问题
    const execute = force ? true : newValue !== oldValue;
    if (execute) {
      trigger('change', { value: newValue });
      // 触发valueChange事件
      parentTrigger('valueChange', {
        type: props.type,
        value: newValue,
        oldValue: oldValue,
        col: originColumn.value,
        rowIndex: rowIndex.value,
        columnIndex: columnIndex.value,
      });
    }
    // update-end--author:liaozhiyang---date:20230718---for：【issues-5025】JVueTable的事件 @valueChange重复触发问题
  }

  /** 通用处理 blur 事件 */
  function handleBlurCommon($value) {
    // update-begin--author:liaozhiyang---date:20230817---for：【issues/636】JVxeTable加上blur事件
    const newValue = enhanced.getValue($value, ctx);
    const oldValue = value.value;
    //trigger('blur', { value });
    // 触发blur事件
    parentTrigger('blur', {
      type: props.type,
      value: newValue,
      oldValue: oldValue,
      col: originColumn.value,
      rowIndex: rowIndex.value,
      columnIndex: columnIndex.value,
    });
    // update-end--author:liaozhiyang---date:20230817---for：【issues/636】JVxeTable加上blur事件
  }

  /**
   * 如果事件存在的话，就触发
   * @param name 事件名
   * @param event 事件参数
   * @param args 其他附带参数
   */
  function trigger(name, event?, args: any[] = []) {
    let listener = listeners.value[name];
    if (isFunction(listener)) {
      if (isObject(event)) {
        event = packageEvent(name, event);
      }
      listener(event, ...args);
    }
  }

  function parentTrigger(name, event, args: any[] = []) {
    args.unshift(packageEvent(name, event));
    trigger('trigger', name, args);
  }

  function packageEvent(name, event: any = {}) {
    event.row = row.value;
    event.column = column.value;
    // online增强参数兼容
    event.column['key'] = column.value['property'];
    // event.cellTarget = this
    if (!event.type) {
      event.type = name;
    }
    if (!event.cellType) {
      event.cellType = props.type;
    }
    // 是否校验表单，默认为true
    if (isBoolean(event.validate)) {
      event.validate = true;
    }
    return event;
  }

  /**
   * 防样式冲突类名生成器
   * @param scope
   */
  function useCellDesign(scope: string) {
    return useDesign(`vxe-cell-${scope}`);
  }

  return {
    ...context,
    enhanced,
    trigger,
    useCellDesign,
  };
}

/**
 * 获取组件默认增强
 */
export function useDefaultEnhanced(): JVxeComponent.EnhancedPartial {
  return {
    installOptions: {
      autofocus: '',
    },
    interceptor: {
      'event.clearActived': () => true,
      'event.clearActived.className': () => true,
    },
    switches: {
      editRender: true,
      visible: false,
    },
    aopEvents: {
      editActived() {},
      editClosed() {},
      activeMethod: () => true,
    },
    translate: {
      enabled: false,
      handler(value, ctx) {
        // 默认翻译方法
        if (ctx) {
          return filterDictText(unref(ctx.context.column).params.options, value);
        } else {
          return value;
        }
      },
    },
    getValue: (value) => value,
    setValue: (value) => value,
    createValue: (defaultValue) => defaultValue,
  } as JVxeComponent.Enhanced;
}
