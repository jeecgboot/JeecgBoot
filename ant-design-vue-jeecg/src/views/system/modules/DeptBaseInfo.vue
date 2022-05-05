<template>
  <a-card :visible="visible">
    <a-form-model ref="form" :model="model">
      <a-form-model-item
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        label="机构名称">
        <a-input style="border:0;" placeholder="" v-model="model.departName"/>
      </a-form-model-item>
      <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="上级部门">
        <a-tree-select
          disabled
          style="width:100%;border: 0;border: none;outline:none;"
          :dropdownStyle="{maxHeight:'200px',overflow:'auto'}"
          :treeData="treeData"
          v-model="model.parentId"
          placeholder="无">
        </a-tree-select>
      </a-form-model-item>
      <a-form-model-item
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        label="机构编码">
        <a-input style="border:0;" placeholder="" v-model="model.orgCode"/>
      </a-form-model-item>
      <a-form-model-item
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        label="机构类型">
          <a-radio-group :disabled="true" v-model="model.orgCategory" read-only>
            <a-radio value="1">
              公司
            </a-radio>
            <a-radio value="2">
              部门
            </a-radio>
            <a-radio value="3">
              岗位
            </a-radio>
          </a-radio-group>
      </a-form-model-item>
      <a-form-model-item
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        label="排序">
        <a-input-number style="border:0;" v-model="model.departOrder"/>
      </a-form-model-item>
      <a-form-model-item
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        label="手机号">
        <a-input style="border:0;" placeholder="" v-model="model.mobile"/>
      </a-form-model-item>
      <a-form-model-item
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        label="地址">
        <a-input style="border:0;" placeholder="" v-model="model.address"/>
      </a-form-model-item>
      <a-form-model-item
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        label="备注">
        <a-textarea style="border:0;" placeholder="" v-model="model.memo"/>
      </a-form-model-item>
    </a-form-model>
  </a-card>
</template>
<script>
  import { queryIdTree } from '@/api/api'

  export default {
    name: 'DeptBaseInfo',
    components: {},
    data() {
      return {
        departTree: [],
        id: '',
        model: {},
        visible: false,
        disable: true,
        treeData: [],
        labelCol: {
          xs: {span: 24},
          sm: {span: 3}
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16}
        },
      }
    },
    created() {
      this.loadTreeData();
    },
    methods: {
      loadTreeData() {
        queryIdTree().then((res) => {
          if (res.success) {
            for (let i = 0; i < res.result.length; i++) {
              let temp = res.result[i];
              this.treeData.push(temp);
            }
          }

        })
      },
      open(record) {
        this.visible = true;
        this.$nextTick(() => {
          this.$refs.form.resetFields()
          this.model = Object.assign({}, record)
        })
      },
      clearForm() {
        this.$refs.form.resetFields();
        this.treeData = [];
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>