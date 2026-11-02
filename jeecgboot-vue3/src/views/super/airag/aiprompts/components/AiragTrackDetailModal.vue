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
      <!-- JSON内容显示区域 -->
      <div class="json-content-wrapper">
        <div class="content-header">
          <span class="content-status">{{ isFormatted ? '格式化内容' : '原始数据' }}</span>
          <a-button type="link" size="small" @click="toggleFormat" class="format-btn" :aria-pressed="isFormatted">
            <Icon :icon="isFormatted ? 'ant-design:compress-outlined' : 'ant-design:expand-outlined'" />
            {{ isFormatted ? '收起' : '展开' }}
          </a-button>
        </div>

        <div v-if="!trackContent" class="empty-state">
          <Icon icon="ant-design:inbox-outlined" style="font-size: 48px; color: #d9d9d9;" />
          <p>暂无轨迹数据</p>
        </div>

        <div v-else class="json-display">
          <pre v-if="isFormatted" >{{ formattedJson }}</pre>
          <pre v-else class="raw-json">{{ trackContent }}</pre>
        </div>
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
@text-color: #262626;
@text-secondary: #8c8c8c;

.track-detail-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 400px;
}

// JSON内容显示区域
.json-content-wrapper {
  flex: 1;
  overflow: auto;
  background: white;
  border: 1px solid @border-color;
  border-radius: 8px;
  position: relative;

  .content-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 42px;
    padding: 0 12px 0 16px;
    border-bottom: 1px solid @border-color;
  }

  .content-status {
    color: @text-secondary;
    font-size: 13px;
  }

  .format-btn {
    display: inline-flex;
    align-items: center;
    gap: 5px;
    padding: 0 4px;
    font-size: 13px;

    &:focus-visible {
      outline: 2px solid fade(@primary-color, 25%);
      outline-offset: 2px;
    }
  }

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
    padding: 16px;

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
  .json-content-wrapper {
    .json-display pre {
      font-size: 12px;
    }
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
