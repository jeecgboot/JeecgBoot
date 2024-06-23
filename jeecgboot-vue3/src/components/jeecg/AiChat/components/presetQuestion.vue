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
        <li v-for="(item, index) in data" :key="index" class="item" @click="handleQuestion(item)">{{ item }}</li>
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
  import { ref, onMounted, onBeforeUnmount } from 'vue';
  const emit = defineEmits(['outQuestion']);
  const data = ref(['请介绍一下JeecgBoot', 'JEECG有哪些优势？', 'JEECG可以做哪些事情？']);
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
      color: #333;
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
  }
</style>
