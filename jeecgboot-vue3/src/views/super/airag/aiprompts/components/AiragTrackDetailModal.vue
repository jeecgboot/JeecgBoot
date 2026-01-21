<template>
  <BasicModal
      v-bind="$attrs"
      @register="registerModal"
      :canFullscreen="true"
      :footer="null"
      destroyOnClose
      title="详情"
      wrapClassName="track-detail-modal"
  >
    <div class="track-detail-container">
      <!-- 顶部工具栏 -->
      <div class="toolbar">
        <a-button
            type="primary"
            size="small"
            @click="toggleFormat"
            class="format-btn"
        >
          <Icon :icon="isFormatted ? 'ant-design:compress-outlined' : 'ant-design:expand-outlined'" />
          {{ isFormatted ? '收起' : '展开' }}
        </a-button>
      </div>

      <!-- JSON内容显示区域 -->
      <div class="json-content-wrapper">
        <div v-if="!trackContent" class="empty-state">
          <Icon icon="ant-design:inbox-outlined" style="font-size: 48px; color: #d9d9d9;" />
          <p>暂无轨迹数据</p>
        </div>

        <div v-else class="json-display">
          <pre v-if="isFormatted" >{{ formattedJson }}</pre>
          <pre v-else class="raw-json">{{ trackContent }}</pre>
        </div>
      </div>

      <!-- 底部信息栏 -->
      <div v-if="trackContent" class="info-bar">
        <span class="info-item">
          <Icon icon="ant-design:info-circle-outlined" />
          {{ isFormatted ? '已格式化' : '原始数据' }}
        </span>
<!--        <span class="info-item">-->
<!--          <Icon icon="ant-design:file-text-outlined" />-->
<!--          字符数: {{ trackContent.length }}-->
<!--        </span>-->
      </div>
    </div>
  </BasicModal>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import Icon from '/@/components/Icon/src/Icon.vue';

// 轨迹详情
const trackContent = ref('');
const isFormatted = ref(true);

// 计算属性：格式化JSON
const formattedJson = computed(() => {
  try {
    // 尝试解析为JSON并格式化
    const jsonData = JSON.parse(trackContent.value);
    return JSON.stringify(jsonData, null, 2);
  } catch (e) {
    // 如果不是有效的JSON，返回原始内容
    return trackContent.value;
  }
});

// 切换格式化状态
const toggleFormat = () => {
  isFormatted.value = !isFormatted.value;
};

// 复制JSON内容
const copyJson = async () => {
  try {
    const content = isFormatted.value ? formattedJson.value : trackContent.value;
    await navigator.clipboard.writeText(content);
    // 这里可以添加一个轻提示
    console.log('JSON内容已复制到剪贴板');
  } catch (err) {
    console.error('复制失败:', err);
  }
};

// 表单赋值
const [registerModal] = useModalInner(async (data) => {
  trackContent.value = data?.dataValue || '';
  // 默认显示格式化内容
  isFormatted.value = true;
});
</script>

<style lang="less" scoped>
@primary-color: #1890ff;
@border-color: #e8e8e8;
@background-color: #f5f7fa;
@text-color: #262626;
@text-secondary: #8c8c8c;

.track-detail-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 400px;
}

// 顶部工具栏
.toolbar {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 16px;
  padding: 12px 16px;
  background: @background-color;
  border-radius: 8px;
  border: 1px solid @border-color;

  .format-btn {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 14px;
    transition: all 0.3s;

    &:hover {
      transform: translateY(-1px);
      box-shadow: 0 4px 8px rgba(24, 144, 255, 0.2);
    }
  }
}

// JSON内容显示区域
.json-content-wrapper {
  flex: 1;
  overflow: auto;
  background: white;
  border: 1px solid @border-color;
  border-radius: 8px;
  padding: 16px;
  position: relative;

  .empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 200px;
    color: @text-secondary;

    p {
      margin-top: 16px;
      font-size: 16px;
    }
  }

  .json-display {
    pre {
      margin: 0;
      padding: 0;
      font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
      font-size: 14px;
      line-height: 1.6;
      white-space: pre-wrap;
      word-break: break-all;
      max-height: 500px;
      overflow: auto;
    }

    .formatted-json {
      color: @text-color;

      // JSON语法高亮样式
      &::before {
        content: '{';
        color: #d73a49;
      }

      &::after {
        content: '}';
        color: #d73a49;
      }
    }

    .raw-json {
      color: @text-secondary;
      background: #fafafa;
      padding: 12px;
      border-radius: 4px;
      border-left: 4px solid @primary-color;
    }
  }
}

// 底部信息栏
.info-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
  padding: 12px 16px;
  background: @background-color;
  border-radius: 8px;
  border: 1px solid @border-color;
  font-size: 14px;
  color: @text-secondary;

  .info-item {
    display: flex;
    align-items: center;
    gap: 6px;

    svg {
      font-size: 16px;
    }
  }
}

// 滚动条样式
.json-content-wrapper::-webkit-scrollbar,
.json-display pre::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.json-content-wrapper::-webkit-scrollbar-track,
.json-display pre::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.json-content-wrapper::-webkit-scrollbar-thumb,
.json-display pre::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;

  &:hover {
    background: #a8a8a8;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .toolbar {
    padding: 10px 12px;
  }

  .json-content-wrapper {
    padding: 12px;

    .json-display pre {
      font-size: 12px;
    }
  }

  .info-bar {
    flex-direction: column;
    gap: 8px;
    align-items: flex-start;
  }
}
</style>

<style lang="less">
// 模态框样式调整
.track-detail-modal {
  .ant-modal-body {
    padding: 24px;
    min-height: 500px;
  }
}
</style>
