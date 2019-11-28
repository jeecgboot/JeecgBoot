<template>
  <div id="activeRingChart">
    <div class="ranking-board-title">经营商品采购金额占比图</div>
    <div class="dv-scroll-ranking-board-text">Commodity Management Proportion</div>
    <dv-active-ring-chart :config="config" style="width:300px;height:300px" />
  </div>
</template>

<script>
  export default {
    name: 'activeRingChart',
    data () {
      return {
        config: {
          radius: '70%',
          activeRadius: '85%',
          lineWidth: 40,
          color:['rgba(21,18,111,1)','rgba(230,0,80,.90)','rgba(238,240,250,.80)'],
          data: [
            {
              name: '石材',
              value: 55
            },
            {
              name: '木材',
              value: 120
            },
            {
              name: '啤酒',
              value: 78
            }
          ],
          digitalFlopStyle: {
            fontSize: 25,
            fill: '#fff'
          }

        }
      }
    }
  }
</script>

<style lang="less">
  #activeRingChart {
    top:15%;
    position: absolute;
    width: 23%;
    right: 1%;
    /*box-shadow: 0 0 3px blue;*/
    display: flex;
    flex-direction: column;
    /*background-color: rgba(6, 30, 93, 0.5);*/
    /*border-top: 2px solid rgba(1, 153, 209, .5);*/
    box-sizing: border-box;
    padding: 0px 30px;

    .ranking-board-title {
      /*font-weight: bold;*/
      height: 50px;
      display: flex;
      align-items: center;
      font-size: 20px;
    }

    .dv-scroll-ranking-board {
      flex: 1;
    }
    .dv-scroll-ranking-board-text {
      flex: 1;
      /*height: 50px;*/
      display: flex;
      align-items: center;
      font-size: 16px;
      font-family: STXihei, "Microsoft YaHei";
    }
  }
</style>