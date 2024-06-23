<template>
  <TimePicker
    :value="innerTimeValue"
    allowClear
    :format="format"
    popupClassName="j-vxe-time-picker"
    style="min-width: 0"
    v-model:open="openPicker"
    v-bind="cellProps"
    @change="handleChange"
  />
</template>

<script lang="ts">
  import { ref, computed, watch, defineComponent } from 'vue';
  import dayjs from 'dayjs';
  import { TimePicker } from 'ant-design-vue';
  import { JVxeComponent } from '/@/components/jeecg/JVxeTable/types';
  import { useJVxeComponent, useJVxeCompProps } from '/@/components/jeecg/JVxeTable/hooks';
  import { isEmpty } from '/@/utils/is';

  export default defineComponent({
    name: 'JVxeTimeCell',
    components: { TimePicker },
    props: useJVxeCompProps(),
    setup(props: JVxeComponent.Props) {
      const { innerValue, cellProps, originColumn, handleChangeCommon } = useJVxeComponent(props);
      const innerTimeValue = ref<any>(null);
      const format = computed(() => {
        let format = originColumn.value.format;
        return format ? format : 'HH:mm:ss';
      });
      const openPicker = ref(true);
      watch(
        innerValue,
        (val) => {
          if (val == null || isEmpty(val)) {
            innerTimeValue.value = null;
          } else {
            innerTimeValue.value = dayjs(val, format.value);
          }
        },
        { immediate: true }
      );

      function handleChange(_mom, dateStr) {
        handleChangeCommon(dateStr);
      }

      return {
        cellProps,
        format,
        innerTimeValue,
        openPicker,
        handleChange,
      };
    },
    // 【组件增强】注释详见：JVxeComponent.Enhanced
    enhanced: {
      aopEvents: {
      },
    } as JVxeComponent.EnhancedPartial,
  });
</script>
