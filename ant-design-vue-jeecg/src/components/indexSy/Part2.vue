<template>
  <div class="Part2">
    <div class="head">
      <span class="left">全景图</span>
      <span class="right">
        <img src="../../assets/max.png"
             @click="toMax"
             alt="">
      </span>
    </div>
    <div class="info ">
      <iframe class="quanjing720yun"
              src="http://720yun.com/t/617jzztntf5?pano_id=6337674"
              frameborder="0"
              ref="qujingtu"
              style="width: 100%;height: 280px;"></iframe>
    </div>
  </div>
</template>
<script>
export default {
  name: "zhongdian",
  data () {
    return {};
  },
  methods: {
    toMax () {
      let tar = this.$refs.qujingtu;
      let requestMethod = tar.requestFullScreen || // W3C
        tar.webkitRequestFullScreen || // Chrome等
        tar.mozRequestFullScreen || // FireFox
        tar.msRequestFullScreen; // IE11
      if (requestMethod) {
        requestMethod.call(tar);
      } else if (typeof window.ActiveXObject !== "undefined") { // for Internet
        // Explorer
        let wscript = new ActiveXObject("WScript.Shell");
        if (wscript !== null) {
          wscript.SendKeys("{F11}");
        }
      }
    }
  }

};
</script>
<style lang="less" scoped>
ul,
li {
  list-style: none;
  margin: 0;
  padding: 0;
}
.head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #297dcc;
}

.head .left {
  font-size: 18px;
  /* border-bottom: 3px solid #297dcc; */
  padding: 10px 0;
  font-weight: 600;
}
.head-small .left {
  font-size: 16px !important;
}

.head .right {
  font-size: 16px;
  cursor: pointer;
}

a {
  text-decoration: none;
  color: #999;
  font-size: 14px;
}

.more:hover {
  color: #297dcc;
}
.contentP {
  line-height: 1.4;
}
.Part2 {
  padding: 20px;
  .right {
    img {
      width: 20px;
      height: 20px;

    }
  }
}
</style>

