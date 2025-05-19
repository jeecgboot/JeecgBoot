<template>
  <div class="p-2">
    <BasicModal destroyOnClose @register="registerModal" :canFullscreen="false" width="1000px" @ok="handleOk" @cancel="handleCancel" okText="替换" wrapClassName='ai-rag-generate-prompt-modal'>
      <div class="prompt">
        <div class="prompt-left">
          <div class="prompt-left-title">提示词生成器</div>
          <div class="prompt-left-desc">提示词生成器使用配置的模型来优化提示词，以获得更高的质量和更好的结构。请写出清晰详细的说明。</div>
          <a-divider></a-divider>
          <div class="prompt-left-try">
            <div class="prompt-left-try-title">试一试</div>
          </div>
          <div class="instructions">
            <div class="instructions-content" v-for="item in instructionsList" @click="instructionsClick(item.value)">
              <Icon :icon="item.icon" size="14" color="#676f83"></Icon>
              <div class="instructions-name">{{ item.name }}</div>
            </div>
          </div>
          <div class="prompt-left-textarea">
            <div class="command">指令</div>
            <a-textarea v-model:value="prompt" :autoSize="{ minRows: 8, maxRows: 8 }"></a-textarea>
          </div>
          <a-button @click="generatedPrompt" class="prompt-left-btn" type="primary" :loading="loading">
            <span style="align-items: center; display: flex" v-if="!loading">
              <svg width="1em" height="1em" viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                <path
                  d="M18.9839 1.85931C19.1612 1.38023 19.8388 1.38023 20.0161 1.85931L20.5021 3.17278C20.5578 3.3234 20.6766 3.44216 20.8272 3.49789L22.1407 3.98392C22.6198 4.1612 22.6198 4.8388 22.1407 5.01608L20.8272 5.50211C20.6766 5.55784 20.5578 5.6766 20.5021 5.82722L20.0161 7.14069C19.8388 7.61977 19.1612 7.61977 18.9839 7.14069L18.4979 5.82722C18.4422 5.6766 18.3234 5.55784 18.1728 5.50211L16.8593 5.01608C16.3802 4.8388 16.3802 4.1612 16.8593 3.98392L18.1728 3.49789C18.3234 3.44216 18.4422 3.3234 18.4979 3.17278L18.9839 1.85931zM13.5482 4.07793C13.0164 2.64069 10.9836 2.64069 10.4518 4.07793L8.99368 8.01834C8.82648 8.47021 8.47021 8.82648 8.01834 8.99368L4.07793 10.4518C2.64069 10.9836 2.64069 13.0164 4.07793 13.5482L8.01834 15.0063C8.47021 15.1735 8.82648 15.5298 8.99368 15.9817L10.4518 19.9221C10.9836 21.3593 13.0164 21.3593 13.5482 19.9221L15.0063 15.9817C15.1735 15.5298 15.5298 15.1735 15.9817 15.0063L19.9221 13.5482C21.3593 13.0164 21.3593 10.9836 19.9221 10.4518L15.9817 8.99368C15.5298 8.82648 15.1735 8.47021 15.0063 8.01834L13.5482 4.07793zM5.01608 16.8593C4.8388 16.3802 4.1612 16.3802 3.98392 16.8593L3.49789 18.1728C3.44216 18.3234 3.3234 18.4422 3.17278 18.4979L1.85931 18.9839C1.38023 19.1612 1.38023 19.8388 1.85931 20.0161L3.17278 20.5021C3.3234 20.5578 3.44216 20.6766 3.49789 20.8272L3.98392 22.1407C4.1612 22.6198 4.8388 22.6198 5.01608 22.1407L5.50211 20.8272C5.55784 20.6766 5.6766 20.5578 5.82722 20.5021L7.14069 20.0161C7.61977 19.8388 7.61977 19.1612 7.14069 18.9839L5.82722 18.4979C5.6766 18.4422 5.55784 18.3234 5.50211 18.1728L5.01608 16.8593z"
                ></path>
              </svg>
              <span style="margin-left: 4px">生成</span>
            </span>
          </a-button>
        </div>
        <div class="prompt-right">
          <div v-if="!loading && !content">
            <svg width="6em" height="6em" viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
              <path
                d="M18.9839 1.85931C19.1612 1.38023 19.8388 1.38023 20.0161 1.85931L20.5021 3.17278C20.5578 3.3234 20.6766 3.44216 20.8272 3.49789L22.1407 3.98392C22.6198 4.1612 22.6198 4.8388 22.1407 5.01608L20.8272 5.50211C20.6766 5.55784 20.5578 5.6766 20.5021 5.82722L20.0161 7.14069C19.8388 7.61977 19.1612 7.61977 18.9839 7.14069L18.4979 5.82722C18.4422 5.6766 18.3234 5.55784 18.1728 5.50211L16.8593 5.01608C16.3802 4.8388 16.3802 4.1612 16.8593 3.98392L18.1728 3.49789C18.3234 3.44216 18.4422 3.3234 18.4979 3.17278L18.9839 1.85931zM13.5482 4.07793C13.0164 2.64069 10.9836 2.64069 10.4518 4.07793L8.99368 8.01834C8.82648 8.47021 8.47021 8.82648 8.01834 8.99368L4.07793 10.4518C2.64069 10.9836 2.64069 13.0164 4.07793 13.5482L8.01834 15.0063C8.47021 15.1735 8.82648 15.5298 8.99368 15.9817L10.4518 19.9221C10.9836 21.3593 13.0164 21.3593 13.5482 19.9221L15.0063 15.9817C15.1735 15.5298 15.5298 15.1735 15.9817 15.0063L19.9221 13.5482C21.3593 13.0164 21.3593 10.9836 19.9221 10.4518L15.9817 8.99368C15.5298 8.82648 15.1735 8.47021 15.0063 8.01834L13.5482 4.07793zM5.01608 16.8593C4.8388 16.3802 4.1612 16.3802 3.98392 16.8593L3.49789 18.1728C3.44216 18.3234 3.3234 18.4422 3.17278 18.4979L1.85931 18.9839C1.38023 19.1612 1.38023 19.8388 1.85931 20.0161L3.17278 20.5021C3.3234 20.5578 3.44216 20.6766 3.49789 20.8272L3.98392 22.1407C4.1612 22.6198 4.8388 22.6198 5.01608 22.1407L5.50211 20.8272C5.55784 20.6766 5.6766 20.5578 5.82722 20.5021L7.14069 20.0161C7.61977 19.8388 7.61977 19.1612 7.14069 18.9839L5.82722 18.4979C5.6766 18.4422 5.55784 18.3234 5.50211 18.1728L5.01608 16.8593z"
              ></path>
            </svg>
            <div>在左侧描述您的用例，</div>
            <div>编排预览将在此处显示。</div>
          </div>
          <div v-if="loading">
            <a-spin :spinning="loading" tip="为您编排应用程序中…"></a-spin>
          </div>
          <div v-if="content">
            <a-textarea v-model:value="content" :autoSize="{ minRows: 18, maxRows: 18 }"></a-textarea>
          </div>
        </div>
      </div>
    </BasicModal>
  </div>
</template>

<script lang="ts">
  import { ref, unref } from 'vue';
  import BasicModal from '@/components/Modal/src/BasicModal.vue';
  import { useModalInner } from '@/components/Modal';
  import { promptGenerate } from '@/views/super/airag/aiapp/AiApp.api';

  export default {
    name: 'AiAppGeneratedPrompt',
    components: {
      BasicModal,
    },
    emits: ['ok', 'register'],
    setup(props, { emit }) {
      //提示词
      const prompt = ref<string>('');
      //加载
      const loading = ref<boolean>(false);
      //显示文本
      const content = ref<string>('');
      //指令提示词
      const instructionsList = ref<any>([
        { name: 'python代码助手', value: 'python', icon: 'ant-design:code-outlined' },
        { name: '翻译器', value: 'translator', icon: 'ant-design:translation-outlined' },
        { name: '会议助手', value: 'meeting', icon: 'ant-design:team-outlined' },
        { name: '润色文章', value: 'article', icon: 'ant-design:profile-outlined' },
        { name: 'sql生成器', value: 'sql', icon: 'ant-design:console-sql-outlined' },
        { name: '旅行规划师', value: 'travel', icon: 'ant-design:car-outlined' },
        { name: 'linux专家', value: 'linux', icon: 'ant-design:fund-projection-screen-outlined' },
        { name: '内容提炼器', value: 'content', icon: 'ant-design:read-outlined' },
      ]);
      //指令
      const tip = ref<any>({
        python: '你是一个python专家，可以帮助用户编写和纠错代码。',
        translator: '一个可以将多种语言翻译为中文的翻译器。',
        meeting: '将会议内容提炼总结，包括讨论主题、关键要点和待办事项。',
        article: '用高超的编辑技巧改进我的文章。',
        sql: '根据用户的描述，生成sql语句，要支持引导用户提供表结构',
        travel: '你是一个旅行规划师，擅长帮助用户轻松规划他们的旅行',
        linux: '你是一个linux专家，擅长解决各种linux相关的问题。',
        content: '你是一个阅读理解大师，可以阅读用户提供的文章，并提炼主要内容输出给用户。',
      });
      //注册modal
      const [registerModal, { closeModal, setModalProps }] = useModalInner(async (data) => {
        content.value = '';
        loading.value = false;
        prompt.value = '';
        setModalProps({ height: 500 });
      });

      /**
       * 保存
       */
      async function handleOk() {
        emit('ok', content.value);
        handleCancel();
      }

      /**
       * 生成
       */
      function generatedPrompt() {
        content.value = '';
        loading.value = true;
        promptGenerate({ prompt: prompt.value })
          .then((res) => {
            if (res.success) {
              content.value = res.result;
            }
            loading.value = false;
          })
          .catch(() => {
            loading.value = false;
          });
      }

      /**
       * 指令点击事件
       */
      function instructionsClick(value) {
        prompt.value = tip.value[value];
      }

      /**
       * 取消
       */
      function handleCancel() {
        closeModal();
      }

      return {
        registerModal,
        handleOk,
        handleCancel,
        prompt,
        generatedPrompt,
        instructionsList,
        loading,
        instructionsClick,
        content,
      };
    },
  };
</script>

<style scoped lang="less">
  .prompt {
    width: 100%;
    display: flex;
  }
  .prompt-left {
    width: 50%;
    padding: 20px;
    border-right: 1px solid #10182814;
    .prompt-left-title {
      background: linear-gradient(92deg, #2250f2 -29.55%, #0ebcf3 75.22%);
      background-clip: text;
      -webkit-text-fill-color: transparent;
      line-height: 28px;
      font-weight: 700;
      font-size: 18px;
    }
    .prompt-left-desc {
      color: #676f83;
      font-weight: 400;
      font-size: 13px;
      margin-top: 4px;
    }
    .prompt-left-try {
      display: flex;
      align-items: center;
      .prompt-left-try-title {
        color: #676f83;
        line-height: 18px;
        text-transform: uppercase;
        font-weight: 600;
        font-size: 12px;
        margin-right: 10px;
      }
    }
    .prompt-left-textarea {
      margin-top: 25px;
      .command {
        color: #101828;
        line-height: 15px;
        font-weight: 500;
        font-size: 12px;
        margin-bottom: 15px;
      }
    }
    .prompt-left-btn {
      width: 80px;
      margin-top: 10px;
      float: right;
    }
  }
  .prompt-right {
    padding: 20px;
    width: 50%;
    text-align: center;
    align-content: center;
    svg {
      color: #676f83;
    }
  }
  .instructions {
    display: flex;
    flex-wrap: wrap;
    .instructions-content {
      padding-left: 5px;
      padding-right: 5px;
      border-radius: 5px;
      align-items: center;
      cursor: pointer;
      height: 20px;
      display: flex;
      margin-top: 8px;
      margin-left: 5px;
    }
    .instructions-name {
      color: #354052;
      font-size: 13px;
      font-weight: 500;
      line-height: 2px;
      margin-left: 4px;
    }
  }
  :deep(.ant-divider-horizontal) {
    margin: 12px 0;
  }
</style>
<style lang="less">
  .ai-rag-generate-prompt-modal {
    .jeecg-modal-content > .scroll-container {
      padding: 0;

      & > .scrollbar__wrap {
        overflow: hidden;
        margin-bottom: 0 !important;
      }
    }
  }
</style>
