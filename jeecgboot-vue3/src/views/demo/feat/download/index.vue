<template>
  <PageWrapper title="文件下载示例">
    <a-alert message="根据后台接口文件流下载" />
    <a-button type="primary" class="my-4" @click="handleDownByData"> 文件流下载 </a-button>

    <a-alert message="根据文件地址下载文件" />
    <a-button type="primary" class="my-4" @click="handleDownloadByUrl"> 文件地址下载 </a-button>

    <a-alert message="base64流下载" />
    <a-button type="primary" class="my-4" @click="handleDownloadByBase64"> base64流下载 </a-button>

    <a-alert message="图片Url下载,如果有跨域问题，需要处理图片跨域" />
    <a-button type="primary" class="my-4" @click="handleDownloadByOnlineUrl"> 图片Url下载 </a-button>
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { downloadByUrl, downloadByData, downloadByBase64, downloadByOnlineUrl } from '/@/utils/file/download';
  import imgBase64 from './imgBase64';
  import { PageWrapper } from '/@/components/Page';
  import { Alert } from 'ant-design-vue';

  export default defineComponent({
    components: { PageWrapper, [Alert.name]: Alert },
    setup() {
      function handleDownByData() {
        downloadByData('text content', 'testName.txt');
      }
      function handleDownloadByUrl() {
        downloadByUrl({
          url: 'https://codeload.github.com/anncwb/vue-Jeecg-admin-doc/zip/master',
          target: '_self',
        });

        downloadByUrl({
          url: 'https://vebn.oss-cn-beijing.aliyuncs.com/Jeecg/logo.png',
          target: '_self',
        });
      }

      function handleDownloadByBase64() {
        downloadByBase64(imgBase64, 'logo.png');
      }

      function handleDownloadByOnlineUrl() {
        downloadByOnlineUrl(
          'https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/5944817f47b8408e9f1442ece49d68ca~tplv-k3u1fbpfcp-watermark.image',
          'logo.png'
        );
      }
      return {
        handleDownloadByUrl,
        handleDownByData,
        handleDownloadByBase64,
        handleDownloadByOnlineUrl,
      };
    },
  });
</script>
