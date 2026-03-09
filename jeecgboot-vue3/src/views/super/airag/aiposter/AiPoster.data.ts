import { FormSchema } from '@/components/Form';

export const formSchema: FormSchema[] = [
  {
    field: 'drawModelId',
    label: '模型',
    component: 'JDictSelectTag',
    required: true,
    helpMessage: [
      '1、需要选择在模型中已有的图像模型',
      '2、智普语言模型不支持尺寸设置',
      "3、openAi旧版模型如(dall-e-2)需要选择尺寸，新版模型直接输入'竖版: 9:16即可'",
      '4、当前只有千问万象模型(wanx2.1-imageedit,wan2.5-i2i-preview)支持图生图',
      '5、wan2.5-i2i-preview支持多张图片',
      '6、当前文生图openAi效果最佳',
    ],
    componentProps: {
      dictCode: "airag_model where model_type = 'IMAGE' and activate_flag = 1,name,id",
    },
  },
  {
    field: 'content',
    label: '提示词',
    component: 'InputTextArea',
    required: true,
    componentProps: {
      rows: 10,
      placeholder: '请输入提示词，例如：一只可爱的猫咪，赛博朋克风格',
    },
  },
  {
    field: 'imageUrl',
    label: '参考图',
    component: 'JImageUpload',
    componentProps: {
      fileMax: 2,
    },
  },
  {
    field: 'imageSize',
    label: '图片尺寸',
    component: 'Select',
    defaultValue: '1024*1024',
    componentProps: {
      options: [
        { label: '1:1 (1024x1024)', value: '1024*1024' },
        { label: '16:9 (1280x720)', value: '1280*720' },
        { label: '9:16 (720x1280)', value: '720*1280' },
        { label: '4:3 (1024x768)', value: '1024*768' },
        { label: '3:4 (768x1024)', value: '768*1024' },
      ],
    },
  },
];
