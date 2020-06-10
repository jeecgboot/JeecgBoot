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

        <a-form-item label="类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="radio" v-decorator="['outputType']"  @change = "handleTypeChange" :trigger-change="true" dictCode="output_type" placeholder="请选择类别"/>
        </a-form-item>
        <a-form-item label="产品名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['outputName', validatorRules.outputName]" placeholder="请输入产品名称"></a-input>
        </a-form-item>
        <a-form-item label="产量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="['yield', validatorRules.yield]" placeholder="请输入产量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="最大储量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['maxStore', validatorRules.maxStore]" placeholder="请输入最大储量"></a-input>
        </a-form-item>
        <a-form-item label="CAS号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['cas', validatorRules.cas]" placeholder="请输入CAS号"></a-input>
        </a-form-item>
        <a-form-item label="储存方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['storeType', validatorRules.storeType]" :trigger-change="true" dictCode="store_type" placeholder="请选择储存方式"/>
        </a-form-item>
        <a-form-item label="危化品类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['hazardousChemicalsCategory', validatorRules.hazardousChemicalsCategory]" :trigger-change="true" dictCode="hazardous_chemicals_category" placeholder="请选择危化品类别"/>
        </a-form-item>
        <a-form-item label="主要危险性" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['mainRisk', validatorRules.mainRisk]" :trigger-change="true" dictCode="main_risk" placeholder="请选择主要危险性"/>
        </a-form-item>
        <a-form-item label="重点监管" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['supervision', validatorRules.supervision]" :trigger-change="true" dictCode="yes_or_no" placeholder="请选择重点监管"/>
        </a-form-item>
        <a-form-item label="剧毒" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['toxic', validatorRules.toxic]" :trigger-change="true" dictCode="yes_or_no" placeholder="请选择剧毒"/>
        </a-form-item>
        <a-form-item label="易制毒" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['precursorChemicals', validatorRules.precursorChemicals]" :trigger-change="true" dictCode="yes_or_no" placeholder="请选择易制毒"/>
        </a-form-item>
        <a-form-item label="状态" v-if="displayPro" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list"  v-decorator="['status', validatorRules.status]" :trigger-change="true" dictCode="output_status" placeholder="请选择状态"/>
        </a-form-item>
        <a-form-item label="是否领证" v-if="displayPro" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list"  v-decorator="['certified', validatorRules.certified]" :trigger-change="true" dictCode="yes_or_no" placeholder="请选择是否领证"/>
        </a-form-item>
        <a-form-item label="主要原材料"  v-if="displayPro" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['rawMaterials', validatorRules.rawMaterials]" rows="4" placeholder="请输入主要原材料"/>
        </a-form-item>
        <a-form-item label="主要生产设备" v-if="displayPro" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['proEquipment', validatorRules.proEquipment]" rows="4" placeholder="请输入主要生产设备"/>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['remake', validatorRules.remake]" rows="4" placeholder="请输入备注"/>
        </a-form-item>

      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"


  export default {
    name: "CompanyProductMaterialModal",
    components: { 
      JDictSelectTag,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        diplay:{},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
          outputName: {
            rules: [
              { required: true, message: '请输入产品名称!'},
            ]
          },
          yield: {
            rules: [
              { required: true, message: '请输入产量!'},
            ]
          },
          maxStore: {
            rules: [
              { required: true, message: '请输入最大储量!'},
            ]
          },
          cas: {
            rules: [
              { required: true, message: '请输入CAS号!'},
            ]
          },
          storeType: {
            rules: [
              { required: true, message: '请输入储存方式!'},
            ]
          },
          hazardousChemicalsCategory: {
            rules: [
              { required: true, message: '请输入危化品类别!'},
            ]
          },
          mainRisk: {
            rules: [
              { required: true, message: '请输入主要危险性!'},
            ]
          },
          supervision: {
            rules: [
              { required: true, message: '请输入重点监管!'},
            ]
          },
          toxic: {
            rules: [
              { required: true, message: '请输入剧毒!'},
            ]
          },
          precursorChemicals: {
            rules: [
              { required: true, message: '请输入易制毒!'},
            ]
          },
          status: {
            rules: [
              { required: true, message: '请输入状态!'},
            ]
          },
          certified: {
            rules: [
              { required: true, message: '请输入是否领证!'},
            ]
          },
          rawMaterials: {
            rules: [
              { required: true, message: '请输入主要原材料!'},
            ]
          },
          proEquipment: {
            rules: [
              { required: true, message: '请输入主要生产设备!'},
            ]
          },
          remake: {
            rules: [
              { required: true, message: '请输入备注!'},
            ]
          },
        },
        url: {
          add: "/companyProductMaterial/add",
          edit: "/companyProductMaterial/edit",
        }
      }
    },
    computed: {
      displayPro:function(){
        return this.model.outputType==='1';
      }
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'outputType','outputName','yield','maxStore','cas','storeType','hazardousChemicalsCategory','mainRisk','supervision','toxic','precursorChemicals','status','certified','rawMaterials','proEquipment','remake'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
         
        })
      },
      handleCancel () {
        this.close()
      },
      handleTypeChange(value){
        this.model.outputType = value;
      }
    }
  }
</script>