<template>
  <div :class="{'comment-active': commentActive}" class="comment-main" @click="handleClickBlank">
    <textarea ref="commentRef" v-model="myComment" @keyup.enter="sendComment" @input="handleCommentChange" @blur="handleBlur" class="comment-content" :rows="3" placeholder="请输入你的评论，可以@成员" />
    <div class="comment-content comment-html-shower" :class="{'no-content':noConent, 'top-div': showHtml, 'bottom-div': showHtml == false }" v-html="commentHtml" @click="handleClickHtmlShower"></div>
    <div class="comment-buttons" v-if="commentActive">
      <div style="cursor: pointer">
        <Tooltip title="选择@用户">
          <user-add-outlined @click="openSelectUser" />
        </Tooltip>

        <Tooltip title="上传附件">
          <PaperClipOutlined @click="uploadVisible = !uploadVisible" />
        </Tooltip>

        <span title="表情" style="display: inline-block">
          <SmileOutlined ref="emojiButton" @click="handleShowEmoji" />
          <div style="position: relative" v-show=""> </div>
        </span>
      </div>
      <div v-if="commentActive">
        <a-button v-if="inner" @click="noComment" style="margin-right: 10px">取消</a-button>
        <a-button type="primary" @click="sendComment" :loading="buttonLoading" :disabled="disabledButton">发 送</a-button>
      </div>
    </div>
    <upload-chunk ref="uploadRef" :visible="uploadVisible" @select="selectFirstFile"></upload-chunk>
  </div>
  <UserSelectModal  rowKey="username" @register="registerModal" @selected="setValue" :multi="false"></UserSelectModal>
  <a-modal v-model:open="visibleEmoji" :footer="null" wrapClassName="emoji-modal" :closable="false" :width="490">
    <template #title>
      <span></span>
    </template>
    <Picker
      :pickerStyles="pickerStyles" 
      :i18n="optionsName" 
      :data="emojiIndex"
      emoji="grinning"
      :showPreview="false" 
      :infiniteScroll="false" 
      :showSearch="false" 
      :showSkinTones="false" 
      set="apple" 
      @select="showEmoji">
    </Picker>
  </a-modal>
</template>

<script lang="ts">
  import {ref, watch, computed, inject} from 'vue';
  import { propTypes } from '/@/utils/propTypes';
  import { UserAddOutlined, PaperClipOutlined, SmileOutlined } from '@ant-design/icons-vue';
  import { Tooltip } from 'ant-design-vue';
  import UserSelectModal from '/@/components/Form/src/jeecg/components/userSelect/UserSelectModal.vue';
  import { useModal } from '/@/components/Modal';
  import UploadChunk from './UploadChunk.vue';
  import 'emoji-mart-vue-fast/css/emoji-mart.css';
  import {getGloablEmojiIndex, useEmojiHtml} from './useComment';

  const optionsName = {
    categories: {
      recent: '最常用的',
      smileys: '表情选择',
      people: '人物&身体',
      nature: '动物&自然',
      foods: '食物&饮料',
      activity: '活动',
      places: '旅行&地点',
      objects: '物品',
      symbols: '符号',
      flags: '旗帜',
    },
  };
  export default {
    name: 'MyComment',
    components: {
      UserAddOutlined,
      Tooltip,
      UserSelectModal,
      PaperClipOutlined,
      UploadChunk,
      SmileOutlined,
    },
    props: {
      inner: propTypes.bool.def(false),
      inputFocus: {
        type: Boolean,
        default: false,
      },
    },
    emits: ['cancel', 'comment'],
    setup(props, { emit }) {
      const uploadVisible = ref(false);
      const uploadRef = ref();
      //注册model
      const [registerModal, { openModal, closeModal }] = useModal();
      const buttonLoading = ref(false);
      const myComment = ref<string>('');
      function sendComment() {
        console.log(myComment.value);
        let content = myComment.value;
        if (!content && content !== '0') {
          disabledButton.value = true;
        } else {
          buttonLoading.value = true;
          let fileList = [];
          if (uploadVisible.value == true) {
            fileList = uploadRef.value.getUploadFileList();
          }
          emit('comment', content, fileList);
          setTimeout(() => {
            buttonLoading.value = false;
          }, 350);
        }
      }
      const disabledButton = ref(false);
      watch(myComment, () => {
        let content = myComment.value;
        if (!content && content !== '0') {
          disabledButton.value = true;
        } else {
          disabledButton.value = false;
        }
      });

      function noComment() {
        emit('cancel');
      }

      const commentRef = ref();
      watch(
        () => props.inputFocus,
        (val) => {
          if (val == true) {
            // commentRef.value.focus()
            myComment.value = '';
            if (uploadVisible.value == true) {
              uploadRef.value.clear();
              uploadVisible.value = false;
            }
          }
        },
        { deep: true, immediate: true }
      );

      function openSelectUser() {
        openModal(true, {
          isUpdate: false,
        });
      }
      function setValue(options) {
        console.log('setValue', options);
        if (options && options.length > 0) {
          const { realname, username } = options[0];
          if (realname && username) {
            let str = `${realname}[${username}]`;
            let temp = myComment.value;
            if (!temp) {
              myComment.value = '@' + str;
            } else {
              if (temp.endsWith('@')) {
                myComment.value = temp + str +' ';
              } else {
                myComment.value = '@' + str + ' ' + temp + ' ';
              }
            }
            //update-begin---author:wangshuai---date:2024-01-22---for:【QQYUN-8002】选完人，鼠标应该放到后面并在前面加上空格---
            showHtml.value = false;
            commentRef.value.focus();
            commentActive.value = true;
            //update-end---author:wangshuai---date:2024-01-22---for:【QQYUN-8002】选完人，鼠标应该放到后面并在前面加上空格---
          }
        }
        closeModal();        
      }

      function handleCommentChange() {
        //console.log(1,e)
      }
      watch(
        () => myComment.value,
        (val) => {
          if (val && val.endsWith('@')) {
            openSelectUser();
          }
        }
      );

      const emojiButton = ref();
      function onSelectEmoji(emoji) {
        let temp = myComment.value || '';
        temp += emoji;
        myComment.value = temp;
        emojiButton.value.click();
      }
      
      const visibleEmoji = ref(false);
      function showEmoji(e) {
        let temp = myComment.value || '';
        let str = e.colons;
        if (str.indexOf('::') > 0) {
          str = str.substring(0, str.indexOf(':') + 1);
        }
        // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-931】评论表情插入光标位置
        const index = commentRef.value?.selectionStart ?? temp.length;
        // myComment.value = temp + str;
        const startStr = temp.substring(0, index);
        const endStr = temp.substring(index);
        myComment.value = startStr + str + endStr;
        // update-end--author:liaozhiyang---date:20240603---for：【TV360X-931】评论表情插入光标位置
        visibleEmoji.value = false;
        handleBlur();
      }

      const pickerStyles = {
        width: '490px'
        /* height: '350px',
        top: '0px',
        left: '-75px',
        position: 'absolute',
        'z-index': 9999*/
      };
      function handleClickBlank(e) {
        console.log('handleClickBlank');
        e.preventDefault();
        e.stopPropagation();
        visibleEmoji.value = false;
        commentActive.value = true;
      }
      function handleShowEmoji(e) {
        console.log('handleShowEmoji');
        e.preventDefault();
        e.stopPropagation();
        visibleEmoji.value = !visibleEmoji.value;
      }
      
      //const emojiIndex = inject('$globalEmojiIndex')
      const emojiIndex = getGloablEmojiIndex()
      const { getHtml } = useEmojiHtml(emojiIndex);

      const commentHtml = computed(() => {
        let temp = myComment.value;
        if (!temp) {
          return '请输入你的评论，可以@成员';
        }
        return getHtml(temp);
      });

      const showHtml = ref(false);
      function handleClickHtmlShower(e) {
        e.preventDefault();
        e.stopPropagation();
        showHtml.value = false;
        commentRef.value.focus();
        console.log(234);
        commentActive.value = true;
      }
      function handleBlur() {
        showHtml.value = true;
      }
      
      const commentActive = ref(false);
      const noConent = computed(()=>{
        if(myComment.value.length>0){
          return false;
        }
        return true;
      });
      function changeActive(){
        if(myComment.value.length==0){
          commentActive.value = false
          uploadVisible.value = false;
        }
      }
      
      function selectFirstFile(fileName){
        if(myComment.value.length==0){
          myComment.value = fileName;
        }
      }
      
      return {
        myComment,
        sendComment,
        noComment,
        disabledButton,
        buttonLoading,
        commentRef,
        registerModal,
        openSelectUser,
        setValue,
        handleCommentChange,
        uploadRef,
        uploadVisible,
        onSelectEmoji,
        optionsName,
        emojiButton,
        emojiIndex,
        showEmoji,
        pickerStyles,
        visibleEmoji,
        handleClickBlank,
        handleShowEmoji,
        commentHtml,
        showHtml,
        handleClickHtmlShower,
        handleBlur,
        commentActive,
        noConent,
        changeActive,
        selectFirstFile
      };
    },
  };
</script>

<style lang="less">
  // update-begin--author:liaozhiyang---date:20240327---for：【QQYUN-8639】暗黑主题适配
  .comment-main {
    border: 1px solid #eee;
    margin: 0;
    position: relative;
  }
  // update-end--author:liaozhiyang---date:20240327---for：【QQYUN-8639】暗黑主题适配
  .comment-content {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
    font-variant: tabular-nums;
    list-style: none;
    font-feature-settings: tnum;
    position: relative;
    display: inline-block;
    width: 100%;
    padding: 4px 11px;
    color: rgba(0, 0, 0, 0.85);
    font-size: 15px;
    line-height: 1.5715;
    background-color: #fff;
    background-image: none;
    border: 1px solid #d9d9d9;
    border-radius: 2px;
    transition: all 0.3s;
    width: 100%;
    border: solid 0px;
    outline: none;

    .emoji-item {
      display: inline-block !important;
      width: 0 !important;
    }
  }
  .comment-buttons {
    padding: 10px;
    display: flex;
    justify-content: space-between;
    border-top: 1px solid #d9d9d9;
    .anticon {
      margin: 5px;
    }
  }
  .comment-html-shower {
    position: absolute;
    top: 0;
    left: 0;
    height: 70px;
    &.bottom-div {
      z-index: -99;
    }
    &.top-div {
      z-index: 9;
    }
  }

  .emoji-modal  {
   > .ant-modal{
      right: 25% !important;
      margin-right: 16px !important;
    }
    .ant-modal-header{
      padding: 0 !important;
    }
    .emoji-mart-bar{
      display: none;
    }
    h3.emoji-mart-category-label{
    /*  display: none;*/
      border-bottom: 1px solid #eee;
    }
  }
  
  .comment-active{
    border-color: @primary-color !important;
    // box-shadow: 0 1px 1px 0 #90caf9, 0 1px 6px 0 #90caf9;
  }
  .no-content{
    color: #a1a1a1
  }
  
  /**聊天表情本地化*/
  .emoji-type-image.emoji-set-apple {
    background-image: url("./image/emoji.png");
  }
  // update-begin--author:liaozhiyang---date:20240327---for：【QQYUN-8639】暗黑主题适配
  html[data-theme='dark'] {
    .emoji-type-image.emoji-set-apple {
      background-image: url("./image/emoji_native.png");
    }
    .comment-main {
      border-color: rgba(253, 253, 253, 0.12);
    }
    .comment-content {
      background-color: #141414;
      color: rgba(255, 255, 255, 0.85);
      border-color: rgba(253, 253, 253, 0.12);
    }
    .comment-buttons{
      border-color: rgba(253, 253, 253, 0.12);
    }
  }
  // update-end--author:liaozhiyang---date:20240327---for：【QQYUN-8639】暗黑主题适配
</style>
