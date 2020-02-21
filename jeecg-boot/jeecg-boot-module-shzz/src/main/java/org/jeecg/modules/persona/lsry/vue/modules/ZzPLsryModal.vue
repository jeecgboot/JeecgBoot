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
            <a-form-item label="姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入姓名"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="身份证" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'sfz', validatorRules.sfz]" placeholder="请输入身份证"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="曾用名" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'oldName', validatorRules.oldName]" placeholder="请输入曾用名"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="radio" v-decorator="['sex']" :trigger-change="true" dictCode="sex" placeholder="请选择性别"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="照片" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-upload v-decorator="['photo']" :trigger-change="true"></j-upload>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="出生日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择出生日期" v-decorator="[ 'birthday', validatorRules.birthday]" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="民族" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['zm']" :trigger-change="true" dictCode="minzu" placeholder="请选择民族"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="籍贯" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'jg', validatorRules.jg]" placeholder="请输入籍贯"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="婚姻状况" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['hyzk']" :trigger-change="true" dictCode="hyzk" placeholder="请选择婚姻状况"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="政治面貌" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['zzmm']" :trigger-change="true" dictCode="zzmm" placeholder="请选择政治面貌"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="学历" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['xl']" :trigger-change="true" dictCode="education" placeholder="请选择学历"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="宗教信仰" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'zjxy', validatorRules.zjxy]" placeholder="请输入宗教信仰"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="职业类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'zylb', validatorRules.zylb]" placeholder="请输入职业类别"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="职业" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'zy', validatorRules.zy]" placeholder="请输入职业"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="户籍门（楼）祥" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'hjxq', validatorRules.hjxq]" placeholder="请输入户籍门（楼）祥"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="现住门（楼）祥" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'xzdxq', validatorRules.xzdxq]" placeholder="请输入现住门（楼）祥"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'lxfs', validatorRules.lxfs]" placeholder="请输入联系方式"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="健康状况" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['jkzk']" :trigger-change="true" dictCode="jkzk" placeholder="请选择健康状况"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="个人年收入" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'grnsr', validatorRules.grnsr]" placeholder="请输入个人年收入"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="家庭年收入" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'jtnsr', validatorRules.jtnsr]" placeholder="请输入家庭年收入"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="人户一致标识" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['rhyzbz']" :trigger-change="true" dictCode="yzbz" placeholder="请选择人户一致标识"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="留守人员类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['lsrylx']" :trigger-change="true" dictCode="lsrylx" placeholder="请选择留守人员类型"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="困难及诉求" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
              <a-textarea v-decorator="['knjsq']" rows="4" placeholder="请输入困难及诉求"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="帮扶情况" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
              <a-textarea v-decorator="['bfqk']" rows="4" placeholder="请输入帮扶情况"/>
            </a-form-item>
          </a-col>

        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="留守人员附表" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="zzPLsryPersonaTable.loading"
            :columns="zzPLsryPersonaTable.columns"
            :dataSource="zzPLsryPersonaTable.dataSource"
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
  import JUpload from '@/components/jeecg/JUpload'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: 'ZzPLsryModal',
    mixins: [JEditableTableMixin],
    components: {
      JDate,
      JUpload,
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
          name: { rules: [{ required: true, message: '请输入姓名!' }] },
          sfz:{},
          oldName:{},
          sex:{},
          photo:{},
          birthday:{},
          zm:{},
          jg:{},
          hyzk:{},
          zzmm:{},
          xl:{},
          zjxy:{},
          zylb:{},
          zy:{},
          hjxq:{},
          xzdxq:{},
          lxfs:{},
          jkzk:{},
          grnsr:{},
          jtnsr:{},
          rhyzbz:{},
          lsrylx:{},
          knjsq:{},
          bfqk:{},
        },
        refKeys: ['zzPLsryPersona', ],
        tableKeys:['zzPLsryPersona', ],
        activeKey: 'zzPLsryPersona',
        // 留守人员附表
        zzPLsryPersonaTable: {
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
            },
            {
              title: '身份证',
              key: 'sfz',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '健康状况',
              key: 'jkzk',
              type: FormTypes.select,
              dictCode:"jkzk",
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '留守人员关系',
              key: 'lsrygx',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '联系方式',
              key: 'lxfs',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '工作地址',
              key: 'gzdz',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
          ]
        },
        url: {
          add: "/lsry/zzPLsry/add",
          edit: "/lsry/zzPLsry/edit",
          zzPLsryPersona: {
            list: '/lsry/zzPLsry/queryZzPLsryPersonaByMainId'
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
        let fieldval = pick(this.model,'name','sfz','oldName','sex','photo','birthday','zm','jg','hyzk','zzmm','xl','zjxy','zylb','zy','hjxq','xzdxq','lxfs','jkzk','grnsr','jtnsr','rhyzbz','lsrylx','knjsq','bfqk')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.zzPLsryPersona.list, params, this.zzPLsryPersonaTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          zzPLsryPersonaList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
     popupCallback(row){
       this.form.setFieldsValue(pick(row,'name','sfz','oldName','sex','photo','birthday','zm','jg','hyzk','zzmm','xl','zjxy','zylb','zy','hjxq','xzdxq','lxfs','jkzk','grnsr','jtnsr','rhyzbz','lsrylx','knjsq','bfqk'))
     },

    }
  }
</script>

<style scoped>
</style>