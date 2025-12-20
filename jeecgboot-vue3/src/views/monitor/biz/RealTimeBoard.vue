<template>
  <div class="p-4">
    <a-card :bordered="false" style="height: 100%">
      <div class="mb-4">
        <h2>实时业务看板</h2>
        <div class="text-sm text-gray-500">数据每30秒自动刷新</div>
      </div>

      <!-- 业务指标卡片 -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
        <a-statistic-card
          title="今日订单量"
          :value="metrics.todayOrders"
          suffix="单"
          :value-style="{ color: '#3f8600' }"
        />
        <a-statistic-card
          title="订单成功率"
          :value="metrics.orderSuccessRate"
          suffix="%"
          :value-style="{ color: '#1890ff' }"
        />
        <a-statistic-card
          title="用户活跃度"
          :value="metrics.userActivity"
          suffix="人"
          :value-style="{ color: '#722ed1' }"
        />
        <a-statistic-card
          title="支付异常率"
          :value="metrics.paymentErrorRate"
          suffix="%"
          :value-style="{ color: metrics.paymentErrorRate > 5 ? '#f5222d' : '#faad14' }"
        />
      </div>

      <!-- 业务链路拓扑图 -->
      <div class="mb-6">
        <h3 class="text-lg font-semibold mb-3">业务链路拓扑图</h3>
        <div id="bizTopology" style="height: 400px"></div>
      </div>

      <!-- 最近10条业务异常日志 -->
      <div>
        <h3 class="text-lg font-semibold mb-3">最近10条业务异常日志</h3>
        <a-list
          :data-source="errorLogs"
          :locale="{ emptyText: '暂无异常日志' }"
        >
          <template #renderItem="{ item }">
            <a-list-item>
              <a-list-item-meta>
                <template #title>
                  <span class="mr-2" :style="{ color: getErrorLevelColor(item.level) }">
                    {{ item.level === 'critical' ? '紧急' : item.level === 'warning' ? '警告' : '一般' }}
                  </span>
                  <span>{{ item.title }}</span>
                </template>
                <template #description>
                  <div class="flex justify-between">
                    <span>{{ item.description }}</span>
                    <span class="text-sm text-gray-500">{{ item.time }}</span>
                  </div>
                </template>
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>
      </div>
    </a-card>
  </div>
</template>

<script lang="ts" name="RealTimeBoard" setup>
import { ref, onMounted, onUnmounted } from 'vue';
import * as echarts from 'echarts';
import dayjs from 'dayjs';

// 业务指标数据
const metrics = ref({
  todayOrders: 0,
  orderSuccessRate: 0,
  userActivity: 0,
  paymentErrorRate: 0,
});

// 异常日志数据
const errorLogs = ref([
  { id: 1, level: 'critical', title: '支付服务异常', description: '支付网关响应超时', time: '2025-07-29 14:30:00' },
  { id: 2, level: 'warning', title: '订单处理延迟', description: '订单处理时间超过10秒', time: '2025-07-29 14:25:00' },
]);

// 获取异常级别颜色
const getErrorLevelColor = (level: string) => {
  switch (level) {
    case 'critical':
      return '#f5222d';
    case 'warning':
      return '#faad14';
    default:
      return '#1890ff';
  }
};

// 初始化业务链路拓扑图
const initTopologyChart = () => {
  const chartDom = document.getElementById('bizTopology');
  if (!chartDom) return;

  const myChart = echarts.init(chartDom);
  const option = {
    tooltip: {
      trigger: 'item',
      triggerOn: 'mousemove',
    },
    series: [
      {
        type: 'graph',
        layout: 'dagre',
        data: [
          { name: '用户注册', symbolSize: 80, itemStyle: { color: '#52c41a' } },
          { name: '下单', symbolSize: 80, itemStyle: { color: '#1890ff' } },
          { name: '支付', symbolSize: 80, itemStyle: { color: '#faad14' } },
        ],
        links: [
          { source: '用户注册', target: '下单' },
          { source: '下单', target: '支付' },
        ],
        roam: true,
        label: {
          show: true,
          fontSize: 14,
        },
      },
    ],
  };

  option && myChart.setOption(option);
};

// 刷新数据
const refreshData = () => {
  console.log('刷新业务数据');
  // 这里可以调用API获取最新数据
};

let refreshInterval: any;

onMounted(() => {
  initTopologyChart();
  refreshData();
  // 每30秒自动刷新
  refreshInterval = setInterval(() => {
    refreshData();
  }, 30000);
});

onUnmounted(() => {
  clearInterval(refreshInterval);
});
</script>
