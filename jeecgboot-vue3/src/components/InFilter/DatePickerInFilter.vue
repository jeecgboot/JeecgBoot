<!-- 在筛选中使用的日期选择器（可选择今天、本周等范围） -->
<template>
  <a-space :id="formItemId" :class="[prefixCls]" direction="vertical">
    <a-space-compact block>
      <!-- 日期范围下拉 -->
      <a-select v-if="isRange" v-model:value="innerValue">
        <a-select-option v-for="opt of RANGE_OPTIONS" :key="opt.key" :value="opt.key">
          {{ opt.label }}
        </a-select-option>
        <a-select-option key="custom" value="custom">
          自定义日期
        </a-select-option>
      </a-select>
      <!-- 自定义日期选择器 -->
      <DatePicker v-else v-model:value="innerValue" v-model:open="datePickerIsOpen" v-bind="attrs"/>
      <!-- 右侧下拉菜单 -->
      <a-dropdown v-if="allowSelectRange" :trigger="['click']">
        <a-button preIcon="ant-design:menu-unfold"/>
        <template #overlay>
          <a-menu @click="onMenuClick">
            <a-menu-item v-for="opt of RANGE_OPTIONS" :key="opt.key">
              {{ opt.label }}
            </a-menu-item>
            <a-menu-item key="custom">
              自定义日期
            </a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
    </a-space-compact>
  </a-space>
</template>

<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
  name: 'DatePickerInFilter',
  inheritAttrs: false,
})
</script>

<script lang="ts" setup>
import {ref, watch, computed, nextTick, useAttrs, defineProps} from 'vue'
import {DatePicker} from 'ant-design-vue'
import {useDesign} from '/@/hooks/web/useDesign';
import { Form } from 'ant-design-vue';
const formItemContext = Form.useInjectFormItemContext();

// 日期范围选项
const RANGE_OPTIONS = [
  {key: 'TODAY', label: '今天'},
  {key: 'YESTERDAY', label: '昨天'},
  {key: 'TOMORROW', label: '明天'},
  {key: 'THIS_WEEK', label: '本周'},
  {key: 'LAST_WEEK', label: '上周'},
  {key: 'NEXT_WEEK', label: '下周'},
  {key: 'LAST_7_DAYS', label: '过去七天'},
  {key: 'THIS_MONTH', label: '本月'},
  {key: 'LAST_MONTH', label: '上月'},
  {key: 'NEXT_MONTH', label: '下月'},
];
const RANGE_OPTION_KEYS = RANGE_OPTIONS.map(item => item.key)

const {prefixCls} = useDesign('j-data-picker-in-filter');
const props = defineProps({
  value: {
    type: [String],
    default: ''
  },
  // 是否允许选择预设范围
  allowSelectRange: {
    type: Boolean,
    default: true,
  }
})
const emit = defineEmits(['change', 'update:value'])
const attrs = useAttrs()

// 组件id
const formItemId  = computed(() => formItemContext.id.value)

const innerValue = ref(props.value)
// 是否打开日期选择器
const datePickerIsOpen = ref(false)

// 判断是否为范围选项
const isRange = computed(() => RANGE_OPTION_KEYS.includes(innerValue.value));

// 同步value
watch(() => props.value, (val) => {
  innerValue.value = val
})

// emit 更改
watch(innerValue, (val) => {
  if (val === 'custom') {
    val = ''
    openDatePicker()
  }
  emit('change', val)
  emit('update:value', val)
  // update-begin--author:liaozhiyang---date:20240509---for：【QQYUN-9227】日期校验没清空
  formItemContext?.onFieldChange();
  // update-end--author:liaozhiyang---date:20240509---for：【QQYUN-9227】日期校验没清空
})

watch(() => props.allowSelectRange, (allow) => {
  // 如果不允许选择范围，且当前值为范围选项，则清空值
  if (!allow && isRange.value) {
    innerValue.value = ''
  }
}, {immediate: true});

// 点击右侧下拉菜单
function onMenuClick(event: Recordable) {
  if (event.key === 'custom') {
    if (isRange.value) {
      innerValue.value = '';
    }
    openDatePicker()
  } else {
    innerValue.value = event.key
  }
}

// 打开日期选择器下拉
async function openDatePicker() {
  await nextTick()
  datePickerIsOpen.value = true
}

</script>

<style lang="less">
//noinspection LessUnresolvedVariable
@prefix-cls: ~'@{namespace}-j-data-picker-in-filter';

.@{prefix-cls} {
  width: 100%;
}
</style>
