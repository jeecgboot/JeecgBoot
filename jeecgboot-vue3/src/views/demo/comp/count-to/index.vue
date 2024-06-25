<template>
  <PageWrapper title="数字动画示例">
    <Card>
      <CardGrid class="count-to-demo-card">
        <CountTo prefix="$" :color="'#409EFF'" :startVal="1" :endVal="200000" :duration="8000" />
      </CardGrid>
      <CardGrid class="count-to-demo-card">
        <CountTo suffix="$" :color="'red'" :startVal="1" :endVal="300000" :decimals="2" :duration="6000" />
      </CardGrid>
      <CardGrid class="count-to-demo-card">
        <CountTo suffix="$" :color="'rgb(0,238,0)'" :startVal="1" :endVal="400000" :duration="7000" />
      </CardGrid>
      <CardGrid class="count-to-demo-card">
        <CountTo separator="-" :color="'rgba(138,43,226,.6)'" :startVal="10000" :endVal="500000" :duration="8000" />
      </CardGrid>
    </Card>
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { Card } from 'ant-design-vue';
  import { CountTo } from '/@/components/CountTo/index';
  import { PageWrapper } from '/@/components/Page';

  export default defineComponent({
    components: {
      Card,
      CardGrid: Card.Grid,
      CountTo,
      PageWrapper,
    },
  });
</script>
<style lang="less" scoped>
  .count-to-demo {
    &-card {
      width: 50%;
      font-size: 30px;
      text-align: center;
    }
  }
</style>
