<template>
  <PageWrapper title="预警规则配置" content="管理系统业务预警规则">
    <a-card :bordered="false">
      <JVxeTable
        ref="ruleTable"
        toolbar
        row-number
        :columns="columns"
        :dataSource="dataSource"
      >
        <template #toolbarSuffix>
          <a-button type="primary" @click="handleSave">保存</a-button>
          <a-button danger @click="handleDelete">删除</a-button>
        </template>
      </JVxeTable>
    </a-card>
  </PageWrapper>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue';
import { JVxeTable } from '/@/components/jeecg/JVxeTable';
import { JVxeColumn, JVxeTableInstance, JVxeTypes } from '/@/components/jeecg/JVxeTable/types';
import { PageWrapper } from '/@/components/Page';
import { useMessage } from '/@/hooks/web/useMessage';
import { defHttp } from '/@/utils/http/axios';
import { getBizAlertRuleList, addBizAlertRule, editBizAlertRule, deleteBizAlertRule } from '/@/api/monitor/bizMonitorApi';
import { onMounted } from 'vue';

const ruleTable = ref<JVxeTableInstance>();

// 预警规则列表
const ruleList = ref([]);

const columns = ref<JVxeColumn[]>([
  {
    title: '规则名称',
    key: 'ruleName',
    type: JVxeTypes.input,
    placeholder: '请输入规则名称'
  },
  {
    title: '业务类型',
    key: 'bizType',
    type: JVxeTypes.select,
    options: [
      { title: '订单', value: 'ORDER' },
      { title: '支付', value: 'PAYMENT' },
      { title: '用户', value: 'USER' },
      { title: '系统', value: 'SYSTEM' }
    ],
    placeholder: '请选择业务类型'
  },
  {
    title: '预警条件',
    key: 'condition',
    type: JVxeTypes.input,
    placeholder: '请输入预警条件'
  },
  {
    title: '预警阈值',
    key: 'threshold',
    type: JVxeTypes.inputNumber,
    placeholder: '请输入预警阈值'
  },
  {
    title: '预警级别',
    key: 'level',
    type: JVxeTypes.select,
    options: [
      { title: '低', value: 'LOW' },
      { title: '中', value: 'MEDIUM' },
      { title: '高', value: 'HIGH' }
    ],
    placeholder: '请选择预警级别'
  },
  {
    title: '通知方式',
    key: 'notifyMethod',
    type: JVxeTypes.select,
    options: [
      { title: '邮件', value: 'EMAIL' },
      { title: '短信', value: 'SMS' },
      { title: '微信', value: 'WECHAT' }
    ],
    placeholder: '请选择通知方式'
  },
  {
    title: '状态',
    key: 'status',
    type: JVxeTypes.select,
    options: [
      { title: '启用', value: 'ACTIVE' },
      { title: '禁用', value: 'INACTIVE' }
    ]
  }
]);

// 数据源
const dataSource = ref([]);

// 加载规则列表
  const loadRuleList = async () => {
    try {
      const response = await getBizAlertRuleList({});
      if (response.success) {
        dataSource.value = response.result.records;
      }
    } catch (error) {
      console.error('加载规则列表失败:', error);
    }
  };

// 保存规则
const handleSave = async () => {
  try {
    const tableData = ruleTable.value?.getTableData();
    console.log('保存规则:', tableData);
    // 这里可以调用保存接口
    // await defHttp.post({ url: api.alertRule.save, data: tableData });
    loadRuleList();
  } catch (error) {
    console.error('保存规则失败:', error);
  }
};

// 删除规则
const handleDelete = async () => {
  try {
    // 这里可以调用删除接口
    console.log('删除规则');
  } catch (error) {
    console.error('删除规则失败:', error);
  }
};

onMounted(() => {
  loadRuleList();
});
</script>
