<template>
  <div :style="{ padding: '0 0 32px 32px' }">
    <v-chart :forceFit="true" :height="height" :data="data" :scale="scale">
      <v-coord type="polar" :startAngle="-202.5" :endAngle="22.5" :radius="0.75"></v-coord>
      <v-axis
        dataKey="value"
        :zIndex="2"
        :line="null"
        :label="axisLabel"
        :subTickCount="4"
        :subTickLine="axisSubTickLine"
        :tickLine="axisTickLine"
        :grid="null"
      ></v-axis>
      <v-axis dataKey="1" :show="false"></v-axis>
      <v-series
        gemo="point"
        position="value*1"
        shape="pointer"
        color="#1890FF"
        :active="false"
      ></v-series>
      <v-guide
        type="arc"
        :zIndex="0"
        :top="false"
        :start="arcGuide1Start"
        :end="arcGuide1End"
        :vStyle="arcGuide1Style"
      ></v-guide>
      <v-guide
        type="arc"
        :zIndex="1"
        :start="arcGuide2Start"
        :end="getArcGuide2End"
        :vStyle="arcGuide2Style"
      ></v-guide>
      <v-guide
        type="html"
        :position="htmlGuidePosition"
        :html="getHtmlGuideHtml()"
      ></v-guide>
    </v-chart>
  </div>
</template>

<script>
  import {registerShape} from 'viser-vue';

  registerShape('point', 'pointer', {
    draw(cfg, container) {
      let point = cfg.points[0];
      point = this.parsePoint(point);
      const center = this.parsePoint({
        x: 0,
        y: 0,
      });
      container.addShape('line', {
        attrs: {
          x1: center.x,
          y1: center.y,
          x2: point.x,
          y2: point.y + 15,
          stroke: cfg.color,
          lineWidth: 5,
          lineCap: 'round',
        }
      });
      return container.addShape('circle', {
        attrs: {
          x: center.x,
          y: center.y,
          r: 9.75,
          stroke: cfg.color,
          lineWidth: 4.5,
          fill: '#fff',
        }
      });
    }
  });

  const scale = [{
    dataKey: 'value',
    min: 0,
    max: 9,
    tickInterval: 1,
    nice: false,
  }];

  const sourceData = [
    {value: 6.7},
  ];

  export default {
    name: "DashChartDemo",
    props: {
      value: {
        type: Number,
        default: 6.7
      },
      title: {
        type: String,
        default: ''
      },
      height: {
        type: Number,
        default: 254
      }
    },
    created() {
      if (!this.value) {
        this.data = sourceData;
      } else {
        this.data = [
          {value: this.value},
        ];
      }
      this.getData()
    },
    watch: {
      'value': function (val) {
        this.data = [
          {value: val},
        ];
        this.getData();
      }
    },
    methods: {
      getData() {
        if (this.data && this.data.length > 0) {
          this.abcd = this.data[0].value * 10
        } else {
          this.abcd = 70
        }
      },
      getHtmlGuideHtml() {
        return '<div style="width: 300px;text-align: center;">\n' +
          '<p style="font-size: 14px;color: #545454;margin: 0;">' + this.title + '</p>\n' +
          '<p style="font-size: 36px;color: #545454;margin: 0;">' + this.abcd + '%</p>\n' +
          '</div>'
      },
      getArcGuide2End() {
        return [this.data[0].value, 0.945]
      }
    },
    data() {
      return {
        data: [],
        scale: scale,
        abcd: 70,
        axisLabel: {
          offset: -16,
          textStyle: {
            fontSize: 18,
            textAlign: 'center',
            textBaseline: 'middle'
          }
        },
        axisSubTickLine: {
          length: -8,
          stroke: '#fff',
          strokeOpacity: 1,
        },
        axisTickLine: {
          length: -17,
          stroke: '#fff',
          strokeOpacity: 1,
        },
        arcGuide1Start: [0, 0.945],
        arcGuide1End: [9, 0.945],
        arcGuide1Style: {
          stroke: '#CBCBCB',
          lineWidth: 18,
        },
        arcGuide2Start: [0, 0.945],
        arcGuide2Style: {
          stroke: '#1890FF',
          lineWidth: 18,
        },
        htmlGuidePosition: ['50%', '100%'],
        htmlGuideHtml: `
        <div style="width: 300px;text-align: center;">
          <p style="font-size: 14px;color: #545454;margin: 0;">${this.title}</p>
          <p style="font-size: 36px;color: #545454;margin: 0;">${this.abcd}%</p>
        </div>
      `,
      };
    },
  };
</script>
