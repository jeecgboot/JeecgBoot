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
            <a-form-item label="人户一致标识" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['rhyz']" :trigger-change="true" dictCode="yzbz" placeholder="请选择人户一致标识"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="户号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'hh', validatorRules.hh]" placeholder="请输入户号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="户主身份证" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'hzSfz', validatorRules.hzSfz]" placeholder="请输入户主身份证"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="户主姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'hzName', validatorRules.hzName]" placeholder="请输入户主姓名"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="与户主关系" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'hzGx', validatorRules.hzGx]" placeholder="请输入与户主关系"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="户主联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'hzLxfs', validatorRules.hzLxfs]" placeholder="请输入户主联系方式"></a-input>
            </a-form-item>
          </a-col>

        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="综治人口" :key="refKeys[0]" :forceRender="true">
          <zz-persona-form ref="zzPersonaForm" @validateError="validateError"></zz-persona-form>
        </a-tab-pane>
      
      </a-tabs>

    </a-spin>
  </a-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import ZzPersonaForm from './ZzPersonaForm.vue'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: 'ZzPHjrkModal',
    mixins: [JEditableTableMixin],
    components: {
    ZzPersonaForm,
      JDictSelectTag,
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
          rhyz:{},
          hh:{},
          hzSfz:{},
          hzName:{},
          hzGx:{},
          hzLxfs:{},
        },
        refKeys: ['zzPersona', ],
        tableKeys:[],
        activeKey: 'zzPersona',
        // 综治人口
        zzPersonaTable: {
          loading: false,
          dataSource: [],
          columns: [
          ]
        },
        url: {
          add: "/hjrk/zzPHjrk/add",
          edit: "/hjrk/zzPHjrk/edit",
          zzPersona: {
            list: '/hjrk/zzPHjrk/queryZzPersonaByMainId'
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
        let fieldval = pick(this.model,'rhyz','hh','hzSfz','hzName','hzGx','hzLxfs')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
          this.$refs.zzPersonaForm.initFormData(this.url.zzPersona.list,this.model.id)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          zzPersonaList: this.$refs.zzPersonaForm.getFormData(),
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
     popupCallback(row){
       this.form.setFieldsValue(pick(row,'rhyz','hh','hzSfz','hzName','hzGx','hzLxfs'))
     },

    }
  }
</script>

<style scoped>
</style>