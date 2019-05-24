<template>
  <a-modal
    :title="title"
    :width="600"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="字典名称">
          <a-input placeholder="请输入字典名称" v-decorator="[ 'dictName', validatorRules.dictName]"/>
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="字典编码">
          <a-input placeholder="请输入字典编码" v-decorator="[ 'dictCode', validatorRules.dictCode]"/>
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="描述">
          <a-input v-decorator="[ 'description']"/>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import pick from 'lodash.pick'
  import { addDict, editDict, duplicateCheck } from '@/api/api'

  export default {
    name: 'DictModal',
    data() {
      return {
        value: 1,
        title: '操作',
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules: {
          dictName: { rules: [{ required: true, message: '请输入字典名称!' }] },
          dictCode: {
            rules: [{ required: true, message: '请输入字典编码!' },
              { validator: this.validateDictCode }]
          }
        }
      }
    },
    created() {
    },
    methods: {
      validateDictCode(rule, value, callback) {
        // 重复校验
        var params = {
          tableName: 'sys_dict',
          fieldName: 'dict_code',
          fieldVal: value,
          dataId: this.model.id
        }
        duplicateCheck(params).then((res) => {
          if (res.success) {
            callback()
          } else {
            callback(res.message)
          }
        })
      },
      handleChange(value) {
        this.model.status = value
      },
      add() {
        this.edit({})
      },
      edit(record) {
        if (record.id) {
          this.visiblekey = true
        } else {
          this.visiblekey = false
        }
        this.form.resetFields()
        this.model = Object.assign({}, record)
        this.visible = true
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'dictName', 'dictCode', 'description'))
        })
      },
      // 确定
      handleOk() {
        const that = this
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true
            values.dictName = (values.dictName || '').trim()
            values.dictCode = (values.dictCode || '').trim()
            values.description = (values.description || '').trim()
            let formData = Object.assign(this.model, values)
            let obj
            console.log(formData)
            if (!this.model.id) {
              formData.delFlag = '1'
              obj = addDict(formData)
            } else {
              obj = editDict(formData)
            }
            obj.then((res) => {
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
      // 关闭
      handleCancel() {
        this.close()
      },
      close() {
        this.$emit('close')
        this.visible = false
      }
    }
  }
</script>