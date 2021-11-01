<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="单元编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="unitCode">
              <a-input v-model="model.unitCode" placeholder="请输入单元编号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="总层数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="unitLayer">
              <a-input v-model="model.unitLayer" placeholder="请输入总层数"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否有电梯 " :labelCol="labelCol" :wrapperCol="wrapperCol" prop="izElevator">
              <j-dict-select-tag type="list" v-model="model.izElevator" dictCode="elevator" placeholder="请选择是否有电梯 " />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="所属小区" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="communityId">
              <a-select
                v-model="model.communityId"
                :default-value="succeedCommunity"
                placeholder="请选择所属小区"
                @change="handleCommunityChange">
                <a-select-option v-for="community in communityData" :key="community.value">
                  {{ community.text }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="所属楼宇" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="buildingId">
              <a-select v-model="model.buildingId" placeholder="请选择所属楼宇">
                <a-select-option v-for="building in buildingData" :key="building.value">
                  {{ building.text }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-input v-model="model.remark" placeholder="请输入备注"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>
<script>

import store from '@/store/'
import { httpAction, getAction } from '@/api/manage'
import { validateDuplicateValue } from '@/utils/util'

export default {
  name: 'SeUnitForm',
  components: {},
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
        unitCode: [
          { required: true, message: '请输入单元编号!' }
        ],
        unitLayer: [
          { required: true, message: '请输入总层数!' },
          { pattern: /^-?\d+\.?\d*$/, message: '请输入数字!' }
        ],
        izElevator: [
          { required: true, message: '请输入是否有电梯 !' }
        ],
        buildingId: [
          { required: true, message: '请输入所属楼宇!' }
        ],
        communityId: [
          { required: true, message: '请输入所属小区!' }
        ]
      },
      url: {
        add: '/property/seUnit/add',
        edit: '/property/seUnit/edit',
        queryById: '/property/seUnit/queryById',
        queryList: '/property/seCommunity/queryList',
        queryBuildList: '/property/seBuilding/queryBuildList'
      },
      sysOrgCode: store.getters.userInfo.orgCode,
      communityData: null,
      buildingData: null,
      succeedBuild: null,
      succeedCommunity: null
    }
  },
  computed: {
    formDisabled() {
      return this.disabled
    }
  },
  watch: {},
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
    this.getCommunitData()
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
    getCommunitData() {
      let that = this
      getAction(this.url.queryList, { sysOrgCode: that.sysOrgCode }).then(res => {
        if (res.result) {
          that.communityData = res.result
          that.getBuildingData(res.result[0].value)
        }
      })
    },
    getBuildingData(id) {
      let that = this
      getAction(this.url.queryBuildList, { sysOrgCode: that.sysOrgCode, communityId: id }).then(res => {
        if (res.result) {
          if (res.result.length === 0) {

          }
          that.buildingData = res.result
        }
      })
    },
    handleCommunityChange(value) {
      this.getBuildingData(value)
    }
  }
}
</script>