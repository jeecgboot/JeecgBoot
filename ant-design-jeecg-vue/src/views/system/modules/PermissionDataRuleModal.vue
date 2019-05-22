<template>
  <a-modal
    :title="title"
    :width="1000"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="规则名称">
          <a-input placeholder="请输入规则名称" v-decorator="['ruleName', validatorRules.ruleName]"/>
        </a-form-item>
        <a-form-item
          v-show="showRuleColumn"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="规则字段">
          <a-input placeholder="请输入规则字段" v-decorator="['ruleColumn', validatorRules.ruleColumn]"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="条件规则">
          <j-dict-select-tag @change="handleChangeRuleCondition" v-decorator="['ruleConditions', validatorRules.ruleConditions]" placeholder="请输入条件规则" :triggerChange="true" dictCode="rule_conditions"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="规则值">
          <a-input placeholder="请输入规则值" v-decorator="['ruleValue', validatorRules.ruleValue]"/>
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="状态">
          <a-radio-group buttonStyle="solid" v-decorator="['status',{initialValue:'1'}]">
            <a-radio-button value="1">有效</a-radio-button>
            <a-radio-button value="0">无效</a-radio-button>
          </a-radio-group>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>
<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'

  export default {
    name: 'PermissionDataRuleModal',
    data() {
      return {
        queryParam: {},
        title: '操作',
        visible: false,
        model: {},
        ruleConditionList: [],
        labelCol: {
          xs: {span: 24},
          sm: {span: 5}
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16}
        },
        confirmLoading: false,
        form: this.$form.createForm(this),
        permissionId: '',
        validatorRules: {
          ruleConditions: {rules: [{required: true, message: '请选择条件!'}]},
          ruleName: {rules: [{required: true, message: '请输入规则名称!'}]},
          ruleValue: {rules: [{required: true, message: '请输入规则值!'}]},
          ruleColumn: {rules: []}
        },
        url: {
          list: '/sys/dictItem/list',
          add: '/sys/permission/addPermissionRule',
          edit: '/sys/permission/editPermissionRule'
        },
        showRuleColumn:true
      }
    },
    created() {
    },
    methods: {
      add(permId) {
        this.permissionId = permId
        this.edit({})
      },
      edit(record) {
        this.form.resetFields()
        this.model = Object.assign({}, record)
        if (record.permissionId) {
          this.model.permissionId = record.permissionId
        } else {
          this.model.permissionId = this.permissionId
        }
        this.visible = true
        this.initRuleCondition()
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'status','ruleName', 'ruleColumn', 'ruleConditions', 'ruleValue'))
        })
      },
      close() {
        this.$emit('close')
        this.visible = false
      },
      handleOk() {
        const that = this
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true
            let httpurl = ''
            let method = ''
            if (!this.model.id) {
              httpurl += this.url.add
              method = 'post'
            } else {
              httpurl += this.url.edit
              method = 'put'
            }
            let formData = Object.assign(this.model, values)
            if(formData.ruleColumn && formData.ruleColumn.length>0){
              formData.ruleColumn = formData.ruleColumn.trim()
            }
            if(formData.ruleValue && formData.ruleValue.length>0){
              formData.ruleValue = formData.ruleValue.trim()
            }
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            }).finally(() => {
              that.confirmLoading = false
              that.close()
            })
          }
        })
      },
      handleCancel() {
        this.close()
      },
      initRuleCondition(){
        if(this.model.ruleConditions && this.model.ruleConditions=='USE_SQL_RULES'){
          this.showRuleColumn = false
        }else{
          this.showRuleColumn = true
        }
      },
      handleChangeRuleCondition(val){
        if(val=='USE_SQL_RULES'){
          this.form.setFieldsValue({
            ruleColumn:''
          })
          this.showRuleColumn = false
        }else{
          this.showRuleColumn = true
        }
      }
    }
  }
</script>

<style scoped>

</style>