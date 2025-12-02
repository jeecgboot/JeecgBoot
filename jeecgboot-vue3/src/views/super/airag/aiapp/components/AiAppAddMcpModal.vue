<template>
  <div class="p-2">
    <BasicModal destroyOnClose @register="registerModal" :canFullscreen="false" width="600px" :title="title" @ok="handleOk" @cancel="handleCancel">
      <div class="flex header">
        <a-input
          @keyup.enter="loadMcpData"
          class="header-search"
          size="small"
          v-model:value="searchText"
          placeholder="请输入MCP名称，回车搜索"
        ></a-input>
      </div>
      <a-row :span="24">
        <a-col :span="12" v-for="item in mcpOption" :key="item.id" @click="handleSelect(item)">
          <a-card :body-style="{padding: '10px 12px'}" hoverable :class="['mcp-card', { 'is-active': item.checked }]">
            <div class="mcp-card-header">
              <div class="mcp-card-left">
                <img class="mcp-card-icon" :src="getIcon(item.icon)" />
                <div class="mcp-card-info">
                  <div class="mcp-card-name" :title="item.name">{{ item.name }}</div>
                  <div class="mcp-card-meta">
                    <div class="pill type-pill" :title="'类型: '+(item.category === 'plugin' ? '插件' : 'MCP')">
                      <Icon :icon="getCategoryIcon(item.category)" class="pill-icon" />
                      <span class="pill-text">{{ item.category === 'plugin' ? '插件' : 'MCP' }}</span>
                    </div>
                    <div class="pill tool-pill" :title="getToolCount(item.metadata)+' 个工具'">
                      <Icon icon="ant-design:tool-outlined" class="pill-icon" />
                      <span class="pill-text">{{ getToolCount(item.metadata) }}</span>
                    </div>
                  </div>
                </div>
              </div>
              <a-checkbox v-model:checked="item.checked" @click.stop class="mcp-card-checker" @change="(e)=>handleChange(e,item)"> </a-checkbox>
            </div>
          </a-card>
        </a-col>
      </a-row>
      <div v-if="pluginIds.length > 0" class="use-select">
        已选择 {{ pluginIds.length }} 个MCP
        <span style="margin-left: 8px; color: #3d79fb; cursor: pointer" @click="handleClearClick">清空</span>
      </div>
      <Pagination
        v-if="mcpOption.length > 0"
        :current="pageNo"
        :page-size="pageSize"
        :page-size-options="pageSizeOptions"
        :total="total"
        :showQuickJumper="true"
        :showSizeChanger="true"
        @change="handlePageChange"
        class="list-footer"
        size="small"
      />
    </BasicModal>
  </div>
</template>

<script lang="ts">
  import { ref } from 'vue';
  import BasicModal from '@/components/Modal/src/BasicModal.vue';
  import { useModalInner } from '@/components/Modal';
  import { Pagination } from 'ant-design-vue';
  import { list as mcpList } from '@/views/super/airag/aimcp/AiragMcp.api';
  import { getFileAccessHttpUrl } from '@/utils/common/compUtils';
  import defaultLogo from '@/views/super/airag/aimcp/imgs/mcpLogo.png';
  import { Icon } from '/@/components/Icon';

  export default {
    name: 'AiAppAddMcpModal',
    components: {
      Pagination,
      BasicModal,
      Icon,
    },
    emits: ['success', 'register'],
    setup(props, { emit }) {
      const title = ref<string>('添加关联MCP');

      const mcpOption = ref<any>([]);
      const pluginIds = ref<any>([]); // 仅存放id
      const pluginDataList = ref<any>([]); // 选中对象

      const pageNo = ref<number>(1);
      const pageSize = ref<number>(10);
      const total = ref<number>(0);
      const searchText = ref<string>('');
      const pageSizeOptions = ref<any>(['10', '20', '30']);

      const [registerModal, { closeModal, setModalProps }] = useModalInner(async (data) => {
        pluginIds.value = data.pluginIds ? [...data.pluginIds] : [];
        pluginDataList.value = data.pluginDataList ? [...data.pluginDataList] : [];
        setModalProps({ minHeight: 500, bodyStyle: { padding: '10px' } });
        loadMcpData();
      });

      function getIcon(icon){
        return icon ? getFileAccessHttpUrl(icon) : defaultLogo;
      }

      async function handleOk() {
        // 拼接插件结构，使用item的category字段
        const plugins = pluginDataList.value.map((item:any)=>({ 
          pluginId: item.id, 
          pluginName: item.name, 
          category: item.category || 'mcp'
        }));
        emit('success', pluginIds.value, pluginDataList.value, plugins);
        handleCancel();
      }

      function handleCancel() {
        closeModal();
      }

      function handleSelect(item:any){
        const id = item.id;
        const target = mcpOption.value.find((it:any)=> it.id === id);
        if(target){
          target.checked = !target.checked;
        }
        if(!pluginIds.value || pluginIds.value.length===0){
          pluginIds.value.push(id);
          pluginDataList.value.push(item);
          return;
        }
        const findIndex = pluginIds.value.findIndex((val:any)=> val === id);
        if(findIndex === -1){
          pluginIds.value.push(id);
          pluginDataList.value.push(item);
        }else{
          pluginIds.value.splice(findIndex,1);
          pluginDataList.value.splice(findIndex,1);
        }
      }

      function loadMcpData(){
        const params = { pageNo: pageNo.value, pageSize: pageSize.value, status: 'enable', synced: 1, name: searchText.value };
        mcpList(params).then((res:any)=>{
          if (res.records) {
            const records = res.records || [];
            if(pluginIds.value.length>0){
              for(const rec of records){
                if(pluginIds.value.includes(rec.id)){
                  rec.checked = true;
                }
              }
            }
            mcpOption.value = records;
            total.value = res.total;
          }else{
            mcpOption.value = [];
            total.value = 0;
          }
        });
      }

      function handlePageChange(page:number, current:number){
        pageNo.value = page;
        pageSize.value = current;
        loadMcpData();
      }

      function handleClearClick(){
        pluginIds.value = [];
        pluginDataList.value = [];
        mcpOption.value.forEach((item:any)=> item.checked = false);
      }

      function handleChange(e:any, item:any){
        if(e.target.checked){
          pluginIds.value.push(item.id);
          pluginDataList.value.push(item);
        }else{
          const findIndex = pluginIds.value.findIndex((val:any)=> val === item.id);
          if(findIndex>-1){
            pluginIds.value.splice(findIndex,1);
            pluginDataList.value.splice(findIndex,1);
          }
        }
      }

      // 工具数量：从 metadata 中读取 tool_count
      function getToolCount(metadata: any): number {
        if (!metadata) return 0;
        let metaObj: any = metadata;
        if (typeof metadata === 'string') {
          try {
            metaObj = JSON.parse(metadata);
          } catch (e) {
            return 0;
          }
        }
        const count = metaObj.tool_count || metaObj.toolCount || 0;
        return typeof count === 'number' ? count : parseInt(count, 10) || 0;
      }

      // 类型图标映射
      function getTypeIcon(type?: string) {
        switch (type) {
          case 'sse':
            return 'ant-design:thunderbolt-outlined';
          case 'stdio':
            return 'ant-design:code-outlined';
          default:
            return 'ant-design:appstore-outlined';
        }
      }

      // category图标映射
      function getCategoryIcon(category?: string) {
        if (category === 'plugin') {
          return 'ant-design:api-outlined';
        }
        return 'ant-design:tool-twotone';
      }

      return {
        registerModal,
        title,
        handleOk,
        handleCancel,
        mcpOption,
        pluginIds,
        pluginDataList,
        pageNo,
        pageSize,
        pageSizeOptions,
        total,
        handlePageChange,
        searchText,
        loadMcpData,
        handleClearClick,
        handleChange,
        handleSelect,
        getIcon,
        getToolCount,
        getTypeIcon,
        getCategoryIcon,
      };
    },
  };
</script>

<style scoped lang="less">
  .header {
    color: #646a73;
    width: 100%;
    justify-content: space-between;
    margin-bottom: 10px;
    .header-search {
      width: 200px;
    }
  }
  .mcp-card {
    margin-bottom: 10px;
    margin-right: 10px;
    border: 1px solid #e5e6eb;
    border-radius: 8px;
    background: #fff;
    transition: box-shadow 0.25s, border-color 0.25s;
    cursor: pointer;
    &.is-active {
      border-color: #3370ff;
      box-shadow: 0 4px 10px rgba(0,0,0,0.08);
    }
    &:hover {
      box-shadow: 0 4px 10px rgba(0,0,0,0.08);
    }
  }
  .mcp-card-header {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    width: 100%;
  }
  .mcp-card-left {
    display: flex;
    align-items: flex-start;
    min-width: 0;
    flex: 1;
  }
  .mcp-card-icon {
    width: 40px;
    height: 40px;
    border-radius: 8px;
    object-fit: cover;
    background: #f5f6f7;
    flex-shrink: 0;
  }
  .mcp-card-info {
    margin-left: 8px;
    flex: 1;
    min-width: 0;
  }
  .mcp-card-name {
    font-size: 14px;
    font-weight: 600;
    color: #1d2129;
    line-height: 20px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    margin-bottom: 6px;
  }
  .mcp-card-meta {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
  }
  .mcp-card-checker {
    margin-left: 12px;
    flex-shrink: 0;
  }
  .pill {
    display: inline-flex;
    align-items: center;
    padding: 2px 8px 2px 6px;
    border-radius: 12px;
    font-size: 11px;
    line-height: 16px;
    font-weight: 500;
    backdrop-filter: saturate(180%) blur(4px);
    box-shadow: 0 0 0 1px rgba(0,0,0,0.05);
    .pill-icon { 
      margin-right: 3px; 
      font-size: 12px;
    }
  }
  .type-pill { 
    background: linear-gradient(135deg,#e6f4ff,#f0f9ff); 
    color:#0958d9; 
  }
  .tool-pill { 
    background: linear-gradient(135deg,#f5f6f7,#f0f1f2); 
    color:#555; 
  }
  .use-select {
    color: #646a73;
    position: absolute;
    bottom: 0;
    left: 20px;
  }
  .list-footer {
    position: absolute;
    bottom: 0;
    right: 10px;
  }
</style>
