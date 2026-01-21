<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :canFullscreen="true"
    defaultFullscreen
    destroyOnClose
    title="评测调试"
    :showOkBtn="false"
    :showCancelBtn="false"
    wrapClassName="evaluator-debug-modal"
  >
    <!-- 步骤条 -->
    <a-steps :current="currentStep" class="steps-container">
      <a-step title="选择提示词">
        <template #icon>
          <Icon icon="ant-design:file-search-outlined" />
        </template>
      </a-step>
      <a-step title="调试配置">
        <template #icon>
          <Icon icon="ant-design:setting-outlined" />
        </template>
      </a-step>
    </a-steps>

    <!-- 第一步：选择提示词和字段映射 -->
    <div v-show="currentStep === 0" class="step-content step-one">
      <div class="form-section">
        <div class="form-item">
          <label class="form-label">选择提示词：</label>
          <a-select v-model:value="selectedPromptKey" placeholder="请选择提示词" class="prompt-select" @change="onPromptChange">
            <a-select-option v-for="prompt in promptOptions" :key="prompt.key" :value="prompt.key">
              <span class="prompt-option">
                <Icon icon="ant-design:file-text-outlined" />
                {{ prompt.name }}
              </span>
            </a-select-option>
          </a-select>
        </div>

        <div class="mapping-section" v-if="selectedPromptKey">
          <div class="section-header">
            <h3>
              <Icon icon="ant-design:link-outlined" />
              字段映射关系
            </h3>
          </div>
          <div class="mapping-list">
            <div class="mapping-item">
              <div class="field-label">
                <Icon icon="ant-design:tag-outlined" />
                用户输入[user_query]
              </div>
              <div class="equal-sign">=</div>
              <a-select allow-clear v-model:value="fieldMappings['user_query']" placeholder="请选择数据集字段" class="field-select">
                <a-select-option v-for="column in datasetColumns" :key="column.key" :value="column.key">
                  <span class="column-option">
                    <span style="color:blue">评测集</span>
                    <span> [{{ column.title }}]</span>
                    <span style="margin-left: 5px">
                      <a-tooltip :title="column.description"><Icon icon="ant-design:info-circle-outlined" size="14" /></a-tooltip>
                    </span>
                  </span>
                </a-select-option>
              </a-select>
            </div>
            <div class="mapping-item" v-for="(item, index) in promptParams" :key="index">
              <div class="field-label">
                <Icon icon="ant-design:tag-outlined" />
                变量[{{ item.name }}]
              </div>
              <div class="equal-sign">=</div>
              <a-select allow-clear v-model:value="fieldMappings[item.name]" placeholder="请选择数据集字段" class="field-select">
                <a-select-option v-for="column in datasetColumns" :key="column.key" :value="column.key">
                  <span class="column-option">
                    <span style="color:blue">评测集</span>
                    <span> [{{ column.title }}]</span>
                    <span style="margin-left: 5px">
                      <a-tooltip :title="column.description"><Icon icon="ant-design:info-circle-outlined" size="14" /></a-tooltip>
                    </span>
                  </span>
                </a-select-option>
              </a-select>
            </div>
          </div>
        </div>
      </div>

      <div class="form-footer">
        <a-button @click="closeModal" class="footer-btn cancel-btn">
          <Icon icon="ant-design:close-circle-outlined" />
          取消
        </a-button>
        <a-button type="primary" @click="nextStep" :disabled="!selectedPromptKey || !fieldMappings['user_query']" class="footer-btn next-btn">
          <Icon icon="ant-design:right-circle-outlined" />
          下一步
        </a-button>
      </div>
    </div>

    <!-- 第二步：调试配置和结果显示 -->
    <div v-show="currentStep === 1" class="step-content step-two">
      <div class="form-section">
        <div class="mapping-section">
          <div class="section-header">
            <h3>
              <Icon icon="ant-design:link-outlined" />
              字段映射
            </h3>
          </div>
          <div class="mapping-list">
            <div class="mapping-item" v-for="(item, index) in evaluatorFields" :key="index">
              <div class="field-label">
                <Icon icon="ant-design:tag-outlined" />
                评估器 [{{ item.field }}]
              </div>
              <div class="equal-sign">=</div>
              <a-select allow-clear v-model:value="fieldMappings[item.field]" placeholder="请选择数据集字段" class="field-select">
                <a-select-option v-for="column in evaluatorColumns" :key="column.name" :value="column.name">
                  <span class="column-option">
<!--                    <a-divider style="margin:4px 0;" v-if="column.label" />-->
                    <span :style="{color:column.label ? 'green' : 'blue'}"> [{{ column.label || '评测集' }}] </span>
                    <span>{{ column.name }}</span>
                    <span style="margin-left: 5px">
                      <a-tooltip :title="column.description">
                        <Icon icon="ant-design:info-circle-outlined" size="14"/>
                      </a-tooltip>
                    </span>
                  </span>
                </a-select-option>
              </a-select>
            </div>
          </div>
        </div>

        <div class="debug-result" v-if="debugResult.length > 0 || confirmLoading">
          <div class="section-header">
            <h3>
              <Icon icon="ant-design:bar-chart-outlined" />
              调试结果
            </h3>
          </div>
          <div class="debug-result-container">
            <!-- 条件渲染，当有调试结果时显示表格 -->
            <div class="result-table-container" v-if="debugResult.length > 0">
              <!-- 添加外部容器和标题 -->
              <div class="table-header">
                <div class="table-title" v-if="!confirmLoading">调试结果</div>
                <div class="table-title"  v-if="confirmLoading"><span>实验初始化中，请稍后点击<a href="javascript:void(0)" @click="handleReload">刷新</a></span></div>
                <div class="table-subtitle">实际输出与参考输出对比</div>
              </div>

              <!-- 表格区域 -->
              <a-table
                :columns="resultColumns"
                :dataSource="debugResult"
                :pagination="false"
                :scroll="{ x: 'max-content' }"
                class="result-table"
                :rowClassName="setRowClassName"
              >
                <!-- 自定义单元格渲染 -->
                <template #bodyCell="{ column, record, text }">
                  <!-- 状态列特殊样式 -->
                  <template v-if="column.dataIndex === 'status'">
                    <span :class="['status-badge', getStatusClass(record)]">
                      {{ text }}
                    </span>
                  </template>
                </template>
              </a-table>
            </div>

            <!-- 无数据时显示加载或提示 -->
            <div v-else class="no-data-container">
              <div class="loading-text" v-if="confirmLoading">
                <loading-outlined style="font-size: 24px; margin-right: 8px" @click="handleReload" />
                <span>实验初始化中，请稍后点击<a href="javascript:void(0)" @click="handleReload">刷新</a></span>
              </div>
              <div class="empty-text" v-else> 暂无调试结果 </div>
            </div>
          </div>
        </div>
      </div>

      <div class="form-footer">
        <a-button @click="prevStep" class="footer-btn prev-btn">
          <Icon icon="ant-design:left-circle-outlined" />
          上一步
        </a-button>
        <a-button v-if="debugResult.length == 0"  :disabled="canConfirmDebug" :loading="confirmLoading" type="primary" @click="confirmDebug" class="footer-btn confirm-btn">
          <Icon v-if="!confirmLoading" icon="ant-design:play-circle-outlined" />
          {{ confirmLoading ? '正在调试...' : '确认调试配置' }}
        </a-button>
        <a-button v-if="debugResult.length > 0" @click="closeModal" class="footer-btn close-btn">
          <Icon icon="ant-design:check-circle-outlined" />
          完成
        </a-button>
      </div>
    </div>
  </BasicModal>
</template>

<script lang="ts" setup>
  import { ref, reactive,computed  } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import Icon from '/@/components/Icon/src/Icon.vue';
  import { list, promptExperiment } from '../AiragPrompts.api';
  import { useMessage } from '@/hooks/web/useMessage';
  import { queryTrackById } from '@/views/super/airag/aiprompts/AiragExtData.api';
  import { LoadingOutlined } from '@ant-design/icons-vue';
  // Emits声明
  const emit = defineEmits(['register', 'success']);
  // 确认调试配置加载状态
  const confirmLoading = ref(false);
  // 提示信息
  const { createMessage } = useMessage();
  //核心数据
  const record = ref<any>({});
  // 步骤控制
  const currentStep = ref(0);
  // 选择调试的提示词
  const selectedPromptKey = ref<string>('');
  const selectedPrompt = ref<any>({});
  // 评估器字段配置
  const evaluatorFields = ref<any>([]);
  // 评估器下拉列配置
  const evaluatorColumns = ref<any>([]);
  //字段映射
  const fieldMappings = reactive<Record<string, string>>({});
  //页码配置
  const page = ref({
    pageNo: 1,
    pageSize: 10,
  });
  // 提示词选项数据
  const promptOptions = ref<any[]>([]);
  // 数据集列
  const datasetColumns = ref<any[]>([]);
  // 提示词参数
  const promptParams = ref<any[]>([]);
  // 调试结果
  const debugResult = ref<any[]>([]);
  // 结果表格列
  const resultColumns = ref<any[]>([
    { title: '问题', dataIndex: 'userQuery', key: 'userQuery',fixed: 'left' },
    { title: '提示词输出答案', dataIndex: 'promptAnswer', key: 'promptAnswer',fixed: 'left' },
    { title: '评分', dataIndex: 'answerScore', key: 'answerScore',fixed: 'left' },
  ]);

  // 确认调试配置
  const canConfirmDebug = computed(() => {
    let canConfirm = true;
    evaluatorFields.value.forEach((field) => {
      if(!fieldMappings[field.name]){
        canConfirm = false;
      }
    });
    return canConfirm;
  });
  // 表单赋值
  const [registerModal, { closeModal }] = useModalInner(async (data) => {
    //调试数据
    record.value = data.record;
    //重置数据
    resetForm();
    //查询提示词
    await getPromptList();
    //查询测评列配置
    getDatasetColumns();
    //查询评估器字段配置
    getEvaluatorFields();
    console.log('evaluatorColumns.value', evaluatorColumns.value);
  });

  /**
   * 获取数据集列字段
   */
  function getDatasetColumns() {
    let datasetValue = record.value?.datasetValue;
    if (datasetValue) {
      let columns = JSON.parse(datasetValue).columns;
      columns.forEach((item) => {
        if(item.name !== 'action') {
          datasetColumns.value.push({ ...item, key: item.name, title: item.name });
          evaluatorColumns.value.push({ ...item });
          if(!resultColumns.value.some(a=> a.dataIndex === item.name)){
            resultColumns.value.push({ title: item.name, dataIndex: item.name, key: item.name });
          }
        }
      });
    } else {
      createMessage.warning('未配置评测集信息！');
    }
  }
  /**
   * 获取数据集列字段
   */
  function getEvaluatorFields() {
    let dataValue = record.value?.dataValue;
    if (dataValue) {
      evaluatorFields.value = dataValue.match(/{{\s*([^}\s]+)\s*}}/g).map((match) => ({ field: match.replace(/{{\s*|\s*}}/g, '') }));
    } else {
      createMessage.warning('未配置评测集信息！');
    }
    //列配置
    evaluatorColumns.value.push({ name: 'actual_output', label: '评测对象', description: '实际输出', dateType: 'String' });
  }

  /**
   * 获取提示词
   */
  async function getPromptList() {
    const res = await list({ ...page });
    if (res?.records) {
      res?.records.forEach((item) => {
        promptOptions.value.push({ name: item.name, key: item.promptKey, value: item.promptKey, ...item });
      });
    }
  }
  // 重置表单
  function resetForm() {
    currentStep.value = 0;
    selectedPromptKey.value = '';
    promptOptions.value = [];
    datasetColumns.value = [];
    promptParams.value = [];
    evaluatorColumns.value = [];
    Object.keys(fieldMappings).forEach((key) => {
      delete fieldMappings[key];
    });
    debugResult.value = [];
  }

  // 提示词改变事件
  function onPromptChange(value: string) {
    //选中的提示词信息
    selectedPrompt.value = promptOptions.value.find((item) => item.value == value);
    // 清空之前的映射
    Object.keys(fieldMappings).forEach((key) => {
      delete fieldMappings[key];
    });
    let modelParam = selectedPrompt.value?.modelParam;
    if(modelParam){
      modelParam = typeof modelParam === 'string' ? JSON.parse(modelParam) : modelParam;
      if(modelParam?.promptVariables && modelParam?.promptVariables.length > 0){
        modelParam.promptVariables.forEach((item) => {
          promptParams.value.push({ name: item.name, label: item.name, description: item.description, dateType: 'String' });
        });
      }
    }
  }

  // 下一步
  function nextStep() {
    if (selectedPromptKey.value) {
      currentStep.value = 1;
    }
  }

  // 上一步
  function prevStep() {
    confirmLoading.value = false;
    debugResult.value = [];
    currentStep.value = 0;
  }

  // 确认调试配置
  async function confirmDebug() {
    // 模拟调试过程
    let params = {
      mappings: fieldMappings,
      promptKey: selectedPromptKey.value,
      extDataId: record.value.id,
    };
    console.log('开始调试...', params);
    confirmLoading.value = true;
    let res = await promptExperiment(params);
    console.log('结束调试res...', res);
    emit('success');
  }

  // 刷新评测集
  async function handleReload() {
    debugResult.value = []
    let res = await queryTrackById({ id: record.value.id });
    console.log('刷新评测集res...', res);
    if (!res.success) {
      createMessage.error(res.message);
      return;
    } else {
      if (res.result && res.result.length > 0) {
        console.log('刷新评测集res.result...', res.result);
        confirmLoading.value = false;
        // 1. 先找出最大version值
        const maxVersion = Math.max(...res.result.map(item => item.version));

        // 2. 过滤出所有具有最大version的项
        const maxVersionItems = res.result.filter(item => item.version === maxVersion);
       
        console.log('刷新评测集maxVersionItems...', maxVersionItems);
        if (maxVersionItems.length > 0) {
          maxVersionItems.forEach((item) => {
            debugResult.value.push(JSON.parse(item.dataValue));
          });
        }
      }else{
        createMessage.warning('数据处理中，请稍后刷新！');
      }
    }
  }

  // 状态样式类
  const getStatusClass = (record) => {
    if (record.status === '成功') return 'status-success';
    if (record.status === '失败') return 'status-failed';
    if (record.status === '警告') return 'status-warning';
    return '';
  };

  // 行样式
  const setRowClassName = (record, index) => {
    return index % 2 === 0 ? 'even-row' : 'odd-row';
  };
</script>

<style lang="less" scoped>
  @primary-color: #1890ff;
  @success-color: #52c41a;
  @warning-color: #faad14;
  @error-color: #ff4d4f;
  @border-color: #e8e8e8;
  @background-color: #f5f7fa;
  @text-color: #262626;
  @text-secondary: #8c8c8c;

  .steps-container {
    margin: 15px 0;
    padding: 30px;
    border-radius: 8px;
    background: linear-gradient(90deg, #f0f8ff, #e6f7ff);
    overflow: auto;
    max-height: calc(100vh - 200px);
    :deep(.ant-steps-item-icon) {
      background: white;
      border-color: @primary-color;
      width: 35px;
      .ant-steps-icon {
        color: @primary-color;
      }
    }

    :deep(.ant-steps-item-finish) {
      .ant-steps-item-icon {
        background: @primary-color;
        width: 35px;
        .ant-steps-icon {
          color: white;
        }
      }

      .ant-steps-item-title {
        color: @primary-color;
      }
    }

    :deep(.ant-steps-item-title) {
      font-weight: 500;
    }
  }

  .step-content {
    padding: 20px;
    border-radius: 8px;
    background-color: white;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);

    &.step-one {
      .prompt-select {
        width: 100%;
        font-size: 14px;
      }
    }

    &.step-two {
      .mapping-summary {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
        gap: 16px;
        margin-top: 16px;
      }

      .summary-item {
        display: flex;
        padding: 12px 16px;
        background-color: @background-color;
        border-radius: 6px;
        border: 1px solid @border-color;

        .field-key {
          font-weight: 500;
          color: @text-color;
          margin-right: 12px;
          min-width: 80px;

          &::after {
            content: ':';
          }
        }

        .field-value {
          color: @text-secondary;
          flex: 1;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }
  }

  .section-header {
    h3 {
      margin: 0 0 20px 0;
      font-size: 18px;
      font-weight: 600;
      color: @text-color;
      display: flex;
      align-items: center;
      gap: 8px;

      svg {
        color: @primary-color;
      }
    }
  }

  .form-section {
    margin-bottom: 24px;
  }

  .form-item {
    margin-bottom: 24px;

    .form-label {
      display: block;
      margin-bottom: 10px;
      font-weight: 500;
      font-size: 15px;
      color: @text-color;
    }

    .prompt-option {
      display: flex;
      align-items: center;
      gap: 8px;
    }
  }

  .mapping-section {
    margin-top: 30px;
    padding: 20px;
    background-color: @background-color;
    border-radius: 8px;
    border: 1px solid @border-color;

    .mapping-list {
      .mapping-item {
        display: flex;
        align-items: center;
        margin-bottom: 16px;
        padding: 16px;
        background: white;
        border-radius: 6px;
        border: 1px solid @border-color;
        transition: all 0.3s;

        &:hover {
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
          border-color: @primary-color;
        }

        .field-label {
          display: flex;
          align-items: center;
          gap: 8px;
          font-weight: 500;
          color: @text-color;
          min-width: 140px;
          padding-right: 16px;
        }

        .equal-sign {
          font-weight: bold;
          color: @text-secondary;
          margin: 0 12px;
        }

        .field-select {
          flex: 1;

          .column-option {
            display: flex;
            align-items: center;
            gap: 8px;
          }
        }
      }
    }
  }

  .debug-result {
    margin-top: 36px;
    padding: 20px;
    background-color: @background-color;
    border-radius: 8px;
    border: 1px solid @border-color;

    .result-table-container {
      margin-top: 16px;
      border-radius: 6px;
      overflow: hidden;
      border: 1px solid @border-color;

      :deep(.result-table) {
        .ant-table-thead > tr > th {
          background-color: #fafafa;
          font-weight: 600;
          color: @text-color;
        }

        .ant-table-tbody > tr:hover {
          background-color: #f0f8ff;
        }
      }
    }
  }

  .form-footer {
    display: flex;
    justify-content: flex-end;
    gap: 16px;
    padding-top: 24px;
    margin-top: 24px;
    border-top: 1px solid @border-color;

    .footer-btn {
      display: flex;
      align-items: center;
      gap: 6px;
      padding: 8px 20px;
      font-size: 14px;
      border-radius: 6px;
      transition: all 0.3s;

      &.cancel-btn {
        &:hover {
          color: @error-color;
          border-color: @error-color;
        }
      }

      &.next-btn,
      &.confirm-btn {
        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 8px rgba(24, 144, 255, 0.2);
        }
      }

      &.prev-btn {
        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
      }

      &.close-btn {
        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 8px rgba(82, 196, 26, 0.2);
        }
      }
    }
  }

  // 响应式设计
  @media (max-width: 768px) {
    .step-content {
      padding: 16px;
    }

    .mapping-section {
      padding: 16px;

      .mapping-list {
        .mapping-item {
          flex-direction: column;
          align-items: flex-start;

          .field-label {
            margin-bottom: 10px;
            padding-right: 0;
          }

          .equal-sign {
            display: none;
          }

          .field-select {
            width: 100%;
            margin-top: 8px;
          }
        }
      }
    }

    .step-two .mapping-summary {
      grid-template-columns: 1fr;
    }

    .form-footer {
      flex-direction: column;

      .footer-btn {
        width: 100%;
        justify-content: center;
      }
    }
  }

  .debug-result-container {
    margin: 16px 0;
  }

  .table-header {
    padding: 12px 16px;
    background: #fafafa;
    border: 1px solid #e8e8e8;
    border-bottom: none;
    border-radius: 4px 4px 0 0;
  }

  .table-title {
    font-size: 16px;
    font-weight: 600;
    color: #1890ff;
  }

  .table-subtitle {
    font-size: 12px;
    color: #999;
    margin-top: 4px;
  }

  .result-table {
    border: 1px solid #e8e8e8;
  }

  :deep(.ant-table-thead > tr > th) {
    background-color: #f5f5f5;
    font-weight: 600;
    color: #333;
    border-bottom: 2px solid #1890ff;
  }

  :deep(.ant-table-tbody > tr.even-row) {
    background-color: #fafafa;
  }

  :deep(.ant-table-tbody > tr.odd-row) {
    background-color: #fff;
  }

  :deep(.ant-table-tbody > tr:hover) {
    background-color: #e6f7ff !important;
  }

  .status-badge {
    display: inline-block;
    padding: 2px 8px;
    border-radius: 10px;
    font-size: 12px;
    font-weight: 500;
  }

  .status-success {
    background-color: #f6ffed;
    color: #52c41a;
    border: 1px solid #b7eb8f;
  }

  .status-failed {
    background-color: #fff2f0;
    color: #ff4d4f;
    border: 1px solid #ffccc7;
  }

  .status-warning {
    background-color: #fffbe6;
    color: #faad14;
    border: 1px solid #ffe58f;
  }

  .latency-text {
    font-weight: 500;
  }

  .latency-low {
    color: #52c41a;
  }

  .latency-medium {
    color: #faad14;
  }

  .latency-high {
    color: #ff4d4f;
  }

  .input-cell,
  .output-cell {
    padding: 4px 0;
  }

  .cell-label {
    font-size: 11px;
    color: #999;
    margin-bottom: 2px;
  }

  .cell-value {
    font-size: 13px;
    color: #333;
    word-break: break-word;
  }

  .output-cell.reference .cell-value {
    color: #1890ff;
  }

  .output-cell.actual .cell-value {
    color: #52c41a;
  }

  .table-footer {
    display: flex;
    justify-content: flex-start;
    gap: 24px;
    padding: 12px 16px;
    background: #fafafa;
    border: 1px solid #e8e8e8;
    border-top: none;
    border-radius: 0 0 4px 4px;
  }

  .footer-item {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .footer-label {
    font-size: 12px;
    color: #666;
  }

  .footer-value {
    font-size: 14px;
    font-weight: 500;
    color: #1890ff;
  }

  .no-data-container {
    padding: 40px;
    text-align: center;
    border: 1px dashed #d9d9d9;
    border-radius: 4px;
    background-color: #fafafa;
  }

  .loading-text {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    color: #666;
  }

  .empty-text {
    color: #999;
  }
</style>
