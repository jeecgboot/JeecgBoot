<template>
  <div class="p-5">
    多层级缓存-页面2
    <br />
    <Input />
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { Input } from 'ant-design-vue';
  export default defineComponent({
    name: 'Menu2Demo',
    components: { Input },
  });
</script>
