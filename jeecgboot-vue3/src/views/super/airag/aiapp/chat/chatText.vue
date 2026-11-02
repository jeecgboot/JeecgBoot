<template>
  <div v-if="parsedText != '' || error" class="textWrap" :class="[inversion === 'user' ? 'self' : (isOnlyImage ? 'chatgpt-image' : 'chatgpt')]" ref="textRef">
    <a-tooltip v-if="inversion !== 'user' && !isOnlyImage && !error && !loading && !hideCopyButton" :title="copied ? '已复制' : '复制 Markdown'">
      <a-button class="copyMarkdownBtn" type="text" size="small" :aria-label="copied ? '已复制' : '复制 Markdown'" @click="handleCopyMarkdown">
        <CheckOutlined v-if="copied" />
        <CopyOutlined v-else />
      </a-button>
    </a-tooltip>
    <div v-if="inversion != 'user'" :style="{ width: getIsMobile? screenWidth : 'auto' }">
      <div ref="markdownBodyRef" class="markdown-body" :class="{ 'markdown-body-generate': loading }" v-html="parsedText" />
      <template v-if="showRefKnow">
        <a-divider orientation="left">引用</a-divider>
        <template v-for="(item, idx) in referenceKnowledge" :key="idx">
          <a-tooltip
            :title="item.content?.substring(0, 800)"
            overlayClassName="knowledge-reference-tooltip"
            :overlayInnerStyle="knowledgeTooltipStyle"
          >
            <a-tag style="min-width: 80px;background: #F7F8FA;padding-inline: 0 7px">
              <a-space style="min-height: 30px;padding-left: 4px;padding-right: 4px;background-color: #F0F1F6;color: #788194">
                <div>{{ 'chunk-' + item.chunk}}</div>
              </a-space>
              <a-space style="min-height: 30px;padding-left: 4px;">
                <img :src="knowledgePng" width="14" height="14" style="position: relative; top: -2px"/>
                <div style="max-width: 240px; overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                  {{ item.docName }}
                </div>
              </a-space>
            </a-tag>
          </a-tooltip>
        </template>
      </template>
      <div v-if="error" class="error-message">
        <p>{{ errorMsg }}</p>
      </div>
    </div>
    <div v-else class="msg user-message-text">{{ props.text }}</div>
  </div>
  <ImageViewer v-if="amplifyImage" :imageUrl="imageUrl" @hide="pictureHide"></ImageViewer>
  <!-- 聊天中渲染JeecgTag -->
  <template v-if="jeecgTagList.length">
    <template v-for="item of jeecgTagList" :key="item.key">
      <teleport :to="item.to">
        <Component :is="item.tag.component" :data="item.data" :loading="loading" />
      </teleport>
    </template>
  </template>
</template>

<script setup lang="ts">
  import type { JeecgTag } from './jeecg-tags/types';
  import { computed, nextTick, onMounted, onUnmounted, onUpdated, ref, watch } from 'vue';
  import { throttle } from 'lodash-es';
  import md5 from 'crypto-js/md5';
  import MarkdownIt from 'markdown-it';
  import mdKatex from '@traptitech/markdown-it-katex';
  import mila from 'markdown-it-link-attributes';
  import hljs from 'highlight.js';
  import './style/github-markdown.less';
  import './style/highlight.less';
  import './style/style.less';
  import ImageViewer from '@/views/super/airag/aiapp/chat/components/ImageViewer.vue';
  import { useAppInject } from "@/hooks/web/useAppInject";
  import { useGlobSetting } from "@/hooks/setting";
  import { mdPluginJeecgTag, JEECG_TAG_CLASS, jeecgTagMap } from './jeecg-tags'
  import knowledgePng from '../../aiknowledge/icon/knowledge.png'
  import { sanitizeRichText } from '/@/utils/htmlSanitizer';
  import { CheckOutlined, CopyOutlined } from '@ant-design/icons-vue';

  /**
   * 屏幕宽度
   */
  const screenWidth = ref<string>();
  const { domainUrl } = useGlobSetting();
  const { getIsMobile } = useAppInject();

  //update-begin---author:wangshuai---date:20260827---for：【LHZP-1614】【AI】思考过程不显示工具调用---
  const props = defineProps(['dateTime', 'text', 'inversion', 'error', 'errorMsg', 'currentToolTag', 'loading', 'referenceKnowledge', 'isLast', 'hideCopyButton']);
  //update-end---author:wangshuai---date:20260827---for：【LHZP-1614】【AI】思考过程不显示工具调用---
  const textRef = ref();
  const markdownBodyRef = ref<HTMLDivElement>();
  const copied = ref(false);
  const knowledgeTooltipStyle = { maxHeight: '240px', overflowY: 'auto', scrollbarGutter: 'stable' };
  let copyResetTimer: ReturnType<typeof setTimeout> | undefined;

  //图片地址
  const imageUrl = ref<string>('');
  //是否放大图片
  const amplifyImage = ref<boolean>(false);

  const parsedText = ref<string>('');

  // 解析出来的 jeecgTag 列表
  const jeecgTagList = ref<{
    key: string;
    to: HTMLDivElement;
    tag: JeecgTag;
    data: string;
  }[]>([]);

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

  // 自定义 mdi 插件
  mdi.use(mdPluginJeecgTag);
  // 官方 mdi 插件
  mdi.use(mila, { attrs: { target: '_blank', rel: 'noopener' } });
  mdi.use(mdKatex, { blockClass: 'katexmath-block rounded-md p-[10px]', errorColor: ' #cc0000' });

  /**
   * 处理聊天文本并在一定时间内节流更新
   */
  const updateTextContent = throttle(() => {
    let value = props.text ?? '';
    if (props.inversion !== 'user') {
      // 先替换图片宽度与域名占位符后再渲染 markdown
      value = replaceImageWith(value);
      value = replaceDomainUrl(value);
      parsedText.value = sanitizeRichText(mdi.render(value));
      // 解析 jeecgTag 标签
      parseJeecgTag();
      return;
    }
    parsedText.value = value;
  }, 100);

  // 是否显示引用知识库
  const showRefKnow = computed(() => {
    const {loading, referenceKnowledge} = props
    if (loading) {
      return false;
    }
    return Array.isArray(referenceKnowledge) && referenceKnowledge.length > 0;
  })

  // 判断是否只有图片
  const isOnlyImage = computed(() => {
    if (showRefKnow.value){
      return false;
    }
    
    const content = props.text || '';
    if (!content){
      return false;
    }
    
    //匹配![图片1](url1) 
    const imageRegex = /!\[.*?\]\(.*?\)/g;
    if (!imageRegex.test(content)) {
      return false;
    }
    
    //替换之后是否存在文本
    const remaining = content.replace(imageRegex, '').trim();
    return remaining.length === 0;
  });

  // 监听文本变化，触发界面更新
  watch(() => props.text, () => updateTextContent(), {immediate: true});

  // 监听 当前调用的工具 变化，追加渲染内容
  watch(() => props.currentToolTag, () => {
    const {isLast, inversion, currentToolTag, loading} = props;
    if (isLast && inversion != 'user' && currentToolTag && loading) {
      parsedText.value += sanitizeRichText(mdi.render(currentToolTag));
      // 解析 jeecgTag 标签
      parseJeecgTag();
    }
  }, {immediate: true});

  //替换图片宽度
  function replaceImageWith(markdownContent) {
    //update-begin---author:wangshuai---date:2026-01-08---for: 兼容返回多张图片集图片默认宽度调整---
    // 1. 连续输出的多张图片优先使用九宫格，包括带width的文章配图
    const regexCustomWidth = /!\[([^\]]*)\]\(([^)]+)\s=([0-9]+)\)/g;
    const regexStandard = /!\[([^\]]*)\]\(([^)]+)\)/g;
    const regexContinuousImages = /(?:!\[[^\]]*\]\([^)]+\)[ \t]*(?:\r?\n[ \t]*)*){2,}/g;
    const renderImage = (alt: string, src: string, className: string) => {
      const reg = /#\s*{\s*domainURL\s*}/g;
      const imageSrc = src.replace(/\s=[0-9]+$/, '').replace(reg, domainUrl);
      return `<div class="${className}"><img src='${imageSrc}' alt='${alt}' /></div>`;
    };

    markdownContent = markdownContent.replace(regexContinuousImages, (imageGroup) => {
      return imageGroup.replace(regexStandard, (_match, alt, src) => renderImage(alt, src, 'chat-image-grid-item'));
    });

    // 2. 支持图片设置width的写法 ![](/static/jimuImages/screenshot_1617252560523.png =100)
    // 必须有空格，避免匹配到url参数中的=
    markdownContent = markdownContent.replace(regexCustomWidth, (_match, alt, src, width) => {
      const reg = /#\s*{\s*domainURL\s*}/g;
      const imageSrc = src.replace(reg, domainUrl);
      return `<div class="chat-image-custom"><img src='${imageSrc}' alt='${alt}' width='${width}' /></div>`;
    });

    // 3. 文章中不连续的普通图片保持单图展示
    markdownContent = markdownContent.replace(regexStandard, (_match, alt, src) => renderImage(alt, src, 'chat-image-single'));
    return markdownContent;
    //update-end---author:wangshuai---date:2026-01-08---for: 兼容返回多张图片集图片默认宽度调整---
  };

  //替换domainURL
  function replaceDomainUrl(markdownContent) {
    const regex = /!\[([^\]]*)\]\(.*?#\s*{\s*domainURL\s*}.*?\)/g;
    return markdownContent.replace(regex, (match) => {
      let reg = /#\s*{\s*domainURL\s*}/g;
      return match.replace(reg,domainUrl);
    })
  }

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


  /**
   * 添加图片点击事件
   */
  function addImageClickEvent() {
    if (textRef.value) {
      const image = textRef.value.querySelectorAll('img');
      image.forEach((img) => {
        img.addEventListener('click', () => {
          imageUrl.value = img.src;
          amplifyImage.value = true;
        })
      });
    }
  }


  /**
   * 移出图片点击事件
   */
  function removeImageClickEvent(){
    if (textRef.value) {
      const image = textRef.value.querySelectorAll('img');
      image.forEach((img) => {
        img.removeEventListener('click', () => { })
      });
    }
  }

  /**
   * 图片隐藏
   */
  function pictureHide(){
    amplifyImage.value = false;
    imageUrl.value = ""
  }


  /**
   * 设置markdown body整体宽度
   */
  function setMarkdownBodyWidth() {
    //平板
    console.log("window.innerWidth::",window.innerWidth)
    if(window.innerWidth>600 && window.innerWidth<1024){
      screenWidth.value = window.innerWidth - 120 + 'px';
    }else if(window.innerWidth < 600){
      //手机
      screenWidth.value = window.innerWidth - 60 + 'px';
    }
  }

  // 解析JeecgTag标签
  async function parseJeecgTag() {
    await nextTick();
    if (!markdownBodyRef.value) {
      return;
    }

    jeecgTagList.value = [];
    const elements = markdownBodyRef.value.querySelectorAll('.' + JEECG_TAG_CLASS);
    elements.forEach((el) => {
      const tagName = el.nodeName.toLowerCase();
      const tag = jeecgTagMap.get(tagName);
      if (!tag) {
        console.warn(`未识别的 jeecg 标签:`, tagName, el);
        return;
      }
      const renderEl = el.querySelector('render') as HTMLDivElement;
      if (!renderEl) {
        if (props.loading) {
          // 渲染中
          el.innerHTML = `<div style="color: #888; margin-top: 12px;">图表渲染中，请稍候...</div>`;
          return;
        }
        el.innerHTML = `<div style="color: red;">模型返回的图表渲染格式不正确，请优化提示词或重新尝试。</div>`;
        return;
      }

      const dataEl = el.querySelector('data');
      const dataStr = dataEl?.textContent || '';
      renderJeecgTag(tag, dataStr, renderEl);
    });
  }

  /**
   * 提交渲染 jeecg 标签
   */
  function renderJeecgTag(tag: JeecgTag, data: string, renderEl: HTMLDivElement) {
    jeecgTagList.value.push({
      key: md5(tag.name + '_' + data).toString(),
      to: renderEl,
      tag: tag,
      data: data,
    });
  }

  /**
   * 复制最终回答的原始 Markdown 内容
   */
  async function handleCopyMarkdown() {
    const markdown = props.text ?? '';
    if (!markdown) {
      return;
    }
    await copyToClip(markdown);
    copied.value = true;
    clearTimeout(copyResetTimer);
    copyResetTimer = setTimeout(() => {
      copied.value = false;
    }, 1500);
  }

  onMounted(() => {
    addCopyEvents();
    addImageClickEvent();
    setMarkdownBodyWidth();
    window.addEventListener('resize', setMarkdownBodyWidth);
  });

  onUpdated(() => {
    addCopyEvents();
    addImageClickEvent();
  });

  onUnmounted(() => {
    removeCopyEvents();
    removeImageClickEvent();
    window.removeEventListener('resize', setMarkdownBodyWidth);
    clearTimeout(copyResetTimer);
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
    box-sizing: border-box;
    min-width: 0;
    max-width: 100%;
    border-radius: 0.375rem;
    padding-top: 0.5rem;
    padding-bottom: 0.5rem;
    padding-left: 0.75rem;
    padding-right: 0.75rem;
    font-size: 0.875rem;
    line-height: 1.25rem;
    position: relative;
  }

  .copyMarkdownBtn {
    position: absolute;
    top: 8px;
    right: 8px;
    z-index: 1;
    width: 30px;
    height: 30px;
    padding: 0;
    border-radius: 8px;
    background: rgba(255, 255, 255, 0.82);
    color: #8b95a5;
    opacity: 0.72;
    transition: color 0.2s ease, background-color 0.2s ease, opacity 0.2s ease;

    &:hover,
    &:focus-visible {
      background: #fff;
      color: @primary-color;
      opacity: 1;
    }
  }

  .error {
    background: linear-gradient(135deg, #FF4444, #FF914D) !important;
    border-radius: 0.375rem;
    padding-top: 0.5rem;
    padding-bottom: 0.5rem;
    padding-left: 0.75rem;
    padding-right: 0.75rem;
    font-size: 0.875rem;
    line-height: 1.25rem;
  }

  .error-message {
    color: #FF4444 !important
  }

  .self {
    // background-color: #d2f9d1;
    background-color: @primary-color;
    color: #fff;
    overflow-wrap: break-word;
    line-height: 1.625;
    min-width: 20px;
  }
  .user-message-text {
    white-space: pre-wrap;
  }
  .chatgpt {
    background-color: #f4f6f8;

    font-size: 0.875rem;
    line-height: 1.25rem;

    :deep(.markdown-body) {
      box-sizing: border-box;
      max-width: 100%;
      overflow-wrap: anywhere;
      padding-right: 48px;
    }
  }
  .chatgpt-image {
    .markdown-body{
      background-color: transparent !important;
    }
  }
  @media (max-width: 1024px) {
    //手机和平板下的样式
    .textWrap{
      margin-left: -40px;
      margin-top: 10px;
    }
  }

  // 生成图片的样式
  :deep(.chat-image-grid-item) {
    display: inline-block;
    width: 24%;
    padding: 4px;
    box-sizing: border-box;
    vertical-align: top;
    img {
      width: 100%;
      height: auto;
      border-radius: 4px;
      cursor: pointer;
      object-fit: cover;
      aspect-ratio: 1/1;
    }
  }
  :deep(.chat-image-single) {
    img {
      max-width: 50%;
      border-radius: 4px;
      cursor: pointer;
    }
  }

  .markdown-body {
    :deep(.jeecg-tag) {
      display: block;
      margin: 8px 0;

      data {
        display: none;
      }

      render {
        display: block;
        width: 100%;
        height: auto;
        min-width: 300px;
      }
    }
  }

  :global(.knowledge-reference-tooltip .ant-tooltip-inner) {
    scrollbar-color: #aeb7c5 #4a4a4a;
    scrollbar-width: auto;
  }

  :global(.knowledge-reference-tooltip .ant-tooltip-inner::-webkit-scrollbar) {
    width: 10px;
  }

  :global(.knowledge-reference-tooltip .ant-tooltip-inner::-webkit-scrollbar-track) {
    background: #4a4a4a;
    border-radius: 5px;
  }

  :global(.knowledge-reference-tooltip .ant-tooltip-inner::-webkit-scrollbar-thumb) {
    background: #aeb7c5;
    border: 2px solid #4a4a4a;
    border-radius: 5px;
  }

  :global(.knowledge-reference-tooltip .ant-tooltip-inner::-webkit-scrollbar-thumb:hover) {
    background: #d7dce3;
  }

</style>
