<template>
  <PageWrapper>
    <a-card :bordered="false">
      <BasicTable @register="registerTable" />
    </a-card>
  </PageWrapper>
</template>

<script lang="ts" setup>
  import { PageWrapper } from '/@/components/Page';
  import { BasicTable, useTable } from '/@/components/Table';
  import { mapTableTotalSummary } from '/@/utils/common/compUtils';

  const [registerTable] = useTable({
    rowKey: 'id',
    bordered: true,
    canResize: false,
    columns: [
      { title: '姓名', dataIndex: 'name' },
      { title: '贡献点', dataIndex: 'point' },
      { title: '等级', dataIndex: 'level' },
      { title: '更新时间', dataIndex: 'updateTime' },
    ],
    dataSource: [
      { id: 0, name: '张三', point: 23, level: 3, updateTime: '2019-8-14' },
      { id: 1, name: '小鹿', point: 33, level: 9, updateTime: '2019-8-10' },
      { id: 2, name: '小王', point: 6, level: 1, updateTime: '2019-8-13' },
      { id: 3, name: '李四', point: 53, level: 8, updateTime: '2019-8-12' },
      { id: 4, name: '小红', point: 44, level: 5, updateTime: '2019-8-11' },
      { id: 5, name: '王五', point: 97, level: 10, updateTime: '2019-8-10' },
      { id: 6, name: '小明', point: 33, level: 2, updateTime: '2019-8-10' },
      { id: 7, name: '小张', point: 33, level: 4, updateTime: '2019-8-10' },
      { id: 8, name: '小六', point: 33, level: 2, updateTime: '2019-8-10' },
      { id: 9, name: '小五', point: 33, level: 7, updateTime: '2019-8-10' },
      { id: 10, name: '小赵', point: 33, level: 2, updateTime: '2019-8-10' },
      { id: 11, name: '李华', point: 33, level: 8, updateTime: '2019-8-10' },
      { id: 12, name: '小康', point: 33, level: 5, updateTime: '2019-8-10' },
    ],
    // 显示底部合计
    showSummary: true,
    // 底部合计计算方法
    summaryFunc: onSummary,
  });

  function onSummary(tableData: Recordable[]) {
    // 可用工具方法自动计算合计
    const totals = mapTableTotalSummary(tableData, ['point', 'level']);
    return [
      totals,
      {
        _row: '平均',
        _index: '平均',
        // 计算平均值
        point: (totals.point / tableData.length).toFixed(2),
        level: (totals.level / tableData.length).toFixed(0),
      },
    ];
  }
</script>
