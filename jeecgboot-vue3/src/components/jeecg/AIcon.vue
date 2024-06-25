<template>
  <Icon :icon="icon" :size="size"></Icon>
</template>

<script lang="ts">
  import { computed, defineComponent } from 'vue';
  import { Icon } from '/@/components/Icon';
  import { isEmpty } from '/@/utils/is';
  import { propTypes } from '/@/utils/propTypes';

  export default defineComponent({
    name: 'AIcon',
    components: { Icon },
    props: {
      icon: String,
      type: String,
      // 图标大小，默认 16
      size: propTypes.any,
      // 样式
      theme: propTypes.any,
    },
    setup(props) {
      const icon = computed(() => {
        if (props.icon && !isEmpty(props.icon)) {
          return props.icon;
        }
        let iconTheme = props.theme ? `-${props.theme}` : '';
        return `ant-design:${props.type}${iconTheme}`;
      });

      return {
        icon,
      };
    },
  });
</script>

<style scoped></style>
