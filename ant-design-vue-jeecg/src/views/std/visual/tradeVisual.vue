<template>
  <div id="data-view">
    <dv-full-screen-container>

      <top-header/>



      <div class="cc-details">
        <div class="card">2</div>
        <div class="card">1</div>
        <div class="card">3</div>
        <div class="card">7</div>
        <div class="card">7</div>
        <div class="card">7</div>
        <div class="card">7</div>
        <div class="card">7</div>
        <div class="card">7</div>
        <div class="card">7</div>
        <div>万元</div>
      </div>


      <globalMap/>

      <!--<scroll-board/>-->

      <ranking-board />
      <activeRingChart />

      <Top-Middle-Cmp />


      <!--<div class="main-content">
        <digital-flop />

        <div class="block-left-right-content">
          <ranking-board />

          <div class="block-top-bottom-content">
            <div class="block-top-content">
              <rose-chart />

              <water-level-chart />

              <scroll-board />
            </div>

            <cards />
          </div>
        </div>
      </div>-->


    </dv-full-screen-container>
  </div>
</template>

<script>
  import topHeader from './topHeader'
  import digitalFlop from './digitalFlop'
  import rankingBoard from './rankingBoard'
  import roseChart from './roseChart'
  import waterLevelChart from './waterLevelChart'
  import scrollBoard from './scrollBoard'
  import cards from './cards'
  import globalMap from './globalMap'
  import activeRingChart from './activeRingChart'
  import TopMiddleCmp from './TopMiddleCmp'

  export default {
    name: 'DataView',
    components: {
      topHeader,
      digitalFlop,
      rankingBoard,
      roseChart,
      waterLevelChart,
      scrollBoard,
      globalMap,
      activeRingChart,
      TopMiddleCmp,
      cards
    },
    data() {
      return {}
    },
    methods: {}
  }
</script>

<style lang="less">
  #data-view {
    width: 100%;
    height: 100%;
    background-color: #030409;
    color: #fff;

    #dv-full-screen-container {
      /*background-image: url('~@/assets/trade/bigScreenBg.png');*/
      background-size: 100% 100%;
      background-color: #000c17;
      box-shadow: 0 0 3px blue;
      display: flex;
      flex-direction: column;
    }

    .main-content {
      flex: 1;
      display: flex;
      flex-direction: column;
    }

    .block-left-right-content {
      flex: 1;
      display: flex;
      margin-top: 20px;
    }

    .block-top-bottom-content {
      flex: 1;
      display: flex;
      flex-direction: column;
      box-sizing: border-box;
      padding-left: 20px;
    }
    .cc-details {
      /*position: absolute;*/
      /*height: 120px;*/
      display: flex;
      justify-content: center;
      font-size: 32px;
      align-items: center;
      /*left: 40%;*/
      /*top: 12%;*/
      z-index: 9999;

      .card {
        background-color: rgba(4,49,128,.8);
        /*color: #08e5ff;*/
        height: 70px;
        width: 70px;
        font-size: 45px;
        font-weight: bold;
        line-height: 70px;
        text-align: center;
        margin: 5px;
      }
    }
    .ranking-board-title-tt {
      height: 35px;
      display: flex;
      /*align-items: center;*/
      font-size: 20px;
      margin-left: 40%;
    }
    .subTitle{position: absolute; font-size: .18rem; color: rgba(255,255,255,0.6);  padding: .09rem 0 .18rem .5rem; top: 6em;}
    .subTitle em{ font-size: .24rem; color: #0dedef; padding: 0 .07rem; font-family: 'myDigifaw'; font-style: normal;  }


    p {
      font-family: 'Audiowide';
      text-align: center;
      color: #00a67c;
      font-size: 7em;
      -webkit-transition: all 1.5s ease;
      transition: all 1.5s ease;
    }
    p:hover {
      color: #fff;
      -webkit-animation: Glow 1.5s ease infinite alternate;
      animation: Glow 1.5s ease infinite alternate;

    }
    @-webkit-keyframes Glow {
      from {
        text-shadow: 0 0 10px #fff,
        0 0 20px #fff,
        0 0 30px #fff,
        0 0 40px #00a67c,
        0 0 70px #00a67c,
        0 0 80px #00a67c,
        0 0 100px #00a67c,
        0 0 150px #00a67c;
      }
      to {
        text-shadow: 0 0 5px #fff,
        0 0 10px #fff,
        0 0 15px #fff,
        0 0 20px #00a67c,
        0 0 35px #00a67c,
        0 0 40px #00a67c,
        0 0 50px #00a67c,
        0 0 75px #00a67c;
      }
    }
    @keyframes Glow {
      from {
        text-shadow: 0 0 10px #fff,
        0 0 20px #fff,
        0 0 30px #fff,
        0 0 40px #00a67c,
        0 0 70px #00a67c,
        0 0 80px #00a67c,
        0 0 100px #00a67c,
        0 0 150px #00a67c;
      }
      to {
        text-shadow: 0 0 5px #fff,
        0 0 10px #fff,
        0 0 15px #fff,
        0 0 20px #00a67c,
        0 0 35px #00a67c,
        0 0 40px #00a67c,
        0 0 50px #00a67c,
        0 0 75px #00a67c;
      }
    }


  }
</style>