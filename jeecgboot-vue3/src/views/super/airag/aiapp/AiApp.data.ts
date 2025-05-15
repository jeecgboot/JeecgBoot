import { FormSchema } from '@/components/Form';

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
    label: '应用名称',
    field: 'name',
    required: true,
    componentProps: {
      //是否展示字数
      showCount: true,
      maxlength: 64,
    },
    component: 'Input',
  },
  {
    label: '应用描述',
    field: 'descr',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '描述该应用的应用场景及用途',
      rows: 4,
      //是否展示字数
      showCount: true,
      maxlength: 256,
    },
  },
  {
    label: '应用图标',
    field: 'icon',
    component: 'JImageUpload',
  },
  {
    label: '选择应用类型',
    field: 'type',
    component: 'Input',
    ifShow:({ values })=>{
      return !values.id;
    },
    slot: 'typeSlot',
  },
];

/**
 * 快捷指令表单
 */
export const quickCommandFormSchema: FormSchema[] = [
  {
    label: 'key',
    field: 'key',
    component: 'Input',
    show: false,
  },
  {
    label: '按钮名称',
    field: 'name',
    required: true,
    component: 'Input',
    componentProps: {
      showCount: true,
      maxLength: 10,
    },
  },
  {
    label: '按钮图标',
    field: 'icon',
    component: 'IconPicker',
  },
  {
    label: '指令内容',
    field: 'descr',
    required: true,
    component: 'InputTextArea',
    componentProps: {
      autosize: { minRows: 4, maxRows: 4 },
      showCount: true,
      maxLength: 100,
    }
  },
];
