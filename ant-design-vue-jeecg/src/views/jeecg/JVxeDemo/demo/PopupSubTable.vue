<template>
  <a-card title="弹出子表示例" :bordered="false">

    <!--
      【弹出子表大体思路】
      1. 必须要有 click-row-show-sub-form 属性，如果该属性设为false，那么就不会弹出子表
      2. 必须要有 sub-form 插槽，用于规定弹出子表的内容
      3. highlight-current-row 属性可有可无，如果有则点击一行的时候，该行会背景色会常亮
    -->

    <!--
      【弹出详细信息（既有主表的数据也有子表的）大体思路】
      1. 必须要有 click-row-show-main-form 属性，如果该属性设为false，那么就不会弹出详细信息
      2. 必须要有 main-form 插槽，用于规定弹出子表的内容
      3. 可选 click-row-show-sub-form 属性，如果有该属性，就会显示子表，否者不显示
    -->

    <j-vxe-table
      toolbar
      row-number
      row-selection

      highlight-current-row
      click-row-show-sub-form
      click-row-show-main-form

      :height="750"
      :loading="loading"
      :columns="columns"
      :dataSource="dataSource"
      @detailsConfirm="handleDetailsConfirm"
    >

      <!-- 主表单 -->
      <template v-slot:mainForm="{row}">
        <template v-if="row">
          <a-form-model
            ref="form2"
            :model="row"
            :rules="rules"
            :label-col="labelCol"
            :wrapper-col="wrapperCol"
          >
            <a-row :gutter="8">
              <a-col :span="8">
                <a-form-model-item label="ID" prop="id">
                  <a-input v-model="row.id" disabled/>
                </a-form-model-item>
              </a-col>
              <a-col :span="8">
                <a-form-model-item label="序号" prop="num">
                  <a-input v-model="row.num"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="8">
                <a-form-model-item label="船名" prop="ship_name">
                  <a-input v-model="row.ship_name"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="8">
                <a-form-model-item label="呼叫" prop="call">
                  <a-input v-model="row.call"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="8">
                <a-form-model-item label="长" prop="len">
                  <a-input v-model="row.len"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="8">
                <a-form-model-item label="吨" prop="ton">
                  <a-input v-model="row.ton"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="8">
                <a-form-model-item label="付款方" prop="payer">
                  <a-input v-model="row.payer"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="8">
                <a-form-model-item label="数" prop="count">
                  <a-input v-model="row.count"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="8">
                <a-form-model-item label="公司" prop="company">
                  <a-input v-model="row.company"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="8">
                <a-form-model-item label="动向" prop="trend">
                  <a-input v-model="row.trend"/>
                </a-form-model-item>
              </a-col>
            </a-row>
          </a-form-model>
        </template>

      </template>

      <!-- 子表单 -->
      <template v-slot:subForm="{row}">
        <template v-if="loadSubData(row)">
          <j-vxe-table
            ref="subFormTable"
            height="auto"
            :max-height="350"
            :loading="subTable.loading"
            :columns="subTable.columns"
            :dataSource="subTable.dataSource"
          />
        </template>
      </template>

    </j-vxe-table>

  </a-card>
</template>

<script>
  import { getAction } from '@api/manage'
  import { JVXETypes } from '@/components/jeecg/JVxeTable'

  // 弹出子表示例
  export default {
    name: 'PopupSubTable',
    data() {
      return {
        loading: false,
        dataSource: [],
        columns: [
          {key: 'num', title: '序号', width: '80px'},
          {key: 'ship_name', title: '船名', width: '180px', type: JVXETypes.input},
          {key: 'call', title: '呼叫', width: '80px'},
          {key: 'len', title: '长', width: '80px'},
          {key: 'ton', title: '吨', width: '120px'},
          {key: 'payer', title: '付款方', width: '120px'},
          {key: 'count', title: '数', width: '40px'},
          {
            key: 'company',
            title: '公司',
            minWidth: '180px',
            // 是否点击显示详细信息
            // 只有当前单元格不能编辑的时候才能生效
            // 如果不设的话，点击就只弹出子表，不会弹出主表的详细信息
            showDetails: true
          },
          {key: 'trend', title: '动向', width: '120px'},
        ],
        // 子表的信息
        subTable: {
          currentRowId: null,
          loading: false,
          pagination: {current: 1, pageSize: 200, pageSizeOptions: ['100', '200'], total: 0},
          selectedRows: [],
          dataSource: [],
          columns: [
            {key: 'dd_num', title: '调度序号', width: '120px'},
            {key: 'tug', title: '拖轮', width: '180px', type: JVXETypes.input},
            {key: 'work_start_time', title: '作业开始时间', width: '180px', type: JVXETypes.input},
            {key: 'work_stop_time', title: '作业结束时间', width: '180px', type: JVXETypes.input},
            {key: 'type', title: '船舶分类', width: '120px', type: JVXETypes.input},
            {key: 'port_area', title: '所属港区', minWidth: '120px', type: JVXETypes.input},
          ],
        },
        // 查询url地址
        url: {
          getData: '/mock/vxe/getData',
        },
        // 主表form表单字段
        mainForm: {
          id: '',
          num: '',
          ship_name: '',
          call: '',
          len: '',
          ton: '',
          payer: '',
          count: '',
          company: '',
          trend: '',
        },
        // form表单 col
        labelCol: {span: 4},
        wrapperCol: {span: 20},
        rules: {
          num: [
            {required: true, message: '必须输入序号'},
          ],
        },
      }
    },

    created() {
      this.loadData()
    },
    methods: {

      log: console.log,

      // 加载数据
      loadData() {
        // 封装查询条件
        let formData = {pageNo: 1, pageSize: 30}
        // 调用查询数据接口
        this.loading = true
        getAction(this.url.getData, formData).then(res => {
          if (res.success) {
            // 将查询的数据赋值给 dataSource
            this.dataSource = res.result.records
            // 重置选择
            this.selectedRows = []
          } else {
            this.$error({title: '主表查询失败', content: res.message})
          }
        }).finally(() => {
          // 这里是无论成功或失败都会执行的方法，在这里关闭loading
          this.loading = false
        })
      },

      // 查询子表数据
      loadSubData(row) {
        if (row) {
          // 这里一定要做限制，限制不能重复查询，否者会出现死循环
          if (this.subTable.currentRowId === row.id) {
            return true
          }
          this.subTable.currentRowId = row.id
          let formData = {pageNo: 1, pageSize: 30, parentId: row.id}
          this.subTable.loading = true
          getAction(this.url.getData, formData).then(res => {
            if (res.success) {
              // 将查询的数据赋值给 dataSource
              this.subTable.dataSource = res.result.records
            } else {
              this.$error({title: '主表查询失败', content: res.message})
            }
          }).finally(() => {
            // 这里是无论成功或失败都会执行的方法，在这里关闭loading
            this.subTable.loading = false
          })
          return true
        } else {
          return false
        }
      },

      // 详细信息里点了确认按钮
      handleDetailsConfirm({row, $table, callback}) {
        console.log('保存的数据：', row)
        // 校验当前行
        $table.validate(row).then((errMap) => {
          // 校验通过
          if (!errMap) {
            // 校验子表，如果需要的话，可以操作下面这个对象：
            // this.$refs.subFormTable

            callback(true)
            this.loading = true
            setTimeout(() => {
              this.loading = false
              this.$message.success('保存成功')
            }, 1000)
          } else {
            callback(false)
            this.$message.warn('校验失败')
          }
        })
      },

    },
  }
</script>

<style scoped>

</style>