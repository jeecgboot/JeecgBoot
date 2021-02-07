<template>
  <div class="chart-mini-progress">
    <div class="target" :style="{ left: target + '%'}">
      <span :style="{ backgroundColor: color }"/>
      <span :style="{ backgroundColor: color }"/>
    </div>
    <div class="progress-wrapper">
      <div class="progress" :style="{ backgroundColor: color, width: percentage + '%', height: height+'px' }"></div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'MiniProgress',
    props: {
      target: {
        type: Number,
        default: 0
      },
      height: {
        type: Number,
        default: 10
      },
      color: {
        type: String,
        default: '#13C2C2'
      },
      percentage: {
        type: Number,
        default: 0
      }
    }
  }
</script>

<style lang="less" scoped>
  .chart-mini-progress {
    padding: 5px 0;
    position: relative;
    width: 100%;

    .target {
      position: absolute;
      top: 0;
      bottom: 0;

      span {
        border-radius: 100px;
        position: absolute;
        top: 0;
        left: 0;
        height: 4px;
        width: 2px;

        &:last-child {
          top: auto;
          bottom: 0;
        }
      }
    }
    .progress-wrapper {
      background-color: #f5f5f5;
      position: relative;

      .progress {
        transition: all .4s cubic-bezier(.08, .82, .17, 1) 0s;
        border-radius: 1px 0 0 1px;
        background-color: #1890ff;
        width: 0;
        height: 100%;
      }
    }
  }
</style>