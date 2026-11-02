import { BasicColumn, FormSchema } from '/@/components/Table';
// @ts-ignore
import {getButtonIconRender} from "./button.data.tsx";

export const columns: BasicColumn[] = [
  { title: '按钮编码', align: 'center', dataIndex: 'buttonCode' },
  { title: '按钮名称', align: 'center', dataIndex: 'buttonName' },
  {
    title: '按钮样式',
    align: 'center',
    dataIndex: 'buttonStyle',
    customRender({ text, record }) {
      if (text === 'form') {
        let p = record.optPosition;
        return text + '(' + (p == '2' ? '底部' : '侧面') + ')';
      } else {
        return text;
      }
    },
  },
  { title: '按钮类型', align: 'center', dataIndex: 'optType' },
  { title: '排序', align: 'center', dataIndex: 'orderNum' },
  {
    title: '按钮图标',
    align: 'center',
    dataIndex: 'buttonIcon',
    customRender: ({text, record}) => {
      return getButtonIconRender({text, buttonCode: record.buttonCode});
    },
  },
  { title: '表达式', align: 'center', dataIndex: 'exp' },
  {
    title: '按钮状态',
    align: 'center',
    dataIndex: 'buttonStatus',
    customRender({ text }) {
      if (text == 1) {
        return '激活';
      } else {
        return '未激活';
      }
    },
  },
];

export const formSchemas = ({ redoModalHeight }): FormSchema[] => {
  return [
    {
      label: '按钮编码',
      field: 'buttonCode',
      component: 'Input',
      required: true,
      // update-begin--author:liaozhiyang---date:20240521---for：【TV360X-139】按钮编码加上正则校验
      dynamicRules: () => {
        return [
          {
            validator: (_, value) => {
              //需要return 一个Promise对象
              return new Promise((resolve, reject) => {
                const reg = /^[a-zA-Z_$][a-zA-Z0-9_$]*$/;
                if (reg.test(value)) {
                  resolve();
                } else {
                  reject('编码只能包含字母、数字、下划线 (_) 和美元符号 ($)且不能以数字开头');
                }
              });
            },
          },
          // update-begin--author:liaozhiyang---date:20240701---for：【TV360X-1693】自定义按钮编码排除sql和java系统内置编码
          {
            validator: (_, value) => {
              //需要return 一个Promise对象
              return new Promise((resolve, reject) => {
                const exclude = ['add', 'edit', 'detail', 'delete', 'batch_delete', 'import', 'export', 'query', 'reset', 'bpm', 'super_query', 'form_confirm'];
                if (exclude.includes(value)) {
                  reject('不可使用内置按钮编码，请在“管理内置按钮”中修改内置按钮');
                } else {
                  resolve();
                }
              });
            },
          },
          // update-end--author:liaozhiyang---date:20240701---for：【TV360X-1693】自定义按钮编码排除sql和java系统内置编码
        ];
      },
      // update-end--author:liaozhiyang---date:20240521---for：【TV360X-139】按钮编码加上正则校验
    },
    {
      label: '按钮名称',
      field: 'buttonName',
      component: 'Input',
      required: true,
    },
    {
      label: '按钮样式',
      field: 'buttonStyle',
      component: 'Select',
      componentProps: {
        options: [
          { label: 'Link', value: 'link' },
          { label: 'Button', value: 'button' },
          { label: 'Form', value: 'form' },
        ],
        // update-begin--author:liaozhiyang---date:20240618---for：【TV360X-1306】自定义按钮弹窗按钮样式切换是重置弹窗高度
        onChange: () => {
          redoModalHeight();
        },
        // update-end--author:liaozhiyang---date:20240618---for：【TV360X-1306】自定义按钮弹窗按钮样式切换是重置弹窗高度
      },
      defaultValue: 'link',
    },
    {
      label: '按钮位置',
      field: 'optPosition',
      component: 'Select',
      componentProps: {
        allowClear: false,
        options: [
          // { label: '侧面', value: '1' },
          { label: '底部', value: '2' },
        ],
      },
      defaultValue: '2',
      show: ({ model }) => model.buttonStyle === 'form',
    },
    {
      label: '按钮类型',
      field: 'optType',
      component: 'Select',
      componentProps: {
        allowClear: false,
        options: [
          { label: 'Js', value: 'js' },
          { label: 'Action', value: 'action' },
        ],
      },
      defaultValue: 'js',
    },
    {
      label: '排序',
      field: 'orderNum',
      component: 'InputNumber',
      componentProps: {
        style: 'width: 100%',
      },
    },
    {
      label: '按钮图标',
      field: 'buttonIcon',
      // update-begin--author:liaozhiyang---date:20240528---for：【TV360X-136】按钮图标改成图标组件选择
      component: 'IconPicker',
      componentProps: {
        clearSelect: true,
        iconPrefixSave: false,
      },
      // update-end--author:liaozhiyang---date:20240528---for：【TV360X-136】按钮图标改成图标组件选择
      ifShow: ({ values, model }) => {
        if (values.buttonStyle == 'button' || values.buttonStyle == 'form') {
          return true;
        } else {
          // model.buttonIcon = null;
          return false;
        }
      },
    },
    {
      label: '表达式',
      field: 'exp',
      component: 'Input',
      // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-89】自定义按钮样式是link时，展示表达式配置
      ifShow: ({ values, model }) => {
        if (values.buttonStyle == 'link') {
          return true;
        } else {
          model.exp = '';
          return false;
        }
      },
      // update-end--author:liaozhiyang---date:20240603---for：【TV360X-89】自定义按钮样式是link时，展示表达式配置
    },
    {
      label: '按钮状态',
      field: 'buttonStatus',
      component: 'RadioButtonGroup',
      componentProps: {
        options: [
          { label: '激活', value: '1' },
          { label: '未激活', value: '0' },
        ],
      },
      defaultValue: '1',
    },
  ]
};
