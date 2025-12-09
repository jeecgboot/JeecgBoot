<template>
  <BasicModal
    destroyOnClose
    @register="registerModal"
    :canFullscreen="false"
    width="640px"
    :title="isEdit ? '编辑MCP' : '新增MCP'"
    wrapClassName="airag-mcp-modal"
  >
    <div class="modal">
      <div class="mcp-content">
        <BasicForm @register="registerForm">
          <!-- category类型选择：单选 -->
          <template #category="{ model, field }">
            <a-radio-group v-model:value="model[field]" @change="onCategoryChange" :disabled="isEdit">
              <a-radio value="mcp">MCP</a-radio>
              <a-radio value="plugin">插件</a-radio>
            </a-radio-group>
          </template>
          <!-- MCP类型选择 -->
<!--          <template #type="{ model, field }">-->
<!--            <a-select v-model:value="model[field]" @change="onTypeChange">-->
<!--              <a-select-option value="sse">SSE</a-select-option>-->
<!--              <a-select-option value="stdio">STDIO</a-select-option>-->
<!--            </a-select>-->
<!--          </template>-->
          <!-- endpoint 根据类型切换 placeholder -->
          <template #endpoint="{ model, field }">
            <a-input
              v-model:value="model[field]"
              :placeholder="endpointPlaceholder"
            />
          </template>
          <!-- headers 根据类型切换标签名称与 placeholder -->
          <template #headers>
            <div class="headers-table-wrapper">
              <a-button type="primary" size="small" @click="addHeaderRow" style="margin-bottom: 8px;">
                添加
              </a-button>
              <div class="headers-table-container">
                <a-table
                  :dataSource="headersData"
                  :columns="headersColumns"
                  :pagination="false"
                  size="small"
                  bordered
                >
                  <template #bodyCell="{ column, record, index }">
                    <template v-if="column.key === 'key'">
                      <a-input v-model:value="record.key" placeholder="请输入键" size="small" />
                    </template>
                    <template v-if="column.key === 'value'">
                      <a-input v-model:value="record.value" placeholder="请输入值" size="small" />
                    </template>
                    <template v-if="column.key === 'action'">
                      <a-button type="link" danger size="small" @click="deleteHeaderRow(index)">删除</a-button>
                    </template>
                  </template>
                </a-table>
              </div>
            </div>
          </template>
          <!-- 授权方式配置（仅插件类型） -->
          <template #authType="{ model, field }">
            <a-select v-model:value="model[field]" @change="onAuthTypeChange">
              <a-select-option value="none">不需要授权</a-select-option>
              <a-select-option value="token">Token / API Key</a-select-option>
            </a-select>
          </template>
          <!-- Token参数名（仅插件类型且选择Token授权时） -->
          <template #tokenParamName="{ model, field }">
            <a-input v-model:value="model[field]" placeholder="请输入Token参数名" />
          </template>
          <!-- Token参数值（仅插件类型且选择Token授权时） -->
          <template #tokenParamValue="{ model, field }">
            <a-input v-model:value="model[field]" type="password" placeholder="请输入Token值" />
          </template>
        </BasicForm>
      </div>
    </div>
    <template #footer>
      <a-button @click="close">关闭</a-button>
      <a-button type="primary" @click="handleSubmit" :loading="submitLoading">保存</a-button>
      <a-button v-if="categoryValue === 'mcp'" type="primary" @click="handleSubmitAndSync" :loading="submitLoading">保存并同步</a-button>
    </template>
  </BasicModal>
</template>
<script lang="ts" setup>
import { ref, computed, nextTick } from 'vue';
import { useModalInner } from '/@/components/Modal';
import BasicModal from '/@/components/Modal/src/BasicModal.vue';
import BasicForm from '/@/components/Form/src/BasicForm.vue';
import { useForm } from '/@/components/Form';
import { saveOrUpdate, saveAndSync } from '../AiragMcp.api';
import { useMessage } from '/@/hooks/web/useMessage';

const emit = defineEmits(['success', 'register']);
const { createMessage } = useMessage();
const isEdit = ref(false);
const submitLoading = ref(false);
const recordId = ref<string | undefined>(undefined);

// category类型（plugin/mcp）
const categoryValue = ref('mcp');
// MCP类型（sse/stdio）
const typeValue = ref('sse');
// 授权方式
const authType = ref('none'); // none: 不需要授权, token: Token / API Key
// Token配置
const tokenParamName = ref('X-Access-Token');
const tokenParamValue = ref('');

const endpointPlaceholder = computed(() => {
  if (categoryValue.value === 'plugin') {
    return '请输入BaseURL，例如：https://api.example.com（可选，不填使用当前系统地址）';
  }
  return typeValue.value === 'sse' ? '请输入服务端点URL，例如：https://example.com/stream' : '请输入启动命令，例如：./start-mcp-service';
});

// 表格数据
const headersData = ref<Array<{ key: string; value: string }>>([]);

// 表格列配置
const headersColumns = [
  { title: '键', dataIndex: 'key', key: 'key', width: '40%' },
  { title: '值', dataIndex: 'value', key: 'value', width: '45%' },
  { title: '操作', key: 'action', width: '15%' },
];

// 添加行
function addHeaderRow() {
  headersData.value.push({ key: '', value: '' });
}

// 删除行
function deleteHeaderRow(index: number) {
  // 如果只剩一行
  if (headersData.value.length <= 1) {
    const lastRow = headersData.value[0];
    // 判断最后一行是否为空
    if (!lastRow.key && !lastRow.value) {
      // 如果键和值都是空的，不处理
      return;
    } else {
      // 如果不是空的，清空这一行的键和值
      lastRow.key = '';
      lastRow.value = '';
      createMessage.success('已清空数据');
      return;
    }
  }
  headersData.value.splice(index, 1);
}

// 将表格数据转换为JSON字符串格式
function headersDataToString(): string {
  const filtered = headersData.value.filter(item => item.key && item.value);
  if (filtered.length === 0) {
    return '';
  }
  const obj = filtered.reduce((acc, item) => {
    acc[item.key] = item.value;
    return acc;
  }, {} as Record<string, string>);
  return JSON.stringify(obj);
}

// 将JSON字符串格式转换为表格数据
function stringToHeadersData(str: string | undefined) {
  if (!str) {
    // 默认添加一行空数据
    headersData.value = [{ key: '', value: '' }];
    return;
  }
  try {
    const obj = JSON.parse(str);
    const entries = Object.entries(obj);
    if (entries.length === 0) {
      // 如果解析后没有数据，默认添加一行空数据
      headersData.value = [{ key: '', value: '' }];
    } else {
      headersData.value = entries.map(([key, value]) => ({
        key,
        value: String(value)
      }));
    }
  } catch (e) {
    // JSON解析失败，默认添加一行空数据
    console.error('解析headers JSON失败:', e);
    headersData.value = [{ key: '', value: '' }];
  }
}

function onAuthTypeChange(val: string) {
  authType.value = val;
  updateSchema([
    { field: 'tokenParamName', required: val === 'token', show: val === 'token' },
    { field: 'tokenParamValue', required: false, show: val === 'token' },
  ]);
}

function onCategoryChange(val) {
  categoryValue.value = val.target.value;
  if (categoryValue.value === 'plugin') {
    // 插件类型：显示BaseURL、请求头、授权方式，隐藏MCP类型选择
    updateSchema([
      { field: 'category', label: '类型' },
      { field: 'endpoint', label: 'BaseURL', required: false },
      { field: 'type', show: false },
      { field: 'headers', label: '请求头', show: true },
      { field: 'authType', label: '授权方式', required: true, show: true },
      { field: 'tokenParamName', label: 'Token参数名', required: authType.value === 'token', show: authType.value === 'token' },
      { field: 'tokenParamValue', label: 'Token参数值', required: false , show: authType.value === 'token' },
    ]);
    // 设置插件类型的默认值
    setFieldsValue({
      type: 'api',
      authType: authType.value || 'none',
      tokenParamName: tokenParamName.value || 'X-Access-Token',
    });
  } else {
    // MCP类型：显示原有字段
    updateSchema([
      { field: 'category', label: '类型' },
      { field: 'endpoint', label: typeValue.value === 'sse' ? 'URL' : '命令', required: true },
      { field: 'type', label: 'MCP类型', show: false },
      { field: 'headers', label: typeValue.value === 'sse' ? '请求头（目前不支持）' : '环境变量', show: false },
      { field: 'authType', show: false },
      { field: 'tokenParamName', show: false },
      { field: 'tokenParamValue', show: false },
    ]);
    // 设置MCP类型的默认值
    setFieldsValue({
      type: typeValue.value || 'sse',
      authType: undefined,
      tokenParamName: undefined,
      tokenParamValue: undefined,
    });
  }
}


// 表单配置
const [registerForm, { resetFields, validate, setFieldsValue, updateSchema }] = useForm({
  showActionButtonGroup: false,
  layout: 'vertical',
  baseColProps: { span: 24 },
  schemas: [
    { field: 'name', component: 'Input', label: '名称', required: true, componentProps: { placeholder: '请输入名称' } },
    { field: 'icon', label: '图标', component: 'JImageUpload' },
    { field: 'category', component: 'RadioGroup', label: '类型', required: true, slot: 'category', defaultValue: 'mcp' },
    { field: 'type', component: 'Select', label: 'MCP类型', required: false, show: false, slot: 'type', defaultValue: 'sse' },
    { field: 'endpoint', component: 'Input', label: 'URL', required: true, slot: 'endpoint' },
    { field: 'headers', component: 'InputTextArea', label: '请求头', slot: 'headers', show: false },
    { field: 'authType', component: 'Select', label: '授权方式', required: true, slot: 'authType', defaultValue: 'none', show: false },
    { field: 'tokenParamName', component: 'Input', label: 'Token参数名', required: false, slot: 'tokenParamName', defaultValue: 'X-Access-Token', show: false },
    { field: 'tokenParamValue', component: 'Input', label: 'Token参数值', required: false, slot: 'tokenParamValue', show: false },
    { field: 'descr', component: 'InputTextArea', label: '描述', componentProps: { rows: 3, placeholder: '请输入描述' } },
  ]
});

const [registerModal, { closeModal }] = useModalInner(async (data) => {
  await resetFields();
  submitLoading.value = false;
  recordId.value = data?.id;
  if (data && Object.keys(data).length > 0) {
    // 区分新增/编辑
    if (data.id) {
      isEdit.value = true;
    } else {
      isEdit.value = false;
    }
    // 获取category，默认为mcp
    const category = data.category || 'mcp';
    categoryValue.value = category;
    const t = data.type || 'sse';
    typeValue.value = t;
    
    // 解析授权配置（从headers或metadata中解析）
    let parsedAuthType = 'none';
    let parsedTokenParamName = 'X-Access-Token';
    let parsedTokenParamValue = '';
    
    if (category === 'plugin') {
      // 尝试从metadata中解析授权配置
      if (data.metadata) {
        try {
          const metadata = typeof data.metadata === 'string' ? JSON.parse(data.metadata) : data.metadata;
          parsedAuthType = metadata.authType || 'none';
          parsedTokenParamName = metadata.tokenParamName || 'X-Access-Token';
          parsedTokenParamValue = metadata.tokenParamValue || '';
        } catch (e) {
          // 解析失败，使用默认值
        }
      }
      
      // 从headers中提取token（如果存在）
      let headersObj: any = {};
      if (data.headers) {
        try {
          headersObj = typeof data.headers === 'string' ? JSON.parse(data.headers) : data.headers;
          // 如果metadata中有token配置，尝试从headers中提取对应的值
          if (parsedAuthType === 'token' && parsedTokenParamName && headersObj[parsedTokenParamName]) {
            parsedTokenParamValue = headersObj[parsedTokenParamName];
            // 从headers中移除token，避免在表格中显示
            delete headersObj[parsedTokenParamName];
          }
        } catch (e) {
          // 解析失败
        }
      }
      
      authType.value = parsedAuthType;
      tokenParamName.value = parsedTokenParamName;
      tokenParamValue.value = parsedTokenParamValue;
      
      updateSchema([
        { field: 'category', label: '类型', componentProps: { disabled: true } },
        { field: 'endpoint', label: 'BaseURL', required: false },
        { field: 'type', show: false },
        { field: 'headers', label: '请求头', show: true },
        { field: 'authType', label: '授权方式', required: true, show: true },
        { field: 'tokenParamName', label: 'Token参数名', required: parsedAuthType === 'token', show: parsedAuthType === 'token' },
        { field: 'tokenParamValue', label: 'Token参数值', required: false, show: parsedAuthType === 'token' },
      ]);
      // 将处理后的headers（已移除token）转换为表格数据
      stringToHeadersData(Object.keys(headersObj).length > 0 ? JSON.stringify(headersObj) : '');
      
      // 需要在下一个tick设置值，确保updateSchema完成
      nextTick(() => {
        setFieldsValue({
          icon: data.icon,
          name: data.name,
          descr: data.descr,
          category: category,
          type: t,
          endpoint: data.endpoint,
          authType: parsedAuthType,
          tokenParamName: parsedTokenParamName,
          tokenParamValue: parsedTokenParamValue,
        });
      });
    } else {
      updateSchema([
        { field: 'category', label: '类型', componentProps: { disabled: true } },
        { field: 'endpoint', label: t === 'sse' ? 'URL' : '命令', required: true },
        { field: 'type', label: 'MCP类型', show: false },
        { field: 'headers', label: t === 'sse' ? '请求头（目前不支持）' : '环境变量', show: false },
        { field: 'authType', show: false },
        { field: 'tokenParamName', show: false },
        { field: 'tokenParamValue', show: false },
      ]);
      // 将 headers 字符串转换为表格数据
      stringToHeadersData(data.headers);
      
      // 需要在下一个tick设置值，确保updateSchema完成
      nextTick(() => {
        setFieldsValue({
          icon: data.icon,
          name: data.name,
          descr: data.descr,
          category: category,
          type: t,
          endpoint: data.endpoint,
        });
      });
    }
  } else {
    isEdit.value = false;
    categoryValue.value = 'mcp';
    typeValue.value = 'sse';
    authType.value = 'none';
    tokenParamName.value = 'X-Access-Token';
    tokenParamValue.value = '';
    // 默认添加一行空数据
    headersData.value = [{ key: '', value: '' }];
    updateSchema([
      { field: 'category', label: '类型' },
      { field: 'endpoint', label: 'URL' },
      { field: 'headers', label: '请求头', show: false },
      { field: 'authType', show: false },
      { field: 'tokenParamName', show: false },
      { field: 'tokenParamValue', show: false },
    ]);
    // 设置默认选中值
    setFieldsValue({ category: 'mcp', type: 'sse', authType: 'none', tokenParamName: 'X-Access-Token', tokenParamValue: '' });
  }
});

async function handleSubmit(){
  try {
    submitLoading.value = true;
    const values = await validate();
    if(recordId.value){
      values.id = recordId.value;
    }
    
    if (values.category === 'plugin') {
      // 插件类型：验证授权配置
      if (values.authType === 'token') {
        if (!values.tokenParamName) {
          createMessage.error('Token授权方式需要填写Token参数名和参数值');
          return;
        }
      }
      
      // 插件类型：处理headers和授权配置
      // 合并headers和token配置
      let headersObj = {};
      const headersStr = headersDataToString();
      if (headersStr) {
        try {
          headersObj = JSON.parse(headersStr);
        } catch (e) {
          headersObj = {};
        }
      }
      
      // 如果选择了Token授权，添加token到headers
      if (values.authType === 'token' && values.tokenParamName && values.tokenParamValue) {
        headersObj[values.tokenParamName] = values.tokenParamValue;
      }
      
      values.headers = Object.keys(headersObj).length > 0 ? JSON.stringify(headersObj) : '';
      
      // 将授权配置保存到metadata
      const metadata: any = {};
      if (values.metadata) {
        try {
          Object.assign(metadata, typeof values.metadata === 'string' ? JSON.parse(values.metadata) : values.metadata);
        } catch (e) {
          // 解析失败，使用空对象
        }
      }
      metadata.authType = values.authType || 'none';
      if (values.authType === 'token') {
        metadata.tokenParamName = values.tokenParamName || 'X-Access-Token';
        metadata.tokenParamValue = values.tokenParamValue || '';
      }
      values.metadata = JSON.stringify(metadata);
      
      values.type = 'api';
    } else {
      // MCP类型：将表格数据转换为字符串
      values.headers = headersDataToString();
      // 清除授权相关字段
      delete values.authType;
      delete values.tokenParamName;
      delete values.tokenParamValue;
    }
    
    const res = await saveOrUpdate(values);
    if(res.success){
      createMessage.success(res.message || '保存成功');
      emit('success');
      closeModal();
    }else{
      createMessage.error(res.message || '保存失败');
    }
  }finally{
    submitLoading.value = false;
  }
}

async function handleSubmitAndSync(){
  try {
    submitLoading.value = true;
    const values = await validate();
    if(recordId.value){
      values.id = recordId.value;
    }
    // 将表格数据转换为字符串
    values.headers = headersDataToString();
    const res = await saveAndSync(values);
    if(res.success){
      createMessage.success(res.message || '保存并同步成功');
      emit('success');
      closeModal();
    }else{
      createMessage.error(res.message || '保存并同步失败');
    }
  }finally{
    submitLoading.value = false;
  }
}

function close(){
  closeModal();
}
</script>
<style scoped lang="less">
.modal {
  padding: 5px 16px 8px 16px;
  .header {
    padding: 0 0 12px 0;
    display: flex;
    justify-content: space-between;
    .header-title {
      font-size: 16px;
      font-weight: bold;
    }
  }
  .mcp-content {
    :deep(.ant-form-item) { margin-bottom: 8px; }
    :deep(.ant-input),
    :deep(.ant-input-number),
    :deep(.ant-select),
    :deep(.ant-select-selector),
    :deep(.ant-textarea),
    :deep(textarea.ant-input) { width: 100% !important; }
    :deep(.ant-select-selector){ padding: 0 8px; }
    
    .headers-table-wrapper {
      .headers-table-container {
        max-height: 200px;
        overflow-y: auto;
        border: 1px solid #d9d9d9;
        border-radius: 2px;
        
        :deep(.ant-table) {
          .ant-table-thead > tr > th {
            background: #fafafa;
            padding: 8px;
          }
          .ant-table-tbody > tr > td {
            padding: 4px 8px;
          }
        }
      }
    }
  }
}
</style>
<style lang="less">
.airag-mcp-modal {
  .jeecg-basic-modal-close > span { margin-left: 0 !important; }
  .ant-modal {
    max-height: 85vh;
    .ant-modal-body {
      max-height: calc(85vh - 110px);
      overflow-y: auto;
    }
  }
}
</style>
