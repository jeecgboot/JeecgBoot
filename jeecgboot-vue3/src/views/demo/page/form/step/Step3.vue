<template>
  <div class="step3">
    <a-result status="success" title="操作成功" sub-title="预计两小时内到账">
      <template #extra>
        <a-button type="primary" @click="redo"> 再转一笔 </a-button>
        <a-button> 查看账单 </a-button>
      </template>
    </a-result>
    <div class="desc-wrap">
      <a-descriptions :column="1" class="mt-5">
        <a-descriptions-item label="付款账户"> ant-design@alipay.com </a-descriptions-item>
        <a-descriptions-item label="收款账户"> test@example.com </a-descriptions-item>
        <a-descriptions-item label="收款人姓名"> Jeecg </a-descriptions-item>
        <a-descriptions-item label="转账金额"> 500元 </a-descriptions-item>
      </a-descriptions>
    </div>
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { Result, Descriptions } from 'ant-design-vue';
  export default defineComponent({
    components: {
      [Result.name]: Result,
      [Descriptions.name]: Descriptions,
      [Descriptions.Item.name]: Descriptions.Item,
    },
    emits: ['redo'],
    setup(_, { emit }) {
      return {
        redo: () => {
          emit('redo');
        },
      };
    },
  });
</script>
<style lang="less" scoped>
  .step3 {
    width: 600px;
    margin: 0 auto;
  }

  .desc-wrap {
    padding: 24px 40px;
    margin-top: 24px;
    background-color: @background-color-light;
  }
</style>
