<template>
  <BasicModal v-bind="$attrs" title="Modal Title" :helpMessage="['提示1', '提示2']" width="700px">
    <p class="h-20" v-for="index in 20" :key="index"> 根据屏幕高度自适应 </p>
  </BasicModal>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicModal } from '/@/components/Modal';
  export default defineComponent({
    components: { BasicModal },
    setup() {
      return {};
    },
  });
</script>
