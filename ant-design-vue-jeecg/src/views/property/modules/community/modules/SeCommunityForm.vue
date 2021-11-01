<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysOrgCode">
              <j-select-depart v-model="model.sysOrgCode" :multi="true" :customReturnField="orgCode" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="城市" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="city">
              <j-area-linkage type="cascader" v-model="model.city" placeholder="请输入省市区" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="小区面积" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="communityArea">
              <a-input v-model="model.communityArea" placeholder="请输入小区面积"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="社区编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="communityCode">
              <a-input v-model="model.communityCode" placeholder="请输入社区编码"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" style="margin-bottom:30px">
            <a-form-model-item label="小区名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <amap-seach
                :query="model.name"
                :city-code="model.city"
                @getPosition="getPosition" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="小区地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="address">
              <a-input v-model="model.address" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label=" x坐标" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mapX">
              <a-input v-model="model.mapX" placeholder="x坐标" disabled />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label=" y坐标" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mapY">
              <a-input v-model="model.mapY" placeholder="y坐标" disabled />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

import { httpAction, getAction } from '@api/manage'
import { validateDuplicateValue } from '@/utils/util'
import AmapSeach from '@views/property/modules/community/profile/amapSeach'

export default {
  name: 'SeCommunityForm',
  components: {
    AmapSeach
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
      orgCode:'orgCode',
      query: '',
      tips: [],
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
        sysOrgCode: [
          { required: true, message: '请输入所属部门!' }
        ],
        name: [
          { required: true, message: '请输入小区名称!' }
        ],
        address: [
          { required: true, message: '请输入小区地址!' }
        ],
        city: [
          { required: true, message: '请输入城市!' }
        ],
        communityArea: [
          { required: true, message: '请输入小区面积!' },
          { pattern: /^-?\d+\.?\d*$/, message: '请输入数字!' }
        ],
        communityCode: [
          { required: true, message: '请输入社区编码!' }
        ]
      },
      url: {
        add: '/property/seCommunity/add',
        edit: '/property/seCommunity/edit',
        queryById: '/property/seCommunity/queryById'
      },
      poi: {
        location: {}
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
  watch: {
    poi: {
      handler(n, o) {
        if (o !== n) {
          this.$set(this.model, 'mapY', n.location.lat)
          this.$set(this.model, 'mapX', n.location.lng)
          this.$set(this.model, 'address', n.address)
          this.$set(this.model, 'name', n.name)
        }
      },
      immediate: true
    }
  },
  methods: {
    amapSearchBox() {
      console.log(this.model.address)
    },
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
    getPosition(poi) {
      this.poi = poi
    }
  }
}
</script>