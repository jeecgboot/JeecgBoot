<template>
  <div class="earthmap" id="earth">

  </div>
</template>

<script>
  import echarts from 'echarts'
  import 'echarts/map/js/world.js'
  import 'echarts-gl'


  import scanImg from '@/assets/trade/green_bg.png'
  import baseImg from '@/assets/trade/black_bg.jpg'
  import blueImg from '@/assets/trade/blue_world.jpg'
  import poinBg from '@/assets/trade/white_point_bg.jpg'

  export default {
    name: 'globalMap',
    data(){
      return{
        mapChart:{},
        airData:[],
        //立体球形纹路
        option :{
          visualMap: {
            show: false,
            min: 0,
            max: 30,
            inRange: {
              symbolSize: [1.0, 10.0]
            }
          },
          globe: {

            displacementScale: 0.04,
            baseTexture: scanImg,//贴图 球形和平面的吻合
            environment: poinBg, //背景
            heightTexture: scanImg, //地球的整个纹路
            shading: 'realistic',
            realisticMaterial: {
              roughness: 0.9
            },
            light: {
              ambient: {
                intensity: 0.4
              },
              main: {
                intensity: 0.4
              }
            },
            postEffect: {
              enable: true
            },
            viewControl: {
              projection: 'perspective',

              center: [0, -10, 0], // 视角
              targetCoord: [118.0,24.48],
              //地球是否自己转动 autoRotate为true时自己转动
              distance: 200,
              autoRotate: true,
              autoRotateDirection:'ccw',
              autoRotateAfterStill: 10,
              animationDurationUpdate: 9000,
              targetCoord: [110.46, 10.92],
            },


          },
          series: [

            {
              type: "lines3D",
              effect: { //是否显示尾迹特效，默认不显示。
                show: true,
                period: 2,
                trailWidth: 3,
                trailLength: 0.5,
                trailOpacity: 1,
                trailColor: "#ffffff"
              },
              label:{
                show:true,
                position:'left'

              },
              lineStyle: {
                width: 2,
                color: "#75acf4",
                opacity: 0.2
              },

              blendMode: "lighter",
              data: [],

            },



          ]

        },

        //平面地球 主要是设置地球的样式
        /*mapOption: {
          //backgroundColor: 'rgba(20,104,121,0.71)',//当和立体球形贴图是海洋的颜色
          visualMap: {
            show: false,
            min: 0,
            max: 100000
          },
          series: [
            {
              type: 'map',
              map: 'world',
              left: 0,
              top: 0,
              right: 0,
              bottom: 0,
              environment: 'rgba(0,0,0,0)',
              boundingCoords: [
                [-180, 90],
                [180, -90]
              ],

            }
          ]
        }*/

      }
    },
    mounted(){
      this.initMap()
    },
    methods:{
      initMap(){

        /*this.mapChart = echarts.init(document.createElement('canvas'), null, {
          width: 3086,
          height: 3048
        });*/
        //获取容器并对其初始化
        this.myChart = echarts.init(document.getElementById('earth'))

        //将平面地球和立体球形的纹路重叠
        /*this.mapChart.setOption(this.mapOption)
        this.option.globe.baseTexture = this.mapChart*/


       /* this.option.series[0].data = this.option.series[0].data.filter(function(dataItem) {
          return dataItem[2] > 0;
        }).map(function(dataItem) {
          return [dataItem[0], dataItem[1], Math.sqrt(dataItem[2])];
        });*/

        //随机划多条线
        for (let i = 0; i < 20; i++) {
          this.option.series[0].data = this.option.series[0].data.concat(this.rodamData())
          console.log(this.option.series[0].data)
          // this.airData = this.airData.concat(this.rodamData())
        }

        this.myChart.setOption(this.option);
        // this.myChart.setOption(this.mapOption);// 平面展开图
      },

      //调用划线方法
      rodamData() {
        let name = '随机点' + Math.random().toFixed(5) * 100000
        // let longitude = Math.random() * 62 + 73
        let longitude = 118.08
        let longitude2 = Math.random() * 360 - 180
        // let latitude = Math.random() * 50 + 3.52
        let latitude = 24.48
        let latitude2 = Math.random() * 180 - 90
        return {
          coords: [
            [longitude2, latitude2],
            [longitude, latitude]
          ],
          value: (Math.random() * 3000).toFixed(2),
          name: name,

        }
      },





    }



  }

</script>


<style>
  .earthmap{
    width: 100%;
    height: 100%;
    background: rgba(16, 167, 202, 0.39);
  }


</style>