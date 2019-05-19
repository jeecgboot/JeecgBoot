<template>
  <a-modal
    :title="title"
    :width="600"
    :visible="visible"
    @cancel="handleCancel"
    :okButtonProps="okButtonProps"
    cancelText="关闭">

    <div class="message">
      <div class="title">{{ record.titile }}</div>
      <div class="createBy">发布人：{{ record.sender }}</div>
      <div class="sendTime">发布时间：{{ record.sendTime }}</div>
      <a-divider />
      <p v-html="record.msgContent"></p>
    </div>

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
        okButtonProps:{
          style: {
            display:"none"
          },
        }
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

<style scoped>
  .message{
    width: 90%;
    margin-left: 5%;
    margin-right: 5%;
    min-height: 200px;
  }
  .title{
    width: 100%;
    font-size: 16px;
    text-align: center;
    font-weight: bolder;
    margin-bottom: 10px;
  }
  .createBy {
    width: 50%;
    text-align: left;
    padding-left: 5%;
    float: left;
  }
  .sendTime{
    width: 50%;
    text-align: right;
    padding-right: 5%;
    float: right;
  }
  .message .ant-divider .ant-divider-horizontal .ant-divider-with-text-right{
    font-size: 16px;
    font-weight: lighter!important;
  }
  .message p{
    font-size: 16px;
  }
  .message .ant-divider-horizontal{
    margin-top: 40px;
    margin-bottom: 8px;
  }
</style>