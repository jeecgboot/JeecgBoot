<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="查看详情" :showCancelBtn="false" :showOkBtn="false" :maxHeight="500">
    <iframe :src="frameSrc" class="detail-iframe" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { propTypes } from '/@/utils/propTypes';
  // 获取props
  defineProps({
    frameSrc: propTypes.string.def(''),
  });
  //表单赋值
  const [registerModal] = useModalInner();
</script>

<style scoped lang="less">
  .detail-iframe {
    border: 0;
    width: 100%;
    height: 100%;
    min-height: 500px;
  }
</style>
