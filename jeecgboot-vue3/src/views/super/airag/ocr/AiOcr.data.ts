import { BasicColumn, FormSchema } from '@/components/Table';

//ocr表格
export const columns: BasicColumn[] = [
  {
    title: '编号',
    dataIndex: 'id',
    ifShow: false,
  },
  {
    title: '标题',
    dataIndex: 'title',
    ellipsis: true,
    width: 300,
  },
  {
    title: '提示词',
    dataIndex: 'prompt',
    ellipsis: true,
    width: 300,
  },
];

//ocr表单
export const schemas: FormSchema[] = [
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    label: '标题',
    field: 'title',
    component: 'Input',
    required: true,
  },
  {
    label: '提示词',
    field: 'prompt',
    component: 'InputTextArea',
    componentProps: {
      row: 4,
      autosize: { minRows: 4, maxRows: 6 },
    },
    required: true,
  },
];

//ocr解析表单
export const analysisSchemas: FormSchema[] = [
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    label: '图片',
    field: 'url',
    component: 'JImageUpload',
    required: true,
  },
  {
    label: '提示词',
    field: 'prompt',
    component: 'InputTextArea',
    componentProps: {
      row: 2,
      autosize: { minRows: 2, maxRows: 2 },
      placeholder:"请填写提示词，如提取图片中的文字"
    }
  },
  {
    label: '解析结果',
    field: 'analysisResult',
    component: 'InputTextArea',
    componentProps: {
      row: 10,
      autosize: { minRows: 10, maxRows: 10 },
      readonly: true,
      placeholder:"解析结果将在这里显示"
    },
  },
];
