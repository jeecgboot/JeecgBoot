<template>
  <div class="ai-assessment-container">
    <!-- 标题和操作栏 -->
    <div class="assessment-header">
      <div class="header-left">
        <h5 class="title">AI内容评估</h5>
      </div>
      <div class="header-right" v-if="variable && variable.length > 0">
        <a-button type="primary" ghost class="clear-btn" @click="handleClear">
          <template #icon><DeleteOutlined /></template>
          清空
        </a-button>
        <a-button type="primary" :loading="loading" class="run-btn" @click="handleRun">
          <template #icon><PlayCircleOutlined /></template>
          运行
        </a-button>
      </div>
    </div>

    <!-- 评估内容区域 -->
    <div class="assessment-content" v-if="variable && variable.length > 0">
      <div class="input-section" v-for="item in variable" :key="item.name">
        <div class="section-header output-header">
          <span class="section-title">{{item.name}}</span>
        </div>
        <div class="section-content output-content">
          <a-input v-model:value="item.value" placeholder="请输入" />
        </div>
      </div>
    </div>
    <div v-else class="empty-container">
      <Empty
          :image="Empty.PRESENTED_IMAGE_SIMPLE"
          description="暂无变量"
          class="custom-empty"
      />
    </div>
    <!-- 结果内容区域 -->
    <div class="debug-result-container" v-if="result">
      <!-- 调试成功提示 -->
      <div class="debug-success">
        <div class="success-header">
          <div class="success-icon">✓</div>
          <span class="success-text">调试成功</span>
        </div>

        <!-- 分数显示 -->
<!--        <div class="score-section">-->
<!--          <span class="score-label">0 分</span>-->
<!--          <div class="score-note">-->
<!--            <span class="note-text">得分仅预览效果，非实际结果。</span>-->
<!--          </div>-->
<!--        </div>-->

        <!-- 原因分析 -->
        <div class="reason-section">
<!--          <div class="reason-label">原因:</div>-->
          <div class="reason-content">
            <span>{{result}}</span>
          </div>
        </div>

        <!-- 免责声明 -->
        <div class="disclaimer">
          <span class="disclaimer-text">内容由AI生成，无法确保真实准确，仅供参考。</span>
        </div>
      </div>

      <!-- 调试失败示例（可选显示） -->
      <div v-if="showFailureExample" class="debug-failure">
        <div class="failure-header">
          <div class="failure-icon">✗</div>
          <span class="failure-text">调试失败</span>
        </div>

        <div class="error-message">
          <span>代码中存在语法错误，请检查后重新调试。</span>
        </div>
      </div>
    </div>
    
    <!-- 底部说明 -->
<!--    <div class="assessment-footer">-->
<!--      <div class="footer-content">-->
<!--        <InfoCircleOutlined class="footer-icon" />-->
<!--        <span>内容由AI生成，无法确保真实准确，仅供参考。</span>-->
<!--      </div>-->
<!--    </div>-->
  </div>
</template>

<script setup>
import { ref, onMounted,watch } from 'vue'
import { Empty } from 'ant-design-vue';
import {
  DeleteOutlined,
  PlayCircleOutlined,
} from '@ant-design/icons-vue'

// 组件属性
const props = defineProps({
  // 原始内容
  content: {
    type: String,
    default: ``
  },
})
// 控制是否显示失败示例
const showFailureExample = ref(false)
// 运行状态
const loading = ref(false)
// 响应式数据
const variable = ref([])
// 结果
const result = ref("")
// 从内容中提取参数
const extractContent = () => {
  if(props.content){
    const vars = props.content.match(/{{\s*([^}\s]+)\s*}}/g);
    if(vars && vars.length > 0){
      variable.value = vars.map((match) => ({value:"",name:match.replace(/{{\s*|\s*}}/g, '')}))
    }
  }else{
    variable.value = []
  }
}

// 清空操作
const handleClear = () => {
  variable.value.forEach((item) => item.value = '')
}

// 运行操作
const handleRun = () => {
  // 触发运行事件
  emit('run', variable.value)
}

// 定义事件
const emit = defineEmits(['clear', 'run'])

// 生命周期
onMounted(() => {
  extractContent()
})

// 监听内容变化
watch(() => props.content, extractContent)

defineExpose({
  loading,
  result
})
</script>

<style scoped>
.ai-assessment-container {
  width: 100%;
  background: #fafafa;
  border: 1px solid #e8e8e8;
  border-radius: 8px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial;
  overflow: hidden;
}

/* 头部样式 */
.assessment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: #fff;
  border-bottom: 1px solid #e8e8e8;
  border-radius: 8px 8px 0 0;
}

.header-left {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.title {
  margin: 0;
  font-size: 14px;
  font-weight: 600;
  color: #262626;
}

.header-right {
  display: flex;
  gap: 12px;
}

.clear-btn {
  color: #ff4d4f;
  border-color: #ff4d4f;
}

.clear-btn:hover {
  color: #ff7875;
  border-color: #ff7875;
}

.run-btn {
  background: #1890ff;
  border-color: #1890ff;
}

.run-btn:hover {
  background: #40a9ff;
  border-color: #40a9ff;
}

/* 内容区域样式 */
.assessment-content {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 通用区块样式 */
.input-section,
.output-section {
  background: #fff;
  border: 1px solid #e8e8e8;
  border-radius: 6px;
  overflow: hidden;
}

.section-header {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
}

.section-title {
  font-size: 14px;
  font-weight: 500;
}

/* 输出区域 */
.output-header {
  background: #e6f7ff;
  border-bottom: 1px solid #91d5ff;
}

.output-icon {
  color: #1890ff;
}

.output-content {
  padding: 16px;
  background: #e6f7ff;
  color: #096dd9;
  font-size: 14px;
  line-height: 1.5;
  min-height: 80px;
}


/* 底部样式 */
.assessment-footer {
  padding: 12px 20px;
  background: #fff;
  border-top: 1px solid #e8e8e8;
  border-radius: 0 0 8px 8px;
}

.footer-content {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: #8c8c8c;
}

.footer-icon {
  font-size: 12px;
}
.debug-result-container {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, sans-serif;
  max-width: 600px;
  margin: 20px auto;
}

/* 成功样式 */
.debug-success {
  border: 1px solid #e1e4e8;
  border-radius: 6px;
  padding: 16px;
  background-color: #f6f8fa;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.success-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.success-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: #2ea44f;
  color: white;
  font-weight: bold;
  font-size: 14px;
  margin-right: 8px;
}

.success-text {
  font-size: 16px;
  font-weight: 600;
  color: #24292e;
}

/* 分数部分 */
.score-section {
  margin-bottom: 16px;
}

.score-label {
  display: inline-block;
  font-size: 14px;
  color: #24292e;
  margin-bottom: 4px;
}

.score-note {
  font-size: 12px;
  color: #57606a;
}

.note-text {
  display: inline-block;
  padding: 4px 8px;
  background-color: #f0f0f0;
  border-radius: 4px;
}

/* 原因部分 */
.reason-section {
  margin-bottom: 16px;
}

.reason-label {
  font-size: 14px;
  font-weight: 600;
  color: #24292e;
  margin-bottom: 4px;
}

.reason-content {
  font-size: 14px;
  color: #24292e;
  padding: 8px 12px;
  background-color: white;
  border: 1px solid #d0d7de;
  border-radius: 4px;
}

/* 免责声明 */
.disclaimer {
  padding: 8px;
  background-color: #fff8e6;
  border: 1px solid #ffd33d;
  border-radius: 4px;
}

.disclaimer-text {
  font-size: 12px;
  color: #57606a;
  font-style: italic;
}

/* 失败样式 */
.debug-failure {
  margin-top: 20px;
  border: 1px solid #e1e4e8;
  border-radius: 6px;
  padding: 16px;
  background-color: #fcf2f2;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.failure-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.failure-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: #cf222e;
  color: white;
  font-weight: bold;
  font-size: 14px;
  margin-right: 8px;
}

.failure-text {
  font-size: 16px;
  font-weight: 600;
  color: #cf222e;
}

.error-message {
  font-size: 14px;
  color: #24292e;
  padding: 8px 12px;
  background-color: white;
  border: 1px solid #d0d7de;
  border-radius: 4px;
}
/* 响应式设计 */
@media (max-width: 768px) {
  .assessment-header {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }

  .header-right {
    justify-content: flex-end;
  }
}
.empty-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 200px;
  padding: 40px 0;
}

.custom-empty {
  :deep(.ant-empty-image) {
    height: 80px;
    margin-bottom: 16px;
  }

  :deep(.ant-empty-description) {
    font-size: 14px;
    color: #8c8c8c;
  }
}
</style>
