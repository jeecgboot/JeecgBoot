<template>
  <div class="head-info" :class="center && 'center'">
    <span>{{ title }}</span>
    <p :style="{ color: `${$props.iconColor}` }">{{ content }} <a-icon :type="icon" :style="{ fontSize: `24px`, color: `${$props.iconColor}` }" /></p>
    <em v-if="bordered" />
  </div>
</template>

<script lang="ts">
  import { defineComponent } from 'vue';

  export default defineComponent({
    name: 'HeadInfo',
    props: {
      title: {
        type: String,
        default: '',
      },
      content: {
        type: String,
        default: '',
      },
      bordered: {
        type: Boolean,
        default: false,
      },
      center: {
        type: Boolean,
        default: true,
      },
      icon: {
        type: String,
        default: false,
      },
      // update-begin--author:liaozhiyang---date:20240407---for：【QQYUN-8762】首页默认及echars颜色调整
      iconColor: {
        type: String,
        default: '#2b99ff',
      },
      // update-end--author:liaozhiyang---date:20240407---for：【QQYUN-8762】首页默认及echars颜色调整
    },
  });
</script>

<style lang="less" scoped>
  .head-info {
    position: relative;
    text-align: left;
    padding: 0 32px 0 0;
    min-width: 125px;

    &.center {
      text-align: center;
      padding: 0 32px;
    }

    span {
      display: inline-block;
      font-size: 14px;
      line-height: 22px;
      margin-bottom: 4px;
    }

    p {
      font-size: 24px;
      line-height: 32px;
      margin: 0;
    }

    em {
      background-color: #e8e8e8;
      position: absolute;
      height: 56px;
      width: 1px;
      top: 0;
      right: 0;
    }
  }
</style>
