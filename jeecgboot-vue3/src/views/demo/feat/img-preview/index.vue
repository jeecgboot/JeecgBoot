<template>
  <PageWrapper title="图片预览示例">
    <h1>有预览图</h1>
    <ImagePreview :imageList="imgList" />
    <a-divider />
    <h1>无预览图</h1>
    <a-button @click="openImg" type="primary">点击预览</a-button>
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { createImgPreview, ImagePreview } from '/@/components/Preview/index';
  import { PageWrapper } from '/@/components/Page';
  import { ImageProps } from '/@/components/Preview/src/typing';

  const imgList: ImageProps[] = [
    { src: 'https://jeecgos.oss-cn-beijing.aliyuncs.com/upload/test/login_1658829954004.png', width: 300 },
    { src: 'https://jeecgos.oss-cn-beijing.aliyuncs.com/upload/test/home_index_1658830084684.png', width: 300 },
    { src: 'https://jeecgos.oss-cn-beijing.aliyuncs.com/upload/test/design_1658830200539.png', width: 300 },
    { src: 'https://static.jeecg.com/upload/test/13_1592320121058.png', width: 300 },
    { src: 'https://static.jeecg.com/upload/test/16_1592320251436.png', width: 300 },
  ];
  export default defineComponent({
    components: { PageWrapper, ImagePreview },
    setup() {
      function openImg() {
        const onImgLoad = ({ index, url, dom }) => {
          console.log(`第${index + 1}张图片已加载，URL为：${url}`, dom);
        };
        // 可以使用createImgPreview返回的 PreviewActions 来控制预览逻辑，实现类似幻灯片、自动旋转之类的骚操作
        let imageList = imgList.map<string>((i) => i.src);
        createImgPreview({ imageList: imageList, defaultWidth: 700, rememberState: true, onImgLoad });
      }

      return { imgList, openImg };
    },
  });
</script>
