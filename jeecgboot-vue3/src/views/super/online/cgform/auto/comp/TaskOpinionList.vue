<template>
  <!-- 意见区域 -->
  <div class="opinion-area" v-if="opinionPrint">
    <!-- 标题头 -->
    <div class="opinion-header">
      <h3>审批意见</h3>
      <div class="header-line"></div>
    </div>

    <!-- 意见列表 -->
    <div class="opinion-list">
      <a-list itemLayout="vertical" :split="false">
        <template v-for="(item, index) in bpmLogList" :key="index">
          <a-list-item class="opinion-item">
            <a-list-item-meta :description="item.remarks">
              <template #title>
                <div class="opinion-meta">
                  <span class="user-name">{{ item.opUserName }}</span>
                  <span class="task-tag">[{{ item.taskName }}]</span>
                  <span class="op-time">{{ formatTime(item.opTime) }}</span>
                </div>
              </template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </div>
  </div>
</template>

<script lang="ts" setup>
  // 监听流程流转信息
  import {inject, ref, watchEffect} from 'vue';
  import dayjs from 'dayjs';
  // 开源仓库不含 BPM 模块。静态 import 会在打包期解析失败（#9845）。
  // 使用 import.meta.glob 按需加载：有 BPM 源码时功能保持不变，没有时跳过请求。
  const bpmApiModules = import.meta.glob('../../../../bpm/process/personalOffice/myHandleTask/task.handle.api.ts');
  // 参数
  const props = defineProps({
    taskId: {
      type: String,
      default: '',
    },
    procInsId: {
      type: String,
      default: '',
    },
    processTabType: {
      type: String,
      default: '',
    }
  });

  const opinionPrint = inject('opinionPrint');
  // 审批记录/意见信息
  const bpmLogList = ref([]);

  // 监听流程流转信息
  watchEffect(() => {
    props.procInsId && getTaskTransInfo();
  });
  // 获取流程流转信息
  async function getTaskTransInfo() {
    const loader = Object.values(bpmApiModules)[0];
    if (!loader) {
      return;
    }
    const { taskTransInfo } = (await loader()) as { taskTransInfo: Function };
    let taskType = props.processTabType || 'history';
    //查询条件-run只需要taskId， history只需要procInstId
    let params = { taskId: props.taskId, procInstId: props.procInsId };
    let data = await taskTransInfo(params, taskType);
    bpmLogList.value = data.bpmLogList;
  }
  // 时间格式化
  function formatTime(time) {
    if (!time) return '';
    return dayjs(time).format('YYYY-MM-DD HH:mm');
  }
</script>

<style lang="less" scoped>
  /* 意见区域整体样式 */
  .opinion-area {
    margin-top: 24px;
    margin-bottom: 24px;
    padding: 16px;
    background-color: #f9f9f9;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  }

  /* 标题头样式 */
  .opinion-header {
    margin-bottom: 16px;
    position: relative;
  }

  .opinion-header h3 {
    color: #333;
    font-size: 18px;
    font-weight: 600;
    margin-bottom: 8px;
  }

  .header-line {
    height: 2px;
    background: linear-gradient(to right, #1890ff, transparent);
    width: 100%;
  }

  /* 意见列表样式 */
  .opinion-list {
    overflow-y: auto;
    padding-right: 8px;
  }

  /* 单个意见项样式 */
  .opinion-item {
    padding: 12px 16px;
    margin-bottom: 12px;
    background-color: #fff;
    border-radius: 6px;
    transition: all 0.3s;
  }

  .opinion-item:hover {
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
  }

  /* 用户信息样式 */
  .opinion-meta {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 8px;
  }

  .user-name {
    font-weight: 500;
    color: #333;
  }

  .task-tag {
    color: #ff6d75;
    font-size: 12px;
  }

  .op-time {
    color: #999;
    font-size: 12px;
  }

  /* 意见内容样式 */
  :deep(.ant-list-item-meta-description) {
    padding-left: 42px;
    color: #555;
    line-height: 1.6;
  }

  @media print {
    body * {
      visibility: hidden;
    }
    .daily-opinion,
    .daily-opinion * {
      visibility: visible;
    }
    .daily-opinion {
      position: absolute;
      left: 0;
      top: 0;
      width: 100%;
    }
  }
  table {
    page-break-inside: avoid;
  }
  ul,
  ol {
    page-break-inside: avoid;
  }
</style>
