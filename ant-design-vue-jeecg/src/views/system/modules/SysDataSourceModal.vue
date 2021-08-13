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
      <a-form :form="form">

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="数据源编码">
          <a-input placeholder="请输入数据源编码" :disabled="!!model.id" v-decorator="['code', validatorRules.code]"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="数据源名称">
          <a-input placeholder="请输入数据源名称" v-decorator="['name', validatorRules.name]"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="数据库类型">
          <j-dict-select-tag placeholder="请选择数据库类型" dict-code="database_type" triggerChange v-decorator="['dbType', validatorRules.dbType]" @change="handleDbTypeChange"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="驱动类">
          <a-input placeholder="请输入驱动类" v-decorator="['dbDriver', validatorRules.dbDriver]"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="数据源地址">
          <a-input placeholder="请输入数据源地址" v-decorator="['dbUrl', validatorRules.dbUrl]"/>
        </a-form-item>
       <!-- <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="数据库名称">
          <a-input placeholder="请输入数据库名称" v-decorator="['dbName', validatorRules.dbName]"/>
        </a-form-item>-->
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="用户名">
          <a-input placeholder="请输入用户名" v-decorator="['dbUsername', validatorRules.dbUsername]"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="密码">
          <a-row :gutter="8">
            <a-col :span="21">
              <a-input-password placeholder="请输入密码" v-decorator="['dbPassword', validatorRules.dbPassword]"/>
            </a-col>
            <a-col :span="3">
              <a-button type="primary" size="small" style="width: 100%" @click="handleTest">测试</a-button>
            </a-col>
          </a-row>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="备注">
          <a-textarea placeholder="请输入备注" v-decorator="['remark', {}]"/>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import pick from 'lodash.pick'
  import { httpAction, postAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'SysDataSourceModal',
    components: {},
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
          code: {
            validateFirst: true,
            rules: [
              { required: true, message: '请输入数据源编码!' },
              {
                validator: (rule, value, callback) => {
                  let pattern = /^[a-z|A-Z][a-z|A-Z\d_-]{0,}$/
                  if (!pattern.test(value)) {
                    callback('编码必须以字母开头，可包含数字、下划线、横杠')
                  } else {
                    validateDuplicateValue('sys_data_source', 'code', value, this.model.id, callback)
                  }
                }
              }
            ]
          },
          name: { rules: [{ required: true, message: '请输入数据源名称!' }] },
          dbType: { rules: [{ required: true, message: '请选择数据库类型!' }] },
          dbDriver: { rules: [{ required: true, message: '请输入驱动类!' }] },
          dbUrl: { rules: [{ required: true, message: '请输入数据源地址!' }] },
          dbName: { rules: [{ required: true, message: '请输入数据库名称!' }] },
          dbUsername: { rules: [{ required: true, message: '请输入用户名!' }] },
          dbPassword: { rules: [{ required: false, message: '请输入密码!' }] }
        },
        url: {
          add: '/sys/dataSource/add',
          edit: '/sys/dataSource/edit',
        },
        dbDriverMap: {
          // MySQL 数据库
          '1': { dbDriver: 'com.mysql.jdbc.Driver' },
          //MySQL5.7+ 数据库
          '4': { dbDriver: 'com.mysql.cj.jdbc.Driver' },
          // Oracle
          '2': { dbDriver: 'oracle.jdbc.OracleDriver' },
          // SQLServer 数据库
          '3': { dbDriver: 'com.microsoft.sqlserver.jdbc.SQLServerDriver' },
          // marialDB 数据库
          '5': { dbDriver: 'org.mariadb.jdbc.Driver' },
          // postgresql 数据库
          '6': { dbDriver: 'org.postgresql.Driver' },
          // 达梦 数据库
          '7': { dbDriver: 'dm.jdbc.driver.DmDriver' },
          // 人大金仓 数据库
          '8': { dbDriver: 'com.kingbase8.Driver' },
          // 神通 数据库
          '9': { dbDriver: 'com.oscar.Driver' },
          // SQLite 数据库
          '10': { dbDriver: 'org.sqlite.JDBC' },
          // DB2 数据库
          '11': { dbDriver: 'com.ibm.db2.jcc.DB2Driver' },
          // Hsqldb 数据库
          '12': { dbDriver: 'org.hsqldb.jdbc.JDBCDriver' },
          // Derby 数据库
          '13': { dbDriver: 'org.apache.derby.jdbc.ClientDriver' },
          // H2 数据库
          '14': { dbDriver: 'org.h2.Driver' },
          // 其他数据库
          '15': { dbDriver: '' }
        },
        dbUrlMap: {
          // MySQL 数据库
          '1': { dbUrl: 'jdbc:mysql://127.0.0.1:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false' },
          //MySQL5.7+ 数据库
          '4': { dbUrl: 'jdbc:mysql://127.0.0.1:3306/jeecg-boot?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai' },
          // Oracle
          '2': { dbUrl: 'jdbc:oracle:thin:@127.0.0.1:1521:ORCL' },
          // SQLServer 数据库
          '3': { dbUrl: 'jdbc:sqlserver://127.0.0.1:1433;SelectMethod=cursor;DatabaseName=jeecgboot' },
          // Mariadb 数据库
          '5': { dbUrl: 'jdbc:mariadb://127.0.0.1:3306/jeecg-boot?characterEncoding=UTF-8&useSSL=false' },
          // Postgresql 数据库
          '6': { dbUrl: 'jdbc:postgresql://127.0.0.1:5432/jeecg-boot' },
          // 达梦 数据库
          '7': { dbUrl: 'jdbc:dm://127.0.0.1:5236/?jeecg-boot&zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8' },
          // 人大金仓 数据库
          '8': { dbUrl: 'jdbc:kingbase8://127.0.0.1:54321/jeecg-boot' },
          // 神通 数据库
          '9': { dbUrl: 'jdbc:oscar://192.168.1.125:2003/jeecg-boot' },
          // SQLite 数据库
          '10': { dbUrl: 'jdbc:sqlite://opt/test.db' },
          // DB2 数据库
          '11': { dbUrl: 'jdbc:db2://127.0.0.1:50000/jeecg-boot' },
          // Hsqldb 数据库
          '12': { dbUrl: 'jdbc:hsqldb:hsql://127.0.0.1/jeecg-boot' },
          // Derby 数据库
          '13': { dbUrl: 'jdbc:derby://127.0.0.1:1527/jeecg-boot' },
          // H2 数据库
          '14': { dbUrl: 'jdbc:h2:tcp://127.0.0.1:8082/jeecg-boot' },
          // 其他数据库
          '15': { dbUrl: '' }
        }
      }
    },
    created() {
    },
    methods: {
      add() {
        this.edit({})
      },
      edit(record) {
        this.form.resetFields()
        this.model = Object.assign({}, record)
        this.visible = true
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'code', 'name', 'remark', 'dbType', 'dbDriver', 'dbUrl', 'dbName', 'dbUsername', 'dbPassword'))
        })
      },
      close() {
        this.$emit('close')
        this.visible = false
      },
      handleOk() {
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            this.confirmLoading = true
            let formData = Object.assign(this.model, values)
            let httpUrl = this.url.add, method = 'post'
            if (this.model.id) {
              httpUrl = this.url.edit
              method = 'put'
              // 由于编码的特殊性，所以不能更改
              formData['code'] = undefined
            }
            httpAction(httpUrl, formData, method).then((res) => {
              if (res.success) {
                this.$message.success(res.message)
                this.$emit('ok')
                this.close()
              } else {
                this.$message.warning(res.message)
              }
            }).finally(() => {
              this.confirmLoading = false
            })
          }
        })
      },
      handleCancel() {
        this.close()
      },
      // 测试数据源配置是否可以正常连接
      handleTest() {
        let keys = ['dbType', 'dbDriver', 'dbUrl', 'dbName', 'dbUsername', 'dbPassword']
        // 获取以上字段的值，并清除校验状态
        let fieldsValues = this.form.getFieldsValue(keys)
        let setFields = {}
        keys.forEach(key => setFields[key] = { value: fieldsValues[key], errors: null })
        // 清除校验状态，目的是可以让错误文字闪烁
        this.form.setFields(setFields)
        // 重新校验
        this.$nextTick(() => {
          this.form.validateFields(keys, (errors, values) => {
            if (!errors) {
              let loading = this.$message.loading('连接中……', 0)
              postAction('/online/cgreport/api/testConnection', fieldsValues).then(res => {
                if (res.success) {
                  this.$message.success('连接成功')
                } else throw new Error(res.message)
              }).catch(error => {
                this.$warning({ title: '连接失败', content: error.message || error })
              }).finally(() => loading())
            }
          })
        })
      },
      // 数据库类型更改时，联动更改数据库驱动
      handleDbTypeChange(val) {
        let dbDriver = this.dbDriverMap[val]
        let dbUrl = this.dbUrlMap[val]
        if (dbDriver) {
          this.form.setFieldsValue(dbDriver)
        }
        if (dbUrl) {
          this.form.setFieldsValue(dbUrl)
        }
      },
    }
  }
</script>

<style lang="less" scoped></style>