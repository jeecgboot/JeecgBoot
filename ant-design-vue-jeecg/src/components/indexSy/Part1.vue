<template>
  <div class="Part1">
    <div class="head">
      <span class="left">延边自治州</span>
      <!-- <span class="right">
        <a href="javascript:;"
           @click="toMax"
           class="more">放大</a>
      </span> -->
    </div>
    <div class="content">
      <div class="top">
        <div class="left">
          <img src="../../assets/pscity.jpg"
               alt="">
        </div>
        <div class="right">
          延边朝鲜族自治州，是吉林省自治州。延边州地处中国东北吉林省东北中朝边境，整个地势西高东低，自西南、西北、东北三面向东南倾斜，地处北半球的中温带，属中温带湿润季风气候。
        </div>
      </div>
      <div class="bottom">
        <ul>
          <li>总人数：533768人</li>
          <li>电话区号：0432</li>
          <li>面积：3867.3平方米</li>
          <li>邮政区码：132300</li>
          <li>政府驻地：东宁街道</li>
          <li>下辖地区：三个街道、13个镇、1个乡</li>
        </ul>
      </div>

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
.Part1 {
  padding: 20px;
  .content {
    .top {
      display: flex;
      justify-content: space-between;
      .left {
        width: 44%;
        height: 160px;
        img{
          width: 100%;
          height: 100%;
        }
      }
      .right {
        width: 52%;
      }
    }
    .bottom {
      margin-top: 20px;
      ul {
        display: flex;
        justify-content: space-between;
        flex-wrap: wrap;
        li {
          width: 49%;
          height: 36px;
          line-height: 36px;
        }
      }
    }
  }
}
</style>

