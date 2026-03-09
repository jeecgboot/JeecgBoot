<template>
  <div class="analysis-panel">
    <div class="panel-header">
      <div class="title">
        <span class="icon-dot"></span>
        <span class="main">智能 OCR 解析</span>
        <span class="sub">上传图片，快速识别并结构化输出</span>
      </div>
      <a-button type="primary" size="large" class="parse-btn" :loading="loading" @click="analysisHandleClick">解析</a-button>
    </div>
    <div class="form-body">
      <a-spin tip="解析中，请稍后" :spinning="loading">
        <BasicForm @register="registerForm" />
      </a-spin>
    </div>
  </div>
</template>
<script lang="ts" name="AiOcrAnalysis" setup>
  import { ref, watch, PropType } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { analysisSchemas } from '../AiOcr.data';
  import { Api } from '../AiOcr.api';
  import { defHttp } from '@/utils/http/axios';
  import { useMessage } from '@/hooks/web/useMessage';

  const { createMessage } = useMessage();
  const loading = ref<boolean>(false);
  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    schemas: analysisSchemas,
    showActionButtonGroup: false,
    layout: 'vertical',
    wrapperCol: { span: 24 },
  });

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
              content: values.prompt ? values.prompt: '解析图片中的文字',
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
          //update-begin---author:wangshuai---date:2026-01-14---for: OCR数据返回信息报错，增加try catch---
          let text = res.result.data.replace(/\s+/g, '').replace('```json','').replace('```','');
          let lastText = "";
          try {
            let parse = JSON.parse(text);
            lastText = parse.text;
          } catch (e) {
            lastText = text;
          }
          //update-end---author:wangshuai---date:2026-01-14---for:  OCR数据返回信息报错，增加try catch---
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
  .analysis-panel {
    background: #fff;
    padding: 20px;
    border-radius: 16px;
    border: 1px solid #e6f4ff;
    box-shadow: 0 12px 30px rgba(22, 119, 255, 0.08), 0 6px 18px rgba(0, 0, 0, 0.06);
    height: 100%;
    display: flex;
    flex-direction: column;
  }
  .panel-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 14px;
  }
  .form-body {
    flex: 1;
    overflow: auto;
  }
  .title {
    display: inline-flex;
    align-items: baseline;
    gap: 10px;
  }
  .icon-dot {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: linear-gradient(135deg, #1677ff 0%, #69b1ff 100%);
    box-shadow: 0 0 0 6px rgba(22, 119, 255, 0.08);
  }
  .main {
    font-size: 18px;
    font-weight: 600;
    color: #1f2328;
  }
  .sub {
    font-size: 13px;
    color: #86909c;
  }
  .parse-btn {
    border-radius: 8px;
    box-shadow: 0 6px 16px rgba(22, 119, 255, 0.25);
  }
  :deep(.ant-form-item) {
    margin-bottom: 16px;
  }
  :deep(.ant-form-item-label > label) {
    color: #3a3a3a;
    font-weight: 500;
  }


  :deep(.ant-input-textarea-show-count > .ant-input-textarea) {
    background: #fafafa;
    border-radius: 10px;
    border: 1px solid #e5e6eb;
    transition: all .2s ease;
  }
  :deep(.ant-input:focus),
  :deep(textarea.ant-input:focus),
  :deep(.ant-input-focused) {
    border-color: #1677ff !important;
    box-shadow: 0 0 0 3px rgba(22, 119, 255, 0.15);
    background: #fff;
  }
  :deep(.ant-upload) {
    border-radius: 10px;
  }
</style>
