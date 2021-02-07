<template>
  <a-form @submit="handleSubmit" :form="form" class="form">
    <a-row class="form-row" :gutter="16">
      <a-col :lg="6" :md="12" :sm="24">
        <a-form-item label="仓库名">
          <a-input
            placeholder="请输入仓库名称"
            v-decorator="[
              'repository.name',
              {rules: [{ required: true, message: '请输入仓库名称', whitespace: true}]}
            ]" />
        </a-form-item>
      </a-col>
      <a-col :xl="{span: 7, offset: 1}" :lg="{span: 8}" :md="{span: 12}" :sm="24">
        <a-form-item
          label="仓库域名">
          <a-input
            addonBefore="http://"
            addonAfter=".com"
            placeholder="请输入"
            v-decorator="[
              'repository.domain',
              {rules: [{ required: true, message: '请输入仓库域名', whitespace: true}, {validator: validate}]}
            ]" />
        </a-form-item>
      </a-col>
      <a-col :xl="{span: 9, offset: 1}" :lg="{span: 10}" :md="{span: 24}" :sm="24">
        <a-form-item
          label="仓库管理员">
          <a-select placeholder="请选择管理员" v-decorator="[ 'repository.manager', {rules: [{ required: true, message: '请选择管理员'}]} ]">
            <a-select-option value="王同学">王同学</a-select-option>
            <a-select-option value="李同学">李同学</a-select-option>
            <a-select-option value="黄同学">黄同学</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
    </a-row>
    <a-row class="form-row" :gutter="16">
      <a-col :lg="6" :md="12" :sm="24">
        <a-form-item
          label="审批人">
          <a-select placeholder="请选择审批员" v-decorator="[ 'repository.auditor', {rules: [{ required: true, message: '请选择审批员'}]} ]">
            <a-select-option value="王晓丽">王晓丽</a-select-option>
            <a-select-option value="李军">李军</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :xl="{span: 7, offset: 1}" :lg="{span: 8}" :md="{span: 12}" :sm="24">
        <a-form-item
          label="生效日期">
          <a-range-picker
            style="width: 100%"
            v-decorator="[
              'repository.effectiveDate',
              {rules: [{ required: true, message: '请选择生效日期'}]}
            ]" />
        </a-form-item>
      </a-col>
      <a-col :xl="{span: 9, offset: 1}" :lg="{span: 10}" :md="{span: 24}" :sm="24">
        <a-form-item
          label="仓库类型">
          <a-select
            placeholder="请选择仓库类型"
            v-decorator="[
              'repository.type',
              {rules: [{ required: true, message: '请选择仓库类型'}]}
            ]" >
            <a-select-option value="公开">公开</a-select-option>
            <a-select-option value="私密">私密</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
    </a-row>
    <a-form-item v-if="showSubmit">
      <a-button htmlType="submit" >Submit</a-button>
    </a-form-item>
  </a-form>
</template>

<script>
  export default {
    name: "RepositoryForm",
    props: {
      showSubmit: {
        type: Boolean,
        default: false
      }
    },
    data () {
      return {
        form: this.$form.createForm(this)
      }
    },
    methods: {
      handleSubmit (e) {
        e.preventDefault()
        this.form.validateFields((err, values) => {
          if (!err) {
            this.$notification['error']({
              message: 'Received values of form:',
              description: values
            })
          }
        })
      },
      validate (rule, value, callback) {
        const regex = /^user-(.*)$/
        if (!regex.test(value)) {
          callback('需要以 user- 开头')
        }
        callback()
      }
    }
  }
</script>

<style scoped>

</style>