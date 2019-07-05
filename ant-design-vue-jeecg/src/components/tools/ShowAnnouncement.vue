<template>
  <a-modal
    width="60%"
    :visible="visible"
    :bodyStyle ="bodyStyle"
    @cancel="handleCancel"
    destroyOnClose
    :footer="null">

    <a-card style="width: 100%;height: 100%" class="daily-article" :loading="loading">
      <a-card-meta
        :title="record.titile"
        :description="'发布人：'+record.sender + ' 发布时间： ' + record.sendTime"/>
      <a-divider />
      <span v-html="record.msgContent" class="article-content"></span>
    </a-card>

  </a-modal>
</template>

<script>
  export default {
    name: "SysAnnouncementModal",
    components: {
    },
    data () {
      return {
        title:"通知消息",
        record: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        visible: false,
        loading: false,
        bodyStyle:{
          padding: "0",
          height:(window.innerHeight*0.8)+"px",
          "overflow-y":"auto"
        },
      }
    },
    created () {
    },
    methods: {
      detail (record) {
        this.visible = true;
        this.record = record;
      },
      handleCancel () {
        this.visible = false;
      },
    }
  }
</script>

<style scoped lang="less">
  .daily-article {
  .article-button {
    font-size: 1.2rem !important;
  }
  .ant-card-body {
    padding: 18px !important;
  }
  .ant-card-head {
    padding: 0 1rem;
  }
  .ant-card-meta {
    margin-bottom: 1rem;
  }
  .article-content {
  p {
    word-wrap: break-word;
    word-break: break-all;
    text-overflow: initial;
    white-space: normal;
    font-size: .9rem !important;
    margin-bottom: .8rem;
  }
  }
  }
</style>