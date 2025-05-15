<template>
  <div class="p-2">
    <BasicModal wrapClassName="ai-app-edit-modal" destroyOnClose @register="registerModal" :canFullscreen="false" defaultFullscreen width="800px" :footer="null" @visible-change="visibleChange">
      <template #title>
        <div style="display: flex;width: 100%;justify-content: space-between;align-items: center;">
          <div style="display: flex">
            <img :src="getImage()" class="header-img"/>
            <div class="header-name">{{formState.name}}</div>
            <a-tooltip title="编辑">
              <Icon icon="ant-design:edit-outlined" style="margin-left: 4px;cursor: pointer" color="#354052" size="20" @click="handleEdit"></Icon>
            </a-tooltip>
          </div>
          <div>应用编排</div>
          <div style="display: flex">
            <a-button @click="handleOk" style="margin-right: 30px" type="primary">保存</a-button>
          </div>
        </div>
      </template>
      <div style="height: 100%; width: 100%">
        <a-row :span="24">
          <a-col :span="10">
            <div class="orchestration">编排</div>
          </a-col>
          <a-col :span="14">
            <div class="view">预览</div>
          </a-col>
        </a-row>
        <a-row :span="24">
          <a-col :span="10" class="setting-left">
            <a-form class="antd-modal-form" ref="formRef" :model="formState" :rules="validatorRules">
              <a-row>
                <a-col :span="24" v-if="formState.type==='chatFLow'" class="mt-10">
                  <div class="prologue-chunk">
                    <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.flowId">
                      <template #label>
                        <div style="display: flex;justify-content: space-between;width: 100%;">
                          <span>关联流程</span>
                          <span @click="handleAddFlowClick" class="knowledge-txt">
                             <Icon icon="ant-design:plus-outlined" size="13" style="margin-right: 2px"></Icon>添加
                          </span>
                        </div>
                      </template>
                      <a-card v-if="flowData" hoverable class="knowledge-card" :body-style="{ width: '100%' }">
                        <div style="display: flex; width: 100%; justify-content: space-between;">
                          <div style="width: 100%;display: flex;">
                            <img :src="getFlowImage(flowData.icon)" class="flow-icon"/>
                            <div style="display: grid;margin-left: 5px;align-items: center;width: calc(100% - 20px)">
                              <span class="flow-name ellipsis align-items: center;">{{ flowData.name }}</span>
                              <div class="flex text-status" v-if="flowData.metadata && flowData.metadata.length>0">
                                <span class="tag-input">输入</span>
                                <div v-for="(metaItem, index) in flowData.metadata">
                                  <a-tag color="rgba(87,104,161,0.08)" class="tags-meadata">
                                    <span v-if="index<5" class="tag-text">{{ metaItem.field }}</span>
                                  </a-tag>
                                </div>
                              </div>
                            </div>
                          </div>
                          <Icon @click="handleDeleteFlow" icon="ant-design:close-outlined" size="20" class="knowledge-icon"></Icon>
                        </div>
                      </a-card>
                      <div v-else class="data-empty-text">
                        工作流支持通过可视化的方式，对大语言模型、脚本、增强等功能进行组合，从而实现复杂、稳定的业务流程编排，例如旅行规划、报告分析。
                      </div>
                    </a-form-item>
                  </div>
                </a-col>
                <a-col :span="24" v-if="formState.type==='chatSimple'">
                  <div class="prompt-back ">
                    <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.prompt" style="margin-bottom:0;">
                      <template #label>
                        <div class="prompt-title-padding item-title space-between">
                          <span>提示词</span>
                          <a-button size="middle" @click="generatedPrompt" ghost>
                            <span style="align-items: center;display:flex">
                              <svg width="1em" height="1em" viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg"><path d="M18.9839 1.85931C19.1612 1.38023 19.8388 1.38023 20.0161 1.85931L20.5021 3.17278C20.5578 3.3234 20.6766 3.44216 20.8272 3.49789L22.1407 3.98392C22.6198 4.1612 22.6198 4.8388 22.1407 5.01608L20.8272 5.50211C20.6766 5.55784 20.5578 5.6766 20.5021 5.82722L20.0161 7.14069C19.8388 7.61977 19.1612 7.61977 18.9839 7.14069L18.4979 5.82722C18.4422 5.6766 18.3234 5.55784 18.1728 5.50211L16.8593 5.01608C16.3802 4.8388 16.3802 4.1612 16.8593 3.98392L18.1728 3.49789C18.3234 3.44216 18.4422 3.3234 18.4979 3.17278L18.9839 1.85931zM13.5482 4.07793C13.0164 2.64069 10.9836 2.64069 10.4518 4.07793L8.99368 8.01834C8.82648 8.47021 8.47021 8.82648 8.01834 8.99368L4.07793 10.4518C2.64069 10.9836 2.64069 13.0164 4.07793 13.5482L8.01834 15.0063C8.47021 15.1735 8.82648 15.5298 8.99368 15.9817L10.4518 19.9221C10.9836 21.3593 13.0164 21.3593 13.5482 19.9221L15.0063 15.9817C15.1735 15.5298 15.5298 15.1735 15.9817 15.0063L19.9221 13.5482C21.3593 13.0164 21.3593 10.9836 19.9221 10.4518L15.9817 8.99368C15.5298 8.82648 15.1735 8.47021 15.0063 8.01834L13.5482 4.07793zM5.01608 16.8593C4.8388 16.3802 4.1612 16.3802 3.98392 16.8593L3.49789 18.1728C3.44216 18.3234 3.3234 18.4422 3.17278 18.4979L1.85931 18.9839C1.38023 19.1612 1.38023 19.8388 1.85931 20.0161L3.17278 20.5021C3.3234 20.5578 3.44216 20.6766 3.49789 20.8272L3.98392 22.1407C4.1612 22.6198 4.8388 22.6198 5.01608 22.1407L5.50211 20.8272C5.55784 20.6766 5.6766 20.5578 5.82722 20.5021L7.14069 20.0161C7.61977 19.8388 7.61977 19.1612 7.14069 18.9839L5.82722 18.4979C5.6766 18.4422 5.55784 18.3234 5.50211 18.1728L5.01608 16.8593z"></path></svg>
                              <span style="margin-left: 4px">生成</span>
                            </span>
                          </a-button>
                        </div>
                      </template>
                      <a-textarea :rows="8" v-model:value="formState.prompt" placeholder="请输入提示词"/>
                    </a-form-item>
                  </div>
                </a-col>
                <a-col :span="24" class="mt-10">
                  <div class="prologue-chunk-edit">
                    <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.prologue" style="margin-bottom:0;">
                      <template #label>
                        <div class="prompt-title-padding item-title">开场白</div>
                      </template>
                      <div class="prologue-chunk-edit">
                        <j-markdown-editor :height="166" v-model:value="formState.prologue" @change="prologueTextAreaBlur" :preview="{ mode: 'view', action: [] }"></j-markdown-editor>
                      </div>
                    </a-form-item>
                  </div>
                </a-col>
                <a-col :span="24" class="mt-10">
                  <div class="prologue-chunk-edit">
                    <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.presetQuestion" style="margin-bottom:0;">
                      <template #label>
                        <div class="prompt-title-padding item-title space-between">
                          <div class="item-title">预设问题</div>
                          <a-tooltip title="添加预设问题">
                            <Icon icon="ant-design:plus-outlined" size="13" style="margin-right: 16px;cursor: pointer" @click="presetQuestionAddClick"></Icon>
                          </a-tooltip>
                        </div>
                      </template>
                      <div style="padding: 0 10px" v-if="presetQuestionList.length>0">
                        <draggable :disabled="disabledDrag" item-key="key" v-model="presetQuestionList" @end="presetQuestionEnd">
                          <template #item="{ element:item }">
                            <div style="display: flex;width: 100%;margin-top: 10px">
                              <Icon icon="ant-design:holder-outlined" size="20"></Icon>
                              <a-input placeholder="输入预设问题" v-model:value="item.descr" style="margin-left: 10px;" @blur="onBlur(item)" @focus="onFocus(item)" @change="questionChange"></a-input>
                              <Icon style="cursor: pointer;margin-left: 10px" icon="ant-design:delete-outlined" @click="deleteQuestionClick(item.key)"></Icon>
                            </div>
                          </template>
                        </draggable>
                      </div>
                      <div v-else class="data-empty-text">
                        预设问题问题是新对话的初始引导，用户可以快速发起预设对话
                      </div>
                    </a-form-item>
                  </div>
                </a-col>
                <a-col :span="24" class="mt-10">
                  <div class="prologue-chunk-edit">
                    <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.presetQuestion" style="margin-bottom:0;">
                      <template #label>
                        <div class="prompt-title-padding item-title space-between">
                          <div class="item-title">快捷指令</div>
                          <a-tooltip title="添加快捷指令">
                            <Icon icon="ant-design:plus-outlined" size="13" style="margin-right: 16px;cursor: pointer" @click="quickCommandAddClick"></Icon>
                          </a-tooltip>
                        </div>
                      </template>
                      <div style="padding: 0 10px" v-if="quickCommandList.length>0">
                        <draggable item-key="key" v-model="quickCommandList" @end="quickCommandEnd">
                          <template #item="{ element:item }">
                            <div class="quick-command">
                              <div style="display: flex;align-items: center">
                                <Icon v-if="item.icon" :icon="item.icon" size="20"></Icon>
                                <svg v-else width="14px" height="14px" viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg"><path d="M18.9839 1.85931C19.1612 1.38023 19.8388 1.38023 20.0161 1.85931L20.5021 3.17278C20.5578 3.3234 20.6766 3.44216 20.8272 3.49789L22.1407 3.98392C22.6198 4.1612 22.6198 4.8388 22.1407 5.01608L20.8272 5.50211C20.6766 5.55784 20.5578 5.6766 20.5021 5.82722L20.0161 7.14069C19.8388 7.61977 19.1612 7.61977 18.9839 7.14069L18.4979 5.82722C18.4422 5.6766 18.3234 5.55784 18.1728 5.50211L16.8593 5.01608C16.3802 4.8388 16.3802 4.1612 16.8593 3.98392L18.1728 3.49789C18.3234 3.44216 18.4422 3.3234 18.4979 3.17278L18.9839 1.85931zM13.5482 4.07793C13.0164 2.64069 10.9836 2.64069 10.4518 4.07793L8.99368 8.01834C8.82648 8.47021 8.47021 8.82648 8.01834 8.99368L4.07793 10.4518C2.64069 10.9836 2.64069 13.0164 4.07793 13.5482L8.01834 15.0063C8.47021 15.1735 8.82648 15.5298 8.99368 15.9817L10.4518 19.9221C10.9836 21.3593 13.0164 21.3593 13.5482 19.9221L15.0063 15.9817C15.1735 15.5298 15.5298 15.1735 15.9817 15.0063L19.9221 13.5482C21.3593 13.0164 21.3593 10.9836 19.9221 10.4518L15.9817 8.99368C15.5298 8.82648 15.1735 8.47021 15.0063 8.01834L13.5482 4.07793zM5.01608 16.8593C4.8388 16.3802 4.1612 16.3802 3.98392 16.8593L3.49789 18.1728C3.44216 18.3234 3.3234 18.4422 3.17278 18.4979L1.85931 18.9839C1.38023 19.1612 1.38023 19.8388 1.85931 20.0161L3.17278 20.5021C3.3234 20.5578 3.44216 20.6766 3.49789 20.8272L3.98392 22.1407C4.1612 22.6198 4.8388 22.6198 5.01608 22.1407L5.50211 20.8272C5.55784 20.6766 5.6766 20.5578 5.82722 20.5021L7.14069 20.0161C7.61977 19.8388 7.61977 19.1612 7.14069 18.9839L5.82722 18.4979C5.6766 18.4422 5.55784 18.3234 5.50211 18.1728L5.01608 16.8593z"></path></svg>
                                <div style="max-width: 400px;margin-left: 4px" class="ellipsis">{{item.name}}</div>
                              </div>
                              <div style="align-items: center" class="quick-command-icon">
                                <a-tooltip title="编辑">
                                  <Icon style="cursor: pointer;margin-left: 10px" icon="ant-design:edit-outlined" @click="editCommandClick(item)"></Icon>
                                </a-tooltip>
                                <a-tooltip title="删除">
                                  <Icon style="cursor: pointer;margin-left: 10px" icon="ant-design:delete-outlined" @click="deleteCommandClick(item.key)"></Icon>
                                </a-tooltip>
                              </div>
                            </div>
                          </template>
                        </draggable>
                      </div>
                      <div v-else class="data-empty-text">
                        快捷指令是对话输入框上方的按钮，配置完成后，用户可以快速发起预设对话
                      </div>
                    </a-form-item>
                  </div>
                </a-col>
                <a-col :span="24" v-if="formState.type==='chatSimple'" class="mt-10">
                  <div class="prologue-chunk">
                    <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.modelId">
                      <template #label>
                       <div style="display: flex;justify-content: space-between;width: 100%;margin-right: 2px">
                         <div class="item-title">AI模型</div>
                         <div @click="handleParamSettingClick('model')" class="knowledge-txt">
                            <Icon icon="ant-design:setting-outlined" size="13" style="margin-right: 2px"></Icon>参数配置
                         </div>
                       </div>
                      </template>
                      <JDictSelectTag
                          v-model:value="formState.modelId"
                          placeholder="请选择AI模型"
                          dict-code="airag_model where model_type = 'LLM',name,id"
                          style="width: 100%;"
                      ></JDictSelectTag>
                    </a-form-item>
                  </div>
                </a-col>
                <a-col :span="24" v-if="formState.type==='chatSimple'" class="mt-10">
                  <div class="prologue-chunk">
                    <a-form-item
                        class="knowledgeId"
                        style="width: 100%"
                        :labelCol="labelCol"
                        :wrapperCol="wrapperCol"
                        v-bind="validateInfos.knowledgeIds"
                    >
                      <template #label>
                        <div style="display: flex; justify-content: space-between; width: 100%;margin-left: 2px;">
                          <div class="item-title">知识库</div>
                          <div>
                            <span @click="handleParamSettingClick('knowledge')" class="knowledge-txt">
                              <Icon icon="ant-design:setting-outlined" size="13" style="margin-right: 2px"></Icon>参数配置
                            </span>
                            <span @click="handleAddKnowledgeIdClick" class="knowledge-txt">
                              <Icon icon="ant-design:plus-outlined" size="13" style="margin-right: 2px"></Icon>添加
                            </span>
                          </div>
                        </div>
                      </template>
                      <a-row :span="24">
                        <a-col :span="12" v-for="item in knowledgeDataList" v-if="knowledgeDataList && knowledgeDataList.length>0">
                          <a-card hoverable class="knowledge-card" :body-style="{ width: '100%' }">
                            <div style="display: flex; width: 100%; justify-content: space-between">
                              <div>
                                <img class="knowledge-img" :src="knowledge" />
                                <span class="knowledge-name">{{ item.name }}</span>
                              </div>
                              <Icon @click="handleDeleteKnowledge(item.id)" icon="ant-design:close-outlined" size="20" class="knowledge-icon"></Icon>
                            </div>
                          </a-card>
                        </a-col>
                        <div v-else class="data-empty-text">
                          添加知识库后，用户发送消息时，智能体能够引用文本知识中的内容回答用户问题。
                        </div>
                      </a-row>
                    </a-form-item>
                  </div>
                </a-col>
                <a-col :span="24" class="mt-10">
                  <div class="prologue-chunk">
                    <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" v-bind="validateInfos.msgNum">
                      <template #label>
                        <div style="margin-left: 2px">历史聊天记录</div>
                      </template>
                      <a-input-number v-model:value="formState.msgNum"></a-input-number>
                    </a-form-item>
                  </div>
                </a-col>
              </a-row>
            </a-form>
          </a-col>
          <a-col :span="14" class="setting-right">
            <chat :uuid="uuid" :prologue="prologue" :appId="appId" :formState="formState" url="/airag/app/debug" :presetQuestion="presetQuestion" :quickCommandData="quickCommandList"></chat>
          </a-col>
        </a-row>
      </div>
    </BasicModal>

    <!--  Ai知识库选择弹窗   -->
    <AiAppAddKnowledgeModal @register="registerKnowledgeModal" @success="handleSuccess"></AiAppAddKnowledgeModal>
    <!--  Ai添加流程弹窗  -->
    <AiAppAddFlowModal @register="registerFlowModal" @success="handleAddFlowSuccess"></AiAppAddFlowModal>
    <!-- Ai配置弹窗   -->
    <AiAppParamsSettingModal @register="registerParamsSettingModal" @ok="handleParamsSettingOk"></AiAppParamsSettingModal>
    <!--  Ai应用新增编辑弹窗  -->
    <AiAppModal @register="registerAiAppModal" @success="handelEditSuccess"></AiAppModal>
    <!-- Ai生成器   -->
    <AiAppGeneratedPromptModal @register="registerAiAppPromptModal" @ok="handleAiAppPromptOk"></AiAppGeneratedPromptModal>
    <!--  Ai快捷指令  -->
    <AiAppQuickCommandModal @register="registerAiAppCommandModal" @ok="handleAiAppCommandOk" @update-ok="handleAiAppCommandUpdateOk"></AiAppQuickCommandModal>
  </div>
</template>

<script lang="ts">
  import { ref, reactive, nextTick, computed } from 'vue';
  import BasicModal from '@/components/Modal/src/BasicModal.vue';
  import { useModal, useModalInner } from '@/components/Modal';
  import { Form, TimePicker } from 'ant-design-vue';
  import { initDictOptions } from '@/utils/dict';
  import {queryKnowledgeBathById, saveApp, queryById, queryFlowById} from '../AiApp.api';
  import JDictSelectTag from '@/components/Form/src/jeecg/components/JDictSelectTag.vue';
  import AiAppAddKnowledgeModal from './AiAppAddKnowledgeModal.vue';
  import AiAppParamsSettingModal from './AiAppParamsSettingModal.vue';
  import AiAppGeneratedPromptModal from './AiAppGeneratedPromptModal.vue';
  import AiAppQuickCommandModal from './AiAppQuickCommandModal.vue';
  import AiAppAddFlowModal from './AiAppAddFlowModal.vue';
  import AiAppModal from './AiAppModal.vue';
  import chat from '../chat/chat.vue';
  import knowledge from '/@/views/super/airag/aiknowledge/icon/knowledge.png';
  import { cloneDeep } from 'lodash-es';
  import dayjs from 'dayjs';
  import JImageUpload from '@/components/Form/src/jeecg/components/JImageUpload.vue';
  import defaultImg from '../img/ailogo.png';
  import {getFileAccessHttpUrl, randomString, simpleDebounce} from "@/utils/common/compUtils";
  import JSearchSelect from "@/components/Form/src/jeecg/components/JSearchSelect.vue";
  import JMarkdownEditor from "@/components/Form/src/jeecg/components/JMarkdownEditor.vue";
  import AiAppJson from './AiApp.json'
  import draggable from 'vuedraggable';
  import { useMessage } from "@/hooks/web/useMessage";
  import defaultFlowImg from "@/assets/images/ai/aiflow.png";
  
  export default {
    name: 'AiAppSettingModal',
    components: {
      draggable,
      JMarkdownEditor,
      JSearchSelect,
      JImageUpload,
      JDictSelectTag,
      BasicModal,
      AiAppAddKnowledgeModal,
      AiAppParamsSettingModal,
      AiAppAddFlowModal,
      AiAppModal,
      chat,
      AiAppGeneratedPromptModal,
      AiAppQuickCommandModal,
    },
    emits: ['success', 'register'],
    setup(props, { emit }) {
      const title = ref<string>('设置');

      //保存或修改
      const isUpdate = ref<boolean>(false);
      //uuid
      const uuid = ref(randomString(16));
      //app类型
      const appTypeOption = ref<any>([]);
      //应用类型
      const type = ref<string>('chatSimple');
      //form表单数据
      const formState = reactive<any>({
        name: '',
        descr: '',
        msgNum: 1,
        prompt: '',
        prologue: null,
        knowledgeIds: '',
        id: '',
        type: '',
        modelId: '',
        icon: '',
        presetQuestion:''
      });
      //表单验证
      const validatorRules = ref<any>({
        name: [{ required: true, message: '请输入应用名称!' }],
        modelId: [{ required: true, message: '请选择AI模型!' }],
        flowId:[{ required: true, message: '请选择AI流程!' }]
      });
      //注册form
      const formRef = ref();
      const useForm = Form.useForm;
      const { resetFields, validate, validateInfos, validateField } = useForm(formState, validatorRules, { immediate: false });
      const labelCol = ref<any>({ span: 24 });
      const wrapperCol = ref<any>({ span: 24 });
      //关联知识库的id
      const knowledgeIds = ref<any>('');
      //知识库集合
      const knowledgeDataList = ref<any>([]);
      //开场白的数据
      const prologue = ref<any>('');
      //应用id
      const appId = ref<any>('');
      //参数配置
      const metadata = ref<any>({});
      const presetQuestion = ref<string>('');
      //预设问题集合
      const presetQuestionList = ref<any>([{ key:1, sort: 1, descr: '' }]);
      //快捷指令集合
      const quickCommandList = ref<any>([]);
      //快捷指令
      const quickCommand = ref<any>('');
      const { createMessage } = useMessage();
      //注册modal
      const [registerModal, { closeModal, setModalProps }] = useModalInner(async (data) => {
        appId.value = data.id;
        isUpdate.value = !!data?.isUpdate;
        clearParam();
        if (isUpdate.value) {
          setTimeout(() => {
            setFormState(data);
          }, 300);
        } else {
          //新增成功之后需要有id
          queryById({ id: data.id }).then((res) => {
            if (res.success) {
              //赋值
              Object.assign(formState, res.result);
              formState.prompt = AiAppJson.prompt;
              formState.prologue = AiAppJson.prologue;
              formState.presetQuestion = JSON.stringify(AiAppJson.presetQuestion);
              prologue.value = AiAppJson.prologue;
              presetQuestion.value = formState.presetQuestion;
              presetQuestionList.value = AiAppJson.presetQuestion;
              addRules(res.result.type)
            }
          });
        }
        setModalProps({ bodyStyle: { padding: '10px' } });
      });

      //注册modal
      const [registerKnowledgeModal, { openModal }] = useModal();
      const [registerFlowModal, { openModal: registerFlowOpen }] = useModal();
      const [registerParamsSettingModal, { openModal: paramsSettingOpen }] = useModal();
      const [registerAiAppModal, { openModal: aiAppModalOpen }] = useModal();
      const [registerAiAppPromptModal, { openModal: aiAppPromptModalOpen }] = useModal();
      const [registerAiAppCommandModal, { openModal: aiAppCommandModalOpen }] = useModal();

      /**
       * 保存
       */
      async function handleOk() {
        try {
          let values = await validate();
          setModalProps({ confirmLoading: true });
          formState.knowledgeIds = knowledgeIds.value;
          await saveApp(formState);
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      //初始化AI应用类型
      initAppTypeOption();

      function initAppTypeOption() {
        initDictOptions('ai_app_type').then((data) => {
          if (data && data.length > 0) {
            for (const datum of data) {
              if (datum.value === 'chatSimple') {
                datum['desc'] = '适合新手创建小助手';
              } else if (datum.value === 'chatFLow') {
                datum['desc'] = '适合高级用户自定义小助手的工作流';
              }
            }
          }
          appTypeOption.value = data;
        });
      }

      /**
       * 取消
       */
      function handleCancel() {
        closeModal();
      }

      /**
       * 应用类型点击事件
       */
      function handleTypeClick(val) {
        type.value = val;
      }

      /**
       * 添加关联知识库
       */
      function handleAddKnowledgeIdClick() {
        openModal(true, {
          knowledgeIds: knowledgeIds.value,
          knowledgeDataList: knowledgeDataList.value,
        });
      }

      /**
       * 选中回调事件
       * @param knowledgeId
       * @param knowledgeData
       */
      function handleSuccess(knowledgeId, knowledgeData) {
        knowledgeIds.value = cloneDeep(knowledgeId.join(','));
        knowledgeDataList.value = cloneDeep(knowledgeData);
        formState.knowledgeIds = knowledgeIds.value;
      }

      /**
       * 删除知识库
       */
      function handleDeleteKnowledge(id) {
        let array = knowledgeIds.value.split(',');
        let findIndex = array.findIndex((item) => item === id);
        if (findIndex != -1) {
          array.splice(findIndex, 1);
          knowledgeIds.value = array ? array.join(',') : '';
          knowledgeDataList.value.splice(findIndex, 1);
          formState.knowledgeIds = knowledgeIds.value;
        }
      }

      /**
       * 根据知识库id查询知识库内容
       * @param ids
       */
      function getKnowledgeDataList(ids) {
        queryKnowledgeBathById({ ids: ids }).then((res) => {
          if (res.success) {
            knowledgeDataList.value = res.result;
            knowledgeIds.value = ids;
          }
        });
      }

      /**
       * 开场白值改变事件
       */
      function prologueTextAreaBlur(value) {
        prologue.value = value;
      }

      /**
       * 关闭弹窗触发列表刷新
       * 
       * @param value
       */
      function visibleChange(value) {
        if(!value){
          emit('success');
        }
      }

      /**
       * 添加检验
       * 
       * @param type
       */
      function addRules(type){
        if(type === 'chatSimple') {
          validatorRules.value = {
            name: [{ required: true, message: '请输入应用名称!' }],
            modelId: [{ required: true, message: '请选择AI模型!' }],
          }
        }else if(type === 'chatFLow') {
          validatorRules.value = {
            name: [{ required: true, message: '请输入应用名称!' }],
            flowId:[{ required: true, message: '请选择AI流程!' }]
          }
        }
      }

      /**
       * 参数配置点击事件
       * @param value
       */
      function handleParamSettingClick(value) {
        paramsSettingOpen(true,{ type: value, metadata : metadata.value })
      }

      /**
       * 参数配置确定回调事件
       * 
       * @param value
       */
      function handleParamsSettingOk(value){
        metadata.value = value;
        if(value) {
          formState.metadata = JSON.stringify(value);
        }
      }

      //流程id
      const flowId = ref<string>('');
      //流程数据
      const flowData = ref<any>(null);

      /**
       * 根据流程id查询流程
       */
      function getFlowDataById(id) {
        queryFlowById({ id: id}).then((res) =>{
          if(res.success){
            flowData.value = res.result;
            flowId.value = res.result.id;
            if(res.result.metadata){
              let metadata = JSON.parse(res.result.metadata);
              if(metadata.inputs){
                flowData.value.metadata = metadata.inputs;
              }
            }
          }
        })
      }

      /**
       * 添加流程
       */
      function handleAddFlowClick() {
        registerFlowOpen(true,{ flowId: flowId.value, flowData: flowData.value })
      }

      /**
       * 添加流程确定事件
       * @param values
       */
      function handleAddFlowSuccess(values) {
        flowId.value = values.flowId;
        formState.flowId = values.flowId;
        flowData.value = values.flowData;
      }

      /**
       * 删除流程
       */
      function handleDeleteFlow() {
        flowId.value = "";
        formState.flowId = "";
        flowData.value = null;
      }

      /**
       * 获取图标
       */
      function getImage() {
        return formState.icon ? getFileAccessHttpUrl(formState.icon): defaultImg;
      }

      /**
       * 获取流程图标
       */
      function getFlowImage(icon) {
        return icon ? getFileAccessHttpUrl(icon) : defaultFlowImg;
      }

      /**
       * 编辑应用弹窗
       */
      function handleEdit() {
        aiAppModalOpen(true,{
          isUpdate: true,
          record: formState
        })
      }

      /**
       * 应用编辑回调事件
       * 
       * @param values
       */
      function handelEditSuccess(values) {
        formState.icon = values.icon ? values.icon:'';
        formState.name = values.name ? values.name:'';
      }
      
      //=========== begin预设问题 ===========================
      
      // 编辑状态不允许拖动
      const disabledDrag = computed(()=>{
        let list = presetQuestionList.value;
        if(list && list.length>0){
          let arr = list.filter(item=>item.update==true)
          if(arr.length>0){
            return true
          }
        }
        return false;
      });
      
      /**
       * 预设问题拖拽
       */
      function presetQuestionEnd(){
        presetQuestion.value = JSON.stringify(presetQuestionList.value);
        formState.presetQuestion = presetQuestion.value;
      }

      /**
       * 预设问题添加
       * 
       * @param e
       */
      function presetQuestionAddClick(e){
        let find = presetQuestionList.value.find((item)=> item.descr == '');
        if(find){
          return;
        }
        const length = presetQuestionList.value.length;
        presetQuestionList.value.push({key: length + 1, sort: length + 1, descr: ''})
      } 
      
      /**
       * 预设问题删除
       * 
       * @param key
       */
      function deleteQuestionClick(key){
        presetQuestionList.value = presetQuestionList.value.filter((item) => item.key !== key);
        presetQuestion.value = JSON.stringify(presetQuestionList.value);
        formState.presetQuestion = presetQuestion.value;
      }

      /**
       * input聚焦事件
       * @param item
       */
      function onFocus(item){
        item.update = true;
      }

      /**
       * input 失焦事件
       * @param item
       */
      function onBlur(item){
        item.update = false;
      }

      /**
       * 预设问题值改变事件
       * 
       */
      function questionChange() {
        if(presetQuestionList.value && presetQuestionList.value.length>0){
          presetQuestion.value = JSON.stringify(presetQuestionList.value);
          formState.presetQuestion = presetQuestion.value;
        }else{
          presetQuestion.value = "";
          formState.presetQuestion = "";
        }
      }
      
      //=========== end预设问题 ===========================
      
      /**
       * 清除参数
       */
      function clearParam() {
        knowledgeIds.value = '';
        knowledgeDataList.value = [];
        prologue.value = '';
        flowId.value = '';
        flowData.value = null;
        presetQuestion.value = '';
        presetQuestionList.value = [];
        quickCommandList.value = [];
        quickCommand.value = '';
      }

      /**
       * 设置form属性
       * @param data
       */
      function setFormState(data: any) {
        resetFields();
        addRules(data.type)
        if (data.prologue) {
          prologue.value = data.prologue ? data.prologue : '';
        }
        data.msgNum = data.msgNum ? data.msgNum : 1;
        if(data.metadata){
          metadata.value = JSON.parse(data.metadata);
        }
        if(data.presetQuestion){
          presetQuestion.value = data.presetQuestion;
          presetQuestionList.value = JSON.parse(data.presetQuestion);
        }
        if(data.quickCommand){
          quickCommandList.value = JSON.parse(data.quickCommand);
        }
        //赋值
        Object.assign(formState, data);
        //根据知识库id查询知识库内容
        if (data.type === 'chatSimple' && data.knowledgeIds) {
          getKnowledgeDataList(data.knowledgeIds);
        }
        //根据知识库id查询流程信息
        if (data.type === 'chatFLow' && data.flowId) {
          getFlowDataById(data.flowId);
        }
      }

      //============= begin 提示词 ================================
      /**
       * 生成提示词
       */
      function generatedPrompt() {
        aiAppPromptModalOpen(true,{})
      }

      /**
       * 提示词回调
       * 
       * @param value
       */
      function handleAiAppPromptOk(value) {
        formState.prompt = value;
      }
      //============= end 提示词 ================================
      
      //=============== begin 快捷指令 ============================
      function quickCommandEnd() {
        quickCommand.value = JSON.stringify(quickCommandList.value);
        formState.quickCommand = quickCommand.value;
      }

      /**
       * 快捷指令新增呢个==点击事件
       */
      function quickCommandAddClick(){
        if(quickCommandList.value && quickCommandList.value.length > 4){
          createMessage.warning("最多只能添加5个！");
          return;
        }
        aiAppCommandModalOpen(true,{})
      }     
      
      /**
       * 快捷指令编辑点击事件
       * @param item
       */
      function editCommandClick(item){
        aiAppCommandModalOpen(true,{
          isUpdate: true,
          record: item
        })
      }    
      
      /**
       * 快捷指令添加回调事件
       * @param value
       */
      function handleAiAppCommandOk(value){
        quickCommandList.value.push({ key: quickCommandList.value.length + 1, ...value });
        quickCommand.value = JSON.stringify(quickCommandList.value);
        formState.quickCommand = quickCommand.value;
      }
      
      /**
       * 快捷指令更新回调事件
       * @param value
       */
      function handleAiAppCommandUpdateOk(value) {
        let findIndex = quickCommandList.value.findIndex(item => item.key === value.key);
        if(findIndex>-1){
          quickCommandList.value[findIndex] = value;
        }
      }

      /**
       * 删除快捷指令
       * @param value
       */
      function deleteCommandClick(value) {
        let findIndex = quickCommandList.value.findIndex(item => item.key === value);
        if(findIndex>-1){
          quickCommandList.value.splice(findIndex, 1);
          quickCommand.value = JSON.stringify(quickCommandList.value);
          formState.quickCommand = quickCommand.value;
        }
      }
      //=============== end 快捷指令 ============================
      
      return {
        registerModal,
        title,
        handleOk,
        handleCancel,
        appTypeOption,
        type,
        handleTypeClick,
        formState,
        validatorRules,
        labelCol,
        wrapperCol,
        validateInfos,
        handleAddKnowledgeIdClick,
        registerKnowledgeModal,
        knowledgeDataList,
        knowledge,
        handleSuccess,
        handleDeleteKnowledge,
        uuid,
        prologueTextAreaBlur,
        prologue,
        appId,
        visibleChange,
        handleParamSettingClick,
        registerParamsSettingModal,
        handleParamsSettingOk,
        registerFlowModal,
        handleAddFlowSuccess,
        handleAddFlowClick,
        flowData,
        handleDeleteFlow,
        getImage,
        handleEdit,
        registerAiAppModal,
        handelEditSuccess,
        presetQuestionEnd,
        presetQuestionList,
        presetQuestionAddClick,
        deleteQuestionClick,
        onBlur,
        onFocus,
        disabledDrag,
        questionChange,
        presetQuestion,
        generatedPrompt,
        registerAiAppPromptModal,
        handleAiAppPromptOk,
        quickCommandList,
        quickCommandEnd,
        registerAiAppCommandModal,
        quickCommandAddClick,
        handleAiAppCommandOk,
        editCommandClick,
        handleAiAppCommandUpdateOk,
        deleteCommandClick,
        quickCommand,
        getFlowImage,
        metadata,
      };
    },
  };
</script>

<style scoped lang="less">
  .pointer {
    cursor: pointer;
  }

  .orchestration,.view{
    color: #0a3069;
    font-weight: bold;
    text-align: center;
    font-size: 18px;
    width: 100%;
    padding-bottom: 10px;
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

  .flow-name{
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
  .header-img{
    width: 35px;
    height: 35px;
    border-radius: 10px;
  }
  .flex{
    display: flex;
  }
  .header-name{
    color:#354052;
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
    max-width: 300px;
    margin-left: 10px;
    align-content: center;
  }
  .prompt-back{
    background-color: rgba(238,244,255,1);
    border-radius: 12px;
    padding: 2px;
    border: 1px solid #77B2F8;
    box-sizing: border-box;
    margin-left: 5px;
    textarea{
      min-height: 250px;
      max-height: 400px;
      border: none !important;
    }
  }
  .prompt-title-padding{
    margin-left: 14px;
    height: 50px;
    align-content: center;
  }
  .prologue-chunk{
    background-color: #f2f4f7;
    border-radius: 12px;
    padding: 2px 10px 2px 10px;
    box-sizing: border-box;
  }  
  
  .prologue-chunk-edit{
    background-color: #f2f4f7;
    border-radius: 12px;
    padding: 2px 0 2px 0;
    box-sizing: border-box;
  }
  .mt-10{
    margin-top: 10px;
  }
  :deep(.ant-form-item-label){
    padding: 0 !important;
  }
 
  :deep(.ant-form-item-required){
    margin-left: 4px !important;
  }
  .knowledge-txt{
    color: #354052;
    cursor: pointer;
    margin-right: 10px;
    font-size: 12px
  }
  .item-title{
    color: #111928;
    font-weight: 400;
  }
  :deep(.ant-form-item){
    margin-bottom: 5px;
  }
  :deep(.vditor){
    border: none;
  } 
  :deep(.vditor-sv){
    font-size: 14px;
  }
  :deep(.vditor-sv:focus){
    background-color: #ffffff;
  }
  .space-between{
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    button{
      padding: 0 6px;
      height: 25px;
      color: #155AEF !important;
      margin-right: 10px;
      border: none;
    }
  }
  .ellipsis{
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
  }
  .quick-command{
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
    .quick-command-icon{
      display: none;
    }
  }
  .quick-command:hover{
    background-color: #EFF0F8;
    .quick-command-icon{
      display: flex;
    }
  }
  .data-empty-text{
    color: rgba(32,41,69,0.6);
    margin-left: 10px;
  }
  .flow-icon{
    width: 34px;
    height: 34px;
    border-radius: 10px;
  }
  :deep(.ant-card .ant-card-body) {
    padding: 16px;
  }
  .text-status{
    font-size: 12px;
    color: #676F83;
  }
  .tag-text {
    display: flow;
    max-width: 100px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    height: 20px;
    font-size: 12px;
    color: rgba(15, 21, 40,0.82);
  }
  .tag-input{
    align-self: center;
    color: rgba(55,67,106,0.7);
    font-size: 12px;
    font-style: normal;
    font-weight: 500;
    line-height: 16px;
    margin-right: 6px;
    text-align: right;
    white-space: nowrap;
  }
  .tags-meadata{
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
    color: #676F83;
  }
</style>
<style lang="less">
  .ai-app-edit-modal{
    .ant-modal .ant-modal-header{
      padding: 13px 32px !important;
    }
    .jeecg-basic-modal-close > span{
      margin-left: 0;
    }
  }
</style>
