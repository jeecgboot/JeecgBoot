<template>
  <Card hoverable :style="{ width: '240px', background: '#fff' }">
    <template #cover>
      <img alt="example" src="https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png" />
    </template>
    <CardMeta title="懒加载组件" />
  </Card>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { Card } from 'ant-design-vue';

  export default defineComponent({
    components: { CardMeta: Card.Meta, Card },
    setup() {
      return {};
    },
  });
</script>
