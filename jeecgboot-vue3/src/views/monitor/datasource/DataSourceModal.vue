<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="title" @ok="handleSubmit" width="40%">
    <BasicForm @register="registerForm">
      <template #pwd="{ model, field }">
        <a-row :gutter="8">
          <a-col :sm="15" :md="16" :lg="17" :xl="19">
            <a-input-password v-model:value="model[field]" placeholder="请输入密码" />
          </a-col>
          <a-col :sm="9" :md="7" :lg="7" :xl="5">
            <a-button type="primary" style="width: 100%" @click="handleTest">测试</a-button>
          </a-col>
        </a-row>
      </template>
    </BasicForm>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formSchema } from './datasource.data';
  import { saveOrUpdateDataSource, getDataSourceById, testConnection } from './datasource.api';
  import { useMessage } from '/@/hooks/web/useMessage';

  const { createMessage } = useMessage();
  // Emits声明
  const emit = defineEmits(['register', 'success']);
  const isUpdate = ref(true);
  //表单配置
  const [registerForm, { getFieldsValue, resetFields, validateFields, setFieldsValue, validate }] = useForm({
    // labelWidth: 150,
    schemas: formSchema,
    showActionButtonGroup: false,
  });
  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    //重置表单
    await resetFields();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;
    if (unref(isUpdate)) {
      //获取详情
      data.record = await getDataSourceById({ id: data.record.id });
      //表单赋值
      await setFieldsValue({
        ...data.record,
      });
    }
  });
  //设置标题
  const title = computed(() => (!unref(isUpdate) ? '新增数据源' : '编辑数据源'));

  async function handleTest() {
    let keys = ['dbType', 'dbDriver', 'dbUrl', 'dbName', 'dbUsername', 'dbPassword'];
    // 获取以上字段的值，并清除校验状态
    let fieldsValues = getFieldsValue(keys);
    let setFields = {};
    keys.forEach((key) => (setFields[key] = { value: fieldsValues[key], errors: null }));
    await validateFields(keys).then((values) => {
      let loading = createMessage.loading('连接中....', 0);
      testConnection(values)
        .then((data) => {
          if (data.success) {
            createMessage.success('连接成功');
          }
        })
        .catch((error) => {})
        .finally(() => loading());
    });
  }

  //表单提交事件
  async function handleSubmit(v) {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      //提交表单
      await saveOrUpdateDataSource(values, isUpdate.value);
      //关闭弹窗
      closeModal();
      //刷新列表
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
