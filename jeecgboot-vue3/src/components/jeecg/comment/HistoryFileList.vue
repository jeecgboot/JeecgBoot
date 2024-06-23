<template>
  <div class="comment-file-his-list" :class="isComment === true ? 'in-comment' : ''">
    <div class="selected-file-list">
      <div class="item" v-for="item in dataList">
        <div class="complex">
          <div class="content">
            <!-- 图片 -->
            <div v-if="isImage(item)" class="content-top" style="height: 100%">
              <div class="content-image" :style="getImageAsBackground(item)">
                <!--<img style="height: 100%;" :src="getImageSrc(item)"/>-->
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
            <div class="next" @click="viewImage(item)">
              <div class="text">
                {{ item.name }}
              </div>
              <div class="text">
                {{ getFileSize(item) }}
              </div>
            </div>
            <div class="buttons">
              <div class="opt-icon">
                <Tooltip title="下载">
                  <download-outlined @click="downLoad(item)" />
                </Tooltip>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="item empty"></div><div class="item empty"></div><div class="item empty"></div> <div class="item empty"></div><div class="item empty"></div><div class="item empty"></div>
    </div>
  </div>
</template>

<script>
  import { Tooltip } from 'ant-design-vue';
  import { UploadOutlined, FolderOutlined, DownloadOutlined, PaperClipOutlined, DeleteOutlined } from '@ant-design/icons-vue';
  import { useFileList } from './useComment';

  export default {
    name: 'HistoryFileList',
    props: {
      dataList: {
        type: Array,
        default: () => [],
      },
      isComment: {
        type: Boolean,
        default: false,
      },
    },
    components: {
      UploadOutlined,
      FolderOutlined,
      DownloadOutlined,
      PaperClipOutlined,
      DeleteOutlined,
      Tooltip,
    },
    setup() {
      const { getBackground, getFileSize, downLoad, isImage, getImageAsBackground, viewImage } = useFileList();
      return {
        getBackground,
        downLoad,
        getFileSize,
        isImage,
        getImageAsBackground,
        viewImage
      };
    },
  };
</script>

<style lang="less" scoped>
  @import 'comment.less';
</style>
