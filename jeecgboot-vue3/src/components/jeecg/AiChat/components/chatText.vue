<template>
  <div class="textWrap" :class="[inversion ? 'self' : 'chatgpt']" ref="textRef">
    <div v-if="!inversion">
      <div class="markdown-body" :class="{ 'markdown-body-generate': loading }" v-html="text" />
    </div>
    <div v-else class="msg" v-text="text" />
  </div>
</template>

<script setup lang="ts">
  import { computed, onMounted, onUnmounted, onUpdated, ref } from 'vue';
  import MarkdownIt from 'markdown-it';
  import mdKatex from '@traptitech/markdown-it-katex';
  import mila from 'markdown-it-link-attributes';
  import hljs from 'highlight.js';

  const props = defineProps(['dateTime', 'text', 'inversion', 'error', 'loading']);
  const textRef = ref();
  const mdi = new MarkdownIt({
    html: false,
    linkify: true,
    highlight(code, language) {
      const validLang = !!(language && hljs.getLanguage(language));
      if (validLang) {
        const lang = language ?? '';
        return highlightBlock(hljs.highlight(code, { language: lang }).value, lang);
      }
      return highlightBlock(hljs.highlightAuto(code).value, '');
    },
  });

  mdi.use(mila, { attrs: { target: '_blank', rel: 'noopener' } });
  mdi.use(mdKatex, { blockClass: 'katexmath-block rounded-md p-[10px]', errorColor: ' #cc0000' });

  const text = computed(() => {
    const value = props.text ?? '';
    if (!props.inversion) return mdi.render(value);
    return value;
  });

  function highlightBlock(str: string, lang?: string) {
    return `<pre class="code-block-wrapper"><div class="code-block-header"><span class="code-block-header__lang">${lang}</span><span class="code-block-header__copy">复制代码</span></div><code class="hljs code-block-body ${lang}">${str}</code></pre>`;
  }
  function addCopyEvents() {
    if (textRef.value) {
      const copyBtn = textRef.value.querySelectorAll('.code-block-header__copy');
      copyBtn.forEach((btn) => {
        btn.addEventListener('click', () => {
          const code = btn.parentElement?.nextElementSibling?.textContent;
          if (code) {
            copyToClip(code).then(() => {
              btn.textContent = '复制成功';
              setTimeout(() => {
                btn.textContent = '复制代码';
              }, 1e3);
            });
          }
        });
      });
    }
  }

  function removeCopyEvents() {
    if (textRef.value) {
      const copyBtn = textRef.value.querySelectorAll('.code-block-header__copy');
      copyBtn.forEach((btn) => {
        btn.removeEventListener('click', () => {});
      });
    }
  }

  onMounted(() => {
    addCopyEvents();
  });

  onUpdated(() => {
    addCopyEvents();
  });

  onUnmounted(() => {
    removeCopyEvents();
  });

  function copyToClip(text: string) {
    return new Promise((resolve, reject) => {
      try {
        const input: HTMLTextAreaElement = document.createElement('textarea');
        input.setAttribute('readonly', 'readonly');
        input.value = text;
        document.body.appendChild(input);
        input.select();
        if (document.execCommand('copy')) document.execCommand('copy');
        document.body.removeChild(input);
        resolve(text);
      } catch (error) {
        reject(error);
      }
    });
  }
</script>
<style lang="less" scoped>
  .textWrap {
    border-radius: 0.375rem;
    padding-top: 0.5rem;
    padding-bottom: 0.5rem;
    padding-left: 0.75rem;
    padding-right: 0.75rem;
    font-size: 0.875rem;
    line-height: 1.25rem;
  }
  .self {
    // background-color: #d2f9d1;
    background-color: @primary-color;
    color: #fff;
    overflow-wrap: break-word;
    line-height: 1.625;
    min-width: 20px;
  }
  .chatgpt {
    background-color: #f4f6f8;

    font-size: 0.875rem;
    line-height: 1.25rem;
  }
</style>
