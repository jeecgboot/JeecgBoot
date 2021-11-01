<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysOrgCode">
              <j-select-depart v-model="model.sysOrgCode" multi  />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="房屋编号(门牌号)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="houseCode">
              <a-input v-model="model.houseCode" placeholder="请输入房屋编号(门牌号)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="房屋楼层" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="houseLayer">
              <a-input v-model="model.houseLayer" placeholder="请输入房屋楼层"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="房屋户型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="houseDoorModel">
              <a-input v-model="model.houseDoorModel" placeholder="请输入房屋户型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="房屋类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="houseType">
              <j-dict-select-tag type="list" v-model="model.houseType" dictCode="house_type" placeholder="请选择房屋类型" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="建筑面积(平方)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="houseFloorArea">
              <a-input-number v-model="model.houseFloorArea" placeholder="请输入建筑面积(平方)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="室内面积(平方)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="houseIndoorArea">
              <a-input-number v-model="model.houseIndoorArea" placeholder="请输入室内面积(平方)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="算费系数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="calculationCoefficient">
              <a-input-number v-model="model.calculationCoefficient" placeholder="请输入算费系数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="精装修 1表示是 0表示否" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="izFineDecoration">
              <j-dict-select-tag type="list" v-model="model.izFineDecoration" dictCode="fine_decoration" placeholder="请选择精装修 1表示是 0表示否" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="houseRemarks">
              <a-input v-model="model.houseRemarks" placeholder="请输入备注"  ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'SeHouseForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
         },
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
           sysOrgCode: [
              { required: true, message: '请输入所属部门!'},
           ],
           houseCode: [
              { required: true, message: '请输入房屋编号(门牌号)!'},
           ],
           houseLayer: [
              { required: true, message: '请输入房屋楼层!'},
              { pattern: /^-?\d+$/, message: '请输入整数!'},
           ],
           houseDoorModel: [
              { required: true, message: '请输入房屋户型!'},
           ],
           houseType: [
              { required: true, message: '请输入房屋类型!'},
           ],
           houseFloorArea: [
              { required: true, message: '请输入建筑面积(平方)!'},
              { pattern: /^-?\d+\.?\d*$/, message: '请输入数字!'},
           ],
           houseIndoorArea: [
              { required: true, message: '请输入室内面积(平方)!'},
              { pattern: /^-?\d+\.?\d*$/, message: '请输入数字!'},
           ],
           calculationCoefficient: [
              { required: true, message: '请输入算费系数!'},
              { pattern: /^[A-Z|a-z]+$/, message: '请输入字母!'},
           ],
           izFineDecoration: [
              { required: true, message: '请输入精装修 1表示是 0表示否!'},
           ],
           commId: [
              { required: true, message: '请输入小区id!'},
           ],
           buildId: [
              { required: true, message: '请输入楼宇id!'},
           ],
           unitId: [
              { required: true, message: '请输入单元id!'},
           ],
        },
        url: {
          add: "/property/seHouse/add",
          edit: "/property/seHouse/edit",
          queryById: "/property/seHouse/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
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
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
    }
  }
</script>