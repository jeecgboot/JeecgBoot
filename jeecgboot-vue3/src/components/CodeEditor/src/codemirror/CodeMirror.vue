<template>
  <div class="relative !h-full w-full overflow-hidden" ref="el"> </div>
</template>

<script lang="ts" setup>
  import { ref, onMounted, onUnmounted, watchEffect, watch, unref, nextTick } from 'vue';
  import { useDebounceFn } from '@vueuse/core';
  import { useAppStore } from '/@/store/modules/app';
  import { useWindowSizeFn } from '/@/hooks/event/useWindowSizeFn';
  import CodeMirror from 'codemirror';
  // css
  import './codemirror.css';
  import 'codemirror/theme/idea.css';
  import 'codemirror/theme/material-palenight.css';
  // modes
  import 'codemirror/mode/javascript/javascript';
  import 'codemirror/mode/css/css';
  import 'codemirror/mode/htmlmixed/htmlmixed';

  const props = defineProps({
    mode: { type: String, default: 'application/json' },
    value: { type: String, default: '' },
    readonly: { type: Boolean, default: false },
  });

  const emit = defineEmits(['change']);

  const el = ref();
  let editor: Nullable<CodeMirror.Editor>;

  const debounceRefresh = useDebounceFn(refresh, 100);
  const appStore = useAppStore();

  watch(
    () => props.value,
    async (value) => {
      await nextTick();
      const oldValue = editor?.getValue();
      if (value !== oldValue) {
        editor?.setValue(value ? value : '');
      }
    },
    { flush: 'post' }
  );

  watchEffect(() => {
    editor?.setOption('mode', props.mode);
  });

  watch(
    () => appStore.getDarkMode,
    async () => {
      setTheme();
    },
    {
      immediate: true,
    }
  );

  function setTheme() {
    unref(editor)?.setOption('theme', appStore.getDarkMode === 'light' ? 'idea' : 'material-palenight');
  }

  function refresh() {
    editor?.refresh();
  }

  async function init() {
    const addonOptions = {
      autoCloseBrackets: true,
      autoCloseTags: true,
      foldGutter: true,
      gutters: ['CodeMirror-linenumbers'],
    };

    editor = CodeMirror(el.value!, {
      value: '',
      mode: props.mode,
      readOnly: props.readonly,
      tabSize: 2,
      theme: 'material-palenight',
      lineWrapping: true,
      lineNumbers: true,
      ...addonOptions,
    });
    editor?.setValue(props.value);
    setTheme();
    editor?.on('change', () => {
      emit('change', editor?.getValue());
    });
  }

  onMounted(async () => {
    await nextTick();
    init();
    useWindowSizeFn(debounceRefresh);
  });

  onUnmounted(() => {
    editor = null;
  });
</script>
