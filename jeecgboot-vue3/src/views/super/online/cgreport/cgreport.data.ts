import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { JVxeTypes, JVxeColumn } from '/@/components/jeecg/JVxeTable/types';
import { duplicateCheckDelay } from '/@/views/system/user/user.api';
import { getDataSourceList } from './cgreport.api';
import {usePermissionStore} from "/@/store/modules/permission";
const permissionStore = usePermissionStore();
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '报表名字',
    align: 'center',
    dataIndex: 'name',
    width: 120,
  },
  {
    title: '报表编码',
    align: 'center',
    dataIndex: 'code',
    width: 120,
  },
  {
    title: '报表SQL',
    align: 'center',
    dataIndex: 'cgrSql',
    width: 360,
  },
  {
    title: '数据源',
    align: 'center',
    dataIndex: 'dbSource',
    customRender: ({ text, record }) => {
      return record["dbSource_dictText"] ? record["dbSource_dictText"] : text
    },
    width: 120,
  },
  {
    title: '创建时间',
    align: 'center',
    dataIndex: 'createTime',
    width: 120,
  },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
  {
    label: '报表名称',
    field: 'name',
    component: 'JInput',
  },
  {
    label: '报表编码',
    field: 'code',
    component: 'JInput',
  },
];

// 编码校验 仅online报表用
const codePattern = /^[a-z|A-Z][a-z|A-Z|\d|_|-]{0,}$/;
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    label: '报表编码',
    field: 'code',
    component: 'Input',
    colProps: {
      sm: 24,
      xs: 24,
      md: 12,
      lg: 8,
      xl: 8,
      xxl: 8,
    },
    dynamicRules: ({ values, model }) => {
      console.log('values:', values);
      return [
        {
          required: true,
          validator: (_, value) => {
            return new Promise((resolve, reject) => {
              if (!value) {
                return reject('请输入报表编码！');
              }
              if (!codePattern.test(value)) {
                return reject('编码必须以字母开头，可包含数字、下划线、横杠！');
              }
              let params = {
                tableName: 'onl_cgreport_head',
                fieldName: 'code',
                fieldVal: value,
                dataId: model.id,
              };
              duplicateCheckDelay(params)
                .then((res) => {
                  res.success ? resolve() : reject('报表编码已存在!');
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
    label: '报表名字',
    field: 'name',
    component: 'Input',
    colProps: {
      sm: 24,
      xs: 24,
      md: 12,
      lg: 8,
      xl: 8,
      xxl: 8,
    },
    dynamicRules: () => {
      return [{ required: true, message: '请输入报表名字!' }];
    },
  },
  {
    label: '动态数据源',
    field: 'dbSource',
    colProps: {
      sm: 24,
      xs: 24,
      md: 12,
      lg: 8,
      xl: 8,
      xxl: 8,
    },
    component: 'ApiSelect',
    rules: [{ required: permissionStore.sysSafeMode, message: '请选择数据源！' }],
    componentProps: {
      api: getDataSourceList,
    },
  },
  /*    {
        label: ' ',
        field: 'line1',
        component: 'Input',
        slot: 'line1',
        colProps: {
            span: 24
        },
        itemProps:{
            labelCol: { xs: 1, sm: 1 },
            wrapperCol: { xs: 23, sm: 23 },
            colon: false
        },
    },*/
  {
    label: '报表SQL',
    field: 'cgrSql',
    component: 'JCodeEditor',
    rules: [{ required: true, message: '请填写报表SQL' }],
    // update-begin--author:liaozhiyang---date:20240509---for：【QQYUN-9230】报表图表弹窗样式调整
    // itemProps: {
    //   labelCol: { xs: 24, sm: 4, md: 2, lg: 2, xl: 3, xxl: 2 },
    //   wrapperCol: { xs: { span: 24 }, sm: { span: 18 }, md: { span: 24 } },
    // },
    // update-end--author:liaozhiyang---date:20240509---for：【QQYUN-9230】报表图表弹窗样式调整
    componentProps: {
      height: '200px',
      fullScreen: true,
    },
    colProps: {
      sm: 24,
      xs: 24,
      md: 18,
      lg: 16,
      xl: 16,
      xxl: 16,
    },
  },
  {
    label: ' ',
    field: 'analyseButton',
    component: 'Input',
    slot: 'analyseButton',
    colProps: {
      xs: 24,
      sm: 24,
      md: 6,
      lg: 8,
      xl: 8,
      xxl: 8,
    },
    itemProps: {
      labelCol: { xs: 1, sm: 1 },
      wrapperCol: { xs: 23, sm: 23 },
      colon: false,
    },
  },
];
//子表表格配置
export const onlCgreportParamColumns: JVxeColumn[] = [
  {
    title: '参数字段',
    key: 'paramName',
    type: JVxeTypes.input,
    width: '150px',
    placeholder: '请输入${title}',
    defaultValue: '',
    validateRules: [{ required: true, message: '${title}不能为空' }],
  },
  {
    title: '参数文本',
    key: 'paramTxt',
    type: JVxeTypes.input,
    width: '150px',
    placeholder: '请输入${title}',
    defaultValue: '',
    validateRules: [{ required: true, message: '${title}不能为空' }],
  },
  {
    title: '默认值',
    key: 'paramValue',
    type: JVxeTypes.input,
    width: '150px',
    placeholder: '请输入${title}',
    defaultValue: '',
  },
];
export const onlCgreportItemColumns: JVxeColumn[] = [
  {
    title: '字段名字',
    key: 'fieldName',
    type: JVxeTypes.input,
    width: '160px',
    placeholder: '请输入${title}',
    defaultValue: '',
    validateRules: [{ required: true, message: '${title}不能为空' }],
  },
  {
    title: '字段文本',
    key: 'fieldTxt',
    type: JVxeTypes.input,
    width: '160px',
    placeholder: '请输入${title}',
    defaultValue: '',
    validateRules: [{ required: true, message: '${title}不能为空' }],
  },
  {
    title: '宽度',
    key: 'fieldWidth',
    type: JVxeTypes.input,
    width: '80px',
    defaultValue: '',
  },
  {
    title: '类型',
    key: 'fieldType',
    width: '120px',
    placeholder: '请输入${title}',
    defaultValue: '',
    validateRules: [{ required: true, message: '${title}不能为空' }],
    type: JVxeTypes.select,
    options: [
      { title: '数值类型', value: 'Integer' },
      { title: '字符类型', value: 'String' },
      { title: '日期类型', value: 'Date' },
      { title: '时间类型', value: 'Datetime' },
      { title: '长整型', value: 'Long' },
      { title: '图片类型', value: 'Image' },
    ],
  },
  {
    title: '列显示',
    key: 'isShow',
    width: '80px',
    align: 'center',
    type: JVxeTypes.checkbox,
    customValue: [1, 0],
    defaultChecked: true,
  },
  {
    title: '字段href',
    key: 'fieldHref',
    type: JVxeTypes.input,
    width: '120px',
    placeholder: '请输入${title}',
    defaultValue: '',
  },
  {
    title: '查询',
    key: 'isSearch',
    type: JVxeTypes.checkbox,
    customValue: ['1', '0'],
    width: '80px',
    align: 'center',
    defaultChecked: false,
  },
  {
    title: '查询模式',
    key: 'searchMode',
    type: JVxeTypes.select,
    width: '120px',
    placeholder: '请选择${title}',
    options: [
      { title: '单值查询', value: 'single' },
      { title: '范围查询', value: 'group' },
    ],
  },
  {
    title: '取值表达式',
    key: 'replaceVal',
    type: JVxeTypes.input,
    width: '120px',
    placeholder: '请输入${title}',
    defaultValue: '',
  },
  {
    title: '字典code',
    key: 'dictCode',
    type: JVxeTypes.input,
    width: '120px',
    titleSuffix: { content: '填写字典编码，或SQL：SELECT 字段 AS value, 字段 AS text FROM 表。人大金仓、PostgreSQL 下 text 是关键字，别名必须写 AS' },
    placeholder: '请输入${title}',
    defaultValue: '',
  },
  {
    title: '分组标题',
    key: 'groupTitle',
    type: JVxeTypes.input,
    width: '120px',
    placeholder: '请输入${title}',
    defaultValue: '',
  },
  {
    title: '合计列',
    align: 'center',
    key: 'isTotal',
    type: JVxeTypes.checkbox,
    customValue: ['1', '0'],
    width: '80px',
    defaultChecked: false,
  },
];
