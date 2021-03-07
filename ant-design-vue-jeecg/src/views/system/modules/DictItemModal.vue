<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="名称">
          <a-input placeholder="请输入名称" v-decorator.trim="['itemText', validatorRules.itemText]"/>
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="数据值">
          <a-input placeholder="请输入数据值" v-decorator.trim="['itemValue', validatorRules.itemValue]"/>
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="描述">
          <a-input v-decorator="['description']"/>
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="排序值">
          <a-input-number :min="1" v-decorator="['sortOrder',{'initialValue':1}]"/>
          值越小越靠前，支持小数
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="是否启用"
          hasFeedback>
          <a-switch checkedChildren="启用" unCheckedChildren="禁用" @change="onChose" v-model="visibleCheck"/>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import pick from 'lodash.pick'
  import {addDictItem, editDictItem} from '@/api/api'
  import { getAction } from '@api/manage'

  export default {
    name: "DictItemModal",
    data() {
      return {
        title: "操作",
        visible: false,
        visibleCheck: true,
        model: {},
        dictId: "",
        status: 1,
        labelCol: {
          xs: {span: 24},
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules: {
          itemText: {rules: [{required: true, message: '请输入名称!'}]},
          itemValue: {rules: [{required: true, message: '请输入数据值!'},{validator: this.validateItemValue}]},
        },
      }
    },
    created() {
    },
    methods: {
      add(dictId) {
        this.dictId = dictId;
        this.edit({});
      },
      edit(record) {
        if (record.id) {
          this.dictId = record.dictId;
          this.status = record.status;
          this.visibleCheck = (record.status == 1) ? true : false;
        }
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.model.dictId = this.dictId;
        this.model.status = this.status;
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'itemText', 'itemValue', 'description', 'sortOrder'))
        });
      },
      onChose(checked) {
        if (checked) {
          this.status = 1;
          this.visibleCheck = true;
        } else {
          this.status = 0;
          this.visibleCheck = false;
        }
      },
      // 确定
      handleOk() {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            values.itemText = (values.itemText || '').trim()
            values.itemValue = (values.itemValue || '').trim()
            values.description = (values.description || '').trim()
            let formData = Object.assign(this.model, values);
            formData.status = this.status;
            let obj;
            if (!this.model.id) {
              obj = addDictItem(formData);
            } else {
              obj = editDictItem(formData);
            }
            obj.then((res) => {
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
      // 关闭
      handleCancel() {
        this.close();
      },
      close() {
        this.$emit('close');
        this.visible = false;
      },
      validateItemValue(rule, value, callback){
        let param = {
          itemValue:value,
          dictId:this.dictId,
        }
        if(this.model.id){
          param.id = this.model.id
        }
        if(value){
          let reg=new RegExp("[`_~!@#$^&*()=|{}'.<>《》/?！￥（）—【】‘；：”“。，、？]")
          if(reg.test(value)){
            callback("数据值不能包含特殊字符！")
          }else{
            //update--begin--autor:lvdandan-----date:20201203------for：JT-27【数据字典】字典 - 数据值可重复
            getAction("/sys/dictItem/dictItemCheck",param).then((res)=>{
              if(res.success){
                callback()
              }else{
                callback(res.message);
              }
            });
            //update--end--autor:lvdandan-----date:20201203------for：JT-27【数据字典】字典 - 数据值可重复
          }
        }else{
          callback()
        }
      }
    }
  }
</script>
