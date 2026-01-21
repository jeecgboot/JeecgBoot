<!-- card 官方模板 -->
<template>
  <div class="card-select-panel" @click="handleClick">
    <!-- 卡片内容 -->
    <div class="card-item">
      <div class="card-title">{{ getTitle }}</div>
      <!-- 缩略图左侧变体 -->
      <div v-if="getTemplateId === 'template-1'" class="card-top">
        <div class="thumb" v-if="!getImage">
          <div class="thumb-dot"></div>
          <div class="thumb-mountain"></div>
        </div>
        <div v-else class="thumb-image">
          <img :src="getImage" />
        </div>
        <div class="desc clamp">
          {{ getContent }}
        </div>
      </div>

      <!-- 缩略图右侧变体 -->
      <div v-else-if="getTemplateId === 'template-2'" class="card-top">
        <div class="desc clamp">
          {{ getContent }}
        </div>
        <div class="thumb" v-if="!getImage">
          <div class="thumb-dot"></div>
          <div class="thumb-mountain"></div>
        </div>
        <div v-else class="thumb-image">
          <img :src="getImage" />
        </div>
      </div>

      <!-- 横幅图片变体 -->
      <div v-else-if="getTemplateId === 'template-3'">
        <div class="banner" v-if="!getImage">
          <div class="banner-dot"></div>
          <div class="banner-mountain"></div>
        </div>
        <div v-else class="banner-image">
          <img :src="getImage" />
        </div>
        <div class="desc">
          {{ getContent }}
        </div>
      </div>

      <!-- 纯文本变体 -->
      <div v-else-if="getTemplateId === 'template-4'" class="desc">
        {{ getContent }}
      </div>
      <div v-if="showDeleteIcon" class="delete">
        <Icon icon="ant-design:close-outlined" @click.parent.stop="handleDelete"></Icon>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, defineProps, defineEmits, computed } from 'vue';
  import {useGlobSetting} from "@/hooks/setting";
  const props = defineProps({
    templateId: { type: String, default: '' },
    showDeleteIcon: { type: Boolean, default: false },
    cardData: { type: Object, default: null },
    cardConfig: { type: Object, default: null },
  });
  const emit = defineEmits(['register', 'click', 'delete']);
  const { domainUrl } = useGlobSetting();
  const content = ref<string>('内容描述是一种重要的沟通和表达，它在描述事物时发挥着至关重要的作用');

  //获取模板id
  const getTemplateId = computed(() => props.templateId);

  //获取内容
  const getContent = computed(() => {
    if (props.cardData && props.cardConfig) {
      return props.cardData[props.cardConfig?.content];
    }
    return content;
  });

  //获取文本
  const getTitle = computed(() => {
    if (props.cardData && props.cardConfig) {
      return props.cardData[props.cardConfig?.title];
    }
    return '标题';
  });

  //获取图片
  const getImage = computed(() => {
    if (props.cardData && props.cardConfig) {
      let src = props.cardData[props.cardConfig?.image];
      let reg = /#\s*{\s*domainURL\s*}/g;
      src = src.replace(reg,domainUrl);
      return src;
    }
    return '';
  });

  /**
   * 卡片点击事件
   */
  function handleClick() {
    emit('click');
  }

  /**
   * 卡片删除事件
   */
  function handleDelete() {
    emit('delete');
  }
</script>

<style scoped lang="less">
  .card-select-panel {
    padding: 8px 0;
    cursor: pointer;
  }
  /* 标题 */
  .card-title {
    width: 100%;
    font-weight: 600;
    color: #1f2937;
    margin-bottom: 10px;
    line-height: 20px;
    letter-spacing: 0;
    white-space: pre-line;
    overflow: hidden;
    display: -webkit-box;
    text-overflow: ellipsis;
    -webkit-box-orient: vertical;
    font-size: 18px;
    text-align: left;
    -webkit-line-clamp: 1;
  }

  /* 描述文本 */
  .desc {
    color: #667085;
    font-size: 13px;
    line-height: 1.6;
  }

  /* 多行省略，用于第一张卡片（示例） */
  .clamp {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  /* 顶部缩略图行（缩略图 + 描述） */
  .card-top {
    display: flex;
    gap: 12px;
    align-items: center;
  }
  /* 卡片容器 */
  .card-item {
    display: flex;
    flex-direction: column;
    position: relative;
    border-radius: 14px;
    background: #f8fafc;
    border: 1px solid #e9edf3;
    padding: 14px 10px;
    transition: all 0.2s ease;
  }

  /* 轻微内阴影与悬浮效果 */
  .card-item::before {
    content: '';
    position: absolute;
    inset: 0;
    border-radius: inherit;
    box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.7);
    pointer-events: none;
  }
  .card-item:hover {
    border-color: #dbe3ea;
    background: #f9fbfd;
    .delete {
      display: block;
    }
  }
  /* 描述文本 */
  .desc {
    color: #667085;
    margin-top: 10px;
    width: 100%;
    font-size: 14px;
    font-weight: 400;
    line-height: 20px;
    letter-spacing: 0;
    white-space: pre-line;
    -webkit-box-orient: vertical;
    overflow: hidden;
    display: -webkit-box;
    text-overflow: ellipsis;
    text-align: left;
    -webkit-line-clamp: 3;
  }
  /* 缩略图占位（与示例一致：左上小图） */
  .thumb {
    width: 44px;
    height: 44px;
    border-radius: 10px;
    background: linear-gradient(180deg, #edf2fa 0%, #e9eef7 100%);
    position: relative;
    flex: 0 0 44px;
  }
  .thumb-dot {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: #cbd5e1;
    position: absolute;
    top: 9px;
    left: 9px;
  }
  .thumb-mountain {
    width: 20px;
    height: 12px;
    background: #dbe2ee;
    border-radius: 3px;
    position: absolute;
    bottom: 9px;
    left: 12px;
  }

  /* 横幅图片占位（第三张卡片的大图） */
  .banner {
    width: 100%;
    height: 96px;
    border-radius: 12px;
    background: linear-gradient(180deg, #eef3fb 0%, #e7edf6 100%);
    position: relative;
    margin-bottom: 10px;
  }
  .banner-dot {
    width: 10px;
    height: 10px;
    border-radius: 50%;
    background: #cbd5e1;
    position: absolute;
    top: 22px;
    left: 26px;
  }
  .banner-mountain {
    width: 120px;
    height: 20px;
    background: #dbe2ee;
    border-radius: 4px;
    position: absolute;
    bottom: 24px;
    left: 50%;
    transform: translateX(-50%);
  }

  /* 变体微调：确保整体间距与示例一致 */
  .variant-text .desc {
    margin-top: 2px;
  }
  .variant-thumb .desc {
    margin-top: 2px;
  }
  .variant-banner .desc {
    margin-top: 8px;
  }
  .delete {
    width: 20px;
    position: absolute;
    top: 6px;
    right: 6px;
    display: none;
    cursor: pointer;
  }

  .thumb-image {
    background-color: transparent;
    display: flex;
    width: 44px;
    height: 44px;
    border-radius: 10px;
    position: relative;
    flex: 0 0 44px;
    img {
      width: 44px;
      height: 44px;
    }
  }
  .banner-image {
    background-color: transparent;
    width: 100%;
    height: 96px;
    border-radius: 12px;
    position: relative;
    margin-bottom: 10px;
    img {
      width: 100%;
      height: 96px;
    }
  }
</style>
