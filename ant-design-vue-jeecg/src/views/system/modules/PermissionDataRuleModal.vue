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
      <a-form-model ref="form" :model="model" :rules="validatorRules">

        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          prop="ruleName"
          label="规则名称">
          <a-input placeholder="请输入规则名称" v-model="model.ruleName"/>
        </a-form-model-item>
        <a-form-model-item
          v-show="showRuleColumn"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          prop="ruleColumn"
          label="规则字段">
          <a-input placeholder="请输入规则字段" v-model.trim="model.ruleColumn"/>
        </a-form-model-item>
        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          prop="ruleConditions"
          label="条件规则">
          <j-dict-select-tag @input="handleChangeRuleCondition" v-model="model.ruleConditions" placeholder="请输入条件规则" dictCode="rule_conditions"/>
        </a-form-model-item>
        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          prop="ruleValue"
          label="规则值">
          <a-input placeholder="请输入规则值" v-model.trim="model.ruleValue"/>
        </a-form-model-item>

        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="状态">
          <a-radio-group buttonStyle="solid" v-model="model.status">
            <a-radio-button value="1">有效</a-radio-button>
            <a-radio-button value="0">无效</a-radio-button>
          </a-radio-group>
        </a-form-model-item>

      </a-form-model>
    </a-spin>
  </a-modal>
</template>
<script>
  import { httpAction } from '@/api/manage'

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
        permissionId: '',
        validatorRules: {
          ruleConditions:  [{required: true, message: '请选择条件!'}],
          ruleName:[{required: true, message: '请输入规则名称!'}],
          ruleValue:  [{required: true, message: '请输入规则值!'}],
          ruleColumn: []
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
        //初始化默认值
        this.edit({status:'1'})
      },
      edit(record) {
        this.model = Object.assign({}, record)
        if (record.permissionId) {
          this.model.permissionId = record.permissionId
        } else {
          this.model.permissionId = this.permissionId
        }
        this.visible = true
        this.initRuleCondition()
      },
      close() {
        this.$emit('close')
        this.visible = false
        this.$refs.form.resetFields()
      },
      handleOk() {
        const that = this
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
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
            httpAction(httpurl, this.model, method).then((res) => {
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
          }else{
            return false;
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
          this.model.ruleColumn=''
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