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
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="规则Code">
          <a-input placeholder="请输入规则Code" v-decorator="['ruleCode', validatorRules.ruleCode]"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="规则描述">
          <a-textarea placeholder="请输入规则描述" v-decorator="['ruleDescription', {}]"/>
        </a-form-item>

      </a-form>
      <!-- 规则设计 -->
      <a-tabs v-model="tabs.activeKey">
        <a-tab-pane tab="局部规则" :key="tabs.design.key" forceRender>
          <a-alert type="info" showIcon message="局部规则按照你输入的位数有序的校验。"/>
          <j-editable-table
            ref="designTable"
            dragSort
            rowNumber
            :maxHeight="240"
            :columns="tabs.design.columns"
            :dataSource="tabs.design.dataSource"
            style="margin-top: 8px;"
          >

            <template #action="props">
              <my-action-button :rowEvent="props"/>
            </template>

          </j-editable-table>
        </a-tab-pane>
        <a-tab-pane tab="全局规则" :key="tabs.global.key" forceRender>
          <j-editable-table
            ref="globalTable"
            dragSort
            rowNumber
            actionButton
            :maxHeight="240"
            :columns="tabs.global.columns"
            :dataSource="tabs.global.dataSource"
          >

            <template #actionButtonAfter>
              <a-alert type="info" showIcon message="全局规则可校验用户输入的所有字符；全局规则的优先级比局部规则的要高。" style="margin-bottom: 8px;"/>
            </template>

            <template #action="props">
              <my-action-button :rowEvent="props" allowEmpty/>
            </template>

          </j-editable-table>
        </a-tab-pane>
      </a-tabs>
    </a-spin>
  </a-modal>
</template>

<script>
  import pick from 'lodash.pick'
  import { httpAction } from '@/api/manage'
  import { validateDuplicateValue, alwaysResolve, failedSymbol } from '@/utils/util'
  import { FormTypes } from '@/utils/JEditableTableUtil'
  import JEditableTable from '@comp/jeecg/JEditableTable'

  export default {
    name: 'SysCheckRuleModal',
    components: {
      JEditableTable,
      'my-action-button': {
        props: { rowEvent: Object, allowEmpty: Boolean },
        methods: {
          confirmIsShow() {
            const { index, allValues: { inputValues } } = this.rowEvent
            let value = inputValues[index]
            return value.digits || value.pattern
          },
          handleLineAdd() {
            const { target } = this.rowEvent
            target.add()
          },
          handleLineDelete() {
            const { rowId, target } = this.rowEvent
            target.removeRows(rowId)
          },
          renderDeleteButton() {
            if (this.allowEmpty || this.rowEvent.index > 0) {
              if (this.confirmIsShow()) {
                return (
                  <a-popconfirm title="确定要删除吗？" onConfirm={this.handleLineDelete}>
                    <a-button icon="minus"/>
                  </a-popconfirm>
                )
              } else {
                return (
                  <a-button icon="minus" onClick={this.handleLineDelete}/>
                )
              }
            }
            return ''
          },
        },
        render() {
          return (
            <div>
              <a-button onClick={this.handleLineAdd} icon="plus"/>
              &nbsp;
              {this.renderDeleteButton()}
            </div>
          )
        }
      }
    },
    data() {
      return {
        title: '操作',
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules: {
          ruleName: { rules: [{ required: true, message: '请输入规则名称!' },] },
          ruleCode: {
            rules: [
              { required: true, message: '请输入规则Code!' },
              { validator: (rule, value, callback) => validateDuplicateValue('sys_check_rule', 'rule_code', value, this.model.id, callback) }
            ]
          },
        },
        tabs: {
          activeKey: 'design',
          global: {
            key: 'global',
            columns: [
              {
                title: '优先级',
                key: 'priority',
                width: '15%',
                type: FormTypes.select,
                defaultValue: '1',
                options: [
                  { title: '优先运行', value: '1' },
                  { title: '最后运行', value: '0' },
                ],
                validateRules: []
              },
              {
                title: '规则（正则表达式）',
                key: 'pattern',
                width: '50%',
                type: FormTypes.input,
                validateRules: [
                  { required: true, message: '规则不能为空' },
                  { handler: this.validatePatternHandler },
                ]
              },
              {
                title: '提示文本',
                key: 'message',
                width: '20%',
                type: FormTypes.input,
                validateRules: [
                  { required: true, message: '${title}不能为空' },
                ]
              },
              {
                title: '操作',
                key: 'action',
                width: '15%',
                slotName: 'action',
                type: FormTypes.slot
              }
            ],
            dataSource: [],
          },
          design: {
            key: 'design',
            columns: [
              {
                title: '位数',
                key: 'digits',
                width: '15%',
                type: FormTypes.inputNumber,
                validateRules: [
                  { required: true, message: '${title}不能为空' },
                  { pattern: /^[1-9]\d*$/, message: '请输入零以上的正整数' },
                ]
              },
              {
                title: '规则（正则表达式）',
                key: 'pattern',
                width: '50%',
                type: FormTypes.input,
                validateRules: [
                  { required: true, message: '规则不能为空' },
                  { handler: this.validatePatternHandler }
                ]
              },
              {
                title: '提示文本',
                key: 'message',
                width: '20%',
                type: FormTypes.input,
                validateRules: [
                  { required: true, message: '${title}不能为空' },
                ]
              },
              {
                title: '操作',
                key: 'action',
                width: '15%',
                slotName: 'action',
                type: FormTypes.slot
              },
            ],
            dataSource: [],
          }
        },
        url: {
          add: '/sys/checkRule/add',
          edit: '/sys/checkRule/edit',
        },
      }
    },
    created() {
    },
    methods: {

      validatePatternHandler(type, value, row, column, callback, target) {
        if (type === 'blur' || type === 'getValues') {
          try {
            new RegExp(value)
            callback(true)
          } catch (e) {
            callback(false, '请输入正确的正则表达式')
          }
        } else {
          callback(true) // 不填写或者填写 null 代表不进行任何操作
        }
      },

      add() {
        this.edit({})
      },
      edit(record) {
        this.form.resetFields()
        this.tabs.activeKey = this.tabs.design.key
        this.tabs.global.dataSource = []
        this.tabs.design.dataSource = [{ digits: '', pattern: '', message: '' }]
        this.model = Object.assign({}, record)
        this.visible = true
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'ruleName', 'ruleCode', 'ruleDescription'))
          // 子表数据
          let ruleJson = this.model.ruleJson
          if (ruleJson) {
            let ruleList = JSON.parse(ruleJson)
            // 筛选出全局规则和局部规则
            let global = [], design = [], priority = '1'
            ruleList.forEach(rule => {
              if (rule.digits === '*') {
                global.push(Object.assign(rule, { priority }))
              } else {
                priority = '0'
                design.push(rule)
              }
            })
            this.tabs.global.dataSource = global
            this.tabs.design.dataSource = design
          }
        })
      },
      close() {
        this.$emit('close')
        this.visible = false
      },
      handleOk() {
        Promise.all([
          // 主表单校验
          alwaysResolve(new Promise((resolve, reject) => {
            this.form.validateFields((error, values) => error ? reject(error) : resolve(values))
          })),
          // 局部规则子表校验
          alwaysResolve(this.$refs.designTable.getValuesPromise),
          // 全局规则子表校验
          alwaysResolve(this.$refs.globalTable.getValuesPromise),
        ]).then(results => {
          let [mainResult, designResult, globalResult] = results

          if (mainResult.type === failedSymbol) {
            return Promise.reject('主表校验未通过')
          } else if (designResult.type === failedSymbol) {
            this.tabs.activeKey = this.tabs.design.key
            return Promise.reject('局部规则子表校验未通过')
          } else if (globalResult.type === failedSymbol) {
            this.tabs.activeKey = this.tabs.global.key
            return Promise.reject('全局规则子表校验未通过')
          } else {
            // 所有校验已通过，这一步是整合数据
            let mainValues = mainResult.data, globalValues = globalResult.data, designValues = designResult.data

            // 整合两个子表的数据
            let firstGlobal = [], afterGlobal = []
            globalValues.forEach(v => {
              v.digits = '*'
              if (v.priority === '1') {
                firstGlobal.push(v)
              } else {
                afterGlobal.push(v)
              }
            })
            let concatValues = firstGlobal.concat(designValues).concat(afterGlobal)
            let subValues = concatValues.map(i => pick(i, 'digits', 'pattern', 'message'))

            // 生成 formData，用于传入后台
            let ruleJson = JSON.stringify(subValues)
            let formData = Object.assign(this.model, mainValues, { ruleJson })

            // 判断请求方式和请求地址，并发送请求
            let method = 'post', httpUrl = this.url.add
            if (this.model.id) {
              method = 'put'
              httpUrl = this.url.edit
            }
            this.confirmLoading = true
            return httpAction(httpUrl, formData, method)
          }
        }).then((res) => {
          if (res.success) {
            this.$message.success(res.message)
            this.$emit('ok')
            this.close()
          } else {
            this.$message.warning(res.message)
          }
        }).catch(e => {
          console.error(e)
        }).finally(() => {
          this.confirmLoading = false
        })
      },
      handleCancel() {
        this.close()
      },

    }
  }
</script>

<style lang="less" scoped></style>