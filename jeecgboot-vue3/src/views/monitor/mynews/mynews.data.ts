import { BasicColumn, FormSchema } from '/@/components/Table';
import { render } from '/@/utils/common/renderUtils';

export const columns: BasicColumn[] = [
  {
    title: '标题',
    dataIndex: 'titile',
    width: 100,
    align: 'left',
  },
  {
    title: '消息类型',
    dataIndex: 'msgCategory',
    width: 80,
    customRender: ({ text }) => {
      return render.renderDictNative(
        text,
        [
          { label: '通知公告', value: '1', color: 'blue' },
          { label: '系统消息', value: '2' },
        ],
        true
      );
    },
  },
  {
    title: '发布人',
    dataIndex: 'sender',
    width: 80,
  },
  {
    title: '发布时间',
    dataIndex: 'sendTime',
    width: 80,
  },
  {
    title: '优先级',
    dataIndex: 'priority',
    width: 80,
    customRender: ({ text }) => {
      const color = text == 'L' ? 'blue' : text == 'M' ? 'yellow' : 'red';
      return render.renderTag(render.renderDict(text, 'priority'), color);
    },
  },
  {
    title: '阅读状态',
    dataIndex: 'readFlag',
    width: 80,
    customRender: ({ text }) => {
      return render.renderDictNative(
        text,
        [
          { label: '未读', value: '0', color: 'red' },
          { label: '已读', value: '1' },
        ],
        true
      );
    },
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'titile',
    label: '标题',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'sender',
    label: '发布人',
    component: 'Input',
    colProps: { span: 8 },
  },
];
