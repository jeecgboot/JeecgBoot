<template>
  <Button v-bind="getBindValue" :class="getButtonClass" @click="onClick">
    <template v-if="preIcon" #icon>
      <Icon :icon="preIcon" :size="iconSize" />
    </template>
    <template #default="data">
      <slot v-bind="data || {}"></slot>
      <Icon :icon="postIcon" v-if="postIcon" :size="iconSize" />
    </template>
  </Button>
</template>

<script lang="ts">
  import { defineComponent } from 'vue';
  export default defineComponent({
    name: 'AButton',
    inheritAttrs: false,
  });
</script>
<script lang="ts" setup>
  import { computed, unref } from 'vue';
  import { Button } from 'ant-design-vue';
  import Icon from '/@/components/Icon/src/Icon.vue';
  import { buttonProps } from './props';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  const props = defineProps(buttonProps);
  // get component class
  const attrs = useAttrs({ excludeDefaultKeys: false });
  const getButtonClass = computed(() => {
    const { color, disabled } = props;
    return [
      {
        [`ant-btn-${color}`]: !!color,
        [`is-disabled`]: disabled,
      },
    ];
  });

  // get inherit binding value
  const getBindValue = computed(() => ({ ...unref(attrs), ...props }));
</script>
