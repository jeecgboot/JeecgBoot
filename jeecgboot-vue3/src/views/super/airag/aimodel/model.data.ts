import { FormSchema } from '@/components/Form';

import anthropic from './icon/anthropic.png';
import deepspeek from './icon/deepspeek.png';
import ollama from './icon/ollama.png';
import OpenAi from './icon/OpenAi.png';
import qianfan from './icon/qianfan.png';
import qianwen from './icon/qianwen.png';
import zhipuai from './icon/zhipuai.png';
import xinference from './icon/xinference.svg';
import vllm from './icon/vllm.png';
import imstdio from './icon/imstdio.png';
import gemini from './icon/gemini.png';
import kimi from './icon/kimi.ico';
import minimax from './icon/minimax.png';
import volcengine from './icon/volcengine.png';
import { ref } from 'vue';

/**
 * 表单
 */
export const formSchema: FormSchema[] = [
  {
    label: 'id',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    label: '模型名称',
    field: 'name',
    required: true,
    component: 'Input',
  },
  {
    label: '模型类型',
    field: 'modelType',
    slot: 'modelType',
    required: true,
    component: 'Select',
  },
  {
    label: '基础模型',
    field: 'modelName',
    required: true,
    slot: 'modelName',
    component: 'Select',
  },
  {
    label: 'API域名',
    field: 'baseUrl',
    required: true,
    component: 'Input'
  },
  {
    label: 'API Key',
    field: 'apiKey',
    required: ({ values }) => values.provider !== 'XINFERENCE',
    component: 'InputPassword',
    componentProps: {
      autocomplete: 'new-password',
    },
    ifShow: ({ values }) => {
      if(values.provider==="OLLAMA"){
        return false;
      }
      return true;
    },
  },
  {
    label: 'Secret Key',
    field: 'secretKey',
    required: false,
    component: 'InputPassword',
    ifShow: ({ values }) => {
      if (
        values.provider === 'DEEPSEEK' ||
        values.provider === 'OLLAMA' ||
        values.provider === 'OPENAI' ||
        values.provider === 'ZHIPU' ||
        values.provider === 'QWEN' ||
        values.provider === 'ANTHROPIC' ||
        values.provider === 'XINFERENCE' ||
        values.provider === 'VLLM' ||
        values.provider === 'LMSTDIO' ||
        values.provider === 'GOOGLE' ||
        values.provider === 'KIMI' ||
        values.provider === 'MINIMAX' ||
        values.provider === 'VOLCENGINE'
      ) {
        return false;
      }
      return true;
    },
  },
  {
    label: 'HTTP1.1协议',
    field: 'httpVersionOne',
    component: 'Switch',
    defaultValue: 1,
    helpMessage: '是否使用HTTP1.1协议，在长时间无响应的情况下，可以尝试关闭此开关',
    componentProps: {
      checkedValue: 1,
      unCheckedValue: 0,
    },
    ifShow: ({ values }) => {
      //update-begin---author:wangshuai---date:20261013---for：【issues/9888】AI 应用调用本地Ollama deepseek1.5模型时总是提示错误，AI助手没问题---
      return values.provider === 'VLLM' || values.provider === 'LMSTDIO' || values.provider === 'XINFERENCE' || values.provider === 'OLLAMA';
      //update-end---author:wangshuai---date:20261013---for：【issues/9888】AI 应用调用本地Ollama deepseek1.5模型时总是提示错误，AI助手没问题---
    },
  },
  {
    label: '额外参数',
    field: 'extraParams',
    slot: 'extraParams',
    component: 'Input',
    ifShow: ({ values }) => values.modelType === 'LLM',
  },
  {
    label: '供应者',
    field: 'provider',
    component: 'Input',
    show: false,
  },
];

/**
 * 图片路径映射
 *
 * @param name
 */
export const imageList = ref<any>({
  ANTHROPIC: anthropic,
  DEEPSEEK: deepspeek,
  OLLAMA: ollama,
  OPENAI: OpenAi,
  QIANFAN: qianfan,
  QWEN: qianwen,
  ZHIPU: zhipuai,
  XINFERENCE: xinference,
  VLLM: vllm,
  LMSTDIO: imstdio,
  GOOGLE: gemini,
  KIMI: kimi,
  MINIMAX: minimax,
  VOLCENGINE: volcengine,
});
