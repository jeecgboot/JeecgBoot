<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="所属小区" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="communityId">
              <community-list
                ref="selectCommunityName"
                :id="model.communityId"
                :url="this.url.queryList"
                @handleChange="handleChange"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="楼宇名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="buildingName">
              <a-input v-model="model.buildingName" placeholder="请输入楼宇名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="楼宇编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="buildingCode">
              <a-input v-model="model.buildingCode" placeholder="请输入楼宇编号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="建筑面积" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="buildingFloorArea">
              <a-input-number v-model="model.buildingFloorArea" placeholder="请输入建筑面积" style="width: 100%" />
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
import communityList from '@views/property/modules/community/modules/communityList'

export default {
  name: 'SeBuildingForm',
  components: {
    communityList
  },
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  data() {
    return {
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
        buildingName: [
          { required: true, message: '请输入楼宇名称!' }
        ],
        buildingCode: [
          { required: true, message: '请输入楼宇编号!' }
        ],
        communityId: [
          { required: true, message: '请选择所属小区!' }
        ]
      },
      url: {
        add: '/property/seBuilding/add',
        edit: '/property/seBuilding/edit',
        queryById: '/property/seBuilding/queryById',
        queryList: '/property/seCommunity/queryList'
      }
    }
  },
  computed: {
    formDisabled() {
      return this.disabled
    }
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
  },
  methods: {
    add() {
      this.edit(this.modelDefault)
    },
    edit(record) {
      this.model = Object.assign({}, record)
      this.visible = true
    },
    submitForm() {
      const that = this
      // 触发表单验证
      this.$refs.form.validate(valid => {
        if (valid) {
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
          httpAction(httpurl, this.model, method).then((res) => {
            if (res.success) {
              that.$message.success(res.message)
              that.$emit('ok')
            } else {
              that.$message.warning(res.message)
            }
          }).finally(() => {
            that.confirmLoading = false
          })
        }

      })
    },
    handleChange(id) {
      this.model.communityId = id
    }
  }
}
</script>
<style scoped>
</style>