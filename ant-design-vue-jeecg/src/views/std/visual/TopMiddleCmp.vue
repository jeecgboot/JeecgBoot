<template>
  <div class="top-middle-cmp">
    <div class="ranking-board-title">全年采购额趋势图</div>
    <div class="dv-scroll-ranking-board-text">Annual Purchase Trend Chart</div>
    <dv-charts :option="option" style="width:45em;height: 35em;margin-left: 15%;margin-top: -20%;"/>
  </div>
</template>

<script>
  import echarts from 'echarts'

export default {
  name: 'TopMiddleCmp',
  data () {
    return {
      option: {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            lineStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0,
                  color: 'rgba(255,255,255,0)' // 0% 处的颜色
                }, {
                  offset: 0.5,
                  color: 'rgba(255,255,255,1)' // 100% 处的颜色
                }, {
                  offset: 1,
                  color: 'rgba(255,255,255,0)' // 100% 处的颜色
                }],
                global: false // 缺省为 false
              }
            },
          },

        },
        // grid: {
        //   top: '18%',
        //   left: '0.01%',
        //   right: '10%',
        //   bottom: '35%',
        //   containLabel: true
        // },
        xAxis: {
          data: [
            '10/01', '10/02', '10/03', '10/04', '10/05', '10/06',
            '10/07', '10/07', '10/08', '10/09', '10/10', '10/11',
            '10/12'
          ],
          type: 'category',
          boundaryGap: false,
          axisLine: { //坐标轴轴线相关设置。数学上的x轴
            show: true,
            lineStyle: {
              color: 'rgba(255,255,255,0.4)'
            },
          },
          axisLabel: {
            interval: 0,
            rotate: 45,
            color: '#7cb4d8',
            margin: 0,
            textStyle: {
              fontSize: 14
            }
          },
          axisTick: {
            show: false,
          },
        },
        yAxis: {
          data: 'value',
          // min: 0,
          // max: 140,
          splitNumber: 4,
          splitLine: {
            show: false,
            lineStyle: {
              color: 'rgba(255,255,255,0.1)'
            }
          },
          axisLine: {
            show: false,
          },
          axisLabel: {
            show: false,
            margin: 20,
            textStyle: {
              color: '#d1e6eb',

            },
            style: {
              fill: '#333',
              fontSize: 20,
              rotate: 0
            }
          },
          axisTick: {
            show: false,
          },
        },
        series: [
          {
            name: '移动',
            type: 'line',
            rLevel:10,
            animationCurve : 'easeOutCubic',
            animationFrame : 50,
            smooth: true,
            symbol: 'circle',
            symbolSize: 5,
            showSymbol: true,
            lineArea: {
              show: true,
              gradient: ['rgba(55, 162, 218, 0.6)', 'rgba(55, 162, 218, 0)'],
              style: {
                opacity: 1
              }
            },
            label:{
              show:true,
              fontSize: 25,
            },
            lineStyle: {
              lineWidth: 6,
            },
            linePoint: {
              radius: 5,
              style: {
                fill: '#00db95'
              }
            },
            data: this.createRandomData3(12, 200, 400)
          }
        ]
      }
    }
  },
  methods:{

    createRandomData3(l, m, n) {
      let data = [];
      for (let i = 0; i < l; i++) {
        data[i] = Math.floor(Math.random() * (m - n) + n);
      }
      return data;
    }
  }
}
</script>

<style lang="less">
.top-middle-cmp {
  position: absolute;
  padding: 0 50px;
  box-sizing: border-box;
  top: 52%;
  right: 1px;

  .chart-name {
    position: absolute;
    right: 70px;
    text-align: right;
    font-size: 20px;
    top: 10%;
  }
  .dv-scroll-ranking-board-text {
    flex: 1;
    /*height: 50px;*/
    display: flex;
    /*align-items: center;*/
    font-size: 16px;
    font-family: STXihei, "Microsoft YaHei";
    margin-left: 40%;
  }
  .ranking-board-title {
    height: 35px;
    display: flex;
    /*align-items: center;*/
    font-size: 20px;
    margin-left: 40%;
  }
}
</style>
