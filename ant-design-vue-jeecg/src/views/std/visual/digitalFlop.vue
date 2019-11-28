<template>
  <div id="digital-flop">
    <div
      class="digital-flop-item"
      v-for="item in digitalFlopData"
      :key="item.title"
    >
      <div class="digital-flop-title">{{ item.title }}</div>
      <div class="digital-flop">
        <dv-digital-flop
          :config="item.number"
          style="width:100px;height:50px;"
        />
        <div class="unit">{{ item.unit }}</div>
      </div>
    </div>

    <dv-decoration-10 />
  </div>
</template>

<script>
  export default {
    name: 'DigitalFlop',
    data () {
      return {
        digitalFlopData: []
      }
    },
    methods: {
      createData () {
        const { randomExtend } = this

        this.digitalFlopData = [
          {
            title: '管养里程',
            number: {
              number: [randomExtend(20000, 30000)],
              content: '{nt}',
              textAlign: 'right',
              style: {
                fill: '#4d99fc',
                fontWeight: 'bold'
              }
            },
            unit: '公里'
          },
          {
            title: '桥梁',
            number: {
              number: [randomExtend(20, 30)],
              content: '{nt}',
              textAlign: 'right',
              style: {
                fill: '#f46827',
                fontWeight: 'bold'
              }
            },
            unit: '座'
          },
          {
            title: '涵洞隧道',
            number: {
              number: [randomExtend(20, 30)],
              content: '{nt}',
              textAlign: 'right',
              style: {
                fill: '#40faee',
                fontWeight: 'bold'
              }
            },
            unit: '个'
          },
          {
            title: '匝道',
            number: {
              number: [randomExtend(10, 20)],
              content: '{nt}',
              textAlign: 'right',
              style: {
                fill: '#4d99fc',
                fontWeight: 'bold'
              }
            },
            unit: '个'
          },
          {
            title: '隧道',
            number: {
              number: [randomExtend(5, 10)],
              content: '{nt}',
              textAlign: 'right',
              style: {
                fill: '#f46827',
                fontWeight: 'bold'
              }
            },
            unit: '个'
          },
          {
            title: '服务区',
            number: {
              number: [randomExtend(5, 10)],
              content: '{nt}',
              textAlign: 'right',
              style: {
                fill: '#40faee',
                fontWeight: 'bold'
              }
            },
            unit: '个'
          },
          {
            title: '收费站',
            number: {
              number: [randomExtend(5, 10)],
              content: '{nt}',
              textAlign: 'right',
              style: {
                fill: '#4d99fc',
                fontWeight: 'bold'
              }
            },
            unit: '个'
          },
          {
            title: '超限站',
            number: {
              number: [randomExtend(5, 10)],
              content: '{nt}',
              textAlign: 'right',
              style: {
                fill: '#f46827',
                fontWeight: 'bold'
              }
            },
            unit: '个'
          },
          {
            title: '停车区',
            number: {
              number: [randomExtend(5, 10)],
              content: '{nt}',
              textAlign: 'right',
              style: {
                fill: '#40faee',
                fontWeight: 'bold'
              }
            },
            unit: '个'
          }
        ]
      },
      randomExtend (minNum, maxNum) {
        if (arguments.length === 1) {
          return parseInt(Math.random() * minNum + 1, 10)
        } else {
          return parseInt(Math.random() * (maxNum - minNum + 1) + minNum, 10)
        }
      }
    },
    mounted () {
      const { createData } = this

      createData()

      setInterval(createData, 30000)
    }
  }
</script>

<style lang="less">
  #digital-flop {
    position: relative;
    height: 15%;
    flex-shrink: 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: rgba(6, 30, 93, 0.5);

    .dv-decoration-10 {
      position: absolute;
      width: 95%;
      left: 2.5%;
      height: 5px;
      bottom: 0px;
    }

    .digital-flop-item {
      width: 11%;
      height: 80%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      border-left: 3px solid rgb(6, 30, 93);
      border-right: 3px solid rgb(6, 30, 93);
    }

    .digital-flop-title {
      font-size: 20px;
      margin-bottom: 20px;
    }

    .digital-flop {
      display: flex;
    }

    .unit {
      margin-left: 10px;
      display: flex;
      align-items: flex-end;
      box-sizing: border-box;
      padding-bottom: 13px;
    }
  }
</style>