<template>
  <div class="chat" :class="[inversion === 'user' ? 'self' : 'chatgpt']">
    <div class="avatar">
      <img v-if="inversion === 'user'" :src="avatar()" />
      <img v-else :src="getAiImg()">
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
      <div class="msgArea">
        <chatText :text="text" :inversion="inversion" :error="error" :loading="loading"></chatText>
      </div>
      <div v-if="presetQuestion" v-for="item in presetQuestion" class="question" @click="presetQuestionClick(item.descr)">
        <span>{{item.descr}}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import chatText from './chatText.vue';
  import defaultAvatar from '/@/assets/images/ai/avatar.jpg';
  import { useUserStore } from '/@/store/modules/user';
  import defaultImg from '../img/ailogo.png';
  
  const props = defineProps(['dateTime', 'text', 'inversion', 'error', 'loading','appData','presetQuestion','images']);
  import { getFileAccessHttpUrl } from '/@/utils/common/compUtils';
  import { createImgPreview } from "@/components/Preview";
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
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .images{
    margin-bottom: 10px;
    flex-wrap: wrap;
    display: flex;
    gap: 10px;
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
</style>
