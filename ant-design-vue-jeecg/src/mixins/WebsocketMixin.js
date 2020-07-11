import store from '@/store/'
export const WebsocketMixin = {
  mounted() {
    this.initWebSocket();
  },
  destroyed: function () {
    // 离开页面生命周期函数
    this.websocketOnclose();
  },
  methods:{
    initWebSocket: function () {
      console.log("------------WebSocket连接成功");
      // WebSocket与普通的请求所用协议有所不同，ws等同于http，wss等同于https
      var userId = store.getters.userInfo.id;
      if(!this.socketUrl.startsWith('/')){
        this.socketUrl = '/' + this.socketUrl
      }
      if(!this.socketUrl.endsWith('/')){
        this.socketUrl = this.socketUrl + '/'
      }
      var url = window._CONFIG['domianURL'].replace("https://","wss://").replace("http://","ws://") + this.socketUrl + userId;
      this.websock = new WebSocket(url);
      this.websock.onopen = this.websocketOnopen;
      this.websock.onerror = this.websocketOnerror;
      this.websock.onmessage = this.websocketOnmessage;
      this.websock.onclose = this.websocketOnclose;
    },
    websocketOnopen: function () {
      console.log("WebSocket连接成功");
    },
    websocketOnerror: function (e) {
      console.log("WebSocket连接发生错误");
      this.reconnect();
    },
    websocketOnclose: function (e) {
      this.reconnect();
    },
    websocketSend(text) {
      // 数据发送
      try {
        this.websock.send(text);
      } catch (err) {
        console.log("send failed (" + err.code + ")");
      }
    },
    reconnect() {
      var that = this;
      if(that.lockReconnect) return;
      that.lockReconnect = true;
      //没连接上会一直重连，设置延迟避免请求过多
      setTimeout(function () {
        console.info("尝试重连...");
        that.initWebSocket();
        that.lockReconnect = false;
      }, 5000);
    },
  }

}