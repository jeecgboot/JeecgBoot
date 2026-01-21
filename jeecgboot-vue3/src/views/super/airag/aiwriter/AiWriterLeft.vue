<template>
  <div class="writer-sidebar">
    <div class="mode-tabs">
      <div class="tab-item" :class="{ active: activeMode === 'compose' }" @click="activeModeClick('compose')"> 撰写 </div>
      <div class="tab-item" :class="{ active: activeMode === 'reply' }" @click="activeModeClick('reply')"> 回复 </div>
    </div>

    <div class="form-content">
      <a-form layout="vertical">
        <template v-if="activeMode === 'reply'">
          <a-form-item>
            <template #label>
              <div style="display: flex; justify-content: space-between; width: 100%; align-items: center">
                <span>原文</span>
                <span class="example-link" @click="fillExample">示例</span>
              </div>
            </template>
            <a-textarea
              v-model:value="formData.originalContent"
              :auto-size="{ minRows: 4, maxRows: 6 }"
              :maxlength="500"
              show-count
              placeholder="请输入原文"
            />
          </a-form-item>
          <a-form-item label="回复内容">
            <a-textarea
              v-model:value="formData.prompt"
              :auto-size="{ minRows: 4, maxRows: 6 }"
              :maxlength="500"
              show-count
              placeholder="请输入回复内容"
            />
          </a-form-item>
        </template>
        <template v-else>
          <a-form-item>
            <template #label>
              <div style="display: flex; justify-content: space-between; width: 100%; align-items: center">
                <span>写作内容</span>
                <span class="example-link" @click="fillComposeExample">示例</span>
              </div>
            </template>
            <a-textarea
              v-model:value="formData.prompt"
              :auto-size="{ minRows: 6, maxRows: 12 }"
              :maxlength="500"
              show-count
              placeholder="请输入写作内容"
            />
          </a-form-item>
        </template>

        <a-form-item label="长度">
          <div class="tag-group">
            <span
              v-for="item in options.length"
              :key="item.value"
              class="custom-tag"
              :class="{ active: formData.length === item.value }"
              @click="formData.length = item.value"
            >
              {{ item.label }}
            </span>
          </div>
        </a-form-item>

        <a-form-item label="格式">
          <div class="tag-group">
            <span
              v-for="item in options.format"
              :key="item.value"
              class="custom-tag"
              :class="{ active: formData.format === item.value }"
              @click="formData.format = item.value"
            >
              {{ item.label }}
            </span>
          </div>
        </a-form-item>

        <a-form-item label="语气">
          <div class="tag-group">
            <span
              v-for="item in options.tone"
              :key="item.value"
              class="custom-tag"
              :class="{ active: formData.tone === item.value }"
              @click="formData.tone = item.value"
            >
              {{ item.label }}
            </span>
          </div>
        </a-form-item>

        <a-form-item label="语言">
          <div class="tag-group">
            <span
              v-for="item in options.language"
              :key="item.value"
              class="custom-tag"
              :class="{ active: formData.language === item.value }"
              @click="formData.language = item.value"
            >
              {{ item.label }}
            </span>
          </div>
        </a-form-item>
      </a-form>
    </div>

    <div class="action-footer">
      <a-button @click="handleReset" style="margin-right: 10px">重置</a-button>
      <a-button type="primary" @click="handleGenerate">生成</a-button>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive } from 'vue';
  import { useMessage } from '@/hooks/web/useMessage';

  const emit = defineEmits(['generate', 'reset', 'update:modelValue']);
  const { createMessage } = useMessage();
  const activeMode = ref('compose');

  const formData = reactive({
    prompt: '',
    originalContent: '',
    length: '自动',
    format: '自动',
    tone: '自动',
    language: '自动',
    activeMode: 'compose',
  });

  const options = {
    //长度
    length: [
      { label: '自动', value: '自动' },
      { label: '短', value: '短' },
      { label: '中等', value: '中等' },
      { label: '长', value: '长' },
    ],
    //格式化
    format: [
      { label: '自动', value: '自动' },
      { label: '电子邮件', value: '电子邮件' },
      { label: '消息', value: '消息' },
      { label: '评论', value: '评论' },
      { label: '段落', value: '段落' },
      { label: '文章', value: '文章' },
      { label: '博客文章', value: '博客文章' },
      { label: '想法', value: '想法' },
      { label: '大纲', value: '大纲' },
    ],
    //语气
    tone: [
      { label: '自动', value: '自动' },
      { label: '友善', value: '友善' },
      { label: '随意', value: '随意' },
      { label: '专业', value: '专业' },
      { label: '诙谐', value: '诙谐' },
      { label: '有趣', value: '有趣' },
      { label: '正式', value: '正式' },
    ],
    //语言
    language: [
      { label: '自动', value: '自动' },
      { label: '中文', value: '中文' },
      { label: '英文', value: '英文' },
      { label: '韩语', value: '韩语' },
      { label: '日语', value: '日语' },
    ],
  };

  /**
   * 撰写和回复切换事件
   * @param type
   */
  function activeModeClick(type) {
    activeMode.value = type;
    formData.originalContent = '';
    formData.prompt = '';
  }

  /**
   * 重置
   */
  function handleReset() {
    formData.originalContent = '';
    formData.prompt = '';
    formData.length = '自动';
    formData.format = '自动';
    formData.tone = '自动';
    formData.language = '自动';
    formData.activeMode = 'compose';
    emit('reset');
  }

  /**
   * 生成
   */
  function handleGenerate() {
    if (activeMode.value === 'compose' && !formData.prompt) {
      createMessage.warn('请填写写作内容!');
      return;
    }
    if (activeMode.value === 'reply' && !formData.prompt) {
      createMessage.warn('请填写原文!');
      return;
    }
    if (activeMode.value === 'reply' && !formData.originalContent) {
      createMessage.warn('请填写回复!');
      return;
    }
    formData.activeMode = activeMode.value;
    emit('generate', { ...formData, mode: activeMode.value });
  }

  /**
   * 回复示例
   */
  function fillExample() {
    formData.originalContent = '关于下周一项目进度会议的安排通知。';
    formData.prompt = '我已收到，下周一会准时前往';
  }

  /**
   * 创作示例
   */
  function fillComposeExample() {
    formData.prompt = '介绍一下 Vue3 的整体架构';
    formData.originalContent = '';
  }
</script>

<style scoped lang="less">
  .writer-sidebar {
    display: flex;
    flex-direction: column;
    height: 100%;
    position: relative;

    .mode-tabs {
      display: flex;
      background: #f1f3f6;
      border-radius: 8px;
      padding: 4px;
      margin-bottom: 16px;
      flex-shrink: 0;
      position: sticky;
      top: 0;
      z-index: 5;

      .tab-item {
        flex: 1;
        text-align: center;
        padding: 6px 0;
        font-size: 14px;
        color: #666;
        cursor: pointer;
        border-radius: 6px;
        transition: all 0.3s;

        &.active {
          background: #fff;
          color: #1677ff;
          font-weight: 500;
          box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
        }
      }
    }

    .form-content {
      flex: 1;
      overflow-y: auto;
      padding-right: 8px;

      &::-webkit-scrollbar {
        width: 4px;
      }
      &::-webkit-scrollbar-thumb {
        background: #ccc;
        border-radius: 2px;
      }
    }

    .action-footer {
      padding-top: 16px;
      display: flex;
      justify-content: center;
      border-top: 1px solid #f0f0f0;
      flex-shrink: 0;
      position: sticky;
      bottom: 0;
      background: #fff;
      z-index: 5;
    }

    .tag-group {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
    }

    .custom-tag {
      padding: 2px 10px;
      border: 1px solid #d9d9d9;
      border-radius: 4px;
      font-size: 12px;
      color: #666;
      cursor: pointer;
      transition: all 0.3s;
      background: #fff;

      &:hover {
        color: #1677ff;
        border-color: #1677ff;
      }

      &.active {
        color: #1677ff;
        border-color: #1677ff;
        background: #e6f7ff;
      }
    }

    .example-link {
      color: #1677ff;
      cursor: pointer;
      font-size: 12px;
      float: right;
    }

    :deep(.ant-form-item-label) {
      display: flex;
      justify-content: space-between;
      width: 100%;

      > label {
        flex: 1;
      }

      .ant-form-item-extra {
        min-height: unset;
      }
    }

    // Custom fix to put extra in label line if possible, but Ant Design puts extra below.
    // So I used a slot in label or handled it via absolute positioning if needed.
    // But standard ant form label slot might be better.
    // Above I used #extra slot which goes below.
    // I put example-link in #extra slot of form-item in template.
    // Actually, I should probably put it in the label slot if I want it on the same line.
  }
</style>
