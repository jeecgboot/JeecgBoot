<template>
  <Tooltip placement="top" v-bind="getBindProps">
    <template #title>
      <span>{{ t('component.table.settingFullScreen') }}</span>
    </template>
    <FullscreenOutlined @click="toggle" v-if="!isFullscreen" />
    <FullscreenExitOutlined @click="toggle" v-else />
  </Tooltip>
</template>
<script lang="ts">
  import { computed, defineComponent } from 'vue';
  import { Tooltip } from 'ant-design-vue';
  import { FullscreenOutlined, FullscreenExitOutlined } from '@ant-design/icons-vue';
  import { useFullscreen } from '@vueuse/core';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useTableContext } from '../../hooks/useTableContext';

  export default defineComponent({
    name: 'FullScreenSetting',
    props: {
      isMobile: Boolean,
    },
    components: {
      FullscreenExitOutlined,
      FullscreenOutlined,
      Tooltip,
    },

    setup(props) {
      const table = useTableContext();
      const { t } = useI18n();
      const { toggle, isFullscreen } = useFullscreen(table.wrapRef);
      const getBindProps = computed(() => {
        let obj = {};
        if (props.isMobile) {
          obj['visible'] = false;
        }
        return obj;
      });
      return {
        getBindProps,
        toggle,
        isFullscreen,
        t,
      };
    },
  });
</script>
