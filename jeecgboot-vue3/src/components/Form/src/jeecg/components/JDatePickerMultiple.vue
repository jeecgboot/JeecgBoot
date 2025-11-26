<!-- 多选日期 -->
<template>
  <div class="j-date-multiple" ref="wrapperRef">
    <!-- 显示选中项：使用 ASelect 多选标签模式，禁止自身下拉，仅作展示与清空/删除入口 -->
    <a-select
      :value="displayValues"
      mode="multiple"
      :placeholder="placeholder"
      :disabled="disabled"
      allowClear
      :open="false"
      :getPopupContainer="getParentContainer"
      v-bind="attrs"
      @change="onSelectChange"
      @click="openPanel"
    />

    <!-- 隐形触发器，仅用于承载弹出层与月份切换，面板受控打开 -->
    <a-date-picker
      ref="pickerRef"
      :value="void 0"
      :open="open"
      :getPopupContainer="getParentContainer"
      :inputReadOnly="true"
      :allowClear="false"
      :disabledDate="disabledDate"
      :format="format"
      :showToday="false"
      @openChange="onOpenChange"
      class="hidden-picker"
      popupClassName="j-date-multiple-popup"
    >
      <template #dateRender="{ current }">
        <div
          class="ant-picker-cell-inner jdm-cell"
          :class="{ 'is-selected': isSelected(current) }"
          @click.stop.prevent="onCellClick(current)"
        >
          {{ current.date() }}
        </div>
      </template>
    </a-date-picker>
  </div>
</template>

<script setup lang="ts">
import {computed, nextTick, onMounted, ref, watch} from 'vue';
import dayjs, {Dayjs} from 'dayjs';
import {propTypes} from '/@/utils/propTypes';
import {useAttrs} from '/@/hooks/core/useAttrs';
import {useRuleFormItem} from '/@/hooks/component/useFormItem';
import {setPopContainer} from '/@/utils';

const props = defineProps({
  value: propTypes.string.def(''), // 多个日期用分隔符拼接
  placeholder: propTypes.string.def('请选择日期'),
  separator: propTypes.string.def(','),
  format: propTypes.string.def('YYYY-MM-DD'), // 展示格式
  valueFormat: propTypes.string.def('YYYY-MM-DD'), // 存储格式
  disabled: propTypes.bool.def(false),
  popContainer: propTypes.string.def(''),
  // 透传给 DatePicker 的禁用规则
  disabledDate: {
    type: Function as unknown as () => (current: Dayjs) => boolean,
    default: undefined,
  },
});

const emit = defineEmits(['change', 'update:value']);

// attrs 透传到 ASelect 以复用大小/样式等
const attrs = useAttrs();

// 与表单校验联动
const emitData = ref<any[]>([]);
const [, , , formItemContext] = useRuleFormItem(props as any, 'value', 'change', emitData);

const wrapperRef = ref<HTMLElement | null>(null);
const pickerRef = ref();
const open = ref(false);

// 内部选中列表（存储格式字符串数组）
const selectedValueStrings = ref<string[]>([]);

// 从外部 value 初始化/同步内部选中
watch(
  () => props.value,
  (val) => {
    if (!val) {
      selectedValueStrings.value = [];
    } else {
      const arr = String(val)
        .split(props.separator)
        .map((s) => s && s.trim())
        .filter((s) => !!s) as string[];
      // 去重
      const set = new Set(arr);
      selectedValueStrings.value = Array.from(set);
    }
  },
  {immediate: true}
);

// 展示值（格式化为 format）
const displayValues = computed<string[]>(() => {
  return selectedValueStrings.value.map((sv) => {
    const d = dayjs(sv, props.valueFormat);
    if (d.isValid()) {
      return d.format(props.format);
    } else {
      return sv;
    }
  });
});

// 选择器容器：优先 popContainer，其次使用本组件包裹元素
function getParentContainer(node: HTMLElement) {
  if (!props.popContainer) {
    return wrapperRef.value ?? node?.parentNode;
  } else {
    return setPopContainer(node, props.popContainer);
  }
}

function openPanel() {
  if (props.disabled) {
    return;
  }
  open.value = true;
}

function onOpenChange(val: boolean) {
  // 仅在外部点击时关闭；日期点击由我们拦截，不会触发默认关闭
  open.value = val;
}

function isSelected(current: Dayjs) {
  const key = current.format(props.valueFormat);
  return selectedValueStrings.value.includes(key);
}

function onCellClick(current: Dayjs) {
  if (props.disabled) {
    return;
  }
  if (props.disabledDate && props.disabledDate(current)) {
    return;
  }
  const key = current.format(props.valueFormat);
  const idx = selectedValueStrings.value.indexOf(key);
  if (idx >= 0) {
    selectedValueStrings.value.splice(idx, 1);
  } else {
    selectedValueStrings.value.push(key);
  }
  triggerChange();
  // 保持面板开启
  nextTick(() => {
    open.value = true;
  });
}

function onSelectChange(nextDisplayValues: string[]) {
  // 从展示值还原为存储格式
  const nextStoreValues: string[] = [];
  for (const dv of nextDisplayValues) {
    const d = dayjs(dv, props.format);
    if (d.isValid()) {
      nextStoreValues.push(d.format(props.valueFormat));
    }
  }
  // 去重
  const set = new Set(nextStoreValues);
  selectedValueStrings.value = Array.from(set);
  triggerChange();
}

function triggerChange() {
  const joined = selectedValueStrings.value.join(props.separator);
  emit('change', joined);
  emit('update:value', joined);
  nextTick(() => {
    if (formItemContext && formItemContext.onFieldChange) {
      formItemContext.onFieldChange();
    }
  });
}

onMounted(() => {
  // nothing now
});

</script>

<style lang="less">
.j-date-multiple {
  position: relative;
  width: 100%;

  .hidden-picker {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 32px;
    opacity: 0;
    pointer-events: none;
  }
}

.j-date-multiple-popup.ant-picker-dropdown {
  .ant-picker-cell {
    .ant-picker-cell-inner.jdm-cell {
      &.is-selected {
        color: #fff !important;
        // noinspection LessUnresolvedVariable
        background-color: @primary-color !important;
      }
    }
  }
}
</style>
