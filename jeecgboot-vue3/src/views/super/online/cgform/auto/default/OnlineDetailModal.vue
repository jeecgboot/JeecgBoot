<template>
  <BasicModal :title="title" :width="modalWidth" :maxHeight="600" :enableComment="enableComment" :defaultFullscreen="false" v-bind="$attrs" @register="registerModal" wrapClassName="jeecg-online-detail-modal" @commentOpen="handleCommentOpen">
    <template #footer>
      <a-row>
        <a-col :span="24 - commentSpan">
          <a-button v-if="cancelBtnCfg.enabled" key="back" @click="handleCancel">
            <span>{{ cancelBtnCfg.buttonName }}</span>
          </a-button>
          <slot name="footerBtn"></slot>
        </a-col>
      </a-row>
    </template>
    <online-form-detail ref="onlineFormCompRef" :id="id" :form-template="formTemplate" :show-sub="showSub" :themeTemplate="isShare && themeTemplate === 'erp' ? 'normal' : themeTemplate" :isShare="isShare" @rendered="renderSuccess" />

    <template #comment>
      <comment-panel ref="commentPanelRef" :tableId="tableId" :tableName="tableName" :dataId="formDataId"></comment-panel>
    </template>
  </BasicModal>
</template>

<script lang="ts">
  import { defineComponent, watch, ref } from 'vue';
  import { BasicModal } from '/@/components/Modal';
  import OnlineFormDetail from '../comp/OnlineFormDetail.vue';
  import { useAutoModal } from '../../hooks/auto/useAutoModal';
  import CommentPanel from '/@/components/jeecg/comment/CommentPanel.vue'
  import { ERPSUBTABLE, INNER_TABLE } from '../../util/constant';

  export default defineComponent({
    name: 'OnlineDetailModal',
    props: {
      id: {
        type: String,
        required: false,
        default: '',
      },
      // 为了区分来源，编码时主要是erp子表有特殊处理
      source: {
        type: String,
        default: '',
      },
      cancelBtnCfg: {
        type: Object,
        default: () => {
          return {
            enabled: true,
            buttonName: '关闭',
            buttonIcon: '',
          }
        }
      },
      // update-begin--author:liaozhiyang---date:20260807---for：【LHZP-166】修复erp风格的外部链接没子表
      isShare: {
        type: Boolean,
        default: false,
      },
      // update-end--author:liaozhiyang---date:20260807---for：【LHZP-166】修复erp风格的外部链接没子表
    },
    components: {
      BasicModal,
      OnlineFormDetail,
      CommentPanel
    },
    emits: ['success', 'register','formConfig'],
    setup(props, {emit}) {
      console.log('进入表单弹框》》》》modal');

      const commentPanelRef = ref();
      const commentSpan = ref(0);
      function reloadComment(){
        if(commentPanelRef.value)
          commentPanelRef.value.reload();
      }
      // update-begin--author:liaozhiyang---date:20260807---for：【LHZP-7】外部链接开启了评论后，编辑数据按钮错乱
      const handleCommentOpen = (visible, span) => {
        commentSpan.value = span;
      };
      // update-end--author:liaozhiyang---date:20260807---for：【LHZP-7】外部链接开启了评论后，编辑数据按钮错乱
      
      const {
        title,
        modalWidth,
        registerModal,
        cgButtonList,
        handleCgButtonClick,
        disableSubmit,
        handleSubmit,
        submitLoading,
        handleCancel,
        handleFormConfig,
        onlineFormCompRef,
        formTemplate,
        isTreeForm,
        pidFieldName,
        renderSuccess,
        formRendered,
        showSub,
        tableName,
        tableId,
        formDataId,
        enableComment,
        themeTemplate,
      } = useAutoModal(false, { emit }, reloadComment);

      // 监听id变化 表单重新渲染
      watch(() => props.id, renderFormItems, { immediate: true });
      async function renderFormItems() {
        formRendered.value = false;
        if (!props.id) {
          return;
        }
        console.log('重新渲染表单》》》》modal');
        // update-begin--author:liaozhiyang---date:20240426---for：【issues/6124】当用户没有【Online表单开发】页面的权限时用户无权查看从表新增和详情的数据
        let params: any = {};
        //update-begin---author:wangshuai---date:2025-10-21---for:【issues/8933】内嵌子表主题（一对多）列表点+号展开明细提示：无权限访问(操作)---
        if (props.source === ERPSUBTABLE || props.source === INNER_TABLE) {
        //update-end---author:wangshuai---date:2025-10-21---for:【issues/8933】内嵌子表主题（一对多）列表点+号展开明细提示：无权限访问(操作)---
          params.tabletype = 3;
        }
        await handleFormConfig(props.id, params);
        // update-end--author:liaozhiyang---date:20240426---for：【issues/6124】当用户没有【Online表单开发】页面的权限时用户无权查看从表新增和详情的数据
      }

      const that = {
        title,
        onlineFormCompRef,
        renderSuccess,
        registerModal,
        handleSubmit,
        handleCancel,
        modalWidth,
        formTemplate,
        disableSubmit,
        cgButtonList,
        handleCgButtonClick,
        isTreeForm,
        pidFieldName,
        submitLoading,
        showSub,
        tableName,
        tableId,
        formDataId,
        enableComment,
        commentPanelRef,
        themeTemplate,
        handleCommentOpen,
        commentSpan,
      };

      return that;
    },
  });
</script>

<style scoped></style>
