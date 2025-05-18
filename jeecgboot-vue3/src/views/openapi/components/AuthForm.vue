<template>
  <a-spin :spinning="confirmLoading">
    <JFormContainer :disabled="disabled">
      <template #detail>
        <BasicTable @register="registerTable" :rowSelection="rowSelection"> </BasicTable>
      </template>
    </JFormContainer>
  </a-spin>
</template>

<script lang="ts" setup>
  import { ref, reactive, defineExpose, nextTick, defineProps, computed } from 'vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { permissionAddFunction, getPermissionList, getApiList } from '../OpenApiAuth.api';
  import { Form } from 'ant-design-vue';
  import JFormContainer from '/@/components/Form/src/container/JFormContainer.vue';
  import { BasicTable } from '@/components/Table';
  import { useListPage } from '@/hooks/system/useListPage';
  import { columns } from '@/views/openapi/OpenApi.data';

  const queryParam = reactive<any>({});
  //注册table数据
  const { tableContext } = useListPage({
    tableProps: {
      title: '授权',
      api: getApiList,
      columns,
      canResize: false,
      useSearchForm: false,

      beforeFetch: async (params) => {
        return Object.assign(params, queryParam);
      },
    },
  });
  const [registerTable, { reload, collapseAll, updateTableDataRecord, findTableDataRecord, getDataSource,setSelectedRowKeys }, { rowSelection, selectedRowKeys,selectedRows }] =
    tableContext;

  const props = defineProps({
    formDisabled: { type: Boolean, default: false },
    formData: { type: Object, default: () => ({}) },
    formBpm: { type: Boolean, default: true },
  });
  const formRef = ref();
  const useForm = Form.useForm;
  const emit = defineEmits(['register', 'ok']);
  const formData = reactive<Record<string, any>>({
    apiAuthId: '',
    apiIdList: [],
  });
  const { createMessage } = useMessage();
  const labelCol = ref<any>({ xs: { span: 24 }, sm: { span: 5 } });
  const wrapperCol = ref<any>({ xs: { span: 24 }, sm: { span: 16 } });
  const confirmLoading = ref<boolean>(false);
  //表单验证
  const validatorRules = reactive({});
  const { resetFields, validate, validateInfos } = useForm(formData, validatorRules, { immediate: false });

  // 表单禁用
  const disabled = computed(() => {
    if (props.formBpm === true) {
      if (props.formData.disabled === false) {
        return false;
      } else {
        return true;
      }
    }
    return props.formDisabled;
  });

  /**
   * 新增
   */
  function add() {
    edit({});
  }

  /**
   * 编辑
   */
  function edit(record) {
    nextTick(() => {
      resetFields();
      //赋值
      formData.apiAuthId = record.id;
      // 获取当前已授权的项目
      getPermissionList({ apiAuthId: record.id }).then((res) => {
        if (res.length > 0) {
          let list =  res;
          let ids = [];
          list.forEach((item) => {
            if(item.ifCheckBox == "1"){
              selectedRowKeys.value.push(item.id)
              selectedRows.value.push(item)
              setSelectedRowKeys(selectedRowKeys.value);
            }
            ids.push(item.apiId);
          });
          // selectedRowKeys.value = ids;
          formData.apiIdList = ids;
        }
      });
    });
  }

  /**
   * 提交数据
   */
  async function submitForm() {
    // if(selectedRowKeys.value.length === 0)
    //   return emit('ok');
    try {
      // 触发表单验证
      await validate();
    } catch ({ errorFields }) {
      if (errorFields) {
        const firstField = errorFields[0];
        if (firstField) {
          formRef.value.scrollToField(firstField.name, { behavior: 'smooth', block: 'center' });
        }
      }
      return Promise.reject(errorFields);
    }
    confirmLoading.value = true;
    //时间格式化
    let model = formData;
    // model.apiIdList = selectedRowKeys.value;
    let apiId = ""
    selectedRowKeys.value.forEach((item) => {
      apiId += item +",";
    })
    model.apiId = apiId;
    delete model.apiIdList
    await permissionAddFunction(model)
      .then((res) => {
        if (res.success) {
          createMessage.success(res.message);
          emit('ok');
          cleanData()
        } else {
          createMessage.warning(res.message);
        }
      })
      .finally(() => {
        confirmLoading.value = false;
      });
  }
  const cleanData = () => {
    selectedRows.value = []
    selectedRowKeys.value = []
  }

  defineExpose({
    add,
    edit,
    submitForm,
    cleanData
  });
</script>

<style lang="less" scoped>
  .antd-modal-form {
    padding: 14px;
  }
</style>
