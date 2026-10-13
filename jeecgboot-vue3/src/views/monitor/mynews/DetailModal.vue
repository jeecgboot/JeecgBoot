<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    title="查看详情"
    :width="800"
    :height="500"
    :showCancelBtn="false"
    :showOkBtn="false"
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
    <!-- update-begin--author:liaozhiyang---date:20260807---for:【LHZP-1253】我的消息查看弹窗样式对齐通知公告查看弹窗 -->
    <div class="daily-article">
      <div class="article-header">
        <div class="article-title">{{ content.titile }}</div>
        <div class="article-meta">
          <span v-if="priorityLabel" class="priority-tag article-meta-item" :class="`priority-tag--${content.priority}`">{{ priorityLabel }}</span>
          <span v-if="content.sender" class="meta-name article-meta-item">{{ content.sender }}</span>
          <span v-if="content.sendTime" class="meta-date article-meta-item">{{ formatSendDate(content.sendTime) }}</span>
          <span v-if="content.visitsNum" class="meta-visits article-meta-item">
            <a-tooltip placement="top" title="访问次数" :autoAdjustOverflow="true">
              <eye-outlined class="item-icon" /> {{ content.visitsNum }}
            </a-tooltip>
          </span>
        </div>
      </div>
      <div v-html="removeSpecialTags(content.msgContent)" class="article-content"></div>
      <div v-if="hasHref" class="article-action">
        <a-button @click="jumpToHandlePage">前往办理<ArrowRightOutlined /></a-button>
      </div>
    </div>
    <!-- update-end--author:liaozhiyang---date:20260807---for:【LHZP-1253】我的消息查看弹窗样式对齐通知公告查看弹窗 -->
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
  import { ref, unref, computed } from 'vue';
  import dayjs from 'dayjs';
  import { getDictItemsByCode } from '/@/utils/dict';
  import { getElectronFileUrl, getFileAccessHttpUrl } from '@/utils/common/compUtils';
  import { useGlobSetting } from '@/hooks/setting';
  import { encryptByBase64 } from '@/utils/cipher';
  import { getToken } from '@/utils/auth';
  import {defHttp} from "@/utils/http/axios";
  import {$electron} from "@/electron";
  import { decodeHtmlEntities, removeSpecialTags } from '@/utils/index';
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
        // update-begin--author:liaozhiyang---date:20260807---for:【LHZP-1128】修复thead标签没解析出来
        // 先解码 HTML 实体（修复 thead 等标签被转义后与 table 等未转义标签混存的问题），再 XSS 过滤
        data.record.msgContent = xss(decodeHtmlEntities(data.record.msgContent), options);
        // update-end--author:liaozhiyang---date:20260807---for:【LHZP-1128】修复thead标签没解析出来
      }

      // 代码逻辑说明: [QQYUN-12521]通知公告消息增加访问量
      if (!data.record?.busId) {
        await addVisitsNum({ id: data.record.id });
      }

      content.value = data.record;
      if(content.value.sender){
        // 发送人账号转姓名改用只读轻接口 queryByNames（仅返回 username/realname，无需 system:user:select 权限）
        const userList = await defHttp.get({ url: '/sys/user/queryByNames', params: { userNames: content.value.sender } });
        content.value.sender = userList && userList.length > 0
            ? userList.find((item) => item.username === content.value.sender)?.realname || content.value.sender
            : content.value.sender;
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

  // update-begin--author:liaozhiyang---date:20260807---for:【LHZP-1253】我的消息查看弹窗样式对齐通知公告查看弹窗
  const priorityLabel = computed(() => {
    if (!content.value?.priority) {
      return '';
    }
    const dictItems = getDictItemsByCode('priority') || [];
    const matched = dictItems.find((item) => item.value == content.value.priority);
    return matched?.text || '';
  });

  function formatSendDate(date?: string) {
    if (!date) {
      return '';
    }
    return dayjs(date).format('YYYY年MM月DD日 HH:mm:ss');
  }
  // update-end--author:liaozhiyang---date:20260807---for:【LHZP-1253】我的消息查看弹窗样式对齐通知公告查看弹窗

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
          padding: 0;
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
        .daily-article {
          padding: 20px 16px 12px;
        }
        .article-title {
          font-size: 22px;
          line-height: 1.4;
          margin: 0 0 14px;
          font-weight: 600;
          color: rgba(0, 0, 0, 0.88);
        }
        .article-meta {
          margin-bottom: 22px;
          line-height: 20px;
          font-size: 0;
        }
        .article-meta-item {
          display: inline-block;
          vertical-align: middle;
          margin: 0 10px 10px 0;
          font-size: 15px;
        }
        .meta-name,
        .meta-date,
        .meta-visits {
          color: rgba(0, 0, 0, 0.3);
        }
        .meta-visits {
          display: inline-flex;
          align-items: center;
        }
        .meta-visits .item-icon {
          margin-right: 4px;
        }
        .priority-tag {
          padding: 0 4px;
          font-size: 12px;
          line-height: 1.67;
          border: 1px solid #d9d9d9;
          border-radius: 4px;
          background: rgba(0, 0, 0, 0.05);
          color: rgba(0, 0, 0, 0.3);
          margin-right: 8px;
          -webkit-print-color-adjust: exact;
          print-color-adjust: exact;
        }
        .priority-tag--H {
          color: #f5222d;
          background: #fff1f0;
          border-color: #ffcfbf;
        }
        .priority-tag--M {
          color: #fa8c16;
          background: #fff7e6;
          border-color: #ffe59a;
        }
        .priority-tag--L {
          color: rgba(0, 0, 0, 0.3);
          background: rgba(0, 0, 0, 0.05);
          border-color: #d9d9d9;
        }
        .article-content {
          color: rgba(0, 0, 0, 0.88);
          font-size: 14px;
          line-height: 1.8;
        }
        .article-content table {
          width: 100%;
          border-collapse: collapse !important;
          border-spacing: 0 !important;
        }
        .article-content table td,
        .article-content table th {
          border: 1px solid #d0d0d0;
          padding: 8px 12px;
          min-width: 20px;
          word-break: break-word;
        }
        .article-content table thead th {
          background-color: #fafafa;
          font-weight: 600;
          color: rgba(0, 0, 0, 0.88);
          -webkit-print-color-adjust: exact;
          print-color-adjust: exact;
        }
        .article-action {
          margin-top: 16px;
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
  // update-end--author:liaozhiyang---date:20260807---for:【LHZP-1253】打印样式对齐通知公告 showContent.ftl

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
      //update-begin-author:scott---date:2026-04-16--for: 【Github #8855】修复文件预览路径处理问题，filePath需要先拼接完整URL再编码
      let url = encodeURIComponent(encryptByBase64(getFileAccessHttpUrl(filePath)));
      //update-end-author:scott---date:2026-04-16--for: 【Github #8855】修复文件预览路径处理问题，filePath需要先拼接完整URL再编码
      let previewUrl = `${glob.viewUrl}?url=` + url;
      //update-begin-author:liusq---date:2025-12-16--for: JHHB-1139桌面端 文件预览统一修改 
      if($electron.isElectron()){
        previewUrl = getElectronFileUrl(filePath);
      }
      //update-end-author:liusq---date:2025-12-16--for: JHHB-1139桌面端 文件预览统一修改
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
  /* update-begin--author:liaozhiyang---date:20260807---for:【LHZP-1253】我的消息查看弹窗样式对齐通知公告查看弹窗 */
  .daily-article {
    padding: 20px 16px 12px;
  }

  .article-title {
    font-size: 22px;
    line-height: 1.4;
    margin: 0 0 14px;
    font-weight: 600;
    color: rgba(0, 0, 0, 0.88);
    word-break: break-word;
  }

  .article-meta {
    margin-bottom: 22px;
    line-height: 20px;
    font-size: 0;
  }

  .article-meta-item {
    display: inline-block;
    vertical-align: middle;
    margin: 0 10px 10px 0;
    font-size: 15px;
  }

  .priority-tag {
    padding: 0 4px;
    font-size: 12px;
    line-height: 1.67;
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    background: rgba(0, 0, 0, 0.05);
    color: rgba(0, 0, 0, 0.3);
    margin-right: 8px;
  }

  .priority-tag--H {
    color: #f5222d;
    background: #fff1f0;
    border-color: #ffcfbf;
  }

  .priority-tag--M {
    color: #fa8c16;
    background: #fff7e6;
    border-color: #ffe59a;
  }

  .priority-tag--L {
    color: rgba(0, 0, 0, 0.3);
    background: rgba(0, 0, 0, 0.05);
    border-color: #d9d9d9;
  }

  .meta-name,
  .meta-date,
  .meta-visits {
    color: rgba(0, 0, 0, 0.3);
  }

  .meta-visits {
    display: inline-flex;
    align-items: center;

    .item-icon {
      margin-right: 4px;
    }
  }

  .article-action {
    margin-top: 16px;
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
    min-height: 500px;
    display: block;
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

  .article-content {
    color: rgba(0, 0, 0, 0.88);
    font-size: 14px;
    line-height: 1.8;

    :deep(table) {
      width: 100%;
      border-collapse: collapse !important;
      border-spacing: 0 !important;
    }
    :deep(table td),
    :deep(table th) {
      border: 1px solid #d0d0d0;
      padding: 8px 12px;
      min-width: 20px;
      word-break: break-word;
    }
    :deep(table thead th) {
      background-color: #fafafa;
      font-weight: 600;
      color: rgba(0, 0, 0, 0.88);
    }
  }

  /* 确保打印内容中的图片有最大宽度限制 */
  .article-content img {
    max-width: 100%;
    height: auto;
  }
  /* update-end--author:liaozhiyang---date:20260807---for:【LHZP-1253】我的消息查看弹窗样式对齐通知公告查看弹窗 */
  .basic-title{
    position: relative;
    display: flex;
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
