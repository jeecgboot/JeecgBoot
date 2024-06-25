import type { App } from 'vue';
// 引入 vxe-table
import 'xe-utils';
import VXETable /*Grid*/ from 'vxe-table';
import VXETablePluginAntd from 'vxe-table-plugin-antd';
import 'vxe-table/lib/style.css';

import JVxeTable from './JVxeTable';
import { getEventPath } from '/@/utils/common/compUtils';
import { registerAllComponent } from './utils/registerUtils';
import { getEnhanced } from './utils/enhancedUtils';

export function registerJVxeTable(app: App) {
  // VXETable 全局配置
  const VXETableSettings = {
    // z-index 起始值
    zIndex: 1000,
    table: {},
  };

  // 添加事件拦截器 event.clearActived
  // 比如点击了某个组件的弹出层面板之后，此时被激活单元格不应该被自动关闭，通过返回 false 可以阻止默认的行为。
  VXETable.interceptor.add('event.clearActived', preventClosingPopUp);
  VXETable.interceptor.add('event.clearEdit', preventClosingPopUp);
  // 注册插件
  VXETable.use(VXETablePluginAntd);
  // 注册自定义组件
  registerAllComponent();
  // 执行注册方法
  app.use(VXETable, VXETableSettings);
  app.component('JVxeTable', JVxeTable);
}


/**
 * 阻止行编辑中关闭弹窗
 * @param params
 */
function preventClosingPopUp(this: any, params) {
  // 获取组件增强
  let col = params.column.params;
  let { $event } = params;
  const interceptor = getEnhanced(col.type).interceptor;
  // 执行增强
  let flag = interceptor['event.clearActived']?.call(this, ...arguments);
  if (flag === false) {
    return false;
  }

  let path = getEventPath($event);
  for (let p of path) {
    let className: any = p.className || '';
    className = typeof className === 'string' ? className : className.toString();

    /* --- 特殊处理以下组件，点击以下标签时不清空编辑状态 --- */

    // 点击的标签是JInputPop
    if (className.includes('j-input-pop')) {
      return false;
    }
    // 点击的标签是JPopup的弹出层、部门选择、用户选择
    if (className.includes('j-popup-modal') || className.includes('j-depart-select-modal') || className.includes('j-user-select-modal')) {
      return false;
    }
    // 点击的是日期选择器
    if (className.includes('j-vxe-date-picker')) {
      return false;
    }
    // 执行增强
    let flag = interceptor['event.clearActived.className']?.call(this, className, ...arguments);
    if (flag === false) {
      return false;
    }
  }
}
