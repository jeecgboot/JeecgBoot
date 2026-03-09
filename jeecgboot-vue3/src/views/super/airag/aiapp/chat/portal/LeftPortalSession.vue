<!-- 左侧应用门户会话设置 -->
<template>
  <div class="space-page">
    <div class="header">
      <img class="header-image" :src="loginLogo" />
      <div class="header-name"> JEECG </div>
    </div>
    <div class="new-session" @click="handleNewSession">
      <div class="left-box">
        <div class="app-icon">
          <Icon icon="ant-design:edit-outlined" size="14"></Icon>
        </div>
        <div class="app-name">新对话</div>
      </div>
    </div>
    <div class="session-scroll">
      <div class="section">
        <template v-for="app in apps">
          <div class="app-item" :class="activeKey === app.id ? 'active' : ''" @click="handleAppClick(app)">
            <div class="app-icon">
              <img :src="getAiImg(app)" />
            </div>
            <div class="app-name" :title="app.name">{{ app.name }}</div>
          </div>
        </template>
      </div>

      <div class="section">
        <div class="section-title">历史对话</div>
        <div v-if="tasks.length" class="task-list">
          <div v-for="task in tasks" :key="task.id" class="task-item" :class="activeKey === task.id ? 'active' : ''" @click="handleTaskClick(task)">
            <div class="task-title" :title="task.title" v-if="!task.isEdit">{{ task.title }}</div>
            <a-input
              class="title"
              ref="inputRef"
              v-if="task.isEdit"
              :defaultValue="task.title"
              placeholder="请输入标题"
              @change="handleInputChange"
              @keyup.enter="inputBlur(task)"
            />
            <div class="icon-edit">
              <a-space>
                <span class="icon edit" @click.prevent.stop="handleEdit(task)" v-if="!task.isEdit">
                  <svg xmlns="http://www.w3.org/2000/svg" role="img" class="iconify iconify--ri" width="1em" height="1em" viewBox="0 0 24 24">
                    <path
                      fill="currentColor"
                      d="M6.414 15.89L16.556 5.748l-1.414-1.414L5 14.476v1.414zm.829 2H3v-4.243L14.435 2.212a1 1 0 0 1 1.414 0l2.829 2.829a1 1 0 0 1 0 1.414zM3 19.89h18v2H3z"
                    ></path>
                  </svg>
                </span>
                <span class="icon del">
                  <a-popconfirm
                    :overlayStyle="{ 'z-index': 9999 }"
                    title="确定删除此记录？"
                    placement="bottom"
                    ok-text="确定"
                    cancel-text="取消"
                    @confirm.prevent.stop="handleDel(task)"
                  >
                    <svg xmlns="http://www.w3.org/2000/svg" role="img" class="iconify iconify--ri" width="1em" height="1em" viewBox="0 0 24 24">
                      <path
                        fill="currentColor"
                        d="M17 6h5v2h-2v13a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V8H2V6h5V3a1 1 0 0 1 1-1h8a1 1 0 0 1 1 1zm1 2H6v12h12zm-9 3h2v6H9zm4 0h2v6h-2zM9 4v2h6V4z"
                      ></path>
                    </svg>
                  </a-popconfirm>
                </span>
              </a-space>
            </div>
          </div>
        </div>
        <a-empty v-else description="暂无历史对话" />
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { onMounted, ref } from 'vue';
  import { getFileAccessHttpUrl } from '@/utils/common/compUtils';
  import defaultImg from '@/views/super/airag/aiapp/img/ailogo.png';
  import { defHttp } from '@/utils/http/axios';
  import loginLogo from '/@/assets/images/logo.png';

  const emit = defineEmits(['register', 'app-click', 'task-click', 'new-session-click']);

  const defaultApp = ref({
    id: '1999373661846880258',
    name: '聊天助手',
  });
  //应用列表
  const apps = ref([
    {
      id: '1998717610730352641',
      name: '帮我写作',
      icon: 'https://jeecgdev.oss-cn-beijing.aliyuncs.com/upload/test/helpWriting_1765520898059.png',
      prologue: '请输入\n出发地：\n目的地：\n人数：',
    },
    {
      id: '1996471445272088578',
      name: '图像识别',
      icon: 'https://jeecgdev.oss-cn-beijing.aliyuncs.com/temp/1dataOCR_1743065089791.png',
      prologue: '上传一张图片，我来为你识别图片的内容',
    },
    {
      id: '1902262577996546050',
      name: '看图说话',
      icon: 'https://jeecgdev.oss-cn-beijing.aliyuncs.com/temp/工具-图片解析_1743065064801.png',
      prologue: '上传一张图片，我来为你讲述图片中的故事',
    },    
    {
      id: '2008448202536456193',
      name: 'Chat2BI',
      icon: 'https://minio.jeecg.com/otatest/chatShow_1769395642452.png',
      prologue: '你好，我是Chat2BI 图表生成智能体。',
      flowId: '2008379264947519489',
      type: 'chatFLow',
      presetQuestion: '[{"key":1,"descr":"请统计系统用户的性别分布比例，并以饼状图和列表表格展示。","update":true}]'
    },    
    {
      id: '2008090512835629057',
      name: 'AI绘画',
      icon: 'https://minio.jeecg.com/otatest/AiWrite_1769395779558.png',
      prologue: '你好，我是 AI绘图智能体。',
      presetQuestion: '[{"key":1,"descr":"请生成一张具有日本风格的动漫成年女孩。","update":true}, {"key":2,"descr":"请生成一幅中国神话故事中，手持武器的哪吒形象。","update":true}]',
      metadata:"{\"izDraw\":\"1\"}"
    },
  ]);

  //应用数据
  const appData = ref<any>({});

  //会话
  const tasks = ref<any>([]);

  /**
   * 点击的key
   */
  const activeKey = ref<string>('');

  /**
   * 获取图片
   */
  const getAiImg = (app) => {
    return getFileAccessHttpUrl(app?.icon) || defaultImg;
  };
  let inputValue: string = '';
  const handleInputChange = (e) => {
    inputValue = e.target.value.trim();
  };

  // 编辑
  const handleEdit = (item) => {
    item.isEdit = true;
    inputValue = item.title;
  };

  /**
   * 应用点击事件
   *
   * @param app
   */
  function handleAppClick(app) {
    activeKey.value = app.id;
    appData.value = app;
    emit('app-click', app);
  }

  /**
   * 添加会话
   */
  function addSession(title, id) {
    activeKey.value = id;
    if (tasks.value?.length > 0) {
      let findIndex = tasks.value.findIndex((item) => item.id === id);
      if (findIndex >= 0) {
        return;
      }
    }
    tasks.value.unshift({ id: id, title: title });
  }

  /**
   * 获取会话
   */
  async function getSessionList() {
    const res = await defHttp.get(
      {
        url: '/airag/chat/getConversationsByType',
        params: { sessionType: 'portal' },
      },
      { isTransformResponse: false }
    );
    if (res && res.success) {
      tasks.value = res.result;
    } else {
      tasks.value = [];
    }
    return tasks.value;
  }

  /**
   * 会话点击事件
   *
   * @param task
   */
  function handleTaskClick(task) {
    if (task.id === activeKey.value) {
      return;
    }
    activeKey.value = task.id;
    emit('task-click', task.id, task.title);
  }

  // 失去焦点
  function inputBlur(item) {
    item.isEdit = false;
    item.title = inputValue;
    defHttp.put(
      {
        url: '/airag/chat/conversation/update/title',
        params: { id: item.id, title: inputValue, sessionType: 'portal' },
      },
      { joinParamsToUrl: true }
    );
  }

  /**
   * 删除
   * @param data
   */
  function handleDel(data) {
    defHttp
      .delete(
        {
          url: '/airag/chat/conversation/' + data.id + '/portal',
        },
        { isTransformResponse: false }
      )
      .then(() => {
        getSessionList();
      });
  }

  /**
   * 新对话
   */
  function handleNewSession() {
    activeKey.value = '';
    emit('app-click', defaultApp.value);
  }

  onMounted(async () => {
    activeKey.value = '';
    const list = await getSessionList();
    if (list && list.length > 0) {
      const first = list[0];
      activeKey.value = first.id;
      emit('task-click', first.id, first.title);
    } else {
      emit('app-click', defaultApp.value);
    }
  });

  defineExpose({
    addSession,
  });
</script>

<style scoped lang="less">
  .space-page {
    padding: 12px 16px;
    height: 100%;
    background: #fbfcff; // 左侧浅色背景，区分右侧
    border-right: 1px solid #eef2f6; // 与右侧的分隔线
    display: flex; // 纵向布局，顶部固定，下面滚动
    flex-direction: column;
    box-sizing: border-box;
  }

  .space-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 8px;
  }

  .space-title {
    font-size: 18px;
    font-weight: 600;
  }

  .section {
    margin-top: 12px;
  }

  .section-title {
    font-size: 12px;
    font-weight: 600;
    margin-bottom: 8px;
  }

  .app-item {
    padding: 8px;
    display: flex;
    align-items: center;
    cursor: pointer;
    margin-bottom: 2px;
  }

  .app-item:hover {
    background: rgba(0, 0, 0, 0.04);
    border-radius: 10px;
  }

  .app-icon {
    width: 14px;
    height: 14px;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .app-name {
    margin-left: 10px;
    font-size: 14px;
    color: #1f2329;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .new-session {
    margin-top: 10px;
    align-items: center;
    border-radius: 12px;
    display: flex;
    justify-content: space-between;
    flex-shrink: 0;
    height: 36px;
    margin-right: 1px;
    overflow: visible;
    padding: 6px 10px !important;
    position: relative;
    transition: all 0.15s ease-in-out;
    cursor: pointer;
    margin-bottom: 2px;
    border: 1px solid #e0ecff;
    background: #f5f8ff;
  }

  .new-session:hover {
    background: #eaf2ff;
    border-color: #d4e5ff;
  }

  .new-session.active {
    background: #eaf2ff;
    border-color: #cfe0ff;
  }

  .new-session .left-box {
    display: flex;
    align-items: center;
  }

  .new-session .app-icon {
    width: 20px;
    height: 20px;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #eaf2ff;
    color: #3761eb;
  }

  .new-session .app-name {
    margin-left: 10px;
    font-size: 14px;
    color: #3761eb;
    font-weight: 600;
  }

  .task-list {
    position: relative;
  }

  .task-item {
    align-items: center;
    border-radius: 12px;
    display: flex;
    flex-shrink: 0;
    height: 36px;
    margin-right: 1px;
    overflow: visible;
    padding: 6px 10px !important;
    position: relative;
    transition: all 0.15s ease-in-out;
    cursor: pointer;
    margin-bottom: 2px;
    justify-content: space-between;
  }

  .task-item:hover {
    background: rgba(0, 0, 0, 0.04);
    border-radius: 12px;

    .edit,
    .del {
      display: block;
    }
  }

  .task-title {
    font-size: 14px;
    color: #1f2329;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .active {
    background: rgba(0, 0, 0, 0.04);
    border-radius: 12px;
    .edit {
      display: block;
    }
  }

  .edit,
  .del {
    display: none;
  }

  .icon-edit {
    display: flex;
  }
  .session-scroll {
    flex: 1; // 占满剩余空间
    min-height: 0; // 允许子元素在容器内正确滚动
    overflow-y: auto; // 仅列表区域滚动
    margin-bottom: 20px;

    // 自定义滚动条样式
    &::-webkit-scrollbar {
      width: 7px;
      height: 8px;
    }
    &::-webkit-scrollbar-thumb {
      background-color: #d9dfe7;
      border-radius: 4px;
    }
    &::-webkit-scrollbar-track {
      background-color: transparent;
    }
  }

  .header {
    margin-left: 0;
    display: flex;
    height: 40px;
    padding: 0;
    background-color: #fff;
    .header-image {
      width: 36px;
      height: 36px;
    }
    .header-name {
      color: rgba(0, 0, 0, 0.85);
      font-size: 16px;
      align-self: center;
      margin-left: 8px;
    }
  }
</style>
