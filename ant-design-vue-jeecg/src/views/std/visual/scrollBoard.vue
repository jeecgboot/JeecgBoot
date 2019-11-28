<template>
  <div id="scroll-board">
    <dv-scroll-board :config="config" />
  </div>
</template>

<script>
  export default {
    name: 'ScrollBoard',
    data () {
      return {
        config: {
          header: ['排名', '进口国',  '提单数','箱量数(TEU)'],
          data: [
            ['1', '土耳其', '5', 'xxxxxxx'],
            ['2', '意大利', '13', 'xxxxxxx'],
            ['3', '埃及', '6', 'xxxxxxx'],
            ['4', '埃及', '2', 'xxxxxxx'],
            ['5', '埃及', '1', 'xxxxxxx'],
            ['6', '埃及', '3', 'xxxxxxx'],
            ['7', '埃及', '4', 'xxxxxxx'],
            ['8', '埃及', '2', 'xxxxxxx'],
            ['9', '埃及', '5', 'xxxxxxx'],
            ['10', '埃及', '3', 'xxxxxxx']
          ],
          index: false,
          columnWidth: [180, 170, 140],
          align: ['center'],
          rowNum: 7,
          headerBGC: 'rgba(0,0,0,0.2)',
          headerHeight: 45,
          oddRowBGC: 'rgba(255,255,255,0.08)',
          evenRowBGC: 'rgba(10, 29, 50, 0.02)'
        }
      }
    }
  }
</script>

<style lang="less">
  #scroll-board {
    position: absolute;
    width: 50%;
    box-sizing: border-box;
    margin-left: 20px;
    height: 30%;
    overflow: hidden;
    top: 50%;
    left: 65%;
  }
  .layout .header{
    height: 45px;
  }
</style>