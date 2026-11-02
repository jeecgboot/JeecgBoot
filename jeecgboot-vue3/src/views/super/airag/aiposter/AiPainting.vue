<template>
  <div class="content-wrapper">
    <!-- 中间参数配置 -->
    <div class="config-panel">
      <div class="config-tabs">
        <a-tabs v-model:activeKey="configTab" :tabBarStyle="{ margin: 0 }">
          <a-tab-pane key="draw" tab="绘图" />
          <a-tab-pane key="face" tab="换脸" />
          <a-tab-pane key="mix" tab="混图" />
        </a-tabs>
      </div>

      <!-- 示例按钮区域 -->
      <div class="example-buttons" v-if="configTab === 'mix'">
        <a-tooltip title="工作证制作">
          <a-button class="example-btn" size="small" @click="applyExample('work_card')">示例一</a-button>
        </a-tooltip>
        <a-tooltip title="换衣">
          <a-button class="example-btn" size="small" @click="applyExample('change_clothes')">示例二</a-button>
        </a-tooltip>
      </div>

      <div class="form-container">
        <BasicForm @register="registerForm" />

        <div class="instructions" v-if="configTab === 'face'">
          <div class="title">说明:</div>
          <p>1 图片都必须包含脸，否则出不来图</p>
          <p>2 "明星图"可以先用mj绘画制作出来</p>
          <p>3 "明星图"其实动漫图也行</p>
          <p>4 "你的头像"建议用一寸个人照</p>
        </div>

        <div class="instructions" v-if="configTab === 'mix'">
          <div class="title">说明:</div>
          <p>1 合成至少2张图片</p>
          <p>2 最多可传3张图</p>
        </div>
      </div>
      <div class="action-container">
        <a-button type="primary" size="large" block @click="handleGenerate" :loading="loading">
          <Icon icon="ant-design:thunderbolt-outlined" />
          立即生成
        </a-button>
      </div>
    </div>

    <!-- 右侧图片生成结果 -->
    <div class="preview-panel">
      <div class="panel-title">生成结果</div>
      <div class="preview-content">
        <div v-if="!generatedImage && !loading" class="empty-state">
          <Icon icon="ant-design:picture-outlined" size="64" color="#ccc" />
          <p>在左侧配置参数并点击生成</p>
        </div>

        <div v-if="loading && !generatedImage" class="loading-state">
          <a-spin size="large" tip="正在绘制图片，请稍候..." />
        </div>

        <div v-if="generatedImage" class="result-image-wrapper group">
          <img :src="generatedImage" class="result-image" alt="Generated Image" />
          <div class="image-actions">
            <a-button class="preview-button" @click="handlePreview">
              <Icon icon="ant-design:eye-outlined" />
              预览
            </a-button>
            <a-button type="primary" @click="handleDownload">
              <Icon icon="ant-design:download-outlined" />
              下载
            </a-button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <ImageViewer v-if="previewVisible" :imageUrl="generatedImage" @hide="previewVisible = false" />
</template>

<script lang="ts" setup>
  import { ref, watch, onMounted, onUnmounted } from 'vue';
  import { BasicForm, useForm } from '@/components/Form';
  import { drawFormSchema, faceSwapFormSchema, mixFormSchema } from './AiPoster.data';
  import ImageViewer from '../aiapp/chat/components/ImageViewer.vue';
  import { useMessage } from '@/hooks/web/useMessage';
  import { Icon } from '@/components/Icon';
  import { defHttp } from '@/utils/http/axios';
  import { useGlobSetting } from '@/hooks/setting';

  const { createMessage } = useMessage();
  const loading = ref(false);
  //update-begin---author:wangshuai---date:2026-04-15---for:【QQYUN-14944】AI绘画改为异步轮询，支持切换菜单后继续查询
  const PAINTING_TASK_ID_KEY = 'ai_painting_task_id';
  let pollTimer: ReturnType<typeof setTimeout> | null = null;
  //update-end---author:wangshuai---date:2026-04-15---for:【QQYUN-14944】AI绘画改为异步轮询，支持切换菜单后继续查询
  const generatedImage = ref('');
  const previewVisible = ref(false);
  const configTab = ref('draw');
  const FACE_SWAP_PROMPT =
    '将图2的人脸替换到图1的人脸区域，保留图2的面部特征和五官细节，保持图1的身体姿态、发型、服装、构图和背景不变，面部融合自然，光影与肤色协调，高分辨率，写实风格';
  const MIX_CHANGE_CLOTHES_PROMPT =
    '以图2作为唯一的主体底图和最终画面，只将图1人物所穿的服装转移到图2人物身上。必须完整保留图2人物的身份、人脸、五官、发型、体型、身体姿态以及图2的构图、环境和背景，不得使用或复制图1的人脸、头部、身体姿态、人物身份和背景。图1仅作为服装参考，准确还原其服装款式、颜色、纹理和材质，使服装自然贴合图2人物，保持图2原有光影和画面风格。最终结果必须是图2中的人穿上图1的衣服，而不是生成图1中的人物。高分辨率，写实风格';

  const { domainUrl } = useGlobSetting();
  const [registerForm, { validate, resetSchema, setFieldsValue }] = useForm({
    schemas: drawFormSchema,
    labelWidth: 100,
    actionColOptions: { span: 24 },
    showActionButtonGroup: false,
  });

  watch(configTab, (val) => {
    if (val === 'draw') {
      resetSchema(drawFormSchema);
    } else if (val === 'face') {
      resetSchema(faceSwapFormSchema).then(() => {
        setFieldsValue({ content: FACE_SWAP_PROMPT });
      });
    } else if (val === 'mix') {
      resetSchema(mixFormSchema).then(() => {
        setFieldsValue({ content: MIX_CHANGE_CLOTHES_PROMPT });
      });
    } else {
      // Default to draw or empty for mix for now
      resetSchema(drawFormSchema);
    }
  });

  //update-begin---author:wangshuai---date:2026-04-15---for:【QQYUN-14944】AI绘画改为异步轮询，支持切换菜单后继续查询
  /** 轮询查询任务结果 */
  function startPolling(taskId: string) {
    const poll = () => {
      defHttp
        .get({ url: `/airag/chat/getAiPosterResult/${taskId}` }, { isTransformResponse: false })
        .then((res) => {
          if (res.success) {
            if (res.result === 'pending' || res.result === null) {
              pollTimer = setTimeout(poll, 3000);
            } else {
              let imageUrl = res.result as string;
              const reg = /#\s*{\s*domainURL\s*}/g;
              imageUrl = imageUrl.replace(reg, domainUrl + '/sys/common/static');
              generatedImage.value = imageUrl;
              loading.value = false;
              localStorage.removeItem(PAINTING_TASK_ID_KEY);
              createMessage.success('图片生成成功！');
            }
          } else {
            loading.value = false;
            localStorage.removeItem(PAINTING_TASK_ID_KEY);
            createMessage.warning(res.message || '图片生成失败！');
          }
        })
        .catch(() => {
          pollTimer = setTimeout(poll, 3000);
        });
    };
    poll();
  }

  onMounted(() => {
    const savedTaskId = localStorage.getItem(PAINTING_TASK_ID_KEY);
    if (savedTaskId) {
      loading.value = true;
      generatedImage.value = '';
      startPolling(savedTaskId);
    }
  });

  onUnmounted(() => {
    if (pollTimer) {
      clearTimeout(pollTimer);
      pollTimer = null;
    }
  });
  //update-end---author:wangshuai---date:2026-04-15---for:【QQYUN-14944】AI绘画改为异步轮询，支持切换菜单后继续查询

  async function handleGenerate() {
    try {
      const values = await validate();
      loading.value = true;
      generatedImage.value = '';
      values.type = configTab.value;

      if (configTab.value === 'face') {
        if (values.sourceImage && values.targetImage) {
          values.imageUrl = values.sourceImage + ',' + values.targetImage;
          delete values.sourceImage;
          delete values.targetImage;
        }
        values.content =
          values.content ||
          FACE_SWAP_PROMPT;
      }

      //update-begin---author:wangshuai---date:2026-04-15---for:【QQYUN-14944】改为异步提交，获取taskId后开始轮询
      const res = await defHttp.post(
        { url: '/airag/chat/genAiPosterAsync', params: values },
        { isTransformResponse: false },
      );
      if (res.success && res.result) {
        const taskId = res.result as string;
        localStorage.setItem(PAINTING_TASK_ID_KEY, taskId);
        startPolling(taskId);
      } else {
        loading.value = false;
        createMessage.warning('提交任务失败！');
      }
      //update-end---author:wangshuai---date:2026-04-15---for:【QQYUN-14944】改为异步提交，获取taskId后开始轮询
    } catch (error) {
      console.error('Validation failed:', error);
      loading.value = false;
    }
  }

  function handlePreview() {
    previewVisible.value = true;
  }

  function applyExample(type: string) {
    if (type === 'work_card') {
      setFieldsValue({
        imageUrl:
          'https://jeecgdev.oss-cn-beijing.aliyuncs.com/upload/test/afdad9ea-077f-44a4-9d85-c26cde7aceed_1770703400282.png,https://jeecgdev.oss-cn-beijing.aliyuncs.com/upload/test/4e4d1886-fb3b-4c01-abf6-25e546a1253e_1770703403479.png',
        content:
          '以[图1]名片设计稿的构图与磨砂玻璃质感为模板，为[图2]人物生成竖版工作卡。圆角半透明卡面，柔和高光与浅投影；人物胸像置于中上区域；左下排版姓名/职位/公司/电话，极简无衬线字体，留白均衡。右上角放置[图1]人物的可爱3D卡通形象，打破边界半浮出卡片并投下轻影，形成层次与视觉焦点。整体明亮自然光、真实材质细节，不添加多余图案与元素。人物需要清晰，不要模糊。',
      });
    } else if (type === 'change_clothes') {
      setFieldsValue({
        imageUrl:
          'https://jeecgdev.oss-cn-beijing.aliyuncs.com/upload/test/4e4d1886-fb3b-4c01-abf6-25e546a1253e_1770703403479.png,https://jeecgdev.oss-cn-beijing.aliyuncs.com/upload/test/63706787-4072-4cba-ad88-385e0584b020_1770703456766.png',
        content: MIX_CHANGE_CLOTHES_PROMPT,
      });
    }
  }

  /**
   * 图片导出
   */
  function handleDownload() {
    if (!generatedImage.value) {
      return;
    }
    const a = document.createElement('a');
    a.href = generatedImage.value;
    a.download = `ai-painting-${Date.now()}.jpg`;
    a.target = '_blank';
    a.click();
  }
</script>

<style lang="less" scoped>
  .content-wrapper {
    flex: 1;
    display: flex;
    gap: 16px;
    overflow: hidden;
    height: 100%; /* Ensure it takes full height of parent */
  }

  .config-panel {
    width: 550px;
    min-width: 350px;
    background: #fff;
    border-radius: 8px;
    display: flex;
    flex-direction: column;
    padding: 20px;
    overflow: hidden;
    position: relative;

    .config-tabs {
      margin-bottom: 20px;

      :deep(.ant-tabs-nav::before) {
        border-bottom: none;
      }

      :deep(.ant-tabs-tab) {
        padding: 8px 0;
        margin: 0 32px 0 0;
        font-size: 16px;

        &.ant-tabs-tab-active .ant-tabs-tab-btn {
          color: #00b96b;
          font-weight: 500;
        }
      }

      :deep(.ant-tabs-ink-bar) {
        background: #00b96b;
      }
    }

    .example-buttons {
      position: absolute;
      top: 28px;
      right: 20px;
      display: flex;
      justify-content: flex-end;
      gap: 10px;
      z-index: 10;

      .example-btn {
        color: #666;
        border-color: #d9d9d9;

        &:hover {
          color: #00b96b;
          border-color: #00b96b;
        }
      }
    }

    .form-container {
      flex: 1;
      overflow-y: auto;

      .instructions {
        margin-top: 20px;
        padding: 0 10px;
        color: #666;
        font-size: 14px;
        line-height: 1.8;

        .title {
          font-weight: 600;
          color: #333;
          margin-bottom: 8px;
        }

        p {
          margin: 0;
        }
      }
    }

    .action-container {
      margin-top: 20px;
      padding-top: 20px;
      border-top: 1px solid #f0f0f0;
    }
  }

  .preview-panel {
    flex: 1;
    background: #fff;
    border-radius: 8px;
    display: flex;
    flex-direction: column;
    padding: 20px;
    overflow: hidden;

    .preview-content {
      flex: 1;
      background: #f7f8fc;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      overflow: hidden;
      position: relative;
    }
  }

  .panel-title {
    font-size: 16px;
    font-weight: 600;
    color: #1f2329;
    margin-bottom: 20px;
    padding-left: 8px;
    border-left: 4px solid #1890ff;
    line-height: 1;
  }

  .empty-state {
    text-align: center;
    color: #8f959e;

    p {
      margin-top: 16px;
    }
  }

  .loading-state {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .result-image-wrapper {
    position: relative;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;

    .result-image {
      width: 100%;
      height: 100%;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      object-fit: contain;
    }

    .image-actions {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 10px;
      padding: 10px;
      border: 1px solid rgba(255, 255, 255, 0.5);
      border-radius: 8px;
      background: rgba(255, 255, 255, 0.92);
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
      opacity: 0;
      transition: opacity 0.2s ease;
      backdrop-filter: blur(6px);

      .preview-button {
        color: #1677ff;
        border-color: #1677ff;
      }
    }

    &:hover .image-actions {
      opacity: 1;
    }
  }
</style>
