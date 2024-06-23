export interface ListItem {
  id: string;
  avatar: string;
  // 通知的标题内容
  title: string;
  // 是否在标题上显示删除线
  titleDelete?: boolean;
  datetime: string;
  type: string;
  read?: boolean;
  description: string;
  clickClose?: boolean;
  extra?: string;
  color?: string;
  // 优先级
  priority?: string;
}

export enum PriorityTypes {
  // 低优先级，一般消息
  L = 'L',
  // 中优先级，重要消息
  M = 'M',
  // 高优先级，紧急消息
  H = 'H',
}

export interface TabItem {
  key: string;
  name: string;
  list: ListItem[];
  unreadlist?: ListItem[];
  count: number;
}

export const tabListData: TabItem[] = [
  {
    key: '1',
    name: '通知',
    list: [
      {
        id: '000000001',
        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png',
        title: '你收到了 14 份新周报',
        description: '',
        datetime: '2017-08-09',
        type: '1',
      },
      {
        id: '000000002',
        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/OKJXDXrmkNshAMvwtvhu.png',
        title: '你推荐的 曲妮妮 已通过第三轮面试',
        description: '',
        datetime: '2017-08-08',
        type: '1',
      },
      {
        id: '000000003',
        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/kISTdvpyTAhtGxpovNWd.png',
        title: '这种模板可以区分多种通知类型',
        description: '',
        datetime: '2017-08-07',
        // read: true,
        type: '1',
      },
      {
        id: '000000004',
        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png',
        title: '左侧图标用于区分不同的类型',
        description: '',
        datetime: '2017-08-07',
        type: '1',
      },
      {
        id: '000000005',
        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png',
        title: '标题可以设置自动显示省略号，本例中标题行数已设为1行，如果内容超过1行将自动截断并支持tooltip显示完整标题。',
        description: '',
        datetime: '2017-08-07',
        type: '1',
      },
      {
        id: '000000006',
        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png',
        title: '左侧图标用于区分不同的类型',
        description: '',
        datetime: '2017-08-07',
        type: '1',
      },
      {
        id: '000000007',
        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png',
        title: '左侧图标用于区分不同的类型',
        description: '',
        datetime: '2017-08-07',
        type: '1',
      },
      {
        id: '000000008',
        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png',
        title: '左侧图标用于区分不同的类型',
        description: '',
        datetime: '2017-08-07',
        type: '1',
      },
      {
        id: '000000009',
        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png',
        title: '左侧图标用于区分不同的类型',
        description: '',
        datetime: '2017-08-07',
        type: '1',
      },
      {
        id: '000000010',
        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png',
        title: '左侧图标用于区分不同的类型',
        description: '',
        datetime: '2017-08-07',
        type: '1',
      },
    ],
    count: 0,
  },
  {
    key: '2',
    name: '系统消息',
    list: [
      {
        id: '000000006',
        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/fcHMVNCjPOsbUGdEduuv.jpeg',
        title: '曲丽丽 评论了你',
        description: '描述信息描述信息描述信息',
        datetime: '2017-08-07',
        type: '2',
        clickClose: true,
      },
      {
        id: '000000007',
        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/fcHMVNCjPOsbUGdEduuv.jpeg',
        title: '朱偏右 回复了你',
        description: '这种模板用于提醒谁与你发生了互动',
        datetime: '2017-08-07',
        type: '2',
        clickClose: true,
      },
      {
        id: '000000008',
        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/fcHMVNCjPOsbUGdEduuv.jpeg',
        title: '标题',
        description:
          '请将鼠标移动到此处，以便测试超长的消息在此处将如何处理。本例中设置的描述最大行数为2，超过2行的描述内容将被省略并且可以通过tooltip查看完整内容',
        datetime: '2017-08-07',
        type: '2',
        clickClose: true,
      },
    ],
    count: 0,
  },
  // {
  //   key: '3',
  //   name: '待办',
  //   list: [
  //     {
  //       id: '000000009',
  //       avatar: '',
  //       title: '任务名称',
  //       description: '任务需要在 2017-01-12 20:00 前启动',
  //       datetime: '',
  //       extra: '未开始',
  //       color: '',
  //       type: '3',
  //     },
  //     {
  //       id: '000000010',
  //       avatar: '',
  //       title: '第三方紧急代码变更',
  //       description: '冠霖 需在 2017-01-07 前完成代码变更任务',
  //       datetime: '',
  //       extra: '马上到期',
  //       color: 'red',
  //       type: '3',
  //     },
  //     {
  //       id: '000000011',
  //       avatar: '',
  //       title: '信息安全考试',
  //       description: '指派竹尔于 2017-01-09 前完成更新并发布',
  //       datetime: '',
  //       extra: '已耗时 8 天',
  //       color: 'gold',
  //       type: '3',
  //     },
  //     {
  //       id: '000000012',
  //       avatar: '',
  //       title: 'ABCD 版本发布',
  //       description: '指派竹尔于 2017-01-09 前完成更新并发布',
  //       datetime: '',
  //       extra: '进行中',
  //       color: 'blue',
  //       type: '3',
  //     },
  //   ],
  // },
];
