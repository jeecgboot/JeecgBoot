<template>
  <PageWrapper title="EditableCell ID字段测试 (Issue #8924)">
    <a-alert
      message="🧪 测试目的：验证 beforeEditSubmit 是否会过滤掉 id 字段"
      description="请编辑任意单元格并点击对号，查看下方的测试结果。如果 record 中没有 id 字段，说明问题存在。"
      type="warning"
      show-icon
      class="mb-4"
    />

    <div class="p-4">
      <a-card title="🔬 测试场景1：id 字段不在 columns 中（最常见场景）" class="mb-4">
        <a-alert
          message="⚠️ 核心测试：id 在数据中，但不在 columns 中显示"
          description="这是最常见的场景：主键字段通常不需要在表格中显示，但在更新数据时必须使用。"
          type="info"
          show-icon
          class="mb-3"
        />
        
        <a-space direction="vertical" style="width: 100%">
          <a-card size="small" title="📋 测试数据说明" :bordered="false">
            <p><strong>数据源包含：</strong>id, name, age, email, address</p>
            <p><strong>Columns 显示：</strong>name, age, email, address（⚠️ 没有 id 列）</p>
            <p><strong>rowKey 配置：</strong>'id'</p>
          </a-card>
          
          <BasicTable
            @register="registerTable1"
            :beforeEditSubmit="handleBeforeEditSubmit1"
          />
          
          <a-card 
            size="small" 
            :title="testResult1.title" 
            :bordered="false"
            :headStyle="{ backgroundColor: testResult1.bgColor, color: 'white' }"
          >
            <a-descriptions bordered :column="1" size="small">
              <a-descriptions-item label="是否包含 id">
                <a-tag :color="testResult1.hasId ? 'success' : 'error'">
                  {{ testResult1.hasId ? '✅ 包含' : '❌ 不包含' }}
                  {{ testResult1.hasId ? `(id=${testResult1.idValue})` : '' }}
                </a-tag>
              </a-descriptions-item>
              <a-descriptions-item label="record 包含的字段">
                <a-tag v-for="field in testResult1.fields" :key="field" color="blue">{{ field }}</a-tag>
              </a-descriptions-item>
              <a-descriptions-item label="完整 record 内容">
                <pre class="test-result-json">{{ testResult1.recordJson }}</pre>
              </a-descriptions-item>
              <a-descriptions-item label="测试结论">
                <a-alert 
                  :message="testResult1.conclusion" 
                  :type="testResult1.hasId ? 'success' : 'error'" 
                  show-icon 
                />
              </a-descriptions-item>
            </a-descriptions>
          </a-card>
        </a-space>
      </a-card>

      <a-card title="💡 测试说明" class="mb-4">
        <a-space direction="vertical" style="width: 100%">
          <a-alert
            message="如何进行测试？"
            description="1. 点击上方表格任意单元格进行编辑
2. 修改内容后点击对号 ✓ 提交
3. 查看测试结果，观察 record 是否包含 id 字段"
            type="info"
            show-icon
          />
          
          <a-alert
            message="预期结果"
            type="success"
            show-icon
          >
            <template #description>
              <p><strong>如果代码正常：</strong></p>
              <p>✅ record 应该包含 id 字段</p>
              <p>✅ 可以使用 record.id 进行数据更新</p>
              <p>✅ 控制台显示绿色成功消息</p>
            </template>
          </a-alert>
          
          <a-alert
            message="Bug 症状（Issue #8924）"
            type="error"
            show-icon
          >
            <template #description>
              <p><strong>如果存在 Bug：</strong></p>
              <p>❌ record 中没有 id 字段</p>
              <p>❌ record 只包含 columns 中定义的字段（name, age, email, address）</p>
              <p>❌ 无法执行数据更新操作</p>
              <p>❌ 控制台显示红色错误消息</p>
            </template>
          </a-alert>
          
          <a-card size="small" title="🔍 原因分析" :bordered="false">
            <p>原代码使用 <code>pick(record, keys)</code> 过滤字段：</p>
            <pre class="code-block">const keys = columns.map(c => c.dataIndex).filter(f => !!f);
// keys = ['name', 'age', 'email', 'address']  // ⚠️ 没有 id

record: pick(record, keys)
// 只保留 keys 中的字段，id 被过滤掉了</pre>
          </a-card>
        </a-space>
      </a-card>
    </div>
  </PageWrapper>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { PageWrapper } from '/@/components/Page';
  import { BasicTable, useTable, BasicColumn } from '/@/components/Table';
  import { useMessage } from '/@/hooks/web/useMessage';

  const { createMessage } = useMessage();

  // ============ 测试场景1：id字段不在columns中 ============
  const testResult1 = ref<{
    title: string;
    bgColor: string;
    hasId: boolean;
    idValue: any;
    fields: string[];
    recordJson: string;
    conclusion: string;
  }>({
    title: '📊 测试结果（点击对号后显示）',
    bgColor: '#909399',
    hasId: false,
    idValue: null,
    fields: [],
    recordJson: '暂无数据，请编辑单元格并点击对号',
    conclusion: '等待测试...',
  });

  const columns1: BasicColumn[] = [
    // 注意：这里没有 id 列
    {
      title: '姓名',
      dataIndex: 'name',
      width: 150,
      edit: true,
      editComponent: 'Input',
    },
    {
      title: '年龄',
      dataIndex: 'age',
      width: 120,
      edit: true,
      editComponent: 'InputNumber',
    },
    {
      title: '邮箱',
      dataIndex: 'email',
      width: 200,
      edit: true,
      editComponent: 'Input',
    },
    {
      title: '地址',
      dataIndex: 'address',
      width: 200,
      edit: true,
      editComponent: 'Input',
    },
  ];

  const dataSource1 = [
    { id: 1, name: '张三', age: 25, email: 'zhangsan@example.com', address: '北京市朝阳区' },
    { id: 2, name: '李四', age: 30, email: 'lisi@example.com', address: '上海市浦东新区' },
    { id: 3, name: '王五', age: 28, email: 'wangwu@example.com', address: '广州市天河区' },
  ];

  const [registerTable1] = useTable({
    rowKey: 'id',  // 使用默认id字段作为主键,
    columns: columns1,
    dataSource: dataSource1,
    pagination: false,
    showIndexColumn: true,
    canResize: false,
  });

  async function handleBeforeEditSubmit1({ record, index, key, value }) {
    console.log('🧪 场景1 测试 - beforeEditSubmit 接收到的数据：', { record, index, key, value });
    console.log('🔍 record 详细内容：', JSON.stringify(record, null, 2));
    
    // 分析 record
    const hasId = 'id' in record;
    const fields = Object.keys(record);
    
    // 更新测试结果
    testResult1.value = {
      title: hasId ? '✅ 测试通过' : '❌ 测试失败 - 发现 Bug！',
      bgColor: hasId ? '#67C23A' : '#F56C6C',
      hasId: hasId,
      idValue: record.id || null,
      fields: fields,
      recordJson: JSON.stringify(record, null, 2),
      conclusion: hasId 
        ? `✅ record 中包含 id 字段（值为 ${record.id}），可以正常更新数据` 
        : `❌ Bug 确认：record 中缺少 id 字段！只包含 ${fields.join(', ')}。这会导致无法执行数据更新操作。`,
    };

    if (!hasId) {
      createMessage.error('❌ 测试失败：record 中缺少 id 字段！这就是 Issue #8924 描述的问题。');
      console.error('❌ Bug 重现：数据源中有 id，但 beforeEditSubmit 收到的 record 中没有 id');
      return false;
    }

    createMessage.success(`✅ 测试通过：获取到 id=${record.id}`);
    console.log('✅ 模拟更新请求：', {
      url: '/api/user/update',
      params: { id: record.id, [key]: value },
    });
    
    return true;
  }

</script>

<style scoped>
  .test-result-json {
    font-size: 12px;
    line-height: 1.5;
    background-color: #f5f5f5;
    padding: 8px;
    border-radius: 4px;
    max-height: 200px;
    overflow: auto;
  }
  
  .code-block {
    font-size: 13px;
    line-height: 1.6;
    background-color: #282c34;
    color: #abb2bf;
    padding: 12px;
    border-radius: 4px;
    overflow-x: auto;
  }
  
  p {
    margin: 8px 0;
  }
</style>
