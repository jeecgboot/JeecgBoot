<template>
  <BasicModal
    destroyOnClose
    @register="registerModal"
    :canFullscreen="false"
    width="1400px"
    :title="isEdit ? '编辑工具' : '新增工具'"
    wrapClassName="plugin-tool-edit-modal"
  >
    <div class="modal">
      <div class="tool-edit-content">
        <!-- 基本信息 -->
        <div class="section">
          <h3 class="section-title">基本信息</h3>
          <a-form :model="toolForm" layout="vertical">
            <a-row :gutter="24">
              <a-col :span="12">
                <a-form-item label="工具名称" required>
                  <a-input v-model:value="toolForm.name" placeholder="请输入工具名称" />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="请求方式" required>
                  <a-select v-model:value="toolForm.method" placeholder="请选择请求方式">
                    <a-select-option value="GET">GET</a-select-option>
                    <a-select-option value="POST">POST</a-select-option>
                    <a-select-option value="PUT">PUT</a-select-option>
                    <a-select-option value="DELETE">DELETE</a-select-option>
                    <a-select-option value="PATCH">PATCH</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row :gutter="24">
              <a-col :span="24">
                <a-form-item label="访问路径" required>
                  <a-input v-model:value="toolForm.path" placeholder="请输入访问路径，如：/api/user/{userId}" @blur="normalizePath" />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row :gutter="24">
              <a-col :span="24">
                <a-form-item label="描述" required>
                  <a-textarea v-model:value="toolForm.description" :rows="3" placeholder="请输入工具描述" />
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </div>

        <!-- 请求参数 -->
        <div class="section">
          <div class="section-header">
            <h3 class="section-title">请求参数</h3>
            <a-button type="primary" size="small" @click="handleAddRequestParam">添加参数</a-button>
          </div>
          <a-table
            :dataSource="requestParams"
            :columns="requestParamsColumns"
            :pagination="false"
            bordered
            size="small"
            rowKey="tempId"
          >
            <template #bodyCell="{ column, record, index }">
              <template v-if="column.key === 'name'">
                <a-input
                  v-model:value="record.name"
                  placeholder="参数名称（字母数字下划线）"
                  @blur="validateParamName(record)"
                />
              </template>
              <template v-else-if="column.key === 'description'">
                <a-input v-model:value="record.description" placeholder="参数描述" />
              </template>
              <template v-else-if="column.key === 'type'">
                <a-select v-model:value="record.type" style="width: 100%">
                  <a-select-option value="String">String</a-select-option>
                  <a-select-option value="Number">Number</a-select-option>
                  <a-select-option value="Integer">Integer</a-select-option>
                  <a-select-option value="Boolean">Boolean</a-select-option>
                </a-select>
              </template>
              <template v-else-if="column.key === 'location'">
                <a-select v-model:value="record.location" style="width: 100%" @change="onLocationChange(record)">
                  <a-select-option value="Body">Raw(json)</a-select-option>
                  <a-select-option value="Form-Data">Form-Data</a-select-option>
                  <a-select-option value="Query">Query</a-select-option>
                  <a-select-option value="Header">Header</a-select-option>
                  <a-select-option value="Path">Path</a-select-option>
                </a-select>
              </template>
              <template v-else-if="column.key === 'required'">
                <a-checkbox v-model:checked="record.required" />
              </template>
              <template v-else-if="column.key === 'defaultValue'">
                <a-input v-model:value="record.defaultValue" placeholder="默认值" />
              </template>
              <template v-else-if="column.key === 'action'">
                <a-button type="link" danger size="small" @click="handleDeleteRequestParam(index)">删除</a-button>
              </template>
            </template>
          </a-table>
        </div>

        <!-- 输出参数 -->
        <div class="section">
          <div class="section-header">
            <h3 class="section-title">输出参数</h3>
            <a-button type="primary" size="small" @click="handleAddResponseParam">添加参数</a-button>
          </div>
          <a-table
            :dataSource="responseParams"
            :columns="responseParamsColumns"
            :pagination="false"
            bordered
            size="small"
            rowKey="tempId"
          >
            <template #bodyCell="{ column, record, index }">
              <template v-if="column.key === 'name'">
                <a-input
                  v-model:value="record.name"
                  placeholder="参数名称（支持点号和数组语法，如data.name、data[].name）"
                  @blur="validateResponseParamName(record)"
                />
              </template>
              <template v-else-if="column.key === 'description'">
                <a-input v-model:value="record.description" placeholder="参数描述" />
              </template>
              <template v-else-if="column.key === 'type'">
                <a-select v-model:value="record.type" style="width: 100%" @change="onResponseTypeChange(record)">
                  <a-select-option value="String">String</a-select-option>
                  <a-select-option value="Number">Number</a-select-option>
                  <a-select-option value="Integer">Integer</a-select-option>
                  <a-select-option value="Boolean">Boolean</a-select-option>
                  <a-select-option value="Object">Object</a-select-option>
                  <a-select-option value="Array">Array</a-select-option>
                </a-select>
              </template>
              <template v-else-if="column.key === 'action'">
                <a-button type="link" danger size="small" @click="handleDeleteResponseParam(index)">删除</a-button>
              </template>
            </template>
          </a-table>
        </div>
      </div>
    </div>
    <template #footer>
      <a-button @click="close">关闭</a-button>
      <a-button type="primary" @click="handleSave" :loading="submitLoading">保存</a-button>
    </template>
  </BasicModal>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue';
import { useModalInner } from '/@/components/Modal';
import BasicModal from '/@/components/Modal/src/BasicModal.vue';
import { useMessage } from '/@/hooks/web/useMessage';
import { queryById, saveTools } from '../AiragMcp.api';

const emit = defineEmits(['success', 'register']);
const { createMessage } = useMessage();
const submitLoading = ref(false);
const pluginId = ref<string | undefined>(undefined);
const isEdit = ref(false);
const toolIndex = ref<number>(-1); // 编辑时工具在数组中的索引

// 工具表单
const toolForm = reactive({
  name: '',
  description: '',
  path: '',
  method: 'GET',
});

// 请求参数列表
const requestParams = ref<any[]>([]);
// 输出参数列表
const responseParams = ref<any[]>([]);

// 请求参数表格列
const requestParamsColumns = [
  { title: '参数名称', key: 'name', width: 150 },
  { title: '参数描述', key: 'description', width: 200 },
  { title: '参数类型', key: 'type', width: 120 },
  { title: '传入方式', key: 'location', width: 150 },
  { title: '是否必须', key: 'required', width: 100 },
  { title: '默认值', key: 'defaultValue', width: 150 },
  { title: '操作', key: 'action', width: 100 },
];

// 输出参数表格列
const responseParamsColumns = [
  { title: '参数名称', key: 'name', width: 200 },
  { title: '参数描述', key: 'description', width: 200 },
  { title: '参数类型', key: 'type', width: 150 },
  { title: '操作', key: 'action', width: 100 },
];

let tempIdCounter = 0;

const [registerModal, { closeModal }] = useModalInner(async (data) => {
  pluginId.value = data?.pluginId;
  isEdit.value = !!data?.tool;
  
  // 重置表单
  toolForm.name = '';
  toolForm.description = '';
  toolForm.path = '';
  toolForm.method = 'GET';
  requestParams.value = [];
  responseParams.value = [];
  toolIndex.value = -1;
  
  if (data?.tool) {
    // 编辑模式：填充数据
    toolForm.name = data.tool.name || '';
    toolForm.description = data.tool.description || '';
    toolForm.path = data.tool.path || '';
    toolForm.method = data.tool.method || 'GET';
    
    requestParams.value = (data.tool.parameters || []).map((p: any) => ({
      ...p,
      tempId: `req_${tempIdCounter++}`,
      required: p.required !== undefined ? p.required : false,
    }));
    
    responseParams.value = (data.tool.responses || []).map((r: any) => ({
      ...r,
      tempId: `resp_${tempIdCounter++}`,
    }));
    
    // 查找工具在列表中的索引
    if (pluginId.value) {
      const res = await queryById(pluginId.value);
      const detail = res.result || res;
      if (detail.tools) {
        try {
          const tools = typeof detail.tools === 'string' ? JSON.parse(detail.tools) : detail.tools;
          toolIndex.value = tools.findIndex((t: any) => t.name === data.tool.name);
        } catch (e) {
          toolIndex.value = -1;
        }
      }
    }
  }
});

function validateParamName(record: any) {
  // 验证参数名称：只允许字母数字下划线
  if (record.name && !/^[a-zA-Z0-9_]+$/.test(record.name)) {
    createMessage.warning('参数名称只能包含字母、数字和下划线');
    record.name = record.name.replace(/[^a-zA-Z0-9_]/g, '');
  }
}

function validateResponseParamName(record: any) {
  // 验证输出参数名称：支持字母、数字、下划线、点、中括号
  // 只有Object和Array类型可以使用点号和中括号
  if (!record.name) return;
  
  const type = record.type;
  const name = record.name;
  
  if (type === 'Object' || type === 'Array') {
    // Object和Array类型：支持字母、数字、下划线、点、中括号
    // 例如：data.name, data[String], data[].name
    if (!/^[a-zA-Z0-9_.\[\]]+$/.test(name)) {
      createMessage.warning('参数名称只能包含字母、数字、下划线、点和中括号');
      record.name = name.replace(/[^a-zA-Z0-9_.\[\]]/g, '');
    }
  } else {
    // 其他类型：只允许字母数字下划线
    if (!/^[a-zA-Z0-9_]+$/.test(name)) {
      createMessage.warning('非Object/Array类型参数名称只能包含字母、数字和下划线');
      record.name = name.replace(/[^a-zA-Z0-9_]/g, '');
    }
  }
}

function onResponseTypeChange(record: any) {
  // 当类型改变时，如果类型不是Object或Array，清除点号和中括号
  if (record.type !== 'Object' && record.type !== 'Array') {
    if (record.name && /[.\[\]]/.test(record.name)) {
      createMessage.warning('非Object/Array类型不支持使用点号和中括号');
      record.name = record.name.replace(/[.\[\]]/g, '');
    }
  }
}

function onLocationChange(record: any) {
  // Body和Form-Data可以同时存在，不再警告
  // 同时存在时，后端默认使用Body
}

function handleAddRequestParam() {
  requestParams.value.push({
    tempId: `req_${tempIdCounter++}`,
    name: '',
    description: '',
    type: 'String',
    location: 'Body',
    required: false,
    defaultValue: '',
  });
}

function handleDeleteRequestParam(index: number) {
  requestParams.value.splice(index, 1);
}

function handleAddResponseParam() {
  responseParams.value.push({
    tempId: `resp_${tempIdCounter++}`,
    name: '',
    description: '',
    type: 'String',
  });
}

function handleDeleteResponseParam(index: number) {
  responseParams.value.splice(index, 1);
}

async function handleSave() {
  try {
    submitLoading.value = true;
    
    // 验证基本字段
    if (!toolForm.name || !toolForm.description || !toolForm.path || !toolForm.method) {
      createMessage.warning('请填写完整的工具基本信息');
      return;
    }
    
    // Body和Form-Data可以同时存在，后端会默认使用Body
    
    if (!pluginId.value) {
      createMessage.error('插件ID不存在');
      return;
    }
    
    // 加载插件数据
    const res = await queryById(pluginId.value);
    const detail = res.result || res;
    
    // 解析现有工具列表
    let tools: any[] = [];
    if (detail.tools) {
      try {
        const parsedTools = typeof detail.tools === 'string' ? JSON.parse(detail.tools) : detail.tools;
        // 确保是数组
        if (Array.isArray(parsedTools)) {
          tools = [...parsedTools]; // 复制数组，避免引用问题
        } else {
          tools = [];
        }
      } catch (e) {
        console.error('解析工具列表失败:', e);
        tools = [];
      }
    }
    
    // 确保tools是数组
    if (!Array.isArray(tools)) {
      tools = [];
    }
    
    // 构建当前工具数据（移除tempId）
    const parameters = requestParams.value.map(p => {
      const { tempId: _tempId, ...param } = p;
      return param;
    });
    const responses = responseParams.value.map(r => {
      const { tempId: _tempId, ...resp } = r;
      return resp;
    });
    
    const toolData = {
      name: toolForm.name,
      description: toolForm.description,
      path: toolForm.path,
      method: toolForm.method,
      enabled: true, // 默认启用
      parameters,
      responses,
    };
    
    // 根据编辑状态处理工具
    if (isEdit.value && toolIndex.value >= 0 && toolIndex.value < tools.length) {
      // 编辑模式：更新现有工具
      tools[toolIndex.value] = toolData;
    } else {
      // 新增模式：添加新工具
      // 检查工具名称是否已存在
      const nameExists = tools.some((t: any) => t.name === toolForm.name);
      if (nameExists) {
        createMessage.error('工具名称已存在，请使用不同的名称');
        return;
      }
      tools.push(toolData);
    }
    
    // 构建工具列表JSON字符串
    const toolsJson = JSON.stringify(tools);
    
    // 调用保存工具接口
    const saveRes = await saveTools(pluginId.value, toolsJson);
    if (saveRes.success) {
      createMessage.success('保存成功');
      emit('success');
      closeModal();
    } else {
      createMessage.error(saveRes.message || '保存失败');
    }
  } finally {
    submitLoading.value = false;
  }
}

function close() {
  closeModal();
}

// 规范化路径：确保以/开头
function normalizePath() {
  if (toolForm.path && !toolForm.path.startsWith('/')) {
    toolForm.path = '/' + toolForm.path;
  }
}
</script>

<style scoped lang="less">
.modal {
  padding: 16px;
  .tool-edit-content {
    .section {
      margin-bottom: 24px;
      .section-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;
      }
      .section-title {
        margin: 0;
        font-size: 16px;
        font-weight: bold;
      }
      :deep(.ant-table) {
        .ant-table-thead > tr > th {
          background: #fafafa;
          padding: 8px;
        }
        .ant-table-tbody > tr > td {
          padding: 8px;
        }
      }
    }
  }
}
</style>

<style lang="less">
.plugin-tool-edit-modal {
  .jeecg-basic-modal-close > span { margin-left: 0 !important; }
  .ant-modal {
    max-height: 90vh;
    .ant-modal-body {
      max-height: calc(90vh - 110px);
      overflow-y: auto;
    }
  }
}
</style>

