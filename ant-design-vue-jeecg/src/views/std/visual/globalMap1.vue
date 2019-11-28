<template>

  <div id="globalMap" ref="mapChartDOM"></div>

</template>


<script>
  import * as d3 from 'd3';
  import echarts from 'echarts'
  import 'echarts/map/js/world.js'
  import 'echarts-gl'

  import scanImg from '@/assets/trade/green_bg.png'
  import baseImg from '@/assets/trade/black_bg.jpg'

  export default {
    name: "globalMap",

    data() {
      return {
        config: {
          lineWidth: 0.5, // 扫描线条宽度
          color: '#00FFC2', // 线条颜色
          levels: 1,
          intensity: 3, // 强度
          threshold: 0.01
        },
        mapChart:{},
        myChart:{},

        scanImg: scanImg,
        baseImg: baseImg,
        mapChartInstance: null, // 地图图表实例
        option: {
          backgroundColor: '#000',
          globe: {
            baseTexture: this.baseImg,
            displacementScale: 0.05,
            displacementQuality: 'high',
            shading: 'realistic',
            realisticMaterial: {
              roughness: 0.2,
              metalness: 0
            },

            postEffect: {
              enable: true,
              depthOfField: {
                // enable: true
              }
            },
            light: {
              ambient: {
                intensity: 1
              },
              main: { // 主光源
                intensity: 0,
                shadow: false
              },
              /*ambientCubemap: {
                  texture: ROOT_PATH + 'data-gl/asset/lake.hdr',
                  exposure: 1,
                  diffuseIntensity: 0.5,
                  specularIntensity: 2
              }*/
            },
            viewControl: {
              center: [0, 0, 0],
              alpha: 30,
              beta: 160,
              autoRotate: true,
              autoRotateAfterStill: 10,
              distance: 240,
              autoRotateSpeed: 4
            },
            layers: [{
              type: 'blend',
              blendTo: 'emission',
              texture: this.mapChart,
              intensity: 3
            }]
          },
          series: [{ // 点
            type: 'scatter3D',
            blendMode: 'lighter',
            coordinateSystem: 'globe',
            showEffectOn: 'render',
            zlevel: 2,
            effectType: 'ripple',
            symbolSize: 15,
            rippleEffect: {
              period: 4,
              scale: 4,
              brushType: 'fill'
            },

            hoverAnimation: true,
            itemStyle: {
              normal: {
                color: 'rgba(235, 232, 6, 0.8)'
              }
            },
            data: [
              [51.511744, 25.292405],
              [28.047305, -26.204103],
              [30.5234, 50.450099],
              [15.981919, 45.815008],
              [19.940063, 50.100353],
              [6.143158, 46.204389],
              [8.541694, 47.376888],
              [-8.629105, 41.157944],
              [-9.139337, 38.722253],
              [4.352033, 50.849644],
              [24.940524, 60.170675],
              [19.040235, 47.497913],
              [16.373819, 48.208176],
              [30.30604, 59.933177],
              [37.61501, 55.75696],
              [-79.383184, 43.653226]
            ]
          }]
        }


      }
    },

    created() {


    },
    mounted() {

      this.initMap()


    },

    methods: {

      initMap(){

        var that=this;

        const canvas = document.createElement('canvas');
        canvas.width = 4096;
        canvas.height = 2048;
        const context = canvas.getContext("2d");

        context.lineWidth = 0.5,
          context.strokeStyle =  '#00FFC2',
          context.fillStyle =  '#00FFC2',
          context.shadowColor =  '#00FFC2',

          this.image(this.scanImg).then(function (image) {
            debugger
            const m = image.height,
              n = image.width,
              values = new Array(n * m),
              contours = d3.contours().size([n, m]).smooth(true),
              projection = d3.geoIdentity().scale(canvas.width / n),
              path = d3.geoPath(projection, context);

            for (let j = 0, k = 0; j < m; ++j) {
              for (let i = 0; i < n; ++i, ++k) {
                values[k] = image.data[(k << 2)] / 255;
              }
            }
            const opt = {
              image: canvas
            }

            let results = [];

            function update(threshold, levels) {
              context.clearRect(0, 0, canvas.width, canvas.height);
              let thresholds = [];
              for (let i = 0; i < levels; i++) {
                thresholds.push((threshold + 1 / levels * i) % 1);
              }
              results = contours.thresholds(thresholds)(values);
              redraw();
            }

            function redraw() {
              results.forEach(function (d, idx) {
                context.beginPath();
                path(d);
                context.globalAlpha = 1;
                context.stroke();
                if (idx > 1 / 5 * 3) {
                  context.globalAlpha = 0.01;
                  context.fill();
                }
              });
              // opt.onupdate();
            }

            d3.timer(function (t) {
              let threshold = (t % 10000) / 10000;
              update(threshold, 1);
            });



            that.mapChart = echarts.init(document.createElement('canvas'), null, {
              width: 4096,
              height: 2048
            });


            //获取容器并对其初始化
            that.myChart = echarts.init(document.getElementById('globalMap'));

            const img = new echarts.graphic.Image({
              style: {
                image: opt.image,
                x: -1,
                y: -1,
                width: opt.image.width + 2,
                height: opt.image.height + 2
              }
            });
            // that.mapChart.getZr().add(img);
            that.myChart.setOption(that.option);

            opt.onupdate = function() {
              img.dirty();
            };


            update(0.01, 1);

          });


      },

      /*scanImageFunc(imageUrl) {
        debugger
        const image = this.image(imageUrl);
        const m = image.height,
          n = image.width,
          values = new Array(n * m),
          contours = d3.contours().size([n, m]).smooth(true),
          projection = d3.geoIdentity().scale(canvas.width / n),
          path = d3.geoPath(projection, context);

        for (let j = 0, k = 0; j < m; ++j) {
          for (let i = 0; i < n; ++i, ++k) {
            values[k] = image.data[(k << 2)] / 255;
          }
        }
        const opt = {
          image: canvas
        }

        var results = [];

        function update(threshold, levels) {
          context.clearRect(0, 0, canvas.width, canvas.height);
          let thresholds = [];
          for (let i = 0; i < levels; i++) {
            thresholds.push((threshold + 1 / levels * i) % 1);
          }
          results = contours.thresholds(thresholds)(values);
          redraw();
        }

        function redraw() {
          results.forEach(function (d, idx) {
            context.beginPath();
            path(d);
            context.globalAlpha = 1;
            context.stroke();
            if (idx > 1 / 5 * 3) {
              context.globalAlpha = 0.01;
              context.fill();
            }
          });
          opt.onupdate();
        }

        d3.timer(function (t) {
          let threshold = (t % 10000) / 10000;
          update(threshold, 1);
        });


        this.mapChart = echarts.init(document.createElement('canvas'), null, {
          width: 4096,
          height: 2048
        });
        //获取容器并对其初始化
        this.myChart = echarts.init(document.getElementById('globalMap'))

        const img = new echarts.graphic.Image({
          style: {
            image: opt.image,
            x: -1,
            y: -1,
            width: opt.image.width + 2,
            height: opt.image.height + 2
          }
        });
        this.mapChart.getZr().add(img);
        this.myChart.setOption(this.option);


        update(0.01, 1);

      },*/


      image(url) {
        return new Promise(function (resolve) {
          let image = new Image();
          image.src = url;
          image.onload = function () {
            // let canvas = this.$refs.mapChartDOM;
            let canvas = document.createElement("canvas");
            canvas.width = image.width / 8;
            canvas.height = image.height / 8;
            let context = canvas.getContext("2d");
            context.drawImage(image, 0, 0, canvas.width, canvas.height);
            resolve(context.getImageData(0, 0, canvas.width, canvas.height));
          };
        });
      },


      // 初始化中部地图
      /*initMapChart(opt) {


        this. mapChart = echarts.init(document.createElement('canvas'), null, {
          width: 4096,
          height: 2048
        });
        const img = new echarts.graphic.Image({
          style: {
            image: opt.image,
            x: -1,
            y: -1,
            width: opt.image.width + 2,
            height: opt.image.height + 2
          }
        });
        // this.mapChart.getZr().add(img);
        this.myChart.setOption(this.option);




      },*/

    },

  }


</script>

<style scoped>

</style>