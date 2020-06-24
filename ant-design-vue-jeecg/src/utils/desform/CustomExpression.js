/*
*
* 这里填写用户自定义的表达式
* 可用在Online表单的默认值表达式中使用
* 需要外部使用的变量或方法一定要 export，否则无法识别
* 示例：
*   export const name = '张三'; // const 是常量
*   export let age = 17; // 看情况 export const 还是 let ，两者都可正常使用
*   export function content(arg) { // export 方法，可传参数，使用时要加括号，值一定要return回去，可以返回Promise
*     return 'content' + arg;
*   }
*   export const address = (arg) => content(arg) + ' | 北京市'; // export 箭头函数也可以
*
*/

/** 字段默认值官方示例：获取地址 */
export function demoFieldDefVal_getAddress(arg) {
  if (!arg) {
    arg = '朝阳区'
  }
  return `北京市 ${arg}`
}