<template>
  <div>
    <a-form style="max-width: 500px; margin: 40px auto 0;">
      <a-form-item
        label="付款账户"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
      >
        <a-select value="1" placeholder="ant-design@alipay.com">
          <a-select-option value="1">ant-design@alipay.com</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item
        label="收款账户"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
      >
        <a-input-group :compact="true" style="display: inline-block; vertical-align: middle">
          <a-select defaultValue="alipay" style="width: 100px">
            <a-select-option value="alipay">支付宝</a-select-option>
            <a-select-option value="wexinpay">微信</a-select-option>
          </a-select>
          <a-input :style="{width: 'calc(100% - 100px)'}" value="test@example.com"/>
        </a-input-group>
      </a-form-item>
      <a-form-item
        label="收款人姓名"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
      >
        <a-input value="Alex" />
      </a-form-item>
      <a-form-item
        label="转账金额"
        :labelCol="{span: 5}"
        :wrapperCol="{span: 19}"
      >
        <a-input prefix="￥" value="5000" />
      </a-form-item>
      <a-form-item :wrapperCol="{span: 19, offset: 5}">
        <a-button type="primary" @click="nextStep">下一步</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
  export default {
    name: "Step1",
    methods: {
      nextStep () {
        this.$emit('nextStep')
      }
    }
  }
</script>

<style scoped>

</style>