<template>
  <div class="ai-poster-page">
    <div class="page-header">
      <a-tabs v-model:activeKey="activeKey">
        <a-tab-pane key="poster" tab="AI 海报" />
        <a-tab-pane key="painting" tab="AI 绘画" />
      </a-tabs>
    </div>

    <div class="content-wrapper" v-show="activeKey === 'poster'">
      <!-- 左侧模板选择 -->
      <div class="template-panel">
        <div class="panel-title">模板选择</div>
        <div class="template-list">
          <div
            v-for="item in templates"
            :key="item.id"
            class="template-item"
            :class="{ active: activeTemplateId === item.id }"
            @click="handleSelectTemplate(item)"
          >
            <div class="template-cover">
              <img :src="item.url" style="width: 100%" />
            </div>
            <div class="template-name">{{ item.name }}</div>
          </div>
        </div>
      </div>
      <!-- 中间参数配置 -->
      <div class="config-panel">
        <div class="panel-title">参数配置</div>
        <div class="form-container">
          <BasicForm @register="registerForm" />
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
            <a-spin size="large" tip="正在绘制海报，请稍候..." />
          </div>

          <div v-if="generatedImage" class="result-image-wrapper group">
            <img :src="generatedImage" class="result-image" alt="Generated Poster" />
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

    <AiPainting v-if="activeKey === 'painting'" />

    <ImageViewer v-if="previewVisible" :imageUrl="generatedImage" @hide="previewVisible = false" />
  </div>
</template>

<script lang="ts" setup>
  import { ref, watch, onMounted, onUnmounted } from 'vue';
  import { BasicForm, useForm } from '@/components/Form';
  import { formSchema } from './AiPoster.data';
  import ImageViewer from '../aiapp/chat/components/ImageViewer.vue';
  import { useMessage } from '@/hooks/web/useMessage';
  import { Icon } from '@/components/Icon';
  import { defHttp } from '@/utils/http/axios';
  import { useGlobSetting } from '@/hooks/setting';
  import AiPainting from './AiPainting.vue';

  const TASK_ID_KEY = 'ai_poster_task_id';
  const ACTIVE_TAB_KEY = 'ai_poster_active_tab';
  const activeKey = ref(localStorage.getItem(ACTIVE_TAB_KEY) || 'poster');
  watch(activeKey, (val) => localStorage.setItem(ACTIVE_TAB_KEY, val));

  const { createMessage } = useMessage();
  const loading = ref(false);
  const generatedImage = ref('');
  const previewVisible = ref(false);

  const activeTemplateId = ref<number | null>(null);
  let pollTimer: ReturnType<typeof setTimeout> | null = null;

  const templates = [
    {
      id: 1,
      name: '淡雅政务风',
      prompt:
        '淡雅政务风横版海报，主色调浅蓝 + 米白 + 淡灰，扁平化矢量风格，叠加细腻宣纸纹理；画面核心元素：简约政务办公楼轮廓（线条简洁）、金色钢笔、展开的公文册、淡蓝色祥云纹样、橄榄枝装饰；背景是米白渐变 + 浅蓝竖条肌理，点缀细金色边框；文字设计：居中用黑体写‘政务为民・高效规范’，下方配‘用心服务・务实笃行’浅蓝小字；整体氛围淡雅庄重、专业简洁，层次分明，光影柔和，高清细节，竖版 9:16 构图',
      size: '720*1280',
      url: 'https://upload.jeecg.com/jeecg/AI/simple.png',
    },
    {
      id: 2,
      name: '节日海报',
      prompt:
        '国潮中国风春节竖版海报，主色调红金 + 暖橙渐变，国潮插画风格，矢量扁平 + 柔和渐变质感，叠加细腻宣纸纹理；画面层次：前景是红色剪纸风梅花、金色福字贴纸、饱满水饺、红色灯笼串，中景是红墙金瓦的传统民居屋檐、飘带式祥云，远景是淡金色烟花绽放 + 暖红色光晕背景；点缀金色铜钱纹、折纸兔子、如意纹样；画面中央偏上用金色书法字体写‘新春大吉’，下方配\'万事如意\'四字楷书；整体氛围喜庆祥和、团圆温馨，层次分明主次清晰，光影柔和不刺眼，高清细节，竖版 9:16 构图',
      url: 'https://upload.jeecg.com/jeecg/AI/image89444392111.png',
      size: '720*1280',
    },
    {
      id: 3,
      name: '科技宣传',
      prompt:
        '未来科技感宣传海报，主色调蓝紫渐变 + 银白金属色，冷光霓虹光效，赛博朋克线条质感；画面核心元素：全息投影的地球数据模型、流动的蓝色数据流、发光的电路板纹理、悬浮的芯片与机械齿轮、未来感建筑轮廓；点缀粒子光效、透明全息界面、霓虹光带；文字设计：居中用未来感无衬线字体写‘科技赋能・智启新程’，下方配‘创新驱动・引领未来’小字，字体带轻微发光描边；整体氛围简洁高级、充满未来感，层次分明，光影锐利，高清细节，横版 16:9 构图',
      size: '720*1280',
      url: 'https://upload.jeecg.com/jeecg/AI/technology.png',
    },
    {
      id: 4,
      name: '优雅复古',
      prompt:
        '民国风优雅复古竖版海报，主色调米黄 + 豆沙红 + 墨黑，低饱和度胶片质感，叠加老报纸纹理与轻微颗粒感；画面核心元素：穿月白旗袍的女性侧影（盘发配珍珠发簪）、油纸伞、复古留声机、雕花木质窗棂、缠绕珍珠的藤蔓花纹；背景是模糊的老上海石库门建筑轮廓，点缀淡粉色玉兰花、复古字体排版的诗句（‘岁月静好，温婉如初’）；文字设计：上方用民国手写体写‘雅致时光’，下方配衬线字体‘复刻民国风雅’，字体带轻微做旧效果；整体氛围温婉知性、静谧典雅，光影柔和（侧光勾勒人物轮廓），层次分明，高清细节，竖版 9:16 构图',
      size: '720*1280',
      url: 'https://upload.jeecg.com/jeecg/AI/retro.png',
    },
    {
      id: 5,
      name: '赛博朋克',
      prompt:
        '国潮赛博朋克横版海报，主色调中国红 + 深空黑 + 鎏金霓虹，传统纹样与科技元素碰撞，叠加红金渐变光效 + 竹简纹理；画面核心元素：龙形霓虹光带（龙身缠绕电路板）、红墙金瓦的赛博风古建筑（屋檐挂霓虹灯笼）、穿汉服改良款的赛博人物（配发光发簪 / 机械袖）、全息投影的汉字霓虹灯牌（‘江湖’‘未来’）；点缀祥云数据流、金属质感的传统回纹、悬浮的鎏金元宝状机械装置；文字设计：上方用金色书法字体写‘赛博江湖’，下方配‘TECH & TRADITION’英文，字体带红金霓虹发光效果；整体氛围大气炫酷、传统与未来交融，光影强烈且富有冲击力，层次分明，高清细节，横版 16:9 构图',
      size: '720*1280',
      url: 'https://upload.jeecg.com/jeecg/AI/cyberpunk.png',
    },
  ];
  const { domainUrl } = useGlobSetting();
  const [registerForm, { validate, setFieldsValue }] = useForm({
    schemas: formSchema,
    labelWidth: 100,
    actionColOptions: { span: 24 },
    showActionButtonGroup: false,
  });

  /**
   * 选中模板事件
   * @param template
   */
  function handleSelectTemplate(template: any) {
    activeTemplateId.value = template.id;
    setFieldsValue({
      content: template.prompt,
      imageUrl: template.url,
      imageSize: template.size,
    });
    createMessage.success(`已应用模板：${template.name}`);
  }

  //update-begin---wangshuai---date:20260415  for：[QQYUN-14944]AI 改成异步的，支持切换菜单------------
  /** 轮询查询任务结果 */
  function startPolling(taskId: string) {
    const poll = () => {
      defHttp
        .get({ url: `/airag/chat/getAiPosterResult/${taskId}` }, { isTransformResponse: false })
        .then((res) => {
          if (res.success) {
            if (res.result === 'pending' || res.result === null) {
              // 继续轮询
              pollTimer = setTimeout(poll, 3000);
            } else {
              // 成功
              let imageUrl = res.result as string;
              const reg = /#\s*{\s*domainURL\s*}/g;
              imageUrl = imageUrl.replace(reg, domainUrl + '/sys/common/static');
              generatedImage.value = imageUrl;
              loading.value = false;
              localStorage.removeItem(TASK_ID_KEY);
              createMessage.success('海报生成成功！');
            }
          } else {
            // 失败
            loading.value = false;
            localStorage.removeItem(TASK_ID_KEY);
            createMessage.warning(res.message || '海报生成失败！');
          }
        })
        .catch(() => {
          pollTimer = setTimeout(poll, 3000);
        });
    };
    poll();
  }
  //update-end---wangshuai---date:20260415  for：[QQYUN-14944]AI 改成异步的，支持切换菜单------------
  
  async function handleGenerate() {
    try {
      const values = await validate();
      loading.value = true;
      generatedImage.value = '';
      //update-begin---wangshuai---date:20260415  for：[QQYUN-14944]AI 改成异步的，支持切换菜单------------
      values.type = 'poster';
      const res = await defHttp.post(
        { url: '/airag/chat/genAiPosterAsync', params: values },
        { isTransformResponse: false },
      );
      if (res.success && res.result) {
        const taskId = res.result as string;
        localStorage.setItem(TASK_ID_KEY, taskId);
        startPolling(taskId);
      } else {
        loading.value = false;
        createMessage.warning('提交任务失败！');
      }
      //update-end---wangshuai---date:20260415  for：[QQYUN-14944]AI 改成异步的，支持切换菜单------------
    } catch (error) {
      loading.value = false;
      console.error('Validation failed:', error);
    }
  }

  //update-begin---wangshuai---date:20260415  for：[QQYUN-14944]AI 改成异步的，支持切换菜单------------
  onMounted(() => {
    // 切换回菜单时，若有未完成的任务则恢复轮询
    const savedTaskId = localStorage.getItem(TASK_ID_KEY);
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
  //update-end---wangshuai---date:20260415  for：[QQYUN-14944]AI 改成异步的，支持切换菜单------------

  function handlePreview() {
    previewVisible.value = true;
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
    a.download = `ai-poster-${Date.now()}.jpg`;
    a.target = '_blank';
    a.click();
  }
</script>

<style lang="less" scoped>
  .ai-poster-page {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    padding: 16px;
    background-color: #f0f2f5;
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    overflow: hidden;

    .page-header {
      margin-bottom: 16px;
      background: #fff;
      padding: 16px 24px;
      border-radius: 8px;

      .title {
        font-size: 20px;
        font-weight: 600;
        color: #1f2329;
        margin-right: 12px;
      }

      .subtitle {
        color: #8f959e;
        font-size: 14px;
      }
    }

    .content-wrapper {
      flex: 1;
      display: flex;
      gap: 16px;
      overflow: hidden;
    }

    .template-panel {
      width: 200px;
      min-width: 180px;
      background: #fff;
      border-radius: 8px;
      display: flex;
      flex-direction: column;
      padding: 12px;
      overflow-y: auto;

      .template-list {
        display: flex;
        flex-direction: column;
        gap: 10px;
      }

      .template-item {
        border: 1px solid #f0f0f0;
        border-radius: 6px;
        padding: 8px;
        cursor: pointer;
        transition: all 0.3s;
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 6px;

        &:hover {
          border-color: #1890ff;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        }

        &.active {
          border-color: #1890ff;
          background-color: #e6f7ff;
        }

        .template-cover {
          width: 100%;
          aspect-ratio: 9 / 16;
          height: auto;
          background-color: #f5f5f5;
          border-radius: 4px;
          display: flex;
          align-items: center;
          justify-content: center;
          color: #8c8c8c;
          overflow: hidden;

          img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            border-radius: 4px;
            display: block;
          }
        }

        .template-name {
          font-size: 14px;
          color: #1f2329;
          text-align: center;
          max-width: 100%;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }
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

      .form-container {
        flex: 1;
        overflow-y: auto;
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
  }
</style>
