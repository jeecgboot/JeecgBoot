<template>
  <a-card :bordered="false">
    <a-row>
      <!-- 左侧文件树 -->
      <a-col :span="4" class="clName">
        <a-tree :treeData="treeData" :defaultExpandAll="true" @select="onSelect" style="height: 500px; overflow-y: auto"> </a-tree>
      </a-col>
      <!--右侧缩略图-->
      <a-col :span="18">
        <div v-for="(file, key) in dataSource" :key="key">
          <a-col :span="24">
            <a-divider orientation="left">{{ file.fileName }}</a-divider>
          </a-col>
          <!-- 预览区域 -->
          <a-col :span="24">
            <template v-if="file.filePdfPath">
              <div @click="pdfPreview(file.title)">
                <img style="width: 80px; height: 80px" src="../../../assets/images/pdf4.jpg" />
              </div>
            </template>
            <template v-else> (暂无材料，点击"选择文件"或"扫描上传"上传文件) </template>
          </a-col>
        </div>
      </a-col>
    </a-row>
    <div style="display: none">
      <iframe id="pdfPreviewIframe" :src="url" frameborder="0" width="100%" height="550px" scrolling="auto"></iframe>
    </div>
  </a-card>
</template>

<script lang="ts">
  import { defineComponent, ref, unref, onMounted } from 'vue';
  import { useGlobSetting } from '/@/hooks/setting';
  import { getToken } from '/@/utils/auth';

  const mockdata = [
    {
      id: '1',
      key: '1',
      title: '实例.pdf',
      fileCode: 'shili',
      fileName: '实例',
      filePdfPath: '实例',
    },
  ];

  export default defineComponent({
    name: 'JeecgPdfView',
    setup() {
      const glob = useGlobSetting();
      const treeData = ref([
        {
          title: '所有PDF电子档',
          key: '0-0',
          children: mockdata,
        },
      ]);
      const dataSource = ref(mockdata);
      const allData = ref(mockdata);
      const url = ref(`${glob.domainUrl}/sys/common/pdf/pdfPreviewIframe`);

      /**
       * 打开iframe窗口
       * @param title
       */
      function pdfPreview(title) {
        let iframe = document.getElementById('pdfPreviewIframe');
        let json = { title: title, token: getToken() };
        iframe.contentWindow.postMessage(json, '*');
      }

      // 选择PDF文件
      function onSelect(selectedKeys, info) {
        dataSource.value = [];
        if (selectedKeys[0] === undefined || selectedKeys[0] === '0-0') {
          dataSource.value = unref(allData);
        } else {
          dataSource.value.push(info.node.dataRef);
        }
      }

      return {
        url,
        dataSource,
        treeData,
        allData,
        onSelect,
        pdfPreview,
      };
    },
  });
</script>
