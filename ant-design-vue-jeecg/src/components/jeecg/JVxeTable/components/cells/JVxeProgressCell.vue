<template>
  <a-progress
    :class="clazz"
    :percent="innerValue"
    size="small"
    v-bind="cellProps"
  />
</template>

<script>
  import JVxeCellMixins from '@/components/jeecg/JVxeTable/mixins/JVxeCellMixins'

  // JVxe 进度条组件
  export default {
    name: 'JVxeProgressCell',
    mixins: [JVxeCellMixins],
    data() {
      return {}
    },
    computed: {
      clazz() {
        return {
          'j-vxe-progress': true,
          'no-animation': this.scrolling
        }
      },
      scrolling() {
        return !!this.renderOptions.scrolling
      },
    },
    methods: {},
    // 【组件增强】注释详见：JVxeCellMixins.js
    enhanced: {
      switches: {
        editRender: false,
      },
      setValue(value) {
        try {
          if (typeof value !== 'number') {
            return Number.parseFloat(value)
          } else {
            return value
          }
        } catch {
          return 0
        }
      },
    }
  }
</script>

<style scoped lang="less">
  // 关闭进度条的动画，防止滚动时动态赋值出现问题
  .j-vxe-progress.no-animation {
    /deep/ .ant-progress-success-bg,
    /deep/ .ant-progress-bg {
      transition: none !important;
    }
  }
</style>