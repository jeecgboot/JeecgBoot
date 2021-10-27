<template>
  <a-time-picker
    ref="timePicker"
    :value="innerDateValue"
    allowClear
    dropdownClassName="j-vxe-date-picker"
    style="min-width: 0;"
    v-bind="cellProps"
    @change="handleChange"
  />
</template>

<script>
  import moment from 'moment'
  import JVxeCellMixins, { dispatchEvent } from '@/components/jeecg/JVxeTable/mixins/JVxeCellMixins'

  export default {
    name: 'JVxeTimeCell',
    mixins: [JVxeCellMixins],
    props: {},
    data() {
      return {
        innerDateValue: null,
        dateFormat: 'HH:mm:ss'
      }
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