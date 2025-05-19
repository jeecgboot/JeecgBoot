<template>
  <div class="model-params-popover">
    <div class="params" v-if="type === 'model'">
      <span style="font-size:14px">参数</span>
      <a-select value="加载预设" style="width: 96px" size="small" @change="onLoadPreset">
        <a-select-option v-for="(preset, idx) of presets" :value="idx" :key="idx">
          <a-space>
            <Icon :icon="preset.icon" />
            <span>{{ preset.name }}</span>
          </a-space>
        </a-select-option>
      </a-select>
    </div>
    <!-- 模型温度 -->
    <div class="setting-item" v-if="type === 'model'">
      <div class="label">
        <span>模型温度</span>
        <a-tooltip :title="tips.temperature">
          <Icon icon="ant-design:question-circle" />
        </a-tooltip>
      </div>
      <a-space>
        <a-switch v-model:checked="temperatureEnable" size="small"/>
        <a-slider v-bind="temperatureProps" v-model:value="model.temperature" :disabled="model['temperature'] === null"/>
        <a-input-number v-bind="temperatureProps" v-model:value="model.temperature" :disabled="model['temperature'] === null"/>
      </a-space>
    </div>
    <!-- 词汇属性 -->
    <div class="setting-item" v-if="type === 'model'">
      <div class="label">
        <span>词汇属性</span>
        <a-tooltip :title="tips.topP">
          <Icon icon="ant-design:question-circle" />
        </a-tooltip>
      </div>
      <a-space>
        <a-switch v-model:checked="topPEnable" size="small"/>
        <a-slider v-bind="topPProps" v-model:value="model.topP" :disabled="model['topP'] === null"/>
        <a-input-number v-bind="topPProps" v-model:value="model.topP" :disabled="model['topP'] === null"/>
      </a-space>
    </div>
    <!-- 话题属性 -->
    <div class="setting-item" v-if="type === 'model'">
      <div class="label">
        <span>话题属性</span>
        <a-tooltip :title="tips.presencePenalty">
          <Icon icon="ant-design:question-circle" />
        </a-tooltip>
      </div>
      <a-space>
        <a-switch v-model:checked="presencePenaltyEnable" size="small" />
        <a-slider v-bind="presencePenaltyProps" v-model:value="model.presencePenalty" :disabled="model['presencePenalty'] === null"/>
        <a-input-number v-bind="presencePenaltyProps" v-model:value="model.presencePenalty" :disabled="model['presencePenalty'] === null"/>
      </a-space>
    </div>
    <!-- 重复属性 -->
    <div class="setting-item" v-if="type === 'model'">
      <div class="label">
        <span>重复属性</span>
        <a-tooltip :title="tips.frequencyPenalty">
          <Icon icon="ant-design:question-circle" />
        </a-tooltip>
      </div>
      <a-space>
        <a-switch v-model:checked="frequencyPenaltyEnable" size="small" />
        <a-slider v-bind="frequencyPenaltyProps" v-model:value="model.frequencyPenalty" :disabled="model['frequencyPenalty'] === null"/>
        <a-input-number v-bind="frequencyPenaltyProps" v-model:value="model.frequencyPenalty" :disabled="model['frequencyPenalty'] === null"/>
      </a-space>
    </div>
    <!-- 最大回复 -->
    <div class="setting-item" v-if="type === 'model'">
      <div class="label">
        <span>最大回复</span>
        <a-tooltip :title="tips.maxTokens">
          <Icon icon="ant-design:question-circle" />
        </a-tooltip>
      </div>
      <a-space>
        <a-switch v-model:checked="maxTokensEnable" size="small" />
        <a-slider v-bind="maxTokensProps" v-model:value="model.maxTokens" :disabled="model['maxTokens'] === null"/>
        <a-input-number v-bind="maxTokensProps" v-model:value="model.maxTokens" :disabled="model['maxTokens'] === null"/>
      </a-space>
    </div>
    <!-- top k -->
    <div class="setting-item" v-if="type === 'knowledge'">
      <div class="label">
        <span>Top K</span>
        <a-tooltip :title="tips.topNumber">
          <Icon icon="ant-design:question-circle" />
        </a-tooltip>
      </div>
      <a-space>
        <a-switch v-model:checked="topNumberEnable" size="small" />
        <a-slider v-bind="topNumberProps" v-model:value="model.topNumber" :disabled="model['topNumber'] === null"/>
        <a-input-number v-bind="topNumberProps" v-model:value="model.topNumber" :disabled="model['topNumber'] === null"/>
      </a-space>
    </div>
    <!-- Score 阈值 -->
    <div class="setting-item" v-if="type === 'knowledge'">
      <div class="label">
        <span>Score 阈值</span>
        <a-tooltip :title="tips.similarity">
          <Icon icon="ant-design:question-circle" />
        </a-tooltip>
      </div>
      <a-space>
        <a-switch v-model:checked="similarityEnable" size="small" />
        <a-slider v-bind="similarityProps" v-model:value="model.similarity" :disabled="model['similarity'] === null"/>
        <a-input-number v-bind="similarityProps" v-model:value="model.similarity" :disabled="model['similarity'] === null"/>
      </a-space>
    </div>
  </div>
</template>

<script lang="ts">
  import { ref, computed } from 'vue';
  import { cloneDeep, omit } from 'lodash-es';

  export default {
    name: 'AiModelSeniorForm',
    components: {},
    props: {
      modelParams: {
        type: Object,
        default: {}
      },
      type: {
        type: String,
        default: 'model'
      }
    },
    emits: ['success', 'register', 'updateModel'],
    setup(props, { emit }) {
      // 预设参数
      const presets = [
        {
          name: '创意',
          icon: 'fxemoji:star',
          params: {
            temperature: 0.8,
            topP: 0.9,
            presencePenalty: 0.1,
            frequencyPenalty: 0.1,
            maxTokens: null,
          },
        },
        {
          name: '平衡',
          icon: 'noto:balance-scale',
          params: {
            temperature: 0.5,
            topP: 0.8,
            presencePenalty: 0.2,
            frequencyPenalty: 0.3,
            maxTokens: null,
          },
        },
        {
          name: '精确',
          icon: 'twemoji:direct-hit',
          params: {
            temperature: 0.2,
            topP: 0.7,
            presencePenalty: 0.5,
            frequencyPenalty: 0.5,
            maxTokens: null,
          },
        },
      ];

      // 参数介绍
      const tips = {
        temperature: '值越大，回复内容越赋有多样性创造性、随机性；设为0根据事实回答，希望得到精准答案应该降低该参数；日常聊天建议0.5-0.8。',
        topP: '值越小，Ai生成的内容越单调也越容易理解；值越大，Ai回复的词汇围越大，越多样化。',
        presencePenalty: '值越大，越能够让Ai更好地控制新话题的引入，建议微调或不变。',
        frequencyPenalty: '值越大，越能够让Ai更好地避免重复之前说过的话，建议微调或不变。',
        maxTokens:
          '设置Ai最大回复内容大小，会影响返回结果的长度。普通聊天建议500-800；短文生成建议800-2000；代码生成建议2000-3600；长文生成建议4000左右（或选择长回复模型)',
        topNumber: '用于筛选与用户问题相似度最高的文本片段。系统同时会根据选用模型上下文窗口大小动态调整分段数量。',
        similarity: '用于设置文本片段筛选的相似度阅值。'
      };

      // 参数：温度
      const temperatureProps = ref<any>({
        min: 0.1,
        max: 1,
        step: 0.1,
      });

      // 参数：词汇属性
      const topPProps = ref<any>({
        min: 0.1,
        max: 1,
        step: 0.1,
      });
      // 参数：话题属性
      const presencePenaltyProps = ref<any>({
        min: -2,
        max: 2,
        step: 0.1,
      });
      // 参数：重复属性
      const frequencyPenaltyProps = ref<any>({
        min: -2,
        max: 2,
        step: 0.1,
      });
      // 参数：最大回复
      const maxTokensProps = ref<any>({
        min: 1,
        max: 16000,
        step: 1,
      });    
      
      // 参数：topk
      const topNumberProps = ref<any>({
        min: 1,
        max: 10,
        step: 1,
      });     
      
      // 参数：Score 阈值
      const similarityProps = ref<any>({
        min: 0.1,
        max: 1,
        step: 0.1,
      });
      
      
      //参数对象
      const model = ref<any>(props.modelParams || {})
      
      //模型温度是否勾选
      const temperatureEnable = computed({
        get:()=> model.value.temperature != null,
        set:(value) => model.value.temperature = !value? null: 0.7
      });    
      
      //词汇属性是否勾选
      const topPEnable = computed({
        get:()=> model.value.topP != null,
        set:(value) => model.value.topP = !value? null: 0
      });
      
      //词汇属性是否勾选
      const presencePenaltyEnable = computed({
        get:()=> model.value.presencePenalty != null,
        set:(value) => model.value.presencePenalty = !value? null: 0
      });  
      
      //重复属性是否勾选
      const frequencyPenaltyEnable = computed({
        get:()=> model.value.frequencyPenalty != null,
        set:(value) => model.value.frequencyPenalty = !value? null: 0
      });   
      
      //最大回复
      const maxTokensEnable = computed({
        get:()=> model.value.maxTokens != null,
        set:(value) => model.value.maxTokens = !value? null: 520
      });
        
      //top k
      const topNumberEnable = computed({
        get:()=> model.value.topNumber != null,
        set:(value) => model.value.topNumber = !value? null: 4
      });   
      
      //Score 阈值
      const similarityEnable = computed({
        get:()=> model.value.similarity != null,
        set:(value) => model.value.similarity = !value? null: 0.74
      });
      
      // 加载预设
      function onLoadPreset(idx: number) {
        const preset = presets[idx];
        if (!preset) {
          return;
        }
        model.value = preset.params;
      }

      /**
       * 更新参数
       *
       * @param model
       */
      function emitChange() {
        return model.value;
      }

      /**
       * 设置modal值
       * @param values
       */
      function setModalParams(values){
        model.value = values
      }
      
      return {
        presets,
        onLoadPreset,
        tips,
        temperatureProps,
        topPProps,
        presencePenaltyProps,
        model,
        frequencyPenaltyProps,
        temperatureEnable,
        maxTokensProps,
        emitChange,
        topPEnable,
        presencePenaltyEnable,
        frequencyPenaltyEnable,
        maxTokensEnable,
        topNumberEnable,
        topNumberProps,
        similarityEnable,
        similarityProps,
        setModalParams,
      };
    },
  };
</script>

<style lang="less" scoped>
  .model-params-popover {
    font-size: 14px;
    width: 100%;
     .params{
       display: flex;
       justify-content: space-between;
     } 
    .setting-item{
      margin-top: 10px;
    }
    .setting-item .label {
      > span {
        vertical-align: middle;

        &.app-iconify {
          cursor: help;
          color: #888888;
        }
      }
    }

    .ant-space {
      .ant-slider {
        width: 380px;
      }

      .ant-input-number {
        width: 110px;
        min-width: 80px;
      }
    }
  }
</style>
