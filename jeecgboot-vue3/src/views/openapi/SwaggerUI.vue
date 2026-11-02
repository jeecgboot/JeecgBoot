<template>
  <div ref="swaggerUiRef" style="height: 100%;" @click="handleCopyClick"></div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
// 尝试直接导入 SwaggerUI 而不是使用 * as
import SwaggerUI from 'swagger-ui-dist/swagger-ui-bundle'; // 确保使用 ESM 版本
import 'swagger-ui-dist/swagger-ui.css';
import { useMessage } from '/@/hooks/web/useMessage';

import { getOpenApiJson } from './OpenApi.api';

const { createMessage } = useMessage();
const swaggerUiRef = ref<HTMLElement | null>(null);
const API_DOMAIN = import.meta.env.VITE_GLOB_DOMAIN_URL
onMounted(async () => {
  try {
    const response = await getOpenApiJson();
    const openApiJson = response;
    if (swaggerUiRef.value) {
      SwaggerUI({
        domNode: swaggerUiRef.value,
        spec: openApiJson,
      });
    }
  } catch (error) {
    console.error('Failed to fetch OpenAPI JSON:', error);
  }
});

/**
 * Swagger UI 自带的复制按钮（接口路径、Curl、示例值等）复制后没有任何反馈，这里统一补一个提示。
 * 按钮结构是 .copy-to-clipboard > svg/button，只有点中这个子元素才会真正复制，点按钮灰色边框不会。
 */
function handleCopyClick(e: MouseEvent) {
  if ((e.target as Element).closest('.copy-to-clipboard > *')) {
    createMessage.success('已复制');
  }
}
</script>

<style scoped>
/* 确保容器有高度 */
.swagger-ui-container {
  height: 100%;
}
</style>
