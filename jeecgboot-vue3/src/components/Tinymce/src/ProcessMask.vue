<template>
  <div :class="[prefixCls]" v-if="showMask && show">
    <div class="progress-bar-rear">
      <div class="progress-bar-front" :style="{ width: progressBarWidth }"></div>
    </div>
    <div class="value">{{ percentage }}</div>
  </div>
</template>
<script lang="ts" setup>
  import { computed, ref } from 'vue';
  import {useDesign} from "@/hooks/web/useDesign";

  const props = defineProps({
    backColor: {
      type: [String],
      default: 'white',
    },
    processColor: {
      type: String,
      default: '#018FFB',
    },
    show: {
      type: Boolean,
      default: false,
    },
  });
  const { prefixCls } = useDesign('tinymce-process-mask');

  //显示遮罩
  const showMask = ref(false);
  //进度值占比
  const progressValue = ref<any>(0);
  //当前数量
  const currentNum = ref(0);
  // 计算进度条宽度的计算属性
  const progressBarWidth = computed(() => {
    return progressValue.value > 0 ? `${progressValue.value}px` : '0px';
  });
  // 计算进度条百分比
  const percentage = computed(() => {
    return `${progressValue.value}%`;
  });
  // 进度色
  const frontColor = computed(() => {
    return props.processColor;
  });
  // 后置背景色
  const rearColor = computed(() => {
    return props.backColor;
  });
  function calcProcess(totalNum) {
    !showMask.value && (showMask.value = true);
    currentNum.value += 1;
    progressValue.value = ((currentNum.value / totalNum) * 100).toFixed(2);
    console.log('currentNum.value', currentNum.value);
    console.log('totalNum.value', totalNum);
    if (currentNum.value == totalNum) {
      showMask.value = false;
      currentNum.value = 0;
      progressValue.value = 0;
    }
  }
  defineExpose({
    calcProcess,
    showMask,
  });
</script>

<style lang="less">
//noinspection LessUnresolvedVariable
@prefix-cls: ~'@{namespace}-tinymce-process-mask';

.@{prefix-cls} {

  & {
    position: absolute; /* 或者使用固定定位等其他方式 */
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5); /* 半透明遮罩 */
    display: flex;
    justify-content: center;
    align-items: center;
    overflow: hidden;
    z-index: 99;
  }

  .progress-bar-rear {
    width: 100px; /* 进度条宽度 */
    height: 10px; /* 进度条高度 */
    background-color: v-bind(rearColor); /* 进度条颜色 */
    border-radius: 4px;
  }

  .progress-bar-front {
    height: 10px; /* 进度条高度 */
    background-color: v-bind(frontColor); /* 进度条颜色 */
    border-radius: 4px;
  }

  .value {
    color: #fff;
    margin-left: 5px;
    font-size: 16px;
    font-weight: 600;
  }
}

</style>
