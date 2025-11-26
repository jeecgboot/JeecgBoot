<template>
  <a-modal
    v-model:open="visible"
    title="对话设置"
    :width="600"
    :maskClosable="false"
    :keyboard="false"
    @ok="handleOk"
    okText="开始对话"
    :cancelButtonProps="{ style: { display: 'none' } }"
    :okButtonProps="{ disabled: !canSubmit }"
  >
    <div class="settings-content">
      <a-form :model="formData" layout="vertical">
        <a-form-item
          v-for="input in flowInputs"
          :key="input.field"
          :label="input.name"
          :required="input.required"
        >
          <!-- 文本类型 - 使用单行输入 -->
          <a-input
            v-if="input.type === 'string'"
            v-model:value="formData[input.field]"
            :placeholder="`请输入${input.name}`"
            :maxlength="input.maxLength"
            show-count
          />
          <!-- 数字类型 -->
          <a-input-number
            v-else-if="input.type === 'number'"
            v-model:value="formData[input.field]"
            :placeholder="`请输入${input.name}`"
            style="width: 100%"
            :min="input.min"
            :max="input.max"
          />
          
            <!-- 图片类型 - 使用类似chat.vue的上传样式，固定支持3张图片 -->
          <div v-else-if="input.type === 'picture'" class="image-upload-container">
            <div class="image-list-wrapper">
              <!-- 已上传的图片 -->
              <div v-for="(img, idx) in imageFileList[input.field]" :key="idx" class="image-preview-item">
                <img :src="getImageUrl(img)" @click="handlePreview(img)" />
                <div class="image-remove-icon" @click="handleRemove(idx, input.field)">
                  <Icon icon="ant-design:close-outlined" :size="12" />
                </div>
              </div>
              <!-- 上传按钮 -->
              <a-upload
                v-if="(imageFileList[input.field]?.length || 0) < 3"
                accept=".jpg,.jpeg,.png"
                name="file"
                :showUploadList="false"
                :headers="headers"
                :beforeUpload="(file) => beforeUpload(file, input.field)"
                @change="(info) => handleChange(info, input.field)"
                :multiple="true"
                :action="uploadUrl"
                :max-count="3"
              >
                <div class="upload-trigger">
                  <Icon icon="ant-design:plus-outlined" :size="20" />
                  <div class="upload-text">上传图片</div>
                </div>
              </a-upload>
            </div>
          </div>
          <!-- 字符串数组类型（如history） -->
          <a-select
            v-else-if="input.type === 'string[]'"
            v-model:value="formData[input.field]"
            mode="tags"
            :placeholder="`请输入${input.name}`"
            style="width: 100%"
          />
          <!-- 其他类型使用文本输入 -->
          <a-input
            v-else
            v-model:value="formData[input.field]"
            :placeholder="`请输入${input.name}`"
          />
        </a-form-item>
      </a-form>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
  import { ref, computed } from 'vue';
  import { message } from 'ant-design-vue';
  import { Icon } from '/@/components/Icon';
  import { getFileAccessHttpUrl, getHeaders } from '@/utils/common/compUtils';
  import { useGlobSetting } from '@/hooks/setting';

  const props = defineProps<{
    flowInputs: any[];
    conversationId: string;
    existingSettings?: Record<string, any>;
  }>();

  const emit = defineEmits<{
    (e: 'ok', data: Record<string, any>): void;
    (e: 'cancel'): void;
  }>();

  const visible = ref(false);
  const formData = ref<Record<string, any>>({});
  const imageFileList = ref<Record<string, any[]>>({});

  const globSetting = useGlobSetting();
  const baseUploadUrl = globSetting.uploadUrl;
  const uploadUrl = `${baseUploadUrl}/airag/chat/upload`;
  const headers = getHeaders();

  // 过滤掉固定参数（history, content, images）
  const flowInputs = computed(() => {
    const fixedParams = ['history', 'content', 'images'];
    return props.flowInputs?.filter((input) => !fixedParams.includes(input.field)) || [];
  });

  // 检查是否可以提交（必填项都已填写）
  const canSubmit = computed(() => {
    if (flowInputs.value.length === 0) return true;
    return flowInputs.value.every((input) => {
      if (!input.required) return true;
      const value = formData.value[input.field];
      if (input.type === 'picture') {
        return imageFileList.value[input.field] && imageFileList.value[input.field].length > 0;
      }
      return value !== undefined && value !== null && value !== '';
    });
  });

  // 上传前处理
  const beforeUpload = (file: any, field: string) => {
    const isImage = file.type?.startsWith('image/');
    if (!isImage) {
      message.error('只能上传图片文件！');
      return false;
    }
    
    const currentCount = imageFileList.value[field]?.length || 0;
    
    if (currentCount >= 3) {
      message.warning('最多只能上传3张图片！');
      return false;
    }
    
    return true;
  };

  // 上传状态变化处理
  const handleChange = (info: any, field: string) => {
    const { file } = info;
    
    if (file.status === 'error' || (file.response && file.response.code === 500)) {
      message.error(file.response?.message || `${file.name} 上传失败`);
      return;
    }
    
    if (file.status === 'done' && file.response) {
      const imageUrl = file.response.message;
      if (!imageFileList.value[field]) {
        imageFileList.value[field] = [];
      }
      
      // 检查是否已达到上限
      if (imageFileList.value[field].length >= 3) {
        message.warning('最多只能上传3张图片！');
        return;
      }
      
      imageFileList.value[field].push(imageUrl);
      
      // 图片类型始终作为数组存储，触发响应式更新
      imageFileList.value = { ...imageFileList.value };
      formData.value[field] = [...imageFileList.value[field]];
      
      console.log(`[图片上传] 当前图片数量: ${imageFileList.value[field].length}`, imageFileList.value[field]);
    }
  };

  // 获取图片URL
  const getImageUrl = (img: any) => {
    if (typeof img === 'string') {
      return getFileAccessHttpUrl(img);
    }
    return getFileAccessHttpUrl(img.url || img);
  };

  // 图片预览
  const handlePreview = (img: any) => {
    const url = typeof img === 'string' ? img : (img.url || img);
    const imageUrl = getFileAccessHttpUrl(url);
    // 可以使用 ant-design-vue 的 Image 预览功能
    window.open(imageUrl, '_blank');
  };

  // 移除图片
  const handleRemove = (index: number, field: string) => {
    if (imageFileList.value[field]) {
      imageFileList.value[field].splice(index, 1);
      
      // 触发响应式更新
      imageFileList.value = { ...imageFileList.value };
      
      // 图片类型始终作为数组存储，删除后更新
      formData.value[field] = imageFileList.value[field].length > 0 
        ? [...imageFileList.value[field]] 
        : [];
      
      console.log(`[图片删除] 当前图片数量: ${imageFileList.value[field].length}`, imageFileList.value[field]);
    }
  };

  // 打开弹窗
  const open = () => {
    visible.value = true;
    // 初始化表单数据
    formData.value = {};
    imageFileList.value = {};
    
    // 如果有已存在的设置，填充表单
    if (props.existingSettings && Object.keys(props.existingSettings).length > 0) {
      Object.keys(props.existingSettings).forEach((key) => {
        const input = props.flowInputs.find((i) => i.field === key);
        if (input) {
          if (input.type === 'picture') {
            // 确保是数组格式
            const urls = Array.isArray(props.existingSettings![key]) 
              ? props.existingSettings![key] 
              : (props.existingSettings![key] ? [props.existingSettings![key]] : []);
            imageFileList.value[key] = urls.filter(url => url); // 过滤空值
            formData.value[key] = [...imageFileList.value[key]]; // 始终作为数组
          } else {
            formData.value[key] = props.existingSettings![key];
          }
        }
      });
    }
  };

  // 确定
  const handleOk = async () => {
    if (!canSubmit.value) {
      message.warning('请填写所有必填项');
      return;
    }
    
    // 构建最终数据
    const result: Record<string, any> = {};
    flowInputs.value.forEach((input) => {
      const value = formData.value[input.field];
      if (value !== undefined && value !== null && value !== '') {
        result[input.field] = value;
      }
    });
    
    // 先保存，再触发事件
    // 直接通过emit返回设置数据，不需要单独保存
    // 设置会在发送消息时自动保存到后端
    emit('ok', result);
    visible.value = false;
  };

  // 暴露方法
  defineExpose({
    open,
  });
</script>

<style scoped lang="less">
  .settings-content {
    max-height: 60vh;
    overflow-y: auto;
    padding: 10px 5px;
    
    :deep(.ant-form-item) {
      margin-bottom: 16px;
    }
    
    :deep(.ant-form-item-label) {
      padding: 0 5px;
    }
    
    :deep(.ant-input),
    :deep(.ant-input-number),
    :deep(.ant-select) {
      margin: 0 5px;
      width: calc(100% - 10px) !important;
    }
    
    :deep(.ant-upload) {
      margin: 0 5px;
    }
  }
  
  .image-upload-container {
    margin: 0 5px;
    
    .image-list-wrapper {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
      align-items: center;
    }
    
    .image-preview-item {
      position: relative;
      width: 80px;
      height: 80px;
      border-radius: 8px;
      overflow: hidden;
      border: 1px solid #d9d9d9;
      flex-shrink: 0;
      
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        cursor: pointer;
      }
      
      .image-remove-icon {
        position: absolute;
        top: 4px;
        right: 4px;
        background-color: rgba(0, 0, 0, 0.6);
        color: white;
        border-radius: 50%;
        width: 20px;
        height: 20px;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        opacity: 0;
        transition: opacity 0.2s;
        
        &:hover {
          background-color: rgba(0, 0, 0, 0.8);
        }
      }
      
      &:hover .image-remove-icon {
        opacity: 1;
      }
    }
    
    .upload-trigger {
      width: 80px;
      height: 80px;
      border: 1px dashed #d9d9d9;
      border-radius: 8px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      transition: all 0.3s;
      background-color: #fafafa;
      flex-shrink: 0;
      
      &:hover {
        border-color: #1890ff;
        color: #1890ff;
      }
      
      .upload-text {
        margin-top: 4px;
        font-size: 12px;
        color: rgba(0, 0, 0, 0.65);
      }
      
      &:hover .upload-text {
        color: #1890ff;
      }
    }
  }
</style>

