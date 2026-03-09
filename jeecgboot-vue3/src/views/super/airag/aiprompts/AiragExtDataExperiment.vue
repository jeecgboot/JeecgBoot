<template>
  <a-card class="ai-prompts-info">
    <AiragExtDataList ref="processRef" v-if="activeKey==='evaluator'"></AiragExtDataList>
  </a-card>
</template>

<script lang="ts" setup>
/**
 * 评估器 evaluator
 * 评测集 evaluationSet
 * 实验 experiment
 */
import { ref, onMounted } from 'vue';
import AiragExtDataList from './AiragExtDataList.vue'

/**
 * 评估器 evaluator
 * 评测集 evaluationSet
 * 实验 experiment
 */
const activeKey = ref('evaluator');
/**
 * 切换tab
 */
function handleChangePanel(key) {
  activeKey.value = key;
}

onMounted(()=>{
  activeKey.value = 'evaluator';
});
</script>

<style scoped lang="less">
.ai-prompts-info {
  margin: 10px;
  :deep(.ant-card-body) {
    padding: 12px 8px;
  }
  :deep(.ant-tabs-tabpane) {
    padding-left: 12px !important;
  }
}
</style>
