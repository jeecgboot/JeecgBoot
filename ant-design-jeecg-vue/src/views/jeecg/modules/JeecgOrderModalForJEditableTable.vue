<template>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel">

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <!-- 主表单区域 -->
        <a-row class="form-row" :gutter="0">
          <a-col :lg="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="订单号">
              <a-input
                placeholder="请输入订单号"
                v-decorator="['orderCode', {rules: [{ required: true, message: '请输入订单号!' }]}]"/>
            </a-form-item>
          </a-col>
          <a-col :lg="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="订单类型">
              <a-select placeholder="请选择订单类型" v-decorator="['ctype',{}]">
                <a-select-option value="1">国内订单</a-select-option>
                <a-select-option value="2">国际订单</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="订单日期">
              <a-date-picker showTime format="YYYY-MM-DD HH:mm:ss" style="width: 100%" v-decorator="[ 'orderDate',{}]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="0">
          <a-col :lg="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="订单金额">
              <a-input-number placeholder="请输入订单金额" style="width: 100%" v-decorator="[ 'orderMoney', {}]"/>
            </a-form-item>
          </a-col>
          <a-col :lg="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="订单备注">
              <a-input placeholder="请输入订单备注" v-decorator="['content', {}]"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="客户信息" key="1" :forceRender="true">

          <j-editable-table
            ref="editableTable1"
            :loading="table1.loading"
            :columns="table1.columns"
            :dataSource="table1.dataSource"
            :maxHeight="300"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="true"/>

        </a-tab-pane>

        <a-tab-pane tab="机票信息" key="2" :forceRender="true">

          <j-editable-table
            ref="editableTable2"
            :loading="table2.loading"
            :columns="table2.columns"
            :dataSource="table2.dataSource"
            :maxHeight="300"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="true"/>

        </a-tab-pane>
      </a-tabs>

    </a-spin>
  </a-modal>
</template>

<script>

  import JEditableTable from '@/components/jeecg/JEditableTable'
  import { FormTypes, VALIDATE_NO_PASSED, getRefPromise, validateFormAndTables } from '@/utils/JEditableTableUtil'
  import { httpAction, getAction } from '@/api/manage'
  import JDate from '@/components/jeecg/JDate'
  import pick from 'lodash.pick'
  import moment from 'moment'

  export default {
    name: 'JeecgOrderModalForJEditableTable',
    components: {
      JDate, JEditableTable
    },
    data() {
      return {
        title: '操作',
        visible: false,
        form: this.$form.createForm(this),
        confirmLoading: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 24 - 6 }
        },
        activeKey: '1',
        // 客户信息
        table1: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '客户名',
              key: 'name',
              width: '24%',
              type: FormTypes.input,
              defaultValue: '',
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '性别',
              key: 'sex',
              width: '18%',
              type: FormTypes.select,
              options: [ // 下拉选项
                { title: '男', value: '1' },
                { title: '女', value: '2' }
              ],
              defaultValue: '',
              placeholder: '请选择${title}'
            },
            {
              title: '身份证号',
              key: 'idcard',
              width: '24%',
              type: FormTypes.input,
              defaultValue: '',
              placeholder: '请输入${title}',
              validateRules: [{
                pattern: '^\\d{6}(18|19|20)?\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|[xX])$',
                message: '${title}格式不正确'
              }]
            },
            {
              title: '手机号',
              key: 'telphone',
              width: '24%',
              type: FormTypes.input,
              defaultValue: '',
              placeholder: '请输入${title}',
              validateRules: [{
                pattern: '^1(3|4|5|7|8)\\d{9}$',
                message: '${title}格式不正确'
              }]
            }
          ]
        },
        // 机票信息
        table2: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '航班号',
              key: 'ticketCode',
              width: '40%',
              type: FormTypes.input,
              defaultValue: '',
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '航班时间',
              key: 'tickectDate',
              width: '30%',
              type: FormTypes.date,
              placeholder: '请选择${title}',
              defaultValue: ''
            }
          ]
        },
        url: {
          add: '/test/jeecgOrderMain/add',
          edit: '/test/jeecgOrderMain/edit',
          orderCustomerList: '/test/jeecgOrderMain/queryOrderCustomerListByMainId',
          orderTicketList: '/test/jeecgOrderMain/queryOrderTicketListByMainId'
        }
      }
    },
    created() {
    },
    methods: {

      // 获取所有的editableTable实例
      getAllTable() {
        return Promise.all([
          getRefPromise(this, 'editableTable1'),
          getRefPromise(this, 'editableTable2')
        ])
      },

      add() {
        // 默认新增一条数据
        this.getAllTable().then(editableTables => {
          editableTables[0].add()
          editableTables[1].add()
        })

        this.edit({})
      },
      edit(record) {
        this.visible = true
        this.activeKey = '1'
        this.form.resetFields()
        this.model = Object.assign({}, record)

        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'orderCode', 'ctype', 'orderMoney', 'content'))
          //时间格式化
          this.form.setFieldsValue({ orderDate: this.model.orderDate ? moment(this.model.orderDate) : null })
        })

        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestTableData(this.url.orderCustomerList, params, this.table1)
          this.requestTableData(this.url.orderTicketList, params, this.table2)
        }

      },
      close() {
        this.visible = false
        this.getAllTable().then(editableTables => {
          editableTables[0].initialize()
          editableTables[1].initialize()
        })
        this.$emit('close')
      },
      /** 查询某个tab的数据 */
      requestTableData(url, params, tab) {
        tab.loading = true
        getAction(url, params).then(res => {
          tab.dataSource = res.result || []
        }).finally(() => {
          tab.loading = false
        })
      },
      handleOk() {
        this.validateFields()
      },
      handleCancel() {
        this.close()
      },
      /** ATab 选项卡切换事件 */
      handleChangeTabs(key) {
        getRefPromise(this, `editableTable${key}`).then(editableTable => {
          editableTable.resetScrollTop()
        })
      },

      /** 触发表单验证 */
      validateFields() {
        this.getAllTable().then(tables => {
          /** 一次性验证主表和所有的次表 */
          return validateFormAndTables(this.form, tables)
        }).then(allValues => {
          let formData = this.classifyIntoFormData(allValues)
          // 发起请求
          return this.requestAddOrEdit(formData)
        }).catch(e => {
          if (e.error === VALIDATE_NO_PASSED) {
            // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
            this.activeKey = e.index == null ? this.activeKey : (e.index + 1).toString()
          } else {
            console.error(e)
          }
        })
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let orderMain = Object.assign(this.model, allValues.formValue)
        //时间格式化
        orderMain.orderDate = orderMain.orderDate ? orderMain.orderDate.format('YYYY-MM-DD HH:mm:ss') : null
        return {
          ...orderMain, // 展开
          jeecgOrderCustomerList: allValues.tablesValue[0].values,
          jeecgOrderTicketList: allValues.tablesValue[1].values
        }
      },
      /** 发起新增或修改的请求 */
      requestAddOrEdit(formData) {
        let url = this.url.add, method = 'post'
        if (this.model.id) {
          url = this.url.edit
          method = 'put'
        }
        this.confirmLoading = true
        httpAction(url, formData, method).then((res) => {
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

    }
  }
</script>

<style scoped>
</style>