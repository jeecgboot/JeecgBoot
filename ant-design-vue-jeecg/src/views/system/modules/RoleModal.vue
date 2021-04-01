<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
    wrapClassName="ant-modal-cust-warp"
    style="top:5%;height: 85%;overflow-y: hidden">

    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form"  v-bind="layout"  :model="model" :rules="validatorRules">
        <a-form-model-item label="角色编码" required prop="roleCode">
          <a-input v-model="model.roleCode" :disabled="roleDisabled"  placeholder="请输入角色编码"/>
        </a-form-model-item>
        <a-form-model-item label="角色名称" required prop="roleName">
          <a-input v-model="model.roleName" placeholder="请输入角色名称"/>
        </a-form-model-item>
        <a-form-model-item label="描述" prop="description">
          <a-textarea :rows="5" v-model="model.description" placeholder="请输入角色描述"/>
        </a-form-model-item>
      </a-form-model>
    </a-spin>
  </a-modal>
</template>

<script>
  import {addRole,editRole,duplicateCheck } from '@/api/api'
  export default {
    name: "RoleModal",
    data () {
      return {
        title:"操作",
        visible: false,
        roleDisabled: false,
        model: {},
        layout: {
          labelCol: { span: 3 },
          wrapperCol: { span: 14 },
        },
        confirmLoading: false,
        validatorRules:{
          roleName: [
            { required: true, message: '请输入角色名称!' },
            { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
          ],
          roleCode: [
            { required: true, message: '请输入角色名称!'},
            { min: 0, max: 64, message: '长度不超过 64 个字符', trigger: 'blur' },
            { validator: this.validateRoleCode}
          ],
          description: [
            { min: 0, max: 126, message: '长度不超过 126 个字符', trigger: 'blur' }
          ]
        },
      }
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
        //编辑页面禁止修改角色编码
        if(this.model.id){
          this.roleDisabled = true;
        }else{
          this.roleDisabled = false;
        }
      },
      close () {
        this.$refs.form.clearValidate();
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let obj;
            if(!this.model.id){
              obj=addRole(this.model);
            }else{
              obj=editRole(this.model);
            }
            obj.then((res)=>{
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
          }else{
            return false;
          }
        })
      },
      handleCancel () {
        this.close()
      },
      validateRoleCode(rule, value, callback){
        if(/[\u4E00-\u9FA5]/g.test(value)){
          callback("角色编码不可输入汉字!");
        }else{
          let params = {
            tableName: "sys_role",
            fieldName: "role_code",
            fieldVal: value,
            dataId: this.model.id,
          };
          duplicateCheck(params).then((res)=>{
            if(res.success){
              callback();
            }else{
              callback(res.message);
            }
          });
        }
      }
    }
  }
</script>

<style scoped>

</style>