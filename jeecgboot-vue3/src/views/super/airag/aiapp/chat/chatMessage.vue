<template>
  <div class="chat" :class="[inversion === 'user' ? 'self' : 'chatgpt']" v-if="getText || hasFlowOutputSegments || (images && images.length > 0) || props.error || (props.presetQuestion && props.presetQuestion.length>0)">
    <div class="avatar" v-if="showAvatar !== 'no'">
      <img v-if="inversion === 'user'" :src="avatar()" />
      <img v-else :src="getAiImg()" />
    </div>
    <div class="content">
      <p class="date" v-if="showAvatar !== 'no'">
        <span v-if="inversion === 'ai'" style="margin-right: 10px">{{appData.name || 'AI助手'}}</span>
        <span>{{ dateTime }}</span>
      </p>
      <div v-if="images && images.length>0" class="images">
          <div v-for="(item,index) in images" :key="index" class="image" @click="handlePreview(item)">
            <img :src="getImageUrl(item)"/>
          </div>
      </div>     
      <div v-if="hasFlowOutputSegments" class="flow-output-segments">
        <div class="flow-image-actions">
          <a-tooltip :title="flowCopied ? '已复制' : '复制 Markdown'">
            <a-button type="text" size="small" :preIcon="flowCopied ? 'ant-design:check-outlined' : 'ant-design:copy-outlined'" @click="copyFlowMarkdown" />
          </a-tooltip>
          <a-button v-if="flowImageUrls.length > 1" size="small" preIcon="ant-design:download-outlined" @click="downloadAllFlowImages">全部下载</a-button>
        </div>
        <template v-for="(segment, index) in flowOutputSegments" :key="index">
          <div v-if="segment.type === 'image'" class="flow-image-item">
            <div class="image" @click="handleFlowPreview(segment.value)">
              <img :src="getImageUrl(segment.value)" />
            </div>
            <a-button size="small" preIcon="ant-design:download-outlined" @click="downloadFlowImage(segment.value)">下载</a-button>
          </div>
          <chatText
            v-else-if="segment.value"
            :text="segment.value"
            :inversion="inversion"
            :error="error"
            :errorMsg="errorMsg"
            :currentToolTag="currentToolTag"
            :loading="false"
            :referenceKnowledge="[]"
            :isLast="isLast"
          />
        </template>
      </div>
      <div v-if="inversion === 'user' && files && files.length>0" class="file-list">
          <div v-for="(item,index) in files" :key="index" class="file-item" @click="handleFilePreview(item?.filePath || item)">
            <div class="file-icon">
              <Icon :icon="getFileIcon(item?.filePath || item)" :color="getFileIconColor(item?.filePath || item)" size="24" />
            </div>
            <div class="file-name" :title="item.name">{{ getFileName(item?.filePath || item)}}</div>
          </div>
      </div>
      <div v-if="inversion === 'ai' && retrievalText && loading" class="retrieval">
        {{retrievalText}}
      </div>
      <div v-if="inversion === 'ai' && isCard" class="card">
        <a-row>
          <a-col :xl="6" :lg="8" :md="10" :sm="24" style="flex:1" v-for="item in getCardList()">
            <a-card class="ai-card" @click="aiCardHandleClick(item.linkUrl)">
               <div class="ai-card-title">{{item.productName}}</div>
               <div class="ai-card-img">
                 <img :src="item.productImage">
               </div>
               <span class="ai-card-desc">{{item.descr}}</span>
            </a-card>
          </a-col>
        </a-row>
      </div>
      <div v-if="inversion === 'ai' && isCardConfig" class="card">
        <a-row>
          <a-col :xl="6" :lg="8" :md="10" :sm="24" style="flex:1;margin-right: 10px;" v-for="item in getCardConfigList()">
            <CardTemplate :template-id="cardConfig?.templateId" :card-data="item" :card-config="cardConfig" @click="handleJumpClick(item)"></CardTemplate>
          </a-col>
        </a-row>
      </div>
      <div class="thinkArea" style="margin-bottom: 10px" v-if="!hasFlowOutputSegments && !isCard && !isCardConfig && isThinkingEvent">
        <a-collapse v-model:activeKey="activeKey" ghost>
          <a-collapse-panel :key="uuid" :header="loading?'正在思考中':'思考结束'">
            <ThinkText :text="thinkingText" :inversion="inversion" :error="error" :loading="loading"></ThinkText>
            <!--update-begin---author:wangshuai---date:20260827---for：【LHZP-1614】【AI】思考过程不显示工具调用-->
            <chatText
              v-if="thinkingToolTags || (isLast && currentToolTag)"
              :text="thinkingToolTags"
              :inversion="inversion"
              :currentToolTag="currentToolTag"
              :loading="loading"
              :isLast="isLast"
              :hideCopyButton="true"
            />
            <!--update-end---author:wangshuai---date:20260827---for：【LHZP-1614】【AI】思考过程不显示工具调用-->
          </a-collapse-panel>
        </a-collapse>
      </div>
      <div class="msgArea" v-else-if="!hasFlowOutputSegments && !isCard && !isCardConfig" :class="showAvatar == 'no' ? 'hidden-avatar' : ''">
        <chatText :text="text" :inversion="inversion" :error="error" :errorMsg="errorMsg" :currentToolTag="currentToolTag" :loading="loading" :referenceKnowledge="referenceKnowledge" :isLast="isLast"></chatText>
      </div>
      <div v-if="presetQuestion" v-for="item in presetQuestion" class="question" @click="presetQuestionClick(item.descr)">
        <span>{{item.descr}}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import chatText from './chatText.vue';
  import ThinkText from './ThinkText.vue';
  import defaultAvatar from "@/assets/images/ai/avatar.jpg";
  import { useUserStore } from '/@/store/modules/user';
  import defaultImg from '../img/ailogo.png';
  import { ref } from 'vue';
  import { buildUUID } from '/@/utils/uuid';
  import { getFileAccessHttpUrl, getFileIcon, getFileIconColor } from '/@/utils/common/compUtils';
  import { createImgPreview } from "@/components/Preview";
  import { computed } from "vue";
  import CardTemplate from '/@/views/super/airag/aiapp/chat/components/CardTemplate.vue';
  import { useGlobSetting } from "@/hooks/setting";
  import {encryptByBase64} from "@/utils/cipher";
  import { downloadByUrl } from '/@/utils/file/download';
  import { copyTextToClipboard } from '/@/hooks/web/useCopyToClipboard';

  const { domainUrl, viewUrl } = useGlobSetting();
  //update-begin---author:wangshuai---date:20260827---for：【LHZP-1614】【AI】思考过程不显示工具调用---
  const props = defineProps(['dateTime', 'text', 'inversion', 'error', 'loading','errorMsg', 'currentToolTag', 'thinkingToolTags', 'appData','presetQuestion','images','retrievalText', 'referenceKnowledge', 'eventType', 'showAvatar',"files", 'isLast', 'flowOutputSegments']);
  //update-end---author:wangshuai---date:20260827---for：【LHZP-1614】【AI】思考过程不显示工具调用---

  const uuid = ref<any>(buildUUID());
  const activeKey = ref<any>(uuid.value);
  // update-begin--author:liaozhiyang---date:20260831---for:【LHZP-2020】新增图像生成节点（维护流程图文结果和复制状态）
  const flowCopied = ref(false);
  let flowCopyTimer: ReturnType<typeof setTimeout> | undefined;
  const isThinkingEvent = computed(() => props.eventType === 'thinking' || props.eventType === 'thinking_end');
  const hasFlowOutputSegments = computed(() => Array.isArray(props.flowOutputSegments) && props.flowOutputSegments.length > 0);
  const flowImageUrls = computed(() =>
    (props.flowOutputSegments || []).filter((segment) => segment.type === 'image').map((segment) => getImageUrl(segment.value))
  );
  // update-end--author:liaozhiyang---date:20260831---for:【LHZP-2020】新增图像生成节点（维护流程图文结果和复制状态）
  const thinkingText = computed(() => {
    const text = props.text ?? '';
    // update-begin--author:wangshuai---date:20260813---for：【LHZP-1615】这种程序底层语言不应该不返回显示把？
    if (typeof text !== 'string') {
      return text;
    }
    return text
      .replace(/<jeecg-tool-exec\b[^>]*>[\s\S]*?<\/jeecg-tool-exec>/gi, '')
      .split('\n')
      .filter((line) => !isToolExecPayload(line))
      .join('\n')
      .trim();
    // update-end--author:wangshuai---date:20260813---for：【LHZP-1615】这种程序底层语言不应该不返回显示把？
  });

  /**
   * 判断思考文本中的单行 JSON 是否为工具执行记录。
   */
  function isToolExecPayload(text: string): boolean {
    const value = text.trim();
    if (!value.startsWith('{') || !value.endsWith('}')) {
      return false;
    }
    try {
      const record = JSON.parse(value);
      return (
        !!record &&
        typeof record === 'object' &&
        !Array.isArray(record) &&
        typeof record.name === 'string' &&
        Object.prototype.hasOwnProperty.call(record, 'input') &&
        Object.prototype.hasOwnProperty.call(record, 'output') &&
        Object.prototype.hasOwnProperty.call(record, 'loading')
      );
    } catch (error) {
      return false;
    }
  }

  const getText = computed(()=>{
    let text = isThinkingEvent.value ? thinkingText.value : props.text || props.retrievalText;
    if(text){
      text = text.trim();
    }
    return text;
  })

  const isCard = computed(() => {
    let text = props.text;
    if (text && text.indexOf('::card::') != -1) {
      return true;
    }
    return false;
  });  
  
  const isCardConfig = computed(() => {
    let text = props.text;
    if (text && text.indexOf('::cardConfig::') != -1) {
      return true;
    }
    return false;
  });
  //卡片配置
  const cardConfig = ref<any>();

  const { userInfo } = useUserStore();
  const avatar = () => {
    return getFileAccessHttpUrl(userInfo?.avatar) || defaultAvatar;
  };
  const emit = defineEmits(['send']);
  const getAiImg = () => {
    return getFileAccessHttpUrl(props.appData?.icon) || defaultImg;
  };

  /**
   * 预设问题点击事件
   *
   */
  function presetQuestionClick(descr) {
    emit("send",descr)
  }

  /**
   * 获取图片
   *
   * @param item
   */
  function getImageUrl(item) {
    let url = item;
    if (typeof item === 'string' && /^(data:|blob:)/i.test(item)) {
      return item;
    }
    if(item && typeof item === 'object' && item.hasOwnProperty('url')){
      url = item.url;
    }
    if(item && typeof item === 'object' && item.hasOwnProperty('base64Data') && item.base64Data){
      let mimeType = item.mimeType ? item.mimeType:'image/png';
      return "data:"+ mimeType +";base64,"+ item.base64Data;
    }
    return getFileAccessHttpUrl(url);
  }

  /**
   * 图片预览
   * @param url
   */
  function handlePreview(url){
    const onImgLoad = ({ index, url, dom }) => {
      console.log(`第${index + 1}张图片已加载，URL为：${url}`, dom);
    };
    let imageList = [getImageUrl(url)];
    createImgPreview({ imageList: imageList, defaultWidth: 700, rememberState: true, onImgLoad });
  }

  // update-begin--author:liaozhiyang---date:20260831---for:【LHZP-2020】新增图像生成节点（支持图片整组预览、单张下载、全部下载和整体复制）
  function handleFlowPreview(url) {
    const imageUrl = getImageUrl(url);
    createImgPreview({ imageList: flowImageUrls.value, index: Math.max(flowImageUrls.value.indexOf(imageUrl), 0), defaultWidth: 700, rememberState: true });
  }

  function downloadFlowImage(url) {
    const imageUrl = getImageUrl(url);
    downloadByUrl({ url: imageUrl, fileName: getImageDownloadName(imageUrl) });
  }

  function downloadAllFlowImages() {
    flowImageUrls.value.forEach((url) => downloadByUrl({ url, fileName: getImageDownloadName(url) }));
  }

  function copyFlowMarkdown() {
    copyTextToClipboard((props.flowOutputSegments || []).map((segment) => segment.value).join(''));
    flowCopied.value = true;
    clearTimeout(flowCopyTimer);
    flowCopyTimer = setTimeout(() => {
      flowCopied.value = false;
    }, 1500);
  }

  function getImageDownloadName(url) {
    return url.split('?')[0].split('/').pop() || '生成图片.png';
  }
  // update-end--author:liaozhiyang---date:20260831---for:【LHZP-2020】新增图像生成节点（支持图片整组预览、单张下载、全部下载和整体复制）

  /**
   * 获取卡片列表
   */
  function getCardList() {
    let text = props.text;
    let card = text.replace('::card::', '').replace(/\s+/g, '');
    try {
      return JSON.parse(card);
    } catch (e) {
      console.log(e)
      return '';
    }
  }

  /**
   * ai卡片点击事件
   * @param url
   */
  function aiCardHandleClick(url){
    window.open(url,'_blank');
  }


  /**
   * 从config获取取卡片列表
   */
  function getCardConfigList() {
    let text = props.text;
    let card = text.replace('::cardConfig::', 'cardConfig').replace(/\s+/g, '');
    try {
      let parse = JSON.parse(card);
      cardConfig.value = JSON.parse(parse?.cardConfig);
      return JSON.parse(parse?.content);
    } catch (e){
      console.log(e)
      return '';
    }
  }

  /**
   * 卡片点击跳转
   */
  function handleJumpClick(item) {
    if(cardConfig.value?.enableJump){
      let src = item[cardConfig.value?.jumpUrl];
      let reg = /#\s*{\s*domainURL\s*}/g;
      src = src.replace(reg,domainUrl);
      window.open(src,"_blank")
    }
  }

  /**
   * 获取文件名字
   * 
   * @param fileUrl
   */
  function getFileName(fileUrl){
    if(!fileUrl) {
      return '未命名的文件';
    }
    let fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1).toLowerCase();
    fileName = fileName.substring(0,fileName.lastIndexOf("."));
    return fileName;
  }

  /**
   * 文件预览
   * 
   * @param fileUrl
   */
  function handleFilePreview(fileUrl) {
    let filePath = encodeURIComponent(encryptByBase64(getFileAccessHttpUrl(fileUrl)));
    let url = `${viewUrl}?url=` + filePath;
    window.open(url, "_blank")
  }
</script>

<style lang="less" scoped>
  .chat {
    display: flex;
    margin-bottom: 1.5rem;
    &.self {
      flex-direction: row-reverse;
      .avatar {
        margin-right: 0;
        margin-left: 10px;
      }
      .msgArea {
        flex-direction: row-reverse;
        margin-bottom: 6px;
      }
      .thinkArea{
        margin: 0;
        padding: 5px 0 5px 22px;
        position: relative;
      }
      .date {
        text-align: right;
      }
    }
  }
  :deep(.ant-collapse-header){
    padding: 0 !important;
  }
  .hidden-avatar{
    left: 44px;
    position: relative;
    top: -18px;
  }
  .avatar {
    flex: none;
    margin-right: 10px;
    img {
      width: 34px;
      height: 34px;
      border-radius: 50%;
      overflow: hidden;
    }
    svg {
      font-size: 28px;
    }
  }
  .chat.chatgpt .avatar img{
    border-radius: 4px;
  }
  .content {
    width: 90%;
    .date {
      color: #b4bbc4;
      font-size: 0.75rem;
      margin-bottom: 10px;
    }
    .msgArea {
      display: flex;
    }
  }

  .question{
    margin-top: 10px;
    border-radius: 0.375rem;
    padding-top: 0.5rem;
    padding-bottom: 0.5rem;
    padding-left: 0.75rem;
    padding-right: 0.75rem;
    background-color: #ffffff;
    font-size: 0.875rem;
    line-height: 1.25rem;
    cursor: pointer;
    border: 1px solid #f0f0f0;
    box-shadow: 0 2px 4px #e6e6e6;
  }

  .images{
    margin-bottom: 10px;
    flex-wrap: wrap;
    display: flex;
    gap: 10px;
    justify-content: end;
    .image{
      width: 120px;
      height: 80px;
      cursor: pointer;
      img{
        width: 100%;
        height: 100%;
        object-fit: cover;
        border-radius: 4px;
      }
    }
  }

  .flow-output-segments {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
    margin-bottom: 10px;
    width: fit-content;
    max-width: 100%;
    padding: 8px 12px;
    border-radius: 6px;
    background-color: #f4f6f8;

    .flow-image-actions {
      display: flex;
      justify-content: flex-end;
      gap: 4px;
      width: 100%;
    }

    .flow-image-item {
      display: flex;
      flex-direction: column;
      gap: 6px;
      width: 180px;
    }

    .image {
      width: 180px;
      height: 180px;
      cursor: pointer;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        border-radius: 4px;
      }
    }

    :deep(.textWrap) {
      padding: 0;
      background-color: transparent;
    }

    :deep(.textWrap .copyMarkdownBtn) {
      display: none;
    }

    :deep(.textWrap .markdown-body) {
      padding-right: 0;
      background-color: transparent;
    }
  }

  /*begin文件列表的样式*/
  .file-list {
    margin-bottom: 10px;
    display: flex;
    flex-direction: column;
    gap: 8px;
    align-items: flex-end;
  }

  .file-item {
    display: flex;
    align-items: center;
    background: #f4f6f8;
    border-radius: 8px;
    padding: 8px 12px;
    cursor: pointer;
    width: fit-content;
    max-width: 100%;

    .file-icon {
      margin-right: 8px;
      display: flex;
      align-items: center;
    }

    .file-name {
      font-size: 14px;
      color: #333;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      max-width: 200px;
    }
  }
  /*end文件列表的样式*/

  .retrieval,
  .card {
    background-color: #f4f6f8;
    font-size: 0.875rem;
    line-height: 1.25rem;
    border-radius: 0.375rem;
    padding-top: 0.5rem;
    padding-bottom: 0.5rem;
    padding-left: 0.75rem;
    padding-right: 0.75rem;
  }
  .retrieval {
    display: inline-block;
    width: fit-content;
    max-width: 100%;
  }
  .retrieval:after{
    animation: ai-chat-cursor-blink 1s step-end infinite;
    color: #000;
    content: '_';
    font-weight: 700;
    margin-left: 3px;
    vertical-align: baseline;
  }
  // update-begin--author:liaozhiyang---date:20260805---for：【LHZP-1436】思考加载的动画独立命名避免和codeMirror冲突
  @keyframes ai-chat-cursor-blink {
    0%,
    100% {
      opacity: 1;
    }
    50% {
      opacity: 0;
    }
  }
  // update-end--author:liaozhiyang---date:20260805---for：【LHZP-1436】思考加载的动画独立命名避免和codeMirror冲突
  .card{
    width: 100%;
    background-color: unset;
  }
  .ai-card{
     width: 98%;
     height: 100%;
     cursor: pointer;
    .ai-card-title{
      width: 100%;
      line-height: 20px;
      letter-spacing: 0;
      white-space: pre-line;
      overflow: hidden;
      display: -webkit-box;
      text-overflow: ellipsis;
      -webkit-box-orient: vertical;
      font-weight: 600;
      font-size: 18px;
      text-align: left;
      color: #191919;
      -webkit-line-clamp: 1;
    }
    .ai-card-img{
      margin-top: 10px;
      background-color: transparent;
      border-radius: 8px;
      display: flex;
      width: 100%;
      height: max-content;
    }
    .ai-card-desc{
      margin-top: 10px;
      width: 100%;
      font-size: 14px;
      font-weight: 400;
      line-height: 20px;
      letter-spacing: 0;
      white-space: pre-line;
      -webkit-box-orient: vertical;
      overflow: hidden;
      display: -webkit-box;
      text-overflow: ellipsis;
      text-align: left;
      color: #666f;
      -webkit-line-clamp: 3;
    }
  }
  @media (max-width: 600px) {
    .content{
      width: 100%;
    }
  }
</style>
