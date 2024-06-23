<template>
  <Tooltip :title="t('layout.header.tooltipErrorLog')" placement="bottom" :mouseEnterDelay="0.5" @click="handleToErrorList">
    <Badge :count="getCount" :offset="[0, 10]" :overflowCount="99">
      <Icon icon="ion:bug-outline" />
    </Badge>
  </Tooltip>
</template>
<script lang="ts">
  import { defineComponent, computed } from 'vue';
  import { Tooltip, Badge } from 'ant-design-vue';
  import Icon from '/@/components/Icon';

  import { useI18n } from '/@/hooks/web/useI18n';
  import { useErrorLogStore } from '/@/store/modules/errorLog';
  import { PageEnum } from '/@/enums/pageEnum';

  import { useRouter } from 'vue-router';

  export default defineComponent({
    name: 'ErrorAction',
    components: { Icon, Tooltip, Badge },

    setup() {
      const { t } = useI18n();
      const { push } = useRouter();
      const errorLogStore = useErrorLogStore();

      const getCount = computed(() => errorLogStore.getErrorLogListCount);

      function handleToErrorList() {
        push(PageEnum.ERROR_LOG_PAGE).then(() => {
          errorLogStore.setErrorLogListCount(0);
        });
      }

      return {
        t,
        getCount,
        handleToErrorList,
      };
    },
  });
</script>
