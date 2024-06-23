<template>
  <div :class="prefixCls">
    <a-select
      v-if="query"
      v-model:value="state"
      :options="selectOptions"
      :disabled="disabled"
      style="width: 100%"
      v-bind="attrs"
      @change="onSelectChange"
    />
    <a-switch v-else v-model:checked="checked" :disabled="disabled" v-bind="attrs" @change="onSwitchChange" />
  </div>
</template>

<script lang="ts" setup>
  import { computed, ref, watch } from 'vue';
  import { propTypes } from '/@/utils/propTypes';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useRuleFormItem } from '/@/hooks/component/useFormItem';
  const { prefixCls } = useDesign('j-switch');
  const props = defineProps({
    // v-model:value
    value: propTypes.oneOfType([propTypes.string, propTypes.number]),
    // 取值 options
    options: propTypes.array.def(() => ['Y', 'N']),
    // 文本 options
    labelOptions: propTypes.array.def(() => ['是', '否']),
    // 是否使用下拉
    query: propTypes.bool.def(false),
    // 是否禁用
    disabled: propTypes.bool.def(false),
  });
  const attrs = useAttrs();
  const emit = defineEmits(['change', 'update:value']);

  const checked = ref<boolean>(false);
  const [state] = useRuleFormItem(props, 'value', 'change');
  watch(
    () => props.value,
    (val) => {
      if (!props.query) {
        // update-begin--author:liaozhiyang---date:20231226---for：【QQYUN-7473】options使用[0,1]，导致开关无法切换
        if (!val && !props.options.includes(val)) {
          checked.value = false;
          emitValue(props.options[1]);
        } else {
          checked.value = props.options[0] == val;
        }
        // update-end--author:liaozhiyang---date:20231226---for：【QQYUN-7473】options使用[0,1]，导致开关无法切换
      }
    },
    { immediate: true }
  );

  const selectOptions = computed(() => {
    let options: any[] = [];
    options.push({ value: props.options[0], label: props.labelOptions[0] });
    options.push({ value: props.options[1], label: props.labelOptions[1] });
    return options;
  });

  function onSwitchChange(checked) {
    let flag = checked === false ? props.options[1] : props.options[0];
    emitValue(flag);
  }

  function onSelectChange(value) {
    emitValue(value);
  }

  function emitValue(value) {
    emit('change', value);
    emit('update:value', value);
  }
</script>

<style lang="less">
  //noinspection LessUnresolvedVariable
  @prefix-cls: ~'@{namespace}-j-switch';

  .@{prefix-cls} {
  }
</style>
