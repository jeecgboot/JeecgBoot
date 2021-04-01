<template>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :destroyOnClose="true"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel">

    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" :label-col="labelCol" :wrapper-col="wrapperCol"  :model="model" >
        <!-- 主表单区域 -->
        <a-row class="form-row" :gutter="0">
          <a-col :lg="8">
            <a-form-model-item  label="订单号" prop="orderCode" :rules="[{ required: true, message: '请输入订单号!' }]">
              <a-input placeholder="请输入订单号" v-model="model.orderCode"/>
            </a-form-model-item>
          </a-col>
          <a-col :lg="8">
            <a-form-model-item  label="订单类型">
              <a-select placeholder="请选择订单类型" v-model="model.ctype">
                <a-select-option value="1">国内订单</a-select-option>
                <a-select-option value="2">国际订单</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :lg="8">
            <a-form-model-item  label="订单日期">
              <a-date-picker showTime valueFormat="YYYY-MM-DD HH:mm:ss" style="width: 100%" v-model="model.orderDate"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="0">
          <a-col :lg="8">
            <a-form-model-item  label="订单金额">
              <a-input-number placeholder="请输入订单金额" style="width: 100%" v-model="model.orderMoney"/>
            </a-form-model-item>
          </a-col>
          <a-col :lg="8">
            <a-form-model-item  label="订单备注">
              <a-input placeholder="请输入订单备注" v-model="model.content"/>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="客户信息" key="1" :forceRender="true">
          <j-vxe-table
            ref="editableTable1"
            toolbar
            row-number
            row-selection
            keep-source
            :height="300"
            :loading="table1.loading"
            :dataSource="table1.dataSource"
            :columns="table1.columns"
            style="margin-top: 8px;"/>

        </a-tab-pane>

        <a-tab-pane tab="机票信息" key="2" :forceRender="true">
          <j-vxe-table
            ref="editableTable2"
            toolbar
            row-number
            row-selection
            keep-source
            :height="300"
            :loading="table2.loading"
            :dataSource="table2.dataSource"
            :columns="table2.columns"
            style="margin-top: 8px;"/>
        </a-tab-pane>
      </a-tabs>

    </a-spin>
  </a-modal>
</template>

<script>

  import JEditableTable from '@/components/jeecg/JEditableTable'
  import { VALIDATE_FAILED, getRefPromise, validateFormModelAndTables } from '@/components/jeecg/JVxeTable/utils/vxeUtils'
  import { httpAction, getAction } from '@/api/manage'
  import { JVXETypes } from '@/components/jeecg/JVxeTable'
  import JDate from '@/components/jeecg/JDate'

  export default {
    name: 'JeecgOrderModalForJvexTable',
    components: {
      JDate, JEditableTable
    },
    data() {
      return {
        title: '操作',
        visible: false,
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
              type: JVXETypes.input,
              defaultValue: '',
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '性别',
              key: 'sex',
              width: '18%',
              type: JVXETypes.select,
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
              type: JVXETypes.input,
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
              type: JVXETypes.input,
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
              type: JVXETypes.input,
              defaultValue: '',
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '航班时间',
              key: 'tickectDate',
              width: '30%',
              type: JVXETypes.date,
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
          //editableTables[0].add()
          //editableTables[1].add()
        })
        this.edit({})
      },
      edit(record) {
        this.visible = true
        this.activeKey = '1'
        this.model = Object.assign({}, record)
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
          this.table1.dataSource=[];
          this.table2.dataSource=[];
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
          return validateFormModelAndTables(this.$refs.form,this.model, tables)
        }).then(allValues => {
          let formData = this.classifyIntoFormData(allValues)
          // 发起请求
          return this.requestAddOrEdit(formData)
        }).catch(e => {
          if (e.error === VALIDATE_FAILED) {
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
        return {
          ...orderMain, // 展开
          jeecgOrderCustomerList: allValues.tablesValue[0].tableData,
          jeecgOrderTicketList: allValues.tablesValue[1].tableData
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