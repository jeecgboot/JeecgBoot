import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { dictItemCheck } from './dict.api';
import { rules } from '/@/utils/helper/validator';
import { h } from "vue";

export const columns: BasicColumn[] = [
  {
    title: '字典名称',
    dataIndex: 'dictName',
    width: 240,
  },
  {
    title: '字典编码',
    dataIndex: 'dictCode',
    width: 240,
  },
  {
    title: '描述',
    dataIndex: 'description',
    // width: 120
  },
];

export const recycleBincolumns: BasicColumn[] = [
  {
    title: '字典名称',
    dataIndex: 'dictName',
    width: 120,
  },
  {
    title: '字典编码',
    dataIndex: 'dictCode',
    width: 120,
  },
  {
    title: '描述',
    dataIndex: 'description',
    width: 120,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    label: '字典名称',
    field: 'dictName',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '字典编码',
    field: 'dictCode',
    component: 'Input',
    colProps: { span: 6 },
  },
];

export const formSchema: FormSchema[] = [
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    label: '字典名称',
    field: 'dictName',
    required: true,
    component: 'Input',
  },
  {
    label: '字典编码',
    field: 'dictCode',
    component: 'Input',
    dynamicDisabled: ({ values }) => {
      return !!values.id;
    },
    dynamicRules: ({ model, schema }) => rules.duplicateCheckRule('sys_dict', 'dict_code', model, schema, true),
  },
  {
    label: '描述',
    field: 'description',
    component: 'Input',
  },
];

export const dictItemColumns: BasicColumn[] = [
  {
    title: '名称',
    dataIndex: 'itemText',
    width: 80,
  },
  {
    title: '数据值',
    dataIndex: 'itemValue',
    width: 80,
  },
  {
    title: '字典颜色',
    dataIndex: 'itemColor',
    width: 80,
    align:'center',
    customRender:({ text }) => {
      return h('div', {
        style: {"background": text, "width":"18px","height":"18px","border-radius":"50%","margin":"0 auto"}
      })
    }
  },
];

export const dictItemSearchFormSchema: FormSchema[] = [
  {
    label: '名称',
    field: 'itemText',
    component: 'Input',
  },
  {
    label: '状态',
    field: 'status',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'dict_item_status',
      stringToNumber: true,
    },
  },
];

export const itemFormSchema: FormSchema[] = [
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    label: '名称',
    field: 'itemText',
    required: true,
    component: 'Input',
  },
  {
    label: '数据值',
    field: 'itemValue',
    component: 'Input',
    dynamicRules: ({ values, model }) => {
      return [
        {
          required: true,
          validator: (_, value) => {
            if (!value) {
              return Promise.reject('请输入数据值');
            }
            if (new RegExp("[`~!@#$^&*()=|{}'.<>《》/?！￥（）—【】‘；：”“。，、？]").test(value)) {
              return Promise.reject('数据值不能包含特殊字符！');
            }
            return new Promise<void>((resolve, reject) => {
              let params = {
                dictId: values.dictId,
                id: model.id,
                itemValue: value,
              };
              dictItemCheck(params)
                .then((res) => {
                  res.success ? resolve() : reject(res.message || '校验失败');
                })
                .catch((err) => {
                  reject(err.message || '验证失败');
                });
            });
          },
        },
      ];
    },
  },
  {
    label: '颜色值',
    field: 'itemColor',
    component: 'Input',
    slot:'itemColor'
  },
  {
    label: '描述',
    field: 'description',
    component: 'Input',
  },
  {
    field: 'sortOrder',
    label: '排序',
    component: 'InputNumber',
    defaultValue: 1,
  },
  {
    field: 'status',
    label: '是否启用',
    defaultValue: 1,
    component: 'JDictSelectTag',
    componentProps: {
      type: 'radioButton',
      dictCode: 'dict_item_status',
      stringToNumber: true,
    },
  },
];
