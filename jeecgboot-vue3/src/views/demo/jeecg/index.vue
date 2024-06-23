<template>
  <div class="p-4">
    <a-card :bordered="false" style="height: 100%">
      <a-tabs v-model:activeKey="activeKey" @change="tabChange">
        <a-tab-pane key="JeecgComponents" tab="下拉选择组件"></a-tab-pane>
        <a-tab-pane key="JCodeEditDemo" tab="代码编辑器" force-render></a-tab-pane>
        <a-tab-pane key="JEditorDemo" tab="富文本&MakeDown"></a-tab-pane>
        <a-tab-pane key="ImgDragSort" tab="图片拖拽"></a-tab-pane>
        <a-tab-pane key="ImgTurnPage" tab="图片翻页"></a-tab-pane>
        <a-tab-pane key="JeecgPdfView" tab="PDF预览"></a-tab-pane>
        <a-tab-pane key="JUploadDemo" tab="文件上传"></a-tab-pane>
      </a-tabs>
      <component :is="currentComponent"></component>
    </a-card>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, computed } from 'vue';
  import JeecgComponents from './JeecgComponents.vue';
  import JEditorDemo from './JEditorDemo.vue';
  import JCodeEditDemo from './JCodeEditDemo.vue';
  import ImgDragSort from './ImgDragSort.vue';
  import ImgTurnPage from './ImgTurnPage.vue';
  import JeecgPdfView from './JeecgPdfView.vue';
  import JUploadDemo from './JUploadDemo.vue';
  export default defineComponent({
    name: 'comp-jeecg-basic',
    setup() {
      const activeKey = ref('JeecgComponents');
      const currentComponent = computed(() => {
        const componentType = {
          JeecgComponents: JeecgComponents,
          JEditorDemo: JEditorDemo,
          JCodeEditDemo: JCodeEditDemo,
          ImgDragSort: ImgDragSort,
          ImgTurnPage: ImgTurnPage,
          JeecgPdfView: JeecgPdfView,
          JUploadDemo: JUploadDemo,
        };
        return componentType[activeKey.value];
      });

      //使用component动态切换tab
      function tabChange(key) {
        activeKey.value = key;
      }
      return {
        activeKey,
        currentComponent,
        tabChange,
      };
    },
  });
</script>
