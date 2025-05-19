<template>
  <div class="presetQuestion-wrap">
    <svg
      v-if="btnShow"
      class="leftBtn"
      :class="leftBtnStatus"
      t="1710296339017"
      viewBox="0 0 1024 1024"
      version="1.1"
      xmlns="http://www.w3.org/2000/svg"
      p-id="5070"
      @click="onScroll('prev')"
    >
      <path
        d="M970.496 543.829333l30.165333-30.165333-415.829333-415.914667a42.837333 42.837333 0 0 0-60.288 0 42.538667 42.538667 0 0 0 0 60.330667l355.413333 355.498667-355.413333 355.285333a42.496 42.496 0 0 0 0 60.288c16.64 16.64 43.861333 16.469333 60.288 0.042667l383.914667-383.701334 1.749333-1.664z"
        fill="currentColor"
        p-id="5071"
      ></path>
    </svg>
    <div class="content">
      <ul ref="ulElemRef">
        <li v-for="(item, index) in data" :key="index" class="item" @click="handleQuestion(item.descr)">
          <div class="question-descr">
            <Icon v-if="item.icon" :icon="item.icon" size="20"></Icon>
            <svg v-else width="14px" height="14px" viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg"><path d="M18.9839 1.85931C19.1612 1.38023 19.8388 1.38023 20.0161 1.85931L20.5021 3.17278C20.5578 3.3234 20.6766 3.44216 20.8272 3.49789L22.1407 3.98392C22.6198 4.1612 22.6198 4.8388 22.1407 5.01608L20.8272 5.50211C20.6766 5.55784 20.5578 5.6766 20.5021 5.82722L20.0161 7.14069C19.8388 7.61977 19.1612 7.61977 18.9839 7.14069L18.4979 5.82722C18.4422 5.6766 18.3234 5.55784 18.1728 5.50211L16.8593 5.01608C16.3802 4.8388 16.3802 4.1612 16.8593 3.98392L18.1728 3.49789C18.3234 3.44216 18.4422 3.3234 18.4979 3.17278L18.9839 1.85931zM13.5482 4.07793C13.0164 2.64069 10.9836 2.64069 10.4518 4.07793L8.99368 8.01834C8.82648 8.47021 8.47021 8.82648 8.01834 8.99368L4.07793 10.4518C2.64069 10.9836 2.64069 13.0164 4.07793 13.5482L8.01834 15.0063C8.47021 15.1735 8.82648 15.5298 8.99368 15.9817L10.4518 19.9221C10.9836 21.3593 13.0164 21.3593 13.5482 19.9221L15.0063 15.9817C15.1735 15.5298 15.5298 15.1735 15.9817 15.0063L19.9221 13.5482C21.3593 13.0164 21.3593 10.9836 19.9221 10.4518L15.9817 8.99368C15.5298 8.82648 15.1735 8.47021 15.0063 8.01834L13.5482 4.07793zM5.01608 16.8593C4.8388 16.3802 4.1612 16.3802 3.98392 16.8593L3.49789 18.1728C3.44216 18.3234 3.3234 18.4422 3.17278 18.4979L1.85931 18.9839C1.38023 19.1612 1.38023 19.8388 1.85931 20.0161L3.17278 20.5021C3.3234 20.5578 3.44216 20.6766 3.49789 20.8272L3.98392 22.1407C4.1612 22.6198 4.8388 22.6198 5.01608 22.1407L5.50211 20.8272C5.55784 20.6766 5.6766 20.5578 5.82722 20.5021L7.14069 20.0161C7.61977 19.8388 7.61977 19.1612 7.14069 18.9839L5.82722 18.4979C5.6766 18.4422 5.55784 18.3234 5.50211 18.1728L5.01608 16.8593z"></path></svg>
            <span>{{ item.name }}</span>
          </div>
        </li>
      </ul>
    </div>
    <svg
      v-if="btnShow"
      class="rightBtn"
      :class="rightBtnStatus"
      t="1710296339017"
      viewBox="0 0 1024 1024"
      version="1.1"
      xmlns="http://www.w3.org/2000/svg"
      p-id="5070"
      @click="onScroll('next')"
    >
      <path
        d="M970.496 543.829333l30.165333-30.165333-415.829333-415.914667a42.837333 42.837333 0 0 0-60.288 0 42.538667 42.538667 0 0 0 0 60.330667l355.413333 355.498667-355.413333 355.285333a42.496 42.496 0 0 0 0 60.288c16.64 16.64 43.861333 16.469333 60.288 0.042667l383.914667-383.701334 1.749333-1.664z"
        fill="currentColor"
        p-id="5071"
      ></path>
    </svg>
  </div>
</template>

<script name="presetQuestion" setup lang="ts">
import {ref, onMounted, onBeforeUnmount, watch} from 'vue';
  const emit = defineEmits(['outQuestion']);
  const props = defineProps({
    quickCommandData:{ type: Object },
  });
  const data = ref(props.quickCommandData);
  const leftBtnStatus = ref('');
  const rightBtnStatus = ref('');
  const rightBtn = ref('');
  const ulElemRef = ref(null);
  const btnShow = ref(false);
  let timer = null;
  const handleScroll = (e) => {
    clearTimeout(timer);
    timer = setTimeout(() => {
      const scrollLeft = e.target.scrollLeft;
      const offsetWidth = e.target.offsetWidth;
      const scrollWidth = e.target.scrollWidth;
      if (scrollWidth > offsetWidth) {
        btnShow.value = true;
      } else {
        btnShow.value = false;
      }
      if (scrollLeft <= 0) {
        leftBtnStatus.value = 'disabled';
      } else if (scrollWidth - offsetWidth == scrollLeft) {
        rightBtnStatus.value = 'disabled';
      } else {
        leftBtnStatus.value = '';
        rightBtnStatus.value = '';
      }
    }, 100);
  };
  const onScroll = (flag) => {
    const offsetWidth = ulElemRef.value.offsetWidth;
    if (flag == 'prev') {
      ulElemRef.value.scrollLeft = ulElemRef.value.scrollLeft - offsetWidth;
    } else if (flag == 'next') {
      ulElemRef.value.scrollLeft = ulElemRef.value.scrollLeft + offsetWidth;
    }
  };
  const handleQuestion = (item) => {
    emit('outQuestion', item);
  };
  
  watch(()=>props.quickCommandData, (val) => {
    data.value = props.quickCommandData;
  })
  
  onMounted(() => {
    ulElemRef.value.addEventListener('scroll', handleScroll, false);
    handleScroll({ target: ulElemRef.value });
  });
  onBeforeUnmount(() => {
    ulElemRef.value.removeEventListener('scroll', handleScroll);
  });
</script>

<style lang="less" scoped>
  .presetQuestion-wrap {
    display: flex;
    align-items: center;
    svg {
      width: 14px;
      height: 14px;
      flex: none;
      cursor: pointer;
      color: #c6c2c2;
      &.leftBtn {
        transform: rotate(180deg);
      }
      &.disabled {
        opacity: 0.5;
        pointer-events: none;
        cursor: default;
      }
    }
    .content {
      flex: 1;
      min-width: 0;
      overflow: hidden;
    }
    ul {
      display: flex;
      margin-bottom: 0;
      width: 100%;
      overflow-y: auto;
      /* 隐藏所有滚动条 */
      &::-webkit-scrollbar {
        display: none;
        height: 0;
        width: 0;
      }
    }
    .item {
      border: 1px solid rgba(0, 0, 0, 0.1);
      border-radius: 16px;
      cursor: pointer;
      font-size: 14px;
      padding: 2px 10px;
      width: max-content;
      margin-right: 6px;
      white-space: nowrap;
      transition: all 300ms ease;
      &:last-child {
        margin-right: 0;
      }
      &:hover {
        color: @primary-color;
        border-color: @primary-color;
      }
    }
    .question-descr{
      display: flex;
      align-items: center;
      span{
        margin-left: 4px;
      }
    }
  }
</style>
