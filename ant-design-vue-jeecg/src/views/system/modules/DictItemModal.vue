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
      <a-form-model  ref="form" :model="model" :rules="validatorRules">

        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          prop="itemText"
          label="名称">
          <a-input placeholder="请输入名称" v-model="model.itemText"/>
        </a-form-model-item>

        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          prop="itemValue"
          label="数据值">
          <a-input placeholder="请输入数据值" v-model="model.itemValue" />
        </a-form-model-item>

        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="描述">
          <a-input v-model="model.description" />
        </a-form-model-item>

        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="排序值">
          <a-input-number :min="1" v-model="model.sortOrder" />
          值越小越靠前
        </a-form-model-item>

        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="是否启用"
          hasFeedback>
          <a-switch checkedChildren="启用" unCheckedChildren="禁用" @change="onChose" v-model="visibleCheck"/>
        </a-form-model-item>

      </a-form-model>
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
        validatorRules: {
          itemText:  [{required: true, message: '请输入名称!'}],
          itemValue:  [{required: true, message: '请输入数据值!'},{validator: this.validateItemValue}],
        },
      }
    },
    created() {
    },
    methods: {
      add(dictId) {
        this.dictId = dictId;
        //初始化默认值
        this.edit({sortOrder:1,status:1});
      },
      edit(record) {
        if (record.id) {
          this.dictId = record.dictId;
        }
        this.status = record.status;
        this.visibleCheck = (record.status == 1) ? true : false;
        this.model = Object.assign({}, record);
        this.model.dictId = this.dictId;
        this.model.status = this.status;
        this.visible = true;
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
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            this.model.itemText = (this.model.itemText || '').trim()
            this.model.itemValue = (this.model.itemValue || '').trim()
            this.model.description = (this.model.description || '').trim()
            this.model.status = this.status;
            let obj;
            if (!this.model.id) {
              obj = addDictItem(this.model);
            } else {
              obj = editDictItem(this.model);
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
          }else{
            return false;
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
        this.$refs.form.resetFields();
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
