import type { Ref, ComponentInternalInstance } from 'vue';
import { unref, isRef } from 'vue';
import { isFunction } from '/@/utils/is';

type dispatchEventOptions = {
  // JVxeTable 的 props
  props;
  // 触发的 event 事件对象
  $event;
  // 行、列
  row?;
  column?;
  // JVxeTable的vue3实例
  instance?: ComponentInternalInstance | any;
  // 要寻找的className
  className: string;
  // 重写找到dom后的处理方法
  handler?: Fn;
  // 是否直接执行click方法而不是模拟click事件
  isClick?: boolean;
};

/** 模拟触发事件 */
export function dispatchEvent(options: dispatchEventOptions) {
  const { props, $event, row, column, instance, className, handler, isClick } = options;
  if ((!$event || !$event.path) && !instance) {
    return;
  }
  // alwaysEdit 下不模拟触发事件，否者会导致触发两次
  if (props && props.alwaysEdit) {
    return;
  }
  let getCell = () => {
    let paths: HTMLElement[] = [...($event?.path ?? [])];
    // 通过 instance 获取 cell dom对象
    if (row && column) {
      let selector = `table.vxe-table--body tbody tr[rowid='${row.id}'] td[colid='${column.id}']`;
      let cellDom = instance!.vnode?.el?.querySelector(selector);
      // -update-begin--author:liaozhiyang---date:20230830---for：【QQYUN-6390】解决online新增字段警告（兼容下）
      if (!cellDom) {
        cellDom = instance!.$el?.querySelector(selector);
      }
      // -update-begin--author:liaozhiyang---date:20230830---for：【QQYUN-6390】解决online新增字段警告（兼容下）
      if (cellDom) {
        paths.unshift(cellDom);
      }
    }
    for (const el of paths) {
      if (el.classList?.contains('vxe-body--column')) {
        return el;
      }
    }
    return null;
  };
  let cell = getCell();
  if (cell) {
    window.setTimeout(() => {
      let getElement = () => {
        let classList = className.split(' ');
        if (classList.length > 0) {
          const getClassName = (cls: string) => {
            if (cls.startsWith('.')) {
              return cls.substring(1, cls.length);
            }
            return cls;
          };
          let get = (target, className, idx = 0) => {
            let elements = target.getElementsByClassName(getClassName(className));
            if (elements && elements.length > 0) {
              return elements[idx];
            }
            return null;
          };
          let element: HTMLElement = get(cell, classList[0]);
          for (let i = 1; i < classList.length; i++) {
            if (!element) {
              break;
            }
            element = get(element, classList[i]);
          }
          return element;
        }
        return null;
      };
      let element = getElement();
      if (element) {
        if (isFunction(handler)) {
          handler(element);
        } else {
          // 模拟触发点击事件
          if (isClick) {
            element.click();
          } else {
            element.dispatchEvent($event);
          }
        }
      }
    }, 10);
  } else {
    console.warn('【JVxeTable】dispatchEvent 获取 cell 失败');
  }
}

/** 绑定 VxeTable 数据 */
export function vModel(value, row, column: Ref<any> | string) {
  // @ts-ignore
  let property = isRef(column) ? column.value.property : column;
  unref(row)[property] = value;
}
