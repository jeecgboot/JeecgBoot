<template>
  <span :class="`${prefixCls}__extra-redo`" @click="handleRedo">
    <SvgIcon name="reload-01"></SvgIcon>
  </span>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useTabs } from '/@/hooks/web/useTabs';
  import { SvgIcon } from '/@/components/Icon/index';
  export default defineComponent({
    name: 'TabRedo',
    components: { SvgIcon },

    setup() {
      const loading = ref(false);

      const { prefixCls } = useDesign('multiple-tabs-content');
      const { refreshPage } = useTabs();

      async function handleRedo() {
        loading.value = true;
        await refreshPage();
        setTimeout(() => {
          loading.value = false;
          // Animation execution time
        }, 1200);
      }
      return { prefixCls, handleRedo, loading };
    },
  });
</script>
