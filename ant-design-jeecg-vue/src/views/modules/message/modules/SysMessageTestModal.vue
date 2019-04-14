<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">

    <a-spin :spinning="confirmLoading">
      <a-form>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="模板标题">
          <a-input disabled v-model="templateName"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="模板内容">
          <a-textarea disabled v-model="templateContent" :autosize="{ minRows: 5, maxRows: 8 }"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="测试数据">
          <a-textarea placeholder="请输入json格式测试数据" v-model="testData" :autosize="{ minRows: 5, maxRows: 8 }"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="消息类型">
          <j-dict-select-tag
            v-model="msgType"
            placeholder="请选择消息类型"
            dictCode="msgType"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="消息接收方">
          <a-input placeholder="请输入消息接收方" v-model="receiver"/>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import {httpAction} from '@/api/manage'

  export default {
    name: "SysMessageTestModal",
    data() {
      return {
        title: "操作",
        visible: false,
        model: {},
        labelCol: {
          xs: {span: 24},
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },

        confirmLoading: false,
        url: {
          send: "/message/sysMessageTemplate/sendMsg",
        },
        templateName: "",
        templateContent: "",
        receiver: "",
        msgType: "",
        testData: "",
        sendParams: {}
      }
    },
    methods: {
      open(record) {
        this.sendParams.templateCode = record.templateCode;
        this.templateName = record.templateName;
        this.templateContent = record.templateContent;
        this.testData = record.templateTestJson;
        this.visible = true;
      },
      close() {
        this.receiver = "";
        this.msgType = "";
        this.sendParams = {};
        this.visible = false;
      },
      handleOk() {
        let httpurl = this.url.send;
        let method = 'post';
        this.sendParams.testData = this.testData;
        this.sendParams.receiver = this.receiver;
        this.sendParams.msgType = this.msgType;
        httpAction(httpurl, this.sendParams, method).then((res) => {
          if (res.success) {
            this.$message.success(res.message);
          } else {
            this.$message.warning(res.message);
          }
        }).finally(() => {
          this.confirmLoading = false;
          this.close();
        })
      },
      handleCancel() {
        this.close()
      },
    }
  }
</script>

<style scoped>

</style>