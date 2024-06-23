<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="title" @ok="handleSubmit" width="700px">
    <BasicForm @register="registerForm" />
    <!--TODO 子表Tab数据-->
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { defHttp } from '/@/utils/http/axios';
  // Emits声明
  const emit = defineEmits(['register', 'success']);
  const isUpdate = ref(true);
  //表单配置
  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 150,
    schemas: [
      {
        field: 'orderCode',
        label: '订单号',
        component: 'Input',
        required: true,
      },
      {
        field: 'ctype',
        label: '订单类型',
        component: 'Select',
        componentProps: {
          options: [
            { label: '国内订单', value: '1' },
            { label: '国际订单', value: '2' },
          ],
        },
      },
      {
        field: 'orderDate',
        label: '订单日期',
        component: 'DatePicker',
        componentProps: {
          valueFormat: 'YYYY-MM-DD hh:mm:ss',
        },
      },
      {
        field: 'orderMoney',
        label: '订单金额',
        component: 'InputNumber',
      },
      {
        field: 'content',
        label: '订单备注',
        component: 'Input',
      },
      {
        field: 'id',
        label: 'id',
        component: 'Input',
        show: false,
      },
    ],
    showActionButtonGroup: false,
  });
  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    //重置表单
    await resetFields();
    setModalProps({ confirmLoading: false });
    isUpdate.value = !!data?.isUpdate;
    if (unref(isUpdate)) {
      //表单赋值
      await setFieldsValue({
        ...data.record,
      });
    }
  });
  //设置标题
  const title = computed(() => (!unref(isUpdate) ? '新增订单' : '编辑订单'));
  //表单提交事件
  async function handleSubmit(v) {
    try {
      let values = await validate();
      setModalProps({ confirmLoading: true });
      //提交表单
      let url = unref(isUpdate) ? '/test/order/edit' : '/test/order/add';
      defHttp.post({ url: url, params: values });
      //关闭弹窗
      closeModal();
      //刷新列表
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>
