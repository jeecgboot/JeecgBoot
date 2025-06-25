<template>
  <div class="chat" :class="[inversion === 'user' ? 'self' : 'chatgpt']" v-if="getText || (props.presetQuestion && props.presetQuestion.length>0)">
    <div class="avatar">
      <img v-if="inversion === 'user'" :src="avatar()" />
      <img v-else :src="getAiImg()" />
    </div>
    <div class="content">
      <p class="date">
        <span v-if="inversion === 'ai'" style="margin-right: 10px">{{appData.name || 'AI助手'}}</span>
        <span>{{ dateTime }}</span>
      </p>
      <div v-if="inversion === 'user' && images && images.length>0" class="images">
          <div v-for="(item,index) in images" :key="index" class="image" @click="handlePreview(item)">
            <img :src="getImageUrl(item)"/>
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
      <div class="msgArea" v-if="!isCard">
        <chatText :text="text" :inversion="inversion" :error="error" :loading="loading" :referenceKnowledge="referenceKnowledge"></chatText>
      </div>
      <div v-if="presetQuestion" v-for="item in presetQuestion" class="question" @click="presetQuestionClick(item.descr)">
        <span>{{item.descr}}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import chatText from './chatText.vue';
  import defaultAvatar from "@/assets/images/ai/avatar.jpg";
  import { useUserStore } from '/@/store/modules/user';
  import defaultImg from '../img/ailogo.png';

  const props = defineProps(['dateTime', 'text', 'inversion', 'error', 'loading','appData','presetQuestion','images','retrievalText', 'referenceKnowledge']);
  import { getFileAccessHttpUrl } from '/@/utils/common/compUtils';
  import { createImgPreview } from "@/components/Preview";
  import { computed } from "vue";

  const getText = computed(()=>{
    let text = props.text || props.retrievalText;
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
    if(item.hasOwnProperty('url')){
      url = item.url;
    }
    if(item.hasOwnProperty('base64Data') && item.base64Data){
      return item.base64Data;
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
      }
      .date {
        text-align: right;
      }
    }
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
  .retrieval:after{
    animation: blink 1s steps(5, start) infinite;
    color: #000;
    content: '_';
    font-weight: 700;
    margin-left: 3px;
    vertical-align: baseline;
  }
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
