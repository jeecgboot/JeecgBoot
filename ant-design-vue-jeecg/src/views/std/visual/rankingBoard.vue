<template>
  <div id="ranking-board">
    <div class="ranking-board-title">进口国家采购量排名指数</div>
    <div class="dv-scroll-ranking-board-text">Import Purchase Volume Ranking Index</div>
    <dv-scroll-ranking-board :config="config" style="width:22em;height: 50em;font-size: 16px;"/>
  </div>
</template>

<script>
  export default {
    name: 'RankingBoard',
    data () {
      return {
        config: {
          data: [
            {
              name: '土耳其',
              value: 155
            },
            {
              name: '意大利',
              value: 220
            },
            {
              name: '埃及',
              value: 178
            },
            {
              name: '埃及',
              value: 166
            },
            {
              name: '埃及',
              value: 180
            },
            {
              name: '埃及',
              value: 45
            },
            {
              name: '埃及',
              value: 129
            },
            {
              name: '埃及',
              value: 129
            },
            {
              name: '埃及',
              value: 219
            },
            {
              name: '埃及',
              value: 129
            },
            {
              name: '埃及',
              value: 129
            },
            {
              name: '埃及',
              value: 29
            },
            {
              name: '埃及',
              value: 129
            },
            {
              name: '西班牙',
              value: 129
            },
            {
              name: '埃及',
              value: 29
            },
            {
              name: '埃及',
              value: 29
            }
          ],
          rowNum: 7,
          carousel:'single',
          unit:'  TEU',
          // waitTime:500000

        }
      }
    }
  }
</script>

<style lang="less">
  #ranking-board {
    top:15%;
    position: absolute;
    width: 23%;
    left: 1%;
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
      font-size: 25px;
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
    .dv-scroll-ranking-board .ranking-info {
      display: -ms-flexbox;
      display: flex;
      width: 100%;
      font-size: 20px;
      font-style: oblique;
    }
    .dv-scroll-ranking-board .ranking-column .inside-column {
      position: relative;
      height: 26px;
      background-color: #1370fb;
      margin-bottom: 2px;
      border-radius: 5px;
      overflow: hidden;
    }
    .dv-scroll-ranking-board .ranking-info .info-name {
      -ms-flex: 1;
      flex: 1;
      margin-left: 20px;
    }

  }
</style>