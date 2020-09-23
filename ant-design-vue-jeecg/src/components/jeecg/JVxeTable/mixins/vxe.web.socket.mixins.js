import store from '@/store/'
import { randomUUID } from '@/utils/util'
// vxe socket
const vs = {
  // 页面唯一 id，用于标识同一用户，不同页面的websocket
  pageId: randomUUID(),
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
    // 消息类型：通用数据传递
    TYPE_CSD: 'common_send_date',
    // 消息类型：更新vxe table数据
    TYPE_UVT: 'update_vxe_table',
  },
  // 心跳检测
  heartCheck: {
    // 间隔时间，间隔多久发送一次心跳消息
    interval: 10000,
    // 心跳消息超时时间，心跳消息多久没有回复后重连
    timeout: 6000,
    timeoutTimer: null,
    clear() {
      clearTimeout(this.timeoutTimer)
      return this
    },
    start() {
      vs.sendMessage(vs.constants.TYPE_HB, '')
      // 如果超过一定时间还没重置，说明后端主动断开了
      this.timeoutTimer = window.setTimeout(() => {
        vs.reconnect()
      }, this.timeout)
      return this
    },
    // 心跳消息返回
    back() {
      this.clear()
      window.setTimeout(() => this.start(), this.interval)
    },
  },

  /** 初始化 WebSocket */
  initialWebSocket() {
    if (this.ws === null) {
      const userId = store.getters.userInfo.id
      const domain = window._CONFIG['domianURL'].replace('https://', 'wss://').replace('http://', 'ws://')
      const url = `${domain}/vxeSocket/${userId}/${this.pageId}`

      this.ws = new WebSocket(url)
      this.ws.onopen = this.on.open.bind(this)
      this.ws.onerror = this.on.error.bind(this)
      this.ws.onmessage = this.on.message.bind(this)
      this.ws.onclose = this.on.close.bind(this)

      console.log('this.ws: ', this.ws)
    }
  },

  // 发送消息
  sendMessage(type, message) {
    try {
      let ws = this.ws
      if (ws != null && ws.readyState === ws.OPEN) {
        ws.send(JSON.stringify({
          type: type,
          data: message
        }))
      }
    } catch (err) {
      console.warn('【VXEWebSocket】发送消息失败：(' + err.code + ')')
    }
  },

  /** 绑定全局VXE表格 */
  tableMap: new Map(),
  CSDMap: new Map(),
  /** 添加绑定 */
  addBind(map, key, value) {
    let binds = map.get(key)
    if (Array.isArray(binds)) {
      binds.push(value)
    } else {
      map.set(key, [value])
    }
  },
  /** 移除绑定 */
  removeBind(map, key, value) {
    let binds = map.get(key)
    if (Array.isArray(binds)) {
      for (let i = 0; i < binds.length; i++) {
        let bind = binds[i]
        if (bind === value) {
          binds.splice(i, 1)
          break
        }
      }
      if (binds.length === 0) {
        map.delete(key)
      }
    } else {
      map.delete(key)
    }
  },
  // 呼叫绑定的表单
  callBind(map, key, callback) {
    let binds = map.get(key)
    if (Array.isArray(binds)) {
      binds.forEach(callback)
    }
  },

  lockReconnect: false,
  /** 尝试重连 */
  reconnect() {
    if (this.lockReconnect) return
    this.lockReconnect = true
    setTimeout(() => {
      if (this.ws && this.ws.close) {
        this.ws.close()
      }
      this.ws = null
      console.info('【VXEWebSocket】尝试重连...')
      this.initialWebSocket()
      this.lockReconnect = false
    }, 5000)
  },

  on: {
    open() {
      console.log('【VXEWebSocket】连接成功')
      this.heartCheck.start()
    },
    error(e) {
      console.warn('【VXEWebSocket】连接发生错误:', e)
      this.reconnect()
    },
    message(e) {
      // 解析消息
      let json
      try {
        json = JSON.parse(e.data)
      } catch (e) {
        console.warn('【VXEWebSocket】收到无法解析的消息:', e.data)
        return
      }
      let type = json[this.constants.TYPE]
      let data = json[this.constants.DATA]
      switch (type) {
        // 心跳检测
        case this.constants.TYPE_HB:
          this.heartCheck.back()
          break
        // 通用数据传递
        case this.constants.TYPE_CSD:
          this.callBind(this.CSDMap, data.key, (fn) => fn.apply(this, data.args))
          break
        // 更新form数据
        case this.constants.TYPE_UVT:
          this.callBind(this.tableMap, data.socketKey, (vm) => this.onVM['onUpdateTable'].apply(vm, data.args))
          break
        default:
          console.warn('【VXEWebSocket】收到不识别的消息类型:' + type)
          break
      }
    },
    close(e) {
      console.log('【VXEWebSocket】连接被关闭:', e)
      this.reconnect()
    },
  },

  onVM: {
    /** 收到更新表格的消息 */
    onUpdateTable(row, caseId) {
      // 判断是不是自己发的消息
      if (this.caseId !== caseId) {
        const tableRow = this.getIfRowById(row.id).row
        // 局部保更新数据
        if (tableRow) {
          // 特殊处理拖轮状态
          if (row['tug_status'] && tableRow['tug_status']) {
            row['tug_status'] = Object.assign({}, tableRow['tug_status'], row['tug_status'])
          }
          // 判断是否启用重载特效
          if (this.reloadEffect) {
            this.$set(this.reloadEffectRowKeysMap, row.id, true)
          }
          Object.keys(row).forEach(key => {
            if (key !== 'id') {
              this.$set(tableRow, key, row[key])
            }
          })
          this.$refs.vxe.reloadRow(tableRow)
        }
      }
    },
  },

}

export default {
  props: {
    // 是否开启使用 webSocket 无痕刷新
    socketReload: {
      type: Boolean,
      default: false
    },
    socketKey: {
      type: String,
      default: 'vxe-default'
    },
  },
  data() {
    return {}
  },
  mounted() {
    if (this.socketReload) {
      vs.initialWebSocket()
      vs.addBind(vs.tableMap, this.socketKey, this)
    }
  },
  methods: {

    /** 发送socket消息更新行 */
    socketSendUpdateRow(row) {
      vs.sendMessage(vs.constants.TYPE_UVT, {
        socketKey: this.socketKey,
        args: [row, this.caseId],
      })
    },

  },
  beforeDestroy() {
    vs.removeBind(vs.tableMap, this.socketKey, this)
  },
}

/**
 * 添加WebSocket通用数据传递绑定，相同的key可以添加多个方法绑定
 * @param key 唯一key
 * @param fn 当消息来的时候触发的回调方法
 */
export function addBindSocketCSD(key, fn) {
  if (typeof fn === 'function') {
    vs.addBind(vs.CSDMap, key, fn)
  }
}

/**
 * 移除WebSocket通用数据传递绑定
 * @param key 唯一key
 * @param fn 要移除的方法，必须和添加时的方法内存层面上保持一致才可以正确移除
 */
export function removeBindSocketCSD(key, fn) {
  if (typeof fn === 'function') {
    vs.removeBind(vs.CSDMap, key, fn)
  }
}
