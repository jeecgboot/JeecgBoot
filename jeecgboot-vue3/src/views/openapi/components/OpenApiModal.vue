<template>
  <BasicModal :bodyStyle="{ padding: '20px' }" v-bind="$attrs" @register="registerModal" destroyOnClose :title="title" width="80%" @ok="handleSubmit">
    <a-row :gutter="24">
      <a-col :span="10">
        <BasicForm @register="registerForm" ref="formRef" name="OpenApiForm" />
      </a-col>
      <a-col :span="14">
        <a-row :gutter="24">
          <a-col :span="24" style="margin-top: -0.6em">
            <JVxeTable
              keep-source
              ref="openApiHeader"
              :loading="openApiHeaderTable.loading"
              :columns="openApiHeaderTable.columns"
              :dataSource="openApiHeaderTable.dataSource"
              :height="240"
              :disabled="formDisabled"
              :rowNumber="true"
              :rowSelection="true"
              :toolbar="true"
              size="mini"
            />
          </a-col>
          <a-col :span="24">
            <JVxeTable
              keep-source
              ref="openApiParam"
              :loading="openApiParamTable.loading"
              :columns="openApiParamTable.columns"
              :dataSource="openApiParamTable.dataSource"
              :height="240"
              :disabled="formDisabled"
              :rowNumber="true"
              :rowSelection="true"
              :toolbar="true"
              size="mini"
            />
          </a-col>
        </a-row>
      </a-col>
    </a-row>
  </BasicModal>
</template>

<script lang="ts" setup>
  import { ref, computed, unref, reactive } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { JVxeTable } from '/@/components/jeecg/JVxeTable';
  import { useJvxeMethod } from '/@/hooks/system/useJvxeMethods.ts';
  import { formSchema, openApiHeaderJVxeColumns, openApiParamJVxeColumns } from '../OpenApi.data';
  import { saveOrUpdate, queryOpenApiHeader, queryOpenApiParam, getGenPath } from '../OpenApi.api';
  import { VALIDATE_FAILED } from '/@/utils/common/vxeUtils';
  import { useMessage } from "@/hooks/web/useMessage";

  // Emits声明
  const $message = useMessage();
  const emit = defineEmits(['register', 'success']);
  const isUpdate = ref(true);
  const formDisabled = ref(false);
  const refKeys = ref(['openApiHeader', 'openApiParam']);
  const activeKey = ref('openApiHeader');
  const openApiHeader = ref();
  const openApiParam = ref();
  const tableRefs = { openApiHeader, openApiParam };
  const openApiHeaderTable = reactive({
    loading: false,
    dataSource: [],
    columns: openApiHeaderJVxeColumns,
  });
  const openApiParamTable = reactive({
    loading: false,
    dataSource: [],
    columns: openApiParamJVxeColumns,
  });
  //表单配置
  const [registerForm, { setProps, resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    schemas: formSchema,
    showActionButtonGroup: false,
    baseColProps: { span: 24 },
    wrapperCol: { span: 24 },
  });
  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    //重置表单
    await reset();
    setModalProps({ confirmLoading: false, showCancelBtn: data?.showFooter, showOkBtn: data?.showFooter });
    isUpdate.value = !!data?.isUpdate;
    formDisabled.value = !data?.showFooter;
    if (unref(isUpdate)) {
      //表单赋值
      await setFieldsValue({
        ...data.record,
      });
      // 请求后端接口获取数据
      //  requestSubTableData(queryOpenApiHeader, {id:data?.record?.id}, openApiHeaderTable)
      //  requestSubTableData(queryOpenApiParam, {id:data?.record?.id}, openApiParamTable)
      openApiHeaderTable.dataSource = !!data.record.headersJson?JSON.parse(data.record.headersJson):[];
      openApiParamTable.dataSource = !!data.record.paramsJson?JSON.parse(data.record.paramsJson):[];
    } else {
      //  /openapi/genpath
      const requestUrlObj = await getGenPath({});
      await setFieldsValue({
        requestUrl: requestUrlObj.result

      });
    }
    // 隐藏底部时禁用整个表单
    setProps({ disabled: !data?.showFooter });
  });
  //方法配置
  const [handleChangeTabs, handleSubmit, requestSubTableData, formRef] = useJvxeMethod(
    requestAddOrEdit,
    classifyIntoFormData,
    tableRefs,
    activeKey,
    refKeys
  );

  //设置标题
  const title = computed(() => (!unref(isUpdate) ? '新增' : !unref(formDisabled) ? '编辑' : '详情'));

  async function reset() {
    await resetFields();
    activeKey.value = 'openApiHeader';
    openApiHeaderTable.dataSource = [];
    openApiParamTable.dataSource = [];
  }
  function classifyIntoFormData(allValues) {
    let main = Object.assign({}, allValues.formValue);
    return {
      ...main, // 展开
      headersJson: allValues.tablesValue[0].tableData,
      paramsJson: allValues.tablesValue[1].tableData,
    };
  }
  //表单提交事件
  async function requestAddOrEdit(values) {
    let headersJson = !!values.headersJson?JSON.stringify(values.headersJson):null;
    let paramsJson = !!values.headersJson?JSON.stringify(values.paramsJson):null;
    try {
      if (!!values.body){
        try {
          if (typeof JSON.parse(values.body)!='object'){
            $message.createMessage.error("JSON格式化错误,请检查输入数据");
            return;
          }
        } catch (e) {
          $message.createMessage.error("JSON格式化错误,请检查输入数据");
          return;
        }
      }
      setModalProps({ confirmLoading: true });
      values.headersJson = headersJson
      values.paramsJson = paramsJson
      //提交表单
      await saveOrUpdate(values, isUpdate.value);
      //关闭弹窗
      closeModal();
      //刷新列表
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>

<style lang="less" scoped>
  /** 时间和数字输入框样式 */
  :deep(.ant-input-number) {
    width: 100%;
  }

  :deep(.ant-calendar-picker) {
    width: 100%;
  }
</style>
