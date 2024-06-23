import { watch, onUnmounted } from 'vue';
import { buildUUID } from '/@/utils/uuid';
import { useGlobSetting } from '/@/hooks/setting';
import { useUserStore } from '/@/store/modules/user';
import { JVxeDataProps, JVxeTableMethods, JVxeTableProps } from '../types';
import { isArray } from '/@/utils/is';
import { getToken } from '/@/utils/auth';

// vxe socket
const vs = {
  // 页面唯一 id，用于标识同一用户，不同页面的websocket
  pageId: buildUUID(),
  // webSocket 对象
  ws: null,
  // 一些常量
  constants: {
    // 消息类型
    TYPE: 'type',
    // 消息数据
    DATA: 'data',
    // 消息类型：心跳检测
    TYPE_HB: 'heart_beat',
    // 消息类型：更新vxe table数据
    TYPE_UVT: 'update_vxe_table',
  },
  // 心跳检测
  heartCheck: {
    // 间隔时间，间隔多久发送一次心跳消息
    interval: 10000,
    // 心跳消息超时时间，心跳消息多久没有回复后重连
    timeout: 6000,
    timeoutTimer: -1,
    clear() {
      clearTimeout(this.timeoutTimer);
      return this;
    },
    start() {
      vs.sendMessage(vs.constants.TYPE_HB, '');
      // 如果超过一定时间还没重置，说明后端主动断开了
      this.timeoutTimer = window.setTimeout(() => {
        vs.reconnect();
      }, this.timeout);
      return this;
    },
    // 心跳消息返回
    back() {
      this.clear();
      window.setTimeout(() => this.start(), this.interval);
    },
  },

  /** 初始化 WebSocket */
  initialWebSocket() {
    if (this.ws === null) {
      const userId = useUserStore().getUserInfo?.id;
      const domainURL = useGlobSetting().uploadUrl!;
      const domain = domainURL.replace('https://', 'wss://').replace('http://', 'ws://');
      const url = `${domain}/vxeSocket/${userId}/${this.pageId}`;
      //update-begin-author:taoyan date:2022-4-24 for: v2.4.6 的 websocket 服务端，存在性能和安全问题。 #3278
      let token = (getToken() || '') as string;
      this.ws = new WebSocket(url, [token]);
      //update-end-author:taoyan date:2022-4-24 for: v2.4.6 的 websocket 服务端，存在性能和安全问题。 #3278
      this.ws.onopen = this.on.open.bind(this);
      this.ws.onerror = this.on.error.bind(this);
      this.ws.onmessage = this.on.message.bind(this);
      this.ws.onclose = this.on.close.bind(this);
    }
  },

  // 发送消息
  sendMessage(type, message) {
    try {
      let ws = this.ws;
      if (ws != null && ws.readyState === ws.OPEN) {
        ws.send(
          JSON.stringify({
            type: type,
            data: message,
          })
        );
      }
    } catch (err: any) {
      console.warn('【JVxeWebSocket】发送消息失败：(' + err.code + ')');
    }
  },

  /** 绑定全局VXE表格 */
  tableMap: new Map(),
  /** 添加绑定 */
  addBind(map, key, value: VmArgs) {
    let binds = map.get(key);
    if (isArray(binds)) {
      binds.push(value);
    } else {
      map.set(key, [value]);
    }
  },
  /** 移除绑定 */
  removeBind(map, key, value: VmArgs) {
    let binds = map.get(key);
    if (isArray(binds)) {
      for (let i = 0; i < binds.length; i++) {
        let bind = binds[i];
        if (bind === value) {
          binds.splice(i, 1);
          break;
        }
      }
      if (binds.length === 0) {
        map.delete(key);
      }
    } else {
      map.delete(key);
    }
  },
  // 呼叫绑定的表单
  callBind(map, key, callback) {
    let binds = map.get(key);
    if (isArray(binds)) {
      binds.forEach(callback);
    }
  },

  lockReconnect: false,
  /** 尝试重连 */
  reconnect() {
    if (this.lockReconnect) return;
    this.lockReconnect = true;
    setTimeout(() => {
      if (this.ws && this.ws.close) {
        this.ws.close();
      }
      this.ws = null;
      console.info('【JVxeWebSocket】尝试重连...');
      this.initialWebSocket();
      this.lockReconnect = false;
    }, 5000);
  },

  on: {
    open() {
      console.info('【JVxeWebSocket】连接成功');
      this.heartCheck.start();
    },
    error(e) {
      console.warn('【JVxeWebSocket】连接发生错误:', e);
      this.reconnect();
    },
    message(e) {
      // 解析消息
      let json;
      try {
        json = JSON.parse(e.data);
      } catch (e: any) {
        console.warn('【JVxeWebSocket】收到无法解析的消息:', e.data);
        return;
      }
      let type = json[this.constants.TYPE];
      let data = json[this.constants.DATA];
      switch (type) {
        // 心跳检测
        case this.constants.TYPE_HB:
          this.heartCheck.back();
          break;
        // 更新form数据
        case this.constants.TYPE_UVT:
          this.callBind(this.tableMap, data.socketKey, (args) => this.onVM.onUpdateTable(args, ...data.args));
          break;
        default:
          console.warn('【JVxeWebSocket】收到不识别的消息类型:' + type);
          break;
      }
    },
    close(e) {
      console.info('【JVxeWebSocket】连接被关闭:', e);
      this.reconnect();
    },
  },

  onVM: {
    /** 收到更新表格的消息 */
    onUpdateTable({ props, data, methods }: VmArgs, row, caseId) {
      if (data.caseId !== caseId) {
        const tableRow = methods.getIfRowById(row.id).row;
        // 局部保更新数据
        if (tableRow) {
          if (props.reloadEffect) {
            data.reloadEffectRowKeysMap[row.id] = true;
          }
          Object.assign(tableRow, row, { id: tableRow.id });
          methods.getXTable().reloadRow(tableRow);
        }
      }
    },
  },
} as {
  ws: Nullable<WebSocket>;
} & Recordable;

type VmArgs = {
  props: JVxeTableProps;
  data: JVxeDataProps;
  methods: JVxeTableMethods;
};

export function useWebSocket(props: JVxeTableProps, data: JVxeDataProps, methods) {
  const args: VmArgs = { props, data, methods };
  watch(
    () => props.socketReload,
    (socketReload: boolean) => {
      if (socketReload) {
        vs.initialWebSocket();
        vs.addBind(vs.tableMap, props.socketKey, args);
      } else {
        vs.removeBind(vs.tableMap, props.socketKey, args);
      }
    },
    { immediate: true }
  );

  /** 发送socket消息更新行 */
  function socketSendUpdateRow(row) {
    vs.sendMessage(vs.constants.TYPE_UVT, {
      socketKey: props.socketKey,
      args: [row, data.caseId],
    });
  }

  onUnmounted(() => {
    vs.removeBind(vs.tableMap, props.socketKey, args);
  });

  return {
    socketSendUpdateRow,
  };
}
