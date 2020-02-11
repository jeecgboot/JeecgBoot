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
      <!-- 主表单区域 -->
      <a-form :form="form">
        <a-row>

          <a-col :span="12">
            <a-form-item label="命案名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'maName', validatorRules.maName]" placeholder="请输入命案名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="命案编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'maCode', validatorRules.maCode]" placeholder="请输入命案编号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="发生时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择发生时间" v-decorator="[ 'fsDate', validatorRules.fsDate]" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="侦查时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择侦查时间" v-decorator="[ 'zcDate', validatorRules.zcDate]" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="简要情况" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
              <a-textarea v-decorator="['maDesc']" rows="4" placeholder="请输入简要情况"/>
            </a-form-item>
          </a-col>

        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="受害人" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="zzMafkPersonShrTable.loading"
            :columns="zzMafkPersonShrTable.columns"
            :dataSource="zzMafkPersonShrTable.dataSource"
            :maxHeight="300"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="true"/>
        </a-tab-pane>
        
        <a-tab-pane tab="嫌疑人" :key="refKeys[1]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[1]"
            :loading="zzMafkPersonXyrTable.loading"
            :columns="zzMafkPersonXyrTable.columns"
            :dataSource="zzMafkPersonXyrTable.dataSource"
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

  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'  

  export default {
    name: 'ZzMafkModal',
    mixins: [JEditableTableMixin],
    components: {
      JDate,
    },
    data() {
      return {
        labelCol: {
          span: 6
        },
        wrapperCol: {
          span: 16
        },
        labelCol2: {
          span: 3
        },
        wrapperCol2: {
          span: 20
        },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
          maName: { rules: [{ required: true, message: '请输入命案名称!' }] },
          maCode: { rules: [{ required: true, message: '请输入命案编号!' }] },
          fsDate:{},
          zcDate:{},
          maDesc:{},
        },
        refKeys: ['zzMafkPersonShr', 'zzMafkPersonXyr', ],
        tableKeys:['zzMafkPersonShr', 'zzMafkPersonXyr', ],
        activeKey: 'zzMafkPersonShr',
        // 受害人
        zzMafkPersonShrTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '姓名',
              key: 'name',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '性别',
              key: 'sex',
              type: FormTypes.select,
              dictCode:"sex",
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '证件类型',
              key: 'zjlx',
              type: FormTypes.select,
              dictCode:"zjlx",
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '证件号',
              key: 'zjh',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '国籍',
              key: 'gj',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '民族',
              key: 'mz',
              type: FormTypes.select,
              dictCode:"minzu",
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '生日',
              key: 'birthday',
              type: FormTypes.date,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
          ]
        },
        // 嫌疑人
        zzMafkPersonXyrTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '姓名',
              key: 'name',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '性别',
              key: 'sex',
              type: FormTypes.select,
              dictCode:"sex",
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '证件类型',
              key: 'zjlx',
              type: FormTypes.select,
              dictCode:"zjlx",
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '证件号',
              key: 'zjh',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '国籍',
              key: 'gj',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '民族',
              key: 'mz',
              type: FormTypes.select,
              dictCode:"minzu",
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '生日',
              key: 'birthday',
              type: FormTypes.date,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
          ]
        },
        url: {
          add: "/mafk/zzMafk/add",
          edit: "/mafk/zzMafk/edit",
          zzMafkPersonShr: {
            list: '/mafk/zzMafk/queryZzMafkPersonShrByMainId'
          },
          zzMafkPersonXyr: {
            list: '/mafk/zzMafk/queryZzMafkPersonXyrByMainId'
          },
        }
      }
    },
    methods: {
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model,'maName','maCode','fsDate','zcDate','maDesc')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.zzMafkPersonShr.list, params, this.zzMafkPersonShrTable)
          this.requestSubTableData(this.url.zzMafkPersonXyr.list, params, this.zzMafkPersonXyrTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          zzMafkPersonShrList: allValues.tablesValue[0].values,
          zzMafkPersonXyrList: allValues.tablesValue[1].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
     popupCallback(row){
       this.form.setFieldsValue(pick(row,'maName','maCode','fsDate','zcDate','maDesc'))
     },

    }
  }
</script>

<style scoped>
</style>