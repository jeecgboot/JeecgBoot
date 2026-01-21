<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :canFullscreen="true"
    destroyOnClose
    title="列配置"
    okText="保存"
    :width="1000"
    @ok="handleSubmit"
  >
    <div class="dynamic-column-config">
      <a-card size="small">
        <!-- 列配置列表 -->
        <div class="column-list">
          <div v-for="(element, index) in columns" :key="element.id" class="column-item">
            <!-- 操作区域 - 放在右上角 -->
            <div class="action-area">
              <a-button type="text" @click="handleCopyColumn(element)" title="复制">
                <copy-outlined />
              </a-button>
              <a-button type="text" danger @click="handleRemoveColumn(index)" :disabled="columns.length < 2" title="删除">
                <DeleteOutlined />
              </a-button>
            </div>

            <!-- 列配置表单 -->
            <div class="column-form">
              <div class="form-row">
                <div class="form-item">
                  <label class="form-label">名称</label>
                  <a-input
                    v-model:value="element.name"
                    placeholder="请输入列名称"
                    :class="{ 'has-error': !element.name }"
                    @blur="validateColumn(element)"
                  />
                </div>

                <div class="form-item">
                  <label class="form-label">数据类型</label>
                  <a-select v-model:value="element.dataType" placeholder="请选择数据类型" style="width: 100%">
                    <a-select-option value="String">字符串</a-select-option>
                    <a-select-option value="FILE">附件</a-select-option>
                  </a-select>
                </div>

                <div class="form-item">
                  <label class="form-label">必填</label>
                  <a-radio-group v-model:value="element.required">
                    <a-radio :value="true">是</a-radio>
                    <a-radio :value="false">否</a-radio>
                  </a-radio-group>
                </div>
              </div>

              <div class="form-row">
                <div class="form-item full-width">
                  <label class="form-label">描述</label>
                  <a-textarea v-model:value="element.description" placeholder="请输入列描述" :rows="2" />
                </div>
              </div>
            </div>
          </div>
          <a-button type="dashed" style="width: 100%" @click="handleAddColumn"> <PlusOutlined />添加列 </a-button>
        </div>
        <!-- 底部操作按钮 -->
        <!--        <div class="footer-actions">-->
        <!--          <a-button type="primary" @click="handleSubmit" :loading="loading"> 创建 </a-button>-->
        <!--          <a-button @click="handleReset" style="margin-left: 8px"> 重置 </a-button>-->
        <!--        </div>-->
      </a-card>
    </div>
  </BasicModal>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { message } from 'ant-design-vue';
  import { PlusOutlined, DeleteOutlined, CopyOutlined } from '@ant-design/icons-vue';
  import { saveOrUpdate } from '../AiragExtData.api';
  import {JImageUpload} from "@/components/Form";
  // Emits声明
  const emit = defineEmits(['register', 'success']);
  // 列配置接口
  interface ColumnConfig {
    id: string;
    name: string;
    description: string;
    dataType: string;
    required: boolean;
  }

  // 响应式数据
  const loading = ref(false);
  const columns = ref<ColumnConfig[]>([
    {
      id: generateId(),
      name: '',
      description: '',
      dataType: 'String',
      required: false,
    },
  ]);
  const defaultColumns = [
    {
      id: generateId(),
      name: 'input',
      description: '作为输入投递给评测对象',
      dataType: 'String',
      required: false,
    },
    {
      id: generateId(),
      name: 'reference_output',
      description: '预期理想输出，可作为评估时的参考标准',
      dataType: 'String',
      required: false,
    },
  ];
  //数据ID
  const dataId = ref('');
  const datasetValue = ref<any>({});
  // 生成唯一ID
  function generateId(): string {
    return Date.now().toString(36) + Math.random().toString(36).substr(2);
  }
  //表单赋值
  const [registerModal, { closeModal }] = useModalInner(async (data) => {
    console.log('data:', data);
    dataId.value = data.id;
    if(data.datasetValue){
      datasetValue.value = data.datasetValue
      if(data.datasetValue?.columns && data.datasetValue.columns.length){
        columns.value = data.datasetValue.columns;
      }else{
        columns.value = defaultColumns;
      }
    }
  });
  // 添加列
  const handleAddColumn = () => {
    columns.value.push({
      id: generateId(),
      name: '',
      description: '',
      dataType: 'String',
      required: false,
    });
  };

  // 删除列
  const handleRemoveColumn = (index: number) => {
    if (columns.value.length > 1) {
      columns.value.splice(index, 1);
    }
  };
  // 复制列
  const handleCopyColumn = (element) => {
    columns.value.push({
      ...element,
      id: generateId(),
    });
  };

  // 验证列配置
  const validateColumn = (column: ColumnConfig) => {
    return column.name && column.name.trim() !== '';
  };

  // 提交配置
  const handleSubmit = async () => {
    // 验证数据
    const invalidColumns = columns.value.filter((col) => !validateColumn(col));

    if (invalidColumns.length > 0) {
      message.error('请填写所有必填项（名称）');
      return;
    }

    loading.value = true;

    try {
      datasetValue.value.columns = columns.value
      // 构造提交数据
      const submitData = {
        datasetValue: JSON.stringify( datasetValue.value),
        id: dataId.value,
      };

      console.log('提交数据:', submitData);

      await saveOrUpdate(submitData, true,false);

      message.success('配置创建成功！');
      // 关闭弹窗并触发成功事件
      closeModal();
      emit('success');
    } catch (error) {
      console.error('提交失败:', error);
    } finally {
      loading.value = false;
    }
  };

  // 重置配置
  const handleReset = () => {
    columns.value = [...defaultColumns];
  };
</script>

<style lang="less" scoped>
  .dynamic-column-config {
    max-width: 1200px;
    margin: 0 auto;
    max-height: 70vh; /* 设置最大高度为视口高度的70% */
    overflow-y: auto; /* 超出时显示垂直滚动条 */

    :deep(.ant-card-head) {
      border-bottom: 1px solid #f0f0f0;
    }

    :deep(.ant-card-body) {
      padding: 12px;
    }

    :deep(.ant-card-extra) {
      padding: 0;
    }
  }

  .column-list {
    margin-bottom: 24px;
  }

  .column-item {
    position: relative; /* 为绝对定位的操作区域提供参考 */
    display: flex;
    align-items: flex-start;
    gap: 12px;
    padding: 16px;
    margin-bottom: 16px;
    background: #fafafa;
    border: 1px solid #f0f0f0;
    border-radius: 6px;
    transition: all 0.3s;

    &:hover {
      border-color: #1890ff;
      background: #f6fbff;
    }

    /* 操作区域样式 */
    .action-area {
      position: absolute;
      top: 8px;
      right: 8px;

      :deep(.ant-btn) {
        width: 24px;
        height: 24px;
        min-width: 24px;
        padding: 0;

        .anticon {
          font-size: 14px;
        }
      }
    }
  }

  .column-form {
    flex: 1;
    width: 100%; /* 确保表单占满宽度 */
  }

  .form-row {
    display: flex;
    gap: 16px;
    margin-bottom: 16px;

    &:last-child {
      margin-bottom: 0;
    }
  }

  .form-item {
    flex: 1;

    &.full-width {
      flex: 3;
    }
  }

  .form-label {
    display: block;
    margin-bottom: 6px;
    font-weight: 500;
    color: rgba(0, 0, 0, 0.85);

    &::after {
      content: '：';
    }
  }

  .error-text {
    color: #ff4d4f;
    font-size: 12px;
    margin-top: 4px;
  }

  .has-error {
    :deep(.ant-input) {
      border-color: #ff4d4f;
    }
  }

  .form-actions {
    display: none; /* 隐藏原来的删除按钮区域 */
  }

  .fixed-columns {
    margin-top: 24px;
    padding-top: 24px;
    border-top: 1px dashed #d9d9d9;

    .tip-text {
      margin-bottom: 16px;
      color: #666;
      font-size: 14px;
    }
  }

  .fixed-column-item {
    margin-bottom: 16px;
    padding: 16px;
    background: #f5f5f5;
    border: 1px solid #e8e8e8;
    border-radius: 6px;

    &:last-child {
      margin-bottom: 0;
    }

    :deep(.ant-input-group-addon) {
      background: #e6f7ff;
      color: #1890ff;
      font-weight: 500;
    }
  }

  .footer-actions {
    display: flex;
    justify-content: center;
    margin-top: 32px;
    padding-top: 8px;
    border-top: 1px solid #f0f0f0;
    position: sticky;
    bottom: 0;
    background: #fff;
    padding-bottom: 8px;
  }
</style>
