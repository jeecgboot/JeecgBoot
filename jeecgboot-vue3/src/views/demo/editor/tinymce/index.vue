<template>
  <PageWrapper title="富文本组件示例">
    <Tinymce v-model="value" @change="handleChange" width="100%" />
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { Tinymce } from '/@/components/Tinymce/index';
  import { PageWrapper } from '/@/components/Page';

  export default defineComponent({
    components: { Tinymce, PageWrapper },
    setup() {
      const value = ref('hello world!');
      function handleChange(value: string) {
        console.log(value);
      }
      return { handleChange, value };
    },
  });
</script>
