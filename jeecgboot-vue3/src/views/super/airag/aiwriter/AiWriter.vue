<template>
  <div class="writer-wrap">
    <div class="sidebar">
      <AiWriterLeft @generate="onGenerate" />
    </div>
    <div class="preview" ref="previewRef">
      <div class="preview-header">
        <span>预览</span>
        <a-tooltip title="复制内容">
          <CopyOutlined class="copy-btn" @click="handleCopy" />
        </a-tooltip>
      </div>
      <a-spin :spinning="writerLoading">
        <div v-html="previewMd" class="markdown-container" />
      </a-spin>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, computed, nextTick } from 'vue';
  import AiWriterLeft from './AiWriterLeft.vue';
  import { defHttp } from '@/utils/http/axios';
  import { CopyOutlined } from '@ant-design/icons-vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { copyTextToClipboard } from '/@/hooks/web/useCopyToClipboard';
  import MarkdownIt from 'markdown-it';
  import mdKatex from '@traptitech/markdown-it-katex';
  import mila from 'markdown-it-link-attributes';
  import hljs from 'highlight.js';
  import '/@/views/super/airag/aiapp/chat/style/github-markdown.less';
  import '/@/views/super/airag/aiapp/chat/style/highlight.less';
  import '/@/views/super/airag/aiapp/chat/style/style.less';

  const { createMessage } = useMessage();
  //加载
  const writerLoading = ref<boolean>(false);
  const writeText = ref<string>('');
  const previewRef = ref<HTMLElement | null>(null);
  const generating = ref<boolean>(false);
  const mdi = new MarkdownIt({
    html: true,
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

  //返回文本（生成中在末尾追加打点）
  const previewMd = computed(() => {
    const html = mdi.render(writeText.value);
    if (generating.value) {
      return html + '<span class=\"typing-dot\"></span><span class=\"typing-dot\"></span><span class=\"typing-dot\"></span>';
    }
    return html;
  });

  function handleCopy() {
    if (!writeText.value) {
      createMessage.warning('暂无内容可复制');
      return;
    }
    const success = copyTextToClipboard(writeText.value);
    if (success) {
      createMessage.success('复制成功');
    } else {
      createMessage.error('复制失败');
    }
  }

  function scrollToBottom() {
    const el = previewRef.value;
    if (!el) return;
    el.scrollTop = el.scrollHeight;
  }

  /**
   * 生成
   *
   * @param data
   */
  async function onGenerate(data) {
    data.responseMode = 'streaming';
    writerLoading.value = true;
    writeText.value = '';
    generating.value = true;

    try {
      let readableStream = await defHttp.post(
        {
          url: '/airag/chat/genAiWriter',
          params: { ...data },
          timeout: 5 * 60 * 1000,
          adapter: 'fetch',
          responseType: 'stream',
        },
        {
          isTransformResponse: false,
        }
      );

      const reader = readableStream.getReader();
      const decoder = new TextDecoder('UTF-8');
      let buffer = '';

      while (true) {
        const { done, value } = await reader.read();
        if (done) {
          break;
        }
        let chunk = decoder.decode(value, { stream: true });
        buffer += chunk;

        // 处理可能被截断的消息
        const lines = buffer.split('\n\n');
        // 保留最后一个片段（可能不完整）
        buffer = lines.pop() || '';

        for (const line of lines) {
          const trimmedLine = line.trim();
          if (!trimmedLine) continue;

          if (trimmedLine.startsWith('data:')) {
            const content = trimmedLine.replace('data:', '').trim();
            if (content) {
              renderText(content);
            }
          } else {
            // 尝试直接解析（兼容非SSE格式或异常格式）
            renderText(trimmedLine);
          }
        }
      }

      // 处理剩余的buffer
      if (buffer && buffer.trim()) {
        const trimmedLine = buffer.trim();
        if (trimmedLine.startsWith('data:')) {
          renderText(trimmedLine.replace('data:', '').trim());
        } else {
          renderText(trimmedLine);
        }
      }
    } catch (e) {
      console.error('Generation error:', e);
      writeText.value += '\n\n[生成出错]';
    } finally {
      writerLoading.value = false;
      // 若服务端结束未触发 MESSAGE_END，兜底关闭生成状态
      generating.value = false;
      nextTick(scrollToBottom);
    }
  }

  /**
   * 渲染文本
   *
   * @param item
   */
  function renderText(item) {
    try {
      let parse = JSON.parse(item);
      if (parse.event == 'MESSAGE') {
        writeText.value += parse.data.message;
        if (writerLoading.value) {
          writerLoading.value = false;
        }
        generating.value = true;
        nextTick(scrollToBottom);
      }
      if (parse.event == 'MESSAGE_END') {
        writerLoading.value = false;
        generating.value = false;
        nextTick(scrollToBottom);
      }
      if (parse.event == 'ERROR') {
        writeText.value = parse.data.message ? parse.data.message : '生成失败，请稍后重试！';
        writerLoading.value = false;
        generating.value = false;
        nextTick(scrollToBottom);
      }
    } catch (error) {
      console.log('Error parsing update:', error);
    }
  }

  /**
   * 代码块
   * @param str
   * @param lang
   */
  function highlightBlock(str: string, lang?: string) {
    return `<pre class="code-block-wrapper"><div class="code-block-header"><span class="code-block-header__lang">${lang}</span><span class="code-block-header__copy">复制代码</span></div><code class="hljs code-block-body ${lang}">${str}</code></pre>`;
  }
</script>

<style scoped lang="less">
  .writer-wrap {
    display: flex;
    height: 100%;
    padding: 16px;
    gap: 16px;
    background: #fff;
  }

  .sidebar {
    width: 340px;
    flex-shrink: 0;
    border: 1px solid #eef0f5;
    border-radius: 12px;
    padding: 16px;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.03);
    background: #fff;
    overflow: visible;
    position: sticky;
    top: 16px;
  }

  .preview {
    flex: 1;
    border: 1px solid #eef0f5;
    border-radius: 12px;
    padding: 12px 16px;
    overflow: auto;
  }

  .preview-header {
    font-size: 14px;
    color: #344767;
    margin-bottom: 12px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #eef0f5;
    padding-bottom: 8px;
  }

  .copy-btn {
    cursor: pointer;
    color: #666;
    font-size: 16px;
    transition: color 0.3s;

    &:hover {
      color: #1890ff;
    }
  }

  .markdown-container {
    min-height: 300px;
    /* 缩小图片宽度 */
    :deep(img) {
      width: 60% !important;
      height: auto;
      border-radius: 6px;
      display: inline-block;
    }
  }

  .typing-dot {
    display: inline-block;
    width: 6px;
    height: 6px;
    margin-left: 3px;
    border-radius: 50%;
    background-color: #999;
    vertical-align: baseline;
    animation: typing-dot 1.2s infinite ease-in-out;
  }

  @keyframes typing-dot {
    0%,
    80%,
    100% {
      transform: scale(0.6);
      opacity: 0.6;
    }
    40% {
      transform: scale(1);
      opacity: 1;
    }
  }
</style>
