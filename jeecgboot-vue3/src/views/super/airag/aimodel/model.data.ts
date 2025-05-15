import { FormSchema } from '@/components/Form';

import deepspeek from '/@/views/super/airag/aimodel/icon/deepspeek.png';
import ollama from '/@/views/super/airag/aimodel/icon/ollama.png';
import OpenAi from '/@/views/super/airag/aimodel/icon/OpenAi.png';
import qianfan from '/@/views/super/airag/aimodel/icon/qianfan.png';
import qianwen from '/@/views/super/airag/aimodel/icon/qianwen.png';
import zhipuai from '/@/views/super/airag/aimodel/icon/zhipuai.png';
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
    required: true,
    component: 'InputPassword',
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
    required: true,
    component: 'InputPassword',
    ifShow: ({ values }) => {
      if(values.provider==='DEEPSEEK' || values.provider==="OLLAMA" || values.provider==="OPENAI"
        || values.provider==="ZHIPU" || values.provider==="QWEN"){
        return false;
      }
      return true;
    },
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
  DEEPSEEK: deepspeek,
  OLLAMA: ollama,
  OPENAI: OpenAi,
  QIANFAN: qianfan,
  QWEN: qianwen,
  ZHIPU: zhipuai,
});
