<template>
  <BasicDrawer v-bind="$attrs" :isDetail="true" title="Drawer Title5">
    <p class="h-20"> Content Message </p>
    <template #titleToolbar> toolbar </template>
  </BasicDrawer>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicDrawer } from '/@/components/Drawer';
  export default defineComponent({
    components: { BasicDrawer },
  });
</script>
