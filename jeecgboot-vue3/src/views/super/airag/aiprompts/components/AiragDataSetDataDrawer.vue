<template>
  <BasicDrawer v-bind="getProps">
    <div class="dynamic-column-config">
      <!-- 列配置列表 -->
      <div class="column-list">
        <!-- 移除了 draggable 组件，直接使用 div 渲染列表 -->
        <div v-for="(element, index) in dataSet" :key="index" class="column-item">
          <!-- 操作区域 - 放在右上角 -->
          <div class="action-area">
            <a-button type="text" danger @click="handleRemoveData(index)" title="删除">
              <DeleteOutlined />
            </a-button>
          </div>
          <!-- 列配置表单 -->
          <div class="column-form">
            <div class="form-row">
              <div class="form-item" v-for="item in columns" :key="item.name">
                <label class="form-label">{{ item.name }}</label>
                <!-- 附件输入框 -->
                <JImageUpload :maxCount="1" v-if="item.dataType === 'FILE'" v-model:value="element[item.name]"></JImageUpload>
                <a-input
                    v-else
                    v-model:value="element[item.name]"
                    :placeholder="'请输入' + item.name"
                    :class="{ 'has-error': !element[item.name] }"
                    @blur="validateColumn(element[item.name])"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
      <a-button v-if="!isUpdate" type="dashed" style="width: 100%" @click="handleAddData"> <PlusOutlined />添加数据 </a-button>
    </div>
  </BasicDrawer>
</template>

<script lang="ts" setup>
  import type { DrawerProps } from '/@/components/Drawer';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { computed, unref, ref } from 'vue';
  import { useAttrs } from '@/hooks/core/useAttrs';
  import { DeleteOutlined, PlusOutlined } from '@ant-design/icons-vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { saveOrUpdate } from '@/views/super/airag/aiprompts/AiragExtData.api';
  import { message } from 'ant-design-vue';
  import {JImageUpload} from "@/components/Form";
  // Emits声明
  const emit = defineEmits(['register', 'success']);
  const attrs = useAttrs();
  // 消息提示
  const { createMessage } = useMessage();
  // 注册抽屉
  const [registerDrawer, { closeDrawer }] = useDrawerInner(open);
  // 抽屉最终props
  const getProps = computed(() => {
    let drawerProps: Partial<DrawerProps> = {
      width: 1000,
      title: '数据配置',
      showFooter: true,
      destroyOnClose: true,
    };
    let finalProps: Recordable = {
      ...unref(attrs),
      ...drawerProps,
      okText: '保存',
      onOk: handleSubmit,
      onCancel: closeDrawer,
      onRegister: registerDrawer,
    };
    return finalProps;
  });
  /** 更新状态 */
  const isUpdate = ref<boolean>(false);
  /** dataId */
  const dataId = ref<any>('');
  /** 数据列表 */
  const allData = ref<any>([]);
  /** 数据列表 */
  const dataSet = ref<any>([]);
  // 列配置
  const columns = ref<any>([]);
  // 验证数据非空
  const validateColumn = (value) => {
    return value && value.trim() !== '';
  };
  // 生成唯一ID
  function generateId(): string {
    return Date.now().toString(36) + Math.random().toString(36).substr(2);
  }
  //添加数据
  function handleAddData() {
    dataSet.value.push({
      id: generateId(),
    });
  }
  //保存数据
  async function handleSubmit() {
    // 验证数据
    const invalidData = dataSet.value.filter((item) => {
      return columns.value.some((col) => col.required && !validateColumn(item[col.name]));
    });
    if (invalidData.length > 0) {
      createMessage.error('请填写所有必填项（名称）');
      return;
    }

    try {
      // 构造提交数据
      let dataSource:any = [];
      if (isUpdate.value) {
        dataSource = allData.value.map(item =>
            item.id === dataSet.value[0].id ? dataSet.value[0] : item
        );
      } else {
        dataSource = allData.value.concat(dataSet.value);
      }
      const submitData = {
        datasetValue: JSON.stringify({
          columns: columns.value,
          dataSource: dataSource,
        }),
        id: dataId.value,
      };

      console.log('提交数据:', submitData);

      await saveOrUpdate(submitData, true, false);

      message.success('数据保存成功！');
      // 关闭弹窗并触发成功事件
      closeDrawer();
      emit('success');
    } catch (error) {
      console.error('提交失败:', error);
    } finally {
      //loading.value = false;
    }
  }
  // 删除列
  const handleRemoveData = (index: number) => {
    dataSet.value.splice(index, 1);
  };
  /** 抽屉开启 */
  function open(data) {
    isUpdate.value = data.isUpdate;
    dataId.value = data?.id || '';
    allData.value = data?.dataSource || [];
    columns.value = data?.columns || [];
    console.log("columns.value",columns.value)
    if (isUpdate.value) {
      console.log("data?.record",data?.record)
      dataSet.value = data?.record?[data?.record] : [];
    } else {
      dataSet.value = [];
    }
  }
</script>

<style scoped lang="less">
  .dynamic-column-config {
    max-width: 1200px;
    margin: 0 auto;
    max-height: calc(100vh - 100px); /* 设置最大高度为视口高度的70% */
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
    flex-direction: column;
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
