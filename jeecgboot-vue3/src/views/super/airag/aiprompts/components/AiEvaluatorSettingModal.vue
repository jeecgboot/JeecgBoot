<template>
  <div class="p-2">
    <BasicModal
      wrapClassName="ai-prompt-edit-modal"
      destroyOnClose
      @register="registerModal"
      :canFullscreen="false"
      defaultFullscreen
      width="800px"
      :footer="null"
      @visible-change="visibleChange"
      @cancel="handleCancel"
    >
      <template #title>
        <div style="display: flex; width: 100%; justify-content: space-between; align-items: center">
          <div style="display: flex">
            <img :src="getImage()" class="header-img" />
            <div class="header-name">{{ formState.name }}</div>
            <a-tooltip title="提示词评估器">
              <Icon icon="ant-design:edit-outlined" style="margin-left: 4px; cursor: pointer" color="#354052" size="20" @click="handleEdit"></Icon>
            </a-tooltip>
          </div>
<!--          <div>-->
<!--            应用编排-->
<!--            <a-tooltip title="AI应用文档">-->
<!--              <a style="color: unset" href="https://help.jeecg.com/aigc/guide/app" target="_blank">-->
<!--                <Icon style="position: relative; left: 2px; top: 1px" icon="ant-design:question-circle-outlined"></Icon>-->
<!--              </a>-->
<!--            </a-tooltip>-->
<!--          </div>-->
          <div style="display: flex">
            <a-button @click="handleOk" style="margin-right: 30px" type="primary">保存</a-button>
          </div>
        </div>
      </template>
      <div style="height: 100%; width: 100%">
        <a-row :span="24">
          <a-col :span="showTest?12:24">
            <div class="orchestration">提示词评估器</div>
          </a-col>
          <a-col :span="12" v-if="showTest">
            <div class="view">构造测试数据</div>
          </a-col>
        </a-row>
        <a-row :span="24">
          <a-col :span="showTest?12:24" class="setting-left">
            <a-form class="antd-modal-form" ref="formRef" :model="formState" :rules="validatorRules">
              <a-row>
                <a-col :span="24">
                  <div class="prologue-chunk">
                    <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.modelId">
                      <template #label>
                        <div style="display: flex; justify-content: space-between; width: 100%; margin-right: 2px">
                          <div class="item-title">AI模型</div>
                          <div @click="handleParamSettingClick('model')" class="knowledge-txt">
                            <Icon icon="ant-design:setting-outlined" size="13" style="margin-right: 2px"></Icon>参数配置
                          </div>
                        </div>
                      </template>
                      <JDictSelectTag
                        v-model:value="formState.modelId"
                        placeholder="请选择AI模型"
                        dict-code="airag_model where model_type = 'LLM' and activate_flag = 1,name,id"
                        style="width: 100%"
                        @change="handleModelIdChange"
                      ></JDictSelectTag>
                    </a-form-item>
                  </div>
                </a-col>
                <!-- 提示词 -->
                <a-col :span="24" class="mt-10">
                  <div class="prompt-back">
                    <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.dataValue" style="margin-bottom: 0">
                      <template #label>
                        <div class="prompt-title-padding item-title space-between">
                          <span>评估器</span>
                          <a-button size="middle" ghost>
                            <span style="align-items: center; display: flex" @click="generatedPrompt">
                              <svg width="1em" height="1em" viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                <path
                                  d="M18.9839 1.85931C19.1612 1.38023 19.8388 1.38023 20.0161 1.85931L20.5021 3.17278C20.5578 3.3234 20.6766 3.44216 20.8272 3.49789L22.1407 3.98392C22.6198 4.1612 22.6198 4.8388 22.1407 5.01608L20.8272 5.50211C20.6766 5.55784 20.5578 5.6766 20.5021 5.82722L20.0161 7.14069C19.8388 7.61977 19.1612 7.61977 18.9839 7.14069L18.4979 5.82722C18.4422 5.6766 18.3234 5.55784 18.1728 5.50211L16.8593 5.01608C16.3802 4.8388 16.3802 4.1612 16.8593 3.98392L18.1728 3.49789C18.3234 3.44216 18.4422 3.3234 18.4979 3.17278L18.9839 1.85931zM13.5482 4.07793C13.0164 2.64069 10.9836 2.64069 10.4518 4.07793L8.99368 8.01834C8.82648 8.47021 8.47021 8.82648 8.01834 8.99368L4.07793 10.4518C2.64069 10.9836 2.64069 13.0164 4.07793 13.5482L8.01834 15.0063C8.47021 15.1735 8.82648 15.5298 8.99368 15.9817L10.4518 19.9221C10.9836 21.3593 13.0164 21.3593 13.5482 19.9221L15.0063 15.9817C15.1735 15.5298 15.5298 15.1735 15.9817 15.0063L19.9221 13.5482C21.3593 13.0164 21.3593 10.9836 19.9221 10.4518L15.9817 8.99368C15.5298 8.82648 15.1735 8.47021 15.0063 8.01834L13.5482 4.07793zM5.01608 16.8593C4.8388 16.3802 4.1612 16.3802 3.98392 16.8593L3.49789 18.1728C3.44216 18.3234 3.3234 18.4422 3.17278 18.4979L1.85931 18.9839C1.38023 19.1612 1.38023 19.8388 1.85931 20.0161L3.17278 20.5021C3.3234 20.5578 3.44216 20.6766 3.49789 20.8272L3.98392 22.1407C4.1612 22.6198 4.8388 22.6198 5.01608 22.1407L5.50211 20.8272C5.55784 20.6766 5.6766 20.5578 5.82722 20.5021L7.14069 20.0161C7.61977 19.8388 7.61977 19.1612 7.14069 18.9839L5.82722 18.4979C5.6766 18.4422 5.55784 18.3234 5.50211 18.1728L5.01608 16.8593z"
                                />
                              </svg>
                              <span style="margin-left: 4px">生成</span>
                            </span>
                          </a-button>
                        </div>
                      </template>
                      <a-textarea :rows="16" v-model:value="formState.dataValue" placeholder="请输入评估提示" />
                      <!-- 评估器内容格式 -->
                      <div class="variable-container">
                        <div class="variable-container-header">
                          <Icon icon="ant-design:file-text-outlined" class="output-format-icon" />
                          <span class="variable-format-title">评估器内容变量要求（点击变量插入到评估器内容）</span>
                        </div>
                        <div class="variable-container-content">
                          <div class="variable-tag-wrapper">
                            <a-tooltip title="评估的输入内容变量（必填）">
                              <a-tag color="blue" class="variable-tag required-tag" @click="handleTagClick('input')">input</a-tag>
                            </a-tooltip>
                            <a-tooltip title="评估的输出内容变量（必填）">
                              <a-tag color="blue" class="variable-tag required-tag" @click="handleTagClick('output')">output</a-tag>
                            </a-tooltip>
                            <a-tooltip title="评估的参考内容变量">
                              <a-tag color="default" class="variable-tag optional-tag" @click="handleTagClick('reference')">reference</a-tag>
                            </a-tooltip>
                          </div>
                        </div>
                      </div>
                      <!-- 输出格式 -->
                      <div class="output-format-card">
                        <div class="output-format-header">
                          <Icon icon="ant-design:file-text-outlined" class="output-format-icon" />
                          <span class="output-format-title">输出格式要求</span>
                        </div>
                        <div class="output-format-content">
                          <div class="output-item">
<!--                            <div class="output-item-label">-->
<!--                              <span class="output-item-title">得分：</span>-->
<!--                            </div>-->
                            <div class="output-item-desc">
                              得分：最终的得分，必须输出，必须输出一个数字，表示满足Prompt中评分标准的程度。得分范围从
                              <span class="score-range">0.0</span> 到 <span class="score-range">1.0</span>，<span class="score-range">1.0</span>
                              表示完全满足评分标准，<span class="score-range">0.0</span> 表示完全不满足评分标准。
                            </div>
                          </div>
                          <div class="output-item">
                            <div class="output-item-label">
<!--                              <span class="output-item-bullet">•</span>-->
<!--                              <span class="output-item-title">原因：</span>-->
                            </div>
                            <div class="output-item-desc">
                              原因：对得分的可读解释。最后，必须用一句话结束理由，该句话为：因此，应该给出的分数是你的评分。
                            </div>
                          </div>
                        </div>
                      </div>
                    </a-form-item>
                  </div>
                </a-col>
              </a-row>
            </a-form>
            <a-button v-if="showTest" class="mt-10 ml" style="float: right" @click="showTest = false">取消</a-button>
<!--            <a-button class="mt-10" style="float: right" @click="showTest = true" type="primary">调试</a-button>-->
          </a-col>
          <a-col :span="12" class="setting-right" v-if="showTest">
            <EvaluatorDebug ref="debugRef" :content="formState.dataValue" @run="debugRun"></EvaluatorDebug>
          </a-col>
        </a-row>
      </div>
    </BasicModal>

    <!-- Ai配置弹窗   -->
    <AiAppParamsSettingModal @register="registerParamsSettingModal" @ok="handleParamsSettingOk"></AiAppParamsSettingModal>
    <!-- Ai生成器   -->
    <AiAppGeneratedPromptModal @register="registerAiAppPromptModal" @ok="handleAiAppPromptOk"></AiAppGeneratedPromptModal>
    <!-- Ai评估器弹窗   -->
    <AiragExtDataModal @register="registerEvaluatorModal" @success="handleSuccess"></AiragExtDataModal>
  </div>
</template>

<script lang="ts" setup>
  import { ref, reactive } from 'vue';
  import BasicModal from '@/components/Modal/src/BasicModal.vue';
  import { useModal, useModalInner } from '@/components/Modal';
  import {Form, message} from 'ant-design-vue';
  import { defHttp } from '@/utils/http/axios';
  import JDictSelectTag from '@/components/Form/src/jeecg/components/JDictSelectTag.vue';
  import AiAppParamsSettingModal from '@/views/super/airag/aiapp/components/AiAppParamsSettingModal.vue';
  import AiAppGeneratedPromptModal from '@/views/super/airag/aiapp/components/AiAppGeneratedPromptModal.vue';
  import AiragExtDataModal from './AiragExtDataModal.vue';
  import EvaluatorDebug from './EvaluatorDebug.vue';
  import defaultImg from '@/views/super/airag/aiapp/img/ailogo.png';
  import { getFileAccessHttpUrl, randomString } from '@/utils/common/compUtils';
  import { cloneDeep } from 'lodash-es';
  import {debugEvaluator, saveOrUpdate} from '@/views/super/airag/aiprompts/AiragExtData.api';

  //保存或修改
  const isUpdate = ref<boolean>(false);
  //uuid
  const uuid = ref(randomString(16));
  //showTest 显示调试器
  const showTest = ref(true);
  //debugRef 调试器引用
  const debugRef = ref(null);
  //form表单数据
  const formState = reactive<any>({
    id: '',
    name: '',
    dataValue: '',
    descr: '',
    modelId: '',
    metadata: '',
  });
  //表单验证
  const validatorRules = ref<any>({
    dataValue: [{ required: true, message: '请输入提示词!' }],
    modelId: [{ required: true, message: '请选择AI模型!' }],
  });
  //注册form
  const useForm = Form.useForm;
  const { resetFields, validate, validateInfos } = useForm(formState, validatorRules, { immediate: false });
  const labelCol = ref<any>({ span: 24 });
  const wrapperCol = ref<any>({ span: 24 });
  //参数配置
  const metadata = ref<any>({});
  // emit
  const emit = defineEmits(['success', 'register']);
  //注册modal
  const [registerModal, { closeModal, setModalProps }] = useModalInner(async (data) => {
    isUpdate.value = !!data?.isUpdate;
    clearFormState();
    resetFields();
    if (isUpdate.value) {
      setFormState(data.record);
    }
    setModalProps({ bodyStyle: { padding: '10px' } });
  });

  //注册modal
  const [registerParamsSettingModal, { openModal: paramsSettingOpen }] = useModal();
  const [registerAiAppPromptModal, { openModal: aiAppPromptModalOpen }] = useModal();
  const [registerEvaluatorModal, { openModal: evaluatorModalOpen }] = useModal();

  //编辑
  function handleEdit() {
    evaluatorModalOpen(true, { isUpdate: true, showFooter: true, record: {
        id: formState.id,
        name: formState.name,
        descr: formState.descr,
      } });
  }
  /**
   * 保存
   */
  async function handleOk() {
    try {
      let values = await validate();
      metadata.value.modelId = values.modelId;
      values.metadata = JSON.stringify(cloneDeep(metadata.value));
      values = Object.assign(formState, values);
      //提交表单
      await saveOrUpdate(values, isUpdate.value);
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
   * 取消
   */
  function handleCancel() {
    closeModal();
  }

  /**
   * 标签点击事件
   * @param type
   */
  function handleTagClick(type) {
    let label = type=='input'?'## 输入参数':type=='output'?'## 输出参数':'## 参考参数';
    if(!formState.dataValue){
      formState.dataValue = `${label}：{{${type}}}`;
    }else{
      formState.dataValue += `\r\n\r\n${label}：{{${type}}}`;
      // 获取textarea元素并滚动到底部
      setTimeout(() => {
        const textarea = document.querySelector('textarea.ant-input') as HTMLTextAreaElement;
        if (textarea) {
          textarea.scrollTop = textarea.scrollHeight;
        }
      }, 0);
    }
  }
  /**
   * 关闭弹窗触发列表刷新
   *
   * @param value
   */
  function visibleChange(value) {
    if (!value) {
      emit('success');
    }
  }
  /**
   * 成功回调
   */
  function handleSuccess(data) {
    setFormState(data);
    emit('success');
  }

  /**
   * 参数配置点击事件
   * @param value
   */
  function handleParamSettingClick(value) {
    paramsSettingOpen(true, { type: value, metadata: metadata.value });
  }

  /**
   * 参数配置确定回调事件
   *
   * @param value
   */
  function handleParamsSettingOk(value) {
    Object.assign(metadata.value, value);
  }

  /**
   * 获取图标
   */
  function getImage() {
    return formState.icon ? getFileAccessHttpUrl(formState.icon) : defaultImg;
  }

  /**
   * 清除参数
   */
  function clearFormState() {
    formState.id = '';
    formState.name = '';
    formState.dataValue = '';
    formState.descr = '';
    formState.modelId = '';
    formState.metadata = '';
  }

  /**
   * 设置form属性
   * @param data
   */
  function setFormState(data: any) {
    //赋值
    Object.assign(formState, data);

    // 如果已有metadata，查询模型信息并更新到metadata中
    if (data?.metadata) {
      metadata.value = data.metadata ? JSON.parse(data.metadata) : {};
      formState.modelId = data.metadata ? JSON.parse(data.metadata).modelId || '' : '';
    }
  }

  //============= begin 提示词 ================================
  /**
   * 生成提示词
   */
  function generatedPrompt() {
    aiAppPromptModalOpen(true, {});
  }

  /**
   * 提示词回调
   *
   * @param value
   */
  function handleAiAppPromptOk(value) {
    formState.dataValue = value;
  }
  //============= end 提示词 ================================
  
  /**
   * 调试运行
   */
  async function debugRun(variables: any[]) {
    //提示词
    let sysMessage = formState.dataValue;
    let userMessage = "输入的内容：";
    //替换变量
    variables.forEach((item) => {
      userMessage += `${item.name}:${item.value}`;
    });
    //定义返回结果
    sysMessage += '定义返回格式：\n\n 得分：最终的得分，必须输出，必须输出一个数字，表示满足Prompt中评分标准的程度。得分范围从 0.0 到 1.0，1.0 表示完全满足评分标准，0.0 表示完全不满足评分标准。\n' +
        '原因：（对得分的可读解释）。最后，必须用一句话结束理由，该句话为：因此，应该给出的分数是(你前面得出的评分)。';
    console.log('userMessage', userMessage);
    if(!formState.modelId) {
      message.warning("请选择AI模型");
      return;
    }
    // 调用调试器运行
    let loading = debugRef.value?.loading;
    if (loading) return;
    debugRef.value.loading = true;
    let params = { prompts: sysMessage,content:userMessage,modelId:formState.modelId,modelParam:JSON.stringify(formState.metadata) }
    let res = await debugEvaluator(params).catch(() => debugRef.value.loading = false);
    debugRef.value.loading = false;
    if(res.success){
      debugRef.value.result = res.result
    }else{
      message.error(res.message);
    }
    console.log("debugEvaluator",res)
  }


  /**
   * 模型ID变化处理
   * 查询模型信息并更新到metadata中，供chat组件使用
   */
  async function handleModelIdChange(modelId: string) {
    if (!modelId) {
      // 如果清空模型，清除模型信息
      if (metadata.value.modelInfo) {
        delete metadata.value.modelInfo;
      }
      return;
    }
    try {
      const res = await defHttp.get(
        {
          url: '/airag/airagModel/queryById',
          params: { id: modelId },
        },
        { isTransformResponse: false }
      );
      if (res.success && res.result) {
        const model = res.result;
        // 将模型信息添加到metadata中
        if (!metadata.value) {
          metadata.value = {};
        }
        metadata.value['modelInfo'] = {
          provider: model.provider || '',
          modelType: model.modelType || '',
          modelName: model.modelName || '',
        };
      }
    } catch (e) {
      console.error('获取模型信息失败', e);
    }
  }
</script>

<style scoped lang="less">
  .pointer {
    cursor: pointer;
  }

  .orchestration,
  .view {
    color: #0a3069;
    font-weight: bold;
    text-align: center;
    font-size: 18px;
    width: 100%;
  }
  .type-title {
    color: #1d2025;
    margin-bottom: 4px;
  }

  .type-desc {
    color: #8f959e;
    font-weight: 400;
  }

  .setting-left {
    padding: 20px;
    overflow-y: auto;
    height: (100vh - 15px);
  }

  .setting-right {
    overflow-y: auto;
    height: (100vh - 15px);
    border-left: 1px solid #dee0e3;
    padding: 12px;
  }

  :deep(.ant-input-number) {
    width: 100%;
  }

  :deep(.ant-form-item .ant-form-item-label > label) {
    width: 100%;
  }

  .knowledge-img {
    width: 30px;
    height: 30px;
  }

  .flow-name {
    font-size: 14px;
    font-weight: bold;
    color: #354052;
    width: calc(100% - 20px);
    overflow: hidden;
    align-content: center;
    text-overflow: ellipsis;
    white-space: nowrap;
    display: grid;
  }
  .knowledge-name {
    margin-left: 4px;
  }

  .knowledge-card {
    margin-bottom: 10px;
    margin-right: 10px;
  }

  .knowledge-icon {
    display: none !important;
    position: relative;
    top: 6px;
  }

  .knowledge-card:hover {
    .knowledge-icon {
      display: block !important;
    }
  }
  .header-img {
    width: 35px;
    height: 35px;
    border-radius: 10px;
  }
  .flex {
    display: flex;
  }
  .header-name {
    color: #354052;
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
    max-width: 300px;
    margin-left: 10px;
    align-content: center;
  }
  .prompt-back {
    background-color: #eef4ff;
    border-radius: 12px;
    padding: 2px;
    border: 1px solid #77b2f8;
    box-sizing: border-box;
    margin-left: 5px;
    textarea {
      min-height: 250px;
      max-height: 400px;
      border: none !important;
    }
  }
  .prompt-title-padding {
    margin-left: 14px;
    height: 50px;
    align-content: center;
  }
  .prologue-chunk {
    background-color: #f2f4f7;
    border-radius: 12px;
    padding: 2px 10px 2px 10px;
    box-sizing: border-box;
  }

  .prologue-chunk-edit {
    background-color: #f2f4f7;
    border-radius: 12px;
    padding: 2px 0 2px 0;
    box-sizing: border-box;
  }
  .mt-10 {
    margin-top: 10px;
  }
  :deep(.ant-form-item-label) {
    padding: 0 !important;
  }

  :deep(.ant-form-item-required) {
    margin-left: 4px !important;
  }
  .knowledge-txt {
    color: #354052;
    cursor: pointer;
    margin-right: 10px;
    font-size: 12px;
  }
  .item-title {
    color: #111928;
    font-weight: 400;
  }
  :deep(.ant-form-item) {
    margin-bottom: 5px;
  }
  :deep(.vditor) {
    border: none;
  }
  :deep(.vditor-sv) {
    font-size: 14px;
  }
  :deep(.vditor-sv:focus) {
    background-color: #ffffff;
  }
  .space-between {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    button {
      padding: 0 6px;
      height: 25px;
      color: #155aef !important;
      margin-right: 10px;
      border: none;
    }
  }
  .ellipsis {
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
  }
  .quick-command {
    display: flex;
    width: 100%;
    margin-top: 10px;
    justify-content: space-between;
    background-color: #ffffff;
    padding: 4px 8px 4px;
    align-items: center;
    align-content: center;
    align-self: center;
    border-radius: 8px;
    height: 40px;
    .quick-command-icon {
      display: none;
    }
  }
  .quick-command:hover {
    background-color: #eff0f8;
    .quick-command-icon {
      display: flex;
    }
  }
  .data-empty-text {
    color: #757c8f;
    margin-left: 10px;
  }
  .mcp-warning-tip {
    display: flex;
    align-items: center;
    color: #fa8c16;
    font-size: 12px;
    line-height: 18px;
    font-weight: 500;
  }
  .flow-icon {
    width: 34px;
    height: 34px;
    border-radius: 10px;
  }
  :deep(.ant-card .ant-card-body) {
    padding: 16px;
  }
  .text-status {
    font-size: 12px;
    color: #676f83;
  }
  .tag-text {
    display: flow;
    max-width: 100px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    height: 20px;
    font-size: 12px;
    color: #3a3f4f;
  }
  .tag-input {
    align-self: center;
    color: #737c97;
    font-size: 12px;
    font-style: normal;
    font-weight: 500;
    line-height: 16px;
    margin-right: 6px;
    text-align: right;
    white-space: nowrap;
  }
  .tags-meadata {
    padding-inline: 2px;
    border-radius: 4px;
    display: flex;
    font-weight: 500;
    max-width: 100%;
  }
  .text-desc {
    width: 100%;
    font-weight: 400;
    display: inline-block;
    text-overflow: ellipsis;
    overflow: hidden;
    text-wrap: nowrap;
    font-size: 12px;
    color: #676f83;
  }

  /* 输出格式卡片样式 */
  .output-format-card {
    background: #f8f9ff;
    border: 1px solid #e1e5ff;
    border-radius: 8px;
    padding: 16px;
    margin-top: 10px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  }

  .output-format-header {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
    border-bottom: 1px solid #e8ecff;
    padding-bottom: 8px;
  }

  .output-format-icon {
    color: #155aef;
    font-size: 16px;
    margin-right: 8px;
  }

  .output-format-title {
    font-weight: 600;
    color: #155aef;
    font-size: 14px;
  }

  .output-format-content {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }

  .output-item {
    display: flex;
    flex-direction: column;
    gap: 4px;
  }

  .output-item-label {
    display: flex;
    align-items: center;
    gap: 6px;
  }

  .output-item-bullet {
    color: #155aef;
    font-weight: bold;
    font-size: 14px;
  }

  .output-item-title {
    font-weight: 600;
    color: #354052;
    font-size: 13px;
  }

  .output-item-desc {
    color: #5a6376;
    font-size: 13px;
    line-height: 1.5;
    margin-left: 20px;
  }

  .score-range {
    color: #e74c3c;
    font-weight: 600;
    background: #fff5f5;
    padding: 1px 4px;
    border-radius: 3px;
    font-size: 12px;
  }
  .output-format-card {
    background: #fafafa;
    border: 1px solid #e8e8e8;
    border-radius: 6px;
    padding: 16px;
    margin-top: 16px;
    transition: all 0.3s ease;

    &:hover {
      border-color: #1890ff;
      box-shadow: 0 2px 8px rgba(24, 144, 255, 0.15);
    }

    .output-format-header {
      display: flex;
      align-items: center;
      margin-bottom: 12px;
      padding-bottom: 8px;
      border-bottom: 1px solid #e8e8e8;

      .output-format-icon {
        font-size: 16px;
        color: #1890ff;
        margin-right: 8px;
      }

      .output-format-title {
        font-size: 14px;
        font-weight: 500;
        color: #262626;
      }
    }

    .output-format-content {
      .output-item {
        margin-bottom: 12px;
        padding: 8px 12px;
        background: #fff;
        border-radius: 4px;
        border-left: 3px solid #1890ff;

        &:last-child {
          margin-bottom: 0;
        }

        .output-item-desc {
          font-size: 13px;
          color: #595959;
          line-height: 1.6;

          .score-range {
            color: #ff7a45;
            font-weight: 600;
            background: rgba(255, 122, 69, 0.1);
            padding: 2px 6px;
            border-radius: 4px;
            margin: 0 2px;
          }
        }
      }
    }
  }
  .variable-container {
    background: #fafafa;
    border: 1px solid #e8e8e8;
    border-radius: 6px;
    padding: 16px;
    margin-top: 16px;
    transition: all 0.3s ease;

    &:hover {
      border-color: #1890ff;
      box-shadow: 0 2px 8px rgba(24, 144, 255, 0.15);
    }

    .variable-container-header {
      display: flex;
      align-items: center;
      margin-bottom: 12px;
      padding-bottom: 8px;
      border-bottom: 1px solid #e8e8e8;

      .output-format-icon {
        font-size: 16px;
        color: #1890ff;
        margin-right: 8px;
      }

      .variable-format-title {
        font-size: 14px;
        font-weight: 500;
        color: #262626;
      }
    }

    .variable-container-content {
      .variable-tag-wrapper {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;

        .variable-tag {
          cursor: pointer;
          border-radius: 4px;
          font-size: 12px;
          padding: 4px 8px;
          margin: 0;
          transition: all 0.3s ease;

          &.required-tag {
            background-color: #e6f7ff;
            border: 1px solid #91d5ff;
            color: #1890ff;
          }

          &.optional-tag {
            background-color: #f5f5f5;
            border: 1px solid #d9d9d9;
            color: #8c8c8c;
          }

          &:hover {
            transform: translateY(-2px);
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
          }
        }
      }
    }
  }
</style>
<style lang="less">
  .ai-prompt-edit-modal {
    .ant-modal .ant-modal-header {
      padding: 13px 32px !important;
    }
    .jeecg-basic-modal-close > span {
      margin-left: 0;
    }
  }
</style>
