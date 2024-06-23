<template>
  <!-- <div v-html="getHtmlData" :class="$props.class" class="markdown-viewer markdown-body"></div> -->
  <!-- update-begin--author:liaozhiyang---date:20231201---for：【issues/872】MarkdownViewer组件无样式 -->
  <div class="preview" :class="[{ preview_dark: isDarkTheme }]">
    <div v-html="getHtmlData" :class="$props.class" class="markdown-viewer vditor-reset"></div>
  </div>
  <!-- update-begin--author:liaozhiyang---date:20231201---for：【issues/872】MarkdownViewer组件无样式 -->
</template>

<script lang="ts" setup>
  import { computed, watch, ref } from 'vue';
  import showdown from 'showdown';
  import 'vditor/dist/index.css';
  import { useRootSetting } from '/@/hooks/setting/useRootSetting';
  import { ThemeEnum } from '/@/enums/appEnum';
  
  const converter = new showdown.Converter();
  converter.setOption('tables', true);
  converter.setOption('emoji', true);
  const props = defineProps({
    value: { type: String },
    class: { type: String },
  });
  const getHtmlData = computed(() => converter.makeHtml(props.value || ''));

  // update-begin--author:liaozhiyang---date:20231213---for：【issues/918】MarkdownViewer加上暗黑主题
  const isDarkTheme = ref(false);
  const { getDarkMode } = useRootSetting();
  watch(
    () => getDarkMode.value,
    (value) => {
      isDarkTheme.value = value === ThemeEnum.DARK;
    },
    { immediate: true }
  );
  // update-end--author:liaozhiyang---date:20231213---for：【issues/918】MarkdownViewer加上暗黑主题
</script>

<style lang="less" scoped>
  .markdown-viewer {
    width: 100%;
  }
  
  .preview {
    width: 100%;
  }
  .preview_dark {
    .vditor-reset {
      color: #d1d5da;
    }
    .vditor-reset a,
    .vditor-ir__link {
      color: #4285f4;
    }

    .vditor-reset h1,
    .vditor-reset h2 {
      padding-bottom: 0.3em;
      border-bottom: 1px solid #d1d5da;
    }
    .vditor-reset hr {
      background-color: #d1d5da;
    }
    .vditor-reset blockquote {
      padding: 0 1em;
      color: #b9b9b9;
      border-left: 0.25em solid #d1d5da;
    }
    .vditor-reset iframe {
      border: 1px solid #141414;
    }
    .vditor-reset table tr {
      background-color: #2f363d;
    }
    .vditor-reset table td,
    .vditor-reset table th {
      border: 1px solid #dfe2e5;
    }
    .vditor-reset table tbody tr:nth-child(2n) {
      background-color: #24292e;
    }
    .vditor-reset code:not(.hljs):not(.highlight-chroma) {
      background-color: rgba(66, 133, 244, 0.36);
    }
    .vditor-reset .language-abc svg,
    .vditor-reset .language-abc path {
      fill: currentColor;
      color: #d1d5da;
    }
    .language-graphviz polygon {
      fill: rgba(66, 133, 244, 0.36);
    }
    .vditor-reset kbd {
      color: #d1d5da;
      background-color: #2f363d;
      border: 1px solid #141414;
      box-shadow: inset 0 -1px 0 #141414;
    }
    .vditor-copy svg {
      color: #b9b9b9;
    }
    .vditor-speech {
      background-color: #1d2125;
      border: 1px solid #141414;
      color: #b9b9b9;
    }
    .vditor-speech--current,
    .vditor-speech:hover {
      color: #fff;
    }
    .vditor-linkcard a {
      background-color: #1d2125;
    }
    .vditor-linkcard a:visited .vditor-linkcard__abstract {
      color: hsla(0, 0%, 72.5%, 0.36);
    }
    .vditor-linkcard__title {
      color: #d1d5da;
    }
    .vditor-linkcard__abstract {
      color: #b9b9b9;
    }
    .vditor-linkcard__site {
      color: #fff;
    }
    .vditor-linkcard__image {
      background-color: hsla(0, 0%, 72.5%, 0.36);
    }
  }
</style>
