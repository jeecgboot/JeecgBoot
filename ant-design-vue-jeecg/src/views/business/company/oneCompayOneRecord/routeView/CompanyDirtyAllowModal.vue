<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-row>
          <a-col span='24'>
            <a-form-item label="许可证编号" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <a-input v-decorator="['licenceCode', validatorRules.licenceCode]" placeholder="请输入许可证编号"
                       :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="有效期限" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-date-picker placeholder="请选择有效开始时间" v-decorator="['validStarttime']" :trigger-change="true"
                             style="width: 100%" v-model="startValue" :disabled-date="disabledStartDate"
                             :disabled="disableSubmit" @openChange="handleStartOpenChange"/>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="至" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-date-picker placeholder="请选择有效结束时间" v-decorator="['validEndtime']" :trigger-change="true"
                             style="width: 100%" v-model="endValue" :disabled-date="disabledEndDate" :open="endOpen"
                             :disabled="disableSubmit" @openChange="handleEndOpenChange"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="发证日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择发证日期" v-decorator="['certificateTime']" :trigger-change="true" style="width: 100%"
                      :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="发证机关" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['certificateOffice']" placeholder="请输入发证机关" :disabled="disableSubmit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='24'>
            <a-form-item label="排污类别" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <a-textarea v-decorator="['dirtyType']" placeholder="请输入排污类别" :disabled="disableSubmit"></a-textarea>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='24'>
            <a-form-item label="许可内容附件" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <j-upload v-decorator="['files']" :trigger-change="true" :disabled="disableSubmit"></j-upload>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel">取消</a-button>
      <a-button type="primary" @click="handleOk">暂存</a-button>
      <a-button type="primary" @click="handDeclare">申报</a-button>
    </template>
  </j-modal>
</template>

<script>

  import {httpAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import {validateDuplicateValue} from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import JUpload from '@/components/jeecg/JUpload'


  export default {
    name: "CompanyDirtyAllowModal",
    components: {
      JDate,
      JUpload,
    },
    data() {
      return {
        startValue: null,
        endValue: null,
        endOpen: false,
        form: this.$form.createForm(this),
        title: "操作",
        width: 800,
        visible: false,
        model: {},
        labelCol: {
          xs: {span: 24},
          sm: {span: 6},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        labelCols: {
          xs: {span: 24},
          sm: {span: 3},
        },
        wrapperCols: {
          xs: {
            span: 24
          },
          sm: {
            span: 20

          }
        },
        confirmLoading: false,
        validatorRules: {
          licenceCode: {
            rules: [
              {required: true, message: '请输入许可证编号!'},
            ]
          },
        },
        url: {
          add: "/dirty/companyDirtyAllow/add",
          edit: "/dirty/companyDirtyAllow/edit",
          declare: "/dirty/companyDirtyAllow/declare",
        }
      }
    },
    created() {
    },
    methods: {
      add() {
        this.edit({});
      },
      edit(record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'licenceCode', 'certificateTime', 'validStarttime', 'validEndtime', 'certificateOffice', 'dirtyType', 'files'))
        })
      },
      close() {
        this.$emit('close');
        this.visible = false;
      },
      handleOk() {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if (!this.model.id) {
              httpurl += this.url.add;
              method = 'post';
            } else {
              httpurl += this.url.edit;
              method = 'put';
            }
            let formData = Object.assign(this.model, values);
            formData.companyId = this.companyId;
            console.log("表单提交数据", formData)
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit('ok');
              } else {
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }

        })
      },
      handleCancel() {
        this.close()
      },
      //申报
      handDeclare() {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpUrl = this.url.declare;
            let method = 'put';
            let formData = Object.assign(this.model, values);
            formData.companyId = this.companyId;
            httpAction(httpUrl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit('ok');
              } else {
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }

        })
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'licenceCode', 'certificateTime', 'validStarttime', 'validEndtime', 'certificateOffice', 'dirtyType', 'files'))
      },
      disabledStartDate(startValue) {
        const endValue = this.endValue;
        if (!startValue || !endValue) {
          return false;
        }
        return startValue.valueOf() > endValue.valueOf();
      },
      disabledEndDate(endValue) {
        const startValue = this.startValue;
        if (!endValue || !startValue) {
          return false;
        }
        return startValue.valueOf() >= endValue.valueOf();
      },
      handleStartOpenChange(open) {
        if (!open) {
          this.endOpen = true;
        }
      },
      handleEndOpenChange(open) {
        this.endOpen = open;
      },
    },
    props: {
      companyId: ""
    },
    watch: {
      startValue(val) {
        console.log('startValue', val);
      },
      endValue(val) {
        console.log('endValue', val);
      },
    },
  }
</script>