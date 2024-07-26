// noinspection JSUnusedGlobalSymbols

import { unref } from 'vue';
import { useWebSocket, WebSocketResult } from '@vueuse/core';
import { getToken } from '/@/utils/auth';

let result: WebSocketResult<any>;
const listeners = new Map();

/**
 * 开启 WebSocket 链接，全局只需执行一次
 * @param url
 */
export function connectWebSocket(url: string) {
  //update-begin-author:taoyan date:2022-4-24 for: v2.4.6 的 websocket 服务端，存在性能和安全问题。 #3278
  const token = (getToken() || '') as string;
  result = useWebSocket(url, {
    // 自动重连 (遇到错误最多重复连接10次)
    autoReconnect: {
      retries: 10,
      delay: 5000,
    },
    // 心跳检测
    heartbeat: {
      message: 'ping',
      // 如果服务器压力再改回来55秒
      interval: 5000,
    },
    protocols: [token],
    // update-begin--author:liaozhiyang---date:20240726---for：[issues/6662] 演示系统socket总断，换一个写法
    onConnected: function (ws) {
      console.log('[WebSocket] 连接成功', ws);
    },
    onDisconnected: function (ws, event) {
      console.log('[WebSocket] 连接断开：', ws, event);
    },
    onError: function (ws, event) {
      console.log('[WebSocket] 连接发生错误: ', ws, event);
    },
    onMessage: function (_ws, e) {
      console.debug('[WebSocket] -----接收消息-------', e.data);
      try {
        //update-begin---author:wangshuai---date:2024-05-07---for:【issues/1161】前端websocket因心跳导致监听不起作用---
        if (e.data === 'ping') {
          return;
        }
        //update-end---author:wangshuai---date:2024-05-07---for:【issues/1161】前端websocket因心跳导致监听不起作用---
        const data = JSON.parse(e.data);
        for (const callback of listeners.keys()) {
          try {
            callback(data);
          } catch (err) {
            console.error(err);
          }
        }
      } catch (err) {
        console.error('[WebSocket] data解析失败：', err);
      }
    },
    // update-end--author:liaozhiyang---date:20240726---for：[issues/6662] 演示系统socket总断，换一个写法
  });
  // update-begin--author:liaozhiyang---date:20240726---for：[issues/6662] 演示系统socket总断，换一个写法
  //update-end-author:taoyan date:2022-4-24 for: v2.4.6 的 websocket 服务端，存在性能和安全问题。 #3278
  // if (result) {
  //   result.open = onOpen;
  //   result.close = onClose;

  //   const ws = unref(result.ws);
  //   if(ws!=null){
  //     ws.onerror = onError;
  //     ws.onmessage = onMessage;
  //     //update-begin---author:wangshuai---date:2024-04-30---for:【issues/1217】发送测试消息后，铃铛数字没有变化---
  //     ws.onopen = onOpen;
  //     ws.onclose = onClose;
  //     //update-end---author:wangshuai---date:2024-04-30---for:【issues/1217】发送测试消息后，铃铛数字没有变化---
  //   }
  // }
  // update-end--author:liaozhiyang---date:20240726---for：[issues/6662] 演示系统socket总断，换一个写法
}

function onOpen() {
  console.log('[WebSocket] 连接成功');
}

function onClose(e) {
  console.log('[WebSocket] 连接断开：', e);
}

function onError(e) {
  console.log('[WebSocket] 连接发生错误: ', e);
}

function onMessage(e) {
  console.debug('[WebSocket] -----接收消息-------', e.data);
  try {
    //update-begin---author:wangshuai---date:2024-05-07---for:【issues/1161】前端websocket因心跳导致监听不起作用---
    if(e==='ping'){
      return;
    }
    //update-end---author:wangshuai---date:2024-05-07---for:【issues/1161】前端websocket因心跳导致监听不起作用---
    const data = JSON.parse(e.data);
    for (const callback of listeners.keys()) {
      try {
        callback(data);
      } catch (err) {
        console.error(err);
      }
    }
  } catch (err) {
    console.error('[WebSocket] data解析失败：', err);
  }
}


/**
 * 添加 WebSocket 消息监听
 * @param callback
 */
export function onWebSocket(callback: (data: object) => any) {
  if (!listeners.has(callback)) {
    if (typeof callback === 'function') {
      listeners.set(callback, null);
    } else {
      console.debug('[WebSocket] 添加 WebSocket 消息监听失败：传入的参数不是一个方法');
    }
  }
}

/**
 * 解除 WebSocket 消息监听
 *
 * @param callback
 */
export function offWebSocket(callback: (data: object) => any) {
  listeners.delete(callback);
}

export function useMyWebSocket() {
  return result;
}
