<template>
  <BasicDrawer v-bind="$attrs" @register="register" title="Drawer Title" width="50%">
    Drawer Info.
    <a-button type="primary" @click="closeDrawer"> 内部关闭drawer </a-button>
  </BasicDrawer>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  export default defineComponent({
    components: { BasicDrawer },
    setup() {
      const [register, { closeDrawer }] = useDrawerInner();
      return { register, closeDrawer };
    },
  });
</script>
