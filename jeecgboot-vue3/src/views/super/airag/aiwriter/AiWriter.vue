<template>
  <div class="writer-wrap">
    <div class="sidebar">
      <AiWriterLeft @generate="onGenerate" />
    </div>
    <div class="preview" ref="previewRef">
      <div class="preview-header">
        <div class="preview-header-left">
          <span class="preview-title-text">{{ isEditing ? '编辑' : '预览' }}</span>
          <a-select
            v-if="historyData && historyData.length"
            v-model:value="activeVersion"
            size="small"
            class="version-select"
            @change="handleVersionChange"
          >
            <a-select-option :value="CURRENT_VERSION_KEY">当前内容</a-select-option>
            <a-select-option v-for="item in historyData" :key="item.version" :value="item.version">
              {{ item.version }}
            </a-select-option>
          </a-select>
          <a-button
            v-if="historyData && historyData.length && activeVersion !== CURRENT_VERSION_KEY"
            type="link"
            size="small"
            class="preview-action-btn version-delete-btn"
            @click="handleDeleteVersion"
          >
            <DeleteOutlined />
            删除
          </a-button>
        </div>
        <div class="preview-actions">
          <a-button v-if="!generating" type="link" size="small" class="preview-action-btn custom-save-btn" @click="toggleEdit">
            <FormOutlined v-if="!isEditing" />
            <CheckOutlined v-else />
            {{ isEditing ? '完成' : '编辑' }}
          </a-button>
          <a-button
            v-if="!generating"
            type="link"
            size="small"
            class="preview-action-btn custom-save-btn"
            :loading="polishLoading"
            @click="handlePolish"
          >
            <ThunderboltOutlined class="preview-actions-icon" style="position: relative; top: 1px" />
            润色
          </a-button>
          <a-tooltip title="保存草稿">
            <a-button v-if="!generating" type="link" size="small" class="preview-action-btn custom-save-btn" :loading="saving" @click="handleSave">
              <SaveOutlined class="preview-actions-icon" />
              保存
            </a-button>
          </a-tooltip>
          <a-tooltip title="复制内容">
            <a-button type="link" size="small" class="preview-action-btn custom-save-btn" @click="handleCopy">
              <CopyOutlined class="preview-actions-icon" />
              复制
            </a-button>
          </a-tooltip>
        </div>
      </div>
      <div v-if="!isEditing" v-html="previewMd" class="markdown-container" @click="!generating" />
      <div v-else class="markdown-editor-container">
        <JMarkdownEditor v-model:value="writeText" height="100vh" :preview="{ mode: 'view', action: [] }" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, computed, nextTick, onMounted } from 'vue';
  import AiWriterLeft from './AiWriterLeft.vue';
  import { defHttp } from '@/utils/http/axios';
  import { CopyOutlined, ThunderboltOutlined, FormOutlined, CheckOutlined, SaveOutlined, DeleteOutlined } from '@ant-design/icons-vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { copyTextToClipboard } from '/@/hooks/web/useCopyToClipboard';
  import { Modal } from 'ant-design-vue';
  import MarkdownIt from 'markdown-it';
  import mdKatex from '@traptitech/markdown-it-katex';
  import mila from 'markdown-it-link-attributes';
  import hljs from 'highlight.js';
  import JMarkdownEditor from '/@/components/Form/src/jeecg/components/JMarkdownEditor.vue';
  import '/@/views/super/airag/aiapp/chat/style/github-markdown.less';
  import '/@/views/super/airag/aiapp/chat/style/highlight.less';
  import '/@/views/super/airag/aiapp/chat/style/style.less';

  const { createMessage } = useMessage();
  const writeText = ref<string>('');
  const previewRef = ref<HTMLElement | null>(null);
  //生成的loading
  const generating = ref<boolean>(false);
  //是否编辑
  const isEditing = ref<boolean>(false);
  //润色的loading
  const polishLoading = ref<boolean>(false);
  //保存loading
  const saving = ref<boolean>(false);
  //左侧的内容
  const leftData = ref<any>();
  //历史数据
  const historyData = ref<any>([]);
  //ai提示文本
  const aiText = ref<string>('');
  //当期版本的key
  const CURRENT_VERSION_KEY = 'CURRENT';
  //当前选中的本本
  const activeVersion = ref<string>(CURRENT_VERSION_KEY);
  //原始内容
  const originalContent = ref<string | null>(null);
  //第一个回复节点之后的内容
  const afterNodeFinished = ref<boolean>(false);
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
    let content = writeText.value || aiText.value;
    if (generating.value) {
      content +=
        ' <span class="typing-dot"></span><span class="typing-dot" style="animation-delay: 0.2s"></span><span class="typing-dot" style="animation-delay: 0.4s"></span>';
    }
    return mdi.render(content);
  });

  /**
   * 编辑
   */
  function toggleEdit() {
    if (generating.value) {
      return;
    }
    isEditing.value = !isEditing.value;
  }

  /**
   * 复制
   */
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
   * @param type
   */
  async function onGenerate(data, type = '') {
    isEditing.value = false;
    data.responseMode = 'streaming';
    writeText.value = '';
    generating.value = true;
    activeVersion.value = CURRENT_VERSION_KEY;
    if (!type) {
      leftData.value = data;
    }

    try {
      let readableStream = await defHttp
        .post(
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
        )
        .catch((res) => {
          createMessage.warn(res.message ? res.message : '请求出错，请稍后重试！');
        });

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
          if (line.startsWith('data:')) {
            const content = line.replace('data:', '').trim();
            if(!content){
              continue;
            }
            if(!content.endsWith('}')){
              buffer = buffer + line;
              continue;
            }
            buffer = "";
            renderText(content)
          } else {
            if(!line) {
              continue;
            }
            if(!line.endsWith('}')) {
              buffer = buffer + line;
              continue;
            }
            buffer = "";
            renderText(line)
          }
        }
      }
    } catch (e) {
      console.error('Generation error:', e);
      writeText.value += '\n\n[生成出错]';
    } finally {
      // 若服务端结束未触发 MESSAGE_END，兜底关闭生成状态
      generating.value = false;
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
      if(parse.event == 'NODE_FINISHED'){
        afterNodeFinished.value = true;
        return;
      }
      if (parse.event == 'MESSAGE') {
        aiText.value = '';
        if (afterNodeFinished.value) {
          writeText.value = parse.data.message;
          afterNodeFinished.value = false;
        } else {
          writeText.value += parse.data.message;
        }
        generating.value = true;
        polishLoading.value = true;
        nextTick(scrollToBottom);
      }
      if (parse.event == 'MESSAGE_END') {
        generating.value = false;
        if (activeVersion.value === CURRENT_VERSION_KEY) {
          originalContent.value = writeText.value;
        }
        nextTick(scrollToBottom);
      }
      if (parse.event == 'ERROR') {
        writeText.value = parse.data.message ? parse.data.message : '生成失败，请稍后重试！';
        generating.value = false;
        polishLoading.value = false;
        nextTick(scrollToBottom);
      }
      
      //开始加点
      if (parse.event === 'NODE_STARTED') {
        if (!parse.data || parse.data.type !== 'end') {
          if (parse.data.type === 'llm' || parse.data.type === 'reply') {
            aiText.value = '正在构建响应内容';
          }
        }
      }
      //流程结束节点
      if (parse.event == 'FLOW_FINISHED') {
        if (parse.data && !parse.data.success) {
          writeText.value = parse.data.message ? parse.data.message : '生成失败，请稍后重试！';
        }
        generating.value = false;
        polishLoading.value = false;
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


  /**
   * 润色
   */
  async function handlePolish() {
    if (!writeText.value) {
      createMessage.warning('暂无内容可润色');
      return;
    }
    if (generating.value || polishLoading.value) {
      return;
    }
    polishLoading.value = true;
    const data: any = {
      prompt: writeText.value,
      originalContent: '',
      length: leftData.value.length,
      format: leftData.value.format,
      tone: leftData.value.tone,
      language: leftData.value.language,
      activeMode: 'polish',
    };
    try {
      await onGenerate(data, 'polish');
    } finally {
      polishLoading.value = false;
    }
  }

  /**
   * 保存
   */
  async function handleSave() {
    if (!writeText.value) {
      createMessage.warning('暂无内容可保存');
      return;
    }
    if (saving.value) {
      return;
    }
    try {
      saving.value = true;
      await defHttp.post({ url: '/airag/app/save/article/write', params: { content: writeText.value } });
      if (activeVersion.value === CURRENT_VERSION_KEY) {
        originalContent.value = writeText.value;
      }
      initHistoryData();
    } catch (e) {
      createMessage.error('保存失败，请稍后重试');
    } finally {
      saving.value = false;
    }
  }

  /**
   * 初始化历史信息
   */
  function initHistoryData() {
    historyData.value = [];
    defHttp.get({ url: "/airag/app/list/article/write" }, { isTransformResponse: false }).then((res)=>{
      if(res.success){
        historyData.value = res.result;
      }
    })
  }

  /**
   * 版本删除
   */
  async function handleDeleteVersion() {
    if (activeVersion.value === CURRENT_VERSION_KEY) {
      return;
    }
    const target = historyData.value.find((item) => item.version === activeVersion.value);
    if (!target) {
      return;
    }
    Modal.confirm({
      title: '删除版本',
      content: '是否确认删除该版本？',
      okText: '确定',
      cancelText: '取消',
      async onOk() {
        try {
          await defHttp.delete(
            { url: '/airag/app/delete/article/write', params: { version: target.version } },
            { joinParamsToUrl: true }
          );
          historyData.value = historyData.value.filter((item) => item.version !== target.version);
          activeVersion.value = CURRENT_VERSION_KEY;
          writeText.value = originalContent.value ?? '';
        } catch (e) {
          createMessage.error('删除失败，请稍后重试');
        }
      },
    });
  }

  /**
   * 版本切换事件
   * @param value
   */
  function handleVersionChange(value: string) {
    if (value === CURRENT_VERSION_KEY) {
      activeVersion.value = value;
      writeText.value = originalContent.value ?? '';
      return;
    }
    const target = historyData.value.find((item) => item.version === value);
    if (!target) {
      return;
    }
    activeVersion.value = value;
    writeText.value = target.content;
  }
  
  onMounted(() => {
    //初始化的时候加载历史版本
    initHistoryData();
  });
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
  /*begin  头部样式 */
  .preview {
    flex: 1;
    border: 1px solid #eef0f5;
    border-radius: 12px;
    padding: 16px 20px;
    overflow: auto;
    background: #fafbff;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.03);
  }

  .preview-header {
    font-size: 14px;
    color: #344767;
    margin-bottom: 12px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #eef0f5;
    padding-bottom: 10px;
  }
  .preview-header-left {
    display: flex;
    align-items: center;
    gap: 12px;
  }

  .preview-title-text {
    font-weight: 500;
    letter-spacing: 0.5px;
  }

  .preview-actions {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .version-select {
    min-width: 120px;
  }
  
  .preview-action-btn {
    padding: 0 14px;
    font-size: 12px;
    border-radius: 4px;
    height: 26px;
    line-height: 24px;
  }

  .custom-green-btn {
    background-color: @primary-color;
    border-color: @primary-color;
    color: #ffffff;

    &:hover,
    &:focus {
      background-color: @primary-color;
      border-color: @primary-color;
      color: #ffffff;
    }
  }

  .preview-actions-icon {
    font-size: 12px;
    color: white;
  }

  .custom-save-btn, .version-delete-btn {
    background-color: #ffffff;
    border-color: @primary-color;
    color: @primary-color;

    &:hover,
    &:focus {
      background-color: fade(@primary-color, 10%);
      border-color: @primary-color;
      color: @primary-color;
    }

    .preview-actions-icon {
      color: @primary-color;
    }
  }
  /*end  头部样式 */

  /*begin 编辑器的样式*/
  .markdown-container {
    min-height: 300px;
    padding: 8px 4px 16px 4px;
    border-radius: 8px;
    background-color: #fff;
    cursor: text;
    :deep(img) {
      width: 40% !important;
      max-width: 280px;
      height: auto;
      border-radius: 6px;
      display: block;
    }
  }

  .markdown-editor-container {
    min-height: 300px;
  }

  :deep(.typing-dot) {
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
  /*end 编辑器的样式*/
</style>
