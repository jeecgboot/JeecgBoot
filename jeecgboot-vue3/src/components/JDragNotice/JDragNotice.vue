<template>
  <div>
    <keep-alive>
      <component
        v-if="currentModal"
        v-bind="bindParams"
        :key="currentModal"
        :is="currentModal"
        @register="modalRegCache[currentModal].register"
        @reply="handReply"
        @selected="reloadPage"
      />
    </keep-alive>
    <!-- 历史弹出框 -->
    <HisTaskDealModal ref="taskDealRef" v-bind="bindParams" />
    <!-- 系统公告弹窗 -->
    <DynamicNotice ref="showDynamNotice" v-bind="bindParams" />
    <!-- eoa查看详情 -->
    <EoaDetailModal ref="detailRef" />
    <!-- 表单设计器弹窗 -->
    <DesformDataModal ref="desformRef" v-bind="bindParams" @added="handleDesformDataAdded" />
    <!-- 我的计划弹窗 -->
    <PlanModal ref="planRef" v-bind="bindParams" @success="reloadPage" />
  </div>
</template>

<script lang="ts">
  import { defineComponent, onMounted } from 'vue';
  import { useDragNotice } from '/@/hooks/web/useDragNotice';
  import EoaMailBoxInModal from '/@/views/super/eoa/email/components/EoaMailBoxInModal.vue';
  import DynamicNotice from '@/views/monitor/mynews/DynamicNotice.vue';
  import EoaDetailModal from '@/views/super/eoa/cmsoa/modules/EoaDetailModal.vue';
  import PlanModal from '/@/views/super/eoa/plan/components/PlanModal.vue';

  export default defineComponent({
    name: 'JDragNotice',
    components: {
      EoaDetailModal,
      DynamicNotice,
      EoaMailBoxInModal,
      PlanModal,
    },
    setup() {
      const {
        initDragWebSocket,
        currentModal,
        modalParams,
        modalRegCache,
        bindParams,
        taskDealRef,
        desformRef,
        handleDesformDataAdded,
        handReply,
        reloadPage,
      } = useDragNotice();

      onMounted(() => {
        initDragWebSocket();
      });

      return {
        currentModal,
        modalParams,
        modalRegCache,
        bindParams,
        taskDealRef,
        desformRef,
        handleDesformDataAdded,
        handReply,
        reloadPage,
      };
    },
  });
</script>

<style scoped lang="less"></style>
