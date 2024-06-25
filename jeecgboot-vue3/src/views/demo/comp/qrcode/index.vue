<template>
  <PageWrapper title="二维码组件使用示例">
    <div class="flex flex-wrap">
      <CollapseContainer title="基础示例" :canExpan="true" class="text-center mb-6 qrcode-demo-item">
        <QrCode :value="qrCodeUrl" />
      </CollapseContainer>

      <CollapseContainer title="渲染成img标签示例" class="text-center mb-6 qrcode-demo-item">
        <QrCode :value="qrCodeUrl" tag="img" />
      </CollapseContainer>

      <CollapseContainer title="配置样式示例" class="text-center mb-6 qrcode-demo-item">
        <QrCode
          :value="qrCodeUrl"
          :options="{
            color: { dark: '#55D187' },
          }"
        />
      </CollapseContainer>

      <CollapseContainer title="本地logo示例" class="text-center mb-6 qrcode-demo-item">
        <QrCode :value="qrCodeUrl" :logo="LogoImg" />
      </CollapseContainer>

      <CollapseContainer title="在线logo示例" class="text-center mb-6 qrcode-demo-item">
        <QrCode
          :value="qrCodeUrl"
          logo="http://jeecg.com/images/logo.png"
          :options="{
            color: { dark: '#55D187' },
          }"
        />
      </CollapseContainer>

      <CollapseContainer title="logo配置示例" class="text-center mb-6 qrcode-demo-item">
        <QrCode
          :value="qrCodeUrl"
          :logo="{
            src: 'http://jeecg.com/images/logo.png',
            logoSize: 0.2,
            borderSize: 0.05,
            borderRadius: 50,
            bgColor: 'blue',
          }"
        />
      </CollapseContainer>

      <CollapseContainer title="下载示例" class="text-center qrcode-demo-item">
        <QrCode :value="qrCodeUrl" ref="qrRef" :logo="LogoImg" />
        <a-button class="mb-2" type="primary" @click="download"> 下载 </a-button>
        <div class="msg"> (在线logo会导致图片跨域，需要下载图片需要自行解决跨域问题) </div>
      </CollapseContainer>

      <CollapseContainer title="配置大小示例" class="text-center qrcode-demo-item">
        <QrCode :value="qrCodeUrl" :width="300" />
      </CollapseContainer>

      <CollapseContainer title="扩展绘制示例" class="text-center qrcode-demo-item">
        <QrCode :value="qrCodeUrl" :width="200" :options="{ margin: 5 }" ref="qrDiyRef" :logo="LogoImg" @done="onQrcodeDone" />
        <a-button class="mb-2" type="primary" @click="downloadDiy"> 下载 </a-button>
        <div class="msg"> 要进行扩展绘制则不能将tag设为img </div>
      </CollapseContainer>
    </div>
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, ref, unref } from 'vue';
  import { QrCode, QrCodeActionType } from '/@/components/Qrcode/index';
  import LogoImg from '/@/assets/images/logo.png';
  import { CollapseContainer } from '/@/components/Container/index';
  import { PageWrapper } from '/@/components/Page';

  const qrCodeUrl = 'https://www.vvbin.cn';
  export default defineComponent({
    components: { CollapseContainer, QrCode, PageWrapper },
    setup() {
      const qrRef = ref<Nullable<QrCodeActionType>>(null);
      const qrDiyRef = ref<Nullable<QrCodeActionType>>(null);
      function download() {
        const qrEl = unref(qrRef);
        if (!qrEl) return;
        qrEl.download('文件名');
      }
      function downloadDiy() {
        const qrEl = unref(qrDiyRef);
        if (!qrEl) return;
        qrEl.download('Qrcode');
      }

      function onQrcodeDone({ ctx }: any) {
        if (ctx instanceof CanvasRenderingContext2D) {
          // 额外绘制
          ctx.fillStyle = 'black';
          ctx.font = '16px "微软雅黑"';
          ctx.textBaseline = 'bottom';
          ctx.textAlign = 'center';
          ctx.fillText('你帅你先扫', 100, 195, 200);
        }
      }
      return {
        onQrcodeDone,
        qrCodeUrl,
        LogoImg,
        download,
        downloadDiy,
        qrRef,
        qrDiyRef,
      };
    },
  });
</script>
<style scoped>
  .qrcode-demo-item {
    width: 30%;
    margin-right: 1%;
  }
</style>
