<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    @ok="handleSubmit"
    :title="title"
    width="900px"
    wrapClassName="notice-cls-modal"
    :maxHeight="800"
    destroyOnClose
  >
    <BasicForm @register="registerForm">
      <template #msgTemplate="{ model, field }">
        <a-select v-model:value="model[field]" placeholder="请选择消息模版" :options="templateOption" @change="handleChange" />
      </template>
    </BasicForm>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formSchema } from './notice.data';
  import { getTempList, saveOrUpdate } from './notice.api';
  // 声明Emits
  const emit = defineEmits(['register', 'success']);
  const isUpdate = ref(true);
  const record = ref<any>({});
  const templateOption = ref([]);
  //表单配置
  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    schemas: formSchema,
    showActionButtonGroup: false,
    labelWidth: 100,
    baseRowStyle: { marginTop: '10px' },
    baseColProps: { xs: 24, sm: 12, md: 12, lg: 12, xl: 12, xxl: 12 },
  });
  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    //加载模版
    await initTemplate();
    //重置表单
    await resetFields();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;
    if (unref(isUpdate)) {
      if (data.record.userIds) {
        data.record.userIds = data.record.userIds.substring(0, data.record.userIds.length - 1);
      }
      //表单赋值
      await setFieldsValue({
        ...data.record,
      });
      record.value = data.record;
    } else {
      //表单赋值
      await setFieldsValue({
        ...data.record,
      });
    }
  });
  //设置标题
  const title = computed(() => (!unref(isUpdate) ? '新增' : '编辑'));
  //表单提交事件
  async function handleSubmit() {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      //提交表单
      // 代码逻辑说明: [issue#429]新增通知公告提交指定用户参数有undefined ---
      if(values.msgType==='ALL'){
        values.userIds = '';
      }else{
        values.userIds += ',';
      }
      if (isUpdate.value && record.value.sendStatus != '2') {
        values.sendStatus = '0';
      }
      await saveOrUpdate(values, isUpdate.value);
      //关闭弹窗
      closeModal();
      //刷新列表
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
  //初始化模板
  async function initTemplate() {
    const res = await getTempList({ templateCategory: 'notice', pageSize: 100 });
    console.log('res', res);
    if (res.records && res.records.length > 0) {
      templateOption.value = res.records.map((item) => {
        return {
          label: item.templateName,
          value: item.templateCode,
          content: item.templateContent,
        };
      });
    }
  }

  /**
   * 模版修改
   * @param val
   */
  function handleChange(val) {
    const content = templateOption.value.find((item: any) => item.value === val)?.content;
    if (content) {
      setFieldsValue({
        msgContent: content,
      });
    }
  }
</script>
<style scoped>
  .notice-cls-modal {
    top: 20px !important;
  }
</style>
