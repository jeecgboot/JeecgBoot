<template>
  <a-radio-group :class="clazz" :value="innerValue" v-bind="cellProps" @change="(e) => handleChangeCommon(e.target.value)">
    <a-radio v-for="item of originColumn.options" :key="item.value" :value="item.value" @click="(e) => handleRadioClick(item, e)"
      >{{ item.text || item.label || item.title || item.value }}
    </a-radio>
  </a-radio-group>
</template>

<script lang="ts">
  import { computed, defineComponent } from 'vue';
  import { JVxeComponent } from '/@/components/jeecg/JVxeTable/types';
  import { useJVxeComponent, useJVxeCompProps } from '/@/components/jeecg/JVxeTable/hooks';

  export default defineComponent({
    name: 'JVxeRadioCell',
    props: useJVxeCompProps(),
    setup(props: JVxeComponent.Props) {
      const { innerValue, cellProps, originColumn, handleChangeCommon } = useJVxeComponent(props);
      const scrolling = computed(() => !!props.renderOptions.scrolling);
      const clazz = computed(() => {
        return {
          'j-vxe-radio': true,
          'no-animation': scrolling.value,
        };
      });

      function handleRadioClick(item) {
        if (originColumn.value.allowClear === true) {
          // 取消选择
          if (item.value === innerValue.value) {
            handleChangeCommon(null);
          }
        }
      }

      return {
        clazz,
        innerValue,
        originColumn,
        cellProps,
        handleRadioClick,
        handleChangeCommon,
      };
    },
    // 【组件增强】注释详见：JVxeComponent.Enhanced
    enhanced: {
      switches: { visible: true },
    } as JVxeComponent.EnhancedPartial,
  });
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
