<template>
  <a-date-picker
    :value="innerDateValue"
    allowClear
    :format="picker ? null : dateFormat"
    :showTime="isDatetime"
    :valueFormat="picker ? dateFormat : null"
    popupClassName="j-vxe-date-picker"
    style="min-width: 0"
    v-model:open="openPicker"
    v-bind="cellProps"
    :picker="picker"
    @change="handleChange"
  />
</template>

<script lang="ts">
  import { ref, computed, watch, defineComponent } from 'vue';
  import dayjs from 'dayjs';
  import { JVxeComponent, JVxeTypes } from '/@/components/jeecg/JVxeTable/types';
  import { useJVxeComponent, useJVxeCompProps } from '/@/components/jeecg/JVxeTable/hooks';
  import { isEmpty } from '/@/utils/is';
  import { getWeekMonthQuarterYear } from '/@/utils';

  export default defineComponent({
    name: 'JVxeDateCell',
    props: useJVxeCompProps(),
    setup(props: JVxeComponent.Props) {
      const { innerValue, cellProps, originColumn, handleChangeCommon } = useJVxeComponent(props);
      const innerDateValue = ref<any>(null);
      const isDatetime = computed(() => props.type === JVxeTypes.datetime);
      const dateFormat = computed(() => {
        let format = originColumn.value.format;
        return format ? format : isDatetime.value ? 'YYYY-MM-DD HH:mm:ss' : 'YYYY-MM-DD';
      });
      const openPicker = ref(true);
      // update-begin--author:liaozhiyang---date:20240509---for：【QQYUN-9205】一对多(jVxetable组件date)支持年，年月，年度度，年周
      const picker = computed(() => {
        const picker = originColumn.value.picker;
        return picker ? picker : null;
      });
      // update-end--author:liaozhiyang---date:20240509---for：【QQYUN-9205】一对多(jVxetable组件date)支持年，年月，年度度，年周
      watch(
        innerValue,
        (val) => {
          if (val == null || isEmpty(val)) {
            innerDateValue.value = null;
          } else {
            innerDateValue.value = dayjs(val, dateFormat.value);
          }
        },
        { immediate: true }
      );

      function handleChange(_mom, dateStr) {
        // update-begin--author:liaozhiyang---date:20240509---for：【QQYUN-9205】一对多(jVxetable组件date)支持年，年月，年度度，年周
        if (picker.value) {
          handleChangeCommon(_mom);
        } else {
          handleChangeCommon(dateStr);
        }
        // update-begin--author:liaozhiyang---date:20240509---for：【QQYUN-9205】一对多(jVxetable组件date)支持年，年月，年度度，年周
      }

      return {
        cellProps,
        isDatetime,
        dateFormat,
        innerDateValue,
        openPicker,
        handleChange,
        picker,
      };
    },
    // 【组件增强】注释详见：JVxeComponent.Enhanced
    enhanced: {
      aopEvents: {
      },
      // update-begin--author:liaozhiyang---date:20240509---for：【QQYUN-9205】一对多(jVxetable组件date)支持年，年月，年度度，年周
      translate: {
        enabled: true,
        handler(value, ctx) {
          let { props, context } = ctx!;
          let { row, originColumn } = context;
          if (originColumn.value.picker && value) {
            return getWeekMonthQuarterYear(value)[originColumn.value.picker];
          }
          return value;
        },
      },
      // update-end--author:liaozhiyang---date:20240509---for：【QQYUN-9205】一对多(jVxetable组件date)支持年，年月，年度度，年周
    } as JVxeComponent.EnhancedPartial,
  });
</script>
