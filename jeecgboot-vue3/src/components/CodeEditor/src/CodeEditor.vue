<template>
  <div class="h-full">
    <CodeMirrorEditor :value="getValue" @change="handleValueChange" :mode="mode" :readonly="readonly" />
  </div>
</template>

<script lang="ts">
  const MODE = {
    JSON: 'application/json',
    html: 'htmlmixed',
    js: 'javascript',
  };
</script>
<script lang="ts" setup>
  import { computed } from 'vue';
  import CodeMirrorEditor from './codemirror/CodeMirror.vue';
  import { isString } from '/@/utils/is';
  
  const props = defineProps({
    value: { type: [Object, String] as PropType<Record<string, any> | string> },
    mode: { type: String, default: MODE.JSON },
    readonly: { type: Boolean },
    autoFormat: { type: Boolean, default: true },
  });

  const emit = defineEmits(['change', 'update:value', 'format-error']);

  const getValue = computed(() => {
    const { value, mode, autoFormat } = props;
    if (!autoFormat || mode !== MODE.JSON) {
      return value as string;
    }
    let result = value;
    if (isString(value)) {
      try {
        result = JSON.parse(value);
      } catch (e) {
        emit('format-error', value);
        return value as string;
      }
    }
    return JSON.stringify(result, null, 2);
  });

  function handleValueChange(v) {
    emit('update:value', v);
    emit('change', v);
  }
</script>
