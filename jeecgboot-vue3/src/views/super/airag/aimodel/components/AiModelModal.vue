<template>
  <BasicModal destroyOnClose @register="registerModal" :canFullscreen="false" width="600px" wrapClassName="ai-model-modal">
    <div class="modal">
      <div class="header">
        <span class="header-title">
          <span v-if="dataIndex ==='list' || dataIndex ==='add'" :class="dataIndex === 'list' ? '' : 'add-header-title pointer'" @click="goToList">选择供应商</span>
          <span v-if="dataIndex === 'add'" class="add-header-title"> > </span>
          <span v-if="dataIndex === 'add'" style="color: #1f2329">添加 {{ providerName }}</span>
        </span>

        <a-select v-if="dataIndex === 'list'" :bordered="false" class="header-select" size="small" v-model:value="modelType" @change="handleChange">
          <a-select-option v-for="item in modelTypeOption" :value="item.value">{{ item.text }}</a-select-option>
        </a-select>
      </div>
      <div class="model-content" v-if="dataIndex === 'list'">
        <a-row :span="24">
          <a-col :xxl="12" :xl="12" :lg="12" :md="12" :sm="12" :xs="24" v-for="item in modelTypeList">
            <a-card class="model-card" @click="handleClick(item)">
              <div class="model-header">
                <div class="flex">
                  <img :src="getImage(item.value)" class="header-img" />
                  <div class="header-text">{{ item.title }}</div>
                </div>
              </div>
            </a-card>
          </a-col>
        </a-row>
      </div>
      <a-tabs v-model:activeKey="activeKey" v-if="dataIndex === 'add' || dataIndex === 'edit'">
        <a-tab-pane :key="1" tab="基础信息">
          <div class="model-content">
            <BasicForm @register="registerForm">
              <template #modelType="{ model, field }">
                <a-select v-model:value="model[field]" @change="handleModelTypeChange" :disabled="modelTypeDisabled">
                  <a-select-option v-for="item in modelTypeAddOption" :value="item">
                    <span v-if="item === 'LLM'">语言模型</span>
                    <span v-else>向量模型</span>
                  </a-select-option>
                </a-select>
              </template>

              <template #modelName="{ model, field }">
                <AutoComplete v-model:value="model[field]" :options="modelNameAddOption" :filter-option="filterOption">
                  <template #option="{ value, label, descr, type }">
                    <a-tooltip placement="right" color="#ffffff" :overlayInnerStyle="{ color:'#646a73' }">
                      <template #title>
                        <div v-html="getTitle(descr)"></div>
                      </template>
                      <div style="display: flex;justify-content: space-between;">
                        <span>{{label}}</span>
                        <div>
                          <a-tag v-if="type && type.indexOf('text') != -1" color="#E8D7C3">文本</a-tag>
                          <a-tag v-if="type && type.indexOf('image') != -1" color="#C3D9DC">图像分析</a-tag>
                          <a-tag v-if="type && type.indexOf('vector') != -1" color="#D4E0D8">向量</a-tag>
                          <a-tag v-if="type && type.indexOf('embeddings') != -1" color="#FFEBD3">文本嵌入</a-tag>
                        </div>
                      </div>
                    </a-tooltip>
                  </template>
                </AutoComplete>
              </template>
            </BasicForm>
          </div>
        </a-tab-pane>
        <a-tab-pane :key="2" tab="高级配置"  v-if="modelParamsShow">
          <AiModelSeniorForm ref="modelParamsRef" :modelParams="modelParams"></AiModelSeniorForm>
        </a-tab-pane>
      </a-tabs>

    </div>
    <template v-if="dataIndex === 'add' || dataIndex === 'edit'" #footer>
      <a-button @click="cancel">关闭</a-button>
      <a-button @click="save" type="primary">保存</a-button>
    </template>
    <template v-else #footer> </template>
  </BasicModal>
</template>

<script lang="ts">
  import { ref, reactive } from 'vue';
  import BasicModal from '@/components/Modal/src/BasicModal.vue';
  import { useModal, useModalInner } from '@/components/Modal';
  import { initDictOptions } from '@/utils/dict';
  import model from './model.json';
  import { AutoComplete } from 'ant-design-vue';

  import BasicForm from '@/components/Form/src/BasicForm.vue';
  import { useForm } from '@/components/Form';
  import { formSchema, imageList } from '../model.data';
  import { editModel, queryById, saveModel } from '../model.api';
  import { useMessage } from '/@/hooks/web/useMessage';
  import AiModelSeniorForm from './AiModelSeniorForm.vue';
  export default {
    name: 'AddModelModal',
    components: {
      BasicForm,
      BasicModal,
      AiModelSeniorForm,
      AutoComplete,
    },
    emits: ['success', 'register'],
    setup(props, { emit }) {
      //ai类型数据
      const modelTypeData = ref<any>([]);
      //模型类型下拉框
      const modelTypeOption = ref<any>([]);
      //模型类型禁用状态
      const modelTypeDisabled = ref<boolean>(false);
      //模型类型
      const modelType = ref<string>('all');
      //模型供应商
      const modelTypeList = ref<any>([]);
      //list:供应商选择页面，add 添加编辑
      const dataIndex = ref<string>('list');
      //供应商名称
      const providerName = ref<string>('');
      //添加模型类型的option
      const modelTypeAddOption = ref<any>([]);
      //添加模型名称的option
      const modelNameAddOption = ref<any>([]);
      //模型数据
      const modelData = ref<any>({});
      //tab切换对应的key
      const activeKey = ref<number>(1);
      //模型参数
      const modelParams = ref<any>({});
      //是否显示模型参数
      const modelParamsShow = ref<boolean>(false);
      //模型参数ref
      const modelParamsRef = ref();

      const getImage = (name) => {
        return imageList.value[name];
      };
      //自动填充文本搜索事件
      const filterOption = (input: string, option: any)=>{
        return option.value.toUpperCase().indexOf(input.toUpperCase()) >= 0;
      }

      //表单配置
      const [registerForm, { resetFields, setFieldsValue, validate, clearValidate }] = useForm({
        schemas: formSchema,
        showActionButtonGroup: false,
        layout: 'vertical',
        wrapperCol: { span: 24 },
      });

      //注册modal
      const [registerModal, { closeModal, setModalProps }] = useModalInner(async (data) => {
        activeKey.value = 1;
        modelParamsShow.value = false;
        if(dataIndex.value !== 'list') {
          //重置表单
          await resetFields();
        }
        setModalProps({ minHeight: 500 });
        if (data.id) {
          dataIndex.value = 'edit';
          let values = await queryById({ id: data.id });
          if (values) {
            if(values.result.credential){
              let credential = JSON.parse(values.result.credential);
              if(credential.secretKey){
                values.result.secretKey = credential.secretKey;
              }
              if(credential.apiKey){
                values.result.apiKey = credential.apiKey;
              }
            }
            let provider = values.result.provider;
            let data = model.data.filter((item) => {
              return item.value.includes(provider);
            });
            if (data && data.length > 0) {
              modelTypeAddOption.value = data[0].type;
              modelNameAddOption.value = data[0][values.result.modelType];
            }
            if(values.result.modelType && values.result.modelType === 'LLM'){
              modelParamsShow.value = true;
            }
            if(values.result.modelParams){
              modelParams.value = JSON.parse(values.result.modelParams)
            }
            modelTypeDisabled.value = true;
            //表单赋值
            await setFieldsValue({
              ...values.result,
            });
            //初始化模型提供者
            initModelProvider();
          }
        } else {
          modelTypeDisabled.value = false;
          //初始化模型提供者
          initModelProvider();
          dataIndex.value = 'list';
          modelNameAddOption.value = [];
        }
      });

      //初始化模型类型
      initModelTypeOption();
      
      /**
       * 初始化 模型类型字典
       */
      function initModelTypeOption() {
        initDictOptions('model_type').then((data) => {
          modelTypeOption.value = data;
          //update-begin---author:wangshuai---date:2025-03-04---for: 解决页面tab刷新一次就多一个全部类型的选项---
          if(data[0].value != 'all'){
            modelTypeOption.value.unshift({
              text: '全部类型',
              value: 'all',
            });
          }
          //update-end---author:wangshuai---date:2025-03-04---for: 解决页面tab刷新一次就多一个全部类型的选项---
        });
      }

      /**
       * 下拉框值选中事件
       * @param value
       */
      function handleChange(value) {
        if ('all' == value) {
          modelTypeList.value = model.data;
          return;
        }
        let data = model.data.filter((item) => {
          return item.type.includes(value);
        });
        modelTypeList.value = data;
      }

      /**
       * 初始化模型提供者
       */
      function initModelProvider() {
        modelTypeList.value = model.data;
      }

      /**
       * 供应商点击事件
       *
       * @param item
       */
      function handleClick(item) {
        dataIndex.value = 'add';
        modelData.value = item;
        providerName.value = item.title;
        modelTypeAddOption.value = item.type;
        setTimeout(()=>{
          setFieldsValue({ 'provider': item.value, 'baseUrl': item.baseUrl })
        },100)
      }

      /**
       * 保存
       */
      async function save() {
        try {
          setModalProps({ confirmLoading: true });
          let values = await validate();
          let credential = {
            apiKey: values.apiKey,
            secretKey: values.secretKey
          }
          if(modelParamsRef.value){
            let modelParams = modelParamsRef.value.emitChange();
            if(modelParams){
              values.modelParams = JSON.stringify(modelParams);
            }
          }
          values.credential = JSON.stringify(credential);
          //新增
          if (!values.id) {
            values.provider = modelData.value.value;
            await saveModel(values);
            closeModal();
            emit('success');
          } else {
            await editModel(values);
            closeModal();
            emit('success');
          }
        }catch(e){
          if(e.hasOwnProperty('errorFields')){
            activeKey.value = 1;
          }
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      /**
       * 取消
       */
      function cancel() {
        dataIndex.value = 'list';
        closeModal();
      }

      /**
       * 模型类型选择事件
       * @param value
       */
      async function handleModelTypeChange(value) {
        await setFieldsValue({ modelName: '' });
        await clearValidate('modelName');
        await setFieldsValue({
          modelName: modelData.value[value+'DefaultValue']
        })
        modelNameAddOption.value = modelData.value[value];
        if(value === 'LLM'){
          modelParamsShow.value = true;
        }else{
          modelParamsShow.value = false;
        }
      }

      /**
       * 选择供应商
       */
      function goToList() {
        if (dataIndex.value === 'add') {
          dataIndex.value = 'list';
        }
      }

      /**
       * 获取标题
       * @param title
       */
      function getTitle(title) {
        if(!title){
          return "暂无描述内容";
        }
        return title.replaceAll("\n","<br>")
      }
      
      return {
        registerModal,
        modelTypeData,
        modelTypeOption,
        modelType,
        handleChange,
        modelTypeList,
        getImage,
        handleClick,
        dataIndex,
        providerName,
        save,
        cancel,
        registerForm,
        handleModelTypeChange,
        modelTypeAddOption,
        modelNameAddOption,
        goToList,
        modelTypeDisabled,
        activeKey,
        modelParams,
        modelParamsShow,
        modelParamsRef,
        filterOption,
        getTitle,
      };
    },
  };
</script>

<style scoped lang="less">
  .modal {
    padding: 12px 20px 20px 20px;
    .header {
      padding: 0 24px 24px 0;
      display: flex;
      justify-content: space-between;
      .header-title {
        font-size: 16px;
        font-weight: bold;
      }
      .header-select {
        margin-right: 10px;
      }
      .add-header-title {
        color: #646a73;
      }
    }
    .model-content {
      .model-header {
        position: relative;
        font-size: 14px;
        .header-img {
          width: 32px;
          height: 32px;
          margin-right: 12px;
        }
        .header-text {
          width: calc(100% - 80px);
          overflow: hidden;
          align-content: center;
        }
      }
    }
    .model-card {
      margin-right: 10px;
      margin-bottom: 10px;
      cursor: pointer;
    }
  }
  :deep(.ant-card .ant-card-body) {
    padding: 12px;
  }

  .pointer {
    cursor: pointer;
  }
  
  :deep(.jeecg-basic-modal-close){
    span{
      margin-left: 0 !important;
    }
  }
</style>
<style lang="less">
.ai-model-modal{
  .jeecg-basic-modal-close > span{
    margin-left: 0 !important;
  }
}
</style>
