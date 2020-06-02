<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-row>
          <a-col span='12'>
            <a-form-item label="许可证编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-search-select-tag placeholder="请做出你的选择"  v-decorator="['licenceCode', validatorRules.licenceCode]"  dict="company_dirty_allow,licence_code,licence_code":async="true"/>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="排放口大类" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['ventCategory', validatorRules.ventCategory]"
                             @change="ventcategoryChange"    :trigger-change="true" dictCode="ventcategory" placeholder="请选择排放口大类"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="排放口编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['ventCode']" placeholder="请输入排放口编号"></a-input>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="排放口名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['ventName']" placeholder="请输入排放口名称"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="排污口税源编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['taxsourceCode', validatorRules.taxsourceCode]" placeholder="请输入排污口税源编号"></a-input>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="排放方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['letMode', validatorRules.letMode]" :trigger-change="true"
                                 dictCode="letmode" placeholder="请选择排放方式"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='24'>
            <a-form-item label="排污口位置" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <a-row>
                <a-col span='21'>
                  <a-input v-decorator="['ventLocate', validatorRules.ventLocate]" placeholder="请输入排污口位置"></a-input>
                </a-col>
                <a-col span='3'>
                  <a-button type="primary" :block="true" @click="searchMap">
                    查询坐标
                  </a-button>
                </a-col>
              </a-row>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12'>
            <a-form-item label="排污口经度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['ventLongitude']" placeholder="请输入排污口经度"></a-input>
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="排污口纬度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['ventLatitude']" placeholder="请输入排污口纬度"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='24' v-if="model.ventCategory==='1'">
            <a-form-item label="水污染排污去向" :labelCol="labelCols" :wrapperCol="wrapperCols">
              <j-dict-select-tag type="list" v-decorator="['letDirection']" :trigger-change="true"
                                 dictCode="letdirection"
                                 placeholder="请选择水污染排污去向"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span='12' v-if="model.ventCategory==='0'">
            <a-form-item label="大气污染排放口类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['ventType']" :trigger-change="true" dictCode="venttype"
                                 placeholder="请选择大气污染物排放口类别" />
            </a-form-item>
          </a-col>
          <a-col span='12'>
            <a-form-item label="主管税务科所" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['taxDepartment', validatorRules.taxDepartment]" placeholder="请输入主管税务科所"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>

  import {httpAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import {validateDuplicateValue} from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
  import {loadBaiduMap} from "../../../requestAction/request"

  export default {
    name: "CompanyEnvTaxModal",
    components: {
      JDictSelectTag,
      JSearchSelectTag,
    },
    data() {
      return {
        form: this.$form.createForm(this),
        title: "操作",
        width: 1200,
        visible: false,
        model: {},
        asyncSelectValue:"",
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
              {required: false, message: '请输入许可证编号!'},
            ]
          },
          ventCategory: {
            rules: [
              {required: true, message: '请输入排放口大类!'},
            ]
          },
          taxsourceCode: {
            rules: [
              {required: true, message: '请输入排污口税源编号!'},
            ]
          },
          letMode: {
            rules: [
              {required: true, message: '请输入排放方式!'},
            ]
          },
          ventLocate: {
            rules: [
              {required: true, message: '请输入排污口位置!'},
            ]
          },
          taxDepartment: {
            rules: [
              {required: true, message: '请输入主管税务科所!'},
            ]
          },
        },
        url: {
          add: "/envtax/companyEnvTax/add",
          edit: "/envtax/companyEnvTax/edit",
        }
      }
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
          this.form.setFieldsValue(pick(this.model, 'licenceCode', 'ventCategory', 'ventCode', 'ventName', 'taxsourceCode', 'letMode', 'ventLocate', 'ventLongitude', 'ventLatitude', 'letDirection', 'ventType', 'taxDepartment'))
        })
      },
      ventcategoryChange(value){
        this.model.ventCategory=value;
        console.log('ventCategory',this.model.ventCategory,this.model.ventCategory==='0');
      },
      searchMap(){
        let that = this;
        this.form.validateFields((err, values) => {
          that.model.ventLocate=values.ventLocate;
        });
        loadBaiduMap({address:this.model.ventLocate}).then((res)=>{
          console.log(res);
          if(res.success){
            this.form.setFieldsValue({ventLongitude:res.result.lng,ventLatitude:res.result.lat});
          }else{
            this.$error({ title: '查询坐标错误', content: '错误信息：' + res.message , maskClosable: true })
          }
        });
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
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'licenceCode', 'ventCategory', 'ventCode', 'ventName', 'taxsourceCode', 'letMode', 'ventLocate', 'ventLongitude', 'ventLatitude', 'letDirection', 'ventType', 'taxDepartment'))
      },


    },
    props: {
      companyId: ""
    }
  }
</script>