<template>
  <div class="m-5 result-success">
    <Result
      status="success"
      title="提交成功"
      sub-title="提交结果页用于反馈一系列操作任务的处理结果， 如果仅是简单操作，使用 Message 全局提示反馈即可。 本文字区域可以展示简单的补充说明，如果有类似展示 “单据”的需求，下面这个灰色区域可以呈现比较复杂的内容。"
    >
      <template #extra>
        <a-button key="console" type="primary"> 返回列表 </a-button>
        <a-button key="buy"> 查看项目 </a-button>
        <a-button key="buy"> 打印 </a-button>
      </template>
    </Result>
    <div class="result-success__content">
      <Descriptions title="项目名称">
        <DescriptionItem label="项目 ID"> 111222 </DescriptionItem>
        <DescriptionItem label="负责人"> Jeecg </DescriptionItem>
        <DescriptionItem label="生效时间"> 2016-12-12 ~ 2017-12-12 </DescriptionItem>
      </Descriptions>
      <Steps :current="1" progress-dot size="small">
        <Step title="创建项目">
          <template #description> <div>Jeecg</div> <p>2016-12-12 12:32</p> </template>
        </Step>
        <Step title="部门初审">
          <template #description>
            <p>Chad</p>
          </template>
        </Step>
        <Step title="财务复核" />
        <Step title="完成" />
      </Steps>
    </div>
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { Result, Steps, Descriptions } from 'ant-design-vue';
  export default defineComponent({
    components: {
      Result,
      Steps,
      Step: Steps.Step,
      Descriptions,
      DescriptionItem: Descriptions.Item,
    },
  });
</script>
<style lang="less" scoped>
  .result-success {
    padding: 48px 32px;
    background-color: @component-background;

    &__content {
      padding: 24px 40px;
      background-color: @background-color-light;
    }
  }
</style>
