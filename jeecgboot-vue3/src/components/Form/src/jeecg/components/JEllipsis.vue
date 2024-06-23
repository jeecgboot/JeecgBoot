<template>
  <a-tooltip placement="topLeft">
    <template #title>
      <span>{{ value }}</span>
    </template>
    {{ showText }}
  </a-tooltip>
</template>
<script lang="ts" setup>
  import { computed } from 'vue';
  import { propTypes } from '/@/utils/propTypes';

  const props = defineProps({
    value: propTypes.oneOfType([propTypes.string, propTypes.number, propTypes.array]),
    length: propTypes.number.def(25),
  });
  //显示的文本
  const showText = computed(() =>
    props.value ? (props.value.length > props.length ? props.value.slice(0, props.length) + '...' : props.value) : props.value
  );
</script>
