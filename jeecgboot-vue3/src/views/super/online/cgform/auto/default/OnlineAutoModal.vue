<template>
  <BasicModal :title="title" @cancel="onCloseEvent" :enableComment="enableComment" :width="modalWidth" v-bind="$attrs" :maxHeight="800" @register="registerModal" wrapClassName="jeecg-online-modal" @ok="handleSubmit" @commentOpen="handleCommentOpen">
    <template #footer>
      <a-row>
        <a-col :span='24 - commentSpan'>
          <a-button
            v-for="btn in cgButtonList"
            :key="btn.id"
            type="primary"
            @click="handleCgButtonClick(btn.optType, btn.buttonCode)"
            :preIcon="btn.buttonIcon ? 'ant-design:' + btn.buttonIcon : ''"
          >
            {{ btn.buttonName }}
          </a-button>

          <a-button
              v-if="!disableSubmit && confirmBtnCfg.enabled"
              key="submit"
              type="primary"
              :preIcon="confirmBtnCfg.buttonIcon"
              :loading="submitLoading"
              @click="handleSubmit"
          >
            <span>{{ confirmBtnCfg.buttonName }}</span>
          </a-button>
          <a-button v-if="cancelBtnCfg.enabled" key="back" @click="handleCancel">
            <span>{{ cancelBtnCfg.buttonName }}</span>
          </a-button>
        </a-col>
      </a-row>
    </template>
    <online-form
      v-bind="$attrs"
      ref="onlineFormCompRef"
      :id="id"
      :disabled="disableSubmit"
      :form-template="formTemplate"
      :isTree="isTreeForm"
      :pidField="pidFieldName"
      :themeTemplate="$attrs.isShare && themeTemplate === 'erp' ? 'normal' : themeTemplate"
      :cgBIBtnMap="cgBIBtnMap"
      :buttonSwitch="buttonSwitch"
      @rendered="renderSuccess"
      @success="handleSuccess"
    >
    </online-form>

    <template #comment>
      <comment-panel ref="commentPanelRef" :tableId="tableId" :tableName="tableName" :dataId="formDataId"></comment-panel>
    </template>
 
  </BasicModal>
</template>

<script lang="ts">
  import { defineComponent, watch, ref } from 'vue';
  import { BasicModal } from '/@/components/Modal';
  import OnlineForm from '../comp/OnlineForm.vue';
  import { useAutoModal } from '../../hooks/auto/useAutoModal';
  import CommentPanel from '/@/components/jeecg/comment/CommentPanel.vue'
  import { ERPSUBTABLE } from '../../util/constant';
  export default defineComponent({
    name: 'OnlineAutoModal',
    props: {
      id: {
        type: String,
        default: '',
      },
      // 为了区分来源，编码时主要是erp子表有特殊处理
      source: {
        type: String,
        default: '',
      },
      buttonSwitch: Object,
      cgBIBtnMap: Object,
      confirmBtnCfg: {
        type: Object,
        default: () => {
          return {
            enabled: true,
            buttonName: '确定',
            buttonIcon: '',
          }
        }
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
    },
    components: {
      BasicModal,
      OnlineForm,
      CommentPanel
    },
    emits: ['success', 'register', 'formConfig'],
    setup(props, { emit }) {
      console.log('进入表单弹框》》》》modal');
      const commentPanelRef = ref();
      const commentSpan = ref(0);
      function reloadComment(){
        if(commentPanelRef.value)
        commentPanelRef.value.reload();
      }
      const {
        title,
        modalWidth,
        registerModal,
        closeModal,
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
        tableName,
        tableId,
        formDataId,
        enableComment,
        onCloseEvent,
        themeTemplate,
      } = useAutoModal(false, { emit }, reloadComment);

      function handleSuccess(formData) {
        emit('success', formData);
        closeModal();
        // 提交完成 触发关闭事件
        onCloseEvent();
      }

      // 监听id变化 表单重新渲染
      watch(() => props.id, renderFormItems, { immediate: true });
      async function renderFormItems() {
        formRendered.value = false;
        if (!props.id) {
          return;
        }
        console.log('重新渲染表单》》》》modal');
        // update-begin--author:liaozhiyang---date:20240426---for：【issues/6124】当用户没有【Online表单开发】页面的权限时用户无权查看从表新增和详情的数据
        const params: any = {};
        if (props.source === ERPSUBTABLE) {
          params.tabletype = 3;
        }
        await handleFormConfig(props.id, params);
        // update-end--author:liaozhiyang---date:20240426---for：【issues/6124】当用户没有【Online表单开发】页面的权限时用户无权查看从表新增和详情的数据
      }
      // update-begin--author:liaozhiyang---date:20240528---for：【TV360X-485】开启评论之后弹窗按钮居右隔一个评论的距离
      const handleCommentOpen =  (visible, span) => { 
        console.log('评论是否展开：', visible);
        commentSpan.value = span;
      }
      // update-end--author:liaozhiyang---date:20240528---for：【TV360X-485】开启评论之后弹窗按钮居右隔一个评论的距离
      const that = {
        title,
        onlineFormCompRef,
        renderSuccess,
        registerModal,
        handleSubmit,
        handleSuccess,
        handleCancel,
        modalWidth,
        formTemplate,
        disableSubmit,
        cgButtonList,
        handleCgButtonClick,
        isTreeForm,
        pidFieldName,
        submitLoading,
        tableName,
        tableId,
        formDataId,
        enableComment,
        commentPanelRef,
        onCloseEvent,
        themeTemplate,
        handleCommentOpen,
        commentSpan,
      };

      return that;
    },
  });
</script>

<style lang="less" scoped></style>
