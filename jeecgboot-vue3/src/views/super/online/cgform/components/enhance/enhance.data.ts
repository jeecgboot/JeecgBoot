import { computed, ref, Ref } from 'vue';
import { BasicColumn, FormSchema } from '/@/components/Table';
import { onlineDefaultButton } from '../../cgform.data';
// update-begin--author:jeecg---date:20260513---for：【QQYUN-15337】online表单SQL增强支持指定数据源
import { initDictOptions } from '/@/utils/dict';
// update-end--author:jeecg---date:20260513---for：【QQYUN-15337】online表单SQL增强支持指定数据源
//只支持开始的增强按钮
const START_EVENT_ONLY_BUTTONS = ['import', 'export', 'query'];

export function useJavaColumns(btnList: Ref<any[]>) {
  let columns: BasicColumn[] = [
    {
      title: '页面按钮',
      align: 'center',
      dataIndex: 'buttonCode',
      customRender: ({ text }) => renderButtonText(text, btnList.value),
    },
    {
      title: '事件状态',
      align: 'center',
      dataIndex: 'event',
      customRender: ({ text }) => (text == 'start' ? '开始' : '结束'),
    },
    {
      title: '类型',
      align: 'center',
      dataIndex: 'cgJavaType',
      customRender: ({ text }) => {
        if (text == 'spring') {
          return 'spring-key';
        } else if (text === 'class') {
          return 'java-class';
        } else if (text === 'http') {
          return 'http-api';
        } else {
          return text;
        }
      },
    },
    {
      title: '内容',
      align: 'center',
      dataIndex: 'cgJavaValue',
    },
    {
      title: '是否生效',
      align: 'center',
      dataIndex: 'activeStatus',
      customRender: ({ text }) => {
        if (text == '1') {
          return '有效';
        } else {
          return '无效';
        }
      },
    },
  ];
  return { columns };
}

export function useJavaFormSchemas(btnList: Ref<any[]>) {
  const formSchemas = computed<FormSchema[]>(() => {
    return [
      {
        label: '页面按钮',
        field: 'buttonCode',
        component: 'Select',
        componentProps: {
          options: [
            { label: '新增', value: 'add' },
            { label: '编辑', value: 'edit' },
            { label: '删除', value: 'delete' },
            { label: '导入', value: 'import' },
            { label: '导出', value: 'export' },
            { label: '查询', value: 'query' },
            ...btnList.value.map((item) => ({ label: item.buttonName, value: item.buttonCode })),
          ],
        },
        defaultValue: 'add',
      },
      {
        label: '事件状态',
        field: 'event',
        component: 'RadioButtonGroup',
        componentProps: {
          options: [
            { label: '开始', value: 'start' },
            { label: '结束', value: 'end' },
          ],
        },
        defaultValue: 'end',
        ifShow: ({ values, model }) => {
          const isStartEventOnly = START_EVENT_ONLY_BUTTONS.includes(values.buttonCode);
          if (isStartEventOnly) {
            model.event = 'start';
          }
          return !isStartEventOnly;
        },
      },
      {
        label: '类型',
        field: 'cgJavaType',
        component: 'RadioButtonGroup',
        componentProps: {
          options: [
            { label: 'spring-key', value: 'spring' },
            { label: 'java-class', value: 'class' },
            { label: 'http-api', value: 'http' },
          ],
        },
        defaultValue: 'spring',
      },
      {
        label: '内容',
        field: 'cgJavaValue',
        component: 'Input',
        required: true,
      },
      {
        label: '是否生效',
        field: 'activeStatus',
        component: 'RadioButtonGroup',
        componentProps: {
          options: [
            { label: '有效', value: '1' },
            { label: '无效', value: '0' },
          ],
        },
        defaultValue: '1',
      },
    ];
  });

  return { formSchemas };
}

export function useSqlColumns(btnList: Ref<any[]>) {
  let columns: BasicColumn[] = [
    {
      title: '页面按钮',
      align: 'center',
      dataIndex: 'buttonCode',
      customRender: ({ text }) => renderButtonText(text, btnList.value),
    },
    // update-begin--author:jeecg---date:20260513---for：【QQYUN-15337】online表单SQL增强支持指定数据源
    {
      title: '数据源',
      align: 'center',
      dataIndex: 'dbSource',
      width: 140,
      customRender: ({ text }) => (!text ? '跟随表单数据源' : text === 'master' ? '主库' : text),
    },
    // update-end--author:jeecg---date:20260513---for：【QQYUN-15337】online表单SQL增强支持指定数据源
    {
      title: '增强SQL',
      align: 'center',
      dataIndex: 'cgbSql',
      ellipsis: true,
    },
  ];
  return { columns };
}

export function useSqlFormSchemas(btnList: Ref<any[]>) {
  // update-begin--author:jeecg---date:20260513---for：【QQYUN-15337】online表单SQL增强支持指定数据源
  // 数据源下拉：跟随表单数据源（值为空）/ 主库（值为 master）/ 各多数据源（值为 sys_data_source.code）
  const FOLLOW_FORM_DS = { label: '跟随表单数据源（默认）', value: '' };
  const MASTER_DS = { label: '主库 / 默认数据源', value: 'master' };
  const dbSourceOptions = ref<any[]>([FOLLOW_FORM_DS, MASTER_DS]);
  initDictOptions('sys_data_source,name,code')
    .then((res) => {
      const list = (res || []).map((d: any) => ({ label: d.label ?? d.text ?? d.title, value: d.value }));
      dbSourceOptions.value = [FOLLOW_FORM_DS, MASTER_DS, ...list];
    })
    .catch(() => {});
  // update-end--author:jeecg---date:20260513---for：【QQYUN-15337】online表单SQL增强支持指定数据源
  const formSchemas = computed<FormSchema[]>(() => {
    return [
      {
        label: '页面按钮',
        field: 'buttonCode',
        component: 'Select',
        componentProps: {
          allowClear: false,
          options: [
            { label: '新增', value: 'add' },
            { label: '编辑', value: 'edit' },
            { label: '删除', value: 'delete' },
            ...btnList.value.map((item) => ({ label: item.buttonName, value: item.buttonCode })),
          ],
        },
        defaultValue: 'add',
      },
      {
        label: '增强SQL',
        field: 'cgbSql',
        component: 'JCodeEditor',
        componentProps: {
          language: 'sql',
          placeholder: '请输入SQL语句',
          languageChange: false,
          lineNumbers: false,
          fullScreen: true,
          height: '320px',
        },
        defaultValue: '',
      },
      // update-begin--author:jeecg---date:20260513---for：【QQYUN-15337】online表单SQL增强支持指定数据源
      {
        label: '数据源',
        field: 'dbSource',
        component: 'Select',
        defaultValue: '',
        componentProps: {
          options: dbSourceOptions.value,
          allowClear: false,
        },
        helpMessage: [
          '增强SQL在哪个库执行：',
          '· 跟随表单数据源（默认）：跟着该表单配置的数据源走（表单也没配则走主库）',
          '· 主库 / 默认数据源：增强SQL要操作 sys_* 等主库里的表时选它',
          '· 指定多数据源：到选中的那个数据源执行',
        ],
      },
      // update-end--author:jeecg---date:20260513---for：【QQYUN-15337】online表单SQL增强支持指定数据源
      {
        label: '描述',
        field: 'content',
        component: 'InputTextArea',
        defaultValue: '',
      },
    ];
  });

  return { formSchemas };
}

function renderButtonText(text: string, btnList: any[]) {
  let str = '';
  for (let item of onlineDefaultButton) {
    if (item.code === text) {
      str = item.title;
      break;
    }
  }
  if (!str) {
    for (let item of btnList) {
      if (item.buttonCode === text) {
        str = item.buttonName;
        break;
      }
    }
  }
  return str || text;
}
