import type { Component } from 'vue';
import { h } from 'vue';
import VXETable from 'vxe-table';
import { definedComponent, addComponent, componentMap, spanEnds, excludeKeywords } from '../componentMap';
import { JVxeRenderType, JVxeTypePrefix, JVxeTypes } from '../types/JVxeTypes';
import { getEnhanced } from './enhancedUtils';
import { isFunction } from '/@/utils/is';

/**
 * 判断某个组件是否已注册
 * @param type
 */
export function isRegistered(type: JVxeTypes | string) {
  if (excludeKeywords.includes(<JVxeTypes>type)) {
    return true;
  }
  return componentMap.has(type);
}

/**
 * 注册vxe自定义组件
 *
 * @param type
 * @param component 编辑状态显示的组件
 * @param spanComponent 非编辑状态显示的组件，可以为空
 */
export function registerComponent(type: JVxeTypes, component: Component, spanComponent?: Component) {
  addComponent(type, component, spanComponent);
  registerOneComponent(type);
}

/**
 * 异步注册vxe自定义组件
 *
 * @param type
 * @param promise
 */
export async function registerAsyncComponent(type: JVxeTypes, promise: Promise<any>) {
  const result = await promise;
  if (isFunction(result.installJVxe)) {
    result.install((component: Component, spanComponent?: Component) => {
      addComponent(type, component, spanComponent);
      registerOneComponent(type);
    });
  } else {
    addComponent(type, result.default);
    registerOneComponent(type);
  }
}

/**
 * 2024-03-08
 * liaozhiyang
 * 异步注册vxe自定义组件
 * 【QQYUN-8241】
 * @param type
 * @param promise
 */
export function registerASyncComponentReal(type: JVxeTypes, component) {
  addComponent(type, component);
  registerOneComponent(type);
}

/**
 * 安装所有vxe组件
 */
export function registerAllComponent() {
  definedComponent();
  // 遍历所有组件批量注册
  const components = [...componentMap.keys()];
  components.forEach((type) => {
    if (!type.endsWith(spanEnds)) {
      registerOneComponent(<JVxeTypes>type);
    }
  });
}

/**
 * 注册单个vxe组件
 *
 * @param type 组件 type
 */
export function registerOneComponent(type: JVxeTypes) {
  const component = componentMap.get(type);
  if (component) {
    const switches = getEnhanced(type).switches;
    if (switches.editRender && !switches.visible) {
      createEditRender(type, component);
    } else {
      createCellRender(type, component);
    }
  } else {
    throw new Error(`【registerOneComponent】"${type}"不存在于componentMap中`);
  }
}

/** 注册可编辑组件 */
function createEditRender(type: JVxeTypes, component: Component, spanComponent?: Component) {
  // 获取当前组件的增强
  const enhanced = getEnhanced(type);
  if (!spanComponent) {
    if (componentMap.has(type + spanEnds)) {
      spanComponent = componentMap.get(type + spanEnds);
    } else {
      // 默认的 span 组件为 normal
      spanComponent = componentMap.get(JVxeTypes.normal);
    }
  }
  // 添加渲染
  VXETable.renderer.add(JVxeTypePrefix + type, {
    // 可编辑模板
    renderEdit: createRender(type, component, JVxeRenderType.editer),
    // 显示模板
    renderCell: createRender(type, spanComponent, JVxeRenderType.spaner),
    // 增强注册
    ...enhanced.installOptions,
  });
}

/** 注册普通组件 */
function createCellRender(type: JVxeTypes, component: Component = <Component>componentMap.get(JVxeTypes.normal)) {
  // 获取当前组件的增强
  const enhanced = getEnhanced(type);
  VXETable.renderer.add(JVxeTypePrefix + type, {
    // 默认显示模板
    renderDefault: createRender(type, component, JVxeRenderType.default),
    // 增强注册
    ...enhanced.installOptions,
  });
}

function createRender(type, component, renderType) {
  return function (renderOptions, params) {
    return [
      h(component, {
        type: type,
        params: params,
        renderOptions: renderOptions,
        renderType: renderType,
      }),
    ];
  };
}
