<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="title" @ok="handleSubmit" :footer="null">
    <a-spin tip="解析中，请稍后" :spinning="loading">
      <BasicForm @register="registerForm" />
      <div style="width: 100%;text-align: center; margin-bottom: 10px">
        <a-button type="primary" @click="analysisHandleClick">解析 </a-button>
      </div>
    </a-spin>
  </BasicModal>
  
</template>
<script lang="ts" name="AiOcrModal" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { analysisSchemas } from '../AiOcr.data';
  import { addOcr, Api, editOcr } from '../AiOcr.api';
  import { defHttp } from '@/utils/http/axios';
  import { useMessage } from '@/hooks/web/useMessage';

  const { createMessage } = useMessage();
  //标题
  const title = ref<string>('解析');
  //是否更新
  const isUpdate = ref<boolean>();
  //加载
  const loading = ref<boolean>(false);
  // 声明Emits
  const emit = defineEmits(['success', 'register']);
  //表单配置
  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    schemas: analysisSchemas,
    showActionButtonGroup: false,
    layout: 'vertical',
    wrapperCol: { span: 24 },
  });

  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: true, bodyStyle:{ padding:'24px'} });
    isUpdate.value = !!data?.isUpdate;
    //重置表单
    await resetFields();
    setModalProps({ confirmLoading: false });
    if (unref(isUpdate)) {
      //表单赋值
      await setFieldsValue({ ...data.record });
    }
  });

  //表单提交事件
  async function handleSubmit() {
    try {
      const values = await validate();
      if (unref(isUpdate)) {
        await editOcr(values);
      } else {
        await addOcr(values);
      }
      setModalProps({ confirmLoading: true });
      //关闭弹窗
      closeModal();
      //刷新列表
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  /**
   * 解析
   */
  async function analysisHandleClick() {
    const values = await validate();
    loading.value = true;
    await defHttp
      .post(
        {
          url: Api.flowRun,
          params: {
            flowId: '1904779811574784002',
            inputParams: {
              content: values.prompt,
              images: values.url,
            },
            responseMode: 'blocking',
          },
          timeout: 5 * 60 * 1000,
        },
        {
          isTransformResponse: false,
        }
      )
      .then((res) => {
        if (res.success) {
          let replace = res.result.data.replace(/\s+/g, '');
          let parse = JSON.parse(replace);
          let text = parse.text;
          let lastText = "";
          for (const textKey in text) {
            lastText = lastText + textKey +":"+ text[textKey] + "\n";
          }
          setFieldsValue({ analysisResult: lastText });
        } else {
          createMessage.warning(res.message);
        }
        loading.value = false;
      }).catch((res)=>{
          createMessage.warning(res.message);
          loading.value = false;
      });
  }
</script>

<style lang="less" scoped>
</style>
