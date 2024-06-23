<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" width="70%" @fullScreen="handleFullScreen">
    <a-form ref="formRef" :model="orderMainModel" :label-col="labelCol" :wrapper-col="wrapperCol" :rules="validatorRules">
      <!-- update-begin--author:liaozhiyang---date:20230803---for：【QQYUN-5866】鼠标放上去有左右滚动条 -->
      <div style="overflow-x: hidden">
        <a-row class="form-row" :gutter="16">
          <a-col :lg="8">
            <a-form-item label="订单号" name="orderCode">
              <a-input v-model:value="orderMainModel.orderCode" placeholder="请输入订单号" />
            </a-form-item>
          </a-col>
          <a-col :lg="8">
            <a-form-item label="订单类型" name="ctype">
              <a-select placeholder="请选择订单类型" v-model:value="orderMainModel.ctype">
                <a-select-option value="1">国内订单</a-select-option>
                <a-select-option value="2">国际订单</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="8">
            <a-form-item label="订单日期" name="orderDate">
              <a-date-picker showTime valueFormat="YYYY-MM-DD HH:mm:ss" v-model:value="orderMainModel.orderDate" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="16">
          <a-col :lg="8">
            <a-form-item label="订单金额" name="orderMoney">
              <a-input v-model:value="orderMainModel.orderMoney" placeholder="请输入订单金额" />
            </a-form-item>
          </a-col>
          <a-col :lg="8">
            <a-form-item label="订单备注" name="content">
              <a-input v-model:value="orderMainModel.content" placeholder="请输入订单备注" />
            </a-form-item>
          </a-col>
        </a-row>
      </div>
      <!-- update-end--author:liaozhiyang---date:20230803---for：【QQYUN-5866】鼠标放上去有左右滚动条 -->
      <!-- 子表单区域 -->
      <a-tabs v-model:activeKey="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="客户信息" key="tableRef1">
          <JVxeTable
            ref="tableRef1"
            stripe
            toolbar
            rowNumber
            rowSelection
            resizable
            keepSource
            :height="tableH"
            :checkbox-config="{ range: true }"
            :loading="table1.loading"
            :columns="table1.columns"
            :dataSource="table1.dataSource"
          ></JVxeTable>
        </a-tab-pane>

        <a-tab-pane tab="机票信息" key="tableRef2" forceRender>
          <JVxeTable
            ref="tableRef2"
            stripe
            toolbar
            rowNumber
            rowSelection
            resizable
            keepSource
            :height="tableH"
            :checkbox-config="{ range: true }"
            :loading="table2.loading"
            :columns="table2.columns"
            :dataSource="table2.dataSource"
          ></JVxeTable>
        </a-tab-pane>
      </a-tabs>
    </a-form>
  </BasicModal>
</template>
<script lang="ts">
  import { defineComponent, ref, reactive, computed, unref } from 'vue';
  import { BasicModal, useModalInner } from '/src/components/Modal';
  import { JVxeTable } from '/src/components/jeecg/JVxeTable';
  import { columns, columns1 } from './jvxetable.data';
  import { orderCustomerList, orderTicketList, saveOrUpdate } from './jvxetable.api';
  import { useJvxeMethod } from '/@/hooks/system/useJvxeMethods.ts';
  export default defineComponent({
    name: 'JVexTableModal',
    components: { BasicModal, JVxeTable },
    emits: ['success', 'register'],
    setup(props, { emit }) {
      const tableH = ref(300);
      const isUpdate = ref(true);
      const tableRef1 = ref();
      const tableRef2 = ref();
      const refKeys = ref(['tableRef1', 'tableRef2']);
      const activeKey = ref('tableRef1');
      const tableRefs = { tableRef1, tableRef2 };
      const labelCol = reactive({
        xs: { span: 24 },
        sm: { span: 5 },
      });
      const wrapperCol = reactive({
        xs: { span: 24 },
        sm: { span: 16 },
      });
      // 客户信息
      const table1 = reactive({
        loading: false,
        dataSource: [],
        columns,
      });
      // 机票信息
      const table2 = reactive({
        loading: false,
        dataSource: [],
        columns: columns1,
      });
      const orderMainModel = reactive({
        id: null,
        orderCode: '',
        orderMoney: '',
        ctype: '',
        content: '',
        jeecgOrderCustomerList: [],
        jeecgOrderTicketList: [],
      });
      const [handleChangeTabs, handleSubmit, requestSubTableData, formRef] = useJvxeMethod(
        requestAddOrEdit,
        classifyIntoFormData,
        tableRefs,
        activeKey,
        refKeys
      );
      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ confirmLoading: false });
        reset();
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
          Object.assign(orderMainModel, data.record);
          //加载子表数据
          let params = { id: orderMainModel.id };
          requestSubTableData(orderCustomerList, params, table1);
          requestSubTableData(orderTicketList, params, table2);
        }
      });

      const validatorRules = { orderCode: [{ required: true, message: '订单号不能为空', trigger: 'blur' }] };
      const getTitle = computed(() => (!unref(isUpdate) ? '新增' : '编辑'));

      function classifyIntoFormData(allValues) {
        let orderMain = Object.assign(orderMainModel, allValues.formValue);
        return {
          ...orderMain, // 展开
          jeecgOrderCustomerList: allValues.tablesValue[0].tableData,
          jeecgOrderTicketList: allValues.tablesValue[1].tableData,
        };
      }
      function reset() {
        orderMainModel.id = null;
        orderMainModel.orderCode = '';
        orderMainModel.orderMoney = '';
        orderMainModel.orderDate = null;
        orderMainModel.ctype = '';
        orderMainModel.content = '';
        orderMainModel.jeecgOrderCustomerList = [];
        orderMainModel.jeecgOrderTicketList = [];
        table1.dataSource = [];
        table2.dataSource = [];
      }
      async function requestAddOrEdit(values) {
        setModalProps({ confirmLoading: true });
        //提交表单
        await saveOrUpdate(values, unref(isUpdate));
        //关闭弹窗
        closeModal();
        //刷新列表
        emit('success');
      }
      // update-begin--author:liaozhiyang---date:20230804---for：【QQYUN-5866】放大行数自适应
      const handleFullScreen = (val) => {
        tableH.value=val ? document.documentElement.clientHeight - 387 :  300;
      };
      // update-end--author:liaozhiyang---date:20230804---for：【QQYUN-5866】放大行数自适应
      return {
        formRef,
        activeKey,
        table1,
        table2,
        tableRef1,
        tableRef2,
        getTitle,
        labelCol,
        wrapperCol,
        validatorRules,
        orderMainModel,
        registerModal,
        handleChangeTabs,
        handleSubmit,
        handleFullScreen,
        tableH,
      };
    },
  });
</script>
<style scoped></style>
