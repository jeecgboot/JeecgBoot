<template>
  <MenuItem :key="itemKey">
    <span class="flex items-center">
      <Icon :icon="icon" class="mr-1" />
      <span>{{ text }}</span>
    </span>
  </MenuItem>
</template>
<script lang="ts">
  import { Menu } from 'ant-design-vue';

  import { computed, defineComponent, getCurrentInstance } from 'vue';

  import Icon from '/@/components/Icon/index';
  import { propTypes } from '/@/utils/propTypes';

  export default defineComponent({
    name: 'DropdownMenuItem',
    components: { MenuItem: Menu.Item, Icon },
    props: {
      // 【issues/6855】
      itemKey: propTypes.string,
      text: propTypes.string,
      icon: propTypes.string,
    },
    setup(props) {
      const instance = getCurrentInstance();
      // update-begin--author:liaozhiyang---date:20240717---for：【issues/6855】组件使用key作props报警告，改为itemKey
      const itemKey = computed(() => props.itemKey || instance?.vnode?.props?.itemKey);
      // update-end--author:liaozhiyang---date:20240717---for：【issues/6855】组件使用key作props报警告，改为itemKey
      return { itemKey };
    },
  });
</script>
