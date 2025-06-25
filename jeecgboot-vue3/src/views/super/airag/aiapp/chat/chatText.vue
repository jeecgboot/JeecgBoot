<template>
  <div v-if="text != ''" class="textWrap" :class="[inversion === 'user' ? 'self' : 'chatgpt']" ref="textRef">
    <div v-if="inversion != 'user'" :style="{ width: getIsMobile? screenWidth : 'auto' }">
      <div class="markdown-body" :class="{ 'markdown-body-generate': loading }" :style="{color:error?'#FF4444 !important':''}" v-html="text" />
      <template v-if="showRefKnow">
        <a-divider orientation="left">引用</a-divider>
        <template v-for="(item, idx) of referenceKnowledge" :key="idx">
          <a-tooltip :title="item.substring(0, 800)">
            <a-tag >
              <a-space>
                <img :src="knowledgePng" width="16" height="16"/>
                <div style="max-width: 240px; overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                  {{ item }}
                </div>
              </a-space>
            </a-tag>
          </a-tooltip>
        </template>
      </template>
    </div>
    <div v-else class="msg" v-html="text" />
  </div>
  <ImageViewer v-if="amplifyImage" :imageUrl="imageUrl" @hide="pictureHide"></ImageViewer>
</template>

<script setup lang="ts">
  import { computed, onMounted, onUnmounted, onUpdated, ref } from 'vue';
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
  import knowledgePng from '../../aiknowledge/icon/knowledge.png'

  /**
   * 屏幕宽度
   */
  const screenWidth = ref<string>();
  const { getIsMobile } = useAppInject();

  const props = defineProps(['dateTime', 'text', 'inversion', 'error', 'loading', 'referenceKnowledge']);
  const textRef = ref();
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

  const text = computed(() => {
    let value = props.text ?? '';
    if (props.inversion != 'user'){
      value = replaceImageWith(value);
      value = replaceDomainUrl(value);
      return mdi.render(value);
    }
    return value.replace("\n","<br>");
  });

  // 是否显示引用知识库
  const showRefKnow = computed(() => {
    const {loading, referenceKnowledge} = props
    if (loading) {
      return false;
    }
    return Array.isArray(referenceKnowledge) && referenceKnowledge.length > 0;
  })

  //替换图片宽度
  const replaceImageWith = markdownContent => {
    // 支持图片设置width的写法 ![](/static/jimuImages/screenshot_1617252560523.png =100)
    const regex = /!\[([^\]]*)\]\(([^)]+)=([0-9]+)\)/g;
    return markdownContent.replace(regex, (match, alt, src, width) => {
      let reg = /#\s*{\s*domainURL\s*}/g;
      src = src.replace(reg,domainUrl);
      return `<div><img src='${src}' alt='${alt}' width='${width}' /></div>`;
    });
  };
  const { domainUrl } = useGlobSetting();
  //替换domainURL
  const replaceDomainUrl = markdownContent => {
    const regex = /!\[([^\]]*)\]\(.*?#\s*{\s*domainURL\s*}.*?\)/g;
    return markdownContent.replace(regex, (match) => {
      let reg = /#\s*{\s*domainURL\s*}/g;
      return match.replace(reg,domainUrl);
    })
  }

  //是否放大图片
  const amplifyImage = ref<boolean>(false);
  //图片地址
  const imageUrl = ref<string>('');

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
  @media (max-width: 1024px) {
    //手机和平板下的样式
    .textWrap{
      margin-left: -40px;
      margin-top: 10px;
    }
  }
</style>
