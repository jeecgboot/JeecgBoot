import {qiankunWindow} from 'vite-plugin-qiankun/dist/helper'

/**
 * 【JEECG作为乾坤子应用】【判断当前是否是以乾坤子应用的模式运行】
 */
export function checkIsQiankunMicro(): boolean {
  return !!qiankunWindow.__POWERED_BY_QIANKUN__;
}

export function getGlobal() {
  return (checkIsQiankunMicro() ? qiankunWindow : window) as Window
}
