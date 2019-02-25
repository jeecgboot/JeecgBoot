<template>
  <a-modal
    title="操作"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="唯一识别码"
          hasFeedback
        >
          <a-input placeholder="唯一识别码" disabled="disabled" v-decorator="[ 'id', {rules: []} ]" />
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="角色名称"
          hasFeedback >
          <a-input placeholder="起一个名字" v-decorator="[ 'name', {rules: [{ required: true, message: '不起一个名字吗？' }] }]" />
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="状态"
          hasFeedback >
          <a-select v-decorator="[ 'status', {rules: []} ]">
            <a-select-option :value="1">正常</a-select-option>
            <a-select-option :value="2">禁用</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="描述"
          hasFeedback
        >
          <a-textarea :rows="5" placeholder="..." v-decorator="[ 'describe', { rules: [] } ]" />
        </a-form-item>

        <a-divider/>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="拥有权限"
          hasFeedback
        >
          <a-row :gutter="16" v-for="(permission, index) in permissions" :key="index">
            <a-col :span="4">
              {{ permission.name }}：
            </a-col>
            <a-col :span="20">
              <a-checkbox
                v-if="permission.actionsOptions.length > 0"
                :indeterminate="permission.indeterminate"
                :checked="permission.checkedAll"
                @change="onChangeCheckAll($event, permission)">
                全选
              </a-checkbox>
              <a-checkbox-group :options="permission.actionsOptions" v-model="permission.selected" @change="onChangeCheck(permission)" />
            </a-col>
          </a-row>

        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import { getPermissions } from '@/api/manage'
  import { actionToObject } from '@/utils/permissions'
  import pick from 'lodash.pick'

  export default {
    name: "RoleModal",
    data () {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        visible: false,
        confirmLoading: false,
        mdl: {},

        form: this.$form.createForm(this),
        permissions: []
      }
    },
    created () {
      this.loadPermissions()
    },
    methods: {
      add () {
        this.edit({ id: 0 })
      },
      edit (record) {
        this.mdl = Object.assign({}, record)
        this.visible = true

        // 有权限表，处理勾选
        if (this.mdl.permissions && this.permissions) {
          // 先处理要勾选的权限结构
          const permissionsAction = {}
          this.mdl.permissions.forEach(permission => {
            permissionsAction[permission.permissionId] = permission.actionEntitySet.map(entity => entity.action)
          })
          // 把权限表遍历一遍，设定要勾选的权限 action
          this.permissions.forEach(permission => {
            permission.selected = permissionsAction[permission.id]
          })
        }

        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.mdl, 'id', 'name', 'status', 'describe'))
        })
        console.log('this.mdl', this.mdl)

      },
      close () {
        this.$emit('close')
        this.visible = false
      },
      handleOk () {
        const _this = this
        // 触发表单验证
        this.form.validateFields((err, values) => {
          // 验证表单没错误
          if (!err) {
            console.log('form values', values)

            _this.confirmLoading = true
            // 模拟后端请求 2000 毫秒延迟
            new Promise((resolve) => {
              setTimeout(() => resolve(), 2000)
            }).then(() => {
              // Do something
              _this.$message.success('保存成功')
              _this.$emit('ok')
            }).catch(() => {
              // Do something
            }).finally(() => {
              _this.confirmLoading = false
              _this.close()
            })
          }
        })
      },
      handleCancel () {
        this.close()
      },
      onChangeCheck (permission) {
        permission.indeterminate = !!permission.selected.length && (permission.selected.length < permission.actionsOptions.length)
        permission.checkedAll = permission.selected.length === permission.actionsOptions.length
      },
      onChangeCheckAll (e, permission) {
        Object.assign(permission, {
          selected: e.target.checked ? permission.actionsOptions.map(obj => obj.value) : [],
          indeterminate: false,
          checkedAll: e.target.checked
        })
      },
      loadPermissions () {
        getPermissions().then(res => {
          let result = res.result
          this.permissions = result.map(permission => {
            const options = actionToObject(permission.actionData)
            permission.checkedAll = false
            permission.selected = []
            permission.indeterminate = false
            permission.actionsOptions = options.map(option => {
              return {
                label: option.describe,
                value: option.action
              }
            })
            return permission
          })
        })
      }

    }
  }
</script>

<style scoped>

</style>