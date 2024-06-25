import { BasicColumn, FormSchema } from '/@/components/Table';
import { render } from '/@/utils/common/renderUtils';
import { duplicateCheckDelay } from '/@/views/system/user/user.api';
import { validateCheckRule } from '/@/views/system/checkRule/check.rule.api';
import { array } from 'vue-types';

export const columns: BasicColumn[] = [
  {
    title: '规则名称',
    dataIndex: 'ruleName',
    width: 200,
    align: 'center',
  },
  {
    title: '规则编码',
    dataIndex: 'ruleCode',
    width: 200,
    align: 'center',
  },
  {
    title: '规则描述',
    dataIndex: 'ruleDescription',
    width: 300,
    align: 'center',
    customRender: function ({ text }) {
      return render.renderTip(text, 30);
    },
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'ruleName',
    label: '规则名称',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'ruleCode',
    label: '规则编码',
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
    field: 'ruleName',
    label: '规则名称',
    component: 'Input',
    required: true,
    colProps: { span: 24 },
  },
  {
    field: 'ruleCode',
    label: '规则编码',
    component: 'Input',
    colProps: { span: 24 },
    dynamicDisabled: ({ values }) => {
      return !!values.id;
    },
    dynamicRules: ({ model }) => {
      return [
        {
          required: true,
          validator: (_, value) => {
            return new Promise((resolve, reject) => {
              if (!value) {
                return reject('请输入规则编码！');
              }
              let params = {
                tableName: 'sys_check_rule',
                fieldName: 'rule_code',
                fieldVal: value,
                dataId: model.id,
              };
              duplicateCheckDelay(params)
                .then((res) => {
                  res.success ? resolve() : reject('规则编码已存在!');
                })
                .catch((err) => {
                  reject(err.message || '校验失败');
                });
            });
          },
        },
      ];
    },
  },
  {
    field: 'ruleDescription',
    label: '规则描述',
    colProps: { span: 24 },
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入规则描述',
      rows: 2,
    },
  },
];

export const checkRuleInput: FormSchema[] = [
  {
    label: '123',
    field: 'ruleCode',
    component: 'Input',
    show: false,
  },
  {
    field: 'testValue',
    label: '需要测试的值:',
    component: 'Input',
    componentProps: ({ formModel }) => {
      return {
        onChange: (e) => {
          formModel.testValue = e.target.value;
        },
      };
    },
    dynamicRules: ({ model }) => {
      const { ruleCode } = model;
      return [
        {
          required: false,
          validator: (_, value) => {
            return new Promise((resolve, reject) => {
              if (ruleCode && value) {
                /*console.log({ruleCode,value})*/
                validateCheckRule(ruleCode, value)
                  .then((res) => {
                    //console.log(1233, res)
                    res['success'] ? resolve() : reject(res['message']);
                  })
                  .catch((err) => {
                    reject(err.message || err);
                  });
              } else {
                resolve();
              }
            });
          },
        },
      ];
    },
  },
];
