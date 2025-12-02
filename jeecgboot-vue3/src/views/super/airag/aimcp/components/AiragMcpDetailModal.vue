<template>
  <BasicModal
    destroyOnClose
    @register="registerModal"
    :canFullscreen="false"
    width="720px"
    wrapClassName="airag-mcp-detail-modal"
  >
    <template #title>
      <div class="detail-modal-title">
        <span>详情</span>
        <a-button 
          class="detail-edit-btn" 
          type="text" 
          @click="emitEdit"
          :title="'编辑'"
        >
          <Icon icon="ant-design:edit-outlined" :size="18" />
        </a-button>
      </div>
    </template>
  <div class="detail-modal" v-loading="loading">
      <div class="detail-header">
        <img :src="displayIcon" class="detail-icon" />
        <div class="detail-titles">
          <div class="detail-name" :title="record?.name">{{ record?.name || '-' }}</div>
          <div class="detail-type-status">
            <a-tag color="blue">{{ record?.category === 'plugin' ? '插件' : (record?.type || '未知类型') }}</a-tag>
            <a-tag v-if="record?.synced" :color="record?.status === 'enable' ? 'green' : 'red'">
              {{ record?.status === 'enable' ? '已启用' : '未启用' }}
            </a-tag>
            <a-tag v-else color="orange">未同步</a-tag>
          </div>
        </div>
      </div>
      <div class="detail-descr" :title="record?.descr">{{ record?.descr || '暂无描述' }}</div>
      <div class="tools-wrapper">
        <div class="tools-header">
          <div class="tools-title">工具列表</div>
          <a-button v-if="record?.category === 'plugin'" type="primary" size="small" @click="handleAddTool">添加工具</a-button>
        </div>
        <div class="tools-grid" v-if="(pluginTools && pluginTools.length) || (tools && tools.length)">
          <!-- 插件类型工具 -->
          <template v-if="record?.category === 'plugin'">
            <div
              v-for="tool in pluginTools"
              :key="tool.name"
              class="tool-item"
              @click="handleEditTool(tool)"
            >
              <div class="tool-header-item" @click.stop>
                <div class="tool-name" :title="tool.name">{{ tool.name }}</div>
                <div class="tool-actions">
                  <a-switch 
                    v-model:checked="tool.enabled" 
                    size="small" 
                    @change="handleToolEnabledChange(tool)"
                    @click.stop
                  />
                  <a-button 
                    type="text" 
                    size="small" 
                    @click.stop="handleEditTool(tool)"
                    :title="'编辑工具'"
                  >
                    <Icon icon="ant-design:edit-outlined" :size="16" />
                  </a-button>
                  <a-button 
                    type="text" 
                    danger
                    size="small" 
                    @click.stop="handleDeleteTool(tool)"
                    :title="'删除工具'"
                  >
                    <Icon icon="ant-design:delete-outlined" :size="16" />
                  </a-button>
                </div>
              </div>
              <div class="tool-descr" :title="tool.description">{{ tool.description || '无描述' }}</div>
              <div v-if="tool.method || tool.path" class="tool-meta">
                <a-tag v-if="tool.method" size="small" :color="getMethodColor(tool.method)">{{ tool.method }}</a-tag>
                <span v-if="tool.path" class="tool-path">{{ tool.path }}</span>
              </div>
            </div>
          </template>
          <!-- MCP类型工具 -->
          <template v-else>
            <div
              v-for="tool in tools"
              :key="tool.name"
              class="tool-item"
            >
              <div class="tool-header-item" @click.stop>
                <div class="tool-name" :title="tool.name">{{ tool.name }}</div>
              </div>
              <div class="tool-descr" :title="tool.descr">{{ tool.descr || '无描述' }}</div>
            </div>
          </template>
        </div>
        <a-empty v-else description="暂无工具" />
      </div>
    </div>
    <template #footer>
      <a-button @click="closeModal()">关闭</a-button>
    </template>
  </BasicModal>
  <!-- 工具编辑弹窗 -->
  <PluginToolEditModal @register="registerToolEditModal" @success="handleToolEditSuccess" />
</template>
<script lang="ts" setup>
import { ref, computed } from 'vue';
import { useModalInner, useModal } from '/@/components/Modal';
import BasicModal from '/@/components/Modal/src/BasicModal.vue';
import { useMessage } from '/@/hooks/web/useMessage';
import { Modal } from 'ant-design-vue';
import { queryById } from '../AiragMcp.api';
import defaultLogo from '../imgs/mcpLogo.png';
import PluginToolEditModal from './PluginToolEditModal.vue';

const { createMessage } = useMessage();

interface McpToolItem {
  name: string;
  descr?: string;
  enabled?: boolean;
}

interface PluginToolItem {
  name: string;
  description?: string;
  path?: string;
  method?: string;
  parameters?: any[];
  responses?: any[];
  enabled?: boolean;
}

interface McpRecord {
  id?: string;
  name?: string;
  descr?: string;
  icon?: string;
  type?: string;
  category?: string;
  status?: string;
  synced?: boolean;
  tools?: McpToolItem[] | string;
  endpoint?: string;
  headers?: string;
  metadata?: string | any;
}

const emit = defineEmits(['register','edit', 'success']);
const record = ref<McpRecord | null>(null);
const tools = ref<McpToolItem[]>([]);
const pluginTools = ref<PluginToolItem[]>([]);
const loading = ref<boolean>(false);

// 工具编辑弹窗
const [registerToolEditModal, { openModal: openToolEditModal }] = useModal();

const displayIcon = computed(() => {
  const icon = record.value?.icon;
  if (!icon) return defaultLogo;
  return icon.startsWith('http') ? icon : icon;
});

const [registerModal, { closeModal }] = useModalInner(async (data: McpRecord) => {
  if(!data?.id){
    record.value = { ...data };
    // 根据category初始化工具列表
    if (data.category === 'plugin') {
      try {
        const toolsData = typeof data.tools === 'string' ? JSON.parse(data.tools) : data.tools;
        pluginTools.value = Array.isArray(toolsData) ? toolsData : [];
      } catch (e) {
        pluginTools.value = [];
      }
      tools.value = [];
    } else {
      tools.value = Array.isArray(data.tools) ? data.tools : [];
      pluginTools.value = [];
    }
    return;
  }
  loading.value = true;
  try {
    const res = await queryById(data.id);
    // 后端返回结构直接使用 res.result 或 res depending on transform; 假设统一为 res.result
    const detail = res.result || res; // 兼容不同返回包装
    record.value = {
      id: detail.id,
      name: detail.name,
      descr: detail.descr,
      icon: detail.icon,
      type: detail.type,
      category: detail.category,
      status: detail.status,
      synced: !!detail.synced,
      endpoint: detail.endpoint,
      headers: detail.headers,
      metadata: detail.metadata,
    };
    
    // 根据category解析工具
    if (detail.category === 'plugin') {
      // 插件类型：解析tools字段为插件工具列表
      let parsedPluginTools: PluginToolItem[] = [];
      const rawTools = detail.tools;
      if (rawTools) {
        try {
          if (typeof rawTools === 'string') {
            parsedPluginTools = JSON.parse(rawTools);
          } else if (Array.isArray(rawTools)) {
            parsedPluginTools = rawTools;
          }
        } catch (e) {
          parsedPluginTools = [];
        }
      }
      // 确保每个工具都有enabled字段，默认为true
      pluginTools.value = parsedPluginTools.map((t: any) => ({
        ...t,
        enabled: t.enabled !== undefined ? t.enabled : true
      }));
      tools.value = [];
    } else {
      // MCP类型：解析tools字段为MCP工具列表
      let parsedTools: McpToolItem[] = [];
      const rawTools = detail.tools;
      if (rawTools) {
        try {
          if (typeof rawTools === 'string') {
            const arr = JSON.parse(rawTools);
            parsedTools = arr.map((t: any) => ({ 
              name: t.name, 
              descr: t.description,
              enabled: t.enabled !== undefined ? t.enabled : true
            }));
          } else if (Array.isArray(rawTools)) {
            parsedTools = rawTools.map((t: any) => ({ 
              name: t.name, 
              descr: t.description,
              enabled: t.enabled !== undefined ? t.enabled : true
            }));
          }
        } catch (e) {
          parsedTools = [];
        }
      }
      tools.value = parsedTools;
      pluginTools.value = [];
    }
  } finally {
    loading.value = false;
  }
});


function handleAddTool() {
  openToolEditModal(true, {
    pluginId: record.value?.id,
    plugin: record.value,
    tool: null, // 新增
  });
}

function handleEditTool(tool: PluginToolItem) {
  openToolEditModal(true, {
    pluginId: record.value?.id,
    plugin: record.value,
    tool: tool,
  });
}

function getMethodColor(method: string): string {
  const colorMap: Record<string, string> = {
    'GET': 'blue',
    'POST': 'green',
    'PUT': 'orange',
    'DELETE': 'red',
    'PATCH': 'purple',
  };
  return colorMap[method] || 'default';
}

async function handleToolEnabledChange(tool: PluginToolItem) {
  // 更新工具启用状态
  if (!record.value?.id) return;
  
  try {
    const res = await queryById(record.value.id);
    const detail = res.result || res;
    let tools: any[] = [];
    if (detail.tools) {
      try {
        tools = typeof detail.tools === 'string' ? JSON.parse(detail.tools) : detail.tools;
      } catch (e) {
        tools = [];
      }
    }
    const index = tools.findIndex((t: any) => t.name === tool.name);
    if (index >= 0) {
      tools[index].enabled = tool.enabled;
      const { saveTools } = await import('../AiragMcp.api');
      await saveTools(record.value.id, JSON.stringify(tools));
    }
  } catch (e) {
    console.error('更新工具启用状态失败:', e);
    // 恢复状态
    tool.enabled = !tool.enabled;
  }
}

function handleDeleteTool(tool: PluginToolItem) {
  // 删除工具前进行二次确认
  Modal.confirm({
    title: '确认删除',
    content: `确定要删除工具"${tool.name}"吗？此操作不可恢复。`,
    okText: '确定',
    okType: 'danger',
    cancelText: '取消',
    onOk: async () => {
      if (!record.value?.id) {
        createMessage.error('插件ID不存在');
        return;
      }
      
      try {
        // 获取最新的工具列表
        const res = await queryById(record.value.id);
        const detail = res.result || res;
        let tools: any[] = [];
        if (detail.tools) {
          try {
            tools = typeof detail.tools === 'string' ? JSON.parse(detail.tools) : detail.tools;
          } catch (e) {
            tools = [];
          }
        }
        
        // 从工具列表中移除该工具
        const filteredTools = tools.filter((t: any) => t.name !== tool.name);
        
        if (filteredTools.length === tools.length) {
          createMessage.warning('未找到要删除的工具');
          return;
        }
        
        // 保存更新后的工具列表
        const { saveTools } = await import('../AiragMcp.api');
        const saveRes = await saveTools(record.value.id, JSON.stringify(filteredTools));
        
        if (saveRes.success) {
          createMessage.success('删除成功');
          // 更新前端显示
          pluginTools.value = pluginTools.value.filter((t: any) => t.name !== tool.name);
          // 触发成功事件，通知父组件刷新
          emit('success');
        } else {
          createMessage.error(saveRes.message || '删除失败');
        }
      } catch (e) {
        console.error('删除工具失败:', e);
        createMessage.error('删除工具失败');
      }
    },
  });
}

function emitEdit() {
  // 触发编辑事件，传递record给父组件
  if (record.value) {
    emit('edit', record.value);
    closeModal();
  }
}

function handleToolEditSuccess() {
  // 重新加载详情数据
  if (record.value?.id) {
    loading.value = true;
    queryById(record.value.id).then((res: any) => {
      const detail = res.result || res;
      
      // 更新record信息
      record.value = {
        id: detail.id,
        name: detail.name,
        descr: detail.descr,
        icon: detail.icon,
        type: detail.type,
        category: detail.category,
        status: detail.status,
        synced: !!detail.synced,
        endpoint: detail.endpoint,
        headers: detail.headers,
        metadata: detail.metadata,
      };
      
      // 根据category解析工具
      if (detail.category === 'plugin') {
        // 插件类型：解析tools字段为插件工具列表
        let parsedPluginTools: PluginToolItem[] = [];
        const rawTools = detail.tools;
        if (rawTools) {
          try {
            if (typeof rawTools === 'string') {
              parsedPluginTools = JSON.parse(rawTools);
            } else if (Array.isArray(rawTools)) {
              parsedPluginTools = rawTools;
            }
          } catch (e) {
            parsedPluginTools = [];
          }
        }
        pluginTools.value = parsedPluginTools.map((t: any) => ({
          ...t,
          enabled: t.enabled !== undefined ? t.enabled : true
        }));
        tools.value = [];
      } else {
        // MCP类型：解析tools字段为MCP工具列表
        let parsedTools: McpToolItem[] = [];
        const rawTools = detail.tools;
        if (rawTools) {
          try {
            if (typeof rawTools === 'string') {
              const arr = JSON.parse(rawTools);
              parsedTools = arr.map((t: any) => ({ name: t.name, descr: t.description }));
            } else if (Array.isArray(rawTools)) {
              parsedTools = rawTools.map((t: any) => ({ name: t.name, descr: t.description }));
            }
          } catch (e) {
            parsedTools = [];
          }
        }
        tools.value = parsedTools;
        pluginTools.value = [];
      }
      loading.value = false;
      // 触发success事件，通知列表页刷新
      emit('success');
    }).catch(() => {
      loading.value = false;
    });
  }
}
</script>
<style scoped lang="less">
.detail-modal {
  padding: 12px 16px 8px 16px;
  max-height: 520px;
  overflow-y: auto;
  .detail-header {
    display: flex;
    align-items: center;
    .detail-icon {
      width: 56px;
      height: 56px;
      border-radius: 8px;
      margin-right: 16px;
      background: #f5f6f7;
      object-fit: cover;
    }
    .detail-titles {
      flex: 1;
      min-width: 0;
      .detail-name {
        font-size: 20px;
        font-weight: 600;
        color: #1f2329;
        line-height: 28px;
        max-width: 480px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      .detail-type-status { margin-top: 4px; }
    }
  }
  .detail-descr {
    margin-top: 12px;
    font-size: 13px;
    line-height: 20px;
    color: #4e5969;
    background: #f5f6f7;
    padding: 8px 12px;
    border-radius: 6px;
    max-height: 80px;
    overflow-y: auto;
  }
  .tools-wrapper {
    margin-top: 16px;
    .tools-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12px;
    }
    .tools-title {
      font-size: 16px;
      font-weight: 600;
    }
    .tools-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 12px;
      max-height: 280px;
      overflow-y: auto;
      padding-right: 4px;
    }
    .tool-item {
      border: 1px solid #e5e6eb;
      border-radius: 8px;
      background: #fff;
      padding: 10px 12px;
      min-height: 88px;
      display: flex;
      flex-direction: column;
      transition: box-shadow 0.25s;
      cursor: default;
      &:hover { box-shadow: 0 4px 10px rgba(0,0,0,0.08); }
      &.tool-item-plugin {
        cursor: pointer;
      }
      .tool-header-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 6px;
        .tool-actions {
          display: flex;
          align-items: center;
          gap: 8px;
          
          .ant-btn-text {
            padding: 0;
            height: 24px;
            width: 24px;
            display: flex;
            align-items: center;
            justify-content: center;
            
            &:hover {
              background-color: rgba(255, 77, 79, 0.1);
            }
          }
        }
      }
      .tool-name {
        font-size: 14px;
        font-weight: 600;
        color: #1d2129;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        flex: 1;
      }
      .tool-descr {
        font-size: 12px;
        color: #4e5969;
        line-height: 18px;
        flex: 1;
        overflow: hidden;
        text-overflow: ellipsis;
        margin-bottom: 8px;
      }
      .tool-meta {
        display: flex;
        align-items: center;
        gap: 8px;
        margin-top: auto;
        .tool-path {
          font-size: 12px;
          color: #86909c;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }
  }
}

.detail-modal-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding-right: 40px;
  
  .detail-edit-btn {
    padding: 4px 8px;
    &:hover {
      color: #1890ff;
      background-color: rgba(24, 144, 255, 0.1);
    }
  }
}
</style>
<style lang="less">
.airag-mcp-detail-modal { 
  .jeecg-basic-modal-close > span { margin-left: 0 !important; }
  
  :deep(.ant-modal-header) {
    padding: 16px 24px;
  }
  
  :deep(.ant-modal-title) {
    width: 100%;
  }
}
</style>
