<template>
  <a-radio-group
    :class="clazz"
    :value="innerValue"
    v-bind="cellProps"
    @change="(e)=>handleChangeCommon(e.target.value)"
  >
    <a-radio
      v-for="item of originColumn.options"
      :key="item.value"
      :value="item.value"
      @click="$event=>handleRadioClick(item,$event)"
    >{{ item.text }}
    </a-radio>
  </a-radio-group>
</template>

<script>
  import JVxeCellMixins from '@/components/jeecg/JVxeTable/mixins/JVxeCellMixins'

  export default {
    name: 'JVxeRadioCell',
    mixins: [JVxeCellMixins],
    computed: {
      scrolling() {
        return !!this.renderOptions.scrolling
      },
      clazz() {
        return {
          'j-vxe-radio': true,
          'no-animation': this.scrolling
        }
      },
    },
    methods: {
      handleRadioClick(item) {
        if (this.originColumn.allowClear === true) {
          // 取消选择
          if (item.value === this.innerValue) {
            this.handleChangeCommon(null)
          }
        }
      },
    },
    // 【组件增强】注释详见：JVxeCellMixins.js
    enhanced: {
      switches: {visible: true},
    }
  }
</script>

<style lang="less">
  // 关闭动画，防止滚动时动态赋值出现问题
  .j-vxe-radio.no-animation {
    .ant-radio-inner,
    .ant-radio-inner::after {
      transition: none !important;
    }
  }
</style>