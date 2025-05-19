<template>
  <div ref="swaggerUiRef" style="height: 100%;"></div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
// 尝试直接导入 SwaggerUI 而不是使用 * as
import SwaggerUI from 'swagger-ui-dist/swagger-ui-bundle'; // 确保使用 ESM 版本
import 'swagger-ui-dist/swagger-ui.css';

import { getOpenApiJson } from './OpenApi.api';

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
</script>

<style scoped>
/* 确保容器有高度 */
.swagger-ui-container {
  height: 100%;
}
</style>
