<template>
  <a-date-picker
    ref="datePicker"
    :value="innerDateValue"
    allowClear
    :format="dateFormat"
    :showTime="isDatetime"
    dropdownClassName="j-vxe-date-picker"
    style="min-width: 0;"
    v-bind="cellProps"
    @change="handleChange"
  />
</template>

<script>
  import moment from 'moment'
  import { JVXETypes } from '@/components/jeecg/JVxeTable/index'
  import JVxeCellMixins, { dispatchEvent } from '@/components/jeecg/JVxeTable/mixins/JVxeCellMixins'

  export default {
    name: 'JVxeDateCell',
    mixins: [JVxeCellMixins],
    props: {},
    data() {
      return {
        innerDateValue: null,
      }
    },
    computed: {
      isDatetime() {
        return this.$type === JVXETypes.datetime
      },
      dateFormat() {
        let format = this.originColumn.format
        return format ? format : (this.isDatetime ? 'YYYY-MM-DD HH:mm:ss' : 'YYYY-MM-DD')
      },
    },
    watch: {
      innerValue: {
        immediate: true,
        handler(val) {
          if (val == null || val === '') {
            this.innerDateValue = null
          } else {
            this.innerDateValue = moment(val, this.dateFormat)
          }
        }
      }
    },
    methods: {
      handleChange(mom, dateStr) {
        this.handleChangeCommon(dateStr)
      }
    },
    // 【组件增强】注释详见：JVxeCellMixins.js
    enhanced: {
      aopEvents: {
        editActived(event) {
          dispatchEvent.call(this, event, 'ant-calendar-picker', el => el.children[0].dispatchEvent(event.$event))
        },
      },
    }
  }
</script>

<style scoped>

</style>