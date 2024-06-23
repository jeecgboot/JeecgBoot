<template>
  <a-card title="弹出子表示例" :bordered="false">
    <!--
      【弹出子表大体思路】
      1. 必须要有 clickRowShowSubForm 属性，如果该属性设为false，那么就不会弹出子表
      2. 必须要有 subForm 插槽，用于规定弹出子表的内容
      3. highlightCurrentRow 属性可有可无，如果有则点击一行的时候，该行会背景色会常亮
    -->
    <!--
      【弹出详细信息大体思路】
      1. 必须要有 clickRowShowMainForm 属性，如果该属性设为false，那么就不会弹出详细信息
      2. 必须要有 mainForm 插槽，用于规定弹出的内容
    -->
    <JVxeTable
      toolbar
      rowNumber
      rowSelection
      highlightCurrentRow
      clickRowShowSubForm
      clickRowShowMainForm
      :height="750"
      :loading="loading"
      :columns="columns"
      :dataSource="dataSource"
      @detailsConfirm="handleDetailsConfirm"
    >
      <!-- 主表单 -->
      <template #mainForm="{ row }">
        <template v-if="row">
          <a-form ref="form2" :model="row" :rules="rules" :label-col="labelCol" :wrapper-col="wrapperCol">
            <a-row :gutter="8">
              <a-col :span="8">
                <a-form-item label="ID" name="id">
                  <a-input v-model:value="row.id" disabled />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="序号" name="num">
                  <a-input v-model:value="row.num" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="船名" name="ship_name">
                  <a-input v-model:value="row.ship_name" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="呼叫" name="call">
                  <a-input v-model:value="row.call" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="长" name="len">
                  <a-input v-model:value="row.len" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="吨" name="ton">
                  <a-input v-model:value="row.ton" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="付款方" name="payer">
                  <a-input v-model:value="row.payer" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="数" name="count">
                  <a-input v-model:value="row.count" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="公司" name="company">
                  <a-input v-model:value="row.company" />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="动向" name="trend">
                  <a-input v-model:value="row.trend" />
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </template>
      </template>

      <!-- 子表单 -->
      <template #subForm="{ row }">
        <template v-if="loadSubData(row)">
          <JVxeTable
            ref="subFormTable"
            height="auto"
            :max-height="350"
            :loading="subTable.loading"
            :columns="subTable.columns"
            :dataSource="subTable.dataSource"
          />
        </template>
      </template>
    </JVxeTable>
  </a-card>
</template>

<script lang="ts" setup>
  // 弹出子表示例
  import { reactive, ref } from 'vue';
  import { defHttp } from '/@/utils/http/axios';
  import { JVxeColumn, JVxeTypes } from '/@/components/jeecg/JVxeTable/types';
  import { useMessage } from '/@/hooks/web/useMessage';

  const { createMessage } = useMessage();
  const loading = ref(false);
  const dataSource = ref([]);
  const columns = ref<JVxeColumn[]>([
    { key: 'num', title: '序号', width: '80px' },
    { key: 'ship_name', title: '船名', width: '180px', type: JVxeTypes.input },
    { key: 'call', title: '呼叫', width: '80px' },
    { key: 'len', title: '长', width: '80px' },
    { key: 'ton', title: '吨', width: '120px' },
    { key: 'payer', title: '付款方', width: '120px' },
    { key: 'count', title: '数', width: '40px' },
    {
      key: 'company',
      title: '公司',
      minWidth: '180px',
      // 是否点击显示详细信息
      // 只有当前单元格不能编辑的时候才能生效
      // 如果不设的话，点击就只弹出子表，不会弹出主表的详细信息
      showDetails: true,
    },
    { key: 'trend', title: '动向', width: '120px' },
  ]);
  const selectedRows = ref([]);
  // 子表的信息
  const subTable = reactive({
    currentRowId: null,
    loading: false,
    pagination: { current: 1, pageSize: 200, pageSizeOptions: ['100', '200'], total: 0 },
    selectedRows: [],
    dataSource: [],
    columns: [
      { key: 'dd_num', title: '调度序号', width: '120px' },
      { key: 'tug', title: '拖轮', width: '180px', type: JVxeTypes.input },
      { key: 'work_start_time', title: '作业开始时间', width: '180px', type: JVxeTypes.input },
      { key: 'work_stop_time', title: '作业结束时间', width: '180px', type: JVxeTypes.input },
      { key: 'type', title: '船舶分类', width: '120px', type: JVxeTypes.input },
      { key: 'port_area', title: '所属港区', minWidth: '120px', type: JVxeTypes.input },
    ] as JVxeColumn[],
  });

  // form表单 col
  const labelCol = reactive({ span: 4 });
  const wrapperCol = reactive({ span: 20 });
  const rules = reactive({
    num: [{ required: true, message: '必须输入序号' }],
  });

  // 查询url地址
  enum Api {
    getData = '/mock/vxe/getData',
  }

  loadData();

  // 加载数据
  function loadData() {
    // 封装查询条件
    // 调用查询数据接口
    loading.value = true;
    defHttp
      .get({
        url: Api.getData,
        params: {
          pageNo: 1,
          pageSize: 30,
        },
      })
      .then((result) => {
        // 将查询的数据赋值给 dataSource
        dataSource.value = result.records;
        // 重置选择
        selectedRows.value = [];
      })
      .finally(() => {
        // 这里是无论成功或失败都会执行的方法，在这里关闭loading
        loading.value = false;
      });
  }

  // 查询子表数据
  function loadSubData(row) {
    if (row) {
      // 这里一定要做限制，限制不能重复查询，否者会出现死循环
      if (subTable.currentRowId === row.id) {
        return true;
      }
      subTable.currentRowId = row.id;
      subTable.loading = true;
      defHttp
        .get({
          url: Api.getData,
          params: {
            pageNo: 1,
            pageSize: 30,
            parentId: row.id,
          },
        })
        .then((result) => {
          // 将查询的数据赋值给 dataSource
          subTable.dataSource = result.records;
        })
        .finally(() => {
          // 这里是无论成功或失败都会执行的方法，在这里关闭loading
          subTable.loading = false;
        });
      return true;
    } else {
      return false;
    }
  }

  // 详细信息里点了确认按钮
  function handleDetailsConfirm({ row, $table, callback }) {
    console.log('保存的数据：', row);
    // 校验当前行
    $table.validate(row).then((errMap) => {
      // 校验通过
      if (!errMap) {
        // 校验子表，如果需要的话，可以操作下面这个对象：
        callback(true);
        loading.value = true;
        setTimeout(() => {
          loading.value = false;
          createMessage.success('保存成功');
        }, 1000);
      } else {
        callback(false);
        createMessage.warn('校验失败');
      }
    });
  }
</script>

<style scoped></style>
