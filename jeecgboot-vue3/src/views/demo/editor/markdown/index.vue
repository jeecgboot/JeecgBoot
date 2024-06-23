<template>
  <PageWrapper title="MarkDown组件示例">
    <div>
      <a-button @click="toggleTheme" class="mb-2" type="primary"> 黑暗主题 </a-button>
      <a-button @click="clearValue" class="mb-2" type="default"> 清空内容 </a-button>
      <MarkDown v-model:value="value" @change="handleChange" ref="markDownRef" placeholder="这是占位文本" />
    </div>
    <div class="mt-2">
      <a-card title="Markdown Viewer 组件演示">
        <MarkdownViewer :value="value" />
      </a-card>
    </div>
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, ref, unref } from 'vue';
  import { MarkDown, MarkDownActionType, MarkdownViewer } from '/@/components/Markdown';
  import { PageWrapper } from '/@/components/Page';
  import { Card } from 'ant-design-vue';

  export default defineComponent({
    components: { MarkDown, PageWrapper, MarkdownViewer, ACard: Card },
    setup() {
      const markDownRef = ref<Nullable<MarkDownActionType>>(null);
      const valueRef = ref(`
# title

# content
`);

      function toggleTheme() {
        const markDown = unref(markDownRef);
        if (!markDown) return;
        const vditor = markDown.getVditor();
        vditor.setTheme('dark');
      }

      function handleChange(v: string) {
        valueRef.value = v;
      }

      function clearValue() {
        valueRef.value = '';
      }

      return {
        value: valueRef,
        toggleTheme,
        markDownRef,
        handleChange,
        clearValue,
      };
    },
  });
</script>
