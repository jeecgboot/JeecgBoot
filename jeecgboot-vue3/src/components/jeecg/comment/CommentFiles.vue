<template>
  <div>
    <a-alert type="info" class="jeecg-comment-files">
      <template #message>
        <span class="j-icon">
          <a-upload multiple v-model:file-list="selectFileList" :showUploadList="false" :before-upload="beforeUpload">
            <span class="inner-button"><upload-outlined />上传</span>
          </a-upload>
        </span>
      </template>
    </a-alert>

    <!-- 正在上传的文件 -->
    <div class="selected-file-warp" v-if="selectFileList && selectFileList.length > 0">
      <div class="selected-file-list">
        <div class="item" v-for="item in selectFileList">
          <div class="complex">
            <div class="content" >
              <!-- 图片 -->
              <div v-if="isImage(item)" class="content-top" style="height: 100%">
                <div class="content-image" :style="getImageAsBackground(item)">
                  <!--  <img style="height: 100%;" :src="getImageSrc(item)">-->
                </div>
              </div>
              <!-- 文件 -->
              <template v-else>
                <div class="content-top">
                  <div class="content-icon" :style="{ background: 'url(' + getBackground(item) + ')  no-repeat' }"></div>
                </div>
                <div class="content-bottom" :title="item.name">
                  <span>{{ item.name }}</span>
                </div>
              </template>
            </div>
            <div class="layer" :class="{'layer-image':isImage(item)}">
              <div class="next" @click="viewImage(item)"><div class="text">{{ item.name }} </div></div>
              <div class="buttons">
                <div class="opt-icon">
                  <Tooltip title="删除">
                    <delete-outlined @click="handleRemove(item)" />
                  </Tooltip>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="item empty"></div><div class="item empty"></div><div class="item empty"></div> <div class="item empty"></div><div class="item empty"></div><div class="item empty"></div>
      </div>

      <div style="margin-bottom: 24px; margin-top: 18px; text-align: right">
        <a-button @click="quxiao">取消</a-button>
        <a-button type="primary" style="margin-left: 10px" @click="queding" :loading="buttonLoading">确定</a-button>
      </div>
    </div>

    <!-- 历史文件 -->
    <history-file-list :dataList="dataList"></history-file-list>
  </div>
</template>

<script>
  import { UploadOutlined, FolderOutlined, DownloadOutlined, PaperClipOutlined, DeleteOutlined } from '@ant-design/icons-vue';
  import JUpload from '/@/components/Form/src/jeecg/components/JUpload/JUpload.vue';
  import { uploadFileUrl } from './useComment';
  import { propTypes } from '/@/utils/propTypes';
  import { computed, watchEffect, unref, ref } from 'vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { fileList } from './useComment';
  import { getFileAccessHttpUrl } from '/@/utils/common/compUtils';
  import { useUserStore } from '/@/store/modules/user';
  import { saveOne, useCommentWithFile, useFileList } from './useComment';
  import {useModal} from "/@/components/Modal";

  import { Tooltip } from 'ant-design-vue';
  import HistoryFileList from './HistoryFileList.vue';

  export default {
    name: 'CommentFiles',
    components: {
      UploadOutlined,
      FolderOutlined,
      JUpload,
      DownloadOutlined,
      PaperClipOutlined,
      DeleteOutlined,
      Tooltip,
      HistoryFileList,
    },
    props: {
      tableName: propTypes.string.def(''),
      dataId: propTypes.string.def(''),
      datetime:  propTypes.number.def(1)
    },
    setup(props) {
      // const { createMessage } = useMessage();
      const [registerModel, { openModal }] = useModal();
      const { userInfo } = useUserStore();
      const dataList = ref([]);
      const commentId = ref('');

      async function loadFileList() {
        const params = {
          tableName: props.tableName,
          tableDataId: props.dataId,
        };
        const data = await fileList(params);
        console.log('1111', data)
        if (!data || !data.records || data.records.length == 0) {
          dataList.value = [];
        } else {
          let array = data.records;
          console.log(123, array);
          dataList.value = array;
        }
        commentId.value = '';
      }

      watchEffect(() => {
        // 每次切换tab都会刷新文件列表--- VUEN-1884 评论里上传的图片未在文件中显示
        if(props.datetime){
          if (props.tableName && props.dataId) {
            loadFileList();
          }
        }
      });

      const { saveCommentAndFiles, buttonLoading } = useCommentWithFile(props);
      const { selectFileList, beforeUpload, handleRemove, getBackground, isImage, getImageAsBackground, viewImage } = useFileList();

      function quxiao() {
        selectFileList.value = [];
      }
      async function queding() {
        let obj = {
          fromUserId: userInfo.id,
          commentContent: '上传了附件'
        }
        await saveCommentAndFiles(obj, selectFileList.value)
        selectFileList.value = [];
        await loadFileList();
      }
      
      function showFileModal() {
        openModal(true, {})
      }
      
      function onSelectFileOk(temp) {
        // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-935】从知识库选择文件判断下是否没选
        if (temp.id === '') return;
        // update-end--author:liaozhiyang---date:20240603---for：【TV360X-935】从知识库选择文件判断下是否没选
        let arr = selectFileList.value;
        arr.push({
          ...temp,
          exist: true
        })
        selectFileList.value = arr;
      }

      return {
        selectFileList,
        beforeUpload,
        handleRemove,
        getBackground,
        isImage,
        dataList,
        uploadFileUrl,
        quxiao,
        queding,
        buttonLoading,
        getImageAsBackground,
        viewImage,
        registerModel,
        showFileModal,
        onSelectFileOk,

      };
    },
  };
</script>

<style lang="less" scoped>
  @import 'comment.less';
</style>
