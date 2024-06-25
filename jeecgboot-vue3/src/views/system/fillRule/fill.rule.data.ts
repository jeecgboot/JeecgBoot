import { BasicColumn, FormSchema } from '/@/components/Table';
import { duplicateCheckDelay } from '/@/views/system/user/user.api';

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
    title: '规则实现类',
    dataIndex: 'ruleClass',
    width: 300,
    align: 'center',
  },
  {
    title: '规则参数',
    dataIndex: 'ruleParams',
    width: 200,
    align: 'center',
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
                tableName: 'sys_fill_rule',
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
    field: 'ruleClass',
    label: '规则实现类',
    component: 'Input',
    required: true,
    colProps: { span: 24 },
  },
  {
    field: 'ruleParams',
    label: '规则参数',
    colProps: { span: 24 },
    component: 'JAddInput',
    componentProps: {
      min: 0,
    },
  },
];
