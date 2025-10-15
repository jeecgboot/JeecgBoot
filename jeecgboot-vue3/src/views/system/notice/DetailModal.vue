<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :width="800" title="查看详情" :showCancelBtn="false" :showOkBtn="false" :maxHeight="500">
    <div class="print-btn" @click="onPrinter">
      <Icon icon="ant-design:printer-filled" />
      <span class="print-text">打印</span>
    </div>
    <iframe ref="iframeRef" :src="frameSrc" class="detail-iframe" @load="onIframeLoad"></iframe>
    <template v-if="noticeFiles && noticeFiles.length > 0">
      <div class="files-title">相关附件：</div>
      <template v-for="(file, index) in noticeFiles" :key="index">
        <div class="files-area">
          <div class="files-area-text">
            <span>
              <paper-clip-outlined />
              <a
                target="_blank"
                rel="noopener noreferrer"
                :title="file.fileName"
                :href="getFileAccessHttpUrl(file.filePath)"
                class="ant-upload-list-item-name"
                >{{ file.fileName }}</a
              >
            </span>
          </div>
          <div class="files-area-operate">
            <download-outlined class="item-icon" @click="handleDownloadFile(file.filePath)" />
            <eye-outlined class="item-icon" @click="handleViewFile(file.filePath)" />
          </div>
        </div>
      </template>
      <a v-if="noticeFiles.length > 1" :href="downLoadFiles + '?id=' + noticeId + '&token=' + getToken()" target="_blank"  style="margin: 15px 6px;color: #5ac0fa;">
        <download-outlined class="item-icon" style="margin-right: 5px"  /><span>批量下载所有附件</span>
      </a>
    </template>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { propTypes } from '/@/utils/propTypes';
  import { ref } from 'vue';
  import { buildUUID } from '@/utils/uuid';
  import { getFileAccessHttpUrl } from '@/utils/common/compUtils';
  import { DownloadOutlined, EyeOutlined, PaperClipOutlined } from '@ant-design/icons-vue';
  import { encryptByBase64 } from '@/utils/cipher';
  import { useGlobSetting } from '@/hooks/setting';
  import { getToken } from "@/utils/auth";
  const glob = useGlobSetting();
  // 获取props
  defineProps({
    frameSrc: propTypes.string.def(''),
  });
  /**
   * 下载文件路径
   */
  const downLoadFiles = `${glob.domainUrl}/sys/annountCement/downLoadFiles`;

  //附件内容
  const noticeFiles = ref([]);
  //数据ID
  const noticeId = ref('');
  //表单赋值
  const [registerModal] = useModalInner((data) => {
    noticeFiles.value = [];
    noticeId.value = data.record.id;
    if (data.record?.files && data.record?.files.length > 0) {
      noticeFiles.value = data.record.files.split(',').map((item) => {
        return {
          fileName: item.split('/').pop(),
          filePath: item,
        };
      });
    }
  });
  // iframe引用
  const iframeRef = ref<HTMLIFrameElement>();
  // 存储当前打印会话ID
  const printSessionId = ref<string>('');
  // iframe加载完成后初始化通信
  const onIframeLoad = () => {
    printSessionId.value = buildUUID(); // 每次加载生成新的会话ID
  };
  //打印
  function onPrinter() {
    if (!iframeRef.value) return;
    console.log('onPrinter', iframeRef.value);
    iframeRef.value?.contentWindow?.postMessage({ printSessionId: printSessionId.value, type: 'action:print' }, '*');
  }
  /**
   * 下载文件
   * @param filePath
   */
  function handleDownloadFile(filePath) {
    window.open(getFileAccessHttpUrl(filePath), '_blank');
  }
  /**
   * 预览文件
   * @param filePath
   */
  function handleViewFile(filePath) {
    if (filePath) {
      let url = encodeURIComponent(encryptByBase64(filePath));
      let previewUrl = `${glob.viewUrl}?url=` + url;
      window.open(previewUrl, '_blank');
    }
  }
</script>

<style scoped lang="less">
  .print-btn {
    position: absolute;
    top: 80px;
    right: 40px;
    cursor: pointer;
    color: #a3a3a5;
    z-index: 999;
    .print-text {
      margin-left: 5px;
    }
    &:hover {
      color: #40a9ff;
    }
  }
  .detail-iframe {
    border: 0;
    width: 100%;
    height: 100%;
    min-height: 500px;
    // -update-begin--author:liaozhiyang---date:20240702---for：【TV360X-1685】通知公告查看出现两个滚动条
    display: block;
    // -update-end--author:liaozhiyang---date:20240702---for：【TV360X-1685】通知公告查看出现两个滚动条
  }
  .files-title {
    font-size: 16px;
    margin: 10px;
    font-weight: 600;
    color: #333;
  }
  .files-area {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    margin: 6px;
    &:hover {
      background-color: #f5f5f5;
    }
    .files-area-text {
      display: flex;
      .ant-upload-list-item-name {
        margin: 0 6px;
        color: #56befa;
      }
    }
    .files-area-operate {
      display: flex;
      margin-left: 10px;
      .item-icon {
        cursor: pointer;
        margin: 0 6px;
        &:hover {
          color: #56befa;
        }
      }
    }
  }
</style>
