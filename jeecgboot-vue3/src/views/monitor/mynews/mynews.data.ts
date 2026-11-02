import { BasicColumn, FormSchema } from '/@/components/Table';
import { render } from '/@/utils/common/renderUtils';

/**
 * 发布人是否为 system：系统自动发送的消息，发布人存的是 system，不是用户账号
 * @author wangshuai
 * @since 2026-11-02 【LHZP-1240】【消息中心】我的消息 发布人未翻译
 */
export function isSystemSender(sender) {
  return sender === 'system';
}

/**
 * 发布人显示文本：system 默认显示为「系统」，其他发布人显示 text（姓名或原值）
 * 按发布人原值判断，就算有叫 system 的账号也不会显示成他的姓名；列表和详情弹窗共用
 * @author wangshuai
 * @since 2026-11-02 【LHZP-1240】【消息中心】我的消息 发布人未翻译
 */
export function getSenderText(sender, text) {
  if (isSystemSender(sender)) {
    return '系统';
  }
  return text;
}

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
    // 账号显示后端翻译的姓名，流程通知等非账号的值原样显示；当前页没有任何发布人匹配到账号时后端不返回 sender_dictText，回退显示原值
    customRender: ({ record }) => getSenderText(record.sender, record.sender_dictText || record.sender),
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
    colProps: { span: 6 },
  },
  {
    field: 'sender',
    label: '发布人',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'sendTime',
    label: '发布时间',
    component: 'RangeDate',
    componentProps: {
      valueType: 'Date',
    },
    colProps: { span: 6 },
  },
  {
    field: 'msgCategory',
    label: '消息类型',
    component: 'Select',
    componentProps: {
      options: [
        { label: '通知公告', value: '1' },
        { label: '系统消息', value: '2' },
        { label: '日程计划', value: 'plan' },
        { label: '流程消息', value: 'flow' },
        { label: '会议', value: 'meeting' },
        { label: '知识库', value: 'file' },
        { label: '协同通知', value: 'collab' },
        { label: '督办通知', value: 'supe' },
      ],
    },
    colProps: { span: 6 },
  },
];
