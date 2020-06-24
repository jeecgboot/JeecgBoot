<template>
  <a-modal
    title="功能测试"
    :width="800"
    :visible="visible"
    @ok="visible=false"
    @cancel="visible=false"
  >
    <a-form :form="form">
      <a-form-item label="功能测试">
        <a-input placeholder="请输入" v-decorator="['test', validatorRules.test]" @change="e=>testValue=e.target.value"/>
      </a-form-item>
    </a-form>
    <a-row type="flex" :gutter="8">
      <a-col v-for="(str,index) of testValue" :key="index">
        <a-row>
          <a-col>
            <a-input :value="str" style="text-align: center;width: 40px;"/>
          </a-col>
          <a-col style="text-align: center;">{{index+1}}</a-col>
        </a-row>
      </a-col>
    </a-row>

  </a-modal>
</template>

<script>
  import { validateCheckRule } from '@/utils/util'

  export default {
    name: 'SysCheckRuleModal',
    data() {
      return {
        title: '操作',
        visible: false,
        ruleCode: '',
        testValue: '',
        form: this.$form.createForm(this),
        validatorRules: {
          test: {
            rules: [{ validator: (rule, value, callback) => validateCheckRule(this.ruleCode, value, callback) }]
          }
        },
      }
    },
    methods: {
      open(ruleCode) {
        this.ruleCode = ruleCode
        this.form.resetFields()
        this.testValue = ''
        this.visible = true
      },
    }
  }
</script>

<style lang="less" scoped></style>