// /**
//  *公共数据
//  */
// import { initGlobalState } from 'qiankun';
// import { store } from '/@/store';
// import { router } from '/@/router';
// import { getToken } from '/@/utils/auth';
// //定义传入子应用的数据
// export function getProps() {
//   return {
//     data: {
//       publicPath: '/',
//       token: getToken(),
//       store,
//       router,
//     },
//   };
// }
//
// /**
//  * 定义全局状态，并返回通信方法,在主应用使用，微应用通过 props 获取通信方法。
//  * @param state 主应用穿的公共数据
//  */
// export function initGlState(info = { userName: 'admin' }) {
//   // 初始化state
//   const actions = initGlobalState(info);
//   // 设置新的值
//   actions.setGlobalState(info);
//   // 注册 观察者 函数 - 响应 globalState 变化，在 globalState 发生改变时触发该 观察者 函数。
//   actions.onGlobalStateChange((newState, prev) => {
//     // state: 变更后的状态; prev 变更前的状态
//     console.info('newState', newState);
//     console.info('prev', prev);
//     for (const key in newState) {
//       console.info('onGlobalStateChange', key);
//     }
//   });
// }
