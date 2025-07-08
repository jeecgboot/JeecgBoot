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
    <!-- 系统公告弹窗 -->
    <DynamicNotice ref="showDynamNotice" v-bind="bindParams" />
  </div>
</template>

<script lang="ts">
  import { defineComponent, onMounted } from 'vue';
  import { useDragNotice } from '/@/hooks/web/useDragNotice';
  import DynamicNotice from '@/views/monitor/mynews/DynamicNotice.vue';

  export default defineComponent({
    name: 'JDragNotice',
    components: {
      DynamicNotice,
    },
    setup() {
      const {
        initDragWebSocket,
        currentModal,
        modalParams,
        modalRegCache,
        bindParams,
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
        reloadPage,
      };
    },
  });
</script>

<style scoped lang="less"></style>
