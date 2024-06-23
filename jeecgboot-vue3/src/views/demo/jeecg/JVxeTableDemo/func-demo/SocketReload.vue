<template>
  <a-card title="无痕刷新示例" :bordered="false">
    <div style="margin-bottom: 8px">
      <span>启用数据变动特效：</span>
      <a-switch v-model:checked="reloadEffect" />
    </div>

    <!--
      【无痕刷新大体思路】：
      1. 该功能依赖于【即时保存】功能，请先看即时保存示例
      2. 必须要有 socket-reload 属性，且设为 true
      3. 必须要有 socket-key 属性，该属性为当前表格的唯一标识，
         系统会自动更新所有 socket-key 相同的表格
      4. 在局部保存 edit-closed 事件中，
         保存成功后调用 socketSendUpdateRow 方法，将当前 row 传递过去即可 （见第 102 行）
    -->
    <JVxeTable
      ref="tableRef"
      rowNumber
      rowSelection
      keepSource
      socketReload
      socketKey="demo-socket-reload"
      :reloadEffect="reloadEffect"
      :height="340"
      :loading="loading"
      :columns="columns"
      :dataSource="dataSource"
      @valueChange="onValueChange"
      @edit-closed="handleEditClosed"
    />
  </a-card>
</template>

<script lang="ts" setup>
  // 无痕刷新示例
  import { ref } from 'vue';
  import { defHttp } from '/@/utils/http/axios';
  import { JVxeColumn, JVxeTableInstance, JVxeTypes } from '/@/components/jeecg/JVxeTable/types';
  import { useMessage } from '/@/hooks/web/useMessage';

  const { createMessage } = useMessage();
  const tableRef = ref<JVxeTableInstance>();
  // 是否启用日历刷新效果
  const reloadEffect = ref(true);
  const loading = ref(false);
  const dataSource = ref<Recordable[]>([]);
  const columns = ref<JVxeColumn[]>([
    { key: 'num', title: '序号', width: 80 },
    { key: 'enabled', title: '启用', width: 80, type: JVxeTypes.checkbox },
    { key: 'ship_name', title: '船名', width: 180, type: JVxeTypes.input },
    { key: 'call', title: '呼叫', width: 80, type: JVxeTypes.input },
    { key: 'len', title: '长', width: 80, type: JVxeTypes.input },
    { key: 'ton', title: '吨', width: 120, type: JVxeTypes.input },
    { key: 'payer', title: '付款方', width: 120, type: JVxeTypes.input },
    { key: 'count', title: '数', width: 40 },
    { key: 'company', title: '公司', minWidth: 180, type: JVxeTypes.input },
    { key: 'trend', title: '动向', width: 120, type: JVxeTypes.input },
  ]);

  // 查询url地址
  enum Api {
    getData = '/mock/vxe/getData',
  }

  loadData();

  // 加载数据
  function loadData() {
    loading.value = true;
    defHttp
      .get({
        url: Api.getData,
        params: { pageNo: 1, pageSize: 200 },
      })
      .then((result) => {
        dataSource.value = result.records;
      })
      .finally(() => {
        loading.value = false;
      });
  }

  /** 单元格值变化时触发的事件 */
  function onValueChange(event) {
    switch (event.type) {
      // 所有不能触发 editClosed 事件的组件，都需要定义在这里，可以安装你自己的业务需求来完善此处的case
      case JVxeTypes.radio:
      case JVxeTypes.checkbox:
        doSendUpdateRow(event);
        break;
    }
  }

  // 单元格编辑完成之后触发的事件
  function handleEditClosed(event) {
    doSendUpdateRow(event);
  }

  // 发送变更行请求
  function doSendUpdateRow(event) {
    let { $table, row, column } = event;
    let field = column.property;
    // 判断单元格值是否被修改
    if ($table.isUpdateByRow(row, field)) {
      // 校验当前行
      $table.validate(row).then((errMap) => {
        // 校验通过
        if (!errMap) {
          // 【模拟保存】（此处需要替换成真实的请求）
          let hideLoading = createMessage.loading(`正在保存"${column.title}"`, 0);
          setTimeout(() => {
            hideLoading();
            createMessage.success(`"${column.title}"保存成功！`);
            // 局部更新单元格为已保存状态
            $table.reloadRow(row, null, field);
            // 发送更新消息
            tableRef.value?.socketSendUpdateRow(row);
          }, 555);
        }
      });
    }
  }
</script>

<style scoped></style>
