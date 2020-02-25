<template>
  <div class="renkou">
    <div class="head">
      <!-- <span class="left">安全知识</span> -->

      <div class="right">
        <ul class="menu">
          <li v-for="(item,index) in menuList"
              :key="index"
              :class="{on:index==isActive}"
              @click="clickMenu(index)">{{item}}</li>
        </ul>
        <!-- <span>
          <a href="http://111.26.182.9:57883/knowledge/#/law"
             target="_blank" class="more">更多</a>
        </span>-->
      </div>
    </div>
    <ul class="content">
      <li v-show="isActive == 0">
        <div id="myChart"
             :style="{width: '100%', height: '280px'}"></div>
      </li>
      <li v-show="isActive == 1">
        <div id="myChart1"
             :style="{width: '50%', height: '280px'}"></div>
        <div id="myChart4"
             :style="{width: '50%', height: '280px'}"></div>
      </li>
      <li v-show="isActive == 2">
        <div id="myChart2"
             :style="{width: '100%', height: '280px'}"></div>
      </li>
      <li v-show="isActive == 3">
        <div id="myChart3"
             :style="{width: '100%', height: '360px'}"></div>
      </li>
    </ul>
  </div>
</template>
<script>
export default {
  name: "renkou",
  data () {
    return {
      myChart: "",
      myChart1: "",
      myChart2: "",
      myChart3: "",
      myChart4: "",
      click1: false,
      click2: false,
      click3: false,
      isActive: 0,
      menuList: ["人口统计", "重点人口", "年龄结构", "特殊人群"],
      sendData: {
        pageSize: 6,
        curPage: 1
      },
      list: [],
      list1: [],
      list2: [],
      list3: [],
      list4: []
    };
  },
  mounted () {
    // this.getlawslistbypages();
    // this.getstandardspecificationlistbypages();
    // this.getaccidentcaselistbypages();
    // this.getprofessionalskilllistbypages();
    // this.getlistbypage();
    this.initChart();
  },
  methods: {
    initChart () {
      let myChart = this.$echarts.init(document.getElementById("myChart"));
      this.myChart = myChart;
      let option = {
        backgroundColor: "#fff",
        tooltip: {
          trigger: "axis",
          formatter: function (v) {
            var str = v[0].name;
            var len = v.length - 1;
            var color = [
              "#fb6d64",
              "#4ebe6b",
              "#479df6",
              "#fbc73f",
              "#7e4dc9",
              "#FA0FE4"
            ];
            v.forEach(function (val, i) {
              str +=
                "<br>" +
                '<i style="width:10px;height: 10px;display: inline-block;border-radius: 100%;overflow: hidden;background: ' +
                color[i] +
                '"></i> ' +
                val.seriesName +
                " : " +
                (val.value + "人") +
                (i === len ? "" : "\n");
            });
            return str;
          }
        },

        legend: {
          left: "venter",
          top: "top",
          itemWidth: 13,
          itemGap: 2,
          itemHeight: 13,
          data: [
            "常住人口",
            "暂住人口",
            "寄住人口",
            "未落户人口",
            "港澳台侨胞",
            "外国人"
          ]
        },
        grid: {
          left: "0%",
          right: "10%",
          bottom: "10%",
          top: "24%",
          containLabel: true
        },
        xAxis: {
          name: "月份",
          type: "category",
          boundaryGap: false,
          nameTextStyle: {
            color: "#555"
          },
          axisTick: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: "#e2e6ed"
            }
          },
          axisLabel: {
            textStyle: {
              color: "#555"
            }
          },
          data: [
            "1月",
            "2月",
            "3月",
            "4月",
            "5月",
            "6月",
            "7月",
            "8月",
            "9月",
            "10月",
            "11月",
            "12月"
          ]
        },
        color: [
          "#fb6d64",
          "#4ebe6b",
          "#479df6",
          "#fbc73f",
          "#7e4dc9",
          "#FA0FE4"
        ],
        yAxis: {
          name: "人口数量（人）",
          type: "value",
          nameTextStyle: {
            color: "#555"
          },
          axisLabel: {
            // formatter: '{value}k',
            textStyle: {
              color: "#333"
            }
          },
          axisLine: {
            lineStyle: {
              color: "#e2e6ed"
            }
          },
          axisTick: {
            show: false
          },
          // show:false,
          splitLine: {
            show: false
          }
        },
        // animationDuration:2500,
        series: [
          {
            name: "常住人口",
            type: "line",
            smooth: true,
            symbol: "pin",
            symbolSize: 10,
            data: [360, 360, 358, 358, 358, 360, 359, 359, 358, 360, 359, 359]
          },
          {
            name: "暂住人口",
            type: "line",
            smooth: true,
            symbol: "rect",
            symbolSize: 7,
            data: [40, 44, 39, 37, 38, 35, 39, 39, 44, 45, 49, 49]
          },
          {
            name: "寄住人口",
            type: "line",
            smooth: true,
            symbol: "triangle",
            symbolSize: 8,
            data: [15, 25, 28, 30, 35, 38, 35, 30, 35, 38, 35, 30]
          },
          {
            name: "未落户人口",
            type: "line",
            smooth: true,
            symbol: "diamond",
            symbolSize: 10,
            data: [58, 59, 55, 50, 51, 59, 52, 54, 51, 59, 52, 54]
          },
          {
            name: "港澳台侨胞",
            type: "line",
            smooth: true,
            symbol: "circle",
            symbolSize: 8,
            data: [12, 12, 12, 14, 18, 17, 17, 17, 18, 17, 17, 17]
          },
          {
            name: "外国人",
            type: "line",
            smooth: true,
            symbol: "circle",
            symbolSize: 8,
            data: [10, 16, 17, 16, 15, 18, 18, 17, 15, 18, 18, 17]
          }
        ]
      };
      myChart.setOption(option);
    },
    initChart1 () {
      let myChart = this.$echarts.init(document.getElementById("myChart1"));
      let myChart1 = this.$echarts.init(document.getElementById("myChart4"));
      this.myChart1 = myChart;
      this.myChart4 = myChart1;
      let option1 = {
        backgroundColor: "#fff",
        tooltip: {
          trigger: "item",
          formatter: "{b}: {c}人"
        },
        grid: {
          left: "0%",
          top: "15%",
          right: "14%",
          bottom: "10%",
          containLabel: true
        },
        legend: {
          x: "center",
          y: "top",
          itemWidth: 10,
          itemHeight: 10,
          textStyle: {
            fontSize: 12
          },
          data: ["残疾人", "失业人员", "60岁以上\n老年人", "已婚育龄妇女"]
        },
        color: ["#edcf1a", "#00cac7", "#ffc58e", "#bfa0da"],
        series: [
          {
            type: "pie",
            radius: ["14%", "38%"],
            startAngle: 45,
            minAngle: 1,
            label: {
              normal: {
                formatter: "{b} \n {c}人"
              }
            },

            data: [
              { value: 89, name: "残疾人" },
              { value: 23, name: "失业人员" },
              { value: 56, name: "60岁以上\n老年人" },
              { value: 30, name: "已婚育龄妇女" }
            ],
            itemStyle: {
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)"
              }
            }
          }
        ]
      };
      let option2 = {
        backgroundColor: "#fff",
        tooltip: {
          trigger: "item",
          formatter: "{b}: {c}户"
        },
        legend: {
          x: "center",
          y: "top",
          itemWidth: 10,
          itemHeight: 10,
          textStyle: {
            fontSize: 12
          },
          data: ["边缘户", "少数民族", "低保", "个体工商"]
        },
        color: ["#fb6d64", "#4ebe6b", "#7e4dc9", "#fbc73f"],
        series: [
          {
            type: "pie",
            radius: ["14%", "38%"],
            startAngle: 45,
            minAngle: 1,
            label: {
              normal: {
                formatter: "{b} \n {c}户"
              }
            },
            data: [
              { value: 28, name: "边缘户" },
              { value: 65, name: "少数民族" },
              { value: 56, name: "低保" },
              { value: 39, name: "个体工商" }
            ],
            itemStyle: {
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)"
              }
            }
          }
        ]
      };
      myChart.setOption(option1);
      myChart1.setOption(option2);
    },
    initChart2 () {
      let myChart = this.$echarts.init(document.getElementById("myChart2"));
      this.myChart2 = myChart;
      let option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow" // 默认为直线，可选为：'line' | 'shadow'
          },
          formatter: function (v) {
            var str = v[0].name;
            var len = v.length - 1;
            var color = "";
            v.forEach(function (val, i) {
              color = i === 0 ? "#429bf9" : "#fa6d64";
              str +=
                "<br>" +
                '<i style="width:10px;height: 10px;display: inline-block;border-radius: 100%;overflow: hidden;background: ' +
                color +
                '"></i> ' +
                val.seriesName +
                " : " +
                Math.abs(val.value) +
                (i === len ? "" : "\n");
            });
            return str;
          }
        },
        legend: {
          data: ["男", "女"]
        },
        color: ["#429bf9", "#fa6d64"],
        grid: {
          left: "0%",
          right: "4%",
          bottom: "10%",
          containLabel: true
        },
        xAxis: {
          type: "value",
          axisLabel: {
            formatter: function (v) {
              return Math.abs(v);
            }
          }
        },
        yAxis: {
          type: "category",
          axisTick: {
            show: false
          },
          data: [
            "0-20岁",
            "21-40岁",
            "41-60岁",
            "61-70岁",
            "71-90岁",
            "91-120岁"
          ]
        },
        series: [
          {
            name: "男",
            type: "bar",
            stack: "总量",
            label: {
              normal: {
                show: true,
                position: "left",
                formatter: function (v) {
                  return Math.abs(v.data);
                }
              }
            },
            data: [-183, -271, -320, -338, -290, -28]
          },
          {
            name: "女",
            type: "bar",
            stack: "总量",
            label: {
              normal: {
                show: true,
                position: "right",
                formatter: function (v) {
                  return Math.abs(v.data);
                }
              }
            },
            data: [164, 252, 287, 309, 275, 23]
          }
        ]
      };
      myChart.setOption(option);
    },
    initChart3 () {
      let myChart = this.$echarts.init(document.getElementById("myChart3"));
      this.myChart3 = myChart;
      let option = {
        color: ["#3D99FC"],
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow" // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          left: "0%",
          right: "4%",
          top: "3%",
          containLabel: true
        },
        xAxis: [
          {
            type: "category",
            data: [
              "涉恐",
              "涉稳",
              "重大刑事犯罪前科",
              "涉毒",
              "在逃",
              "肇事肇祸精神病人",
              "重点上访"
            ],
            axisTick: {
              alignWithLabel: true
            },
            axisLabel: {
              interval: 0,
              rotate: 45
            }
          }
        ],
        yAxis: [
          {
            type: "value"
          }
        ],
        series: [
          {
            name: "数量",
            type: "bar",
            barWidth: "50%",
            data: [20, 24, 200, 100, 60, 330, 220]
          }
        ]
      };
      myChart.setOption(option);
    },
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
.renkou {
  padding: 20px;
  .right {
    display: flex;
    justify-content: space-between;
    padding: 14px 0;
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
    li {
      position: relative;
      width: 100%;
      display: flex;
      justify-content: space-between;
      align-items: center;
      line-height: 48px;
      border-bottom: 1px dotted #ccc;
      padding-left: 30px;
      box-sizing: border-box;
      font-size: 15px;
      &:last-child {
        border-bottom: none;
      }
      //   &:before {
      //     content: "";
      //     position: absolute;
      //     top: 18px;
      //     left: 2px;
      //     width: 14px;
      //     height: 14px;
      //     background: url("../../assets/image/quan.png") no-repeat;
      //   }
      div:first-child {
        width: calc(100% - 200px);
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      div:last-child {
        width: 130px;
      }
    }
  }
}
</style>

