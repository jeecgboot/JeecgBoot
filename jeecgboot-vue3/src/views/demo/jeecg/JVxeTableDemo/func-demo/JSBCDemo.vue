<template>
  <a-card title="即时保存示例" :bordered="false">
    <!--
      【即时保存大体思路】：
      1. JVxeTable 上必须加 keep-source 属性
      2. 监听 edit-closed事件，这个事件是在编辑完成后触发
      3. 在这个事件里面判断数据是否更改，如果更改了就调用接口进行保存操作
    -->
    <JVxeTable
      toolbar
      :toolbarConfig="toolbarConfig"
      rowNumber
      rowSelection
      keepSource
      asyncRemove
      :height="340"
      :loading="loading"
      :columns="columns"
      :dataSource="dataSource"
      :pagination="pagination"
      @save="handleTableSave"
      @removed="handleTableRemove"
      @edit-closed="handleEditClosed"
      @pageChange="handlePageChange"
      @selectRowChange="handleSelectRowChange"
    />
  </a-card>
</template>

<script lang="ts" setup>
  // 即时保存示例
  import { reactive, ref } from 'vue';
  import { defHttp } from '/@/utils/http/axios';
  import { JVxeColumn, JVxeTypes } from '/@/components/jeecg/JVxeTable/types';
  import { useMessage } from '/@/hooks/web/useMessage';

  const { createMessage } = useMessage();
  // 工具栏的按钮配置
  const toolbarConfig = reactive({
    // add 新增按钮；remove 删除按钮；clearSelection 清空选择按钮
    btn: ['add', 'save', 'remove', 'clearSelection'],
  });
  // 是否正在加载
  const loading = ref(false);
  // 分页器参数
  const pagination = reactive({
    // 当前页码
    current: 1,
    // 每页的条数
    pageSize: 200,
    // 可切换的条数
    pageSizeOptions: ['10', '20', '30', '100', '200'],
    // 数据总数（目前并不知道真实的总数，所以先填写0，在后台查出来后再赋值）
    total: 0,
  });
  // 选择的行
  const selectedRows = ref<Recordable[]>([]);
  // 数据源，控制表格的数据
  const dataSource = ref<Recordable[]>([]);
  // 列配置，控制表格显示的列
  const columns = ref<JVxeColumn[]>([
    { key: 'num', title: '序号', width: 80, type: JVxeTypes.normal },
    {
      // 字段key，跟后台数据的字段名匹配
      key: 'ship_name',
      // 列的标题
      title: '船名',
      // 列的宽度
      width: 180,
      // 如果加上了该属性，就代表当前单元格是可编辑的，type就是表单的类型，input就是简单的输入框
      type: JVxeTypes.input,
    },
    { key: 'call', title: '呼叫', width: 80, type: JVxeTypes.input },
    { key: 'len', title: '长', width: 80, type: JVxeTypes.input },
    { key: 'ton', title: '吨', width: 120, defaultValue: 233, type: JVxeTypes.input },
    { key: 'payer', title: '付款方', width: 120, defaultValue: '张三', type: JVxeTypes.input },
    { key: 'count', title: '数', width: 40, type: JVxeTypes.normal },
    {
      key: 'company',
      title: '公司',
      // 最小宽度，与宽度不同的是，这个不是固定的宽度，如果表格有多余的空间，会平均分配给设置了 minWidth 的列
      // 如果要做占满表格的列可以这么写
      minWidth: 180,
      type: JVxeTypes.input,
    },
    { key: 'trend', title: '动向', width: 120, type: JVxeTypes.input },
  ]);

  // 查询url地址
  enum Api {
    getData = '/mock/vxe/getData',
    // 模拟保存单行数据（即时保存）
    saveRow = '/mock/vxe/immediateSaveRow',
    // 模拟保存整个表格的数据
    saveAll = '/mock/vxe/immediateSaveAll',
  }

  loadData();

  // 加载数据
  async function loadData() {
    loading.value = true;
    // 调用查询数据接口
    await defHttp
      .get({
        // 请求地址
        url: Api.getData,
        // 封装查询条件
        params: {
          pageNo: pagination.current,
          pageSize: pagination.pageSize,
        },
      })
      .then((result) => {
        // 后台查询回来的 total，数据总数量
        pagination.total = result.total;
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

  // 【整体保存】点击保存按钮时触发的事件
  function handleTableSave({ $table, target }) {
    // 校验整个表格
    $table.validate().then((errMap) => {
      // 校验通过
      if (!errMap) {
        // 获取所有数据
        let tableData = target.getTableData();
        console.log('当前保存的数据是：', tableData);
        // 获取新增的数据
        let newData = target.getNewData();
        console.log('-- 新增的数据：', newData);
        // 获取删除的数据
        let deleteData = target.getDeleteData();
        console.log('-- 删除的数据：', deleteData);
        // 【模拟保存】
        loading.value = true;
        defHttp
          .post({
            url: Api.saveAll,
            params: tableData,
          })
          .then(() => {
            createMessage.success(`保存成功！`);
          })
          .finally(() => {
            loading.value = false;
          });
      }
    });
  }

  // 触发单元格删除事件
  function handleTableRemove(event) {
    // 把 event.deleteRows 传给后台进行删除（注意：这里不会传递前端逻辑新增的数据，因为不需要请求后台删除）
    console.log('待删除的数据: ', event.deleteRows);
    // 也可以只传ID，因为可以根据ID删除
    let deleteIds = event.deleteRows.map((row) => row.id);
    console.log('待删除的数据ids: ', deleteIds);

    // 模拟请求后台删除
    loading.value = true;
    window.setTimeout(() => {
      loading.value = false;
      createMessage.success('删除成功');
      // 假设后台返回删除成功，必须要调用 confirmRemove() 方法，才会真正在表格里移除（会同时删除选中的逻辑新增的数据）
      event.confirmRemove();
    }, 1000);
  }

  // 单元格编辑完成之后触发的事件
  function handleEditClosed(event) {
    let { $table, row, column } = event;
    let field = column.property;
    // 判断单元格值是否被修改
    if ($table.isUpdateByRow(row, field)) {
      // 校验当前行
      $table.validate(row).then((errMap) => {
        // 校验通过
        if (!errMap) {
          // 【模拟保存】
          let hideLoading = createMessage.loading(`正在保存"${column.title}"`, 0);
          console.log('即时保存数据：', row);
          defHttp
            .put({
              url: Api.saveRow,
              params: row,
            })
            .then((res) => {
              createMessage.success(`"${column.title}"保存成功！`);
              // 局部更新单元格为已保存状态
              $table.reloadRow(row, null, field);
            })
            .finally(() => {
              hideLoading();
            });
        }
      });
    }
  }

  // 当分页参数变化时触发的事件
  function handlePageChange(event) {
    // 重新赋值
    pagination.current = event.current;
    pagination.pageSize = event.pageSize;
    // 查询数据
    loadData();
  }

  // 当选择的行变化时触发的事件
  function handleSelectRowChange(event) {
    selectedRows.value = event.selectedRows;
  }
</script>

<style scoped></style>
