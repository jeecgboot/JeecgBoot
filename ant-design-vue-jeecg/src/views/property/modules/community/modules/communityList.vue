<template>
  <a-select
    show-search
    :value="value"
    placeholder="请选择所属小区"
    :default-active-first-option="false"
    :show-arrow="false"
    :filter-option="false"
    :not-found-content="null"
    @change="handleChange"
  >
    <a-select-option v-for="d in data" :key="d.value" :value="d.value">
      {{ d.text }}
    </a-select-option>
  </a-select>
</template>

<script>

import { httpAction, getAction } from '@/api/manage'
import store from '@/store/'

export default {
  name: 'communityList',
  props: {
    id: {
      type: String,
      default: null
    },
    url: {
      type: String,
      default: null
    }
  },
  created() {
    this.getCommunityList()
  },
  data() {
    return {
      data: [],
      value: undefined
    }
  },
  methods: {
    getCommunityList() {
      let data = { sysOrgCode: store.getters.userInfo.orgCode }
      getAction(this.url, data).then(res => {
        if (res.code === 200) {
          this.data = res.result
          if (this.id) {
            this.handleChange(this.id)
          }
        }
      })
    },
    handleChange(value) {
      this.data.forEach(i => {
        if (i.value === value) {
          this.value = i.text
        }
      })
      this.$emit('handleChange', value)
    }
  }
}
</script>
