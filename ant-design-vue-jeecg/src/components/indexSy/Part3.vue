<template>
  <div class="Part3">
    <div class="head">
      <!-- <span class="left">安全知识</span> -->

      <div class="right">
        <ul class="menu">
          <li v-for="(item,index) in menuList"
              :key="index"
              :class="{on:index==isActive}"
              @click="clickMenu(index)">{{item}}</li>
        </ul>
        <span>
          <a href="http://111.26.182.9:57883/knowledge/#/law"
             target="_blank"
             class="more">更多</a>
        </span>
      </div>
    </div>
    <ul class="content">
      <li v-show="isActive == 0">
        <ul>
          <li>
            <a><span class="circleTip">⊙</span>市长于技群到市政府中心、社会保障服务中心调研</a>
          </li>
          <li>
            <a><span class="circleTip">⊙</span>我市召开政法工作会议</a>
          </li>
          <li>
            <a><span class="circleTip">⊙</span>市委书记张静辉到宝山调研</a>
          </li>
          <li>
            <a><span class="circleTip">⊙</span>我市召开市委理论学习中心组集中学习（扩大）会议</a>
          </li>
          <li>
            <a><span class="circleTip">⊙</span>“中法生态示范城市”落户磐石</a>
          </li>
          <li>
            <a><span class="circleTip">⊙</span>我市对乡局级领导干部及理论骨干进行集中轮训</a>
          </li>
          <li>
            <a><span class="circleTip">⊙</span>市长于技群到市政府中心、社会保障服务中心调研</a>
          </li>
          <li>
            <a><span class="circleTip">⊙</span>我市召开政法工作会议</a>
          </li>

        </ul>
      </li>
      <li v-show="isActive == 1">

      </li>
      <li v-show="isActive == 2">

      </li>
    </ul>
  </div>
</template>
<script>

export default {
  name: "renkou",
  data () {
    return {
      isActive: 0,
      menuList: ["新闻中心", "通知公告", "政策法规"],
      list: [],
      list1: [],
      list2: [],
      list3: [],
      list4: []
    };
  },
  mounted () {
    this.initChart();
  },
  methods: {
    clickMenu (index) {
      this.isActive = index;
      let _this = this;
      if (index == 0) {
        setTimeout(() => {
          _this.myChart.resize();
        }, 300);
      }
      if (index == 1) {
        if (!this.click1) {
          this.click1 = true;
          setTimeout(() => {
            this.initChart1();
          });
        } else {
          setTimeout(() => {
            _this.myChart1.resize();
            _this.myChart4.resize();
          });
        }
      }
      if (index == 2) {
        if (!this.click2) {
          this.click2 = true;
          setTimeout(() => {
            this.initChart2();
          });
        } else {
          setTimeout(() => {
            _this.myChart2.resize();
          });
        }
      }
      if (index == 3) {
        if (!this.click3) {
          this.click3 = true;
          setTimeout(() => {
            this.initChart3();
          });
        } else {
          setTimeout(() => {
            _this.myChart3.resize();
          });
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
  width: 100%;
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
.Part3 {
  padding: 20px;
  .right {
    display: flex;
    justify-content: space-between;
    padding: 12px 0;
    .menu {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      margin-right: 40px;
      li {
        padding: 0 6px;
        border-right: 2px solid #bcbcbc;
        color: #666;
        &:hover {
          color: #297dcc;
          font-weight: 600;
        }
      }
      li:first-child {
        padding-left: 0;
      }
      li:last-child {
        border-right: none;
      }
      li.on {
        color: #297dcc;
        border-right: 2px solid #297dcc;
        font-weight: 600;
      }
      li:last-child.on {
        border-right: none;
      }
    }
  }
  .content {
    ul {
      li {
        height: 36px;
        line-height: 36px;
        border-bottom: 1px solid #eee;
        a {
          display: inline-block;
          width: 100%;
          color: #333;
          cursor: pointer;
          font-size: 14px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }
    .circleTip {
      font-size: 16px;
      display: inline-block;
      margin-right: 10px;
    }
  }
}
</style>

