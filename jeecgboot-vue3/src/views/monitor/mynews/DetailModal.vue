<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    title="查看详情"
    :width="800"
    :minHeight="600"
    :showCancelBtn="false"
    :showOkBtn="false"
    :height="88"
    :destroyOnClose="true"
    @visible-change="handleVisibleChange"
  >
   <template #title>
     <span class="basic-title">查看详情</span>
     <div class="print-btn" @click="onPrinter">
       <Icon icon="ant-design:printer-filled" />
       <span class="print-text">打印</span>
     </div>
   </template>
    <a-card class="daily-article">
      <a-card-meta :title="content.titile">
        <template #description>
          <div class="article-desc">
            <span>发布人：{{ content.sender }}</span>
            <span>发布时间：{{ content.sendTime }}</span>
            <span v-if="content.visitsNum">
              <a-tooltip placement="top" title="访问次数" :autoAdjustOverflow="true">
                <eye-outlined class="item-icon" /> {{ content.visitsNum }}
              </a-tooltip>
            </span>
          </div>
        </template>
      </a-card-meta>
      <a-divider />
      <div v-html="content.msgContent" class="article-content"></div>
      <div>
        <a-button v-if="hasHref" @click="jumpToHandlePage">前往办理<ArrowRightOutlined /></a-button>
      </div>
    </a-card>
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
      <a v-if="noticeFiles.length > 1" :href="downLoadFiles + '?id=' + content.id + '&token=' + getToken()" target="_blank" style="margin: 15px 6px; color: #5ac0fa">
        <download-outlined class="item-icon" style="margin-right: 5px" /><span>批量下载所有附件</span>
      </a>
    </template>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { ArrowRightOutlined, PaperClipOutlined, DownloadOutlined, EyeOutlined } from '@ant-design/icons-vue';
  import { addVisitsNum } from '@/views/system/notice/notice.api';
  import { useRouter } from 'vue-router';
  import xss from 'xss';
  import { options } from './XssWhiteList';
  import { ref, unref } from 'vue';
  import { getFileAccessHttpUrl } from '@/utils/common/compUtils';
  import { useGlobSetting } from '@/hooks/setting';
  import { encryptByBase64 } from '@/utils/cipher';
  import { getToken } from '@/utils/auth';
  import {defHttp} from "@/utils/http/axios";
  const router = useRouter();
  const glob = useGlobSetting();
  const isUpdate = ref(true);
  const content = ref<any>({});
  const noticeFiles = ref([]);
  /**
   * 下载文件路径
   */
  const downLoadFiles = `${glob.domainUrl}/sys/annountCement/downLoadFiles`;
  const emit = defineEmits(['close', 'register']);
  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    isUpdate.value = !!data?.isUpdate;
    noticeFiles.value = [];
    if (unref(isUpdate)) {
      //data.record.msgContent = '<p>2323</p><input onmouseover=alert(1)>xss test';
      // 代码逻辑说明: VUEN-1702 【禁止问题】sql注入漏洞
      if (data.record.msgContent) {
        // 代码逻辑说明: 【QQYUN-7049】3.6.0版本 通知公告中发布的富文本消息，在我的消息中查看没有样式---
        data.record.msgContent = xss(data.record.msgContent, options);
      }

      // 代码逻辑说明: [QQYUN-12521]通知公告消息增加访问量
      if (!data.record?.busId) {
        await addVisitsNum({ id: data.record.id });
      }

      content.value = data.record;
      if(content.value.sender){
        const userInfo = await defHttp.get({ url: '/sys/user/queryUserComponentData?isMultiTranslate=true', params: { username: content.value.sender } });
        content.value.sender = userInfo && userInfo?.records && userInfo?.records.length>0?userInfo.records[0].realname : content.value.sender;
      }
      console.log('data---------->>>', data);
      if (data.record?.files && data.record?.files.length > 0) {
        noticeFiles.value = data.record.files.split(',').map((item) => {
          return {
            fileName: item.split('/').pop(),
            filePath: item,
          };
        });
      }
      showHrefButton();
    }
  });

  const hasHref = ref(false);
  //查看消息详情可以跳转
  function showHrefButton() {
    if (content.value.busId) {
      hasHref.value = true;
    }
  }
  //跳转至办理页面
  function jumpToHandlePage() {
    let temp: any = content.value;
    if (temp.busId) {
      //这个busId是 任务ID
      let jsonStr = temp.msgAbstract;
      let query = {};
      try {
        if (jsonStr) {
          let temp = JSON.parse(jsonStr);
          if (temp) {
            Object.keys(temp).map((k) => {
              query[k] = temp[k];
            });
          }
        }
      } catch (e) {
        console.log('参数解析异常', e);
      }

      console.log('query', query, jsonStr);
      console.log('busId', temp.busId);

      if (Object.keys(query).length > 0) {
        // taskId taskDefKey procInsId
        router.push({ path: '/task/handle/' + temp.busId, query: query });
      } else {
        router.push({ path: '/task/handle/' + temp.busId });
      }
    }
    closeModal();
  }
  //打印
  function onPrinter() {
    // 获取要打印的内容
    const printContent = document.querySelector('.daily-article');

    if (!printContent) return;

    // 创建一个iframe来处理打印
    const printFrame = document.createElement('iframe');
    printFrame.style.position = 'absolute';
    printFrame.style.width = '0';
    printFrame.style.height = '0';
    printFrame.style.border = 'none';
    printFrame.style.left = '-9999px';

    printFrame.onload = function () {
      const frameDoc = printFrame.contentDocument || printFrame.contentWindow?.document;
      if (!frameDoc) return;

      // 复制内容到iframe
      const clone = printContent.cloneNode(true);
      frameDoc.body.appendChild(clone);

      // 添加打印样式
      const style = frameDoc.createElement('style');
      style.innerHTML = `
        body {
          margin: 0;
          padding: 15px;
          font-family: Arial, sans-serif;
        }
        img {
          max-width: 100%;
          height: auto;
        }
        @page {
          size: auto;
          margin: 15mm;
        }
        @media print {
          body {
            padding: 0;
          }
        }
        .ant-card-meta-detail {
            display: flex !important ;
            justify-content: center !important;
            align-items: center !important;
            flex-direction: column !important;
        }
        .ant-card-meta-title {
            font-size: 22px !important;
            color: rgba(51, 51, 51, 0.88);
            font-weight: 600;
            font-size: 16px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
        }
        .ant-card .ant-card-meta-description {
            color: rgba(51, 51, 51, 0.45);
        }
      `;
      frameDoc.head.appendChild(style);

      // 确保图片加载完成
      const images = frameDoc.getElementsByTagName('img');
      let imagesToLoad = images.length;

      const printWhenReady = () => {
        if (imagesToLoad === 0) {
          setTimeout(() => {
            printFrame.contentWindow?.focus();
            printFrame.contentWindow?.print();
            document.body.removeChild(printFrame);
          }, 300);
        }
      };

      if (imagesToLoad === 0) {
        printWhenReady();
      } else {
        Array.from(images).forEach((img) => {
          img.onload = () => {
            imagesToLoad--;
            printWhenReady();
          };
          // 处理可能已经缓存的图片
          if (img.complete && img.naturalWidth !== 0) {
            imagesToLoad--;
            printWhenReady();
          }
        });
      }
    };

    document.body.appendChild(printFrame);
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
      console.log('glob.onlineUrl', glob.viewUrl);
      let url = encodeURIComponent(encryptByBase64(filePath));
      let previewUrl = `${glob.viewUrl}?url=` + url;
      window.open(previewUrl, '_blank');
    }
  }

  function handleVisibleChange(visible: boolean) {
    if (!visible) {
      emit('close');
    }
  }
</script>

<style scoped lang="less">
  .daily-article {
    :deep(.ant-card-meta-detail) {
      display: flex !important;
      justify-content: center !important;
      align-items: center !important;
      flex-direction: column !important;
    }
    :deep(.ant-card-meta-detail .ant-card-meta-title) {
      font-size: 22px !important;
    }
  }

  .print-btn {
    position: absolute;
    right: 100px;
    top: 20px;
    cursor: pointer;
    color: #a3a3a5;
    z-index: 999;
    .print-text {
      margin-left: 5px;
      font-size: 14px;
    }
    &:hover {
      color: #40a9ff;
    }
  }
  .detail-iframe {
    border: 0;
    width: 100%;
    height: 100%;
    min-height: 600px;
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

  .article-desc {
    display: flex;
    align-items: center;
    span:not(:first-child) {
      margin-left: 5px;
    }
  }
  /* 确保打印内容中的图片有最大宽度限制 */
  .article-content img {
    max-width: 100%;
    height: auto;
  }
  .basic-title{
    position: relative;
    display: flex;
    padding-left: 7px;
    font-size: 16px;
    font-weight: 500;
    line-height: 24px;
    color: rgba(0,0,0,0.88);
    cursor: move;
    -webkit-user-select: none;
    -moz-user-select: none;
    user-select: none;
  }
</style>
