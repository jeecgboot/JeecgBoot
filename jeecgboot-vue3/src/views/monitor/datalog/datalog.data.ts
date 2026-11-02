import { BasicColumn, FormSchema } from '/@/components/Table';
import { h } from 'vue';
import { Tag, Tooltip } from 'ant-design-vue';

export const columns: BasicColumn[] = [
  {
    title: '表名',
    dataIndex: 'dataTable',
    width: 120,
    align: 'left',
    customRender: ({ text }) => {
      return h(Tag, { color: 'blue' }, () => text);
    },
  },
  {
    title: '数据ID',
    dataIndex: 'dataId',
    width: 260,
    align: 'left',
    ellipsis: true,
    customRender: ({ text }) => {
      return h(
        'span',
        { style: 'font-family: Consolas, Monaco, monospace; font-size: 12px; color: #595959' },
        text
      );
    },
  },
  {
    title: '版本号',
    dataIndex: 'dataVersion',
    width: 70,
    align: 'center',
    customRender: ({ text }) => {
      return h(Tag, { color: 'green' }, () => 'V' + text);
    },
  },
  {
    title: '数据内容',
    dataIndex: 'dataContent',
    ellipsis: true,
    customRender: ({ text }) => {
      if (!text) return '--';
      // 尝试格式化 JSON 显示关键字段
      try {
        const obj = JSON.parse(text);
        const keys = Object.keys(obj);
        const preview = keys
          .slice(0, 3)
          .map((k) => {
            const v = obj[k];
            const val = v === null || v === undefined || v === '' ? '--' : String(v);
            return `${k}: ${val.length > 20 ? val.substring(0, 20) + '...' : val}`;
          })
          .join(' | ');
        const suffix = keys.length > 3 ? ` (+${keys.length - 3} 字段)` : '';
        return h(
          Tooltip,
          { title: JSON.stringify(obj, null, 2), overlayStyle: { maxWidth: '500px', whiteSpace: 'pre-wrap', fontFamily: 'Consolas, monospace', fontSize: '12px' } },
          () => h('span', { style: 'font-size: 12px; color: #595959' }, preview + suffix)
        );
      } catch {
        return text;
      }
    },
  },
  {
    title: '创建人',
    dataIndex: 'createBy',
    sorter: true,
    width: 90,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
    sorter: true,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'dataTable',
    label: '表名',
    component: 'JInput',
    componentProps: {
      placeholder: '请输入表名',
    },
    colProps: { span: 6 },
  },
  {
    field: 'dataId',
    label: '数据ID',
    component: 'JInput',
    componentProps: {
      placeholder: '请输入数据ID',
    },
    colProps: { span: 6 },
  },
  {
    field: 'createBy',
    label: '创建人',
    component: 'JSelectUserByDepartment',
    componentProps: {
      rowKey: 'username',
      labelKey: 'realname',
      showButton: false,
      isRadioSelection: true,
    },
    colProps: { span: 6 },
  },
];
