<template>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
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
              <j-dict-select-tag type="radio" v-decorator="['sex', validatorRules.sex]" :trigger-change="true"
                                 dictCode="sex" placeholder="请选择性别"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="照片" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-upload v-decorator="['photo', validatorRules.photo]" :trigger-change="true"></j-upload>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="出生日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择出生日期" v-decorator="[ 'birthday', validatorRules.birthday]" :trigger-change="true"
                      style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="民族" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['zm', validatorRules.zm]" :trigger-change="true"
                                 dictCode="minzu" placeholder="请选择民族"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="籍贯" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'jg', validatorRules.jg]" placeholder="请输入籍贯"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="婚姻状况" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['hyzk', validatorRules.hyzk]" :trigger-change="true"
                                 dictCode="hyzk" placeholder="请选择婚姻状况"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="政治面貌" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['zzmm', validatorRules.zzmm]" :trigger-change="true"
                                 dictCode="zzmm" placeholder="请选择政治面貌"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="学历" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['xl', validatorRules.xl]" :trigger-change="true"
                                 dictCode="education" placeholder="请选择学历"/>
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
            <a-form-item label="流入原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['lryy', validatorRules.lryy]" :trigger-change="true"
                                 dictCode="lryy" placeholder="请选择流入原因"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="办证类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['bzlx', validatorRules.bzlx]" :trigger-change="true"
                                 dictCode="bzlx" placeholder="请选择办证类型"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="证件号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'zjh', validatorRules.zjh]" placeholder="请输入证件号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="登记日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择登记日期" v-decorator="[ 'djDate', validatorRules.djDate]" :trigger-change="true"
                      style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="证件到期日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择证件到期日期" v-decorator="[ 'zjdqDate', validatorRules.zjdqDate]"
                      :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="住所类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['zslx', validatorRules.zslx]" :trigger-change="true"
                                 dictCode="zslx" placeholder="请选择住所类型"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="是否重点关注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['sfZdgz', validatorRules.sfZdgz]" :trigger-change="true"
                                 dictCode="yes_no" placeholder="请选择是否重点关注"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import JUpload from '@/components/jeecg/JUpload'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'

  export default {
    name: 'ZzPLdrkModal',
    components: {
      JDate,
      JUpload,
      JDictSelectTag
    },
    data() {
      return {
        form: this.$form.createForm(this),
        title: '操作',
        width: 800,
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
        validatorRules: {
          name: {
            rules: [
              { required: true, message: '请输入姓名!' }
            ]
          },
          sfz: {
            rules: []
          },
          oldName: {
            rules: []
          },
          sex: {
            rules: []
          },
          photo: {
            rules: []
          },
          birthday: {
            rules: []
          },
          zm: {
            rules: []
          },
          jg: {
            rules: []
          },
          hyzk: {
            rules: []
          },
          zzmm: {
            rules: []
          },
          xl: {
            rules: []
          },
          zjxy: {
            rules: []
          },
          zylb: {
            rules: []
          },
          zy: {
            rules: []
          },
          hjxq: {
            rules: []
          },
          xzdxq: {
            rules: []
          },
          lxfs: {
            rules: []
          },
          lryy: {
            rules: []
          },
          bzlx: {
            rules: []
          },
          zjh: {
            rules: []
          },
          djDate: {
            rules: []
          },
          zjdqDate: {
            rules: []
          },
          zslx: {
            rules: []
          },
          sfZdgz: {
            rules: []
          }
        },
        url: {
          add: '/ldrk/zzPLdrk/add',
          edit: '/ldrk/zzPLdrk/edit'
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
          this.form.setFieldsValue(pick(this.model, 'name', 'sfz', 'oldName', 'sex', 'photo', 'birthday', 'zm', 'jg', 'hyzk', 'zzmm', 'xl', 'zjxy', 'zylb', 'zy', 'hjxq', 'xzdxq', 'lxfs', 'lryy', 'bzlx', 'zjh', 'djDate', 'zjdqDate', 'zslx', 'sfZdgz'))
        })
      },
      close() {
        this.$emit('close')
        this.visible = false
      },
      handleOk() {
        const that = this
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true
            let httpurl = ''
            let method = ''
            if (!this.model.id) {
              httpurl += this.url.add
              method = 'post'
            } else {
              httpurl += this.url.edit
              method = 'put'
            }
            let formData = Object.assign(this.model, values)
            console.log('表单提交数据', formData)
            httpAction(httpurl, formData, method).then((res) => {
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
      handleCancel() {
        this.close()
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'name', 'sfz', 'oldName', 'sex', 'photo', 'birthday', 'zm', 'jg', 'hyzk', 'zzmm', 'xl', 'zjxy', 'zylb', 'zy', 'hjxq', 'xzdxq', 'lxfs', 'lryy', 'bzlx', 'zjh', 'djDate', 'zjdqDate', 'zslx', 'sfZdgz'))
      }


    }
  }
</script>